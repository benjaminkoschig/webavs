/*
 * Créé le 03 12 08
 * 
 * Attention: la convention de nomage des fichier doit être conservée
 */
package globaz.phenix.process.communications;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.divers.CPPeriodeFiscaleManager;
import globaz.phenix.util.CPCSVFile;
import globaz.pyxis.constantes.IConstantes;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Ce process permet de mettre à jour les communications fiscales qui ont été rejeté par le FISC
 */

/*
 * Ce process peut être appélé @author jpa
 */
public class CPProcessTraitementAnomalies extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final int EN_ERREUR = 2;
    protected static final int FIN_FICHIER = 3;

    public final static String RECEPTION_TOLAL_CREER_JOURNAL = "1";
    public final static String RECEPTION_TOTAL = "2";
    public final static String RECEPTIONNER_ENCORE = "3";

    protected static final int SUCCES = 1;
    private String csCanton = null;

    private String dateReception = "";
    private String idJournalRetour = "";
    private String inputFileName = "";
    private String libelleJournal = "";
    private String messageErreur = "";

    private String nbCommunication = "";

    private String nomFichier = "";
    private String readerPath = "";
    protected int result;
    private HSSFSheet sheet1;
    private HSSFWorkbook wb;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        boolean successful = false;
        try {
            successful = lireFichierRecu();
            if (!successful) {
                return false;
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getLabel("PROCTRAITANOMALIES_ERROR_RECEPTION_IMPOSSIBLE") + e.getMessage(),
                    FWMessage.ERREUR, "CPProcessTraitementAnomalies");
            successful = false;
        }
        return successful;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    private void creationLigneExcel(String date, String periodeFiscale, String numCtb, String nomCtb, String prenomCtb,
            String numAvs, String categorieAssureAVS, String numAffilie, String motifMiseEnAnomalie) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
        int colNum = 0;
        // numAffilié
        HSSFCell cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(numAffilie));
        cell.setCellStyle(style);
        // numAvs
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(NSUtil.formatAVSUnknown(numAvs)));
        ;
        cell.setCellStyle(style);
        // numContribuable
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(numCtb));
        cell.setCellStyle(style);
        // nom
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(nomCtb));
        cell.setCellStyle(style);
        // prenom
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(prenomCtb));
        cell.setCellStyle(style);
        // categorieAssurecategorieAssure
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(categorieAssureAVS));
        cell.setCellStyle(style);
        // periodeFiscale
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(periodeFiscale));
        cell.setCellStyle(style);
        // date
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(date));
        cell.setCellStyle(style);
        // refusFisc
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(motifMiseEnAnomalie));
        cell.setCellStyle(style);
        // erreur
        cell = row.createCell((short) colNum++);
        cell.setCellValue(new HSSFRichTextString(messageErreur));
        cell.setCellStyle(style);
    }

    public CPCommunicationFiscale getCommunicationFiscale(String numAffilie, String numAvs, String annee)
            throws Exception {
        CPCommunicationFiscale communicationFiscale = null;
        // boolean trouve = false;
        // AFAffiliation affiliation = null;
        // TITiersViewBean tiers = new TITiersViewBean();
        // tiers.setSession(getSession());
        if (!JadeStringUtil.isEmpty(annee)) {
            // On recherche en premier par le numéro d'affilié
            // if (!JadeStringUtil.isEmpty(numAffilie)) {
            // AFAffiliationManager histo = new AFAffiliationManager();
            // histo.setSession(getSession());
            // histo.setForAffilieNumero(numAffilie);
            // histo.find();
            // if (histo.size() > 0) {
            // tiers.setIdTiers(((AFAffiliation)
            // histo.getFirstEntity()).getIdTiers());
            // affiliation = CPToolBox.returnAffiliation(getSession(),
            // (BTransaction) getTransaction(), numAffilie, annee, "", 2);
            // trouve = true;
            // }
            // }
            /**
             * Problème de recherche par numAvs, impossible car par exemple pour le numéro d'affilié 1606620-20 il nous
             * retourne une affiliation 1606620-70
             */
            // Si on a pas trouvé on recherche par rapport au nnuméro AVS
            // if (!trouve) {
            // if (!JadeStringUtil.isEmpty(numAvs)) {
            // TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
            // histo.setSession(getSession());
            // histo.setForNumAvs(NSUtil.formatAVSUnknown(numAvs));
            // histo.find();
            // if (histo.size() > 0) {
            // tiers.setIdTiers(((TIHistoriqueAvs)
            // histo.getFirstEntity()).getIdTiers());
            // affiliation = CPToolBox.returnAffiliation(getSession(),
            // (BTransaction) getTransaction(), tiers.getIdTiers(), annee, "",
            // 1);
            // trouve = true;
            // }
            // }
            // }
            // if (!trouve /*|| affiliation == null*/) {
            // messageErreur +=
            // getSession().getLabel("PROCTRAITANOMALIES_ERROR_AFF_NON_TROUVEE");
            // } else {
            String idIfd = getNumIfd(annee);
            if (!JadeStringUtil.isEmpty(idIfd)) {
                // tiers.retrieve();
                // On recherche la communication fiscale
                CPCommunicationFiscale communication = new CPCommunicationFiscale();
                communication.setSession(getSession());
                // communication.setIdTiers(tiers.getIdTiers());
                communication.setIdIfd(idIfd);
                communication.setNumAffilie(numAffilie);
                communicationFiscale = communication.getCommunicationFiscaleEnvoyee(0);
                if ((communicationFiscale == null) || communicationFiscale.isNew()) {
                    messageErreur += getSession().getLabel("CP_ERROR_COM_NON_TROUVEE");
                }
            } else {
                messageErreur += getSession().getLabel("CP_ERROR_COM_NON_TROUVEE");
            }
            // }
        } else {
            messageErreur += getSession().getLabel("CP_ERROR_COM_NON_TROUVEE");
        }
        return communicationFiscale;

    }

    /**
     * @return
     */
    public String getCsCanton() {
        return csCanton;
    }

    /**
     * @return
     */
    public String getDateReception() {
        return dateReception;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        final String titre = getSession().getLabel("TRAIT_ANOMALIES_ACI_TITRE");
        return titre;
    }

    /**
     * @return
     */
    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    /**
     * @return
     */
    public String getInputFileName() {
        return inputFileName;
    }

    /**
     * @return
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * @return
     */
    public String getNbCommunication() {
        return nbCommunication;
    }

    /**
     * @return
     */
    public String getNomFichier() {
        return nomFichier;
    }

    public String getNumIfd(String annee) {
        String idIfd = "";
        try {
            CPPeriodeFiscaleManager periode = new CPPeriodeFiscaleManager();
            periode.setSession(getSession());
            periode.setForAnneeDecisionDebut(annee);
            periode.find();
            if (periode.size() > 0) {
                idIfd = ((CPPeriodeFiscale) periode.getFirstEntity()).getIdIfd();
            }
        } catch (Exception e) {
            return "";
        }
        return idIfd;
    }

    /**
     * @return
     */
    public String getReaderPath() {
        return readerPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void lectureLigneVD(int row, CPCSVFile fichierCSV) {
        // String identifiant = fichierCSV.getData(row, 0);
        // String numero = fichierCSV.getData(row, 1);
        String date = fichierCSV.getData(row, 2);
        String periodeFiscale = fichierCSV.getData(row, 3);
        // String flagDemandeUrgente = fichierCSV.getData(row, 4);
        String numCtb = fichierCSV.getData(row, 5);
        String nomCtb = fichierCSV.getData(row, 6);
        String prenomCtb = fichierCSV.getData(row, 7);
        String numAvs = fichierCSV.getData(row, 8);
        String categorieAssureAVS = fichierCSV.getData(row, 9);
        String numAffilie = fichierCSV.getData(row, 10);
        // String dateDebPeriode = fichierCSV.getData(row, 11);
        // String dateFinPeriode = fichierCSV.getData(row, 12);
        // String IdtPeriodeImposition = fichierCSV.getData(row, 13);
        // String IdtTaxation = fichierCSV.getData(row, 14);
        // String flagAssureMonsieur = fichierCSV.getData(row, 15);
        // String etatDemande = fichierCSV.getData(row, 16);
        // String motifMiseEnAttente = fichierCSV.getData(row, 17);
        String motifMiseEnAnomalie = fichierCSV.getData(row, 18);
        // String motifSuppression = fichierCSV.getData(row, 19);
        // String motifRefus = fichierCSV.getData(row, 20);
        // String motifAnnulation = fichierCSV.getData(row, 21);
        // String flagDemandeManuelle = fichierCSV.getData(row, 22);
        // String flagCommunicationAutomatique = fichierCSV.getData(row, 23);
        // String dateCreation = fichierCSV.getData(row, 24);
        // String auteurCreation = fichierCSV.getData(row, 25);
        // String dateDerniereModif = fichierCSV.getData(row, 26);
        // String auteurDerniereModif = fichierCSV.getData(row, 27);
        // String sigleTiersDemandeur = fichierCSV.getData(row, 28);
        // String nomContact = fichierCSV.getData(row, 29);
        // String emailContact = fichierCSV.getData(row, 30);
        // String telContact = fichierCSV.getData(row, 31);
        // String nomOID = fichierCSV.getData(row, 32);
        // String nomForDeGestion = fichierCSV.getData(row, 33);
        // String sigleTaxateur = fichierCSV.getData(row, 34);

        // On recherche la communication fiscale
        try {
            CPCommunicationFiscale communication = getCommunicationFiscale(numAffilie, numAvs, periodeFiscale);
            if (JadeStringUtil.isEmpty(messageErreur)) {
                // On met la date d'envoi à 0
                miseAJourDateEnvoi(communication);
            }
        } catch (Exception e) {
            messageErreur += getSession().getLabel("PROCTRAITANOMALIES_ERROR_COM_NON_TROUVEE");
        }
        // On commit l'update sur la date d'envoi, sinon on log l'erreur et on
        // affiche une erreur dans la liste excel
        if (!getTransaction().hasErrors() && !isAborted()) {
            try {
                getTransaction().commit();
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                e.printStackTrace();
            }
        } else {
            getMemoryLog().logMessage(getTransaction().getErrors() + " : " + numAffilie + " " + periodeFiscale,
                    FWMessage.FATAL, this.getClass().getName());
            messageErreur += getSession().getLabel("PROCTRAITANOMALIES_ERROR_IMPOSSIBLE_UPDATE");
            getTransaction().clearErrorBuffer();
            System.out.println(getTransaction().getErrors() + " : " + numAffilie + " " + periodeFiscale);
        }
        creationLigneExcel(date, periodeFiscale, numCtb, nomCtb, prenomCtb, numAvs, categorieAssureAVS, numAffilie,
                motifMiseEnAnomalie);
        messageErreur = "";
    }

    /*
     * Lecture du fichier de reception et transformation en xml les fichiers xml générés se trouve dans le tableau
     * xmlFileProduced
     */
    private boolean lireFichierRecu() throws Exception {
        final String titre = getSession().getLabel("TRAIT_ANOMALIES_ACI_TITRE");
        // Pour le canton de Vaud
        if (getCsCanton().equals(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
            try {
                // Initialisation du document excel
                wb = new HSSFWorkbook();
                sheet1 = wb.createSheet(titre);
                HSSFCellStyle style = wb.createCellStyle();
                style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                // Création des colonnes
                sheet1 = setTitleRowMiseCompte(wb, sheet1);
                // Lecture du fichier csv
                CPCSVFile fichierCSV = new CPCSVFile(Jade.getInstance().getHomeDir() + "work/" + getInputFileName());
                // On set la progress bar
                setProgressScaleValue(fichierCSV.getRowsCount() - 1);
                // On parcourt les lignes
                // On ne prend pas la 1ère, car entête
                for (int row = 1; row < fichierCSV.getRowsCount(); row++) {
                    // Lecture de la ligne + traitement
                    lectureLigneVD(row, fichierCSV);
                    incProgressCounter();
                }
                fichierCSV = null;
                // Création du fichier excel
                File f = File.createTempFile(titre + " - " + JACalendar.todayJJsMMsAAAA() + "  ", ".xls");
                f.deleteOnExit();
                FileOutputStream out = new FileOutputStream(f);
                wb.write(out);
                out.close();
                // Ajout du document
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setDocumentType("0268CCP");
                docInfo.setDocumentTypeNumber("");
                this.registerAttachedDocument(docInfo, f.getAbsolutePath());
            } catch (Exception e1) {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCTRAITANOMALIES_ERROR_RECEPTION_IMPOSSIBLE") + e1.getMessage(),
                        FWMessage.ERREUR, "CPProcessTraitementAnomalies");
                return false;
            }
        } else {
            getMemoryLog().logMessage(getSession().getLabel("TRAIT_ANOMALIES_ACI_CANTON_NON_IMPLEMENTE"),
                    FWMessage.ERREUR, "CPProcessTraitementAnomalies");
        }

        return true;
    }

    private void miseAJourDateEnvoi(CPCommunicationFiscale communication) {
        boolean dateExistante = false;
        if (!JadeStringUtil.isEmpty(communication.getDateRetour())) {
            messageErreur += getSession().getLabel("PROCTRAITANOMALIES_ERROR_DATE_RETOUR_EXISTANTE");
            dateExistante = true;
        }
        if (!JadeStringUtil.isEmpty(communication.getDateComptabilisation())) {
            messageErreur += getSession().getLabel("PROCTRAITANOMALIES_ERROR_DATE_COMPTA_EXISTANTE");
            dateExistante = true;
        }
        if (!dateExistante) {
            // On remet la date d'envoi à 0
            communication.setDateEnvoi("");
            try {
                communication.update();
            } catch (Exception e) {
                messageErreur += getSession().getLabel("PROCTRAITANOMALIES_ERROR_MAJ_DATE_ENVOI");
            }
        }
    }

    /**
     * @param string
     */
    public void setCsCanton(String string) {
        csCanton = string;
    }

    /**
     * @param string
     */
    public void setDateReception(String string) {
        dateReception = string;
    }

    /**
     * @param string
     */
    public void setIdJournalRetour(String string) {
        idJournalRetour = string;
    }

    /**
     * @param string
     */
    public void setInputFileName(String string) {
        inputFileName = string;
    }

    /**
     * @param string
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    /**
     * @param string
     */
    public void setNbCommunication(String string) {
        nbCommunication = string;
    }

    /**
     * @param string
     */
    public void setNomFichier(String string) {
        nomFichier = string;
    }

    /**
     * @param string
     */
    public void setReaderPath(String string) {
        readerPath = string;
    }

    private HSSFSheet setTitleRowMiseCompte(HSSFWorkbook wb, HSSFSheet sheet) {
        // dfinition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 3000);
        sheet1.setColumnWidth((short) 1, (short) 3500);
        sheet1.setColumnWidth((short) 2, (short) 3000);
        sheet1.setColumnWidth((short) 3, (short) 5000);
        sheet1.setColumnWidth((short) 4, (short) 5000);
        sheet1.setColumnWidth((short) 5, (short) 3000);
        sheet1.setColumnWidth((short) 6, (short) 3000);
        sheet1.setColumnWidth((short) 7, (short) 4000);
        sheet1.setColumnWidth((short) 8, (short) 10000);
        sheet1.setColumnWidth((short) 9, (short) 10000);
        // nom des cell
        final String numAffilie = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_NUMAFFILIE");
        final String numAvs = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_NUMAVS");
        final String numContribuable = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_NUMCTB");
        final String nom = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_NOM");
        final String prenom = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_PRENOM");
        final String categorieAssure = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_CATASS");
        final String periodeFiscale = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_PERFISC");
        final String date = getSession().getLabel("TRAIT_ANOMALIES_ACI_DOC_DATE");
        final String refusFisc = getSession().getLabel("TRAIT_ANOMALIES_ACI_REFUSFISC_DATE");
        final String erreur = getSession().getLabel("TRAIT_ANOMALIES_ACI_REFUSFISC_ERREUR");

        final String titre = getSession().getLabel("TRAIT_ANOMALIES_ACI_TITRE");

        final String[] COL_TITLES = { numAffilie, numAvs, numContribuable, nom, prenom, categorieAssure,
                periodeFiscale, date, refusFisc, erreur };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font.setFontHeight((short) 500);
        font.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style = wb.createCellStyle();

        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        style2.setFont(font2);
        style.setFont(font);

        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 1);
        c.setCellValue(new HSSFRichTextString(titre));
        c.setCellStyle(style2);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // let's use a nifty font for the title
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }
}
