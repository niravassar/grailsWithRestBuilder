package com.nanlabs.grails.plugin.logicaldelete

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.VariableScope
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ListExpression
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression
import org.codehaus.groovy.ast.expr.TupleExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.messages.LocatedMessage
import org.codehaus.groovy.syntax.Token

import static com.nanlabs.grails.plugin.logicaldelete.LogicalDeleteASTTRansformation.DELETED_FIELD_NAME
import static groovyjarjarasm.asm.Opcodes.ACC_PUBLIC
import static groovyjarjarasm.asm.Opcodes.ACC_STATIC

class GLogicalDeleteASTTransformation {
    public static final String NOT_DELETED_COLUMN_VALUE = '0'

    private static final String CONSTRAINTS_FIELD = 'constraints'
    private static final String UNIQUE_CONSTRAINT_KEY = 'unique'

    /**
     * Adds the 'deleted' column to all unique constraints found in the specified Grails domain class, {@code classNode}.
     * Thus, all unique constraints will become composite constraints after this call, if not already a composite.<br/>
     * <br/>
     * This unique constraint modification is necessary in order to allow logically deleted entities to be re-added
     * without violating its unique constraints. In the example below, consider the case when an entity is logically deleted,
     * then a new record is added with 'someProperty' containing the value of the logically deleted record.
     * If the specified class does not have a static 'constraints' field,
     * or if there are no unique constraint definitions, no modifications will be made.<br/>
     * <br/>
     * Handles three types of Grails unique constraint definitions:
     * <ul>
     *     <li>someProperty(unique: true)</li>
     *     <li>someProperty(unique: 'prop2')</li>
     *     <li>someProperty(unique: ['prop2', 'prop3'])</li>
     * </ul>
     *
     * @param classNode class to modify
     * @param sourceUnit to handle error messages
     */
    static void addDeletedPropertyToAllUniqueConstraints(ClassNode classNode, SourceUnit sourceUnit) {
        FieldNode constraints = classNode.getField(CONSTRAINTS_FIELD)
        if (!constraints) {
            return
        }

        ClosureExpression closure = (ClosureExpression) constraints.initialExpression
        BlockStatement block = (BlockStatement) closure.code

        // Iterate through each statement in the closure (i.e., one property's constraint definitions at a time).
        for (stmt in block.statements) {
            List<MapEntryExpression> mapEntries = getNamedArgumentEntriesFromMethodCallArgument(stmt)

            if (!mapEntries) {
                // This Grails property did not have a (map) named argument expression (i.e., no constraints specified).
                continue
            }

            // Normally there should only be one 'unique' constraint specified per statement, but who knows.
            MapEntryExpression uniqueConstraintMapEntry = findMapEntryWithStringKey(mapEntries, UNIQUE_CONSTRAINT_KEY)

            if (!uniqueConstraintMapEntry) {
                // No unique constraint defined for this domain class property (e.g., no "unique: true", etc.)
                continue
            }

            Expression mapEntryValue = uniqueConstraintMapEntry.valueExpression

            switch (mapEntryValue) {
                case ConstantExpression:
                    switch (((ConstantExpression) mapEntryValue).value) {
                        case Boolean.TRUE:
                            // Case example: fooProp(unique: true)
                            uniqueConstraintMapEntry.valueExpression = new ConstantExpression(DELETED_FIELD_NAME)
                            break

                        case String:
                            // Case example: fooProp(unique: 'prop2')

                            // Add 'deleted' to front of this list so that in the database's unique constraint it will appear near the end (see comment on "Ordering of composite unique constraints").
                            uniqueConstraintMapEntry.valueExpression = new ListExpression([new ConstantExpression(DELETED_FIELD_NAME), mapEntryValue])

                            // Ordering of composite unique constraints:
                            //
                            // Consider the following Grails constraint definition: fooProp(unique: ['prop2', 'prop3'])
                            // The resulting database constraint will be: UNIQUE (prop3, prop2, fooProp)
                            //
                            // First note that Grails/Hibernate seems to reverse the order of the properties when it creates the schema in the database.
                            // Secondly, for performance reasons, we try to ensure the 'deleted' property appears near the end of the
                            // database's UNIQUE constraint list. If 'deleted' were to appear first, it will also be used first in
                            // the INDEX for that unique constraint. However, since most values for 'deleted' will be '0' (the initial value
                            // meaning "not deleted"), for performance reasons it's best to use other properties in the composite index
                            // to reduce the result set sooner.

                            break
                    }
                    break

                case ListExpression:
                    // Case example: fooProp(unique: ['prop2', 'prop3']
                    List<Expression> expressions = ((ListExpression) mapEntryValue).expressions

                    // Add to front of list so that in the database's unique constraint it will appear near the end (see comment on "Ordering of composite unique constraints").
                    expressions.add(0, new ConstantExpression(DELETED_FIELD_NAME))
                    break

                default:
                    Token token = Token.newString(classNode.text, classNode.lineNumber, classNode.columnNumber)
                    LocatedMessage message = new LocatedMessage("Unknown type for unique constraint's map entry value: $mapEntryValue", token, sourceUnit)
                    sourceUnit.errorCollector.addError(message)
                    break
            }
        }
    }

    /**
     * Adds various Grails domain class constraints for the 'deleted' property, such as limiting its String
     * size to 40 chars.
     * (TODO: Move to util class? Another copy in UUIDs plugin?)
     *
     * @param classNode class to modify
     * @param sourceUnit to handle error messages
     */
    @SuppressWarnings('UnusedMethodParameter')
    static void addConstraintsForDeletedProperty(ClassNode classNode, SourceUnit sourceUnit) {
        addEmptyConstraintsFieldIfNeeded(classNode)

        FieldNode constraints = classNode.getField(CONSTRAINTS_FIELD)
        ClosureExpression closure = (ClosureExpression) constraints.initialExpression
        BlockStatement block = (BlockStatement) closure.code

        List<ASTNode> deletedBlock = new AstBuilder().buildFromCode {
            // Large enough to store 36-char UUIDs such as: 9d048347-129a-47ed-b0f6-eec6708368f9
            deleted maxSize: 40
        }

        ReturnStatement deletedMethodCall = (ReturnStatement) (((BlockStatement) deletedBlock[0]).statements[0])
        block.addStatement new ExpressionStatement(deletedMethodCall.expression)
    }

    /**
     * Add a method to check if a domain class has been deleted
     * @param classNode class to modify
     */
    static void addHasBeenDeletedMethod(ClassNode classNode) {
        List<ASTNode> methodBody = new AstBuilder().buildFromCode {
            return deleted != '0'
        }

        MethodNode methodNode = new MethodNode('hasBeenDeleted', ACC_PUBLIC, ClassHelper.boolean_TYPE, [] as Parameter[], [] as ClassNode[], (Statement) methodBody[0])
        classNode.addMethod methodNode
    }

    /**
     * <p> Overrides the specified {@code classNode}'s delete method in order to logically delete the entity rather than
     * physically delete it.
     *
     * <p> With these changes domain classes can register callbacks by creating an "onDelete()" method (similar to Grails's "beforeDelete()").
     * Such callbacks can be useful for (but not limited to) manually cascading deletes to other entities,
     * rather than relying on GORM's hasMany/belongsTo.
     *
     * @param classNode ClassNode of a Grails domain class
     */
    // TODO: Add the @grails.persistence.PersistenceMethod annotation which Grails normally does?
    // TODO: Consider changing this to a global AST transform so that LogicalDeleteDomainClassEnhancer.enhanceWithOnDeleteSupport() will no longer be needed.
    static void overrideDeleteMethod(ClassNode classNode) {
        overrideDeleteMethodMapArgs(classNode)
        overrideDeleteMethodNoArgs(classNode)
    }

    private static void overrideDeleteMethodMapArgs(ClassNode classNode) {
        // Delete any existing methods matching our signature.
        deleteMatchingMethods(classNode, 'delete', 1)

        List<ASTNode> methodBody = new AstBuilder().buildFromCode {
            if (this.respondsTo('onDelete')) {
                onDelete()
            }

            if (com.nanlabs.grails.plugin.logicaldelete.LogicalDeleteDomainClass.isAssignableFrom(this.getClass())) {
                // Logically delete.
                deleted = id as String
                save(args)
            } else {
                // Delegate to GORM's real delete method.
                currentGormInstanceApi().delete(this)
            }

            return
        }

        MethodNode methodNode = new MethodNode(
                'delete',
                ACC_PUBLIC,
                ClassHelper.VOID_TYPE,
                [new Parameter(new ClassNode(Map), 'args')] as Parameter[],
                [] as ClassNode[],
                (Statement) methodBody[0])

        classNode.addMethod methodNode
    }

    private static void overrideDeleteMethodNoArgs(ClassNode classNode) {
        // Delete any existing methods matching our signature.
        deleteMatchingMethods(classNode, 'delete', 0)

        List<ASTNode> methodBody = new AstBuilder().buildFromCode {
            delete(null)
            return
        }

        MethodNode methodNode = new MethodNode(
                'delete',
                ACC_PUBLIC, ClassHelper.VOID_TYPE,
                [] as Parameter[],
                [] as ClassNode[],
                (Statement) methodBody[0])

        classNode.addMethod methodNode
    }

    // TODO: Consider using Hibernate's PreDeleteEventListener or Grails's AbstractPersistenceEventListener to allow the client to use their own beforeDelete triggers. See: http://grails.org/doc/latest/guide/GORM.html
    @SuppressWarnings('UnusedPrivateMethod')
    private static ensureNoExistingBeforeDeleteTriggers(ClassNode node, SourceUnit sourceUnit) {
        if (node.methods.any { it.name == 'beforeDelete' }) {
            Token token = Token.newString(node.getText(), node.getLineNumber(), node.getColumnNumber())
            LocatedMessage message = new LocatedMessage("Logical Delete plugin: Your class '$node.name' cannot have its own beforeDelete() method defined", token, sourceUnit)
            sourceUnit.getErrorCollector().addError(message)
        }
    }

    /**
     * Returns the map entries from a {@code Statement}'s named argument list, if found. If the specified {@code Statement} is
     * not a method call expression, or if a named argument list is not found in the method call's argument list, then
     * an empty {@code List} is returned.<br/>
     * <br/>
     * For example, the statement fooMethod(entry1: val1, entry2: val2) would return two entries, whereas barMethod()
     * would return an empty list.
     *
     * @param stmt the Statement to inspect
     * @return all named argument map entries, or an empty List if none found
     */
    private static List<MapEntryExpression> getNamedArgumentEntriesFromMethodCallArgument(Statement stmt) {
        if (!(stmt instanceof ExpressionStatement)) {
            return []
        }

        if (!(stmt.expression instanceof MethodCallExpression)) {
            return []
        }

        MethodCallExpression methodCall = (MethodCallExpression) stmt.expression

        if (!(methodCall.arguments instanceof TupleExpression)) {
            return []
        }

        TupleExpression methodCallArgs = (TupleExpression) methodCall.arguments
        List<MapEntryExpression> result = []

        for (methodArg in methodCallArgs) {
            if (!(methodArg instanceof NamedArgumentListExpression)) {
                continue
            }

            result.addAll(methodArg.mapEntryExpressions)
        }

        return result
    }

    /**
     * Looks through {@code mapEntries} for the entry which has a key matching the value {@code keyName}.
     * For example, if the specified mapEntries contain keys of other types (Integer, etc) or if their String-based key's
     * are not equal to {@code keyName}, they will not be returned in the result.
     *
     * @param mapEntries the map entries to look through
     * @param keyName value of the map entry's key in order to successfully match
     * @return the matching map entry, or {@code null} if no match found
     */
    private
    static MapEntryExpression findMapEntryWithStringKey(List<MapEntryExpression> mapEntries, String keyName) {
        for (mapEntry in mapEntries) {
            Expression mapKey = mapEntry.keyExpression

            if (!(mapKey instanceof ConstantExpression)) {
                continue
            }

            if (!(mapKey.value instanceof String)) {
                continue
            }

            if (mapKey.value == keyName) {
                return mapEntry
            }
        }

        return null
    }

    /**
     * Adds a static field named "constraints" to the specified class, and initializes it to an empty closure. Equivalent code: <br/>
     * <pre>
     *     static constraints = {}</pre>
     * (TODO: Candidate for the Accelrys Commons plugin?)
     *
     * @param classNode class to enhance
     */
    private static addEmptyConstraintsFieldIfNeeded(ClassNode classNode) {
        if (classNode.getField(CONSTRAINTS_FIELD)) {
            return
        }

        classNode.addProperty(CONSTRAINTS_FIELD, ACC_PUBLIC | ACC_STATIC, new ClassNode(Object), emptyClosureExpression(), null, null)
    }

    /**
     * Creates an empty {@link Closure} expression which can be used assign to a variable, passed to a method, etc.
     * The equivalent Groovy code would look like:<pre>
     *{}</pre> (TODO: Candidate for Accelrys Commons plugin?)
     *
     * @return empty closure expression
     */
    private static ClosureExpression emptyClosureExpression() {
        Parameter[] closureParams = [new Parameter(new ClassNode(Object), "it")]
        ClosureExpression closureExpression = new ClosureExpression(closureParams, new BlockStatement())

        // TODO: Is this variable scope correct? Is there a parent scope we should use instead of a new scope? Research this.
        closureExpression.variableScope = new VariableScope()
        return closureExpression
    }

    /**
     * Deletes zero or more methods in the specified class which match name {@code methodName} and accept {@code argCount} number of arguments.
     * For example, if two methods of signatures "Object foo(String)" and "Date foo(int)" exist, and methodName=foo, argCount=1,
     * they will both be deleted. (TODO: Promote this to utility class?)
     *
     * @param classNode class to modify
     * @param methodName method name to search for and delete
     * @param argCount number of arguments that methodName must accept for a successful match
     */
    static deleteMatchingMethods(ClassNode classNode, String methodName, int argCount) {
        // Is this the right way to delete methods? Seems to work but maybe Groovy has a util class that does this?
        // See: http://stackoverflow.com/questions/23485170/how-to-delete-existing-class-methods-from-a-groovy-ast-transform

        // There seem to be two locations we must delete existing methods from: classNode.getMethods() and classNode.getDeclaredMethods(String).

        classNode.methods.removeAll {
            it.name == methodName && it.parameters?.size() == argCount
        }

        classNode.getDeclaredMethods(methodName).removeAll {
            it.name == methodName && it.parameters?.size() == argCount
        }
    }
}
