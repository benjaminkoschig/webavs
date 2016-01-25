/**
 * 
 */
package globaz.perseus.vb.donneesfinancieres;

import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueType;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneType;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFEnfantDFViewBean extends PFAbstractDonneesFinancieresViewBean {

    private Revenu aideFormation = null;
    private Revenu autresRevenusEnfant = null;
    private Revenu brapaEnfant = null;
    private DepenseReconnue chargesAnnuelles = null;
    private Fortune fortuneEnfant = null;
    private DepenseReconnue fraisRepas = null;
    private DepenseReconnue fraisTransport = null;
    private DepenseReconnue fraisVetements = null;
    private String idEnfant = null;
    private DepenseReconnue loyerAnnuel = null;
    private Revenu pensionAlimentaireEnfant = null;
    private Revenu renteEnfant = null;
    private Revenu revenuActiviteEnfant = null;

    public PFEnfantDFViewBean() {
        super();

        // Revenus
        autresRevenusEnfant = new Revenu(RevenuType.AUTRES_REVENUS_ENFANT);
        revenus.add(autresRevenusEnfant);
        revenuActiviteEnfant = new Revenu(RevenuType.REVENUS_ACTIVITE_ENFANT);
        revenus.add(revenuActiviteEnfant);
        aideFormation = new Revenu(RevenuType.AIDE_FORMATION);
        revenus.add(aideFormation);
        renteEnfant = new Revenu(RevenuType.RENTE_ENFANT);
        revenus.add(renteEnfant);
        pensionAlimentaireEnfant = new Revenu(RevenuType.PENSION_ALIMENTAIRE_ENFANT);
        revenus.add(pensionAlimentaireEnfant);
        brapaEnfant = new Revenu(RevenuType.BRAPA_ENFANT);
        revenus.add(getBrapaEnfant());

        // Fortune
        fortuneEnfant = new Fortune(FortuneType.FORTUNE_ENFANT);
        fortunes.add(fortuneEnfant);

        // Depenses reconnues
        chargesAnnuelles = new DepenseReconnue(DepenseReconnueType.CHARGES_ANNUELLES);
        depensesReconnues.add(chargesAnnuelles);
        fraisRepas = new DepenseReconnue(DepenseReconnueType.FRAIS_REPAS);
        depensesReconnues.add(fraisRepas);
        fraisTransport = new DepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT);
        depensesReconnues.add(fraisTransport);
        fraisVetements = new DepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS);
        depensesReconnues.add(fraisVetements);
        loyerAnnuel = new DepenseReconnue(DepenseReconnueType.LOYER_ANNUEL);
        depensesReconnues.add(loyerAnnuel);

    }

    /**
     * @return the aideFormation
     */
    public Revenu getAideFormation() {
        return aideFormation;
    }

    /**
     * @return the autresRevenusEnfant
     */
    public Revenu getAutresRevenusEnfant() {
        return autresRevenusEnfant;
    }

    public Revenu getBrapaEnfant() {
        return brapaEnfant;
    }

    /**
     * @return the chargesAnnuelles
     */
    public DepenseReconnue getChargesAnnuelles() {
        return chargesAnnuelles;
    }

    /**
     * @return the fortuneEnfant
     */
    public Fortune getFortuneEnfant() {
        return fortuneEnfant;
    }

    /**
     * @return the fraisRepas
     */
    public DepenseReconnue getFraisRepas() {
        return fraisRepas;
    }

    /**
     * @return the fraisTransport
     */
    public DepenseReconnue getFraisTransport() {
        return fraisTransport;
    }

    /**
     * @return the fraisVetements
     */
    public DepenseReconnue getFraisVetements() {
        return fraisVetements;
    }

    /**
     * @return the idEnfant
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * @return the loyerAnnuel
     */
    public DepenseReconnue getLoyerAnnuel() {
        return loyerAnnuel;
    }

    public Revenu getPensionAlimentaireEnfant() {
        return pensionAlimentaireEnfant;
    }

    public Revenu getRenteEnfant() {
        return renteEnfant;
    }

    /**
     * @return the revenuActiviteEnfant
     */
    public Revenu getRevenuActiviteEnfant() {
        return revenuActiviteEnfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // Retrieve des revenus

        RevenuSearchModel searchModel = new RevenuSearchModel();
        searchModel.setForIdDemande(demande.getId());
        searchModel.setForIdMembreFamille(membreFamille.getId());
        searchModel = PerseusServiceLocator.getRevenuService().search(searchModel);

        for (JadeAbstractModel monRevenu : searchModel.getSearchResults()) {
            Revenu revenu = (Revenu) monRevenu;
            switch (revenu.getTypeAsEnum()) {
                case REVENUS_ACTIVITE_ENFANT:
                    revenuActiviteEnfant.copy(revenu);
                    break;
                case AUTRES_REVENUS_ENFANT:
                    autresRevenusEnfant.copy(revenu);
                    break;
                case AIDE_FORMATION:
                    aideFormation.copy(revenu);
                    break;
                case RENTE_ENFANT:
                    renteEnfant.copy(revenu);
                    break;
                case PENSION_ALIMENTAIRE_ENFANT:
                    pensionAlimentaireEnfant.copy(revenu);
                    break;
                case BRAPA_ENFANT:
                    getBrapaEnfant().copy(revenu);
                    break;
            }
        }

        // Retrieve de la fortune

        FortuneSearchModel fortuneSearchModel = new FortuneSearchModel();
        fortuneSearchModel.setForIdDemande(demande.getId());
        fortuneSearchModel.setForIdMembreFamille(membreFamille.getId());
        fortuneSearchModel = PerseusServiceLocator.getFortuneService().search(fortuneSearchModel);

        for (JadeAbstractModel maFortune : fortuneSearchModel.getSearchResults()) {
            Fortune fortune = (Fortune) maFortune;
            switch (fortune.getTypeAsEnum()) {
                case FORTUNE_ENFANT:
                    fortuneEnfant.copy(fortune);
                    break;
            }
        }

        // Retrieve des dépenses reconnues

        DepenseReconnueSearchModel depenseReconnueSearchModel = new DepenseReconnueSearchModel();
        depenseReconnueSearchModel.setForIdDemande(demande.getId());
        depenseReconnueSearchModel.setForIdMembreFamille(membreFamille.getId());
        depenseReconnueSearchModel = PerseusServiceLocator.getDepenseReconnueService().search(
                depenseReconnueSearchModel);

        for (JadeAbstractModel maDepenseReconnue : depenseReconnueSearchModel.getSearchResults()) {
            DepenseReconnue depenseReconnue = (DepenseReconnue) maDepenseReconnue;
            switch (depenseReconnue.getTypeAsEnum()) {
                case LOYER_ANNUEL:
                    loyerAnnuel.copy(depenseReconnue);
                    break;
                case CHARGES_ANNUELLES:
                    chargesAnnuelles.copy(depenseReconnue);
                    break;
                case FRAIS_REPAS:
                    fraisRepas.copy(depenseReconnue);
                    break;
                case FRAIS_TRANSPORT:
                    fraisTransport.copy(depenseReconnue);
                    break;
                case FRAIS_VETEMENTS:
                    fraisVetements.copy(depenseReconnue);
                    break;
            }
        }

    }

    /**
     * @param aideFormation
     *            the aideFormation to set
     */
    public void setAideFormation(Revenu aideFormation) {
        this.aideFormation = aideFormation;
    }

    /**
     * @param autresRevenusEnfant
     *            the autresRevenusEnfant to set
     */
    public void setAutresRevenusEnfant(Revenu autresRevenusEnfant) {
        this.autresRevenusEnfant = autresRevenusEnfant;
    }

    public void setBrapaEnfant(Revenu brapaEnfant) {
        this.brapaEnfant = brapaEnfant;
    }

    /**
     * @param chargesAnnuelles
     *            the chargesAnnuelles to set
     */
    public void setChargesAnnuelles(DepenseReconnue chargesAnnuelles) {
        this.chargesAnnuelles = chargesAnnuelles;
    }

    /**
     * @param fortuneEnfant
     *            the fortuneEnfant to set
     */
    public void setFortuneEnfant(Fortune fortuneEnfant) {
        this.fortuneEnfant = fortuneEnfant;
    }

    /**
     * @param fraisRepas
     *            the fraisRepas to set
     */
    public void setFraisRepas(DepenseReconnue fraisRepas) {
        this.fraisRepas = fraisRepas;
    }

    /**
     * @param fraisTransport
     *            the fraisTransport to set
     */
    public void setFraisTransport(DepenseReconnue fraisTransport) {
        this.fraisTransport = fraisTransport;
    }

    /**
     * @param fraisVetements
     *            the fraisVetements to set
     */
    public void setFraisVetements(DepenseReconnue fraisVetements) {
        this.fraisVetements = fraisVetements;
    }

    /**
     * @param idEnfant
     *            the idEnfant to set
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * @param loyerAnnuel
     *            the loyerAnnuel to set
     */
    public void setLoyerAnnuel(DepenseReconnue loyerAnnuel) {
        this.loyerAnnuel = loyerAnnuel;
    }

    public void setPensionAlimentaireEnfant(Revenu pensionAlimentaireEnfant) {
        this.pensionAlimentaireEnfant = pensionAlimentaireEnfant;
    }

    public void setRenteEnfant(Revenu renteEnfant) {
        this.renteEnfant = renteEnfant;
    }

    /**
     * @param revenuActiviteEnfant
     *            the revenuActiviteEnfant to set
     */
    public void setRevenuActiviteEnfant(Revenu revenuActiviteEnfant) {
        this.revenuActiviteEnfant = revenuActiviteEnfant;
    }

    @Override
    public void update() throws Exception {
        // Si les frais de transport sont saisis, contrôler que les frais de transport acceptés sont remplis
        // if (!JadeStringUtil.isEmpty(this.fraisTransport.getSimpleDonneeFinanciere().getValeur())
        // && JadeStringUtil.isEmpty(this.fraisTransport.getSimpleDonneeFinanciere().getValeurModifieeTaxateur())) {
        // JadeThread.logError(PFDonneefinanciereViewBean.class.getName(),
        // "perseus.donneesfinancieres.depenseReconnue.fraisTransport.check");
        // }
        super.update();
    }

}
