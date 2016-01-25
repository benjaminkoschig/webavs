package globaz.naos.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;

/**
 * Moniteur sur les historiques de Tiers (TIHISTP), permettant d'ins�rer toute modification dans les annonces de
 * l'affiliation.
 */
public class AFMoniteurContribuable extends AFMoniteurTiersAbs {
    // type de mutations num�ro de contribuable
    // public static final String TIERS = CodeSystem.CHAMPS_MOD_HIST_TIER; //
    // "813025";
    @Override
    public void createAnnonce(BEntity entity) throws Exception {
        TIHistoriqueContribuable historique = (TIHistoriqueContribuable) entity;
        this.createAnnonceToAff(historique, historique.getIdTiers(), CodeSystem.CHAMPS_MOD_TIER_CONTRIB,
                historique.getNumContribuable());
    }

}
