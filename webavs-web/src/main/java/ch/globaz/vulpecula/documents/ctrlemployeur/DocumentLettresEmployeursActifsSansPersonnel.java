package ch.globaz.vulpecula.documents.ctrlemployeur;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

/**
 * 
 * @author jwe
 * 
 */
public class DocumentLettresEmployeursActifsSansPersonnel extends VulpeculaDocumentManager<Employeur> {
    private static final String FREE_USER_INPUT_FIELD = " .........................................";

    private static final long serialVersionUID = 1L;

    private static final String P_CONCERNE = "P_CONCERNE";
    private static final String P_TITRE = "P_TITRE";
    private static final String P_P1 = "P_P1";
    private static final String P_P2 = "P_P2";
    private static final String P_P3 = "P_P3";
    private static final String P_SALUTATIONS = "P_SALUTATIONS";
    private static final String P_SIGNATURE = "P_SIGNATURE";
    // informations liées au formulaire à remplir par l'entreprise active et sans personnel
    private static final String P_DECLARATION = "P_DECLARATION";
    private static final String P_P4 = "P_P4";
    private static final String P_P5 = "P_P5";
    private static final String P_P6 = "P_P6";
    private static final String P_P7 = "P_P7";
    private static final String P_P8 = "P_P8";

    private String dateEnvoi;
    private String dateReference;

    public DocumentLettresEmployeursActifsSansPersonnel() throws Exception {
        this(null, "", "");
    }

    public DocumentLettresEmployeursActifsSansPersonnel(Employeur employeur, String dateEnvoi, String dateReference)
            throws Exception {
        super(employeur, DocumentConstants.LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_CT_NAME,
                DocumentConstants.LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_TYPE_NUMBER);
        setDateEnvoi(dateEnvoi);
        setDateReference(dateReference);
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_CT_NAME;
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_TEMPLATE;
    }

    @Override
    public void fillFields() throws Exception {
        setDocumentTitle(getCurrentElement().getAffilieNumero());
        setParametres(P_CONCERNE, getConcerne());
        setParametres(P_TITRE, formatTextWithFormulePolitesse(getTexte(1, 2)));
        setParametres(P_P1, getFirstParagraph());
        setParametres(P_P2, getTexte(3, 1));
        setParametres(P_P3, getTexte(4, 1));
        setParametres(P_SALUTATIONS, formatTextWithFormulePolitesse(getTexte(5, 1)));
        setParametres(P_SIGNATURE, getTexte(6, 1));
        setParametres(P_DECLARATION, getTexte(7, 1));
        setParametres(P_P4, getTexte(7, 2));
        setParametres(P_P5, getTexte(8, 1) + FREE_USER_INPUT_FIELD);
        setParametres(P_P6, getTexte(8, 2) + FREE_USER_INPUT_FIELD);
        setParametres(P_P7, FREE_USER_INPUT_FIELD + getTexte(8, 3) + FREE_USER_INPUT_FIELD);
        setParametres(P_P8, getTexte(9, 1));
    }

    private String getConcerne() throws Exception {
        return getTexte(1, 1) + " " + getDateReference();
    }

    private String formatTextWithFormulePolitesse(String texte) {

        CodeSystem codePolitesse = CodeSystemUtils.searchCodeSystemTraduction(
                getCurrentElement().getPolitesse(getCodeLangue()), getSession(), getCodeLangue().getCodeIsoLangue());

        return texte.replace("{titre}", codePolitesse.getTraduction());
    }

    private String getFirstParagraph() throws Exception {

        return getTexte(2, 1).replace("{annee}", getDateReference());

    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {

        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        beanReport.setAdresse(getCurrentElement().getAdressePrincipale().getAdresseFormatte());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setUser(getSession().getUserInfo());
        beanReport.setDate(getDateEnvoi());
        beanReport.setNoAffilie(getCurrentElement().getAffilieNumero());
        beanReport.setConfidentiel(true);
        return beanReport;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return CodeLangue.fromValue(getCurrentElement().getLangue());
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {

        return DocumentType.LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL.getCsCode();
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getDateReference() {
        return dateReference;
    }

    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

}
