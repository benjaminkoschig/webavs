package ch.globaz.pegasus.business.domaine.droit;

import com.google.common.base.Preconditions;

public class VersionDroit {
    private String id;
    private Integer numero;
    private EtatDroit etat;
    private MotifDroit motif;
    private boolean demandeInitiale;

    public VersionDroit(String id, Integer numero, EtatDroit etat, MotifDroit motif, boolean demandeInitiale) {
        Preconditions.checkNotNull(id, "IdVersion is null");
        Preconditions.checkNotNull(numero, "NumeroVersion is null");
        Preconditions.checkNotNull(etat, "Etat is null");
        Preconditions.checkNotNull(motif, "Motif is null");

        this.id = id;
        this.numero = numero;
        this.etat = etat;
        this.motif = motif;
        this.demandeInitiale = demandeInitiale;
    }

    public VersionDroit(String id) {
        Preconditions.checkNotNull(id, "IdVersion is null");
        this.id = id;
    }

    public boolean isInitial() {
        return numero.equals(1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public EtatDroit getEtat() {
        return etat;
    }

    public void setEtat(EtatDroit etat) {
        this.etat = etat;
    }

    public MotifDroit getMotif() {
        return motif;
    }

    public void setMotif(MotifDroit motif) {
        this.motif = motif;
    }

    public boolean isDemandeInitiale() {
        return demandeInitiale;
    }

    public void setDemandeInitiale(boolean demandeInitiale) {
        this.demandeInitiale = demandeInitiale;
    }
}
