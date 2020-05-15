package globaz.apg.rapg.rules;

import globaz.apg.ApgServiceLocator;
import globaz.apg.db.droits.APDroitPanSituation;
import globaz.apg.enums.APCategorieEntreprise;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import org.safehaus.uuid.Logger;

/**
 * <strong>Règles de validation des plausibilités Pandémie</br>
 * Description :</strong></br>
 * Si le champ « serviceType » =  402
 * et catégorie entreprise = "Autre" -> Erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author mpe
 */
public class Rule64 extends Rule {

    /**
     * @param errorCode
     */
    public Rule64(String errorCode) {
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

        if (serviceType.equals(APGenreServiceAPG.IndependantPandemie.getCodePourAnnonce())) {
            try {
                APDroitPanSituation situation = ApgServiceLocator.getEntityService().getDroitPanSituation(getSession(), getSession().getCurrentThreadTransaction(), champsAnnonce.getIdDroit());
                return !APCategorieEntreprise.AUTRES.getCodeSystem().equals(situation.getCategorieEntreprise());
            } catch (Exception e) {
                Logger.logError("Error ");
            }
        }

        return true;
    }

}
