package globaz.osiris.db.ventilation;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIVPDetailMontant;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.print.list.CAAbstractListExcel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author jmc Créé le 8 déc. 05
 * @modified by sel le 21.11.2007
 * @modified by nch le 10.07.2008
 */

public class CAVPDocumentImpression extends CAAbstractListExcel {
    /* taille des polices (en 1/20 de point) */
    private static final short COLUMN_WIDTH_DATE = 2200;
    /* taille par défaut (en point) */
    private static final short COLUMN_WIDTH_DEFAULT = 15;
    private static final short COLUMN_WIDTH_DESCRIPTION = 5000;
    private static final short COLUMN_WIDTH_SECTION = 2500;
    private static final short COLUMN_WIDTH_TOTAL = 3800;
    private static final String FILE_NAME = "ventilation";
    private static final double MONTANT_ZERO = 0.00;
    private static final int NUMCOLSTART = 2;
    private static final String NUMERO_REFERENCE_INFOROM = "0163GCA";
    private static final String ORDRE_MAX = "99999999";
    private static final short ROW_FROM = (short) 3;
    private static final short ROW_TO = (short) 4;
    private static final String SUM = "SUM";

    private String colDouble = "";
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private String idCompteAnnexe = "";
    private boolean isLibelleSolde = false;
    private int ligneCouvert = 0;
    private int ligneMontant = 0;
    private int ligneTotal = 0;
    private HashMap<String, ArrayList<CAVPDetailSection>> listVentilationSection = null;
    private int longueurCol = 0;
    private int nbCol = 0;
    private int nbrSection = 0;
    private int nbrSectionActu = 0;
    private int rowEcritureEnd = 0;
    private int rowEcritureStart = 0;
    private int rowRecapStart = 0;
    private HashMap<?, ?> rubriquesPresentes = null;
    private String section = "";
    private CAVPVentilateur ventilateur = null;
    private Boolean VentilationGlobale = null;

    /**
     * @param documentTitle
     * @param session
     * @param ventilateur
     */
    public CAVPDocumentImpression(String documentTitle, BSession session, CAVPVentilateur ventilateur) {
        super(session, CAVPDocumentImpression.FILE_NAME, documentTitle);
        rubriquesPresentes = new HashMap<Object, Object>();
        listVentilationSection = new HashMap<String, ArrayList<CAVPDetailSection>>();

        this.ventilateur = ventilateur;
        setFontHeight((short) 8);
        createSheet(getSession().getLabel("VENTILATION_RECAPITULATIF"));
        setBorder(true);
        initPage(true);
        createFooter(CAVPDocumentImpression.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Rempli les montants des sections de la feuille de récapitulation
     * 
     * @param CAVPListePosteParSection
     *            listePostesSection
     * @param String
     *            key
     * @param HashMap
     *            info
     */
    private void _fillSection(CAVPListePosteParSection listePostesSection, String key, Map<String, String> info) {
        // set si montant ventile
        boolean ventile = false;
        // colonne de départ
        int colStart = 0;
        // total des colonnes
        BigDecimal total = new BigDecimal(CAVPDocumentImpression.MONTANT_ZERO);
        nbrSectionActu++;
        // Iterer les section présentes
        rubriquesPresentes = ventilateur.getRubriquesPresentesMap();
        createRow();

        if (CAVPVentilateur.KEY_POSTE_TOTAL.equals(key)) {
            // ajout libellé total
            setColNum(2);
            this.createCell(key, getStyleListLeft());
        } else {
            // ajout de la description, du numéro de section et de la date
            String id = (String) JadeStringUtil.split(info.get(key), "/").get(0);
            String description = (String) JadeStringUtil.split(info.get(key), "/").get(1);
            this.createCell(JadeStringUtil.remove(id, id.length() - 1, 1), getStyleListLeft());
            this.createCell(description, getStyleListLeft());
            try {
                JADate date = new JADate(new BigDecimal(key.substring(0, 8)));
                this.createCell(JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY), getStyleListLeft());
            } catch (JAException e) {
                e.printStackTrace();
            }
        }
        // contient le numéro de la colonne de départ
        colStart = getColNum();
        // liste des montants ventilés
        ArrayList<CAVPDetailSection> tabListVentilationSection = new ArrayList<CAVPDetailSection>();
        // trie les rubriques
        Iterator<?> it = sortMap(rubriquesPresentes);

        // Ajoute les montants
        while (it.hasNext()) {
            // Traiter differemment les montants simples des autres
            String idRubrique = ((CAVPDetailRubriqueSection) it.next()).getIdRubrique();
            if (listePostesSection.containsKey(idRubrique) && (nbrSectionActu < nbrSection)) {
                if (((Boolean) rubriquesPresentes.get(idRubrique)).booleanValue()
                        && !CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(idRubrique)) {
                    CAVPPoste posteBase = listePostesSection.getPoste(idRubrique);
                    BigDecimal montantCell = new BigDecimal(CAVPDocumentImpression.MONTANT_ZERO);
                    montantCell = posteBase
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE)
                            .getMontantBase()
                            .subtract(
                                    posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE)
                                            .getMontantVentile());
                    total = total.add(montantCell);
                    this.createCell(montantCell.doubleValue(), getStyleMontant());
                    if (posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantVentile()
                            .compareTo(new BigDecimal("0")) <= 0) {
                        ventile = true;
                    } else {
                        ventile = false;
                    }
                    tabListVentilationSection.add(getFinalLineDetail(idRubrique,
                            APIVPDetailMontant.CS_VP_MONTANT_SIMPLE, montantCell, ventile));

                } else if (!CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(idRubrique)) {
                    CAVPPoste posteBase = listePostesSection.getPoste(idRubrique);
                    BigDecimal montantCell = new BigDecimal(CAVPDocumentImpression.MONTANT_ZERO);
                    montantCell = posteBase
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)
                            .getMontantBase()
                            .subtract(
                                    posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR)
                                            .getMontantVentile());
                    total = total.add(montantCell);
                    this.createCell(montantCell.doubleValue(), getStyleMontant());

                    if (posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR).getMontantVentile()
                            .compareTo(new BigDecimal("0")) <= 0) {
                        ventile = true;
                    } else {
                        ventile = false;
                    }
                    tabListVentilationSection.add(getFinalLineDetail(idRubrique,
                            APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR, montantCell, ventile));

                    montantCell = new BigDecimal(CAVPDocumentImpression.MONTANT_ZERO);
                    montantCell = posteBase
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE)
                            .getMontantBase()
                            .subtract(
                                    posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE)
                                            .getMontantVentile());
                    total = total.add(montantCell);
                    this.createCell(montantCell.doubleValue(), getStyleMontant());

                    if (posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE).getMontantVentile()
                            .compareTo(new BigDecimal("0")) <= 0) {
                        ventile = true;
                    } else {
                        ventile = false;
                    }
                    tabListVentilationSection.add(getFinalLineDetail(idRubrique,
                            APIVPDetailMontant.CS_VP_MONTANT_SALARIE, montantCell, ventile));

                }
            } else if (!CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(idRubrique)) {
                if (isMergeCol(Integer.toString(getColNum()))) {
                    if (nbrSectionActu < nbrSection) {
                        this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                        this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                        tabListVentilationSection.add(getFinalLineDetail(idRubrique,
                                APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR, new BigDecimal(0), true));
                        tabListVentilationSection.add(getFinalLineDetail(idRubrique,
                                APIVPDetailMontant.CS_VP_MONTANT_SALARIE, new BigDecimal(0), true));
                    } else {
                        String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(getColNum()) + rowRecapStart
                                + ":" + getColAlpha(getColNum()) + (rowRecapStart + (nbrSection - 2)) + ")";
                        createCellFormula(formule, getStyleMontant());
                        formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(getColNum()) + rowRecapStart + ":"
                                + getColAlpha(getColNum()) + (rowRecapStart + (nbrSection - 2)) + ")";
                        createCellFormula(formule, getStyleMontant());
                    }
                } else {
                    if (nbrSectionActu < nbrSection) {
                        this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                        tabListVentilationSection.add(getFinalLineDetail(idRubrique,
                                APIVPDetailMontant.CS_VP_MONTANT_SIMPLE, new BigDecimal(0), true));
                    } else {
                        String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(getColNum()) + rowRecapStart
                                + ":" + getColAlpha(getColNum()) + (rowRecapStart + (nbrSection - 2)) + ")";
                        createCellFormula(formule, getStyleMontant());
                    }
                }
            }
        }

        listVentilationSection.put(key, tabListVentilationSection);

        // ajoute le montant à ventiler
        CAVPPoste posteBase = listePostesSection.getPoste(CAVPVentilateur.KEY_POSTE_A_VENTILER);
        BigDecimal montantCell = new BigDecimal(CAVPDocumentImpression.MONTANT_ZERO);
        if (posteBase != null) {
            montantCell = posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE).getMontantBase();
        }
        if (nbrSectionActu < nbrSection) {
            this.createCell(montantCell.doubleValue(), getStyleMontant());
        } else {
            // ajoute la formule excel dans le cas de la dernière ligne de total
            String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(getColNum()) + rowRecapStart + ":"
                    + getColAlpha(getColNum()) + (rowRecapStart + (nbrSection - 2)) + ")";
            createCellFormula(formule, getStyleMontant());
        }

        // ajoute le montant restant
        if (posteBase != null) {
            montantCell = montantCell.subtract(posteBase.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE)
                    .getMontantVentile());
        }
        if (nbrSectionActu < nbrSection) {
            this.createCell(montantCell.doubleValue(), getStyleMontant());
        } else {
            // ajoute la formule excel dans le cas de la dernière ligne de total
            String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(getColNum()) + rowRecapStart + ":"
                    + getColAlpha(getColNum()) + (rowRecapStart + (nbrSection - 2)) + ")";
            createCellFormula(formule, getStyleMontant());
        }

        // ajoute le total
        if (nbrSectionActu < nbrSection) {
            String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(colStart)
                    + (getCurrentSheet().getPhysicalNumberOfRows()) + ":" + getColAlpha(getColNum() - 3)
                    + (getCurrentSheet().getPhysicalNumberOfRows()) + ")";
            createCellFormula(formule, getStyleMontant()); // =
            // total.doubleValue()
        } else {
            // ajoute la formule excel dans le cas de la dernière ligne de total
            String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(colStart)
                    + (rowRecapStart + (nbrSection - 1)) + ":" + getColAlpha(getColNum() - 3)
                    + (rowRecapStart + (nbrSection - 1)) + ")" + "-" + getColAlpha(getColNum() - 1)
                    + (rowRecapStart + (nbrSection - 1));
            createCellFormula(formule, getStyleMontant());
        }
        this.createCell("", getStyleMontant());
    }

    /**
     * Ajoute un titre et un nom de rubrique pour chaque colonne de la feuille de récaputilation
     */
    private void _initColonnes() {
        initCritere();
        rubriquesPresentes = ventilateur.getRubriquesPresentesMap();

        int ln1 = createRow();
        int ln2 = createRow();

        // Iterator it = sectionsPresentes.keySet().iterator();
        // trie les rubriques.
        Iterator<?> it = sortMap(rubriquesPresentes);
        this.setCurrentRow(ln1);

        // Ajout les colonnes Section,description et date
        currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_SECTION);
        this.createMergedRegion(
                getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM, CAVPDocumentImpression.ROW_TO),
                this.createCell(getSession().getLabel("VENTILATION_SECTION"), getStyleListTitleVerticalAlignTopCenter()));
        currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_DESCRIPTION);
        this.createMergedRegion(
                getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM, CAVPDocumentImpression.ROW_TO),
                this.createCell(getSession().getLabel("VENTILATION_DESCRIPTION"),
                        getStyleListTitleVerticalAlignTopCenter()));
        currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_DATE);
        this.createMergedRegion(
                getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM, CAVPDocumentImpression.ROW_TO),
                this.createCell(getSession().getLabel("VENTILATION_DATE"), getStyleListTitleVerticalAlignTopCenter()));

        setColNum(3);

        while (it.hasNext()) {
            // String key = (String) it.next();
            String key = ((CAVPDetailRubriqueSection) it.next()).getIdRubrique();

            if (!CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(key)) {
                // Si c'est pas un montant simple, fusionner les cellules
                if (!((Boolean) rubriquesPresentes.get(key)).booleanValue()) {
                    // sotck les numéros des colonnes ou les cellules sont
                    // fusionnées
                    colDouble += getColNum() + ",";
                    getCurrentSheet().setDefaultColumnWidth(CAVPDocumentImpression.COLUMN_WIDTH_DEFAULT);
                    String libelleRubrique = getLibelleRubrique(key);
                    this.setCurrentRow(ln1);
                    this.createMergedRegion(
                            getRegion(getColNum(), getColNum() + 1, CAVPDocumentImpression.ROW_FROM,
                                    CAVPDocumentImpression.ROW_FROM), this.createCell(libelleRubrique,
                                    getStyleListTitleVerticalAlignTopCenter()));

                    this.setCurrentRow(ln2);
                    setColNum(getColNum() - 1);
                    this.createCell(getSession().getLabel("VENTILATION_EMPLOYEUR"),
                            getStyleListTitleVerticalAlignTopCenter());
                    this.createCell(getSession().getLabel("VENTILATION_SALARIE"),
                            getStyleListTitleVerticalAlignTopCenter());
                } else if (!CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(key)) {
                    String libelleRubrique = getLibelleRubrique(key);
                    this.setCurrentRow(ln1);
                    this.createMergedRegion(
                            getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM,
                                    CAVPDocumentImpression.ROW_TO), this.createCell(libelleRubrique,
                                    getStyleListTitleVerticalAlignTopCenter()));
                }
            }
        }

        this.setCurrentRow(ln1);
        currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_TOTAL);
        this.createMergedRegion(
                getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM, CAVPDocumentImpression.ROW_TO),
                this.createCell(getSession().getLabel("VENTILATION_MONTANT_A_VENTILER"),
                        getStyleListTitleVerticalAlignTopCenter()));
        currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_TOTAL);
        this.createMergedRegion(
                getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM, CAVPDocumentImpression.ROW_TO),
                this.createCell(getSession().getLabel("VENTILATION_MONTANT_A_VENTILER_RESTANT"),
                        getStyleListTitleVerticalAlignTopCenter()));
        currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_TOTAL);
        this.createMergedRegion(
                getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM, CAVPDocumentImpression.ROW_TO),
                this.createCell(getSession().getLabel("VENTILATION_TOTAL"), getStyleListTitleVerticalAlignTopCenter()));
        currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_TOTAL);
        this.createMergedRegion(
                getRegion(getColNum(), getColNum(), CAVPDocumentImpression.ROW_FROM, CAVPDocumentImpression.ROW_TO),
                this.createCell(getSession().getLabel("VENTILATION_IMPROD"), getStyleListTitleVerticalAlignTopCenter()));
        // stocke le nombre de colonne de la feuille excel.
        nbCol = getColNum();
        // stocke le numéro de la première ligne de détails.
        rowRecapStart = (ln2 + 2);
    }

    /**
     * Crée un onglet contenant le détail pour chaque section
     */
    private void addDetailSection() {
        TreeMap<?, ?> tableauFinal = ventilateur.getTableauFinal();
        Iterator<?> itTableau = tableauFinal.keySet().iterator();
        ArrayList<String> listSheet = new ArrayList<String>();
        while (itTableau.hasNext()) {
            isLibelleSolde = true;

            String key = (String) itTableau.next();
            if (!CAVPVentilateur.KEY_POSTE_TOTAL.equals(key)) {
                List<?> myList = JadeStringUtil.split(ventilateur.getInfoSectionMap().get(key), "/");

                // contient le numéro de la section en cours
                String id = (String) myList.get(0);
                // contient le total du montant à ventiler
                BigDecimal montantAVentiler = ventilateur.getListMontantBaseVentilationSectionMap().get(key);
                // contient les différents tableau de structure.
                ArrayList<?> listRubrique = (ArrayList<?>) ventilateur.getListRubriqueDetailSectionMap().get(key);
                Iterator<?> itListRubriqueDetail = listRubrique.iterator();
                ArrayList<?> listDetail = ventilateur.getListDetailSectionMap().get(key);
                Iterator<?> itListDetail = listDetail.iterator();
                ArrayList<?> listVentilation = listVentilationSection.get(key);

                if (montantAVentiler == null) {
                    montantAVentiler = new BigDecimal(0);
                }

                // récupère le numéro de section
                section = JadeStringUtil.remove(id, id.length() - 1, 1);
                // récupère la date du début de la période.
                dateDebutPeriode = (String) myList.get(2);
                // mise en forme de la date de début.
                dateDebutPeriode = JadeStringUtil.remove(dateDebutPeriode, dateDebutPeriode.length() - 1, 1);
                // récupère la date de fin de la période.
                if (myList.size() > 3) {
                    dateFinPeriode = (String) myList.get(3);
                } else {
                    dateFinPeriode = "";
                }

                // crée un nouvel onglet(titre = numéro de section) - Incrémenter pour le cas ou la section est
                // plusieurs fois dans la ventilation afin d'éviter une erreur page existante (tests campagne 2 de la
                // version 1.14.00
                int incrementOnglet = 0;
                String sectionPourOnglet = section;
                while (listSheet.contains(sectionPourOnglet)) {
                    sectionPourOnglet = section + " - " + incrementOnglet;
                    incrementOnglet++;
                }
                HSSFSheet sheet = createSheet(sectionPourOnglet);
                listSheet.add(sectionPourOnglet);

                // set les propriétés de l'onglet
                setDetailPageProperties(sheet);
                // ajoute l'entête ainsi que les libellés de l'onglet
                setDetailPageHead(itListRubriqueDetail);
                // sauvegarde le numéro de la première ligne des écritures
                rowEcritureStart = currentSheet.getPhysicalNumberOfRows() + 1;
                // ajoute les écritures
                setDetailSection(itListDetail, listRubrique);
                // sauvegarde le numéro de la dernière colonne.
                longueurCol = getColNum() - 2;
                // sauvegarde le numéro de la dernière ligne des écritures
                rowEcritureEnd = currentSheet.getPhysicalNumberOfRows();
                // remise itérator à zero
                itListRubriqueDetail = listRubrique.iterator();
                // ajoute le solde
                setDetailSolde(itListRubriqueDetail, listVentilation);
                // ajoute
                setDetailPageTotal(montantAVentiler);
            }
        }
    }

    /**
     * Retourne l'idExterneRole du compte annexe.
     * 
     * @return l'idExterneRole du compte annexe.
     */
    public String getCompteAnnexe() {
        CACompteAnnexe compte = new CACompteAnnexe();
        compte.setSession(getSession());
        compte.setIdCompteAnnexe(getIdCompteAnnexe());
        try {
            compte.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return compte.getCARole().getDescription() + " " + compte.getIdExterneRole() + " " + compte.getDescription();
    }

    /**
     * Remplie une structure CAVPDetailSection avec les informations passées en paramètre.
     * 
     * @param String
     *            rubrique : numéro de la rubrique.
     * @param String
     *            type : définit le type du montant, double(employeur/salarie) ou simple.
     * @param BigDecimal
     *            montant
     * @param boolean ventile : définit si le montant est ventilé ou non.
     * @return CAVPDetailSection
     */
    public CAVPDetailSection getFinalLineDetail(String rubrique, String type, BigDecimal montant, boolean ventile) {
        CAVPDetailSection detailSection = new CAVPDetailSection();
        detailSection.setIdRubrique(rubrique);
        detailSection.setVentile(ventile);

        if (JadeStringUtil.equals(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR, type, true)) {
            detailSection.setMontantEmployeur(montant);
            detailSection.setMontantSimple(false);
        } else if (JadeStringUtil.equals(APIVPDetailMontant.CS_VP_MONTANT_SALARIE, type, true)) {
            detailSection.setMontantSalarie(montant);
            detailSection.setMontantSimple(false);
        } else {
            detailSection.setMontant(montant);
            detailSection.setMontantSimple(true);
        }

        return detailSection;
    }

    /**
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @param key
     * @return le libellé de la rubrique ou <code>key</code> en cas d'erreur.
     */
    private String getLibelleRubrique(String key) {
        String libelleRubrique = "";
        if (!CAVPVentilateur.KEY_POSTE_A_VENTILER.equals(key)) {
            CARubrique rubrique = new CARubrique();
            rubrique.setIdRubrique(key);
            try {
                rubrique.setSession(getSession());
                rubrique.retrieve();
                libelleRubrique = rubrique.getDescription(getSession().getIdLangueISO());
            } catch (Exception e) {
                libelleRubrique = key;
            }
        } else {
            libelleRubrique = key;
        }
        return libelleRubrique;
    }

    @Override
    public String getNumeroInforom() {
        return CAVPDocumentImpression.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return the ventilationGlobale
     */
    public Boolean getVentilationGlobale() {
        return VentilationGlobale;
    }

    /**
     * S'affiche au dessus des titres de colonnes.
     */
    private void initCritere() {
        createRow();
        this.createCell(getSession().getLabel("COMPTEANNEXE") + " : " + getCompteAnnexe(), getStyleCritere());
        // createRow();
        createRow();
        if (getVentilationGlobale().booleanValue()) {
            this.createCell(
                    getSession().getLabel("VENTILATION_GLOBALE") + " : "
                            + getSession().getLabel("VENTILATION_GLOBALE_YES"), getStyleCritere());
        } else {
            this.createCell(
                    getSession().getLabel("VENTILATION_GLOBALE") + " : "
                            + getSession().getLabel("VENTILATION_GLOBALE_NO"), getStyleCritere());
        }
        createRow();
    }

    /**
     * Vérifie si la colonne est double (cas employeur/salarie) ou simple
     * 
     * @param String
     *            colActu : colonne actuel
     * @return true si la colonne est double
     */
    public boolean isMergeCol(String colActu) {
        boolean val = false;
        if (colDouble.length() != 0) {
            String[] temp = colDouble.split(",");

            for (int i = 0; i < temp.length; i++) {
                if (JadeStringUtil.equals(temp[i], colActu, true)) {
                    val = true;
                }
            }
        }
        return val;
    }

    /**
     * Crée la feuille de recapitulatiion et les onglets de détails
     * 
     * @param session
     *            la session courante
     */
    public void populateSheet(BSession session) {
        createHeader();

        int rowStart = 0;
        int rowEnd = 0;

        _initColonnes();
        TreeMap<String, CAVPListePosteParSection> tableauAIterer = ventilateur.getTableauFinal();
        Iterator<String> itTableau = tableauAIterer.keySet().iterator();
        nbrSection = tableauAIterer.size();
        // stock le numéro de la première ligne des résultats
        rowStart = currentSheet.getPhysicalNumberOfRows() + 1;
        while (itTableau.hasNext()) {
            String key = itTableau.next();
            _fillSection(tableauAIterer.get(key), key, ventilateur.getInfoSectionMap());
        }
        // stock le numéro de la dernière ligne des résultats
        rowEnd = currentSheet.getPhysicalNumberOfRows();

        createRow();
        setColNum(nbCol - 2);
        // cellule qui contient le total de la collone IMPROD
        String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(nbCol - 1) + rowStart + ":"
                + getColAlpha(nbCol - 1) + rowEnd + ")";
        createCellFormula(formule, getStyleMontant());

        createRow();
        this.createCell(getSession().getLabel("VENTILATION_PART_PENAL"), getStyleListTitleLeft());
        this.createCell(ventilateur.getPartPenal().doubleValue(), getStyleMontantTotal());

        setColNum(nbCol - 2);
        // cellule qui contient les totaux horizontaux/verticaux
        formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(nbCol - 2) + rowEnd + ":" + getColAlpha(nbCol - 2)
                + (rowEnd + 1) + ")";
        createCellFormula(formule, getStyleMontant());

        addDetailSection();

    }

    /**
     * Ajoute le titre ainsi que le libellé des rubriques de chaque onglet.
     * 
     * @param Iterator
     *            (CAVPDetailRubriqueSection) itListRubriqueDetail : contient la structure de détail de chaque
     *            rubriques.
     */
    private void setDetailPageHead(Iterator<?> itListRubriqueDetail) {
        boolean vide = false;
        // titre
        this.createCell(getSession().getLabel("COMPTEANNEXE") + " : " + getCompteAnnexe(), getStyleCritere());
        createRow();
        this.createCell(getSession().getLabel("SECTION") + " : " + section + "  " + getSession().getLabel("PERIODE")
                + " : " + dateDebutPeriode + "-" + dateFinPeriode, getStyleCritere());
        createRow();

        int libelle = createRow();
        int titreEmployeur = createRow();

        this.setCurrentRow(libelle);

        if (itListRubriqueDetail.hasNext()) {
            currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_DATE);
            this.createMergedRegion(getRegion(getColNum(), getColNum(), libelle, titreEmployeur), this.createCell(
                    getSession().getLabel("VENTILATION_DATE"), getStyleListTitleVerticalAlignTopCenter()));
            currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_DESCRIPTION);
            this.createMergedRegion(getRegion(getColNum(), getColNum(), libelle, titreEmployeur), this.createCell(
                    getSession().getLabel("VENTILATION_LIBELLE"), getStyleListTitleVerticalAlignTopCenter()));
        } else {
            isLibelleSolde = false;
            vide = true;
        }

        setColNum(CAVPDocumentImpression.NUMCOLSTART);

        while (itListRubriqueDetail.hasNext()) {
            CAVPDetailRubriqueSection libelleRubrique = (CAVPDetailRubriqueSection) itListRubriqueDetail.next();
            if (!libelleRubrique.isMontantSimple()) {
                this.setCurrentRow(libelle);
                this.createMergedRegion(getRegion(getColNum(), getColNum() + 1, libelle, libelle), this.createCell(
                        getLibelleRubrique(libelleRubrique.getIdRubrique()), getStyleListTitleVerticalAlignTopCenter()));
                this.setCurrentRow(titreEmployeur);

                setColNum(getColNum() - 1);
                libelleRubrique.setColEmployeur(getColNum());
                this.createCell(getSession().getLabel("VENTILATION_EMPLOYEUR"),
                        getStyleListTitleVerticalAlignTopCenter());
                libelleRubrique.setColSalarie(getColNum());
                this.createCell(getSession().getLabel("VENTILATION_SALARIE"), getStyleListTitleVerticalAlignTopCenter());
            } else {
                libelleRubrique.setCol(getColNum());
                this.setCurrentRow(libelle);
                this.createMergedRegion(getRegion(getColNum(), getColNum(), libelle, titreEmployeur), this.createCell(
                        getLibelleRubrique(libelleRubrique.getIdRubrique()), getStyleListTitleVerticalAlignTopCenter()));
            }
        }

        // si aucune colonne alors iterator vide donc on affiche rien
        if (!vide) {
            this.setCurrentRow(libelle);
            currentSheet.setColumnWidth((short) getColNum(), CAVPDocumentImpression.COLUMN_WIDTH_TOTAL);
            this.createMergedRegion(getRegion(getColNum(), getColNum(), libelle, titreEmployeur), this.createCell(
                    getSession().getLabel("VENTILATION_TOTAL"), getStyleListTitleVerticalAlignTopCenter()));
        }
    }

    /**
     * set les proprietés du nouvel onglet de détails.
     * 
     * @param HSSFSheet
     *            sheet : onglet de la page excel courante.
     */
    private void setDetailPageProperties(HSSFSheet sheet) {
        setHeader(sheet);
        setFooter(sheet);
        setBorder(true);
        initPage(true);
        setCurrentSheet(sheet);
        setColNum(0);
        this.setCurrentRow(sheet.createRow(0));
        getCurrentSheet().setDefaultColumnWidth(CAVPDocumentImpression.COLUMN_WIDTH_DEFAULT);
    }

    /**
     * Ajoute le tableau des totaux (total à ventiler, total ventilation et total restant) à l'onglet de détail.
     * 
     * @param BigDecimal
     *            montantAVentiler : le total à ventiler
     */
    private void setDetailPageTotal(BigDecimal montantAVentiler) {
        createRow();
        int TotalVentilerRow = createRow();
        int TotalVentilationRow = createRow();
        int TotalRestantRow = createRow();

        this.setCurrentRow(TotalVentilerRow);
        setColNum(1);
        this.createCell(getSession().getLabel("VENTILATION_TOTAL_A_VENTILER"), getStyleListLeft());
        setColNum(2);
        this.createCell(montantAVentiler.doubleValue(), getStyleMontant());

        this.setCurrentRow(TotalVentilationRow);
        setColNum(1);
        this.createCell(getSession().getLabel("VENTILATION_TOTAL_VENTILATION"), getStyleListLeft());
        setColNum(2);
        String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(2) + (rowEcritureEnd + 2) + ":"
                + getColAlpha(longueurCol) + (rowEcritureEnd + 2) + ")";
        createCellFormula(formule, getStyleMontant());

        this.setCurrentRow(TotalRestantRow);
        setColNum(1);
        this.createCell(getSession().getLabel("VENTILATION_TOTAL_RESTANT"), getStyleListLeft());
        setColNum(2);
        formule = getColAlpha(2) + (TotalVentilerRow + 1) + "-" + getColAlpha(2) + (TotalVentilationRow + 1);
        createCellFormula(formule, getStyleMontantTotal());
    }

    /**
     * Ajoute le détail des écritures d'une section.
     * 
     * @param Iterator
     *            (CAVPDetailSection) itListDetail : contient la structure de détail des écritures d'une section.
     * @param ArrayList
     *            (CAVPDetailRubriqueSection) listRubrique : contient la structure de détail de chaque rubriques.
     */
    private void setDetailSection(Iterator<?> itListDetail, ArrayList<?> listRubrique) {
        Iterator<?> itListRubriqueDetail;
        int row = 0;
        while (itListDetail.hasNext()) {
            row = createRow() + 1;
            CAVPDetailSection dSection = (CAVPDetailSection) itListDetail.next();
            this.createCell(dSection.getDate(), getStyleListLeft());
            this.createCell(dSection.getLibelle(), getStyleListLeft());
            itListRubriqueDetail = listRubrique.iterator();
            while (itListRubriqueDetail.hasNext()) {
                CAVPDetailRubriqueSection libelleRubrique = (CAVPDetailRubriqueSection) itListRubriqueDetail.next();

                if (dSection.getIdRubrique().equals(libelleRubrique.getIdRubrique())) {
                    if (dSection.isMontantSimple()) {
                        this.createCell(dSection.getMontant().doubleValue(), getStyleMontant());
                    } else {
                        this.createCell(dSection.getMontantEmployeur().doubleValue(), getStyleMontant());
                        this.createCell(dSection.getMontantSalarie().doubleValue(), getStyleMontant());
                    }
                } else {
                    if (libelleRubrique.isMontantSimple()) {
                        this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                    } else {
                        this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                        this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());

                    }
                }
            }
            String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(CAVPDocumentImpression.NUMCOLSTART) + row
                    + ":" + getColAlpha((getColNum() - 1)) + row + ")";
            createCellFormula(formule, getStyleMontant());
        }
    }

    /**
     * Ajoute les lignes Montant ventilation/Solde après ventilation à l'onglet de détail.
     * 
     * @param Iterator
     *            (CAVPDetailRubriqueSection) itListRubriqueDetail : contient la structure de détail des différentes
     *            rubriques.
     * @param ArrayList
     *            (CAVPDetailSection) listVentilation : contient la structure de détail du solde des montants suite à la
     *            ventilation.
     */
    private void setDetailSolde(Iterator<?> itListRubriqueDetail, ArrayList<?> listVentilation) {
        ligneMontant = createRow();
        ligneCouvert = createRow();
        ligneTotal = createRow();

        // ajoute les libellés Montant ventilation/Solde après ventilation
        if (isLibelleSolde) {
            this.setCurrentRow(ligneMontant);
            this.createCell("");
            this.createCell(getSession().getLabel("VENTILATION_MONTANT"), getStyleListLeft());

            this.setCurrentRow(ligneCouvert);
            setColNum(0);
            this.createCell("");
            this.createCell(getSession().getLabel("VENTILATION_MONTANT_COUVERT"), getStyleListLeft());

            this.setCurrentRow(ligneTotal);
            setColNum(0);
            this.createCell("");
            this.createCell(getSession().getLabel("VENTILATION_SOLDE"), getStyleListLeft());
        }

        // Parcours les rubriques pour y ajouter la somme de la colonne
        // et le solde après ventilation
        while (itListRubriqueDetail.hasNext()) {
            CAVPDetailRubriqueSection libelleRubrique = (CAVPDetailRubriqueSection) itListRubriqueDetail.next();

            for (int i = 0; i < listVentilation.size(); i++) {
                CAVPDetailSection valeur = (CAVPDetailSection) listVentilation.get(i);
                if (valeur.getIdRubrique().equals(libelleRubrique.getIdRubrique())) {

                    // cas simple
                    if (valeur.isMontantSimple()) {
                        // ligne montant ventilation
                        this.setCurrentRow(ligneMontant);
                        setColNum(libelleRubrique.getCol());
                        // vérifife si le total est utilisé pour la ventilation
                        // ou non
                        if (valeur.isVentile()) {
                            this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                            this.setCurrentRow(ligneCouvert);
                            setColNum(libelleRubrique.getCol());
                            this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                        } else {
                            // formule qui additionne les montants de la
                            // colonne.
                            String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(libelleRubrique.getCol())
                                    + rowEcritureStart + ":" + getColAlpha(libelleRubrique.getCol()) + rowEcritureEnd
                                    + ")";
                            createCellFormula(formule, getStyleMontant());

                            // ligne total montant couvert dans le cas d'un
                            // montant ventilé
                            this.setCurrentRow(ligneCouvert);
                            setColNum(libelleRubrique.getCol());
                            formule = getColAlpha(libelleRubrique.getCol()) + (rowEcritureEnd + 1) + "-"
                                    + getColAlpha(libelleRubrique.getCol()) + (rowEcritureEnd + 3);
                            createCellFormula(formule, getStyleMontant());
                        }

                        // ligne solde après ventilation
                        setColNum(libelleRubrique.getCol());
                        this.setCurrentRow(ligneTotal);

                        if (valeur.getMontant().compareTo(new BigDecimal("0")) <= 0) {
                            this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                        } else {
                            this.createCell(valeur.getMontant().doubleValue(), getStyleMontant());
                        }
                    } else {
                        // cas double (employeur)
                        if (valeur.getMontantEmployeur() != null) {
                            // ligne montant ventilation
                            this.setCurrentRow(ligneMontant);
                            setColNum(libelleRubrique.getColEmployeur());

                            if (valeur.isVentile()) {
                                this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());

                                this.setCurrentRow(ligneCouvert);
                                setColNum(libelleRubrique.getColEmployeur());
                                this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                            } else {
                                String formule = CAVPDocumentImpression.SUM + "("
                                        + getColAlpha(libelleRubrique.getColEmployeur()) + rowEcritureStart + ":"
                                        + getColAlpha(libelleRubrique.getColEmployeur()) + rowEcritureEnd + ")";
                                createCellFormula(formule, getStyleMontant());

                                // ligne total montant couvert dans le cas d'un
                                // montant ventilé
                                this.setCurrentRow(ligneCouvert);
                                setColNum(libelleRubrique.getColEmployeur());
                                formule = getColAlpha(libelleRubrique.getColEmployeur()) + (rowEcritureEnd + 1) + "-"
                                        + getColAlpha(libelleRubrique.getColEmployeur()) + (rowEcritureEnd + 3);
                                createCellFormula(formule, getStyleMontant());
                            }

                            // ligne solde après ventilation
                            setColNum(libelleRubrique.getColEmployeur());
                            this.setCurrentRow(ligneTotal);

                            if (valeur.getMontantEmployeur().compareTo(new BigDecimal("0")) <= 0) {
                                this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                            } else {
                                this.createCell(valeur.getMontantEmployeur().doubleValue(), getStyleMontant());
                            }
                            // cas double (salarié)
                        } else if (valeur.getMontantSalarie() != null) {
                            // ligne montant ventilation
                            this.setCurrentRow(ligneMontant);
                            setColNum(libelleRubrique.getColSalarie());

                            if (valeur.isVentile()) {
                                this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());

                                this.setCurrentRow(ligneCouvert);
                                setColNum(libelleRubrique.getColSalarie());
                                this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                            } else {
                                String formule = CAVPDocumentImpression.SUM + "("
                                        + getColAlpha(libelleRubrique.getColSalarie()) + rowEcritureStart + ":"
                                        + getColAlpha(libelleRubrique.getColSalarie()) + rowEcritureEnd + ")";
                                createCellFormula(formule, getStyleMontant());

                                // ligne total montant couvert dans le cas d'un
                                // montant ventilé
                                this.setCurrentRow(ligneCouvert);
                                setColNum(libelleRubrique.getColSalarie());
                                formule = getColAlpha(libelleRubrique.getColSalarie()) + (rowEcritureEnd + 1) + "-"
                                        + getColAlpha(libelleRubrique.getColSalarie()) + (rowEcritureEnd + 3);
                                createCellFormula(formule, getStyleMontant());
                            }

                            // ligne solde après ventilation
                            setColNum(libelleRubrique.getColSalarie());
                            this.setCurrentRow(ligneTotal);

                            if (valeur.getMontantSalarie().compareTo(new BigDecimal("0")) <= 0) {
                                this.createCell(CAVPDocumentImpression.MONTANT_ZERO, getStyleMontant());
                            } else {
                                this.createCell(valeur.getMontantSalarie().doubleValue(), getStyleMontant());
                            }

                        }
                    }
                }

            }
        }

        // si aucune colonne alors iterator vide donc on affiche rien
        if (getColNum() > 0) {
            this.setCurrentRow(ligneMontant);
            String formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(CAVPDocumentImpression.NUMCOLSTART)
                    + (rowEcritureEnd + 1) + ":" + getColAlpha((getColNum() - 1)) + (rowEcritureEnd + 1) + ")";
            createCellFormula(formule, getStyleMontant());

            this.setCurrentRow(ligneCouvert);
            setColNum((getColNum() - 1));
            formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(CAVPDocumentImpression.NUMCOLSTART)
                    + (rowEcritureEnd + 2) + ":" + getColAlpha((getColNum() - 1)) + (rowEcritureEnd + 2) + ")";
            createCellFormula(formule, getStyleMontant());

            this.setCurrentRow(ligneTotal);
            setColNum((getColNum() - 1));
            formule = CAVPDocumentImpression.SUM + "(" + getColAlpha(CAVPDocumentImpression.NUMCOLSTART)
                    + (rowEcritureEnd + 3) + ":" + getColAlpha((getColNum() - 1)) + (rowEcritureEnd + 3) + ")";
            createCellFormula(formule, getStyleMontant());
        }
    }

    /**
     * Créé le : 23 août 06
     * 
     * @param sheet
     * @return HSSFFooter
     */
    private HSSFFooter setFooter(HSSFSheet sheet) {
        HSSFFooter footer = sheet.getFooter();
        footer.setRight(getSession().getLabel("PAGE") + HSSFFooter.page() + "/" + HSSFFooter.numPages());
        footer.setLeft(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1) + " - "
                + JACalendar.todayJJsMMsAAAA() + " - " + getSession().getUserName());
        return footer;
    }

    /**
     * @param sheet
     * @return
     */
    private HSSFHeader setHeader(HSSFSheet sheet) {
        getDocumentInfo().setTemplateName("");
        HSSFHeader header = sheet.getHeader();
        header.setLeft(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        header.setRight(getSession().getLabel("SECTION") + " " + section);
        return header;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param ventilationGlobale
     *            the ventilationGlobale to set
     */
    public void setVentilationGlobale(Boolean ventilationGlobale) {
        VentilationGlobale = ventilationGlobale;
    }

    /**
     * Trie les rubriques de la section
     * 
     * @param Map
     *            sectionMap : map dont les clés correspondent aux rubriques non triées.
     * @return Iterator (CAVPDetailRubriqueSection) contenant la liste des rubriques triées.
     */
    private Iterator<CAVPDetailRubriqueSection> sortMap(Map<?, ?> sectionMap) {
        ArrayList<CAVPDetailRubriqueSection> list = new ArrayList<CAVPDetailRubriqueSection>();
        Iterator<?> it = sectionMap.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            CAVPDetailRubriqueSection rubrique = new CAVPDetailRubriqueSection();
            rubrique.setIdRubrique(key);
            if (!key.equals(CAVPVentilateur.KEY_POSTE_A_VENTILER)) {
                String ordre = CAVPVentilateur.getRubriqueOrdre(rubrique.getIdRubrique(), super.getSession(),
                        ventilateur.getTypeDeProcedure());
                rubrique.setOrdre(ordre);
            } else {
                rubrique.setOrdre(CAVPDocumentImpression.ORDRE_MAX);
            }

            list.add(rubrique);
        }

        Collections.sort(list, new CAVPComparator());
        return list.iterator();
    }

}
