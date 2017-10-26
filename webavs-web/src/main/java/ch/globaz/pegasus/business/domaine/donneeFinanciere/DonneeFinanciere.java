package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public abstract class DonneeFinanciere {

    protected DonneeFinanciereType typeDonnneeFianciere;
    private final RoleMembreFamille roleMembreFamille;
    protected final Date debut;
    protected final Date fin;
    private final String id;
    private final String idDroitMembreFamille;

    public DonneeFinanciere(RoleMembreFamille roleMembreFamille, Date debut, Date fin, String id,
            String idDroitMembreFamille) {
        Checkers.checkNotNull(roleMembreFamille, "roleMembreFamille");
        Checkers.checkNotNull(debut, "dateDebut");
        Checkers.checkNotNull(id, "id");
        definedTypeDonneeFinanciere();
        this.roleMembreFamille = roleMembreFamille;
        this.debut = debut;
        this.fin = fin;
        this.id = id;
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    public DonneeFinanciere() {
        roleMembreFamille = RoleMembreFamille.INDEFINIT;
        debut = new Date();
        fin = null;
        id = "-1";
        idDroitMembreFamille = "-1";
    }

    public DonneeFinanciere(DonneeFinanciere donneeFinanciere) {
        Checkers.checkNotNull(donneeFinanciere, "donneeFinanciere");
        definedTypeDonneeFinanciere();
        roleMembreFamille = donneeFinanciere.getRoleMembreFamille();
        debut = donneeFinanciere.getDebut();
        fin = donneeFinanciere.getFin();
        id = donneeFinanciere.getId();
        idDroitMembreFamille = donneeFinanciere.getIdDroitMembreFamille();
    }

    protected abstract void definedTypeDonneeFinanciere();

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public RoleMembreFamille getRoleMembreFamille() {
        return roleMembreFamille;
    }

    public DonneeFinanciereType getTypeDonneeFinanciere() {
        return typeDonnneeFianciere;
    }

    public Date getDebut() {
        return debut;
    }

    public Date getFin() {
        return fin;
    }

    public String getId() {
        return id;
    }

    public boolean isRequerant() {
        return roleMembreFamille.isRequerant();
    }

    public boolean isEnfant() {
        return roleMembreFamille.isEnfant();
    }

    public boolean isConjoint() {
        return roleMembreFamille.isConjoint();
    }

    public boolean isInPeriode(Date debut, Date fin) {
        Date debutPeriode = debut.toMonthYear();
        Date finPeriode = null;

        if (fin != null) {
            finPeriode = fin.toMonthYear();
            if (finPeriode.before(debutPeriode)) {
                throw new RuntimeException("The dateDebut is before the dateFin");
            }
        }
        if (this.debut.beforeOrEquals(debutPeriode)) {
            if (this.fin == null) {
                return true;
            } else if (this.fin.before(this.debut)) {
                return false;
            } else if (finPeriode == null && this.fin.afterOrEquals(debutPeriode)) {
                return true;
            } else if (finPeriode != null && this.fin.afterOrEquals(finPeriode) && this.fin.afterOrEquals(debutPeriode)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "DonneeFinanciere [typeDonnneeFianciere=" + typeDonnneeFianciere + ", roleMembreFamille="
                + roleMembreFamille + ", debut=" + debut + ", fin=" + fin + ", id=" + id + ", idDroitMembreFamille="
                + idDroitMembreFamille + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((debut == null) ? 0 : debut.hashCode());
        result = prime * result + ((fin == null) ? 0 : fin.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((idDroitMembreFamille == null) ? 0 : idDroitMembreFamille.hashCode());
        result = prime * result + ((roleMembreFamille == null) ? 0 : roleMembreFamille.hashCode());
        result = prime * result + ((typeDonnneeFianciere == null) ? 0 : typeDonnneeFianciere.hashCode());
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
        DonneeFinanciere other = (DonneeFinanciere) obj;
        if (debut == null) {
            if (other.debut != null) {
                return false;
            }
        } else if (!debut.equals(other.debut)) {
            return false;
        }
        if (fin == null) {
            if (other.fin != null) {
                return false;
            }
        } else if (!fin.equals(other.fin)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (idDroitMembreFamille == null) {
            if (other.idDroitMembreFamille != null) {
                return false;
            }
        } else if (!idDroitMembreFamille.equals(other.idDroitMembreFamille)) {
            return false;
        }
        if (roleMembreFamille != other.roleMembreFamille) {
            return false;
        }
        if (typeDonnneeFianciere != other.typeDonnneeFianciere) {
            return false;
        }
        return true;
    }

}
