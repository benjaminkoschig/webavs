package ch.globaz.vulpecula.repositoriesjade.postetravail.converters;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.CotisationConverter;

/**
 * Convertisseur d'objet {@link AdhesionCotisationPosteTravail} <--> {@link AdhesionCaissePosteTravailComplexModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 8 janv. 2014
 * 
 */
public final class AdhesionCotisationPosteTravailConverter {

    /**
     * Constructeur vide empêchant l'instantiation de l'objet
     */
    private AdhesionCotisationPosteTravailConverter() {

    }

    public static AdhesionCotisationPosteTravailSimpleModel convertToPersistence(final String idPoste,
            final AdhesionCotisationPosteTravail adhesionCaissePosteTravail) {
        AdhesionCotisationPosteTravailSimpleModel adhesionCotisationPosteTravailSimpleModel = new AdhesionCotisationPosteTravailSimpleModel();
        adhesionCotisationPosteTravailSimpleModel.setId(adhesionCaissePosteTravail.getId());
        adhesionCotisationPosteTravailSimpleModel.setIdCotisation(adhesionCaissePosteTravail.getCotisation().getId());
        adhesionCotisationPosteTravailSimpleModel.setIdPosteTravail(idPoste);
        if (adhesionCaissePosteTravail.getPeriode() != null) {
            adhesionCotisationPosteTravailSimpleModel.setDateDebut(adhesionCaissePosteTravail.getPeriode()
                    .getDateDebutAsSwissValue());
            adhesionCotisationPosteTravailSimpleModel.setDateFin(adhesionCaissePosteTravail.getPeriode()
                    .getDateFinAsSwissValue());
        }
        adhesionCotisationPosteTravailSimpleModel.setSpy(adhesionCaissePosteTravail.getSpy());
        return adhesionCotisationPosteTravailSimpleModel;
    }

    /**
     * Conversion d'un objet {@link AdhesionCotisationPosteTravail} en {@link AdhesionCotisationPosteTravail}
     * 
     * @param adhesionCaisseSocialeComplexModel
     *            Complex model contenant le couple Adhesion et CaisseSociale
     * @return {@link AdhesionCotisationPosteTravail} objet métier représentant
     *         une adhésion à une caisse sociale
     */
    public static AdhesionCotisationPosteTravail convertToDomain(
            final AdhesionCotisationPosteTravailComplexModel adhesionCotisationPosteTravailComplexModel) {
        AdhesionCotisationPosteTravailSimpleModel adhesionCaissePosteTravailSimpleModel = adhesionCotisationPosteTravailComplexModel
                .getAdhesionCotisationPosteTravail();
        CotisationComplexModel cotisationComplexModel = adhesionCotisationPosteTravailComplexModel
                .getCotisationComplexModel();

        Cotisation cotisation = CotisationConverter.convertToDomain(cotisationComplexModel);

        AdhesionCotisationPosteTravail adhesionCotisation = new AdhesionCotisationPosteTravail();
        adhesionCotisation.setId(adhesionCaissePosteTravailSimpleModel.getId());
        Periode periode;
        try {
            periode = new Periode(adhesionCaissePosteTravailSimpleModel.getDateDebut(),
                    adhesionCaissePosteTravailSimpleModel.getDateFin());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Période de l'adhésion invalide (id adhesion= "
                    + adhesionCotisation.getId() + ")", e);
        }
        adhesionCotisation.setPeriode(periode);
        adhesionCotisation.setCotisation(cotisation);
        adhesionCotisation.setSpy(adhesionCaissePosteTravailSimpleModel.getSpy());

        adhesionCotisation.setIdPosteTravail(adhesionCaissePosteTravailSimpleModel.getIdPosteTravail());

        return adhesionCotisation;
    }

    public static List<AdhesionCotisationPosteTravail> convertToDomain(
            List<AdhesionCotisationPosteTravailComplexModel> adhesionCotisationsComplexModel) {
        List<AdhesionCotisationPosteTravail> cotisations = new ArrayList<AdhesionCotisationPosteTravail>();
        for (AdhesionCotisationPosteTravailComplexModel adhesionCotisationComplexModel : adhesionCotisationsComplexModel) {
            cotisations.add(convertToDomain(adhesionCotisationComplexModel));
        }
        return cotisations;
    }
}
