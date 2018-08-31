package ch.globaz.vulpecula.documents.af;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class DocumentAttestationAF extends VulpeculaDocumentManager<PrestationGroupee> {

    private static final long serialVersionUID = 1L;

    private static final String P_ALLOCATIONS_FAMILIALES = "P_ALLOCATIONS_FAMILIALES";
    private static final String P_TITRE = "P_TITRE";
    private static final String P_P1 = "P_P1";
    private static final String P_BENEFICIAIRE = "P_BENEFICIAIRE";
    private static final String P_MONTANT_PRESTATION = "P_MONTANT_PRESTATION";
    private static final String P_P2 = "P_P2";
    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static final String P_IMPOT_SOURCE = "P_IMPOT_SOURCE";
    private static final String P_PERIODE_AF = "P_PERIODE_AF";
    private static final String P_MONTANT_IMPOTS = "P_MONTANT_IMPOTS";
    private static final String P_MONTANT_VERSE = "P_MONTANT_VERSE";
    private static final String P_REFERENCES = "P_REFERENCES";
    private static final String P_NB = "P_NB";

    private static final String PARAM_DATE_DEBUT = "dateDebut";
    private static final String PARAM_DATE_FIN = "dateFin";
    private static final String PARAM_REFERENCE = "reference";
    private static final String PARAM_TEL = "tel";
    private static final String PARAM_EMAIL = "email";

    public DocumentAttestationAF() throws Exception {
        this(null);
    }

    public DocumentAttestationAF(PrestationGroupee prestation) throws Exception {
        super(prestation, DocumentConstants.ATTESTATION_AF_CT_NAME, DocumentConstants.ATTESTATION_AF_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.ATTESTATION_AF_TEMPLATE;
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
        return DocumentConstants.ATTESTATION_AF_CT_NAME;
    }

    @Override
    public void fillFields() throws Exception {
        setDocumentTitle(getCurrentElement().getNss());

        setReferences();
        setParametres(P_ALLOCATIONS_FAMILIALES, getTexte(1, 1));
        setParametresP1();

        String titre = getTitreToPrint();
        String[] titles = {titre};
        setParametres(P_TITRE, formatMessage(getTexte(10,1), titles));

        setParametres(P_BENEFICIAIRE, getCurrentElement().getRaisonSociale());
        setParametres(P_MONTANT_PRESTATION, getCurrentElement().getMontantPrestations().getValueNormalisee());
        setParametres(P_PERIODE_AF, getCurrentElement().getDebutVersement().getSwissValue() + " - "
                + getCurrentElement().getFinVersement().getLastDayOfMonth().getSwissValue());
        setParametres(P_IMPOT_SOURCE, getTexte(3, 1));
        setParametres(P_MONTANT_IMPOTS, getCurrentElement().getImpots().getValueNormalisee());
        setParametres(P_MONTANT_VERSE,
                getCurrentElement().getMontantPrestations().substract(getCurrentElement().getImpots())
                .getValueNormalisee());
        setParametres(P_P2, formatMessage(getTexte(8,1), titles));
        setParametres(P_SIGNATURE, getTexte(9, 1));
        setParametres(P_NB, getTexte(9, 2));
    }

    private String getTitreToPrint() throws Exception {
        Travailleur travAllocataire = VulpeculaRepositoryLocator.getTravailleurRepository().findByNss(getCurrentElement().getNss());
        if(travAllocataire.getIdTiers().equals(getCurrentElement().getIdTiersBeneficiaire())) {            
            TITiersViewBean bean = new TITiersViewBean();
            bean.setIdTiers(travAllocataire.getIdTiers());
            bean.retrieve();
            return bean.getFormulePolitesse(getCodeLangue().getValue());
        }else {
            TITiersViewBean bean = new TITiersViewBean();
            bean.setIdTiers(getCurrentElement().getIdTiersBeneficiaire());
            bean.retrieve();
            return bean.getFormulePolitesse(getCodeLangue().getValue());
        }
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
        parametres.put(PARAM_DATE_DEBUT, getCurrentElement().getDebutVersement().getFirstDayOfMonth().getSwissValue());
        parametres.put(PARAM_DATE_FIN, getCurrentElement().getFinVersement().getLastDayOfMonth().getSwissValue());
        setParametres(P_P1, getTexteFormatte(2, 1, parametres));
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
        Travailleur travAllocataire = VulpeculaRepositoryLocator.getTravailleurRepository().findByNss(getCurrentElement().getNss());

        Tiers tiersBeneficiaire = VulpeculaRepositoryLocator.getTiersRepository().findById(getCurrentElement().getIdTiersBeneficiaire());

        if(travAllocataire.getIdTiers().equals(tiersBeneficiaire.getIdTiers())) {
            CodeLangue langue = getCurrentElement().getLangue();
            if (langue != null) {
                return langue;
            } else {
                return CodeLangue.FR;
            }
        }else {            
            return CodeLangue.fromValue(tiersBeneficiaire.getLangue());
        }
    }  
}
