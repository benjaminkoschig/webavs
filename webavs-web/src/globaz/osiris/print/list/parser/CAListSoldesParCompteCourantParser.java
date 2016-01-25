package globaz.osiris.print.list.parser;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptecourant.CASoldesParCompteCourant;
import globaz.osiris.db.comptecourant.CASoldesParCompteCourantManager;
import globaz.osiris.print.list.CAAbstractListExcel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author dda
 */
public class CAListSoldesParCompteCourantParser extends CAAbstractListExcel {

    class SectionsCompteAnnexe {
        String compteAnnexe = "";
        TreeMap<String, TreeMap<String, String>> sections = new TreeMap<String, TreeMap<String, String>>();
    }

    private static final int COLUMN_SIZE_AFILIE = 5000;
    private static final int COLUMN_SIZE_COMPTE_COURANT = 4000;
    private static final int COLUMN_SIZE_COMPTE_TOTAL = 4500;
    private static final int COLUMN_SIZE_FACTURE = 2800;
    private static final String LABEL_AFFILIE = "AFFILIE";

    private static final String LABEL_DESCRIPTION = "DESCRIPTION";
    private static final String LABEL_FACTURE = "FACTURE";
    private static final String LABEL_LISTE = "LISTE";
    private static final String LABEL_TOTAL = "Total";

    private static final String NUMERO_REFERENCE_INFOROM = "0124GCA";
    private BProcess process = null;
    private HSSFCellStyle styleBorderLeftRightAlignCenter = null;
    private HSSFCellStyle styleBorderLeftRightAlignLeft = null;

    private HSSFCellStyle styleBorderLeftRightAlignRight = null;

    private HSSFCellStyle styleBorderLeftRightAlignRightNoBg = null;
    private HSSFCellStyle styleFinalTotalLineAllBorder = null;
    private HSSFCellStyle styleFinalTotalLineNoBorderLeftRight = null;
    private HSSFCellStyle styleFinalTotalLineNoBorderRight = null;

    private HSSFCellStyle styleFinalTotalLineNoBorderRightAlignLeft = null;
    private HSSFCellStyle styleMontantNoBorder = null;
    private HSSFCellStyle styleTotalLineAllBorder = null;
    private HSSFCellStyle styleTotalLineNoBorderLeftRight = null;

    private HSSFCellStyle styleTotalLineNoBorderRight = null;

    private HSSFCellStyle styleTotalLineNoBorderRightAlignLeft = null;

    /**
     * @param session
     * @param process
     */
    public CAListSoldesParCompteCourantParser(BSession session, BProcess process) {
        super(session, "CAListSoldesParCompteCourantParser", session
                .getLabel("IMPRESSION_LISTE_SOLDE_PAR_COMPTE_COURANT"));
        this.process = process;
    }

    /**
     * Ajout d'une ligne pour l'affilié. Par section.
     * 
     * @param compteCourantsList
     * @param idExterneRole
     * @param idExterneRoleNotPrinted
     * @param totalParCompteCourant
     * @param idExterneSection
     * @param compteCourants
     * @return
     */
    private boolean addLineAffilie(ArrayList<String> compteCourantsList, String idExterneRole, String descriptionCpte,
            boolean idExterneRoleNotPrinted, TreeMap<String, String> totalParCompteCourant, String idExterneSection,
            TreeMap<String, String> compteCourants) {
        createRow();

        if (idExterneRoleNotPrinted) {
            this.createCell(idExterneRole, getStyleBorderLeftRightAlignRight());
            this.createCell(descriptionCpte, getStyleBorderLeftRightAlignLeft());
            idExterneRoleNotPrinted = false;
        } else {
            this.createCell("", getStyleBorderLeftRightAlignRight());
            this.createCell("", getStyleBorderLeftRightAlignRight());
        }

        this.createCell(idExterneSection, getStyleBorderLeftRightAlignCenter());

        FWCurrency totalLine = new FWCurrency();
        for (int j = 0; j < compteCourantsList.size(); j++) {
            FWCurrency montant = new FWCurrency("" + compteCourants.get(compteCourantsList.get(j)));

            totalLine.add(montant);

            if (!montant.isZero()) {
                this.createCell(JadeStringUtil.parseDouble("" + compteCourants.get(compteCourantsList.get(j)), 0),
                        getStyleMontantNoBorder());
            } else {
                this.createCell("", getStyleMontantNoBorder());
            }

            if (totalParCompteCourant.containsKey(compteCourantsList.get(j))) {
                FWCurrency montantParCompteCourant = new FWCurrency(""
                        + totalParCompteCourant.get(compteCourantsList.get(j)));
                montantParCompteCourant.add("" + compteCourants.get(compteCourantsList.get(j)));
                totalParCompteCourant.put(compteCourantsList.get(j), montantParCompteCourant.toString());
            } else {
                totalParCompteCourant.put(compteCourantsList.get(j), compteCourants.get(compteCourantsList.get(j)));
            }
        }

        this.createCell(JadeStringUtil.parseDouble(totalLine.toString(), 0),
                getStyleBorderLeftRightAlignRightNoBackground());

        return idExterneRoleNotPrinted;
    }

    /**
     * Ajout du total des comptes courants pour 1 affilié.
     * 
     * @param idExterneRole
     * @param totalParCompteCourant
     * @param totalParCompteCourantAllAffilies
     */
    private void addLineAffilieSubTotal(String idExterneRole, String descriptionCpte,
            TreeMap<String, String> totalParCompteCourant, TreeMap<String, String> totalParCompteCourantAllAffilies) {
        createRow();
        this.createCell(getSession().getLabel(CAListSoldesParCompteCourantParser.LABEL_TOTAL) + " " + idExterneRole,
                getStyleTotalLineNoBorderRightAlignLeft());
        this.createCell(descriptionCpte, getStyleTotalLineNoBorderRightAlignLeft());
        this.createCell("", getStyleTotalLineNoBorderLeftRight());

        FWCurrency montantTotal = new FWCurrency();
        boolean styleForFirstColumnUsed = false;
        Iterator<String> e3 = totalParCompteCourant.keySet().iterator();
        while (e3.hasNext()) {
            String key = "" + e3.next();
            String montantTotalParCompteCourant = totalParCompteCourant.get(key);

            if (!styleForFirstColumnUsed) {
                this.createCell(JadeStringUtil.parseDouble(montantTotalParCompteCourant, 0),
                        getStyleTotalLineNoBorderRight());
                styleForFirstColumnUsed = true;
            } else {
                this.createCell(JadeStringUtil.parseDouble(montantTotalParCompteCourant, 0),
                        getStyleTotalLineNoBorderLeftRight());
            }

            if (totalParCompteCourantAllAffilies.containsKey(key)) {
                FWCurrency montantParCompteCourantTotal = new FWCurrency("" + totalParCompteCourantAllAffilies.get(key));
                montantParCompteCourantTotal.add(montantTotalParCompteCourant);
                totalParCompteCourantAllAffilies.put(key, montantParCompteCourantTotal.toString());
            } else {
                totalParCompteCourantAllAffilies.put(key, montantTotalParCompteCourant);
            }

            montantTotal.add(montantTotalParCompteCourant);
        }

        this.createCell(JadeStringUtil.parseDouble(montantTotal.toString(), 0), getStyleTotalLineAllBorder());
    }

    /**
     * Ajout d'un total par compte courant pour tous les affiliés.
     * 
     * @param totalParCompteCourantAllAffilies
     */
    private void addLineTotal(TreeMap<String, String> totalParCompteCourantAllAffilies) {
        createRow();
        this.createCell(CAListSoldesParCompteCourantParser.LABEL_TOTAL, getStyleFinalTotalLineNoBorderRightAlignLeft());
        this.createCell("", getStyleFinalTotalLineNoBorderLeftRight());
        this.createCell("", getStyleFinalTotalLineNoBorderLeftRight());

        FWCurrency montantTotal = new FWCurrency();
        boolean styleForFirstColumnUsed = false;
        Iterator<String> e3 = totalParCompteCourantAllAffilies.keySet().iterator();
        while (e3.hasNext()) {
            String key = "" + e3.next();
            String montantTotalParCompteCourant = totalParCompteCourantAllAffilies.get(key);

            if (!styleForFirstColumnUsed) {
                this.createCell(JadeStringUtil.parseDouble(montantTotalParCompteCourant, 0),
                        getStyleFinalTotalLineNoBorderRight());
                styleForFirstColumnUsed = true;
            } else {
                this.createCell(JadeStringUtil.parseDouble(montantTotalParCompteCourant, 0),
                        getStyleFinalTotalLineNoBorderLeftRight());
            }

            montantTotal.add(montantTotalParCompteCourant);
        }

        this.createCell(JadeStringUtil.parseDouble(montantTotal.toString(), 0), getStyleFinalTotalLineAllBorder());
    }

    /**
     * Création du contenu du tableau.
     * 
     * @param compteCourantsList
     * @param affilies
     */
    private void createSheetPresentation(ArrayList<String> compteCourantsList,
            TreeMap<String, SectionsCompteAnnexe> affilies) {
        if (affilies.size() > 0) {
            initListe(compteCourantsList);

            TreeMap<String, String> totalParCompteCourantAllAffilies = new TreeMap<String, String>();
            Iterator<String> e = affilies.keySet().iterator();
            while (e.hasNext() && !process.isAborted()) {
                String idExterneRole = "" + e.next();
                boolean idExterneRoleNotPrinted = true;
                TreeMap<String, TreeMap<String, String>> sections = affilies.get(idExterneRole).sections;

                TreeMap<String, String> totalParCompteCourant = new TreeMap<String, String>();
                Iterator<String> e2 = sections.keySet().iterator();
                while (e2.hasNext() && !process.isAborted()) {
                    String idExterneSection = "" + e2.next();
                    TreeMap<String, String> compteCourants = sections.get(idExterneSection);

                    idExterneRoleNotPrinted = addLineAffilie(compteCourantsList, idExterneRole,
                            affilies.get(idExterneRole).compteAnnexe, idExterneRoleNotPrinted, totalParCompteCourant,
                            idExterneSection, compteCourants);
                }

                addLineAffilieSubTotal(idExterneRole, affilies.get(idExterneRole).compteAnnexe, totalParCompteCourant,
                        totalParCompteCourantAllAffilies);

                process.incProgressCounter();
            }

            addLineTotal(totalParCompteCourantAllAffilies);
        }
    }

    /**
     * Retourne un TreeMap contenant les affiliés qui contient un treemap pour ces sections qui contient un treemap pour
     * les comptes courants.
     * 
     * @param manager
     * @param compteCourantsList
     * @return
     */
    private TreeMap<String, SectionsCompteAnnexe> getAffiliesSorted(CASoldesParCompteCourantManager manager,
            ArrayList<String> compteCourantsList) {
        TreeMap<String, SectionsCompteAnnexe> affilies = new TreeMap<String, SectionsCompteAnnexe>();
        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CASoldesParCompteCourant compte = (CASoldesParCompteCourant) manager.get(i);

            if (affilies.containsKey(compte.getIdExterneRole())) {
                TreeMap<String, TreeMap<String, String>> sections = affilies.get(compte.getIdExterneRole()).sections;

                if (sections.containsKey(compte.getIdExterneSection())) {
                    TreeMap<String, String> compteCourants = sections.get(compte.getIdExterneSection());

                    FWCurrency newMontant = new FWCurrency("" + compteCourants.get(compte.getIdExterneCompteCourant()));
                    newMontant.add(compte.getMontant());
                    compteCourants.put(compte.getIdExterneCompteCourant(), newMontant.toString());
                } else {
                    TreeMap<String, String> compteCourants = new TreeMap<String, String>();

                    for (int j = 0; (j < compteCourantsList.size()) && !process.isAborted(); j++) {
                        if (compte.getIdExterneCompteCourant().equals("" + compteCourantsList.get(j))) {
                            compteCourants.put(compteCourantsList.get(j), compte.getMontant());
                        } else {
                            compteCourants.put(compteCourantsList.get(j), "0.00");
                        }
                    }

                    sections.put(compte.getIdExterneSection(), compteCourants);
                }
            } else {
                SectionsCompteAnnexe sectionsCpte = new SectionsCompteAnnexe();
                TreeMap<String, String> compteCourants = new TreeMap<String, String>();

                for (int j = 0; (j < compteCourantsList.size()) && !process.isAborted(); j++) {
                    if (compte.getIdExterneCompteCourant().equals("" + compteCourantsList.get(j))) {
                        compteCourants.put(compteCourantsList.get(j), compte.getMontant());
                    } else {
                        compteCourants.put(compteCourantsList.get(j), "0.00");
                    }
                }

                sectionsCpte.sections.put(compte.getIdExterneSection(), compteCourants);
                sectionsCpte.compteAnnexe = compte.getDescription();
                affilies.put(compte.getIdExterneRole(), sectionsCpte);
            }
            process.incProgressCounter();
        }
        return affilies;
    }

    /**
     * Retourne la liste complète (ordrée) des comptes courants que l'on va trouver dans le résultat.
     * 
     * @param manager
     * @return
     */
    private ArrayList<String> getCompteCourants(CASoldesParCompteCourantManager manager) {
        ArrayList<String> compteCourantsList = new ArrayList<String>();
        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CASoldesParCompteCourant compte = (CASoldesParCompteCourant) manager.get(i);

            if (!compteCourantsList.contains(compte.getIdExterneCompteCourant())) {
                compteCourantsList.add(compte.getIdExterneCompteCourant());
            }
            process.incProgressCounter();
        }
        Collections.sort(compteCourantsList);
        return compteCourantsList;
    }

    @Override
    public String getNumeroInforom() {
        return CAListSoldesParCompteCourantParser.NUMERO_REFERENCE_INFOROM;
    }

    private HSSFCellStyle getStyleBorderLeftRightAlignCenter() {
        if (styleBorderLeftRightAlignCenter == null) {
            styleBorderLeftRightAlignCenter = getWorkbook().createCellStyle();
            styleBorderLeftRightAlignCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            styleBorderLeftRightAlignCenter.setBorderBottom(HSSFCellStyle.BORDER_NONE);
            styleBorderLeftRightAlignCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignCenter.setBorderTop(HSSFCellStyle.BORDER_NONE);

            styleBorderLeftRightAlignCenter.setWrapText(true);
        }
        return styleBorderLeftRightAlignCenter;
    }

    private HSSFCellStyle getStyleBorderLeftRightAlignLeft() {
        if (styleBorderLeftRightAlignLeft == null) {
            styleBorderLeftRightAlignLeft = getWorkbook().createCellStyle();
            styleBorderLeftRightAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            styleBorderLeftRightAlignLeft.setBorderBottom(HSSFCellStyle.BORDER_NONE);
            styleBorderLeftRightAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignLeft.setBorderTop(HSSFCellStyle.BORDER_NONE);

            styleBorderLeftRightAlignLeft.setFillBackgroundColor(HSSFColor.WHITE.index);
            styleBorderLeftRightAlignLeft.setFillForegroundColor(HSSFColor.WHITE.index);
            styleBorderLeftRightAlignLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            styleBorderLeftRightAlignLeft.setWrapText(true);
        }
        return styleBorderLeftRightAlignLeft;
    }

    private HSSFCellStyle getStyleBorderLeftRightAlignRight() {
        if (styleBorderLeftRightAlignRight == null) {
            styleBorderLeftRightAlignRight = getWorkbook().createCellStyle();
            styleBorderLeftRightAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleBorderLeftRightAlignRight.setBorderBottom(HSSFCellStyle.BORDER_NONE);
            styleBorderLeftRightAlignRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignRight.setBorderTop(HSSFCellStyle.BORDER_NONE);

            styleBorderLeftRightAlignRight.setFillBackgroundColor(HSSFColor.WHITE.index);
            styleBorderLeftRightAlignRight.setFillForegroundColor(HSSFColor.WHITE.index);
            styleBorderLeftRightAlignRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            styleBorderLeftRightAlignRight.setWrapText(true);
        }
        return styleBorderLeftRightAlignRight;
    }

    private HSSFCellStyle getStyleBorderLeftRightAlignRightNoBackground() {
        if (styleBorderLeftRightAlignRightNoBg == null) {
            styleBorderLeftRightAlignRightNoBg = getWorkbook().createCellStyle();
            styleBorderLeftRightAlignRightNoBg.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            styleBorderLeftRightAlignRightNoBg.setBorderBottom(HSSFCellStyle.BORDER_NONE);
            styleBorderLeftRightAlignRightNoBg.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignRightNoBg.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleBorderLeftRightAlignRightNoBg.setBorderTop(HSSFCellStyle.BORDER_NONE);

            styleBorderLeftRightAlignRightNoBg.setWrapText(true);
        }
        return styleBorderLeftRightAlignRightNoBg;
    }

    private HSSFCellStyle getStyleFinalTotalLineAllBorder() {
        if (styleFinalTotalLineAllBorder == null) {
            styleFinalTotalLineAllBorder = getWorkbook().createCellStyle();
            styleFinalTotalLineAllBorder.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleFinalTotalLineAllBorder.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleFinalTotalLineAllBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineAllBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineAllBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineAllBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleFinalTotalLineAllBorder.setFont(getFontBold());

            styleFinalTotalLineAllBorder.setWrapText(true);
        }
        return styleFinalTotalLineAllBorder;
    }

    private HSSFCellStyle getStyleFinalTotalLineNoBorderLeftRight() {
        if (styleFinalTotalLineNoBorderLeftRight == null) {
            styleFinalTotalLineNoBorderLeftRight = getWorkbook().createCellStyle();
            styleFinalTotalLineNoBorderLeftRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleFinalTotalLineNoBorderLeftRight.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleFinalTotalLineNoBorderLeftRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineNoBorderLeftRight.setBorderLeft(HSSFCellStyle.BORDER_NONE);
            styleFinalTotalLineNoBorderLeftRight.setBorderRight(HSSFCellStyle.BORDER_NONE);
            styleFinalTotalLineNoBorderLeftRight.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleFinalTotalLineNoBorderLeftRight.setFont(getFontBold());

            styleFinalTotalLineNoBorderLeftRight.setWrapText(true);
        }
        return styleFinalTotalLineNoBorderLeftRight;
    }

    private HSSFCellStyle getStyleFinalTotalLineNoBorderRight() {
        if (styleFinalTotalLineNoBorderRight == null) {
            styleFinalTotalLineNoBorderRight = getWorkbook().createCellStyle();
            styleFinalTotalLineNoBorderRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleFinalTotalLineNoBorderRight.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleFinalTotalLineNoBorderRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineNoBorderRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineNoBorderRight.setBorderRight(HSSFCellStyle.BORDER_NONE);
            styleFinalTotalLineNoBorderRight.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleFinalTotalLineNoBorderRight.setFont(getFontBold());

            styleFinalTotalLineNoBorderRight.setWrapText(true);
        }
        return styleFinalTotalLineNoBorderRight;
    }

    private HSSFCellStyle getStyleFinalTotalLineNoBorderRightAlignLeft() {
        if (styleFinalTotalLineNoBorderRightAlignLeft == null) {
            styleFinalTotalLineNoBorderRightAlignLeft = getWorkbook().createCellStyle();
            styleFinalTotalLineNoBorderRightAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleFinalTotalLineNoBorderRightAlignLeft.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleFinalTotalLineNoBorderRightAlignLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineNoBorderRightAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleFinalTotalLineNoBorderRightAlignLeft.setBorderRight(HSSFCellStyle.BORDER_NONE);
            styleFinalTotalLineNoBorderRightAlignLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleFinalTotalLineNoBorderRightAlignLeft.setFont(getFontBold());

            styleFinalTotalLineNoBorderRightAlignLeft.setWrapText(true);
        }

        return styleFinalTotalLineNoBorderRightAlignLeft;
    }

    private HSSFCellStyle getStyleMontantNoBorder() {
        if (styleMontantNoBorder == null) {
            styleMontantNoBorder = getWorkbook().createCellStyle();
            styleMontantNoBorder.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleMontantNoBorder.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleMontantNoBorder.setBorderBottom(HSSFCellStyle.BORDER_NONE);
            styleMontantNoBorder.setBorderLeft(HSSFCellStyle.BORDER_NONE);
            styleMontantNoBorder.setBorderRight(HSSFCellStyle.BORDER_NONE);
            styleMontantNoBorder.setBorderTop(HSSFCellStyle.BORDER_NONE);

            styleMontantNoBorder.setWrapText(true);
        }
        return styleMontantNoBorder;
    }

    private HSSFCellStyle getStyleTotalLineAllBorder() {
        if (styleTotalLineAllBorder == null) {
            styleTotalLineAllBorder = getWorkbook().createCellStyle();
            styleTotalLineAllBorder.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleTotalLineAllBorder.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleTotalLineAllBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleTotalLineAllBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleTotalLineAllBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);
            styleTotalLineAllBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleTotalLineAllBorder.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineAllBorder.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineAllBorder.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            styleTotalLineAllBorder.setFont(getFontBold());

            styleTotalLineAllBorder.setWrapText(true);
        }
        return styleTotalLineAllBorder;
    }

    private HSSFCellStyle getStyleTotalLineNoBorderLeftRight() {
        if (styleTotalLineNoBorderLeftRight == null) {
            styleTotalLineNoBorderLeftRight = getWorkbook().createCellStyle();
            styleTotalLineNoBorderLeftRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleTotalLineNoBorderLeftRight.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleTotalLineNoBorderLeftRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleTotalLineNoBorderLeftRight.setBorderLeft(HSSFCellStyle.BORDER_NONE);
            styleTotalLineNoBorderLeftRight.setBorderRight(HSSFCellStyle.BORDER_NONE);
            styleTotalLineNoBorderLeftRight.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleTotalLineNoBorderLeftRight.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineNoBorderLeftRight.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineNoBorderLeftRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            styleTotalLineNoBorderLeftRight.setFont(getFontBold());

            styleTotalLineNoBorderLeftRight.setWrapText(true);
        }
        return styleTotalLineNoBorderLeftRight;
    }

    private HSSFCellStyle getStyleTotalLineNoBorderRight() {
        if (styleTotalLineNoBorderRight == null) {
            styleTotalLineNoBorderRight = getWorkbook().createCellStyle();
            styleTotalLineNoBorderRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            styleTotalLineNoBorderRight.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleTotalLineNoBorderRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleTotalLineNoBorderRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleTotalLineNoBorderRight.setBorderRight(HSSFCellStyle.BORDER_NONE);
            styleTotalLineNoBorderRight.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleTotalLineNoBorderRight.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineNoBorderRight.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineNoBorderRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            styleTotalLineNoBorderRight.setFont(getFontBold());

            styleTotalLineNoBorderRight.setWrapText(true);
        }
        return styleTotalLineNoBorderRight;
    }

    private HSSFCellStyle getStyleTotalLineNoBorderRightAlignLeft() {
        if (styleTotalLineNoBorderRightAlignLeft == null) {
            styleTotalLineNoBorderRightAlignLeft = getWorkbook().createCellStyle();
            styleTotalLineNoBorderRightAlignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styleTotalLineNoBorderRightAlignLeft.setDataFormat(CAAbstractListExcel.FORMAT_MONTANT);

            styleTotalLineNoBorderRightAlignLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            styleTotalLineNoBorderRightAlignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            styleTotalLineNoBorderRightAlignLeft.setBorderRight(HSSFCellStyle.BORDER_NONE);
            styleTotalLineNoBorderRightAlignLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);

            styleTotalLineNoBorderRightAlignLeft.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineNoBorderRightAlignLeft.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            styleTotalLineNoBorderRightAlignLeft.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            styleTotalLineNoBorderRightAlignLeft.setFont(getFontBold());

            styleTotalLineNoBorderRightAlignLeft.setWrapText(true);
        }

        return styleTotalLineNoBorderRightAlignLeft;
    }

    /**
     * Création de la page, ajout du titres des colonnes.
     * 
     * @return
     */
    private void initListe(ArrayList<String> compteCourantsList) {
        createSheet(getSession().getLabel(CAListSoldesParCompteCourantParser.LABEL_LISTE));

        ArrayList<ParamTitle> titles = new ArrayList<ParamTitle>();
        titles.add(new ParamTitle(getSession().getLabel(CAListSoldesParCompteCourantParser.LABEL_AFFILIE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSoldesParCompteCourantParser.LABEL_DESCRIPTION)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSoldesParCompteCourantParser.LABEL_FACTURE)));
        for (Iterator<String> it = compteCourantsList.iterator(); it.hasNext() && !process.isAborted();) {
            titles.add(new ParamTitle(it.next()));
        }
        titles.add(new ParamTitle(getSession().getLabel(CAListSoldesParCompteCourantParser.LABEL_TOTAL)));

        initTitleRow(titles);
        initPage(true);
        createHeader();
        createFooter(CAListSoldesParCompteCourantParser.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) CAListSoldesParCompteCourantParser.COLUMN_SIZE_AFILIE);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_COMPTEANNEXE);
        currentSheet.setColumnWidth((short) numCol++, (short) CAListSoldesParCompteCourantParser.COLUMN_SIZE_FACTURE);

        for (int i = 0; (i < compteCourantsList.size()) && !process.isAborted(); i++) {
            currentSheet.setColumnWidth((short) numCol++,
                    (short) CAListSoldesParCompteCourantParser.COLUMN_SIZE_COMPTE_COURANT);
        }

        currentSheet.setColumnWidth((short) numCol++,
                (short) CAListSoldesParCompteCourantParser.COLUMN_SIZE_COMPTE_TOTAL);
    }

    /**
     * Initialisation de la feuille xls.
     * 
     * @param manager
     * @param transaction
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(CASoldesParCompteCourantManager manager, BTransaction transaction)
            throws Exception {
        ArrayList<String> compteCourantsList = null;
        TreeMap<String, SectionsCompteAnnexe> affilies = null;

        manager.setSession(getSession());
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        process.setProgressScaleValue(manager.size() * 3);

        if (!process.isAborted()) {
            compteCourantsList = getCompteCourants(manager);
        }
        if (!process.isAborted()) {
            affilies = getAffiliesSorted(manager, compteCourantsList);
        }
        if (!process.isAborted()) {
            createSheetPresentation(compteCourantsList, affilies);
        }
        return currentSheet;
    }
}
