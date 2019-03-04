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
import ch.globaz.al.business.constantes.enumerations.echeances.ALEnumDocumentGroup;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DroitEcheanceService;
import ch.globaz.al.utils.ALEcheanceUtils;
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
    
    private ALEnumDocumentGroup groupPar = ALEnumDocumentGroup.AUCUN;
    
    private Boolean mailSepare;
    
    private Map<DocumentData, String> infosMail;

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
            Set<String> motifFin = echeanceService.getListMotifsAvis();
            Set<String> motifEcheance = echeanceService.getListMotifsAutres();
            Set<String> typeDroit = echeanceService.getListTypeDroit();
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
            
            try {
                publishDocument(container, listeDroitUniqueEcheanceFin);

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
    
    private void publishDocument(JadePrintDocumentContainer container, ArrayList<DroitEcheanceComplexModel> listeDroits)
            throws JadePersistenceException, JadeApplicationException, JadeApplicationServiceNotAvailableException,
            JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {
        
        if (ALEnumDocumentGroup.AUCUN.equals(groupPar)) {
            
            JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();

            pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
            pubInfo.setOwnerId(JadeThread.currentUserId());
            pubInfo.setDocumentTitle(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"));
            pubInfo.setDocumentSubject(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"));
            pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            pubInfo.setPublishDocument(true);
            
            container.addDocument(ALServiceLocator.getProtocoleDroitEcheancesService().loadData(listeDroits,
                    ALConstEcheances.LISTE_AVIS_ECHEANCES, getDateEcheance()), pubInfo);

            
        } else {
            Map<String, ArrayList<DroitEcheanceComplexModel>> map = new HashMap<>();
            infosMail = new HashMap<>();
            Map<String, String> infosMailTmp = new HashMap<>();
            List<DocumentData> listData = new ArrayList<>();
            
            if (ALEnumDocumentGroup.AFFILLIE.equals(groupPar)){
                for (DroitEcheanceComplexModel droit : listeDroits) {
                    String key = droit.getNumAffilie();
                    String infoMailSup = droit.getNumAffilie() + " - " + droit.getTiersLiaisonComplexModel().getTiersReference().getDesignation1();
                    infosMailTmp.put(key, infoMailSup);
                    ALEcheanceUtils.createMap(map, droit, key);
                }
    
            } else if (ALEnumDocumentGroup.PAYS.equals(groupPar)) {
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
            }
            
            // Sort data
            SortedSet<String> keys = new TreeSet<>(map.keySet());
            
            for (String keyByFile : keys) {
                DocumentData data = ALServiceLocator.getProtocoleDroitEcheancesService().loadData(map.get(keyByFile),
                        ALConstEcheances.LISTE_AVIS_ECHEANCES, getDateEcheance());
                listData.add(data);
                infosMail.put(data, infosMailTmp.get(keyByFile));
            }
            
            if(mailSepare) {
                for(DocumentData data:listData){
                    JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
                    pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
                    pubInfo.setOwnerId(JadeThread.currentUserId());
                    pubInfo.setDocumentTitle(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"));
                    pubInfo.setDocumentSubject(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance") + " - "
                            + infosMail.get(data));
                    pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
                    pubInfo.setPublishDocument(true);
                    container.addDocument(data, pubInfo);
                }
            } else {
//                JadeSmtpClient.getInstance().sendMail(
//                        JadeThread.currentUserEmail(),
//                        JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"),
//                        JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance") + " : "
//                                + dateEcheance.substring(3), filesPath);
            }
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

    public ALEnumDocumentGroup getGroupPar() {
        return groupPar;
    }

    public void setGroupPar(ALEnumDocumentGroup groupPar) {
        this.groupPar = groupPar;
    }

    public Boolean getMailSepare() {
        return mailSepare;
    }

    public void setMailSepare(Boolean mailSepare) {
        this.mailSepare = mailSepare;
    }
    

}
