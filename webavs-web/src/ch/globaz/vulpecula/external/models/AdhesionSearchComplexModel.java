package ch.globaz.vulpecula.external.models;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.Annee;

/**
 * @author Arnaud Geiser (AGE) | Créé le 7 févr. 2014
 * 
 */
public class AdhesionSearchComplexModel extends JadeSearchComplexModel {

    private String forIdAffilie;
    private String forAnneeDebut;
    private String forAnneeFin;
    private String forTypeCaisse;
    private String forIdPlanCaisse;

    public String getForIdPlanCaisse() {
        return forIdPlanCaisse;
    }

    public void setForIdPlanCaisse(String forIdPlanCaisse) {
        this.forIdPlanCaisse = forIdPlanCaisse;
    }

    public String getForAnneeDebut() {
        return forAnneeDebut;
    }

    public void setForAnneeDebut(String forAnneeDebut) {
        this.forAnneeDebut = forAnneeDebut;
    }

    public String getForAnneeFin() {
        return forAnneeFin;
    }

    public void setForAnneeFin(String forAnneeFin) {
        this.forAnneeFin = forAnneeFin;
    }

    public String getForTypeCaisse() {
        return forTypeCaisse;
    }

    public void setForTypeCaisse(String forTypeCaisse) {
        this.forTypeCaisse = forTypeCaisse;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }

    @Override
    public Class<AdhesionComplexModel> whichModelClass() {
        return AdhesionComplexModel.class;
    }

    public String getForDateDebut() {
        if (JadeStringUtil.isBlankOrZero(getForAnneeDebut())) {
            return "";
        } else {
            return new Annee(getForAnneeDebut()).getFirstDayOfYear().getSwissValue();
        }
    }

    public String getForDateFin() {
        if (JadeStringUtil.isBlankOrZero(getForAnneeFin())) {
            return "";
        } else {
            return new Annee(getForAnneeFin()).getLastDayOfYear().getSwissValue();
        }
    }
}
