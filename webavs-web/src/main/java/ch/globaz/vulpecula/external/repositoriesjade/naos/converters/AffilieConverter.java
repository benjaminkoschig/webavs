/**
 * 
 */
package ch.globaz.vulpecula.external.repositoriesjade.naos.converters;

import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.model.AffiliationTiersComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.vulpecula.external.models.affiliation.Affilie;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.TiersConverter;

/**
 * Convertisseur d'objet {@link Affilie} <--> {@link AffiliationTiersComplexModel}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 20 d�c. 2013
 * 
 */
public final class AffilieConverter {
    /**
     * Constructeur vide emp�chant l'instantiation de la classe
     */
    private AffilieConverter() {

    }

    /**
     * Conversion d'un objet {@link AffiliationTiersComplexModel} en objet du domaine {@link Affilie}
     * 
     * @param affiliationTiersComplexModel
     *            Repr�sente une structure d'un affilie en base de donn�es
     * @return {@link Affilie} objet Affilie au sens du module Naos
     */
    public static Affilie convertToDomain(final AffiliationTiersComplexModel affiliationTiersComplexModel) {
        return AffilieConverter.convertToDomain(affiliationTiersComplexModel.getAffiliation(),
                affiliationTiersComplexModel.getTiersAffiliation());
    }

    /**
     * Conversion d'un objet {@link AffiliationTiersComplexModel} en objet du domaine {@link Affilie}
     * 
     * @param affiliationSimpleModel
     *            Repr�sente la structure d'une affiliation en base de donn�es
     * @param tiersSimpleModel
     *            Repr�sente la structure d'un tiers en base de donn�es
     * @return {@link Affilie} objet Affilie au sens du module Naos
     */
    public static Affilie convertToDomain(final AffiliationSimpleModel affiliationSimpleModel,
            final TiersSimpleModel tiersSimpleModel) {
        Tiers tiers = TiersConverter.convertToDomain(tiersSimpleModel);

        Affilie affilie = new Affilie(tiers);
        affilie.setId(affiliationSimpleModel.getAffiliationId());
        affilie.setAffilieNumero(affiliationSimpleModel.getAffilieNumero());
        affilie.setBrancheEconomique(affiliationSimpleModel.getBrancheEconomique());
        affilie.setDateDebut(affiliationSimpleModel.getDateDebut());
        affilie.setDateFin(affiliationSimpleModel.getDateFin());
        affilie.setIdTiers(affiliationSimpleModel.getIdTiers());
        affilie.setMotifFin(affiliationSimpleModel.getMotifFin());
        affilie.setPeriodicite(affiliationSimpleModel.getPeriodicite());
        affilie.setPersonnaliteJuridique(affiliationSimpleModel.getPersonnaliteJuridique());
        affilie.setRaisonSociale(affiliationSimpleModel.getRaisonSociale());
        affilie.setRaisonSocialeCourt(affiliationSimpleModel.getRaisonSocialeCourt());
        affilie.setReleveParitaire(affiliationSimpleModel.getReleveParitaire());
        affilie.setRelevePersonnel(affiliationSimpleModel.getRelevePersonnel());
        affilie.setAccesSecurite(affiliationSimpleModel.getAccesSecurite());
        affilie.setSpy(affiliationSimpleModel.getSpy());
        return affilie;
    }
}
