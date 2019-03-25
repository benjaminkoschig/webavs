package globaz.apg.rapg.rules;

import java.util.ArrayList;
import java.util.List;
import org.safehaus.uuid.Logger;
import globaz.apg.ApgServiceLocator;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.api.IAFAssurance;

/**
 * <strong>Règles de validation des plausibilités RAPG</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 15
 * et/ou 16 et le champ « numberOfDays » est > 42 jours -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author eniv
 */
public class Rule50 extends Rule {

    private static final int NB_JOUR_MAX = 31;

    /**
     * @param errorCode
     */
    public Rule50(String errorCode) {
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
        Integer nbreJours = new Integer(champsAnnonce.getNumberOfDays());
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<String>();
        services.add("15");
        services.add("16");

        if (services.contains(serviceType)) {

        }

        boolean hasComplementCIAB = false;
        boolean isVersementEmployeur = false;
        List<APSitProJointEmployeur> list;
        List<IAFAssurance> listAssurance;
        try {
            list = ApgServiceLocator.getEntityService().getSituationProfJointEmployeur(getSession(), null, idDroit);
            for (APSitProJointEmployeur employeur : list) {
                listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                        employeur.getIdAffilie(), getSession());
                String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                        .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
                String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                        .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);

                String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                        .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
                String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                        .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

                if (employeur.getIsVersementEmployeur()) {
                    isVersementEmployeur = true;
                }
                for (IAFAssurance assurance : listAssurance) {
                    if (assurance.getAssuranceId().equals(idAssuranceParitaireJU)
                            || assurance.getAssuranceId().equals(idAssurancePersonnelJU)
                            || assurance.getAssuranceId().equals(idAssuranceParitaireBE)
                            || assurance.getAssuranceId().equals(idAssurancePersonnelBE)) {
                        hasComplementCIAB = true;
                    }

                }

            }
        } catch (Exception e) {
            Logger.logError("Error ");
        }
        if ((hasComplementCIAB && isVersementEmployeur) && nbreJours > NB_JOUR_MAX) {
            return false;
        }

        return true;
    }

}
