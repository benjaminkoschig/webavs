package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

public class AnnonceLapramsDonneeFinanciereSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> forInIdAnnonceLaprams = new ArrayList<String>();

    public List<String> getForInIdAnnonceLaprams() {
        return forInIdAnnonceLaprams;
    }

    public void setForInIdAnnonceLaprams(List<String> forInIdAnnonceLaprams) {
        this.forInIdAnnonceLaprams = forInIdAnnonceLaprams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return AnnonceLapramsDonneeFinanciere.class;
    }

}
