package globaz.draco.print.itext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSInscAnneeMaxMinEntity;
import globaz.draco.db.inscriptions.DSInscAnneeMaxMinManager;
import globaz.draco.util.DSUtil;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import java.util.HashMap;

public class DSImpressionDeclaration extends FWIDocumentManager {

    private static final long serialVersionUID = -243433120764181674L;
    private AFAffiliation affEnCours = null;
    private DSDeclarationViewBean declaration = null;
    private String DRACO_PREIMP_DECSAL_MASTER_1 = "DRACO_PREIMP_DECSAL_MASTER_1";
    private String DRACO_REMP_DECSAL_MASTER_1 = "DRACO_REMP_DECSAL_MASTER_1";
    private String DRACO_REMP_DECSAL_MASTER_2 = "DRACO_REMP_DECSAL_MASTER_2";
    private String idDeclaration = "";
    private boolean isFirst = true;
    private String langueForCS = "";
    private String langueIsoTiers = "";
    private String MODEL_NAME = "";

    public DSImpressionDeclaration() throws Exception {
        this(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
        super.setDocumentTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));
    }

    /**
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public DSImpressionDeclaration(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
        // TODO Raccord de constructeur auto-généré
    }

    /**
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public DSImpressionDeclaration(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
        // TODO Raccord de constructeur auto-généré
    }

    public DSImpressionDeclaration(globaz.globall.db.BSession session) throws Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, "ImpressionDeclaration");

    }

    protected void _headerText() throws Exception {
        // Paramètres à setter si on imprime la déclaration
        super.setParametres(Doc1_PreImp_Param.P_HEAD_1,
                getSession().getApplication().getLabel("ENTETE_CAISSE", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.P_HEAD_2, getLibelleLangue(getDeclaration().getTypeDeclaration()));

        String labelNumAff = getSession().getApplication().getLabel("ENTETE_NUMEROAFFILIE", langueIsoTiers);

        if (!JadeStringUtil.isEmpty(AFIDEUtil.defineNumeroStatutForDoc(affEnCours.getNumeroIDE(),
                affEnCours.getIdeStatut()))) {
            labelNumAff += "/" + getSession().getApplication().getLabel("ENTETE_NUMEROIDE", langueIsoTiers);
        }

        super.setParametres(Doc1_PreImp_Param.P_HEAD_3, labelNumAff);

        String numAffilie = AFIDEUtil.createNumAffAndNumeroIdeForPrint(affEnCours.getAffilieNumero(),
                affEnCours.getNumeroIDE(), affEnCours.getIdeStatut());

        super.setParametres(Doc1_PreImp_Param.P_HEAD_4, numAffilie);
        super.setParametres(Doc1_PreImp_Param.P_HEAD_5,
                getSession().getApplication().getLabel("ENTETE_DECLARATIONPERIODE", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.P_HEAD_6, getPeriodeDecompte());
        super.setParametres(
                Doc1_PreImp_Param.P_ADRESSE,
                affEnCours.getTiers().getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA(),
                        affEnCours.getAffilieNumero()));
        super.setParametres(
                Doc1_PreImp_Param.P_NUMEROAFFILIE,
                getSession().getApplication().getLabel("ENVOI_NUM_AFFILIE", langueIsoTiers) + " "
                        + affEnCours.getAffilieNumero());
        super.setParametres(Doc1_PreImp_Param.P_NOMPRENOMAFFILIE, affEnCours.getTiers().getPrenomNom());
        super.setParametres(Doc1_PreImp_Param.P_PAS_PERSO,
                getSession().getApplication().getLabel("ENVOI_PAS_PERSONNEL", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.P_SI_CHANGE,
                getSession().getApplication().getLabel("ENVOI_SI_CHANGEMENT", langueIsoTiers));
    }

    private void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        bean.setAdresse(affEnCours.getTiers().getAdresseAsString(getDocumentInfo(),
                IConstantes.CS_AVOIR_ADRESSE_COURRIER, DSApplication.CS_DOMAINE_DECLARATION_SALAIRES,
                JACalendar.todayJJsMMsAAAA(), affEnCours.getAffilieNumero()));
        bean.setNoAffilie(affEnCours.getAffilieNumero());
        bean.setNoAvs(" ");
        bean.setEmailCollaborateur(" ");
        bean.setNomCollaborateur(getSession().getUserFullName());

        AFIDEUtil.addNumeroIDEInDoc(bean, affEnCours.getNumeroIDE(), affEnCours.getIdeStatut());

    }

    /**
     * Permet de setter l'en-tête des collonnes du tableau Commentaire relatif à la méthode _tableHeader.
     */
    protected void _tableHeader() throws Exception {
        // Paramètres à setter si on imprime une déclaration
        super.setParametres(Doc1_PreImp_Param.L_1,
                getSession().getApplication().getLabel("ENTETE_MEMBRESPERSONNEL", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_2,
                getSession().getApplication().getLabel("ENTETE_PERIODETRAVAIL", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_3,
                getSession().getApplication().getLabel("ENTETE_SALAIRE", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_4, getSession().getApplication().getLabel("ENTETE_AVS", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_5,
                getSession().getApplication().getLabel("ENTETE_NOMPRENOM", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_6,
                getSession().getApplication().getLabel("ENTETE_DEBUT", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_7, getSession().getApplication().getLabel("ENTETE_FIN", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_8,
                getSession().getApplication().getLabel("ENTETE_AVSAIAPG", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_9,
                getSession().getApplication().getLabel("ENTETE_ASSURANCECHOMAGE", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_17, getSession().getApplication().getLabel("PRE_ANNEE", langueIsoTiers));
        // Ici on sette les paramètres pour savoir si les collonnes doivent être
        // affichées ou non
        DSApplication application = null;
        application = (DSApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(
                DSApplication.DEFAULT_APPLICATION_DRACO);
        super.setParametres(Doc1_PreImp_Param.L_11, getSession().getApplication().getLabel("ENVOI_AF", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_12,
                getSession().getApplication().getLabel("ENVOI_AMAT_GE", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_13, getSession().getApplication()
                .getLabel("ENVOI_JOUR", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_14, getSession().getApplication()
                .getLabel("ENVOI_MOIS", langueIsoTiers));
        super.setParametres(Doc1_PreImp_Param.L_20, getSession().getApplication()
                .getLabel("ENTETE_ACM", langueIsoTiers));
        super.setParametres("L_18", getSession().getApplication().getLabel("REMARQUE", langueIsoTiers));
        super.setParametres("L_19", getSession().getApplication().getLabel("AC_II", langueIsoTiers));

        HashMap<String, String> map = new HashMap<String, String>();

        if (application.isCategoriePersonne()) {
            map.put("categorie_personne", "categorie_personne");
        }
        if (application.isCanton()) {
            map.put("canton", "canton");
        }
        if (map.size() == 2) {
            // si juste 2 colonnes, voir ce qui doit être affiché
            int nb = 1;
            if (map.containsKey("categorie_personne")) {
                super.setParametres(Doc1_PreImp_Param.P_TEXTE_CATEGORIE_PERSONNEL,
                        application.getTexteCategoriePersonnel(langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.P_2,
                        getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.L_10,
                        getSession().getApplication().getLabel("ENVOI_CAT_PERSON", langueIsoTiers));
                nb++;
            }
            if (map.containsKey("canton")) {
                if (nb > 1) {
                    super.setParametres(Doc1_PreImp_Param.P_1,
                            getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                    super.setParametres(Doc1_PreImp_Param.L_15,
                            getSession().getApplication().getLabel("ENVOI_CANTON", langueIsoTiers));
                }
            }

        } else if (map.size() == 1) {
            // si juste 1 colonne, voir laquelle et l'afficher
            if (map.containsKey("categorie_personne")) {
                super.setParametres(Doc1_PreImp_Param.P_TEXTE_CATEGORIE_PERSONNEL,
                        application.getTexteCategoriePersonnel(langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.P_1,
                        getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.L_15,
                        getSession().getApplication().getLabel("ENVOI_CAT_PERSON", langueIsoTiers));
            }
            if (map.containsKey("canton")) {
                super.setParametres(Doc1_PreImp_Param.P_1,
                        getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.L_15,
                        getSession().getApplication().getLabel("ENVOI_CANTON", langueIsoTiers));
            }

        } else {
            // Aucune catégorie, on ajoute rien.

        }
    }

    @Override
    public void afterBuildReport() {
        // TODO Auto-generated method stub
        setDocumentTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            setDocumentTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));
            setFileTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));

            DSApplication application = (DSApplication) getSession().getApplication();

            // Si le template est spécifié, on l'utilise
            if ((application.getModelDSSpecifique() != null) && !application.getModelDSSpecifique().isEmpty()) {
                MODEL_NAME = application.getModelDSSpecifique();
            } else {
                // Si non spécifié, on le déduit grâce au modele de préimpression
                if (DRACO_PREIMP_DECSAL_MASTER_1.equals(application.getModelDS())) {
                    MODEL_NAME = DRACO_REMP_DECSAL_MASTER_1;
                } else {
                    MODEL_NAME = DRACO_REMP_DECSAL_MASTER_2;
                }
            }

            // TODO : Test leve exception
            // TODO : Passer les properties en utilisant application.getModelDSSpecifique();

            setTemplateFile(MODEL_NAME);
        } catch (Exception e) {
            JadeLogger.error(this, "Unabled to set template for Impression Declaration " + e.getMessage());
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setFileTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));
        setDocumentTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));
        setSendCompletionMail(true);
        setSendMailOnError(true);

        // on test si l'utilisateur à les droits sur l'affiliation
        if (!getAffilie().hasRightAccesSecurity()) {
            getMemoryLog().logMessage(getSession().getLabel("DROIT_AFFILIE_INSUF"), FWMessage.ERREUR,
                    this.getClass().getName());
            abort();
        }
    }

    @Override
    public void createDataSource() throws Exception {
        setFileTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));
        setDocumentTitle(getSession().getLabel("MSG_IMPRESSION_DECL_TITRE"));
        affEnCours = getAffilie();
        if (JadeStringUtil.isBlankOrZero(idDeclaration)) {
            throw new Exception("Impossible d'imprimer la DS");
        }
        getDocumentInfo().setDocumentTypeNumber("0178CDS");
        getDocumentInfo().setDocumentProperty("annee", getDeclaration().getAnnee());
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affEnCours.getAffilieNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affEnCours.getAffilieNumero()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), affEnCours.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    affEnCours.getAffilieNumero(), affilieFormater.unformat(affEnCours.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affEnCours.getAffilieNumero());
        }
        Doc1_Remplissage mgr = new Doc1_Remplissage();
        mgr.setForIdDeclaration(idDeclaration);
        mgr.setSession(getSession());
        if (DSUtil.isNNSSActif(getSession(), JACalendar.todayJJsMMsAAAA())) {
            mgr.setDateProdNNSS(true);
        }
        try {
            super.setDataSource(mgr.getCollectionData());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, "Impression DS");
            abort();
            return;
        }

        langueIsoTiers = affEnCours.getTiers().getLangueIso();
        if ("fr".equalsIgnoreCase(langueIsoTiers)) {
            langueForCS = "F";
        } else if ("de".equalsIgnoreCase(langueIsoTiers)) {
            langueForCS = "D";
        } else if ("it".equalsIgnoreCase(langueIsoTiers)) {
            langueForCS = "I";
        }
        mgr.setLangueForCS(langueForCS);
        _headerText();
        _tableHeader();
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueIsoTiers);
        // if ("DRACO_PREIMP_DECSAL_MASTER_1".equals(this.MODEL_NAME)) {
        // this.MODEL_NAME = "DRACO_REMP_DECSAL_MASTER_1";
        // } else {
        // this.MODEL_NAME = "DRACO_REMP_DECSAL_MASTER_2";
        // }
        setTemplateFile(MODEL_NAME);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _setHeader(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        getImporter().getParametre().put(
                ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), "header.filename.declaration"));

    }

    private AFAffiliation getAffilie() {
        if (affEnCours == null) {
            affEnCours = getDeclaration().getAffiliation();
        }
        return affEnCours;

    }

    private DSDeclarationViewBean getDeclaration() {
        if (declaration == null) {
            try {
                declaration = new DSDeclarationViewBean();
                declaration.setSession(getSession());
                declaration.setIdDeclaration(getIdDeclaration());
                declaration.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return declaration;
    }

    @Override
    protected String getEMailObject() {
        if (getTransaction() != null) {
            if (!isOnError() && !getTransaction().hasErrors() && !isAborted()) {
                return getSession().getLabel("MAIL_OBJECT_IMPRESSION_DS") + " " + affEnCours.getAffilieNumero();
            } else {
                return getSession().getLabel("MAIL_OBJECT_ERR_IMPRESSION_DS") + " " + affEnCours.getAffilieNumero();
            }
        } else {
            return getSession().getLabel("MAIL_OBJECT_IMPRESSION_DS") + " " + affEnCours.getAffilieNumero();
        }
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    /**
     * Renvoie le code utilisateur correspondant à un code système.
     * 
     * @return le code utilisateur, év. <code>null</code>
     * @param idCode
     *            l'id du code système
     */
    private final String getLibelleLangue(String idCode) {
        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(getSession());
        userCode.setIdCodeSysteme(idCode);
        userCode.setIdLangue(langueForCS);
        try {
            userCode.retrieve();
            if (!userCode.isNew()) {
                return userCode.getLibelle();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Méthode qui retourne la période du décompte en fonction des années
     * 
     * @return String
     */
    private String getPeriodeDecompte() {
        try {
            if (JadeStringUtil.isBlankOrZero(idDeclaration)) {
                return "";
            }
            if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getDeclaration().getTypeDeclaration())
                    && JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                // on ragarde année mini et année maxi
                DSInscAnneeMaxMinManager mgrMax = new DSInscAnneeMaxMinManager();
                DSInscAnneeMaxMinEntity minMax = null;
                mgrMax.setForIdDeclaration(getDeclaration().getIdDeclaration());
                mgrMax.setSession(getSession());
                mgrMax.find();
                if (mgrMax.size() > 0) {
                    minMax = (DSInscAnneeMaxMinEntity) mgrMax.getFirstEntity();
                } else {
                    return "";
                }
                if (!JadeStringUtil.isBlankOrZero(minMax.getValeurMin())
                        && !JadeStringUtil.isBlankOrZero(minMax.getValeurMax())) {
                    return "01.01." + minMax.getValeurMin() + " - 31.12." + minMax.getValeurMax();
                }

            } else {
                return "01.01." + declaration.getAnnee() + " - 31.12." + declaration.getAnnee();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        // si First est à true on va au suivant sinon on s'arrête
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

}
