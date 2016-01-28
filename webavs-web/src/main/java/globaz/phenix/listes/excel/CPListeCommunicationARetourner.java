/**
 * class CPImpressionCotisationDifferente écrit le 19/01/05 par JPA
 * 
 * class créant un document .xls des décisions avec mise en compte
 * 
 * @author JPA
 */
package globaz.phenix.listes.excel;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPCommunicationPlausibilite;
import globaz.phenix.db.communications.CPCommunicationPlausibiliteManager;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CPListeCommunicationARetourner {
    public static final int CS_ALLEMAND = 503002;

    public static final int CS_FRANCAIS = 503001;

    public static final int CS_ITALIEN = 503004;

    private String canton;

    private String dateFichier = "";

    private String idPlausibilite;

    private BProcess processAppelant = null;

    private BSession session;

    private HSSFSheet sheet1;
    private Boolean simulation = Boolean.TRUE;
    private HSSFWorkbook wb;

    public CPListeCommunicationARetourner() {
        wb = new HSSFWorkbook();
        // Paramètres d'impression
        // HSSFPrintSetup psa = sheet1.getPrintSetup();
        // psa.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // psa.setLandscape(true);
        // sheet1.setSelected(true);
    }

    public String getCanton() {
        return canton;
    }

    public String getDateFichier() {
        return dateFichier;
    }

    /**
     * Réalise le mapping des langue du canton en CODE SYSTEM
     * 
     * @author:BTC
     * @return int CS_FRANCAIS = 503001; CS_ALLEMAND = 503002; CS_ITALIEN = 503004;
     */
    private int getIdLangue() throws Exception {
        int idLangue = CPListeCommunicationARetourner.CS_FRANCAIS; // defaut
        idLangue = Integer.parseInt(((CPApplication) getSession().getApplication())
                .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(getSession(), getCanton())));
        return idLangue;
    }

    public String getIdPlausibilite() {
        return idPlausibilite;
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

    /**
     * @param retour
     * @throws Exception
     */
    private String getLibelleErreur(ICommunicationRetour retour) throws Exception {
        String msgString = "";
        CPCommunicationPlausibiliteManager managerPlausi = new CPCommunicationPlausibiliteManager();
        managerPlausi.setSession(getSession());
        managerPlausi.setForIdCommunication(retour.getIdRetour());
        managerPlausi.find();
        for (int i = 0; i < managerPlausi.size(); i++) {
            CPCommunicationPlausibilite lien = (CPCommunicationPlausibilite) managerPlausi.get(i);
            if (JadeStringUtil.isEmpty(msgString) == false) {
                msgString += "\n";
            }
            msgString += lien.getLibellePlausibilite(getSession(), retour);

        }
        return msgString;
    }

    /*
     * méthode pour gérer le fichier en sortie
     */
    public String getOutputFile() {
        try {

            // Recherche du nom de fichier (par canton)
            String nom = "";
            CPReceptionReaderManager manager = new CPReceptionReaderManager();
            manager.setSession(getSession());
            manager.setForIdCanton(getCanton());
            manager.changeManagerSize(1);
            manager.find();
            if (manager.size() > 0) {
                CPReceptionReader reader = (CPReceptionReader) manager.getFirstEntity();
                if (reader.getNomFichier().length() > 0) {
                    nom = reader.getNomFichier() + "_";
                }
            }
            String nomSuite = "";
            try {
                nomSuite = (getSession().getApplication()).getProperty("nomPourRetourFisc");
                if (JadeStringUtil.isNull(nomSuite)) {
                    nomSuite = "";
                } else {
                    nomSuite += "_";
                }
            } catch (Exception e) {
                nomSuite = "";
            }
            nom += nomSuite;
            if (!JadeStringUtil.isEmpty(getIdPlausibilite())) {
                nom += getIdPlausibilite() + "_";
            }
            nom += JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD) + "_";
            File f = File.createTempFile(nom, ".xls");
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

    public Boolean getSimulation() {
        return simulation;
    }

    public HSSFSheet populateSheet(ICommunicationrRetourManager manager, BTransaction transaction) throws Exception {
        HSSFSheet sheet = null;
        // On retourne la liste spécifique au canton de Zurich
        if (getCanton().equals(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
            sheet = populateSheetVaud(manager, transaction);
            // On retourne la liste standard
        } else {
            sheet = populateSheetStandard(manager, transaction);
        }
        return sheet;
    }

    /*
     * initialisation de la feuille xls Standard
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheetStandard(ICommunicationrRetourManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleRetourViewBean retour = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount(transaction));
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
        sheet1.setColumnWidth((short) 2, (short) 4000);
        sheet1.setColumnWidth((short) 3, (short) 6000);
        sheet1.setColumnWidth((short) 4, (short) 6000);
        sheet1.setColumnWidth((short) 5, (short) 2000);
        sheet1.setColumnWidth((short) 6, (short) 5000);
        sheet1.setColumnWidth((short) 7, (short) 5000);
        sheet1.setColumnWidth((short) 8, (short) 2000);
        sheet1.setColumnWidth((short) 9, (short) 10000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((retour = (CPCommunicationFiscaleRetourViewBean) manager.cursorReadNext(statement)) != null)
                && (!retour.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows() + 1);
            int colNum = 0;
            HSSFCell cell;
            // 0 - n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(retour.getNumAffilie()));
            cell.setCellStyle(style);
            // 1 - n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(retour.getNumAvs(0)));
            // 2 - n° de contribuable
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(retour.getNumContribuableRecu()));
            // 3 - prénom et nom
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(retour.getPrenom() + " " + retour.getNom()));
            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(retour.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + retour.getAnnee1(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + retour.getAnnee1(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + retour.getAnnee1(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + retour.getAnnee1(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }
            // 4 - Rue
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(rue + " " + numRue));
            // 5 - npa
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(npa));
            // 6 - localité
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(ville));
            // 7 - Genre
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), retour.getGenreAffilie())));
            // 8 - Année
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(retour.getAnnee1()));
            // 9 Erreur
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(getLibelleErreur(retour)));
            cell.setCellStyle(style2);
            // Mise à jour
            if (getSimulation().equals(Boolean.FALSE)) {
                CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                comFis.setSession(getSession());
                comFis.setIdCommunication(retour.getIdCommunication());
                comFis.retrieve(transaction);
                if (!comFis.isNew()) {
                    comFis.setDateRetour("");
                    comFis.update(transaction);
                }
            }
            processAppelant.incProgressCounter();
        }
        return sheet1;
    }

    public HSSFSheet populateSheetVaud(ICommunicationrRetourManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleRetourVDViewBean retour = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount(transaction));
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
        // date retour
        sheet1.setColumnWidth((short) 0, (short) 3000);
        // année
        sheet1.setColumnWidth((short) 1, (short) 2000);
        // Contribuable
        sheet1.setColumnWidth((short) 2, (short) 5000);
        // nom prenom
        sheet1.setColumnWidth((short) 3, (short) 8000);
        // nnss
        sheet1.setColumnWidth((short) 4, (short) 5000);
        // Genre
        sheet1.setColumnWidth((short) 5, (short) 3500);
        // n° affilié
        sheet1.setColumnWidth((short) 6, (short) 5000);
        // Erreur
        sheet1.setColumnWidth((short) 7, (short) 10000);
        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((retour = (CPCommunicationFiscaleRetourVDViewBean) manager.cursorReadNext(statement)) != null)
                && (!retour.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows() + 1);
            int colNum = 0;
            HSSFCell cell;
            // 0 date retour
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(retour.getDateRetour()));
            // 1 année
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(retour.getAnnee1()));
            // 2 numéro de contribuable
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(retour.getVdNumContribuable()));
            // 3 nom et prénom
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(retour.getVdNomPrenom()));
            // 4 n° AVS
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(retour.getVdNumAvs()));
            // 5 Genre
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(retour.getVdGenreAffilie()));
            // 6 n° affilié
            cell = row.createCell((short) colNum++);
            cell.setCellStyle(style2);
            cell.setCellValue(new HSSFRichTextString(retour.getVdNumAffilie()));
            // 7 Erreur
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(getLibelleErreur(retour)));
            cell.setCellStyle(style2);
            // Mise à jour
            if (Boolean.FALSE.equals(getSimulation())) {
                CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                comFis.setSession(getSession());
                comFis.setIdCommunication(retour.getIdCommunication());
                comFis.retrieve(transaction);
                if (!comFis.isNew()) {
                    comFis.setDateRetour("");
                    comFis.update(transaction);
                }
            }
            processAppelant.incProgressCounter();
        }
        return sheet1;
    }

    public void setCanton(String forCanton) {
        canton = forCanton;
    }

    public void setDateFichier(String string) {
        dateFichier = string;
    }

    public void setIdPlausibilite(String idPlausibilite) {
        this.idPlausibilite = idPlausibilite;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
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
        final String genre = getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue());
        final String annee = getSession().getApplication().getLabel("ANNEE", getLangue());
        final String numContribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String erreur = getSession().getApplication().getLabel("ERREUR", getLangue());
        final String[] COL_TITLES = { naff, numAvs, numContribuable, prenomNom, adresse, npa, localite, genre, annee,
                erreur };
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
        c.setCellValue(new HSSFRichTextString(getSession().getApplication().getLabel("LISTCOMMUNICATIONRETOUR",
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
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication()
                    .getLabel("DATE_IMPRESSION", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getDateFichier()));
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
        final String dateRetour = getSession().getApplication().getLabel("DETAIL_FISC_VD_DATE_RECEPTION", getLangue());
        final String annee = getSession().getApplication().getLabel("ANNEE", getLangue());
        final String numContribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String nomPrenom = getSession().getApplication().getLabel("NOM_PRENOM", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String genreAffilie = getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue());
        final String numAffilie = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        // final String adresse =
        // getSession().getApplication().getLabel("ADRESSE", getLangue());
        // final String localite =
        // getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String erreur = getSession().getApplication().getLabel("ERREUR", getLangue());
        final String[] COL_TITLES = { dateRetour, annee, numContribuable, nomPrenom, numAvs, genreAffilie, numAffilie,
                erreur };
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
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getCanton())));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(getSession().getApplication()
                    .getLabel("DATE_IMPRESSION", getLangue())));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getDateFichier()));
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