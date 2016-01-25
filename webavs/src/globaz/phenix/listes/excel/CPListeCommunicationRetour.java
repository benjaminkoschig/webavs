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
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationPlausibilite;
import globaz.phenix.db.communications.CPCommunicationPlausibiliteManager;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import globaz.phenix.translation.CodeSystem;
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

public class CPListeCommunicationRetour {
    private String date_impression = "";
    private String forIdRetour = "";
    private String forStatus = "";
    private String fromNumAffilie = "";
    private String idJournalRetour = "";
    private BProcess processAppelant = null;
    private BSession session;
    private HSSFSheet sheet1;
    private String tillNumAffilie = "";
    private HSSFWorkbook wb;

    public CPListeCommunicationRetour() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");
    }

    // créé la feuille xls
    public CPListeCommunicationRetour(BSession session) {
        this.session = session;
        wb = new HSSFWorkbook();
        setDate_impression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        // fileName = getSession().getLabel("LISTRETOUR");
    }

    public String getDate_impression() {
        return date_impression;
    }

    public String getForIdRetour() {
        return forIdRetour;
    }

    public String getForStatus() {
        return forStatus;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    /*
     * méthode pour gérer le fichier en sortie
     */
    public String getOutputFile() {
        try {
            // récup du viewBean pour avoir accès au idpassage et libellepassage
            // pour le sujet de l'e-mail
            // viewBean.execute();
            File f = File.createTempFile(getSession().getLabel("LISTCOMMUNICATIONRETOUR") + " - "
                    + getIdJournalRetour(), ".xls");
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

    public String getTillNumAffilie() {
        return tillNumAffilie;
    }

    /*
     * initialisation de la feuille xls
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheet(ICommunicationrRetourManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        ICommunicationRetour comRet = null;
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
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        HSSFCellStyle styleMontant = wb.createCellStyle();
        styleMontant.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleMontant.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleMontant.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleMontant.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleMontant.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        styleMontant.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styleMontant.setDataFormat((short) 4);
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        // format
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        sheet1 = setTitleRow(wb, sheet1);
        // définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 4000);
        sheet1.setColumnWidth((short) 1, (short) 4000);
        sheet1.setColumnWidth((short) 2, (short) 4000);
        sheet1.setColumnWidth((short) 3, (short) 4000);
        sheet1.setColumnWidth((short) 4, (short) 10000);
        sheet1.setColumnWidth((short) 5, (short) 2500);
        sheet1.setColumnWidth((short) 6, (short) 4000);
        sheet1.setColumnWidth((short) 7, (short) 4000);
        sheet1.setColumnWidth((short) 8, (short) 4000);
        sheet1.setColumnWidth((short) 9, (short) 4000);
        sheet1.setColumnWidth((short) 10, (short) 10000);

        statement = manager.cursorOpen(transaction);
        String champRecherche = "";
        // parcours du manager et remplissage des cell
        while (((comRet = (ICommunicationRetour) manager.cursorReadNext(statement)) != null) && (!comRet.isNew())) {
            if (JadeStringUtil.isBlankOrZero(champRecherche)) {
                champRecherche = comRet.getJournalRetour().getZoneRecherche();
            }
            HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows());
            int colNum = 0;
            HSSFCell cell;
            // numéro affilié
            String varString = comRet.getAffiliation().getAffilieNumero();
            if (JadeStringUtil.isEmpty(varString)) {
                varString = comRet.getDescription(2);
            }
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(varString));
            cell.setCellStyle(style);
            // numéro AVS
            if (!JadeStringUtil.isBlankOrZero(comRet.getIdTiers())
                    || !JadeStringUtil.isBlankOrZero(comRet.getIdConjoint())) {
                if (JadeStringUtil.isBlankOrZero(comRet.getIdTiers())) {
                    comRet.setIdTiers(comRet.getIdConjoint());
                }
                varString = comRet.getTiers().getNumAvsActuel();
            } else {
                varString = comRet.getNumAvs(0);
            }
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(varString));
            cell.setCellStyle(style);
            // numéro de contribuable
            if (comRet.getTiers() != null) {
                varString = comRet.getTiers().getNumContribuableActuel();
            } else {
                varString = comRet.getDescription(1);
            }
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(varString));
            cell.setCellStyle(style);
            // numéro de référence avec le fisc
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(comRet.getValeurRechercheBD(champRecherche)));
            cell.setCellStyle(style);
            // nom et prénom
            cell = row.createCell((short) colNum++);
            if (comRet.getTiers() != null) {
                cell.setCellValue(new HSSFRichTextString(comRet.getTiers().getNomPrenom()));
            } else {
                cell.setCellValue(new HSSFRichTextString(""));
            }
            cell.setCellStyle(style);
            // annee
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(comRet.getAnnee1()));
            cell.setCellStyle(style);
            // revenu
            String montant = "0";
            if (!JadeStringUtil.isEmpty(comRet.getRevenu1())) {
                montant = JANumberFormatter.deQuote(comRet.getRevenu1());
            }
            cell = row.createCell((short) colNum++);
            Double dMontant = new Double(montant);
            cell.setCellValue(dMontant.doubleValue());
            cell.setCellStyle(styleMontant);
            // Capital
            montant = "0";
            if (!JadeStringUtil.isEmpty(comRet.getCapital())) {
                montant = JANumberFormatter.deQuote(comRet.getCapital());
            }
            cell = row.createCell((short) colNum++);
            dMontant = new Double(montant);
            cell.setCellValue(dMontant.doubleValue());
            cell.setCellStyle(styleMontant);
            // Fortune
            montant = "0";
            if (!JadeStringUtil.isEmpty(comRet.getFortune())) {
                montant = JANumberFormatter.deQuote(comRet.getFortune());
            }
            cell = row.createCell((short) colNum++);
            dMontant = new Double(montant);
            cell.setCellValue(dMontant.doubleValue());
            cell.setCellStyle(styleMontant);
            // Status
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), comRet.getStatus())));
            cell.setCellStyle(style);
            // Erreur
            String msgString = "";
            CPCommunicationPlausibiliteManager managerPlausi = new CPCommunicationPlausibiliteManager();
            managerPlausi.setSession(getSession());
            managerPlausi.setForIdCommunication(comRet.getIdRetour());
            managerPlausi.find();
            for (int i = 0; i < managerPlausi.size(); i++) {
                CPCommunicationPlausibilite lien = (CPCommunicationPlausibilite) managerPlausi.get(i);
                // PO 9153
                msgString += lien.getLibellePlausibilite(getSession(), comRet) + "\n";
            }
            cell = row.createCell((short) colNum++);
            cell.setCellValue(new HSSFRichTextString(msgString));
            cell.setCellStyle(style);
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0078CCP");
        return sheet1;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setForIdRetour(String forIdRetour) {
        this.forIdRetour = forIdRetour;
    }

    public void setForStatus(String forStatus) {
        this.forStatus = forStatus;
    }

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setIdJournalRetour(String idJournalRetour) {
        this.idJournalRetour = idJournalRetour;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTillNumAffilie(String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures...
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        final String naff = session.getLabel("NUM_AFFILIE");
        final String numavs = session.getLabel("NUM_AVS");
        final String nContribuable = session.getLabel("NUM_CONTRIBUABLE");
        final String nReference = session.getLabel("NUM_REFERENCE");
        final String nomprenom = session.getLabel("NOM_PRENOM");
        final String annee = session.getLabel("ANNEE");
        final String revenu = session.getLabel("REVENU_ANNUEL");
        final String capital = session.getLabel("CAPITAL");
        final String fortune = session.getLabel("ITEXT_DECISION_FORTUNE");
        final String status = session.getLabel("STATUS");
        final String erreur = session.getLabel("ERREUR");

        final String[] COL_TITLES = { naff, numavs, nContribuable, nReference, nomprenom, annee, revenu, capital,
                fortune, status, erreur };
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
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTCOMMUNICATIONRETOUR")));
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
            row = sheet.createRow(4);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("DATE_IMPRESSION")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getDate_impression()));
            c.setCellStyle(style3);
            row = sheet.createRow(5);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("JOURNAL")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getIdJournalRetour()));
            c.setCellStyle(style3);
            row = sheet.createRow(6);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_DEB")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getFromNumAffilie()));
            c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("LISTE_AFF_FIN")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getTillNumAffilie()));
            c.setCellStyle(style3);
            row = sheet.createRow(8);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("STATUS")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(CodeSystem.getLibelle(getSession(), getForStatus())));
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
        row = sheet.createRow(9);
        row = sheet.createRow(10);
        for (int i = 0; i < COL_TITLES.length; i++) {
            c = row.createCell((short) i);
            c.setCellValue(new HSSFRichTextString(COL_TITLES[i]));
            c.setCellStyle(style);
        }
        return sheet;
    }

}