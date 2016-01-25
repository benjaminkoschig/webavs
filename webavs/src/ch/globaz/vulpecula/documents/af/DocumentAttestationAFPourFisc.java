package ch.globaz.vulpecula.documents.af;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.framework.printing.itext.exception.FWIException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public class DocumentAttestationAFPourFisc extends VulpeculaDocumentManager<PrestationGroupee> {

    private static final long serialVersionUID = 1L;

    private static final String P_DECLARATION_IMPOT = "P_DECLARATION_IMPOT";
    private static final String P_TITRE = "P_TITRE";
    private static final String P_ALLOCATIONS_FAMILIALES = "P_ALLOCATIONS_FAMILIALES";
    private static final String P_P1 = "P_P1";
    private static final String P_AFFILIE = "P_AFFILIE";
    private static final String P_GENRE_PRESTATIONS = "P_GENRE_PRESTATIONS";
    private static final String P_MONTANTS = "P_MONTANTS";
    private static final String P_MONTANT = "P_MONTANT";
    private static final String P_P2 = "P_P2";
    private static final String P_P3 = "P_P3";
    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static final String P_REFERENCES = "P_REFERENCES";
    private static final String P_NB = "P_NB";

    private static final String PARAM_ANNEE = "annee";
    private static final String PARAM_REFERENCE = "reference";
    private static final String PARAM_TEL = "tel";
    private static final String PARAM_EMAIL = "email";

    public DocumentAttestationAFPourFisc() throws Exception {
        this(null);
    }

    public DocumentAttestationAFPourFisc(PrestationGroupee prestation) throws Exception {
        super(prestation, DocumentConstants.ATTESTATION_AF_FISC_CT_NAME,
                DocumentConstants.ATTESTATION_AF_FISC_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.ATTESTATION_AF_FISC_TEMPLATE;
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {
        return DocumentType.AF.getCsCode();
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.ATTESTATION_AF_FISC_CT_NAME;
    }

    @Override
    public void fillFields() throws Exception {
        setReferences();
        setDeclarationImpot();
        setParametres(P_TITRE, getCodeLibelle(getCurrentElement().getTitre()));
        setParametresP1();
        setAffilie();
        setParametres(P_GENRE_PRESTATIONS, getTexte(3, 1));
        setParametres(P_MONTANTS, getTexte(3, 2));
        setParametres(P_MONTANT, getCurrentElement().getMontantPrestations().getValue());
        setParametres(P_ALLOCATIONS_FAMILIALES, getTexte(3, 3));
        setParametres(P_P2, getTexte(8, 1));
        setParametres(P_P3, getTexte(8, 2));
        setParametres(P_SIGNATURE, getTexte(9, 1));
        setParametres(P_NB, getTexte(9, 2));

        setDocumentTitle(getCurrentElement().getNss());
    }

    private void setDeclarationImpot() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(PARAM_ANNEE, String.valueOf(getCurrentElement().getDebutVersement().getYear()));
        setParametres(P_DECLARATION_IMPOT, getTexteFormatte(1, 1, parametres));
    }

    private void setReferences() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(PARAM_REFERENCE, getSession().getUserFullName());
        parametres.put(PARAM_TEL, getSession().getUserInfo().getPhone());
        parametres.put(PARAM_EMAIL, getSession().getUserEMail());
        setParametres(P_REFERENCES, getTexteFormatte(4, 1, parametres));
    }

    private void setParametresP1() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(PARAM_ANNEE, String.valueOf(getCurrentElement().getDebutVersement().getYear()));
        setParametres(P_P1, getTexteFormatte(2, 1, parametres));
    }

    private void setAffilie() throws Exception {
        PrestationGroupee entetePrestation = getCurrentElement();
        StringBuilder sb = new StringBuilder();
        sb.append(entetePrestation.getRaisonSociale());
        sb.append(" - ");
        sb.append(entetePrestation.getLocalite());
        setParametres(P_AFFILIE, sb.toString());
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        beanReport.setAdresse(getCurrentElement().getAdresseFormattee());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());
        beanReport.setDate(Date.now().getSwissValue());
        beanReport.setNoAffilie(getCurrentElement().getNss());
        beanReport.setConfidentiel(true);
        return beanReport;
    }

    @Override
    public CodeLangue getCodeLangue() {
        CodeLangue langue = getCurrentElement().getLangue();
        if (langue != null) {
            return langue;
        } else {
            return CodeLangue.FR;
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        StringBuilder sb = new StringBuilder();
        sb.append(getCurrentElement().getNss());
        sb.append(getCurrentElement().getDebutVersement().getYear());
        sb.append(0);
        getDocumentInfo().setBarcode(sb.toString());
    }
}
