package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class LigneTaxationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private String forIdTaxationOffice;
    private String forIdCotisation;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTaxationOffice() {
        return forIdTaxationOffice;
    }

    public void setForIdTaxationOffice(String forIdTaxationOffice) {
        this.forIdTaxationOffice = forIdTaxationOffice;
    }

    public String getForIdCotisation() {
        return forIdCotisation;
    }

    public void setForIdCotisation(String forIdCotisation) {
        this.forIdCotisation = forIdCotisation;
    }

    @Override
    public Class<LigneTaxationComplexModel> whichModelClass() {
        return LigneTaxationComplexModel.class;
    }
}
