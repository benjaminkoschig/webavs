package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class RenteIjApi extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private IjApg ijApg = null;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private SimpleAllocationImpotent simpleAllocationImpotent = null;
    private SimpleAutreApi simpleAutreApi = null;
    private SimpleAutreRente simpleAutreRente = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi = null;
    private SimpleRenteAvsAi simpleRenteAvsAi = null;

    /**
	 * 
	 */
    public RenteIjApi() {
        super();
        membreFamilleEtendu = new MembreFamilleEtendu();
        simpleRenteAvsAi = new SimpleRenteAvsAi();
        simpleIndemniteJournaliereAi = new SimpleIndemniteJournaliereAi();
        ijApg = new IjApg();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        simpleAutreRente = new SimpleAutreRente();
        simpleAutreApi = new SimpleAutreApi();
        simpleAllocationImpotent = new SimpleAllocationImpotent();

    }

    public JadeAbstractModel getDonneeFinanciere() {
        JadeAbstractModel result = null;
        result = (simpleRenteAvsAi.isNew() ? result : simpleRenteAvsAi);
        result = (simpleIndemniteJournaliereAi.isNew() ? result : simpleIndemniteJournaliereAi);
        result = (ijApg.isNew() ? result : ijApg);
        result = (simpleAllocationImpotent.isNew() ? result : simpleAllocationImpotent);
        result = (simpleAutreRente.isNew() ? result : simpleAutreRente);
        result = (simpleAutreApi.isNew() ? result : simpleAutreApi);

        return result;
    }

    @Override
    public String getId() {
        return getDonneeFinanciere().getId();
    }

    /**
     * @return the simpleIjApg
     */
    public IjApg getIjApg() {
        return ijApg;
    }

    /**
     * @return the membreFamilleEtendu
     */
    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    /**
     * @return the simpleAllocationImpotent
     */
    public SimpleAllocationImpotent getSimpleAllocationImpotent() {
        return simpleAllocationImpotent;
    }

    /**
     * @return the simpleAutreApi
     */
    public SimpleAutreApi getSimpleAutreApi() {
        return simpleAutreApi;
    }

    /**
     * @return the simpleAutreRente
     */
    public SimpleAutreRente getSimpleAutreRente() {
        return simpleAutreRente;
    }

    /**
     * @return the simpleDonneeFinanciereHeader
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    /**
     * @return the simpleIndemniteJournaliereAi
     */
    public SimpleIndemniteJournaliereAi getSimpleIndemniteJournaliereAi() {
        return simpleIndemniteJournaliereAi;
    }

    /**
     * @return the simpleRenteAvsAi
     */
    public SimpleRenteAvsAi getSimpleRenteAvsAi() {
        return simpleRenteAvsAi;
    }

    @Override
    public String getSpy() {
        return getDonneeFinanciere().getSpy();
    }

    @Override
    public void setId(String id) {
        getDonneeFinanciere().setId(id);
    }

    /**
     * @param membreFamilleEtendu
     *            the membreFamilleEtendu to set
     */
    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    /**
     * @param simpleAllocationImpotent
     *            the simpleAllocationImpotent to set
     */
    public void setSimpleAllocationImpotent(SimpleAllocationImpotent simpleAllocationImpotent) {
        this.simpleAllocationImpotent = simpleAllocationImpotent;
    }

    /**
     * @param simpleAutreApi
     *            the simpleAutreApi to set
     */
    public void setSimpleAutreApi(SimpleAutreApi simpleAutreApi) {
        this.simpleAutreApi = simpleAutreApi;
    }

    /**
     * @param simpleAutreRente
     *            the simpleAutreRente to set
     */
    public void setSimpleAutreRente(SimpleAutreRente simpleAutreRente) {
        this.simpleAutreRente = simpleAutreRente;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    /**
     * @param simpleIjApg
     *            the simpleIjApg to set
     */
    public void setSimpleIjApg(IjApg ijApg) {
        this.ijApg = ijApg;
    }

    /**
     * @param simpleIndemniteJournaliereAi
     *            the simpleIndemniteJournaliereAi to set
     */
    public void setSimpleIndemniteJournaliereAi(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi) {
        this.simpleIndemniteJournaliereAi = simpleIndemniteJournaliereAi;
    }

    /**
     * @param simpleRenteAvsAi
     *            the simpleRenteAvsAi to set
     */
    public void setSimpleRenteAvsAi(SimpleRenteAvsAi simpleRenteAvsAi) {
        this.simpleRenteAvsAi = simpleRenteAvsAi;
    }

    @Override
    public void setSpy(String spy) {
        getDonneeFinanciere().setSpy(spy);
    }

}
