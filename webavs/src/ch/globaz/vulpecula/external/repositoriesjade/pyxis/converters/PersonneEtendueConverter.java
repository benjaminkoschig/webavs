package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.PersonneEtendue;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/***
 * Convertisseur de {@link PersonneEtendueSimpleModel} PersonneEtendue <-> {@link PersonneEtendue}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public final class PersonneEtendueConverter {
    /***
     * Constructeur empêchant l'instantiation de la classe
     */
    private PersonneEtendueConverter() {

    }

    /***
     * Conversion d'un objet {@link PersonneEtendue} en un objet de persistance {@link PersonneEtendueSimpleModel}
     * pouvant être utilisé pour effectuer des
     * manipulations en base de données.
     * 
     * @param personneEtendue
     * @return
     */
    public static PersonneEtendueSimpleModel convertToPersistence(final PersonneEtendue personneEtendue) {
        PersonneEtendueSimpleModel personneEtendueSimpleModel = new PersonneEtendueSimpleModel();
        personneEtendueSimpleModel.setNumAvsActuel(personneEtendue.getNumAvsActuel());
        personneEtendueSimpleModel.setNumContribuableActuel(personneEtendue.getNumContribuableActuel());
        personneEtendueSimpleModel.setAncienNumAvs(personneEtendue.getAncienNumAvs());
        return personneEtendueSimpleModel;
    }

    /**
     * Conversion d'un objet {@link PersonneEtendueComplexModel} en un objet métier {@link PersonneEtendue}
     * 
     * @param personneEtendueComplexModel {@link PersonneEtendueComplexModel} représentant une {@link PerosnneEtendue}
     *            en base de données
     * @return {@link PersonneEtendue} représentant un objet du domaine (tiers)
     */
    public static PersonneEtendue convertToDomain(final PersonneEtendueComplexModel personneEtendueComplexModel) {
        Tiers tiers = TiersConverter.convertToDomain(personneEtendueComplexModel.getTiers());
        PersonneEtendue personneEtendue = new PersonneEtendue(PersonneSimpleConverter.convertToDomain(
                personneEtendueComplexModel.getPersonne(), tiers));
        PersonneEtendueSimpleModel personneEtendueSimpleModel = personneEtendueComplexModel.getPersonneEtendue();
        personneEtendue.setNumAvsActuel(personneEtendueSimpleModel.getNumAvsActuel());
        personneEtendue.setNumContribuableActuel(personneEtendueSimpleModel.getNumContribuableActuel());
        personneEtendue.setAncienNumAvs(personneEtendueSimpleModel.getAncienNumAvs());
        return personneEtendue;
    }
}
