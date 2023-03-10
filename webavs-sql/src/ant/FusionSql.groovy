package src.ant

import java.nio.charset.Charset

import static groovy.io.FileType.*

// D:/workspaces/avs/1.14.00/sql
class FusionSql {

    def version = null
    def sqlFilePath = null
    def targetDir = null
    def distributionDir

    def mapTableReorg = [:]

    public FusionSql(currentVersion, sourceSqlDir, targetSqlDir) {
        super()
//        System.setProperty("file.encoding","Cp1252")
//        System.setProperty("sun.jnu.encoding","Cp1252")
//        System.setProperty("user.timezone","Europe/Zurich")
//        System.setProperty("user.country","CH")

        println '     Groovy args : '
        println '     -> currentVersion' + currentVersion
        println '     -> sourceSqlDir=' + sourceSqlDir
        println '     -> targetSqlDir=' + targetSqlDir
        println '     Encoding :'
        println '     -> file.encoding: ' + System.getProperty("file.encoding")
        println '     -> Default Locale: ' + Locale.getDefault()
        println '     -> Default Charset: ' + System.getProperty("sun.jnu.encoding")
        println '     -> Default Encoding: ' + Charset.defaultCharset().name()

        version = currentVersion
        sqlFilePath = sourceSqlDir
        distributionDir = targetSqlDir
    }

    public FusionSql() {
        super()
    }

    public make() {
        
        File scriptDmlFile = initFile(distributionDir + "version.sql")
        
        // After script concatenation we add the WebAVS version SQL instruction
       	def sqlVersion = 'insert into SCHEMA.JADEDBVE (verlab, applab, reldat) values(\'@@@\', \'WEBAVS\', yyyymmdd);'
        sqlVersion = sqlVersion.replaceAll("@@@", version)
        sqlVersion = sqlVersion.replaceAll("yyyymmdd", String.valueOf(new Date().format( 'yyyyMMdd' )))

        printHeader(scriptDmlFile, "WebAVS Version")

        scriptDmlFile << sqlVersion

    }

    private File printHeader(File file, String text) {
        file << "---------------------------------------------------------------" + "\n"
        file << "-----   " + text + "\n"
        file << "---------------------------------------------------------------" + "\n"
        return file
    }

    private File initFile(String path) {

        File file = new File(path)
        file.delete()

        if (!file.createNewFile()) {
            println "Erreur create " + path
        }

        return file
    }

    private String generateReorg(String instruction, Map mapTable) {
        def table = ""

        instruction.tokenize(' ;\n').each() {
            if (it.trim().startsWith("SCHEMA.")) {
                table = it.trim().split('\\.')[1]
            }
        }

        if (!mapTable.containsKey(table)) {
            mapTable[table] = instruction
            return "REORG TABLE SCHEMA." + table + " ALLOW READ ACCESS;" + "\n"
        }
        return ""
    }

    static void main(args) {
//                def f = new FusionSql("1.14.02", 'D:/workspaces/avs/1.14.00/sql')
        def f = new FusionSql(args[0], args[1], args[2])
        f.make();
    }

}