package ch.globaz.pegasus.business.domaine.demande;

import ch.globaz.common.domaine.Date;

public class Demande {
    private String id;
    private Date debut;
    private Date fin;
    private EtatDemande etat;
    private Boolean isFratrie;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public EtatDemande getEtat() {
        return etat;
    }

    public void setEtat(EtatDemande etat) {
        this.etat = etat;
    }

    public Boolean getIsFratrie() {
        return isFratrie;
    }

    public void setIsFratrie(Boolean isFratrie) {
        this.isFratrie = isFratrie;
    }

}
