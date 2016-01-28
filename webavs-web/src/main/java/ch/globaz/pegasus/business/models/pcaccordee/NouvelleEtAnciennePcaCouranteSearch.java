package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

public class NouvelleEtAnciennePcaCouranteSearch extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1L;
    private List<String> inIdsVersionDroit = new ArrayList<String>();
    private String forDateFinPca;

    public String getForDateFinPca() {
        return forDateFinPca;
    }

    public void setForDateFinPca(String forDateFinPca) {
        this.forDateFinPca = forDateFinPca;
    }

    public NouvelleEtAnciennePcaCouranteSearch() {
        super();
    }

    public List<String> getInIdsVersionDroit() {
        return inIdsVersionDroit;
    }

    public void setInIdsVersionDroit(List<String> inIdsVersionDroit) {
        this.inIdsVersionDroit = inIdsVersionDroit;
    }

    @Override
    public Class<NouvelleEtAnciennePcaCourante> whichModelClass() {
        return NouvelleEtAnciennePcaCourante.class;
    }

}
