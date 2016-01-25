package globaz.corvus.db.demandes;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRHierarchique;

/**
 * BEntity de la demande de rente
 * 
 * @author bsc
 */
public class REDemandeRente extends BEntity implements IPRCloneable, PRHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_DEMANDE_RENTE_PARENT = 2;

    public static final int ALTERNATE_KEY_ID_RENTE_CALCULEE = 1;
    /** lors d'un clonage, un fils de la demande sera crée */
    public static final int CLONE_FILS = 11110012;
    /** lors d'un clonage, un tout nouvelle demande sera créé. */
    public static final int CLONE_NORMAL = 11110011;
    public static final int DEFAULT_ALTERNATE_KEY_ID_DEMANDE_RENTE = 3;
    public static final String FIELDNAME_CS_ETAT = "YATETA";
    public static final String FIELDNAME_CS_TYPE_CALCUL = "YATTYC";
    public static final String FIELDNAME_CS_TYPE_DEMANDE_RENTE = "YATTYD";
    public static final String FIELDNAME_DATE_DEBUT = "YADDEB";
    public static final String FIELDNAME_DATE_DEPOT = "YADDEP";
    public static final String FIELDNAME_DATE_FIN = "YADFIN";
    public static final String FIELDNAME_DATE_RECEPTION = "YADREC";
    public static final String FIELDNAME_DATE_TRAITEMENT = "YADTRA";
    public static final String FIELDNAME_ID_DEMANDE_PRESTATION = "YAIMDO";
    // Nom des champs de la table
    public static final String FIELDNAME_ID_DEMANDE_RENTE = "YAIDEM";
    public static final String FIELDNAME_ID_DEMANDE_RENTE_PARENT = "YAIDPA";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final String FIELDNAME_ID_GESTIONNAIRE = "YAIGES";
    public static final String FIELDNAME_ID_INFO_COMPLEMENTAIRE = "YAIINC";
    public static final String FIELDNAME_ID_RENTE_CALCULEE = "YAIRCA";
    public static final String FIELDNAME_IS_DEMANDE_HISTORISE = "YABHIS";
    // Nom de la table
    public static final String TABLE_NAME_DEMANDE_RENTE = "REDEREN";

    /**
     * Charge une instance correcte de l'une des classes descendantes de REDemandeRente suivant l'id et le type
     * transmis.
     * 
     * <p>
     * Note: si le type est null, cette methode tente de le retrouver pour l'id correspondant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idDemandeRente
     *            jamais null
     * @param csTypeDemande
     *            peut etre vide ou null
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final REDemandeRente loadDemandeRente(BSession session, BITransaction transaction,
            String idDemandeRente, String csTypeDemandeRente) throws Exception {
        REDemandeRente retValue;

        // recupere le type de demande de rente s'il n'est pas transmis
        if (JadeStringUtil.isNull(csTypeDemandeRente) || JadeStringUtil.isIntegerEmpty(csTypeDemandeRente)) {

            retValue = new REDemandeRente();
            retValue.setIdDemandeRente(idDemandeRente);
            retValue.setSession(session);

            if (transaction == null) {
                retValue.retrieve();
            } else {
                retValue.retrieve(transaction);
            }

            csTypeDemandeRente = retValue.getCsTypeDemandeRente();
        }

        // charge la rente
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteAPI();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteInvalidite();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteSurvivant();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteVieillesse();
        } else {
            // oops, ben zut alors, on n'a pas le type de la rente
            throw new Exception(session.getLabel("ERREUR_TYPE_DEMANDE_VIDE"));
        }

        retValue.setSession(session);
        retValue.setIdDemandeRente(idDemandeRente);
        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    /**
     * Charge une instance correcte de l'une des classes descendantes de REDemandeRente suivant le no avs et le type
     * transmis.
     * 
     * <p>
     * Note: si le type est null, cette methode tente de le retrouver pour l'id correspondant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param nss
     *            nss
     * @param csTypeDemande
     *            peut etre vide ou null
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final REDemandeRente loadDemandeRenteParNss(BSession session, BITransaction transaction, String nss,
            String csTypeDemandeRente) throws Exception {

        PRTiersWrapper wrapper = PRTiersHelper.getTiers(session, nss);

        if (wrapper == null) {
            throw new Exception("Le tiers avec nss" + nss + "n'existe pas");
        }

        PRDemandeManager mgr = new PRDemandeManager();
        mgr.setSession(session);
        mgr.setForIdTiers(wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        mgr.setForTypeDemande(IPRDemande.CS_TYPE_RENTE);
        mgr.find(transaction, 1);
        if (mgr == null || mgr.size() == 0) {
            throw new Exception("Aucune demande de prestation n'existe pour ce tiers :" + nss + "("
                    + wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS) + ") Veuillez la créer.");
        }

        PRDemande demande = (PRDemande) mgr.getEntity(0);

        REDemandeRenteManager mgr2 = new REDemandeRenteManager();
        mgr2.setSession(session);
        mgr2.setForIdDemandePrestation(demande.getIdDemande());
        mgr2.setForCsTypeDemandeRente(csTypeDemandeRente);
        // Une seule demande possible par personne et par type de demande.
        mgr2.find(transaction, 2);
        if (mgr2.size() == 0) {
            throw new Exception("Aucune demande de rente n'existe pour ce tiers :" + nss + "("
                    + wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS) + ") Veuillez la créer.");
        } else if (mgr2.size() > 1) {
            throw new Exception("Il existe plusieurs demande pour ce tiers :" + nss + "("
                    + wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS) + ").");
        }

        String idDemandeRente = ((REDemandeRente) mgr2.getEntity(0)).getIdDemandeRente();
        REDemandeRente retValue = null;

        // charge la rente
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteAPI();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteInvalidite();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteSurvivant();
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(csTypeDemandeRente)) {
            retValue = new REDemandeRenteVieillesse();
        } else {
            // oops, ben zut alors, on n'a pas le type de la rente
            throw new Exception(session.getLabel("ERREUR_TYPE_DEMANDE_VIDE"));
        }

        retValue.setSession(session);
        retValue.setIdDemandeRente(idDemandeRente);
        retValue.retrieve(transaction);
        return retValue;
    }

    private String csEtat = "";
    private String csTypeCalcul = "";
    private String csTypeDemandeRente = "";
    private String dateDebut = "";
    private String dateDepot = "";
    private String dateFin = "";
    // private Boolean isDemandeHistorise = Boolean.FALSE;
    private String dateReception = "";

    private String dateTraitement = "";
    private transient PRDemande demande;

    private String idDemandePrestation = "";
    protected String idDemandeRente = "";
    private String idDemandeRenteParent = "";

    private String idGestionnaire = "";

    private String idInfoComplementaire = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idRenteCalculee = "";

    private transient PRInfoCompl infoComplementaire;

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idDemandeRente = _incCounter(transaction, idDemandeRente, TABLE_NAME_DEMANDE_RENTE);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_DEMANDE_RENTE;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDemandeRente = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE);
        idDemandePrestation = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_PRESTATION);
        idDemandeRenteParent = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE_PARENT);
        dateTraitement = statement.dbReadDateAMJ(FIELDNAME_DATE_TRAITEMENT);
        dateDepot = statement.dbReadDateAMJ(FIELDNAME_DATE_DEPOT);
        dateReception = statement.dbReadDateAMJ(FIELDNAME_DATE_RECEPTION);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idRenteCalculee = statement.dbReadNumeric(FIELDNAME_ID_RENTE_CALCULEE);
        idInfoComplementaire = statement.dbReadNumeric(FIELDNAME_ID_INFO_COMPLEMENTAIRE);
        csTypeCalcul = statement.dbReadNumeric(FIELDNAME_CS_TYPE_CALCUL);
        csTypeDemandeRente = statement.dbReadNumeric(FIELDNAME_CS_TYPE_DEMANDE_RENTE);
        idGestionnaire = statement.dbReadString(FIELDNAME_ID_GESTIONNAIRE);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN);
        // isDemandeHistorise =
        // statement.dbReadBoolean(FIELDNAME_IS_DEMANDE_HISTORISE);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_RENTE_CALCULEE:
                statement.writeKey(FIELDNAME_ID_RENTE_CALCULEE,
                        _dbWriteNumeric(statement.getTransaction(), getIdRenteCalculee(), "idRenteCalculee"));
                break;
            case ALTERNATE_KEY_ID_DEMANDE_RENTE_PARENT:
                statement.writeKey(FIELDNAME_ID_DEMANDE_RENTE_PARENT,
                        _dbWriteNumeric(statement.getTransaction(), getIdDemandeRenteParent(), "idDemandeRenteParent"));
                break;
            case DEFAULT_ALTERNATE_KEY_ID_DEMANDE_RENTE:
                statement.writeKey(FIELDNAME_ID_DEMANDE_RENTE,
                        _dbWriteNumeric(statement.getTransaction(), getIdDemandeRente(), "idDemandeRente"));
                break;

            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_DEMANDE_RENTE, _dbWriteNumeric(statement.getTransaction(), idDemandeRente));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_DEMANDE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
        statement.writeField(FIELDNAME_ID_DEMANDE_PRESTATION,
                _dbWriteNumeric(statement.getTransaction(), idDemandePrestation, "idDemandePrestation"));
        statement.writeField(FIELDNAME_ID_DEMANDE_RENTE_PARENT,
                _dbWriteNumeric(statement.getTransaction(), idDemandeRenteParent, "idDemandeRenteParent"));
        statement.writeField(FIELDNAME_DATE_TRAITEMENT,
                _dbWriteDateAMJ(statement.getTransaction(), dateTraitement, "dateTraitement"));
        statement.writeField(FIELDNAME_DATE_DEPOT, _dbWriteDateAMJ(statement.getTransaction(), dateDepot, "dateDepot"));
        statement.writeField(FIELDNAME_DATE_RECEPTION,
                _dbWriteDateAMJ(statement.getTransaction(), dateReception, "dateReception"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_ID_RENTE_CALCULEE,
                _dbWriteNumeric(statement.getTransaction(), idRenteCalculee, "idRenteCalculee"));
        statement.writeField(FIELDNAME_ID_INFO_COMPLEMENTAIRE,
                _dbWriteNumeric(statement.getTransaction(), idInfoComplementaire, "idInfoComplementaire"));
        statement.writeField(FIELDNAME_CS_TYPE_CALCUL,
                _dbWriteNumeric(statement.getTransaction(), csTypeCalcul, "csTypeCalcul"));
        statement.writeField(FIELDNAME_CS_TYPE_DEMANDE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), csTypeDemandeRente, "csTypeDemandeRente"));
        statement.writeField(FIELDNAME_ID_GESTIONNAIRE,
                _dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(FIELDNAME_DATE_DEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATE_FIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        // statement.writeField(FIELDNAME_IS_DEMANDE_HISTORISE,
        // _dbWriteBoolean(statement.getTransaction(), isDemandeHistorise,
        // BConstants.DB_TYPE_BOOLEAN_CHAR, "isDemandeHistorise"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        REDemandeRente clone = new REDemandeRente();
        duplicateDemandeRente(clone, action);

        return clone;
    }

    /**
     * copie les données de bases du prononce pour un clonage.
     * 
     * @param clone
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected void duplicateDemandeRente(REDemandeRente clone, int action) throws Exception {

        clone.setIdDemandePrestation(getIdDemandePrestation());
        clone.setDateTraitement(getDateTraitement());
        clone.setDateDepot(getDateDepot());
        clone.setDateReception(getDateReception());
        clone.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
        clone.setCsTypeCalcul(getCsTypeCalcul());
        clone.setCsTypeDemandeRente(getCsTypeDemandeRente());
        clone.setIdGestionnaire(getSession().getUserId());
        clone.setDateDebut(getDateDebut());
        clone.setDateFin(getDateFin());
        // clone.setIsDemandeHistorise(getIsDemandeHistorise());

        // CLONE FILS
        if (action == REDemandeRente.CLONE_FILS) {
            clone.setIdDemandeRenteParent(getIdDemandeRente());
        } else {
            clone.setIdDemandeRenteParent("0");
        }

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);
    }

    /**
     * getter pour l'attribut cs etat.
     * 
     * @return la valeur courante de l'attribut cs etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * getter pour l'attribut csTypeCalcul
     * 
     * @return la valeur courante de l'attribut csTypeCalcul
     */
    public String getCsTypeCalcul() {
        return csTypeCalcul;
    }

    /**
     * getter pour l'attribut csTypeDemandeRente
     * 
     * @return la valeur courante de l'attribut csTypeDemandeRente
     */
    public String getCsTypeDemandeRente() {
        return csTypeDemandeRente;
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut dateDepot
     * 
     * @return la valeur courante de l'attribut dateDepot
     */
    public String getDateDepot() {
        return dateDepot;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut dateReception
     * 
     * @return la valeur de l'attribut dateReception
     */
    public String getDateReception() {
        return dateReception;
    }

    /**
     * getter pour l'attribut dateTraitement
     * 
     * @return la valeur courante de l'attribut dateTraitement
     */
    public String getDateTraitement() {
        return dateTraitement;
    }

    /**
     * getter pour l'attribut idDemandePrestation
     * 
     * @return la valeur courante de l'attribut idDemandePrestation
     */
    public String getIdDemandePrestation() {
        return idDemandePrestation;
    }

    /**
     * getter pour l'attribut idDemandeRente
     * 
     * @return la valeur courante de l'attribut idDemandeRente
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * getter pour l'attribut idDemandeRenteParent
     * 
     * @return la valeur courante de l'attribut idDemandeRenteParent
     */
    public String getIdDemandeRenteParent() {
        return idDemandeRenteParent;
    }

    /**
     * getter pour l'attribut idGestionnaire
     * 
     * @return la valeur courante de l'attribut idGestionnaire
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * getter pour l'attribut idInfoComplementaire
     * 
     * @return la valeur courante de l'attribut idInfoComplementaire
     */
    public String getIdInfoComplementaire() {
        return idInfoComplementaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.tools.PRHierarchique#getIdMajeur()
     */
    @Override
    public String getIdMajeur() {
        return idDemandeRente;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.tools.PRHierarchique#getIdParent()
     */
    @Override
    public String getIdParent() {
        return idDemandeRenteParent;
    }

    /**
     * getter pour l'attribut idRenteCalculee
     * 
     * @return la valeur courante de l'attribut idRenteCalculee
     */
    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdDemandeRente();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRDemande loadDemandePrestation(BITransaction transaction) throws Exception {
        if (demande == null) {
            demande = new PRDemande();
        }

        demande.setSession(getSession());

        if (!JadeStringUtil.isIntegerEmpty(idDemandePrestation) && !idDemandePrestation.equals(demande.getIdDemande())) {
            demande.setIdDemande(idDemandePrestation);
            if (transaction == null) {
                demande.retrieve();
            } else {
                demande.retrieve(transaction);
            }
        }

        return demande;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRInfoCompl loadInfoComplementaire(BITransaction transaction) throws Exception {
        if (infoComplementaire == null) {
            infoComplementaire = new PRInfoCompl();
        }

        infoComplementaire.setSession(getSession());

        if (!JadeStringUtil.isIntegerEmpty(idInfoComplementaire)
                && !idInfoComplementaire.equals(infoComplementaire.getIdInfoCompl())) {
            infoComplementaire.setIdInfoCompl(idInfoComplementaire);
            infoComplementaire.retrieve(transaction);

        }

        return infoComplementaire;
    }

    /**
     * setter pour l'attribut cs etat.
     * 
     * @param csEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /**
     * setter pour l'attribut csTypeCalcul.
     * 
     * @param csTypeCalcul
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeCalcul(String string) {
        csTypeCalcul = string;
    }

    /**
     * setter pour l'attribut csTypeDemandeRente.
     * 
     * @param csTypeDemandeRente
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeDemandeRente(String string) {
        csTypeDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * setter pour l'attribut dateDepot.
     * 
     * @param dateDepot
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDepot(String string) {
        dateDepot = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * setter pour l'attribut dateReception.
     * 
     * @param dateReception
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateReception(String string) {
        dateReception = string;
    }

    /**
     * setter pour l'attribut dateTraitement.
     * 
     * @param dateTraitement
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateTraitement(String string) {
        dateTraitement = string;
    }

    /**
     * setter pour l'attribut idDemandePrestation.
     * 
     * @param idDemandePrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemandePrestation(String string) {
        idDemandePrestation = string;
    }

    /**
     * setter pour l'attribut idDemandeRente.
     * 
     * @param idDemandeRente
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * setter pour l'attribut idDemandeRenteParent.
     * 
     * @param idDemandeRenteParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemandeRenteParent(String string) {
        idDemandeRenteParent = string;
    }

    /**
     * setter pour l'attribut idGestionnaire.
     * 
     * @param idGestionnaire
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdGestionnaire(String string) {
        idGestionnaire = string;
    }

    /**
     * setter pour l'attribut idInfoComplementaire.
     * 
     * @param idInfoComplementaire
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdInfoComplementaire(String string) {
        idInfoComplementaire = string;
    }

    /**
     * setter pour l'attribut idRenteCalculee.
     * 
     * @param idRenteCalculee
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRenteCalculee(String string) {
        idRenteCalculee = string;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdDemandeRente(pk);
    }

}
