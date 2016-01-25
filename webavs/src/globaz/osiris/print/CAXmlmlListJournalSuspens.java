package globaz.osiris.print;

import globaz.globall.util.JACalendar;
import globaz.osiris.utils.CAOsirisContainer;

public class CAXmlmlListJournalSuspens {

    private static final String DATA_EXCEL_COMPANYNAME = "P_COMPANYNAME";
    private static final String DATA_EXCEL_DATE_DOC = "DATE_DOC";
    private static final String DATA_EXCEL_DESC_COMPTE_ANNEXE = "DESC_COMPTE_ANNEXE";
    private static final String DATA_EXCEL_ID_EXTERNE_SECTION = "ID_EXTERNE_SECTION";
    private static final String DATA_EXCEL_MESSAGE = "MESSAGE";
    private static final String DATA_EXCEL_MONTANT = "MONTANT";
    private static final String DATA_EXCEL_NOM = "NOM";
    private static final String DATA_EXCEL_NUMERO_INFOROM = "NUMERO_INFOROM";
    private static final String DATA_EXCEL_NUMERO_JOURNAL = "NUMERO_JOURNAL";
    private static final String DATA_EXCEL_P_TITLE = "P_TITLE";
    private static final String DATA_EXCEL_REF_BVR = "REF_BVR";
    private static final String DATA_EXCEL_SUM_MONTANT = "SUM_MONTANT";

    private static final String LABEL_EXCEL_DATE_DOC = "LABEL_DATE_DOC";
    private static final String LABEL_EXCEL_DESC_COMPTE_ANNEXE = "LABEL_DESC_COMPTE_ANNEXE";
    private static final String LABEL_EXCEL_ID_EXTERNE_SECTION = "LABEL_ID_EXTERNE_SECTION";
    private static final String LABEL_EXCEL_MESSAGE = "LABEL_MESSAGE";
    private static final String LABEL_EXCEL_MONTANT = "LABEL_MONTANT";
    private static final String LABEL_EXCEL_NOM = "LABEL_NOM";
    private static final String LABEL_EXCEL_NUMERO_JOURNAL = "LABEL_NUMERO_JOURNAL";
    private static final String LABEL_EXCEL_REF_BVR = "LABEL_REF_BVR";
    private static final String LABEL_EXCEL_SUM_MONTANT = "LABEL_SUM_MONTANT";

    public static final String NUMERO_INFOROM = "0188GCA";
    public static final String XLS_DOC_NAME = "ListeSuspensJournal";

    private String companyName = "";
    private CAOsirisContainer container = new CAOsirisContainer();
    private String labelDateDoc = "";
    private String labelDescCompteAnnexe = "";
    private String labelIdExterneSection = "";
    private String labelMessage = "";
    private String labelMontant = "";
    private String labelNom = "";
    private String labelNumeroJournal = "";
    private String labelRefBvr = "";
    private String labelSumMontant = "";
    private String numeroJournal = "";
    private String sumMontant = "";
    private String title = "";

    public void createLigne(String nom, String descCompteAnnexe, String idExterneSection, String montant,
            String refBvr, String message) {
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_NOM, nom);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_DESC_COMPTE_ANNEXE, descCompteAnnexe);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_ID_EXTERNE_SECTION, idExterneSection);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_MONTANT, montant);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_REF_BVR, refBvr);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_MESSAGE, message);
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLabelDateDoc() {
        return labelDateDoc;
    }

    public String getLabelDescCompteAnnexe() {
        return labelDescCompteAnnexe;
    }

    public String getLabelIdExterneSection() {
        return labelIdExterneSection;
    }

    public String getLabelMessage() {
        return labelMessage;
    }

    public String getLabelMontant() {
        return labelMontant;
    }

    public String getLabelNom() {
        return labelNom;
    }

    public String getLabelNumeroJournal() {
        return labelNumeroJournal;
    }

    public String getLabelRefBvr() {
        return labelRefBvr;
    }

    public String getLabelSumMontant() {
        return labelSumMontant;
    }

    public String getNumeroJournal() {
        return numeroJournal;
    }

    public String getSumMontant() {
        return sumMontant;
    }

    public String getTitle() {
        return title;
    }

    private void loadDivers() {
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_NUMERO_INFOROM,
                CAXmlmlListJournalSuspens.NUMERO_INFOROM);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_SUM_MONTANT, sumMontant);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_DATE_DOC, JACalendar.todayJJsMMsAAAA());
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_NUMERO_JOURNAL, numeroJournal);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_COMPANYNAME, companyName);
        container.addValue(CAXmlmlListJournalSuspens.DATA_EXCEL_P_TITLE, title);

        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_DESC_COMPTE_ANNEXE, labelDescCompteAnnexe);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_ID_EXTERNE_SECTION, labelIdExterneSection);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_MESSAGE, labelMessage);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_MONTANT, labelMontant);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_NOM, labelNom);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_REF_BVR, labelRefBvr);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_SUM_MONTANT, labelSumMontant);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_DATE_DOC, labelDateDoc);
        container.addValue(CAXmlmlListJournalSuspens.LABEL_EXCEL_NUMERO_JOURNAL, labelNumeroJournal);
    }

    public CAOsirisContainer loadResults() {
        loadDivers();

        return container;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setLabelDateDoc(String labelDateDoc) {
        this.labelDateDoc = labelDateDoc;
    }

    public void setLabelDescCompteAnnexe(String labelDescCompteAnnexe) {
        this.labelDescCompteAnnexe = labelDescCompteAnnexe;
    }

    public void setLabelIdExterneSection(String labelIdExterneSection) {
        this.labelIdExterneSection = labelIdExterneSection;
    }

    public void setLabelMessage(String labelMessage) {
        this.labelMessage = labelMessage;
    }

    public void setLabelMontant(String labelMontant) {
        this.labelMontant = labelMontant;
    }

    public void setLabelNom(String labelNom) {
        this.labelNom = labelNom;
    }

    public void setLabelNumeroJournal(String labelNumeroJournal) {
        this.labelNumeroJournal = labelNumeroJournal;
    }

    public void setLabelRefBvr(String labelRefBvr) {
        this.labelRefBvr = labelRefBvr;
    }

    public void setLabelSumMontant(String labelSumMontant) {
        this.labelSumMontant = labelSumMontant;
    }

    public void setNumeroJournal(String numeroJournal) {
        this.numeroJournal = numeroJournal;
    }

    public void setSumMontant(String sumMontant) {
        this.sumMontant = sumMontant;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
