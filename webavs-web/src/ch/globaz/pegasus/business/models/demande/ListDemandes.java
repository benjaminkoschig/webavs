package ch.globaz.pegasus.business.models.demande;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionRefus;
import ch.globaz.pegasus.business.models.dossier.Dossier;

public class ListDemandes extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatVersionDroit = null;
    private Dossier dossier = null;
    private SimpleDecisionRefus simpleDecisionRefus = null;
    private SimpleDemande simpleDemande = null;

    public ListDemandes() {
        super();
        simpleDemande = new SimpleDemande();
        dossier = new Dossier();
        simpleDecisionRefus = new SimpleDecisionRefus();
    }

    public String getCsEtatVersionDroit() {
        return csEtatVersionDroit;
    }

    /**
     * @return the dossier
     */
    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return simpleDemande.getId();
    }

    /**
     * @return the simpleDecisionRefus
     */
    public SimpleDecisionRefus getSimpleDecisionRefus() {
        return simpleDecisionRefus;
    }

    /**
     * @return the demande
     */
    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    @Override
    public String getSpy() {
        return simpleDemande.getSpy();
    }

    public void setCsEtatVersionDroit(String csEtatVersionDroit) {
        this.csEtatVersionDroit = csEtatVersionDroit;
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        simpleDemande.setId(id);

    }

    /**
     * @param simpleDecisionRefus
     *            the simpleDecisionRefus to set
     */
    public void setSimpleDecisionRefus(SimpleDecisionRefus simpleDecisionRefus) {
        this.simpleDecisionRefus = simpleDecisionRefus;
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setSimpleDemande(SimpleDemande demande) {
        simpleDemande = demande;
    }

    @Override
    public void setSpy(String spy) {
        simpleDemande.setSpy(spy);
    }

}
