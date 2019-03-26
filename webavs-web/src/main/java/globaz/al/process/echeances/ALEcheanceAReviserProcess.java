package globaz.al.process.echeances;

import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DroitEcheanceService;
import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class ALEcheanceAReviserProcess extends ALAbsrtactProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void setAdiExclu(Boolean adiExclu) {
        this.adiExclu = adiExclu;
    }

    /**
     * Exclusion en compte des dossiers adi, par défaut oui
     */

    private Boolean adiExclu = true;

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * La date limite d'échéance
     */
    private String dateEcheance = null;

    /**
     * @return the dateEcheance
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.echeances.ALProtocoleEcheancesProcess.description");
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.echeances.ALProtocoleEcheancesProcess.name");
    }

    @Override
    protected void process() {
        DroitEcheanceService echeanceService;
        ArrayList<DroitEcheanceComplexModel> listeDroitUniqueEcheanceAReviser;

        try {
            //
            echeanceService = ALServiceLocator.getDroitEcheanceService();
        } catch (JadeApplicationServiceNotAvailableException e1) {
            JadeLogger.error(this, new Exception("Erreur pour atteindre le service getDroitEcheanceService", e1));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur pour atteindre le service getDroitEcheanceService");
            return;
        }

        try {
            Set<String> motifEcheance = echeanceService.getListMotifsAutres();
            Set<String> typeDroit = echeanceService.getListTypeDroit();
            // traitement liste des échéances pour autres motifs(dossier à
            // réviser)
            listeDroitUniqueEcheanceAReviser = echeanceService.searchDroitsForEcheance(motifEcheance, typeDroit,
                    dateEcheance, ALConstEcheances.TYPE_ALL, ALConstEcheances.LISTE_AUTRES_ECHEANCES, adiExclu);
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service droit/échéance", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service droit/échéance");
            return;
        }

        JadePrintDocumentContainer container = new JadePrintDocumentContainer();

        // génération de la liste des dossiers à réviser

        if (listeDroitUniqueEcheanceAReviser.size() > 0) {
            JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
            pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
            pubInfo.setOwnerId(JadeThread.currentUserId());
            pubInfo.setDocumentTitle(JadeThread.getMessage("al.echeances.titre.protocole.dossierReviser"));
            pubInfo.setDocumentSubject(JadeThread.getMessage("al.echeances.titre.protocole.dossierReviser"));
            pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            pubInfo.setPublishDocument(true);

            try {
                container.addDocument(
                        ALServiceLocator.getProtocoleDroitEcheancesService().loadData(listeDroitUniqueEcheanceAReviser,
                                ALConstEcheances.LISTE_AUTRES_ECHEANCES, getDateEcheance()), pubInfo);
            } catch (Exception e1) {
                JadeLogger.error(this, new Exception("Erreur à l'utilisation du service loadData", e1));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur à l'utilisation du service loadData");
                return;
            }

            try {
                this.createDocuments(container);

            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur à l'utilisation du service updateDroitImprimerEcheance"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur à l'utilisation du service updateDroitImprimerEcheance");
            }
        }

    }
}
