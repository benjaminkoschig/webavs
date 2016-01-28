package ch.globaz.vulpecula.external.repositoriesjade.naos.converters;

import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;

/**
 * Convertisseur {@link AssuranceSimpleModel} en {@link Assurance}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 * 
 */
public final class AssuranceConverter {
    /**
     * Constructeur vide empêchant l'instanciation de la classe
     */
    private AssuranceConverter() {

    }

    /**
     * Conversion d'un {@link AssuranceSimpleModel} en {@link Assurance}
     * 
     * @param assuranceSimpleModel
     *            Représentation de la table
     * @return Objet métier {@link Assurance}
     */
    public static Assurance convertToDomain(AssuranceSimpleModel assuranceSimpleModel) {
        Assurance assurance = new Assurance();

        assurance.setId(assuranceSimpleModel.getId());
        assurance.setLibelleFr(assuranceSimpleModel.getAssuranceLibelleFr());
        assurance.setLibelleAl(assuranceSimpleModel.getAssuranceLibelleAl());
        assurance.setLibelleIt(assuranceSimpleModel.getAssuranceLibelleIt());
        assurance.setLibelleCourtFr(assuranceSimpleModel.getAssuranceLibelleCourtFr());
        assurance.setLibelleCourtAl(assuranceSimpleModel.getAssuranceLibelleCourtAl());
        assurance.setLibelleCourtIt(assuranceSimpleModel.getAssuranceLibelleCourtIt());
        assurance.setRubriqueId(assuranceSimpleModel.getRubriqueId());
        assurance.setAssuranceCanton(assuranceSimpleModel.getAssuranceCanton());
        assurance.setAssuranceGenre(assuranceSimpleModel.getAssuranceGenre());
        assurance.setTypeAssurance(TypeAssurance.fromValue(assuranceSimpleModel.getTypeAssurance()));
        assurance.setAssurance13(assuranceSimpleModel.getAssurance13());
        assurance.setTauxParCaisse(assuranceSimpleModel.getTauxParCaisse());
        assurance.setSurDocAcompte(assuranceSimpleModel.getSurDocAcompte());

        return assurance;
    }
}
