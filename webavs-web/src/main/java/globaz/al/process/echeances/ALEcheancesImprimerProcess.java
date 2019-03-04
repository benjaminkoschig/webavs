package globaz.al.process.echeances;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DroitEcheanceService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALEcheanceUtils;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Classe de gestion des échéances à imprimer
 * 
 * @author PTA
 * 
 */
public class ALEcheancesImprimerProcess extends ALAbsrtactProcess {

    private static final String CODE_INFOROM_AVIS_ECHEANCE = "3027WAF";
    private static final long serialVersionUID = -4755483201383089329L;
    /**
     * Prise en compte des dossiers adi, par défaut non
     */

    private Boolean adiExclu = true;
    /**
     * indique si la copie pour les allocataires doit être imprimée pour les dossiers à tiers bénéficiaire, si pas
     * setter par le viewBean appelant => false
     */
    private Boolean copieAllocPourDossierBeneficiaire = false;
    
    private Boolean groupParPays = false;

    /**
     * La date limite d'échéance
     */
    private String dateEcheance = null;

    public Boolean getAdiExclu() {
        return adiExclu;
    }

    public Boolean getCopieAllocPourDossierBeneficiaire() {
        return copieAllocPourDossierBeneficiaire;
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
                "globaz.al.process.echeances.ALEcheancesImprimerProcess.description");
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.echeances.ALEcheancesImprimerProcess.name");
    }

    // modification
    @Override
    protected void process() {
        ProtocoleLogger logger = new ProtocoleLogger();
        boolean errorInProcess = false;

        DroitEcheanceService deService = null;

        ArrayList<DroitEcheanceComplexModel> listeDroitUniqueEcheanceFin = new ArrayList<>();
        ArrayList<DroitEcheanceComplexModel> listeDroitUniqueEcheanceAReviser = new ArrayList<>();

        try {
            deService = ALServiceLocator.getDroitEcheanceService();

            Set<String> motifFin = deService.getListMotifsAvis();
            Set<String> typeDroit = deService.getListTypeDroit();
            Set<String> motifEcheance = deService.getListMotifsAutres();
            if ((motifFin == null) || (typeDroit == null) || (motifEcheance == null)) {
                JadeLogger.error(this, new Exception("Erreur: motif de fin ou type de droit est null"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur: motif de fin ou type de droit est null");
                return;
            }

            // service recherche pour les dossiers à avis
            // d'échéances
            listeDroitUniqueEcheanceFin = deService.searchDroitsForEcheance(motifFin, typeDroit, dateEcheance,
                    ALConstEcheances.TYPE_ALL, ALConstEcheances.LISTE_AVIS_ECHEANCES, adiExclu);

            // recherche pour les dossier à réviser
            listeDroitUniqueEcheanceAReviser = deService.searchDroitsForEcheance(motifEcheance, typeDroit,
                    dateEcheance, ALConstEcheances.TYPE_ALL, ALConstEcheances.LISTE_AUTRES_ECHEANCES, adiExclu);

            // mise à jour des droits dont échéance est imprimé(=> annoncer échéance décochée)
            try {
                deService.updateDroitImprimerEcheance(listeDroitUniqueEcheanceFin, logger);
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur à l'utilisation du service updateDroitImprimerEcheance"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur à l'utilisation du service updateDroitImprimerEcheance");
                return;

            }
            
            // mise à jour des droits à réviser (=> annoncer échéance décochée)
            try {

                for (int i = 0; i < listeDroitUniqueEcheanceAReviser.size(); i++) {

                    DroitEcheanceComplexModel droitEcheance = listeDroitUniqueEcheanceAReviser.get(i);

                    LibraServiceLocator.getEcheanceService().createRappelWithTestDossier(
                            droitEcheance.getDroitModel().getFinDroitForcee(),
                            droitEcheance.getDroitModel().getIdDossier(),
                            JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                                    "al.journlisation.dossierAReviser"), droitEcheance.getIdTiersAllocataire(),
                            ILIConstantesExternes.CS_DOMAINE_AF, true);

                }

            } catch (Exception e) {
                JadeLogger
                        .error(this,
                                new Exception(
                                        "Erreur à l'utilisation du service LibraServiceLocator.getEcheanceService().createRappelOnDossierTier"));
            }
            try {
                deService.updateDroitImprimerEcheance(listeDroitUniqueEcheanceAReviser, logger);
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur à l'utilisation du service updateDroitImprimerEcheance"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur à l'utilisation du service updateDroitImprimerEcheance");
                return;

            }

            // publication du document fusionné
            // container document
            JadePrintDocumentContainer container = new JadePrintDocumentContainer();
            JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();

            pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
            pubInfo.setOwnerId(JadeThread.currentUserId());
            pubInfo.setDocumentTitle(JadeThread.getMessage("al.echeances.titre.echeanceImpression"));
            pubInfo.setDocumentSubject(JadeThread.getMessage("al.echeances.titre.echeanceImpression"));
            pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            pubInfo.setPublishDocument(true);
            pubInfo.setArchiveDocument(false);
            pubInfo.setDocumentTypeNumber(ALEcheancesImprimerProcess.CODE_INFOROM_AVIS_ECHEANCE);

            container = ALServiceLocator.getImpressionEcheancesServices().loadDocuments(listeDroitUniqueEcheanceFin,
                    getDateEcheance(), copieAllocPourDossierBeneficiaire, logger);

            container.setMergedDocDestination(pubInfo);

            // ajout du protocole d'erreur

            // préparation du protocole
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(ALConstProtocoles.INFO_PASSAGE, "non défini");

            params.put(ALConstProtocoles.INFO_PROCESSUS, JadeThread.getMessage("al.echeances.titre.echeanceImpression"));
            params.put(ALConstProtocoles.INFO_TRAITEMENT,
                    JadeThread.getMessage("al.echeances.titre.echeanceImpression"));
            params.put(ALConstProtocoles.INFO_PERIODE, dateEcheance);

            this.createDocuments(container);

            JadePrintDocumentContainer container2 = new JadePrintDocumentContainer();
            container2.addDocument(ALImplServiceLocator.getProtocoleErreursImpressionEcheancesService()
                    .getDocumentData(logger, params), pubInfo);
            this.createDocuments(container2);
            
            if(groupParPays) {
                processParPays(listeDroitUniqueEcheanceFin, logger);
            }
            
        } catch (JadeApplicationException e1) {
            errorInProcess = true;
            e1.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e1.getMessage() });
        } catch (JadePersistenceException e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        } catch (JadeServiceLocatorException e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        } catch (JadeServiceActivatorException e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        } catch (NullPointerException e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        } catch (ClassCastException e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        } catch (JadeClassCastException e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        } catch (Exception e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        }

        // Envoie d'un mail si problème pour lancer le traitement
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        if (errorInProcess) {
            try {
                sendCompletionMail(emails);
            } catch (Exception e1) {
                JadeLogger.error(this,
                        "Impossible d'envoyer le mail de résultat du traitement. Raison : " + e1.getMessage() + ", "
                                + e1.getCause());
            }

        }

    }
    
    private void processParPays(ArrayList<DroitEcheanceComplexModel> listeDroits, ProtocoleLogger logger) throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException, JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {
        Map<String, ArrayList<DroitEcheanceComplexModel>> map = new HashMap<>();
        Map<String, String> infosMailTmp = new HashMap<>();
        
        Map<String, String> pays = new HashMap<>();
        for (DroitEcheanceComplexModel droit : listeDroits) {
            String key = droit.getIdPaysResidence();
            if(pays.get(key) == null) {
                pays.put(key, ALEcheanceUtils.getLibellePays(key, getSession().getIdLangueISO()));
            }
            key = pays.get(key);
            infosMailTmp.put(key, key);
            ALEcheanceUtils.createMap(map, droit, key);
        }
        
        // Sort data
        SortedSet<String> keys = new TreeSet<>(map.keySet());
        
        for (String keyByFile : keys) {
            DocumentData data = ALServiceLocator.getProtocoleDroitEcheancesService().loadData(map.get(keyByFile),
                    ALConstEcheances.LISTE_AVIS_ECHEANCES, getDateEcheance());
            
            JadePrintDocumentContainer container = ALServiceLocator.getImpressionEcheancesServices().loadDocuments(listeDroits,
                    getDateEcheance(), copieAllocPourDossierBeneficiaire, logger) ; 
            
            JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
            pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
            pubInfo.setOwnerId(JadeThread.currentUserId());
            pubInfo.setDocumentTitle(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"));
            pubInfo.setDocumentSubject(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance") + " - "
                    + infosMailTmp.get(keyByFile));
            pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            pubInfo.setPublishDocument(true);
            container.addDocument(data, pubInfo);
            
            //container.setMergedDocDestination(pubInfo);
            createDocuments(container);
        }
        
    }

    public void setAdiExclu(Boolean adi) {
        adiExclu = adi;
    }

    public void setCopieAllocPourDossierBeneficiaire(Boolean copieAllocPourDossierBeneficiaire) {
        this.copieAllocPourDossierBeneficiaire = copieAllocPourDossierBeneficiaire;
    }

    /**
     * @param dateEcheance
     *            the dateEcheance to set
     */
    public void setDateEcheance(String dateEcheance) {
        // vérif param
        this.dateEcheance = dateEcheance;
    }

    public Boolean getGroupParPays() {
        return groupParPays;
    }

    public void setGroupParPays(Boolean groupParPays) {
        this.groupParPays = groupParPays;
    }
    
}
