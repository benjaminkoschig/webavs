/*
 * Crée le 30.08.2007
 */
package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.application.REApplication;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.tools.PRDateFormater;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * @author hpe
 * 
 */
public class REListeEtatRentesPage1 extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FICHIER_MODELE = "RE_ETAT_DES_RENTES_PAGE_1";
    public static final String FICHIER_RESULTAT = "etatRentes";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private REEtatRentesAdapter adapter;
    private String forMoisAnnee = "";

    private boolean hasNext = true;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REListeEtatRentes.
     */
    public REListeEtatRentesPage1() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REListeEtatRentes.
     * 
     * @param parent
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public REListeEtatRentesPage1(BProcess parent) throws FWIException {
        super(parent, REApplication.APPLICATION_CORVUS_REP, REListeEtatRentesPage1.FICHIER_RESULTAT);
    }

    /**
     * Crée une nouvelle instance de la classe REListeEtatRentes.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public REListeEtatRentesPage1(BSession session) throws FWIException {
        super(session, REApplication.APPLICATION_CORVUS_REP, REListeEtatRentesPage1.FICHIER_RESULTAT);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        try {

            setTemplateFile(REListeEtatRentesPage1.FICHIER_MODELE);

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

            // Rentes AVS
            // Titres
            setParamIfNotNull("P_TITRE_RENTES_AVS", getSession().getLabel("LISTE_ETR_RENTES_AVS"));
            setParamIfNotNull("P_TITRE_1", getSession().getLabel("LISTE_ETR_RENTE_SIMPLE_VIEILLESSE"));
            setParamIfNotNull("P_TITRE_2", getSession().getLabel("LISTE_ETR_RENTE_VEUVE"));
            setParamIfNotNull("P_TITRE_3", getSession().getLabel("LISTE_ETR_RENTE_COMPLEMENTAIRE_EPOUSE"));
            setParamIfNotNull("P_TITRE_4", getSession().getLabel("LISTE_ETR_RENTE_ORPHELIN_PERE"));
            setParamIfNotNull("P_TITRE_5", getSession().getLabel("LISTE_ETR_RENTE_ORPHELIN_MERE"));
            setParamIfNotNull("P_TITRE_6", getSession().getLabel("LISTE_ETR_RENTE_ORPHELIN_DOUBLE"));
            setParamIfNotNull("P_TITRE_7", getSession().getLabel("LISTE_ETR_RENTE_SIMPLE_UN_BENEF"));
            setParamIfNotNull("P_TITRE_8", getSession().getLabel("LISTE_ETR_RENTE_SIMPLE_UNE_BENEF"));
            setParamIfNotNull("P_TITRE_9a", getSession().getLabel("LISTE_ETR_RENTE_DOUBLE_ENFANT"));

            // Ordinaire Nombres
            setParamIfNotNull("P_RO_NB_1", String.valueOf(adapter.getNb_AVS_10()));
            setParamIfNotNull("P_RO_NB_2", String.valueOf(adapter.getNb_AVS_13()));
            setParamIfNotNull("P_RO_NB_3", String.valueOf(adapter.getNb_AVS_33()));
            setParamIfNotNull("P_RO_NB_4", String.valueOf(adapter.getNb_AVS_14()));
            setParamIfNotNull("P_RO_NB_5", String.valueOf(adapter.getNb_AVS_15()));
            setParamIfNotNull("P_RO_NB_6", String.valueOf(adapter.getNb_AVS_16()));
            setParamIfNotNull("P_RO_NB_7", String.valueOf(adapter.getNb_AVS_34()));
            setParamIfNotNull("P_RO_NB_8", String.valueOf(adapter.getNb_AVS_35()));
            setParamIfNotNull("P_RO_NB_9a", String.valueOf(adapter.getNb_AVS_36()));

            // Ordinaire Montants
            setParamIfNotNull("P_RO_MONTANT_1", adapter.getMontant_AVS_10().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_2", adapter.getMontant_AVS_13().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_3", adapter.getMontant_AVS_33().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_4", adapter.getMontant_AVS_14().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_5", adapter.getMontant_AVS_15().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_6", adapter.getMontant_AVS_16().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_7", adapter.getMontant_AVS_34().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_8", adapter.getMontant_AVS_35().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_9a", adapter.getMontant_AVS_36().toStringFormat());

            // Extraordinaire Nombres
            setParamIfNotNull("P_REO_NB_1", String.valueOf(adapter.getNb_AVS_20()));
            setParamIfNotNull("P_REO_NB_2", String.valueOf(adapter.getNb_AVS_23()));
            setParamIfNotNull("P_REO_NB_3", String.valueOf(adapter.getNb_AVS_43()));
            setParamIfNotNull("P_REO_NB_4", String.valueOf(adapter.getNb_AVS_24()));
            setParamIfNotNull("P_REO_NB_5", String.valueOf(adapter.getNb_AVS_25()));
            setParamIfNotNull("P_REO_NB_6", String.valueOf(adapter.getNb_AVS_26()));
            setParamIfNotNull("P_REO_NB_7", String.valueOf(adapter.getNb_AVS_44()));
            setParamIfNotNull("P_REO_NB_8", String.valueOf(adapter.getNb_AVS_45()));
            setParamIfNotNull("P_REO_NB_9a", String.valueOf(adapter.getNb_AVS_46()));

            // Extraordinaire Montants
            setParamIfNotNull("P_REO_MONTANT_1", adapter.getMontant_AVS_20().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_2", adapter.getMontant_AVS_23().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_3", getAdapter().getMontant_AVS_43().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_4", adapter.getMontant_AVS_24().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_5", adapter.getMontant_AVS_25().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_6", adapter.getMontant_AVS_26().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_7", adapter.getMontant_AVS_44().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_8", adapter.getMontant_AVS_45().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_9a", adapter.getMontant_AVS_46().toStringFormat());

            // Totaux
            setParamIfNotNull("P_TOTAL_RENTE_AVS", getSession().getLabel("LISTE_ETR_TOTAL_RENTES_AVS"));
            setParamIfNotNull("P_RO_NB_TOTAL_RENTES_AVS", String.valueOf(adapter.getNb_AVS_TOTAL_RO()));
            setParamIfNotNull("P_RO_MONTANT_TOTAL_RENTES_AVS", adapter.getMontant_AVS_TOTAL_RO().toStringFormat());
            setParamIfNotNull("P_REO_NB_TOTAL_RENTES_AVS", String.valueOf(adapter.getNb_AVS_TOTAL_RE()));
            setParamIfNotNull("P_REO_MONTANT_TOTAL_RENTES_AVS", adapter.getMontant_AVS_TOTAL_RE().toStringFormat());

            // Rentes entières AI
            // Titres
            setParamIfNotNull("P_TITRE_RENTES_ENTIERES_AI", getSession().getLabel("LISTE_ETR_RENTE_ENTIERES_AI"));
            setParamIfNotNull("P_TITRE_9", getSession().getLabel("LISTE_ETR_RENTE_ENTIERE_SIMPLE_INV"));
            setParamIfNotNull("P_TITRE_9B", getSession().getLabel("LISTE_ETR_MOITIE_RENTE_ENTIERE_INV_COUPLE"));
            setParamIfNotNull("P_TITRE_10", getSession().getLabel("LISTE_ETR_RENTE_COMPLEMENTAIRE_EPOUSE"));
            setParamIfNotNull("P_TITRE_11", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UN_BENEF"));
            setParamIfNotNull("P_TITRE_12", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UNE_BENEF"));
            setParamIfNotNull("P_TITRE_30", getSession().getLabel("LISTE_ETR_RENTE_DOUBLE_ENFANT"));

            // Ordinaire Nombres
            setParamIfNotNull("P_RO_NB_9", String.valueOf(adapter.getNb_AI_50_1()));
            setParamIfNotNull("P_RO_NB_9B", String.valueOf(adapter.getNb_AI_52()));
            setParamIfNotNull("P_RO_NB_10", String.valueOf(adapter.getNb_AI_53_1()));
            setParamIfNotNull("P_RO_NB_11", String.valueOf(adapter.getNb_AI_54_1()));
            setParamIfNotNull("P_RO_NB_12", String.valueOf(adapter.getNb_AI_55_1()));
            setParamIfNotNull("P_RO_NB_30", String.valueOf(adapter.getNb_AI_56_1()));

            // Ordinaire Montants
            setParamIfNotNull("P_RO_MONTANT_9", adapter.getMontant_AI_50_1().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_9B", adapter.getMontant_AI_52().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_10", adapter.getMontant_AI_53_1().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_11", adapter.getMontant_AI_54_1().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_12", adapter.getMontant_AI_55_1().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_30", adapter.getMontant_AI_56_1().toStringFormat());

            // Extraordinaire Nombres
            setParamIfNotNull("P_REO_NB_9", String.valueOf(adapter.getNb_AI_70_1()));
            setParamIfNotNull("P_REO_NB_9B", String.valueOf(adapter.getNb_AI_72_1()));
            setParamIfNotNull("P_REO_NB_10", String.valueOf(adapter.getNb_AI_73_1()));
            setParamIfNotNull("P_REO_NB_11", String.valueOf(adapter.getNb_AI_74_1()));
            setParamIfNotNull("P_REO_NB_12", String.valueOf(adapter.getNb_AI_75_1()));
            setParamIfNotNull("P_REO_NB_30", String.valueOf(adapter.getNb_AI_76_1()));

            // Extraordinaire Montants
            setParamIfNotNull("P_REO_MONTANT_9", adapter.getMontant_AI_70_1().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_9B", adapter.getMontant_AI_72_1().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_10", adapter.getMontant_AI_73_1().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_11", adapter.getMontant_AI_74_1().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_12", adapter.getMontant_AI_75_1().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_30", adapter.getMontant_AI_76_1().toStringFormat());

            // Totaux
            setParamIfNotNull("P_TOTAL_RENTES_ENTIERES_AI", getSession().getLabel("LISTE_ETR_TOTAL_RENTES_AI"));
            setParamIfNotNull("P_RO_NB_TOTAL_RENTES_ENTIERES_AI", String.valueOf(adapter.getNb_AI_1_TOTAL_RO()));
            setParamIfNotNull("P_RO_MONTANT_TOTAL_RENTES_ENTIERES_AI", adapter.getMontant_AI_1_TOTAL_RO()
                    .toStringFormat());
            setParamIfNotNull("P_REO_NB_TOTAL_RENTES_ENTIERES_AI", String.valueOf(adapter.getNb_AI_1_TOTAL_RE()));
            setParamIfNotNull("P_REO_MONTANT_TOTAL_RENTES_ENTIERES_AI", adapter.getMontant_AI_1_TOTAL_RE()
                    .toStringFormat());

            // Demi-rente AI
            // Titres
            setParamIfNotNull("P_TITRE_RENTES_DEMI_AI", getSession().getLabel("LISTE_ETR_DEMI_RENTES_AI"));
            setParamIfNotNull("P_TITRE_13", getSession().getLabel("LISTE_ETR_DEMI_RENTES_SIMPLE_AI"));
            setParamIfNotNull("P_TITRE_14", getSession().getLabel("LISTE_ETR_RENTE_COMPLEMENTAIRE_EPOUSE"));
            setParamIfNotNull("P_TITRE_15", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UN_BENEF"));
            setParamIfNotNull("P_TITRE_16", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UNE_BENEF"));
            setParamIfNotNull("P_TITRE_31", getSession().getLabel("LISTE_ETR_RENTE_DOUBLE_ENFANT"));

            // Ordinaire Nombres
            setParamIfNotNull("P_RO_NB_13", String.valueOf(adapter.getNb_AI_50_2()));
            setParamIfNotNull("P_RO_NB_14", String.valueOf(adapter.getNb_AI_53_2()));
            setParamIfNotNull("P_RO_NB_15", String.valueOf(adapter.getNb_AI_54_2()));
            setParamIfNotNull("P_RO_NB_16", String.valueOf(adapter.getNb_AI_55_2()));
            setParamIfNotNull("P_RO_NB_31", String.valueOf(adapter.getNb_AI_56_2()));

            // Ordinaire Montants
            setParamIfNotNull("P_RO_MONTANT_13", adapter.getMontant_AI_50_2().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_14", adapter.getMontant_AI_53_2().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_15", adapter.getMontant_AI_54_2().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_16", adapter.getMontant_AI_55_2().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_31", adapter.getMontant_AI_56_2().toStringFormat());

            // Extraordinaire Nombres
            setParamIfNotNull("P_REO_NB_13", String.valueOf(adapter.getNb_AI_70_2()));
            setParamIfNotNull("P_REO_NB_14", String.valueOf(adapter.getNb_AI_73_2()));
            setParamIfNotNull("P_REO_NB_15", String.valueOf(adapter.getNb_AI_74_2()));
            setParamIfNotNull("P_REO_NB_16", String.valueOf(adapter.getNb_AI_75_2()));
            setParamIfNotNull("P_REO_NB_31", String.valueOf(adapter.getNb_AI_76_2()));

            // Extraordinaire Montants
            setParamIfNotNull("P_REO_MONTANT_13", adapter.getMontant_AI_70_2().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_14", adapter.getMontant_AI_73_2().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_15", adapter.getMontant_AI_74_2().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_16", adapter.getMontant_AI_75_2().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_31", adapter.getMontant_AI_76_2().toStringFormat());

            // Totaux
            setParamIfNotNull("P_TOTAL_RENTES_DEMI_AI", getSession().getLabel("LISTE_ETR_TOTAL_DEMI_RENTES_AI"));
            setParamIfNotNull("P_RO_NB_TOTAL_RENTES_DEMI_AI", String.valueOf(adapter.getNb_AI_2_TOTAL_RO()));
            setParamIfNotNull("P_RO_MONTANT_TOTAL_RENTES_DEMI_AI", adapter.getMontant_AI_2_TOTAL_RO().toStringFormat());
            setParamIfNotNull("P_REO_NB_TOTAL_RENTES_DEMI_AI", String.valueOf(adapter.getNb_AI_2_TOTAL_RE()));
            setParamIfNotNull("P_REO_MONTANT_TOTAL_RENTES_DEMI_AI", adapter.getMontant_AI_2_TOTAL_RE().toStringFormat());

            // Quart de rente AI
            // Titres
            setParamIfNotNull("P_TITRE_RENTES_QUART_AI", getSession().getLabel("LISTE_ETR_QUART_RENTE_AI"));
            setParamIfNotNull("P_TITRE_17", getSession().getLabel("LISTE_ETR_QUART_RENTE_INVALIDITE"));
            setParamIfNotNull("P_TITRE_18", getSession().getLabel("LISTE_ETR_RENTE_COMPLEMENTAIRE_EPOUSE"));
            setParamIfNotNull("P_TITRE_19", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UN_BENEF"));
            setParamIfNotNull("P_TITRE_20", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UNE_BENEF"));
            setParamIfNotNull("P_TITRE_32", getSession().getLabel("LISTE_ETR_RENTE_DOUBLE_ENFANT"));

            // Ordinaire Nombres
            setParamIfNotNull("P_RO_NB_17", String.valueOf(adapter.getNb_AI_50_4()));
            setParamIfNotNull("P_RO_NB_18", String.valueOf(adapter.getNb_AI_53_4()));
            setParamIfNotNull("P_RO_NB_19", String.valueOf(adapter.getNb_AI_54_4()));
            setParamIfNotNull("P_RO_NB_20", String.valueOf(adapter.getNb_AI_55_4()));
            setParamIfNotNull("P_RO_NB_32", String.valueOf(adapter.getNb_AI_56_4()));

            // Ordinaire Montants
            setParamIfNotNull("P_RO_MONTANT_17", adapter.getMontant_AI_50_4().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_18", adapter.getMontant_AI_53_4().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_19", adapter.getMontant_AI_54_4().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_20", adapter.getMontant_AI_55_4().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_32", adapter.getMontant_AI_56_4().toStringFormat());

            // Extraordinaire Nombres
            setParamIfNotNull("P_REO_NB_17", String.valueOf(adapter.getNb_AI_70_4()));
            setParamIfNotNull("P_REO_NB_18", String.valueOf(adapter.getNb_AI_73_4()));
            setParamIfNotNull("P_REO_NB_19", String.valueOf(adapter.getNb_AI_74_4()));
            setParamIfNotNull("P_REO_NB_20", String.valueOf(adapter.getNb_AI_75_4()));
            setParamIfNotNull("P_REO_NB_32", String.valueOf(adapter.getNb_AI_76_4()));

            // Extraordinaire Montants
            setParamIfNotNull("P_REO_MONTANT_17", adapter.getMontant_AI_70_4().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_18", adapter.getMontant_AI_73_4().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_19", adapter.getMontant_AI_74_4().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_20", adapter.getMontant_AI_75_4().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_32", adapter.getMontant_AI_76_4().toStringFormat());

            // Totaux
            setParamIfNotNull("P_TOTAL_RENTES_QUART_AI", getSession().getLabel("LISTE_ETR_TOTAL_QUART_RENTE_AI"));
            setParamIfNotNull("P_RO_NB_TOTAL_RENTES_QUART_AI", String.valueOf(adapter.getNb_AI_4_TOTAL_RO()));
            setParamIfNotNull("P_RO_MONTANT_TOTAL_RENTES_QUART_AI", adapter.getMontant_AI_4_TOTAL_RO().toStringFormat());
            setParamIfNotNull("P_REO_NB_TOTAL_RENTES_QUART_AI", String.valueOf(adapter.getNb_AI_4_TOTAL_RE()));
            setParamIfNotNull("P_REO_MONTANT_TOTAL_RENTES_QUART_AI", adapter.getMontant_AI_4_TOTAL_RE()
                    .toStringFormat());

            // Trois-quarts de rente AI
            // Titres
            setParamIfNotNull("P_TITRE_RENTES_TROIS_QUARTS_AI", getSession().getLabel("LISTE_ETR_TROIS_QUART_RENTE_AI"));
            setParamIfNotNull("P_TITRE_21", getSession().getLabel("LISTE_ETR_TROIS_QUART_RENTE_INVALIDITE"));
            setParamIfNotNull("P_TITRE_22", getSession().getLabel("LISTE_ETR_RENTE_COMPLEMENTAIRE_EPOUSE"));
            setParamIfNotNull("P_TITRE_23", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UN_BENEF"));
            setParamIfNotNull("P_TITRE_24", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UNE_BENEF"));
            setParamIfNotNull("P_TITRE_33", getSession().getLabel("LISTE_ETR_RENTE_DOUBLE_ENFANT"));

            // Ordinaire Nombres
            setParamIfNotNull("P_RO_NB_21", String.valueOf(adapter.getNb_AI_50_3()));
            setParamIfNotNull("P_RO_NB_22", String.valueOf(adapter.getNb_AI_53_3()));
            setParamIfNotNull("P_RO_NB_23", String.valueOf(adapter.getNb_AI_54_3()));
            setParamIfNotNull("P_RO_NB_24", String.valueOf(adapter.getNb_AI_55_3()));
            setParamIfNotNull("P_RO_NB_33", String.valueOf(adapter.getNb_AI_56_3()));

            // Ordinaire Montants
            setParamIfNotNull("P_RO_MONTANT_21", adapter.getMontant_AI_50_3().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_22", adapter.getMontant_AI_53_3().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_23", adapter.getMontant_AI_54_3().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_24", adapter.getMontant_AI_55_3().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_33", adapter.getMontant_AI_56_3().toStringFormat());

            // Extraordinaire Nombres
            setParamIfNotNull("P_REO_NB_21", String.valueOf(adapter.getNb_AI_70_3()));
            setParamIfNotNull("P_REO_NB_22", String.valueOf(adapter.getNb_AI_73_3()));
            setParamIfNotNull("P_REO_NB_23", String.valueOf(adapter.getNb_AI_74_3()));
            setParamIfNotNull("P_REO_NB_24", String.valueOf(adapter.getNb_AI_75_3()));
            setParamIfNotNull("P_REO_NB_33", String.valueOf(adapter.getNb_AI_76_3()));

            // Extraordinaire Montants
            setParamIfNotNull("P_REO_MONTANT_21", adapter.getMontant_AI_70_3().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_22", adapter.getMontant_AI_73_3().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_23", adapter.getMontant_AI_74_3().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_24", adapter.getMontant_AI_75_3().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_33", adapter.getMontant_AI_76_3().toStringFormat());

            // Totaux
            setParamIfNotNull("P_TOTAL_RENTES_TROIS_QUARTS_AI",
                    getSession().getLabel("LISTE_ETR_TOTAL_TROIS_QUART_RENTE_AI"));
            setParamIfNotNull("P_RO_NB_TOTAL_RENTES_TROIS_QUARTS_AI", String.valueOf(adapter.getNb_AI_3_TOTAL_RO()));
            setParamIfNotNull("P_RO_MONTANT_TOTAL_RENTES_TROIS_QUARTS_AI", adapter.getMontant_AI_3_TOTAL_RO()
                    .toStringFormat());
            setParamIfNotNull("P_REO_NB_TOTAL_RENTES_TROIS_QUARTS_AI", String.valueOf(adapter.getNb_AI_3_TOTAL_RE()));
            setParamIfNotNull("P_REO_MONTANT_TOTAL_RENTES_TROIS_QUARTS_AI", adapter.getMontant_AI_3_TOTAL_RE()
                    .toStringFormat());

            // Récapitulation de l'AI
            // Titres
            setParamIfNotNull("P_TITRE_RENTES_RECAP_AI", getSession().getLabel("LISTE_ETR_RECAPITULATION_AI"));
            setParamIfNotNull("P_TITRE_25", getSession().getLabel("LISTE_ETR_RENTE_SIMPLE_INV"));
            setParamIfNotNull("P_TITRE_25B", getSession().getLabel("LISTE_ETR_MOITIE_RENTE_INV_COUPLE"));
            setParamIfNotNull("P_TITRE_26", getSession().getLabel("LISTE_ETR_RENTE_COMPLEMENTAIRE_EPOUSE"));
            setParamIfNotNull("P_TITRE_27", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UN_BENEF"));
            setParamIfNotNull("P_TITRE_28", getSession().getLabel("LISTE_ETR_RENTE_COMPL_UNE_BENEF"));
            setParamIfNotNull("P_TITRE_34", getSession().getLabel("LISTE_ETR_RENTE_DOUBLE_ENFANT"));

            // Ordinaire Nombres
            setParamIfNotNull("P_RO_NB_25", String.valueOf(adapter.getNb_AI_50()));
            setParamIfNotNull("P_RO_NB_25B", String.valueOf(adapter.getNb_AI_52()));
            setParamIfNotNull("P_RO_NB_26", String.valueOf(adapter.getNb_AI_53()));
            setParamIfNotNull("P_RO_NB_27", String.valueOf(adapter.getNb_AI_54()));
            setParamIfNotNull("P_RO_NB_28", String.valueOf(adapter.getNb_AI_55()));
            setParamIfNotNull("P_RO_NB_34", String.valueOf(adapter.getNb_AI_56()));

            // Ordinaire Montants
            setParamIfNotNull("P_RO_MONTANT_25", adapter.getMontant_AI_50().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_25B", adapter.getMontant_AI_52().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_26", adapter.getMontant_AI_53().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_27", adapter.getMontant_AI_54().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_28", adapter.getMontant_AI_55().toStringFormat());
            setParamIfNotNull("P_RO_MONTANT_34", adapter.getMontant_AI_56().toStringFormat());

            // Extraordinaire Nombres
            setParamIfNotNull("P_REO_NB_25", String.valueOf(adapter.getNb_AI_70()));
            setParamIfNotNull("P_REO_NB_25B", String.valueOf(adapter.getNb_AI_72()));
            setParamIfNotNull("P_REO_NB_26", String.valueOf(adapter.getNb_AI_73()));
            setParamIfNotNull("P_REO_NB_27", String.valueOf(adapter.getNb_AI_74()));
            setParamIfNotNull("P_REO_NB_28", String.valueOf(adapter.getNb_AI_75()));
            setParamIfNotNull("P_REO_NB_34", String.valueOf(adapter.getNb_AI_76()));

            // Extraordinaire Montants
            setParamIfNotNull("P_REO_MONTANT_25", adapter.getMontant_AI_70().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_25B", adapter.getMontant_AI_72().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_26", adapter.getMontant_AI_73().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_27", adapter.getMontant_AI_74().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_28", adapter.getMontant_AI_75().toStringFormat());
            setParamIfNotNull("P_REO_MONTANT_34", adapter.getMontant_AI_76().toStringFormat());

            // Totaux
            setParamIfNotNull("P_TOTAL_RENTES_RECAP_AI", getSession().getLabel("LISTE_ETR_TOTAL_RECAP_AI"));
            setParamIfNotNull("P_RO_NB_TOTAL_RENTES_RECAP_AI", String.valueOf(adapter.getNb_AI_TOTAL_RO()));
            setParamIfNotNull("P_RO_MONTANT_TOTAL_RENTES_RECAP_AI", adapter.getMontant_AI_TOTAL_RO().toStringFormat());
            setParamIfNotNull("P_REO_NB_TOTAL_RENTES_RECAP_AI", String.valueOf(adapter.getNb_AI_TOTAL_RE()));
            setParamIfNotNull("P_REO_MONTANT_TOTAL_RENTES_RECAP_AI", adapter.getMontant_AI_TOTAL_RE().toStringFormat());

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
            return getSession().getLabel("LISTE_ETR_MAIL_OBJECT_KO");
        } else {
            return getSession().getLabel("LISTE_ETR_MAIL_OBJECT_OK");
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

    public void setAdapter(REEtatRentesAdapter adapter) {
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
