package ch.globaz.vulpecula.process.is;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.is.DetailPrestationAF;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import globaz.globall.db.BSession;

/**
 * Classe permettant de préparer un classeur Excel de contenu de prestations détaillées pour la liste des AF versées.
 *
 * @author jwe
 *
 */
public class ListAFVerseesExcel extends AbstractListExcel {
    private final int COL_CAISSE_AF = 0;
    private final int COL_ID_TRAVAILLEUR = 1;
    private final int COL_TRAVAILLEUR = 2;
    private final int COL_NO_AFFILIE = 3;
    private final int COL_EMPLOYEUR = 4;
    private final int COL_BENEFICIAIRE = 5;
    private final int COL_TIERS_BENEFICIAIRE = 6;
    private final int COL_PERIODE = 7;
    private final int COL_DATE_VERSEMENT = 8;
    private final int COL_MONTANT_VERSE = 9;
    private final int COL_MONTANT_IS = 10;
    private final int COL_TOTAL = 11;
    private final int COL_NO_DOSSIER = 12;

    private Map<String, Collection<DetailPrestationAF>> prestationsAImprimer;
    private Date dateDebut;
    private Date dateFin;

    public ListAFVerseesExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    @Override
    public void createContent() {
        createSheet();
        createTitle();
        createEntetes();
        SortedSet<String> orderedKeys = new TreeSet<String>(prestationsAImprimer.keySet());
        Montant totalMontantVerse = Montant.ZERO;
        Montant totalMontantIS = Montant.ZERO;
        Montant totalTotaux = Montant.ZERO;

        for (String keyAF : orderedKeys) {

            Collection<DetailPrestationAF> detailPrestAFList = prestationsAImprimer.get(keyAF);

            for (DetailPrestationAF detailPrest : detailPrestAFList) {

                createRow();
                createCell(detailPrest.getLibelleCaisseAF(), getStyleListLeft());
                createCell(detailPrest.getIdTiersAllocataire(), getStyleListLeft());
                createCell(detailPrest.getNomAllocataire() + " " + detailPrest.getPrenomAllocataire(),
                        getStyleListLeft());
                createCell(detailPrest.getNumeroAffilie(), getStyleListLeft());
                createCell(detailPrest.getRaisonSocialEmployeur(), getStyleListLeft());
                String beneficiaire = "Tiers";
                String nomPrenomBenef = detailPrest.getNomTiersBeneficiaire() + " "
                        + detailPrest.getPrenomTiersBeneficiaire();
                if (detailPrest.isBenefEmployeur()) {
                    beneficiaire = "Employeur";
                    nomPrenomBenef = "";
                }
                createCell(beneficiaire, getStyleListLeft());
                createCell(nomPrenomBenef, getStyleListLeft());
                Date datePeriodeDeb = new Date(detailPrest.getPeriodeDebut());
                Date datePeriodeFin = new Date(detailPrest.getPeriodeFin());
                createCell(datePeriodeDeb.getMoisAnneeFormatte() + "-" + datePeriodeFin.getMoisAnneeFormatte(),
                        getStyleListRight());
                Date dateComptabilisation = new Date(detailPrest.getDateComptabilisation());
                createCell(dateComptabilisation.getSwissValue(), getStyleListRight());
                Montant montantVerse = detailPrest.getMontantPrestation()
                        .substract(detailPrest.getMontantImpotSource());
                createCell(montantVerse, getStyleMontant());
                totalMontantVerse = totalMontantVerse.add(montantVerse);
                createCell(detailPrest.getMontantImpotSource(), getStyleMontant());
                totalMontantIS = totalMontantIS.add(detailPrest.getMontantImpotSource());
                createCell(detailPrest.getMontantPrestation(), getStyleMontant());
                totalTotaux = totalTotaux.add(detailPrest.getMontantPrestation());
                createCell(detailPrest.getNumeroDossier(), getStyleListRight());
            }

        }
        setFooter(totalMontantVerse, totalMontantIS, totalTotaux);
    }

    private void setFooter(Montant totalMontantVerse, Montant totalMontantIS, Montant totalTotaux) {

        createRow();
        createCell(getLabel("LISTE_AF_VERSEES_TOTAL_GENERAL"), getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell("", getStyleGris25PourcentGras());
        createCell(totalMontantVerse, getStyleGris25PourcentGrasMontant());
        createCell(totalMontantIS, getStyleGris25PourcentGrasMontant());
        createCell(totalTotaux, getStyleGris25PourcentGrasMontant());
        createCell("", getStyleGris25PourcentGras());

    }

    private void createSheet() {
        HSSFSheet sheet = createSheet("Liste");
        sheet.setAutobreaks(true);

        /*
         * Mise en page de la feuille Excel
         */
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        printSetup = initPage(true);
        printSetup.setFitWidth((short) 1);
        printSetup.setFitHeight((short) 0);

        sheet.setColumnWidth((short) COL_CAISSE_AF, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_ID_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_NO_AFFILIE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_EMPLOYEUR, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_BENEFICIAIRE, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_TIERS_BENEFICIAIRE, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_PERIODE, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DATE_VERSEMENT, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_MONTANT_VERSE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_MONTANT_IS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_TOTAL, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NO_DOSSIER, AbstractListExcel.COLUMN_WIDTH_4500);

    }

    private void createTitle() {
        createRow();
        createCell(getLabel("LISTE_AF_VERSEES_TITLE"));
        createRow();
        createCell(getLabel("LISTE_AF_VERSEES_PERIODE"));
        createCell(getDateDebut().getFirstDayOfMonth().getSwissValue() + "-"
                + getDateFin().getLastDayOfMonth().getSwissValue());
    }

    private void createEntetes() {
        createRow();
        createRow();
        createRow();
        createRow();
        createCell(getLabel("LISTE_AF_VERSEES_CAISSE_AF"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_ID_TRAVAILLEUR"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_TRAVAILLEUR"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_NO_AFFILIE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_EMPLOYEUR"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_BENEFICIAIRE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_TIERS_BENEFICIAIRE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_PERIODE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_DATE_VERSEMENT"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_MONTANT_VERSE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_MONTANT_IS"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_MONTANT_TOTAL"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_VERSEES_NO_DOSSIER"), getStyleGris25PourcentGras());
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_AF_RETENUES;
    }

    public void setPrestationsAImprimer(Map<String, Collection<DetailPrestationAF>> prestationsAImprimer) {
        this.prestationsAImprimer = prestationsAImprimer;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

}
