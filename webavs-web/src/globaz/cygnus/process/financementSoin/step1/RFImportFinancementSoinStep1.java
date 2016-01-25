package globaz.cygnus.process.financementSoin.step1;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeManager;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.process.financementSoin.CellulesExcelEnum;
import globaz.cygnus.process.financementSoin.NSS;
import globaz.cygnus.process.financementSoin.RFLigneFichierExcel;
import globaz.cygnus.service.RFimportService;
import globaz.cygnus.utils.RFUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.utils.PRDateUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nu.xom.IllegalAddException;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.HomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * 
 * @author LFO (old)
 * @author MBO 10.01.2014
 * 
 */
public class RFImportFinancementSoinStep1 implements JadeProcessStepInterface, JadeProcessStepBeforable,
        JadeProcessStepAfterable, JadeProcessStepInfoCurrentStep {

    /**
     * Methode qui retourne True si un doublon es présent dans la liste
     * 
     * @param list
     * @return boolean true/false
     */
    private static boolean hasDuplicate(ArrayList<RFLigneFichierExcel> list) {

        List<RFLigneFichierExcel> listeLignesParNss = new ArrayList<RFLigneFichierExcel>();

        for (RFLigneFichierExcel ligne : list) {

            if (listeLignesParNss.isEmpty()) {
                listeLignesParNss.add(ligne);
            } else {

                if (listeLignesParNss.contains(ligne)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String gestionnaire = null;

    private JadeProcessExecut jadeInfo;
    private List<JadeProcessEntity> listEntity = new ArrayList<JadeProcessEntity>();
    private List<String> listNAVS = new ArrayList<String>();
    Map<NSS, ArrayList<RFLigneFichierExcel>> mapLignesEnTraitement = new HashMap<NSS, ArrayList<RFLigneFichierExcel>>();

    /**
     * Methode pour créer les demandes respectives (sans erreurs)
     */
    private void AddDemandeRfm() throws Exception {

        try {
            // for (Iterator iterator = this.mapSoin.keySet().iterator(); iterator.hasNext();) {
            for (NSS keyAVS : mapLignesEnTraitement.keySet()) {

                for (RFLigneFichierExcel ligne : mapLignesEnTraitement.get(keyAVS)) {
                    if (ligne.getCellulesEnErreur().size() == 0) {

                        RFPrDemandeJointDossier dossier = null;

                        try {
                            // recherche du tiers par nss
                            PRTiersWrapper tiers = PRTiersHelper.getTiers(BSessionUtil.getSessionFromThreadContext(),
                                    keyAVS.getNss());
                            String idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

                            // recherche du dossier par idTiers
                            dossier = RFUtils.getDossierJointPrDemande(idTiers,
                                    BSessionUtil.getSessionFromThreadContext());

                            // création du dossier si inexistant
                            if (dossier == null) {
                                BSession session = BSessionUtil.getSessionFromThreadContext();
                                BTransaction transaction = BSessionUtil.getSessionFromThreadContext()
                                        .getCurrentThreadTransaction();

                                RFUtils.ajouterDossier(idTiers, gestionnaire, session, transaction);
                                dossier = RFUtils.getDossierJointPrDemande(idTiers,
                                        BSessionUtil.getSessionFromThreadContext());
                            }

                            // Ajout des demandes rfm
                            RFDemandeManager rfDemandeMgr = new RFDemandeManager();
                            rfDemandeMgr.setSession(BSessionUtil.getSessionFromThreadContext());

                            RFDemande rfDemande = new RFDemande();

                            rfDemande.setSession(BSessionUtil.getSessionFromThreadContext());
                            rfDemande.setIdSousTypeDeSoin(IRFTypesDeSoins.st_20_FINANCEMENT_DES_SOINS);
                            rfDemande.setIdDossier(dossier.getIdDossier());
                            rfDemande.setDateFacture(ligne.getDateDebut());
                            rfDemande.setDateReception(ligne.getDateDecompte());
                            rfDemande.setDateDebutTraitement(ligne.getDateDebut());
                            rfDemande.setDateFinTraitement(ligne.getDateFin());
                            rfDemande.setMontantAPayer(ligne.getMontantTotal());
                            rfDemande.setMontantFacture(ligne.getMontantTotal());
                            rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                            rfDemande.setCsSource(IRFDemande.SYSTEME);
                            rfDemande.setSpy(new BSpy("importrfm"));
                            rfDemande.setIdGestionnaire(gestionnaire);

                            try {

                                HomeSearch search = new HomeSearch();
                                search.setForNumeroIdentification(ligne.getNumHome());
                                HomeSearch homeSearch = PegasusServiceLocator.getHomeService().search(search);
                                Home home = null;
                                boolean error = false;

                                if (homeSearch.getSize() > 0) {
                                    home = (Home) homeSearch.getSearchResults()[0];
                                    if (JadeStringUtil.isEmpty(home.getSimpleHome().getIdTiersHome())) {
                                        error = true;
                                    } else {

                                        AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService()
                                                .getAdressePaiementTiers(home.getSimpleHome().getIdTiersHome(),
                                                        Boolean.TRUE,
                                                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                                                        JACalendar.todayJJsMMsAAAA(), null);

                                        if (adresse.getAdresseFormate() == null) {
                                            error = true;
                                        } else {
                                            rfDemande.setIdFournisseur(home.getSimpleHome().getIdTiersHome());
                                            rfDemande.setIdAdressePaiement(home.getSimpleHome().getIdTiersHome());
                                        }

                                    }
                                }

                                if ((home == null) || error) {

                                    throw new IllegalAddException(
                                            "Home introuvable - Impossible de créer la demande pour la ligne n°"
                                                    + ligne.getNumeroLigne());
                                } else {
                                    rfDemande.add(BSessionUtil.getSessionFromThreadContext()
                                            .getCurrentThreadTransaction());
                                }
                            } catch (Exception e) {
                                throw new IllegalAddException(
                                        "Home introuvable - Impossible de créer la demande pour la ligne n°"
                                                + ligne.getNumeroLigne());
                            }
                        } catch (Exception e1) {

                            throw new IllegalAddException(
                                    "Erreur de tiers - Impossible de créer la demande pour la ligne "
                                            + ligne.getNumeroLigne() + " :" + e1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(
                    "RFImportFinacmentSoinStep1_AddDemandeRfm() : erreur lors de l'insertion des demandes dans l'application. ");
        }
    }

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        BSession session = BSessionUtil.getSessionFromThreadContext();

        try {

            gestionnaire = map.get(RFProcessImportFinancementSoinEnum.GESTIONNAIRE);

            // 1. Iteration sur la liste Entity et remplissag map k,v (NSS keyNss, List Ligne).
            regrouperEntitesParNss();

            // 2. Gestion doublons, si doublons --> log, et on le sort de liste
            verifierSiDoublonDeLignes(mapLignesEnTraitement);

            // 2b. check superposition
            verifierChevauchementPeriodes(mapLignesEnTraitement);

            // 3. Traitement des lignes, ajout des demandes rfm
            AddDemandeRfm();

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            session.addError(e.toString());

        } finally {

            // envoi mail
            RFimportService importService = new RFimportService();

            String email = map.get(RFProcessImportFinancementSoinEnum.EMAIL);

            importService.sendMail(session, mapLignesEnTraitement, email, gestionnaire);

            try {
                // bz_7053 : Suppression du fichier temporaire créé lors de l'importation
                JadeFsFacade.delete(jadeInfo.getProperties().get(
                        RFProcessImportFinancementSoinEnum.FILE_PATH_FOR_POPULATION));

            } catch (Exception e) {
                new RuntimeException(e);
            }
        }
    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new RFImportFinancementSoinStep1Handler(listEntity);
    }

    /**
     * Methode pour charger la liste d'entités dans une map et la trier regrouper par nss
     */
    private void regrouperEntitesParNss() throws Exception {

        try {
            // parcours de listEntity
            for (JadeProcessEntity ligne : listEntity) {

                String cellule[] = ligne.getValue1().split(";");

                RFLigneFichierExcel ligneExcel = new RFLigneFichierExcel(cellule, ligne.getIdRef());

                if (mapLignesEnTraitement.containsKey(new NSS(ligneExcel.getNumNss()))) {
                    mapLignesEnTraitement.get(new NSS(ligneExcel.getNumNss())).add(ligneExcel);
                } else {

                    ArrayList<RFLigneFichierExcel> newLigne = new ArrayList<RFLigneFichierExcel>();
                    newLigne.add(ligneExcel);

                    mapLignesEnTraitement.put(new NSS(ligneExcel.getNumNss().toString()), newLigne);

                }
            }

            for (NSS keyNss : mapLignesEnTraitement.keySet()) {
                Collections.sort(mapLignesEnTraitement.get(keyNss));
            }
        } catch (Exception e) {
            throw new Exception(
                    "RFImportFinacmentSoinStep1_regrouperEntitesParNss() : erreur dans le regroupement des lignes par nss. ");
        }
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut jadeInfo) {
        this.jadeInfo = jadeInfo;

    }

    /**
     * Methode pour vérifier si plusieurs demandes d'un assuré, on des période qui se chevauchent.
     * 
     * @param listEntity
     */
    private void verifierChevauchementPeriodes(Map<NSS, ArrayList<RFLigneFichierExcel>> listEntity) throws Exception {
        try {
            // Iteration sur chaque clé
            for (NSS keyNss : mapLignesEnTraitement.keySet()) {

                // Iteration sur chaque entité d'une clé
                for (RFLigneFichierExcel ligne : mapLignesEnTraitement.get(keyNss)) {

                    // Ne traite pas la ligne si celle-ci à déjà été utilisé dans l'iteration précédente et que le
                    // chevauchement à été détecté,
                    if ((!ligne.getCellulesEnErreur().contains(CellulesExcelEnum.CHEVAUCHEMENT_PERIODES))
                            && (!ligne.getCellulesEnErreur().contains(CellulesExcelEnum.LIGNE_INCOMPLETE))) {

                        // Iteration sur chaque entité d'une clé, excepté l'entité utilisé par 'ligne'
                        for (RFLigneFichierExcel lignesSuivantes : mapLignesEnTraitement.get(keyNss)) {
                            if (!lignesSuivantes.getNumeroLigne().equals(ligne.getNumeroLigne())) {

                                PRPeriode periode = new PRPeriode();
                                periode.setDateDeDebut(lignesSuivantes.getDateDebut());
                                periode.setDateDeFin(lignesSuivantes.getDateFin());

                                // Si doublon, ajout du message au 2 lignes concernées.
                                if (PRDateUtils.isDateDansLaPeriode(periode, ligne.getDateFin())) {

                                    ligne.addCelluleExcelEnum(CellulesExcelEnum.CHEVAUCHEMENT_PERIODES);
                                    lignesSuivantes.addCelluleExcelEnum(CellulesExcelEnum.CHEVAUCHEMENT_PERIODES);

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(
                    "RFImportFinacmentSoinStep1_verifierChevauchementPeriodes() : erreur dans la vérification de chevauchement de périodes. ");
        }
    }

    /**
     * Methode pour ajouter message d'erreur à la ligne, si présence de doublons dans la liste
     * 
     * @param mapLignes
     */
    private void verifierSiDoublonDeLignes(Map<NSS, ArrayList<RFLigneFichierExcel>> mapLignes) throws Exception {
        try {
            for (NSS keyNss : mapLignesEnTraitement.keySet()) {

                // si doublon, on ajoute un message d'erreur à toutes les lignes du même tiers.
                if (RFImportFinancementSoinStep1.hasDuplicate(mapLignesEnTraitement.get(keyNss))) {

                    for (RFLigneFichierExcel ligne : mapLignesEnTraitement.get(keyNss)) {

                        ligne.addCelluleExcelEnum(CellulesExcelEnum.DOUBLON_LIGNE);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(
                    "RFImportFinancementSoinStep1_verifierSiDoublonDeLignes() : erreur dans la vérification de doublons de lignes. ");
        }
    }
}