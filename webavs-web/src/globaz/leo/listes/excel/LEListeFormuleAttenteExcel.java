package globaz.leo.listes.excel;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.leo.db.envoi.LEEtapesSuivantesViewBean;
import globaz.lupus.db.journalisation.LUComplementJournalListViewBean;
import globaz.lupus.db.journalisation.LUComplementJournalViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author dda
 */
public class LEListeFormuleAttenteExcel extends LEAbstractListExcel {

    private static final String NUMERO_REFERENCE_INFOROM = "0234GEN";

    private BProcess process = null;

    /**
     * @param session
     * @param process
     */
    public LEListeFormuleAttenteExcel(BSession session, BProcess process) {
        super(session, "LEListeFormuleAttenteExcel", session.getLabel("LISTE_FORMULES_ATTENTE"));
        this.process = process;
    }

    /**
     * Création de la page, ajout du titres des colonnes.
     * 
     * @return
     */
    private void initListe() {
        createSheet(getSession().getLabel("LISTE"));

        ArrayList titles = new ArrayList();
        titles.add(new ParamTitle(getSession().getLabel("LISTE_NUM_AFFILIE")));
        titles.add(new ParamTitle(getSession().getLabel("LISTE_RAISON_SOCIALE_COURTE")));
        titles.add(new ParamTitle(getSession().getLabel("LISTE_CAT_SUIVI")));
        titles.add(new ParamTitle(getSession().getLabel("LISTE_ETAPE")));
        titles.add(new ParamTitle(getSession().getLabel("LISTE_ETAPE_SUIVANTE")));
        titles.add(new ParamTitle(getSession().getLabel("LISTE_PERIODE")));
        titles.add(new ParamTitle(getSession().getLabel("LISTE_DATE_RAPPEL")));

        initTitleRow(titles);
        initPage(true);
        createHeader();
        createFooter(LEListeFormuleAttenteExcel.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, LEAbstractListExcel.COLUMN_WIDTH_4500);
        currentSheet.setColumnWidth((short) numCol++, LEAbstractListExcel.COLUMN_WIDTH_10000);
        currentSheet.setColumnWidth((short) numCol++, LEAbstractListExcel.COLUMN_WIDTH_6500);
        currentSheet.setColumnWidth((short) numCol++, LEAbstractListExcel.COLUMN_WIDTH_10000);
        currentSheet.setColumnWidth((short) numCol++, LEAbstractListExcel.COLUMN_WIDTH_10000);
        currentSheet.setColumnWidth((short) numCol++, LEAbstractListExcel.COLUMN_WIDTH_3500);
        currentSheet.setColumnWidth((short) numCol++, LEAbstractListExcel.COLUMN_WIDTH_3500);
    };

    /**
     * Initialisation de la feuille xls.
     * 
     * @param manager
     * @param transaction
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(LEEtapesSuivantesListViewBean manager, BTransaction transaction)
            throws Exception {
        manager.setSession(getSession());
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (process.isAborted()) {
            return null;
        }

        if (!manager.isEmpty()) {
            initListe();
        }

        process.setProgressScaleValue(manager.size());

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {

            String cat = "";
            String periode = "";
            String etapeSuivante = "";

            LEEtapesSuivantesViewBean etape = (LEEtapesSuivantesViewBean) manager.get(i);
            etapeSuivante = etape.getEtapeSuivante();
            etape.setLoadedFromManager(false);
            etape.retrieve(transaction);

            createRow();

            LUComplementJournalListViewBean l = etape.getCplJourn();
            if (l != null) {
                for (int j = 0; j < l.size(); j++) {
                    LUComplementJournalViewBean c = (LUComplementJournalViewBean) l.getEntity(j);
                    if (ILEConstantes.CS_CATEGORIE_GROUPE.equals(c.getCsTypeCodeSysteme())) {
                        cat = c.getValeurCodeSysteme();
                    }
                }
            }
            LUReferenceProvenanceListViewBean l2 = etape.getRefProvList();
            if (l2 != null) {
                for (int j = 0; j < l2.size(); j++) {
                    LUReferenceProvenanceViewBean c = (LUReferenceProvenanceViewBean) l2.getEntity(j);
                    // if(ILEConstantes.CS_PARAM_GEN_PERIODE.equals(c.getIdCleReferenceProvenance())){
                    if (ILEConstantes.CS_PARAM_GEN_PERIODE.equals(c.getTypeReferenceProvenance())) {
                        periode = c.getIdCleReferenceProvenance();
                    }
                }
            }

            // Numéro d'affilié
            this.createCell(etape.getLibelle(), getStyleListLeft());
            // Raison sociale courte
            this.createCell(etape.getDestinataire(), getStyleListLeft());
            // Catégorie du suivi
            this.createCell(getSession().getCodeLibelle(cat), getStyleListLeft());
            // Etapes
            this.createCell(JadeStringUtil.change(etape.getLibelleAffichage(), "<BR>", ""), getStyleListLeft());
            // Etapes suivantes
            // createCell(getSession().getCodeLibelle(etape.getEtapeSuivante()),
            // getStyleListLeft());
            this.createCell(getSession().getCodeLibelle(etapeSuivante), getStyleListLeft());
            // Période
            this.createCell(periode, getStyleListLeft());
            // Date de rappel
            this.createCell(etape.getDateRappel(), getStyleListLeft());
            process.incProgressCounter();
        }

        return currentSheet;
    }

}
