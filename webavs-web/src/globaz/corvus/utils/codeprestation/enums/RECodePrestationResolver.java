package globaz.corvus.utils.codeprestation.enums;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;

/**
 * Cette classe propose des méthodes utilitaires statiques pour résoudre les caractéristiques d'une prestation en
 * fonction de son code prestation
 * 
 * @author lga
 */
public class RECodePrestationResolver {

    /**
     * Test le codePrestation passé en paramètre pour déterminer de quelle type de prestation il s'agit.
     * 
     * @param codePrestation
     *            Le code prestation sous forme de String à évaluer
     * @return Le genre de prestation correspondant au code prestation sinon PRTypeCodePrestation.INCONNU
     */
    public static PRTypeCodePrestation getGenreDePrestation(String codePrestation) {
        IPRCodePrestationEnum cpe = RECodePrestationResolver.getEnumAssocieAuCodePrestation(codePrestation);
        if (cpe != null) {
            switch (cpe.getTypeDePrestation()) {
                case VIEILLESSE:
                case SURVIVANT:
                case INVALIDITE:
                case API:
                case PC:
                case RFM:
                    return cpe.getTypeDePrestation();
                default:
                    return PRTypeCodePrestation.INCONNU;
            }
        }
        return PRTypeCodePrestation.INCONNU;
    }

    /**
     * Renseigne si le code prestation est un code de prestation principale de type PRTypeCodePrestation.INVALIDITE
     * PRTypeCodePrestation.API PRTypeCodePrestation.SURVIVANT PRTypeCodePrestation.VIEILLESSE
     * 
     * @return True si le code prestation est un code de prestation principale de type (INVALIDITE, SURVIVANT,
     *         VIEILLESSE, API)
     */
    public static boolean isPrestationPrincipale(String codePrestation) {
        IPRCodePrestationEnum cpe = RECodePrestationResolver.getEnumAssocieAuCodePrestation(codePrestation);
        if (cpe != null) {
            return cpe.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Renseigne si le code prestation est un code de prestation ordinaire
     * 
     * @return True si le code prestation est un code de prestation ordinaire
     */
    public static boolean isPrestationOrdinaire(String codePrestation) {
        IPRCodePrestationEnum cpe = RECodePrestationResolver.getEnumAssocieAuCodePrestation(codePrestation);
        if (cpe != null) {
            return cpe.isPrestationOrdinaire();
        }
        return false;
    }

    /**
     * Renseigne si le code prestation est un code de prestation pour enfant
     * 
     * @return True si le code prestation est un code de pour enfant
     */
    public static boolean isPrestationPourEnfant(String codePrestation) {
        IPRCodePrestationEnum cpe = RECodePrestationResolver.getEnumAssocieAuCodePrestation(codePrestation);
        if (cpe != null) {
            return cpe.isPrestationPourEnfant();
        }
        return false;
    }

    /**
     * Peut retourner null
     * 
     * @param codePrestation
     * @return
     */
    public static IPRCodePrestationEnum getEnumAssocieAuCodePrestation(String codePrestation) {
        if (!JadeStringUtil.isEmpty(codePrestation)) {
            if (PRCodePrestationInvalidite.isCodePrestationInvalidite(codePrestation)) {
                return PRCodePrestationInvalidite.getCodePrestation(codePrestation);
            } else if (PRCodePrestationAPI.isCodePrestationAPI(codePrestation)) {
                return PRCodePrestationAPI.getCodePrestation(codePrestation);
            } else if (PRCodePrestationSurvivant.isCodePrestationSurvivant(codePrestation)) {
                return PRCodePrestationSurvivant.getCodePrestation(codePrestation);
            } else if (PRCodePrestationVieillesse.isCodePrestationVieillesse(codePrestation)) {
                return PRCodePrestationVieillesse.getCodePrestation(codePrestation);
            }
        }
        return null;
    }
}
