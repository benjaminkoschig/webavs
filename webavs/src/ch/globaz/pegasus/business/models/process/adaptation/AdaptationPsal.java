package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleCotisationsPsal;

public class AdaptationPsal extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemande = null;
    private SimpleCotisationsPsal simpleCotisationsPsal = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleDroitMembreFamille simpleDroitMembreFamille = null;

    public AdaptationPsal() {
        simpleCotisationsPsal = new SimpleCotisationsPsal();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        simpleDroitMembreFamille = new SimpleDroitMembreFamille();
    }

    @Override
    public String getId() {
        return simpleDonneeFinanciereHeader.getIdDonneeFinanciereHeader();
    }

    public String getIdDemande() {
        return idDemande;
    }

    public SimpleCotisationsPsal getSimpleCotisationsPsal() {
        return simpleCotisationsPsal;
    }

    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    public SimpleDroitMembreFamille getSimpleDroitMembreFamille() {
        return simpleDroitMembreFamille;
    }

    @Override
    public String getSpy() {
        return simpleDonneeFinanciereHeader.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDonneeFinanciereHeader.setId(id);
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setSimpleCotisationsPsal(SimpleCotisationsPsal simpleCotisationsPsal) {
        this.simpleCotisationsPsal = simpleCotisationsPsal;
    }

    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    public void setSimpleDroitMembreFamille(SimpleDroitMembreFamille simpleDroitMembreFamille) {
        this.simpleDroitMembreFamille = simpleDroitMembreFamille;
    }

    @Override
    public void setSpy(String spy) {
        simpleDonneeFinanciereHeader.setSpy(spy);
    }

}
