package ch.globaz.vulpecula.external.repositoriesjade.naos.converters;

import ch.globaz.naos.business.model.PlanCaisseSimpleModel;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

/**
 * Convertisseur {@link PlanCaisseSimpleModel} en {@link PlanCaisse}
 *
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 *
 */
public final class PlanCaisseConverter {
    /**
     * Constructeur vide empêchant l'instanciation de la classe
     */
    private PlanCaisseConverter() {

    }

    /**
     * Conversion d'un {@link PlanCaisseSimpleModel} en {@link PlanCaisse}
     *
     * @param assuranceSimpleModel
     *            Représentation de la table
     * @return Objet métier {@link PlanCaisse}
     */
    public static PlanCaisse convertToDomain(final PlanCaisseSimpleModel planCaisseSimpleModel) {
        PlanCaisse planCaisse = new PlanCaisse();

        planCaisse.setId(planCaisseSimpleModel.getId());
        planCaisse.setLibelle(planCaisseSimpleModel.getLibelle());
        planCaisse.setTypeAffiliation(planCaisseSimpleModel.getTypeAffiliation());

        Administration admin = new Administration();
        admin.setId(planCaisseSimpleModel.getIdTiers());
        planCaisse.setAdministration(admin);

        return planCaisse;
    }

    public static PlanCaisse convertToDomain(final PlanCaisseSimpleModel planCaisseSimpleModel,
            final Administration administration) {
        PlanCaisse planCaisse = convertToDomain(planCaisseSimpleModel);
        planCaisse.setAdministration(administration);
        return planCaisse;
    }
}
