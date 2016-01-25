/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * N'est utilisee que pour la generation de la liste de l'etat des rentes
 * 
 * @author BSC
 * 
 */
public class REEtatRentes extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // champs cree lors de la requete
    public static final String FIELDNAME_MONTANT_TOTAL_FOR_CODE = "MONTANT";
    public static final String FIELDNAME_NB_FOR_CODE = "NB";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codePrestation = "";
    private String fractionRente = "";
    private String montantPrestation = "";

    // champs cree lors de la requete
    private String montantTotalForCode = "";
    private String nbForCode = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        fractionRente = statement.dbReadString(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);

        // champs cree lors de la requete
        montantTotalForCode = statement.dbReadNumeric(FIELDNAME_MONTANT_TOTAL_FOR_CODE);
        nbForCode = statement.dbReadNumeric(FIELDNAME_NB_FOR_CODE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // PAS UTILISE
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        // PAS UTILISE
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // PAS UTILISE
    }

    /**
     * @return
     */
    public String getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return
     */
    public String getFractionRente() {
        return fractionRente;
    }

    /**
     * @return
     */
    public String getMontantPrestation() {
        return montantPrestation;
    }

    /**
     * @return
     */
    public String getMontantTotalForCode() {
        return montantTotalForCode;
    }

    /**
     * @return
     */
    public String getNbForCode() {
        return nbForCode;
    }
}
