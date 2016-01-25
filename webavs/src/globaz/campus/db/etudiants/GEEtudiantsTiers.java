package globaz.campus.db.etudiants;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class GEEtudiantsTiers extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_NAME_TIERS_A = "TIERSA";
    public static final String ALIAS_NAME_TIERS_B = "TIERSB";
    public static final String ALIASNAME_NOM_ECOLE = "NOMECO";
    public static final String ALIASNAME_NOM_ETUDIANT = "NOMETU";
    public static final String ALIASNAME_PRENOM_ETUDIANT = "PRENOMETU";
    public static final String FIELDNAME_NOM_ECOLE = ALIAS_NAME_TIERS_B + ".HTLDE1";
    public static final String FIELDNAME_NOM_ETUDIANT = ALIAS_NAME_TIERS_A + ".HTLDE1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM_ETUDIANT = ALIAS_NAME_TIERS_A + ".HTLDE2";
    public static final String TABLE_NAME_PERSONNE_AVS = "TIPAVSP";
    public static final String TABLE_NAME_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idEtudiant = null;
    private String idTiersEcole = null;
    private String idTiersEtudiant = null;
    private String nomEcole = null;
    private String nomEtudiant = null;
    private String numAvs = null;
    private String numImmatriculation = null;
    private String prenomEtudiant = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getTableName() {
        return GEEtudiants.TABLE_NAME_ETUDIANT;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEtudiant = statement.dbReadNumeric(GEEtudiants.FIELDNAME_ID_ETUDIANT);
        idTiersEtudiant = statement.dbReadNumeric(GEEtudiants.FIELDNAME_ID_TIERS_ETUDIANT);
        idTiersEcole = statement.dbReadNumeric(GEEtudiants.FIELDNAME_ID_TIERS_ECOLE);
        numImmatriculation = statement.dbReadString(GEEtudiants.FIELDNAME_NUM_IMMATRICULATION);
        nomEtudiant = statement.dbReadString(ALIASNAME_NOM_ETUDIANT);
        prenomEtudiant = statement.dbReadString(ALIASNAME_PRENOM_ETUDIANT);
        nomEcole = statement.dbReadString(ALIASNAME_NOM_ECOLE);
        numAvs = statement.dbReadString(FIELDNAME_NUM_AVS);
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

    public String getIdEtudiant() {
        return idEtudiant;
    }

    public String getIdTiersEcole() {
        return idTiersEcole;
    }

    public String getIdTiersEtudiant() {
        return idTiersEtudiant;
    }

    public String getNomEcole() {
        return nomEcole;
    }

    public String getNomEtudiant() {
        return nomEtudiant;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumImmatriculation() {
        return numImmatriculation;
    }

    public String getPrenomEtudiant() {
        return prenomEtudiant;
    }

}
