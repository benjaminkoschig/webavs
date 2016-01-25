package ch.globaz.vulpecula.daemon.suividecompte;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.jade.properties.JadePropertiesService;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.application.ApplicationConstants;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.documents.rappels.DocumentSommationPrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;

public class ProcessSommation extends AbstractDaemon {
    /**
     * Propriété contenant l'adresse email à laquelle les documents doivent être envoyés.
     */
    private static final String VULPECULA_PROCESS_EMAIL_ADDRESS = "vulpecula.process.contactEmail";

    /**
     * Adresse email à laquelle sont envoyés les documents.
     */
    private String emailAddress;

    private BSession bsession;

    public ProcessSommation() {
        emailAddress = JadePropertiesService.getInstance().getProperty(VULPECULA_PROCESS_EMAIL_ADDRESS);
    }

    @Override
    public void run() {
        try {
            initBsession();
            List<Decompte> decomptesForSommation = getDecomptesForSommation();

            if (decomptesForSommation.isEmpty() == false) {
                for (Decompte decompte : decomptesForSommation) {
                    addSommationToHistorique(decompte);
                    decompte.setEtat(EtatDecompte.SOMMATION);
                    setNouvelleDateDeRappel(decompte);
                    VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);
                    decompte.getEmployeur().setAdressePrincipale(
                            VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                                    decompte.getEmployeur().getIdTiers()));
                }

                DocumentSommationPrinter documentSommation = new DocumentSommationPrinter(
                        DocumentPrinter.getIds(decomptesForSommation));
                documentSommation.setEMailAddress(emailAddress);
                documentSommation.setSession(bsession);
                BProcessLauncher.start(documentSommation);
            }
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
        }
    }

    private void setNouvelleDateDeRappel(Decompte decompte) {
        int nbJoursSuperieurEtablissement = VulpeculaServiceLocator.getPropertiesService()
                .getTaxationNombreJoursSuperieureEtablissement();
        int nbJoursInferieurEtablissement = VulpeculaServiceLocator.getPropertiesService()
                .getTaxationNombreJoursInferieureEtablissement();
        Date nouvelleDateRappel = decompte.calculerTaxation(nbJoursSuperieurEtablissement,
                nbJoursInferieurEtablissement, Date.now());
        decompte.setDateRappel(nouvelleDateRappel);
    }

    private void initBsession() throws Exception {
        bsession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(bsession, this);
    }

    /**
     * Retourne la liste des décomptes à passer en sommation.
     */
    private List<Decompte> getDecomptesForSommation() {
        return VulpeculaRepositoryLocator.getDecompteRepository().findDecomptesForSommation();
    }

    /**
     * Ajoute un historique "SOMMATION" à l'historique du décompte.
     */
    private void addSommationToHistorique(Decompte decompte) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDate(Date.now());
        historiqueDecompte.setEtat(EtatDecompte.SOMMATION);
        historiqueDecompte.setDecompte(decompte);
        VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().create(historiqueDecompte);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }
}
