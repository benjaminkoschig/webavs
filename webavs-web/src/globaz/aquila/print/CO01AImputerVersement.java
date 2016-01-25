/*
 * Created on Aug 17, 2004
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.aquila.print;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntTiers;

/**
 * <H1>Description</H1> Document : Imputer versement <br>
 * Niveaux et paramètres utilisés : <br>
 * <ul>
 * <li>niveau 1 : Dump du niveau 1. <br>
 * {0} = "\n" ;
 * <li>niveau 2 : Dump du niveau 1. <br>
 * {0} = nom section ; <br>
 * {1} = date de la section
 * <li>niveau 3 : Dump du niveau 1. <br>
 * {0} = nom section ; <br>
 * {1} = date de la section
 * <li>niveau 4 : Dump du niveau 1. <br>
 * {0} = nom section ; <br>
 * {1} = date de la section
 * </ul>
 * 
 * @author cuva
 */
public class CO01AImputerVersement extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NUMERO_REFERENCE_INFOROM = "0035GCO";
    private static final long serialVersionUID = 2348103302283800108L;
    private static final String TEMPLATE_NAME = "CO_01A_IMPUTER_VERSEMENT_OP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annexe = null;
    private String dateVersement = null;
    private String montantImputation = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO01BRadiationPoursuite.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CO01AImputerVersement() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO01AImputerVersement(BSession parent) throws FWIException {
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
        setTemplateFile(CO01AImputerVersement.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_IMPUTER_VERSEMENT"));
        setNumeroReferenceInforom(CO01AImputerVersement.NUMERO_REFERENCE_INFOROM);
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

            // -- titre du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            StringBuffer body = new StringBuffer();

            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            /**
             * formater le titre, les conventions de remplacement pour les paragraphes du corps sont : <br />
             * {0} = Intro premier motif <br />
             * {1} = Valeur premier motif <br />
             * {2} = Intro second motif <br />
             * {3} = Valeur second motif <br />
             * {4} = Dernière étape {5} = déscription de la séction {6} = libellé InfoRom246
             */
            String derniereEtape = getSession().getCodeLibelle(curContentieux.getLibEtape());
            if (JadeStringUtil.isEmpty(curContentieux.getNumPoursuite())) {
                this.setParametres(
                        COParameter.T1,
                        formatMessage(
                                body,
                                new Object[] {
                                        getCatalogueTextesUtil().texte(getParent(), 3, 3),
                                        this.getAdresseInLine(curContentieux.getCompteAnnexe().getTiers()),
                                        getCatalogueTextesUtil().texte(getParent(), 3, 4),
                                        curContentieux.getSection().getIdExterne(),
                                        derniereEtape,
                                        curContentieux.getSection().getDescription(destinataireDocument.getLangueISO()),
                                        giveLibelleInfoRom246() }));
            } else {
                this.setParametres(
                        COParameter.T1,
                        formatMessage(
                                body,
                                new Object[] {
                                        getCatalogueTextesUtil().texte(getParent(), 3, 1),
                                        curContentieux.getNumPoursuite(),
                                        getCatalogueTextesUtil().texte(getParent(), 3, 2),
                                        " " + this.getAdresseInLine(curContentieux.getCompteAnnexe().getTiers()),
                                        derniereEtape,
                                        curContentieux.getSection().getDescription(destinataireDocument.getLangueISO()),
                                        giveLibelleInfoRom246() }));
            }

            // -- corps du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            /**
             * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: <br />
             * {0} = montant imputation <br />
             * {1} = date de versement
             */
            this.setParametres(
                    COParameter.T2,
                    formatMessage(body, new Object[] { formatMontant(getMontantImputation()),
                            formatDate(getDateVersement()) }));

            if (!JadeStringUtil.isBlank(annexe)) {
                body.setLength(0);
                getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "");
                this.setParametres(COParameter.T3, body + "\n" + annexe);
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @return le texte des annexes
     */
    public String getAnnexe() {
        return annexe;
    }

    /**
     * @return the dateVersement
     */
    public String getDateVersement() {
        return dateVersement;
    }

    /**
     * getter pour l'attribut montant imputation.
     * 
     * @return la valeur courante de l'attribut montant imputation
     */
    public String getMontantImputation() {
        return JANumberFormatter.deQuote(montantImputation);
    }

    /**
     * @param annexe
     */
    public void setAnnexe(String annexe) {
        this.annexe = annexe;
    }

    /**
     * @param dateVersement
     *            the dateVersement to set
     */
    public void setDateVersement(String dateVersement) {
        this.dateVersement = dateVersement;
    }

    /**
     * @see globaz.aquila.print.COIDocumentOnCopy#setDestinataire(IntTiers)
     */
    public void setDestinataire(IntTiers destinataire) {
        destinataireDocument = destinataire;
    }

    /**
     * setter pour l'attribut montant imputation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantImputation(String string) {
        montantImputation = string;
    }
}
