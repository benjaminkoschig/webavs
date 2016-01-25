package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleAnnonceLapramsDonneeFinanciereHeaderSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> inIdsAnnonceHeader = null;

    public List<String> getInIdsAnnonceHeader() {
        return inIdsAnnonceHeader;
    }

    public void setInIdsAnnonceHeader(List<String> inIdsAnnonceHeader) {
        this.inIdsAnnonceHeader = inIdsAnnonceHeader;
    }

    @Override
    public Class<SimpleAnnonceLapramsDonneeFinanciereHeader> whichModelClass() {
        // TODO Auto-generated method stub
        return SimpleAnnonceLapramsDonneeFinanciereHeader.class;
    }

}
