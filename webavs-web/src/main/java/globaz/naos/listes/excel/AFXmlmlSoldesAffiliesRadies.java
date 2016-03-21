package globaz.naos.listes.excel;

import globaz.webavs.common.CommonExcelmlContainer;

public class AFXmlmlSoldesAffiliesRadies {

    public static final String DATA_EXCEL_DATE_A_PARTIR = "DATE_A_PARTIR";
    public static final String DATA_EXCEL_DATE_DOC = "DATE_DOC";
    private static final String DATA_EXCEL_DATE_RADIATION = "DATE_RADIATION";
    private static final String DATA_EXCEL_DATE_SECTION = "DATE_SECTION";
    private static final String DATA_EXCEL_DESCRIPTION_SECTION = "DESCRIPTION_SECTION";
    private static final String DATA_EXCEL_NOM = "NOM";
    private static final String DATA_EXCEL_NUM_AFFILIE = "NUM_AFFILIE";
    private static final String DATA_EXCEL_NUM_INFOROM = "NUMERO_INFOROM";
    private static final String DATA_EXCEL_NUM_SECTION = "NUM_SECTION";
    private static final String DATA_EXCEL_PERIODE = "PERIODE";
    private static final String DATA_EXCEL_SOLDE = "SOLDE";
    public static final String DATA_EXCEL_TITRE_DOC = "TITRE_DOC";
    private static final String DATA_EXCEL_TYPE = "TYPE";

    public static final String NUMERO_INFOROM = "0117CAF";
    public static final String XLS_DOC_NAME = "ListeSoldesAffiliesRadies";

    private CommonExcelmlContainer container = new CommonExcelmlContainer();

    public void createLigne(String numAffilie, String nom, String dateRadiation, String periode, String numSection,
            String dateSection, String descSection, String solde) {
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_NUM_AFFILIE, numAffilie);
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_NOM, nom);
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_DATE_RADIATION, dateRadiation);
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_PERIODE, periode);
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_NUM_SECTION, numSection);
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_DATE_SECTION, dateSection);
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_DESCRIPTION_SECTION, descSection);
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_SOLDE, solde);
    }

    private void loadDivers() {
        container.put(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_NUM_INFOROM, AFXmlmlSoldesAffiliesRadies.NUMERO_INFOROM);
    }

    public CommonExcelmlContainer loadResults() {
        loadDivers();

        return container;
    }

    public void putData(String column, String value) {
        container.put(column, value);
    }
}
