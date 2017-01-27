package ch.globaz.pegasus.business.services.synchronisation;

import java.util.List;

public class MembreFamilleToSync {
    private String idDemande;
    private List<String> idsMembreFamille;

    public MembreFamilleToSync() {

    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public List<String> getIdsMembreFamille() {
        return idsMembreFamille;
    }

    public void setIdsMembreFamille(List<String> idsMembreFamille) {
        this.idsMembreFamille = idsMembreFamille;
    }

}
