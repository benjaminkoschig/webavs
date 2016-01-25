package globaz.naos.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.webavs.common.ICommonConstantes;

/**
 * Moniteur sur les adresses de courrier, Professionel, Domicile permettant d'insérer toute modification dans les
 * annonces de l'affiliation.
 */
public class AFMoniteurAdresseCourrier extends AFMoniteurTiersAbs {
    // type de mutations adresse courrier
    public static final String ADR_COURRIER = CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER; // "813026";
    // //TODO:
    // Pourquoi
    // ?
    // domaine standard
    public static final String DOM_STANDARD = "519004";
    public static final String TYPE_COURRIER = "508001";
    // type à monitorer
    public static final String TYPE_DOMICILE = "508008";
    public static final String TYPE_EXPLOITATION = "508021";

    @Override
    public void createAnnonce(BEntity entity) throws Exception {
        // créer annonce seulement si domaine standart,
        // type exploitation, courrier ou domicile
        TIAvoirAdresse avoirAdr = (TIAvoirAdresse) entity;
        if (AFMoniteurAdresseCourrier.DOM_STANDARD.equals(avoirAdr.getIdApplication())
                || ICommonConstantes.CS_APPLICATION_COTISATION.equals(avoirAdr.getIdApplication())) {
            if (AFMoniteurAdresseCourrier.TYPE_COURRIER.equals(avoirAdr.getTypeAdresse())) {
                this.createAnnonceToAff(avoirAdr, avoirAdr.getIdTiers(), CodeSystem.CHAMPS_MOD_ADRESSE_COURRIER,
                        avoirAdr.getIdExterne(), null);
            } else if (AFMoniteurAdresseCourrier.TYPE_EXPLOITATION.equals(avoirAdr.getTypeAdresse())) {
                this.createAnnonceToAff(avoirAdr, avoirAdr.getIdTiers(), CodeSystem.CHAMPS_MOD_ADRESSE_PROFES,
                        avoirAdr.getIdExterne(), null);
            } else if (AFMoniteurAdresseCourrier.TYPE_DOMICILE.equals(avoirAdr.getTypeAdresse())) {
                this.createAnnonceToAff(avoirAdr, avoirAdr.getIdTiers(), CodeSystem.CHAMPS_MOD_ADRESSE_DOMICILE,
                        avoirAdr.getIdExterne(), null);
            }
        }
    }
}
