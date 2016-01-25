package ch.globaz.vulpecula.documents.rappels;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.globall.util.JANumberFormatter;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.util.CodeSystemUtil;

public class DocumentTaxationOffice extends VulpeculaDocumentManager<TaxationOffice> {
    private static final long serialVersionUID = 1L;

    private static final String P_CONCERNE = "P_CONCERNE";
    private static final String P_TITRE = "P_TITRE";
    private static final String P_P1 = "P_P1";
    private static final String P_SALAIRES = "P_SALAIRES";
    private static final String P_P2 = "P_P2";
    private static final String P_P3 = "P_P3";
    private static final String P_P4 = "P_P4";
    private static final String P_SALUTATIONS = "P_SALUTATIONS";
    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static final String P_PS = "P_PS";

    public DocumentTaxationOffice() throws Exception {
        this(null);
    }

    public DocumentTaxationOffice(TaxationOffice taxationOffice) throws Exception {
        super(taxationOffice, DocumentConstants.TAXATION_OFFICE_CT_NAME, DocumentConstants.TAXATION_OFFICE_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.TAXATION_OFFICE_TEMPLATE;
    }

    @Override
    public void fillFields() throws Exception {
        setParametres(P_CONCERNE, getTexte(1, 1));
        setParametres(P_TITRE, getPolitesse());
        setParametres(P_P1, getParagraphe1());
        setParametres(P_SALAIRES, getSalaires());
        setParametres(P_P2, getTexte(4, 1));
        setParametres(P_P3, getTexte(5, 1));
        setParametres(P_P4, getParagraphe4());
        setParametres(P_SALUTATIONS, getSalutations());
        setParametres(P_SIGNATURE, getTexte(10, 1));
        setParametres(P_PS, getTexte(11, 1));

        fillDocumentTitle();
    }

    private String getSalutations() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put("titre", getPolitesse());

        return getTexteFormatte(9, 1, parametres);
    }

    /**
     * Retourne la politesse de l'employeur.
     * Si la politesse spécifique est inexistante, c'est le code système par défaut qui est affiché.
     * 
     * @return
     */
    private String getPolitesse() throws Exception {
        TaxationOffice taxationOffice = getCurrentElement();
        return CodeSystemUtil.getFormulePolitesse(getSession(), taxationOffice.getIdTiers());
    }

    private String getSalaires() throws Exception {
        String sommeSalaires = getCurrentElement().getMontant();

        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put("montant", JANumberFormatter.fmt(String.valueOf(sommeSalaires), true, true, false, 2));
        String phrase1 = getTexteFormatte(3, 1, parametres);

        return phrase1;
    }

    private String getParagraphe1() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put("periodeDebut", getCurrentElement().getPeriodeDebutWithDayAsSwissValue());
        parametres.put("periodeFin", getCurrentElement().getPeriodeFinWithDayAsSwissValue());
        String phrase1 = getTexteFormatte(2, 1, parametres);

        StringBuilder sb = new StringBuilder();
        sb.append(phrase1);
        sb.append("\n\n");
        sb.append(getTexte(2, 2));

        return sb.toString();
    }

    private String getParagraphe4() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(getTexte(6, 1));
        sb.append("\n\n");
        sb.append(getTexte(6, 2));

        return sb.toString();
    }

    private void fillDocumentTitle() {
        TaxationOffice taxationOffice = getCurrentElement();
        setDocumentTitle(taxationOffice.getEmployeurAffilieNumero());
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        TaxationOffice taxationOffice = getCurrentElement();

        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        beanReport.setAdresse(taxationOffice.getAdressePrincipaleFormattee());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());
        beanReport.setDate(Date.now().getSwissValue());
        beanReport.setNoAffilie(taxationOffice.getEmployeurAffilieNumero());
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
        return DocumentType.DECOMPTE_TAXATION_OFFICE.getCsCode();
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.TAXATION_OFFICE_CT_NAME;
    }
}
