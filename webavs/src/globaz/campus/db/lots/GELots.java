package globaz.campus.db.lots;

import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author acr
 */

public class GELots extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Codes Système
    // EtatLot
    public final static String CS_ETAT_A_TRAITER = "930001";
    public final static String CS_ETAT_COMPTABILISE = "930005";
    public final static String CS_ETAT_EN_COURS = "930002";
    public final static String CS_ETAT_ERREUR = "930003";
    public final static String CS_ETAT_VALIDE = "930004";
    public static final String FIELDNAME_ANNEE = "YANANN";
    public final static String FIELDNAME_DATE_RECEPTION = "YADREC";

    public final static String FIELDNAME_ETAT_LOT = "YATETA";
    public final static String FIELDNAME_ID_LOT = "YAILOT";
    public static final String FIELDNAME_ID_TIERS_ECOLE = "YAITEC";
    public static final String FIELDNAME_LIBELLE_TRAITEMENT = "YANOMT ";
    public final static String TABLE_NAME_LOT = "GELOTSP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annee = "";
    private String csEtatLot = "";
    private String dateReceptionLot = "";
    private String idLot = "";
    private String idTiersEcole = "";
    private String libelleTraitement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdLot(_incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // La suppression est inderdite si le lot et validé ou comptabilisé
        if (CS_ETAT_VALIDE.equals(getCsEtatLot()) || CS_ETAT_COMPTABILISE.equals(getCsEtatLot())) {
            _addError(transaction, getSession().getLabel("SUPPRESSION_LOT_IMPOSSIBLE"));
        } else {
            // Suppression des annonces
            GEAnnoncesManager annoncesMng = new GEAnnoncesManager();
            annoncesMng.setSession(getSession());
            annoncesMng.setForIdLot(getIdLot());
            if (annoncesMng.getCount(transaction) > 0) {
                annoncesMng.delete(transaction);
            }
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (CS_ETAT_VALIDE.equals(getCsEtatLot()) || CS_ETAT_COMPTABILISE.equals(getCsEtatLot())) {
            _addError(transaction, getSession().getLabel("MODIFICATION_LOT_IMPOSSIBLE"));
        }
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME_LOT;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric(FIELDNAME_ID_LOT);
        dateReceptionLot = statement.dbReadDateAMJ(FIELDNAME_DATE_RECEPTION);
        csEtatLot = statement.dbReadNumeric(FIELDNAME_ETAT_LOT);
        idTiersEcole = statement.dbReadNumeric(FIELDNAME_ID_TIERS_ECOLE);
        libelleTraitement = statement.dbReadString(FIELDNAME_LIBELLE_TRAITEMENT);
        annee = statement.dbReadNumeric(FIELDNAME_ANNEE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getDateReceptionLot())) {
            _addError(statement.getTransaction(), getSession().getLabel("DATE_LOT_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getCsEtatLot())) {
            _addError(statement.getTransaction(), getSession().getLabel("ETAT_LOT_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getIdTiersEcole())) {
            _addError(statement.getTransaction(), getSession().getLabel("ECOLE_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlank(getLibelleTraitement())) {
            _addError(statement.getTransaction(), getSession().getLabel("LIBELLE_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getAnnee())) {
            _addError(statement.getTransaction(), getSession().getLabel("ANNEE_OBLIGATOIRE"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_LOT, _dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_LOT, _dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(FIELDNAME_DATE_RECEPTION,
                _dbWriteDateAMJ(statement.getTransaction(), dateReceptionLot, "dateReceptionLot"));
        statement.writeField(FIELDNAME_ETAT_LOT, _dbWriteNumeric(statement.getTransaction(), csEtatLot, "csEtatLot"));
        statement.writeField(FIELDNAME_ID_TIERS_ECOLE,
                _dbWriteNumeric(statement.getTransaction(), idTiersEcole, "idTiersEcole"));
        statement.writeField(FIELDNAME_LIBELLE_TRAITEMENT,
                _dbWriteString(statement.getTransaction(), libelleTraitement, "libelleTraitement"));
        statement.writeField(FIELDNAME_ANNEE, _dbWriteNumeric(statement.getTransaction(), annee, "annee"));
    }

    public String getAnnee() {
        return annee;
    }

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getDateReceptionLot() {
        return dateReceptionLot;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdTiersEcole() {
        return idTiersEcole;
    }

    public String getLibelleTraitement() {
        return libelleTraitement;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCsEtatLot(String csEtatLot) {
        this.csEtatLot = csEtatLot;
    }

    public void setDateReceptionLot(String dateReceptionLot) {
        this.dateReceptionLot = dateReceptionLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdTiersEcole(String idTiersEcole) {
        this.idTiersEcole = idTiersEcole;
    }

    public void setLibelleTraitement(String libelleTraitement) {
        this.libelleTraitement = libelleTraitement;
    }
}
