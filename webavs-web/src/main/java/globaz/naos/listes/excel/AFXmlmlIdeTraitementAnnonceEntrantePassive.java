package globaz.naos.listes.excel;

import globaz.webavs.common.CommonExcelmlContainer;

public class AFXmlmlIdeTraitementAnnonceEntrantePassive {

    public static final String DATA_EXCEL_DATE_TRAITEMENT = "DATE_TRAITEMENT";

    private static final String DATA_EXCEL_TYPE_ANNONCE = "TYPE_ANNONCE";
    private static final String DATA_EXCEL_ETAT_ANNNOCE = "ETAT_ANNNOCE";
    private static final String DATA_EXCEL_DATE_CREATION_ANNONCE = "DATE_CREATION_ANNONCE";
    private static final String DATA_EXCEL_NUM_IDE = "NUM_IDE";
    private static final String DATA_EXCEL_STATUT_NUM = "STATUT_NUM";
    private static final String DATA_EXCEL_RAISON_SOCIALE = "RAISON_SOCIALE";
    private static final String DATA_EXCEL_MESSAGE_ERREUR = "MESSAGE_ERREUR";
    private static final String DATA_EXCEL_NUM_AFFILIATION = "NUM_AFFILIATION";

    private static final String DATA_EXCEL_RUE = "RUE";
    private static final String DATA_EXCEL_NPA = "NPA";
    private static final String DATA_EXCEL_LOCALITE = "LOCALITE";
    private static final String DATA_EXCEL_CANTON = "CANTON";
    private static final String DATA_EXCEL_NAISSANCE = "NAISSANCE";
    private static final String DATA_EXCEL_NOGA = "NOGA";

    private static final String DATA_EXCEL_MESSAGE_SEDEX_50 = "MESSAGE";

    public static final String DATA_EXCEL_TITRE_DOC = "TITRE_DOC";

    public static final String DATA_EXCEL_NUM_INFOROM = "NUMERO_INFOROM";
    public static final String NUMERO_INFOROM = "0318CAF";
    public static final String XLS_DOC_NAME = "IdeTraitementAnnonceEntrantePassive";

    private CommonExcelmlContainer container = new CommonExcelmlContainer();

    public CommonExcelmlContainer getContainer() {
        return container;
    }

    public void createLigne(String typeAnnonce, String etatAnnonce, String dateCreation, String numIde,
            String numIdeStatut, String numAffiliation, String raisonSoc, String rue, String npa, String localite,
            String canton, String naissance, String noga, String messageSedex50, String messErreur) {
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_TYPE_ANNONCE, typeAnnonce);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_ETAT_ANNNOCE, etatAnnonce);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_DATE_CREATION_ANNONCE, dateCreation);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_NUM_IDE, numIde);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_STATUT_NUM, numIdeStatut);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_NUM_AFFILIATION, numAffiliation);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_RAISON_SOCIALE, raisonSoc);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_RUE, rue);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_NPA, npa);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_LOCALITE, localite);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_CANTON, canton);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_NAISSANCE, naissance);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_NOGA, noga);
        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_MESSAGE_SEDEX_50, messageSedex50);

        container.put(AFXmlmlIdeTraitementAnnonceEntrantePassive.DATA_EXCEL_MESSAGE_ERREUR, messErreur);
    }

    public void putData(String column, String value) {
        container.put(column, value);
    }
}
