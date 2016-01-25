package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class FortuneUsuelle extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AssuranceVie assuranceVie = null;
    private BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale = null;
    private BienImmobilierNonHabitable bienImmobilierNonHabitable = null;
    private BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale = null;

    private CapitalLPP capitalLPP = null;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private SimpleAutresDettesProuvees simpleAutresDettesProuvees = null;
    private SimpleCompteBancaireCCP simpleCompteBancaireCCP = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private SimpleTitre simpleTitre = null;

    /**
	 * 
	 */
    public FortuneUsuelle() {
        super();
        assuranceVie = new AssuranceVie();
        membreFamilleEtendu = new MembreFamilleEtendu();
        simpleAutresDettesProuvees = new SimpleAutresDettesProuvees();
        bienImmobilierHabitationNonPrincipale = new BienImmobilierHabitationNonPrincipale();
        bienImmobilierNonHabitable = new BienImmobilierNonHabitable();
        bienImmobilierServantHabitationPrincipale = new BienImmobilierServantHabitationPrincipale();
        capitalLPP = new CapitalLPP();
        simpleCompteBancaireCCP = new SimpleCompteBancaireCCP();
        simpleTitre = new SimpleTitre();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
    }

    /**
     * @return the assuranceVie
     */
    public AssuranceVie getAssuranceVie() {
        return assuranceVie;
    }

    /**
     * @return the bienImmobilierHabitationNonPrincipale
     */
    public BienImmobilierHabitationNonPrincipale getBienImmobilierHabitationNonPrincipale() {
        return bienImmobilierHabitationNonPrincipale;
    }

    /**
     * @return the bienImmobilierNonHabitable
     */
    public BienImmobilierNonHabitable getBienImmobilierNonHabitable() {
        return bienImmobilierNonHabitable;
    }

    /**
     * @return the bienImmobilierServantHabitationPrincipale
     */
    public BienImmobilierServantHabitationPrincipale getBienImmobilierServantHabitationPrincipale() {
        return bienImmobilierServantHabitationPrincipale;
    }

    /**
     * @return the capitalLPP
     */
    public CapitalLPP getCapitalLPP() {
        return capitalLPP;
    }

    public JadeAbstractModel getDonneeFinanciere() {
        JadeAbstractModel result = null;
        result = (simpleCompteBancaireCCP.isNew() ? result : simpleCompteBancaireCCP);

        result = (assuranceVie.isNew() ? result : assuranceVie);

        result = (simpleAutresDettesProuvees.isNew() ? result : simpleAutresDettesProuvees);
        result = (bienImmobilierHabitationNonPrincipale.isNew() ? result : bienImmobilierHabitationNonPrincipale);
        result = (bienImmobilierNonHabitable.isNew() ? result : bienImmobilierNonHabitable);

        result = (bienImmobilierServantHabitationPrincipale.isNew() ? result
                : bienImmobilierServantHabitationPrincipale);

        result = (capitalLPP.isNew() ? result : capitalLPP);

        result = (simpleTitre.isNew() ? result : simpleTitre);

        return result;
    }

    @Override
    public String getId() {
        return getDonneeFinanciere().getId();
    }

    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    public SimpleAutresDettesProuvees getSimpleAutresDettesProuvees() {
        return simpleAutresDettesProuvees;
    }

    public SimpleCompteBancaireCCP getSimpleCompteBancaireCCP() {
        return simpleCompteBancaireCCP;
    }

    /**
     * @return the simpleDonneeFinanciereHeader
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    public SimpleTitre getSimpleTitre() {
        return simpleTitre;
    }

    @Override
    public String getSpy() {
        return getDonneeFinanciere().getSpy();
    }

    /**
     * @param assuranceVie
     *            the assuranceVie to set
     */
    public void setAssuranceVie(AssuranceVie assuranceVie) {
        this.assuranceVie = assuranceVie;
    }

    /**
     * @param bienImmobilierHabitationNonPrincipale
     *            the bienImmobilierHabitationNonPrincipale to set
     */
    public void setBienImmobilierHabitationNonPrincipale(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale) {
        this.bienImmobilierHabitationNonPrincipale = bienImmobilierHabitationNonPrincipale;
    }

    /**
     * @param bienImmobilierNonHabitable
     *            the bienImmobilierNonHabitable to set
     */
    public void setBienImmobilierNonHabitable(BienImmobilierNonHabitable bienImmobilierNonHabitable) {
        this.bienImmobilierNonHabitable = bienImmobilierNonHabitable;
    }

    /**
     * @param bienImmobilierServantHabitationPrincipale
     *            the bienImmobilierServantHabitationPrincipale to set
     */
    public void setBienImmobilierServantHabitationPrincipale(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale) {
        this.bienImmobilierServantHabitationPrincipale = bienImmobilierServantHabitationPrincipale;
    }

    /**
     * @param capitalLPP
     *            the capitalLPP to set
     */
    public void setCapitalLPP(CapitalLPP capitalLPP) {
        this.capitalLPP = capitalLPP;
    }

    @Override
    public void setId(String id) {
        getDonneeFinanciere().setId(id);
    }

    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    public void setSimpleAutresDettesProuvees(SimpleAutresDettesProuvees simpleAutresDettesProuvees) {
        this.simpleAutresDettesProuvees = simpleAutresDettesProuvees;
    }

    public void setSimpleCompteBancaireCCP(SimpleCompteBancaireCCP simpleCompteBancaireCCP) {
        this.simpleCompteBancaireCCP = simpleCompteBancaireCCP;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        this.simpleDonneeFinanciereHeader = simpleDonneeFinanciereHeader;
    }

    public void setSimpleTitre(SimpleTitre simpleTitre) {
        this.simpleTitre = simpleTitre;
    }

    @Override
    public void setSpy(String spy) {
        getDonneeFinanciere().setSpy(spy);
    }

}