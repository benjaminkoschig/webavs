package ch.globaz.vulpecula.documents.decompte;

import globaz.framework.printing.itext.FWIDocumentManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

/**
 * Process de lancement des décomptes vides
 * 
 * @since WebBMS 1.0
 */
public class DocumentDecompteVidePrinter extends DocumentPrinter<DecompteContainer> {
    private static final long serialVersionUID = 1L;
    private boolean isPrintingFromEbu = false;

    static class InfoACDecompte {
        public Montant trancheACMax;
        public Montant trancheAC2Min;
    }

    private List<DecompteContainer> decompteContainer = new ArrayList<DecompteContainer>();
    private Map<Annee, InfoACDecompte> mapCotisations = new HashMap<Annee, DocumentDecompteVidePrinter.InfoACDecompte>();

    public DocumentDecompteVidePrinter() {
        super();
    }

    public static DocumentDecompteVidePrinter createWithDecompteContainer(
            Collection<DecompteContainer> decompteContainer) {
        DocumentDecompteVidePrinter printer = new DocumentDecompteVidePrinter();
        printer.setPrintingFromEbu(false);
        printer.decompteContainer = new ArrayList<DecompteContainer>(decompteContainer);
        return printer;
    }

    public static DocumentDecompteVidePrinter createWithDecompteContainer(
            Collection<DecompteContainer> decompteContainer, boolean printingFromEbu) {
        DocumentDecompteVidePrinter printer = new DocumentDecompteVidePrinter();
        printer.setPrintingFromEbu(printingFromEbu);
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
        TreeMap<String, DecompteContainer> decompteContainerTri = new TreeMap<String, DecompteContainer>();
        for (DecompteContainer decompteCon : decompteContainer) {
            // String key = decompteCon.getDecompte().getEmployeurAffilieNumero() + "-"
            // + decompteCon.getDecompte().getId();
            // BMS-2502 Demande de Mme Dell'Estate le 01.09.2016
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

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        DecompteContainer decompteContainer = getCurrentElement();
        Annee anneeFin = new Annee(decompteContainer.getDecompte().getAnneePeriodeFin());
        PeriodeMensuelle periode = decompteContainer.getDecompte().getPeriode();
        int multiplicateur = periode.getNombreMois() + 1;
        DocumentDecompteVide documentDecompteVide = DocumentDecompteVideFactory.getTypeDocumentDecompteVide(
                decompteContainer, isPrintingFromEbu());
        documentDecompteVide.setPrintingFromEbu(isPrintingFromEbu());
        documentDecompteVide.setParent(this);
        documentDecompteVide.setInfo(getInfosAC(anneeFin, multiplicateur));
        if (TypeDecompte.COMPLEMENTAIRE.equals(decompteContainer.getTypeDecompte())) {
            setDocumentFileName(DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_FILENAME);
        } else {
            setDocumentFileName(DocumentConstants.DECOMPTE_VIDE_PERIODIQUE_FILENAME);
        }

        return documentDecompteVide;
    }

    private InfoACDecompte getInfosAC(Annee annee, int multiplicateur) {
        if (!mapCotisations.containsKey(annee)) {
            InfoACDecompte info = new InfoACDecompte();
            Montant plafondAC = VulpeculaServiceLocator.getCotisationService()
                    .getPlafondAC(annee.getFirstDayOfYear(), getSession()).divide(12);
            plafondAC = plafondAC.multiply(multiplicateur);
            info.trancheACMax = plafondAC;
            info.trancheAC2Min = plafondAC.add(1);
            mapCotisations.put(annee, info);
        }
        return mapCotisations.get(annee);
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

    public boolean isPrintingFromEbu() {
        return isPrintingFromEbu;
    }

    public void setPrintingFromEbu(boolean isPrintingFromEbu) {
        this.isPrintingFromEbu = isPrintingFromEbu;
    }

}
