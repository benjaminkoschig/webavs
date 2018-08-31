package ch.globaz.vulpecula.documents.rappels;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.I18NUtil;

public class DocumentSommation extends VulpeculaDocumentManager<Decompte> {
    private static final long serialVersionUID = 1L;

    private static final String P_CONCERNE = "P_CONCERNE";
    private static final String P_TITRE = "P_TITRE";
    private static final String P_P1 = "P_P1";
    private static final String P_PERIODE = "P_PERIODE";
    private static final String P_P2 = "P_P2";
    private static final String P_P3 = "P_P3";
    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static final String P_PS = "P_PS";

    public DocumentSommation() throws Exception {
        this(null);
    }

    public DocumentSommation(Decompte decompte) throws Exception {
        super(decompte, DocumentConstants.SOMMATION_CT_NAME, DocumentConstants.SOMMATION_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.SOMMATION_TEMPLATE;
    }

    @Override
    public void fillFields() throws Exception {
        Decompte decompte = getCurrentElement();
        CodeLangue codeLangue = decompte.getEmployeurLangue();
        Locale locale = I18NUtil.getLocaleOf(codeLangue);
        String politesse = CodeSystemUtil.getFormulePolitesse(getSession(), decompte.getIdTiers());
        setParametres(P_CONCERNE, getTexte(1, 1));
        setParametres(P_TITRE, politesse);
        setParametres(P_P1, getTexte(2, 1));

        if (TypeDecompte.COMPLEMENTAIRE.equals(decompte.getType())) {

        	setParametres(P_PERIODE, getLabel("COMPLEMENTAIRE") + " " + decompte.getPeriode().getAnneeDebut());

        } else {
            setParametres(P_PERIODE, decompte.getDescription(locale));
        }

        setParametres(P_P2, getTexte(2, 2));
        setParametres(P_P3, getParagraphe3(politesse));
        setParametres(P_SIGNATURE, getTexte(9, 1));
        setParametres(P_PS, getTexte(10, 1));

        fillDocumentTitle();
    }

    private String getParagraphe3(String politesse) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(getTexte(3, 1));
        sb.append("\n\n\n");
        sb.append(getTexte(3, 2));
        sb.append("\n\n\n");
        sb.append(getTexte(3, 3));
        sb.append("\n\n\n");
        sb.append(getTexte3_4Formatte(politesse));

        return sb.toString();
    }

    private String getTexte3_4Formatte(String politesse) throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put("titre", politesse);
        return getTexteFormatte(3, 4, parametres);
    }

    private void fillDocumentTitle() {
        Decompte decompte = getCurrentElement();
        setDocumentTitle(decompte.getEmployeurAffilieNumero());
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        Decompte decompte = getCurrentElement();

        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        beanReport.setAdresse(decompte.getFullAdressePrincipaleFormatte());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());
        beanReport.setDate(Date.now().getSwissValue());
        beanReport.setNoAffilie(decompte.getEmployeurAffilieNumero());
        beanReport.setConfidentiel(true);
        return beanReport;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return getCurrentElement().getEmployeurLangue();
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {
        return DocumentType.DECOMPTE_SOMMATION.getCsCode();
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.SOMMATION_CT_NAME;
    }
}
