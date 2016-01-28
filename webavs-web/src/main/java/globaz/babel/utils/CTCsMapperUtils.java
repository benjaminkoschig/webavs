/*
 * Créé le 18 déc. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.utils;

import globaz.babel.api.ICTIntervenant;
import globaz.babel.api.cat.ICTCatalogues;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTCsMapperUtils {

    /**
     * recherche la famille du CS des intervenant pour le CS du domaine document donné
     * 
     * @param csDomaineDocument
     * @return
     */
    public static String getCsIntervanantTypeForCsDomaineCatalogue(String csDomaineDocument) {
        String result = "";

        // recherche la famille du CS des intervenant pour ce domaine de
        // document
        if (ICTCatalogues.CS_DOCUMENT_DOMAINE_APG.equals(csDomaineDocument)) {
            result = ICTIntervenant.NOM_CS_GROUPE_GENRE_INTERVENANT_APG;
        } else if (ICTCatalogues.CS_DOCUMENT_DOMAINE_MATERNITE.equals(csDomaineDocument)) {
            result = ICTIntervenant.NOM_CS_GROUPE_GENRE_INTERVENANT_MAT;
        } else if (ICTCatalogues.CS_DOCUMENT_DOMAINE_IJ.equals(csDomaineDocument)) {
            result = ICTIntervenant.NOM_CS_GROUPE_GENRE_INTERVENANT_IJ;
        }

        return result;
    }

    /**
	 * 
	 */
    public CTCsMapperUtils() {
        super();
    }

}
