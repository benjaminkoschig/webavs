/*
 * Created on Jul 11, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
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
 * @author hpe
 * 
 */
public class REListeRecapitulativePaiement extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FICHIER_MODELE = "RE_RECAP_PRESTATIONS";
    public static final String FICHIER_RESULTAT = "recap_paiement";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private RERecapitulationPaiementAdapter adapter;

    private String forMoisAnnee = "";

    private boolean hasNext = true;

    /**
     * Crée une nouvelle instance de la classe REListeRecapitulativePaiement.
     */
    public REListeRecapitulativePaiement() {
        super();
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REListeRecapitulativePaiement.
     * 
     * @param parent
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public REListeRecapitulativePaiement(BProcess parent) throws FWIException {
        super(parent, REApplication.APPLICATION_CORVUS_REP, REListeRecapitulativePaiement.FICHIER_RESULTAT);
    }

    /**
     * Crée une nouvelle instance de la classe REListeRecapitulativePaiement.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public REListeRecapitulativePaiement(BSession session) throws FWIException {
        super(session, REApplication.APPLICATION_CORVUS_REP, REListeRecapitulativePaiement.FICHIER_RESULTAT);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentProperty(REGenererListesVerificationProcess.PROPERTY_DOCUMENT_ORDER,
                REGenererListesVerificationProcess.LISTE_RECAP_PAIEM_ORDER);

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_RECAPITULATIVE_DU_PAIEMENT);

    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        try {

            if (adapter == null) {
                adapter = new RERecapitulationPaiementAdapter(getSession(), getForMoisAnnee());
                adapter.chargerParGenrePrestation();
            }

            setTemplateFile(REListeRecapitulativePaiement.FICHIER_MODELE);

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

            setParamIfNotNull("P_RO", getSession().getLabel("LISTE_RECAP_RENTES_ORDINAIRES"));
            setParamIfNotNull("P_REO", getSession().getLabel("LISTE_RECAP_RENTES_EXTRAORDINAIRES"));
            setParamIfNotNull("P_API", getSession().getLabel("LISTE_RECAP_ALLOCATION_IMPOTENT"));
            setParamIfNotNull("P_TOTAL_LIGNE", getSession().getLabel("LISTE_RECAP_TOTAL"));

            setParamIfNotNull("P_NB", getSession().getLabel("LISTE_RECAP_NB"));
            setParamIfNotNull("P_MONTANT", getSession().getLabel("LISTE_RECAP_CHF"));
            setParamIfNotNull("P_EN_COURS", getSession().getLabel("LISTE_RECAP_EN_COURS"));
            setParamIfNotNull("P_RETENUES", getSession().getLabel("LISTE_RECAP_RETENUES"));
            setParamIfNotNull("P_VERSEMENT", getSession().getLabel("LISTE_RECAP_VERSEMENT"));

            // nb, montant, retenues et versement par categories
            setParamIfNotNull("P_NB_RO_AVS", String.valueOf(adapter.getNbRAROAVS()));
            setParamIfNotNull("P_MONTANT_RO_AVS", adapter.getMontantROAVS().toStringFormat());
            setParamIfNotNull("P_RETENUES_RO_AVS", adapter.getRetenuesROAVS().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_RO_AVS", adapter.getVersementROAVS().toStringFormat());

            setParamIfNotNull("P_NB_REO_AVS", String.valueOf(adapter.getNbRAREOAVS()));
            setParamIfNotNull("P_MONTANT_REO_AVS", adapter.getMontantREOAVS().toStringFormat());
            setParamIfNotNull("P_RETENUES_REO_AVS", adapter.getRetenuesREOAVS().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_REO_AVS", adapter.getVersementREOAVS().toStringFormat());

            setParamIfNotNull("P_NB_API_AVS", String.valueOf(adapter.getNbRAAPIAVS()));
            setParamIfNotNull("P_MONTANT_API_AVS", adapter.getMontantAPIAVS().toStringFormat());
            setParamIfNotNull("P_RETENUES_API_AVS", adapter.getRetenuesAPIAVS().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_API_AVS", adapter.getVersementAPIAVS().toStringFormat());

            setParamIfNotNull("P_NB_TOTAL_AVS", String.valueOf(adapter.getNbTotalAVS()));
            setParamIfNotNull("P_MONTANT_TOTAL_AVS", adapter.getMontantTotalAVS().toStringFormat());
            setParamIfNotNull("P_RETENUES_TOTAL_AVS", adapter.getRetenuesTotalAVS().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_TOTAL_AVS", adapter.getVersementTotalAVS().toStringFormat());

            setParamIfNotNull("P_NB_RO_AI", String.valueOf(adapter.getNbRAROAI()));
            setParamIfNotNull("P_MONTANT_RO_AI", adapter.getMontantROAI().toStringFormat());
            setParamIfNotNull("P_RETENUES_RO_AI", adapter.getRetenuesROAI().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_RO_AI", adapter.getVersementROAI().toStringFormat());

            setParamIfNotNull("P_NB_REO_AI", String.valueOf(adapter.getNbRAREOAI()));
            setParamIfNotNull("P_MONTANT_REO_AI", adapter.getMontantREOAI().toStringFormat());
            setParamIfNotNull("P_RETENUES_REO_AI", adapter.getRetenuesREOAI().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_REO_AI", adapter.getVersementREOAI().toStringFormat());

            setParamIfNotNull("P_NB_API_AI", String.valueOf(adapter.getNbRAAPIAI()));
            setParamIfNotNull("P_MONTANT_API_AI", adapter.getMontantAPIAI().toStringFormat());
            setParamIfNotNull("P_RETENUES_API_AI", adapter.getRetenuesAPIAI().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_API_AI", adapter.getVersementAPIAI().toStringFormat());

            setParamIfNotNull("P_NB_TOTAL_AI", String.valueOf(adapter.getNbTotalAI()));
            setParamIfNotNull("P_MONTANT_TOTAL_AI", adapter.getMontantTotalAI().toStringFormat());
            setParamIfNotNull("P_RETENUES_TOTAL_AI", adapter.getRetenuesTotalAI().toStringFormat());
            setParamIfNotNull("P_VERSEMENT_TOTAL_AI", adapter.getVersementTotalAI().toStringFormat());

            // recapitulation
            setParamIfNotNull("P_TOTAL", getSession().getLabel("LISTE_RECAP_TOTAL_GENERAL"));
            setParamIfNotNull("P_NB_TOTAL_GENERAL", String.valueOf(adapter.getNbTotalGeneral()));
            setParamIfNotNull("P_MONTANT_TOTAL_GENERAL", adapter.getMontantTotalGeneral().toStringFormat());

            setParamIfNotNull("P_MONTANT_OPAE", getSession().getLabel("LISTE_RECAP_LIBELLE_OPAE"));
            setParamIfNotNull("P_MONTANT_OPAE_VALEUR", adapter.getRetenuesTotalGeneral().toStringFormat());

            setParamIfNotNull("P_TOTAL_FINAL", getSession().getLabel("LISTE_RECAP_MONTANT_OPAE"));
            setParamIfNotNull("P_MONTANT_TOTAL_FINAL", adapter.getVersementTotalGeneral().toStringFormat());

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

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("LISTE_RECAP_IMPRESSION_KO");
        } else {
            return getSession().getLabel("LISTE_RECAP_IMPRESSION_OK");
        }
    }

    /**
     * getter pour l'attribut for mois annee
     * 
     * @return la valeur courante de l'attribut for mois annee
     */
    public String getForMoisAnnee() {
        return forMoisAnnee;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
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

    /**
     * setter pour l'attribut for moisAnnee.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnnee(String string) {
        forMoisAnnee = string;
    }

    private void setParamIfNotNull(String name, Object value) {
        if (value != null) {
            getImporter().setParametre(name, value);
        }
    }

}
