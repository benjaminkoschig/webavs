/*
 * Globaz SA
 */
package globaz.naos.listes.pdf.extraitDS;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.controleLpp.AFSuiviLppAnnuelSalarie;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITIRole;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Classe permettant la création de l'extrait de salaire pour un affilié pour une année donnée
 * 
 * @author est
 */
public class AFListeExtraitDS extends BProcess {

    private final transient Logger LOG = LoggerFactory.getLogger(this.getClass());
    private static final long serialVersionUID = 251609627022834101L;

    private static final String NUM_INFORMOM = "0324CAF";

    private static final short CELL_EMPLOYEUR_NOM = 0;
    private static final short CELL_EMPLOYEUR_RUE = 1;
    private static final short CELL_EMPLOYEUR_NPA = 2;
    private static final short CELL_EMPLOYEUR_LOCALITE = 3;
    private static final short CELL_EMPLOYEUR_NUM_AFFI = 4;

    private static final short CELL_SALARIE_NSS = 0;
    private static final short CELL_SALARIE_NOM = 1;
    private static final short CELL_SALARIE_PERIODE = 2;
    private static final short CELL_SALARIE_SALAIRE = 3;
    private static final short CELL_SALARIE_SEUIL = 4;

    private List<AFSuiviLppAnnuelSalarie> listeSalarie;
    private transient List<HSSFRow> listeRowSalarie = null;
    private AFAffiliation employeur;
    private transient TIAdresseDataSource adresseEmployeur;
    private String nomCaisse;
    private String adresseCaisse;
    private String annee;
    private transient BaseFont font = null;
    private transient HSSFSheet sheet;

    private String langueIso;

    private String date;

    private int nivSecuUser = 0;

    private JadePublishDocumentInfo documentInfoPdf;
    private String path;

    public AFListeExtraitDS() {
        // Nothing
    }

    private void createListPDF() throws Exception {
        createTableauSalarie(NUM_INFORMOM + "_" + JadeUUIDGenerator.createStringUUID());
    }

    private void initSheet() {
        HSSFWorkbook wb = new HSSFWorkbook();
        sheet = wb.createSheet("Sheet");
        sheet.setColumnWidth((short) 1, (short) (256 * 27));

    }

    private void initFonts() {
        try {
            font = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
        } catch (DocumentException e) {
            LOG.error(e.toString());
            throw new CommonTechnicalException(e);
        } catch (IOException e) {
            LOG.error(e.toString());
            throw new CommonTechnicalException(e);
        }
    }

    private JadePublishDocumentInfo createDocInfo() throws Exception {
        JadePublishDocumentInfo documentInfo = createDocumentInfo();
        documentInfo.setPublishDocument(false);
        documentInfo.setArchiveDocument(true);
        documentInfo.setDocumentTypeNumber(NUM_INFORMOM);
        documentInfo.setDocumentProperty("numero.affilie.formatte", employeur.getAffilieNumero());
        documentInfo.setDocumentProperty("annee", getAnnee());

        String numAffNonFormatte = formatNumAffilie(employeur.getAffilieNumero());

        documentInfo.setDocumentProperty("numero.affilie.non.formatte", numAffNonFormatte);

        TIDocumentInfoHelper.fill(documentInfo, employeur.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                employeur.getAffilieNumero(), numAffNonFormatte);

        return documentInfo;
    }

    private String formatNumAffilie(String numAffilie) throws Exception {
        String numAffNonFormatte;
        IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

        try {
            numAffNonFormatte = affilieFormater.unformat(numAffilie);
        } catch (Exception e) {
            numAffNonFormatte = numAffilie;
            LOG.warn("Unabled to format numAffilie : " + numAffilie, e);
        }
        return numAffNonFormatte;
    }

    /***
     * Permet de remplir la list des AFExtraitDS
     * 
     * @param listeDS
     */
    private void fillRowsList(List<AFSuiviLppAnnuelSalarie> listeSalarie) {
        listeRowSalarie = new ArrayList<HSSFRow>();

        HSSFRow rowEmployeur = sheet.createRow(0);
        rowEmployeur.setHeightInPoints(10 * 15);
        rowEmployeur.createCell(CELL_EMPLOYEUR_NOM).setCellValue(getEmployeur().getRaisonSociale());
        rowEmployeur.createCell(CELL_EMPLOYEUR_RUE).setCellValue(
                getAdresseEmployeur().getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE));
        rowEmployeur.createCell(CELL_EMPLOYEUR_NPA).setCellValue(
                getAdresseEmployeur().getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));
        rowEmployeur.createCell(CELL_EMPLOYEUR_LOCALITE).setCellValue(
                getAdresseEmployeur().getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE));
        rowEmployeur.createCell(CELL_EMPLOYEUR_NUM_AFFI).setCellValue(getEmployeur().getAffilieNumero());

        listeRowSalarie.add(0, rowEmployeur);

        for (int i = 0; i < listeSalarie.size(); i++) {
            // i + 1 car la première ligne est réservé aux informations de l'employeur
            HSSFRow row = sheet.createRow(i + 1);
            row.setHeightInPoints(10 * 15);
            if (!JadeStringUtil.isEmpty(listeSalarie.get(i).getNss())) {
                row.createCell(CELL_SALARIE_NSS).setCellValue(NSUtil.formatAVSNewNum(listeSalarie.get(i).getNss()));
            } else {
                row.createCell(CELL_SALARIE_NSS).setCellValue(listeSalarie.get(i).getNss());
            }
            row.createCell(CELL_SALARIE_NOM).setCellValue(listeSalarie.get(i).getNomSalarie());
            row.createCell(CELL_SALARIE_PERIODE).setCellValue(
                    JadeStringUtil.fillWithZeroes(listeSalarie.get(i).getMoisDebut().toString(), 2) + " - "
                            + JadeStringUtil.fillWithZeroes(listeSalarie.get(i).getMoisFin().toString(), 2));
            // Test si code secu
            if (isUserHasRightToShowSalaire(listeSalarie.get(i).getNiveauSecurite())) {
                row.createCell(CELL_SALARIE_SALAIRE).setCellValue(listeSalarie.get(i).getSalaire().toStringFormat());
            } else {
                row.createCell(CELL_SALARIE_SALAIRE).setCellValue(getSession().getLabel("NAOS_CACHE"));
            }
            row.createCell(CELL_SALARIE_SEUIL).setCellValue(listeSalarie.get(i).getSeuilEntree().toStringFormat());
            // i + 1 car la première ligne est réservé aux informations de l'employeur
            listeRowSalarie.add(i + 1, row);
        }
    }

    private boolean isUserHasRightToShowSalaire(Integer niveauSecuCi) {
        int nivSecuCI = 0;
        int nivSecuAffilie = 0;
        try {
            nivSecuAffilie = Character.getNumericValue(employeur.getAccesSecurite().charAt(
                    employeur.getAccesSecurite().length() - 1));
        } catch (Exception e) {
            nivSecuAffilie = 0;
        }

        try {
            nivSecuCI = Character.getNumericValue(niveauSecuCi.toString().charAt(niveauSecuCi.toString().length() - 1));
        } catch (Exception e) {
            nivSecuCI = 0;
        }

        if ((nivSecuUser < nivSecuAffilie) || (nivSecuUser < nivSecuCI)) {
            return false;
        }

        return true;
    }

    private void retrieveNivSecuUser() throws Exception {
        FWSecureUserDetail user = new FWSecureUserDetail();
        user.setSession(getSession());
        user.setUser(getSession().getUserId());
        user.setLabel(CICompteIndividuel.SECURITE_LABEL);
        user.retrieve(getTransaction());

        if (!user.isNew()) {
            nivSecuUser = Integer.parseInt(user.getData());
        }
    }

    /**
     * Creation du tableau récapitulatif des salariés
     * 
     * @param fileName
     * @param listeSalarie
     * @throws Exception
     */
    private void createTableauSalarie(String fileName) throws Exception {
        // nouveau document pdf, on défini un nouveau rectangle inversant les taille du format A4 pour l'avoir en format
        // paysage
        Document out = new Document(new Rectangle(842.0F, 595.0F));

        // // création des fonts, standard et header (gras)
        Font tableStandardFont = new Font(font);
        tableStandardFont.setSize(8);
        tableStandardFont.setColor(Color.BLACK);

        Font tableHeaderFont = new Font(font);
        tableHeaderFont.setSize(8);
        tableHeaderFont.setStyle("bold");

        // Construction nom fichier et path
        setPath(Jade.getInstance().getPersistenceDir() + fileName + ".pdf");

        FileOutputStream file = new FileOutputStream(getPath());

        PdfWriter.getInstance(out, file);

        out.open();

        // Debut de la feuille
        out.newPage();
        // *********** en tete
        out.add(createEnteteFeuilleTable(tableHeaderFont));
        // *********** Titre feuille
        out.add(createTitreFeuilleTable(tableHeaderFont));
        // *********** Bloc employeur
        out.add(createBlocEmployeurFeuilleTable(tableHeaderFont, tableStandardFont));
        // ********** Bloc salarié, 1 par année
        out.add(createBlocSalarieFeuilleTable(tableHeaderFont, tableStandardFont));
        // ********** Fin de la feuille
        out.close();

        file.close();
    }

    /**
     * Retourne l'en tete de la caisse pour chaque feuille récapitulative des salariées
     * 
     * @param font
     * @return
     * @throws Exception
     */
    private PdfPTable createEnteteFeuilleTable(Font font) throws Exception {
        // tableau de base
        PdfPTable table = new PdfPTable(3);
        // largeur 100
        table.setWidthPercentage(100);
        // titre caisse
        PdfPCell cell = new PdfPCell(new Phrase(getNomCaisse(), font));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        // date, ajouter la date voulue
        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_DATE", langueIso)
                + date, font));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        // localité
        cell = new PdfPCell(new Phrase(getAdresseCaisse(), font));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        // lignes vide pour espace avant tableau employeur
        cell = new PdfPCell(new Phrase(" ", font));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" ", font));
        cell.setColspan(3);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    /**
     * Création du titre de la feuille des salariées
     * 
     * @param tableHeaderFont
     * @return
     * @throws Exception
     */
    private PdfPTable createTitreFeuilleTable(Font tableHeaderFont) throws Exception {
        // En tete titre détaié
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_TITRE",
                langueIso), tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        // ligne vide
        cell = new PdfPCell(new Phrase(" ", tableHeaderFont));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        return table;
    }

    /**
     * Creation du bloc employeur, titre, entete et tableau
     * 
     * @param header
     * @param standard
     * @return
     * @throws Exception
     */
    private PdfPTable createBlocEmployeurFeuilleTable(Font header, Font standard) throws Exception {

        // récupération d'une ligne
        HSSFRow ligneEmployeur = listeRowSalarie.get(0);

        // tableau de 8 colones
        PdfPTable tableau = new PdfPTable(8);
        tableau.setWidthPercentage(100.00f);
        // ligne employeur
        PdfPCell cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel(
                "NAOS_LISTE_EXTRAIT_DS_EMPLOYEUR", langueIso), header));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(8);
        tableau.addCell(cell);

        // **** En tete employeur, en tete du tableau

        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_NOM", langueIso),
                header));
        cell.setColspan(2);
        tableau.addCell(cell);
        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_RUE", langueIso),
                header));
        cell.setColspan(2);
        tableau.addCell(cell);
        tableau.addCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_NPA", langueIso),
                header));
        tableau.addCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_LOCALITE", langueIso),
                header));
        tableau.addCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_NUMAFFI", langueIso),
                header));
        tableau.addCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_ANNEE_DECOMPTE",
                langueIso), header));

        // Valeurs de la cellule employeur

        // Nom - colonne 0
        cell = new PdfPCell(new Phrase(ligneEmployeur.getCell(CELL_EMPLOYEUR_NOM).getStringCellValue(), standard));
        cell.setColspan(2);
        tableau.addCell(cell);

        // Rue - colonne 1
        cell = new PdfPCell(new Phrase(ligneEmployeur.getCell(CELL_EMPLOYEUR_RUE).getStringCellValue(), standard));
        cell.setColspan(2);
        tableau.addCell(cell);

        // NPA - colonne 2
        tableau.addCell(new Phrase(ligneEmployeur.getCell(CELL_EMPLOYEUR_NPA).getStringCellValue(), standard));

        // Localite - colonne 3
        tableau.addCell(new Phrase(ligneEmployeur.getCell(CELL_EMPLOYEUR_LOCALITE).getStringCellValue(), standard));

        // No Affilie - colonne 4
        tableau.addCell(new Phrase(ligneEmployeur.getCell(CELL_EMPLOYEUR_NUM_AFFI).getStringCellValue(), standard));

        // Annee
        tableau.addCell(new Phrase(getAnnee(), standard));

        // ligne vide
        PdfPCell cellVide = new PdfPCell(new Phrase(" ", standard));
        cellVide.setBorder(Rectangle.NO_BORDER);
        cellVide.setColspan(8);
        tableau.addCell(cellVide);

        return tableau;

    }

    /**
     * Création du bloc salarié
     * 
     * @param header
     * @param standard
     * @param listeSalarie
     * @return
     * @throws Exception
     */
    private PdfPTable createBlocSalarieFeuilleTable(Font header, Font standard) throws Exception {

        // tableau de 8 colones
        PdfPTable tableau = new PdfPTable(6);
        tableau.setWidthPercentage(100.00f);
        // ligne employeur
        PdfPCell cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_SALARIE",
                langueIso), header));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(8);
        tableau.addCell(cell);

        // en tete du tableau
        // nss
        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_NSS", langueIso),
                header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // nom du salarie
        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_NOM_SALARIE",
                langueIso), header));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableau.addCell(cell);

        // Période
        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_PERIODE",
                langueIso), header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // montant salaire
        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_SALAIRE_AVS",
                langueIso), header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // Deduction
        cell = new PdfPCell(new Phrase(getSession().getApplication().getLabel("NAOS_LISTE_EXTRAIT_DS_SEUIL_LPP",
                langueIso), header));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableau.addCell(cell);

        // iteration sur les salarie (commence à 1 car la ligne 0 est l'employeur)
        for (int i = 1; i < listeRowSalarie.size(); i++) {
            HSSFRow ligne = listeRowSalarie.get(i);

            // nss - colonne 0
            HSSFCell nssCell = ligne.getCell(CELL_SALARIE_NSS);

            // Si nss pas null
            if (nssCell != null) {
                cell = new PdfPCell(new Phrase(nssCell.getStringCellValue(), standard));
            } else {
                cell = new PdfPCell(new Phrase(""));
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableau.addCell(cell);

            // nom - colonne 1
            cell = new PdfPCell(new Phrase(ligne.getCell(CELL_SALARIE_NOM).getStringCellValue(), standard));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableau.addCell(cell);

            // période - colonne 2
            cell = new PdfPCell(new Phrase(ligne.getCell(CELL_SALARIE_PERIODE).getStringCellValue(), standard));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableau.addCell(cell);

            // montant salaire - colonne 3
            cell = new PdfPCell(new Phrase(ligne.getCell(CELL_SALARIE_SALAIRE).getStringCellValue(), standard));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableau.addCell(cell);

            // seuil entrée LPP - colonne 4
            cell = new PdfPCell(new Phrase(ligne.getCell(CELL_SALARIE_SEUIL).getStringCellValue(), standard));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableau.addCell(cell);

        }
        return tableau;
    }

    // Getters

    public AFAffiliation getEmployeur() {
        return employeur;
    }

    public String getAnnee() {
        return annee;
    }

    public TIAdresseDataSource getAdresseEmployeur() {
        return adresseEmployeur;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public String getAdresseCaisse() {
        return adresseCaisse;
    }

    public List<AFSuiviLppAnnuelSalarie> getListeSalarie() {
        return listeSalarie;
    }

    public String getPath() {
        return path;
    }

    public JadePublishDocumentInfo getDocumentInfoPdf() {
        return documentInfoPdf;
    }

    public String getLangueIso() {
        return langueIso;
    }

    public String getDate() {
        return date;
    }

    // Setters

    public void setEmployeur(AFAffiliation employeur) {
        this.employeur = employeur;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setAdresseEmployeur(TIAdresseDataSource adresseEmployeur) {
        this.adresseEmployeur = adresseEmployeur;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }

    public void setAdresseCaisse(String adresseCaisse) {
        this.adresseCaisse = adresseCaisse;
    }

    public void setListeSalarie(List<AFSuiviLppAnnuelSalarie> listeSalarie) {
        this.listeSalarie = listeSalarie;
    }

    public void setDocumentInfoPdf(JadePublishDocumentInfo documentInfoPdf) {
        this.documentInfoPdf = documentInfoPdf;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLangueIso(String langueIso) {
        this.langueIso = langueIso;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    protected void _executeCleanUp() {
        //
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        retrieveNivSecuUser();

        setDocumentInfoPdf(createDocInfo());

        setNomCaisse(FWIImportProperties.getInstance().getProperty(getDocumentInfoPdf(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        setAdresseCaisse(FWIImportProperties.getInstance().getProperty(getDocumentInfoPdf(),
                ACaisseReportHelper.JASP_PROP_HEADER_ADRESSE_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        initFonts();

        initSheet();

        fillRowsList(getListeSalarie());

        createListPDF();

        return true;

    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("NAOS_LISTE_EXTRAIT_DS_CORPS_MAIL");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }
}