package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.SimpleRenteAvsAi;

public class RenteToUpdate extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String nss = null;
    private SimpleAllocationImpotent simpleAllocationImpotent = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleRenteAvsAi simpleRenteAvsAi = null;

    public RenteToUpdate() {
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        simpleRenteAvsAi = new SimpleRenteAvsAi();
        simpleAllocationImpotent = new SimpleAllocationImpotent();
    }

    @Override
    public String getId() {
        return simpleDonneeFinanciereHeader.getId();
    }

    public String getNss() {
        return nss;
    }

    public SimpleAllocationImpotent getSimpleAllocationImpotent() {
        return simpleAllocationImpotent;
    }

    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    public SimpleRenteAvsAi getSimpleRenteAvsAi() {
        return simpleRenteAvsAi;
    }

    @Override
    public String getSpy() {
        return getSimpleDonneeFinanciereHeader().getId();
    }

    @Override
    public void setId(String id) {
        getSimpleDonneeFinanciereHeader().setId(id);

    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setSimpleAllocationImpotent(SimpleAllocationImpotent simpleAllocationImpotent) {
        this.simpleAllocationImpotent = simpleAllocationImpotent;
    }

    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    public void setSimpleRenteAvsAi(SimpleRenteAvsAi simpleRenteAvsAi) {
        this.simpleRenteAvsAi = simpleRenteAvsAi;
    }

    @Override
    public void setSpy(String spy) {
        simpleDonneeFinanciereHeader.setSpy(spy);
    }
}
