package ch.globaz.vulpecula.domain.models.postetravail;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

/**
 * Classe repr�sentant une adh�sion d'un poste de travail � une cotisation
 * 
 */
public class AdhesionCotisationPosteTravail implements DomainEntity {
    private String id;
    private Periode periode;
    private Cotisation cotisation;
    private String spy;

    /**
     * Retourne l'id de l'adh�sion
     * 
     * @return String repr�sentant l'id de l'adh�sion
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Mise � jour de l'id repr�sentant l'adh�sion
     * 
     * @param id
     *            Nouvel id de l'adh�sion
     */
    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public Cotisation getCotisation() {
        return cotisation;
    }

    public void setCotisation(final Cotisation cotisation) {
        this.cotisation = cotisation;
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(final Periode periode) {
        this.periode = periode;
    }

    /**
     * Retourne le spy utilis� pour g�rer la concurrence en base de donn�es
     * 
     * @return spy du tuple
     */
    @Override
    public String getSpy() {
        return spy;
    }

    /**
     * Mise � jour du spy utilis� pour g�rer la concurrence en base de donn�es
     * 
     * @param spy
     *            Nouveau spy du tuple
     */
    @Override
    public void setSpy(final String spy) {
        this.spy = spy;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AdhesionCotisationPosteTravail) {
            AdhesionCotisationPosteTravail adhesionCaisse = (AdhesionCotisationPosteTravail) obj;
            return adhesionCaisse.getId().equals(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id.hashCode();
    }

    public String getDateEntree() {
        if (periode != null) {
            return periode.getDateDebut().getSwissValue();
        }
        return null;
    }

    /**
     * Mise � jour du taux de la cotisation.
     * 
     * @param taux
     *            Taux de la cotisation
     */
    public void setTaux(final Taux taux) {
        cotisation.setTaux(taux);
    }

    /**
     * Retourne l'id de la cotisation.
     * 
     * @return String repr�sentant l'id � une cotisation
     */
    public String getIdCotisation() {
        return cotisation.getId();
    }

    /**
     * Retourne le type d'assurance de la cotisation
     * 
     * @return String repr�sentant un code syst�me
     */
    public TypeAssurance getTypeAssurance() {
        return cotisation.getTypeAssurance();
    }

    /**
     * Retourne le plan caisse de la cotisation.
     * 
     * @return {@link PlanCaisse} de la cotisation
     */
    public PlanCaisse getPlanCaisse() {
        return cotisation.getPlanCaisse();
    }

    public boolean isActif() {
        return isActif(Date.now());
    }

    public boolean isActif(final Date date) {
        if (cotisation.getDateFin() != null && cotisation.getDateFin().before(date)) {
            return false;
        }
        return periode.contains(date);
    }

    public boolean isActifPourLeMois(final Date date) {
        if (cotisation.getDateFin() != null && cotisation.getDateFin().before(date)) {
            return false;
        }
        return periode.isActifMois(date);
    }

    /**
     * Retourne le taux contribuable de l'adh�sion � une cotisation
     * 
     * @return double repr�sentant le taux contribuable, ou null si inexistant
     */
    public Taux getTauxContribuable() {
        if (cotisation == null) {
            return null;
        }
        return cotisation.getTaux();
    }

    public boolean isAssuranceAC2() {
        return cotisation.isAssuranceAC2();
    }

    public boolean isAssuranceACOrAC2() {
        return cotisation.isAssuranceAC() || cotisation.isAssuranceAC2();
    }

    /**
     * Retourne si la cotisation doit �tre ignorer pour la g�n�ration des cotisations.
     * 
     * @return true si doit �tre ignor�
     */
    public boolean aIgnorer(TypeDecompte type, Convention convention) {
        if (TypeDecompte.COMPLEMENTAIRE.equals(type)) {
            if (convention.isElectricite()) {
                switch (getTypeAssurance()) {
                    case CONTRIBUTION_GENERALE:
                    case CONTRIBUTION_GENERALE_REDUITE:
                    case CPR_TRAVAILLEUR:
                        return true;
                    default:
                        return false;
                }
            } else {
                if (TypeAssurance.CONGES_PAYES.equals(getTypeAssurance())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "AdhesionCotisationPosteTravail [id=" + id + ", periode=" + periode + ", cotisation=" + cotisation
                + ", spy=" + spy + "]";
    }

    public Assurance getAssurance() {
        return cotisation.getAssurance();
    }

    public static Map<TypeAssurance, Collection<AdhesionCotisationPosteTravail>> groupByTypeAssurance(
            List<AdhesionCotisationPosteTravail> adhesions) {
        return Multimaps.index(adhesions, new Function<AdhesionCotisationPosteTravail, TypeAssurance>() {
            @Override
            public TypeAssurance apply(AdhesionCotisationPosteTravail adhesionCotisationPosteTravail) {
                return adhesionCotisationPosteTravail.getTypeAssurance();
            }
        }).asMap();
    }

    public boolean isActifIn(Annee annee) {
        return periode.isActifIn(annee);
    }

    public String getIdAssurance() {
        return cotisation.getAssuranceId();
    }
}
