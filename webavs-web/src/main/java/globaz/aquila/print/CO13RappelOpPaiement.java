package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntTiers;
import java.util.HashMap;
import java.util.List;

/**
 * <H1>Description</H1> Rappel de saisie
 * 
 * @author Alexandre Cuva, 18-aug-2004 Rappel de saisie
 */
public class CO13RappelOpPaiement extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0037GCO";
    private static final long serialVersionUID = -6146775777533682429L;
    private static final String TEMPLATE_NAME = "CO_13_RAPPEL_SAISIE_OP";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO13RappelOpPaiement.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO13RappelOpPaiement() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO13RappelOpPaiement(BSession parent) throws FWIException {
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
        setTemplateFile(CO13RappelOpPaiement.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RAPPEL_PAIEMENT"));
        setNumeroReferenceInforom(CO13RappelOpPaiement.NUMERO_REFERENCE_INFOROM);
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
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule de
             * politesse (ex: Madame la préposée, Monsieur le préposé) {1} = date du PV de saisie {2} = date reception
             * de la saisie {3} = no poursuite {4} = montant de la poursuite
             */
            COHistorique historiquePvSaisie = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_PV_DE_SAISIE_SAISI);
            String datePV = null;
            COEtapeInfo etapeInfo = null;

            if (historiquePvSaisie != null) {
                // setParametres(COParameter.D3, historiquePvSaisieSalaire.getDateModif());
                datePV = historiquePvSaisie.getDateExecution();
                etapeInfo = historiquePvSaisie.loadEtapeInfo(COEtapeInfoConfig.CS_DATE_RECEPTION_PV_SAISIE);
            }

            if (etapeInfo == null) {
                this.log("impossible de trouver la date d'exécution de reception de la saisie");
            }

            this.setParametres(
                    COParameter.T2,
                    formatMessage(body,
                            new Object[] { this._getProperty(CODocumentManager.JASP_PROP_BODY_FORMULE_DESTINATAIRE),
                                    formatDate(datePV), formatDate(etapeInfo != null ? etapeInfo.getValeur() : ""),
                                    curContentieux.getNumPoursuite(),
                                    formatMontant(curContentieux.getSection().getSolde()) }));

            // -- montants et frais ---------------------------------------------------------------------------
            // on fait débuter l'extrait de compte le jour de la dernière RCP
            COHistorique historiqueRCP = historiqueService.getHistoriqueForLibEtape(getSession(), curContentieux,
                    ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE);
            List dataSource = createSituationCompteDS(historiqueRCP.getDateExecution(), COParameter.F1, COParameter.F2,
                    COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2),
                    ICOEtape.CS_1ER_RAPPEL_DE_SAISIE_ENVOYE, "0");

            // et on insére la créance en première ligne
            HashMap fields = new HashMap();
            String solde = historiqueRCP.getSolde();

            // on soustrait les taxes car elles n'ont pas forcément toutes été comptabilisées
            if (!JadeStringUtil.isDecimalEmpty(historiqueRCP.getTaxes())) {
                FWCurrency soldeCorrige = new FWCurrency(solde);

                soldeCorrige.sub(historiqueRCP.getTaxes());
                solde = soldeCorrige.toString();
            }

            fields.put(
                    COParameter.F1,
                    formatMessage(new StringBuilder(getCatalogueTextesUtil().texte(getParent(), 3, 1)),
                            new Object[] { formatDate(historiqueRCP.getDateExecution()) }));
            fields.put(COParameter.F2, formatMontant(solde));
            fields.put(COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2));
            dataSource.add(0, fields);

            // -- lignes pour nouveaux frais
            FWCurrency totalNouvellesTaxes = addTaxesToDS(dataSource, COParameter.F1, COParameter.F2, COParameter.F3,
                    getCatalogueTextesUtil().texte(getParent(), 3, 2));

            this.setDataSource(dataSource);

            this.setParametres(COParameter.T6, getCatalogueTextesUtil().texte(getParent(), 3, 2));
            this.setParametres(COParameter.T7, getCatalogueTextesUtil().texte(getParent(), 3, 5));

            if (totalNouvellesTaxes != null) {
                totalNouvellesTaxes.sub(curContentieux.getSolde());
                totalNouvellesTaxes.abs();
                this.setParametres(COParameter.M4, formatMontant(totalNouvellesTaxes.toString()));
            } else {
                this.setParametres(COParameter.M4, formatMontant(curContentieux.getSolde()));
            }

            // -- pied après détail ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du pied après détail
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "\n\n");

            /*
             * formater le pied après détail, les conventions de remplacement sont: {0} = no CCP caisse {1} = formule de
             * politesse (ex: Madame la préposée, Monsieur le préposé)
             */
            this.setParametres(COParameter.T8, formatMessage(body, new Object[] { getNumeroCCP() }));
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }
}
