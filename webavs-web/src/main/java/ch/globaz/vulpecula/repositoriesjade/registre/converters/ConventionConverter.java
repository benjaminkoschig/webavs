package ch.globaz.vulpecula.repositoriesjade.registre.converters;

import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSimpleModel;
import ch.globaz.vulpecula.domain.models.registre.Convention;

/**
 * Convertisseur d'objet {@link Convention} <--> {@link ConventionSimpleModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public final class ConventionConverter {
    /**
     * Constructeur vide empêchant l'instantiation de la classe
     */
    private ConventionConverter() {

    }

    /**
     * Conversion d'un {@link ConventionSimpleModel} en {@link Convention}
     * 
     * @param conventionSimpleModel
     *            SimpleModel contenant les informations relatives à une
     *            convention
     * @return Convention objet du domaine
     */
    public static Convention convertToDomain(AdministrationSimpleModel administrationSimpleModel) {
        Convention convention = new Convention();
        convention.setId(administrationSimpleModel.getId());
        convention.setCode(administrationSimpleModel.getCodeAdministration());
        return convention;
    }

    /**
     * Conversion d'un {@link ConventionComplexModel} en {@link Convention}
     * 
     * @param conventionComplexModel
     *            ComplexModel contenant les informations relatives à une
     *            convention
     * @return {@link Convention} objet du domaine
     */
    public static Convention convertToDomain(AdministrationComplexModel administrationComplexModel) {
        Convention convention = ConventionConverter.convertToDomain(administrationComplexModel.getAdmin());
        convention.setDesignation(administrationComplexModel.getTiers().getDesignation1());
        return convention;
    }
}
