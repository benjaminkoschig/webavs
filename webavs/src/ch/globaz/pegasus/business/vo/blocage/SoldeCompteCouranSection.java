package ch.globaz.pegasus.business.vo.blocage;

import ch.globaz.osiris.business.model.SoldeCompteCourant;

public class SoldeCompteCouranSection extends SoldeCompteCourant {
    private String descriptionSection;

    public String getDescriptionSection() {
        return descriptionSection;
    }

    public void setDescriptionSection(String descriptionSection) {
        this.descriptionSection = descriptionSection;
    }
}
