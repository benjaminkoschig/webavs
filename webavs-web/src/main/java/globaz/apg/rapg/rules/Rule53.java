package globaz.apg.rapg.rules;

import java.util.ArrayList;
import java.util.List;
import org.safehaus.uuid.Logger;
import globaz.apg.ApgServiceLocator;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.api.IAFAssurance;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br>
 * Description :</strong></br>
 * Si le champ � serviceType � = 15
 * et/ou 16 et le champ � numberOfDays � est > 42 jours -> erreur </br>
 * <strong>Champs concern�(s) :</strong></br>
 *
 * @author eniv
 */
public class Rule53 extends Rule {

    private static final int NB_JOUR_MAX = 31;

    /**
     * @param errorCode
     */
    public Rule53(String errorCode) {
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
        String idDroit = champsAnnonce.getIdDroit();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<String>();
        services.add(APGenreServiceAPG.Demenagement.getCodePourAnnonce());
        services.add(APGenreServiceAPG.Naissance.getCodePourAnnonce());
        services.add(APGenreServiceAPG.MariageLPart.getCodePourAnnonce());
        services.add(APGenreServiceAPG.Deces.getCodePourAnnonce());
        services.add(APGenreServiceAPG.InspectionRecrutementLiberation.getCodePourAnnonce());
        services.add(APGenreServiceAPG.CongeJeunesse.getCodePourAnnonce());
        services.add(APGenreServiceAPG.ServiceEtranger.getCodePourAnnonce());

        if (services.contains(serviceType)) {
            boolean hasComplementCIAB = false;
            boolean isVersementEmployeur = false;
            List<APSitProJointEmployeur> list;
            List<IAFAssurance> listAssurance;
            try {
                list = ApgServiceLocator.getEntityService().getSituationProfJointEmployeur(getSession(), null, idDroit);
                for (APSitProJointEmployeur employeur : list) {
                    listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                            employeur.getIdAffilie(), getSession());
                    String idAssuranceJU = JadePropertiesService.getInstance()
                            .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_JU_ID);
                    String idAssuranceBE = JadePropertiesService.getInstance()
                            .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_BE_ID);
                    if (employeur.getIsVersementEmployeur()) {
                        isVersementEmployeur = true;
                    }
                    for (IAFAssurance assurance : listAssurance) {
                        if ((assurance.getAssuranceId().equals(idAssuranceBE)
                                || assurance.getAssuranceId().equals(idAssuranceJU))) {
                            hasComplementCIAB = true;
                        }

                    }

                }
            } catch (Exception e) {
                Logger.logError("Error ");
            }
            if (!hasComplementCIAB && isVersementEmployeur) {
                return false;
            }
        }

        return true;
    }

}