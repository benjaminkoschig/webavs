package globaz.helios.db.classifications;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 28 sept. 04
 * 
 * @author scr
 * 
 */
public class CGClasseCompteViewBean extends CGClasseCompte implements globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String selectedNodeId = null;

    /**
     * Constructor for CGClasseCompteViewBean.
     */
    public CGClasseCompteViewBean() {
        super();
    }

    /**
     * Returns the selectedNodeId.
     * 
     * @return String
     */
    public String getSelectedNodeId() {
        return selectedNodeId;
    }

    /**
     * Sets the selectedNodeId.
     * 
     * @param selectedNodeId
     *            The selectedNodeId to set
     */
    public void setSelectedNodeId(String selectedNodeId) {
        this.selectedNodeId = selectedNodeId;
    }

}
