package ch.globaz.vulpecula.domain.specifications.congepaye;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;

public class CPSalaireDeclareSpecification extends AbstractSpecification<CongePaye> {

    @Override
    public boolean isValid(CongePaye congePaye) {
        if (!isDateSalaireNonDeclareSaisie(congePaye) && isSalaireNonDeclaireSaisi(congePaye)) {
            addMessage(SpecificationMessage.CP_SALAIRE_NON_DECLARE_DATE_REQUISE);
        }
        if (isDateSalaireNonDeclareSaisie(congePaye) && !isSalaireNonDeclaireSaisi(congePaye)) {
            addMessage(SpecificationMessage.CP_DATE_SALAIRE_DECLARE_MONTANT_REQUIS);
        }
        return true;
    }

    private boolean isSalaireNonDeclaireSaisi(CongePaye congePaye) {
        return congePaye.getSalaireNonDeclare() != null && !congePaye.getSalaireNonDeclare().equals(Montant.ZERO);
    }

    private boolean isDateSalaireNonDeclareSaisie(CongePaye congePaye) {
        return congePaye.getDateSalaireNonDeclare() != null;
    }

}
