package globaz.ij.helpers.acor;

import ch.globaz.common.util.Dates;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
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
 * @author ebko
 */

public class IJCalculFpiStandard implements IIJCalculStandard {

    public List calculPrestationsSansAcor(BSession session, BTransaction transaction, IJPrononce prononce,
                                          IJBaseIndemnisation baseIndemnisation, IJIJCalculee ijCalculee) throws Exception {

        IJFpiCalculee fpiCalculee = (IJFpiCalculee) ijCalculee;
        String jnc = baseIndemnisation.getNombreJoursCouverts();
        String rjm = fpiCalculee.getMontantBase();
        String rjme = fpiCalculee.getMontantEnfants();
        String sal = fpiCalculee.getSalaireMensuel();

        int jourNonCompris =  calculerJoursNonCompris(jnc, baseIndemnisation);





        List retValue = new ArrayList();

        String indemniteExt = "0.0";
        String indemniteInt = "0.0";
        String montantBrutExt = "0.0";
        String montantBrutInt = "0.0";

        // creation de la prestation, d'apres le schema, il y a au maximum UN
        // element paiement
        IJPrestation prestation = new IJPrestation();

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
        if (dateFinIJCalculee == null) {
            ;
        } else if (cal.compare(dateFinPrestation, dateFinIJCalculee) == JACalendar.COMPARE_FIRSTUPPER) {
            dateFinPrestation = dateFinIJCalculee;
        }

        prestation.setDateDebut(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDebutPrestation.toStrAMJ()));
        prestation.setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateFinPrestation.toStrAMJ()));
        prestation.setIdIJCalculee(ijCalculee.getIdIJCalculee());
        prestation.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());

        IJIndemniteJournaliereManager mgr = new IJIndemniteJournaliereManager();
        mgr.setSession(session);
        mgr.setForIdIJCalculee(ijCalculee.getIdIJCalculee());
        mgr.find(transaction);
        for (int i = 0; i < mgr.size(); i++) {
            IJIndemniteJournaliere elm = (IJIndemniteJournaliere) mgr.getEntity(i);
            if (IIJMesure.CS_INTERNE.equals(elm.getCsTypeIndemnisation())) {
                indemniteInt = elm.getMontantJournalierIndemnite();
            } else {
                indemniteExt = elm.getMontantJournalierIndemnite();
            }
        }

        if ((indemniteExt == null) && (indemniteInt == null)) {
            throw new PRACORException(session.getLabel("AUCUNE_IJ_CALCULEE"));
        }

        IJAttestationsJoursAdapter attestationsJours = new IJAttestationsJoursAdapter(baseIndemnisation, ijCalculee);

        prestation.setMontantBrutExterne(IJCalculACORDecompteHelper.multiply(indemniteExt,
                attestationsJours.getNbJoursExternes()));
        prestation.setMontantBrutInterne(IJCalculACORDecompteHelper.multiply(indemniteInt,
                attestationsJours.getNbJoursInternes()));
        prestation.setNombreJoursExt(attestationsJours.getNbJoursExternes());
        prestation.setNombreJoursInt(attestationsJours.getNbJoursInternes());
        prestation.setDateDecompte(JACalendar.todayJJsMMsAAAA());
        FWCurrency mbr = new FWCurrency(prestation.getMontantBrutInterne());
        mbr.add(prestation.getMontantBrutExterne());
        prestation.setMontantBrut(mbr.toString());

        // on recopie les montants journalier
        prestation.setMontantJournalierExterne(indemniteExt);
        prestation.setMontantJournalierInterne(indemniteInt);

        // sauver la sous prestation dans la base
        prestation.setSession(session);
        prestation.add(transaction);

        retValue.add(prestation);
        return retValue;

    }

    private int calculerJoursNonCompris(String JoursNonComprisSaisie, IJBaseIndemnisation baseIndemnisation) {

        int jourNonCompris = 0;
        if(JadeStringUtil.isBlankOrZero(JoursNonComprisSaisie)) {
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

//            if(jourFPI > finMois.getDayOfMonth()) {
//                // Le mois de février a moins de 30 jours donc prendre 30 jours
//                jourNonCompris += jourFPI - fin.getDayOfMonth();
//            } else if(jourFPI < finMois.getDayOfMonth()) {
//                // mois à 31 jours
//
//            } else {
//                jourNonCompris += finMois.getDayOfMonth() - fin.getDayOfMonth();
//            }
        }

        if(jourNonCompris > jourFPI) {
            // Max de 30 jours
            return jourFPI;
        }

        return jourNonCompris;
    }
}
