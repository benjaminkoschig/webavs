/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;

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
public class SFRequerant extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALT_KEY_DOMAINE_MEMBRE = 1;

    public static final int ALT_KEY_IDREQUER_IDMEMBRE = 2;

    /** DOCUMENT ME! */
    public static final String FIELD_IDDOMAINEAPPLICATION = "WDTDOA";

    /** DOCUMENT ME! */
    public static final String FIELD_IDMEMBREFAMILLE = "WDIMEF";

    /** DOCUMENT ME! */
    public static final String FIELD_IDREQUERANT = "WDIREQ";

    public static final String FIELD_PROVENANCE = "WTLPRO";

    public static final String PROVENANCE_REPRISE_DONNEES = "1";

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "SFREQUER";

    private String idDomaineApplication = "";
    private String idMembreFamille = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idRequerant = "";
    private String provenance = "";

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
        setIdRequerant(_incCounter(transaction, "0"));
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
        idRequerant = statement.dbReadNumeric(FIELD_IDREQUERANT);
        idMembreFamille = statement.dbReadNumeric(FIELD_IDMEMBREFAMILLE);
        idDomaineApplication = statement.dbReadNumeric(FIELD_IDDOMAINEAPPLICATION);
        provenance = statement.dbReadString(FIELD_PROVENANCE);
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
        // idMembreFamille doit être renseigné
        if (JadeStringUtil.isEmpty(idMembreFamille)) {
            _addError(statement.getTransaction(), getSession().getLabel("VALIDATE_ID_MEMBRE_FAMILLE_OBLIGATOIRE"));
        }
        // si idDomaine n'est pas renseingé, c'est le domaine par défaut
        if (JadeStringUtil.isEmpty(idDomaineApplication)) {
            idDomaineApplication = ISFSituationFamiliale.CS_DOMAINE_ID_STANDARD;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALT_KEY_DOMAINE_MEMBRE) {
            statement.writeKey(FIELD_IDDOMAINEAPPLICATION,
                    _dbWriteNumeric(statement.getTransaction(), idDomaineApplication, "idDomaineApplication"));
            statement.writeKey(FIELD_IDMEMBREFAMILLE,
                    _dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
        } else if (alternateKey == ALT_KEY_IDREQUER_IDMEMBRE) {
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
        statement.writeKey(FIELD_IDREQUERANT, _dbWriteNumeric(statement.getTransaction(), idRequerant, "idRequerant"));
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
        statement
                .writeField(FIELD_IDREQUERANT, _dbWriteNumeric(statement.getTransaction(), idRequerant, "idRequerant"));
        statement.writeField(FIELD_IDMEMBREFAMILLE,
                _dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
        statement.writeField(FIELD_IDDOMAINEAPPLICATION,
                _dbWriteNumeric(statement.getTransaction(), idDomaineApplication, "idDomaineApplication"));

        statement.writeField(FIELD_PROVENANCE, _dbWriteString(statement.getTransaction(), provenance, "provenance"));

    }

    /**
     * getter pour l'attribut id domaine application
     * 
     * @return la valeur courante de l'attribut id domaine application
     */
    public String getIdDomaineApplication() {
        return idDomaineApplication;
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
     * getter pour l'attribut id requerant
     * 
     * @return la valeur courante de l'attribut id requerant
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    public String getProvenance() {
        return provenance;
    }

    /**
     * setter pour l'attribut id domaine application
     * 
     * @param idDomaineApplication
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
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
     * setter pour l'attribut id requerant
     * 
     * @param idRequerant
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

}
