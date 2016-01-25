package ch.globaz.pegasus.business.models.recap;

import java.math.BigDecimal;
import java.util.Map;
import ch.globaz.pegasus.business.models.mutation.RecapInfoDomaine;
import ch.globaz.pegasus.business.models.mutation.RecapListMutation;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;

/**
 * RecapDomainePca: Représente le domaine d'une PCA. Ex: AI ou AVS. Mais par la suite on pourrait gérer d'autre domaine
 * 
 * RecapCategorie: Représente la catégorie de la mutation. EX Future, Adaptation, Normal. En faite Représente plus un
 * onglet
 * 
 * @author dma
 * 
 */
public class Recap {

    Map<RecapDomainePca, RecapInfoDomaine> infosDomaine;

    Map<RecapCategorie, Map<RecapDomainePca, RecapListMutation>> recapMutation;

    RepracPcaInfos recapPaimentCourant = null;
    BigDecimal totalMois = new BigDecimal(0);
    BigDecimal totalMutation = new BigDecimal(0);
    BigDecimal totalPaiement = new BigDecimal(0);

    public Map<RecapDomainePca, RecapInfoDomaine> getInfosDomaine() {
        return infosDomaine;
    }

    public Map<RecapCategorie, Map<RecapDomainePca, RecapListMutation>> getRecapMutation() {
        return recapMutation;
    }

    public RepracPcaInfos getRecapPaimentCourant() {
        return recapPaimentCourant;
    }

    public BigDecimal getTotalMois() {
        return totalMois;
    }

    public BigDecimal getTotalMutation() {
        return totalMutation;
    }

    public BigDecimal getTotalPaiement() {
        return totalPaiement;
    }

    public void setInfosDomaine(Map<RecapDomainePca, RecapInfoDomaine> infosDomaine) {
        this.infosDomaine = infosDomaine;
    }

    public void setRecapMutation(Map<RecapCategorie, Map<RecapDomainePca, RecapListMutation>> recapMutation) {
        this.recapMutation = recapMutation;
    }

    public void setRecapPaimentCourant(RepracPcaInfos recapPaimentCourant) {
        this.recapPaimentCourant = recapPaimentCourant;
    }

    public void setTotalMois(BigDecimal totalMois) {
        this.totalMois = totalMois;
    }

    public void setTotalMutation(BigDecimal totalMutation) {
        this.totalMutation = totalMutation;
    }

    public void setTotalPaiement(BigDecimal totalPaiement) {
        this.totalPaiement = totalPaiement;
    }

}
