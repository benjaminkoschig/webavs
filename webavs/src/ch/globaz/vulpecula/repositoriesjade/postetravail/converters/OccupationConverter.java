/**
 * 
 */
package ch.globaz.vulpecula.repositoriesjade.postetravail.converters;

import ch.globaz.vulpecula.business.models.postetravail.TauxOccupationSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;

/**
 * Convertisseur de {@link TauxOccupationSimpleModel} TauxOccupation <-> {@link Occupation}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 24 déc. 2013
 * 
 */
public final class OccupationConverter {
    /**
     * Constructeur privé empêchant l'instanciation de la classe
     */
    private OccupationConverter() {

    }

    /**
     * Conversion d'un objet {@link TauxOccupationSimpleModel} en {@link Occupation}
     * 
     * @param tauxOccupationSimpleModel
     *            Représentation d'une occupation dans la base de données
     * @return Objet du domaine représentant une occupation
     */
    public static Occupation convertToDomain(final TauxOccupationSimpleModel tauxOccupationSimpleModel) {
        Occupation occupation = new Occupation();
        occupation.setId(tauxOccupationSimpleModel.getId());
        occupation.setTaux(new Taux(tauxOccupationSimpleModel.getTaux()));
        occupation.setDateValidite(new Date(tauxOccupationSimpleModel.getDateValidite()));
        occupation.setSpy(tauxOccupationSimpleModel.getSpy());
        return occupation;
    }

    /**
     * Conversion d'un objet {@link Occupation} en {@link TauxOccupationSimpleModel}
     * 
     * @param idPoste
     *            Id du poste de travail auquel l'occupation est attachée
     * @param occupation
     *            {@link Occupation} à convertir en sa représentation en base de
     *            données
     * @return {@link TauxOccupationSimpleModel}
     */
    public static TauxOccupationSimpleModel convertToPersistence(final String idPoste, final Occupation occupation) {
        TauxOccupationSimpleModel tauxOccupationSimpleModel = new TauxOccupationSimpleModel();
        tauxOccupationSimpleModel.setId(occupation.getId());
        tauxOccupationSimpleModel.setIdPosteTravail(idPoste);
        tauxOccupationSimpleModel.setDateValidite(occupation.getDateValidite().getSwissValue());
        tauxOccupationSimpleModel.setTaux(String.valueOf(occupation.getTaux().getValue()));
        tauxOccupationSimpleModel.setSpy(occupation.getSpy());
        return tauxOccupationSimpleModel;
    }
}
