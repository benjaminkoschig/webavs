package globaz.naos.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TICompositionTiers;

/**
 * Moniteur sur le conjoint de Tiers (TICTIEP), permettant d'insérer toute modification dans les annonces de
 * l'affiliation.
 */
public class AFMoniteurLiaison extends AFMoniteurTiersAbs {
    // type de mutations agence AVS
    public static final String AGENCE_AVS = CodeSystem.CHAMPS_MOD_AGENCE_AVS; // "813029";
    // type de mutations conjoint
    public static final String CONJOINT = CodeSystem.CHAMPS_MOD_TIER_CONJOINT; // "813028";
    // lien agence
    public static final String LIEN_AGENCE = "507007";
    public static final String LIEN_AGENCE_PRIVE = "507008";

    @Override
    public void createAnnonce(BEntity entity) throws Exception {
        // créer annonce seulement si type lien = conjoint
        TICompositionTiers conjoint = (TICompositionTiers) entity;
        if (TICompositionTiers.CS_CONJOINT.equals(conjoint.getTypeLien())) {
            // annonce pour tiers
            this.createAnnonceToAff(conjoint, conjoint.getIdTiersEnfant(), AFMoniteurLiaison.CONJOINT, null);
            // annonce pour conjoint
            this.createAnnonceToAff(conjoint, conjoint.getIdTiersParent(), AFMoniteurLiaison.CONJOINT, null);
        } else if (AFMoniteurLiaison.LIEN_AGENCE.equals(conjoint.getTypeLien())
                || AFMoniteurLiaison.LIEN_AGENCE_PRIVE.equals(conjoint.getTypeLien())) {
            // annonce agence AVS
            this.createAnnonceToAff(conjoint, conjoint.getIdTiersParent(), AFMoniteurLiaison.AGENCE_AVS, null);
        }
    }
}
