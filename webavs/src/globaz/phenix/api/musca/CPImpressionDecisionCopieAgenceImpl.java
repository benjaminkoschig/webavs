package globaz.phenix.api.musca;

import globaz.musca.external.IntModuleFacturation;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (24.04.2003 12:51:01)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.04.2003 08:52:52)
     */
    @Override
    protected Boolean getGed() throws Exception {
        // ne pas mettre en GED si c'est une copie agence
        return null;
    }

    @Override
    protected String getOrder() {
        // trier les d�cisions par agence communale
        return "2";
    }
}
