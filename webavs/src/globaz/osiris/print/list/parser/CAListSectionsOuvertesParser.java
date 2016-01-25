package globaz.osiris.print.list.parser;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.sections.CASectionsOuvertes;
import globaz.osiris.db.sections.CASectionsOuvertesManager;
import globaz.osiris.print.list.CAAbstractListExcel;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author dda
 */
public class CAListSectionsOuvertesParser extends CAAbstractListExcel {

    private static final String LABEL_AFFILIE = "AFFILIE";
    private static final String LABEL_BLOQUE = "MODE_COMPENSATION";
    private static final String LABEL_DATE = "DATE";
    private static final String LABEL_LISTE = "LISTE";
    private static final String LABEL_ROLE = "ROLE";
    private static final String LABEL_SECTION = "SECTION";
    private static final String LABEL_SOLDE = "SOLDE";
    private static final String NUMERO_REFERENCE_INFOROM = "122GCA";

    private BProcess process = null;

    /**
     * @param session
     * @param process
     */
    public CAListSectionsOuvertesParser(BSession session, BProcess process) {
        super(session, "CAListSectionsOuvertesParser", session.getLabel("IMPRESSION_LISTE_SECTIONS_OUVERTES"));
        this.process = process;
    }

    @Override
    public String getNumeroInforom() {
        return CAListSectionsOuvertesParser.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * Création de la page, ajout du titres des colonnes.
     * 
     * @return
     */
    private void initListe() {
        createSheet(getSession().getLabel(CAListSectionsOuvertesParser.LABEL_LISTE));

        ArrayList titles = new ArrayList();
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesParser.LABEL_AFFILIE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesParser.LABEL_ROLE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesParser.LABEL_SECTION)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesParser.LABEL_DATE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesParser.LABEL_SOLDE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesParser.LABEL_BLOQUE)));

        initTitleRow(titles);
        initPage(true);
        createHeader();
        createFooter(CAListSectionsOuvertesParser.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_AFILIE);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_ROLE);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_SECTION);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DATE);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_BLOQUE);
    }

    /**
     * Initialisation de la feuille xls.
     * 
     * @param manager
     * @param transaction
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(CASectionsOuvertesManager manager, BTransaction transaction) throws Exception {
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
            CASectionsOuvertes section = (CASectionsOuvertes) manager.get(i);

            createRow();

            this.createCell(section.getIdExterneCompteAnnexe(), getStyleListLeft());
            this.createCell(section.getCompteAnnexe().getRole().getDescription(), getStyleListLeft());
            this.createCell(section.getIdExterne(), getStyleListLeft());
            this.createCell(JACalendar.format(section.getDateSection()), getStyleListLeft());
            this.createCell(JadeStringUtil.parseDouble("" + section.getSolde(), 0), getStyleMontant());
            this.createCell(getSession().getCodeLibelle(section.getIdModeCompensation()), getStyleListLeft());
            process.incProgressCounter();
        }

        return currentSheet;
    }
}
