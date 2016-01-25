package globaz.babel.process;

import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.jade.job.AbstractJadeJob;

public abstract class CTAbstractJadeJob extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ICTScalableDocumentProperties scalableDocumentProperties = null;

    public ICTScalableDocumentProperties getScalableDocumentProperties() {
        return scalableDocumentProperties;
    }

    public void setScalableDocumentProperties(ICTScalableDocumentProperties scalableDoc) {
        scalableDocumentProperties = scalableDoc;
    }

}
