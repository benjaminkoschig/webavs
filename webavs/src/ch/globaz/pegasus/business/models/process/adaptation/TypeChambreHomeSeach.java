package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TypeChambreHomeSeach extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsCategorie;
    private String forDateValable;
    private String forIdTiers;
    private String forIdTypeChambre;
    private boolean forIsSupprime = false;

    public String getForCsCategorie() {
        return forCsCategorie;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTypeChambre() {
        return forIdTypeChambre;
    }

    public boolean getForIsSupprime() {
        return forIsSupprime;
    }

    public void setForCsCategorie(String forCsCategorie) {
        this.forCsCategorie = forCsCategorie;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTypeChambre(String forIdTypeChambre) {
        this.forIdTypeChambre = forIdTypeChambre;
    }

    public void setForIsSupprime(boolean forIsSupprime) {
        this.forIsSupprime = forIsSupprime;
    }

    @Override
    public Class<TypeChambreHome> whichModelClass() {
        return TypeChambreHome.class;
    }

}
