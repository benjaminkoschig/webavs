package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * <H1>Description</H1> . Rappel � l'OP du CDP
 * 
 * @author Alexandre Cuva, 18-aug-2004
 */
public class CO02RappelCDP extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0024GCO";
    private static final long serialVersionUID = -6159956118266423162L;
    private static final String TEMPLATE_NAME = "CO_02_CDP_OP";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe CO02RappelCDP.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO02RappelCDP() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de probl�me d'initialisaion
     */
    public CO02RappelCDP(BSession parent) throws FWIException {
        super(parent, parent.getLabel("AQUILA_RAPPEL_CDP"));
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
        setTemplateFile(CO02RappelCDP.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RAPPEL_CDP"));
        setNumeroReferenceInforom(CO02RappelCDP.NUMERO_REFERENCE_INFOROM);
    }

    @Override
    public String getJasperTemplate() {
        return null;
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

            // Gestion de l'en-t�te/pied de page/signature
            this._handleHeaders(destinataireDocument, curContentieux, true, false, true);

            // -- zone titre du document --------------------------------------------------------------------
            StringBuilder body = new StringBuilder();
            IntTiers affilie = curContentieux.getCompteAnnexe().getTiers();
            TIAdresseDataSource adresse = getAdresseDataSourcePrincipalEnvoiOP(affilie);
            COHistorique historique = initHistorique();
            String dateRP = "";
            if (historique != null) {
                dateRP = formatDate(historique.getDateExecution());
            }

            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            this.setParametres(COParameter.T6, affilie.getNom());

            if (adresse != null) {
                this.setParametres(COParameter.T7, adresse.rue + " " + adresse.numeroRue + "\n" + adresse.localiteNpa
                        + " " + adresse.localiteNom);
            }

            String msgFormt = formatMessage(body,
                    new Object[] { this.getAdresseInLine(affilie), curContentieux.getNumPoursuite(),
                            curContentieux.getSection().getDescription(getLangue()),
                            curContentieux.getPeriodeSection(), dateRP, giveLibelleInfoRom246() });
            // Hack pour corriger un bug de iText
            msgFormt = splitOnXChar(msgFormt, CODocumentManager.CONCERNE_NB_CHAR_PER_LINE);
            this.setParametres(COParameter.T1, msgFormt);

            // -- corps du doc ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            /*
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = date de la
             * r�quisition de poursuite {1} = montant de la poursuite
             */
            String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                    curContentieux.getIdSection());
            this.setParametres(COParameter.T5,
                    formatMessage(body, new Object[] { dateRP, formatMontant(infoSection[0]) }));
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private COHistorique initHistorique() throws Exception {
        // Contr�ler si ADB d�j� effectuer, si oui on r�cup�res les infos de l'ADB (n� d'ADB)
        COHistorique historique = null;
        // Charge l'historique de la d�cision
        historique = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);

        return historique;
    }
}
