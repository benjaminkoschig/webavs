/**
 * 
 */
package ch.globaz.al.business.constantes;

/**
 * @author pta
 * 
 *         Constantes liées au aux attestations de versements
 * 
 */
public interface ALConstAttestattionVersees {
    /**
     * type de prestations à rechercher: paiement direct et indirect
     */
    public static final String ATTEST_PRESTA_DIR_INDIR = "ATTEST_PRESTA_DIR_IND";
    /**
     * type de prestations à rechercher: paiement direct et tiers bénéficiaire
     */
    public static final String ATTEST_PRESTA_DIR_TIERS_BEN = "ATTEST_PRESTA_DIR_TB";
    /**
     * type de prestations à rechercher: paiement direct
     */
    public static final String ATTEST_PRESTA_DIRECT = "ATTEST_PRESTA_DIR";
    /**
     * type de prestations à rechercher: TOUTES (Indirect, direct et tiers bénéficiaire
     */
    public static final String ATTEST_PRESTA_IND_DIR_TIERS_BEN = "ATTEST_PRESTA_IND_DIR_TB";
    /**
     * type de presations à rechercher: paiement indirect et tiers bénéficiaire
     */
    public static final String ATTEST_PRESTA_IND_TIERS_BEN = "ATTEST_PRESTA_IND_TB";
    /**
     * type de prestations à rechercher: paiement indirect
     */
    public static final String ATTEST_PRESTA_INDIRECT = "ATTEST_PRESTA_IND";
    /**
     * type de prestations à rechercher: paiement à tiers bénéficiaire
     */
    public static final String ATTEST_PRESTA_TIERS_BEN = "ATTEST_PRESTA_TB";
    /**
     * type de recherche par AFFILIE et NSS ALLOC
     */
    public static final String ATTEST_RECHERCHE_AFF_NSS_ALLOC = "RECH_AFFIL_NSS_ALLOC";
    /**
     * type de recherche par AFFILIE
     */
    public static final String ATTEST_RECHERCHE_AFFILIE = "RECH_AFFILIE";
    /**
     * type de recherche par NSS Allocataire
     */
    public static final String ATTEST_RECHERCHE_NSS_ALLOC = "RECH_NSS_ALLOC";
    /**
     * type de document détaillé ET par période (2 documents)
     */
    public static final String ATTEST_TYPE_DOC_DET_ET_NON_DET = "DOC_DET_NON_DET";
    /**
     * type de document détaillé par période et prestations allocataire
     */
    public static final String ATTEST_TYPE_DOC_DET_PERIODE = "DOC_DET";
    /**
     * type de document non détaille par période
     */
    public static final String ATTEST_TYPE_DOC_NON_DETAILLE = "DOC_NON_DET";
};
