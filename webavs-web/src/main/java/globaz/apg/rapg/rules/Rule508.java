package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitMaterniteJointTiersManager;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Une seule maternité par année
 * civile :</br> Une personne ne peut pas avoir plus de 98 jours avec serviceType = 90, au cours d'une même année
 * civile.</br> De plus, une même personne ne peut pas cumuler plusieurs annonces de type 1 avec serviceType = 90 sur
 * une même année civile. </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule508 extends Rule {

    /**
     * @param errorCode
     */
    public Rule508(String errorCode) {
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
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String endOfPeriod = champsAnnonce.getEndOfPeriod();
        String nss = champsAnnonce.getInsurant();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if ("90".equals(champsAnnonce.getServiceType())) {
            validNotEmpty(nss, "NSS");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");

            // Voir les prestations en même temps
            APDroitMaterniteJointTiersManager mgr = new APDroitMaterniteJointTiersManager();
            mgr.setForDroitContenuDansDateDebut("01.01." + startOfPeriod.substring(6));
            mgr.setForDroitContenuDansDateFin("31.12." + endOfPeriod.substring(6));
            mgr.setLikeNumeroAvs(nss);

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            mgr.setForEtatDroitNotIn(etatIndesirable);

            // On ne filtre pas avec l'id du droit courant à cause du filtrage avec les droits parent
            // if (!JadeStringUtil.isEmpty(champsAnnonce.getIdDroit())) {
            // mgr.getForIdDroitNotIn().add(champsAnnonce.getIdDroit());
            // }
            mgr.setSession(getSession());
            List<APDroitAvecParent> droitsTries = null;
            try {
                mgr.find();
                List<APDroitAvecParent> tousLesDroits = mgr.getContainer();
                // Contient tous les droit du tiers pour la période donnée
                droitsTries = skipDroitParent(tousLesDroits);
                // Si on connaît l'id du droit courant (celui que l'on calcul), on test > 1
                if (droitsTries.size() == 0) {
                    return true;
                }
                // Cas le plus probable, on s'assure que le droit qu'on ait trouvé soit celui que l'on calcul
                else if (droitsTries.size() == 1) {
                    if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getIdDroit())) {
                        if (droitsTries.get(0).getIdDroit().equals(champsAnnonce.getIdDroit())) {
                            return true;
                        }
                    }
                }
                // Tous les autres cas sont faux
                else {
                    return false;
                }
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
        }

        return true;
    }
}
