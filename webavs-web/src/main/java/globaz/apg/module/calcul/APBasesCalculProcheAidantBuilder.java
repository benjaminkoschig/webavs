package globaz.apg.module.calcul;

import ch.globaz.common.domaine.Periode;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.Dates;
import globaz.apg.db.droits.*;
import globaz.apg.properties.APParameter;
import globaz.apg.properties.APProperties;
import globaz.globall.db.BManager;
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
import java.util.Optional;

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

        int nbJourDisponible = ((APDroitProcheAidant) droit).calculerNbJourDisponible();

        calculNbJourSoldesMax(nbJourSoldes.intValue(), nbJourDisponible);
    }

    /**
     * ajouter les événements relatifs à l'enfant proche aidant à la
     * liste des commandes.
     */
    private void ajouterEnfantPai(String dateDebut, String dateFin) throws Exception {

        // ajouter les enfants aux commandes
        APEnfantMatManager mgr = new APEnfantMatManager();

        mgr.setSession(session);
        mgr.setForIdDroitMaternite(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction(), BManager.SIZE_USEDEFAULT);

        if(mgr.size() > 0) {
            // controle l'age legale
            Optional<Periode> periode = controleAgeLegal(mgr.<APEnfantMat>getContainerAsList().get(0), this.droit.getDateDebutDroit(), dateFin);

            if(periode.isPresent()) {
                // creer les commandes de début de droit et de find de droit à
                // l'allocation proche aidant
                EnfantMatCommand commandDebut = new EnfantMatCommand(Dates.toJavaDate(Dates.toDate(periode.get().getDateDebut())), true);
                EnfantMatCommand commandFin = new EnfantMatCommand(Dates.toJavaDate(Dates.toDate(periode.get().getDateFin())), false);

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
        }
    }

    public String findDateDebutValidityProcheAidant(){
        LocalDate date = APParameter.PROCHE_AIDANT_DATE_DE_DEBUT.findDateDebutValidite(ch.globaz.common.domaine.Date.now().getSwissValue(),session);
        return Dates.formatSwiss(date);
    }

    private void calculNbJourSoldesMax(int nbJourSoldes, int nbJourDisponible) {
        if(nbJourSoldes > nbJourDisponible) {
            nbJourSoldes = nbJourDisponible;
        }

        ((APDroitProcheAidant) droit).setNbrJourSoldes(String.valueOf(nbJourSoldes));
        nbJoursSoldes = nbJourSoldes;
    }

    private Optional<Periode> controleAgeLegal(APEnfantMat enfant, String dateDebut, String dateFin) throws PropertiesException {
        LocalDate dateNaissance = Dates.toDate(enfant.getDateNaissance());
        LocalDate datelegale = dateNaissance.plusYears(Integer.parseInt(APProperties.PROCHE_AIDANT_AGE_LEGAL.getValue()));
        LocalDate controleDate = Dates.toDate(dateDebut);
        if(controleDate.isAfter(datelegale) || controleDate.isEqual(datelegale)) {
            return Optional.empty();
        }
        return Optional.of(new Periode(dateDebut, dateFin));
    }

}
