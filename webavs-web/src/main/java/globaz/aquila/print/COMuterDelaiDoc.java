package globaz.aquila.print;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.GlobazJobQueue;

/**
 * <H1>Description</H1>
 * <p>
 * Un document qui informe l'assuré que le délai pour le paiement de sa facture a été muté.
 * </p>
 * 
 * @author vre
 */
public class COMuterDelaiDoc extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String CS_ETAPE_MUTER_DELAI = "5300099";

    public static final String NUMERO_REFERENCE_INFOROM = "0021GCO";
    private static final long serialVersionUID = 1611301777606626016L;
    private static final String TEMPLATE_NAME = "CO_MUTER_DELAI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDocument = null;
    private String libSequence = null;
    private Boolean paraEcheanceAffiche = null;
    private Boolean paraInteretsMoratoiresAffiche = null;
    private String prochaineDateDeclenchement = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        // chargement des infos sur le contentieux
        setTemplateFile(COMuterDelaiDoc.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_CONFIRMATION_MUTATION"));
        setNumeroReferenceInforom(COMuterDelaiDoc.NUMERO_REFERENCE_INFOROM);
        getCatalogueTextesUtil().setTypeDocument(COMuterDelaiDoc.CS_ETAPE_MUTER_DELAI);
    }

    /**
     * Remplit le corps du document.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            // destinataire est l'affilié
            destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
            _setLangueFromTiers(destinataireDocument);

            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(curContentieux, true, false, true, getAdressePrincipale(destinataireDocument),
                    dateDocument);

            // remplacement du texte
            // ---------------------------------------------------------------------
            StringBuilder buffer = new StringBuilder();

            // titre - {0} = libelle de la facture
            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, buffer, "");
            this.setParametres(COParameter.T1,
                    formatMessage(buffer, new Object[] { curContentieux.getSection().getDescription(getLangue()) }));

            // corps de texte - {0} = titre, {1} = nouvelle date délai, {2} =
            // montant, {3} = paragraphes
            buffer.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, buffer, "");

            // Création des textes de remplacements
            Object[] args = new Object[4];

            args[0] = getFormulePolitesse(destinataireDocument); // titre du
            // tiers
            args[1] = formatDate(prochaineDateDeclenchement); // nouveau délai
            args[2] = formatMontant(curContentieux.getSolde()); // solde de la
            // section
            args[3] = ""; // si pas de paragraphes supplémentaires à afficher

            // paragraphe 'cotisations doivent être réglées dans l'échéance
            // légale'
            if ((paraEcheanceAffiche != null) && paraEcheanceAffiche.booleanValue()) {
                args[3] = getCatalogueTextesUtil().texte(getParent(), 3, 1);
            }

            // paragraphe 'nous calculerons l'interet moratoire'
            if ((paraInteretsMoratoiresAffiche != null) && paraInteretsMoratoiresAffiche.booleanValue()) {
                args[3] = args[3] + getCatalogueTextesUtil().texte(getParent(), 3, 2);
            }

            this.setParametres(COParameter.T5, formatMessage(buffer, args));
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * getter pour l'attribut date document.
     * 
     * @return la valeur courante de l'attribut date document
     */
    public String getDateDocument() {
        return dateDocument;
    }

    /**
     * getter pour l'attribut lib sequence.
     * 
     * @return la valeur courante de l'attribut lib sequence
     */
    public String getLibSequence() {
        return libSequence;
    }

    /**
     * getter pour l'attribut para echeance affiche.
     * 
     * @return la valeur courante de l'attribut para echeance affiche
     */
    public Boolean getParaEcheanceAffiche() {
        return paraEcheanceAffiche;
    }

    /**
     * getter pour l'attribut para interets moratoires affiche.
     * 
     * @return la valeur courante de l'attribut para interets moratoires affiche
     */
    public Boolean getParaInteretsMoratoiresAffiche() {
        return paraInteretsMoratoiresAffiche;
    }

    /**
     * getter pour l'attribut prochaine date declenchement.
     * 
     * @return la valeur courante de l'attribut prochaine date declenchement
     */
    public String getProchaineDateDeclenchement() {
        return prochaineDateDeclenchement;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

    /**
     * setter pour l'attribut date document.
     * 
     * @param dateDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    /**
     * setter pour l'attribut lib sequence.
     * 
     * @param libSequence
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibSequence(String libSequence) {
        this.libSequence = libSequence;
    }

    /**
     * setter pour l'attribut para echeance affiche.
     * 
     * @param paraEcheanceAffiche
     *            une nouvelle valeur pour cet attribut
     */
    public void setParaEcheanceAffiche(Boolean paraEcheanceAffiche) {
        this.paraEcheanceAffiche = paraEcheanceAffiche;
    }

    /**
     * setter pour l'attribut para interets moratoires affiche.
     * 
     * @param paraInteretsMoratoiresAffiche
     *            une nouvelle valeur pour cet attribut
     */
    public void setParaInteretsMoratoiresAffiche(Boolean paraInteretsMoratoiresAffiche) {
        this.paraInteretsMoratoiresAffiche = paraInteretsMoratoiresAffiche;
    }

    /**
     * setter pour l'attribut prochaine date declenchement.
     * 
     * @param prochaineDateDeclenchement
     *            une nouvelle valeur pour cet attribut
     */
    public void setProchaineDateDeclenchement(String prochaineDateDeclenchement) {
        this.prochaineDateDeclenchement = prochaineDateDeclenchement;
    }
}
