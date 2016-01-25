package ch.globaz.vulpecula.domain.models.congepaye;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;

/**
 * Contient une liste de compteurs relatif à un poste de travail.
 */
public class Compteurs implements Iterable<Compteur> {
    private List<Compteur> compteurs;

    public Compteurs(List<Compteur> compteurs) {
        this.compteurs = compteurs;
    }

    public List<Compteur> getCompteurs() {
        return compteurs;
    }

    public void setCompteurs(List<Compteur> compteurs) {
        this.compteurs = compteurs;
    }

    @Override
    public Iterator<Compteur> iterator() {
        return compteurs.iterator();
    }

    public void add(CongePaye congePaye) {
        Montant montantAAbsorber = congePaye.getTotalSalaire();
        Iterator<Compteur> compteurIterator = compteurs.iterator();

        absorbe(compteurIterator, montantAAbsorber, congePaye);
    }

    protected final void add(Compteur compteur) {
        compteurs.add(compteur);
    }

    private void absorbe(Iterator<Compteur> compteurIterator, Montant montantAAbsorber, CongePaye congePaye) {
        if (montantAAbsorber.isZero()) {
            return;
        }

        Compteur compteur = compteurIterator.next();
        Montant nouveauMontantAAbsorber = new Montant(0);
        if (!compteur.isFull()) {
            nouveauMontantAAbsorber = compteur.absorbe(congePaye, montantAAbsorber);
        } else {
            nouveauMontantAAbsorber = montantAAbsorber;
        }

        if (isLastCompteur(compteurIterator)) {
            if (nouveauMontantAAbsorber.isPositive()) {
                compteur.forceAbsorbe(congePaye, nouveauMontantAAbsorber);
            }
        } else {
            absorbe(compteurIterator, nouveauMontantAAbsorber, congePaye);
        }

    }

    /**
     * Si c'est le dernier compteur, dans l'iterator, on force alors les montants sur ce dernier.
     * 
     * @return true si dernier élément de la liste.
     */
    private boolean isLastCompteur(Iterator<Compteur> compteurIterator) {
        return !compteurIterator.hasNext();
    }

    /**
     * Retourne si au moins un des compteurs est actif.
     * 
     * @return true si au moins un compteur est actif, false dans les autres cas (y.c si aucun compteur)
     */
    public boolean hasCompteursActifs() {
        for (Compteur compteur : compteurs) {
            if (!compteur.isFull()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne si une ligne existe pour la période passée en paramètre.
     * 
     * @param anneeDebut Année de début
     * @param anneeFin Année de fin
     * @return true si une ligne est présente
     */
    public boolean hasLignePourPeriodeSaisie(Annee anneeDebut, Annee anneeFin) {
        return getCompteursBetween(anneeDebut, anneeFin).size() > 0;
    }

    private List<Compteur> getCompteursBetween(Annee anneeDebut, Annee anneeFin) {
        List<Compteur> compteurs = new ArrayList<Compteur>();
        for (Compteur compteur : this) {
            if (compteur.getAnnee().isContained(anneeDebut, anneeFin)) {
                if (compteur.hasLignes()) {
                    compteurs.add(compteur);
                }
            }
        }
        return compteurs;
    }
}
