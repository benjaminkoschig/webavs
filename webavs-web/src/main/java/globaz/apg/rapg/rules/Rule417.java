package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <strong>Règles de validation des plausibilités RAPG</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 15
 * et plus d'une interruptions de service ou nombres de jour > 42 jours -> erreur
 * Si le champ « serviceType » = 16
 * et plus de 2 interruptions de service par année civile
 * ou nombre de jour par interruption > 42 -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author eniv
 */
public class Rule417 extends Rule {

    private static final String TYPE_SERVICE_15 = "15";
    private static final String TYPE_SERVICE_16 = "16";
    private static final int NB_JOUR_MAX = 42;
    private static final int NB_PERIODE_MAX_16 = 2;
    private static final int NB_PERIODE_MAX_15 = 1;

    /**
     * @param errorCode
     */
    public Rule417(String errorCode) {
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
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<>();
        services.add(TYPE_SERVICE_15);
        services.add(TYPE_SERVICE_16);

        if (services.contains(serviceType)) {

            int totalDeJours = 0;
            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "NSS");

            List<APDroitAvecParent> droitsSansParents = getDroitSansParents(serviceType, nss);

            if (droitsSansParents == null) {
                return true;
            }

            if ((serviceType.equals(APGenreServiceAPG.InterruptionAvantEcoleSousOfficier.getCodePourAnnonce())
                            && erreurPlausiService15(totalDeJours, droitsSansParents)) // test service 15
                            || (serviceType.equals(APGenreServiceAPG.InterruptionPendantServiceAvancement.getCodePourAnnonce())
                            && erreurPlausiService16(droitsSansParents))) { // test service 16
                return false;
            }
        }

        return true;
    }

    /**
     * Charge les droits liée au NSS
     * @param serviceType
     * @param nss
     * @return
     * @throws APRuleExecutionException
     */
    private List<APDroitAvecParent> getDroitSansParents(String serviceType, String nss) throws APRuleExecutionException {
        APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
        manager.setSession(getSession());
        manager.setForCsGenreService(APGenreServiceAPG.resoudreGenreParCodeAnnonce(serviceType).getCodeSysteme());
        manager.setLikeNumeroAvs(nss);

        // Ne pas traiter les droits en état refusé ou transféré
        List<String> etatIndesirable = new ArrayList<>();
        etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
        etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
        manager.setForEtatDroitNotIn(etatIndesirable);
        List<APDroitAvecParent> droitsSansParents = null;
        try {
            manager.find(BManager.SIZE_NOLIMIT);
            List<APDroitAvecParent> tousLesDroits = manager.getContainer();
            droitsSansParents = skipDroitParent(tousLesDroits);
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        return droitsSansParents;
    }

    /**
     * Vérifie la plausi pour le service interruption 15
     * @param totalDeJours
     * @param droitsSansParents
     * @return
     */
    private boolean erreurPlausiService15(int totalDeJours, List<APDroitAvecParent> droitsSansParents) {

        for (APDroitAvecParent d : droitsSansParents) {
            APDroitAPGJointTiers droit = (APDroitAPGJointTiers) d;
            if (!JadeStringUtil.isEmpty(droit.getNbrJourSoldes())) {
                totalDeJours += Integer.valueOf(droit.getNbrJourSoldes());
            }
        }

        if (totalDeJours > NB_JOUR_MAX) {
            return true;
        }

        // Si plus d'une interruption de service : erreur
        List<JadePeriodWrapper> periodesConsecutives = periodesConsecutives(droitsSansParents.stream().map(obj -> (APDroitAPGJointTiers) obj).collect(Collectors.toList()));
        return periodesConsecutives.size() > NB_PERIODE_MAX_15;
    }

    /**
     * Vérifie la plausi pour le service interruption 16
     * @param droitsSansParents
     * @return
     */
    private boolean erreurPlausiService16(List<APDroitAvecParent> droitsSansParents) {

        // Si plus de 42 jours dans une periode consecutive : erreur
        List<JadePeriodWrapper> periodesConsecutives = periodesConsecutives(droitsSansParents.stream().map(obj -> (APDroitAPGJointTiers) obj).collect(Collectors.toList()));
        for (JadePeriodWrapper periode : periodesConsecutives) {
            if (getNombreJourFromPeriode(periode) > NB_JOUR_MAX) {
                return true;
            }
        }

        // Si plus de 2 interruptions de service par annee civile : erreur
        Map<Integer, List<JadePeriodWrapper>> periodesAnnee = getPeriodesConsecutivesParAnnee(periodesConsecutives);
        for (List<JadePeriodWrapper> periodes : periodesAnnee.values()) {
            if (periodes.size() > NB_PERIODE_MAX_16) {
                return true;
            }
        }
        return false;
    }

    /**
     * Merges les périodes consécutives
     * @param droits
     * @return
     */
    private List<JadePeriodWrapper> periodesConsecutives(List<APDroitAPGJointTiers> droits) {
        List<JadePeriodWrapper> periodes = new ArrayList<>();
        for (APDroitAPGJointTiers droit : droits) {
            reducePeriode(droit);
            periodes.addAll(droit.getPeriodes());
        }
        Collections.sort(periodes);

        Map<Integer, JadePeriodWrapper> periodesConsecutives = new HashMap<>();
        JadePeriodWrapper first = periodes.get(0);
        periodesConsecutives.put(0, first);
        periodes.remove(first);
        int numPeriode = 0;

        for (JadePeriodWrapper periode : periodes) {
            JadePeriodWrapper periodesUnion = periodesConsecutives.get(numPeriode).union(periode);
            if (periodesUnion != null) {
                periodesConsecutives.put(numPeriode, periodesUnion);
            } else {
                numPeriode++;
                periodesConsecutives.put(numPeriode, periode);
            }
        }

        return new ArrayList(periodesConsecutives.values());
    }

    /**
     * renvoie le nombre de jour de la période
     * @param periode
     * @return
     */
    private Integer getNombreJourFromPeriode(JadePeriodWrapper periode) {
        return JadeDateUtil.getNbDaysBetween(periode.getDateDebut(), periode.getDateFin()) + 1;
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
            nbJour += JadeDateUtil.getNbDaysBetween(periode.getDateDebut(), periode.getDateFin()) + 1;
            lastPeriode = periode;
        }
        int diff = nbJour - Integer.valueOf(droit.getNbrJourSoldes());
        if (lastPeriode != null && diff > 0) {
            JadePeriodWrapper newPeriode = new JadePeriodWrapper(lastPeriode.getDateDebut(), JadeDateUtil.addDays(lastPeriode.getDateFin(), -diff));
            droit.getPeriodes().remove(lastPeriode);
            droit.getPeriodes().add(newPeriode);
        }
    }

}
