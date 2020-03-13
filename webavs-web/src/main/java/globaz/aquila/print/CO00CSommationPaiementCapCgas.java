/*
 * Créé le 10 janv. 06
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.print;

import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.service.cataloguetxt.COCatalogueTextesService;
import globaz.aquila.service.taxes.COTaxe;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.pdf.JadePdfUtil;
import globaz.osiris.api.APISection;
import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Cette classe permet de créer un document de sommation pour CCVD et Agrivit dans le cadre d'une séquence CAP/CGAS.
 *
 * <H1>Description</H1> Document : Sommation <br>
 * Niveaux et paramètres utilisés : <br>
 * <ul>
 * <li>niveau 1 : Dump du niveau 1. <br>
 * {0} = nom section ; <br>
 * {1} = date de la section
 * <li>niveau 2 : Dump du niveau 2. <br>
 * {0} = formule de politesse <br>
 * {1} = niveau 9 avec la date rappel. <br>
 * {2} = date de la section
 * <li>niveau 3 : Position pris séparéments. Pas de paramètre possible.
 * <li>niveau 4 : Dump du niveau 4. <br>
 * {0} = formule de politesse <br>
 * {1} = date de délai du paiement
 * <li>niveau 5 : Dump du niveau. Ce niveau s'affiche uniquement pour les comptes annexes de catégorie employeur
 * (804002).
 * <li>niveau 9 : Si Premier rappel envoyé, dump du niveau 9. Paramètre possible : date d'execution.
 * </ul>
 * Document : Voies de droits <br>
 * Prend tous les niveaux et les concatènes. Aucun paramètre possible.
 *
 * @author vre, sel
 */
public class CO00CSommationPaiementCapCgas extends CODocumentManager {

    private static final Logger LOG = LoggerFactory.getLogger(CO00CSommationPaiementCapCgas.class);

    public static final String NOM_DOCUMENT_SOMMATION_CAP_CGAS = "Sommation CAP/CGAS";
    /**
     * Numéro Inforom pour la sommation LTN
     */
    public static final String NUM_REF_SOMMATION_LTN = "0197GCO";
    /**
     * Numéro Inforom
     */
    public static final String NUMERO_REFERENCE_INFOROM = "0022GCO";

    public static final String NUMERO_REFERENCE_INFOROM_SOMMATION_CAP_CGAS = "0297GCO";

    private static final long serialVersionUID = -3645938861761414428L;

    /**
     * Nom du document du catalogue de textes à utiliser pour les sommation LTN
     */
    private static final String SOMMATION_LTN = "sommation LTN";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final int STATE_IDLE = 0;
    private static final int STATE_LETTRE = 1;
    private static final int STATE_VD = 2;

    /**
     * Modele Jasper
     */
    private static final String TEMPLATE_NAME = "CO_00C_SOMMATION_AF_CAP_CGAS_QR";
    /**
     * Le nom du modèle Jasper pour les voies de droits
     */
    private static final String TEMPLATE_NAME_VD = "CO_00C_SOMMATION_VOIES_DROIT";

    private static final String TITLE_VOIES_DE_DROIT = "sommation voies de droit";
    private static final String TYPE_AFFILI_EMPLOY = "804002";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient ReferenceBVR bvr = null;
    private String dateDelaiPaiement = null;
    private int state = CO00CSommationPaiementCapCgas.STATE_IDLE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CO00CSommationPaiement.
     */
    public CO00CSommationPaiementCapCgas() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe CO00SommationPaiement.
     *
     * @param session
     * @throws FWIException
     */
    public CO00CSommationPaiementCapCgas(BSession session) throws FWIException {
        super(session, session.getLabel("AQUILA_SOMMATION"));
    }


    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        computeTotalPage();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     *
     * @see globaz.aquila.print.CODocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        super.getDocumentInfo().setDocumentTypeNumber(getNumeroReferenceInforom());
        try {
            getDocumentInfo().setDocumentProperty("annee", getAnneeFromContentieux());

            // Imprime les documents dans un fichier séparé selon la propriété de la section
            if (curContentieux.getSection().getNonImprimable().booleanValue()) {
                getDocumentInfo().setSeparateDocument(true);
            }
        } catch (Exception e) {
            this._addError(e.toString());
        }
    }

    /**
     * Après la création de tous les documents
     */
    @Override
    public void afterExecuteReport() {
        try {
            if ((getSession().getApplication().getProperty(CODocumentManager.GESTION_VERSO_AQUILA) == null)
                    || getSession().getApplication().getProperty(CODocumentManager.GESTION_VERSO_AQUILA)
                    .equals(CODocumentManager.AVEC_VERSO)) {
                this.mergePDF(getDocumentInfo(), true, 500, false, null, JadePdfUtil.DUPLEX_ON_FIRST);
            } else {
                this.mergePDF(getDocumentInfo(), true, 500, false, null);
            }
        } catch (Exception e) {
            this._addError(e.toString());
        }
    }

    /**
     * @throws FWIException
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CO00CSommationPaiementCapCgas.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_SOMMATION"));
        setNumeroReferenceInforom(CO00CSommationPaiementCapCgas.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        try {
            if (state == CO00CSommationPaiementCapCgas.STATE_LETTRE) {
                createDataSourceLettre();
            } else {
                createDataSourceVoiesDroit();
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * DataSource pour la lettre de sommation
     *
     * @throws Exception
     */
    private void createDataSourceLettre() throws Exception {

        if (APISection.ID_CATEGORIE_SECTION_LTN.equals(curContentieux.getSection().getCategorieSection())
                || APISection.ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE.equals(curContentieux.getSection()
                .getCategorieSection())) {
            // Setter pour un CT différent
            getCatalogueTextesUtil().setNomDocument(CO00CSommationPaiementCapCgas.SOMMATION_LTN);
            setNumeroReferenceInforom(CO00CSommationPaiementCapCgas.NUM_REF_SOMMATION_LTN);
        }

        setNumeroReferenceInforom(CO00CSommationPaiementCapCgas.NUMERO_REFERENCE_INFOROM_SOMMATION_CAP_CGAS);
        getCatalogueTextesUtil().setDomaineDocument(COCatalogueTextesService.CS_DOMAINE_CONTENTIEUX_CAP_CGAS);
        getCatalogueTextesUtil().setNomDocument(CO00CSommationPaiementCapCgas.NOM_DOCUMENT_SOMMATION_CAP_CGAS);

        String adresse = getAdresseDestinataire();

        // Bug 7823
        setDocumentConfidentiel(true);
        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(curContentieux, true, false, true, adresse);

        // -- titre du doc
        initTitreDoc(getParent());

        // -- corps du doc
        initCorpsDoc(getParent());

        // -- boucle de detail
        FWCurrency montantTotal = initDetail(getParent());

        // -- texte en dessous du detail
        initTexteDetail(getParent());

        if (CommonProperties.QR_FACTURE.getBooleanValue()) {
            // -- QR
            qrFacture = new ReferenceQR();
            qrFacture.setSession(getSession());
            // Initialisation des variables du document
            initVariableQR(montantTotal);
            // Génération du document QR
            qrFacture.initQR(this);
        } else {
            // -- BVR
            initBVR(montantTotal);
        }

    }

    /**
     * DataSource pour les voies de droits
     */
    private void createDataSourceVoiesDroit() {
        getCatalogueTextesUtil().setNomDocument(CO00CSommationPaiementCapCgas.TITLE_VOIES_DE_DROIT);

        if (getCatalogueTextesUtil().isExistDocument(getParent(), CO00CSommationPaiementCapCgas.TITLE_VOIES_DE_DROIT)) {
            setTemplateFile(CO00CSommationPaiementCapCgas.TEMPLATE_NAME_VD);
            setDocumentTitle(getSession().getLabel("AQUILA_SOMMATION"));

            StringBuilder body = new StringBuilder("");
            // rechercher tous les paragraphes du corps du document
            for (int i = 1; i <= 9; i++) {
                try {
                    getCatalogueTextesUtil().dumpNiveau(getParent(), i, body, "\n");
                } catch (Exception e) {
                    this.log("exception: " + e.getMessage());
                }
            }

            this.setParametres(COParameter.P_TEXTVD, JadeStringUtil.isBlank(body.toString()) ? " " : body.toString());
        }
    }

    /**
     * Renvoie la référence BVR.
     *
     * @return la référence BVR.
     */
    public ReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new ReferenceBVR();
        }
        return bvr;
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

        // Informations pour la référence et l'OCRB
        getBvr().setSession(getSession());
        getBvr().setBVR(curContentieux.getSection(), cMontant.toString());
        // commencer à écrire les paramètres
        String adresseDebiteur = "";

        try {
            adresseDebiteur = getAdresseDestinataire();
        } catch (Exception e) {
            LOG.error("A error occured while retrieving the address of the addressee.", e);
        }
        try {
            // Modification suite à QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");

            super.setParametres(COParameter.P_ADRESSE, getBvr().getAdresse());
            super.setParametres(COParameter.P_ADRESSECOPY, getBvr().getAdresse());
            super.setParametres(COParameter.P_COMPTE, getBvr().getNumeroCC());// numéro CC
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
     * corps du doc
     *
     * @throws Exception
     */
    private void initCorpsDoc(Object key) throws Exception {
        // -- corps du doc
        StringBuilder body = new StringBuilder();
        // rechercher tous les paragraphes du corps du document
        getCatalogueTextesUtil().dumpNiveau(key, 2, body, "\n\n");

        StringBuilder optionnel = new StringBuilder("");

        if (getTransition().getEtape().getLibEtape().equals(ICOEtape.CS_PREMIER_RAPPEL_ENVOYE)) {
            getCatalogueTextesUtil().dumpNiveau(key, 9, optionnel, " ");
        }

        /**
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: <br/>
         * {0} = formule de politesse <br/>
         * {1} = date rappel <br/>
         * {2} = date de la section
         */
        this.setParametres(
                COParameter.T5,
                formatMessage(
                        body,
                        new Object[]{
                                getFormulePolitesse(destinataireDocument),
                                formatMessage(optionnel, new Object[]{formatDate(curContentieux.getDateExecution())}),
                                formatDate(curContentieux.getSection().getDateSection())}));
    }

    /**
     * boucle de detail
     *
     * @return
     */
    private FWCurrency initDetail(Object key) {
        LinkedList<HashMap<String, String>> lignes = new LinkedList<>();
        HashMap<String, String> fields = new HashMap<>();

        fields.put(COParameter.F1, getCatalogueTextesUtil().texte(key, 3, 1));
        fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
        fields.put(COParameter.F3, formatMontant(curContentieux.getSection().getSolde()));
        lignes.add(fields);

        // ajout des taxes si necessaire
        FWCurrency montantTotal = curContentieux.getSection().getSoldeToCurrency();

        for (Iterator taxesIter = getTaxes().iterator(); taxesIter.hasNext(); ) {
            COTaxe taxe = (COTaxe) taxesIter.next();
            montantTotal.add(taxe.getMontantTaxe());
        }

        this.setDataSource(lignes);
        return montantTotal;
    }

    /**
     * texte en dessous du detail
     *
     * @throws Exception
     */
    private void initTexteDetail(Object key) throws Exception {
        StringBuilder body = new StringBuilder();

        // Si l'affiliation est de type employeur on affiche la phrase de niveau 5
        if (curContentieux.getSection().getCompteAnnexe().getIdCategorie()
                .equals(CO00CSommationPaiementCapCgas.TYPE_AFFILI_EMPLOY)) {
            getCatalogueTextesUtil().dumpNiveau(key, 5, body, "\n\n");
        }

        // rechercher tous les paragraphes du corps du document
        getCatalogueTextesUtil().dumpNiveau(key, 4, body, "\n\n");

        /**
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: <br/>
         * {0} = formule de politesse <br/>
         * {1} = date de délai du paiement
         */
        this.setParametres(
                COParameter.T6,
                formatMessage(body, new Object[]{getFormulePolitesse(destinataireDocument),
                        formatDate(dateDelaiPaiement)}));
    }

    /**
     * titre du doc
     *
     * @return
     */
    private StringBuilder initTitreDoc(Object key) {
        // -- titre du doc
        // rechercher tous les paragraphes du titre du document
        StringBuilder body = new StringBuilder();

        getCatalogueTextesUtil().dumpNiveau(key, 1, body, "\n");

        /**
         * formater le titre, les conventions de remplacement pour les paragraphes du titre sont: <br/>
         * {0} = nom section ; <br/>
         * {1} = date de la section
         */
        this.setParametres(
                COParameter.T1,
                formatMessage(body, new Object[]{curContentieux.getSection().getDescription(getLangue()),
                        curContentieux.getSection().getDateSection()}));
        return body;
    }

    /**
     * On surcharge la méthode next() afin de gérer les voies de droits au verso de la sommation.
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:
                if (super.next()) {
                    state = CO00CSommationPaiementCapCgas.STATE_LETTRE;
                    // on va créer la lettre
                    return true;
                } else {
                    // il n'y a plus de documents à créer
                    return false;
                }
            case STATE_LETTRE:
                // on vient de créer la lettre, on va créer les voies de droits
                state = CO00CSommationPaiementCapCgas.STATE_VD;
                return (getCatalogueTextesUtil().isExistDocument(getParent(), CO00CSommationPaiementCapCgas.TITLE_VOIES_DE_DROIT));
            default:
                // on regarder s'il y a encore des contentieux à traiter.
                state = CO00CSommationPaiementCapCgas.STATE_IDLE;
                return next();
        }
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

    /**
     * setter pour l'attribut date delai paiement.
     *
     * @param dateDelaiPaiement une nouvelle valeur pour cet attribut
     */
    public void setDateDelaiPaiement(String dateDelaiPaiement) {
        this.dateDelaiPaiement = dateDelaiPaiement;
    }
}
