/*
 * Créé le 17 févr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.interets;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author jts 17 févr. 05 11:38:30
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
