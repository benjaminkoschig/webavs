package ch.globaz.vulpecula.domain.models.congepaye;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

/**
 * Un compteur permet de d�finir si un poste de travail � le droit � des cong�s pay�s.
 * Pour chaque ann�e, on retrouve un maximum de cotisation qui peuvent �tre vers�es.
 * A chaque versement d'un nouveau cong� pay�, le compteur est incr�ment�.
 * 
 */
public class Compteur implements DomainEntity {
    private String id;
    private PosteTravail posteTravail;
    private Montant cumulCotisation;
    private Montant montantRestant;
    private Annee annee;
    private List<LigneCompteur> lignes;
    private String spy;

    public Compteur() {
        this(null);
    }

    public Compteur(String id) {
        this.id = id;
        cumulCotisation = new Montant(0);
        montantRestant = new Montant(0);
        lignes = new ArrayList<LigneCompteur>();
    }

    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    public Montant getCumulCotisation() {
        return cumulCotisation;
    }

    public void setCumulCotisation(Montant cumulCotisation) {
        this.cumulCotisation = cumulCotisation;
    }

    public Montant getMontantRestant() {
        return montantRestant;
    }

    /**
     * Retourne la somme des lignes de compteurs correspondant ainsi au montant des cong�s pay�s vers�s
     * 
     * @return Montant vers� pour les cong�s pay�s.
     */
    public Montant getMontantVerse() {
        Montant montant = Montant.ZERO;
        for (LigneCompteur ligne : lignes) {
            montant = montant.add(ligne.getMontant());
        }
        return montant;
    }

    public void setMontantRestant(Montant montantRestant) {
        this.montantRestant = montantRestant;
    }

    public Annee getAnnee() {
        return annee;
    }

    /**
     * Retourne l'annn�e du compteur. Si l'ann�e est null, retourne alors 0 correspondant � null en base de donn�es.
     * 
     * @return L'ann�e du compteur, ou 0 si ann�e null
     */
    public int getAnneeAsValue() {
        if (annee == null) {
            return 0;
        }
        return annee.getValue();
    }

    /**
     * Retourne l'id du poste de travail ou null si poste de travail inexistant.
     * 
     * @return L'id du poste de travail ou null si null
     */
    public String getIdPosteTravail() {
        if (posteTravail == null) {
            return null;
        }
        return posteTravail.getId();
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public boolean hasLignes() {
        return lignes.size() > 0;
    }

    public List<LigneCompteur> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCompteur> lignes) {
        this.lignes = lignes;
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

    /**
     * Permet d'ajouter un montant aux cotisations cumul�es du compteur
     * 
     * @param cotisationToAdd montant � ajouter
     */
    public void addCotisation(Montant cotisationToAdd) {
        if (cotisationToAdd != null && !cotisationToAdd.isZero()) {
            cumulCotisation = cumulCotisation.add(cotisationToAdd);
        }
    }

    /**
     * Retourne si le compteur est rempli
     * 
     * @return true si rempli, false si il est encore possible de rajouter un montant
     * @throws NullPointerException si cumulCotisation est null ou montantRestant est null
     */
    public boolean isFull() {
        return montantRestant.isZero() || montantRestant.isNegative();
    }

    /**
     * Le compteur tente d'absorber le montant pass� en param�tre. Si celui-ci ne peut pas l'�tre compl�tement, un
     * nouveau montant � absorber est retourn�.
     * 
     * @param montant Montant � absorber
     */
    public Montant absorbe(CongePaye congePaye, Montant montantAAbsorber) {
        Montant montantNonAbsorbable = getMontantNonAbsorbable(montantAAbsorber);

        if (isAllMontantAbsorbable(montantAAbsorber)) {
            addLigne(congePaye, montantAAbsorber);
            montantRestant = montantRestant.substract(montantAAbsorber);
        } else {
            addLigne(congePaye, montantRestant);
            montantRestant = Montant.ZERO;
        }

        if (montantNonAbsorbable.isNegative()) {
            return Montant.ZERO;
        }
        return montantNonAbsorbable;
    }

    /**
     * Le compteur ABSORBE le montant pass� en param�tre et ce quelque soit l'�tat du compteur.
     * 
     * @param montantAAbsorber Montant � absorber
     */
    public void forceAbsorbe(CongePaye congePaye, Montant montantAAbsorber) {
        addLigne(congePaye, montantAAbsorber);
        montantRestant = montantRestant.substract(montantAAbsorber);
    }

    private boolean isAllMontantAbsorbable(Montant montantAAbsorber) {
        Montant montantNonAbsorbable = getMontantNonAbsorbable(montantAAbsorber);
        return montantNonAbsorbable.isZero() || montantNonAbsorbable.isNegative();
    }

    private Montant getMontantNonAbsorbable(Montant montantAAbsorber) {
        Montant montantAbsorbable = getMontantRestant();
        return montantAAbsorber.substract(montantAbsorbable);
    }

    /**
     * Ajout d'une ligne de compteur
     * 
     * @param congePaye CongePaye li� � la ligne de compteur
     * @param montant Montant � ajouter
     */
    public void addLigne(CongePaye congePaye, Montant montant) {
        LigneCompteur ligneCompteur = new LigneCompteur(this, congePaye);
        ligneCompteur.setMontant(montant);
        lignes.add(ligneCompteur);
    }

    public static Map<Convention, Collection<Compteur>> groupByConvention(Collection<Compteur> compteurs) {
        return Multimaps.index(compteurs, new Function<Compteur, Convention>() {
            @Override
            public Convention apply(Compteur compteur) {
                return compteur.getPosteTravail().getConvention();
            }
        }).asMap();
    }
}
