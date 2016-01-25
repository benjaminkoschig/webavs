/*
 * Created on Aug 17, 2004
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.service.historique.COHistoriqueService;
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
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 *         </p>
 */
public class CORetraitRequisitionVente extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0190GCA";
    public static final int SEND_TO_ASSURE = 2;
    public static final int SEND_TO_BOTH = 3;

    public static final int SEND_TO_OP = 1;
    private static final long serialVersionUID = -747641724530484745L;
    private static final int STATE_ASSURE = 1;

    private static final int STATE_IDLE = 0;
    private static final int STATE_OP = 2;
    private static final String TEMPLATE_NAME = "CO_RETRAIT_RV_OP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private IntTiers destinataireDocument = null;
    private int sendTo = CORetraitRequisitionVente.SEND_TO_BOTH;
    private int state = CORetraitRequisitionVente.STATE_IDLE;
    private String typeSaisie = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO10ARetraitPoursuite.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CORetraitRequisitionVente() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CORetraitRequisitionVente(BSession parent) throws FWIException {
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
        setTemplateFile(CORetraitRequisitionVente.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RETRAIT_REQUISITION_VENTE"));
        setNumeroReferenceInforom(CORetraitRequisitionVente.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            String affilieName = "";
            int numLettre = 0;
            StringBuffer body = new StringBuffer();

            // destinataire est l'OP
            if (state == CORetraitRequisitionVente.STATE_OP) {
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
            this._handleHeaders(curContentieux, true, false, true, getAdressePrincipale(destinataireDocument));

            // -- titre du doc
            // ------------------------------------------------------------------------------
            // rechercher toutes les lignes du titre du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            // recherche de l'historique
            COHistorique historiqueRV = historiqueService.getHistoriqueForLibEtapeTypeSaisie(getSession(),
                    curContentieux, ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE, getTypeSaisie());
            String typeSaisie = new String();
            String numSerie = new String();
            if (historiqueRV != null) {
                typeSaisie = getSession().getCodeLibelle(
                        historiqueRV.loadEtapeInfo(COHistoriqueService.CS_TYPE_SAISIE).getValeur());
                COEtapeInfo etapeInfo = historiqueRV.loadEtapeInfo(COHistoriqueService.CS_NUM_SERIE);
                if (etapeInfo != null) {
                    numSerie = etapeInfo.getValeur();
                }
            }

            /*
             * formater le titre, les conventions de remplacement pour les lignes du titre sont: {0} = numéro de
             * poursuite {1} = affilie {2} = description
             */
            IntTiers affilie = curContentieux.getCompteAnnexe().getTiers();
            this.setParametres(COParameter.T6,
                    formatMessage(body, new Object[] { curContentieux.getNumPoursuite(), affilieName, numSerie }));

            // -- corps du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), numLettre, body, "\n\n");

            /*
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule
             * politesse (ex: Monsieur,) {1} = date rdcp
             */

            this.setParametres(
                    COParameter.T7,
                    formatMessage(
                            body,
                            new Object[] {
                                    (state == CORetraitRequisitionVente.STATE_OP) ? getCatalogueTextesUtil().texte(
                                            getParent(), 3, 1) : getFormulePolitesse(affilie),
                                    formatDate(historiqueRV.getDateExecution()), typeSaisie }));

            // Rajoute la date à la signature
            this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_DATA,
                    getImporter().getParametre().get(ICaisseReportHelper.PARAM_SIGNATURE_DATA) + " "
                            + formatDate(getDateExecution()));

            // -- copie
            // ------------------------------------------------------------------------------------
            if (sendTo == CORetraitRequisitionVente.SEND_TO_BOTH) {
                this.setParametres(
                        COParameter.T8,
                        formatMessage(
                                new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 3, 2)),
                                new Object[] { (state == CORetraitRequisitionVente.SEND_TO_ASSURE) ? getCatalogueTextesUtil()
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
     * @return the typeSaisie
     */
    public String getTypeSaisie() {
        return typeSaisie;
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
                            state = CORetraitRequisitionVente.STATE_ASSURE;

                            break;
                        case SEND_TO_OP:
                            state = CORetraitRequisitionVente.STATE_OP;

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
                        state = CORetraitRequisitionVente.STATE_OP;

                        // on va créer le document pour l'OP
                        return true;
                    default:
                        state = CORetraitRequisitionVente.STATE_IDLE;

                        // on regarde s'il y a d'autres contentieux à traiter
                        return next();
                }

            case STATE_OP:

                // on a créé le document pour l'OP, on regarde s'il reste des
                // contentieux à traiter
                state = CORetraitRequisitionVente.STATE_IDLE;

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

    /**
     * @param typeSaisie
     *            the typeSaisie to set
     */
    public void setTypeSaisie(String typeSaisie) {
        this.typeSaisie = typeSaisie;
    }
}
