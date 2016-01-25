package ch.globaz.pegasus.business.models.recap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;

public class RepracPcaInfos {
    Map<RecapDomainePca, BigDecimal> diffPcaReprac = new HashMap<RecapDomainePca, BigDecimal>();

    Map<RecapDomainePca, RecapituatifPaiement> pca = new HashMap<RecapDomainePca, RecapituatifPaiement>();

    Map<RecapDomainePca, RecapituatifPaiement> reprac = new HashMap<RecapDomainePca, RecapituatifPaiement>();

    public Map<RecapDomainePca, BigDecimal> getDiffPcaReprac() {
        return diffPcaReprac;
    }

    public Map<RecapDomainePca, RecapituatifPaiement> getPca() {
        return pca;
    }

    public Map<RecapDomainePca, RecapituatifPaiement> getReprac() {
        return reprac;
    }

    public void setDiffPcaReprac(Map<RecapDomainePca, BigDecimal> diffPcaReprac) {
        this.diffPcaReprac = diffPcaReprac;
    }

    public void setPca(Map<RecapDomainePca, RecapituatifPaiement> pca) {
        this.pca = pca;
    }

    public void setReprac(Map<RecapDomainePca, RecapituatifPaiement> reprac) {
        this.reprac = reprac;
    }

}
