package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class Habitat extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleLoyer simpleLoyer = null;
    private TaxeJournaliereHome taxeJournaliereHome = null;

    public Habitat() {
        super();
        membreFamilleEtendu = new MembreFamilleEtendu();
        simpleLoyer = new SimpleLoyer();
        taxeJournaliereHome = new TaxeJournaliereHome();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
    }

    public JadeAbstractModel getDonneeFinanciere() {
        JadeAbstractModel result = null;
        result = (simpleLoyer.isNew() ? result : simpleLoyer);
        result = (taxeJournaliereHome.isNew() ? result : taxeJournaliereHome);
        return result;
    }

    @Override
    public String getId() {
        return getDonneeFinanciere().getId();
    }

    /**
     * @return the membreFamilleEtendu
     */
    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    /**
     * @return the simpleDonneeFinanciereHeader
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    /**
     * @return the simpleLoyer
     */
    public SimpleLoyer getSimpleLoyer() {
        return simpleLoyer;
    }

    @Override
    public String getSpy() {
        return getDonneeFinanciere().getSpy();
    }

    /**
     * @return the taxeJournaliereHome
     */
    public TaxeJournaliereHome getTaxeJournaliereHome() {
        return taxeJournaliereHome;
    }

    @Override
    public void setId(String id) {
        getDonneeFinanciere().setId(id);
    }

    /**
     * @param membreFamilleEtendu the membreFamilleEtendu to set
     */
    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    /**
     * @param simpleDonneeFinanciereHeader the simpleDonneeFinanciereHeader to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    /**
     * @param simpleLoyer the simpleLoyer to set
     */
    public void setSimpleLoyer(SimpleLoyer simpleLoyer) {
        this.simpleLoyer = simpleLoyer;
    }

    @Override
    public void setSpy(String spy) {
        getDonneeFinanciere().setSpy(spy);
    }

    /**
     * @param taxeJournaliereHome the taxeJournaliereHome to set
     */
    public void setTaxeJournaliereHome(TaxeJournaliereHome taxeJournaliereHome) {
        this.taxeJournaliereHome = taxeJournaliereHome;
    }

}
