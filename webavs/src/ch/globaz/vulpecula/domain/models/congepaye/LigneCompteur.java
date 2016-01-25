package ch.globaz.vulpecula.domain.models.congepaye;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;

/**
 * Une ligne de compteur correspond aux montants versés à un poste de travail lorsqu'il prend des congés payés.
 * 
 */
public class LigneCompteur implements DomainEntity {
    private String id;
    private Montant montant;
    private String spy;
    private Compteur compteur;
    private CongePaye congePaye;

    public LigneCompteur(Compteur compteur) {
        this(compteur, null);
    }

    public LigneCompteur(Compteur compteur, CongePaye congePaye) {
        this.compteur = compteur;
        this.congePaye = congePaye;
        montant = Montant.ZERO;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public Compteur getCompteur() {
        return compteur;
    }

    public void setCompteur(Compteur compteur) {
        this.compteur = compteur;
    }

    public CongePaye getCongePaye() {
        return congePaye;
    }

    public void setCongePaye(CongePaye congePaye) {
        this.congePaye = congePaye;
    }

    /**
     * Retourne l'id du compteur.
     * 
     * @return String représentant l'id du compteur
     */
    public String getIdCompteur() {
        if (compteur == null) {
            return null;
        }
        return compteur.getId();
    }

    /**
     * Retourne l'id du congé payé.
     * 
     * @return String représentant l'id du congé payé
     */
    public String getIdCongePaye() {
        if (congePaye == null) {
            return null;
        }
        return congePaye.getId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }
}
