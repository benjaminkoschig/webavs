package globaz.helios.db.modeles;

import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (09.09.2002 15:09:50)
 * 
 * @author: Administrator
 */

public class CGLigneModeleEcritureCollectiveViewBean extends CGLigneModeleEcriture implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idMandat = new String();
    private String idModeleEcriture = new String();

    /**
     * Commentaire relatif au constructeur CGModeleEcritureViewBean.
     */
    public CGLigneModeleEcritureCollectiveViewBean() {
        super();
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idModeleEcriture.
     * 
     * @return String
     */
    public String getIdModeleEcriture() {
        return idModeleEcriture;
    }

    public CGExerciceComptable getLastExercice() {
        CGExerciceComptableManager mgr = new CGExerciceComptableManager();
        mgr.setForIdMandat(getIdMandat());
        mgr.setSession(getSession());
        mgr.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
        try {
            mgr.find(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mgr.size() > 0) {
            return (CGExerciceComptable) mgr.getEntity(0);
        } else {
            return null;
        }
    }

    public CGModeleEcriture retrieveModeleEcriture() {

        if (JadeStringUtil.isBlank(getIdModeleEcriture())) {
            return null;
        } else if (JadeStringUtil.isBlank(getIdMandat())) {
            return null;
        } else {
            CGModeleEcriture modele = new CGModeleEcriture();
            modele.setSession(getSession());
            modele.setIdModeleEcriture(getIdModeleEcriture());
            modele.setIdMandat(getIdMandat());
            try {
                modele.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return modele;
        }
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the idModeleEcriture.
     * 
     * @param idModeleEcriture
     *            The idModeleEcriture to set
     */
    public void setIdModeleEcriture(String idModeleEcriture) {
        this.idModeleEcriture = idModeleEcriture;
    }

}
