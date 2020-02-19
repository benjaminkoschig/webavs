package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.osiris.external.IntTiers;

/**
 * <H1>Description</H1>
 * 
 * @author sch, 17.10.2007
 */
public class CORappelSursisVenteNonRespecte extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0189GCA";
    private static final long serialVersionUID = 8207820863743411680L;
    private static final String TEMPLATE_NAME = "CO_RAPPEL_SURSIS_VENTE_NON_RESP_OP";

    private String typeSaisie = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CORappelSursisVenteNonRespecte.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CORappelSursisVenteNonRespecte() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CORappelSursisVenteNonRespecte(BSession parent) throws FWIException {
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
        setTemplateFile(CORappelSursisVenteNonRespecte.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RAPPEL_SURSIS_VENTE_NON_RESPECTE"));
        setNumeroReferenceInforom(CORappelSursisVenteNonRespecte.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            // destinataire est l'OP
            destinataireDocument = getTiersService().getOfficePoursuite(getSession(),
                    curContentieux.getCompteAnnexe().getTiers(), curContentieux.getCompteAnnexe().getIdExterneRole());

            if (destinataireDocument == null) {
                this.log(getSession().getLabel("AQUILA_ERR_OP_INTROUVABLE"), FWMessage.AVERTISSEMENT);
            } else {
                _setLangueFromTiers(destinataireDocument);
            }

            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(destinataireDocument, curContentieux, true, false, true);

            // -- zone titre du document --------------------------------------------------------------------
            StringBuilder body = new StringBuilder();
            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            IntTiers affilie = curContentieux.getCompteAnnexe().getTiers();
            /*
             * formater le titre, les conventions de remplacement pour les lignes du titre sont: {0} = débiteur {1} = no
             * poursuite {2} = période
             */
            String msgFormt = formatMessage(
                    body,
                    new Object[] { this.getAdresseInLine(affilie), curContentieux.getNumPoursuite(),
                            curContentieux.getSection().getDescription(affilie.getLangueISO()),
                            curContentieux.getPeriodeSection() });
            // Hack pour corriger un bug de iText
            msgFormt = splitOnXChar(msgFormt, CODocumentManager.CONCERNE_NB_CHAR_PER_LINE);
            this.setParametres(COParameter.T1, msgFormt);

            // -- corps du doc ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            // -- pied après détail ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du pied après détail
            // body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "\n\n");

            /*
             * formater le pied après détail, les conventions de remplacement sont: {0} = no CCP caisse
             */
            // setParametres(COParameter.T9, formatMessage(body, new Object[] { getNumeroCCP() }));
            COHistorique historiqueSursisArt123 = historiqueService.getHistoriqueForLibEtape(getSession(),
                    curContentieux, ICOEtape.CS_SURSIS_DE_L_ARTICLE_123LP_SAISI);
            /*
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont : <br/> - {0} =
             * formule de politesse (ex: Madame la préposée, Monsieur le préposé) <br/> - {1} = date d'éxécution de
             * l'étape SursisArt123 <br/> - {2} = no poursuite <br/> - {3} = montant de la poursuite <br/> - {4} =
             * typeSaisie <br/> - {5} = no CCP caisse
             */
            this.setParametres(
                    COParameter.T2,
                    formatMessage(
                            body,
                            new Object[] {
                                    this._getProperty(CODocumentManager.JASP_PROP_BODY_FORMULE_DESTINATAIRE),
                                    historiqueSursisArt123 != null ? formatDate(historiqueSursisArt123
                                            .getDateExecution()) : "", curContentieux.getNumPoursuite(),
                                    formatMontant(curContentieux.getSolde()), typeSaisie, getNumeroCCP() }));
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }
}
