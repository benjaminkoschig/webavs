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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class APBasesCalculProcheAidantBuilder extends APBasesCalculBuilder{

    public APBasesCalculProcheAidantBuilder(BSession session, APDroitLAPG droit) {
        super(session, droit);
    }

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    void ajoutDesCommandes() throws Exception {
        ajouterSituationProfessionnelle();
        ajouterTauxImposition();
        ajouterDateMinDebutParam(findDateDebutValidityProcheAidant());
        ajouterPeriodesPai();
    }

    private void ajouterPeriodesPai() throws Exception {
        List<APPeriodeComparable> listPeriode = getApPeriodeDroit(droit.getIdDroit());

        PeriodeTauxCourant tauxCourant = PeriodeTauxCourant.of(listPeriode.stream().findFirst().orElse(null));

        Long nbJourSoldes = listPeriode.stream().map(periode -> periode.nbJourSoldes()).reduce(0L, Long::sum);

        String dateDebut = listPeriode.stream().findFirst().get().getDateDebutPeriode();
        String dateFin = listPeriode.stream().reduce((first, second) -> second).get().getDateFinPeriode();
        ajouterEnfantPai(dateDebut, dateFin);

        calculNbJourSoldesMax(nbJourSoldes.intValue());
    }

    /**
     * ajouter les événements relatifs à l'enfant proche aidant à la
     * liste des commandes.
     */
    private void ajouterEnfantPai(String dateDebut, String dateFin) throws ParseException {
        // obtenir les date des débuts et de fin du droit
        Date debut = DF.parse(dateDebut);
        Calendar fin = getCalendarInstance();

        fin.setTime(DF.parse(dateFin));

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
