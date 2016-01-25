package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.PersonneSimple;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/***
 * Convertisseur de {@link PersonneSimpleModel} PersonneSimple <-> {@link PersonneSimple}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 20 d�c. 2013
 * 
 */
public final class PersonneSimpleConverter {
    /***
     * Constructeur priv� emp�chant l'instanciation de la classe
     */
    private PersonneSimpleConverter() {

    }

    /**
     * Conversion d'un objet m�tier {@link PesonneSimple} en {@link PersonneSimpleModel} pouvant �tre manipul� en base
     * de donn�es.
     * 
     * @param personneSimple {@link PersonneSimple} repr�sentant une Personne du module PYXIS
     * @return {@link PersonneSimple} pouvant �tre manipul� en base de donn�es
     */
    public static PersonneSimpleModel convertToPersistence(final PersonneSimple personneSimple) {
        PersonneSimpleModel personneSimpleModel = new PersonneSimpleModel();
        personneSimpleModel.setIdLocaliteDependance(personneSimple.getIdLocaliteDependance());
        personneSimpleModel.setDateNaissance(personneSimple.getDateNaissance());
        personneSimpleModel.setDateDeces(personneSimple.getDateDeces());
        personneSimpleModel.setEtatCivil(personneSimple.getEtatCivil());
        personneSimpleModel.setSexe(personneSimple.getSexe());
        personneSimpleModel.setCanton(personneSimple.getCanton());
        personneSimpleModel.setDistrict(personneSimple.getDistrict());
        return personneSimpleModel;
    }

    /**
     * Conversion d'un objet {@link PersonneSimpleModel} en un objet du domaine {@link PersonneSimple}
     * 
     * @param personneSimpleModel
     * @param tiers
     * @return
     */
    public static PersonneSimple convertToDomain(final PersonneSimpleModel personneSimpleModel, final Tiers tiers) {
        PersonneSimple personneSimple = new PersonneSimple(tiers);
        personneSimple.setIdLocaliteDependance(personneSimpleModel.getIdLocaliteDependance());
        personneSimple.setDateNaissance(personneSimpleModel.getDateNaissance());
        personneSimple.setDateDeces(personneSimpleModel.getDateDeces());
        personneSimple.setEtatCivil(personneSimpleModel.getEtatCivil());
        personneSimple.setSexe(personneSimpleModel.getSexe());
        personneSimple.setCanton(personneSimpleModel.getCanton());
        personneSimple.setDistrict(personneSimpleModel.getDistrict());
        return personneSimple;
    }
}
