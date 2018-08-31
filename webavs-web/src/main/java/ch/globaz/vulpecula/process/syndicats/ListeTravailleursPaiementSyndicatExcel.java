package ch.globaz.vulpecula.process.syndicats;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class ListeTravailleursPaiementSyndicatExcel extends AbstractListExcel {
    private Map<Administration, Map<Administration, List<AffiliationSyndicat>>> affiliationsGroupBySyndicat;
    private Map<Pair<Administration, Administration>, ParametreSyndicat> parametresSyndicats;

    private final int COL_N_AVS = 0;
    private final int COL_NOM = 1;
    private final int COL_PRENOM = 2;
    private final int COL_DATE_NAISSANCE = 3;
    private final int COL_PERIODE_DEBUT = 4;
    private final int COL_PERIODE_FIN = 5;
    private final int COL_COTISATIONS = 6;
    private final int COL_SALAIRE_ANNUEL = 7;

    private FWMemoryLog log;

    public FWMemoryLog getLog() {
        return log;
    }

    public void setLog(FWMemoryLog log) {
        this.log = log;
    }

    private final String REFERENCE_COTISATIONS_ROW = "G4";

    private Annee annee;
    private String idTravailleur;
    /**
     * Le nombre de travailleurs affichés sur la page excel
     */
    private int nbTravailleurs = 0;

    public ListeTravailleursPaiementSyndicatExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        setWantHeader(true);
        setWantFooter(true);
    }

    public void setAffiliationsSyndicats(
            Map<Administration, Map<Administration, List<AffiliationSyndicat>>> affiliationsGroupBySyndicat) {
        this.affiliationsGroupBySyndicat = affiliationsGroupBySyndicat;
    }

    public void setParametresSyndicats(
            Map<Pair<Administration, Administration>, ParametreSyndicat> parametresSyndicats) {
        this.parametresSyndicats = parametresSyndicats;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    @Override
    public void createContent() {
        for (Map.Entry<Administration, Map<Administration, List<AffiliationSyndicat>>> entry : affiliationsGroupBySyndicat
                .entrySet()) {
            Administration syndicat = entry.getKey();
            for (Map.Entry<Administration, List<AffiliationSyndicat>> affiliationsGroupByCaisseMetier : entry.getValue()
                    .entrySet()) {
                Administration caisseMetier = affiliationsGroupByCaisseMetier.getKey();
                List<AffiliationSyndicat> affiliationSyndicats = affiliationsGroupByCaisseMetier.getValue();
                ParametreSyndicat parametreSyndicat = parametresSyndicats
                        .get(new Pair<Administration, Administration>(syndicat, caisseMetier));
                if (parametreSyndicat == null) {
                    log.logMessage(
                            "Pas de paramétrage trouvé pour le syndicat " + syndicat.getDesignation1()
                                    + " pour le métier " + caisseMetier.getDesignation1(),
                            "Warn", this.getClass().getName());
                    continue;

                    // throw new IllegalStateException("Pas de paramétrage trouvé pour le syndicat "
                    // + syndicat.getDesignation1() + " pour le métier " + caisseMetier.getDesignation1());
                }

                caisseMetier.setDesignation1(caisseMetier.getDesignation1().replace('/', '_'));
                createSheet(syndicat, caisseMetier);
                createCriteres(syndicat, caisseMetier);
                createRow(2);
                createEntetes();
                createRows(affiliationSyndicats);

                createRow(2);
                clearStyle();
                createEmptyCell(5);
                createCell(getLabel("LISTE_SYNDICAT_TOTAL_SALAIRE"));
                createCellFormula(FORMULA_SUM + "("
                        + getRangeBetween(REFERENCE_COTISATIONS_ROW, getReferenceValueFromCurrentCell(2, 0)) + ")",
                        getStyleMontant());
                createRow();
                clearStyle();
                createEmptyCell(5);
                createCell(getLabel("LISTE_SYNDICAT_POURCENTAGE_REVERSE"));
                createCell(Double.parseDouble(parametreSyndicat.getPourcentage().getValue()), getStyleMontant());
                clearStyle();
                createRow();
                createEmptyCell(5);
                createCell(getLabel("LISTE_SYNDICAT_TOTAL_REVERSE"));
                createCellFormula("ROUND((" + getTopCellReferenceValue() + OP_DIVIDE_BY_100 + OP_MULTIPLY
                        + getTopCellReferenceValue(2) + ")/0.05;0)*0.05", getStyleMontant());
                createRow();
                createCell(getLabel("LISTE_SYNDICAT_NOMBRE_DE_TRAVAILLEURS"));
                createCell(nbTravailleurs, getStyleMontant());
                createRow();
                createCell(getLabel("LISTE_SYNDICAT_MONTANT_PAR_TRAVAILLEUR"));
                createCell(parametreSyndicat.getMontantParTravailleur(), getStyleMontant());
                createRow();
                createCell(getLabel("LISTE_SYNDICAT_TOTAL_TRAVAILLEURS"));
                createCellFormula(getTopCellReferenceValue() + OP_MULTIPLY + getTopCellReferenceValue(2),
                        getStyleMontant());
                clearStyle();
                createRow(2);
                createEmptyCell(2);
                createCell(getLabel("LISTE_SYNDICAT_MONTANT_TOTAL_REVERSE"));
                createCellFormula("ROUND((" + getReferenceValueFromCurrentCell(2, 2) + OP_ADD
                        + getReferenceValueFromCurrentCell(5, -3) + ")/0.05;0)*0.05", getStyleMontant());
            }
        }
    }

    private void createSheet(Administration syndicat, Administration caisseMetier) {
        HSSFSheet sheet = createSheet(syndicat.getDesignation1() + "_" + caisseMetier.getDesignation1());
        /*
         * Mise en page de la feuille
         */
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        printSetup = initPage(true);
        printSetup.setFitWidth((short) 1);
        printSetup.setFitHeight((short) 0);
        sheet.setAutobreaks(true);
        sheet.setColumnWidth((short) COL_N_AVS, (short) 7500);
        sheet.setColumnWidth((short) COL_NOM, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_PRENOM, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PERIODE_DEBUT, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_PERIODE_FIN, (short) 5000);
        sheet.setColumnWidth((short) COL_COTISATIONS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_SALAIRE_ANNUEL, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    private void createCriteres(Administration syndicat, Administration caisseMetier) {
        createRow();
        createCell(getLabel("LISTE_SYNDICATS_SYNDICAT"), getStyleCritereTitle());
        createCell(syndicat.getDesignation1() + " : " + caisseMetier.getDesignation1(), getStyleCritere());
        if (!JadeStringUtil.isBlank(idTravailleur)) {
            createRow();
            createCell(getLabel("LISTE_SYNDICATS_ID_TRAVAILLEUR"), getStyleCritereTitle());
            createCell(Integer.valueOf(idTravailleur));
        }
        createRow();
        createCell(getLabel("LISTE_SYNDICATS_ANNEE"), getStyleCritereTitle());
        createCell(annee.getValue());
    }

    private void createEntetes() {
        createCell(getLabel("LISTE_SYNDICATS_NO_AVS"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_NOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_PRENOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_DATE_DE_NAISSANCE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_PERIODE_DEBUT"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_PERIODE_FIN"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_COTISATIONS"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_SALAIRE"), getStyleListTitleLeft());
    }

    private void createRows(Collection<AffiliationSyndicat> affiliationsSyndicats) {
        setNbTravailleurs(0);
        for (AffiliationSyndicat affiliationSyndicat : affiliationsSyndicats) {

            List<PosteTravail> postesTravail = VulpeculaServiceLocator.getPosteTravailService()
                    .findPostesActifDurantAnnee(affiliationSyndicat.getIdTravailleur(), annee);
            if (!postesTravail.isEmpty()) {
                // Récupère le premier taux CPR trouvé dans la liste des postes de travail
                Taux taux = VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository()
                        .findTauxCPRTravailleurByAnnee(postesTravail, annee);
                // Si le taux est différent de zéro, la personne possède au moins une cotisation CPR
                if (taux != null) {
                    createRow();
                    createCell(affiliationSyndicat.getNoAVSTravailleur(), getStyleListLeft());
                    createCell(affiliationSyndicat.getNomTravailleur(), getStyleListLeft());
                    createCell(affiliationSyndicat.getPrenomTravailleur(), getStyleListLeft());
                    createCell(affiliationSyndicat.getDateNaissanceTravailleur(), getStyleListLeft());
                    createCell(affiliationSyndicat.getDateDebutAsSwissValue(), getStyleListLeft());
                    if (affiliationSyndicat.getDateFinAsSwissValue() != null) {
                        createCell(affiliationSyndicat.getDateFinAsSwissValue(), getStyleListLeft());
                    } else {
                        createEmptyCell();
                    }
                    Montant montant = affiliationSyndicat.getCumulSalaires();
                    Montant montantCotisation = montant.multiply(taux);
                    createCell(Double.valueOf(montantCotisation.getValueNormalisee()), getStyleMontant());
                    createCell(Double.valueOf(montant.getValueNormalisee()), getStyleMontant());
                    nbTravailleurs++;
                }
            }
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_PAIEMENT_SYNDICAT;
    }

    public void setNbTravailleurs(int nbTravailleurs) {
        this.nbTravailleurs = nbTravailleurs;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

}
