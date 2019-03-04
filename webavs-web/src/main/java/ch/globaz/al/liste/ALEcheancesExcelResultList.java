package ch.globaz.al.liste;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import ch.globaz.al.business.constantes.enumerations.echeances.ALEnumDocumentGroup;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import globaz.aquila.print.list.COAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;

/**
 * Manage the excel output of the deadline notif list
 * 
 * @author Mourad Bengmah
 * 
 */
public class ALEcheancesExcelResultList extends COAbstractListExcel {

    /**
     * The due date
     */
    private String dateEcheance;

    private BSession session;
    
    private ALEnumDocumentGroup groupPar;

    /**
     * The constructor
     * 
     * @param session
     * @param dateEcheance
     */
    public ALEcheancesExcelResultList(BSession session, String dateEcheance, String affilie, ALEnumDocumentGroup groupPar) {
        super(session, session.getLabel("ECHEANCES_EXCEL_PROCESS_FILENAME") + "_", session
                .getLabel("ECHEANCES_EXCEL_PROCESS_FILENAME"));
        this.session = session;
        this.dateEcheance = dateEcheance;
        this.groupPar = groupPar;
        setFilenameRoot("echeance");
//        if (affilie != null) {
//            setFilenameRoot(labels.get("ECHEANCES_EXCEL_PROCESS_FILENAME") + "_" + affilie + "_");
//        } else {
//            setFilenameRoot(labels.get("ECHEANCES_EXCEL_PROCESS_FILENAME") + "_");
//        }
    }

    /**
     * Create the output excel of the list
     */
    public void createResultList(Map<String, ArrayList<DroitEcheanceComplexModel>> map, SortedSet<String> keys) {
        String sheetName;

        for (String key : keys) {
            sheetName = getGroupName(key) + " " + key;
            createSheet(sheetName, key, map.get(key));
        }

//        if (map.isEmpty()) {
//            createSheet((String) labels.get("ECHEANCES_EXCEL_PROCESS_NO_DATA_SHEET"));
//            createRow();
//            createCell((String) labels.get("ECHEANCES_EXCEL_PROCESS_NO_DATA"));
//        }
    }
    
    public void createResultList(List<DroitEcheanceComplexModel> listeDroits) {
        String sheetName;

        sheetName = JadeThread.getMessage("al.protocoles.echeances.info.traitement.val");
        createSheet(sheetName, null, listeDroits);
    }


    /**
     * Create an excel sheet
     * 
     * @param sheetName the sheetname
     * @param list the list to process
     */
    private void createSheet(String sheetName, String group, List<DroitEcheanceComplexModel> list) {

        createSheet(sheetName);
        initPage(true);
        createHeader();
        //createFooter(JadeThread.getMessage("al.protocoles.echeances.liste.entete.affilie"));

        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol, (short) 5000);

        createRow();
        createCell(JadeThread.getMessage("al.protocoles.echeances.liste.entete.dateEcheance"), getStyleLeftWihtoutBorder());
        createCell(dateEcheance, getStyleLeftWihtoutBorder());
        createRow();
        if(group != null) {
            setFilenameRoot("echeance_"+group+"_");
            createCell(getGroupName(group), getStyleLeftWihtoutBorder());
            createCell(group, getStyleLeftWihtoutBorder());
        }

        ArrayList<String> colTitles = new ArrayList<>();
        
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.affilie"));
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.nss"));
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.dossier"));
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.allocataire"));
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.enfant"));
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.dateNaissance"));
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.dateEcheance"));
        colTitles.add(JadeThread.getMessage("al.protocoles.echeances.liste.entete.motif"));

        initTitleRow(colTitles);

        for(DroitEcheanceComplexModel model:list) {
            createRow(model);
        }

    }

    private String getGroupName(String group) {
        if(ALEnumDocumentGroup.AFFILLIE.equals(groupPar)) {
            return JadeThread.getMessage("al.protocoles.echeances.liste.entete.affilie");
        } else {
            return JadeThread.getMessage("al.protocoles.echeances.liste.entete.pays");
        }
    }

    /**
     * Create an excel row
     * 
     * @param complexModel the right model
     */
    private void createRow(DroitEcheanceComplexModel complexModel) {
        createRow();
        createCell(complexModel.getNumAffilie());
        createCell(complexModel.getNumNss());
        createCell(complexModel.getDroitModel().getIdDossier());
        createCell(complexModel.getNomAllocataire() + " " + complexModel.getPrenomAllocataire());
        createCell(complexModel.getNomEnfant() + " " + complexModel.getPrenomEnfant());
        createCell(complexModel.getDateNaissanceEnfant());
        createCell(complexModel.getDroitModel().getFinDroitForcee());
        try {
            createCell(ALServiceLocator.getDroitEcheanceService().getLibelleMotif(complexModel,
                    JadeThread.currentLanguage()));
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Error : impossible to get the libelleMotif of the droit"));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Error : impossible to get the libelleMotif of the droit");
        }
    }

    /**
     * The inforom of the document, none for this class
     */
    @Override
    public String getNumeroInforom() {
        return null;
    }
}
