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
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionNonDefinitives;
import globaz.phenix.db.principale.CPDecisionNonDefinitivesManager;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CPListeDecisionsNonDefinitives {
    private String anneeDecision;
    private String date_impression = "";
    private BProcess processAppelant = null;
    // private String fileName = new String();
    private BSession session;
    private HSSFSheet sheet1;
    private String user = "";
    private HSSFWorkbook wb;

    public CPListeDecisionsNonDefinitives() {
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("");
    }

    // créé la feuille xls
    public CPListeDecisionsNonDefinitives(BSession session, String annee) {
        this.session = session;
        wb = new HSSFWorkbook();
        setDate_impression(JACalendar.today().getDay() + "." + JACalendar.today().getMonth() + "."
                + JACalendar.today().getYear());
        setUser(getSession().getUserFullName());
        setAnneeDecision(annee);
        // fileName = getSession().getLabel("LISTNONDEF");
    }

    public String getAnneeDecision() {
        return anneeDecision;
    }

    public String getDate_impression() {
        return date_impression;
    }

    /*
     * méthode pour gérer le fichier en sortie
     */
    public String getOutputFile() {
        try {
            // récup du viewBean pour avoir accès au idpassage et libellepassage
            // pour le sujet de l'e-mail
            // viewBean.execute();
            File f = File.createTempFile(getSession().getLabel("LISTNONDEF") + " - " + getAnneeDecision(), ".xls");
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

    public String getUser() {
        return user;
    }

    /*
     * initialisation de la feuille xls
     */
    // création de la liste décisions avec mise en compte
    public HSSFSheet populateSheet(CPDecisionNonDefinitivesManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        CPDecisionNonDefinitives decisionsMec = null;
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
        sheet1 = wb.createSheet(getSession().getLabel("PAGE") + " 1");
        sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        // orientation
        sheet1.getPrintSetup().setLandscape(true);
        // marges en-tête/pied de page
        sheet1.getPrintSetup().setHeaderMargin(0);
        sheet1.getPrintSetup().setFooterMargin(0);
        sheet1.setMargin((short) 0, 0.5); // Marge gauche
        sheet1.setMargin((short) 1, 0.5); // Marge droite
        sheet1 = setTitleRow(wb, sheet1);

        // sheet1.setDefaultColumnWidth((short)30);
        // définition de la taille des cell
        sheet1.setColumnWidth((short) 0, (short) 3500); // N° affilié
        sheet1.setColumnWidth((short) 1, (short) 4000); // NSS
        sheet1.setColumnWidth((short) 2, (short) 4000); // N° contribuable
        sheet1.setColumnWidth((short) 3, (short) 7000); // Nom prénom
        sheet1.setColumnWidth((short) 4, (short) 2500); // Canton
        sheet1.setColumnWidth((short) 5, (short) 6000); // NPA - Localité
        sheet1.setColumnWidth((short) 6, (short) 3000); // Période
        sheet1.setColumnWidth((short) 7, (short) 5000); // Type de décision

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((decisionsMec = (CPDecisionNonDefinitives) manager.cursorReadNext(statement)) != null)
                && (!decisionsMec.isNew())) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (!CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decisionsMec.getSpecification())) {
                HSSFRow row = sheet1.createRow(sheet1.getPhysicalNumberOfRows() + 1);
                int colNum = 0;
                HSSFCell cell;
                // numéro affilié
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getNumAffilie()));
                cell.setCellStyle(style);
                // numéro AVS
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getNumAvs()));
                cell.setCellStyle(style);
                // numéro contribuable
                TIHistoriqueContribuable hist = new TIHistoriqueContribuable();
                hist.setSession(getSession());
                String numContri = "";
                try {
                    numContri = hist.findPrevKnownNumContribuable(decisionsMec.getIdTiers(),
                            "31.12." + decisionsMec.getAnnee());
                    if (JadeStringUtil.isIntegerEmpty(numContri)) {
                        numContri = hist.findNextKnownNumContribuable(decisionsMec.getIdTiers(), "31.12."
                                + decisionsMec.getAnnee());
                    }
                } catch (Exception e) {
                    numContri = "";
                }
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(numContri));
                cell.setCellStyle(style);
                // nom et prénom
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(decisionsMec.getNomPrenom()));
                cell.setCellStyle(style);
                // Code canton - NPA localité
                TITiersViewBean t = new TITiersViewBean();
                t.setSession(getSession());
                t.setIdTiers(decisionsMec.getIdTiers());
                t.retrieve();

                TIAdresseDataSource adresse = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "31.12." + decisionsMec.getAnnee(), true);
                String codeCanton = "";
                Hashtable<?, ?> data = null;
                if (adresse != null) {
                    data = adresse.getData();
                    codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                    if (codeCanton.equalsIgnoreCase("030") || JadeStringUtil.isEmpty(codeCanton)) { // 30
                        // =
                        // Etranger
                        adresse = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005", "31.12."
                                + decisionsMec.getAnnee(), true);
                        if (adresse != null) {
                            data = adresse.getData();
                            codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                            if ("050".equalsIgnoreCase(codeCanton)) {
                                codeCanton = IConstantes.CS_LOCALITE_CANTON_JURA;
                            } else {
                                codeCanton = "505" + codeCanton;
                            }
                        }
                    }
                }
                if (JadeStringUtil.isEmpty(codeCanton)) {
                    codeCanton = t.getCanton();
                }
                cell = row.createCell((short) colNum++);
                if (data != null) {
                    cell.setCellValue(new HSSFRichTextString((String) data
                            .get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_COURT)));
                }
                cell.setCellStyle(style);
                cell = row.createCell((short) colNum++);
                if (data != null) {
                    cell.setCellValue(new HSSFRichTextString((String) data
                            .get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA)
                            + " - "
                            + (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE)));
                }
                cell.setCellStyle(style);
                // periode
                cell = row.createCell((short) colNum++);
                cell.setCellValue(new HSSFRichTextString(JACalendar.getDay(decisionsMec.getPeriodeDeb()) + "."
                        + JACalendar.getMonth(decisionsMec.getPeriodeDeb()) + "-"
                        + JACalendar.getDay(decisionsMec.getPeriodeFin()) + "."
                        + JACalendar.getMonth(decisionsMec.getPeriodeFin())));
                cell.setCellStyle(style);
                // type de décision
                try {
                    if ((decisionsMec.getTypeDecision() != "0") && (decisionsMec.getTypeDecision() != null)) {
                        cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(globaz.phenix.translation.CodeSystem.getLibelle(
                                getSession(), decisionsMec.getTypeDecision())));
                        cell.setCellStyle(style);
                    } else {
                        cell = row.createCell((short) colNum++);
                        cell.setCellValue(new HSSFRichTextString(" "));
                        cell.setCellStyle(style);
                    }
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                }
            }
            processAppelant.incProgressCounter();
        }
        HSSFFooter footer = sheet1.getFooter();
        footer.setLeft(HSSFFooter.font("Stencil-Normal", "Italic") + HSSFFooter.fontSize((short) 8) + "Ref: 0087CCP");
        return sheet1;
    }

    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures...
     */
    private HSSFSheet setTitleRow(HSSFWorkbook wb, HSSFSheet sheet) {
        final String naff = session.getLabel("NUM_AFFILIE");
        final String numavs = session.getLabel("NUM_AVS");
        final String numContribuable = session.getLabel("NUM_CONTRIBUABLE");
        final String nomprenom = session.getLabel("NOM_PRENOM");
        final String canton = session.getLabel("CANTON");
        final String localite = session.getLabel("LOCALITE");
        final String periode = session.getLabel("PERIODE");
        final String typedecision = session.getLabel("TYPE_DECISION");

        final String[] COL_TITLES = { naff, numavs, numContribuable, nomprenom, canton, localite, periode, typedecision };
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
        c.setCellValue(new HSSFRichTextString(session.getLabel("LISTNONDEF")));
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
            c.setCellValue(new HSSFRichTextString(session.getLabel("DATE_IMPRESSION")));
            c.setCellStyle(style3);
            c = row.createCell((short) 3);
            c.setCellValue(new HSSFRichTextString(getDate_impression()));
            c.setCellStyle(style3);
            // row = sheet.createRow(6);
            // c = row.createCell((short) 0);
            // c.setCellValue("");
            // c.setCellStyle(style3);
            // c = row.createCell((short) 1);
            // c.setCellValue(this.session.getLabel("CANTON"));
            // c.setCellStyle(style3);
            // c = row.createCell((short) 3);
            // c.setCellValue(getCanton());
            // c.setCellStyle(style3);
            row = sheet.createRow(7);
            c = row.createCell((short) 0);
            c.setCellValue(new HSSFRichTextString(""));
            c.setCellStyle(style3);
            c = row.createCell((short) 1);
            c.setCellValue(new HSSFRichTextString(session.getLabel("ANNEE")));
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

    public void setUser(String string) {
        user = string;
    }

}