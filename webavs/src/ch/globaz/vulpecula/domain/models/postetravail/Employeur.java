package ch.globaz.vulpecula.domain.models.postetravail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.registre.TypeFacturation;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Affilie;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.CotisationChecker;

/***
 * Un employeur est un affili� (NAOS) qui dispose de postes de travail. Il
 * s'agit d'un des mod�les principales pour l'application BMS
 * 
 */
public class Employeur extends Affilie implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean bvr;
    private boolean editerSansTravailleur;
    private TypeFacturation typeFacturation;
    private List<PosteTravail> postesTravail;
    private Set<Travailleur> travailleurs;
    private List<Adhesion> adhesionsCaisses;
    private List<Decompte> decomptes;
    private Adhesion caisseMetier;

    public Employeur() {
        super();
        init();
    }

    public Employeur(final Affilie affilie) {
        super(affilie);
        init();
    }

    private void init() {
        postesTravail = new ArrayList<PosteTravail>();
    }

    public void setTypeFacturation(TypeFacturation typeFacturation) {
        this.typeFacturation = typeFacturation;
    }

    public TypeFacturation getTypeFacturation() {
        return typeFacturation;
    }

    /**
     * Retourne la liste des postes de travail auxquels l'employeur est affect�s
     * 
     * @return Collection de postes de travail
     */
    public List<PosteTravail> getPostesTravail() {
        return postesTravail;
    }

    /**
     * Mise � jour des postes de travail pour l'employeur
     * 
     * @param postesDeTravail
     *            Collection de postes de travail
     */
    public void setPostesDeTravail(final List<PosteTravail> postesTravail) {
        this.postesTravail = postesTravail;
    }

    /**
     * Retourne la liste des travailleurs embauch�e par l'employeur
     * 
     * @return Collection de {@link Travailleur}
     */
    public Set<Travailleur> getTravailleurs() {
        return travailleurs;
    }

    /**
     * Mise � jour des postes de travail de l'employeurs
     * 
     * @param travailleurs
     *            Collection de postes de travail
     */
    public void setTravailleurs(final Set<Travailleur> travailleurs) {
        this.travailleurs = travailleurs;
    }

    /**
     * Retourne si l'envoyeur dispose de l'attribut BVR (utilit� iconnue pour le
     * moment)
     * 
     * @return true si il dispose de l'attribut BVR
     */
    public boolean isBvr() {
        return bvr;
    }

    /**
     * Mise � jour de l'attribut BVR sur l'employeur
     * 
     * @param bvr
     *            true si il dispose d'un BVR
     */
    public void setBvr(final boolean bvr) {
        this.bvr = bvr;
    }

    /**
     * Retourne les d�comptes li�es � l'employeur
     * 
     * @return Liste de d�comptes
     */
    public List<Decompte> getDecomptes() {
        return decomptes;
    }

    /**
     * Mise � jour des d�comptes li�s � l'employeur
     * 
     * @param decomptes
     *            Nouvelle liste de d�comptes li�es � l'employeur
     */
    public void setDecomptes(final List<Decompte> decomptes) {
        this.decomptes = decomptes;
    }

    public void setPostesTravail(final List<PosteTravail> postesTravail) {
        this.postesTravail = postesTravail;
    }

    public List<Adhesion> getAdhesionsCaisses() {
        return adhesionsCaisses;
    }

    public void setAdhesionsCaisses(final List<Adhesion> adhesionsCaisses) {
        this.adhesionsCaisses = adhesionsCaisses;
    }

    public void addCotisation(final Cotisation cotisation) {
        cotisations.add(cotisation);
    }

    public boolean isSoumisAVS() {
        return CotisationChecker.isSoumisAVS(cotisations);
    }

    public boolean isSoumisAC() {
        return CotisationChecker.isSoumisAC(cotisations);
    }

    public boolean isSoumisAC2() {
        return CotisationChecker.isSoumisAC2(cotisations);
    }

    /**
     * Retourne une collection repr�sentant les taux contribuables diff�rents (aggr�gation des taux de cotisation dans
     * caisses sociales) pour chaque poste de travail.
     * 
     * @return Set repr�sentant l'aggra�gation des taux contribuables des postes de travail
     */
    public Set<Taux> getTauxContribuablesDifferentsDesPostes() {
        Set<Taux> tauxContribuables = new HashSet<Taux>();

        for (PosteTravail posteTravail : postesTravail) {
            tauxContribuables.add(posteTravail.getTauxContribuable());
        }

        return tauxContribuables;
    }

    public void addPosteTravail(final PosteTravail posteTravail) {
        postesTravail.add(posteTravail);
    }

    /**
     * Retourne le num�ro de convention. Dans le cas o� la convention est null, retourne null.
     * 
     * @return String repr�sentant le no de la convention
     */
    public String getNo() {
        if (convention == null) {
            return null;
        }
        return convention.getCode();
    }

    public final boolean isEditerSansTravailleur() {
        return editerSansTravailleur;
    }

    public final void setEditerSansTravailleur(boolean editerSansTravailleur) {
        this.editerSansTravailleur = editerSansTravailleur;
    }

    public final Adhesion getCaisseMetier() {
        return caisseMetier;
    }

    public final void setCaisseMetier(Adhesion caisseMetier) {
        this.caisseMetier = caisseMetier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Employeur) {
            Employeur other = (Employeur) obj;
            if (other.getId() == null || getId() == null) {
                return false;
            } else {
                return other.getId().equals(getId());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return 37;
        }
        return getId().hashCode();
    }

    /**
     * Contr�le si l'employeur est actif dans la p�riode demand� en poss�dant soit
     * - pas de date de fin mais une date de d�but d'activ� avant la date de fin de la p�riode demand�
     * ou
     * - une p�riode d'activit� (avec une date de fin diff�rente de NULL) qui chevauche la p�riode demand�
     * 
     * @return true si l'employeur est actif
     * @param periodeDemande Periode dans laquel l'employeur doit avoir une activit�
     */
    public boolean isActif(final Periode periodeDemande) {
        Periode periodeActivite = new Periode(getDateDebut(), getDateFin());
        return periodeDemande.isActif(periodeActivite);
    }
}
