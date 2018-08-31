package ch.globaz.vulpecula.external.repositoriesjade.naos.converters;

import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;
import ch.globaz.naos.business.model.PlanCaisseSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.repositories.tiers.AdministrationRepository;

/**
 * Convertisseur {@link CotisationSimpleModel} en {@link Cotisation} Convertie également les objets
 * de la table associative
 * correspondant à la classe {@link AdhesionCotisationPosteTravailSimpleModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 * 
 */
public final class CotisationConverter {
    /**
     * Constructeur vide empêchant l'instanciation de la classe
     */
    private CotisationConverter() {

    }

    private static Cotisation convertToDomain(final CotisationSimpleModel cotisationSimpleModel) {
        Cotisation cotisation = new Cotisation();

        cotisation.setId(cotisationSimpleModel.getId());
        cotisation.setAdhesionId(cotisationSimpleModel.getAdhesionId());
        cotisation.setAnneeDecision(cotisationSimpleModel.getAnneeDecision());
        cotisation.setCategorieTauxId(cotisationSimpleModel.getCategorieTauxId());
        cotisation.setExceptionPeriodicite(cotisationSimpleModel.getExceptionPeriodicite());
        cotisation.setMaisonMere(cotisationSimpleModel.getMaisonMere());
        cotisation.setMasseAnnuelle(cotisationSimpleModel.getMasseAnnuelle());
        cotisation.setMontantAnnuel(cotisationSimpleModel.getMontantAnnuel());
        cotisation.setMontantMensuel(cotisationSimpleModel.getMontantMensuel());
        cotisation.setMontantSemestriel(cotisationSimpleModel.getMontantSemestriel());
        cotisation.setMontantTrimestriel(cotisationSimpleModel.getMontantTrimestriel());
        cotisation.setMotifFin(cotisationSimpleModel.getMotifFin());
        cotisation.setPeriodicite(cotisationSimpleModel.getPeriodicite());
        cotisation.setPlanAffiliationId(cotisationSimpleModel.getPlanAffiliationId());
        cotisation.setTauxAssuranceId(cotisationSimpleModel.getTauxAssuranceId());
        cotisation.setTraitementMoisAnnee(cotisationSimpleModel.getTraitementMoisAnnee());
        cotisation.setDateDebut(new Date(cotisationSimpleModel.getDateDebut()));
        if (cotisationSimpleModel.getDateFin() != null && cotisationSimpleModel.getDateFin().length() != 0) {
            cotisation.setDateFin(new Date(cotisationSimpleModel.getDateFin()));
        }

        return cotisation;
    }

    public static AdhesionCotisationPosteTravailSimpleModel convertToPersistence(final Cotisation cotisation) {
        AdhesionCotisationPosteTravailSimpleModel adhesionCaissePosteTravailSimpleModel = new AdhesionCotisationPosteTravailSimpleModel();
        adhesionCaissePosteTravailSimpleModel.setIdPosteTravail(null);
        adhesionCaissePosteTravailSimpleModel.setIdCotisation(cotisation.getId());
        return adhesionCaissePosteTravailSimpleModel;
    }

    public static Cotisation convertToDomain(final CotisationComplexModel cotisationComplexModel) {
        AssuranceSimpleModel assuranceSimpleModel = cotisationComplexModel.getAssuranceSimpleModel();
        CotisationSimpleModel cotisationSimpleModel = cotisationComplexModel.getCotisationSimpleModel();
        PlanCaisseSimpleModel planCaisseSimpleModel = cotisationComplexModel.getPlanCaisseSimpleModel();

        Cotisation cotisation = CotisationConverter.convertToDomain(cotisationSimpleModel);

        AdministrationRepository administrationRepository = VulpeculaRepositoryLocator.getAdministrationRepository();
        Administration administration = administrationRepository.findById(planCaisseSimpleModel.getIdTiers());

        cotisation.setPlanCaisse(PlanCaisseConverter.convertToDomain(planCaisseSimpleModel, administration));
        cotisation.setAssurance(AssuranceConverter.convertToDomain(assuranceSimpleModel));

        return cotisation;
    }
}
