/*
 * Créé le 9 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class PRDemande extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static PRDemande demandeBidon = null;

    /** le nom du champ etat dans la table des demandes */
    public static final String FIELDNAME_ETAT = "WATETA";

    /** le nom du champ idDemande dans la table des demandes */
    public static final String FIELDNAME_ID_META_DOSSIER = "WAIMDO";

    /** le nom du champ idDemande dans la table des demandes */
    public static final String FIELDNAME_IDDEMANDE = "WAIDEM";

    /** le nom du champ idTiers dans la table des demandes */
    public static final String FIELDNAME_IDTIERS = "WAITIE";

    /** le nom du champ typeDemande dans la table des demandes */
    public static final String FIELDNAME_TYPE_DEMANDE = "WATTDE";

    /**
     * un id tiers bidon pour donner une demande a un droit qui n'a pas (encore) de tiers associé
     */
    public static final String ID_TIERS_DEMANDE_BIDON = "0";
    /** le nom de la table des demandes */
    public static final String TABLE_NAME = "PRDEMAP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut demande bidon
     * 
     * @param session
     *            DOCUMENT ME!
     * @param typeDemande
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut demande bidon
     */
    public static PRDemande getDemandeBidon(BSession session, String typeDemande) {
        try {
            if (PRDemande.demandeBidon == null) {
                PRDemandeManager demandeManager = new PRDemandeManager();
                demandeManager.setSession(session);
                demandeManager.setForIdTiers(PRDemande.ID_TIERS_DEMANDE_BIDON);
                demandeManager.setForTypeDemande(typeDemande);
                demandeManager.find();

                if (demandeManager.size() == 1) { // vraiment au cas ou...
                    PRDemande.demandeBidon = (PRDemande) demandeManager.getEntity(0);
                } else if (demandeManager.size() > 1) {
                    throw new Exception("plusieurs demandes bidons existent !!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PRDemande.demandeBidon;
    }

    private String etat = "";
    private String idDemande = "";
    private String idMetaDossier = "";
    private String idTiers = "";
    private transient PRTiersWrapper tiers = null; // transient pour ne pas
    private String typeDemande = "";

    /**
     * initialisation de Id Demande et de Type Demande
     * 
     * @param transaction
     *            la transaction courante
     * @throws Exception
     *             si une erreur est survenue
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdDemande(this._incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * @return la chaine TABLE_NAME
     */
    @Override
    protected String _getTableName() {
        return PRDemande.TABLE_NAME;
    }

    /**
     * DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_IDDEMANDE);
        typeDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_TYPE_DEMANDE);
        idTiers = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        etat = statement.dbReadNumeric(PRDemande.FIELDNAME_ETAT);
        idMetaDossier = statement.dbReadNumeric(PRDemande.FIELDNAME_ID_META_DOSSIER);
    }

    /**
     * DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(PRDemande.FIELDNAME_IDDEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#__writeProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(PRDemande.FIELDNAME_IDDEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
        statement.writeField(PRDemande.FIELDNAME_TYPE_DEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), typeDemande, "typeDemande"));
        statement.writeField(PRDemande.FIELDNAME_IDTIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(PRDemande.FIELDNAME_ETAT, this._dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(PRDemande.FIELDNAME_ID_META_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idMetaDossier, "idMetaDossier"));
    }

    /**
     * getter pour l'attribut Etat
     * 
     * @return la valeur courante de l'attribut Etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * getter pour l'attribut Id Demande
     * 
     * @return la valeur courante de l'attribut Id Demande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return
     */
    public String getIdMetaDossier() {
        return idMetaDossier;
    }

    /**
     * getter pour l'attribut Id Tiers
     * 
     * @return la valeur courante de l'attribut Id Tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut Type Demande
     * 
     * @return la valeur courante de l'attribut Type Demande
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * Charge le tiers associé avec cette demande.
     * <p>
     * Le tiers est automatiquement rechargé si le champ idTiers de ce bean est modifié.
     * </p>
     * 
     * @return Une instance de PRCiWrapper ou null s'il n'existe pas de tiers avec cet identifiant.
     * @throws Exception
     *             Si la recherche lance une exception.
     */
    public PRTiersWrapper loadTiers() throws Exception {
        if (!JadeStringUtil.isBlank(idTiers)
                && ((tiers == null) || !idTiers.equals(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS)))) {
            tiers = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);
        }

        return tiers;
    }

    /**
     * setter pour l'attribut Etat
     * 
     * @param etat
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * setter pour l'attribut Id Demande
     * 
     * @param idDemande
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param string
     */
    public void setIdMetaDossier(String string) {
        idMetaDossier = string;
    }

    /**
     * setter pour l'attribut Id Tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut Type Demande
     * 
     * @param typeDemande
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}
