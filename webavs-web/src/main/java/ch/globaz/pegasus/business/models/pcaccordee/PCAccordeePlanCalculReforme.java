package ch.globaz.pegasus.business.models.pcaccordee;

import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.dossier.SimpleDossier;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author EBKO
 * 
 */
public class PCAccordeePlanCalculReforme extends JadeComplexModel {

    private static final long serialVersionUID = 1L;
    private String idPca;
    private String idPlanDeCalcul;
    private String idVersionDroit;
    private String dateDebut = null;
    private String dateFin = null;
    private Boolean reformePc = false;
    private Boolean isPlanRetenu;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIdPca() {
        return idPca;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public String getIdPlanDeCalcul() {
        return idPlanDeCalcul;
    }

    public void setIdPlanDeCalcul(String idPlanDeCalcul) {
        this.idPlanDeCalcul = idPlanDeCalcul;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getReformePc() {
        return reformePc;
    }

    public void setReformePc(Boolean reformePc) {
        this.reformePc = reformePc;
    }

    public Boolean getIsPlanRetenu() {
        return isPlanRetenu;
    }

    public void setIsPlanRetenu(Boolean planRetenu) {
        isPlanRetenu = planRetenu;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String s) {

    }

    @Override
    public void setSpy(String s) {

    }
}
