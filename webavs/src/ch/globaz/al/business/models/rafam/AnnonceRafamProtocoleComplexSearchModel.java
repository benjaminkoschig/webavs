package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * @author jts
 * 
 */
public class AnnonceRafamProtocoleComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtat = null;
    private String forTypeAnnonce = null;
    private Collection<String> inCodeRetour = null;
    private Collection<String> inTypeAnnonce = null;

    public String getForEtat() {
        return forEtat;
    }

    public String getForTypeAnnonce() {
        return forTypeAnnonce;
    }

    public Collection<String> getInCodeRetour() {
        return inCodeRetour;
    }

    public Collection<String> getInTypeAnnonce() {
        return inTypeAnnonce;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForTypeAnnonce(String forTypeAnnonce) {
        this.forTypeAnnonce = forTypeAnnonce;
    }

    public void setInCodeRetour(Collection<String> inCodeRetour) {
        this.inCodeRetour = inCodeRetour;
    }

    public void setInTypeAnnonce(Collection<String> inTypeAnnonce) {
        this.inTypeAnnonce = inTypeAnnonce;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AnnonceRafamProtocoleComplexModel> whichModelClass() {
        return AnnonceRafamProtocoleComplexModel.class;
    }
}
