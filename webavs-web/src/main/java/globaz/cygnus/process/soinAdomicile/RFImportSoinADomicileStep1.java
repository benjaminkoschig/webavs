package globaz.cygnus.process.soinAdomicile;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.process.financementSoin.NSS;
import globaz.cygnus.process.financementSoin.step1.RFImportFinancementSoinStep1Handler;
import globaz.cygnus.process.financementSoin.step1.RFProcessImportFinancementSoinEnum;
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
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.business.services.PyxisCrudServiceLocator;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class RFImportSoinADomicileStep1 implements JadeProcessStepInterface, JadeProcessStepBeforable,
        JadeProcessStepAfterable, JadeProcessStepInfoCurrentStep {

    private String gestionnaire = null;
    private JadeProcessExecut jadeInfo;
    private List<JadeProcessEntity> listEntity = new ArrayList<JadeProcessEntity>();
    private Map<NSS, List<LigneFichier>> mapLignesEnTraitement = new HashMap<NSS, List<LigneFichier>>();
    private Map<LigneFichier, RFDemande> demandesAcreer = new HashMap<LigneFichier, RFDemande>();
    private boolean hasErrors = false;

    private SimpleHome findHome(String noIdentification) throws HomeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        SimpleHomeSearch search = new SimpleHomeSearch();
        search.setForNoIdentification(noIdentification);

        JadeAbstractModel[] results = PegasusImplServiceLocator.getSimpleHomeService().search(search)
                .getSearchResults();

        // List<String> ids = QueryExecutor
        // .execute(
        // "SELECT ID_TIERS FROM (SELECT "
        // + "case when TITIERP1.HTLDU2 is not null then TRIM(TITIERP1.HTLDU1)||' '|| TRIM(TITIERP1.HTLDU2) "
        // + "ELSE TITIERP1.HTLDU1 end as libelle,  TITIERP1.HTITIE as ID_TIERS  "
        // + "FROM SCHEMA.TIADMIP TIADMIP1 "
        // + "INNER JOIN SCHEMA .TITIERP TITIERP1 ON ( TIADMIP1.HTITIE=TITIERP1.HTITIE )) where libelle = '"
        // + nom.toUpperCase() + "' or   libelle = '"
        // + JadeStringUtil.convertSpecialChars(nom).toUpperCase() + "' ", String.class);

        if (results.length == 1) {
            return ((SimpleHome) results[0]);
        }
        // AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        // search.setForDesignation1Like(nom);
        // search = TIBusinessServiceLocator.getAdministrationService().find(search);
        // if (search.getSize() == 1) {
        // return (AdministrationComplexModel) search.getSearchResults()[0];
        // }
        return null;
    }

    private boolean hasAdressePaiement(String idTiers) throws Exception {
        if (!JadeStringUtil.isEmpty(idTiers)) {
            AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers,
                    Boolean.TRUE, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    JACalendar.todayJJsMMsAAAA(), null);
            if (adresse != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method pour créer les demandes respectives (sans erreurs)
     */
    private void generateDemandes() {
        for (Entry<NSS, List<LigneFichier>> entry : mapLignesEnTraitement.entrySet()) {
            NSS nss = entry.getKey();
            List<LigneFichier> lignes = entry.getValue();
            for (LigneFichier ligne : lignes) {
                try {

                    // String idAdministration = findHome(ligne.getNomService());
                    SimpleHome home = findHome(ligne.getNoIdentification());

                    if (home == null) {
                        ligne.addErrors("ADMINISTRATION_NON_TROUVER");
                    } else if (!hasAdressePaiement(home.getIdTiersHome())) {
                        ligne.addErrors("ADMINISTRATION_ADRESSE_DE_PAIEMENT_NON_TROUVE");
                    }

                    PersonneAVS personneAvs = PyxisCrudServiceLocator.getPersonneAvsCrudService().readByNss(
                            nss.getNss());
                    if (personneAvs.estInitialisee()) {
                        if (personneAvs.getNom().isEmpty()) {
                            ligne.addErrors("TIERS_NOM_VIDE");
                        }

                        if (personneAvs.getPrenom().isEmpty()) {
                            ligne.addErrors("TIERS_PRENOM_VIDE");
                        }
                    } else {
                        ligne.addErrors("NSS_TIERS_NON_EXISTANT");
                    }

                    if (!ligne.hasError() && !hasErrors) {
                        RFPrDemandeJointDossier dossier = findDossierOrCreate(String.valueOf(personneAvs.getId()));
                        demandesAcreer.put(ligne, createDemande(ligne, dossier, home.getIdTiersHome()));
                    } else {
                        hasErrors = true;
                    }
                } catch (Exception e) {
                    hasErrors = true;
                    ligne.addException(e);
                }
            }
        }
    }

    private RFDemande createDemande(LigneFichier ligne, RFPrDemandeJointDossier dossier, String idTiersFournisseur) {
        RFDemande rfDemande = new RFDemande();

        rfDemande.setSession(BSessionUtil.getSessionFromThreadContext());
        rfDemande.setIdSousTypeDeSoin(IRFTypesDeSoins.st_25_SOINS_A_DOMICILE);
        rfDemande.setIdDossier(dossier.getIdDossier());
        rfDemande.setDateFacture(ligne.getDateDebut().getSwissValue());
        rfDemande.setDateReception(ligne.getDateDecompte().getSwissValue());
        rfDemande.setDateDebutTraitement(ligne.getDateDebut().getSwissValue());
        rfDemande.setDateFinTraitement(ligne.getDateFin().getSwissValue());
        rfDemande.setMontantAPayer(ligne.getTotal().toStringFormat());
        rfDemande.setMontantFacture(ligne.getTotal().toStringFormat());
        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
        rfDemande.setCsSource(IRFDemande.SYSTEME);
        rfDemande.setSpy(new BSpy("importrfm"));
        rfDemande.setIdGestionnaire(gestionnaire);
        rfDemande.setIdFournisseur(idTiersFournisseur);
        rfDemande.setIdAdressePaiement(idTiersFournisseur);
        return rfDemande;
    }

    private RFPrDemandeJointDossier findDossierOrCreate(String idTiers) throws Exception {
        RFPrDemandeJointDossier dossier;
        // recherche du dossier par idTiers
        dossier = RFUtils.getDossierJointPrDemande(idTiers, BSessionUtil.getSessionFromThreadContext());

        // création du dossier si inexistant
        if (dossier == null) {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            BTransaction transaction = BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction();

            RFUtils.ajouterDossier(idTiers, gestionnaire, session, transaction);
            dossier = RFUtils.getDossierJointPrDemande(idTiers, BSessionUtil.getSessionFromThreadContext());
        }
        return dossier;
    }

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        BSession session = BSessionUtil.getSessionFromThreadContext();

        try {
            gestionnaire = map.get(RFProcessImportFinancementSoinEnum.GESTIONNAIRE);

            // 1. Iteration sur la liste Entity et remplissage map k,v (NSS keyNss, List Ligne).
            regrouperEntitesParNss();

            // 2. Gestion doublons, si doublons --> log, et on le sort de liste
            verifierSiDoublonDeLignes(mapLignesEnTraitement);

            // 2b. check superposition
            verifierChevauchementPeriodes(mapLignesEnTraitement);

            // 3. Traitement des lignes, ajout des demandes rfm
            generateDemandes();
            if (!hasErrors) {
                createAllDemandes();
            } else {
                // On supprimer la businessKey pour permettre la reimportation du fichier
                jadeInfo.getSimpleExecutionProcess().setBusinessKey(null);
                JadeProcessServiceLocator.getSimpleExecutionProcessService().update(
                        jadeInfo.getSimpleExecutionProcess());
            }

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            session.addError(e.toString());
            e.printStackTrace();
        } finally {

            // envoi mail
            RFimportService importService = new RFimportService();

            String email = map.get(RFProcessImportFinancementSoinEnum.EMAIL);

            importService.sendMailForSoinDomicile(session, mapLignesEnTraitement, email);

            try {
                JadeFsFacade.delete(jadeInfo.getProperties().get(
                        RFProcessImportFinancementSoinEnum.FILE_PATH_FOR_POPULATION));
            } catch (Exception e) {
                new RuntimeException(e);
            }
        }
    }

    private void createAllDemandes() {
        for (Entry<LigneFichier, RFDemande> entry : demandesAcreer.entrySet()) {
            try {
                entry.getValue().add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());
            } catch (Exception e) {
                entry.getKey().addErrors("Impossible de créer la demande - " + e.toString());
                e.printStackTrace();
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
     * Method pour charger la liste d'entités dans une map et la trier regrouper par nss
     */
    private void regrouperEntitesParNss() {
        // parcours de listEntity
        for (JadeProcessEntity entity : listEntity) {
            String cellule[] = entity.getValue1().split(";");

            LigneFichier ligne = new LigneFichier(cellule, Integer.valueOf(entity.getIdRef()));
            if (ligne.isValid()) {
                if (!mapLignesEnTraitement.containsKey(ligne.getNumNss())) {
                    mapLignesEnTraitement.put(ligne.getNumNss(), new ArrayList<LigneFichier>());
                }
                mapLignesEnTraitement.get(ligne.getNumNss()).add(ligne);
            }
        }

        for (NSS keyNss : mapLignesEnTraitement.keySet()) {
            Collections.sort(mapLignesEnTraitement.get(keyNss));
        }
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut jadeInfo) {
        this.jadeInfo = jadeInfo;
    }

    /**
     * Method pour vérifier si plusieurs demandes d'un assuré, on des période qui se chevauchent.
     * 
     * @param listEntity
     */
    private void verifierChevauchementPeriodes(Map<NSS, List<LigneFichier>> map) {

        // Iteration sur chaque clé
        for (Entry<NSS, List<LigneFichier>> entry : map.entrySet()) {

            // Iteration sur chaque entité d'une clé
            for (LigneFichier ligne : entry.getValue()) {
                if (!ligne.isPeriodeEmpty()) {
                    for (LigneFichier lignesSuivantes : map.get(entry.getKey())) {
                        if (!lignesSuivantes.getNumeroLigne().equals(ligne.getNumeroLigne())) {
                            if (lignesSuivantes.hasChevauchement(ligne)) {
                                lignesSuivantes.addErrors("CHEVAUCHEMENT_PERIODES");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Methode qui retourne True si un doublon es présent dans la liste
     * 
     * @param list
     * @return boolean true/false
     */
    private static boolean hasDuplicate(List<LigneFichier> list) {

        List<LigneFichier> listeLignesParNss = new ArrayList<LigneFichier>();

        for (LigneFichier ligne : list) {

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

    /**
     * Methode pour ajouter message d'erreur à la ligne, si présence de doublons dans la liste
     * 
     * @param mapLignes
     */
    private void verifierSiDoublonDeLignes(Map<NSS, List<LigneFichier>> mapLignes) {

        for (NSS keyNss : mapLignesEnTraitement.keySet()) {
            // si doublon, on ajoute un message d'erreur à toutes les lignes du même tiers.
            if (RFImportSoinADomicileStep1.hasDuplicate(mapLignesEnTraitement.get(keyNss))) {
                for (LigneFichier ligne : mapLignesEnTraitement.get(keyNss)) {
                    ligne.addErrors("DOUBLON_LIGNE");
                }
            }
        }
    }

}