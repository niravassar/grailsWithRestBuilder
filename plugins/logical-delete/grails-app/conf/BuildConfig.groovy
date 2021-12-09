Closure versionForPlugin = { sharedVersionForPlugin(it) }

Map sharedConfigConstructorArgMap = [
        appSpecificDependencyResolution: {
            plugins {
                compile(':hibernate4:' + versionForPlugin('hibernate4')) { export = false }
            }
        }
]

// -----------------------------------------------------------------------------------------------------------------
// TODO: Remove backwards compatibility once all developers have migrated
// to new Perforce mapping: https://bio-rdjira.dsone.3ds.com/browse/DAT-8072
def oldDir = '../../..'
def newDir = '../../shared/build/config'
File loaderFile = new File(System.getProperty('load-shared-config', "$newDir/LoadSharedBuildConfig.groovy"))
if (!loaderFile.exists()) {
    def oldFile = new File("$oldDir/LoadSharedBuildConfig.groovy")
    loaderFile = oldFile
}
Eval.xyz(this, sharedConfigConstructorArgMap, versionForPlugin, loaderFile.text)
// -----------------------------------------------------------------------------------------------------------------
