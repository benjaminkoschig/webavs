/*
 * Cr�� le 26 sept. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.vb.tauxImposition;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.tauxImposition.PRTauxImposition;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class DOTauxImpositionViewBean extends PRTauxImposition implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return
     */
    public String getCSCaisseDefaultCanton() {
        String result;
        try {
            result = getSession().getApplication().getProperty("default.canton.caisse.location");
        } catch (Exception e) {
            return "";
        }

        return JadeStringUtil.isNull(result) ? "" : result;
    }

    /**
     * Donne le libelle du canton
     * 
     * @return
     */
    public String getCsCantonLibelle() {
        return getSession().getCodeLibelle(getCsCanton());
    }

    /**
     * Donne le libelle du type d'impot � la source
     * 
     * @return
     */
    public String getTypeImpotSourceLibelle() {
        return getSession().getCodeLibelle(getTypeImpotSource());
    }

}
