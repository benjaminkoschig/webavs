package globaz.phenix.api.musca;

import globaz.musca.external.IntModuleFacturation;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class CPImpressionDecisionCopieAgenceImpl extends CPImpressionDecisionImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public CPImpressionDecisionCopieAgenceImpl() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 08:52:52)
     */
    @Override
    protected Boolean getGed() throws Exception {
        // ne pas mettre en GED si c'est une copie agence
        return null;
    }

    @Override
    protected String getOrder() {
        // trier les décisions par agence communale
        return "2";
    }
}
