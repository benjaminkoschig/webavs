/*
 * Créé le 5 juil. 07
 */

package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.globall.db.BStatement;

/**
 * @author HPE
 * 
 */

// Il faudra afficher dans les rentes liées, les rentes PCAccordée, des
// prestations complémentaires.

public class RERenteLieeJointPrestationAccordee extends RERenteAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // informations comptabilités
    private String idCompteAnnexe = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += super._getFrom(statement);

        // jointure entre table des prestations accordées et informations
        // comptabilités
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;
        getFrom += " ON ";
        getFrom += FIELDNAME_ID_INFO_COMPTA;
        getFrom += "=";
        getFrom += REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA;

        return getFrom;
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
        super._readProperties(statement);

        // informations comptabilités
        idCompteAnnexe = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);

    }

    public String getCsTypeBasesCalcul() throws Exception {

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(getSession());
        bc.setIdBasesCalcul(getIdBaseCalcul());
        bc.retrieve();

        if (bc.isNew()) {
            return "";
        } else {
            return bc.getDroitApplique();
        }

    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdRenteCalculee() throws Exception {

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(getSession());
        bc.setIdBasesCalcul(getIdBaseCalcul());
        bc.retrieve();

        if (bc.isNew()) {
            return "";
        } else {
            return bc.getIdRenteCalculee();
        }

    }


    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

}
