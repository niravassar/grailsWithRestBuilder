package com.nanlabs.grails.plugin.logicaldelete;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.grails.compiler.injection.GrailsASTUtils;

import java.lang.reflect.Modifier;

import static com.nanlabs.grails.plugin.logicaldelete.GLogicalDeleteASTTransformation.addConstraintsForDeletedProperty;
import static com.nanlabs.grails.plugin.logicaldelete.GLogicalDeleteASTTransformation.addDeletedPropertyToAllUniqueConstraints;
import static com.nanlabs.grails.plugin.logicaldelete.GLogicalDeleteASTTransformation.addHasBeenDeletedMethod;
import static com.nanlabs.grails.plugin.logicaldelete.GLogicalDeleteASTTransformation.overrideDeleteMethod;


@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class LogicalDeleteASTTRansformation implements ASTTransformation {

    public final static String DELETED_FIELD_NAME = "deleted";
    public final static int CLASS_NODE_ORDER = 1;

    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        if (!validate(nodes)) return;
        ClassNode classNode = (ClassNode) nodes[CLASS_NODE_ORDER];
        addDeletedProperty(classNode);
        addHasBeenDeletedMethod(classNode);
        addDeletedPropertyToAllUniqueConstraints(classNode, source);
        addConstraintsForDeletedProperty(classNode, source);
        overrideDeleteMethod(classNode);
        implementDeletedDomainClassInterface(classNode);
    }

    private boolean validate(ASTNode[] nodes) {
        return nodes != null && nodes[0] != null && nodes[1] != null;
    }

    private void addDeletedProperty(ClassNode node) {
        if (!GrailsASTUtils.hasOrInheritsProperty(node, DELETED_FIELD_NAME)) {
            // TODO: At some point the original author may fix the PostgreSQL compatibility while still using Boolean instead of Byte: https://github.com/nanlabs/logical-delete/issues/5
            node.addProperty(DELETED_FIELD_NAME, Modifier.PUBLIC, new ClassNode(String.class), new ConstantExpression(GLogicalDeleteASTTransformation.NOT_DELETED_COLUMN_VALUE), null, null);
        }
    }

    private void implementDeletedDomainClassInterface(ClassNode node) {
        ClassNode iNode = new ClassNode(LogicalDeleteDomainClass.class);
        if (!iNode.implementsInterface(iNode)) {
            node.addInterface(iNode);
        }
    }
}
