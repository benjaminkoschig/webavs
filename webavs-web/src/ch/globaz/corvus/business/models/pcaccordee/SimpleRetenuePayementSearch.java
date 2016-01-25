/**
 * 
 */
package ch.globaz.corvus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author LFO
 * 
 */
public class SimpleRetenuePayementSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdRente = null;
    private List<String> inIdsRente = null;

    public String getForIdRente() {
        return forIdRente;
    }

    public List<String> getInIdsRente() {
        return inIdsRente;
    }

    public void setForIdRente(String forIdRente) {
        this.forIdRente = forIdRente;
    }

    public void setInIdsRente(List<String> inIdsRente) {
        this.inIdsRente = inIdsRente;
    }

    @Override
    public Class<SimpleRetenuePayement> whichModelClass() {
        return SimpleRetenuePayement.class;
    }

}
