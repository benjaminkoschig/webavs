package globaz.ij.helpers.acor;

import ch.globaz.common.util.Dates;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.ij.acor.adapter.IJAttestationsJoursAdapter;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.*;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.tools.PRDateFormater;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculateur des prestations de type FPI
 *
 * @author ebko
 */

public class IJCalculFpiStandard implements IIJCalculStandard {

    public List calculPrestationsSansAcor(BSession session, BTransaction transaction, IJPrononce prononce,
                                          IJBaseIndemnisation baseIndemnisation, IJIJCalculee ijCalculee) throws Exception {

        IJPrestation prestation = new IJPrestation();
        prestation.setIdIJCalculee(ijCalculee.getIdIJCalculee());
        prestation.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());

        calculDatesPrestation(baseIndemnisation, ijCalculee, prestation);
        try {
            calculMontantsPrestation(baseIndemnisation, (IJFpiCalculee) ijCalculee, prestation);
        } catch (IJCalculException exception) {
            throw new IJCalculException(session.getLabel(exception.getKeyMsg()));
        }

        prestation.setSession(session);
        prestation.add(transaction);

        List retValue = new ArrayList();
        retValue.add(prestation);
        return retValue;

    }

    private static void calculDatesPrestation(IJBaseIndemnisation baseIndemnisation, IJIJCalculee ijCalculee, IJPrestation prestation) throws JAException {
        JADate dateDebutPrestation = new JADate(baseIndemnisation.getDateDebutPeriode());
        JADate dateFinPrestation = new JADate(baseIndemnisation.getDateFinPeriode());

        JADate dateDebutIJCalculee = new JADate(ijCalculee.getDateDebutDroit());
        JADate dateFinIJCalculee = null;

        if (!JadeStringUtil.isBlankOrZero(ijCalculee.getDateFinDroit())) {
            dateFinIJCalculee = new JADate(ijCalculee.getDateFinDroit());
        }

        JACalendar cal = new JACalendarGregorian();

        // On prend la plus grande date de début entre la base d'indemnisation
        // et de l'ijCalculee
        if (cal.compare(dateDebutPrestation, dateDebutIJCalculee) == JACalendar.COMPARE_FIRSTLOWER) {
            dateDebutPrestation = dateDebutIJCalculee;
        }

        // On prend la plus petite date de fin entre la base d'indemnisation et
        // de l'ijCalculee
        if (dateFinIJCalculee != null && cal.compare(dateFinPrestation, dateFinIJCalculee) == JACalendar.COMPARE_FIRSTUPPER) {
            dateFinPrestation = dateFinIJCalculee;
        }

        prestation.setDateDebut(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDebutPrestation.toStrAMJ()));
        prestation.setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateFinPrestation.toStrAMJ()));
        prestation.setDateDecompte(JACalendar.todayJJsMMsAAAA());
    }

    protected static void calculMontantsPrestation(IJBaseIndemnisation baseIndemnisation, IJFpiCalculee fpiCalculee, IJPrestation prestation) throws IJCalculException{
        Integer jourMaxFpi = Integer.valueOf(IIJPrestation.JOUR_FPI);
        String jnc = baseIndemnisation.getNombreJoursNonCouverts();
        Double rjm = Double.valueOf(fpiCalculee.getMontantBase());
        Double rjme = Double.valueOf(fpiCalculee.getMontantEnfants());
        Double sal = Double.valueOf(fpiCalculee.getSalaireMensuel());

        int jnct = calculerJoursNonCompris(jnc, baseIndemnisation);

        // total non couvert
        Double tnc = rjm * jnct;
        // Montant de la prestation pour la période
        Double mpr = sal.doubleValue() - tnc;
        // jours à payer
        Integer prj = jourMaxFpi - jnct;
        // Total non couvert
        Double tnce = rjme * prj;
        // montant journalier
        FWCurrency prmj = new FWCurrency(rjm + rjme);
        // montant brut
        FWCurrency prmb = new FWCurrency(mpr + tnce);

        if(prmb.isZero() || prmb.isNegative() || prj <= 0) {
            throw new IJCalculException("Aucune prestation ne peut être calculée pour cette période", "CALCUL_IJ_AUCUNE_PRESTATION");
        }

        prestation.setNombreJoursExt(prj.toString());
        prestation.setMontantBrut(prmb.toString());
        prestation.setMontantJournalierExterne(prmj.toString());
    }

    private static int calculerJoursNonCompris(String JoursNonComprisSaisie, IJBaseIndemnisation baseIndemnisation) {

        int jourNonCompris = 0;
        if(!JadeStringUtil.isBlankOrZero(JoursNonComprisSaisie)) {
            jourNonCompris +=  Integer.valueOf(JoursNonComprisSaisie);
        }
        int jourFPI = Integer.valueOf(IIJPrestation.JOUR_FPI);

        LocalDate debut = Dates.toDate(baseIndemnisation.getDateDebutPeriode());
        LocalDate fin = Dates.toDate(baseIndemnisation.getDateFinPeriode());
        LocalDate debutMois = debut.withDayOfMonth(1);
        LocalDate finMois = fin.withDayOfMonth(fin.lengthOfMonth());

        if(debut.isAfter(debutMois)) {
            // ajouter les jours entre le début du mois et le début de la période
            jourNonCompris += debut.getDayOfMonth() - 1;
        }

        if(fin.isBefore(finMois)) {
            // ajouter les jours entre la fin de la période et la fin du mois
            // considérer qu'un mois contient toujours 30 jours
            jourNonCompris += jourFPI - fin.getDayOfMonth();

        } else if(jourFPI > finMois.getDayOfMonth()
                && fin.getDayOfMonth() != finMois.getDayOfMonth()) {
            // ajouter des jours non compris pour le mois de février si pas un mois complet
            jourNonCompris += jourFPI - finMois.getDayOfMonth();
        }

        if(jourNonCompris > jourFPI) {
            // Max de 30 jours
            return jourFPI;
        }

        return jourNonCompris;
    }
}
