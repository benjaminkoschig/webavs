package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;

/**
 * @author BSC
 * 
 */
public class PCAIdMembreFamilleRetenu extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatPC = null;
    private String idPCAccordee = null;
    private String idTiersBeneficiaire = null;
    private String montantPCMensuelle = null;
    private SimpleDroitMembreFamille simpleDroitMembreFamille = null;
    private SimplePCAccordee simplePCAccordee = null;

    public PCAIdMembreFamilleRetenu() {
        super();
        simpleDroitMembreFamille = new SimpleDroitMembreFamille();
        simplePCAccordee = new SimplePCAccordee();
    }

    public String getEtatPC() {
        return etatPC;
    }

    @Override
    public String getId() {
        return idPCAccordee;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getMontantPCMensuelle() {
        return montantPCMensuelle;
    }

    public SimpleDroitMembreFamille getSimpleDroitMembreFamille() {
        return simpleDroitMembreFamille;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    @Override
    public String getSpy() {
        return "";
    }

    public void setEtatPC(String etatPC) {
        this.etatPC = etatPC;
    }

    @Override
    public void setId(String id) {
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setMontantPCMensuelle(String montantPCMensuelle) {
        this.montantPCMensuelle = montantPCMensuelle;
    }

    public void setSimpleDroitMembreFamille(SimpleDroitMembreFamille simpleDroitMembreFamille) {
        this.simpleDroitMembreFamille = simpleDroitMembreFamille;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    @Override
    public void setSpy(String spy) {
    }

}
