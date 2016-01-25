package globaz.corvus.itext;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.process.REGenererListesVerificationProcess;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.webavs.common.CommonProperties;
import java.util.Calendar;

/**
 * @author SCR
 */
public class REListeRecapitulativePaiementPC_RFM extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FICHIER_MODELE = "RE_RECAP_PRESTATIONS_PCRFM";
    public static final String FICHIER_RESULTAT = "recap_paiement_pcrfm";

    private RERecapitulationPaiementAdapter adapter;
    private String forMoisAnnee = "";
    private boolean hasNext = true;

    public REListeRecapitulativePaiementPC_RFM() {
        super();
    }

    public REListeRecapitulativePaiementPC_RFM(BProcess parent) throws FWIException {
        super(parent, REApplication.APPLICATION_CORVUS_REP, REListeRecapitulativePaiementPC_RFM.FICHIER_RESULTAT);
    }

    public REListeRecapitulativePaiementPC_RFM(BSession session) throws FWIException {
        super(session, REApplication.APPLICATION_CORVUS_REP, REListeRecapitulativePaiementPC_RFM.FICHIER_RESULTAT);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentProperty(REGenererListesVerificationProcess.PROPERTY_DOCUMENT_ORDER,
                REGenererListesVerificationProcess.LISTE_RECAP_PAIEM_ORDER);

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_RECAPITULATIVE_DU_PAIEMENT);
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            if (adapter == null) {
                adapter = new RERecapitulationPaiementAdapter(getSession(), getForMoisAnnee());
                adapter.chargerParGenrePrestation();
            }

            setTemplateFile(REListeRecapitulativePaiementPC_RFM.FICHIER_MODELE);

            Calendar cal = Calendar.getInstance();

            setParamIfNotNull(
                    "P_DATE",
                    getSession().getLabel("LISTE_RECAP_DATE") + " "
                            + JACalendar.format(JACalendar.todayJJsMMsAAAA(), getSession().getIdLangueISO()));

            String numCaisse = null;

            numCaisse = (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);

            setParamIfNotNull("P_NUM_CAISSE", getSession().getLabel("LISTE_RECAP_NO_CAISSE") + " " + numCaisse);
            setParamIfNotNull("P_TITRE_PAGE", getSession().getLabel("LISTE_RECAP_TITRE"));
            setParamIfNotNull("P_MOIS_ANNEE", getSession().getLabel("LISTE_RECAP_MOIS_ANNEE") + " " + getForMoisAnnee());

            JADate asJADate = new JADate(getForMoisAnnee());

            cal.set(asJADate.getYear(), asJADate.getMonth() - 1, 1);

            setParamIfNotNull("P_TYPE_AVS", getSession().getLabel("LISTE_RECAP_AVS"));
            setParamIfNotNull("P_TYPE_AI", getSession().getLabel("LISTE_RECAP_AI"));

            setParamIfNotNull("P_PC_ALLOC_NOEL", getSession().getLabel("LISTE_RECAP_PCRFM_PC_ALLOCATION_NOEL"));
            setParamIfNotNull("P_PC", getSession().getLabel("LISTE_RECAP_PCRFM_PC_STANDARD"));
            setParamIfNotNull("P_RFM", getSession().getLabel("LISTE_RECAP_PCRFM_RFM"));
            setParamIfNotNull("P_TOTAL_LIGNE", getSession().getLabel("LISTE_RECAP_TOTAL"));

            setParamIfNotNull("P_NB", getSession().getLabel("LISTE_RECAP_NB"));
            setParamIfNotNull("P_MONTANT", getSession().getLabel("LISTE_RECAP_CHF"));
            setParamIfNotNull("P_EN_COURS", getSession().getLabel("LISTE_RECAP_EN_COURS"));
            setParamIfNotNull("P_RETENUES", getSession().getLabel("LISTE_RECAP_RETENUES"));
            setParamIfNotNull("P_VERSEMENT", getSession().getLabel("LISTE_RECAP_VERSEMENT"));

            // PC AVS Allocation de Noël
            setParamIfNotNull("P_NB_PC_AVS_AN", String.valueOf(adapter.getNbRAPCAVSAllocationNoel()));
            setParamIfNotNull("P_MONTANT_PC_AVS_AN", adapter.getMontantPCAVSAllocationNoel().toStringFormat());
            setParamIfNotNull("P_RETENUES_PC_AVS_AN", adapter.getRetenuesPCAVSAllocationNoel().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_PC_AVS_AN", adapter.getVersementPCAVSAllocationNoel().toStringFormat());

            // PC AI Allocation de Noël
            setParamIfNotNull("P_NB_PC_AI_AN", String.valueOf(adapter.getNbRAPCAIAllocationNoel()));
            setParamIfNotNull("P_MONTANT_PC_AI_AN", adapter.getMontantPCAIAllocationNoel().toStringFormat());
            setParamIfNotNull("P_RETENUES_PC_AI_AN", adapter.getRetenuesPCAIAllocationNoel().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_PC_AI_AN", adapter.getVersementPCAIAllocationNoel().toStringFormat());

            // nb, montant, retenues et versement par categories
            setParamIfNotNull("P_NB_PC_AVS", String.valueOf(adapter.getNbRAPCAVSStandard()));
            setParamIfNotNull("P_MONTANT_PC_AVS", adapter.getMontantPCAVSStandard().toStringFormat());
            setParamIfNotNull("P_RETENUES_PC_AVS", adapter.getRetenuesPCAVSStandard().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_PC_AVS", adapter.getVersementPCAVSStandard().toStringFormat());

            setParamIfNotNull("P_NB_RFM_AVS", String.valueOf(adapter.getNbRARFMAVS()));
            setParamIfNotNull("P_MONTANT_RFM_AVS", adapter.getMontantRFMAVS().toStringFormat());
            setParamIfNotNull("P_RETENUES_RFM_AVS", adapter.getRetenuesRFMAVS().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_RFM_AVS", adapter.getVersementRFMAVS().toStringFormat());

            setParamIfNotNull("P_NB_TOTAL_2_AVS", String.valueOf(adapter.getNbTotal2AVS()));
            setParamIfNotNull("P_MONTANT_TOTAL_2_AVS", adapter.getMontantTotal2AVS().toStringFormat());
            setParamIfNotNull("P_RETENUES_TOTAL_2_AVS", adapter.getRetenuesTotal2AVS().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_TOTAL_2_AVS", adapter.getVersementTotal2AVS().toStringFormat());

            setParamIfNotNull("P_NB_PC_AI", String.valueOf(adapter.getNbRAPCAIStandard()));
            setParamIfNotNull("P_MONTANT_PC_AI", adapter.getMontantPCAIStandard().toStringFormat());
            setParamIfNotNull("P_RETENUES_PC_AI", adapter.getRetenuesPCAIStandard().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_PC_AI", adapter.getVersementPCAIStandard().toStringFormat());

            setParamIfNotNull("P_NB_RFM_AI", String.valueOf(adapter.getNbRARFMAI()));
            setParamIfNotNull("P_MONTANT_RFM_AI", adapter.getMontantRFMAI().toStringFormat());
            setParamIfNotNull("P_RETENUES_RFM_AI", adapter.getRetenuesRFMAI().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_RFM_AI", adapter.getVersementRFMAI().toStringFormat());

            setParamIfNotNull("P_NB_TOTAL_2_AI", String.valueOf(adapter.getNbTotal2AI()));
            setParamIfNotNull("P_MONTANT_TOTAL_2_AI", adapter.getMontantTotal2AI().toStringFormat());
            setParamIfNotNull("P_RETENUES_TOTAL_2_AI", adapter.getRetenuesTotal2AI().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_TOTAL_2_AI", adapter.getVersementTotal2AI().toStringFormat());

            // recapitulation
            setParamIfNotNull("P_TOTAL", getSession().getLabel("LISTE_RECAP_TOTAL_GENERAL"));
            setParamIfNotNull("P_NB_TOTAL_2_GENERAL", String.valueOf(adapter.getNbTotal2General()));
            setParamIfNotNull("P_MONTANT_TOTAL_2_GENERAL", adapter.getMontantTotal2General().toStringFormat());

            setParamIfNotNull("P_MONTANT_OPAE", getSession().getLabel("LISTE_RECAP_LIBELLE_OPAE"));
            setParamIfNotNull("P_MONTANT_OPAE_2_VALEUR", adapter.getRetenuesTotal2General().toStringFormat());

            setParamIfNotNull("P_TOTAL_FINAL", getSession().getLabel("LISTE_RECAP_MONTANT_OPAE"));
            setParamIfNotNull("P_MONTANT_TOTAL_2_FINAL", adapter.getVersementTotal2General().toStringFormat());

            setParamIfNotNull("P_TOTAL_FINAL", getSession().getLabel("LISTE_RECAP_MONTANT_OPAE"));
            setParamIfNotNull("P_MONTANT_TOTAL_3_FINAL", adapter.getVersementTotal3General().toStringFormat());

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("LISTE_RECAP_TITRE"));
            throw new FWIException(e);
        }
    }

    @Override
    public void createDataSource() throws Exception {

    }

    public RERecapitulationPaiementAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("LISTE_RECAP_IMPRESSION_KO");
        } else {
            return getSession().getLabel("LISTE_RECAP_IMPRESSION_OK");
        }
    }

    public String getForMoisAnnee() {
        return forMoisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (hasNext) {
            hasNext = false;

            return true;
        } else {
            return false;
        }
    }

    public void setAdapter(RERecapitulationPaiementAdapter adapter) {
        this.adapter = adapter;
    }

    public void setForMoisAnnee(String string) {
        forMoisAnnee = string;
    }

    private void setParamIfNotNull(String name, Object value) {
        if (value != null) {
            getImporter().setParametre(name, value);
        }
    }
}
