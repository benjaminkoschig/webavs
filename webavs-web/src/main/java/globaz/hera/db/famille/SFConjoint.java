/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.util.Iterator;

/**
 * <H1>entiy sur la table des conjoints</H1>
 * 
 * @author jpa
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFConjoint extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY = 1;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    public static final String FIELD_IDCONJOINT1 = "WJICO1";

    /** DOCUMENT ME! */
    public static final String FIELD_IDCONJOINT2 = "WJICO2";

    /** DOCUMENT ME! */
    public static final String FIELD_IDCONJOINTS = "WJICON";

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "SFCONJOI";

    private String idConjoint1 = "";
    private String idConjoint2 = "";
    private String idConjoints = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFConjoint.
     */
    public SFConjoint() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdConjoints(_incCounter(transaction, "0"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // Ne Supprime pas la relation si les conjoints ont des enfants
        SFEnfantManager enfantsManager = new SFEnfantManager();
        enfantsManager.setSession(getSession());
        enfantsManager.setForIdConjoint(getIdConjoints());
        int nbEnfants = enfantsManager.getCount(transaction);
        if (nbEnfants > 0) {
            _addError(transaction, transaction.getSession().getLabel("ERROR_DELETE_CONJOINT_ENFANT"));
            return;
        }

        // Supprime les relations des conjoints
        // a noter que le membre n est pas effacé, mais reste sans lien avec le
        // requérant
        SFRelationConjointManager relManager = new SFRelationConjointManager();
        relManager.setSession(getSession());
        relManager.setForIdDesConjoints(getIdConjoints());
        relManager.find(transaction);
        Iterator it = relManager.iterator();
        while (it.hasNext()) {
            SFRelationConjoint relation = (SFRelationConjoint) it.next();
            relation.delete(transaction);
        }

        removeRelationRequerant(getIdConjoint1(), getIdConjoint2(), transaction);
        removeRelationRequerant(getIdConjoint2(), getIdConjoint1(), transaction);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idConjoints = statement.dbReadNumeric(FIELD_IDCONJOINTS);
        idConjoint1 = statement.dbReadNumeric(FIELD_IDCONJOINT1);
        idConjoint2 = statement.dbReadNumeric(FIELD_IDCONJOINT2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALTERNATE_KEY) {

        }
        super._writeAlternateKey(statement, alternateKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDCONJOINTS, _dbWriteNumeric(statement.getTransaction(), idConjoints, "idConjoints"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement
                .writeField(FIELD_IDCONJOINTS, _dbWriteNumeric(statement.getTransaction(), idConjoints, "idConjoints"));
        statement
                .writeField(FIELD_IDCONJOINT1, _dbWriteNumeric(statement.getTransaction(), idConjoint1, "idConjoint1"));
        statement
                .writeField(FIELD_IDCONJOINT2, _dbWriteNumeric(statement.getTransaction(), idConjoint2, "idConjoint2"));
    }

    /**
     * renvoie un itérateur sur les enfants de conjoints null est renvoyé en cas d'erreur
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @return les enfants pour un idConjoints ou null en cas d'erreur
     */
    public Iterator getEnfants(BITransaction transaction) {

        // On est obligé de passer par la table des conjoints
        SFApercuEnfantManager enfantsManager = new SFApercuEnfantManager();

        try {
            enfantsManager.setSession(getSession());
            enfantsManager.setForIdConjoint(getIdConjoints());
            enfantsManager.find(transaction);
        } catch (Exception e) {
            return null;
        }

        return enfantsManager.iterator();
    }

    /**
     * getter pour l'attribut id conjoint1
     * 
     * @return la valeur courante de l'attribut id conjoint1
     */
    public String getIdConjoint1() {
        return idConjoint1;
    }

    /**
     * getter pour l'attribut id conjoint2
     * 
     * @return la valeur courante de l'attribut id conjoint2
     */
    public String getIdConjoint2() {
        return idConjoint2;
    }

    /**
     * getter pour l'attribut id conjoints
     * 
     * @return la valeur courante de l'attribut id conjoints
     */
    public String getIdConjoints() {
        return idConjoints;
    }

    /**
     * Renvoie l'idMembreFamille du conjoint du membre donné par l'id
     * 
     * @param idMembre
     * @return null si l'id donné en parametre ne correspond à aucun conjoint
     */
    public String getIdMembreFamilleConjoint(String idMembre) {
        if (getIdConjoint1().equals(idMembre)) {
            return idConjoint2;
        } else if (getIdConjoint2().equals(idMembre)) {
            return idConjoint1;
        } else {
            return null;
        }
    }

    private void removeRelationRequerant(String idMembreRequerant, String idMembreConjoint, BITransaction transaction)
            throws Exception {
        // Supprime la relation au niveau requerant
        // Si le premier conjoint est un requerant
        SFRequerantManager reqMgr = new SFRequerantManager();
        reqMgr.setSession(getSession());
        reqMgr.setForIdMembreFamille(idMembreRequerant);
        reqMgr.find(transaction);
        // Supprime les relation qu'à le requerant dans tous les domaines avec
        // le conjoint
        for (Iterator itReq = reqMgr.iterator(); itReq.hasNext();) {
            SFRequerant requerant = (SFRequerant) itReq.next();
            SFRelationFamilialeRequerant relReq = new SFRelationFamilialeRequerant();
            relReq.setSession(getSession());
            relReq.setIdRequerant(requerant.getIdRequerant());
            relReq.setIdMembreFamille(idMembreConjoint);
            relReq.setAlternateKey(SFRelationFamilialeRequerant.ALTERNATE_KEY_MEMBRE_REQ);
            relReq.retrieve(transaction);
            if (!relReq.isNew()) {
                relReq.delete(transaction);
            }
        }
    }

    /**
     * setter pour l'attribut id conjoint1
     * 
     * @param idConjoint1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoint1(String idConjoint1) {
        this.idConjoint1 = idConjoint1;
    }

    /**
     * setter pour l'attribut id conjoint2
     * 
     * @param idConjoint2
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoint2(String idConjoint2) {
        this.idConjoint2 = idConjoint2;
    }

    /**
     * setter pour l'attribut id conjoints
     * 
     * @param idConjoints
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoints(String idConjoints) {
        this.idConjoints = idConjoints;
    }

}
