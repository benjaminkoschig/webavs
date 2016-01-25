package ch.globaz.pegasus.business.models.summary;

import globaz.jade.persistence.model.JadeComplexModel;

public class SummaryRente extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csApi;
    private String csRenteAvsAi;

    public String getCsApi() {
        return csApi;
    }

    public String getCsRenteAvsAi() {
        return csRenteAvsAi;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCsApi(String csApi) {
        this.csApi = csApi;
    }

    public void setCsRenteAvsAi(String csRenteAvsAi) {
        this.csRenteAvsAi = csRenteAvsAi;
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public void setSpy(String spy) {
    }

}
