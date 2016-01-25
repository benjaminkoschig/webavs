package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1> . Rappel de réclamation de frais et intérêts
 * 
 * @author Pascal Lovy, 25-nov-2004
 */
public class CO05RappelReceptionPaiement extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0151GCA";
    private static final long serialVersionUID = -5059887918854418489L;
    private static final String TEMPLATE_NAME = "CO_05_RAPPEL_REC_PAIEMENT_AF";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO05RappelReceptionPaiement.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO05RappelReceptionPaiement() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO05RappelReceptionPaiement(BSession parent) throws FWIException {
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
        setTemplateFile(CO05RappelReceptionPaiement.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RAP_RECEPTION_PAIEMENT"));
        setNumeroReferenceInforom(CO05RappelReceptionPaiement.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            // Récupération des objets métiers
            COHistorique historiqueFormule44 = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_FRAIS_ET_INTERETS_RECLAMES);

            // destinataire est l'affilie
            destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
            _setLangueFromTiers(destinataireDocument);

            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(curContentieux, true, false, true, getAdressePrincipale(destinataireDocument));

            // -- titre du doc ------------------------------------------------------------------------------
            // rechercher toutes les lignes du titre du document
            StringBuffer body = new StringBuffer();

            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            /*
             * formater le titre, les conventions de remplacement pour les lignes du titre sont: {0} = retour chariot
             * (\n) {1} = numéro de poursuite {2} = période
             */
            String msgFormt = formatMessage(body,
                    new Object[] { "\n", curContentieux.getNumPoursuite(), curContentieux.getPeriodeSection(),
                            curContentieux.getSection().getDescription(getLangue()) });

            // Hack pour corriger un bug de iText
            msgFormt = splitOnXChar(msgFormt, CODocumentManager.CONCERNE_NB_CHAR_PER_LINE);

            this.setParametres(COParameter.T1, msgFormt);

            // -- corps du doc ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            /*
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule de
             * politesse (ex: Madame, Monsieur) {1} = date réception partielle {2} = solde de l'historique {3} = délai
             * paiement {4} = solde de la section
             */
            this.setParametres(
                    COParameter.T5,
                    formatMessage(body, new Object[] { getFormulePolitesse(destinataireDocument),
                            (historiqueFormule44 != null) ? formatDate(historiqueFormule44.getDateExecution()) : "",
                            (historiqueFormule44 != null) ? historiqueFormule44.getSolde() : "",
                            getTransition().getDuree(), curContentieux.getSection().getSoldeFormate() }));
        } catch (Exception e) {
            this.log("exception: " + e.toString());
        }
    }
}
