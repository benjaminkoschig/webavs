/*
 * Cr�� le 17 f�vr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.interets;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author jts 17 f�vr. 05 11:38:30
 */
public class CADetailInteretMoratoireListViewBean extends CADetailInteretMoratoireManager implements
        FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        CADetailInteretMoratoireViewBean viewBean = new CADetailInteretMoratoireViewBean();
        viewBean.setDomaine(getForDomaine());
        return viewBean;
    }
}
