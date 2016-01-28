// /**
// *
// */
// package globaz.corvus.api.arc.downloader;
//
// import globaz.corvus.api.ci.IRERassemblementCI;
// import globaz.corvus.api.external.arc.REDownloaderException;
// import globaz.corvus.db.ci.RECompteIndividuel;
// import globaz.corvus.db.ci.RECompteIndividuelManager;
// import globaz.corvus.db.ci.REInscriptionCI;
// import globaz.corvus.db.ci.RERassemblementCI;
// import globaz.corvus.db.ci.RERassemblementCIManager;
// import globaz.corvus.vb.annonces.REAnnonceInscriptionsCIViewBean;
// import globaz.globall.api.BISession;
// import globaz.globall.api.BITransaction;
// import globaz.globall.db.BSession;
// import globaz.globall.db.BTransaction;
// import globaz.globall.util.JACalendar;
// import globaz.hermes.api.IHEOutputAnnonce;
// import globaz.hermes.api.helper.IHEAnnoncesCentraleHelper;
// import globaz.hermes.application.HEApplication;
// import globaz.prestation.interfaces.tiers.PRTiersHelper;
// import globaz.prestation.interfaces.tiers.PRTiersWrapper;
// import globaz.prestation.tools.PRDateFormater;
// import globaz.prestation.tools.PRSession;
//
// /**
// * @author SCR
// *
// */
//
// public class REDownloaderInscriptionsCI_BSC extends REAbstractDownloader {
//
//
// //~ Static fields/initializers -------------------------------------------------------------------------------------
//
// public static final Integer MAX_NB_DEMANDES = new Integer(100);
//
// // pour differencier le traitement des CI et des CI-ADD
// private static final int STATE_TRAITEMENT_CI = 1000;
// private static final int STATE_TRAITEMENT_CI_ADD = 1010;
//
//
// //~ Methods --------------------------------------------------------------------------------------------------------
//
//
// /* (non-Javadoc)
// * @see globaz.corvus.api.external.arc.IREUploader#upload(globaz.globall.api.BITransaction,
// globaz.corvus.api.external.arc.rassemblement.IArcVO[])
// */
// public void upload(BTransaction transaction) throws REDownloaderException {
//
// BISession remoteSession = null;
// BITransaction remoteTransaction = null;
//
// try {
// assertBeforeExecute();
//
// remoteSession = PRSession.connectSession(getSession(), HEApplication.DEFAULT_APPLICATION_HERMES);
// IHEAnnoncesCentraleHelper aCHelper = new IHEAnnoncesCentraleHelper();
//
// IHEOutputAnnonce[] annonces = null;
//
// // traitement par lot des annonces
// do {
//
// // Une nouvelle transaction HERMES pour chaque lot
// remoteTransaction = ((BSession)remoteSession).newTransaction();
// if(!remoteTransaction.isOpened()){
// remoteTransaction.openTransaction();
// }
//
// // Recuperation des annonces HERMES
// annonces =aCHelper.getAnnoncesTerminees((BTransaction)remoteTransaction, MAX_NB_DEMANDES);
//
// // traitement et insertion des annonces HERMES dans CORVUS
// if(!remoteTransaction.hasErrors()){
// doTraitement(transaction, annonces);
// }
//
// // validation du lot
// if(!remoteTransaction.hasErrors() && !transaction.hasErrors()){
// remoteTransaction.commit();
// transaction.commit();
// remoteTransaction.closeTransaction();
// }
//
// } while (annonces != null &&
// annonces.length != 0 &&
// !remoteTransaction.hasErrors());
//
// } catch (Exception e) {
//
// try {
// transaction.rollback();
// remoteTransaction.rollback();
// } catch (Exception e2) {
// // on forward l'Exception source
// }
//
// throw new REDownloaderException("Exception occurs in : " + this.getClass().getName(), e);
// }
// }
//
// /**
// * Recuperation des annonces HERMES
// *
// * ------------------------------------------------------------------------------
// *
// * Structure des envois de Hermes:
// * ===============================
// *
// * -- Code app. 11 <-- la ref. ARC est ici
// * ---- Code app. 38
// * ---- Code app. 38
// * ---- Code app. 38
// * -- Code app. 39
// * ---- Code app. 38
// * ---- Code app. 38
// * -- Code app. 39
// * ---- Code app. 38 |
// * -- Code app. 39 |<-- CI-ADD => pas de ref. ARC
// * -- Code app. 11 <-- la ref. ARC est ici
// * ---- Code app. 38
// * -- Code app. 39
// *
// * Pour simplifier le traitement, la structure est parcourue de bas en haut.
// *
// * ------------------------------------------------------------------------------
// *
// * Traitement:
// * ===========
// *
// * - si CI
// * - si un RCI correspondant existe (on trouve un RCI avec la ref. ARC)
// * - mise a jour du RCI
// * - ajout inscription
// * - ajout annonce
// *
// * - si inexistant
// * - on ne fait rein (normalement HERMES doit filtrer ces cas)
// *
// * - si CI-ADD
// * - si on trouve une correspondance dans les tiers au NSS
// * - si un RCI correspondant existe (memes motif et date de cloture)
// * - On creation d'un RCI enfant (un nouveau pour chaque CI-ADD ( pour chaque annonce 39))
// * - ajout inscription
// * - ajout annonce
// * - si il n'y a pas de RCI
// * - creation d'un RCI (avec motif et date de cloture de l'annonce 39)
// * - creation d'un CI si necessaire
// * - On creation d'un RCI enfant
// * - ajout inscription
// * - ajout annonce
// *
// * - si on ne trouve rien dans les tiers pour ce NSS
// * - on ne fait rien (ces cas seront recupere avec un autre process)
// *
// * ------------------------------------------------------------------------------
// *
// * Recapitulatif:
// * ==============
// *
// */
// public void doTraitement (BITransaction transaction, IHEOutputAnnonce[] annonces) throws REDownloaderException {
// try {
// int state = STATE_TRAITEMENT_CI_ADD;
//
// // On parcourt les annonces de Hermes dans le sens inverse pour avoir les 39 avant les 38
// // le deplacement du curseur (i) se fait dans les blocs de traitement
// for (int i = annonces.length-1; i >=0 ; ) {
//
// IHEOutputAnnonce annonce = annonces[i];
// state = getState(annonce);
//
// //////////////////////////////////////////////////////////////////////////////////////////////////////
// // traitement des CI add
// //////////////////////////////////////////////////////////////////////////////////////////////////////
// if(state == STATE_TRAITEMENT_CI_ADD){
//
// // on cherche l'assure dans Pyxis
// String nssAssureAyantDroit = annonce.get(IHEOutputAnnonce.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE);
// PRTiersWrapper assureAyantDroit = PRTiersHelper.getTiers(getSession(), nssAssureAyantDroit);
//
// // le tiers existe dans Pyxis
// if(assureAyantDroit != null){
//
// // Recherche du dernier RCI de l'assure pour le meme motif et la meme date de cloture
// RECompteIndividuel ci = null;
// RERassemblementCI rci = null;
// RERassemblementCIManager rciMgr = new RERassemblementCIManager();
// rciMgr.setSession((BSession)getSession());
// rciMgr.setForIdTiers(assureAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
// rciMgr.setForMotif(annonce.get(IHEOutputAnnonce.MOTIF_ANNONCE));
// rciMgr.setForDateClotureAAAAMM(PRDateFormater.convertDate_MMAA_to_AAAAMM(annonce.get(IHEOutputAnnonce.DATE_CLOTURE_MMAA)));
// rciMgr.setOrderBy(RERassemblementCI.FIELDNAME_ID_RCI + "DESC");
// rciMgr.find(transaction, 1);
//
// if(rciMgr.getSize()>0){
//
// // le dernier rassemblement CI (pour memes motif et date de cloture)
// rci = (RERassemblementCI)rciMgr.getFirstEntity();
//
// // on cherche le CI
// ci = new RECompteIndividuel();
// ci.setSession((BSession)getSession());
// ci.setIdCi(rci.getIdCI());
// ci.retrieve();
//
// }else{
//
// // si pas de RCI, creation d'un nouvel RCI qui va etre le parent du RCI du CI-add
// rci = new RERassemblementCI();
// rci.setSession((BSession)getSession());
//
// // on cherche le CI de l'assure
// RECompteIndividuelManager ciMgr = new RECompteIndividuelManager();
// ciMgr.setSession((BSession)getSession());
// ciMgr.setForIdTiers(assureAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
// ciMgr.find(transaction);
//
// if(rciMgr.getSize()>0){
// ci = (RECompteIndividuel)ciMgr.getFirstEntity();
// }else{
// // Si pas de CI, creation d'un CI pour l'assure
// ci = new RECompteIndividuel();
// ci.setSession((BSession)getSession());
// ci.setIdTiers(assureAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
// ci.add(transaction);
// }
//
// // on met a jours du RCI
// rci.setIdCI(ci.getIdCi());
// rci.setCsEtat(IRERassemblementCI.CS_ETAT_RASSEMBLE);
// rci.setMotif(annonce.get(IHEOutputAnnonce.MOTIF_ANNONCE));
// rci.setDateCloture(PRDateFormater.convertDate_MMAA_to_AAAAMM(annonce.get(IHEOutputAnnonce.DATE_CLOTURE_MMAA)));
// rci.setIdTiersAyantDroit(assureAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
// rci.add(transaction);
// }
//
// // creation d'un RCI enfant pour le CI-add
// RERassemblementCI rciPourCiAdd = null;
// rciPourCiAdd.setSession((BSession)getSession());
// rciPourCiAdd.setIdParent(rci.getIdRCI());
// rciPourCiAdd.setCsEtat(IRERassemblementCI.CS_ETAT_ENCOURS);
// rciPourCiAdd.setMotif(rci.getMotif());
// rciPourCiAdd.setDateCloture(rci.getDateCloture());
// rciPourCiAdd.setIdTiersAyantDroit(rci.getIdTiersAyantDroit());
//
// // on met a jours du RCI enfant
// rciPourCiAdd.setIdCI(ci.getIdCi());
// rciPourCiAdd.setCsEtat(IRERassemblementCI.CS_ETAT_RASSEMBLE);
// rciPourCiAdd.setDateRassemblement(JACalendar.todayJJsMMsAAAA());
// rciPourCiAdd.add(transaction);
//
// // pour toutes les annonces 38 de l'annonce 39
// i--;
// while(i>=0 && !"39".equals(annonces[i].get(IHEOutputAnnonce.CODE_APPLICATION))){
//
// //creation de l'annonce
// String idAnnonce = createAnnonce38(transaction, annonces[i]);
//
// // creation de l'inscription
// REInscriptionCI inscription = new REInscriptionCI();
// inscription.setSession((BSession)getSession());
// inscription.setIdArc(idAnnonce);
// inscription.setIdRCI(rciPourCiAdd.getIdRCI());
// inscription.add(transaction);
//
// i--;
// }
//
// }else{
// // On saute tout le bloc des annonces 38 (on cherche la prochaine annonce 39)
// // pas de traitement pour les tiers qui ne sont pas dans Pyxis
// i--;
// while(i>=0 && !"39".equals(annonces[i-1].get(IHEOutputAnnonce.CODE_APPLICATION))){
// i--;
// }
// }
//
// //////////////////////////////////////////////////////////////////////////////////////////////////////
// // traitement des CI
// //////////////////////////////////////////////////////////////////////////////////////////////////////
// }else{
//
// // on cherche l'annonce code application 11 pour trouver la ref. ARC
// String refARC = null;
// int j = i-1;
// while (j>=0 && !"11".equals(annonces[j].get(IHEOutputAnnonce.CODE_APPLICATION))) {
// j--;
// }
// if("11".equals(annonces[j].get(IHEOutputAnnonce.CODE_APPLICATION))){
// refARC = annonces[j].get(IHEOutputAnnonce.NUMERO_ANNONCE);
// }else{
// throw new REDownloaderException("Rassemblement CI sans annonce code application 11 pour: " +
// annonce.get(IHEOutputAnnonce.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE));
// }
//
// // on cherche le dernier RCI avec la ref. ARC
// RERassemblementCI rci = null;
// RERassemblementCIManager rciMgr = new RERassemblementCIManager();
// rciMgr.setSession((BSession)getSession());
// rciMgr.setForRefARC(refARC);
// rciMgr.setOrderBy(RERassemblementCI.FIELDNAME_ID_RCI + "DESC");
// rciMgr.find(transaction, 1);
//
// if(rciMgr.getSize()>0){
//
// rci = (RERassemblementCI)rciMgr.getFirstEntity();
//
// // on check que le rassemblement soit toujours en attente
// if(!IRERassemblementCI.CS_ETAT_ENCOURS.equals(rci.getCsEtat())){
//
// // creation d'un autre RCI identique avec la ref. ARC, mais dans l'etat "en attente"
// // creation d'un RCI enfant pour le CI-add
// RERassemblementCI newRci = new RERassemblementCI();
// newRci.setSession((BSession)getSession());
// newRci.setCsEtat(IRERassemblementCI.CS_ETAT_ENCOURS);
// newRci.setMotif(rci.getMotif());
// newRci.setDateCloture(rci.getDateCloture());
// newRci.setIdTiersAyantDroit(rci.getIdTiersAyantDroit());
// newRci.add(transaction);
//
// rci = newRci;
// }
//
// // pour tous les enregistrements entre la premiere annonce 39 et l'annonce 11
// for (int k = i; k > j; k--) {
//
// if("39".equals(annonces[k].get(IHEOutputAnnonce.CODE_APPLICATION))){
//
// // on ne traite pas lea annonces 39
// continue;
//
// }else{
// //creation de l'annonce
// String idAnnonce = createAnnonce38(transaction, annonces[k]);
//
// // creation de l'inscription
// REInscriptionCI inscription = new REInscriptionCI();
// inscription.setSession((BSession)getSession());
// inscription.setIdArc(idAnnonce);
// inscription.setIdRCI(rci.getIdRCI());
// inscription.add(transaction);
// }
// }
//
// // on passe au prochain bloc
// i = j-1;
//
// }else{
// // si pas de RCI avec la ref. ARC on ne fait rien
// // on passe au prochain bloc
// i = j-1;
// }
// }
// }
//
// } catch (Exception e) {
// throw new REDownloaderException("Exception occurs in : " + this.getClass().getName(), e);
// }
// }
//
// /**
// * Retourne l'etat de traitement en fonction de l'annonce
// * @param annonce
// * @return
// */
// private int getState(IHEOutputAnnonce annonce) throws REDownloaderException {
//
// String codeApplication;
// try {
// codeApplication = annonce.get(IHEOutputAnnonce.CODE_APPLICATION);
//
// if("39".equals(codeApplication)){
// if("1".equals(annonce.get(IHEOutputAnnonce.CI_ADDITIONNEL))){
// return STATE_TRAITEMENT_CI_ADD;
// }else{
// return STATE_TRAITEMENT_CI;
// }
// }else{
// throw new
// REDownloaderException("Un changement de l'�tat de traitement ne peut se faire que lors de la lecture d'une annonce code application 39");
// }
// } catch (Exception e) {
// throw new REDownloaderException("Exception occurs in : " + this.getClass().getName(), e);
// }
//
// }
//
//
// /**
// * Cr�� l'annonce 38.
// *
// * -------------------------------------------------------------------
// * Enregistrement d'inscription 1 (partie CI)
// * -----+----------+-------------------------------------+------------
// * champ| position | Contenu | Observation
// * -----+----------+-------------------------------------+------------
// * 1 | 1- 2 | Code application: 38 |
// * 2 | 3- 5 | Code enregistrement: 001-999 |6
// * 3 | 6- 8 | Num�ro de la Caisse commettante |3
// * 4 | 9- 11 | Num�ro de l'agence commettante |3
// * 5 | 12- 31 | R�f�rence interne de la Caisse |
// * | | com-mettante |3
// * 6 | 32- 42 | Num�ro d'assur� de l'ayant droit ou |
// * | | du partenaire en cas d'ordre |
// * | | de splitting |3, 12
// * 7 | 43- 44 | Motif de l'annonce |3
// * 8 | 45- 48 | Date de cl�ture: MMAA |2, 3
// * 9 | 49- 54 | Date de l'ordre: JJMMAA |3
// * 10 | 55- 57 | Num�ro de la Caisse tenant le CI |1
// * 11 | 58- 60 | Num�ro de l'agence tenant le CI |1
// * 12 | 61- 71 | Num�ro d'assur� |3
// * 13 | 72 | Code 1 (= partie CI) |11
// * 14 | 73- 79 | R�serve: z�ros |
// * 15 | 80- 90 | Num�ro d'affili� |5, 14
// * 16 | 91 | Chiffre-cl� d�signant les extournes |1
// * 17 | 92 | Chiffre-cl� du genre de cotisations |
// * 18 | 93 | Chiffre-cl� particulier |2, 8
// * 19 | 94- 95 | Part aux bonifications d'assistance |1, 2, 9
// * 20 | 96- 97 | Code sp�cial |13
// * 21 | 98- 99 | Dur�e de cotisations (d�but) |1
// * 22 | 100-101 | Dur�e de cotisations (fin) |1
// * 23 | 102-103 | Ann�e de cotisations |10
// * 24 | 104-112 | Revenu |1, 4
// * 25 | 113 | R�serve: z�ro |
// * 26 | 114 | Code A: amortissement |2
// * | | Code D: exemption de cotisations |2
// * | | Code S: sursis au paiement des |
// * | | coti-sations (seulement CSC)|2
// * 27 | 115-116 | Branche �conomique |7
// * 28 | 117-120 | Ann�e de cotisations (AAAA) |
// * -----+----------+-------------------------------------+------------
// *
// * Les champs inutilis�s sont � blanc.
// * 1 = Cadr� � droite, les positions inoccup�es sont compl�t�es par des z�ros.
// * 2 = Si le champ n'est pas utilis�, la position est � blanc.
// * 3 = Selon les donn�es de l'Ordre de cl�ture et de transmission du CI.
// * 4 = Sans signe pr�alable.
// * 5 = Les inscriptions des ann�es 1948 � 1968 comportent
// * obliga-toirement le chiffre de la branche �conomique
// * aux deux der-ni�res positions (doc. 318.104.01 Appendice IX).
// * 6 = Pour les CI comportant plus de 999 inscriptions la s�quence
// * d'enregistrement est � recommencer � partir de 001.
// * 7 = La branche �conomique figure aussi, obligatoirement, dans
// * le champ de rechange 27.
// * 8 = Splitting en cas de divorce
// * 1: revenus partag�s issus des ann�es de jeunesse
// * 2: revenus partag�s qui sont ins�r�s dans une lacune de
// * cotisations d'un conjoint s'�tendant sur l'ann�e enti�re, la-cune
// * qui peut �tre combl�e en tenant compte d'une ann�e de jeunesse
// * 3: revenus partag�s qui sont ins�r�s dans une lacune de cotisations
// * d'un conjoint s'�tendant sur l'ann�e enti�re, la-cune qui peut
// * �tre combl�e en tenant compte d'une ann�e d'appoint
// * 4: revenu annuel moyen d�terminant partag� pour les ann�es civiles
// * o� l'un des conjoints �tait b�n�ficiaire d'une rente d'invalidit�
// * 5: revenus partag�s d�j� pris en compte pour une rente
// * 9 = Part aux bonifications d'assistance
// * par ex.:
// * 01 = bonification d'assistance enti�re
// * 02 = demi-bonification d'assistance
// * 03 = tiers de bonification d'assistance
// * 10 = Les deux derni�res positions.
// * 11 = A la partie CI (enregistrement d'inscription 1) succ�de la partie
// * Information selon l'enregistrement d'inscription 2. Si cette
// * derni�re partie n'est pas utilis�e, l'enregistrement
// * d'ins-cription 2 est sans objet.
// * 12 = En cas de splitting: num�ro d'assur� du partenaire (selon champ 3
// * de l'enre-gistrement fixe 03 ou du champ 27 de l'enregistrement
// * va-riable relatif � l'application 22).
// * 13 = Code sp�cial (si inutilis�, compl�ter par des z�ros).
// * 01 = Cotisation minimale acquitt�e par la collectivit� pu-blique.
// * 02 = Revenus non formateurs de rente des personnes de condition
// * ind�pendante et des salari�s pour qui l'em-ployeur n'est
// * pas tenu de payer des cotisations.
// * 03 = Revenus non formateurs de rente des salari�s.
// * 14 = Cadr� � gauche, les positions inoccup�es sont � blanc.
// * -------------------------------------------------------------------
// *
// * -------------------------------------------------------------------
// * Enregistrement d'inscription 2 (partie Information)
// * -----+----------+-------------------------------------+------------
// * champ| position | Contenu | Observation
// * -----+----------+-------------------------------------+------------
// * 14 | 73-114 | Partie Information |6
// * 15 | 115-120 | Num�ro postal de l'employeur |2, 7
// * -----+----------+-------------------------------------+------------
// *
// * Les champs inutilis�s sont � blanc.
// * 2 = Card� � gauche, les positions inoccup�es sont � blanc.
// * 6 = Nom et, le cas �ch�ant, lieu de l'employeur.
// * 7 = Facultatif. Si le num�ro postal est indiqu�, le lieu correspondant
// *
// *
// * -------------------------------------------------------------------
// *
// * @param annonce
// * @return id de l'annonce cr��e
// * @throws Exception
// */
// private String createAnnonce38(BITransaction transaction, IHEOutputAnnonce annonce) throws Exception {
//
// REAnnonceInscriptionsCIViewBean vb = new REAnnonceInscriptionsCIViewBean();
//
//
// ////////////////////////////////////////////////////////////////////////////
// // RECUPERATION DES VALEUR DE L'ANNONCE RETOURNEE PAR HERMES
// ////////////////////////////////////////////////////////////////////////////
//
// // 1 | Code application: 38
// vb.setCodeApplication(annonce.get(IHEOutputAnnonce.CODE_APPLICATION));
//
// // 2 | Code enregistrement: 001-999
// vb.setCodeEnregistrement01(annonce.get(IHEOutputAnnonce.CODE_ENREGISTREMENT));
//
// // 3 | Num�ro de la Caisse commettante
// vb.setNumeroCaisse(annonce.get(IHEOutputAnnonce.NUMERO_CAISSE_COMMETTANTE));
//
// // 4 | Num�ro de l'agence commettante
// vb.setNumeroAgence(annonce.get(IHEOutputAnnonce.NUMERO_AGENCE_COMMETTANTE));
//
// // 5 | R�f�rence interne de la Caisse com-mettante
// vb.setRefInterneCaisse(annonce.get(IHEOutputAnnonce.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
//
// // 6 | Num�ro d'assur� de l'ayant droit ou du partenaire en cas d'ordre de splitting
// //Recuperation de l'id tiers de l'assure
// PRTiersWrapper twAyantDroit = PRTiersHelper.getTiers(getSession(),
// annonce.get(IHEOutputAnnonce.NUMERO_ASSURE_AYANT_DROIT));
// if (!(twAyantDroit == null)){
// vb.setIdTiersAyantDroit(twAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
// }
//
// // 7 | Motif de l'annonce
// vb.setMotif(annonce.get(IHEOutputAnnonce.MOTIF_ANNONCE));
//
// // 8 | Date de cl�ture: MMAA
// vb.setDateCloture(annonce.get(IHEOutputAnnonce.DATE_CLOTURE_MMAA));
//
// // 9 | Date de l'ordre: JJMMAA
// vb.setDateOrdre(annonce.get(IHEOutputAnnonce.DATE_ORDRE_JJMMAA));
//
// // 10 | Num�ro de la Caisse tenant le CI
// vb.setNoAgenceTenantCI(annonce.get(IHEOutputAnnonce.NUMERO_CAISSE__CI));
//
// // 11 | Num�ro de l'agence tenant le CI
// vb.setNoCaisseTenantCI(annonce.get(IHEOutputAnnonce.NUMERO_AGENCE_CI));
//
// // 12 | Num�ro d'assur�
// PRTiersWrapper twAssure = PRTiersHelper.getTiers(getSession(),
// annonce.get(IHEOutputAnnonce.NUMERO_ASSURE_AYANT_DROIT));
// if (twAssure==null){
// throw new REDownloaderException("Assure inexistant : " + annonce.get(IHEOutputAnnonce.NUMERO_ASSURE_AYANT_DROIT));
// }
// vb.setIdTiers(twAssure.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
//
// ///////////////////////////////////////////////////////////////////////////////
// // Inscription 1
// ///////////////////////////////////////////////////////////////////////////////
//
// // 13 | Code 1 (= partie CI)
// // pas utilise
//
// // 14 | R�serve: z�ros
// // pas utilise
//
// // 15 | Num�ro d'affili�
// vb.setNoAffilie(annonce.get(IHEOutputAnnonce.NUMERO_AFILLIE));
//
// // 16 | Chiffre-cl� d�signant les extournes
// vb.setCodeExtourne(annonce.get(IHEOutputAnnonce.CHIFFRE_CLE_EXTOURNES));
//
// // 17 | Chiffre-cl� du genre de cotisations
// vb.setGenreCotisation(annonce.get(IHEOutputAnnonce.CHIFFRE_CLE_GENRE_COTISATIONS));
//
// // 18 | Chiffre-cl� particulier (code particulier)
// vb.setCodeParticulier(annonce.get(IHEOutputAnnonce.CHIFFRE_CLEF_PARTICULIER));
//
// // 19 | Part aux bonifications d'assistance
// vb.setPartBonifAssist(annonce.get(IHEOutputAnnonce.PART_BONIFICATIONS_ASSISTANCES));
//
// // 20 | Code sp�cial
// vb.setCodeSpeciale(annonce.get(IHEOutputAnnonce.CODE_SPECIAL));
//
// // 21 | Dur�e de cotisations (d�but)
// vb.setMoisDebutCotisations(annonce.get(IHEOutputAnnonce.DUREE_COTISATIONS_DEBUT));
//
// // 22 | Dur�e de cotisations (fin)
// vb.setMoisFinCotisations(annonce.get(IHEOutputAnnonce.DUREE_COTISATIONS_FIN));
//
// // 23 | Ann�e de cotisations (AA)
// // Pas utilisie, on utilise le champs 28 (Ann�e cotisations AAAA) a la place
//
// // 24 | Revenu
// vb.setRevenu(annonce.get(IHEOutputAnnonce.REVENU));
//
// // 25 |R�serve: z�ro
// // pas utilise
//
// // 26 | Code A: amortissement
// // | Code D: exemption de cotisations
// // | Code S: sursis au paiement des coti-sations (seulement CSC)
// vb.setCodeADS(annonce.get(IHEOutputAnnonce.CODE_A_D_S));
//
// // 27 | Branche �conomique
// vb.setBrancheEconomique(annonce.get(IHEOutputAnnonce.BRANCHE_ECONOMIQUE));
//
// // 28 | Ann�e de cotisations (AAAA)
// vb.setAnneeCotisations(annonce.get(IHEOutputAnnonce.ANNEE_COTISATIONS));
//
// ///////////////////////////////////////////////////////////////////////////////
// // Inscription 2
// ///////////////////////////////////////////////////////////////////////////////
//
// // 14 | Partie Information
// vb.setPartieInformation(annonce.get(IHEOutputAnnonce.PARTIE_INFORMATION));
//
// // 15 | Num�ro postal de l'employeur
// vb.setNoPostalEmployeur(annonce.get(IHEOutputAnnonce.NUMERO_POSTAL_EMPLOYEUR));
//
// //Provenance par ligne, valeur par d�faut.
// // Car c'est HELIOS qui nous donne les valeurs
// vb.setProvenance("1");
//
// vb.add((BTransaction)transaction);
//
// return vb.getIdAnnonce();
// }
// }
