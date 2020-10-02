package ch.globaz.pegasus.business.models.assurancemaladie;

import globaz.jade.persistence.model.JadeSearchComplexModel;

import java.util.List;

public class AssuranceMaladieSearch extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1L;

    public final static String FOR_ALL_VALABLE_LE = "forAllValableAssuranceMaladie";

    private String forDateValable = null;
    private String forIdDroit = null;
    private String forIdEntity = null;
    private String forNumeroVersion = null;
    private List<String> inCsTypeDonneeFinancierer = null;

    public String getForDateValable() {
        return forDateValable;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public String getForIdEntity() {
        return forIdEntity;
    }

    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    public List<String> getInCsTypeDonneeFinancierer() {
        return inCsTypeDonneeFinancierer;
    }

    public void setInCsTypeDonneeFinancierer(List<String> inCsTypeDonneeFinancierer) {
        this.inCsTypeDonneeFinancierer = inCsTypeDonneeFinancierer;
    }

    @Override
    public Class whichModelClass() {
        return AssuranceMaladie.class;
    }
}
