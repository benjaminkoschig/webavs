package globaz.campus.db.etudiants;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class GEEtudiants extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ADMINISTRATION_ECOLE = "509036";
    public static final String FIELDNAME_ID_ETUDIANT = "YBIETU";
    public static final String FIELDNAME_ID_TIERS_ECOLE = "YBITEC";
    public static final String FIELDNAME_ID_TIERS_ETUDIANT = "YBITET";
    public static final String FIELDNAME_NUM_IMMATRICULATION = "YBNIMM";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    // public final static int AK_NUM_IMM_ECOLE = 1;
    public static final String TABLE_NAME_ETUDIANT = "GEETUDP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idEtudiant = null;
    private String idTiersEcole = null;
    private String idTiersEtudiant = null;
    private String numImmatriculation = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdEtudiant(_incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // Impossible de supprimer un étudiant s'il est lié à une annonce
        // validée
        GEAnnoncesManager annoncesMng = new GEAnnoncesManager();
        annoncesMng.setSession(getSession());
        annoncesMng.setForIdEtudiant(getIdEtudiant());
        annoncesMng.setForAnnonceValideeOuComptabilisee(new Boolean(true));
        if (annoncesMng.getCount(transaction) >= 1) {
            _addError(transaction, getSession().getLabel("SUPPRESSION_ETUDIANT_IMPOSSIBLE"));
        }
        // Si on supprime l'étudiant d'une annoce à traiter, en erreur, ou
        // erreur archivée,
        // il faut mettre à 0 l'idetudiant de l'annonce.
        annoncesMng.setForAnnonceValideeOuComptabilisee(new Boolean(false));
        annoncesMng.setForAnnoncesATraiterErreurOuArchivee(new Boolean(true));
        annoncesMng.find(transaction);
        for (int i = 0; i < annoncesMng.size(); i++) {
            GEAnnonces annonces = (GEAnnonces) annoncesMng.getEntity(i);
            annonces.setIdEtudiant("");
            annonces.wantCallValidate(false);
            annonces.update(transaction);
        }

    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Impossible de modifier un étudiant lié à une annone
        GEAnnoncesManager annoncesMng = new GEAnnoncesManager();
        annoncesMng.setSession(getSession());
        annoncesMng.setForIdEtudiant(getIdEtudiant());
        if (annoncesMng.getCount() >= 1) {
            _addError(transaction, getSession().getLabel("MODIFICATION_ETUDIANT_IMPOSSIBLE"));
        }
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME_ETUDIANT;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEtudiant = statement.dbReadNumeric(FIELDNAME_ID_ETUDIANT);
        idTiersEtudiant = statement.dbReadNumeric(FIELDNAME_ID_TIERS_ETUDIANT);
        idTiersEcole = statement.dbReadNumeric(FIELDNAME_ID_TIERS_ECOLE);
        numImmatriculation = statement.dbReadString(FIELDNAME_NUM_IMMATRICULATION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // idTiersEtudiant obligatoire
        if (JadeStringUtil.isBlank(getIdTiersEtudiant())) {
            _addError(statement.getTransaction(), getSession().getLabel("ID_TIERS_ETUDIANT_OBLIGATOIRE"));
        }
        // idTiersEcole obligatoire
        if (JadeStringUtil.isBlank(getIdTiersEcole())) {
            _addError(statement.getTransaction(), getSession().getLabel("ID_TIERS_ECOLE_OBLIGATOIRE"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement
                .writeKey(FIELDNAME_ID_ETUDIANT, _dbWriteNumeric(statement.getTransaction(), idEtudiant, "idEtudiant"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_ETUDIANT,
                _dbWriteNumeric(statement.getTransaction(), idEtudiant, "idEtudiant"));
        statement.writeField(FIELDNAME_ID_TIERS_ETUDIANT,
                _dbWriteNumeric(statement.getTransaction(), idTiersEtudiant, "idTiersEtudiant"));
        statement.writeField(FIELDNAME_ID_TIERS_ECOLE,
                _dbWriteNumeric(statement.getTransaction(), idTiersEcole, "idTiersEcole"));
        statement.writeField(FIELDNAME_NUM_IMMATRICULATION,
                _dbWriteString(statement.getTransaction(), numImmatriculation, "numImmatriculation"));
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

    public String getNumImmatriculation() {
        return numImmatriculation;
    }

    public void setIdEtudiant(String idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public void setIdTiersEcole(String idTiersEcole) {
        this.idTiersEcole = idTiersEcole;
    }

    public void setIdTiersEtudiant(String idTiersEtudiant) {
        this.idTiersEtudiant = idTiersEtudiant;
    }

    public void setNumImmatriculation(String numImmatriculation) {
        this.numImmatriculation = numImmatriculation;
    }
}
