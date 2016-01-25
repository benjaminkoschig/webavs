// package globaz.corvus.itext;
//
// import globaz.babel.api.ICTDocument;
// import globaz.caisse.helper.CaisseHelperFactory;
// import globaz.caisse.report.helper.CaisseHeaderReportBean;
// import globaz.caisse.report.helper.ICaisseReportHelper;
// import globaz.corvus.api.codesystem.IRECatalogueTexte;
// import globaz.corvus.application.REApplication;
// import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjour;
// import globaz.corvus.process.REListerEcheancesProcess;
// import globaz.externe.IPRConstantesExternes;
// import globaz.framework.printing.itext.FWIDocumentManager;
// import globaz.framework.printing.itext.exception.FWIException;
// import globaz.framework.util.FWMessage;
// import globaz.globall.db.BProcess;
// import globaz.globall.db.BSession;
// import globaz.globall.db.GlobazJobQueue;
// import globaz.globall.util.JACalendar;
// import globaz.globall.util.JACalendarGregorian;
// import globaz.globall.util.JADate;
// import globaz.hera.api.ISFSituationFamiliale;
// import globaz.hera.db.famille.SFMembreFamilleJointPeriode;
// import globaz.hera.db.famille.SFMembreFamilleJointPeriodeManager;
// import globaz.jade.admin.user.bean.JadeUser;
// import globaz.jade.client.util.JadeStringUtil;
// import globaz.prestation.interfaces.babel.PRBabelHelper;
// import globaz.prestation.interfaces.tiers.PRTiersHelper;
// import globaz.prestation.interfaces.tiers.PRTiersWrapper;
// import globaz.prestation.interfaces.util.nss.PRUtil;
// import globaz.prestation.tools.PRDateFormater;
// import globaz.prestation.tools.PRStringUtils;
// import globaz.pyxis.api.ITITiers;
//
// import java.util.HashMap;
// import java.util.Hashtable;
// import java.util.Iterator;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Map;
// /**
// *
// * @author JJE
// */
// public class REEcheancesRente extends FWIDocumentManager {
//
// private static final String FICHIER_RESULTAT = "echeanceRente";
// private static final String FICHIER_MODELE = "RE_ECHEANCE_RENTE";
// private static final String CDT_PERS_REF = "{persRef}";
// private static final String CDT_TEL = "{tel}";
// private static final String CDT_NSS = "{nss}";
// private static final String CDT_BENEFICIAIRE = "{beneficiaire}";
// private static final String CDT_TITRE = "{titre}";
// private static final String CDT_DATENAISSANCE = "{dateNaissance}";
// private static final String CDT_DATEFINRENTE = "{dateFinRente}";
// //private static final String CDT_SIGNATURE = "{signature}";
// private static final String HEADER_NAME = "Header_AVS_AI_Prest.jasper";
//
//
// private ICTDocument document;
// private ICTDocument documentHelper;
// private CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
// private ICaisseReportHelper caisseHelper;
// private String codeIsoLangue = "fr";
// private String titre = "";
// private Iterator echeancesIterator = null;
// private List echeances = null;
// private RERenteAccJoinTblTiersJoinDemRenteJoinAjour echeanceCourrante = null;
// private PRTiersWrapper tiers;
// private String nomBeneficiaire ="";
// private String dateNaissanceBeneficiaire ="";
//
//
//
//
// public REEcheancesRente() {
// super();
// }
//
// public REEcheancesRente(BProcess parent) throws FWIException {
// super(parent, REApplication.APPLICATION_CORVUS_REP, FICHIER_RESULTAT);
// }
//
// public REEcheancesRente(BSession session) throws FWIException {
// super(session, REApplication.APPLICATION_CORVUS_REP, FICHIER_RESULTAT);
// }
//
//
// public REEcheancesRente(BProcess parent, String rootApplication, String fileName) throws FWIException {
// super(parent, rootApplication, fileName);
// }
//
// public REEcheancesRente(BSession session, String rootApplication, String fileName) throws FWIException {
// super(session, rootApplication, fileName);
// }
//
// public void beforeBuildReport() throws FWIException {
//
// try {
//
// Map parametres = getImporter().getParametre();
// if (parametres == null) {
// parametres = new HashMap();
// getImporter().setParametre(parametres);
// } else {
// parametres.clear();
// }
//
// //creation des paramètres pour l'en-tête
//
// //recherche du tiers
// tiers = PRTiersHelper.getTiersParId(getISession(), echeanceCourrante.getIdTiersBeneficiaire());
// if (tiers == null){
// tiers = PRTiersHelper.getAdministrationParId(getISession(), echeanceCourrante.getIdTiersBeneficiaire());
// }
//
// //recherche du titre
// ITITiers tiersTitre = (ITITiers)getSession().getAPIFor(ITITiers.class);
// Hashtable params = new Hashtable();
// params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
// ITITiers[] t = tiersTitre.findTiers(params);
// if(t!=null && t.length>0){
// tiersTitre= t[0];
// }
// titre = tiersTitre.getFormulePolitesse(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
//
// //Recherche des informations sur le bénéficiaire de la rente
// if (null != tiers){
// nomBeneficiaire= tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)+ " " +
// tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
// dateNaissanceBeneficiaire= tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
// }else{
// getMemoryLog().logMessage(getSession().getLabel("ERREUR_AUCUN_BENEFICIAIRE_TROUVE"), FWMessage.ERREUR,
// "REEcheancesRente");
// abort();
// }
//
// //recherche de la langue
// codeIsoLangue = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
// codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
//
// //creation du helper pour les entetes et pieds de page
// caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getSession().getApplication(), codeIsoLangue);
// caisseHelper.setHeaderName(HEADER_NAME);
//
// //recherche de l'adresse
// String adresse="";
// adresse = PRTiersHelper.getAdresseCourrierFormatee(
// getISession(),
// echeanceCourrante.getIdTiersBeneficiaire(),
// null,
// REApplication.CS_DOMAINE_ADRESSE_CORVUS);
// if (adresse.equals("")){
// getMemoryLog().logMessage("Aucune adresse trouvée",
// FWMessage.ERREUR,
// "REEcheanceRente");
// abort();
// }else{
// crBean.setAdresse(adresse);
// }
//
// //recherche de la date du jour
// String date = JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue);
// crBean.setDate(date);
//
// //ajoute la mention traîté par
//
// String texte_3 = "";
//
// if (!JadeStringUtil.isBlank(echeanceCourrante.getIdGestionnaire())){
// JadeUser userName = getSession().getApplication()._getSecurityManager().getUserForVisa(getSession(),
// echeanceCourrante.getIdGestionnaire());
// String user = userName.getFirstname () + " " + userName.getLastname();
//
// String traitePar = PRStringUtils.replaceString(document.getTextes(1).getTexte(1).getDescription(),
// CDT_PERS_REF,user);
// //crBean.setNomCollaborateur(traitePar);
// texte_3 += traitePar;
//
// String telephone = PRStringUtils.replaceString(document.getTextes(1).getTexte(2).getDescription(),
// CDT_TEL,userName.getPhone());
// //crBean.setTelCollaborateur(telephone);
// texte_3 += telephone;
// }
//
//
// if (!JadeStringUtil.isEmpty(echeanceCourrante.getNss())){
// String noAvis = PRStringUtils.replaceString(document.getTextes(1).getTexte(3).getDescription(),
// CDT_NSS,echeanceCourrante.getNss());
// texte_3 += noAvis;
// }
//
// if (!JadeStringUtil.isBlank(texte_3)){
//
// parametres.put("PARAM_ZONE_3",texte_3);
// }
//
// caisseHelper.addHeaderParameters(getImporter(), crBean);
//
// //Création du corps de la lettre
//
// //Le texte se modifie selon le motif de l'échéance
// String texte_1 = "";
//
// if (REListerEcheancesProcess.MOTIF_18_ANS.equals(echeanceCourrante.getMotif())){
// texte_1 = getTexteMotif18ans();
// }else{
// if (REListerEcheancesProcess.MOTIF_25_ANS.equals(echeanceCourrante.getMotif())){
// texte_1 = getTexteMotif25ans();
// }else{
// if (REListerEcheancesProcess.MOTIF_FIN_ETUDES.equals(echeanceCourrante.getMotif())){
// texte_1 = getTexteMotifFinEtudes();
// parametres.put("PARAM_ZONE_2",getTexteAnnexeFinEudes());
// }else{
// if (REListerEcheancesProcess.MOTIF_F_AGE_VIEILLESSE.equals(echeanceCourrante.getMotif()) ||
// REListerEcheancesProcess.MOTIF_H_AGE_VIEILLESSE.equals(echeanceCourrante.getMotif())){
// texte_1 = getTexteMotifEnAgeAVS();
// //TODO: Ajouter les annexes !!!!!!!
// }else{
//
// }
// }
// }
// }
//
// parametres.put("PARAM_ZONE_1",texte_1);
//
// //Création de la signature
// caisseHelper.addSignatureParameters(getImporter());
//
//
// } catch (Exception e) {
// e.printStackTrace();
// getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "REEcheancesRente");
// abort();
// }
//
// }
//
// public void createDataSource() throws Exception {
// //pas de détail
// LinkedList lignes = new LinkedList();
// lignes.add("");
// setDataSource(lignes);
// }
//
// public void beforeExecuteReport() throws FWIException {
// // efface les pdf à la fin
// setDeleteOnExit(true);
// // set le modèle
// setTemplateFile(FICHIER_MODELE);
//
// // initialisation du tableau contenant les infos des échéances à imprimer
// if (null != echeances){
// echeancesIterator = echeances.iterator();
// }
//
// try {
// // chargement du catalogue de texte
// if (documentHelper == null) {
// documentHelper = PRBabelHelper.getDocumentHelper(getISession());
// documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
// documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_ECHEANCE);
// documentHelper.setDefault(Boolean.TRUE);
// documentHelper.setActif(Boolean.TRUE);
// documentHelper.setCodeIsoLangue(codeIsoLangue);
// }
//
// documentHelper.setCodeIsoLangue(codeIsoLangue);
//
// ICTDocument[] documents = documentHelper.load();
//
// if ((documents == null) || (documents.length == 0)) {
// getMemoryLog().logMessage(getSession().getLabel("ERREUR_CHARGEMENT_CDT_RENTE"), FWMessage.ERREUR,
// "REEcheancesRente");
// abort();
// } else {
// document = documents[0];
// }
// } catch (Exception e) {
// e.printStackTrace();
// getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "REEcheancesRente");
// abort();
// }
// }
//
// public boolean next() throws FWIException {
// try {
// if (echeancesIterator.hasNext()){
// //On charge l'échéance courante
// echeanceCourrante = (RERenteAccJoinTblTiersJoinDemRenteJoinAjour) echeancesIterator.next();
// return true;
//
// } else {
// return false;
// }
//
// } catch (Exception e) {
// e.printStackTrace();
// getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "REEcheancesRente");
// abort();
//
// return false;
// }
// }
//
// public GlobazJobQueue jobQueue() {
// return GlobazJobQueue.READ_SHORT;
// }
//
// public List getEcheances() {
// return echeances;
// }
//
// public void setEcheances(List echeances) {
// this.echeances = echeances;
// }
//
// private String getTexteMotif18ans(){
// StringBuffer buffer = new StringBuffer();
//
// buffer.append(PRStringUtils.replaceString(document.getTextes(2).getTexte(1).getDescription(),CDT_BENEFICIAIRE,nomBeneficiaire));
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(1).getDescription(),CDT_TITRE,titre));
// String date18ansBeneficiaire="";
// try {
// JADate date18ansBeneficiaireJaD = new JADate(dateNaissanceBeneficiaire);
// JACalendarGregorian jaCalGre = new JACalendarGregorian();
// date18ansBeneficiaire = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(
// jaCalGre.addYears(date18ansBeneficiaireJaD,18).toStrAMJ());
// } catch (Exception e) {
// e.printStackTrace();
// getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "REEcheancesRente");
// abort();
// }
//
// String niv3_2 = PRStringUtils.replaceString(document.getTextes(3).getTexte(2).getDescription(),CDT_DATENAISSANCE,
// JACalendar.format(date18ansBeneficiaire, codeIsoLangue));
// niv3_2 = PRStringUtils.replaceString(niv3_2,CDT_DATEFINRENTE,
// dateFinEtude(echeanceCourrante));
// buffer.append(niv3_2);
// buffer.append(document.getTextes(3).getTexte(3).getDescription());
// buffer.append(document.getTextes(3).getTexte(4).getDescription());
// buffer.append(document.getTextes(3).getTexte(5).getDescription());
// buffer.append(document.getTextes(3).getTexte(6).getDescription());
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(7).getDescription(),CDT_TITRE,titre));
//
// return buffer.toString();
// }
//
// private String getTexteMotif25ans(){
// StringBuffer buffer = new StringBuffer();
//
// buffer.append(document.getTextes(2).getTexte(2).getDescription());
// buffer.append(document.getTextes(2).getTexte(3).getDescription());
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(1).getDescription(),CDT_TITRE,titre));
// buffer.append(document.getTextes(3).getTexte(8).getDescription());
// buffer.append(document.getTextes(3).getTexte(9).getDescription());
// buffer.append(document.getTextes(3).getTexte(10).getDescription());
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(11).getDescription(),CDT_TITRE,titre));
//
// return buffer.toString();
// }
//
// private String getTexteMotifFinEtudes(){
// StringBuffer buffer = new StringBuffer();
//
// buffer.append(PRStringUtils.replaceString(document.getTextes(2).getTexte(1).getDescription(),
// CDT_BENEFICIAIRE,nomBeneficiaire));
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(1).getDescription(),CDT_TITRE,titre));
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(12).getDescription(),CDT_DATEFINRENTE,
// dateFinEtude(echeanceCourrante)));
// buffer.append(document.getTextes(3).getTexte(13).getDescription());
// buffer.append(document.getTextes(3).getTexte(14).getDescription());
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(15).getDescription(),CDT_TITRE,titre));
//
// return buffer.toString();
// }
//
// private String getTexteAnnexeFinEudes(){
// StringBuffer buffer = new StringBuffer();
//
// buffer.append(document.getTextes(5).getTexte(1).getDescription());
// buffer.append(document.getTextes(5).getTexte(2).getDescription());
// buffer.append(document.getTextes(5).getTexte(3).getDescription());
// buffer.append(document.getTextes(5).getTexte(4).getDescription());
// buffer.append(document.getTextes(5).getTexte(5).getDescription());
// buffer.append(document.getTextes(5).getTexte(6).getDescription());
// buffer.append(document.getTextes(5).getTexte(7).getDescription());
//
// return buffer.toString();
// }
//
// private String getTexteMotifEnAgeAVS(){
// StringBuffer buffer = new StringBuffer();
//
// buffer.append(document.getTextes(2).getTexte(4).getDescription());
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(1).getDescription(),CDT_TITRE,titre));
// buffer.append(document.getTextes(3).getTexte(16).getDescription());
// buffer.append(document.getTextes(3).getTexte(17).getDescription());
// buffer.append(document.getTextes(3).getTexte(18).getDescription());
// buffer.append(document.getTextes(3).getTexte(19).getDescription());
// buffer.append(PRStringUtils.replaceString(document.getTextes(3).getTexte(20).getDescription(),CDT_TITRE,titre));
//
// return buffer.toString();
// }
//
// private String dateFinEtude(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity){
//
// try {
// //On commence par tester si la personne possède une ou pls périodes d'études dans le domaine des rentes
// SFMembreFamilleJointPeriodeManager sfMemFamJointPerMgr = new SFMembreFamilleJointPeriodeManager();
// sfMemFamJointPerMgr.setSession(getSession());
// sfMemFamJointPerMgr.setForTypePeriode(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE);
// sfMemFamJointPerMgr.setForCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
// sfMemFamJointPerMgr.setForIdMembreFamille(entity.getIdMembreFamille());
// sfMemFamJointPerMgr.find();
//
// //Si ce n'est pas le cas on test avec le domaine standard
// if (sfMemFamJointPerMgr.getSize() == 0){
// sfMemFamJointPerMgr.setForCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
// sfMemFamJointPerMgr.find();
// }
//
// String dateFinEtude="";
// boolean premiereIteration = true;
// for(Iterator sfMemFamJointPerIter = sfMemFamJointPerMgr.iterator();sfMemFamJointPerIter.hasNext();){
// SFMembreFamilleJointPeriode sfMemFamJoiPer = (SFMembreFamilleJointPeriode) sfMemFamJointPerIter.next();
//
// if (null != sfMemFamJoiPer){
//
// if (!JadeStringUtil.isBlank(sfMemFamJoiPer.getDateDebut())){
// if (premiereIteration){
// dateFinEtude=sfMemFamJoiPer.getDateFin();
// premiereIteration= false;
// }
// if (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(sfMemFamJoiPer.getDateFin())).intValue() >
// new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinEtude)).intValue()){
// dateFinEtude=sfMemFamJoiPer.getDateFin();
// }
// }
//
// }
// }
//
// String dateFinEtudeRetournee = JACalendar.format(dateFinEtude, codeIsoLangue);
//
// return dateFinEtudeRetournee.substring(dateFinEtudeRetournee.indexOf(" ")+1);
//
// } catch (Exception e) {
// getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess");
// abort();
// return "";
// }
// }
//
// public void afterBuildReport() {
//
// getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_ECHEANCES);
// }
//
// }