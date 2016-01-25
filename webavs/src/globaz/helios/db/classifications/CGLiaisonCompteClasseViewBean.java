package globaz.helios.db.classifications;

import java.util.Vector;

public class CGLiaisonCompteClasseViewBean extends CGLiaisonCompteClasse implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Vector currentClassesComptes = null;
    private Vector newClassesComptes = null;

    /**
     * Commentaire relatif au constructeur CGClassificationViewBean.
     */
    public CGLiaisonCompteClasseViewBean() {
        super();
    }

    /**
     * Returns the currentClassesComptes.
     * 
     * @return Vector
     */
    public Vector getCurrentClassesComptes() {
        return currentClassesComptes;
    }

    /**
     * Returns the newClassesComptes.
     * 
     * @return Vector
     */
    public Vector getNewClassesComptes() {
        return newClassesComptes;
    }

    /**
     * Sets the currentClassesComptes.
     * 
     * @param currentClassesComptes
     *            The currentClassesComptes to set
     */
    public void setCurrentClassesComptes(Vector currentClassesComptes) {
        this.currentClassesComptes = currentClassesComptes;
    }

    /**
     * Sets the newClassesComptes.
     * 
     * @param newClassesComptes
     *            The newClassesComptes to set
     */
    public void setNewClassesComptes(Vector newClassesComptes) {
        this.newClassesComptes = newClassesComptes;
    }

}
