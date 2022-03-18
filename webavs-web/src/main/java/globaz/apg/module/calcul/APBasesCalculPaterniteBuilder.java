package globaz.apg.module.calcul;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitPaternite;
import globaz.apg.db.droits.APEnfantMatManager;
import globaz.apg.db.droits.APPeriodeComparable;
import globaz.apg.properties.APParameter;
import globaz.apg.utils.APGDatesUtils;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.text.ParseException;
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

        nbJoursSoldes = 0;
        nbJoursSoldesAnneeSuivante = 0;
        boolean isNewYear = false;
        // pour chaque p�riode

        String dateDebut = listPeriode.get(0).getDateDebutPeriode();
        String dateFin = listPeriode.get(0).getDateFinPeriode();
        String currentCanton = listPeriode.get(0).getCantonImposition();
        String currentTaux = listPeriode.get(0).getTauxImposition();

        int jourMax = Integer.parseInt(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", APParameter.PATERNITE_JOUR_MAX.getParameterName(), "0", "", 0));

        for (APPeriodeComparable periode : listPeriode) {

            if(!APGDatesUtils.isMemeAnnee(periode.getDateDebutPeriode(), dateFin)) {
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

            Integer nbJourBetween =  PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
            if(!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                Integer nbJour = Integer.valueOf(periode.getNbrJours());
                if (nbJourBetween > nbJour) {
                    nbJourBetween = nbJour;
                }
            }
            addJour(nbJourBetween, jourMax, isNewYear);
        }
        ajouterSituationFamilialePat(mgr, dateDebut, dateFin);
        if (!JadeStringUtil.isBlankOrZero(currentCanton)){
            ajouterTauxImposition(currentTaux, dateDebut, dateFin, currentCanton);
        }

        ((APDroitPaternite) droit).setNbrJourSoldes(String.valueOf(nbJoursSoldes + nbJoursSoldesAnneeSuivante));
    }

    private void addJour(int nbJours, int jourMax, boolean isNewYear) {
        if(!isNewYear) {
            nbJoursSoldes += nbJours;
            if(nbJoursSoldes > jourMax) {
                nbJoursSoldes = jourMax;
            }
        } else {
            nbJoursSoldesAnneeSuivante += nbJours;
            if(nbJoursSoldesAnneeSuivante + nbJoursSoldes > jourMax) {
                nbJoursSoldesAnneeSuivante =  jourMax - nbJoursSoldes;
            }
        }
    }

    // ajouter les �v�nements relatifs � la situation familiale paternit� � la
    // liste des commandes.
    private void ajouterSituationFamilialePat(APEnfantMatManager mgr, String dateDebut, String dateFin) throws ParseException {
        // obtenir les date des d�buts et de fin du droit
        Date debut = DF.parse(dateDebut);
        Calendar fin = getCalendarInstance();

        fin.setTime(DF.parse(dateFin));

        // creer les commandes de d�but de droit et de find de droit �
        // l'allocation maternit�
        EnfantMatCommand commandDebut = new EnfantMatCommand(debut, true);
        EnfantMatCommand commandFin = new EnfantMatCommand(fin.getTime(), false);

        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            commands.add(commandDebut);
            commands.add(commandFin);
        }

        Calendar bCal = getCalendarInstance();
        // couper par ann�e si n�cessaire
        calendar.setTime(commandDebut.date);
        bCal.setTime(commandFin.date);

        for (int year = calendar.get(Calendar.YEAR); year < bCal.get(Calendar.YEAR); ++year) {
            calendar.set(year, Calendar.DECEMBER, 31);
            commands.add(new NouvelleBaseCommand(calendar.getTime(), false));
        }
    }
}
