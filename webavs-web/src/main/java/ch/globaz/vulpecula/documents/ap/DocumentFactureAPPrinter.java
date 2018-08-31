package ch.globaz.vulpecula.documents.ap;

import java.util.List;
import java.util.TreeMap;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import globaz.framework.printing.itext.FWIDocumentManager;

public class DocumentFactureAPPrinter extends DocumentPrinter<FactureAssociation> {
    private static final long serialVersionUID = 374499513534476823L;

    private List<String> ids;
    private String idPassage;

    @Override
    public String getNumeroInforom() {
        return null;
    }

    @Override
    public void retrieve() {
        List<FactureAssociation> factures = VulpeculaRepositoryLocator.getFactureAssociationRepository().findByIdIn(ids)
                .getFactures();
        idPassage = factures.get(0).getIdPassageFacturation();

        TreeMap<String, FactureAssociation> facturesOrderByRaisonSociale = new TreeMap<String, FactureAssociation>();
        for (FactureAssociation fa : factures) {
            fa.getEmployeur().setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(fa.getEmployeur().getIdTiers()));
            facturesOrderByRaisonSociale.put(fa.getEmployeurRaisonSociale() + "-" + fa.getId(), fa);
        }
        setElements(facturesOrderByRaisonSociale.values());
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentFactureAP(getCurrentElement());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.FACTURE_AP_NAME + "; idPassage : " + idPassage;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
