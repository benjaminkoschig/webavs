package globaz.osiris.print.list;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.print.CAFailliteForExcelList;
import globaz.osiris.db.print.CAFailliteForExcelListManager;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;

public class CAListFailliteExcel extends CAAbstractListExcel {

    private final static String NUMERO_REFERENCE_INFOROM = "0236GCA";

    private String forIdCategorie;
    private String forSelectionRole;

    public CAListFailliteExcel(BSession session) {
        super(session, session.getLabel("LIST_TITLE_SHORT"), session.getLabel("LIST_TITLE_LONG"));
    }

    @Override
    public String getNumeroInforom() {
        return CAListFailliteExcel.NUMERO_REFERENCE_INFOROM;
    }

    private void initCritere() {
        try {
            createRow();
            this.createCell(getSession().getLabel("NUMERO_CAISSE"), getStyleCritereTitle());
            this.createCell(
                    CommonPropertiesUtils.getValue(ch.globaz.common.properties.CommonProperties.KEY_NO_CAISSE_FORMATE),
                    getStyleCritere());

        } catch (PropertiesException e) {
            this.createCell("");
            JadeLogger.info(e, e.getMessage());
        }

        createRow();
        this.createCell(getSession().getLabel("DATE_ETABLISSEMENT"), getStyleCritereTitle());
        this.createCell(new Date().getSwissValue(), getStyleCritere());
    }

    private void initListe() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(getSession().getLabel("NUMERO"));
        colTitles.add(getSession().getLabel("ROLE"));
        colTitles.add(getSession().getLabel("CATEGORIE"));
        colTitles.add(getSession().getLabel("SOCIETE"));
        colTitles.add(getSession().getLabel("DATE_FAILLITE"));
        colTitles.add(getSession().getLabel("DATE_PRODUCTION"));
        colTitles.add(getSession().getLabel("DATE_PRODUCTION_DEFINITIVE"));
        colTitles.add(getSession().getLabel("DATE_ANNULATION_PRODUCTION"));
        colTitles.add(getSession().getLabel("DATE_REVOCATION_RETRACTATION"));
        colTitles.add(getSession().getLabel("SUSPENSION_FAILLITE"));
        colTitles.add(getSession().getLabel("ETAT_COLLOCATION"));
        colTitles.add(getSession().getLabel("MODIF_ETAT_COLLOCATION"));
        colTitles.add(getSession().getLabel("CLOTURE_FAILLITE"));
        colTitles.add(getSession().getLabel("MONTANT_PRODUCTION"));
        colTitles.add(getSession().getLabel("COMMENTAIRE"));

        createSheet(getSession().getLabel("LISTE_FAILLITE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        HSSFPrintSetup ps = currentSheet.getPrintSetup();
        ps.setFitWidth((short) 1);
        ps.setFitHeight((short) 0);
        currentSheet.setAutobreaks(true);
        createHeader();
        createFooter(CAListFailliteExcel.NUMERO_REFERENCE_INFOROM);

        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Numéro Administrateur
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Role
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Catégorie
        currentSheet.setColumnWidth((short) numCol++, (short) 20000); // Société
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date faillite
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date production
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date production définitive
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date annulation production
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date révocation rétractation
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // date de suspension
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // date etat colloc
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // date modif état colloc
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // date cloture faillite
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // montant production
        currentSheet.setColumnWidth((short) numCol++, (short) 15000); // commentaire
    }

    /**
     * Methode setTitleRow. Permet de définir l'entête des colonnes
     * 
     * @author: sel Créé le : 05.06.2007
     * @param currentSheet
     * @param col_titles
     * @return la feuille Excel courante
     */
    @Override
    protected HSSFSheet initTitleRow(ArrayList col_titles) {
        HSSFRow row = null;
        HSSFCell c;
        HSSFCellStyle styleCenter = getStyleListTitleCenter();
        // styleCenter.setFillPattern(HSSFCellStyle.FINE_DOTS ); //points en
        // trame de fond

        // create Title Row
        createRow(); // Ligne vide
        row = currentSheet.createRow(currentSheet.getPhysicalNumberOfRows());
        int i = 0;
        for (i = 0; i < col_titles.size(); i++) {
            // set cell value
            c = row.createCell((short) i);
            c.setCellValue((String) col_titles.get(i));
            c.setCellStyle(styleCenter);
        }

        return currentSheet;
    }

    public HSSFSheet populateSheetListe(CAFailliteForExcelListManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        CAFailliteForExcelList faillite = null;

        initListe();

        while (((faillite = (CAFailliteForExcelList) manager.cursorReadNext(statement)) != null) && !faillite.isNew()) {
            if (faillite != null) {
                createRow();
                this.createCell(faillite.getNumAdmin(), getStyleListCenterVertical()); // numéro admin
                this.createCell(getSession().getCodeLibelle(faillite.getRole()), getStyleListCenterVertical()); // role
                this.createCell(getSession().getCodeLibelle(faillite.getTypeAffiliation()),
                        getStyleListCenterVertical()); // Catégorie
                this.createCell(faillite.getSociete(), getStyleListLeftVerticalCenter()); // société
                this.createCell(faillite.getDateFaillite(), getStyleListCenterVertical()); // date de la faillite
                this.createCell(faillite.getDateProduction(), getStyleListCenterVertical()); // date de production
                this.createCell(faillite.getDateProductionDefinitive(), getStyleListCenterVertical()); // date de
                                                                                                       // produciton
                // définitive
                this.createCell(faillite.getDateAnnulationProduction(), getStyleListCenterVertical()); // date
                                                                                                       // d'annulation
                // production
                this.createCell(faillite.getDateRevocation(), getStyleListCenterVertical()); // date de révocation
                this.createCell(faillite.getDateSuspension(), getStyleListCenterVertical());// suspension de la faillite
                this.createCell(faillite.getDateEtatColloc(), getStyleListCenterVertical()); // etat de collocation
                this.createCell(faillite.getDateModificationCollocation(), getStyleListCenterVertical()); // date de
                // modification
                // d'état collocation
                this.createCell(faillite.getDateCloture(), getStyleListCenterVertical()); // date de cloture faillite
                if (JadeStringUtil.isBlank(faillite.getMontantProduction())) {
                    this.createCell(0, getStyleMontant()); // Montant production
                } else {
                    this.createCell(new Double(faillite.getMontantProduction()), getStyleMontantVerticalCenter());
                }

                this.createCell(faillite.getCommentaire(), getStyleListLeftTop()); // commentaire
            }
        }
        return currentSheet;
    }

}
