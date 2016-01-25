package ch.globaz.pegasus.business.vo.pcaccordee;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author BSC
 * 
 */
public class PlanDeCalculVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List enfantsDansCalcul = null;
    private String excedentPCAnnuel = null;
    private Boolean isPlanRetenu = null;

    private String montantPCMensuelle = null;

    /**
     * Creation d'un PlanDeCalculVO
     * 
     * @param montantPCMensuelle
     * @param excedentRevenus
     * @param excedentDepenses
     * @param isPlanRetenu
     */
    public PlanDeCalculVO(String montantPCMensuelle, String excedentPCAnnuel, Boolean isPlanRetenu) {
        super();
        this.montantPCMensuelle = montantPCMensuelle;
        this.excedentPCAnnuel = excedentPCAnnuel;
        this.isPlanRetenu = isPlanRetenu;
    }

    /**
     * @return la liste des enfants compris dans les calculs comparatifs
     */
    public List getEnfantsDansCalcul() {
        return enfantsDansCalcul;
    }

    public String getExcedentPCAnnuel() {
        return excedentPCAnnuel;
    }

    /**
     * @return the isPlanRetenu
     */
    public Boolean getIsPlanRetenu() {
        return isPlanRetenu;
    }

    /**
     * @return the montantPCMensuelle
     */
    public String getMontantPCMensuelle() {
        return montantPCMensuelle;
    }

    /**
     * @param enfantsDansCalcul
     */
    public void setEnfantsDansCalcul(List enfantsDansCalcul) {
        this.enfantsDansCalcul = enfantsDansCalcul;
    }

    public void setExcedentPCAnnuel(String excedentPCAnnuel) {
        this.excedentPCAnnuel = excedentPCAnnuel;
    }

    /**
     * @param isPlanRetenu
     *            the isPlanRetenu to set
     */
    public void setIsPlanRetenu(Boolean isPlanRetenu) {
        this.isPlanRetenu = isPlanRetenu;
    }

    /**
     * @param montantPCMensuelle
     *            the montantPCMensuelle to set
     */
    public void setMontantPCMensuelle(String montantPCMensuelle) {
        this.montantPCMensuelle = montantPCMensuelle;
    }
}
