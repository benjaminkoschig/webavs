package ch.globaz.vulpecula.process.listeQuorums;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import globaz.globall.db.BSession;

public class ListeQuorumsExcel extends AbstractListExcel {

    private Map<String, List<LigneQuorums>> mapDetail;
    private Map<String, Map<String, int[][]>> mapTableau;
    private boolean detail;
    private Periode periode;

    public ListeQuorumsExcel(BSession session, String filenameRoot, String documentTitle,
            Map<String, List<LigneQuorums>> mapDetailExcel, Map<String, Map<String, int[][]>> mapTableauExel,
            boolean detail, Periode periode) {
        super(session, filenameRoot, documentTitle);
        mapDetail = mapDetailExcel;
        mapTableau = mapTableauExel;
        this.periode = periode;
        this.detail = detail;
    }

    @Override
    public void createContent() {

        if (detail) {
            for (Map.Entry<String, List<LigneQuorums>> entry : mapDetail.entrySet()) {
                List<LigneQuorums> lignes = entry.getValue();
                HSSFSheet sheet = createSheet(entry.getKey());
                createHeader();
                createFooter(getNumeroInforom());

                /*
                 * Activation des en-têtes et pieds de page
                 */
                setWantHeader(true);
                setWantFooter(true);

                /*
                 * Mise en page de la feuille
                 */
                HSSFPrintSetup printSetup;
                printSetup = initPage(false);
                printSetup.setFitWidth((short) 1);

                /*
                 * Création de l'en-tête
                 */
                createRow();
                createCell(getLabel("LISTE_QUORUMS_ENTETE_TITRE"), getStyleCritereTitle());
                setColNum(1);
                createCell(entry.getKey());
                createRow();
                createCell(getLabel("LISTE_QUORUMS_ENTETE_PERIODE"), getStyleCritereTitle());
                setColNum(1);
                createCell(periode.getDateDebutAsSwissValue() + " - " + periode.getDateFinAsSwissValue());
                createRow(2);

                /*
                 * Paramétrage des colonnes
                 */
                sheet.setColumnWidth((short) 0, (short) 5000);
                sheet.setColumnWidth((short) 1, (short) 5000);
                sheet.setColumnWidth((short) 2, (short) 3000);
                sheet.setColumnWidth((short) 3, (short) 3000);
                sheet.setColumnWidth((short) 4, (short) 3000);
                sheet.setColumnWidth((short) 5, (short) 7500);
                sheet.setColumnWidth((short) 6, (short) 7500);

                /*
                 * Création des en-tête de colonne
                 */
                createCell(getLabel("LISTE_QUORUMS_COL_AFFILIE"), getStyleListTitleLeft());
                createCell(getLabel("LISTE_QUORUMS_COL_NSS"), getStyleListTitleLeft());
                createCell(getLabel("LISTE_QUORUMS_COL_CONVENTION"), getStyleListTitleLeft());
                createCell(getLabel("LISTE_QUORUMS_COL_QUALIFICATION"), getStyleListTitleLeft());
                createCell(getLabel("LISTE_QUORUMS_COL_LANGUE"), getStyleListTitleLeft());
                createCell(getLabel("LISTE_QUORUMS_COL_SYNDICAT"), getStyleListTitleLeft());
                createCell(getLabel("LISTE_QUORUMS_COL_AVEC_SANS_SYNDICAT"), getStyleListTitleLeft());

                /*
                 * Insertion des lignes dans le fichier Excel
                 */
                for (LigneQuorums ligne : lignes) {
                    createRow();
                    createCell(ligne.getNoAffilie(), getStyleListLeft());
                    createCell(ligne.getNSS(), getStyleListLeft());
                    createCell(ligne.getCodeConvention(), getStyleListCenter());
                    createCell(ligne.getQualification(), getStyleListCenter());
                    createCell(ligne.getLangue(), getStyleListCenter());
                    createCell(ligne.getSyndicat(), getStyleListLeft());
                    createCell(getSyndicat(ligne.getSyndicat()), getStyleListLeft());
                }
            }
        }

        // Récupération des 3 maps (entreprises, travailleurs et syndiques)
        Map<String, int[][]> entreprises = new TreeMap<String, int[][]>(mapTableau.get("entreprises"));
        Map<String, int[][]> entreprisesSansPersonnel = new TreeMap<String, int[][]>(
                mapTableau.get("entreprisesSansPersonnel"));
        Map<String, int[][]> travailleurs = mapTableau.get("travailleurs");
        Map<String, int[][]> syndiques = mapTableau.get("syndiques");

        for (Map.Entry<String, int[][]> map : entreprises.entrySet()) {
            HSSFSheet sheet = createSheet(map.getKey());
            sheet.setAutobreaks(true);
            createHeader();
            createFooter(getNumeroInforom());

            HSSFPrintSetup printSetup;
            printSetup = initPage(false);
            printSetup.setFitWidth((short) 1);

            /*
             * Activation des en-têtes et pieds de page
             */
            setWantHeader(true);
            setWantFooter(true);

            /*
             * Création des critères
             */
            createRow();
            createCell(getLabel("LISTE_QUORUMS_ENTETE_PERIODE"), getStyleCritereTitle());
            setColNum(1);
            createCell(periode.getDateDebutAsSwissValue() + " - " + periode.getDateFinAsSwissValue());

            /*
             * Paramétrage des colonnes
             */
            sheet.setColumnWidth((short) 0, (short) 5000);
            sheet.setColumnWidth((short) 1, (short) 5000);
            sheet.setColumnWidth((short) 2, (short) 5500);
            sheet.setColumnWidth((short) 3, (short) 3000);
            sheet.setColumnWidth((short) 4, (short) 3000);

            // Données sur les entreprises
            createRow(3);
            createCell(getLabel("DETAIL_QUORUMS_ENTREPTISE"), getStyleGras());
            setColNum(1);
            createCell(map.getKey(), getStyleGras());
            createRow(2);
            setColNum(3);
            createCell(getLabel("DETAIL_QUORUMS_MEMBRE"), getStyleGras());
            setColNum(4);
            createCell(getLabel("DETAIL_QUORUMS_NON_MEMBRE"), getStyleGras());
            createRow();
            createCell(getLabel("DETAIL_QUORUMS_ENTREPTISE_TITREA"));
            createRow(2);
            setColNum(1);
            createCell(getLabel("DETAIL_QUORUMS_BAS_VALAIS"));
            setColNum(3);
            createCell(entreprises.get(map.getKey())[0][0]);
            setColNum(4);
            createCell(entreprises.get(map.getKey())[0][1]);
            createRow();
            setColNum(1);
            createCell(getLabel("DETAIL_QUORUMS_HAUT_VALAIS"));
            setColNum(3);
            createCell(entreprises.get(map.getKey())[1][0]);
            setColNum(4);
            createCell(entreprises.get(map.getKey())[1][1]);
            createRow(1);
            setColNum(2);
            createCell(getLabel("DETAIL_QUORUMS_SOUS_TOTAL"), getStyleListRightNone());
            setColNum(3);
            int sommeEntM = entreprises.get(map.getKey())[0][0] + entreprises.get(map.getKey())[1][0];
            createCell(sommeEntM);
            setColNum(4);
            int sommeEntNM = entreprises.get(map.getKey())[0][1] + entreprises.get(map.getKey())[1][1];
            createCell(sommeEntNM);
            setColNum(5);
            createCell(sommeEntM + sommeEntNM, getStyleListRightNone());
            createRow(3);
            createCell(getLabel("DETAIL_QUORUMS_RESULTAT"));
            setColNum(2);
            createCell(getLabel("DETAIL_QUORUMS_TOTAL"), getStyleListRightNone());
            setColNum(3);
            createCell(sommeEntM);
            setColNum(4);
            createCell(sommeEntNM);
            createRow(3);
            setColNum(2);
            createCell("en %", getStyleListRightNone());
            setColNum(3);
            createCell(getPourcentage(sommeEntM + sommeEntNM, sommeEntM) + "%", getStyleListRightNone());
            setColNum(4);
            createCell(getPourcentage(sommeEntM + sommeEntNM, sommeEntNM) + "%", getStyleListRightNone());
            createRow(4);
            // Sans personnel
            createCell(getLabel("DETAIL_QUORUMS_ENTREPTISESP_TITREC"));
            createRow(2);
            setColNum(1);
            createCell(getLabel("DETAIL_QUORUMS_BAS_VALAIS"));
            setColNum(3);
            createCell(entreprisesSansPersonnel.get(map.getKey())[0][0]);
            setColNum(4);
            createCell(entreprisesSansPersonnel.get(map.getKey())[0][1]);
            createRow();
            setColNum(1);
            createCell(getLabel("DETAIL_QUORUMS_HAUT_VALAIS"));
            setColNum(3);
            createCell(entreprisesSansPersonnel.get(map.getKey())[1][0]);
            setColNum(4);
            createCell(entreprisesSansPersonnel.get(map.getKey())[1][1]);
            createRow(1);
            setColNum(2);
            createCell(getLabel("DETAIL_QUORUMS_SOUS_TOTAL"), getStyleListRightNone());
            setColNum(3);
            int sommeEntSPM = entreprisesSansPersonnel.get(map.getKey())[0][0]
                    + entreprisesSansPersonnel.get(map.getKey())[1][0];
            createCell(sommeEntSPM);
            setColNum(4);
            int sommeEntSPNM = entreprisesSansPersonnel.get(map.getKey())[0][1]
                    + entreprisesSansPersonnel.get(map.getKey())[1][1];
            createCell(sommeEntSPNM);
            setColNum(5);
            createCell(sommeEntSPM + sommeEntSPNM, getStyleListRightNone());
            createRow(3);
            createCell(getLabel("DETAIL_QUORUMS_RESULTATSP"));
            setColNum(2);
            createCell(getLabel("DETAIL_QUORUMS_TOTAL"), getStyleListRightNone());
            setColNum(3);
            createCell(sommeEntSPM);
            setColNum(4);
            createCell(sommeEntSPNM);
            createRow(3);
            setColNum(2);
            createCell("en %", getStyleListRightNone());
            setColNum(3);
            createCell(getPourcentage(sommeEntSPM + sommeEntSPNM, sommeEntSPM) + "%", getStyleListRightNone());
            setColNum(4);
            createCell(getPourcentage(sommeEntSPM + sommeEntSPNM, sommeEntSPNM) + "%", getStyleListRightNone());
            createRow(4);

            // Données sur les travailleurs
            createCell(getLabel("DETAIL_QUORUMS_TRAVAILLEUR"), getStyleGras());
            createRow(2);
            setColNum(3);
            createCell(getLabel("DETAIL_QUORUMS_MEMBRE"), getStyleGras());
            setColNum(4);
            createCell(getLabel("DETAIL_QUORUMS_NON_MEMBRE"), getStyleGras());
            createRow();
            createCell(getLabel("DETAIL_QUORUMS_TRAVAILLEUR_TITREA"));
            createRow(2);
            setColNum(1);
            createCell(getLabel("DETAIL_QUORUMS_BAS_VALAIS"));
            setColNum(3);
            createCell(travailleurs.get(map.getKey())[0][0]);
            setColNum(4);
            createCell(travailleurs.get(map.getKey())[0][1]);
            createRow();
            setColNum(1);
            createCell(getLabel("DETAIL_QUORUMS_HAUT_VALAIS"));
            setColNum(3);
            createCell(travailleurs.get(map.getKey())[1][0]);
            setColNum(4);
            createCell(travailleurs.get(map.getKey())[1][1]);
            createRow(1);
            setColNum(2);
            createCell(getLabel("DETAIL_QUORUMS_SOUS_TOTAL"), getStyleListRightNone());
            setColNum(3);
            int sommeTraM = travailleurs.get(map.getKey())[0][0] + travailleurs.get(map.getKey())[1][0];
            createCell(sommeTraM);
            setColNum(4);
            int sommeTraNM = travailleurs.get(map.getKey())[0][1] + travailleurs.get(map.getKey())[1][1];
            createCell(sommeTraNM);
            setColNum(5);
            createCell(sommeTraM + sommeTraNM, getStyleListRightNone());
            createRow(3);
            createCell(getLabel("DETAIL_QUORUMS_RESULTAT"));
            setColNum(2);
            createCell(getLabel("DETAIL_QUORUMS_TOTAL"), getStyleListRightNone());
            setColNum(3);
            createCell(sommeTraM);
            setColNum(4);
            createCell(sommeTraNM);
            createRow(3);
            setColNum(2);
            createCell("en %", getStyleListRightNone());
            setColNum(3);
            createCell(getPourcentage(sommeTraM + sommeTraNM, sommeTraM) + "%", getStyleListRightNone());
            setColNum(4);
            createCell(getPourcentage(sommeTraM + sommeTraNM, sommeTraNM) + "%", getStyleListRightNone());
            createRow(4);

            // Données sur les syndiqués
            createCell(getLabel("DETAIL_QUORUMS_SYNDIQUE"), getStyleGras());
            createRow(2);
            setColNum(3);
            createCell(getLabel("DETAIL_QUORUMS_SYNDIQUE"), getStyleGras());
            setColNum(4);
            createCell(getLabel("DETAIL_QUORUMS_NON_SYNDIQUE"), getStyleGras());
            createRow();
            createCell(getLabel("DETAIL_QUORUMS_SYNDIQUE_TITREA"));
            createRow(2);
            setColNum(3);
            int nbSyndiques = syndiques.get(map.getKey())[0][0];
            createCell(nbSyndiques);
            setColNum(4);
            int nbNonSyndiques = syndiques.get(map.getKey())[1][0];
            createCell(nbNonSyndiques);
            int somme = nbSyndiques + nbNonSyndiques;
            setColNum(5);
            createCell(somme, getStyleListRightNone());
            createRow(3);
            createCell(getLabel("DETAIL_QUORUMS_RESULTAT"));
            setColNum(2);
            createCell("en %", getStyleListRightNone());
            setColNum(3);
            createCell(getPourcentage(somme, nbSyndiques) + "%", getStyleListRightNone());
            setColNum(4);
            createCell(getPourcentage(somme, nbNonSyndiques) + "%", getStyleListRightNone());
        }

    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTE_QUORUMS;
    }

    public String getSyndicat(String syndicat) {
        return syndicat == null ? getLabel("LISTE_QUORUMS_COL_SANS_SYNDICAT")
                : getLabel("LISTE_QUORUMS_COL_AVEC_SYNDICAT");
    }

    private double getPourcentage(int total, int val) {
        double result = ((double) val / (double) total) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        String dx = df.format(result);
        return Double.valueOf(dx);
    }
}
