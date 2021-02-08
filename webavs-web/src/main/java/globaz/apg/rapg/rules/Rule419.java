package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.*;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *Si entre le premier et le dernier jour de congé de paternité ou de congé de proche-aidant :
 * Le nombre de jours avec code de service ? 91
 * + nombre de jours avec code de service = 91
 * > Nombre de jours entre la période de début et de fin du congé de paternité ?  erreur
 * Si nombre de jours avec type de service ? 92
 * + nombre de jours avec type de service = 92
 * > Nombre de jours entre la période de début et de fin du congé de paternité ?  erreur
 * @author von
 */
public class Rule419 extends Rule {

    private static final int NOMBRE_JOURS_MAX = 14;


    /**
     * @param errorCode
     */
    public Rule419(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException {

        String serviceType = champsAnnonce.getServiceType();
        int nombreJoursPat = Integer.parseInt(champsAnnonce.getNumberOfDays());
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        String nss = champsAnnonce.getInsurant();
        validNotEmpty(nss, "NSS");
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if(serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce())){

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            APDroitPaterniteJointTiersManager manager = new APDroitPaterniteJointTiersManager();
            manager.setSession(getSession());
            manager.setLikeNumeroAvs(nss);
            manager.setForEtatDroitNotIn(etatIndesirable);
            List<APDroitAvecParent> droitsTries = null;
            try {
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsTries = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            // On va trouver tous les droits, même celui qu'on vient de créer,
            // on additionne tous les jours soldées

            for (int i = 0; i < droitsTries.size(); i++) {
                APDroitPaterniteJointTiers droit = (APDroitPaterniteJointTiers) droitsTries.get(i);
                for(APPeriodeAPG periodePat : droit.getPeriodes()){
                    if (!JadeStringUtil.isBlank(periodePat.getNbrJours()) && IsSameOldestChild(droit.getIdDroit(),champsAnnonce.getChildInsurantVn())) {
                        int nbreJoursAAjouter = Integer.parseInt(periodePat.getNbrJours());
                        nombreJoursPat += nbreJoursAAjouter;
                    }
                }
            }
            if(nombreJoursPat>NOMBRE_JOURS_MAX){
                return false;
            }

        }

        return true;
    }

    private boolean IsSameOldestChild(String idDroit, String childInsurantVn) throws APRuleExecutionException {
        APSituationFamilialePatManager manager = new APSituationFamilialePatManager();
        manager.setSession(getSession());
        manager.setForIdDroitPaternite(idDroit);
        try {
            manager.find();
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        String nssEnfantVieux = "";
        String dateNaissance = "";
        for(APSituationFamilialePat situationFamilialePat :   (List<APSituationFamilialePat>) manager.getContainer()){
            if(JadeStringUtil.isBlankOrZero(dateNaissance)
                    || JadeDateUtil.isDateBefore(situationFamilialePat.getDateNaissance(),dateNaissance) ){
                dateNaissance = situationFamilialePat.getDateNaissance();
                nssEnfantVieux = situationFamilialePat.getNoAVS();
            }
        }

        if(nssEnfantVieux.equals(childInsurantVn)){
            return true;
        }
        return false;

    }




    /**
     * Trie les périodes par année civile
     * @param periodesConsecutives
     * @return
     */
    private Map<Integer, List<JadePeriodWrapper>> getPeriodesConsecutivesParAnnee(List<JadePeriodWrapper> periodesConsecutives) {
        Map<Integer, List<JadePeriodWrapper>> periodeAnnee = new HashMap<>();
        for (JadePeriodWrapper periode : periodesConsecutives) {
            int year = JadeDateUtil.getGlobazCalendar(periode.getDateDebut()).get(Calendar.YEAR);
            periodeAnnee.computeIfAbsent(year, val -> new ArrayList<>()).add(periode);
            // pour les periodes chevauchantes : associer la periode avec l annee de fin
            int yearFin = JadeDateUtil.getGlobazCalendar(periode.getDateFin()).get(Calendar.YEAR);
            if (year != yearFin) {
                periodeAnnee.computeIfAbsent(yearFin, val -> new ArrayList<>()).add(periode);
            }
        }

        return periodeAnnee;
    }

    /**
     * Reduit les périodes par rapport aux jours soldés
     * ex :  01.04.2019 au 30.04.2019 et 29 jours solés -> la fin de la période doit être 29.04.2019
     *
     * @param droit
     */
    private void reducePeriode(APDroitAPGJointTiers droit) {
        Collections.sort(droit.getPeriodes());
        int nbJour = 0;
        JadePeriodWrapper lastPeriode = null;
        for (JadePeriodWrapper periode : droit.getPeriodes()) {
            nbJour += JadeDateUtil.getNbDayBetween(periode.getDateDebut(), periode.getDateFin()) + 1;
            lastPeriode = periode;
        }
        int diff = nbJour - Integer.valueOf(droit.getNbrJourSoldes());
        if (lastPeriode != null && diff > 0) {
            diff = checkRemovePeriode(diff, lastPeriode, droit);
            lastPeriode = droit.getPeriodes().get(droit.getPeriodes().size()-1);
            JadePeriodWrapper newPeriode = new JadePeriodWrapper(lastPeriode.getDateDebut(), JadeDateUtil.addDays(lastPeriode.getDateFin(), -diff));
            droit.getPeriodes().remove(lastPeriode);
            droit.getPeriodes().add(newPeriode);
        }
    }

    /**
     * Méthode récursive pour supprimer les périodes en trop si le nb de jours soldés n'est pas suffisant
     * @param diff
     * @param periode
     * @param droit
     * @return nb jour restants à retirer aux périodes
     */
    private Integer checkRemovePeriode(Integer diff, JadePeriodWrapper periode, APDroitAPGJointTiers droit) {
        int nbJourLastPeriode = JadeDateUtil.getNbDayBetween(periode.getDateDebut(), periode.getDateFin()) + 1;
        if(diff >= nbJourLastPeriode) {
            diff -= nbJourLastPeriode;
            droit.getPeriodes().remove(periode);
            diff = checkRemovePeriode(diff, droit.getPeriodes().get(droit.getPeriodes().size()-1), droit);
        }
        return diff;
    }

}
