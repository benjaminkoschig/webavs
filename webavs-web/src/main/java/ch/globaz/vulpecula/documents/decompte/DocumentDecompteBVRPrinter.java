package ch.globaz.vulpecula.documents.decompte;

import globaz.framework.printing.itext.FWIDocumentManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;

public class DocumentDecompteBVRPrinter extends DocumentPrinter<DecompteContainer> {
    private static final long serialVersionUID = 5203096441297081387L;

    private List<DecompteContainer> decompteContainer = new ArrayList<DecompteContainer>();

    public DocumentDecompteBVRPrinter() {
        super();
    }

    public static DocumentDecompteBVRPrinter createWithDecompteContainer(Collection<DecompteContainer> decompteContainer) {
        DocumentDecompteBVRPrinter printer = new DocumentDecompteBVRPrinter();
        printer.decompteContainer = new ArrayList<DecompteContainer>(decompteContainer);
        return printer;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.DECOMPTE_BVR_TYPE_NUMBER;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        DocumentDecompteBVR documentDecompteBVR = new DocumentDecompteBVR(getCurrentElement());
        documentDecompteBVR.setParent(this);
        setDocumentFileName(DocumentConstants.DECOMPTE_BVR_FILENAME);
        return documentDecompteBVR;
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.DECOMPTE_BVR_SUBJECT;
    }

    @Override
    public void retrieve() {
        setElements(decompteContainer);
    }

    /**
     * @return the decompteContainer
     */
    public List<DecompteContainer> getDecompteContainer() {
        return decompteContainer;
    }

    /**
     * @param decompteContainer the decompteContainer to set
     */
    public void setDecompteContainer(List<DecompteContainer> decompteContainer) {
        this.decompteContainer = decompteContainer;
    }

}
