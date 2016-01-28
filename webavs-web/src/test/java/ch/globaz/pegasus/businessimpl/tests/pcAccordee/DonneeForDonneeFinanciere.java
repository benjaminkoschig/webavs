package ch.globaz.pegasus.businessimpl.tests.pcAccordee;

import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.droit.Droit;

public class DonneeForDonneeFinanciere {
    private Demande demande = null;
    private Dossier dossier = null;
    private Droit droit = null;
    private String idTiersConjoint = null;
    private String idTiersRequerant = null;

    public Demande getDemande() {
        return demande;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public Droit getDroit() {
        return droit;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public void setDroit(Droit droit) {
        this.droit = droit;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

}
