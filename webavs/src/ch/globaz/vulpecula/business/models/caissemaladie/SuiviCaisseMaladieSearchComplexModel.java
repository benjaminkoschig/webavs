package ch.globaz.vulpecula.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import ch.globaz.vulpecula.domain.models.caissemaladie.TypeDocumentCaisseMaladie;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class SuiviCaisseMaladieSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -1253449333080586177L;

    private String forId;
    private String forIdTravailleur;
    private String forIdCaisseMaladie;
    private Boolean forIsEnvoye;
    private Collection<String> forTypeDocument;

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

    public String getForIdCaisseMaladie() {
        return forIdCaisseMaladie;
    }

    public void setForIdCaisseMaladie(String forIdCaisseMaladie) {
        this.forIdCaisseMaladie = forIdCaisseMaladie;
    }

    @Override
    public Class<SuiviCaisseMaladieComplexModel> whichModelClass() {
        return SuiviCaisseMaladieComplexModel.class;
    }

    public void setForCaisseMaladie(Administration caisseMaladie) {
        if (caisseMaladie != null) {
            forIdCaisseMaladie = caisseMaladie.getIdTiers();
        }
    }

    public Boolean getForIsEnvoye() {
        return forIsEnvoye;
    }

    public void setForIsEnvoye(Boolean forIsEnvoye) {
        this.forIsEnvoye = forIsEnvoye;
    }

    public Collection<String> getForTypeDocument() {
        return forTypeDocument;
    }

    public void setForTypeDocument(Collection<String> forTypeDocument) {
        this.forTypeDocument = forTypeDocument;
    }

    public void setForTypeDocumentStandard() {
        forTypeDocument = TypeDocumentCaisseMaladie.STANDARD;
    }

    public void setForTypeDocumentFichesAnnonce() {
        forTypeDocument = TypeDocumentCaisseMaladie.FICHES_ANNONCES;
    }
}
