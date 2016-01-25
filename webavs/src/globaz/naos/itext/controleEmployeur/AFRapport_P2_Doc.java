/*
 * Créé le 14 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.pyxis.api.ITIRole;
import java.util.Map;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFRapport_P2_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Numéro du document
    private static final String DOC_NO = "0186CAF";

    public final static String PROP_SIGN_NOM_CAISSE = "signature.nom.caisse.";
    /**
     * @param session
     * @param nomDocument
     * @throws FWIException
     */
    public final static String TEMPLATE_FILENAME = "NAOS_RAPPORT2_CE";

    public static String getTemplateFilename() {
        return AFRapport_P2_Doc.TEMPLATE_FILENAME;
    }

    protected java.lang.String adresseDomicile;

    protected String adressePaiement;
    protected String adressePrincipale;
    AFControleEmployeur controle = new AFControleEmployeur();
    ICTDocument document = null;
    ICTDocument[] documents = null;
    private String emailAdress = new String();
    private Boolean envoyerGed = new Boolean(false);
    private boolean hasNext = true;
    private String idControle = new String();
    private String idPersRef = new String();
    int index = 1;

    AFControleEmployeurManager manager = new AFControleEmployeurManager();

    int nbNiveaux = 0;

    protected java.lang.String numeroCompte;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public AFRapport_P2_Doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    public AFRapport_P2_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("NAOS_CONTROLES_TITRE_P2"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFRapport_P2_Doc(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("NAOS_CONTROLES_TITRE_P2"));
    }

    public AFRapport_P2_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("NAOS_CONTROLES_TITRE_P2"));
        this.idControle = idControle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:33)
     */
    protected void _summaryText() {

        try {
            String autre = getSession().getApplication().getLabel("RAPPORT_AUTRE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_AUTRE, autre);
            String affLaa = getSession().getApplication().getLabel("RAPPORT_AFF_LAA", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_AFF_LAA, affLaa);
            String affLpp = getSession().getApplication().getLabel("RAPPORT_AFF_LPP", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_AFF_LPP, affLpp);
            String afOui = (getSession().getApplication().getLabel("NAOS_LIBELLE_OUI", controle.getLangueTiers()));
            super.setParametres(AFRapport_Param.L_OUI, afOui);
            String afNon = (getSession().getApplication().getLabel("NAOS_LIBELLE_NON", controle.getLangueTiers()));
            super.setParametres(AFRapport_Param.L_NON, afNon);
            String complet = getSession().getApplication().getLabel("RAPPORT_COMPLET", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_COMPLET, complet);
            String sondage = getSession().getApplication().getLabel("RAPPORT_SONDAGE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_SONDAGE, sondage);
            String compta = getSession().getApplication().getLabel("RAPPORT_COMPTA", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_COMPTA, compta);
            String bilan = getSession().getApplication().getLabel("RAPPORT_BILAN", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_BILAN, bilan);
            String apg = getSession().getApplication().getLabel("RAPPORT_APG", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_APG, apg);
            String alloc = getSession().getApplication().getLabel("RAPPORT_ALLOC", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_ALLOC, alloc);
            String af = getSession().getApplication().getLabel("RAPPORT_AF", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_AF, af);
            String afSepare = getSession().getApplication().getLabel("RAPPORT_AF_SEPARE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_AF_SEPARE, afSepare);
            String remConstate = getSession().getApplication().getLabel("RAPPORT_REM_CONSTATE",
                    controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_REM_CONSTATE, remConstate);
            String remConseil = getSession().getApplication()
                    .getLabel("RAPPORT_REM_CONSEIL", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_REM_CONSEIL, remConseil);
            String remRemarque = getSession().getApplication().getLabel("RAPPORT_REM_REMARQUE",
                    controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_REM_REMARQUE, remRemarque);

        } catch (Exception e) {
            this._addError(null, e.getMessage());
        }

        super.setParametres(AFRapport_Param.P_APG_COM, controle.getDocAllocPerteComplet());
        super.setParametres(AFRapport_Param.P_APG_SON, controle.getDocAllocPerteSondage());
        super.setParametres(AFRapport_Param.P_ALLOC_COM, controle.getDocAllocMiliComplet());
        super.setParametres(AFRapport_Param.P_ALLOC_SON, controle.getDocAllocMiliSondage());
        super.setParametres(AFRapport_Param.P_AF_COM, controle.getDocDroitAllocComplet());
        super.setParametres(AFRapport_Param.P_AF_SON, controle.getDocDroitAllocSondage());
        if (controle.getRapportAFSepare().booleanValue()) {
            super.setParametres(AFRapport_Param.P_AF_OUI, "X");
            super.setParametres(AFRapport_Param.P_AF_NON, "");
        } else {
            super.setParametres(AFRapport_Param.P_AF_OUI, "");
            super.setParametres(AFRapport_Param.P_AF_NON, "X");
        }
        super.setParametres(AFRapport_Param.P_REM_CONSTATE, controle.getChampConstate());
        super.setParametres(AFRapport_Param.P_REM_CONSEIL, controle.getChampConseil());
        super.setParametres(AFRapport_Param.P_REM_REMARQUE, controle.getChampRemarque());
        super.setParametres(
                AFRapport_Param.P_SIGNATURE,
                getTemplateProperty(getDocumentInfo(),
                        AFRapport_P2_Doc.PROP_SIGN_NOM_CAISSE + JadeStringUtil.toUpperCase(controle.getLangueTiers())));

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        super.setDocumentTitle(controle.getNumAffilie() + " -2- " + controle.getNomTiers());
    }

    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTemplateFile(AFRapport_P2_Doc.TEMPLATE_FILENAME);
        if (getEnvoyerGed().booleanValue()) {
            setTailleLot(1);
        } else {
            setTailleLot(500);
        }
        // Initialise le document pour le catalogue de texte
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();
        // super.setTemplateFile(getTemplateFilename(getEntity()));
        // setTemplateFile(TEMPLATE_FILENAME);
        manager.setSession(getSession());
        manager.setForControleEmployeurId(idControle);
        manager.find();
        if (manager.size() > 0) {
            controle = (AFControleEmployeur) manager.getFirstEntity();
        }

        // initialiser les variables d'aide

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());
        CaisseHeaderReportBean hb = new CaisseHeaderReportBean();
        hb.setNomCollaborateur(getSession().getUserFullName());
        hb.setUser(getSession().getUserInfo());

        _summaryText();
        String reviseur = "";
        if (!JadeStringUtil.isEmpty(controle.getControleurNom())) {
            reviseur = controle.getControleurNom();
        }
        caisseReportHelper.addHeaderParameters(this, hb);
        caisseReportHelper.addSignatureParameters(this);

        String nomReviseurSign = (getSession().getApplication()).getProperty("nomReviseurSignature");

        if (!JadeStringUtil.isBlank(nomReviseurSign) && nomReviseurSign.equals("true")) {
            Map map = getImporter().getParametre();

            String signataires = map.get(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE) + "";

            this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, signataires + "       " + reviseur);
        }

        // if(user!=null){
        // caisseReportHelper.addFooterParameters(this, user);
        // }

    }

    private void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber(AFRapport_P2_Doc.DOC_NO);
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", controle.getNumAffilie());

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(controle.getNumAffilie()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), controle.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    controle.getNumAffilie(), affilieFormater.unformat(controle.getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", controle.getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
    }

    /**
     * @return
     */
    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    /**
     * @return
     */
    public String getIdControle() {
        return idControle;
    }

    // protected JasperPrint getVerso() throws Exception {
    // String langue = controle.getLangueTiers();
    // String documentKey = "";
    // JasperPrint doc = null;
    // String sousType = entity.getIdSousType();
    // sousType = sousType.substring(4,6);
    // int type = JadeStringUtil.parseInt(sousType, 0);
    //
    // if((type >= 1 && type <=12)||(type>=40 && type<=46)){
    // if(((BApplication)
    // getSession().getApplication()).getProperty("gestionVersoAcompte").equals("avec")){
    // documentKey = "MUSCA_FACTURE_VERSO_" +
    // getDecisionProVersoPourCaisse(langue);
    // }else{
    // documentKey = "MUSCA_FACTURE_VERSO";
    // }
    // }else{
    // if(((BApplication)
    // getSession().getApplication()).getProperty("gestionVersoDecompte").equals("avec")){
    // documentKey = "MUSCA_FACTURE_VERSO_" +
    // getDecisionProVersoPourCaisse(langue);
    // }else{
    // documentKey = "MUSCA_FACTURE_VERSO";
    // }
    // }
    // try{
    // //if ((doc = (JasperPrint) m_cache.get(documentKey)) == null)
    // doc = super.getImporter().importReport(documentKey,
    // super.getImporter().getImportPath());
    // } catch (Exception e) {
    // doc = null;
    // }
    // if (doc == null){
    // // Recherche verso par défaut
    // documentKey = "MUSCA_FACTURE_VERSO";
    // try{
    // //if ((doc = (JasperPrint) m_cache.get(documentKey)) == null)
    // doc = super.getImporter().importReport(documentKey,
    // super.getImporter().getImportPath());
    // } catch (Exception e) {
    // doc = null;
    // }
    // }
    //
    // return doc;
    // }
    // /**
    // * @return
    // */
    // private String getDecisionProVersoPourCaisse(String langue) {
    // String numCaisse = null;
    // try {
    // numCaisse = ((BApplication)
    // getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
    // } catch (Exception e) {
    // JadeLogger.error(this,
    // "Erreur pendant la lecture des properties du numéro de caisse pour l'impression du verso");
    // return langue;
    // }
    // if(JAUtil.isStringNull(numCaisse)){
    // return langue;
    // }
    // else if(JAUtil.isStringEmpty(numCaisse)){
    // return langue;
    // }
    // return numCaisse+"_"+langue;
    // }

    // public void afterBuildReport() {
    // try {
    // if(((BApplication)
    // getSession().getApplication()).getProperty("gestionVerso").equals("avec")){
    // JasperPrint verso = null;
    // verso = getVerso();
    // if (verso != null){
    // verso.setName(entity.getIdExterneRole() + " - " + index + " - 2_VERSO");
    // index++;
    // super.getDocumentList().add(verso);
    // }
    // }
    // //Info
    // setDocumentInfo();
    // //GED
    // if(getEnvoyerGed().booleanValue()){
    // getDocumentInfo().setPublishDocument(false);
    // getDocumentInfo().setArchiveDocument(true);
    // }else{
    // getDocumentInfo().setPublishDocument(true);
    // getDocumentInfo().setArchiveDocument(false);
    // }
    // }catch (Exception e){
    // JadeLogger.error(this,
    // "Erreur pendant la lecture des properties l'impression du verso");
    // }
    // }

    /*
     * Insertion des infos pour la publication (GED)
     */
    // public void setDocumentInfo() throws FWIException {
    // FAApplication app = null;
    // IFormatData affilieFormater = null;
    // try {
    // app = (FAApplication)getSession().getApplication();
    // affilieFormater= app.getAffileFormater();
    // } catch (Exception e1) {
    // getMemoryLog().logMessage("",FWMessage.ERREUR,getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE"));
    // }
    // try {
    // //On rempli le documentInfo avec les infos du document
    // TIDocumentInfoHelper.fill(getDocumentInfo(),
    // entity.getIdTiers(),getSession(),entity.getIdRole(),
    // entity.getIdExterneRole(),
    // affilieFormater.unformat(entity.getIdExterneRole()));
    // CADocumentInfoHelper.fill(getDocumentInfo(),
    // entity.getIdExterneFacture(), entity.getIdTypeFacture(),
    // entity.getImDate());
    // getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID,
    // "FAC");
    // } catch (Exception e) {
    // getMemoryLog().logMessage(e.getMessage().toString(),FWMessage.ERREUR,getSession().getLabel("GED_ERROR_GETTING_TIERS_INFO"));
    // }
    // }

    /**
     * @return
     */
    public String getIdPersRef() {
        return idPersRef;
    }

    /**
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterExecuteReport()
     */
    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterExecuteReport()
     */
    // public void afterExecuteReport() {
    // super.afterExecuteReport();
    // //Envoie un e-mail avec les pdf fusionnés
    //
    // if(getEnvoyerGed().booleanValue()){
    // JadePublishDocumentInfo info = createDocumentInfo();
    // try {
    // mergePDFAndAdd(info);
    // } catch (Exception e) {
    // JadeLogger.error("Erreur lors du merge des PDF");
    // }
    // }
    // }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = false;
        }

        return retValue;
    }

    /**
     * @param boolean1
     */
    public void setEnvoyerGed(Boolean boolean1) {
        envoyerGed = boolean1;
    }

    /**
     * @param string
     */
    public void setIdControle(String string) {
        idControle = string;
    }

    /**
     * @param string
     */
    public void setIdPersRef(String string) {
        idPersRef = string;
    }

    /**
     * @param i
     */
    public void setIndex(int i) {
        index = i;
    }

}
