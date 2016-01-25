package globaz.aquila.print.list;

import globaz.aquila.db.access.paiement.COPaiement;
import globaz.aquila.db.access.paiement.COPaiementManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class COListPaiementExcel extends COAbstractListExcel {

    private static final String NUMERO_REFERENCE_INFOROM = "0225GCO";

    public COListPaiementExcel(BSession session) {
        super(session, session.getLabel("PAIEMENT_LIST_TITLE_CONCAT"), session.getLabel("PAIEMENT_LIST_TITLE_COMPL"));
    }

    @Override
    public String getNumeroInforom() {
        return COListPaiementExcel.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * Critères
     * 
     * @author: sel Créé le : 20 oct. 06
     * @param sheet
     */
    private void initCritere() {
        //
        /*
         * if (!JadeStringUtil.isBlank(getCsSequence())) { createRow(); createCell(getSession().getLabel("SEQUENCE"),
         * getStyleCritereTitle()); try { createCell(CACodeSystem.getLibelle(getSession(), getCsSequence()),
         * getStyleCritere()); } catch (Exception e) { e.printStackTrace(); } } if
         * (!JadeStringUtil.isBlank(getCsEtape())) { createRow(); createCell(getSession().getLabel("ETAPE"),
         * getStyleCritereTitle()); try { createCell(CACodeSystem.getLibelle(getSession(), getCsEtape()),
         * getStyleCritere()); } catch (Exception e) { e.printStackTrace(); } }
         */
    }

    private void initListe() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(getSession().getLabel("NUMERO"));
        colTitles.add(getSession().getLabel("ROLE"));
        colTitles.add(getSession().getLabel("COMPTEANNEXE"));
        colTitles.add(getSession().getLabel("SECTION"));
        colTitles.add(getSession().getLabel("AQUILA_DATE_OPERATION"));
        colTitles.add(getSession().getLabel("MONTANT"));
        colTitles.add(getSession().getLabel("AQUILA_PROVENANCE_PAIEMENT"));
        colTitles.add(getSession().getLabel("ETAPE"));
        colTitles.add(getSession().getLabel("AQUILA_RUBRIQUE"));
        colTitles.add(getSession().getLabel("AQUILA_RUBRIQUE_DESCRIPTION"));

        createSheet(getSession().getLabel("LISTE"));

        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(COListPaiementExcel.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cellules
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Numéro
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // Role
        currentSheet.setColumnWidth((short) numCol++, (short) 15000); // Compte Annexe
        currentSheet.setColumnWidth((short) numCol++, (short) 15000); // Section
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // Date opération
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // montant
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // Provenance du paiement
        currentSheet.setColumnWidth((short) numCol++, (short) 15000); // étape
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // rubrique id externe
        currentSheet.setColumnWidth((short) numCol++, (short) 10000); // rubrique description

    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(COPaiementManager manager, BTransaction transaction) throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        COPaiement paiement = null;

        // création de la page
        initListe();

        // parcours du manager et remplissage des cell
        while (((paiement = (COPaiement) manager.cursorReadNext(statement)) != null) && !paiement.isNew()) {
            if (paiement != null) {
                // Recherche du compte annexe
                CACompteAnnexe compte = paiement.getCompteAnnexe();

                createRow();
                this.createCell(compte.getIdExterneRole(), getStyleListLeft()); // Numéro
                this.createCell(compte.getCARole().getDescription(), getStyleListLeft()); // Role
                this.createCell(compte.getDescription(), getStyleListLeft()); // Compte Annexe
                this.createCell(paiement.getSection().getIdExterne() + " " + paiement.getSection().getDescription(),
                        getStyleListLeft()); // Section
                this.createCell(paiement.getDateOperation(), getStyleListLeft()); // Date opération
                this.createCell(JANumberFormatter.format(paiement.getMontant()), getStyleListLeft()); // montant
                if (!JadeStringUtil.isEmpty(paiement.getLibelleProvenance())) {
                    this.createCell(paiement.getLibelleProvenance(), getStyleListLeft()); // Provenance du paiement
                } else {
                    this.createCell("", getStyleListLeft()); // Provenance du paiement
                }
                this.createCell(paiement.getLibelleEtape(), getStyleListLeft()); // étape
                if (paiement.getCompte() != null) {
                    this.createCell(paiement.getCompte().getIdExterne(), getStyleListLeft()); // rubrique id
                                                                                              // externe
                    this.createCell(paiement.getCompte().getDescription(), getStyleListLeft()); // rubrique
                                                                                                // description
                } else {
                    this.createCell("", getStyleListLeft());
                    this.createCell("", getStyleListLeft());
                }
            }
        }
        return currentSheet;
    }
}
