package ch.globaz.vulpecula.documents.decompte;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.client.JadePublishDocument;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class DocumentDecompteSpecialPrinter extends BProcessWithContext {
    private Collection<String> ids;

    private final DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();

    @Override
    protected String getEMailObject() {
        return "Décompte spécial";
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
                    .findAdresseDomicileByIdTiers(decompte.getIdTiers()));
            DocumentDecompteSpecialExcel doc = new DocumentDecompteSpecialExcel(getSession(), decompte);
            doc.executeProcess();
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
