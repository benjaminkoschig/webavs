package ch.globaz.vulpecula.documents.decompte;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.client.JadePublishDocument;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class DocumentDecompteSpecialPrinter extends BProcessWithContext {
    private Collection<String> ids;

    private final DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();
    private TypeDecompte typeDecompte;

    @Override
    protected String getEMailObject() {
        if (TypeDecompte.CPP == typeDecompte) {
            return getSession().getLabel("DOCUMENT_IMPRESSION_DECOMPTE_CPP");
        } else {
            return getSession().getLabel("DOCUMENT_IMPRESSION_DECOMPTE_SPECIAL");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        List<Decompte> decomptes = decompteRepository.findByIdInWithDependencies(ids);
        for (Decompte decompte : decomptes) {
            decompte.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(decompte.getIdTiers()));
            DocumentDecompteSpecialExcel doc = new DocumentDecompteSpecialExcel(getSession(), decompte);
            doc.executeProcess();
            typeDecompte = decompte.getType();
            JadePublishDocument jadePublishDocument = new JadePublishDocument(doc.getOutputPath(), createDocumentInfo());
            getAttachedDocuments().add(jadePublishDocument);
        }
        return true;
    }

    public void setIds(Collection<String> ids) {
        this.ids = ids;
    }

    public Collection<String> getIds() {
        return ids;
    }
}
