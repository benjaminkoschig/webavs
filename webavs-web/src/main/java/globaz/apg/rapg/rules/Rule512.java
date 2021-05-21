package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitMaterniteJointTiers;
import globaz.apg.db.droits.APDroitMaterniteJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BManager;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br>
 * Champ « serviceType » = 90 </br> Une maternité ne peut pas durer plus de 154 jours.
 * </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule512 extends Rule {

   /**
     * @param errorCode
     */
    public Rule512(String errorCode) {
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

        List<String> services = new ArrayList<String>();
        services.add(APGenreServiceAPG.Maternite.getCodePourAnnonce());

        if (services.contains(serviceType)) {
            int totalDeJours = 0;
            validNotEmpty(nss, "NSS");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");

            // Voir les prestations en même temps
            APDroitMaterniteJointTiersManager mgr = new APDroitMaterniteJointTiersManager();
            mgr.setForDroitContenuDansDateDebut(startOfPeriod);
            mgr.setForDroitContenuDansDateFin(endOfPeriod);
            mgr.setLikeNumeroAvs(nss);

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            mgr.setForEtatDroitNotIn(etatIndesirable);
            mgr.setSession(getSession());

            List<APDroitAvecParent> droitsSansParents = null;
            try {
                mgr.find(BManager.SIZE_NOLIMIT);
                List<APDroitAvecParent> tousLesDroits = mgr.getContainer();
                droitsSansParents = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }

            try {
                int matExtMax = Integer.parseInt(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1", APParameter.MATERNITE_EXT_JOUR_MAX.getParameterName(), champsAnnonce.getStartOfPeriod(), "", 0));

                for (Object d : droitsSansParents) {
                    APDroitMaterniteJointTiers droit = (APDroitMaterniteJointTiers) d;
                    if (!JadeStringUtil.isBlankOrZero(droit.getUneDateDebutPeriode())
                            && droit.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                        String dateDebutMat = droit.getUneDateDebutPeriode();
                        String dateFinMat = droit.getUneDateFinPeriode();
                        totalDeJours += PRDateUtils.getNbDayBetween(dateDebutMat, dateFinMat) + 1;
                    }
                }

                if (totalDeJours > matExtMax) {
                    return false;
                }
            } catch (Exception e) {
                throw new APRuleExecutionException(e);
            }

        }

        return true;
    }
}
