/*
 * Créé le 10 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.print;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CO00BRappelPaiement extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0020GCO";
    private static final long serialVersionUID = -6253823454355503549L;
    private static final String TEMPLATE_NAME = "CO_00A_RAPPEL_AF";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDelaiPaiement = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO00BRappelPaiement.
     */
    public CO00BRappelPaiement() {
    }

    /**
     * Crée une nouvelle instance de la classe CO00RappelPaiement.
     * 
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     */
    public CO00BRappelPaiement(BSession session) throws Exception {
        super(session);
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
        setTemplateFile(CO00BRappelPaiement.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RAPPEL_PAIEMENT"));
        setNumeroReferenceInforom(CO00BRappelPaiement.NUMERO_REFERENCE_INFOROM);
    }

    @Override
    public String getJasperTemplate() {
        return null;
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            // destinataire est l'affilié
            destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
            _setLangueFromTiers(destinataireDocument);

            String adresse = getAdressePrincipale(destinataireDocument);

            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(curContentieux, true, false, true, adresse);

            // -- titre du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du titre du document
            StringBuilder body = new StringBuilder();

            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            /*
             * formater le titre, les conventions de remplacement pour les paragraphes du titre sont: {0} = nom section
             */
            this.setParametres(COParameter.T1,
                    formatMessage(body, new Object[] { curContentieux.getSection().getDescription(getLangue()) }));

            // -- corps du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            // formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
            // {0} = formule de politesse
            // {1} = montant facture
            // {2} = date échéance
            // {3} = date de délai de paiement
            // {4} = date de la section

            this.setParametres(
                    COParameter.T5,
                    formatMessage(body, new Object[] { getFormulePolitesse(destinataireDocument),
                            formatMontant(curContentieux.getSection().getSolde()),
                            formatDate(curContentieux.getSection().getDateEcheance()), formatDate(dateDelaiPaiement),
                            formatDate(curContentieux.getSection().getDateSection()) }));
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * getter pour l'attribut date delai paiement.
     * 
     * @return la valeur courante de l'attribut date delai paiement
     */
    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    /**
     * setter pour l'attribut date delai paiement.
     * 
     * @param dateDelaiPaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDelaiPaiement(String dateDelaiPaiement) {
        this.dateDelaiPaiement = dateDelaiPaiement;
    }
}
