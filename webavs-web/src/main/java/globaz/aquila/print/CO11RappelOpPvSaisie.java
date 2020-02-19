package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.osiris.external.IntTiers;

/**
 * <H1>Description</H1>
 * <p>
 * Rappel à l'OP après la RCP
 * </p>
 * 
 * @author Alexandre Cuva, 18-aug-2004
 */
public class CO11RappelOpPvSaisie extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0032GCO";
    private static final long serialVersionUID = 8227276319973661378L;
    private static final String TEMPLATE_NAME = "CO_11_RAPPEL_PV_SAISIE_OP";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO11RappelOpPvSaisie.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO11RappelOpPvSaisie() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO11RappelOpPvSaisie(BSession parent) throws FWIException {
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
        setTemplateFile(CO11RappelOpPvSaisie.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RAPP_PV_SAISIE"));
        setNumeroReferenceInforom(CO11RappelOpPvSaisie.NUMERO_REFERENCE_INFOROM);
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
            String msgFormt = formatMessage(body,
                    new Object[] { this.getAdresseInLine(affilie), curContentieux.getNumPoursuite(),
                            curContentieux.getSection().getDescription(getLangue()),
                            curContentieux.getPeriodeSection(), giveLibelleInfoRom246() });

            // Hack pour corriger un bug de iText
            msgFormt = splitOnXChar(msgFormt, CODocumentManager.CONCERNE_NB_CHAR_PER_LINE);

            this.setParametres(COParameter.T1, msgFormt);

            // -- corps du doc ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            /*
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = date de la
             * réquisition de poursuite {1} = no poursuite {2} = solde de la section
             */
            // @TODO Vérifier s'il faut un montant (historiqueRCP.getMontantTotal ou historiqueRCP.getMontantInitial)
            // pris
            // depuis la réquisiton de poursuite dans l'historique ou directement le montant courrant
            // contentieux.getMontantInitial
            COHistorique historiqueRCP = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE);

            // setParametres(COParameter.T2, formatMessage(body, new Object[] { historiqueRCP != null ?
            // formatDate(historiqueRCP.getDateExecution()) : "", curContentieux.getNumPoursuite(),
            // formatMontant(curContentieux.getSolde()) }));

            // -- lignes de détail -----------------------------------------------------------------------
            // on fait débuter l'extrait de compte le jour de la dernière RCP
            // List dataSource = createSituationCompteDS(historiqueRCP.getDateExecution(), COParameter.F1,
            // COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2),
            // ICOEtape.CS_1ER_RAPPEL_DE_PV_DE_SAISIE_ENVOYE, "0");

            // et on insére la créance en première ligne
            // HashMap fields = new HashMap();
            // String solde = historiqueRCP.getSolde();

            // on soustrait les taxes car elles n'ont pas forcément toutes été comptabilisées
            // if (!JadeStringUtil.isDecimalEmpty(historiqueRCP.getTaxes())) {
            // FWCurrency soldeCorrige = new FWCurrency(solde);
            //
            // soldeCorrige.sub(historiqueRCP.getTaxes());
            // solde = soldeCorrige.toString();
            // }

            // fields.put(COParameter.F1, formatMessage(new StringBuffer(getCatalogueTextesUtil().texte(getParent(), 3,
            // 1)), new Object[] { formatDate(historiqueRCP.getDateExecution()) }));
            // fields.put(COParameter.F2, formatMontant(solde));
            // fields.put(COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2));
            // dataSource.add(0, fields);

            // -- lignes pour nouveaux frais
            // FWCurrency totalNouvellesTaxes = addTaxesToDS(dataSource, COParameter.F1, COParameter.F2, COParameter.F3,
            // getCatalogueTextesUtil().texte(getParent(), 3, 2));

            // -- lignes des versements
            // setDataSource(dataSource);

            // setParametres(COParameter.T5, getCatalogueTextesUtil().texte(getParent(), 3, 2));
            // setParametres(COParameter.T6, getCatalogueTextesUtil().texte(getParent(), 3, 6));

            // if (totalNouvellesTaxes != null) {
            // totalNouvellesTaxes.add(historiqueRCP.getSolde());
            // setParametres(COParameter.M3, formatMontant(totalNouvellesTaxes.toString()));
            // } else {
            // setParametres(COParameter.M3, formatMontant(historiqueRCP.getSolde()));
            // }

            // -- pied après détail ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du pied après détail
            // body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "\n\n");

            /*
             * formater le pied après détail, les conventions de remplacement sont:
             */
            // setParametres(COParameter.T7, formatMessage(body, new Object[] {}));
            this.setParametres(
                    COParameter.T2,
                    formatMessage(body,
                            new Object[] { historiqueRCP != null ? formatDate(historiqueRCP.getDateExecution()) : "",
                                    curContentieux.getNumPoursuite(), formatMontant(curContentieux.getSolde()) }));
        } catch (Exception e) {
            this.log("exception: " + e.toString());
        }
    }

}
