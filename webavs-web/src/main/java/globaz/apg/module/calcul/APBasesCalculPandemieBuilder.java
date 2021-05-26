package globaz.apg.module.calcul;

import globaz.apg.ApgServiceLocator;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitPanSituation;
import globaz.apg.db.droits.APDroitPandemie;
import globaz.apg.db.droits.APPeriodeComparable;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class APBasesCalculPandemieBuilder extends APBasesCalculBuilder{

    public APBasesCalculPandemieBuilder(BSession session, APDroitLAPG droit) {
        super(session, droit);
    }

    void ajoutDesCommandes() throws Exception {
        boolean isIndependant = ajouterSituationProfessionnelle();
        ajouterTauxImposition();
        String dateDebut = ajouterDateDebutFromParam();
        int jourAutrePresta = ApgServiceLocator.getEntityService().getTotalJourAutreDroit(session, droit.getIdDroit());
        nbJoursSoldes = ajouterPeriodesPan(dateDebut, jourAutrePresta, isIndependant);
        ((APDroitPandemie) droit).setNbrJourSoldes(String.valueOf(nbJoursSoldes));
    }

    /**
     * Ajoute la date de début des versements selon le genre de service
     * @return Date de début du genre de service
     * @throws Exception
     */
    private String ajouterDateDebutFromParam() throws Exception {
        String valeur = "";
        String dateDebut = "";
        switch(droit.getGenreService()){
            case IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE:
                valeur = APParameter.GARDE_PARENTAL_JOURS_SANS_IDEMNISATION.getParameterName();break;
            case IAPDroitLAPG.CS_GARDE_PARENTALE:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP:
                valeur = APParameter.GARDE_PARENTAL_JOURS_SANS_IDEMNISATION.getParameterName();break;
            case IAPDroitLAPG.CS_QUARANTAINE:
                valeur = APParameter.QUARANTAINE_JOURS_SANS_INDEMISATION.getParameterName();break;
            case IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE:
                APDroitPanSituation droitSituation = ApgServiceLocator.getEntityService().getDroitPanSituation(session, session.getCurrentThreadTransaction(),
                        droit.getIdDroit());
                if(!JadeStringUtil.isEmpty(droitSituation.getDateDebutManifestationAnnulee())
                        && (JadeStringUtil.isEmpty(droitSituation.getDateFermetureEtablissementDebut())
                        || DF.parse(droitSituation.getDateDebutManifestationAnnulee()).before(DF.parse(droitSituation.getDateFermetureEtablissementDebut())))){
                    valeur = APParameter.MANIFESTATION_INDERDITE_DATE_MINI.getParameterName();
                } else {
                    valeur = APParameter.FERMETURE_EMTREPRISE_DATE_MINI.getParameterName();
                }
                break;
            case IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS:
                valeur = APParameter.INDEPENDANT_PERTE_DE_GAIN_MAX.getParameterName();break;
            case IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE:
                valeur = APParameter.FERMETURE_EMTREPRISE_DATE_MINI.getParameterName();break;
            case IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL:
                valeur = APParameter.SALARIE_EVENEMENTIEL_DATE.getParameterName();break;
            case IAPDroitLAPG.CS_INDEPENDANT_FERMETURE:
            case IAPDroitLAPG.CS_DIRIGEANT_SALARIE_FERMETURE:
            case IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE:
            case IAPDroitLAPG.CS_DIRIGEANT_SALARIE_MANIFESTATION_ANNULEE:
            case IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE:
            case IAPDroitLAPG.CS_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20:
            case IAPDroitLAPG.CS_QUARANTAINE_17_09_20:
                valeur = APParameter.DIRECTIVE_NOVEMBRE_2020.getParameterName();break;
            case IAPDroitLAPG.CS_SALARIE_PERSONNE_VULNERABLE:
            case IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE:
                valeur = APParameter.DIRECTIVE_JANVIER_2021.getParameterName();break;

            default:return dateDebut;
        }

        dateDebut = ajouterDateMinDebutParam(valeur, dateDebut);
        return dateDebut;
    }

    /**
     * Ajoute les périodes pour pandémie aux versements des prestations
     * @param dateDebut
     * @param autreJours
     * @param isIndependant
     * @return le nombre de jour soldés total de toutes les périodes
     * @throws Exception
     */
    private Integer ajouterPeriodesPan(String dateDebut, int autreJours, boolean isIndependant) throws Exception {

        List<APPeriodeComparable> listPeriode = getApPeriodeDroit(droit.getIdDroit());

        int jCarrence = getJourCarenceSelonService();

        calcDateDebutFin(jCarrence, dateDebut, listPeriode);

        // ajout des jours de carrence selon les services
        if(jCarrence != 0){
            delaiSelonService(listPeriode, jCarrence);
        }

        int nbJourSoldes = 0;
        // pour chaque période
        for (APPeriodeComparable periode : listPeriode) {

            int nbJourSoldesAvantAjout = nbJourSoldes;
            if(!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                nbJourSoldes += Integer.valueOf(periode.getNbrJours());
            } else {
                nbJourSoldes += PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
            }

            nbJourSoldes = joursMaxSelonService(autreJours, nbJourSoldes, periode, isIndependant);

            PeriodeAPGCommand bCommand = new PeriodeAPGCommand(periode.getDateDebutPeriode(), true);
            PeriodeAPGCommand eCommand = new PeriodeAPGCommand(periode.getDateFinPeriode(), false);

            // cota dépassé
            if(nbJourSoldesAvantAjout >= nbJourSoldes){
                break;
            }

            // ajouter l'événement "début de la période"
            commands.add(bCommand);

            // ajouter l'événement "fin de la période"
            commands.add(eCommand);

            if(!IAPDroitLAPG.CS_QUARANTAINE.equals(droit.getGenreService())
                    && !IAPDroitLAPG.CS_QUARANTAINE_17_09_20.equals(droit.getGenreService())) {
                // couper par mois
                Date debut = DF.parse(periode.getDateDebutPeriode());
                Calendar fin = getCalendarInstance();

                fin.setTime(DF.parse(periode.getDateFinPeriode()));
                couperParMois(debut, fin);
            } else {
                Calendar bCal = getCalendarInstance();
                // couper par année si nécessaire
                calendar.setTime(bCommand.date);
                bCal.setTime(eCommand.date);

                for (int year = calendar.get(Calendar.YEAR); year < bCal.get(Calendar.YEAR); ++year) {
                    calendar.set(year, Calendar.DECEMBER, 31);
                    commands.add(new NouvelleBaseCommand(calendar.getTime(), false));
                }
            }
        }

        return nbJourSoldes;
    }

    private int getJourCarenceSelonService() throws Exception {
        Integer jourCarence = 0;
        String valeur = "";
        switch(droit.getGenreService()){
            case IAPDroitLAPG.CS_GARDE_PARENTALE:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20:
                valeur = APParameter.GARDE_PARENTAL_JOURS_SANS_IDEMNISATION.getParameterName();break;
            case IAPDroitLAPG.CS_QUARANTAINE:
            case IAPDroitLAPG.CS_QUARANTAINE_17_09_20:
                valeur = APParameter.QUARANTAINE_JOURS_SANS_INDEMISATION.getParameterName();break;
            case IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE:
            case IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS:
            case IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE:
            case IAPDroitLAPG.CS_INDEPENDANT_FERMETURE:
            case IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE:
            case IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE:
            case IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE:
                valeur = APParameter.INDEPENDANT_JOURS_SANS_INDEMISATION.getParameterName();break;
            default:
                valeur = "";
        }

        if(!valeur.isEmpty()) {
            jourCarence = Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", valeur, droit.getDateDebutDroit(), "", 0));
        }
        return jourCarence;
    }

    /**
     * Ajuste la date de début et date de fin selon les plages de valeurs : date début, date fin et délai
     * @param delai
     * @param dateDebut
     * @param listPeriode
     * @throws Exception
     */
    private void calcDateDebutFin(int delai, String dateDebut, List<APPeriodeComparable> listPeriode) throws Exception {

        String dateFin = ajouterDateFinFromParam();

        // pour chaque période
        for (APPeriodeComparable periode : listPeriode) {
            boolean dateModif = false;
            // adapter la date de début de la période par rapport à la date de début officielle du genre de service
            if(!JadeStringUtil.isEmpty(dateDebut) && JadeDateUtil.isDateBefore(periode.getDateDebutPeriode(), dateDebut)) {
                periode.setDateDebutPeriode(dateDebut);
                dateModif = true;
                if(!JadeStringUtil.isEmpty(periode.getDateFinPeriode()) && JadeDateUtil.isDateBefore(periode.getDateFinPeriode(), dateDebut)){
                    periode.setDateFinPeriode(dateDebut);
                }
            }
            // adapter la date de fin de la période par rapport à la date de fin officielle du genre de service
            if(!JadeStringUtil.isEmpty(dateFin) && (JadeStringUtil.isEmpty(periode.getDateFinPeriode()) || JadeDateUtil.isDateAfter(periode.getDateFinPeriode(), dateFin))) {
                periode.setDateFinPeriode(dateFin);
                dateModif = true;
                if(JadeDateUtil.isDateAfter(periode.getDateDebutPeriode(), dateFin)){
                    periode.setDateDebutPeriode(dateFin);
                }
            }
            // si pas de date fin mettre la fin du mois en cours pour le calcul
            if (JadeStringUtil.isEmpty(periode.getDateFinPeriode())) {
                if(IAPDroitLAPG.CS_QUARANTAINE.equals(droit.getGenreService())
                        || IAPDroitLAPG.CS_QUARANTAINE_17_09_20.equals(droit.getGenreService())) {
                    resolveFinJourMaxParam(periode, APParameter.QUARANTAINE_JOURS_MAX.getParameterName(), delai);
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                    periode.setDateFinPeriode(JadeDateUtil.getGlobazFormattedDate(cal.getTime()));
                }
            }

            // si la date de début ou fin a été modifiée, ajuster le nombre de jours soldés par rapport à la période
            if(dateModif && !JadeStringUtil.isBlankOrZero(periode.getNbrJours())){
                int nbJours = PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
                if(nbJours < Integer.valueOf(periode.getNbrJours())) {
                    periode.setNbrJours(Integer.toString(nbJours));
                }
            }
        }
    }

    /**
     * Ajout d'un délai selon le genre de service
     * @param listPeriode
     */
    private int delaiSelonService(List<APPeriodeComparable> listPeriode, int delai) {
        if(!listPeriode.isEmpty()) {

            //décale la date de début ou supprime la période si pas assez de jours
            for(APPeriodeComparable periode: new ArrayList<>(listPeriode)) {
                int nbjours = JadeStringUtil.isBlankOrZero(periode.getNbrJours()) ? PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1 : Integer.valueOf(periode.getNbrJours());
                if (delai <= 0) {
                    break;
                }
                if (nbjours > delai) {
                    periode.setDateDebutPeriode(JadeDateUtil.addDays(periode.getDateDebutPeriode(), delai));
                    if(!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                        periode.setNbrJours(Integer.toString(Integer.valueOf(periode.getNbrJours()) - delai));
                    }
                    break;
                } else {
                    listPeriode.remove(periode);
                    delai -= nbjours;
                }
            }
        }
        return delai;
    }

    private int joursMaxSelonService(int autreJours, int nbJours, APPeriodeComparable periode, boolean independant) throws Exception {
        Integer jourMaximum = null;
        Integer nbJoursNew = nbJours;
        switch(droit.getGenreService()){
            case IAPDroitLAPG.CS_GARDE_PARENTALE:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP:
                if(independant) {
                    jourMaximum = getJourMax(APParameter.GARDE_PARENTAL_INDE_JOURS_MAX.getParameterName(), droit.getDateDebutDroit());
                }
                break;
            case IAPDroitLAPG.CS_QUARANTAINE:
            case IAPDroitLAPG.CS_QUARANTAINE_17_09_20:
                autreJours = 0;
                jourMaximum = getJourMax(APParameter.QUARANTAINE_JOURS_MAX.getParameterName(), droit.getDateDebutDroit());break;
            default:
        }

        if(jourMaximum != null && (nbJours + autreJours > jourMaximum )) {
            nbJoursNew = jourMaximum - autreJours;
            periode.setDateFinPeriode(JadeDateUtil.addDays(periode.getDateFinPeriode(), nbJoursNew - nbJours));
        }

        return nbJoursNew;
    }

    /**
     * Ajoute la date de fin des versements selon le genre de service
     * @return date de Fin du genre de service
     * @throws Exception
     */
    private String ajouterDateFinFromParam() throws Exception {
        String valeur = "";
        switch(droit.getGenreService()){
            case IAPDroitLAPG.CS_SALARIE_PERSONNE_VULNERABLE:
            case IAPDroitLAPG.CS_INDEPENDANT_PERSONNE_VULNERABLE:
                valeur = APParameter.DIRECTIVE_JANVIER_2021_FIN.getParameterName();break;

            default:return null;
        }
        String dateFin = null;
        FWFindParameterManager manager = new FWFindParameterManager();
        manager.setSession(session);
        manager.setIdCodeSysteme("1");
        manager.setIdCleDiffere(valeur);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0){
            FWFindParameter param = (FWFindParameter) manager.get(0);
            Date dateFinValide = DF.parse(new ch.globaz.common.domaine.Date(param.getDateDebutValidite()).getSwissValue());
            String fin = droit.getDateFinDroit();
            if (JadeStringUtil.isEmpty(fin) || DF.parse(fin).after(dateFinValide)) {
                Calendar c = Calendar.getInstance();
                c.setTime(dateFinValide);
                c.add(Calendar.DATE, 1);
                commands.add(new VersementInterditCommand(c.getTime(), true));
            }
            dateFin = JadeDateUtil.getGlobazFormattedDate(dateFinValide);
        }
        return dateFin;
    }

    private void resolveFinJourMaxParam(APPeriodeComparable periode, String param, int delai) throws Exception {
        int jourMax = Integer.parseInt(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", param, droit.getDateDebutDroit(), "", 0));
        String dateFin = JadeDateUtil.addDays(periode.getDateDebutPeriode(), delai + jourMax - 1);
        periode.setDateFinPeriode(dateFin);
    }

    private Integer getJourMax(String param, String dateDebut) throws Exception {
        String beginDate = dateDebut;
        if(beginDate == null) {
            beginDate = "0";
        }
        if(!param.isEmpty()) {
            Integer date =  Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", param, beginDate, "", 0));
            if(date == 0){
                return null;
            }
            return date;
        }
        return null;
    }
}
