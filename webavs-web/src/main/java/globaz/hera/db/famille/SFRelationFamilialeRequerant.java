/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author mmu
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFRelationFamilialeRequerant extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_MEMBRE_REQ = 1;

    /** DOCUMENT ME! */
    public static final String FIELD_IDMEMBREFAMILLE = "WFIMEF";

    /** DOCUMENT ME! */
    public static final String FIELD_IDRELATIONFAMILIALE = "WFIRFR";

    /** DOCUMENT ME! */
    public static final String FIELD_IDREQUERANT = "WFIREQ";

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "SFREFARE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idMembreFamille = "";
    private String idRelationFamilialeRequerant = "";
    private String idRequerant = "";

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
        setIdRelationFamilialeRequerant(_incCounter(transaction, "0"));
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
        idRelationFamilialeRequerant = statement.dbReadNumeric(FIELD_IDRELATIONFAMILIALE);
        idMembreFamille = statement.dbReadNumeric(FIELD_IDMEMBREFAMILLE);
        idRequerant = statement.dbReadNumeric(FIELD_IDREQUERANT);
    }

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
        if (alternateKey == ALTERNATE_KEY_MEMBRE_REQ) {
            statement.writeKey(FIELD_IDMEMBREFAMILLE,
                    _dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
            statement.writeKey(FIELD_IDREQUERANT,
                    _dbWriteNumeric(statement.getTransaction(), idRequerant, "idRequerant"));

        } else {
            super._writeAlternateKey(statement, alternateKey);
        }
    }

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
        statement.writeKey(
                FIELD_IDRELATIONFAMILIALE,
                _dbWriteNumeric(statement.getTransaction(), idRelationFamilialeRequerant,
                        "idRelationFamilialeRequerant"));
    }

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
        statement.writeField(
                FIELD_IDRELATIONFAMILIALE,
                _dbWriteNumeric(statement.getTransaction(), idRelationFamilialeRequerant,
                        "idRelationFamilialeRequerant"));
        statement.writeField(FIELD_IDMEMBREFAMILLE,
                _dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
        statement
                .writeField(FIELD_IDREQUERANT, _dbWriteNumeric(statement.getTransaction(), idRequerant, "idRequerant"));
    }

    /**
     * getter pour l'attribut id membre famille
     * 
     * @return la valeur courante de l'attribut id membre famille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * getter pour l'attribut id relation familiale requerant
     * 
     * @return la valeur courante de l'attribut id relation familiale requerant
     */
    public String getIdRelationFamilialeRequerant() {
        return idRelationFamilialeRequerant;
    }

    /**
     * getter pour l'attribut id requerant
     * 
     * @return la valeur courante de l'attribut id requerant
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * setter pour l'attribut id membre famille
     * 
     * @param idMembresFamilles
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdMembreFamille(String idMembresFamilles) {
        idMembreFamille = idMembresFamilles;
    }

    /**
     * setter pour l'attribut id relation familiale requerant
     * 
     * @param idRelationFamilialeRequerant
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRelationFamilialeRequerant(String idRelationFamilialeRequerant) {
        this.idRelationFamilialeRequerant = idRelationFamilialeRequerant;
    }

    /**
     * setter pour l'attribut id requerant
     * 
     * @param idRequerant
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

}
