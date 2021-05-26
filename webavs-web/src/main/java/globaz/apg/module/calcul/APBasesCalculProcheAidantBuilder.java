package globaz.apg.module.calcul;

import ch.globaz.common.util.Dates;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.db.droits.APPeriodeComparable;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class APBasesCalculProcheAidantBuilder extends APBasesCalculBuilder{

    public APBasesCalculProcheAidantBuilder(BSession session, APDroitLAPG droit) {
        super(session, droit);
    }

    void ajoutDesCommandes() throws Exception {
        ajouterSituationProfessionnelle();
        ajouterDateMinDebutParam(findDateDebutValidityProcheAidant());
        ajouterPeriodesPai();
    }

    private void ajouterPeriodesPai() throws Exception {
        List<APPeriodeComparable> listPeriode = getApPeriodeDroit(droit.getIdDroit());

        int nbJourSoldes = 0;
        int nbJourSupplementaire = 0;

        PeriodeTauxCourant tauxCourant = PeriodeTauxCourant.of(listPeriode.stream().findFirst().orElse(null));

        // pour chaque période
        for (APPeriodeComparable periode : listPeriode) {

            if(changeImposition(periode, tauxCourant)){
                tauxCourant = getPeriodeTauxCourant(tauxCourant, periode);
            }

            if(JadeDateUtil.isDateBefore(periode.getDateDebutPeriode(), tauxCourant.dateDebut)) {
                tauxCourant = tauxCourant.updateDateDebut(periode.getDateDebutPeriode());
            }
            if(JadeDateUtil.isDateAfter(periode.getDateFinPeriode(), tauxCourant.dateFin)) {
                tauxCourant = tauxCourant.updateDateFin(periode.getDateFinPeriode());
            }

            Integer nbJourPeriodeCourante =  PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
            if(!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                Integer nbJourPeriodeSaisie = Integer.valueOf(periode.getNbrJours());
                if (nbJourPeriodeCourante > nbJourPeriodeSaisie) {
                    nbJourPeriodeCourante = nbJourPeriodeSaisie;
                }
            }

            nbJourSoldes += nbJourPeriodeCourante;
            nbJourSupplementaire += Integer.parseInt(periode.getNbJourSupplementaire());
        }
        ajouterEnfantPai(tauxCourant);
        if (!JadeStringUtil.isBlankOrZero(tauxCourant.canton)){
            ajouterTauxImposition(tauxCourant);
        }

        nbJourSoldes += nbJourSupplementaire;

        calculNbJourSoldesMax(nbJourSoldes);
    }

    private PeriodeTauxCourant getPeriodeTauxCourant(PeriodeTauxCourant tauxCourant, APPeriodeComparable periode) throws Exception {
        ajouterEnfantPai(tauxCourant);
        if (!JadeStringUtil.isBlankOrZero(tauxCourant.canton)) {
            ajouterTauxImposition(tauxCourant);
        }
        return PeriodeTauxCourant.of(periode);
    }

    /**
     * ajouter les événements relatifs à l'enfant proche aidant à la
     * liste des commandes.
     */
    private void ajouterEnfantPai(PeriodeTauxCourant periode) throws ParseException {
        // obtenir les date des débuts et de fin du droit
        Date debut = DF.parse(periode.dateDebut);
        Calendar fin = getCalendarInstance();

        fin.setTime(DF.parse(periode.dateFin));

        // creer les commandes de début de droit et de find de droit à
        // l'allocation proche aidant
        EnfantMatCommand commandDebut = new EnfantMatCommand(debut, true);
        EnfantMatCommand commandFin = new EnfantMatCommand(fin.getTime(), false);

        // 1 seul enfant par droit
        commands.add(commandDebut);
        commands.add(commandFin);

        Calendar bCal = getCalendarInstance();
        // couper par année si nécessaire
        calendar.setTime(commandDebut.date);
        bCal.setTime(commandFin.date);

        for (int year = calendar.get(Calendar.YEAR); year < bCal.get(Calendar.YEAR); ++year) {
            calendar.set(year, Calendar.DECEMBER, 31);
            commands.add(new NouvelleBaseCommand(calendar.getTime(), false));
        }
    }

    public String findDateDebutValidityProcheAidant(){
        LocalDate date = APParameter.PROCHE_AIDANT_DATE_DE_DEBUT.findDateDebutValidite(ch.globaz.common.domaine.Date.now().getSwissValue(),session);
        return Dates.formatSwiss(date);
    }

    protected void calculNbJourSoldesMax(int nbJourSoldes) {
        int jourMax = APParameter.PROCHE_AIDANT_JOUR_MAX.findValue(this.droit.getDateDebutDroit(), this.session);

        if(nbJourSoldes > jourMax) {
            nbJourSoldes = jourMax;
        }

        ((APDroitProcheAidant) droit).setNbrJourSoldes(String.valueOf(nbJourSoldes));
        nbJoursSoldes = nbJourSoldes;
    }


}
