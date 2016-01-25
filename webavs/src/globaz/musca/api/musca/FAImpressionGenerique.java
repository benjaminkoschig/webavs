package globaz.musca.api.musca;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.globall.api.BIEntity;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.external.IntModuleImpression;
import globaz.musca.itext.FAImpressionFacturation;
import java.util.ArrayList;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public abstract class FAImpressionGenerique extends FAGenericPrintImpl implements IntModuleFacturation,
        IntModuleImpression {
    protected FAImpressionFacturation _document = null; // le document itext
    protected ArrayList<BIEntity> entityList = new ArrayList<BIEntity>();

    /**
     * Constructor for FAImpressionGenerique.
     */
    public FAImpressionGenerique() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleImpression#add(BIEntity)
     */
    @Override
    public void add(BIEntity intEntity) throws Exception {
        entityList.add(intEntity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleImpression#endPrinting(IFAPassage, FAImpressionFactureProcess)
     */
    @Override
    public boolean endPrinting(IFAPassage passage, IFAImpressionFactureProcess context) throws Exception {
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.05.2003 10:34:56)
     * 
     * @return globaz.musca.itext.FAImpressionFacture_BVR_Doc
     */
    @Override
    public FWIDocumentInterface get_document() {
        return _document;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleImpression#print(FAImpressionFactureProcess )
     */
    @Override
    public boolean print() throws Exception {
        _document.setEntityList(entityList);
        _document.executeProcess();
        return true;
    }

}
