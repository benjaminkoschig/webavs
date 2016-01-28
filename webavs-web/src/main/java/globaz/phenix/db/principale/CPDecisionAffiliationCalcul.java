package globaz.phenix.db.principale;

import globaz.globall.util.JANumberFormatter;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CPDecisionAffiliationCalcul extends CPDecisionAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDonneesCalcul = "";
    private String montant = "";

    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {

        // String fields = super._getFields(statement);
        String fields = "";
        fields += _getCollection() + "CPDECIP.IAIDEC IAIDEC," + _getCollection() + "CPDECIP.HTITIE HTITIE,"
                + _getCollection() + "CPDECIP.MAIAFF MAIAFF," + _getCollection() + "AFAFFIP.MALNAF MALNAF,"
                + _getCollection() + "AFAFFIP.MADDEB MADDEB," + _getCollection() + "AFAFFIP.MADFIN MADFIN,"
                + _getCollection() + "CPDECIP.EBIPAS EBIPAS," + _getCollection() + "CPDECIP.IATTDE IATTDE,"
                + _getCollection() + "CPDECIP.IATGAF IATGAF," + _getCollection() + "CPDECIP.IAANNE IAANNE,"
                + _getCollection() + "CPDECIP.IADDEB IADDEB," + _getCollection() + "CPDECIP.IADFIN IADFIN,"
                + _getCollection() + "CPDECIP.IAACTI IAACTI," + _getCollection() + "CPDECIP.IABIMP IATETA, "
                + _getCollection() + "CPDOCAP.IHMDCA IHMDCA, " + _getCollection() + "CPDOCAP.IHIDCA IHIDCA ";
        return fields;

    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String calcul = _getCollection() + "CPDOCAP";
        String from = super._getFrom(statement);
        from += " INNER JOIN " + calcul + " ON (" + calcul + ".IAIDEC=" + _getCollection() + "CPDECIP.IAIDEC)";
        return from;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        montant = statement.dbReadNumeric("IHMDCA");
        idDonneesCalcul = statement.dbReadNumeric("IHIDCA");
    }

    /**
     * @return
     */
    public String getIdDonneesCalcul() {
        return idDonneesCalcul;
    }

    /**
     * @return
     */
    public String getMontant() {
        if ((CPDonneesCalcul.CS_TAUX_COTISATION.equalsIgnoreCase(idDonneesCalcul))
                || (CPDonneesCalcul.CS_TAUX_INTCAP.equalsIgnoreCase(idDonneesCalcul))) {
            return JANumberFormatter.fmt(montant, true, true, true, 5);
        } else if (CPDonneesCalcul.CS_COTISATION_BRUT.equalsIgnoreCase(idDonneesCalcul)
                || CPDonneesCalcul.CS_INTERET_MORATOIRE.equalsIgnoreCase(idDonneesCalcul)) {
            return JANumberFormatter.fmt(montant, true, true, true, 2);
        } else {
            return JANumberFormatter.fmt(montant, true, false, true, 0);
        }
    }

    /**
     * @param string
     */
    public void setIdDonneesCalcul(String string) {
        idDonneesCalcul = string;
    }

    /**
     * @param string
     */
    public void setMontant(String newMontant) {
        montant = JANumberFormatter.deQuote(newMontant);
    }

}
