package globaz.musca.external;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.globall.api.BIContainer;
import globaz.globall.api.BIEntity;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAPassage;

/**
 * Insérez la description du type ici. Date de création : (01.04.2003 18:01:11)
 * 
 * @author: Administrator
 */
public interface IntModuleImpression {
    public void add(BIEntity intEntity) throws Exception;

    public boolean beginPrinting(IFAPassage passage, IFAImpressionFactureProcess context) throws Exception;

    public boolean endPrinting(IFAPassage passage, IFAImpressionFactureProcess context) throws Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (22.05.2003 10:34:56)
     * 
     * @return globaz.musca.itext.FAImpressionFacture_BVR_Doc
     */
    public FWIDocumentInterface get_document();

    /**
     * Method get_documentManager Retourne le container utilisé dans la recherche de tous les entity du document à
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
