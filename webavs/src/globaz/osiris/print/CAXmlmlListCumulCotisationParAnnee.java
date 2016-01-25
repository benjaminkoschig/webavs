package globaz.osiris.print;

import globaz.globall.util.JACalendar;
import globaz.osiris.utils.CAOsirisContainer;

public class CAXmlmlListCumulCotisationParAnnee {

    private static final String DATA_EXCEL_ANNEE_COTISATION = "ANNEE_COTISATION";
    private static final String DATA_EXCEL_COMPANYNAME = "COMPANYNAME";
    private static final String DATA_EXCEL_DATE_DOC = "DATE_DOC";
    private static final String DATA_EXCEL_DATE_VALEUR = "DATE_VALEUR";
    private static final String DATA_EXCEL_MASSE = "MASSE";
    private static final String DATA_EXCEL_MONTANT = "MONTANT";
    private static final String DATA_EXCEL_NUMERO_INFOROM = "NUMERO_INFOROM";
    private static final String DATA_EXCEL_RUB_VALEUR = "RUB_VALEUR";
    private static final String DATA_EXCEL_RUBRIQUE = "RUBRIQUE";
    private static final String DATA_EXCEL_TITLE = "TITLE";
    private static final String DATA_EXCEL_TOTAL_MASSE = "TOTAL_MASSE";
    private static final String DATA_EXCEL_TOTAL_MONTANT = "TOTAL_MONTANT";

    private static final String LABEL_EXCEL_ANNEE_COTISATION = "LABEL_ANNEE_COTISATION";
    private static final String LABEL_EXCEL_DATE_DOC = "LABEL_DATE_DOC";
    private static final String LABEL_EXCEL_DATE_VALEUR = "LABEL_DATE_VALEUR";
    private static final String LABEL_EXCEL_MASSE = "LABEL_MASSE";
    private static final String LABEL_EXCEL_MONTANT = "LABEL_MONTANT";
    private static final String LABEL_EXCEL_RUBRIQUE = "LABEL_RUBRIQUE";
    private static final String LABEL_EXCEL_RUBRIQUE_VALEUR = "LABEL_RUBRIQUE_VALEUR";
    private static final String LABEL_EXCEL_TOTAL = "LABEL_TOTAL";

    public static final String NUMERO_INFOROM = "0202GCA";
    public static final String XLS_DOC_NAME = "CumulCotisationParAnnee";

    private String companyName = "";
    private CAOsirisContainer container = new CAOsirisContainer();
    private String dateValeur = "";
    private String labelAnnee = "";
    private String labelDateDoc = "";
    private String labelDateValeur = "";
    private String labelMasse = "";
    private String labelMontant = "";
    private String labelRubrique = "";
    private String labelTotal = "";
    private String rubValeur = "";
    private String title = "";
    private String totalMasse = "";
    private String totalMontant = "";

    public void createLigne(String rubrique, String anneeCotisation, String montant, String masse) {
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_RUBRIQUE, rubrique);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_ANNEE_COTISATION, anneeCotisation);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_MONTANT, montant);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_MASSE, masse);
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public String getLabelAnnee() {
        return labelAnnee;
    }

    public String getLabelDateDoc() {
        return labelDateDoc;
    }

    public String getLabelDateValeur() {
        return labelDateValeur;
    }

    public String getLabelMasse() {
        return labelMasse;
    }

    public String getLabelMontant() {
        return labelMontant;
    }

    public String getLabelRubrique() {
        return labelRubrique;
    }

    public String getLabelTotal() {
        return labelTotal;
    }

    public String getRubValeur() {
        return rubValeur;
    }

    public String getTitle() {
        return title;
    }

    public String getTotalMasse() {
        return totalMasse;
    }

    public String getTotalMontant() {
        return totalMontant;
    }

    private void loadDivers() {
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_NUMERO_INFOROM,
                CAXmlmlListCumulCotisationParAnnee.NUMERO_INFOROM);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_DATE_DOC, JACalendar.todayJJsMMsAAAA());
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_COMPANYNAME, companyName);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_TITLE, title);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_TOTAL_MASSE, totalMasse);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_TOTAL_MONTANT, totalMontant);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_DATE_VALEUR, dateValeur);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.DATA_EXCEL_RUB_VALEUR, rubValeur);

        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_DATE_VALEUR, labelDateValeur);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_DATE_DOC, labelDateDoc);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_TOTAL, labelTotal);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_ANNEE_COTISATION, labelAnnee);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_MASSE, labelMasse);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_MONTANT, labelMontant);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_RUBRIQUE, labelRubrique);
        container.addValue(CAXmlmlListCumulCotisationParAnnee.LABEL_EXCEL_RUBRIQUE_VALEUR, labelRubrique);

    }

    public CAOsirisContainer loadResults() {
        loadDivers();

        return container;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setLabelAnnee(String labelAnnee) {
        this.labelAnnee = labelAnnee;
    }

    public void setLabelDateDoc(String labelDateDoc) {
        this.labelDateDoc = labelDateDoc;
    }

    public void setLabelDateValeur(String labelDateValeur) {
        this.labelDateValeur = labelDateValeur;
    }

    public void setLabelMasse(String labelMasse) {
        this.labelMasse = labelMasse;
    }

    public void setLabelMontant(String labelMontant) {
        this.labelMontant = labelMontant;
    }

    public void setLabelRubrique(String labelRubrique) {
        this.labelRubrique = labelRubrique;
    }

    public void setLabelTotal(String labelTotal) {
        this.labelTotal = labelTotal;
    }

    public void setRubValeur(String rubValeur) {
        this.rubValeur = rubValeur;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalMasse(String totalMasse) {
        this.totalMasse = totalMasse;
    }

    public void setTotalMontant(String totalMontant) {
        this.totalMontant = totalMontant;
    }
}
