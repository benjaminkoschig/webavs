/**
 * class CPImpressionCotisationDifferente écrit le 19/01/05 par JPA
 * 
 * class créant un document .xls des décisions avec mise en compte
 * 
 * @author JPA
 */
package globaz.phenix.listes.excel;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.formater.TILocaliteLongFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CPListeCommunicationEnvoi {
    public static final int CS_ALLEMAND = 503002;

    public static final int CS_FRANCAIS = 503001;

    public static final int CS_ITALIEN = 503004;

    private String anneeDecision;

    private String dateEnvoi = "";

    private String dateEnvoiVide;

    private String fileName = "";

    private String forCanton;

    private String forGenreAffilie;

    private BProcess processAppelant = null;

    private BSession session;

    private HSSFSheet sheet1;

    private HSSFWorkbook wb;

    public CPListeCommunicationEnvoi() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");
        // Paramètres d'impression
        HSSFPrintSetup psa = sheet1.getPrintSetup();
        psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        psa.setLandscape(true);
        sheet1.setSelected(true);
    }

    // créé la feuille xls
    public CPListeCommunicationEnvoi(BSession session, String annee, String canton, String genre, String dateEdition) {
        this.session = session;
        wb = new HSSFWorkbook();
        // HSSFPrintSetup psa = sheet1.getPrintSetup();
        // psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // psa.setLandscape(true);
        // sheet1.setSelected(true);
        setDateEnvoi(dateEdition);
        setForCanton(canton);
        setForGenreAffilie(genre);
        setAnneeDecision(annee);
        try {
            fileName = getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES", getLangue()) + "-"
                    + CodeSystem.getCode(getSession(), canton) + "-" + annee + "-";
        } catch (Exception e) {
            try {
                fileName = getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES", getLangue()) + "-"
                        + annee + "-";
            } catch (Exception e1) {
                fileName = "ListeCommunicationsFiscales-" + annee + "-";
            }
        }
    }

    public String getAnneeDecision() {
        return anneeDecision;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateEnvoiVide() {
        return dateEnvoiVide;
    }

    public String getForCanton() {
        return forCanton;
    }

    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * Réalise le mapping des langue du canton en CODE SYSTEM
     * 
     * @author:BTC
     * @return int CS_FRANCAIS = 503001; CS_ALLEMAND = 503002; CS_ITALIEN = 503004;
     */
    private int getIdLangue() throws Exception {
        int idLangue = CPListeCommunicationEnvoi.CS_ALLEMAND; // defaut
        idLangue = Integer.parseInt(((CPApplication) getSession().getApplication())
                .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(getSession(), getForCanton())));
        return idLangue;
    }

    protected String getLangue() throws Exception {
        switch (getIdLangue()) {
            case CS_FRANCAIS:
                return "FR";
            case CS_ITALIEN:
                return "IT";
            default:
                return "DE";
        }
    }

    /*
     * méthode pour gérer le fichier en sortie
     */
    public String getOutputFile() {
        try {
            // récup du viewBean pour avoir accès au idpassage et libellepassage
            // pour le sujet de l'e-mail
            // viewBean.execute();
            File f = File.createTempFile(fileName, ".xls");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    public HSSFSheet populateSheet(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        HSSFSheet sheet = null;
        // On retourne la liste spécifique au canton de Zurich
        if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_ZURICH)) {
            sheet = populateSheetZurich(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_NIDWALD)) {
            sheet = populateSheetNidwald(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_GLARIS)) {
            sheet = populateSheetGlaris(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_SCHAFFOUSE)) {
            sheet = populateSheetSchaffouse(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_APPENZELL_AR)) {
            sheet = populateSheetAppenzelR(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_SAINT_GALL)) {
            sheet = populateSheetSaintGall(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_GRISONS)) {
            sheet = populateSheetGrisonThurgovie(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_THURGOVIE)) {
            sheet = populateSheetGrisonThurgovie(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
            sheet = populateSheetVaud(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_VALAIS)) {
            sheet = populateSheetValais(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL)) {
            sheet = populateSheetNeuchatel(manager, transaction);
        } else if (getForCanton().equals(IConstantes.CS_LOCALITE_CANTON_JURA)) {
            sheet = populateSheetJura(manager, transaction);
        }
        // On retourne la liste standard
        else {
            sheet = populateSheetStandard(manager, transaction);
        }
        return sheet;
    }

    /*
     * initialisation de la feuille xls Standard
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetAppenzelR(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowAppenzelR(wb, sheet1);

        // Définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 2000);
        sheet1.setColumnWidth((short) 1, (short) 5000);
        sheet1.setColumnWidth((short) 2, (short) 10000);
        sheet1.setColumnWidth((short) 3, (short) 10000);
        sheet1.setColumnWidth((short) 4, (short) 1500);
        sheet1.setColumnWidth((short) 5, (short) 5000);
        sheet1.setColumnWidth((short) 6, (short) 5000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getPrenom() + " " + communication.getNom()));
            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }

            // adresse
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));
            // n° avs epoux
            // On test si c'est une femme sinon, pas afficher numéro du
            // conjoint
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean tiersAVS = new TITiersViewBean();
            tiersAVS.setSession(getSession());
            tiersAVS.setIdTiers(communication.getIdTiers());
            tiersAVS.retrieve();
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNumAvsActuel()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }
            // date de naissance epoux
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getDateNaissance()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }
            // n° de contribuable
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getTiers().getNumContribuableActuel()));
            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    public HSSFSheet populateSheetGlaris(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);

        sheet1 = setTitleRowGlaris(wb, sheet1);
        // Définition de la taille des cellules
        sheet1.setColumnWidth((short) 0, (short) 3000);
        sheet1.setColumnWidth((short) 1, (short) 8000);
        sheet1.setColumnWidth((short) 2, (short) 6000);
        sheet1.setColumnWidth((short) 3, (short) 3000);
        sheet1.setColumnWidth((short) 4, (short) 3000);
        sheet1.setColumnWidth((short) 5, (short) 4000);
        sheet1.setColumnWidth((short) 6, (short) 3000);
        sheet1.setColumnWidth((short) 7, (short) 4000);
        sheet1.setColumnWidth((short) 8, (short) 10000);
        sheet1.setColumnWidth((short) 9, (short) 3500);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // 0 numéro de contribuable
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumContri()));
            // 1 nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));
            // 2 Localité
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(communication.getIdTiers());
            persAvs.retrieve();
            cell.setCellValue(new HSSFRichTextString(persAvs.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    "519005", JACalendar.todayJJsMMsAAAA(), new TILocaliteLongFormater())));
            // 3 n° AVS
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // 4 n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // 5 Catégorie
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                    communication.getGenreAffilie())));
            // 6 n° de caisse
            // Recherche de la description de la caisse
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            TIAdministrationViewBean admin = new TIAdministrationViewBean();
            admin.setSession(transaction.getSession());
            admin.setIdTiersAdministration(communication.getIdCaisse());
            try {
                admin.retrieve();
                cell.setCellValue(new HSSFRichTextString(admin.getCodeAdministration()));
            } catch (Exception e) {
                cell.setCellValue(new HSSFRichTextString(" "));
            }
            cell.setCellStyle(style);
            // n° avs epoux
            // On test si c'est une femme sinon, pas afficher numéro du
            // conjoint
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean tiersAVS = new TITiersViewBean();
            tiersAVS.setSession(getSession());
            tiersAVS.setIdTiers(communication.getIdTiers());
            tiersAVS.retrieve();
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNumAvsActuel()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // 7b Nom prénom epoux
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNomPrenom() + " "));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // 8 Date d'affiliation
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            AFAffiliation af = new AFAffiliation();
            af.setSession(getSession());
            af.setId(communication.getIdAffiliation());
            af.retrieve();
            cell.setCellValue(new HSSFRichTextString(af.getDateDebut()));

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    /*
     * initialisation de la feuille xls Standard
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetGrisonThurgovie(CPCommunicationFiscaleAffichageManager manager,
            BTransaction transaction) throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowGrisonThurgovie(wb, sheet1);

        // Définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 2000);
        sheet1.setColumnWidth((short) 1, (short) 5000);
        sheet1.setColumnWidth((short) 2, (short) 10000);
        sheet1.setColumnWidth((short) 3, (short) 10000);
        sheet1.setColumnWidth((short) 4, (short) 1500);

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getPrenom() + " " + communication.getNom()));
            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }

            // adresse
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    /*
     * initialisation de la feuille xls spécifique au canton du Jura
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetJura(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowJura(wb, sheet1);

        // Définition de la taille des cell
        // nom prenom
        sheet1.setColumnWidth((short) 0, (short) 7000);
        // navs
        sheet1.setColumnWidth((short) 1, (short) 4000);
        // naff
        sheet1.setColumnWidth((short) 2, (short) 3000);
        // adresse
        sheet1.setColumnWidth((short) 3, (short) 6000);
        // npa
        sheet1.setColumnWidth((short) 4, (short) 1500);
        // localité
        sheet1.setColumnWidth((short) 5, (short) 4500);
        // adresse prof.
        sheet1.setColumnWidth((short) 6, (short) 9000);
        // nom prénom époux
        sheet1.setColumnWidth((short) 7, (short) 5000);
        // nss époux
        sheet1.setColumnWidth((short) 8, (short) 4000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));
            // n° AVS
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);

            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }

            // adresse
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));

            // Adresse Proffessionnelle
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            String adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "31.12."
                    + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "01.01."
                        + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            }
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005",
                        JACalendar.todayJJsMMsAAAA(), Boolean.TRUE)).replace('\n', ' ');
            }
            cell.setCellValue(new HSSFRichTextString(adresseProf));
            // n° avs epoux
            // On test si c'est une femme sinon, pas afficher numéro du
            // conjoint
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean tiersAVS = new TITiersViewBean();
            tiersAVS.setSession(getSession());
            tiersAVS.setIdTiers(communication.getIdTiers());
            tiersAVS.retrieve();
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNomPrenom()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // n° avs epoux
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNumAvsActuel()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    public HSSFSheet populateSheetNeuchatel(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowNeuchatel(wb, sheet1);
        // Définition de la taille des cellules
        // num caisse
        sheet1.setColumnWidth((short) 0, (short) 3000);
        // nom caisse
        sheet1.setColumnWidth((short) 1, (short) 4500);
        // cat
        sheet1.setColumnWidth((short) 2, (short) 4000);
        // naff
        sheet1.setColumnWidth((short) 3, (short) 3000);
        // navs
        sheet1.setColumnWidth((short) 4, (short) 3000);
        // contribuable
        sheet1.setColumnWidth((short) 5, (short) 3200);
        // nom prenom
        sheet1.setColumnWidth((short) 6, (short) 8000);
        // date naissance
        sheet1.setColumnWidth((short) 7, (short) 4000);

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;

            // 0 n° de caisse
            // Recherche de la description de la caisse
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            TIAdministrationViewBean admin = new TIAdministrationViewBean();
            admin.setSession(transaction.getSession());
            admin.setIdTiersAdministration(communication.getIdCaisse());
            try {
                admin.retrieve();
                cell.setCellValue(new HSSFRichTextString(admin.getCodeAdministration()));
            } catch (Exception e) {
                cell.setCellValue(new HSSFRichTextString(" "));
            }
            cell.setCellStyle(style);

            // 1 nom de caisse
            // Recherche de la description de la caisse
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            TIAdministrationViewBean admin2 = new TIAdministrationViewBean();
            admin2.setSession(transaction.getSession());
            admin2.setIdTiersAdministration(communication.getIdCaisse());
            try {
                admin2.retrieve();
                cell.setCellValue(new HSSFRichTextString(admin2.getNom()));
            } catch (Exception e) {
                cell.setCellValue(new HSSFRichTextString(" "));
            }
            cell.setCellStyle(style);

            // 2 Catégorie
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                    communication.getGenreAffilie())));

            // 3 n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);

            // 4 n° AVS
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));

            // 5 numéro de contribuable
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumContri()));

            // 6 nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));

            // 7 date de naissance
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getTiers().getDateNaissance()));

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    public HSSFSheet populateSheetNidwald(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowNidwald(wb, sheet1);
        // Définition de la taille des cellules
        sheet1.setColumnWidth((short) 0, (short) 3000);
        sheet1.setColumnWidth((short) 1, (short) 10000);
        sheet1.setColumnWidth((short) 2, (short) 5000);
        sheet1.setColumnWidth((short) 3, (short) 3000);
        sheet1.setColumnWidth((short) 4, (short) 8000);
        sheet1.setColumnWidth((short) 5, (short) 4000);
        sheet1.setColumnWidth((short) 6, (short) 3000);
        sheet1.setColumnWidth((short) 7, (short) 4000);
        sheet1.setColumnWidth((short) 8, (short) 10000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // n° de caisse
            // Recherche de la description de la caisse
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            TIAdministrationViewBean admin = new TIAdministrationViewBean();
            admin.setSession(transaction.getSession());
            admin.setIdTiersAdministration(communication.getIdCaisse());
            try {
                admin.retrieve();
                cell.setCellValue(new HSSFRichTextString(admin.getCodeAdministration()));
            } catch (Exception e) {
                cell.setCellValue(new HSSFRichTextString(" "));
            }
            cell.setCellStyle(style);
            // nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));
            // n° AVS
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // Localité
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(communication.getIdTiers());
            persAvs.retrieve();
            cell.setCellValue(new HSSFRichTextString(persAvs.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    "519005", JACalendar.todayJJsMMsAAAA(), new TILocaliteLongFormater())));
            // Catégorie
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                    communication.getGenreAffilie())));
            // Date d'affiliation
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            AFAffiliation af = new AFAffiliation();
            af.setSession(getSession());
            af.setId(communication.getIdAffiliation());
            af.retrieve();
            cell.setCellValue(new HSSFRichTextString(af.getDateDebut()));
            // n° avs epoux
            // On test si c'est une femme sinon, pas afficher numéro du
            // conjoint
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean tiersAVS = new TITiersViewBean();
            tiersAVS.setSession(getSession());
            tiersAVS.setIdTiers(communication.getIdTiers());
            tiersAVS.retrieve();
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNumAvsActuel()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // Nom prénom epoux
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNomPrenom() + " "));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    /*
     * initialisation de la feuille xls spécifique au canton de Zurich
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetSaintGall(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowSaintGall(wb, sheet1);

        // Définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 5000);
        sheet1.setColumnWidth((short) 1, (short) 5000);
        sheet1.setColumnWidth((short) 2, (short) 10000);
        sheet1.setColumnWidth((short) 3, (short) 10000);
        sheet1.setColumnWidth((short) 4, (short) 1500);
        sheet1.setColumnWidth((short) 5, (short) 5000);
        sheet1.setColumnWidth((short) 6, (short) 10000);

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));

            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }

            // adresse
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));

            // Adresse Proffessionnelle
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            String adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "31.12."
                    + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "01.01."
                        + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            }
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005",
                        JACalendar.todayJJsMMsAAAA(), Boolean.TRUE)).replace('\n', ' ');
            }
            cell.setCellValue(new HSSFRichTextString(adresseProf));
            // n° avs epoux
            // On test si c'est une femme sinon, pas afficher numéro du
            // conjoint
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean tiersAVS = new TITiersViewBean();
            tiersAVS.setSession(getSession());
            tiersAVS.setIdTiers(communication.getIdTiers());
            tiersAVS.retrieve();
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNumAvsActuel()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // Nom prénom epoux
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNomPrenom() + " "));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    /*
     * initialisation de la feuille xls spécifique au canton de Schaffouse
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetSchaffouse(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowSchaffouse(wb, sheet1);
        // Définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 2000);
        sheet1.setColumnWidth((short) 1, (short) 5000);
        sheet1.setColumnWidth((short) 2, (short) 10000);
        sheet1.setColumnWidth((short) 3, (short) 12000);
        sheet1.setColumnWidth((short) 4, (short) 1500);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // n° affilié
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // n° AVS
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getPrenom() + " " + communication.getNom()));
            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }

            // adresse
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));
            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    /*
     * initialisation de la feuille xls Standard
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetStandard(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        sheet1 = setTitleRowStandart(wb, sheet1);
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        // Définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 4000);
        sheet1.setColumnWidth((short) 1, (short) 4000);
        sheet1.setColumnWidth((short) 2, (short) 7000);
        sheet1.setColumnWidth((short) 3, (short) 6000);

        sheet1.setColumnWidth((short) 4, (short) 1500);

        sheet1.setColumnWidth((short) 5, (short) 4000);
        sheet1.setColumnWidth((short) 6, (short) 7000);
        sheet1.setColumnWidth((short) 7, (short) 7000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // n° AVS
            // cell = row.createCell((short) colNum++);
            // cell.setCellStyle(style);
            // 3 n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getPrenom() + " " + communication.getNom()));

            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }
            // adresse
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));

            // n° avs epoux
            // On test si c'est une femme sinon, pas afficher numéro du
            // conjoint
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean tiersAVS = new TITiersViewBean();
            tiersAVS.setSession(getSession());
            tiersAVS.setIdTiers(communication.getIdTiers());
            tiersAVS.retrieve();
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNumAvsActuel()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // n° de contribuable
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getTiers().getNumContribuableActuel()));
            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    public HSSFSheet populateSheetValais(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowValais(wb, sheet1);
        // Définition de la taille des cellules
        // nom prenom
        sheet1.setColumnWidth((short) 0, (short) 6000);
        // date naissance
        sheet1.setColumnWidth((short) 1, (short) 3500);
        // domicile
        sheet1.setColumnWidth((short) 2, (short) 5000);
        // navs
        sheet1.setColumnWidth((short) 3, (short) 4500);
        // contribuable
        sheet1.setColumnWidth((short) 4, (short) 4500);
        // num caisse
        sheet1.setColumnWidth((short) 5, (short) 3000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // 0 nom et prénom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));

            // 1 date de naissance
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getTiers().getDateNaissance()));

            // 2Localité
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(communication.getIdTiers());
            persAvs.retrieve();
            cell.setCellValue(new HSSFRichTextString(persAvs.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    "519005", JACalendar.todayJJsMMsAAAA(), new TILocaliteLongFormater())));

            // 3 n° AVS
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));

            // 4 numéro de contribuable
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumContri()));

            // 5 n° de caisse
            // Recherche de la description de la caisse
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            TIAdministrationViewBean admin = new TIAdministrationViewBean();
            admin.setSession(transaction.getSession());
            admin.setIdTiersAdministration(communication.getIdCaisse());
            try {
                admin.retrieve();
                cell.setCellValue(new HSSFRichTextString(admin.getCodeAdministration()));
            } catch (Exception e) {
                cell.setCellValue(new HSSFRichTextString(" "));
            }
            cell.setCellStyle(style);

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    public HSSFSheet populateSheetVaud(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowVaud(wb, sheet1);
        // Définition de la taille des cellules
        // nom prenom
        sheet1.setColumnWidth((short) 0, (short) 5000);
        // date naissance
        sheet1.setColumnWidth((short) 1, (short) 3000);
        // adresse
        sheet1.setColumnWidth((short) 2, (short) 5000);
        // npa
        sheet1.setColumnWidth((short) 3, (short) 2500);
        // localité
        sheet1.setColumnWidth((short) 4, (short) 3500);
        // adresse prof.
        sheet1.setColumnWidth((short) 5, (short) 7000);
        // nnss
        sheet1.setColumnWidth((short) 6, (short) 3500);
        // navs13
        sheet1.setColumnWidth((short) 7, (short) 5000);
        // n° affilié
        sheet1.setColumnWidth((short) 8, (short) 3500);
        // Contribuable
        sheet1.setColumnWidth((short) 9, (short) 3500);
        // catégorie
        sheet1.setColumnWidth((short) 10, (short) 3500);

        sheet1.setColumnWidth((short) 11, (short) 4000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // 0 nom et prénom
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));

            // 1 date de naissance
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getTiers().getDateNaissance()));

            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }

            // adresse
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));

            // 3 Adresse Proffessionnelle
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            String adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "31.12."
                    + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "01.01."
                        + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            }
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005",
                        JACalendar.todayJJsMMsAAAA(), Boolean.TRUE)).replace('\n', ' ');
            }
            cell.setCellValue(new HSSFRichTextString(adresseProf));
            // 3 n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // numAvs13
            // numéro AVS 13
            // String varString =
            // NSUtil.unFormatAVS(CPUtil.getNssOrNavs(communication.getNumAvs(),
            // getSession()));
            cell = row.createCell((short) colNum++);
            // cell.setCellValue(varString);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAvs()));
            cell.setCellStyle(style2);
            // 4 n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));

            // 0 numéro de contribuable
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(communication.getNumContri()));

            // 5 Catégorie
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                    communication.getGenreAffilie())));

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    /*
     * initialisation de la feuille xls spécifique au canton de Zurich
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetZurich(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleAffichage communication = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setWrapText(true);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRowZurich(wb, sheet1);

        // Définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 5000);
        sheet1.setColumnWidth((short) 1, (short) 5000);
        sheet1.setColumnWidth((short) 2, (short) 10000);
        sheet1.setColumnWidth((short) 3, (short) 5000);
        sheet1.setColumnWidth((short) 4, (short) 1500);
        sheet1.setColumnWidth((short) 5, (short) 10000);
        sheet1.setColumnWidth((short) 6, (short) 5000);
        sheet1.setColumnWidth((short) 7, (short) 10000);
        sheet1.setColumnWidth((short) 8, (short) 4000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((communication = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNumAffilie()));
            cell.setCellStyle(style);
            // n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            String numero = "";
            if (communication.getNumAvs() != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(communication.getNumAvs()).length() < tailleMin) {
                    int difference = tailleMin - communication.getNumAvs().length();
                    numero = communication.getNumAvs();
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(communication.getNumAvs());
                }
                if (numero.length() == 13) {
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            cell.setCellValue(new HSSFRichTextString(numero));
            // nom et prénom
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(communication.getNom() + " " + communication.getPrenom()));

            // Catégorie
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(),
                    communication.getGenreAffilie())));

            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(communication.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + communication.getAnneeDecision(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + communication.getAnneeDecision(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + communication.getAnneeDecision(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }

            // adresse
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));

            // Adresse Proffessionnelle
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            String adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "31.12."
                    + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005", "01.01."
                        + getAnneeDecision(), Boolean.TRUE)).replace('\n', ' ');
            }
            if (JadeStringUtil.isEmpty(adresseProf)) {
                adresseProf = (t.getAdresseAsString(CPApplication.TYPE_ADRESSE_PROFFESSIONNELLE, "519005",
                        JACalendar.todayJJsMMsAAAA(), Boolean.TRUE)).replace('\n', ' ');
            }
            cell.setCellValue(new HSSFRichTextString(adresseProf));
            // n° avs epoux
            // On test si c'est une femme sinon, pas afficher numéro du
            // conjoint
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            TITiersViewBean tiersAVS = new TITiersViewBean();
            tiersAVS.setSession(getSession());
            tiersAVS.setIdTiers(communication.getIdTiers());
            tiersAVS.retrieve();
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNumAvsActuel()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }
            // Nom prénom epoux
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            if (!tiersAVS.isNew()) {
                if (tiersAVS.getSexe().equals(TITiersViewBean.CS_FEMME)) {
                    if (communication.getConjoint() != null) {
                        cell.setCellValue(new HSSFRichTextString(communication.getConjoint().getNomPrenom() + " "));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(""));
                }
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }

            // Date d'affiliation
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            AFAffiliation af = new AFAffiliation();
            af.setSession(getSession());
            af.setId(communication.getIdAffiliation());
            af.retrieve();
            cell.setCellValue(new HSSFRichTextString(af.getDateDebut()));

            // Mise à jour
            CPCommunicationFiscale comFis = new CPCommunicationFiscale();
            comFis.setSession(getSession());
            comFis.setIdCommunication(communication.getIdCommunication());
            comFis.retrieve(transaction);
            if (!comFis.isNew()) {
                comFis.setDateEnvoi(getDateEnvoi());
                comFis.update(transaction);
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0213CCP");
        return sheet1;
    }

    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }

    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    public void setDateEnvoiVide(String newDateEnvoiVide) {
        dateEnvoiVide = newDateEnvoiVide;
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    public void setForGenreAffilie(String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private HSSFSheet setTitleRowAppenzelR(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String numAvsEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String dateNaissanceEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String numContribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String[] COL_TITLES = { naff, numAvs, prenomNom, adresse, npa, localite, numAvsEpoux, dateNaissanceEpoux,
                numContribuable };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... spécifique à Nidwald
     */
    private HSSFSheet setTitleRowGlaris(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String contribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String domicile = getSession().getApplication().getLabel("DOMICILE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String numAffilie = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String categorie = getSession().getApplication().getLabel("CATEGORIE", getLangue());
        final String numCaisse = getSession().getApplication().getLabel("NUM_CAISSE", getLangue());
        final String numAvsEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String nomEpoux = getSession().getApplication().getLabel("NOM_PRENOM_EPOUX", getLangue());
        final String dateAffiliation = getSession().getApplication().getLabel("DATE_AFFILIATION", getLangue());
        final String[] COL_TITLES = { contribuable, prenomNom, domicile, numAvs, numAffilie, categorie, numCaisse,
                numAvsEpoux, nomEpoux, dateAffiliation };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private HSSFSheet setTitleRowGrisonThurgovie(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String[] COL_TITLES = { naff, numAvs, prenomNom, adresse, npa, localite };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private HSSFSheet setTitleRowJura(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String adresseProf = getSession().getApplication().getLabel("ADRESSE_PROF", getLangue());
        final String prenomNomEpoux = getSession().getApplication().getLabel("NOM_PRENOM_EPOUX", getLangue());
        final String numAvsEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String[] COL_TITLES = { prenomNom, numAvs, naff, adresse, npa, localite, adresseProf, prenomNomEpoux,
                numAvsEpoux };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... spécifique à valais
     */
    private HSSFSheet setTitleRowNeuchatel(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String numCaisse = getSession().getApplication().getLabel("NUM_CAISSE", getLangue());
        final String nomCaisse = getSession().getApplication().getLabel("NOM_CAISSE", getLangue());
        final String categorie = getSession().getApplication().getLabel("CATEGORIE", getLangue());
        final String numAffilie = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String contribuable = getSession().getApplication().getLabel("NUM_BDP", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String dateNaissance = getSession().getApplication().getLabel("DATE_NAISSANCE", getLangue());

        final String[] COL_TITLES = { numCaisse, nomCaisse, categorie, numAffilie, numAvs, contribuable, prenomNom,
                dateNaissance };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... spécifique à Nidwald
     */
    private HSSFSheet setTitleRowNidwald(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String numCaisse = getSession().getApplication().getLabel("NUM_CAISSE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String numAffilie = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String domicile = getSession().getApplication().getLabel("DOMICILE", getLangue());
        final String categorie = getSession().getApplication().getLabel("CATEGORIE", getLangue());
        final String dateAffiliation = getSession().getApplication().getLabel("DATE_AFFILIATION", getLangue());
        final String numAvsEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String nomEpoux = getSession().getApplication().getLabel("NOM_PRENOM_EPOUX", getLangue());

        final String[] COL_TITLES = { numCaisse, prenomNom, numAvs, numAffilie, domicile, categorie, dateAffiliation,
                numAvsEpoux, nomEpoux };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private HSSFSheet setTitleRowSaintGall(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String adresseProf = getSession().getApplication().getLabel("ADRESSE_PROF", getLangue());
        final String numAvsEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String prenomNomEpoux = getSession().getApplication().getLabel("NOM_PRENOM_EPOUX", getLangue());
        final String[] COL_TITLES = { naff, numAvs, prenomNom, adresse, npa, localite, adresseProf, numAvsEpoux,
                prenomNomEpoux };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private HSSFSheet setTitleRowSchaffouse(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String[] COL_TITLES = { naff, numAvs, prenomNom, adresse, npa, localite };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private HSSFSheet setTitleRowStandart(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String numAvsEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String numContribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String[] COL_TITLES = { naff, numAvs, prenomNom, adresse, npa, localite, numAvsEpoux, numContribuable };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... spécifique à valais
     */
    private HSSFSheet setTitleRowValais(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String dateNaissance = getSession().getApplication().getLabel("DATE_NAISSANCE", getLangue());
        final String domicile = getSession().getApplication().getLabel("DOMICILE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String contribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String numCaisse = getSession().getApplication().getLabel("NUM_CAISSE", getLangue());

        final String[] COL_TITLES = { prenomNom, dateNaissance, domicile, numAvs, contribuable, numCaisse };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... spécifique à vaud
     */
    private HSSFSheet setTitleRowVaud(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String dateNaissance = getSession().getApplication().getLabel("DATE_NAISSANCE", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String adresseProf = getSession().getApplication().getLabel("ADRESSE_PROF", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String numAvs13 = getSession().getApplication().getLabel("NUM_AVS13", getLangue());
        final String numAffilie = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String contribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String categorie = getSession().getApplication().getLabel("CATEGORIE", getLangue());
        final String[] COL_TITLES = { prenomNom, dateNaissance, adresse, npa, localite, adresseProf, numAvs, numAvs13,
                numAffilie, contribuable, categorie };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private HSSFSheet setTitleRowZurich(HSSFWorkbook wb, HSSFSheet sheet) throws Exception {
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String categorie = getSession().getApplication().getLabel("CATEGORIE", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String adresseProf = getSession().getApplication().getLabel("ADRESSE_PROF", getLangue());
        final String numAvsEpoux = getSession().getApplication().getLabel("NUM_AVS_EPOUX", getLangue());
        final String prenomNomEpoux = getSession().getApplication().getLabel("NOM_PRENOM_EPOUX", getLangue());
        final String dateAffiliation = getSession().getApplication().getLabel("DATE_AFFILIATION", getLangue());
        final String[] COL_TITLES = { naff, numAvs, prenomNom, categorie, adresse, npa, localite, adresseProf,
                numAvsEpoux, prenomNomEpoux, dateAffiliation };
        HSSFRow row = null;
        HSSFCell c;
        // création du style pour le titre de la page
        HSSFFont font2 = wb.createFont();
        font2.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font2.setFontHeight((short) 200);
        font2.setColor(HSSFFont.COLOR_NORMAL);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        row = sheet.createRow(0);
        c = row.createCell((short) 0);
        try {
            c.setCellValue(new HSSFRichTextString(session.getApplication().getProperty(
                    "COMPANYNAME_" + getSession().getIdLangueISO().toUpperCase())));
        } catch (Exception e) {
            c.setCellValue(new HSSFRichTextString(""));
        }
        c.setCellStyle(style2);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        c = row.createCell((short) 2);
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LIST_COMMUNICATIONS_FISCALES",
                getLangue())));
        c.setCellStyle(style2);
        // critères de sélection
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName(HSSFFont.FONT_ARIAL); // arial font
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // arial font bold
        font3.setFontHeight((short) 200);
        style3.setFont(font3);
        row = sheet.createRow(3);
        row = sheet.createRow(4);
        // création de l'entête
        try {
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("CANTON", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForGenreAffilie())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("ANNEE", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getAnneeDecision()));
            c.setCellStyle(style3);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // let's use a nifty font for the title
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setFont(font2);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // aligned center
        // ??? style.setFillBackgroundColor((short) 150);
        style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        // create Title Row
        row = sheet.createRow(8);
        row = sheet.createRow(9);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

}