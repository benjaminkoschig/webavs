package ch.globaz.pegasus.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.prestation.business.models.demande.DemandePrestation;

public class DossierRCList extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DemandePrestation demandePrestation = null;
    private SimpleDemande simpleDemande = null;
    private SimpleDossier simpleDossier = null;

    public DossierRCList() {
        super();
        simpleDossier = new SimpleDossier();
        simpleDemande = new SimpleDemande();
        demandePrestation = new DemandePrestation();
    }

    /**
     * @return the demandePrestation
     */
    public DemandePrestation getDemandePrestation() {
        return demandePrestation;
    }

    @Override
    public String getId() {
        return simpleDossier.getId();
    }

    /**
     * @return the simpleDemande
     */
    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    /**
     * @return the simpleDossier
     */
    public SimpleDossier getSimpleDossier() {
        return simpleDossier;
    }

    @Override
    public String getSpy() {
        return simpleDossier.getSpy();
    }

    /**
     * @param demandePrestation
     *            the demandePrestation to set
     */
    public void setDemandePrestation(DemandePrestation demandePrestation) {
        this.demandePrestation = demandePrestation;
    }

    @Override
    public void setId(String id) {
        simpleDossier.setId(id);
    }

    /**
     * @param simpleDemande
     *            the simpleDemande to set
     */
    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    /**
     * @param simpledossier
     *            the simpledossier to set
     */
    public void setSimpleDossier(SimpleDossier simpleDossier) {
        this.simpleDossier = simpleDossier;
    }

    @Override
    public void setSpy(String spy) {
        simpleDossier.setSpy(spy);
    }

}
