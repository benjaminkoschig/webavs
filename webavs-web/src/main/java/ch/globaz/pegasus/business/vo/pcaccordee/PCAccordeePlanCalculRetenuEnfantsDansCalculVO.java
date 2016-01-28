package ch.globaz.pegasus.business.vo.pcaccordee;

import java.io.Serializable;
import java.util.List;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalcul;

/**
 * 
 * @author BSC
 * 
 */
public class PCAccordeePlanCalculRetenuEnfantsDansCalculVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PCAccordeeVO pcAccordee = null;
    private List<PersonneDansPlanCalcul> personneDansCalcul = null;
    private PlanDeCalculVO planDeCalculRetenu = null;

    /**
     * Construction
     * 
     * @param pcAccordee
     *            la pc accordee
     * @param planDeCalculRetenu
     *            la plan de calcul qui a ete retenu
     * @param enfantsDansCalcul
     *            la liste des enfants du calcul comparatif
     */
    public PCAccordeePlanCalculRetenuEnfantsDansCalculVO(PCAccordeeVO pcAccordee, PlanDeCalculVO planDeCalculRetenu,
            List<PersonneDansPlanCalcul> personneDansCalcul) {
        super();
        this.pcAccordee = pcAccordee;
        this.planDeCalculRetenu = planDeCalculRetenu;
        this.personneDansCalcul = personneDansCalcul;
    }

    /**
     * @return the pcAccordee
     */
    public PCAccordeeVO getPcAccordee() {
        return pcAccordee;
    }

    public List<PersonneDansPlanCalcul> getPersonneDansCalcul() {
        return personneDansCalcul;
    }

    /**
     * @return the planDeCalculRetenu
     */
    public PlanDeCalculVO getPlanDeCalculRetenu() {
        return planDeCalculRetenu;
    }

    /**
     * @param pcAccordee
     *            the pcAccordee to set
     */
    public void setPcAccordee(PCAccordeeVO pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    public void setPersonneDansCalcul(List<PersonneDansPlanCalcul> personneDansCalcul) {
        this.personneDansCalcul = personneDansCalcul;
    }

    /**
     * @param planDeCalcul
     *            the planDeCalculRetenu to set
     */
    public void setPlanDeCalculRetenu(PlanDeCalculVO planDeCalcul) {
        planDeCalculRetenu = planDeCalcul;
    }

}
