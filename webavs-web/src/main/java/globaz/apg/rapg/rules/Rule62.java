package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APDroitLAPGJointTiersManager;
import globaz.apg.db.droits.APSituationFamilialePan;
import globaz.apg.db.droits.APSituationFamilialePanManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import org.safehaus.uuid.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <strong>Règles de validation des plausibilités Pandémie</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 400
 * et chevauchement de périodes entre celles du requérant et de son conjoint -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author mpe
 */
public class Rule62 extends Rule {

    /**
     * @param errorCode
     */
    public Rule62(String errorCode) {
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

        if (serviceType.equals(APGenreServiceAPG.GardeParentale.getCodePourAnnonce())
            || serviceType.equals(APGenreServiceAPG.GardeParentaleHandicap.getCodePourAnnonce())) {
            try {
                // charger toutes les personnes pour la situation familiale de ce droit.
                APSituationFamilialePanManager mgr = new APSituationFamilialePanManager();

                mgr.setSession(getSession());
                mgr.setForIdDroit(champsAnnonce.getIdDroit());
                mgr.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);

                List<APSituationFamilialePan> list = mgr.toList().stream().map(obj -> (APSituationFamilialePan) obj).collect(Collectors.toList());

                for(APSituationFamilialePan sit : list){
                    if(IAPDroitMaternite.CS_TYPE_CONJOINT.equals(sit.getType())
                        && hasPrestationPourConjoint(sit, champsAnnonce)) {
                        return false;
                    }
                }

                return true;
            } catch (Exception e) {
                Logger.logError("Error ");
            }
        }

        return true;
    }

    private boolean hasPrestationPourConjoint(APSituationFamilialePan conjoint, APChampsAnnonce champsAnnonce) throws Exception {
        if(!JadeStringUtil.isBlankOrZero(conjoint.getNoAVS())) {
            APDroitLAPGJointTiersManager mgr2 = new APDroitLAPGJointTiersManager();
            mgr2.setSession(getSession());
            mgr2.setLikeNumeroAvs(conjoint.getNoAVS());
            mgr2.find(BManager.SIZE_NOLIMIT);
            List<APDroitAvecParent> tousLesDroits = mgr2.getContainer();
            return hasPrestationEnConflit(champsAnnonce.getStartOfPeriod(), champsAnnonce.getEndOfPeriod(), tousLesDroits);
        }
        return false;
    }

}
