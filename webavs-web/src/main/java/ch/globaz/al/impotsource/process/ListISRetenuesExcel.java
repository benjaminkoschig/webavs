package ch.globaz.al.impotsource.process;

import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Pays;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import globaz.globall.db.BSession;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListISRetenuesExcel extends AbstractListExcel {
    private static final Logger LOG = LoggerFactory.getLogger(ListISRetenuesExcel.class);

    private final int COL_REF_PERMIS = 0;
    private final int COL_NSS = 1;
    private final int COL_NOM = 2;
    private final int COL_PRENOM = 3;
    private final int COL_DATE_NAISSANCE = 4;
    private final int COL_GENRE = 5;
    private final int COL_NUM_OFS = 6;
    private final int COL_NPA = 7;
    private final int COL_LOCALITE = 8;
    private final int COL_CANTON = 9;
    private final int COL_PERIODE_AF_DEBUT = 10;
    private final int COL_PERIODE_AF_FIN = 11;
    private final int COL_BAREME = 12;
    private final int COL_MONTANT = 13;
    private final int COL_IMPOTS = 14;

    private final String START_IMPOTS_COLUMN = "O8";
    private final String START_MONTANT_COLUMN = "N8";

    // Critères
    private String dateDebut;
    private String dateFin;
    private String canton;
    private String idProcessusAF;
    private String langueUser;

    private List<PrestationGroupee> prestationsAImprimer;

    public ListISRetenuesExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        setWantHeader(true);
        setWantFooter(true);
    }

    @Override
    public void createContent() {
        Langues langue = Langues.getLangueDepuisCodeIso(getLangueUser());
        String bareme = StringUtils.EMPTY;
        try {
            bareme = ALProperties.BAREME_IMPOT_SOURCE.getValue();
        } catch (PropertiesException e) {
            LOG.error("Un problème est survenu lors de la récupération de la propriété barème", e);
        }

        createNewSheet(getCodeLibelle(canton));
        createCriteres();
        createRow();
        createRow();
        createEntetes();
        for (PrestationGroupee eachPrestation : prestationsAImprimer) {
            createRow();
            createCell(eachPrestation.getReferencePermis(), getStyleListLeft());
            createCell(eachPrestation.getNss(), getStyleListLeft());
            createCell(eachPrestation.getNom(), getStyleListLeft());
            createCell(eachPrestation.getPrenom(), getStyleListLeft());
            createCell(eachPrestation.getDateNaissance().getSwissValue(), getStyleListLeft());
            if (StringUtils.equals(globaz.naos.translation.CodeSystem.SEXE_HOMME, eachPrestation.getGenre())){
                createCell("M", getStyleListLeft());
            } else if (StringUtils.equals(globaz.naos.translation.CodeSystem.SEXE_FEMME, eachPrestation.getGenre())) {
                createCell("F", getStyleListLeft());
            } else {
                createCell("", getStyleListLeft());
            }
            // N. OFS : inconnu dans WebAF.
            createCell(StringUtils.EMPTY, getStyleListLeft());
            createCell(eachPrestation.getNpa(), getStyleListLeft());
            createCell(eachPrestation.getLocalite(), getStyleListLeft());
            if (StringUtils.equals("100", eachPrestation.getPaysResidence())) {
                CodeSystem codeSystemCanton = CodeSystemUtil.getCodeSysteme(eachPrestation.getCantonResidence(), langue);
                createCell(codeSystemCanton.getCode(), getStyleListLeft());
            } else {
                Pays paysResidence = ALRepositoryLocator.getPaysRepository().findById(eachPrestation.getPaysResidence());
                switch (langue) {
                    case Francais:
                        createCell(paysResidence.getLibelleFr(), getStyleListLeft());
                        break;
                    case Allemand:
                        createCell(paysResidence.getLibelleAl(), getStyleListLeft());
                        break;
                    case Italien:
                        createCell(paysResidence.getLibelleIt(), getStyleListLeft());
                        break;
                    default:
                        createCell(StringUtils.EMPTY, getStyleListLeft());
                        break;
                }

            }
            createCell(eachPrestation.getDebutVersement().getSwissValue(), getStyleListLeft());
            createCell(eachPrestation.getFinVersement().getSwissValue(), getStyleListLeft());
            createCell(bareme, getStyleListLeft());
            createCell(eachPrestation.getMontantPrestations().doubleValue(), getStyleMontant());
            createCell(eachPrestation.getImpots().doubleValue(), getStyleMontant());
        }
        createTotaux();
    }

    private void createNewSheet(String name) {
        HSSFSheet sheet = createSheet(name);
        sheet.setAutobreaks(true);
        HSSFPrintSetup ps = initPage(true);
        ps.setFitWidth((short) 1);
        ps.setFitHeight((short) 0);
        createHeader();
        createFooter(getNumeroInforom());
        setWantHeader(true);
        setWantFooter(true);
        sheet.setColumnWidth((short) COL_REF_PERMIS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NSS, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_NOM, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PRENOM, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_GENRE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NUM_OFS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NPA, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_LOCALITE, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_CANTON, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_PERIODE_AF_DEBUT, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_PERIODE_AF_FIN, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_BAREME, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_MONTANT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_IMPOTS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        initPage(true);
    }

    private void createCriteres() {
        createRow();
        createCell(getLabel("LISTE_AF_RETENUES_TITRE").replace("{0}", dateDebut).replace("{1}", dateFin));
        createRow();
        createRow();
        createCell(getLabel("LISTE_AF_CANTON"), getStyleCritereTitle());
        createCell(getCodeLibelle(canton), getStyleCritere());
    }

    private void createEntetes() {
        createRow();
        createCell(getLabel("LISTE_AF_REF_PERMIS"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_NSS"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_NOM"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_PRENOM"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_DATE_NAISSANCE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_GENRE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_NUM_OFS"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_NPA"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_LOCALITE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_CANTON"), getStyleGris25PourcentGras());
        createMergedRegion(2, getLabel("LISTE_AF_PERIODE_AF"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_BAREME"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_MONTANT"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_IMPOTS"), getStyleGris25PourcentGras());
    }

    private void createTotaux() {
        createRow();
        createEmptyCell(13);
        createCellFormula(
                FORMULA_SUM + "(" + getRangeBetween(START_MONTANT_COLUMN, getReferenceValueFromCurrentCell(1, 0)) + ")",
                getStyleMontant());
        createCellFormula(
                FORMULA_SUM + "(" + getRangeBetween(START_IMPOTS_COLUMN, getReferenceValueFromCurrentCell(1, 0)) + ")",
                getStyleMontant());
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_AF_RETENUES;
    }

    public List<PrestationGroupee> getPrestationsAImprimer() {
        return prestationsAImprimer;
    }

    public void setPrestationsAImprimer(List<PrestationGroupee> prestationsAImprimer) {
        this.prestationsAImprimer = prestationsAImprimer;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setIdProcessusAF(String idProcessusAF) {
        this.idProcessusAF = idProcessusAF;
    }

    public String getLangueUser() {
        return langueUser;
    }

    public void setLangueUser(String langueUser) {
        this.langueUser = langueUser;
    }
}
