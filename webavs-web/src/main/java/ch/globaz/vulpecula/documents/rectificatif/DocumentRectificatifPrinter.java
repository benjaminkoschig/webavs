package ch.globaz.vulpecula.documents.rectificatif;

import globaz.framework.printing.itext.FWIDocumentManager;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

public class DocumentRectificatifPrinter extends DocumentPrinter<Decompte> {
    private static final long serialVersionUID = 4045943166925721836L;

    private Collection<String> ids;

    public DocumentRectificatifPrinter() {
        super();
    }

    public DocumentRectificatifPrinter(Collection<String> ids) {
        this.ids = ids;
    }

    public Collection<String> getIds() {
        return ids;
    }

    public void setIds(Collection<String> ids) {
        this.ids = ids;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.RECTIFICATIF_TYPE_NUMBER;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentRectificatif(getCurrentElement());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.RECTIFICATIF_SUBJECT;
    }

    @Override
    public void retrieve() {
        List<Decompte> decomptes = VulpeculaRepositoryLocator.getDecompteRepository().findByIdInWithDependencies(ids);
        TreeMap<String, Decompte> decomptesForRectificationTri = new TreeMap<String, Decompte>();
        for (Decompte decompte : decomptes) {
            String key = getKey(decompte);
            decompte.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findByIdDecompteWithDependencies(decompte.getId()));
            decompte.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(decompte.getIdTiers()));
            decomptesForRectificationTri.put(key, decompte);
        }
        setElements(decomptesForRectificationTri.values());
    }

    /**
     * @param decompte
     * @return
     */
    private String getKey(Decompte decompte) {
        String key = decompte.getEmployeur().getConvention().getCode();
        return key + decompte.getEmployeur().getRaisonSociale() + "-" + decompte.getId();
    }
}
