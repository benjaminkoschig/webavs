package ch.globaz.pegasus.business.models.assurancemaladie;

import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeComplexModel;

public class AssuranceMaladie extends JadeComplexModel {

    private static final long serialVersionUID = 1L;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie = null;
    private SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie = null;

    public AssuranceMaladie() {
        super();
        membreFamilleEtendu = new MembreFamilleEtendu();
        simplePrimeAssuranceMaladie = new SimplePrimeAssuranceMaladie();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        simpleSubsideAssuranceMaladie = new SimpleSubsideAssuranceMaladie();
    }

    public JadeAbstractModel getDonneeFinanciere() {
        JadeAbstractModel result = null;
        result = (simplePrimeAssuranceMaladie.isNew() ? result : simplePrimeAssuranceMaladie);
        result = (simpleSubsideAssuranceMaladie.isNew() ? result : simpleSubsideAssuranceMaladie);
        return result;
    }

    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    public SimplePrimeAssuranceMaladie getSimplePrimeAssuranceMaladie() {
        return simplePrimeAssuranceMaladie;
    }

    public void setSimplePrimeAssuranceMaladie(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) {
        this.simplePrimeAssuranceMaladie = simplePrimeAssuranceMaladie;
    }

    public SimpleSubsideAssuranceMaladie getSimpleSubsideAssuranceMaladie() {
        return simpleSubsideAssuranceMaladie;
    }

    public void setSimpleSubsideAssuranceMaladie(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) {
        this.simpleSubsideAssuranceMaladie = simpleSubsideAssuranceMaladie;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setSpy(String spy) {

    }
}
