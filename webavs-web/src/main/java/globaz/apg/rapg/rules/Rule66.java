package globaz.apg.rapg.rules;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialePan;
import globaz.apg.db.droits.APSituationFamilialePanManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeDateUtil;
import org.safehaus.uuid.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <strong>R�gles de validation des plausibilit�s Pand�mie</br>
 * Description :</strong></br>
 * Si le champ � serviceType � = 404
 * et que l'enfant a 20 ans dans la p�riode -> Erreur </br>
 * <strong>Champs concern�(s) :</strong></br>
 *
 */
public class Rule66 extends Rule {

    private static int AGE_MAX = 20;

    /**
     * @param errorCode
     */
    public Rule66(String errorCode) {
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

        if (serviceType.equals(APGenreServiceAPG.GardeParentaleHandicap.getCodePourAnnonce())) {
            try {
                // charger toutes les personnes pour la situation familiale de ce droit.
                APSituationFamilialePanManager mgr = new APSituationFamilialePanManager();

                mgr.setSession(getSession());
                mgr.setForIdDroit(champsAnnonce.getIdDroit());
                mgr.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);

                List<APSituationFamilialePan> list = mgr.toList().stream().map(obj -> (APSituationFamilialePan) obj).collect(Collectors.toList());

                boolean hasEnfant = false;
                for(APSituationFamilialePan sit : list){
                    if(IAPDroitMaternite.CS_TYPE_ENFANT.equals(sit.getType())) {
                        hasEnfant = true;
                        if(hasVingtAnsDansLaPeriode(sit.getDateNaissance(), champsAnnonce.getStartOfPeriod(), champsAnnonce.getEndOfPeriod())) {
                            return false;
                        }
                    }
                }
                if(!hasEnfant) {
                    return false;
                }

                } catch(Exception e){
                    Logger.logError("Error ");
                }

            }
            return true;
    }

    private boolean hasVingtAnsDansLaPeriode(String dateNaissance, String dateDebut, String dateFin) {
        String dateCompare = JadeDateUtil.addYears(dateNaissance, AGE_MAX);
        return JadeDateUtil.isDateBefore(dateCompare, dateFin);
    }

}
