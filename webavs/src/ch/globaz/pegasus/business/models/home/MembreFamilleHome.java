package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

/**
 * 
 * Modele complexe retournant le type de home (SASH,SPAS). Utilisé uniquement par les RFM
 * 
 * @author jje
 * 
 */
public class MembreFamilleHome extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csServiceEtat = "";
    private SimpleVersionDroit simpleVersionDroit;

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    public MembreFamilleHome() {
        super();
    }

    public String getCsServiceEtat() {
        return csServiceEtat;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCsServiceEtat(String csServiceEtat) {
        this.csServiceEtat = csServiceEtat;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub
    }

}
