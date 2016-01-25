package globaz.osiris.print.list;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.print.CAFailliteForExcelList;
import globaz.osiris.db.print.CAFailliteForExcelListManager;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class CAListFailliteExcel extends CAAbstractListExcel {

    private final static String NUMERO_REFERENCE_INFOROM = "0236GCA";

    private String forIdCategorie;
    private String forSelectionRole;

    public CAListFailliteExcel(BSession session) {
        super(session, session.getLabel("LIST_TITLE_SHORT"), session.getLabel("LIST_TITLE_LONG"));
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    @Override
    public String getNumeroInforom() {
        return CAListFailliteExcel.NUMERO_REFERENCE_INFOROM;
    }

    private void initCritere() {

        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            if (JadeStringUtil.contains(getForSelectionRole(), ",")) {
                createRow();
                this.createCell(getSession().getLabel("ROLE"), getStyleCritereTitle());
                this.createCell(getSession().getLabel("TOUS"), getStyleCritere());
            } else {
                createRow();
                this.createCell(getSession().getLabel("ROLE"), getStyleCritereTitle());
                this.createCell(getSession().getCodeLibelle(getForSelectionRole()), getStyleCritere());
            }
        }

        if (!JadeStringUtil.isBlank(getForIdCategorie())) {
            if (JadeStringUtil.contains(getForIdCategorie(), ",")) {
                createRow();
                this.createCell(getSession().getLabel("CATEGORIE"), getStyleCritereTitle());
                this.createCell(getSession().getLabel("TOUS"), getStyleCritere());
            } else {
                createRow();
                this.createCell(getSession().getLabel("CATEGORIE"), getStyleCritereTitle());
                this.createCell(getSession().getCodeLibelle(getForIdCategorie()), getStyleCritere());
            }
        }
    }

    private void initListe() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(getSession().getLabel("NUMERO"));
        colTitles.add(getSession().getLabel("ROLE"));
        colTitles.add(getSession().getLabel("SOCIETE"));
        colTitles.add(getSession().getLabel("DATE_FAILLITE"));
        colTitles.add(getSession().getLabel("ETAT_COLLOCATION"));
        colTitles.add(getSession().getLabel("SUSPENSION_FAILLITE"));
        colTitles.add(getSession().getLabel("COMMENTAIRE"));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListFailliteExcel.NUMERO_REFERENCE_INFOROM);

        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Numéro
        // Administrateur
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Nom
        // Administrateur
        currentSheet.setColumnWidth((short) numCol++, (short) 20000); // Société
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date
        // Plainte
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // date
        // etat
        // colloc
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // date de
        // suspension
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
                this.createCell(faillite.getNumAdmin(), getStyleListLeft()); // numéro
                // admin
                this.createCell(getSession().getCodeLibelle(faillite.getRole()), getStyleListLeft()); // role
                this.createCell(faillite.getSociete(), getStyleListLeft()); // société
                this.createCell(faillite.getDateFaillite(), getStyleListLeft()); // date
                // de
                // la
                // faillite
                this.createCell(faillite.getDateEtatColloc(), getStyleListLeft()); // etat
                // de
                // collocation
                this.createCell(faillite.getDateSuspension(), getStyleListLeft());// suspension
                // de
                // la
                // faillite
                this.createCell(faillite.getCommentaire(), getStyleListLeft()); // commentaire
            }
        }
        return currentSheet;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }
}
