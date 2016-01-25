/**
 * 
 */
package ch.globaz.amal.business.models.journalisation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author LFO
 * 
 */
public class HistoriqueImportationSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forProvenance = null;

    public HistoriqueImportationSearch() {
        forProvenance = new String();
    }

    public String getForProvenance() {
        return forProvenance;
    }

    public void setForProvenance(String forProvenance) {
        this.forProvenance = forProvenance;
    }

    @Override
    public Class whichModelClass() {
        return HistoriqueImportation.class;
    }

}
