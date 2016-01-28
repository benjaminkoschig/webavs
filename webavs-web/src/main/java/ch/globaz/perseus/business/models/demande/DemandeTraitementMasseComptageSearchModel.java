package ch.globaz.perseus.business.models.demande;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DemandeTraitementMasseComptageSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Search definition
    private String forExecutionProcess = null;
    private String forKeyProperty = null;

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public String getForExecutionProcess() {
        return forExecutionProcess;
    }

    public void setForExecutionProcess(String forExecutionProcess) {
        this.forExecutionProcess = forExecutionProcess;
    }

    public String getForKeyProperty() {
        return forKeyProperty;
    }

    public void setForKeyProperty(String forKeyProperty) {
        this.forKeyProperty = forKeyProperty;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    @Override
    public Class<DemandeTraitementMasseComptage> whichModelClass() {
        return DemandeTraitementMasseComptage.class;
    }

}
