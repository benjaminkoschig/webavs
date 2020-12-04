package globaz.osiris.print.list;

import globaz.globall.db.BSession;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class CACotisationsImpayeesExcelList extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0130GCA";


    // créé la feuille xls
    public CACotisationsImpayeesExcelList(BSession session) {
        super(session, "ListCotisationsImpayes", "Liste des cotisations impayees");
    }

    @Override
    public String getNumeroInforom() {
        return CACotisationsImpayeesExcelList.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @param dateValeur
     */
    private void initCritere(String dateValeur) {
        // Titre
        createRow();
        this.createCell(getSession().getLabel("COTIMP_DATE_AU") + " : ", getStyleCritereTitle());
        this.createCell(dateValeur, getStyleCritere());
    }

    /**
     * Création de la page
     * 
     * @return
     */
    private void initListe(String dateValeur) {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COTIMP_TITLE")));
        colTitles.add(new ParamTitle(getSession().getLabel("COTIMP_SECTEUR2")));
        colTitles.add(new ParamTitle(getSession().getLabel("COTIMP_SECTEUR9")));
        colTitles.add(new ParamTitle(getSession().getLabel("COTIMP_SECTEUR4_8")));
        colTitles.add(new ParamTitle(getSession().getLabel("COTIMP_TOTAL")));

        createSheet(getSession().getLabel("LISTE"));

        initCritere(dateValeur);

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CACotisationsImpayeesExcelList.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_5500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(double[] tab_sous, double[] tab_ante,double[] tab_poursuites,double[] tab_sommations,double[] tab_surcis, double[] tab_autres,double[] tab_tot, String dateValeur)
            throws Exception {
        // création de la page
        initListe(dateValeur);

        // parcours du manager et remplissage des cell
        // Recherche du compte annexe pour obtenir le motif de blocage

        createRow();
        this.createCell(getSession().getLabel("COTIMP_ANTERIEUR"), getStyleListTitleLeft());
        this.createCell(tab_ante[0], getStyleListRight());
        this.createCell(tab_ante[1], getStyleListRight());
        this.createCell(tab_ante[2], getStyleListRight());
        this.createCell(tab_ante[3], getStyleListRight());

        createRow();
        this.createCell(getSession().getLabel("COTIMP_REVUE"), getStyleListTitleLeft());
        this.createCell(tab_sous[0], getStyleListRight());
        this.createCell(tab_sous[1], getStyleListRight());
        this.createCell(tab_sous[2], getStyleListRight());
        this.createCell(tab_sous[3], getStyleListRight());

        createRow();
        this.createCell(getSession().getLabel("COTIMP_TOTAL_TITLE"), getStyleListTitleLeft());
        this.createCell(tab_tot[0], getStyleListRight());
        this.createCell(tab_tot[1], getStyleListRight());
        this.createCell(tab_tot[2], getStyleListRight());
        this.createCell(tab_tot[3], getStyleListRight());

        createRow();
        this.createCell(getSession().getLabel("COTIMP_POURSUITE"), getStyleListTitleLeft());
        this.createCell(tab_poursuites[0], getStyleListRight());
        this.createCell(tab_poursuites[1], getStyleListRight());
        this.createCell(tab_poursuites[2], getStyleListRight());
        this.createCell(tab_poursuites[3], getStyleListRight());


        createRow();
        this.createCell(getSession().getLabel("COTIMP_SOMMATION"), getStyleListTitleLeft());
        this.createCell(tab_sommations[0], getStyleListRight());
        this.createCell(tab_sommations[1], getStyleListRight());
        this.createCell(tab_sommations[2], getStyleListRight());
        this.createCell(tab_sommations[3], getStyleListRight());


        createRow();
        this.createCell(getSession().getLabel("COTIMP_SURSIS"), getStyleListTitleLeft());
        this.createCell(tab_surcis[0], getStyleListRight());
        this.createCell(tab_surcis[1], getStyleListRight());
        this.createCell(tab_surcis[2], getStyleListRight());
        this.createCell(tab_surcis[3], getStyleListRight());

        createRow();
        this.createCell(getSession().getLabel("COTIMP_AUTRES"), getStyleListTitleLeft());
        this.createCell(tab_autres[0], getStyleListRight());
        this.createCell(tab_autres[1], getStyleListRight());
        this.createCell(tab_autres[2], getStyleListRight());
        this.createCell(tab_autres[3], getStyleListRight());
        return currentSheet;
    }



}
