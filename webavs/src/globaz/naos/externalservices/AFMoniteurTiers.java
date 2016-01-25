package globaz.naos.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.api.ITIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;

/**
 * Moniteur sur les historiques de Tiers (TIHISTP), permettant d'insérer toute modification dans les annonces de
 * l'affiliation.
 */
public class AFMoniteurTiers extends AFMoniteurTiersAbs {
    // type de mutations moyens de communications
    // public static final String TIERS = CodeSystem.CHAMPS_MOD_HIST_TIER; //
    // "813025";
    @Override
    public void createAnnonce(BEntity entity) throws Exception {
        TIHistoriqueTiers historique = (TIHistoriqueTiers) entity;
        // System.out.println("External AFMoniteurTiers pour champ: "+historique.getChamp());
        if (ITIHistoriqueTiers.FIELD_PAYS.equals(historique.getChamp())) {
            // pays
            this.createAnnonceToAff(historique, historique.getIdTiers(), CodeSystem.CHAMPS_MOD_TIER_ORIG,
                    historique.getValeur());
        } else if (ITIHistoriqueTiers.FIELD_TITRE.equals(historique.getChamp())) {
            // titre
            this.createAnnonceToAff(historique, historique.getIdTiers(), CodeSystem.CHAMPS_MOD_TIER_TITRE,
                    historique.getValeur());
        } else {
            // nom
            this.createAnnonceToAff(historique, historique.getIdTiers(), CodeSystem.CHAMPS_MOD_TIER_NOM,
                    historique.getValeur());
            return;
        }
    }

}
