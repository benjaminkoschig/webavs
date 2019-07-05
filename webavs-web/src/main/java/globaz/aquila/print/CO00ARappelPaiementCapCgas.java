/*
 * Cr�� le 10 janv. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.print;

import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.service.cataloguetxt.COCatalogueTextesService;
import globaz.aquila.service.taxes.COTaxe;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.osiris.db.utils.CAReferenceBVR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Cette classe permet de cr�er un document de rappel pour CCVD et Agrivit dans le cadre d'une s�quence CAP/CGAS.
 * 
 * @author ebsc
 */
public class CO00ARappelPaiementCapCgas extends CODocumentManager {

    private static final Logger LOG = LoggerFactory.getLogger(CO00ARappelPaiementCapCgas.class);

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String NOM_DOCUMENT_RAPPEL_CAP_CGAS = "Rappel CAP/CGAS";
    public static final String NUMERO_REFERENCE_INFOROM = "0020GCO";

    public static final String NUMERO_REFERENCE_INFOROM_RAPPEL_CAP_CGAS = "0300GCO";
    private static final long serialVersionUID = 4866945152594877861L;
    private static final String TEMPLATE_NAME = "CO_00A_RAPPEL_AF_CAP_CGAS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDelaiPaiement = null;

    private transient CAReferenceBVR bvr = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe CO00ARappelPaiement.
     */
    public CO00ARappelPaiementCapCgas() {
    }

    /**
     * Cr�e une nouvelle instance de la classe CO00ARappelPaiement.
     *
     * @param session
     *            DOCUMENT ME!
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public CO00ARappelPaiementCapCgas(BSession session) throws FWIException {
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
        setTemplateFile(CO00ARappelPaiementCapCgas.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RAPPEL_PAIEMENT"));
        setNumeroReferenceInforom(CO00ARappelPaiementCapCgas.NUMERO_REFERENCE_INFOROM);

    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {

            if (ICOSequenceConstante.CS_SEQUENCE_CAP_CGAS.equalsIgnoreCase(curContentieux.getSequence()
                    .getLibSequence())) {
                setNumeroReferenceInforom(CO00ARappelPaiementCapCgas.NUMERO_REFERENCE_INFOROM_RAPPEL_CAP_CGAS);
                super.getDocumentInfo().setDocumentTypeNumber(
                        CO00ARappelPaiementCapCgas.NUMERO_REFERENCE_INFOROM_RAPPEL_CAP_CGAS);
                getCatalogueTextesUtil().setDomaineDocument(COCatalogueTextesService.CS_DOMAINE_CONTENTIEUX_CAP_CGAS);
                getCatalogueTextesUtil().setNomDocument(CO00ARappelPaiementCapCgas.NOM_DOCUMENT_RAPPEL_CAP_CGAS);
            }

            // destinataire est l'affili�
            destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
            _setLangueFromTiers(destinataireDocument);

            String adresse = getAdressePrincipale(destinataireDocument);

            // Gestion de l'en-t�te/pied de page/signature
            this._handleHeaders(curContentieux, true, false, true, adresse);

            // -- titre du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du titre du document
            StringBuffer body = new StringBuffer();

            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            // formater le titre, les conventions de remplacement pour les paragraphes du titre sont:
            // {0} = nom section
            // {1} = date de la section
            this.setParametres(
                    COParameter.T1,
                    formatMessage(body, new Object[] { curContentieux.getSection().getDescription(getLangue()),
                            formatDate(curContentieux.getSection().getDateSection()) }));

            // -- corps du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            // formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
            // {0} = formule de politesse
            // {1} = montant facture
            // {2} = date �ch�ance
            // {3} = date de d�lai de paiement
            // {4} = date de la section
            this.setParametres(
                    COParameter.T5,
                    formatMessage(body, new Object[] { getFormulePolitesse(destinataireDocument),
                            formatMontant(curContentieux.getSection().getSolde()),
                            formatDate(curContentieux.getSection().getDateEcheance()), formatDate(dateDelaiPaiement),
                            formatDate(curContentieux.getSection().getDateSection()) }));

            // -- BVR
            // ajout des taxes si necessaire
            FWCurrency montantTotal = curContentieux.getSection().getSoldeToCurrency();

            for (Iterator taxesIter = getTaxes().iterator(); taxesIter.hasNext();) {
                COTaxe taxe = (COTaxe) taxesIter.next();
                montantTotal.add(taxe.getMontantTaxe());
            }
            initBVR(montantTotal);
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * BVR
     *
     * @param montantTotal
     * @throws Exception
     */
    private void initBVR(FWCurrency montantTotal) throws Exception {
        // -- BVR
        FWCurrency cMontant = new FWCurrency(0.00);
        cMontant.add(montantTotal);

        // Recherche les informations pour remplir le bordereau
        String montantSansCentime = JAUtil.createBigDecimal(cMontant.toString()).toBigInteger().toString();
        BigDecimal montantSansCentimeBigDecimal = JAUtil.createBigDecimal(montantSansCentime);
        BigDecimal montantAvecCentimeBigDecimal = JAUtil.createBigDecimal(cMontant.toString());
        String centimes = montantAvecCentimeBigDecimal.subtract(montantSansCentimeBigDecimal).toString()
                .substring(2, 4);
        montantSansCentime = JANumberFormatter.formatNoRound(montantSansCentime);

        // Informations pour la r�f�rence et l'OCRB
        getBvr().setSession(getSession());
        getBvr().setBVR(curContentieux.getSection(), cMontant.toString());
        // commencer � �crire les param�tres
        String adresseDebiteur = "";

        try {
            adresseDebiteur = getAdresseDestinataire();
        } catch (Exception e) {
           LOG.error("A error occured while retrieving the address of the addressee.", e);
        }
        try {
            super.setParametres(COParameter.P_ADRESSE, getBvr().getAdresseBVR());
            super.setParametres(COParameter.P_ADRESSECOPY, getBvr().getAdresseBVR());
            super.setParametres(COParameter.P_COMPTE, getBvr().getNumeroCC());// num�ro CC
            super.setParametres(COParameter.P_VERSE, getBvr().getLigneReference() + "\n" + adresseDebiteur);
            super.setParametres(COParameter.P_PAR, adresseDebiteur);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(COParameter.P_OCR, getBvr().getOcrb());
            super.setParametres(COParameter.P_FRANC, JANumberFormatter.deQuote(montantSansCentime));
            super.setParametres(COParameter.P_CENTIME, centimes);
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la sommation : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
    }

    /**
     * Renvoie la r�f�rence BVR.
     *
     * @return la r�f�rence BVR.
     */
    private CAReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new CAReferenceBVR();
        }
        return bvr;
    }

    /**
     * @return l'adresse d�finie dans la section sinon getAdresseString(destinataireDocument)
     * @throws Exception
     */
    private String getAdresseDestinataire() throws Exception {
        return getAdressePrincipale(destinataireDocument);
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
