package ch.globaz.pegasus.business.models.fortuneparticuliere;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class FortuneParticuliere extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AssuranceRenteViagere assuranceRenteViagere = null;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere = null;
    private SimpleBetail simpleBetail = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleMarchandisesStock simpleMarchandisesStock = null;
    private SimpleNumeraire simpleNumeraire = null;
    private SimplePretEnversTiers simplePretEnversTiers = null;
    private SimpleVehicule simpleVehicule = null;

    /**
	 * 
	 */
    public FortuneParticuliere() {
        super();
        membreFamilleEtendu = new MembreFamilleEtendu();
        assuranceRenteViagere = new AssuranceRenteViagere();
        simpleAutreFortuneMobiliere = new SimpleAutreFortuneMobiliere();
        simpleBetail = new SimpleBetail();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        simpleMarchandisesStock = new SimpleMarchandisesStock();
        simpleNumeraire = new SimpleNumeraire();
        simplePretEnversTiers = new SimplePretEnversTiers();
        simpleVehicule = new SimpleVehicule();
    }

    /**
     * @return the assuranceRenteViagere
     */
    public AssuranceRenteViagere getAssuranceRenteViagere() {
        return assuranceRenteViagere;
    }

    public JadeAbstractModel getDonneeFinanciere() {
        JadeAbstractModel result = null;
        result = (assuranceRenteViagere.isNew() ? result : assuranceRenteViagere);
        result = (simpleAutreFortuneMobiliere.isNew() ? result : simpleAutreFortuneMobiliere);
        result = (simpleBetail.isNew() ? result : simpleBetail);
        result = (simpleMarchandisesStock.isNew() ? result : simpleMarchandisesStock);
        result = (simpleNumeraire.isNew() ? result : simpleNumeraire);
        result = (simplePretEnversTiers.isNew() ? result : simplePretEnversTiers);
        result = (simpleVehicule.isNew() ? result : simpleVehicule);

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
     * @return the simpleAutreFortuneMobiliere
     */
    public SimpleAutreFortuneMobiliere getSimpleAutreFortuneMobiliere() {
        return simpleAutreFortuneMobiliere;
    }

    /**
     * @return the simpleBetail
     */
    public SimpleBetail getSimpleBetail() {
        return simpleBetail;
    }

    /**
     * @return the simpleDonneeFinanciereHeader
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    /**
     * @return the simpleMarchandisesStock
     */
    public SimpleMarchandisesStock getSimpleMarchandisesStock() {
        return simpleMarchandisesStock;
    }

    /**
     * @return the simpleNumeraire
     */
    public SimpleNumeraire getSimpleNumeraire() {
        return simpleNumeraire;
    }

    /**
     * @return the simplePretEnversTiers
     */
    public SimplePretEnversTiers getSimplePretEnversTiers() {
        return simplePretEnversTiers;
    }

    /**
     * @return the simpleVehicule
     */
    public SimpleVehicule getSimpleVehicule() {
        return simpleVehicule;
    }

    @Override
    public String getSpy() {
        return getDonneeFinanciere().getSpy();
    }

    /**
     * @param assuranceRenteViagere
     *            the assuranceRenteViagere to set
     */
    public void setAssuranceRenteViagere(AssuranceRenteViagere assuranceRenteViagere) {
        this.assuranceRenteViagere = assuranceRenteViagere;
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
     * @param simpleAutreFortuneMobiliere
     *            the simpleAutreFortuneMobiliere to set
     */
    public void setSimpleAutreFortuneMobiliere(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere) {
        this.simpleAutreFortuneMobiliere = simpleAutreFortuneMobiliere;
    }

    /**
     * @param simpleBetail
     *            the simpleBetail to set
     */
    public void setSimpleBetail(SimpleBetail simpleBetail) {
        this.simpleBetail = simpleBetail;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    /**
     * @param simpleMarchandisesStock
     *            the simpleMarchandisesStock to set
     */
    public void setSimpleMarchandisesStock(SimpleMarchandisesStock simpleMarchandisesStock) {
        this.simpleMarchandisesStock = simpleMarchandisesStock;
    }

    /**
     * @param simpleNumeraire
     *            the simpleNumeraire to set
     */
    public void setSimpleNumeraire(SimpleNumeraire simpleNumeraire) {
        this.simpleNumeraire = simpleNumeraire;
    }

    /**
     * @param simplePretEnversTiers
     *            the simplePretEnversTiers to set
     */
    public void setSimplePretEnversTiers(SimplePretEnversTiers simplePretEnversTiers) {
        this.simplePretEnversTiers = simplePretEnversTiers;
    }

    /**
     * @param simpleVehicule
     *            the simpleVehicule to set
     */
    public void setSimpleVehicule(SimpleVehicule simpleVehicule) {
        this.simpleVehicule = simpleVehicule;
    }

    @Override
    public void setSpy(String spy) {
        getDonneeFinanciere().setSpy(spy);
    }

}
