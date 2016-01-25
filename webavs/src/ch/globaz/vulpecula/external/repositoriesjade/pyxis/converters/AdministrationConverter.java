package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/**
 * Convertisseur d'objet {@link Administration} <--> {@link AdministrationComplexModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public final class AdministrationConverter {
    /**
     * Constructeur vide empêchant l'instantiation de la classe
     */
    private AdministrationConverter() {

    }

    /**
     * Conversion d'un {@link AdministrationComplexModel} en {@link Administration}
     */
    public static Administration convertToDomain(AdministrationComplexModel administrationComplexModel) {
        AdministrationSimpleModel administrationSimpleModel = administrationComplexModel.getAdmin();
        TiersSimpleModel tiersSimpleModel = administrationComplexModel.getTiers();

        Tiers tiers = TiersConverter.convertToDomain(tiersSimpleModel);

        Administration administration = new Administration(tiers);
        administration.setId(administrationSimpleModel.getIdTiersAdministration());
        administration.setGenre(administrationSimpleModel.getGenreAdministration());
        administration.setCanton(administrationSimpleModel.getCanton());
        administration.setCodeAdministration(administrationSimpleModel.getCodeAdministration());
        administration.setCodeInstitution(administrationSimpleModel.getCodeInstitution());

        return administration;
    }
}
