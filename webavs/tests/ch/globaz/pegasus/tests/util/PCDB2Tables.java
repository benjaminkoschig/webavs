package ch.globaz.pegasus.tests.util;

import java.util.HashMap;
import java.util.Map;

public class PCDB2Tables {

    public static Map<String, PCDB2Table> pcTables = new HashMap<String, PCDB2Table>();

    static {
        PCDB2Tables.pcTables.put("PCADEPR", new PCDB2Table("PCADEPR", true));
        PCDB2Tables.pcTables.put("PCAFORMO", new PCDB2Table("PCAFORMO", true));
        PCDB2Tables.pcTables.put("PCALFAM", new PCDB2Table("PCALFAM", true));
        PCDB2Tables.pcTables.put("PCANOEL", new PCDB2Table("PCALNOEL", true));
        PCDB2Tables.pcTables.put("PCALOCIM", new PCDB2Table("PCALOCIM", true));
        PCDB2Tables.pcTables.put("PCANNDEC", new PCDB2Table("PCANNDEC", true));
        PCDB2Tables.pcTables.put("PCASREVI", new PCDB2Table("PCASREVI", true));
        PCDB2Tables.pcTables.put("PCASVIE", new PCDB2Table("PCASVIE", true));
        PCDB2Tables.pcTables.put("PCAUREV", new PCDB2Table("PCAUREV", true));
        PCDB2Tables.pcTables.put("PCAUTAPI", new PCDB2Table("PCAUTAPI", true));
        PCDB2Tables.pcTables.put("PCAUTREN", new PCDB2Table("PCAUTREN", true));
        PCDB2Tables.pcTables.put("PCBETAIL", new PCDB2Table("PCBETAIL", true));
        PCDB2Tables.pcTables.put("PCBINHA", new PCDB2Table("PCBINHA", true));
        PCDB2Tables.pcTables.put("PCBISHP", new PCDB2Table("PCBISHP", true));
        PCDB2Tables.pcTables.put("PCBISPH", new PCDB2Table("PCBISPH", true));
        PCDB2Tables.pcTables.put("PCCALPP", new PCDB2Table("PCCALPP", true));
        PCDB2Tables.pcTables.put("PCCBCCP", new PCDB2Table("PCCBCCP", true));
        PCDB2Tables.pcTables.put("PCCENVI", new PCDB2Table("PCCENVI", true));
        PCDB2Tables.pcTables.put("PCCOMOCC", new PCDB2Table("PCCOMOCC", true));
        PCDB2Tables.pcTables.put("PCCONREV", new PCDB2Table("PCCONREV", true));
        PCDB2Tables.pcTables.put("PCCOPDEC", new PCDB2Table("PCCOPDEC", true));
        PCDB2Tables.pcTables.put("PCCPSAL", new PCDB2Table("PCCPSAL", true));
        PCDB2Tables.pcTables.put("PCCREACC", new PCDB2Table("PCCREACC", true));
        PCDB2Tables.pcTables.put("PCCREANC", new PCDB2Table("PCCREANC", true));
        PCDB2Tables.pcTables.put("PCDECACA", new PCDB2Table("PCDECACA", true));
        PCDB2Tables.pcTables.put("PCDECHEA", new PCDB2Table("PCDECHEA", true));
        PCDB2Tables.pcTables.put("PCDECREF", new PCDB2Table("PCDECREF", true));
        PCDB2Tables.pcTables.put("PCDECSUP", new PCDB2Table("PCDECSUP", true));
        PCDB2Tables.pcTables.put("PCDEMPC", new PCDB2Table("PCDEMPC", true));
        PCDB2Tables.pcTables.put("PCDESFOR", new PCDB2Table("PCDESFOR", true));
        PCDB2Tables.pcTables.put("PCDESREV", new PCDB2Table("PCDESREV", true));
        PCDB2Tables.pcTables.put("PCDOFINH", new PCDB2Table("PCDOFINH", true));
        PCDB2Tables.pcTables.put("PCDONPERS", new PCDB2Table("PCDONPERS", true));
        PCDB2Tables.pcTables.put("PCDOSSI", new PCDB2Table("PCDOSSI", true));
        PCDB2Tables.pcTables.put("PCDRMBRFA", new PCDB2Table("PCDRMBRFA", true));
        PCDB2Tables.pcTables.put("PCDROIT", new PCDB2Table("PCDROIT", true));
        PCDB2Tables.pcTables.put("PCFOPRAM", new PCDB2Table("PCFOPRAM", true));
        PCDB2Tables.pcTables.put("PCHOME", new PCDB2Table("PCHOME", true));
        PCDB2Tables.pcTables.put("PCIJAPG", new PCDB2Table("PCIJAPG", true));
        PCDB2Tables.pcTables.put("PCINJOAI", new PCDB2Table("PCINJOAI", true));
        PCDB2Tables.pcTables.put("PCJOUAPP", new PCDB2Table("PCJOUAPP", true));
        PCDB2Tables.pcTables.put("PCLIZOLO", new PCDB2Table("PCLIZOLO", true));
        PCDB2Tables.pcTables.put("PCLOYER", new PCDB2Table("PCLOYER", true));
        PCDB2Tables.pcTables.put("PCMARSTO", new PCDB2Table("PCMARSTO", true));
        PCDB2Tables.pcTables.put("PCMONETR", new PCDB2Table("PCMONETR", true));
        PCDB2Tables.pcTables.put("PCNUMER", new PCDB2Table("PCNUMER", true));
        PCDB2Tables.pcTables.put("PCORDVER", new PCDB2Table("PCORDVER", true));
        PCDB2Tables.pcTables.put("PCPCACC", new PCDB2Table("PCPCACC", true));
        PCDB2Tables.pcTables.put("PCPEALI", new PCDB2Table("PCPEALI", true));
        PCDB2Tables.pcTables.put("PCPERSET", new PCDB2Table("PCADEPR", true));
        PCDB2Tables.pcTables.put("PCPLCAL", new PCDB2Table("PCPERSET", true));
        PCDB2Tables.pcTables.put("PCPRCHAM", new PCDB2Table("PCPRCHAM", true));
        PCDB2Tables.pcTables.put("PCPRENTI", new PCDB2Table("PCPRENTI", true));
        PCDB2Tables.pcTables.put("PCPRESTA", new PCDB2Table("PCPRESTA", true));
        PCDB2Tables.pcTables.put("PCRALDE", new PCDB2Table("PCRALDE", true));
        PCDB2Tables.pcTables.put("PCRALIN", new PCDB2Table("PCRALIN", true));
        PCDB2Tables.pcTables.put("PCRAVSAI", new PCDB2Table("PCRAVSAI", true));
        PCDB2Tables.pcTables.put("PCREHYP", new PCDB2Table("PCREHYP", true));
        PCDB2Tables.pcTables.put("PCTAJOHO", new PCDB2Table("PCTAJOHO", true));
        PCDB2Tables.pcTables.put("PCTCEVI", new PCDB2Table("PCTCEVI", true));
        PCDB2Tables.pcTables.put("PCTFORE", new PCDB2Table("PCTFORE", true));
        PCDB2Tables.pcTables.put("PCTITRE", new PCDB2Table("PCTITRE", true));
        PCDB2Tables.pcTables.put("PCTYCHAM", new PCDB2Table("PCTYCHAM", true));
        PCDB2Tables.pcTables.put("PCVALDEC", new PCDB2Table("PCVALDEC", true));
        PCDB2Tables.pcTables.put("PCVARMET", new PCDB2Table("PCVARMET", true));
        PCDB2Tables.pcTables.put("PCVEHICU", new PCDB2Table("PCVEHICU", true));
        PCDB2Tables.pcTables.put("PCVERDRO", new PCDB2Table("PCVERDRO", true));
        PCDB2Tables.pcTables.put("PCZONFOR", new PCDB2Table("PCZONFOR", true));
    }

    public static void addTable(PCDB2Table newTable) {
        PCDB2Tables.pcTables.put(newTable.getTableName(), newTable);
    }
}
