package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RenteAdapationDemandeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ORDER_PROCESS_GROUPE = "processGroupe";

    private String forIdDemandePC;
    private String forIdProcess;

    public String getForIdDemandePC() {
        return forIdDemandePC;
    }

    public String getForIdProcess() {
        return forIdProcess;
    }

    public void setForIdDemandePC(String forIdDemandePC) {
        this.forIdDemandePC = forIdDemandePC;
    }

    public void setForIdProcess(String forIdProcess) {
        this.forIdProcess = forIdProcess;
    }

    @Override
    public Class<RenteAdapationDemande> whichModelClass() {
        return RenteAdapationDemande.class;
    }

}
