package ch.globaz.vulpecula.documents.af;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.af.DocumentPrimeNaissanceAF.Element;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import globaz.caisse.report.helper.CaisseHeaderReportBean;

public class DocumentPrimeNaissanceAF extends VulpeculaDocumentManager<Element> {
    public static class Element implements Serializable {
        private static final long serialVersionUID = -8177852147562766687L;

        public Travailleur travailleur;
        public Date dateNaissance;
        public String nomEnfant;

        public String getAdressePrincipaleFormattee() {
            return travailleur.getAdressePrincipaleFormatee();
        }

        public String getNSS() {
            return travailleur.getNumAvsActuel();
        }
    }

    public DocumentPrimeNaissanceAF(Element element, String documentName, String numeroInforom) throws Exception {
        super(element, documentName, numeroInforom);
    }

    private static final long serialVersionUID = -856667097732888389L;

    private static final String P_CONCERNE = "P_CONCERNE";
    private static final String P_P1 = "P_P1";
    private static final String P_SIGNATURE = "P_SIGNATURE";

    private static final String PARAM_PRENOM_ENFANT = "prenomEnfant";
    private static final String PARAM_DATE_NAISSANCE_ENFANT = "dateNaissanceEnfant";

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
        return DocumentConstants.PRIME_NAISSANCE_AF_CT_NAME;
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.PRIME_NAISSANCE_AF_TEMPLATE;
    }

    @Override
    public void fillFields() throws Exception {
        setDocumentTitle(getCurrentElement().getNSS());
        setParametres(P_CONCERNE, getTexte(1, 1));
        setMainText();
        setParametres(P_SIGNATURE, getTexte(9, 1));

    }

    private void setMainText() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(PARAM_PRENOM_ENFANT, getCurrentElement().nomEnfant);
        parametres.put(PARAM_DATE_NAISSANCE_ENFANT, getCurrentElement().dateNaissance.getSwissValue());
        setParametres(P_P1, getTexteFormatte(2, 1, parametres));
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        beanReport.setAdresse(getCurrentElement().getAdressePrincipaleFormattee());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());
        beanReport.setDate(Date.now().getSwissValue());
        beanReport.setNoAffilie(getCurrentElement().getNSS());
        beanReport.setConfidentiel(true);
        return beanReport;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return CodeLangue.fromValue(getCurrentElement().travailleur.getLangue());
    }

}
