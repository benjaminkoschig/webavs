package globaz.al.process.echeances;

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
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DroitEcheanceService;

/**
 * Process pour l'échéances des droits, pour les listes provisoires
 * 
 * @author PTA
 */
public class ALProtocoleEcheancesProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Exclusion en compte des dossiers adi, par défaut oui
     */

    private Boolean adiExclu = true;
    /**
     * La date limite d'échéance
     */
    private String dateEcheance = null;

    /**
     * 
     * @return the Adi
     */
    public Boolean getAdiExclu() {
        return adiExclu;
    }

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
        ArrayList<DroitEcheanceComplexModel> listeDroitUniqueEcheanceFin = new ArrayList<DroitEcheanceComplexModel>();
        ArrayList<DroitEcheanceComplexModel> listeDroitUniqueEcheanceAReviser = new ArrayList<DroitEcheanceComplexModel>();

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
            HashSet motifFin = echeanceService.getListMotifsAvis();
            HashSet motifEcheance = echeanceService.getListMotifsAutres();
            HashSet typeDroit = echeanceService.getListTypeDroit();
            if ((motifFin == null) || (typeDroit == null) || (motifEcheance == null)) {
                JadeLogger
                        .error(this, new Exception("Erreur: motif de fin , type de droit ou motif echeance est null"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur: motif de fin , type de droit ou motif echeance est null");
                return;
            }

            // traitement liste des échéances pour motif fin(avis échéances)

            listeDroitUniqueEcheanceFin = echeanceService.searchDroitsForEcheance(motifFin, typeDroit, dateEcheance,
                    ALConstEcheances.TYPE_ALL, ALConstEcheances.LISTE_AVIS_ECHEANCES, adiExclu);

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

        // génération de la liste des dossiers avec avis d'échéance

        if (listeDroitUniqueEcheanceFin.size() > 0) {
            JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();

            pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
            pubInfo.setOwnerId(JadeThread.currentUserId());
            pubInfo.setDocumentTitle(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"));
            pubInfo.setDocumentSubject(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"));
            pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            pubInfo.setPublishDocument(true);

            try {
                container.addDocument(
                        ALServiceLocator.getProtocoleDroitEcheancesService().loadData(listeDroitUniqueEcheanceFin,
                                ALConstEcheances.LISTE_AVIS_ECHEANCES, getDateEcheance()), pubInfo);

            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur à l'utilisation du service loadData", e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur à l'utilisation du service loadData");
                return;

            }

        }
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

        }
        try {
            this.createDocuments(container);
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service updateDroitImprimerEcheance"));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service updateDroitImprimerEcheance");
            return;
        }

    }

    public void setAdiExclu(Boolean adiExclu) {
        this.adiExclu = adiExclu;
    }

    /**
     * @param dateEcheance
     *            the dateEcheance to set
     */
    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

}
