package ch.globaz.vulpecula.documents.decompte;

import globaz.framework.printing.itext.FWIDocumentManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;

/**
 * Process de lancement des décomptes vides
 * 
 * @since WebBMS 1.0
 */
public class DocumentDecompteVidePrinter extends DocumentPrinter<DecompteContainer> {
    private static final long serialVersionUID = 1L;

    private List<DecompteContainer> decompteContainer = new ArrayList<DecompteContainer>();

    public DocumentDecompteVidePrinter() {
        super();
    }

    public static DocumentDecompteVidePrinter createWithDecompteContainer(
            Collection<DecompteContainer> decompteContainer) {
        DocumentDecompteVidePrinter printer = new DocumentDecompteVidePrinter();
        printer.decompteContainer = new ArrayList<DecompteContainer>(decompteContainer);
        return printer;
    }

    @Override
    public String getNumeroInforom() {
        switch (getCurrentElement().getDecompte().getType()) {
            case COMPLEMENTAIRE:
                return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_TYPE_NUMBER;
            case PERIODIQUE:
                return DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_TYPE_NUMBER;
            default:
                return "";
        }
    }

    @Override
    public void retrieve() {
        setElements(decompteContainer);
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        DocumentDecompteVide documentDecompteVide = DocumentDecompteVideFactory
                .getTypeDocumentDecompteVide(getCurrentElement());
        documentDecompteVide.setParent(this);
        setDocumentFileName(DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_FILENAME);
        return documentDecompteVide;
    }

    @Override
    protected String getEMailObject() {
        switch (getCurrentElement().getDecompte().getType()) {
            case COMPLEMENTAIRE:
                return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_SUBJECT;
            case PERIODIQUE:
                return DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_SUBJECT;
            default:
                return DocumentConstants.DECOMPTE_VIDE_DEFAULT_SUBJECT;
        }
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
