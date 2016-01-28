package globaz.helios.db.modeles;

import globaz.helios.db.comptes.CGMandat;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (09.09.2002 15:09:50)
 * 
 * @author: Administrator
 */

public class CGModeleEcritureViewBean extends CGModeleEcriture implements globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGModeleEcritureViewBean.
     */
    public CGModeleEcritureViewBean() {
        super();
    }

    public CGMandat getMandat() {
        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());

        try {
            mandat.retrieve();

            if (mandat.isNew()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return mandat;
    }

    /**
     * Le modèle d'écritures contient-il des entêtes d'écritures, soit des écritures ?
     * 
     * @return
     */
    public boolean hasEnteteModele() {
        if (JadeStringUtil.isIntegerEmpty(getIdModeleEcriture())) {
            return false;
        }

        CGEnteteModeleEcritureManager manager = new CGEnteteModeleEcritureManager();
        manager.setSession(getSession());

        manager.setForIdModeleEcriture(getIdModeleEcriture());

        try {
            return (manager.getCount() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
