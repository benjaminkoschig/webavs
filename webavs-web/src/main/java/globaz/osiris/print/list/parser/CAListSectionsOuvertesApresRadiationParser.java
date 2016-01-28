package globaz.osiris.print.list.parser;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.sections.CASectionsOuvertesApresRadiation;
import globaz.osiris.db.sections.CASectionsOuvertesApresRadiationManager;
import globaz.osiris.print.list.CAAbstractListExcel;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author jsi
 */
public class CAListSectionsOuvertesApresRadiationParser extends CAAbstractListExcel {

    // private static final String LABEL_AFFILIE = "AFFILIE";
    private static final String LABEL_CATEGORIE = "CATEGORIE";
    private static final String LABEL_DESCRIPTION_CA = "DESCRIPTION_COMPTEANNEXE";
    private static final String LABEL_DESCRIPTION_SECTION = "DESCRIPTION_SECTION";
    private static final String LABEL_ETAT = "ETAT";
    private static final String LABEL_LISTE = "LISTE";
    private static final String LABEL_NUMERO_CA = "TRICOMPTEANNEXENUMERO";
    private static final String LABEL_ROLE = "ROLE";
    private static final String LABEL_SECTION = "SECTION";
    private static final String LABEL_SEQUENCE = "SEQUENCE";
    private static final String LABEL_SOLDE = "SOLDE";
    private static final String NUMERO_REFERENCE_INFOROM = "122GCA";

    private BProcess process = null;

    /**
     * @param session
     * @param process
     */
    public CAListSectionsOuvertesApresRadiationParser(BSession session, BProcess process) {
        super(session, "CAListSectionsOuvertesParser", session.getLabel("IMPRESSION_LISTE_SECTIONS_OUVERTES"));
        this.process = process;
    }

    @Override
    public String getNumeroInforom() {
        return CAListSectionsOuvertesApresRadiationParser.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * Création de la page, ajout du titres des colonnes.
     * 
     * @return
     */
    private void initListe() {
        createSheet(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_LISTE));

        ArrayList<ParamTitle> titles = new ArrayList<ParamTitle>();
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_ROLE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_NUMERO_CA)));
        titles.add(new ParamTitle(getSession()
                .getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_DESCRIPTION_CA)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_CATEGORIE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_SECTION)));
        titles.add(new ParamTitle(getSession().getLabel(
                CAListSectionsOuvertesApresRadiationParser.LABEL_DESCRIPTION_SECTION)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_SOLDE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_SEQUENCE)));
        titles.add(new ParamTitle(getSession().getLabel(CAListSectionsOuvertesApresRadiationParser.LABEL_ETAT)));

        initTitleRow(titles);
        initPage(true);
        createHeader();
        createFooter(CAListSectionsOuvertesApresRadiationParser.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_ROLE);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_COMPTEANNEXE);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_3500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_SECTION);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_3500);
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_3500);
    }

    /**
     * Initialisation de la feuille xls.
     * 
     * @param manager
     * @param transaction
     * @return
     * @throws Exception
     */
    public HSSFSheet populateSheetListe(CASectionsOuvertesApresRadiationManager manager, BTransaction transaction)
            throws Exception {
        manager.setSession(getSession());
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (process.isAborted()) {
            return null;
        }

        if (!manager.isEmpty()) {
            initListe();
        }

        String etatErrone = getSession().getLabel("ETAT_SECTION_ERRONEE");
        String etatAVerifier = getSession().getLabel("ETAT_SECTION_A_VERIFIER");

        process.setProgressScaleValue(manager.size());

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CASectionsOuvertesApresRadiation section = (CASectionsOuvertesApresRadiation) manager.get(i);

            // Date de début de période
            String dp = section.getDateDebutPeriode();
            // Date de fin de période
            String fp = section.getDateFinPeriode();
            // Date de radiation
            String fd = section.getCompteAnnexe().getRole().getDateFin();
            if (!JadeStringUtil.isBlankOrZero(fd) && JadeDateUtil.isDateAfter(fp, fd)) {
                createRow();

                this.createCell(section.getCompteAnnexe().getRole().getDescription(), getStyleListLeft());
                this.createCell(section.getCompteAnnexe().getIdExterneRole(), getStyleListLeft());
                this.createCell(section.getCompteAnnexe().getDescription(), getStyleListLeft());
                this.createCell(section.getCategorieSection(), getStyleListLeft());
                this.createCell(section.getIdExterne(), getStyleListLeft());
                this.createCell(section.getDescription(), getStyleListLeft());
                this.createCell(JadeStringUtil.parseDouble("" + section.getSolde(), 0), getStyleMontant());
                this.createCell(section.getSequenceContentieux().getDescription(), getStyleListLeft());

                if (JadeDateUtil.isDateBefore(fd, dp)) {
                    this.createCell(etatErrone, getStyleListLeft());
                } else {
                    this.createCell(etatAVerifier, getStyleListLeft());
                }
                process.incProgressCounter();
            }
        }

        return currentSheet;
    }
}
