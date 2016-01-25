package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.dossier.SimpleDossier;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

/**
 * @author DMA
 * 
 */
public class PCAccordeePlanCalcul extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDemande simpleDemande = null;
    private SimpleDossier simpleDossier = null;
    private SimpleDroit simpleDroit = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimplePlanDeCalcul simplePlanDeCalcul = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    public PCAccordeePlanCalcul() {
        super();
        simpleDemande = new SimpleDemande();
        simpleDossier = new SimpleDossier();
        simpleDroit = new SimpleDroit();
        simplePCAccordee = new SimplePCAccordee();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        simpleVersionDroit = new SimpleVersionDroit();
    }

    @Override
    public String getId() {
        return simplePCAccordee.getIdPCAccordee();
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public SimpleDossier getSimpleDossier() {
        return simpleDossier;
    }

    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    @Override
    public void setId(String id) {
        simplePCAccordee.setId(id);

    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    public void setSimpleDossier(SimpleDossier simpleDossier) {
        this.simpleDossier = simpleDossier;
    }

    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);
    }
}
