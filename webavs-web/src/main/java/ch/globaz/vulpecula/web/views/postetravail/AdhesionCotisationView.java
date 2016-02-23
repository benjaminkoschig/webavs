package ch.globaz.vulpecula.web.views.postetravail;

/**
 * DTO facilitant la gestion des cotisations dans l'écran de mutation du poste
 * de travail. Il représente fidèlement les données nécessaires au
 * fonctionnement de l'écran quant à la gestion des adhésions aux cotisations
 * d'un poste travail.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 3 mars 2014
 * 
 */
public class AdhesionCotisationView implements Comparable<AdhesionCotisationView> {
    public String id;
    public String idCotisation;
    public String nom;
    public String dateDebut;
    public String dateFin;
    public String dateDebutCotisation;
    public String dateFinCotisation;
    public String idPlanCaisse;
    public String libellePlanCaisse;
    public String spy;
    public String idAssurance;
    public boolean checked;

    public String getIdCotisation() {
        return idCotisation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdPlanCaisse() {
        return idPlanCaisse;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getNom() {
        return nom;
    }

    public String getLibellePlanCaisse() {
        return libellePlanCaisse;
    }

    /**
     * @return the dateDebutCotisation
     */
    public String getDateDebutCotisation() {
        return dateDebutCotisation;
    }

    /**
     * @return the dateFinCotisation
     */
    public String getDateFinCotisation() {
        return dateFinCotisation;
    }

    public String getId() {
        return id;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpy() {
        return spy;
    }

    public void setSpy(String spy) {
        this.spy = spy;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (checked ? 1231 : 1237);
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateDebutCotisation == null) ? 0 : dateDebutCotisation.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
        result = prime * result + ((dateFinCotisation == null) ? 0 : dateFinCotisation.hashCode());
        result = prime * result + ((idCotisation == null) ? 0 : idCotisation.hashCode());
        result = prime * result + ((idPlanCaisse == null) ? 0 : idPlanCaisse.hashCode());
        result = prime * result + ((libellePlanCaisse == null) ? 0 : libellePlanCaisse.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AdhesionCotisationView other = (AdhesionCotisationView) obj;
        if (checked != other.checked) {
            return false;
        }
        if (dateDebut == null) {
            if (other.dateDebut != null) {
                return false;
            }
        } else if (!dateDebut.equals(other.dateDebut)) {
            return false;
        }
        if (dateDebutCotisation == null) {
            if (other.dateDebutCotisation != null) {
                return false;
            }
        } else if (!dateDebutCotisation.equals(other.dateDebutCotisation)) {
            return false;
        }
        if (dateFin == null) {
            if (other.dateFin != null) {
                return false;
            }
        } else if (!dateFin.equals(other.dateFin)) {
            return false;
        }
        if (dateFinCotisation == null) {
            if (other.dateFinCotisation != null) {
                return false;
            }
        } else if (!dateFinCotisation.equals(other.dateFinCotisation)) {
            return false;
        }
        if (idCotisation == null) {
            if (other.idCotisation != null) {
                return false;
            }
        } else if (!idCotisation.equals(other.idCotisation)) {
            return false;
        }
        if (idPlanCaisse == null) {
            if (other.idPlanCaisse != null) {
                return false;
            }
        } else if (!idPlanCaisse.equals(other.idPlanCaisse)) {
            return false;
        }
        if (libellePlanCaisse == null) {
            if (other.libellePlanCaisse != null) {
                return false;
            }
        } else if (!libellePlanCaisse.equals(other.libellePlanCaisse)) {
            return false;
        }
        if (nom == null) {
            if (other.nom != null) {
                return false;
            }
        } else if (!nom.equals(other.nom)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(AdhesionCotisationView o) {
        String compare1 = nom + idCotisation + dateDebut + dateFin;
        String compare2 = o.nom + o.idCotisation + o.dateDebut + o.dateFin;

        return compare1.compareTo(compare2);
    }
}
