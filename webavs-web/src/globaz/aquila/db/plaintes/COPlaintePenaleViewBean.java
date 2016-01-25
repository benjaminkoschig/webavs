/*
 * Cr�� le 27 f�vr. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.plaintes;

import globaz.aquila.db.access.plaintes.COPlaintePenale;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author dvh
 */
public class COPlaintePenaleViewBean extends COPlaintePenale implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getLabelDescriptionPlainte() {
        return getSession().getCodeLibelle(getCsDescriptionPlainte());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getLabelMotifPlainte() {
        return getSession().getCodeLibelle(getCsMotifPlainte());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getLabelTypePlainte() {
        return getSession().getCodeLibelle(getCsTypePlainte());
    }

}
