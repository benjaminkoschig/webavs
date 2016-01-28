/*
 * Créé le 5 juil. 07
 */

package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;

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

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return true or false
     */
    public boolean isPreparationDecisionValide() {

        try {

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(getSession());
            bc.setIdBasesCalcul(getIdBaseCalcul());
            bc.retrieve();

            // Recherche des dates dt,dj,dpmt,ddeb
            REDemandeRente dem = new REDemandeRente();
            dem.setSession(getSession());
            dem.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            dem.setIdRenteCalculee(bc.getIdRenteCalculee());
            dem.retrieve();
            PRAssert.notIsNew(dem, "Entity not found");

            JACalendar cal = new JACalendarGregorian();
            JADate datePmtMensuel = null;

            if (!JadeStringUtil.isBlankOrZero(REPmtMensuel.getDateDernierPmt(getSession()))) {
                datePmtMensuel = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(REPmtMensuel
                        .getDateDernierPmt(getSession())));
            }

            JADate dateDebutDroit = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateDebut()));
            JADate dateTraitement = new JADate(
                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateTraitement()));
            JADate dateJour = JACalendar.today();
            dateJour.setDay(1);

            // Si (dt=dj et dt=dpmt) ou (dt<dj et dt<dpmt et ddeb > dpmt) la
            // préparation de la décision peut s'effectuer
            if (datePmtMensuel != null) {
                return ((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_FIRSTLOWER
                        && cal.compare(dateTraitement, datePmtMensuel) == JACalendar.COMPARE_FIRSTLOWER && cal.compare(
                        dateDebutDroit, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER) ||

                (cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_EQUALS && cal.compare(dateTraitement,
                        datePmtMensuel) == JACalendar.COMPARE_EQUALS));
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

}
