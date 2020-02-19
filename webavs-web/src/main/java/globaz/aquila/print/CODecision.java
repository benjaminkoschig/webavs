/*
 * Créé le 10 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.print;

import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
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
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.db.utils.CAReferenceQR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author sch
 */
public class CODecision extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CENT_DEFAUT = "XX";
    public static final String MONTANT_DEFAUT = "XXXX";
    public static final String NUMERO_REFERENCE_INFOROM = "0126GCO";

    public static final String OCRB_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String REFERENCE_NON_FACTURABLE_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final long serialVersionUID = -3645938861761414428L;
    private static final int STATE_IDLE = 0;

    private static final int STATE_LETTRE = 1;
    private static final int STATE_VD = 2;
    private static final String TEMPLATE_NAME = "CO_DECISION_QR";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME_VD = "CO_DECISION_VOIES_DROIT";

    private static final String TITLE_VOIES_DE_DROIT = "décision voies de droit";
    private static final String TYPE_AFFILI_EMPLOY = "804002";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private CAReferenceBVR bvr = null;
    private int state = CODecision.STATE_IDLE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CODecision.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public CODecision() throws Exception {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe CODecision.
     * 
     * @param session
     * @throws FWIException
     */
    public CODecision(BSession session) throws FWIException {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Pour les voies de droit
     * 
     * @see globaz.aquila.print.CODocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        super.getDocumentInfo().setDocumentTypeNumber(getNumeroReferenceInforom());
        try {
            getDocumentInfo().setDocumentProperty("annee", getAnneeFromContentieux());

            // Imprime les documents dans un fichier séparé selon la propriété
            // de la section
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
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CODecision.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_DECISION"));
        setNumeroReferenceInforom(CODecision.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            if (state == CODecision.STATE_LETTRE) {
                createDataSourceLettre();
            } else {
                createDataSourceVoiesDroit();
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    private void createDataSourceLettre() throws Exception {
        String adresse = getAdresseDestinataire();
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
            qrFacture = new CAReferenceQR();
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
     * Créé les voies de droit s'il existe un catalogue de textes pour.
     */
    private void createDataSourceVoiesDroit() {
        getCatalogueTextesUtil().setNomDocument(CODecision.TITLE_VOIES_DE_DROIT);

        if (getCatalogueTextesUtil().isExistDocument(getParent(), CODecision.TITLE_VOIES_DE_DROIT)) {
            setTemplateFile(CODecision.TEMPLATE_NAME_VD);
            setDocumentTitle(getSession().getLabel("AQUILA_DECISION"));

            StringBuilder body = new StringBuilder();
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
    public CAReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new CAReferenceBVR();
        }
        return bvr;
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
        montantSansCentime = globaz.globall.util.JANumberFormatter.formatNoRound(montantSansCentime);

        // Informations pour la référence et l'OCRB
        getBvr().setSession(getSession());
        getBvr().setBVR(curContentieux.getSection(), cMontant.toString());

        // commencer à écrire les paramètres
        String adresseDebiteur = "";

        try {
            adresseDebiteur = getAdresseDestinataire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // Modification suite à QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");
            super.setParametres(COParameter.P_SUBREPORT_QR_CURRENT_PAGE, getImporter().getImportPath() + "BVR_TEMPLATE_CURRENT_PAGE.jasper");

            // Paramètre de footer si document sur plusieurs pages
            // N'est plus utilisé sur document CO_DECISION, car il est sur une seule page
            super.setParametres(FWIImportParametre.PARAM_REFERENCE + "_X", CODecision.REFERENCE_NON_FACTURABLE_DEFAUT);
            super.setParametres(COParameter.P_OCR + "_X", CODecision.OCRB_DEFAUT);
            super.setParametres(COParameter.P_FRANC + "_X", CODecision.MONTANT_DEFAUT);
            super.setParametres(COParameter.P_CENTIME + "_X", CODecision.CENT_DEFAUT);
            //

            super.setParametres(COParameter.P_ADRESSE, getBvr().getAdresse());
            super.setParametres(COParameter.P_ADRESSECOPY, getBvr().getAdresse());
            super.setParametres(COParameter.P_COMPTE, getBvr().getNumeroCC());// numéro
            // CC
            super.setParametres(COParameter.P_VERSE, getBvr().getLigneReference() + "\n" + adresseDebiteur);
            super.setParametres(COParameter.P_PAR, adresseDebiteur);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(COParameter.P_OCR, getBvr().getOcrb());
            super.setParametres(COParameter.P_FRANC, JANumberFormatter.deQuote(montantSansCentime));
            super.setParametres(COParameter.P_CENTIME, centimes);
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la Décision : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
    }

    /**
     * corps du doc
     * 
     * @param key
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

        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule de
         * politesse {1} = date rappel
         */
        this.setParametres(
                COParameter.T5,
                formatMessage(
                        body,
                        new Object[] {
                                getFormulePolitesse(destinataireDocument),
                                formatMessage(optionnel, new Object[] { formatDate(curContentieux.getDateExecution()) }) }));
    }

    /**
     * boucle de detail
     * 
     * @return
     * @throws Exception
     */
    private FWCurrency initDetail(Object key) throws Exception {
        // -- boucle de detail
        // si on a des taxes, on les affichent avec un total, sinon, on affiche
        // juste le solde de la section
        HashMap fields = new HashMap();

        String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                curContentieux.getIdSection());

        // Détail de la section avec paiements
        List dataSource = new ArrayList();
        fields.put(COParameter.F1, curContentieux.getSection().getDescription(getLangue()));
        fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
        fields.put(COParameter.F3, formatMontant(infoSection[0]));
        dataSource.add(fields);

        List situation = createSituationCompteDS(infoSection[2], COParameter.F1, COParameter.F3, COParameter.F2,
                getCatalogueTextesUtil().texte(key, 3, 2), ICOEtape.CS_DECISION, infoSection[1]);
        dataSource.addAll(situation);
        // ajout des taxes si necessaire
        FWCurrency montantTotal = new FWCurrency(infoSection[0]);
        Iterator it = situation.iterator();
        while (it.hasNext()) {
            HashMap m = (HashMap) it.next();
            montantTotal.add(new FWCurrency((String) m.get(COParameter.F3)));
        }

        if (getTaxes() != null) {
            for (Iterator taxesIter = getTaxes().iterator(); taxesIter.hasNext();) {
                COTaxe taxe = (COTaxe) taxesIter.next();

                montantTotal.add(taxe.getMontantTaxe());

                fields = new HashMap();
                String libelle = "";
                if ((taxe.getLibelle(getSession()) != null) || JadeStringUtil.isBlank(taxe.getLibelle(getSession()))) {
                    libelle = taxe.getLibelle(getSession());
                } else {
                    libelle = taxe.loadCalculTaxe(getSession()).getRubrique().getDescription(getLangue());
                }
                fields.put(COParameter.F1, libelle);
                fields.put(COParameter.F2, getCatalogueTextesUtil().texte(key, 3, 2));
                fields.put(COParameter.F3, formatMontant(taxe.getMontantTaxe()));
                dataSource.add(fields);
            }
        }

        this.setDataSource(dataSource);

        if (montantTotal != null) {
            this.setParametres(COParameter.P_TOT_DEVISE, getCatalogueTextesUtil().texte(key, 3, 2));
            this.setParametres(COParameter.P_TOT_MONTANT, formatMontant(montantTotal.toString()));
            this.setParametres(COParameter.P_TOT_LIBELLE, getCatalogueTextesUtil().texte(key, 3, 3));
        }

        return montantTotal;
    }

    /**
     * texte en dessous du detail
     * 
     * @throws Exception
     */
    private void initTexteDetail(Object key) throws Exception {
        // -- texte en dessous du detail
        StringBuilder body = new StringBuilder();

        // Si l'affiliation est de type employeur on affiche la phrase de niveau
        // 5
        if (curContentieux.getSection().getCompteAnnexe().getIdCategorie().equals(CODecision.TYPE_AFFILI_EMPLOY)) {
            getCatalogueTextesUtil().dumpNiveau(key, 5, body, "\n\n");
        }

        // rechercher tous les paragraphes du corps du document
        getCatalogueTextesUtil().dumpNiveau(key, 4, body, "\n\n");

        /*
         * formater le corps, les conventions de remplacement pour les paragraphes du corps sont: {0} = formule de
         * politesse {1} = date de délai du paiement
         */
        this.setParametres(COParameter.T6,
                formatMessage(body, new Object[] { getFormulePolitesse(destinataireDocument) }));
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

        /*
         * formater le titre, les conventions de remplacement pour les paragraphes du titre sont: {0} = nom section ;
         * {1} = date de la section
         */
        this.setParametres(
                COParameter.T1,
                formatMessage(body, new Object[] { curContentieux.getSection().getDescription(getLangue()),
                        curContentieux.getSection().getDateSection() }));
        return body;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.print.CODocumentManager#next()
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:
                // on commence ou a deja traité un contentieux, on regarde s'il
                // reste des contentieux a traiter
                if (super.next()) {
                    state = CODecision.STATE_LETTRE;
                    // on va créer la lettre
                    return true;
                } else {
                    // il n'y a plus de documents à créer
                    return false;
                }
            case STATE_LETTRE:
                // on vient de créer la lettre, on va créer les voies de droits
                state = CODecision.STATE_VD;
                if (getCatalogueTextesUtil().isExistDocument(getParent(), CODecision.TITLE_VOIES_DE_DROIT)) {
                    return true;
                } else {
                    return false;
                }
            default:
                // on regarder s'il y a encore des contentieux à traiter.
                state = CODecision.STATE_IDLE;
                return next();
        }
    }

}
