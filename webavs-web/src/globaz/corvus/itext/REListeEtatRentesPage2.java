package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.application.REApplication;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.tools.PRDateFormater;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * @author HPE
 */
public class REListeEtatRentesPage2 extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FICHIER_MODELE = "RE_ETAT_DES_RENTES_PAGE_2";
    public static final String FICHIER_RESULTAT = "etatRentes";

    private REEtatRentesAdapter adapter;
    private String forMoisAnnee = "";
    private boolean hasNext = true;

    public REListeEtatRentesPage2() {
        super();
    }

    public REListeEtatRentesPage2(final BProcess parent) throws FWIException {
        super(parent, REApplication.APPLICATION_CORVUS_REP, REListeEtatRentesPage2.FICHIER_RESULTAT);
    }

    public REListeEtatRentesPage2(final BSession session) throws FWIException {
        super(session, REApplication.APPLICATION_CORVUS_REP, REListeEtatRentesPage2.FICHIER_RESULTAT);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {

            setTemplateFile(REListeEtatRentesPage2.FICHIER_MODELE);

            DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), "dd MMMM yyyy");
            Calendar cal = Calendar.getInstance();

            // En-tête
            setParamIfNotNull(
                    "P_NOM_CAISSE",
                    FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                            ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            setParamIfNotNull("P_TITRE_DOCUMENT", getSession().getLabel("LISTE_ETR_TITRE_DOCUMENT") + " "
                    + getForMoisAnnee());
            setParamIfNotNull("P_DATE", getSession().getLabel("LISTE_ETR_DATE"));
            setParamIfNotNull("P_PAGE", getSession().getLabel("LISTE_ETR_PAGE"));
            setParamIfNotNull("P_DATE_VALUE", df.format(cal.getTime()));

            // Titre général
            setParamIfNotNull("P_TITRE_PRESTATION", getSession().getLabel("LISTE_ETR_PRESTATION"));
            setParamIfNotNull("P_TITRE_ORDINAIRE", getSession().getLabel("LISTE_ETR_ORDINAIRE"));
            setParamIfNotNull("P_TITRE_EXTRAORDINAIRE", getSession().getLabel("LISTE_ETR_EXTRAORDINAIRE"));
            setParamIfNotNull("P_TITRE_AVS", getSession().getLabel("LISTE_ETR_AVS"));
            setParamIfNotNull("P_TITRE_AI", getSession().getLabel("LISTE_ETR_AI"));

            // API
            // Titres
            setParamIfNotNull("P_TITRE_API", getSession().getLabel("LISTE_ETR_ALLOCATIONS_IMPOTENCE"));
            setParamIfNotNull("P_TITRE_29", getSession().getLabel("LISTE_ETR_IMPOTENCE_FAIBLE_DOMICILE"));
            setParamIfNotNull("P_TITRE_29B", getSession()
                    .getLabel("LISTE_ETR_IMPOTENCE_FAIBLE_DOMICILE_SOINS_DOMICILE"));
            setParamIfNotNull("P_TITRE_30", getSession().getLabel("LISTE_ETR_IMPOTENCE_MOYEN_DOMICILE"));
            setParamIfNotNull("P_TITRE_31", getSession().getLabel("LISTE_ETR_IMPOTENCE_GRAVE_DOMICILE"));
            setParamIfNotNull("P_TITRE_32", getSession().getLabel("LISTE_ETR_IMPOTENCE_FAIBLE_ASSISTANCE"));
            setParamIfNotNull("P_TITRE_33", getSession().getLabel("LISTE_ETR_IMPOTENCE_MOYEN_ASSISTANCE"));
            setParamIfNotNull("P_TITRE_34", getSession().getLabel("LISTE_ETR_IMPOTENCE_FAIBLE"));
            setParamIfNotNull("P_TITRE_35", getSession().getLabel("LISTE_ETR_IMPOTENCE_MOYEN"));
            setParamIfNotNull("P_TITRE_36", getSession().getLabel("LISTE_ETR_IMPOTENCE_GRAVE"));

            // Ordinaire Nombres
            setParamIfNotNull("P_RO_NB_29", String.valueOf(adapter.getNb_API_81()));
            setParamIfNotNull("P_RO_NB_29B", "--");
            setParamIfNotNull("P_RO_NB_30", String.valueOf(adapter.getNb_API_82()));
            setParamIfNotNull("P_RO_NB_31", String.valueOf(adapter.getNb_API_83()));
            setParamIfNotNull("P_RO_NB_32", String.valueOf(adapter.getNb_API_84()));
            setParamIfNotNull("P_RO_NB_33", String.valueOf(adapter.getNb_API_88()));
            setParamIfNotNull("P_RO_NB_34", String.valueOf(adapter.getNb_API_91()));
            setParamIfNotNull("P_RO_NB_35", String.valueOf(adapter.getNb_API_92()));
            setParamIfNotNull("P_RO_NB_36", String.valueOf(adapter.getNb_API_93()));

            // Ordinaire Montants
            setParamIfNotNull("P_RO_MONTANT_29", adapter.getMontant_API_81().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_29B", "--");
            setParamIfNotNull("P_RO_MONTANT_30", adapter.getMontant_API_82().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_31", adapter.getMontant_API_83().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_32", adapter.getMontant_API_84().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_33", adapter.getMontant_API_88().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_34", adapter.getMontant_API_91().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_35", adapter.getMontant_API_92().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_36", adapter.getMontant_API_93().toStringFormat());

            // Extraordinaire Nombres
            setParamIfNotNull("P_REO_NB_29", String.valueOf(adapter.getNb_API_85()));
            setParamIfNotNull("P_REO_NB_29B", String.valueOf(adapter.getNb_API_89()));
            setParamIfNotNull("P_REO_NB_30", String.valueOf(adapter.getNb_API_86()));
            setParamIfNotNull("P_REO_NB_31", String.valueOf(adapter.getNb_API_87()));
            setParamIfNotNull("P_REO_NB_32", "--");
            setParamIfNotNull("P_REO_NB_33", "--");
            setParamIfNotNull("P_REO_NB_34", String.valueOf(adapter.getNb_API_94() + adapter.getNb_API_95()));
            setParamIfNotNull("P_REO_NB_35", String.valueOf(adapter.getNb_API_96()));
            setParamIfNotNull("P_REO_NB_36", String.valueOf(adapter.getNb_API_97()));

            // Extraordinaire Montants
            setParamIfNotNull("P_REO_MONTANT_29", adapter.getMontant_API_85().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_29B", adapter.getMontant_API_89().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_30", adapter.getMontant_API_86().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_31", adapter.getMontant_API_87().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_32", "--");
            setParamIfNotNull("P_REO_MONTANT_33", "--");
            FWCurrency montant94et95 = adapter.getMontant_API_94();
            montant94et95.add(adapter.getMontant_API_95());
            setParamIfNotNull("P_REO_MONTANT_34", montant94et95.toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_35", adapter.getMontant_API_96().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_36", adapter.getMontant_API_97().toStringFormat());

            // Totaux
            setParamIfNotNull("P_TOTAL_API", getSession().getLabel("LISTE_ETR_TOTAL_API"));
            setParamIfNotNull("P_RO_NB_TOTAL_API", String.valueOf(adapter.getNb_API_TOTAL_AI()));
            setParamIfNotNull("P_RO_MONTANT_TOTAL_API", adapter.getMontant_API_TOTAL_AI().toStringFormat());
            setParamIfNotNull("P_REO_NB_TOTAL_API", String.valueOf(adapter.getNb_API_TOTAL_AVS()));
            setParamIfNotNull("P_REO_MONTANT_TOTAL_API", adapter.getMontant_API_TOTAL_AVS().toStringFormat());

            remplirSectionPC();
            remplirSectionRFM();

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("LISTE_ETR_MAIL"));
            throw new FWIException(e);
        }
    }

    @Override
    public void createDataSource() throws Exception {
    }

    public REEtatRentesAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("LISTE_ETR_MAIL_OBJECT_KO");
        } else {
            return getSession().getLabel("LISTE_ETR_MAIL_OBJECT_OK");
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

    private void remplirSectionPC() {

        // Titre
        setParamIfNotNull("P_TITRE_PC", getSession().getLabel("LISTE_ETR_PC_TIRE"));

        // PC AI
        setParamIfNotNull("P_TYPE_PC_INVALIDITE", getSession().getLabel("LISTE_ETR_PC_AI"));
        setParamIfNotNull("P_NB_PC_INVALIDITE", adapter.getNb_PC_150());
        setParamIfNotNull("P_MONTANT_PC_INVALIDITE", adapter.getMontant_PC_150().toStringFormat());

        // PC AVS
        setParamIfNotNull("P_TYPE_PC_VIEILLESSE", getSession().getLabel("LISTE_ETR_PC_VIEILLESSE"));
        setParamIfNotNull("P_NB_PC_VIEILLESSE", adapter.getNb_PC_110());
        setParamIfNotNull("P_MONTANT_PC_VIEILLESSE", adapter.getMontant_PC_110().toStringFormat());

        // PC Survivant
        setParamIfNotNull("P_TYPE_PC_SURVIVANT", getSession().getLabel("LISTE_ETR_PC_SURVIVANT"));
        setParamIfNotNull("P_NB_PC_SURVIVANT", adapter.getNb_PC_113());
        setParamIfNotNull("P_MONTANT_PC_SURVIVANT", adapter.getMontant_PC_113().toStringFormat());

        // PC Allocation de Noël (payé sur l'adresse de paiement rente)
        setParamIfNotNull("P_TYPE_PC_ALLOC_NOEL_RENTES", getSession().getLabel("LISTE_ETR_PC_ALLOC_NOEL_RENTES"));
        setParamIfNotNull("P_NB_PC_ALLOC_NOEL_RENTES", adapter.getNb_PC_198());
        setParamIfNotNull("P_MONTANT_PC_ALLOC_NOEL_RENTES", adapter.getMontant_PC_198().toStringFormat());

        // PC Allocation de Noël (payé par mandat postal)
        setParamIfNotNull("P_TYPE_PC_ALLOC_NOEL_MANDAT_POSTAL",
                getSession().getLabel("LISTE_ETR_PC_ALLOC_NOEL_MANDAT_POSTAL"));
        setParamIfNotNull("P_NB_PC_ALLOC_NOEL_MANDAT_POSTAL", adapter.getNb_PC_199());
        setParamIfNotNull("P_MONTANT_PC_ALLOC_NOEL_MANDAT_POSTAL", adapter.getMontant_PC_199().toStringFormat());

        // Totaux
        setParamIfNotNull("P_TOTAL_PC", getSession().getLabel("LISTE_ETR_TOTAL_PC"));
        setParamIfNotNull("P_NB_TOTAL_PC", adapter.getNb_PC_TOTAL());
        setParamIfNotNull("P_MONTANT_TOTAL_PC", adapter.getMontant_PC_TOTAL().toStringFormat());
    }

    private void remplirSectionRFM() {

        // Titre
        setParamIfNotNull("P_TITRE_RFM", getSession().getLabel("LISTE_ETR_RFM_TIRE"));

        // RFM AI
        setParamIfNotNull("P_TYPE_RFM_INVALIDITE", getSession().getLabel("LISTE_ETR_RFM_AI"));
        setParamIfNotNull("P_NB_RFM_INVALIDITE", adapter.getNb_RFM_250());
        setParamIfNotNull("P_MONTANT_RFM_INVALIDITE", adapter.getMontant_RFM_250().toStringFormat());

        // PC AVS
        setParamIfNotNull("P_TYPE_RFM_VIEILLESSE", getSession().getLabel("LISTE_ETR_RFM_VIEILLESSE"));
        setParamIfNotNull("P_NB_RFM_VIEILLESSE", adapter.getNb_RFM_210());
        setParamIfNotNull("P_MONTANT_RFM_VIEILLESSE", adapter.getMontant_RFM_210().toStringFormat());

        // PC Survivant
        setParamIfNotNull("P_TYPE_RFM_SURVIVANT", getSession().getLabel("LISTE_ETR_RFM_SURVIVANT"));
        setParamIfNotNull("P_NB_RFM_SURVIVANT", adapter.getNb_RFM_213());
        setParamIfNotNull("P_MONTANT_RFM_SURVIVANT", adapter.getMontant_RFM_213().toStringFormat());

        // Totaux
        setParamIfNotNull("P_TOTAL_RFM", getSession().getLabel("LISTE_ETR_TOTAL_RFM"));
        setParamIfNotNull("P_NB_TOTAL_RFM", adapter.getNb_RFM_TOTAL());
        setParamIfNotNull("P_MONTANT_TOTAL_RFM", adapter.getMontant_RFM_TOTAL().toStringFormat());
    }

    public void setAdapter(final REEtatRentesAdapter adapter) {
        this.adapter = adapter;
    }

    public void setForMoisAnnee(final String string) {
        forMoisAnnee = string;
    }

    private void setParamIfNotNull(final String name, final Object value) {
        if (value != null) {
            getImporter().setParametre(name, value);
        }
    }
}
