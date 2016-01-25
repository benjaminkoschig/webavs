package globaz.corvus.utils;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;

public class REDiminutionRenteUtils {

    public static void majDemandePourDiminution(BSession session, BTransaction transaction, RERenteAccordee raDiminuee)
            throws Exception {

        // On récupère la demande de la rente accordée
        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(raDiminuee.getIdBaseCalcul());
        bc.retrieve(transaction);

        RERenteCalculee rc = new RERenteCalculee();
        rc.setSession(session);
        rc.setIdRenteCalculee(bc.getIdRenteCalculee());
        rc.retrieve(transaction);
        PRAssert.notIsNew(rc, null);

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.setIdRenteCalculee(rc.getIdRenteCalculee());
        demande.retrieve(transaction);

        PRAssert.notIsNew(demande, null);

        String dateDiminution = REDiminutionRenteUtils.getDateDiminutionDemande(session, transaction, demande);
        demande.setDateFin(dateDiminution);
        demande.update(transaction);
    }

    public static String getDateDiminutionDemande(BSession session, BTransaction transaction, REDemandeRente demande)
            throws Exception {

        // Est-ce que toutes les RA de cette demande sont diminuées ?
        RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
        mgr.setForNoDemandeRente(demande.getIdDemandeRente());
        mgr.setSession(session);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        JADate plusGrandeDateDiminution = new JADate("01.01.1900");
        JACalendar cal = new JACalendarGregorian();
        for (int i = 0; i < mgr.size(); i++) {
            RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) mgr.getEntity(i);

            if (JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
                return null;
            }
            // if
            // (!IREPrestationAccordee.CS_ETAT_DIMINUE.equals(elm.getCsEtat()))
            // {
            // return null;
            // }

            else {
                if (JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
                    throw new Exception(session.getLabel("ERREUR_DATE_FIN_DROIT_RA") + elm.getIdPrestationAccordee());
                }
                JADate date = new JADate(elm.getDateFinDroit());
                if (cal.compare(date, plusGrandeDateDiminution) == JACalendar.COMPARE_FIRSTUPPER) {
                    plusGrandeDateDiminution = new JADate(date.toStr("."));
                }
            }
        }

        int dayInMonth = cal.daysInMonth(plusGrandeDateDiminution.getMonth(), plusGrandeDateDiminution.getYear());

        // Si on arrive ici, toute les RA ont été diminuées...
        return String.valueOf(dayInMonth) + "."
                + PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(plusGrandeDateDiminution.toStrAMJ());
    }

    /**
     * Test si la rente accordée possède un supplément d'ajournement
     * 
     * @param renteAccordee La rente accordée à tester
     * @return <code>true</code> Si la rente n'est pas <code>null</code> et qu'elle possède un supplément
     *         d'ajournement
     */
    public static boolean hasMontantAjournement(RERenteAccordee renteAccordee) {
        if (renteAccordee != null) {
            if (!isMontantEmpty(renteAccordee.getSupplementAjournement())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test si la rente accordée possède un montant d'anticipation
     * 
     * @param renteAccordee La rente accordée à tester
     * @return <code>true</code> Si la rente n'est pas <code>null</code> et qu'elle possède un montant d'anticipation
     */
    public static boolean hasMontantAnticipation(RERenteAccordee renteAccordee) {
        if (renteAccordee != null) {
            if (!isMontantEmpty(renteAccordee.getMontantReducationAnticipation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test si un montant est vide ou égal à 0 ou 0.00
     * 
     * @param montant La valeur à tester sous forme de string
     * @return True si la valeur du montant est <code>null</code>, une chaîne vide, égal à 0 ou égal à 0.00.
     */
    private static boolean isMontantEmpty(String montant) {
        if (JadeStringUtil.isBlankOrZero(montant)) {
            return true;
        }
        FWCurrency currency = new FWCurrency(montant);
        return currency.isZero();
    }

}
