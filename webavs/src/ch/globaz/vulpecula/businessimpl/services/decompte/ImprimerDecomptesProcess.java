package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.globall.db.GlobazJobQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.documents.decompte.CotisationsInfo;
import ch.globaz.vulpecula.documents.decompte.DecompteContainer;
import ch.globaz.vulpecula.documents.decompte.DocumentDecompteBVRPrinter;
import ch.globaz.vulpecula.documents.decompte.DocumentDecompteVidePrinter;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.external.BProcessWithContext;

/**
 * Processus permettant d'imprimer les décomptes vides ET/OU les BVR.
 * Ces processus sont lancés de manière asynchrones sur le serveur de batch.
 * Comme il n'est pas possible de synchroniser les deux processus pour générer les documents, on lance deux processus
 * séparés.
 * 
 * Pour l'impression des décomptes vides, le processus va rechercher le type de décompte du premier décompte passé en
 * paramètre afin de déterminer le type de documents. Ce processus n'est pas capable de générer en même temps des
 * décomptes complémentaires et des décomptes périodiques.
 */
public class ImprimerDecomptesProcess extends BProcessWithContext {
    private static final long serialVersionUID = -2978250142659956380L;

    private final DecompteService decompteService = VulpeculaServiceLocator.getDecompteService();
    private List<String> ids = new ArrayList<String>();

    private boolean printDecompteVide = true;
    private boolean printDecompteBVR = true;

    /**
     * A NE PAS UTILISER, POUR LA DESERIALISATION DU JOB PAR LE FRAMEWORK
     */
    public ImprimerDecomptesProcess() {
    }

    /**
     * Construction de l'objet grâce à une liste de décomptes. Les ids seront ensuite extraits afin de pouvoir être
     * transmissible au serveur de Batch (sérialisation).
     * 
     * @param decomptes Décomptes à sérialiser
     * @return Processus de génération des documents
     */
    public static ImprimerDecomptesProcess createWithDecomptes(Collection<Decompte> decomptes) {
        ImprimerDecomptesProcess process = new ImprimerDecomptesProcess();
        process.ids = DocumentPrinter.getIds(decomptes);
        return process;
    }

    /**
     * Construction de l'objet grâce à une liste d'id.
     * 
     * @param idDecomptes Liste des ids sur lesquels générer les documents
     * @return Processus de génération des documents
     */
    public static ImprimerDecomptesProcess createWithIds(List<String> idDecomptes) {
        ImprimerDecomptesProcess process = new ImprimerDecomptesProcess();
        process.ids = idDecomptes;
        return process;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        setSendCompletionMail(false);
        print();
        return true;
    }

    private void print() throws Exception {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        if (!ids.isEmpty()) {
            decomptes = VulpeculaRepositoryLocator.getDecompteRepository().findByIdInWithDependencies(ids);
        }

        List<DecompteContainer> decompteContainers = new ArrayList<DecompteContainer>();
        for (Decompte decompte : decomptes) {
            decompteService.retrieveDecompteInfos(decompte);

            CotisationsInfo cotisationInfo = decompteService.retrieveCotisationsInfo(decompte.getIdEmployeur(),
                    decompte.getPeriodeDebut());
            DecompteContainer decompteContainer = new DecompteContainer(decompte.getType());
            decompteContainer.setDecompte(decompte);
            decompteContainer.setCotisationsInfo(cotisationInfo);
            decompteContainers.add(decompteContainer);
        }

        printDecomptesBVR(decompteContainers);
        printDecomptesVides(decompteContainers);
    }

    private void printDecomptesBVR(List<DecompteContainer> decompteContainers) throws Exception {
        if (printDecompteBVR) {
            DocumentDecompteBVRPrinter documentDecompteBVRPrinter = DocumentDecompteBVRPrinter
                    .createWithDecompteContainer(decompteContainers);
            documentDecompteBVRPrinter.setParent(this);
            documentDecompteBVRPrinter.setEMailAddress(getEMailAddress());
            documentDecompteBVRPrinter.executeProcess();
        }
    }

    private void printDecomptesVides(List<DecompteContainer> decompteContainers) throws Exception {
        if (printDecompteVide) {
            DocumentDecompteVidePrinter documentDecompteVidePrinter = DocumentDecompteVidePrinter
                    .createWithDecompteContainer(decompteContainers);
            documentDecompteVidePrinter.setParent(this);
            documentDecompteVidePrinter.setEMailAddress(getEMailAddress());
            documentDecompteVidePrinter.executeProcess();
        }
    }

    public void dontPrintDecompteBVR() {
        printDecompteBVR = false;
    }

    public void dontPrintDecompteVide() {
        printDecompteVide = false;
    }

    @Override
    protected String getEMailObject() {
        // Pas besoin d'objet puisque ce processus n'enverra aucun email
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public final List<String> getIds() {
        return ids;
    }

    public final void setIds(List<String> ids) {
        this.ids = ids;
    }
}
