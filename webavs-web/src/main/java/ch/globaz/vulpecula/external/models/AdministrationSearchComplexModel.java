package ch.globaz.vulpecula.external.models;

import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

public class AdministrationSearchComplexModel extends ch.globaz.pyxis.business.model.AdministrationSearchComplexModel {
    private static final long serialVersionUID = -3708516069261645626L;

    private String forDesignation1Like;
    private String forDesignation2Like;

    private List<String> forIdTiersAdministrationIn;

    @Override
    public void setForCodeAdministrationLike(String forCodeAdministrationLike) {
        if (!JadeStringUtil.isEmpty(forCodeAdministrationLike)) {
            super.setForCodeAdministrationLike(JadeStringUtil.toUpperCase(forCodeAdministrationLike));
        }
    }

    public void setForDesignationLike(String forDesignationLike) {
        if (!JadeStringUtil.isEmpty(forDesignationLike)) {
            String designationUpper = "%" + JadeStringUtil.toUpperCase(forDesignationLike);
            setForDesignation1Like(designationUpper);
            setForDesignation2Like(designationUpper);
        }

    }

    @Override
    public final String getForDesignation1Like() {
        return forDesignation1Like;
    }

    @Override
    public final void setForDesignation1Like(String forDesignation1Like) {
        this.forDesignation1Like = forDesignation1Like;
    }

    public final String getForDesignation2Like() {
        return forDesignation2Like;
    }

    public final void setForDesignation2Like(String forDesignation2Like) {
        this.forDesignation2Like = forDesignation2Like;
    }

    public List<String> getForIdTiersAdministrationIn() {
        return forIdTiersAdministrationIn;
    }

    public void setForIdTiersAdministrationIn(List<String> forIdTiersAdministrationIn) {
        this.forIdTiersAdministrationIn = forIdTiersAdministrationIn;
    }

    @Override
    public Class<?> whichModelClass() {
        return AdministrationComplexModel.class;
    }
}
