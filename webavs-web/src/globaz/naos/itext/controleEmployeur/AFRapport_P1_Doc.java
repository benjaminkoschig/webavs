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
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.naos.application.AFApplication;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.api.ITIRole;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFRapport_P1_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Numéro du document
    private static final String DOC_NO = "0186CAF";

    /**
     * @param session
     * @param nomDocument
     * @throws FWIException
     */
    public final static String TEMPLATE_FILENAME = "NAOS_RAPPORT_CE";

    public static String getTemplateFilename() {
        return AFRapport_P1_Doc.TEMPLATE_FILENAME;
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

    private boolean preRapport = false;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public AFRapport_P1_Doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    public AFRapport_P1_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("NAOS_CONTROLES_TITRE_P1"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public AFRapport_P1_Doc(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("NAOS_CONTROLES_TITRE_P1"));
    }

    public AFRapport_P1_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("NAOS_CONTROLES_TITRE_P1"));
        this.idControle = idControle;
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText(CaisseHeaderReportBean headerBean) {

        try {
            String dateImpression2 = JACalendar.todayJJsMMsAAAA();

            // Retiré car pas besoin de ses informations pour ce document
            // JadeUserService service =
            // JadeAdminServiceLocatorProvider.getLocator().getUserService();
            // JadeUser user = service.loadForVisa(getIdPersRef());

            headerBean.setDate(JACalendar.format(dateImpression2, controle.getLangueTiers()));

            // super.setParametres(
            // FAImpressionFacture_Param.getParamP(1),
            // getSession().getApplication().getLabel(
            // "FACDATE",
            // entity.getISOLangueTiers())
            // + " "
            // + dateImpression2);

            // adresse du tiers
            headerBean.setAdresse(adressePrincipale);
            // super.setParametres(FAImpressionFacture_Param.getParamP(2),
            // adressePrincipale);

            // //Ajout du nom et du num tèl de la personne de référence
            // JadeUser user;
            // JadeUserService service =
            // JadeAdminServiceLocatorProvider.getLocator().getUserService();
            // user = service.loadForVisa(getIdPersRef());
            // if(user != null){
            // headerBean.setNomCollaborateur(user.getFirstname()+" "+user.getLastname());
            // headerBean.setTelCollaborateur(user.getPhone());
            // }

            // numéro AVS
            headerBean.setNoAvs("");
            // headerBean.setNoAvs(getEntity().getNumeroAVSTiers(getTransaction()));
            // super.setParametres(
            // FAImpressionFacture_Param.getParamP(3),
            // getSession().getApplication().getLabel(
            // "FACAVS",
            // entity.getISOLangueTiers())
            // + ": "
            // + entity.getNumeroAVSTiers(getTransaction()));

            // No affilié
            headerBean.setNoAffilie(controle.getNumAffilie());
            headerBean.setConfidentiel(true);

            // Retiré car pas besoin de ses informations
            // headerBean.setNomCollaborateur(user.getFirstname() + " " +
            // user.getLastname());
            // headerBean.setTelCollaborateur(user.getPhone());
            // headerBean.setUser(user);

            // super.setParametres(
            // FAImpressionFacture_Param.getParamP(4),
            // getSession().getApplication().getLabel(
            // "FACNOAFF",
            // entity.getISOLangueTiers())
            // + ": "
            // + getEntity().getIdExterneRole());

        } catch (Exception e) {
            getMemoryLog().logMessage("Les paramêtres de l'objet peuvent ne pas avoir été mis correctement",
                    FWMessage.AVERTISSEMENT, headerBean.getClass().getName());
            ;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:33)
     */
    protected void _summaryText() throws Exception {

        try {
            String titreRapport = getSession().getApplication().getLabel("RAPPORT_TITRE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_TITRE, titreRapport);
            String titrePreRapport = "";
            if (isPreRapport()) {
                titrePreRapport = getSession().getApplication().getLabel("RAPPORT_PRERAPPORT",
                        controle.getLangueTiers());
            }
            super.setParametres(AFRapport_Param.L_PRERAPPORT, titrePreRapport);
            String caracteristique = getSession().getApplication().getLabel("RAPPORT_CARACTERISTIQUE",
                    controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_CARACTERISTIQUE, caracteristique);
            String document = getSession().getApplication().getLabel("RAPPORT_DOCUMENT", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_DOCUMENT, document);
            String numRapport = getSession().getApplication()
                    .getLabel("RAPPORT_NUM_RAPPORT", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_NUM_RAPPORT, numRapport);
            String nomReviseur = getSession().getApplication().getLabel("RAPPORT_NOM_REVISEUR",
                    controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_NOM_REVISEUR, nomReviseur);
            String dateControle = getSession().getApplication().getLabel("RAPPORT_DATE_CONTROLE",
                    controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_DATE_CONTROLE, dateControle);
            String periode = getSession().getApplication().getLabel("RAPPORT_PERIODE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_PERIODE, periode);
            String datePrecedent = getSession().getApplication().getLabel("RAPPORT_DATE_PRECEDENT",
                    controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_DATE_PRECEDENT, datePrecedent);
            String brancheEco = getSession().getApplication()
                    .getLabel("RAPPORT_BRANCHE_ECO", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_BRANCHE_ECO, brancheEco);
            String formeJuri = getSession().getApplication().getLabel("RAPPORT_FORME_JURI", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_FORME_JURI, formeJuri);
            String succursale = getSession().getApplication().getLabel("RAPPORT_SUCCURSALE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_SUCCURSALE, succursale);
            String inscriRc = getSession().getApplication().getLabel("RAPPORT_INSCRI_RC", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_INSCRI_RC, inscriRc);
            String nbSalarie = getSession().getApplication().getLabel("RAPPORT_NB_SALARIE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_NB_SALARIE, nbSalarie);
            String dateBoucle = getSession().getApplication()
                    .getLabel("RAPPORT_DATE_BOUCLE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_DATE_BOUCLEMENT, dateBoucle);
            String nomFidu = getSession().getApplication().getLabel("RAPPORT_NOM_FIDU", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_NOM_FIDU, nomFidu);
            String contact = getSession().getApplication().getLabel("RAPPORT_CONTACT", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_CONTACT, contact);
            String affLaa = getSession().getApplication().getLabel("RAPPORT_AFF_LAA", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_AFF_LAA, affLaa);
            String affLpp = getSession().getApplication().getLabel("RAPPORT_AFF_LPP", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_AFF_LPP, affLpp);
            String complet = getSession().getApplication().getLabel("RAPPORT_COMPLET", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_COMPLET, complet);
            String sondage = getSession().getApplication().getLabel("RAPPORT_SONDAGE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_SONDAGE, sondage);
            String compta = getSession().getApplication().getLabel("RAPPORT_COMPTA", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_COMPTA, compta);
            String bilan = getSession().getApplication().getLabel("RAPPORT_BILAN", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_BILAN, bilan);
            String pourboire = getSession().getApplication().getLabel("RAPPORT_POURBOIRE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_POURBOIRE, pourboire);
            String nature = getSession().getApplication().getLabel("RAPPORT_NATURE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_NATURE, nature);
            String honoraire = getSession().getApplication().getLabel("RAPPORT_HONORAIRE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_HONORAIRE, honoraire);
            String indemnite = getSession().getApplication().getLabel("RAPPORT_INDEMNITE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_INDEMNITE, indemnite);
            String mensuel = getSession().getApplication().getLabel("RAPPORT_MENSUEL", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_MENSUEL, mensuel);
            String piece = getSession().getApplication().getLabel("RAPPORT_PIECE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_PIECE, piece);
            String commission = getSession().getApplication().getLabel("RAPPORT_COMMISSION", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_COMMISSION, commission);
            String heure = getSession().getApplication().getLabel("RAPPORT_HEURE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_HEURE, heure);
            String domicile = getSession().getApplication().getLabel("RAPPORT_DOMICILE", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_DOMICILE, domicile);
            String gratification = getSession().getApplication().getLabel("RAPPORT_GRATIFICATION",
                    controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_GRATIFICATION, gratification);
            super.setParametres(AFRapport_Param.L_LIBELLE_1, controle.getEleLibelleAutre1());
            super.setParametres(AFRapport_Param.L_LIBELLE_2, controle.getEleLibelleAutre2());
            String element = getSession().getApplication().getLabel("RAPPORT_ELEMENT", controle.getLangueTiers());
            super.setParametres(AFRapport_Param.L_ELEMENT, element);
        } catch (Exception e) {
            this._addError(null, e.getMessage());
        }

        super.setParametres(AFRapport_Param.P_NUM_RAPPORT, controle.getRapportNumero());
        super.setParametres(AFRapport_Param.P_NOM_REVISEUR, controle.getControleurNom());
        super.setParametres(AFRapport_Param.P_DATE_CONTROLE, controle.getDateEffective());
        super.setParametres(AFRapport_Param.P_PERIODE,
                controle.getDateDebutControle() + " au " + controle.getDateFinControle());
        super.setParametres(AFRapport_Param.P_DATE_PRECEDENT, controle.getDatePrecedente());
        super.setParametres(AFRapport_Param.P_BRANCHE_ECO,
                CodeSystem.getLibelleIso(getSession(), controle.getBrancheEco(), controle.getLangueTiers()));
        super.setParametres(AFRapport_Param.P_FORME_JURI,
                CodeSystem.getLibelleIso(getSession(), controle.getFormeJuri(), controle.getLangueTiers()));
        super.setParametres(AFRapport_Param.P_SUCCURSALE, controle.getSuccLibelle());
        super.setParametres(AFRapport_Param.P_INSCRI_RC, controle.getInscriLibelle());
        super.setParametres(AFRapport_Param.P_NB_SALARIE, controle.getNombreSalariesFixes());
        super.setParametres(AFRapport_Param.P_DATE_BOUCLE, controle.getDateBouclement());
        super.setParametres(AFRapport_Param.P_NOM_FIDU, controle.getComptaTenuPar());
        super.setParametres(AFRapport_Param.P_CONTACT_1, controle.getPersonneContact1());
        super.setParametres(AFRapport_Param.P_CONTACT_2, controle.getPersonneContact2());
        super.setParametres(AFRapport_Param.P_CONTACT_3, controle.getPersonneContact3());
        super.setParametres(AFRapport_Param.P_AFF_LAA, controle.getAffiliationLaa());
        super.setParametres(AFRapport_Param.P_AFF_LPP, controle.getAffiliationLpp());
        super.setParametres(AFRapport_Param.P_COMPTA_COM, controle.getDocComptaComplet());
        super.setParametres(AFRapport_Param.P_COMPTA_SON, controle.getDocComptaSondage());
        super.setParametres(AFRapport_Param.P_BILAN_COM, controle.getDocBilanComplet());
        super.setParametres(AFRapport_Param.P_BILAN_SON, controle.getDocBilanSondage());
        if (controle.getElePourboire().booleanValue()) {
            super.setParametres(AFRapport_Param.P_POURBOIRE, "X");
        }
        if (controle.getEleNature().booleanValue()) {
            super.setParametres(AFRapport_Param.P_NATURE, "X");
        }
        if (controle.getEleHonoraire().booleanValue()) {
            super.setParametres(AFRapport_Param.P_HONORAIRE, "X");
        }
        if (controle.getEleIndemnite().booleanValue()) {
            super.setParametres(AFRapport_Param.P_INDEMNITE, "X");
        }
        if (controle.getEleMensuel().booleanValue()) {
            super.setParametres(AFRapport_Param.P_MENSUEL, "X");
        }
        if (controle.getElePiece().booleanValue()) {
            super.setParametres(AFRapport_Param.P_PIECE, "X");
        }
        if (controle.getEleCommission().booleanValue()) {
            super.setParametres(AFRapport_Param.P_COMMISSION, "X");
        }
        if (controle.getEleAutre1().booleanValue()) {
            super.setParametres(AFRapport_Param.P_LIBELLE_1, "X");
        }
        if (controle.getEleAutre2().booleanValue()) {
            super.setParametres(AFRapport_Param.P_LIBELLE_2, "X");
        }
        if (controle.getEleHeure().booleanValue()) {
            super.setParametres(AFRapport_Param.P_HEURE, "X");
        }
        if (controle.getEleDomicile().booleanValue()) {
            super.setParametres(AFRapport_Param.P_DOMICILE, "X");
        }
        if (controle.getEleGratification().booleanValue()) {
            super.setParametres(AFRapport_Param.P_GRATIFICATION, "X");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        super.setDocumentTitle(controle.getNumAffilie() + " -1- " + controle.getNomTiers());
    }

    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTemplateFile(AFRapport_P1_Doc.TEMPLATE_FILENAME);
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
        // super.setTemplateFile(getTemplateFilename(getEntity()));
        // setTemplateFile(TEMPLATE_FILENAME);
        manager.setSession(getSession());
        manager.setForControleEmployeurId(idControle);
        manager.find();
        if (manager.size() > 0) {
            controle = (AFControleEmployeur) manager.getFirstEntity();
        }

        fillDocInfo();

        // initialiser les variables d'aide
        try {
            adressePrincipale = controle.getAdressePrincipale(getTransaction(), controle.getDatePrevue());
            // adresseDomicile = entity.getAdresseDomicile(getTransaction(),
            // getPassage().getDateFacturation());
            // adressePaiement = controle.getAdressePaiement(getTransaction(),
            // controle.getDatePrevue());

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de retrouver l'adress principale, " + "du domicile pour : " + "ID="
                            + controle.getNumAffilie(), FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        _headerText(headerBean);
        _summaryText();

        caisseReportHelper.addHeaderParameters(this, headerBean);

    }

    private void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber(AFRapport_P1_Doc.DOC_NO);
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

    public boolean isPreRapport() {
        return preRapport;
    }

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

    public void setPreRapport(boolean preRapport) {
        this.preRapport = preRapport;
    }

}
