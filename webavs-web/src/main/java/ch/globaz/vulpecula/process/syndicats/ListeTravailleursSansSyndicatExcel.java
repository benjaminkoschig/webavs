package ch.globaz.vulpecula.process.syndicats;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.common.domaine.Date;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.syndicat.ListeTravailleursSansSyndicat;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class ListeTravailleursSansSyndicatExcel extends AbstractListExcel {
    private Map<Administration, List<Travailleur>> travailleurByCaisseMetier;
    private Map<String, List<ListeTravailleursSansSyndicat>> travailleursByCaisseMetier;
    private final int COL_N_AVS = 0;
    private final int COL_NOM = 1;
    private final int COL_PRENOM = 2;
    private final int COL_DATE_NAISSANCE = 3;

    private Annee annee;
    private String idCaisseMetier;

    public ListeTravailleursSansSyndicatExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    @Override
    public void createContent() {

        // for (Map.Entry<Administration, List<Travailleur>> travailleurs : travailleurByCaisseMetier.entrySet()) {
        // Administration caisseMetier = travailleurs.getKey();
        // createSheet(caisseMetier);
        // initPage(true);
        // createCriteres();
        // createRow();
        // createEntetes();
        // createRows(travailleurs.getValue());
        // }

        for (Map.Entry<String, List<ListeTravailleursSansSyndicat>> travailleurs : travailleursByCaisseMetier
                .entrySet()) {
            // Administration caisseMetier = travailleurs.getKey();
            // On met le libellé de la caisse métier du premier travailleur (ils ont tous la même)
            createSheetExcel(travailleurs.getValue().get(0).getLibCaisseMetier());
            createCriteres(travailleurs.getValue().get(0).getLibCaisseMetier());
            createRow();
            createEntetes();
            createRows(travailleurs.getValue());
        }
    }

    private void createCriteres(String libCaisseMetier) {
        createRow();
        createCell(getLabel("LISTE_SYNDICATS_ANNEE"), getStyleCritereTitle());
        createCell(annee.getValue());
        if (!JadeStringUtil.isBlank(idCaisseMetier)) {
            createRow();
            createCell(getLabel("LISTE_SYNDICATS_CAISSE_METIER"), getStyleCritereTitle());
            createCell(idCaisseMetier);
        }
    }

    private void createEntetes() {
        createRow();
        createCell(getLabel("LISTE_SYNDICATS_NO_AVS"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_NOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_PRENOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_DATE_DE_NAISSANCE"), getStyleListTitleLeft());
    }

    private void createRows(List<ListeTravailleursSansSyndicat> travailleurs) {
        for (ListeTravailleursSansSyndicat travailleur : travailleurs) {
            createRow();
            createCell(travailleur.getNumAvs(), getStyleListLeft());
            createCell(travailleur.getNomTravailleur(), getStyleListLeft());
            createCell(travailleur.getPrenomTravailleur(), getStyleListLeft());
            String dateNaiss = "";
            if (!JadeStringUtil.isBlank(travailleur.getDateNaissance())) {
                dateNaiss = new Date(travailleur.getDateNaissance()).getSwissValue();
            }
            createCell(dateNaiss, getStyleListLeft());
        }
    }

    private void createSheetExcel(String libCaisseMetier) {
        HSSFSheet sheet = createSheet(libCaisseMetier);
        initPage(false);
        setWantHeader(true);
        setWantFooter(true);
        sheet.setColumnWidth((short) COL_N_AVS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NOM, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_PRENOM, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_4500);
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT;
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        this.idCaisseMetier = idCaisseMetier;
    }

    /**
     * @param travailleurByCaisseMetier the travailleurByCaisseMetier to set
     */
    public void setTravailleurByCaisseMetier(Map<Administration, List<Travailleur>> travailleurByCaisseMetier) {
        this.travailleurByCaisseMetier = travailleurByCaisseMetier;
    }

    public void setTravailleursByCaisseMetier(
            Map<String, List<ListeTravailleursSansSyndicat>> travailleursByCaisseMetier) {
        this.travailleursByCaisseMetier = travailleursByCaisseMetier;
    }

}
