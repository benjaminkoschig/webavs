package ch.globaz.vulpecula.external.repositoriesjade.naos.converters;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.naos.business.model.AdhesionSimpleModel;
import ch.globaz.naos.business.model.PlanCaisseSimpleModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.AdhesionComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;

/**
 * Convertisseur {@link AdhesionComplexModel} en {@link Adhesion} Convertie également les objets
 * de la table associative
 * correspondant à la classe {@link AdhesionCotisationPosteTravailSimpleModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 * 
 */
public final class AdhesionConverter {
    /**
     * Constructeur vide empêchant l'instanciation de la classe
     */
    private AdhesionConverter() {

    }

    public static Adhesion convertToDomain(final AdhesionComplexModel adhesionComplexModel) {
        AdhesionSimpleModel adhesionSimpleModel = adhesionComplexModel.getAdhesionSimpleModel();
        PlanCaisseSimpleModel planCaisseSimpleModel = adhesionComplexModel.getPlanCaisseSimpleModel();
        AdministrationComplexModel administrationComplexModelForAdhesionComplexModel = adhesionComplexModel
                .getAdministrationForAdhesionComplexModel();
        AdministrationComplexModel administrationComplexModelForPlanCaisseComplexModel = adhesionComplexModel
                .getAdministrationForPlanCaisseComplexModel();

        // Conversion des models de base de données en modèles métier

        // Si l'adhésion pour l'adhésion n'est pas setté
        Administration administrationForAdhesion = null;
        if (!administrationComplexModelForAdhesionComplexModel.getTiers().getId().isEmpty()) {
            administrationForAdhesion = AdministrationConverter
                    .convertToDomain(administrationComplexModelForAdhesionComplexModel);
        }
        Administration administrationForPlanCaisse = AdministrationConverter
                .convertToDomain(administrationComplexModelForPlanCaisseComplexModel);
        PlanCaisse planCaisse = PlanCaisseConverter.convertToDomain(planCaisseSimpleModel, administrationForPlanCaisse);

        Adhesion adhesion = new Adhesion();

        adhesion.setPlanCaisse(planCaisse);
        adhesion.setAdministration(administrationForAdhesion);
        adhesion.setId(adhesionSimpleModel.getId());
        adhesion.setDateDebut(new Date(adhesionSimpleModel.getDateDebut()));
        adhesion.setTypeAdhesion(adhesionSimpleModel.getTypeAdhesion());

        if (!JadeStringUtil.isBlankOrZero(adhesionSimpleModel.getDateFin())) {
            adhesion.setDateFin(new Date(adhesionSimpleModel.getDateFin()));
        }

        return adhesion;
    }

    public static AdhesionCotisationPosteTravailSimpleModel convertToPersistence(final Cotisation cotisation) {

        AdhesionCotisationPosteTravailSimpleModel adhesionCaissePosteTravailSimpleModel = new AdhesionCotisationPosteTravailSimpleModel();
        adhesionCaissePosteTravailSimpleModel.setIdPosteTravail(cotisation.getAdhesionId());
        adhesionCaissePosteTravailSimpleModel.setIdCotisation(cotisation.getId());
        return adhesionCaissePosteTravailSimpleModel;
    }
}
