package ch.globaz.vulpecula.documents.catalog;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.external.BProcessWithContext;

public abstract class DocumentPrinter<T extends Serializable> extends BProcessWithContext {
    private static final long serialVersionUID = -1228965146801743634L;

    private List<T> elements;
    private T currentElement;
    private String documentFileName = null;

    protected String getMessageNoElements() {
        return getSession().getLabel("EMAIL_PAS_ELEMENTS");
    }

    public DocumentPrinter() {
        super();
    }

    public static List<String> getIds(Collection<? extends DomainEntity> entities) {
        List<String> ids = new ArrayList<String>();
        for (DomainEntity entity : entities) {
            ids.add(entity.getId());
        }
        return ids;
    }

    @Override
    protected final boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        if (isOnError()) {
            return false;
        }
        if (elements == null || elements.size() == 0) {
            getTransaction().addErrors(getMessageNoElements());
            return false;
        }

        if (getParent() != null) {
            getParent().setProgressScaleValue(elements.size());
        } else {
            setProgressScaleValue(elements.size());
        }
        for (T element : elements) {
            if (getParent() != null) {
                getParent().incProgressCounter();
            } else {
                incProgressCounter();
            }

            currentElement = element;
            FWIDocumentManager documentManager = createDocument();
            documentManager.setParent(this);
            documentManager.executeProcess();
        }
        fusion();
        return true;
    }

    public void fusion() throws Exception {
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentType(getNumeroInforom());
        docInfo.setDocumentTypeNumber(getNumeroInforom());

        if (documentFileName != null && documentFileName.length() != 0) {
            this.mergePDF(docInfo, documentFileName, false, 0, false, null, null);
        } else {
            this.mergePDF(docInfo, false, 0, false, null);
        }
    }

    public abstract String getNumeroInforom();

    public abstract void retrieve();

    public T getCurrentElement() {
        return currentElement;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setCurrentElement(T currentElement) {
        this.currentElement = currentElement;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public abstract FWIDocumentManager createDocument() throws Exception;

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param documentFileName the documentFileName to set
     */
    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

}
