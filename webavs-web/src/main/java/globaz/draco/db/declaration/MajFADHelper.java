package globaz.draco.db.declaration;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.draco.business.domaine.DeclarationSalaireType;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.naos.exception.MajorationFraisAdminException;
import globaz.draco.properties.DSProperties;
import globaz.globall.db.BTransaction;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.db.affiliation.AFAffiliation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Permet d'effectuer les actions nécessaires à la
 * la majoration des frais d'administrations d'un affilié.
 *
 * @author ESVE | Créé le 05 août 2020
 *
 */
public class MajFADHelper {

    private static final Logger LOG = LoggerFactory.getLogger(MajFADHelper.class);

    /**
     * Update le boolean majFAD sur l'affiliation en fonction des propriété système
     * draco.majoration.declaration.manuelle.active et draco.majoration.declaration.manuelle.assurance
     * et de leur concordence avec les valeurs trouvées dans l'affiliation et la déclaration
     *
     * @param transaction la transaction
     * @param afAffiliation l'affiliation
     * @param provenance la provenance
     * @param typeDeclaration le type de déclaration de salaire
     */
    public void updateMajFADAvecDeclaration(List<String> typesDeclarationDepuisPropriete, BTransaction transaction, AFAffiliation afAffiliation, String provenance, String typeDeclaration, String anneeDeclSalaire) throws Exception {
        try {
            HashSet<String> idsAssurancesAffiliation = AFBusinessServiceLocator.getAffiliationService().getIdsAssurancesAffiliation(transaction.getSession(), afAffiliation.getAffilieNumero());
            List<String> idsAssurancesTous = AFBusinessServiceLocator.getAssuranceService().getIdsAssurancesTous(transaction.getSession());
            List<String> idsAssurancesDepuisPropriete = getIdsAssurancesDepuisProprietes(idsAssurancesTous);

            if (typesDeclarationDepuisPropriete.contains(typeDeclaration)
                    && idsAssurancesDepuisPropriete.size() > 0
                    && idsAssurancesAffiliation.size() > 0
                    && idsAssurancesAffiliation.stream()
                    .anyMatch(id -> idsAssurancesDepuisPropriete.contains(id))) {

                if (provenance.equals(DSDeclarationViewBean.PROVENANCE_MANUELLE)) {
                    afAffiliation.setMajFAD(true);
                    afAffiliation.setSaisieSysteme(true);
                    afAffiliation.update();
                    AFBusinessServiceLocator.getAffiliationService().addOrActivateCotisationAssuranceMajoration(transaction, afAffiliation, anneeDeclSalaire);
                } else {
                    afAffiliation.setMajFAD(false);
                    afAffiliation.setSaisieSysteme(true);
                    afAffiliation.update();
                    AFBusinessServiceLocator.getAffiliationService().deactivateCotisationAssuranceMajoration(transaction, afAffiliation);
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            String message = "Erreur durant la recherche du service pour mettre à jour la cotisation à l'asssurance de majoration des frais d'admin.";
            LOG.error(message, e);
            throw new MajorationFraisAdminException(message, e);
        }
    }

    /**
     * Update le boolean majFAD sur l'affiliation en fonction des propriété système
     * draco.majoration.declaration.manuelle.active et draco.majoration.declaration.manuelle.assurance
     * ainsi que leur concordence avec les valeurs trouvées dans l'affiliation et la déclaration
     *
     * @param transaction la transaction
     * @param afAffiliation l'affiliation
     */
    public void updateMajFADSansDeclaration(BTransaction transaction, AFAffiliation afAffiliation) throws Exception {
        try {
            HashSet<String> idsAssurancesAffiliation = AFBusinessServiceLocator.getAffiliationService().getIdsAssurancesAffiliation(transaction.getSession(), afAffiliation.getAffilieNumero());
            List<String> idsAssurancesTous = AFBusinessServiceLocator.getAssuranceService().getIdsAssurancesTous(transaction.getSession());
            List<String> idsAssurancesDepuisPropriete = getIdsAssurancesDepuisProprietes(idsAssurancesTous);

            if (idsAssurancesDepuisPropriete.size() > 0
                    && idsAssurancesAffiliation.size() > 0
                    && idsAssurancesAffiliation.stream()
                    .anyMatch(id -> idsAssurancesDepuisPropriete.contains(id))) {

                if (afAffiliation.isMajFAD()) {
                    AFBusinessServiceLocator.getAffiliationService().addOrActivateCotisationAssuranceMajoration(transaction, afAffiliation, null);
                } else {
                    AFBusinessServiceLocator.getAffiliationService().deactivateCotisationAssuranceMajoration(transaction, afAffiliation);
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            String message = "Erreur durant la recherche du service pour mettre à jour la cotisation à l'asssurance de majoration des frais d'admin.";
            LOG.error(message, e);
            throw new MajorationFraisAdminException(message, e);
        }
    }


    /**
     * Retourne la valeur de la propriété draco.majoration.declaration.manuelle.active
     * cette valeur représente une liste de type de déclaration qui limite les déclarations
     * auxquel une majoration des frais administratif pourra être appliqué.
     *
     * @return Une liste des types de déclaration présents dans la propriété
     */
    public static List<String> getTypesDeclarationDepuisProprietes() {
        try {
            String majorationDeclarationTypes = CommonPropertiesUtils.getValue(DSProperties.MAJORATION_DECLARATION_MANUELLE_ACTIVE);
            List<String> majorationDeclarationTypesSplitted = Arrays.asList(majorationDeclarationTypes.split(","));
            List<String> declarationTypesTous = Arrays.stream(DeclarationSalaireType.values())
                    .map(DeclarationSalaireType::getCodeSystem)
                    .collect(Collectors.toList());
            if (majorationDeclarationTypesSplitted.size() > 0) {
                if (declarationTypesTous.containsAll(majorationDeclarationTypesSplitted)) {
                    return majorationDeclarationTypesSplitted;
                } else {
                    String message = "La propriété: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ACTIVE.getPropertyName() + " possède des valeurs érronées. Desc: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE.getDescription();
                    LOG.warn(message);
                }
            }
        } catch (PropertiesException e) {
            String message = "La propriété: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ACTIVE.getPropertyName() + " n'existe pas en base de données. Desc: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE.getDescription();
            LOG.error(message, e);
        }

        return new ArrayList<>();
    }

     /**
     * Retourne la valeur de la propriété draco.majoration.declaration.manuelle.assurance
     * cette valeur représente une liste d'assurances qui limite les déclarations de salaires
     * auxquel une majoration des frais administratif pourra être appliqué.
     *
     * @param idsAssurancesTous Tous les ids d'assurances existant
     * @return Une liste des ids d'assurances présents dans la propriété
     */
    public static List<String> getIdsAssurancesDepuisProprietes(List<String> idsAssurancesTous) {
        try {
            String idsAssurancesDepuisProprietePourMajoration = CommonPropertiesUtils.getValue(DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE);
            List<String> idsAssurancesDepuisProprietePourMajorationSplitted = Arrays.asList(idsAssurancesDepuisProprietePourMajoration.split(","));
            if (idsAssurancesDepuisProprietePourMajorationSplitted.size() > 0) {
                if (idsAssurancesTous.containsAll(idsAssurancesDepuisProprietePourMajorationSplitted)) {
                    return idsAssurancesDepuisProprietePourMajorationSplitted;
                } else {
                    String message = "La propriété: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE.getPropertyName() + " possède des valeurs érronées. Desc: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE.getDescription();
                    LOG.warn(message);
                }
            } else {
                String message = "La propriété: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE.getPropertyName() + " ne possède aucune valeur. Desc: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE.getDescription();
                LOG.info(message);
            }
        } catch (PropertiesException e) {
            String message = "La propriété: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ACTIVE.getPropertyName() + " n'existe pas en base de données. Desc: " + DSProperties.MAJORATION_DECLARATION_MANUELLE_ASSURANCE.getDescription();
            LOG.error(message, e);
        }
        return new ArrayList<>();
    }
}
