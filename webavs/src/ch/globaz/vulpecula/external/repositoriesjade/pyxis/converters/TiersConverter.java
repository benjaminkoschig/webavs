package ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters;

import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/***
 * Convertisseur de {@link TiersSimpleModel} Tiers <-> {@link Tiers}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public final class TiersConverter {

    /***
     * Constructeur privé empêchant l'instanciation de la classe
     */
    private TiersConverter() {

    }

    /***
     * Conversion d'un {@link Tiers} en {@link TiersSimpleModel} pouvant être
     * manipulé pour effectuer des actions en base de données.
     * 
     * @param tiers
     *            {@link Tiers} représentant un objet du domaine
     * @return {@link TiersSimpleModel}
     */
    public static TiersSimpleModel convertToPersistence(final Tiers tiers) {
        TiersSimpleModel tiersSimpleModel = new TiersSimpleModel();
        tiersSimpleModel.setDesignation1(tiers.getDesignation1());
        tiersSimpleModel.setDesignation2(tiers.getDesignation2());
        tiersSimpleModel.setDesignation3(tiers.getDesignation3());
        tiersSimpleModel.setDesignation4(tiers.getDesignation4());
        tiersSimpleModel.setDesignationCourt(tiers.getDesignationCourt());
        tiersSimpleModel.setDesignationUpper1(tiers.getDesignationUpper1());
        tiersSimpleModel.setDesignationUpper2(tiers.getDesignationUpper2());
        tiersSimpleModel.setId(String.valueOf(tiers.getId()));
        tiersSimpleModel.setIdPays(tiers.getPays().getId());
        tiersSimpleModel.setIdTiersExterne(tiers.getIdTiersExterne());
        tiersSimpleModel.setLangue(tiers.getLangue());
        tiersSimpleModel.setPolitesseSpecDe(tiers.getPolitesseSpecDe());
        tiersSimpleModel.setPolitesseSpecFr(tiers.getPolitesseSpecFr());
        tiersSimpleModel.setPolitesseSpecIt(tiers.getPolitesseSpecIt());
        tiersSimpleModel.setTitreTiers(tiers.getTitreTiers());
        tiersSimpleModel.setTypeTiers(tiers.getTypeTiers());
        return tiersSimpleModel;
    }

    /**
     * Conversion d'un objet {@link TiersSimpleModel} en objet du domaine {@link Tiers}
     * 
     * @param tiersSimpleModel
     *            {@link TiersSimpleModel}
     * @return {@link Tiers} représente un tiers du module Pyxis
     */
    public static Tiers convertToDomain(final TiersSimpleModel tiersSimpleModel) {
        Tiers tiers = new Tiers();
        tiers.setDesignation1(tiersSimpleModel.getDesignation1());
        tiers.setDesignation2(tiersSimpleModel.getDesignation2());
        tiers.setDesignation3(tiersSimpleModel.getDesignation3());
        tiers.setDesignation4(tiersSimpleModel.getDesignation4());
        tiers.setDesignationCourt(tiersSimpleModel.getDesignationCourt());
        tiers.setDesignationUpper1(tiersSimpleModel.getDesignationUpper1());
        tiers.setDesignationUpper2(tiersSimpleModel.getDesignationUpper2());
        tiers.setId(tiersSimpleModel.getId());
        tiers.setIdTiersExterne(tiersSimpleModel.getIdTiersExterne());
        tiers.setLangue(tiersSimpleModel.getLangue());
        tiers.setPolitesseSpecDe(tiersSimpleModel.getPolitesseSpecDe());
        tiers.setPolitesseSpecFr(tiersSimpleModel.getPolitesseSpecFr());
        tiers.setPolitesseSpecIt(tiersSimpleModel.getPolitesseSpecIt());
        tiers.setTitreTiers(tiersSimpleModel.getTitreTiers());
        tiers.setTypeTiers(tiersSimpleModel.getTypeTiers());
        return tiers;
    }
}
