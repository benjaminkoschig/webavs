/*
 * Cr�� le 18 d�c. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.babel.utils;

import globaz.babel.api.ICTIntervenant;
import globaz.babel.api.cat.ICTCatalogues;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CTCsMapperUtils {

    /**
     * recherche la famille du CS des intervenant pour le CS du domaine document donn�
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
