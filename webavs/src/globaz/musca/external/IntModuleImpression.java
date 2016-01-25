package globaz.musca.external;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.globall.api.BIContainer;
import globaz.globall.api.BIEntity;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAPassage;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (01.04.2003 18:01:11)
 * 
 * @author: Administrator
 */
public interface IntModuleImpression {
    public void add(BIEntity intEntity) throws Exception;

    public boolean beginPrinting(IFAPassage passage, IFAImpressionFactureProcess context) throws Exception;

    public boolean endPrinting(IFAPassage passage, IFAImpressionFactureProcess context) throws Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.05.2003 10:34:56)
     * 
     * @return globaz.musca.itext.FAImpressionFacture_BVR_Doc
     */
    public FWIDocumentInterface get_document();

    /**
     * Method get_documentManager Retourne le container utilis� dans la recherche de tous les entity du document �
     * imprimer
     * 
     * @return BIContainer
     */
    public BIContainer get_documentManager(IFAImpressionFactureProcess context);

    /**
     * Method print.
     * 
     * @param intEntity
     * @param context
     * @return boolean
     * @throws Exception
     */
    public boolean print() throws Exception;
}
