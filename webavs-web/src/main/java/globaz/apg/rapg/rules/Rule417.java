package globaz.apg.rapg.rules;

import java.util.*;
import java.util.stream.Collectors;

import ch.globaz.vulpecula.domain.models.common.Periode;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 15
 * et/ou 16 et le champ « numberOfDays » est > 42 jours -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author eniv
 */
public class Rule417 extends Rule {

    private static final int NB_JOUR_MAX = 42;
    private static final int NB_PERIODE_MAX = 3;

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
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<String>();
        services.add("15");
        services.add("16");

        if (services.contains(serviceType)) {

            int totalDeJours = 0;
            String nss = champsAnnonce.getInsurant();
            validNotEmpty(nss, "NSS");

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setForCsGenreService(APGenreServiceAPG.resoudreGenreParCodeAnnonce(serviceType).getCodeSysteme());
            manager.setLikeNumeroAvs(nss);

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            manager.setForEtatDroitNotIn(etatIndesirable);
            List<APDroitAvecParent> droitsSansParents = null;
            try {
                manager.find();
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsSansParents = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }

            if(serviceType.equals(APGenreServiceAPG.InterruptionAvantEcoleSousOfficier.getCodePourAnnonce())) {
                // Pour genre service 15

                for (Object d : droitsSansParents) {
                    APDroitAPGJointTiers droit = (APDroitAPGJointTiers) d;
                    if (!JadeStringUtil.isEmpty(droit.getNbrJourSoldes())) {
                        totalDeJours += Integer.valueOf(droit.getNbrJourSoldes());
                    }
                }

                if (totalDeJours > NB_JOUR_MAX) {
                    return false;
                }

            } else {
                // Pour genre service 16

                // Si plus de 42 jours dans une periode consecutive : erreur
                List<JadePeriodWrapper> periodesConsecutives = periodesConsecutives(droitsSansParents.stream().map(obj -> (APDroitAPGJointTiers) obj).collect(Collectors.toList()));
                for (JadePeriodWrapper periode : periodesConsecutives) {
                    if (getNombreJourFromPeriode(periode) > NB_JOUR_MAX) {
                        return false;
                    }
                }

                // Si plus de 2 interruptions par annee civile : erreur
                Map<Integer, List<JadePeriodWrapper>> periodesAnnee = getPeriodesConsecutivesParAnnee(periodesConsecutives);
                for (List<JadePeriodWrapper> periodes : periodesAnnee.values()) {
                    // 3 periodes = 2 interruptions
                    if (periodes.size() > NB_PERIODE_MAX) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private List<JadePeriodWrapper> periodesConsecutives(List<APDroitAPGJointTiers> droits) {
        List<JadePeriodWrapper> periodes = new ArrayList<>();
        for(APDroitAPGJointTiers droit : droits) {
            reducePeriode(droit);
            periodes.addAll(droit.getPeriodes());
        }
        Collections.sort(periodes);

        Map<Integer, JadePeriodWrapper> periodesConsecutives = new HashMap<>();
        JadePeriodWrapper first = periodes.get(0);
        periodesConsecutives.put(0, first);
        periodes.remove(first);
        int numPeriode = 0;

        for(JadePeriodWrapper periode : periodes){
            JadePeriodWrapper periodesUnion = periodesConsecutives.get(numPeriode).union(periode);
            if(periodesUnion != null){
                periodesConsecutives.put(numPeriode, periodesUnion);
            } else {
                numPeriode++;
                periodesConsecutives.put(numPeriode, periode);
            }
        }

        return new ArrayList(periodesConsecutives.values());
    }

    private Integer getNombreJourFromPeriode(JadePeriodWrapper periode) {
        return JadeDateUtil.getNbDayBetween(periode.getDateDebut(), periode.getDateFin()) + 1;
    }

    private Map<Integer, List<JadePeriodWrapper>> getPeriodesConsecutivesParAnnee(List<JadePeriodWrapper> periodesConsecutives) {
        Map<Integer, List<JadePeriodWrapper>> periodeAnnee = new HashMap<>();
        for(JadePeriodWrapper periode: periodesConsecutives) {
            int year = JadeDateUtil.getGlobazCalendar(periode.getDateDebut()).get(Calendar.YEAR);
            periodeAnnee.computeIfAbsent(year, val -> new ArrayList<>()).add(periode);
            // pour les periodes chevauchantes : associer la periode avec l annee de fin
            int yearFin = JadeDateUtil.getGlobazCalendar(periode.getDateFin()).get(Calendar.YEAR);
            if(year != yearFin){
                periodeAnnee.computeIfAbsent(yearFin, val -> new ArrayList<>()).add(periode);
            }
        }

        return periodeAnnee;
    }

    /**
     * Reduit les périodes par rapport aux jours soldés
     * ex :  01.04.2019 au 30.04.2019 et 29 jours solés -> la fin de la période doit être 29.04.2019
     * @param droit
     */
    private void reducePeriode(APDroitAPGJointTiers droit){
        Collections.sort(droit.getPeriodes());
        int nbJour = 0;
        JadePeriodWrapper lastPeriode = null;
        for(JadePeriodWrapper periode:droit.getPeriodes()) {
            nbJour +=JadeDateUtil.getNbDayBetween(periode.getDateDebut(), periode.getDateFin()) + 1;
            lastPeriode = periode;
        }
        int diff = nbJour - Integer.valueOf(droit.getNbrJourSoldes());
        if(diff > 0) {
            JadePeriodWrapper newPeriode = new JadePeriodWrapper(lastPeriode.getDateDebut(), JadeDateUtil.addDays(lastPeriode.getDateFin(), -diff));
            droit.getPeriodes().remove(lastPeriode);
            droit.getPeriodes().add(newPeriode);
        }
    }

}
