/**
 * 
 */
package ch.globaz.vulpecula.external.repositoriesjade.naos.converters;

import ch.globaz.naos.business.model.ParticulariteSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.affiliation.Particularite;

/**
 * Convertisseur d'objet {@link Particularite} <--> {@link ParticulariteSimpleModel}
 * 
 * @author Jonas Paratte | Créé le 12.11.2014
 * 
 */

public final class ParticulariteConverter {
    /**
     * Constructeur vide empêchant l'instantiation de la classe
     */
    private ParticulariteConverter() {

    }

    /**
     * Conversion d'un objet {@link ParticulariteSimpleModel} en objet du domaine {@link Particularite}
     * 
     * @param particulariteSimpleModel
     *            Représente la structure d'une particularité d'affiliation en base de données
     * 
     * @return {@link Particularite} objet particularité d'affiliation
     */
    public static Particularite convertToDomain(ParticulariteSimpleModel particulariteSimpleModel) {
        Particularite particularite = new Particularite();
        particularite.setId(particulariteSimpleModel.getParticulariteId());
        particularite.setAffiliationId(particulariteSimpleModel.getAffiliationId());
        particularite.setChampAlphanumerique(particulariteSimpleModel.getChampAlphanumerique());
        particularite.setChampNumerique(particulariteSimpleModel.getChampNumerique());
        if (particulariteSimpleModel.getDateDebut() != null && !particulariteSimpleModel.getDateDebut().equals("0")) {
            particularite.setDateDebut(new Date(particulariteSimpleModel.getDateDebut()));
        }
        if (particulariteSimpleModel.getDateFin() != null && !particulariteSimpleModel.getDateFin().equals("0")) {
            particularite.setDateFin(new Date(particulariteSimpleModel.getDateFin()));
        }
        particularite.setParticularite(particulariteSimpleModel.getParticularite());
        return particularite;
    }
}
