package ch.globaz.pegasus.business.models.dettecomptatcompense;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleDetteComptatCompenseSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroit = null; // Attention n'est pas utiliser dans la recherche pour les
                                      // SimpleDetteComptatCompense. Ceci est utiliser pour retrouver les dettes en
                                      // comptats
    private String forIdVersionDroit = null;
    private boolean forIsCompens = false;

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public boolean getForIsCompens() {
        return forIsCompens;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForIsCompens(boolean forIsCompens) {
        this.forIsCompens = forIsCompens;
    }

    @Override
    public Class<SimpleDetteComptatCompense> whichModelClass() {
        return SimpleDetteComptatCompense.class;
    }
}
