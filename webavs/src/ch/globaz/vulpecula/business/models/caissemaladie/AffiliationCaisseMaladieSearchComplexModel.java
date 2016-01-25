package ch.globaz.vulpecula.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class AffiliationCaisseMaladieSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -1253449333080586177L;

    private String forId;
    private String forIdTravailleur;
    private String forIdPosteTravail;
    private String forMoisDebutBefore;
    private String forIdCaisseMaladie;
    private String forDateDebutAnnonceIsZero;
    private String forMoisFinBefore;
    private String forDateFinAnnonceIsZero;
    private String forMoisFinIsNotZero;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getForMoisDebutBefore() {
        return forMoisDebutBefore;
    }

    public void setForMoisDebutBefore(String forMoisDebutBefore) {
        this.forMoisDebutBefore = forMoisDebutBefore;
    }

    public String getForIdCaisseMaladie() {
        return forIdCaisseMaladie;
    }

    public void setForIdCaisseMaladie(String forIdCaisseMaladie) {
        this.forIdCaisseMaladie = forIdCaisseMaladie;
    }

    public String getForDateDebutAnnonceIsZero() {
        return forDateDebutAnnonceIsZero;
    }

    public void setForDateDebutAnnonceIsZero() {
        forDateDebutAnnonceIsZero = "0";
    }

    @Override
    public Class<AffiliationCaisseMaladieComplexModel> whichModelClass() {
        return AffiliationCaisseMaladieComplexModel.class;
    }

    public void setForMoisDebutBefore(Date date) {
        if (date != null) {
            forMoisDebutBefore = date.getAnneeMois();
        }
    }

    public void setForCaisseMaladie(Administration caisseMaladie) {
        if (caisseMaladie != null) {
            forIdCaisseMaladie = caisseMaladie.getIdTiers();
        }
    }

    public void setForMoisFinBefore(Date date) {
        if (date != null) {
            forMoisFinBefore = date.getAnneeMois();
        }
    }

    public void setForDateFinAnnonceIsZero() {
        forDateFinAnnonceIsZero = "0";
    }

    public String getForDateFinAnnonceIsZero() {
        return forDateFinAnnonceIsZero;
    }

    public String getForMoisFinBefore() {
        return forMoisFinBefore;
    }

    public void setForMoisFinBefore(String forMoisFinBefore) {
        this.forMoisFinBefore = forMoisFinBefore;
    }

    public String getForMoisFinIsNotZero() {
        return forMoisFinIsNotZero;
    }

    public void setForMoisFinIsNotZero() {
        forMoisFinIsNotZero = "0";
    }

    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    public void setForIdPosteTravail(String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }
}
