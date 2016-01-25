package globaz.naos.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.webavs.common.ICommonConstantes;

/**
 * Moniteur sur les adresses de paiements, permettant d'insérer toute modification dans les annonces de l'affiliation.
 */
public class AFMoniteurAdressePaiement extends AFMoniteurTiersAbs {
    // type de mutations adresse courrier
    public static final String ADR_PAIEMENT = CodeSystem.CHAMPS_MOD_ADRESSE_PAIEMENT; // "813027";
    // domaine standard
    public static final String DOM_STANDARD = "519004";

    @Override
    public void createAnnonce(BEntity entity) throws Exception {
        TIAvoirPaiement avoirPai = (TIAvoirPaiement) entity;
        if (AFMoniteurAdressePaiement.DOM_STANDARD.equals(avoirPai.getIdApplication())
                || ICommonConstantes.CS_APPLICATION_COTISATION.equals(avoirPai.getIdApplication())) {
            // annonce
            this.createAnnonceToAff(avoirPai, avoirPai.getIdTiers(), AFMoniteurAdressePaiement.ADR_PAIEMENT, null);
        }
    }
}
