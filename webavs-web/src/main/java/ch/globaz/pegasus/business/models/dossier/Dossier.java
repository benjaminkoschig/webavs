package ch.globaz.pegasus.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.prestation.business.models.demande.DemandePrestation;

public class Dossier extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DemandePrestation demandePrestation = null;
    private SimpleDossier dossier = null;

    public Dossier() {
        super();
        dossier = new SimpleDossier();
        demandePrestation = new DemandePrestation();
    }

    /**
     * retourne la demande de prestation du dossier
     * 
     * @return the demandePrestation
     */
    public DemandePrestation getDemandePrestation() {
        return demandePrestation;
    }

    /**
     * retourne le dossier
     * 
     * @return the dossier
     */
    public SimpleDossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return dossier.getId();
    }

    @Override
    public String getSpy() {
        return dossier.getSpy();
    }

    /**
     * @param demandePrestation
     *            the demandePrestation to set
     */
    public void setDemandePrestation(DemandePrestation demandePrestation) {
        this.demandePrestation = demandePrestation;
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(SimpleDossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        dossier.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        dossier.setSpy(spy);
    }

}
