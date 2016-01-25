/*
 * Créé le 16 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.vb.famille;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.hera.db.famille.SFPeriodeManager;

/**
 * @author jpa
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFPeriodeListViewBean extends SFPeriodeManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFPeriodeViewBean();
    }

}
