package globaz.naos.listes.excel;

import globaz.naos.listes.excel.util.NaosContainer;

public class AFXmlmlAffiliationNAIndSansCI {

    private static final String DATA_EXCEL_AGENCE = "AGENCE";
    private static final String DATA_EXCEL_DATE = "DATE";
    public static final String DATA_EXCEL_DATE_DOC = "DATE_DOC";
    private static final String DATA_EXCEL_NOM = "NOM";
    private static final String DATA_EXCEL_NSS = "NSS";
    private static final String DATA_EXCEL_NUM_AFFILIE = "NUM_AFFILIE";
    private static final String DATA_EXCEL_NUM_INFOROM = "NUMERO_INFOROM";
    public static final String DATA_EXCEL_TITRE_DOC = "TITRE_DOC";
    private static final String DATA_EXCEL_TYPE = "TYPE";

    public static final String NUMERO_INFOROM = "0116CAF";
    public static final String XLS_DOC_NAME = "ListeAffiliationNAIndSansCI";

    private NaosContainer container = new NaosContainer();

    public void createLigne(String numAffilie, String nss, String nom, String type, String dateDebut, String agence) {
        container.put(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_NUM_AFFILIE, numAffilie);
        container.put(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_NSS, nss);
        container.put(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_NOM, nom);
        container.put(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_TYPE, type);
        container.put(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_DATE, dateDebut);
        container.put(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_AGENCE, agence);
    }

    private void loadDivers() {
        container.put(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_NUM_INFOROM,
                AFXmlmlAffiliationNAIndSansCI.NUMERO_INFOROM);
    }

    public NaosContainer loadResults() {
        loadDivers();

        return container;
    }

    public void putData(String column, String value) {
        container.put(column, value);
    }
}
