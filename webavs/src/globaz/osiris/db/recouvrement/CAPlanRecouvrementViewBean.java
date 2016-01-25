package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;

/**
 * Repr�sente le model de la vue "_de".
 * 
 * @author Arnaud Dostes, 20-apr-2005
 */
public class CAPlanRecouvrementViewBean extends CAPlanRecouvrement implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne la valeur format�e. Si cette derni�re est d�j� formatt�e aucune op�ration ne sera effectu�e pour la
     * reformater.
     * 
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getAcompteFormate() {
        if (getAcompte().indexOf("'") > -1) {
            return getAcompte();
        } else {
            return JANumberFormatter.formatNoRound(getAcompte(), 2);
        }
    }

    /**
     * Retourne la valeur format�e. Si cette derni�re est d�j� formatt�e aucune op�ration ne sera effectu�e pour la
     * reformater.
     * 
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getPlafondFormate() {
        if (getPlafond().indexOf("'") > -1) {
            return getPlafond();
        } else {
            return JANumberFormatter.formatNoRound(getPlafond(), 2);
        }
    }

    /**
     * Retourne la valeur format�e. Si cette derni�re est d�j� formatt�e aucune op�ration ne sera effectu�e pour la
     * reformater.
     * 
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getPremierAcompteFormate() {
        if (getPremierAcompte().indexOf("'") > -1) {
            return getPremierAcompte();
        } else {
            return JANumberFormatter.formatNoRound(getPremierAcompte(), 2);
        }
    }

}
