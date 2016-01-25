package ch.globaz.perseus.business.models.donneesfinancieres;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;

public class DonneeFinanciere extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Demande demande = null;
    private MembreFamille membreFamille = null;
    private SimpleDonneeFinanciere simpleDonneeFinanciere = null;

    public DonneeFinanciere() {
        super();
        demande = new Demande();
        membreFamille = new MembreFamille();
        simpleDonneeFinanciere = new SimpleDonneeFinanciere();
    }

    /**
     * Permet de copier les éléments d'une donnée financière dans une autre sans changer d'objet
     * 
     * @param DonneeFinanciere
     *            à copier dans l'objet
     */
    public void copy(DonneeFinanciere donneeFinanciere) {
        demande = donneeFinanciere.getDemande();
        membreFamille = donneeFinanciere.getMembreFamille();
        simpleDonneeFinanciere = donneeFinanciere.getSimpleDonneeFinanciere();
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    @Override
    public String getId() {
        return simpleDonneeFinanciere.getId();
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    /**
     * @return the simpleDonneeFinanciere
     */
    public SimpleDonneeFinanciere getSimpleDonneeFinanciere() {
        return simpleDonneeFinanciere;
    }

    @Override
    public String getSpy() {
        return simpleDonneeFinanciere.getSpy();
    }

    public Float getValeur() {
        return Float.valueOf(simpleDonneeFinanciere.getValeur());
    }

    public Float getValeurModifieeTaxateur() {
        return Float.valueOf(simpleDonneeFinanciere.getValeurModifieeTaxateur());
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String id) {
        simpleDonneeFinanciere.setId(id);
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    /**
     * @param simpleDonneeFinanciere
     *            the simpleDonneeFinanciere to set
     */
    public void setSimpleDonneeFinanciere(SimpleDonneeFinanciere simpleDonneeFinanciere) {
        this.simpleDonneeFinanciere = simpleDonneeFinanciere;
    }

    @Override
    public void setSpy(String spy) {
        simpleDonneeFinanciere.setSpy(spy);
    }

    public void setValeur(Float valeur) {
        simpleDonneeFinanciere.setValeur(valeur.toString());
    }

    public void setValeurModifieeTaxateur(Float valeur) {
        simpleDonneeFinanciere.setValeurModifieeTaxateur(valeur.toString());
    }

}
