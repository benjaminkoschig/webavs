package globaz.osiris.print.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteCourant;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author BJO<br>
 *         <h1>description:</h1> Génère un décompte annuel mentionnant les montants FSFP facturés cumulés pour l'année
 *         comptable, les montants encaissés/compensés cumulés, le total des soldes ouverts et le total des soldes en
 *         poursuite / irrécouvrable
 */
public class CAListDecompteAnnuelFonds extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0195GCA";

    private String dateDebutPeriode = new String();
    private String dateFinPeriode = new String();
    private String forIdCompteCourant = new String();
    private BTransaction transaction;

    public CAListDecompteAnnuelFonds(BSession session, BTransaction transaction) {
        super(session, "ListDecompteAnnuelFonds", "Décompte annuel autre tâche");
        this.transaction = transaction;
    }

    protected void _executeCleanUp() {
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * @return Returns the forIdCompteCourant.
     */
    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    @Override
    public String getNumeroInforom() {
        return CAListDecompteAnnuelFonds.NUMERO_REFERENCE_INFOROM;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    private void initCritere() {
        //
        if (!JadeStringUtil.isBlank(getForIdCompteCourant())) {
            createRow();
            this.createCell(getSession().getLabel("COMPTECOURANT"), getStyleCritereTitle());

            CACompteCourant compteCourant = new CACompteCourant();
            compteCourant.setSession(getSession());
            compteCourant.setIdCompteCourant(getForIdCompteCourant());

            try {
                compteCourant.retrieve(getTransaction());
                this.createCell(compteCourant.getIdExterne() + " - " + compteCourant.getRubrique().getDescription());
            } catch (Exception e) {
            }

        }
        if (!JadeStringUtil.isBlank(getDateDebutPeriode())) {
            createRow();
            this.createCell(getSession().getLabel("PERIODE"), getStyleCritereTitle());
            this.createCell(getDateDebutPeriode() + " - " + getDateFinPeriode(), getStyleCritere());
        }
    }

    private void initListe() {

        // création de la page
        createSheet(getSession().getLabel("LIST_DECOMPTE_ANNUEL_FONDS"));

        initCritere();
        initPage(false);
        createHeader();
        createFooter(CAListDecompteAnnuelFonds.NUMERO_REFERENCE_INFOROM);

        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION); // description
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // montant
    }

    /**
     * <H1>Description :</H1> Méthode permettant de créer un fichier excel et de le remplire
     * 
     * @return String : chemin du fichier
     * @throws Exception
     */
    public HSSFSheet populateSheet(ArrayList idExterneFsfp, ArrayList sommeMontantFsfp,
            ArrayList idExterneEncaisseCompense, ArrayList sommeMontantEncaisseCompense,
            FWCurrency sommeMontantFsfpTotal, FWCurrency sommeMontantEncaisseCompenseTotal,
            FWCurrency sommeMontantSoldeOuvert, FWCurrency sommeMontantPoursuiteIrrecouvrableTotal) throws Exception {

        initListe();
        createRow();// ligne vide

        // Impression de la partie cotisation
        createRow();
        this.createCell(getSession().getLabel("COTISATION"), getStyleGris25Pourcent());
        this.createCell("", getStyleGris25Pourcent());

        for (int i = 0; i < idExterneFsfp.size(); i++) {
            createRow();
            this.createCell(idExterneFsfp.get(i).toString(), getStyleListLeft());
            this.createCell(sommeMontantFsfp.get(i).toString(), getStyleMontant());
        }

        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListLeft());
        this.createCell(sommeMontantFsfpTotal.toString(), getStyleMontantTotal());
        createRow();// ligne vide

        // Impression de la partie encaisse/compense
        createRow();
        this.createCell(getSession().getLabel("MONTANTS_ENCAISSES_COMPENSES_VERSES"), getStyleGris25Pourcent());
        this.createCell("", getStyleGris25Pourcent());

        for (int i = 0; i < idExterneEncaisseCompense.size(); i++) {
            createRow();
            this.createCell(idExterneEncaisseCompense.get(i).toString(), getStyleListLeft());
            this.createCell(sommeMontantEncaisseCompense.get(i).toString(), getStyleMontant());
        }

        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListLeft());
        this.createCell(sommeMontantEncaisseCompenseTotal.toString(), getStyleMontantTotal());
        createRow();// ligne vide

        // Impression du solde ouvert
        createRow();
        this.createCell(getSession().getLabel("SOLDE_OUVERT"), getStyleGris25Pourcent());
        this.createCell("", getStyleGris25Pourcent());

        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListLeft());
        this.createCell(sommeMontantSoldeOuvert.toString(), getStyleMontantTotal());
        createRow();// ligne vide

        // Impression du montant en poursuite ou irrécouvrable
        createRow();
        this.createCell(getSession().getLabel("MONTANT_POURSUITE_IRRECOUVRABLE"), getStyleGris25Pourcent());
        this.createCell("", getStyleGris25Pourcent());

        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListLeft());
        this.createCell(sommeMontantPoursuiteIrrecouvrableTotal.toString(), getStyleMontantTotal());

        return currentSheet;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    /**
     * @param forIdCompteCourant
     *            The forIdCompteCourant to set.
     */
    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }
}
