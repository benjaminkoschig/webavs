package ch.globaz.pegasus.business.models.pcaccordee;

import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import globaz.jade.persistence.model.JadeComplexModel;

public class PcaDecisionHistorisee  extends JadeComplexModel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String idPCAccordee;
    private String idPcaParent;
    private String idVersionDroit;
    
    private SimplePCAccordee simplePCAccordee = null;

    @Override
    public String getId() {
        return simplePCAccordee.getId();
    }
    
    @Override
    public void setId(String id) {
        simplePCAccordee.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);
    }
    
    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public String getIdPcaParent() {
        return idPcaParent;
    }

    public void setIdPcaParent(String idPcaParent) {
        this.idPcaParent = idPcaParent;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }
    
}
