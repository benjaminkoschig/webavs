/*
 * Créé le 3 jan. 07
 */
package globaz.corvus.vb.basescalcul;

import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;

/**
 * @author bsc
 */
public class REBasesCalculNeuviemeRevisionViewBean extends REAbstractBasesCalculProxyViewBean {

    public REBasesCalculNeuviemeRevisionViewBean() {
        super(new REBasesCalculNeuviemeRevision());
    }

    @Override
    public REBasesCalculNeuviemeRevision getBasesCalcul() {
        return (REBasesCalculNeuviemeRevision) super.getBasesCalcul();
    }

    public String getBonificationTacheEducative() {
        return getBasesCalcul().getBonificationTacheEducative();
    }

    public String getCleInfirmiteEpouse() {
        return getBasesCalcul().getCleInfirmiteEpouse();
    }

    public String getCodeOfficeAiEpouse() {
        return getBasesCalcul().getCodeOfficeAiEpouse();
    }

    public String getDegreInvaliditeEpouse() {
        return getBasesCalcul().getDegreInvaliditeEpouse();
    }

    public String getNbrAnneeEducation() {
        return getBasesCalcul().getNbrAnneeEducation();
    }

    public String getSurvenanceEvenementAssureEpouse() {
        return getBasesCalcul().getSurvenanceEvenementAssureEpouse();
    }

    public Boolean isInvaliditePrecoceEpouse() {
        return getBasesCalcul().isInvaliditePrecoceEpouse();
    }

    public void setBonificationTacheEducative(String string) {
        getBasesCalcul().setBonificationTacheEducative(string);
    }

    public void setCleInfirmiteEpouse(String string) {
        getBasesCalcul().setCleInfirmiteEpouse(string);
    }

    public void setCodeOfficeAiEpouse(String string) {
        getBasesCalcul().setCodeOfficeAiEpouse(string);
    }

    public void setDegreInvaliditeEpouse(String string) {
        getBasesCalcul().setDegreInvaliditeEpouse(string);
    }

    public void setInvaliditePrecoceEpouse(Boolean boolean1) {
        getBasesCalcul().setInvaliditePrecoceEpouse(boolean1);
    }

    public void setNbrAnneeEducation(String string) {
        getBasesCalcul().setNbrAnneeEducation(string);
    }

    public void setSurvenanceEvenementAssureEpouse(String string) {
        getBasesCalcul().setSurvenanceEvenementAssureEpouse(string);
    }
}
