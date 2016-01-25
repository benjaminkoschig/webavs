package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Classe : type_conteneur Description : Date de création: 7 juin 04
 * 
 * @author scr
 */
public class CAPaiementEtrangerViewBean extends CAPaiementEtranger implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String noAvs1 = new String();
    private String noAvs2 = new String();
    private String noAvs3 = new String();
    private String noAvs4 = new String();

    private Boolean simulation = new Boolean(true);

    /**
     * Constructor for CAPaiementEtrangerViewBean.
     */
    public CAPaiementEtrangerViewBean() {
        super();
    }

    /**
     * Returns the noAvs1.
     * 
     * @return String
     */
    public String getNoAvs1() {
        return noAvs1;
    }

    /**
     * Returns the noAvs2.
     * 
     * @return String
     */
    public String getNoAvs2() {
        return noAvs2;
    }

    /**
     * Returns the noAvs3.
     * 
     * @return String
     */
    public String getNoAvs3() {
        return noAvs3;
    }

    /**
     * Returns the noAvs4.
     * 
     * @return String
     */
    public String getNoAvs4() {
        return noAvs4;
    }

    /**
     * Returns the simulation.
     * 
     * @return Boolean
     */
    public Boolean getSimulation() {
        return simulation;
    }

    public boolean isSimulationMode() {
        return simulation.booleanValue();
    }

    /**
     * Sets the noAvs1.
     * 
     * @param noAvs1
     *            The noAvs1 to set
     */
    public void setNoAvs1(String noAvs1) {
        this.noAvs1 = noAvs1;
    }

    /**
     * Sets the noAvs2.
     * 
     * @param noAvs2
     *            The noAvs2 to set
     */
    public void setNoAvs2(String noAvs2) {
        this.noAvs2 = noAvs2;
    }

    /**
     * Sets the noAvs3.
     * 
     * @param noAvs3
     *            The noAvs3 to set
     */
    public void setNoAvs3(String noAvs3) {
        this.noAvs3 = noAvs3;
    }

    /**
     * Sets the noAvs4.
     * 
     * @param noAvs4
     *            The noAvs4 to set
     */
    public void setNoAvs4(String noAvs4) {
        this.noAvs4 = noAvs4;
    }

    /**
     * Sets the simulation.
     * 
     * @param simulation
     *            The simulation to set
     */
    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

}
