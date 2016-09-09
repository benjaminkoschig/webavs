package globaz.prestation.utils.codeprestation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;

/**
 * Cette classe est librement inspirée de RECodePrestationResolver mais s'étend sur toutes les prestation
 * 
 * @author cel
 */
public class PRCodePrestationResolver {

    /**
     * Test le codePrestation passé en paramètre pour déterminer de quelle type de prestation il s'agit.
     * 
     * @param codePrestation
     *            Le code prestation sous forme de String à évaluer
     * @return Le genre de prestation correspondant au code prestation sinon PRTypeCodePrestation.INCONNU
     */
    public static PRTypeCodePrestation getGenreDePrestation(String codePrestation) {
        IPRCodePrestationEnum cpe = PRCodePrestationResolver.getEnumAssocieAuCodePrestation(codePrestation);
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
            } else if (PRCodePrestationPC.isCodePrestationPC(codePrestation)) {
                return PRCodePrestationPC.getCodePrestation(codePrestation);
            } else if (PRCodePrestationRFM.isCodePrestationRFM(codePrestation)) {
                return PRCodePrestationRFM.getCodePrestation(codePrestation);
            }
        }
        return null;
    }
}
