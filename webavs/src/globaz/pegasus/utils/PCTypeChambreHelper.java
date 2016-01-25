/*
 * Créé le 9 ovembre 2009
 */
package globaz.pegasus.utils;

import globaz.globall.db.BSession;
import ch.globaz.pegasus.business.models.home.TypeChambre;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author bsc
 */
public class PCTypeChambreHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Donne la description du tier si particulairté est vrai
     * 
     * @return designation de la personne (si particulairté)
     */
    public static String getTierDesignation(TypeChambre typechambre) {
        String tier = null;
        if ((typechambre != null) && typechambre.getSimpleTypeChambre().getIsParticularite().booleanValue()) {
            tier = typechambre.getPersonneEtendue().getTiers().getDesignation1().trim() + ", "
                    + typechambre.getPersonneEtendue().getTiers().getDesignation2().trim();
        }
        return tier;
    }

    /**
     * Donne la description ()
     * 
     * @return le typeChambre designation
     */
    public static String getTypeChambreDescription(TypeChambre typechambre, BSession session) {
        String api = (typechambre.getSimpleTypeChambre().getIsApiFacturee()) ? session
                .getLabel("JSP_PC_PARAM_TYPE_CHAMBRE_LISTE_API_FACT") : session
                .getLabel("JSP_PC_PARAM_TYPE_CHAMBRE_LISTE_API_NON_FACT");
        String designation = PCTypeChambreHelper.getTypeChambreDesignation(typechambre) + " - " + api + " ("
                + session.getCode(typechambre.getSimpleTypeChambre().getCsCategorie()) + ")";

        return designation;
    }

    /**
     * Donne la designation d'un type de chambre ("Designation" + si particularite "Nom, prénom")
     * 
     * @return la designation du type de chambre
     */

    public static String getTypeChambreDesignation(TypeChambre typechambre) {

        String designation = typechambre.getSimpleTypeChambre().getDesignation();
        String tier = PCTypeChambreHelper.getTierDesignation(typechambre);
        if (tier != null) {
            designation += " - " + tier;
        }
        return designation;
    }
}
