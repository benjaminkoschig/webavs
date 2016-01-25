package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class RevenusDepenses extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AllocationsFamiliales allocationsFamiliales = null;
    private CotisationsPsal cotisationsPsal = null;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private PensionAlimentaire pensionAlimentaire = null;
    private RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante = null;
    private RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante = null;
    private SimpleAutresRevenus simpleAutresRevenus = null;
    private SimpleContratEntretienViager simpleContratEntretienViager = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager = null;
    private SimpleRevenuHypothetique simpleRevenuHypothetique = null;
    private SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = null;

    /**
	 * 
	 */
    public RevenusDepenses() {
        super();
        revenuActiviteLucrativeDependante = new RevenuActiviteLucrativeDependante();
        membreFamilleEtendu = new MembreFamilleEtendu();
        revenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependante();
        allocationsFamiliales = new AllocationsFamiliales();
        simpleAutresRevenus = new SimpleAutresRevenus();
        simpleContratEntretienViager = new SimpleContratEntretienViager();
        cotisationsPsal = new CotisationsPsal();
        pensionAlimentaire = new PensionAlimentaire();
        simpleRevenuHypothetique = new SimpleRevenuHypothetique();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();
        simpleLibelleContratEntretienViager = new SimpleLibelleContratEntretienViager();
    }

    public AllocationsFamiliales getAllocationsFamiliales() {
        return allocationsFamiliales;
    }

    public CotisationsPsal getCotisationsPsal() {
        return cotisationsPsal;
    }

    public JadeAbstractModel getDonneeFinanciere() {
        JadeAbstractModel result = null;
        result = (revenuActiviteLucrativeDependante.isNew() ? result : revenuActiviteLucrativeDependante);
        result = (revenuActiviteLucrativeIndependante.isNew() ? result : revenuActiviteLucrativeIndependante);
        result = (allocationsFamiliales.isNew() ? result : allocationsFamiliales);
        result = (simpleAutresRevenus.isNew() ? result : simpleAutresRevenus);
        result = (simpleContratEntretienViager.isNew() ? result : simpleContratEntretienViager);
        result = (cotisationsPsal.isNew() ? result : cotisationsPsal);
        result = (pensionAlimentaire.isNew() ? result : pensionAlimentaire);
        result = (simpleRevenuHypothetique.isNew() ? result : simpleRevenuHypothetique);

        return result;
    }

    @Override
    public String getId() {
        return getDonneeFinanciere().getId();
    }

    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    public PensionAlimentaire getPensionAlimentaire() {
        return pensionAlimentaire;
    }

    public RevenuActiviteLucrativeDependante getRevenuActiviteLucrativeDependante() {
        return revenuActiviteLucrativeDependante;
    }

    public RevenuActiviteLucrativeIndependante getRevenuActiviteLucrativeIndependante() {
        return revenuActiviteLucrativeIndependante;
    }

    public SimpleAutresRevenus getSimpleAutresRevenus() {
        return simpleAutresRevenus;
    }

    public SimpleContratEntretienViager getSimpleContratEntretienViager() {
        return simpleContratEntretienViager;
    }

    /**
     * @return the simpleDonneeFinanciereHeader
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    public SimpleLibelleContratEntretienViager getSimpleLibelleContratEntretienViager() {
        return simpleLibelleContratEntretienViager;
    }

    public SimpleRevenuHypothetique getSimpleRevenuHypothetique() {
        return simpleRevenuHypothetique;
    }

    public SimpleTypeFraisObtentionRevenu getSimpleTypeFraisObtentionRevenu() {
        return simpleTypeFraisObtentionRevenu;
    }

    @Override
    public String getSpy() {
        return getDonneeFinanciere().getSpy();
    }

    public void setAllocationsFamiliales(AllocationsFamiliales allocationsFamiliales) {
        this.allocationsFamiliales = allocationsFamiliales;
    }

    public void setCotisationsPsal(CotisationsPsal cotisationsPsal) {
        this.cotisationsPsal = cotisationsPsal;
    }

    @Override
    public void setId(String id) {
        getDonneeFinanciere().setId(id);
    }

    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    public void setPensionAlimentaire(PensionAlimentaire pensionAlimentaire) {
        this.pensionAlimentaire = pensionAlimentaire;
    }

    public void setRevenuActiviteLucrativeDependante(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante) {
        this.revenuActiviteLucrativeDependante = revenuActiviteLucrativeDependante;
    }

    public void setRevenuActiviteLucrativeIndependante(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) {
        this.revenuActiviteLucrativeIndependante = revenuActiviteLucrativeIndependante;
    }

    public void setSimpleAutresRevenus(SimpleAutresRevenus simpleAutresRevenus) {
        this.simpleAutresRevenus = simpleAutresRevenus;
    }

    public void setSimpleContratEntretienViager(SimpleContratEntretienViager simpleContratEntretienViager) {
        this.simpleContratEntretienViager = simpleContratEntretienViager;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    public void setSimpleLibelleContratEntretienViager(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) {
        this.simpleLibelleContratEntretienViager = simpleLibelleContratEntretienViager;
    }

    public void setSimpleRevenuHypothetique(SimpleRevenuHypothetique simpleRevenuHypothetique) {
        this.simpleRevenuHypothetique = simpleRevenuHypothetique;
    }

    public void setSimpleTypeFraisObtentionRevenu(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu) {
        this.simpleTypeFraisObtentionRevenu = simpleTypeFraisObtentionRevenu;
    }

    @Override
    public void setSpy(String spy) {
        getDonneeFinanciere().setSpy(spy);
    }

}