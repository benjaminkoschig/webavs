package globaz.al.process.echeances;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.constantes.enumerations.echeances.ALEnumDocumentGroup;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DroitEcheanceService;
import ch.globaz.al.liste.ALEcheancesExcelResultList;
import ch.globaz.al.utils.ALEcheanceUtils;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;

/**
 * Process for the deadline notifs, get the result SQL request and put it in an excel file
 * 
 * @author Mourad Bengmah
 */
public class ALEcheancesExcelProcess extends ALAbsrtactProcess {

    private static final long serialVersionUID = 1L;
    
    private Boolean adiExclu = true;

    private String dateEcheance = null;

    private ALEnumDocumentGroup groupPar = ALEnumDocumentGroup.AUCUN;
    
    private Boolean mailSepare;
    
    private Map<String, String> infosMail;

    public String getDateEcheance() {
        return dateEcheance;
    }

    public ALEnumDocumentGroup getGroupPar() {
        return groupPar;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setGroupPar(ALEnumDocumentGroup groupPar) {
        this.groupPar = groupPar;
    }
    
    /**
     * Get the description process
     */
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

    /**
     * Execute the SQL request and put it in an excel file
     */
    @Override
    public void process() {
        DroitEcheanceService echeanceExcelService;
        ArrayList<DroitEcheanceComplexModel> listeDroitUniqueEcheanceFin;

        try {
            echeanceExcelService = ALServiceLocator.getDroitEcheanceService();
        } catch (JadeApplicationServiceNotAvailableException e1) {
            JadeLogger.error(this, new Exception("Erreur pour atteindre le service getDroitEcheanceService", e1));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur pour atteindre le service getDroitEcheanceService");
            return;
        }

        try {
            Set<String> motifFin = echeanceExcelService.getListMotifsAvis();
            Set<String> motifEcheance = echeanceExcelService.getListMotifsAutres();
            Set<String> typeDroit = echeanceExcelService.getListTypeDroit();
            if ((motifFin == null) || (typeDroit == null) || (motifEcheance == null)) {
                JadeLogger
                        .error(this, new Exception("Erreur: motif de fin , type de droit ou motif echeance est null"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur: motif de fin , type de droit ou motif echeance est null");
                return;
            }

            // Search the deadline motif
            listeDroitUniqueEcheanceFin = echeanceExcelService.searchDroitsForEcheance(motifFin, typeDroit,
                    dateEcheance, ALConstEcheances.TYPE_ALL, ALConstEcheances.LISTE_AVIS_ECHEANCES, adiExclu);
            
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service droit/échéance", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service droit/échéance");
            return;
        }
        
        // Generate deadline motif in an excel file
        String[] filesPath = null;
        try {
            ArrayList<String> outputFiles = generationResultList(listeDroitUniqueEcheanceFin, dateEcheance);
            filesPath = new String[outputFiles.size()];
            outputFiles.toArray(filesPath);
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur lors de la construction du fichier excel"));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du process des avis d'echéances excel");
            return;
        }

        try {

            
            if(mailSepare) {
                for(String file:filesPath){
                    JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
                    pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
                    pubInfo.setOwnerId(JadeThread.currentUserId());
                    pubInfo.setDocumentTitle(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"));
                    pubInfo.setDocumentSubject(JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance") + " - "
                            + infosMail.get(file));
                    pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
                    pubInfo.setPublishDocument(true);
                    JadePublishDocument docInfo = new JadePublishDocument(file, pubInfo);
                    JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfo));
                }
            } else {
                JadeSmtpClient.getInstance().sendMail(
                        JadeThread.currentUserEmail(),
                        JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance"),
                        JadeThread.getMessage("al.echeances.titre.protocole.avisEcheance") + " : "
                                + dateEcheance.substring(3), filesPath);
            }
          
            
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur lors de l'envoi de l'email"));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du process des avis d'echéances excel");
            return;
        }

    }

    /**
     * Generate the result in excel format
     * 
     * @param listeDroits the list to process
     * @param dateEcheance the due date
     * @return an arraylist of output file
     * @throws AnnonceException 
     */
    private ArrayList<String> generationResultList(ArrayList<DroitEcheanceComplexModel> listeDroits, String dateEcheance) throws AnnonceException {
        ArrayList<String> outputFiles = new ArrayList<>();

        if (ALEnumDocumentGroup.AUCUN.equals(groupPar)) {
            ALEcheancesExcelResultList echeancesExcelResultList = new ALEcheancesExcelResultList(getSession(),
                    dateEcheance, null, groupPar);
            echeancesExcelResultList.createResultList(listeDroits);

            outputFiles.add(echeancesExcelResultList.getOutputFile());
        } else {
            Map<String, ArrayList<DroitEcheanceComplexModel>> map = new HashMap<>();
            infosMail = new HashMap<>();
            Map<String, String> infosMailTmp = new HashMap<>();
            
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
                ALEcheancesExcelResultList echeancesExcelResultList = new ALEcheancesExcelResultList(getSession(),
                        dateEcheance, keyByFile, groupPar);
                HashMap<String, ArrayList<DroitEcheanceComplexModel>> mapByFile = new HashMap<>();
                mapByFile.put(keyByFile, map.get(keyByFile));
                SortedSet<String> onlyOneKey = new TreeSet<>(mapByFile.keySet());
                echeancesExcelResultList.createResultList(map, onlyOneKey);
                String file = echeancesExcelResultList.getOutputFile();
                outputFiles.add(file);
                infosMail.put(file, infosMailTmp.get(keyByFile));
            }
        }

        return outputFiles;
    }

    public Boolean getAdiExclu() {
        return adiExclu;
    }

    public void setAdiExclu(Boolean adiExclu) {
        this.adiExclu = adiExclu;
    }

    public Boolean getMailSepare() {
        return mailSepare;
    }

    public void setMailSepare(Boolean mailSepare) {
        this.mailSepare = mailSepare;
    }
    
}
