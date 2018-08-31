package ch.globaz.vulpecula.process.syndicats;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class ListeTravailleursSyndicatExcel extends AbstractListExcel {
    private Map<Administration, Map<Administration, List<AffiliationSyndicat>>> affiliationsGroupBySyndicat;

    private final int COL_N_AVS = 0;
    private final int COL_NOM = 1;
    private final int COL_PRENOM = 2;
    private final int COL_DATE_NAISSANCE = 3;
    private final int COL_PERIODE_DEBUT = 4;
    private final int COL_PERIODE_FIN = 5;

    private Annee annee;
    private String idTravailleur;

    public ListeTravailleursSyndicatExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public void setAffiliationsSyndicats(
            Map<Administration, Map<Administration, List<AffiliationSyndicat>>> affiliationsGroupBySyndicat) {
        this.affiliationsGroupBySyndicat = affiliationsGroupBySyndicat;
    }

    @Override
    public void createContent() {
        for (Map.Entry<Administration, Map<Administration, List<AffiliationSyndicat>>> affiliationsBySyndicat : affiliationsGroupBySyndicat
                .entrySet()) {
            Administration syndicat = affiliationsBySyndicat.getKey();

            for (Map.Entry<Administration, List<AffiliationSyndicat>> affiliationsByCaisseMetier : affiliationsBySyndicat
                    .getValue().entrySet()) {
                Administration caisseMetier = affiliationsByCaisseMetier.getKey();
                createSheet(syndicat, caisseMetier);
                createCriteres(syndicat, caisseMetier);
                createRow(2);
                createEntetes();
                createRows(affiliationsByCaisseMetier.getValue());
            }
        }
    }

    private void createSheet(Administration syndicat, Administration caisseMetier) {
        HSSFSheet sheet = createSheet(syndicat.getDesignation1() + "_" + caisseMetier.getDesignation1());
        initPage(false);
        setWantFooter(true);
        setWantHeader(true);
        sheet.setColumnWidth((short) COL_N_AVS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NOM, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PRENOM, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PERIODE_DEBUT, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_PERIODE_FIN, AbstractListExcel.COLUMN_WIDTH_DATE);
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
    }

    private void createRows(Collection<AffiliationSyndicat> affiliationsSyndicats) {
        for (AffiliationSyndicat affiliationSyndicat : affiliationsSyndicats) {
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
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }
}
