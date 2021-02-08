package globaz.apg.rapg.rules;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitMaterniteJointTiers;
import globaz.apg.db.droits.APDroitMaterniteJointTiersManager;
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
 * Période de contrôle :
 * Si, durant le congé maternité code 90 et codes 10, 11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 30, 40, 41, 50 </br>
 *
 * @author mmo
 */
public class Rule400 extends Rule {

    public Rule400(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);

        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<String>();
        services.add("10");
        services.add("11");
        services.add("12");
        services.add("13");
        services.add("14");
        services.add("15");
        services.add("16");
        services.add("20");
        services.add("21");
        services.add("22");
        services.add("23");
        services.add("30");
        services.add("40");
        services.add("41");
        services.add("50");
        services.add(APGenreServiceAPG.Maternite.getCodePourAnnonce());
        services.add("92");


        if (services.contains(serviceType)) {

            String nss = champsAnnonce.getInsurant();
            String startOfPeriod = champsAnnonce.getStartOfPeriod();
            String endOfPeriod = champsAnnonce.getEndOfPeriod();
            
            boolean isDroitAAjouter = true;

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");        

            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");
            validNotEmpty(nss, "nss");

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            JadePeriodWrapper periodeControle = new JadePeriodWrapper(startOfPeriod, endOfPeriod);

            try {

                // On recherche tous les droits liés à ce NSS pour la période donnée
                APDroitMaterniteJointTiersManager manager = new APDroitMaterniteJointTiersManager();
                manager.setSession(getSession());
                manager.setForEtatDroitNotIn(etatIndesirable);
                manager.setLikeNumeroAvs(nss);
                List<APDroitAvecParent> droitsSansParents = null;
                try {
                    manager.find();
                    List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                    droitsSansParents = skipDroitParent(tousLesDroits);
                    
                    // On supprime le droit courant de la liste des droits triés
                    List<APDroitAvecParent> droitsTries = new ArrayList<APDroitAvecParent>();
                    for (APDroitAvecParent d : droitsSansParents) {
                        if (!JadeStringUtil.isBlankOrZero(d.getIdDroit()) && d.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                            continue;
                        }
                        droitsTries.add(d);
                    }

                    for (Object d : droitsTries) {
                        APDroitMaterniteJointTiers droit = (APDroitMaterniteJointTiers) d;
//
//                        if ((droit.getUneDateDebutPeriode().equals(startOfPeriod)) || (droit.getUneDateFinPeriode().equals(endOfPeriod))) {
//                            isDroitAAjouter = false;
//                        }
//                        if (isDroitAAjouter) {
                            boolean isPrestationDansPeriodeControle = isHorsPeriode(startOfPeriod, endOfPeriod, droit);

                            // periodeControle.isDateDansLaPeriode(droit.getUneDateDebutPeriode())
                            // && periodeControle.isDateDansLaPeriode(droit.getUneDateFinPeriode());
                            if (!isPrestationDansPeriodeControle) {
                                return false;
                            }
//                        } 
//                        isDroitAAjouter = true;
                    }
                } catch (Exception e) {
                    throwRuleExecutionException(e);
                }
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }

    private boolean isHorsPeriode(String startOfPeriod, String endOfPeriod, APDroitMaterniteJointTiers droit) {
        boolean isHorsPeriode = false;
        String dateDebutMat = droit.getUneDateDebutPeriode();
        String dateFinMat = droit.getUneDateFinPeriode();
        if ((JadeDateUtil.isDateBefore(dateDebutMat, startOfPeriod)
                && JadeDateUtil.isDateBefore(dateDebutMat, endOfPeriod))
                && (JadeDateUtil.isDateBefore(dateFinMat, startOfPeriod)
                        && JadeDateUtil.isDateBefore(dateFinMat, endOfPeriod))
                || (JadeDateUtil.isDateAfter(dateDebutMat, startOfPeriod)
                        && JadeDateUtil.isDateAfter(dateDebutMat, endOfPeriod))
                        && (JadeDateUtil.isDateAfter(dateFinMat, startOfPeriod)
                                && JadeDateUtil.isDateAfter(dateFinMat, endOfPeriod))) {
            isHorsPeriode = true;

        }

        return isHorsPeriode;
    }
}
