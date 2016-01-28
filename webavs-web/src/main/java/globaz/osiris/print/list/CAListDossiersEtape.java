package globaz.osiris.print.list;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAContentieuxManager;
import globaz.osiris.db.contentieux.CASequenceContentieux;
import globaz.osiris.parser.CASelectBlockParser;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Crée la liste des dossiers par étape
 * 
 * @author SEL
 * 
 */
public class CAListDossiersEtape extends CAAbstractListExcel {

    private static final String NUMERO_REFERENCE_INFOROM = "0136GCA";
    private String csEtape = "";
    private String sequence = "";

    // créé la feuille xls
    public CAListDossiersEtape(BSession session) {
        super(session, "ListDossiersEtape", "Liste des dossiers pour l'étape");
    }

    /**
     * @return the csEtape
     */
    public String getCsEtape() {
        return csEtape;
    }

    @Override
    public String getNumeroInforom() {
        return CAListDossiersEtape.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Critères
     * 
     * @author: sel Créé le : 20 oct. 06
     * @param sheet
     */
    private void initCritere() {
        //
        if (!JadeStringUtil.isBlank(getSequence())) {
            createRow();
            this.createCell(getSession().getLabel("SEQUENCE"), getStyleCritereTitle());

            if (!getSequence().equals("-1")) {
                CASequenceContentieux seq = new CASequenceContentieux();
                seq.setSession(getSession());
                seq.setIdSequenceContentieux(getSequence());
                try {
                    seq.retrieve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.createCell(seq.getDescription(), getStyleCritere());
            } else {
                this.createCell(getSession().getLabel(CASelectBlockParser.LABEL_TOUS), getStyleCritere());
            }
        }
        if (!JadeStringUtil.isBlank(getCsEtape())) {
            createRow();
            this.createCell(getSession().getLabel("ETAPE"), getStyleCritereTitle());
            try {
                this.createCell(CACodeSystem.getLibelle(getSession(), getCsEtape()), getStyleCritere());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Création de la page
     * 
     * @return
     */
    private void initListe() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("ROLE")));
        // colTitles.add(new ParamTitle(getSession().getLabel("SEQUENCE")));
        colTitles.add(new ParamTitle(getSession().getLabel("SECTION")));
        colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));
        colTitles.add(new ParamTitle(getSession().getLabel("NUM_POURSUITE")));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListDossiersEtape.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION); // Compte
        // Annexe
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_ROLE); // Role
        // currentSheet.setColumnWidth((short) numCol++, (short) 4000); //
        // Sequence
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_SECTION); // Section
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Solde
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_3500); // N°
        // poursuite
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(CAContentieuxManager manager, BTransaction transaction) throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        CASection contentieux = null;

        // création de la page
        initListe();
        while (((contentieux = (CASection) manager.cursorReadNext(statement)) != null) && (!contentieux.isNew())) {
            // parcours du manager et remplissage des cell
            if (contentieux != null) {
                // Recherche du compte annexe pour obtenir le motif de blocage
                CACompteAnnexe compte = (CACompteAnnexe) contentieux.getCompteAnnexe();

                createRow();
                this.createCell(compte.getIdExterneRole() + " " + compte.getDescription(), getStyleListLeft()); // CA
                this.createCell(compte.getRole().getDescription(), getStyleListLeft()); // Role
                // createCell(contentieux.getIdSequenceContentieux(),
                // getStyleListLeft()); // Sequence
                this.createCell(contentieux.getIdExterne(), getStyleListLeft()); // Section
                this.createCell(JadeStringUtil.parseDouble(contentieux.getSolde(), 0), false); // Solde
                this.createCell(contentieux.getNumeroPoursuite(), getStyleListLeft()); // N°
                // poursuite
            }
        }
        return currentSheet;
    }

    /**
     * @param csEtape
     *            the csEtape to set
     */
    public void setCsEtape(String csEtape) {
        this.csEtape = csEtape;
    }

    /**
     * @param sequence
     *            the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
