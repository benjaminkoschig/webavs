/*
 * Created on Aug 17, 2004
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.osiris.external.IntTiers;

/**
 * <H1>Description</H1>
 * <p>
 * .
 * </p>
 * 
 * @author cuva
 *         <p>
 *         Retrait RCP
 *         </p>
 */
public class CO10ARetraitPoursuite extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0033GCO";
    public static final int SEND_TO_ASSURE = 2;
    public static final int SEND_TO_BOTH = 3;

    public static final int SEND_TO_OP = 1;
    private static final long serialVersionUID = -747641724530484745L;
    private static final int STATE_ASSURE = 1;

    private static final int STATE_IDLE = 0;
    private static final int STATE_OP = 2;
    private static final String TEMPLATE_NAME = "CO_10A_RETRAIT_RCP_OP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private IntTiers destinataireDocument = null;
    private int sendTo = CO10ARetraitPoursuite.SEND_TO_BOTH;
    private int state = CO10ARetraitPoursuite.STATE_IDLE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO10ARetraitPoursuite.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO10ARetraitPoursuite() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO10ARetraitPoursuite(BSession parent) throws FWIException {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CO10ARetraitPoursuite.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RETRAIT_POURSUITE"));
        setNumeroReferenceInforom(CO10ARetraitPoursuite.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            String affilieName = "";
            int numLettre = 0;
            StringBuilder body = new StringBuilder();

            // destinataire est l'OP
            if (state == CO10ARetraitPoursuite.STATE_OP) {
                destinataireDocument = getTiersService().getOfficePoursuite(getSession(),
                        curContentieux.getCompteAnnexe().getTiers(),
                        curContentieux.getCompteAnnexe().getIdExterneRole());
                _setLangueFromTiers(destinataireDocument); // sel : doit être
                // placé avant
                // l'appel au
                // catalogue de
                // textes afin que
                // la bonne langue
                // soit utilisée.
                body.append(getCatalogueTextesUtil().texte(getParent(), 3, 5));
                affilieName = formatMessage(body,
                        new Object[] { this.getAdresseInLine(curContentieux.getCompteAnnexe().getTiers()) });
                numLettre = 2;
            } else {
                destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
                _setLangueFromTiers(destinataireDocument);
                numLettre = 4;
            }

            if (destinataireDocument == null) {
                this.log(getSession().getLabel("AQUILA_ERR_OP_INTROUVABLE"), FWMessage.AVERTISSEMENT);
            }

            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(destinataireDocument, curContentieux, true, false, true);

            // -- titre du doc
            // ------------------------------------------------------------------------------
            // rechercher toutes les lignes du titre du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            /*
             * formater le titre, les conventions de remplacement pour les lignes du titre sont: {0} = numéro de
             * poursuite {1} = affilie {2} = description
             */
            IntTiers affilie = curContentieux.getCompteAnnexe().getTiers();

            this.setParametres(COParameter.T6,
                    formatMessage(body, new Object[] { curContentieux.getNumPoursuite(), affilieName }));

            // -- corps du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), numLettre, body, "\n\n");

            /*
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule
             * politesse (ex: Monsieur,) {1} = date rdcp
             */
            COHistorique historiqueRDCP = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE);

            this.setParametres(
                    COParameter.T7,
                    formatMessage(body, new Object[] {
                            state == CO10ARetraitPoursuite.STATE_OP ? getCatalogueTextesUtil().texte(getParent(), 3, 1)
                                    : getFormulePolitesse(affilie), formatDate(historiqueRDCP.getDateExecution()) }));

            // Rajoute la date à la signature
            this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA,
                    getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_DATA) + " "
                            + formatDate(getDateExecution()));

            // -- copie
            // ------------------------------------------------------------------------------------
            if (sendTo == CO10ARetraitPoursuite.SEND_TO_BOTH) {
                this.setParametres(
                        COParameter.T8,
                        formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 3, 2)),
                                new Object[] { state == CO10ARetraitPoursuite.SEND_TO_ASSURE ? getCatalogueTextesUtil()
                                        .texte(getParent(), 3, 4) : getCatalogueTextesUtil().texte(getParent(), 3, 3) }));
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * getter pour l'attribut send to.
     * 
     * @return la valeur courante de l'attribut send to
     */
    public int getSendTo() {
        return sendTo;
    }

    /**
     * @return DOCUMENT ME!
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:

                // on commence ou on a termine un contentieux, on regarder s'il en
                // reste à traiter
                if (super.next()) {
                    switch (sendTo) {
                        case SEND_TO_BOTH:
                        case SEND_TO_ASSURE:
                            state = CO10ARetraitPoursuite.STATE_ASSURE;

                            break;
                        case SEND_TO_OP:
                            state = CO10ARetraitPoursuite.STATE_OP;

                            break;
                    }

                    // il reste des documents à créer
                    return true;
                } else {
                    // il n'y a plus de contentieux à traiter, donc plus de
                    // documents à envoyer
                    return false;
                }

            case STATE_ASSURE:

                // on a créé le document pour les assurés, on regarde s'il faut
                // créer celui pour l'OP
                switch (sendTo) {
                    case SEND_TO_BOTH:
                        state = CO10ARetraitPoursuite.STATE_OP;

                        // on va créer le document pour l'OP
                        return true;
                    default:
                        state = CO10ARetraitPoursuite.STATE_IDLE;

                        // on regarde s'il y a d'autres contentieux à traiter
                        return next();
                }

            case STATE_OP:

                // on a créé le document pour l'OP, on regarde s'il reste des
                // contentieux à traiter
                state = CO10ARetraitPoursuite.STATE_IDLE;

                return next();

            default:

                // par défaut on ne crée rien
                return false;
        }
    }

    /**
     * @see globaz.aquila.print.COIDocumentOnCopy#setDestinataire(IntTiers)
     */
    public void setDestinataire(IntTiers destinataire) {
        destinataireDocument = destinataire;
    }

    /**
     * setter pour l'attribut send to.
     * 
     * @param sendTo
     *            une nouvelle valeur pour cet attribut
     */
    public void setSendTo(int sendTo) {
        this.sendTo = sendTo;
    }
}
