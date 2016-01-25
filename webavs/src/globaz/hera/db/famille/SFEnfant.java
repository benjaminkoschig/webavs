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
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFSituationFamiliale;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author jpa
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFEnfant extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_IDMEMBREFAMILLE = 1;

    /** DOCUMENT ME! */
    public static final String FIELD_DATEADOPTION = "WIDADO";

    /** DOCUMENT ME! */
    public static final String FIELD_IDCONJOINT = "WIICON";

    /** DOCUMENT ME! */
    public static final String FIELD_IDENFANT = "WIIENF";

    /** DOCUMENT ME! */
    public static final String FIELD_IDMEMBREFAMILLE = "WIIMEF";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "SFENFANT";
    private String dateAdoption = "";
    private String idConjoint = "";
    private String idEnfant = "";

    private String idMembreFamille = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFEnfant.
     */
    public SFEnfant() {
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
        setIdEnfant(this._incCounter(transaction, "0"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // On supprime les périodes attachée à l'enfant, mais on garde le membre
        // de famille
        SFPeriodeManager periodeMgr = new SFPeriodeManager();
        periodeMgr.setSession(getSession());
        periodeMgr.setForIdMembreFamille(getIdMembreFamille());
        periodeMgr.find(transaction);
        for (Iterator it = periodeMgr.iterator(); it.hasNext();) {
            SFPeriode periode = (SFPeriode) it.next();
            if (ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE.equals(periode.getType())) {
                periode.delete(transaction);
            }
        }
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
        return SFEnfant.TABLE_NAME;
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
        idEnfant = statement.dbReadNumeric(SFEnfant.FIELD_IDENFANT);
        dateAdoption = statement.dbReadDateAMJ(SFEnfant.FIELD_DATEADOPTION);
        idMembreFamille = statement.dbReadNumeric(SFEnfant.FIELD_IDMEMBREFAMILLE);
        idConjoint = statement.dbReadNumeric(SFEnfant.FIELD_IDCONJOINT);
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
        // IdMembreFamille doit être renseigné
        _propertyMandatory(statement.getTransaction(), idMembreFamille, getSession().getLabel("7060"));

        // si renseignée, la date d'adoption doit être valide
        if (!JAUtil.isDateEmpty(dateAdoption)) {
            _checkDate(statement.getTransaction(), dateAdoption, getSession().getLabel("VALIDATE_DATE_ADOPTION"));
        }
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == SFEnfant.ALTERNATE_KEY_IDMEMBREFAMILLE) {
            statement.writeKey(SFEnfant.FIELD_IDMEMBREFAMILLE,
                    this._dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
        } else {
            super._writeAlternateKey(statement, alternateKey);
        }

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
        statement.writeKey(SFEnfant.FIELD_IDENFANT,
                this._dbWriteNumeric(statement.getTransaction(), idEnfant, "idEnfant"));
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
        statement.writeField(SFEnfant.FIELD_IDENFANT,
                this._dbWriteNumeric(statement.getTransaction(), idEnfant, "idEnfant"));
        statement.writeField(SFEnfant.FIELD_DATEADOPTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateAdoption, "dateAdoption"));
        statement.writeField(SFEnfant.FIELD_IDMEMBREFAMILLE,
                this._dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
        statement.writeField(SFEnfant.FIELD_IDCONJOINT,
                this._dbWriteNumeric(statement.getTransaction(), idConjoint, "idConjoint"));
    }

    /**
     * Renvoie les parents d'un enfant ou null en cas d'erreur
     * 
     * @param transaction
     * @return
     */
    public SFConjoint getConjoints(BITransaction transaction) {
        SFConjoint conjoint = new SFConjoint();
        conjoint.setSession(getSession());
        conjoint.setIdConjoints(getIdConjoint());
        try {
            conjoint.retrieve(transaction);
        } catch (Exception e) {
            return null;
        }
        if (conjoint.isNew()) {
            return null;
        } else {
            return conjoint;
        }
    }

    /**
     * getter pour l'attribut date adoption
     * 
     * @return la valeur courante de l'attribut date adoption
     */
    public String getDateAdoption() {
        return dateAdoption;
    }

    /**
     * getter pour l'attribut id conjoint
     * 
     * @return la valeur courante de l'attribut id conjoint
     */
    public String getIdConjoint() {
        return idConjoint;
    }

    /**
     * getter pour l'attribut id enfant
     * 
     * @return la valeur courante de l'attribut id enfant
     */
    public String getIdEnfant() {
        return idEnfant;
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
     * setter pour l'attribut date adoption
     * 
     * @param dateAdoption
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateAdoption(String dateAdoption) {
        this.dateAdoption = dateAdoption;
    }

    /**
     * setter pour l'attribut id conjoint
     * 
     * @param idConjoint
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoint(String idConjoint) {
        this.idConjoint = idConjoint;
    }

    /**
     * setter pour l'attribut id enfant
     * 
     * @param idEnfant
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * setter pour l'attribut id membre famille
     * 
     * @param idMembreFamille
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

}
