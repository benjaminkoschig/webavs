package globaz.naos.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;

/**
 * Moniteur sur les historiques de Tiers (TIHISTP), permettant d'insérer toute modification dans les annonces de
 * l'affiliation.
 */
public class AFMoniteurAVS extends AFMoniteurTiersAbs {
    // type de mutations numéro AVS
    // public static final String TIERS = CodeSystem.CHAMPS_MOD_HIST_TIER; //
    // "813025";
    @Override
    public void createAnnonce(BEntity entity) throws Exception {
        TIHistoriqueAvs historique = (TIHistoriqueAvs) entity;
        this.createAnnonceToAff(historique, historique.getIdTiers(), CodeSystem.CHAMPS_MOD_TIER_AVS,
                historique.getNumAvs());
    }

}
