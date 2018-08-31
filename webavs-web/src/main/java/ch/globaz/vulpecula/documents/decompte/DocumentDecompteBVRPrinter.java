package ch.globaz.vulpecula.documents.decompte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import globaz.framework.printing.itext.FWIDocumentManager;

public class DocumentDecompteBVRPrinter extends DocumentPrinter<DecompteContainer> {
    private static final long serialVersionUID = 5203096441297081387L;

    private List<DecompteContainer> decompteContainer = new ArrayList<DecompteContainer>();

    public DocumentDecompteBVRPrinter() {
        super();
    }

    public static DocumentDecompteBVRPrinter createWithDecompteContainer(
            Collection<DecompteContainer> decompteContainer) {
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
        TreeMap<String, DecompteContainer> decompteContainerTri = new TreeMap<String, DecompteContainer>();
        for (DecompteContainer decompteCon : decompteContainer) {
            // BMS-2502 Demande de Mme Dell'Estate le 01.09.2016
            // String key = decompteCon.getDecompte().getEmployeurAffilieNumero() + "-"
            // + decompteCon.getDecompte().getId();
            String key = getKey(decompteCon.getDecompte());
            decompteContainerTri.put(key, decompteCon);
        }

        setElements(decompteContainerTri.values());
    }

    /**
     * @param decompte
     * @return
     */
    private String getKey(Decompte decompte) {
        String key = decompte.getEmployeur().getConvention().getCode();
        return key + decompte.getEmployeur().getRaisonSociale() + "-" + decompte.getId();
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
