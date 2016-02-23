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
 * @author Jonas Paratte | Cr�� le 12.11.2014
 * 
 */

public final class ParticulariteConverter {
    /**
     * Constructeur vide emp�chant l'instantiation de la classe
     */
    private ParticulariteConverter() {

    }

    /**
     * Conversion d'un objet {@link ParticulariteSimpleModel} en objet du domaine {@link Particularite}
     * 
     * @param particulariteSimpleModel
     *            Repr�sente la structure d'une particularit� d'affiliation en base de donn�es
     * 
     * @return {@link Particularite} objet particularit� d'affiliation
     */
    public static Particularite convertToDomain(ParticulariteSimpleModel particulariteSimpleModel) {
        Particularite particularite = new Particularite();
        particularite.setId(particulariteSimpleModel.getParticulariteId());
        particularite.setAffiliationId(particulariteSimpleModel.getAffiliationId());
        particularite.setChampAlphanumerique(particulariteSimpleModel.getChampAlphanumerique());
        particularite.setChampNumerique(particulariteSimpleModel.getChampNumerique());
        if (isNotEmpty(particulariteSimpleModel.getDateDebut())) {
            particularite.setDateDebut(new Date(particulariteSimpleModel.getDateDebut()));
        }
        if (isNotEmpty(particulariteSimpleModel.getDateFin())) {
            particularite.setDateFin(new Date(particulariteSimpleModel.getDateFin()));
        }
        particularite.setParticularite(particulariteSimpleModel.getParticularite());
        return particularite;
    }

    /**
     * @param date
     * @return true if date isn't empty or zero
     */
    private static boolean isNotEmpty(String date) {
        return date != null && !date.equals("0") && date.length() > 0;
    }
}
