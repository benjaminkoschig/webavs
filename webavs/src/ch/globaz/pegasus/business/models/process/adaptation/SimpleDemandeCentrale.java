package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDemandeCentrale extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemandeCentral;
    private String idDemandePC;
    private String idProcess;
    private String nss;
    private String referenceInterne;

    @Override
    public String getId() {
        return idDemandeCentral;
    }

    public String getIdDemandeCentral() {
        return idDemandeCentral;
    }

    public String getIdDemandePC() {
        return idDemandePC;
    }

    public String getIdProcess() {
        return idProcess;
    }

    public String getNss() {
        return nss;
    }

    public String getReferenceInterne() {
        return referenceInterne;
    }

    @Override
    public void setId(String id) {
        idDemandeCentral = id;
    }

    public void setIdDemandeCentral(String idDemandeCentral) {
        this.idDemandeCentral = idDemandeCentral;
    }

    public void setIdDemandePC(String idDemandePC) {
        this.idDemandePC = idDemandePC;
    }

    public void setIdProcess(String idProcess) {
        this.idProcess = idProcess;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setReferenceInterne(String referenceInterne) {
        this.referenceInterne = referenceInterne;
    }
}
