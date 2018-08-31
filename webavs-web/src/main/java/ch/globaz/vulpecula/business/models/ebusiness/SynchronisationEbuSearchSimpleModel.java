package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SynchronisationEbuSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -82638725696538266L;

    private boolean forDateSynchronisationIsEmpty;
    private String forId;
    private String forIdDecompte;

    @Override
    public Class<SynchronisationEbuSimpleModel> whichModelClass() {
        return SynchronisationEbuSimpleModel.class;
    }

    /**
     * @return the forDateSynchronisationIsEmpty
     */
    public boolean isForDateSynchronisationIsEmpty() {
        return forDateSynchronisationIsEmpty;
    }

    /**
     * @param forDateSynchronisationIsEmpty the forDateSynchronisationIsEmpty to set
     */
    public void setForDateSynchronisationIsEmpty(boolean forDateSynchronisationIsEmpty) {
        this.forDateSynchronisationIsEmpty = forDateSynchronisationIsEmpty;
    }

    /**
     * @return the forId
     */
    public String getForId() {
        return forId;
    }

    /**
     * @param forId the forId to set
     */
    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdDecompte() {
        return forIdDecompte;
    }

    public void setForIdDecompte(String forIdDecompte) {
        this.forIdDecompte = forIdDecompte;
    }

}
