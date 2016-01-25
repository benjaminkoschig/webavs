package ch.globaz.vulpecula.external.models.affiliation;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

/**
 * @author Arnaud Geiser (AGE) | Créé le 6 févr. 2014
 * 
 */
public class Adhesion {
    private String id;
    private PlanCaisse planCaisse;
    private Administration administration;
    private Date dateDebut;
    private Date dateFin;
    private String typeAdhesion;

    public String getId() {
        return id;
    }

    public String getIdTiersAdministration() {
        return planCaisse.getIdTiersAdministration();
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlanCaisse getPlanCaisse() {
        return planCaisse;
    }

    public void setPlanCaisse(PlanCaisse planCaisse) {
        this.planCaisse = planCaisse;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getTypeAdhesion() {
        return typeAdhesion;
    }

    public void setTypeAdhesion(String typeAdhesion) {
        this.typeAdhesion = typeAdhesion;
    }

    public Administration getAdministration() {
        return administration;
    }

    public void setAdministration(Administration administration) {
        this.administration = administration;
    }

    public String getLibellePlanCaisse() {
        return planCaisse.getLibelle();
    }

    /**
     * Retourne si l'adhésion est active à la date du jour
     * 
     * @return true si active
     */
    public boolean isActif() {
        return isActif(new Date());
    }

    /**
     * Retourne le libelle de l'administration lié au plan caisse.
     * 
     * @return String représentant le libelle du plan caisse
     */
    public String getLibelleAdministrationPlanCaisse() {
        return planCaisse.getAdministration().getDesignation1();
    }

    /**
     * Retourne le code de l'administration lié au plan caisse.
     * 
     * @return String représentant le code lié au plan caisse
     */
    public String getCodeAdministrationPlanCaisse() {
        return planCaisse.getAdministration().getCodeAdministration();
    }

    /**
     * Retourne si l'adhésion est active par rapport à la date passée en
     * paramètre
     * 
     * @param date
     *            {@link Date} à laquelle l'on souhaite déduire l'activité
     * @return true si active
     */
    public boolean isActif(Date date) {
        if (dateDebut != null) {
            if (dateDebut.getTime() < date.getTime()) {
                if (dateFin == null) {
                    return true;
                }
                if (dateFin.getTime() > date.getTime()) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public Administration getAdministrationPlanCaisse() {
        return planCaisse.getAdministration();
    }
}
