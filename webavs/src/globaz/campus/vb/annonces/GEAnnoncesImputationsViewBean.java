package globaz.campus.vb.annonces;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class GEAnnoncesImputationsViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_FIELDNAME_ETAT_ANNONCE = "ETAT";
    // alias champs
    public static final String ALIAS_FIELDNAME_ID_ANNONCE = "IDANNONCE";
    public static final String ALIAS_FIELDNAME_ID_ANNONCE_PARENT = "IDPARENT";
    public static final String ALIAS_FIELDNAME_ID_IMPUTATION = "IDIMPUTATION";
    public static final String ALIAS_FIELDNAME_ID_LOT = "IDLOT ";
    public static final String ALIAS_FIELDNAME_IS_IMPUTATION = "ISIMPUTATION";
    public static final String ALIAS_FIELDNAME_NOM = "NOM";
    public static final String ALIAS_FIELDNAME_NUM_AVS = "NUMAVS";
    public static final String ALIAS_FIELDNAME_NUM_IMMATRICULATION_TRANSMIS = "NUMIMM";
    public static final String ALIAS_FIELDNAME_PRENOM = "PRENOM";
    // Alias table
    public static final String ALIAS_TABLE_NAME_ANNONCE = "ANNONCE";
    public static final String ALIAS_TABLE_NAME_IMPUTATION = "IMPUTATION";
    public static final String FIELDNAME_ETAT_ANNONCE = ALIAS_TABLE_NAME_ANNONCE + "."
            + GEAnnonces.FIELDNAME_ETAT_ANNONCE;
    // champs
    public static final String FIELDNAME_ID_ANNONCE = ALIAS_TABLE_NAME_ANNONCE + "." + GEAnnonces.FIELDNAME_ID_ANNONCE;
    public static final String FIELDNAME_ID_ANNONCE_PARENT = ALIAS_TABLE_NAME_ANNONCE + "."
            + GEAnnonces.FIELDNAME_ID_ANNONCE_PARENT;
    public static final String FIELDNAME_ID_IMPUTATION = ALIAS_TABLE_NAME_IMPUTATION + "."
            + GEAnnonces.FIELDNAME_ID_ANNONCE;
    public static final String FIELDNAME_ID_LOT = ALIAS_TABLE_NAME_ANNONCE + "." + GEAnnonces.FIELDNAME_ID_LOT;
    public static final String FIELDNAME_IS_IMPUTATION = ALIAS_TABLE_NAME_ANNONCE + "."
            + GEAnnonces.FIELDNAME_IS_IMPUTATION;
    public static final String FIELDNAME_NOM = ALIAS_TABLE_NAME_ANNONCE + "." + GEAnnonces.FIELDNAME_NOM;
    public static final String FIELDNAME_NUM_AVS = ALIAS_TABLE_NAME_ANNONCE + "." + GEAnnonces.FIELDNAME_NUM_AVS;
    public static final String FIELDNAME_NUM_IMMATRICULATION_TRANSMIS = ALIAS_TABLE_NAME_ANNONCE + "."
            + GEAnnonces.FIELDNAME_NUM_IMMATRICULATION_TRANSMIS;
    public static final String FIELDNAME_PRENOM = ALIAS_TABLE_NAME_ANNONCE + "." + GEAnnonces.FIELDNAME_PRENOM;

    private String csEtatAnnonce = "";
    private String idAnnonce = "";
    private String idAnnonceParent = "";
    private String idImputation = "";
    private String idLot = "";
    private Boolean isImputation = null;
    private String nom = "";
    private String numAvs = "";
    private String numImmatriculationTransmis = "";
    private String prenom = "";

    public GEAnnoncesImputationsViewBean() {
        super();
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric(ALIAS_FIELDNAME_ID_ANNONCE);
        idImputation = statement.dbReadNumeric(ALIAS_FIELDNAME_ID_IMPUTATION);
        idLot = statement.dbReadNumeric(ALIAS_FIELDNAME_ID_LOT);
        csEtatAnnonce = statement.dbReadNumeric(ALIAS_FIELDNAME_ETAT_ANNONCE);
        numAvs = statement.dbReadString(ALIAS_FIELDNAME_NUM_AVS);
        nom = statement.dbReadString(ALIAS_FIELDNAME_NOM);
        prenom = statement.dbReadString(ALIAS_FIELDNAME_PRENOM);
        numImmatriculationTransmis = statement.dbReadString(ALIAS_FIELDNAME_NUM_IMMATRICULATION_TRANSMIS);
        idAnnonceParent = statement.dbReadNumeric(ALIAS_FIELDNAME_ID_ANNONCE_PARENT);
        isImputation = statement.dbReadBoolean(ALIAS_FIELDNAME_IS_IMPUTATION);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public Boolean avecImputation() {
        if (JadeStringUtil.isBlankOrZero(getIdImputation())) {
            return new Boolean(false);
        } else {
            return new Boolean(true);
        }
    }

    public String getCsEtatAnnonce() {
        return csEtatAnnonce;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdAnnonceParent() {
        return idAnnonceParent;
    }

    public String getIdImputation() {
        return idImputation;
    }

    public String getIdLot() {
        return idLot;
    }

    public Boolean getIsImputation() {
        return isImputation;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumImmatriculationTransmis() {
        return numImmatriculationTransmis;
    }

    public String getPrenom() {
        return prenom;
    }
}
