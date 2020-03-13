package globaz.aquila.print;

import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.service.taxes.COTaxe;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.pdf.JadePdfUtil;
import globaz.osiris.db.bulletinneutre.CAComptabiliserBulletinNeutre;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationBulletinNeutre;
import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class COSommationBN extends CODocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CAT_TEXTE_SOMMATION = "sommation BN";
    public static final String CAT_TEXTE_VOIE_DROIT = "sommation BN voies de droit";
    public static final String NUMERO_REFERENCE_INFOROM = "0261GCO";
    public static final int STATE_IDLE = 0;
    public static final int STATE_LETTRE = 1;
    public static final int STATE_VOIE_DROIT = 2;
    public static final String TAXE_SOMMATION_TEST = "15.00";
    public static final String TEMPLATE_NAME_SOMMATION = "CO_SOMMATION_BN_QR";
    public static final String TEMPLATE_NAME_VOIE_DROIT = "CO_SOMMATION_BN_VOIE_DROIT";
    public static final String TYPE_DOC_SOMMATION_BN = "5300048";

    private ReferenceBVR bvr = null;
    private String dateDelaiPaiement = null;
    private int state = COSommationBN.STATE_IDLE;

    public COSommationBN() throws Exception {
        super();
    }

    public COSommationBN(BSession session) throws FWIException {
        super(session, session.getLabel("AQUILA_SOMMATION_BN"));
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        computeTotalPage();
    }

    /**
     * Après la génération de chaque document
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
     * Avant la création de tous les documents
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        getCatalogueTextesUtil().setTypeDocument(COSommationBN.TYPE_DOC_SOMMATION_BN);
        setDocumentTitle(getSession().getLabel("AQUILA_SOMMATION_BN"));
        setNumeroReferenceInforom(COSommationBN.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {

        try {
            if (state == COSommationBN.STATE_LETTRE) {
                createDataSourceLettre();
            } else {
                createDataSourceVoiesDroit();
            }
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    private void createDataSourceLettre() throws Exception {
        getCatalogueTextesUtil().setNomDocument(COSommationBN.CAT_TEXTE_SOMMATION);
        setTemplateFile(COSommationBN.TEMPLATE_NAME_SOMMATION);

        String adresse = getAdresseDestinataire();

        this._handleHeaders(curContentieux, true, false, false, adresse);

        initTitreDoc(getParent());

        initDetail(getParent());

        initFooterDetail(getParent());

        if (CommonProperties.QR_FACTURE.getBooleanValue()) {
            // -- QR
            qrFacture = new ReferenceQR();
            qrFacture.setSession(getSession());
            // Initialisation des variables du document
            initVariableQR(null);
            qrFacture.setQrNeutre(true);
            // Génération du document QR
            qrFacture.initQR(this);
        } else {
            // BVR
            initBVR();
        }
    }

    private void createDataSourceVoiesDroit() throws Exception {
        getCatalogueTextesUtil().setNomDocument(COSommationBN.CAT_TEXTE_VOIE_DROIT);
        setTemplateFile(COSommationBN.TEMPLATE_NAME_VOIE_DROIT);

        StringBuilder body = new StringBuilder("");
        for (int i = 1; i <= 6; i++) {
            try {
                getCatalogueTextesUtil().dumpNiveau(getParent(), i, body, "\n");
                body.append("\n\n");
            } catch (Exception e) {
                this.log("exception: " + e.getMessage());
            }
        }

        this.setParametres(COParameter.P_TEXTVD, JadeStringUtil.isBlank(body.toString()) ? " " : body.toString());

    }


    public ReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new ReferenceBVR();
        }
        return bvr;
    }

    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    private void initBVR() throws Exception {

        // Informations pour la référence et l'OCRB
        getBvr().setSession(getSession());
        getBvr().setBVRNeutre(curContentieux.getSection());

        String adresseDebiteur = "";

        try {
            adresseDebiteur = getAdresseDestinataire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // Modification suite à QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE_NEUTRE.jasper");

            super.setParametres(COParameter.P_ADRESSE, getBvr().getAdresse());
            super.setParametres(COParameter.P_ADRESSECOPY, getBvr().getAdresse());
            super.setParametres(COParameter.P_COMPTE, getBvr().getNumeroCC());// numéro CC
            super.setParametres(COParameter.P_VERSE, getBvr().getLigneReference() + "\n" + adresseDebiteur);
            super.setParametres(COParameter.P_PAR, adresseDebiteur);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(COParameter.P_OCR, getBvr().getOcrb());
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la sommation : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
    }

    /**
     * @param key
     * @throws Exception
     */
    private void initDetail(Object key) throws Exception {
        LinkedList<HashMap<String, Comparable>> lignes = new LinkedList<HashMap<String, Comparable>>();
        HashMap<String, Comparable> fields;
        ArrayList<CAOperationBulletinNeutre> operationBN = CAComptabiliserBulletinNeutre.loadOperationsBN(
                getTransaction(), curContentieux.getSection().getIdSection());

        Iterator<CAOperationBulletinNeutre> it = operationBN.iterator();
        while (it.hasNext()) {
            CAOperation op = it.next();
            fields = new HashMap<String, Comparable>();
            fields.put(COParameter.F_COL_1B, op.getLibelle());
            fields.put(COParameter.F_COL_7, JANumberFormatter.fmt(op.getTaux(), true, true, false, 2)
                    + getCatalogueTextesUtil().texte(key, 3, 1));
            fields.put(COParameter.F_COL_5, getCatalogueTextesUtil().texte(key, 3, 2));
            lignes.add(fields);
        }

        fields = new HashMap<String, Comparable>();
        fields.put(COParameter.F_COL_1B, getCatalogueTextesUtil().texte(key, 3, 3));
        fields.put(COParameter.F_COL_5, getCatalogueTextesUtil().texte(key, 3, 2));
        if ((getTaxes() != null) && (getTaxes().size() == 1)) {
            fields.put(COParameter.F_COL_6, ((COTaxe) getTaxes().get(0)).getMontantTaxe());
        }
        lignes.add(fields);

        fields = new HashMap<String, Comparable>();
        fields.put(COParameter.F_COL_1B, "");
        lignes.add(fields);

        fields = new HashMap<String, Comparable>();

        /**
         * le field F_LINE_TOTAL indique la dernière ligne de la zone détail utilisé ensuite dans le report comme
         * condition pour afficher les lignes qui soulignent le total Print when expression new
         * Boolean($F{F_LINE_TOTAL}.booleanValue() ==true)
         */
        fields.put(COParameter.F_LINE_TOTAL, new Boolean("true"));

        fields.put(COParameter.F_COL_1B, getCatalogueTextesUtil().texte(key, 3, 4));
        fields.put(COParameter.F_COL_5, getCatalogueTextesUtil().texte(key, 3, 2));
        lignes.add(fields);

        this.setDataSource(lignes);

    }

    private void initFooterDetail(Object key) {
        StringBuilder body = new StringBuilder();
        getCatalogueTextesUtil().dumpNiveau(key, 4, body, "\n\n");
        this.setParametres(COParameter.P_REMARQUE, body.toString());
    }

    private void initTitreDoc(Object key) {

        StringBuilder body = new StringBuilder();

        getCatalogueTextesUtil().dumpNiveau(key, 1, body, "\n");
        this.setParametres(COParameter.P_LIBELLE, body.toString());
        this.setParametres(COParameter.P_DESC_SECTION, curContentieux.getSection().getDescription(getLangue())
                .toString());
    }

    /**
     * On surcharge la méthode next() afin de gérer les voies de droits au verso de la sommation.
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:
                if (super.next()) {
                    state = COSommationBN.STATE_LETTRE;
                    return true;
                } else {
                    // il n'y a plus de documents à créer
                    return false;
                }
            case STATE_LETTRE:
                // on vient de créer la lettre, on va créer les voies de droits
                state = COSommationBN.STATE_VOIE_DROIT;
                if (getCatalogueTextesUtil().isExistDocument(getParent(), COSommationBN.CAT_TEXTE_VOIE_DROIT)) {
                    return true;
                } else {
                    return false;
                }
            default:
                // on regarder s'il y a encore des contentieux à traiter.
                state = COSommationBN.STATE_IDLE;
                return next();
        }
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME_SOMMATION;
    }

    public void setDateDelaiPaiement(String dateDelaiPaiement) {
        this.dateDelaiPaiement = dateDelaiPaiement;
    }

}
