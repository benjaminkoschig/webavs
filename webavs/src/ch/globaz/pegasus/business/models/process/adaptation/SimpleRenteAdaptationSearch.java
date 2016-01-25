package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleRenteAdaptationSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ORDER_PROCESS_GROUPE = "processGroupe";

    private String forIdGroupe;
    private String forIdProcess;

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    public String getForIdProcess() {
        return forIdProcess;
    }

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }

    public void setForIdProcess(String forIdProcess) {
        this.forIdProcess = forIdProcess;
    }

    @Override
    public Class<SimpleRenteAdaptation> whichModelClass() {
        return SimpleRenteAdaptation.class;
    }

}
