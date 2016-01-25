/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.demandes;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author hpe
 * 
 *         viewBean utilisé pour l'affichage du récapitulatif de demandes de rentes... pour l'instant, seules certaines
 *         infos sont affichées en attendant des précisions sur l'écran !!
 * 
 */

public class RERecapitulatifDemandeRenteViewBean extends REDemandeRenteJointDemande implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return
     */
    public String getSexe() {
        return getSession().getCodeLibelle(getCsSexe());
    }

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return true or false
     */
    public boolean isPreparationDecisionValide() {

        try {

            // Recherche des dates dt,dj,dpmt,ddeb
            REDemandeRente dem = new REDemandeRente();
            dem.setSession(getSession());
            dem.setIdDemandeRente(getIdDemandeRente());
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

}
