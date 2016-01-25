package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

public class RenteMembreFamilleCalcule extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamilleEtendu membreFamilleEtendu;
    private SimpleAllocationImpotent simpleAllocationImpotent;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader;
    private SimpleDroit simpleDroit;
    private SimplePCAccordee simplePCAccordee;
    private SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul;
    private SimplePlanDeCalcul simplePlanDeCalcul;
    private SimpleRenteAvsAi simpleRenteAvsAi;
    private SimpleVersionDroit simpleVersionDroit;

    public RenteMembreFamilleCalcule() {
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        simplePCAccordee = new SimplePCAccordee();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        simpleRenteAvsAi = new SimpleRenteAvsAi();
        simpleAllocationImpotent = new SimpleAllocationImpotent();
        simpleVersionDroit = new SimpleVersionDroit();
        simpleDroit = new SimpleDroit();
        membreFamilleEtendu = new MembreFamilleEtendu();
        simplePersonneDansPlanCalcul = new SimplePersonneDansPlanCalcul();
    }

    @Override
    public String getId() {
        return simpleDonneeFinanciereHeader.getId();
    }

    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    public SimpleAllocationImpotent getSimpleAllocationImpotent() {
        return simpleAllocationImpotent;
    }

    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePersonneDansPlanCalcul getSimplePersonneDansPlanCalcul() {
        return simplePersonneDansPlanCalcul;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    public SimpleRenteAvsAi getSimpleRenteAvsAi() {
        return simpleRenteAvsAi;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        return simpleDonneeFinanciereHeader.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDonneeFinanciereHeader.getId();

    }

    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    public void setSimpleAllocationImpotent(SimpleAllocationImpotent simpleAllocationImpotent) {
        this.simpleAllocationImpotent = simpleAllocationImpotent;
    }

    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePersonneDansPlanCalcul(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul) {
        this.simplePersonneDansPlanCalcul = simplePersonneDansPlanCalcul;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    public void setSimpleRenteAvsAi(SimpleRenteAvsAi simpleRenteAvsAi) {
        this.simpleRenteAvsAi = simpleRenteAvsAi;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        simpleDonneeFinanciereHeader.setSpy(spy);

    }
}
