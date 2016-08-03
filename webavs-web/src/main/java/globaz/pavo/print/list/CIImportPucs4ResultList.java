package globaz.pavo.print.list;

import globaz.aquila.print.list.COAbstractListExcel;
import globaz.globall.db.BSession;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4DetailResultBean;
import globaz.pavo.db.inscriptions.declaration.CIImportPucs4DetailResultInscriptionBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Crée la liste des rentiers et irrécouvrables
 * 
 * @author SEL
 */
public class CIImportPucs4ResultList extends COAbstractListExcel {

    private CIImportPucs4DetailResultBean detailResultBean;

    // créé la feuille xls
    public CIImportPucs4ResultList(BSession session, CIImportPucs4DetailResultBean detailResultBean) {
        super(session, session.getLabel("IMPORT_PUCS_4_PROCESS_ROOT_FILE_NAME"), session
                .getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_TITLE"));
        this.detailResultBean = detailResultBean;
    }

    @Override
    public String getNumeroInforom() {
        return "TODO";
    }

    public void createResultList() {

        initDocument();
        createTitleRow();

        for (Entry<String, List<CIImportPucs4DetailResultInscriptionBean>> entryAnneeListInscriptions : detailResultBean
                .getMapAnneeListInscriptions().entrySet()) {
            createContentRows(entryAnneeListInscriptions.getValue());
        }

    }

    private void initDocument() {

        createSheet("TODO");
        initPage(false);
        createHeader();
        createFooter("TODO");

        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 14000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);
        currentSheet.setColumnWidth((short) numCol++, (short) 5000);

    }

    private void createTitleRow() {

        ArrayList<String> colTitles = new ArrayList<String>();
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_NSS"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_NOM") + " / "
                + getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_MESSAGE"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_MM") + "-"
                + getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_MM"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_ANNEE"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_CC"));
        colTitles.add(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_REVENU"));

        initTitleRow(colTitles);

    }

    private void createContentRows(List<CIImportPucs4DetailResultInscriptionBean> listDetailResultInscriptionBean) {

        for (CIImportPucs4DetailResultInscriptionBean aDetailResultInscriptionBean : listDetailResultInscriptionBean) {
            createRow();
            createCell(aDetailResultInscriptionBean.getNss());
            createCell(aDetailResultInscriptionBean.getNom());
            createCell(aDetailResultInscriptionBean.getMoisDebut() + "-" + aDetailResultInscriptionBean.getMoisFin());
            createCell(aDetailResultInscriptionBean.getAnnee());
            createCell(aDetailResultInscriptionBean.getGenre());
            createCell(aDetailResultInscriptionBean.getRevenu());

            for (String aErreur : aDetailResultInscriptionBean.getErrors()) {
                createRow();
                createCell(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_ERREUR") + " : ");
                createCell(aErreur);
            }

            for (String aInfo : aDetailResultInscriptionBean.getInfos()) {
                createRow();
                createCell(getSession().getLabel("IMPORT_PUCS_4_DETAIL_RESULT_LIST_AVERTISSEMENT") + " : ");
                createCell(aInfo);
            }
        }

    }

}
