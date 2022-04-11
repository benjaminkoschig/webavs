package globaz.apg.module.calcul;

import ch.globaz.common.util.Dates;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitPaternite;
import globaz.apg.db.droits.APEnfantMatManager;
import globaz.apg.db.droits.APPeriodeComparable;
import globaz.apg.properties.APParameter;
import globaz.apg.utils.APGDatesUtils;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class APBasesCalculPaterniteBuilder extends APBasesCalculBuilder{

    public APBasesCalculPaterniteBuilder(BSession session, APDroitLAPG droit) {
        super(session, droit);
    }

    void ajoutDesCommandes() throws Exception {
        ajouterSituationProfessionnelle();
        ajouterDateMinDebutParam(APParameter.PATERNITE.getParameterName(), "");
        ajouterSituationFamilialePat();
    }

    private void ajouterSituationFamilialePat() throws Exception {
        List<APPeriodeComparable> listPeriode = getApPeriodeDroit(droit.getIdDroit());

        // ajouter les enfants aux commandes
        APEnfantMatManager mgr = new APEnfantMatManager();

        mgr.setSession(session);
        mgr.setForIdDroitMaternite(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction(), BManager.SIZE_USEDEFAULT);

        nbJoursConges = 0;
        nbJoursSupp = 0;
        nbJoursSoldes = 0;
        nbJoursSuppAnneeSuivante = 0;
        nbJoursSoldesAnneeSuivante = 0;
        boolean isNewYear = false;
        // pour chaque période

        String dateDebut = listPeriode.get(0).getDateDebutPeriode();
        String dateFin = listPeriode.get(0).getDateFinPeriode();
        String currentCanton = listPeriode.get(0).getCantonImposition();
        String currentTaux = listPeriode.get(0).getTauxImposition();

        int jourMax = Integer.parseInt(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", APParameter.PATERNITE_JOUR_MAX.getParameterName(), "0", "", 0));


        for (APPeriodeComparable periode : listPeriode) {

            if(!APGDatesUtils.isMemeAnnee(periode.getDateDebutPeriode(), dateFin)) {
                dateFin = calculeFinAvecJourSupplementaire(dateDebut, dateFin);
                ajouterSituationFamilialePat(mgr, dateDebut, dateFin);
                dateDebut = periode.getDateDebutPeriode();
                dateFin = periode.getDateFinPeriode();
                currentCanton = periode.getCantonImposition();
                currentTaux = periode.getTauxImposition();
                isNewYear = true;
            }

            if(changeImposition(periode, currentCanton, currentTaux)){
                ajouterSituationFamilialePat(mgr, dateDebut, dateFin);
                if (!JadeStringUtil.isBlankOrZero(currentCanton)) {
                    ajouterTauxImposition(currentTaux, dateDebut, dateFin, currentCanton);
                }
                dateDebut = periode.getDateDebutPeriode();
                dateFin = periode.getDateFinPeriode();
                currentCanton = periode.getCantonImposition();
                currentTaux = periode.getTauxImposition();
            }

            if(JadeDateUtil.isDateBefore(periode.getDateDebutPeriode(), dateDebut)) {
                dateDebut = periode.getDateDebutPeriode();
            }
            if(JadeDateUtil.isDateAfter(periode.getDateFinPeriode(), dateFin)) {
                dateFin = periode.getDateFinPeriode();
            }

            Integer nbJours =  PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
            if(!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                Integer nbJoursPeriode = Integer.valueOf(periode.getNbrJours());
                if (nbJours > nbJoursPeriode) {
                    nbJours = nbJoursPeriode;
                }
            }
            addJour(nbJours, jourMax, Integer.valueOf(periode.getNbJourSupplementaire()), isNewYear);
        }

        if(dateFinSaisie == null) {
            dateFinSaisie = new JADate(dateFin);
        } else {
            dateFinSaisieAnneeSuivante = new JADate(dateFin);
        }

        nbJoursSoldes += nbJoursSupp;
        nbJoursSoldesAnneeSuivante += nbJoursSuppAnneeSuivante;

        dateFin =  controleDateFinSelonDateFinSaisie(dateDebut, dateFin);

        ajouterSituationFamilialePat(mgr, dateDebut, dateFin);
        if (!JadeStringUtil.isBlankOrZero(currentCanton)){
            ajouterTauxImposition(currentTaux, dateDebut, dateFin, currentCanton);
        }

        ((APDroitPaternite) droit).setNbrJourSoldes(String.valueOf(nbJoursSoldes + nbJoursSoldesAnneeSuivante));
    }

    private String calculeFinAvecJourSupplementaire(String dateDebut, String dateFin) throws JAException {
        LocalDate lDateDebut = Dates.toDate(dateDebut);
        LocalDate lDateFin = Dates.toDate(dateFin);
        LocalDate lDateFinAnnee = lDateFin.with(TemporalAdjusters.lastDayOfYear());
        long nbJoursTotalPeriode = Dates.daysBetween(lDateDebut, lDateFin);
        long nbJoursDispoEntreDateDebutDateFin =  nbJoursTotalPeriode - nbJoursSoldes;
        long nbJoursSupAajouterEnFin = nbJoursSupp - nbJoursDispoEntreDateDebutDateFin;
        if(nbJoursSupAajouterEnFin < 0) {
            // il y a assez de jours pour les jours supplémentaires sans toucher la date de fin
            return dateFin;
        }
        long nbJoursRestants = Dates.daysBetween(lDateFin, lDateFinAnnee);
        if(nbJoursSupAajouterEnFin <= nbJoursRestants) {
            // il reste assez de jours dispos dans la période pour les jours supplémentaires de cette année :
            // ajustement de la date de fin
            dateFinSaisie = new JADate(dateFin);
            return JadeDateUtil.addDays(dateFin, (int) nbJoursSupAajouterEnFin);
        }
        // report de jours supplémentaires sur l'année suivante
        nbJoursSuppAnneeSuivante += nbJoursSupp - nbJoursSupAajouterEnFin;
        nbJoursSupp = (int) nbJoursSupAajouterEnFin;
        return Dates.formatSwiss(lDateFinAnnee);
    }

    private String controleDateFinSelonDateFinSaisie(String dateDebut, String dateFin) throws JAException {
        if (nbJoursSoldesAnneeSuivante != 0 && nbJoursSoldesAnneeSuivante > Dates.daysBetween(dateDebut, dateFin)) {
            // si il y a 2 années : control qu'il y a suffisamment de jour pour l'année n + 1
            dateFinSaisieAnneeSuivante = new JADate(dateFin);
            return JadeDateUtil.addDays(dateFin, nbJoursSoldesAnneeSuivante - 1);
        // Sinon on prend la date de fin calculee si elle est plus grande que la date de fin de la période
        } else if(JadeDateUtil.isDateAfter((((APDroitPaternite) droit).getDateFinDroitCalculee()), dateFin)) {
            return (((APDroitPaternite) droit).getDateFinDroitCalculee());
        }
        return dateFin;
    }

    private void addJour(int nbJours, int jourMax, int jourSup, boolean isNewYear) {
        if(!isNewYear) {
            nbJoursSoldes += nbJours;
            if(nbJoursSoldes > jourMax) {
                nbJoursSoldes = jourMax;
            }
            // On somme les jours supplémentaires pour toutes les périodes
            nbJoursSupp += jourSup;
        } else {
            nbJoursSoldesAnneeSuivante += nbJours;
            if(nbJoursSoldesAnneeSuivante + nbJoursSoldes > jourMax) {
                nbJoursSoldesAnneeSuivante =  jourMax - nbJoursSoldes;
            }
            nbJoursSuppAnneeSuivante += jourSup;
        }
    }

    // ajouter les événements relatifs à la situation familiale paternité à la
    // liste des commandes.
    private void ajouterSituationFamilialePat(APEnfantMatManager mgr, String dateDebut, String dateFin) throws ParseException {
        // obtenir les date des débuts et de fin du droit
        Date debut = DF.parse(dateDebut);
        Calendar fin = getCalendarInstance();

        fin.setTime(DF.parse(dateFin));

        // creer les commandes de début de droit et de find de droit à
        // l'allocation maternité
        EnfantMatCommand commandDebut = new EnfantMatCommand(debut, true);
        EnfantMatCommand commandFin = new EnfantMatCommand(fin.getTime(), false);

        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            commands.add(commandDebut);
            commands.add(commandFin);
        }

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

