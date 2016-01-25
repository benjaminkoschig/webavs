package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import ch.globaz.vulpecula.domain.models.common.Date;

public class OperationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 5141271165027932012L;

    private Collection<String> forIdCompte;
    private String forDateLessOrEquals;
    private String forDateAfterOrEquals;
    private String forEtat;
    private String forIdTypeOperation;
    private String forCantonResidence;
    private String forIdAllocataire;

    public Collection<String> getForIdCompte() {
        return forIdCompte;
    }

    public void setForIdCompte(Collection<String> forIdCompte) {
        this.forIdCompte = forIdCompte;
    }

    public String getForDateLessOrEquals() {
        return forDateLessOrEquals;
    }

    public void setForDateLessOrEquals(String forDateLessOrEquals) {
        this.forDateLessOrEquals = forDateLessOrEquals;
    }

    public String getForDateAfterOrEquals() {
        return forDateAfterOrEquals;
    }

    public void setForDateAfterOrEquals(String forDateAfterOrEquals) {
        this.forDateAfterOrEquals = forDateAfterOrEquals;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForIdTypeOperation() {
        return forIdTypeOperation;
    }

    public void setForIdTypeOperation(String forIdTypeOperation) {
        this.forIdTypeOperation = forIdTypeOperation;
    }

    public String getForCantonResidence() {
        return forCantonResidence;
    }

    public void setForCantonResidence(String forCantonResidence) {
        this.forCantonResidence = forCantonResidence;
    }

    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    public void setForIdAllocataire(String forIdAllocataire) {
        this.forIdAllocataire = forIdAllocataire;
    }

    @Override
    public Class<OperationComplexModel> whichModelClass() {
        return OperationComplexModel.class;
    }

    public void setForDateAfterOrEquals(Date dateDebut) {
        setForDateAfterOrEquals(dateDebut.getSwissValue());
    }

    public void setForDateLessOrEquals(Date dateFin) {
        setForDateLessOrEquals(dateFin.getSwissValue());
    }

}
