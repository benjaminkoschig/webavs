/*
 * Cr�� le 18 janv. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.interets;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author jts 18 janv. 05 10:27:22
 */
public class CAApercuInteretMoratoireListViewBean extends CAApercuInteretMoratoireManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.interets.CAApercuInteretMoratoireManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAApercuInteretMoratoireViewBean();
    }

    public String getIdTiers(int pos) {
        return ((CAApercuInteretMoratoire) getEntity(pos)).getIdTiers();
    }

}
