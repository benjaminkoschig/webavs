/**
 * 
 */
package globaz.amal.process.annonce;

import globaz.amal.process.AMALabstractProcess;
import globaz.amal.process.annonce.cosama.AMCosamaRecord;
import globaz.amal.process.annonce.cosama.AMCosamaRecordDetail;
import globaz.amal.process.annonce.cosama.AMCosamaRecordEnTete;
import globaz.amal.process.annonce.cosama.AMCosamaRecordTotal;
import globaz.amal.process.annonce.cosama.IAMCosamaRecord;
import globaz.amal.process.annonce.fileHelper.AnnonceCMProcessCosamaFileHelper;
import globaz.amal.process.annonce.fileHelper.AnnonceCMProcessCsvFileHelper;
import globaz.amal.process.annonce.fileHelper.AnnonceCMProcessFileHelper;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dhi
 * 
 */
public class AnnonceCMProcess extends AMALabstractProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeHistorique = null;
    private List<String> errorMessages = null;
    private List<String> idTiersCM = null;
    private String idTiersGroupe = null;

    private boolean isSimulation = true;

    /**
     * Enregistrement du nom des fichiers générés par zipKey
     * 
     * zipkey, fullfilenames
     * 0008/2010 simulation.csv
     */
    private HashMap<String, List<String>> listesFileName = null;

    /**
     * Création du mail à envoyer à l'utilisateur
     * 
     * @param idTiersCMInput
     * @param idTiersGroupeInput
     */
    private void createMail(List<String> idTiersCMInput, String idTiersGroupeInput) {
        // --------------------------------------------------
        // 0) Préparation du fichier zip
        // --------------------------------------------------
        String zipFileName = Jade.getInstance().getPersistenceDir();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String csDateDuJourShort = sdf.format(date);
        if (getIsSimulation()) {
            zipFileName += "Simulation_Annonces_" + csDateDuJourShort + ".zip";
        } else {
            zipFileName += "Annonces_" + csDateDuJourShort + ".zip";
        }

        Map<String, byte[]> myMap = new HashMap<String, byte[]>();
        if (listesFileName == null) {
            listesFileName = new HashMap<String, List<String>>();
        }
        Iterator<String> keySetIterator = listesFileName.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String currentKeySet = keySetIterator.next();
            List<String> fileNames = listesFileName.get(currentKeySet);
            Iterator<String> fileNameIterator = fileNames.iterator();
            while (fileNameIterator.hasNext()) {
                String currentFileName = fileNameIterator.next();
                String keyZip = currentKeySet;
                try {
                    if (currentFileName.lastIndexOf('\\') >= 0) {
                        keyZip += currentFileName.substring(currentFileName.lastIndexOf('\\') + 1);
                    } else if (currentFileName.lastIndexOf('/') >= 0) {
                        keyZip += currentFileName.substring(currentFileName.lastIndexOf('/') + 1);
                    } else {
                        continue;
                    }
                    FileInputStream fstream = null;
                    fstream = new FileInputStream(currentFileName);
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine = "";
                    String fullFile = "";
                    while ((strLine = br.readLine()) != null) {
                        fullFile += strLine + System.getProperty("line.separator");
                    }
                    myMap.put(keyZip, fullFile.getBytes());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
        }

        // --------------------------------------------------
        // 1) Préparation du message (body and subject)
        // --------------------------------------------------
        String subject = "";
        String message = "";
        try {
            if (getIsSimulation()) {
                subject += "Web@Lamal : processus de simulation d\'annonce terminé";
                message += "Processus de simulation d\'annonce aux assureurs terminé.\n\n";
            } else {
                subject += "Web@Lamal : processus d\'annonce terminé";
                message += "Processus d\'annonce aux assureurs terminé.\n\n";
            }
            if (JadeStringUtil.isEmpty(getAnneeHistorique())) {
                message += "Année de subside : pas de sélection (toutes)\n\n";
            } else {
                message += "Année de subside : " + getAnneeHistorique() + "\n\n";
            }
            if (JadeStringUtil.isBlankOrZero(idTiersGroupeInput)) {
                message += "Groupe sélectionné : pas de sélection particulière\n\n";
            } else {
                String currentGroupe = getIdTiersGroupe();
                String currentNoGroupe = "";
                String currentNomGroupe = "";
                CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                cmSearch.setForIdTiersGroupe(currentGroupe);
                cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                cmSearch.setWhereKey("rcListeOnlyActifs");
                cmSearch.setDefinedSearchSize(0);
                try {
                    cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                    if (cmSearch.getSize() > 0) {
                        CaisseMaladie currentCMGroupe = (CaisseMaladie) cmSearch.getSearchResults()[0];
                        currentNoGroupe = currentCMGroupe.getNumGroupe();
                        currentNomGroupe = currentCMGroupe.getNomGroupe();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (JadeStringUtil.isEmpty(currentNomGroupe) && JadeStringUtil.isEmpty(currentNoGroupe)) {
                    message += "Groupe sélectionné : " + currentGroupe + "\n\n";
                } else {
                    message += "Groupe sélectionné : " + JadeStringUtil.fillWithZeroes(currentNoGroupe, 4) + " "
                            + currentNomGroupe + "\n\n";
                }
            }

            if (idTiersCMInput == null) {
                if (!JadeStringUtil.isBlankOrZero(idTiersGroupeInput)) {
                    idTiersCMInput = new ArrayList<String>();
                    CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                    cmSearch.setForIdTiersGroupe(idTiersGroupeInput);
                    cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                    cmSearch.setWhereKey("rcListeOnlyActifs");
                    cmSearch.setDefinedSearchSize(0);
                    try {
                        cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                        for (int iCm = 0; iCm < cmSearch.getSize(); iCm++) {
                            CaisseMaladie currentCM = (CaisseMaladie) cmSearch.getSearchResults()[iCm];
                            if (!idTiersCMInput.contains(currentCM.getIdTiersCaisse())) {
                                idTiersCMInput.add(currentCM.getIdTiersCaisse());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (idTiersCMInput != null) {
                Iterator<String> cmIterator = idTiersCMInput.iterator();
                while (cmIterator.hasNext()) {
                    String currentCM = cmIterator.next();
                    String currentCMNo = "";
                    String currentCMName = "";
                    CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                    cmSearch.setForIdTiersCaisse(currentCM);
                    cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                    // cmSearch.setWhereKey("rcListeOnlyActifs");
                    cmSearch.setWhereKey("rcListe");
                    cmSearch.setDefinedSearchSize(0);
                    try {
                        cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                        if (cmSearch.getSize() > 0) {
                            CaisseMaladie currentCMIndividuel = (CaisseMaladie) cmSearch.getSearchResults()[0];
                            currentCMNo = currentCMIndividuel.getNumCaisse();
                            currentCMName = currentCMIndividuel.getNomCaisse();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (JadeStringUtil.isEmpty(currentCMNo) && JadeStringUtil.isEmpty(currentCMName)) {
                        message += "Caisse maladie " + currentCM + " : " + "annonces selon fichier joint\n";
                    } else {
                        boolean bFound = false;
                        currentCMNo = JadeStringUtil.fillWithZeroes(currentCMNo, 4);
                        keySetIterator = listesFileName.keySet().iterator();
                        while (keySetIterator.hasNext()) {
                            String currentKeySet = keySetIterator.next();
                            if (currentKeySet.startsWith(currentCMNo)) {
                                bFound = true;
                                break;
                            }
                        }
                        if (bFound) {
                            message += "Caisse maladie " + currentCMNo + " " + currentCMName + " : "
                                    + "annonces selon fichier joint\n";
                        } else {
                            message += "Caisse maladie " + currentCMNo + " " + currentCMName + " : " + "--\n";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (errorMessages != null) {
            message += "\n\nErrors : ";
            message += errorMessages.size();
            Iterator<String> errorMessagesIterator = errorMessages.iterator();
            while (errorMessagesIterator.hasNext()) {
                message += "\n" + errorMessagesIterator.next();
            }
        }
        // --------------------------------------------------
        // 2) Envoi du message
        // --------------------------------------------------
        try {
            // Envoi du message avec attachement si nécessaire
            if (listesFileName.size() > 0) {
                OutputStream myZipFile = new FileOutputStream(zipFileName);
                JadeZipUtil.zip(myZipFile, myMap);
                String[] allFileNames = new String[1];
                allFileNames[0] = zipFileName;
                JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                        subject, message, allFileNames);
            } else {
                JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                        subject, message, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Génération des records Cosama pour les annonces
     * Lecture des éléments en base de données
     * job in progress, récupération des annonces enregistrées
     * 
     * @return
     */
    private HashMap<String, List<AMCosamaRecord>> generateAnnonceCosamaRecords() {

        HashMap<String, List<AMCosamaRecord>> returnMap = new HashMap<String, List<AMCosamaRecord>>();

        // -----------------------------------------------------------
        // 0) Recherche des caisses maladies avec annonces in progress
        // -----------------------------------------------------------
        try {
            idTiersCM = AmalServiceLocator.getAnnonceService().getIdTiersCMAnnonceInProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<String> cmIterator = idTiersCM.iterator();
        while (cmIterator.hasNext()) {
            String currentIdTiersCM = cmIterator.next();
            // -----------------------------------------------------------
            // 2) Détermination du job en relation
            // -----------------------------------------------------------
            SimpleControleurJobSearch currentJobSearch = new SimpleControleurJobSearch();
            currentJobSearch.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
            currentJobSearch.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
            currentJobSearch.setForSubTypeJob(currentIdTiersCM);
            currentJobSearch.setDefinedSearchSize(0);
            try {
                currentJobSearch = AmalServiceLocator.getControleurEnvoiService().search(currentJobSearch);
                for (int iJob = 0; iJob < currentJobSearch.getSize(); iJob++) {
                    SimpleControleurJob currentJob = (SimpleControleurJob) currentJobSearch.getSearchResults()[iJob];
                    // -----------------------------------------------------------
                    // 3) Par job, recherches les annonces
                    // -----------------------------------------------------------
                    SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
                    statusSearch.setDefinedSearchSize(0);
                    statusSearch.setForIdJob(currentJob.getIdJob());
                    statusSearch = AmalServiceLocator.getControleurEnvoiService().search(statusSearch);
                    for (int iStatus = 0; iStatus < statusSearch.getSize(); iStatus++) {
                        SimpleControleurEnvoiStatus currentStatus = (SimpleControleurEnvoiStatus) statusSearch
                                .getSearchResults()[iStatus];
                        SimpleAnnonce currentAnnonce = AmalImplServiceLocator.getSimpleAnnonceService().read(
                                currentStatus.getIdAnnonce());
                        // -----------------------------------------------------------
                        // 5) Générer les objets cosama subsides
                        // -----------------------------------------------------------
                        AMCosamaRecord recordDetail = generateCosamaRecordDetail(currentAnnonce.getIdDetailFamille());
                        if (recordDetail != null) {
                            List<AMCosamaRecord> recordsForCurrentCM = returnMap.get(currentIdTiersCM);
                            if (recordsForCurrentCM == null) {
                                recordsForCurrentCM = new ArrayList<AMCosamaRecord>();
                            } else {
                                returnMap.remove(currentIdTiersCM);
                            }
                            recordsForCurrentCM.add(recordDetail);
                            returnMap.put(currentIdTiersCM, recordsForCurrentCM);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnMap;
    }

    /**
     * Création d'un record cosama pour l'id detail famille fournit en paramètre
     * 
     * @param currentIdSubside
     * @return
     */
    private AMCosamaRecord generateCosamaRecordDetail(String currentIdSubside) {
        AMCosamaRecord returnRecord = null;
        try {
            SimpleDetailFamille currentSubside = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                    currentIdSubside);
            if (!currentSubside.getCodeActif()) {
                // Subside désactivé !
                return null;
            }
            SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                    currentSubside.getIdFamille());
            Contribuable currentContribuable = AmalServiceLocator.getContribuableService().read(
                    currentSubside.getIdContribuable());
            if (JadeStringUtil.isBlankOrZero(currentContribuable.getContribuable().getIdTier())) {
                // Contribuable historique !
                return null;
            }
            AdresseComplexModel currentAdresse = AmalServiceLocator.getContribuableService().getContribuableAdresse(
                    currentContribuable.getContribuable().getIdTier());
            AMCosamaRecord currentRecord = AMCosamaRecord.getInstance(IAMCosamaRecord._TypeEnregistrementDetail);
            if (currentRecord instanceof AMCosamaRecordDetail) {
                // no assuré CM
                ((AMCosamaRecordDetail) currentRecord).setNoAssure(currentSubside.getNoAssure());
                // no personnel cantonal >> no contribuable
                ((AMCosamaRecordDetail) currentRecord).setNoPersonnelCantonal(currentContribuable.getPersonneEtendue()
                        .getPersonneEtendue().getNumContribuableActuel());
                // nom prénom
                ((AMCosamaRecordDetail) currentRecord).setNomPrenomUsuel(currentFamille.getNomPrenomUpper());
                // état civil cosama C, D, M, S, V
                if (currentFamille.getIsContribuable()) {
                    String etatCivil = currentContribuable.getPersonneEtendue().getPersonne().getEtatCivil();
                    if (etatCivil.equals(TITiersViewBean.CS_CELIBATAIRE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("C");
                    } else if (etatCivil.equals(TITiersViewBean.CS_DIVORCE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("D");
                    } else if (etatCivil.equals(TITiersViewBean.CS_MARIE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("M");
                    } else if (etatCivil.equals(TITiersViewBean.CS_SEPARE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("S");
                    } else if (etatCivil.equals(TITiersViewBean.CS_VEUF)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("V");
                    }
                }
                if ((currentAdresse != null) && (currentAdresse.getAdresse() != null)
                        && (currentAdresse.getLocalite() != null)) {
                    // Rue ou case postale
                    if (JadeStringUtil.isBlankOrZero(currentAdresse.getAdresse().getCasePostale())) {
                        ((AMCosamaRecordDetail) currentRecord).setNomOfficielRue(currentAdresse.getAdresse()
                                .getNumeroRue().toUpperCase()
                                + ", " + currentAdresse.getAdresse().getRue().toUpperCase());// "NO, RUE");
                    } else {
                        ((AMCosamaRecordDetail) currentRecord).setNomOfficielRue("CASE POSTALE "
                                + currentAdresse.getAdresse().getCasePostale());// "NO, RUE");
                    }
                    // npa
                    ((AMCosamaRecordDetail) currentRecord).setCodePostal(currentAdresse.getLocalite().getNumPostal()
                            .substring(0, 4));
                    // localité
                    ((AMCosamaRecordDetail) currentRecord).setLocalite(currentAdresse.getLocalite().getLocalite()
                            .toUpperCase());
                }
                // date naissance YYYYMMdd
                String dateNaissance = "";
                String sexe = "";
                // Set Personne Etendue
                // ---------------------------------------------------------------------------
                if (!JadeStringUtil.isEmpty(currentFamille.getIdTier())) {
                    // Set Personne Etendue
                    PersonneEtendueComplexModel personne = new PersonneEtendueComplexModel();
                    personne.setId(currentFamille.getIdTier());
                    personne.getTiers().setIdTiers(currentFamille.getIdTier());
                    try {
                        personne = TIBusinessServiceLocator.getPersonneEtendueService().read(personne.getId());
                        dateNaissance = personne.getPersonne().getDateNaissance();
                        sexe = personne.getPersonne().getSexe();
                        if (JadeStringUtil.isBlankOrZero(dateNaissance)) {
                            dateNaissance = currentFamille.getDateNaissance();
                        }
                        String nss = personne.getPersonneEtendue().getNumAvsActuel();
                        ((AMCosamaRecordDetail) currentRecord).setNoAVS(nss);
                    } catch (Exception exTiers) {
                        exTiers.printStackTrace();
                    }
                } else {
                    dateNaissance = currentFamille.getDateNaissance();
                }
                ((AMCosamaRecordDetail) currentRecord).setDateNaissance(JadeDateUtil.getYMDDate(JadeDateUtil
                        .getGlobazDate(dateNaissance)));
                // sexe 1-homme, 2-femme
                if (sexe.equals(TITiersViewBean.CS_FEMME)) {
                    ((AMCosamaRecordDetail) currentRecord).setSexe("2");
                } else {
                    ((AMCosamaRecordDetail) currentRecord).setSexe("1");
                }
                // bénéficiaire pc 0-non, 1-oui
                if (currentSubside.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
                    ((AMCosamaRecordDetail) currentRecord).setBeneficiairePC("1");
                }
                // bénéficiaire aide sociale 0-non, 1 - oui
                if (currentSubside.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue())) {
                    ((AMCosamaRecordDetail) currentRecord).setBeneficiaireAssiste("1");
                }
                // type d'octroi - 4 octroi
                ((AMCosamaRecordDetail) currentRecord).setTypeDecision("4");

                // travail avec les date de fin et de début
                String dateFin = JadeDateUtil.getYMDDate(JadeDateUtil.getGlobazDate("01."
                        + currentSubside.getFinDroit()));
                String dateDebut = JadeDateUtil.getYMDDate(JadeDateUtil.getGlobazDate("01."
                        + currentSubside.getDebutDroit()));
                String currentYear = "2999";
                if (dateDebut.length() > 3) {
                    currentYear = dateDebut.substring(0, 4);
                }
                String montantAnnuel = "";
                String montantMensuel = "";
                try {
                    int iNbMonth = 0;
                    if (JadeStringUtil.isEmpty(dateFin)) {
                        iNbMonth = JadeDateUtil.getNbMonthsBetween("01." + currentSubside.getDebutDroit(), "02.12."
                                + currentYear);
                    } else {
                        iNbMonth = JadeDateUtil.getNbMonthsBetween("01." + currentSubside.getDebutDroit(), "02."
                                + currentSubside.getFinDroit());
                    }
                    float fMontantContribution = Float
                            .parseFloat(currentSubside.getMontantContributionAvecSupplExtra());
                    montantMensuel = "" + ((int) fMontantContribution) + "00";
                    int iTotal = iNbMonth * ((int) fMontantContribution);
                    montantAnnuel = "" + iTotal + "00";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // montant mensuel du subside
                ((AMCosamaRecordDetail) currentRecord).setMontantEffectifSubside(montantMensuel);
                // montant du décompte (mensuel * période)
                ((AMCosamaRecordDetail) currentRecord).setMontantDecompte(montantAnnuel);

                // type de réduction de prime : 1 - rabais en francs, 2- en %, 3- charge assuré
                ((AMCosamaRecordDetail) currentRecord).setTypeReductionPrime("1");
                // montant de la réduction maximale mensuelle en francs
                ((AMCosamaRecordDetail) currentRecord).setMontantMaximumReduction(montantMensuel);
                // début du subside - YYYYMM
                if (dateDebut.length() > 5) {
                    dateDebut = dateDebut.substring(0, 6);
                }
                ((AMCosamaRecordDetail) currentRecord).setDateDebutSubside(dateDebut);
                // fin du subside - YYYYMM
                if (dateFin.length() > 5) {
                    dateFin = dateFin.substring(0, 6);
                } else {
                    dateFin = currentYear + "12";
                }
                ((AMCosamaRecordDetail) currentRecord).setDateFinSubside(dateFin);
            }
            returnRecord = currentRecord;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnRecord;
    }

    /**
     * Génération des lignes de total et d'entête
     * 
     * 
     * @param annoncesByYear
     *            Annonces groupées par année
     * @param noCaisseMaladie
     *            no caisse maladie
     * @return
     *         Hashmap complétée
     */
    private HashMap<String, List<AMCosamaRecord>> generateHeaderAndTotalRows(
            HashMap<String, List<AMCosamaRecord>> annoncesByYear, String noCaisseMaladie) {

        HashMap<String, List<AMCosamaRecord>> returnedMap = new HashMap<String, List<AMCosamaRecord>>();
        String noOrdre = "0";
        SimpleParametreApplication currentCosamaParametre = null;
        String dateCreation = JadeDateUtil.getYMDDate(new Date());
        // Recherche du numéro d'ordre cosama
        SimpleParametreApplicationSearch ordreSearch = new SimpleParametreApplicationSearch();
        ordreSearch.setForCsTypeParametre(IAMCodeSysteme.AMParametreApplication.COSAMA_INCR.getValue());
        try {
            ordreSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(ordreSearch);
            if (ordreSearch.getSize() == 1) {
                currentCosamaParametre = (SimpleParametreApplication) ordreSearch.getSearchResults()[0];
                noOrdre = currentCosamaParametre.getValeurParametre();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Iterator<String> anneeIterator = annoncesByYear.keySet().iterator();
        while (anneeIterator.hasNext()) {
            String currentYear = anneeIterator.next();
            // ---------------------------------------------------
            // génération en-tête
            // ---------------------------------------------------
            AMCosamaRecord enTete = AMCosamaRecord.getInstance(IAMCosamaRecord._TypeEnregistrementEnTete);
            if (enTete instanceof AMCosamaRecordEnTete) {
                ((AMCosamaRecordEnTete) enTete).setAnnee(currentYear);
                ((AMCosamaRecordEnTete) enTete).setCanton(IAMCosamaRecord._CantonJura);
                ((AMCosamaRecordEnTete) enTete).setDateCreation(dateCreation);
                ((AMCosamaRecordEnTete) enTete).setMoisDebut("01");
                ((AMCosamaRecordEnTete) enTete).setMoisFin("12");
                try {
                    int iNoOrdre = Integer.parseInt(noOrdre);
                    noOrdre = "" + (iNoOrdre + 1);
                } catch (Exception noOrdreException) {
                    noOrdreException.printStackTrace();
                }
                ((AMCosamaRecordEnTete) enTete).setNoOrdre(noOrdre);
                ((AMCosamaRecordEnTete) enTete).setNoPartition("1");
                ((AMCosamaRecordEnTete) enTete).setTypeTransmission("12");
                ((AMCosamaRecordEnTete) enTete).setNoCaisseMaladie(noCaisseMaladie);
            }
            int iCounter = 0;
            int iCurrentTotal = 0;
            // ---------------------------------------------------
            // all records parsing
            // ---------------------------------------------------
            List<AMCosamaRecord> allRecords = annoncesByYear.get(currentYear);
            Iterator<AMCosamaRecord> allRecordsIterator = allRecords.iterator();
            while (allRecordsIterator.hasNext()) {
                AMCosamaRecord currentRecord = allRecordsIterator.next();
                if (currentRecord instanceof AMCosamaRecordDetail) {
                    ((AMCosamaRecordDetail) currentRecord).setNoArticle("" + (++iCounter));
                    String montantDecompte = ((AMCosamaRecordDetail) currentRecord).getMontantDecompte();
                    try {
                        int iMontantDecompte = Integer.parseInt(montantDecompte);
                        iCurrentTotal += iMontantDecompte;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            // ---------------------------------------------------
            // génération total
            // ---------------------------------------------------
            AMCosamaRecord total = AMCosamaRecord.getInstance(IAMCosamaRecord._TypeEnregistrementTotal);
            if (total instanceof AMCosamaRecordTotal) {
                ((AMCosamaRecordTotal) total).setNombreArticles("" + iCounter);
                ((AMCosamaRecordTotal) total).setMontantTotalCumule("" + iCurrentTotal);
            }
            // ---------------------------------------------------
            // ajout des 2 lignes dans la collection
            // ---------------------------------------------------
            allRecords.add(enTete);
            allRecords.add(total);
            returnedMap.put(currentYear, allRecords);
        }
        // écriture du nouveau numéro d'ordre cosama
        if (currentCosamaParametre != null) {
            currentCosamaParametre.setValeurParametre(noOrdre);
            try {
                currentCosamaParametre = AmalServiceLocator.getSimpleParametreApplicationService().update(
                        currentCosamaParametre);
                JadeThread.commitSession();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return returnedMap;
    }

    /**
     * Génération des objets Cosama Record en fonction de la liste obtenues
     * 
     * @param subsidesToAnnounce
     * @return
     */
    private HashMap<String, List<AMCosamaRecord>> generateSimulationCosamaRecords(
            HashMap<String, List<String>> subsidesToAnnounce) {
        HashMap<String, List<AMCosamaRecord>> returnMap = new HashMap<String, List<AMCosamaRecord>>();
        List<SimpleControleurJob> jobToDelete = new ArrayList<SimpleControleurJob>();

        // ---------------------------------------------------------------------------
        // 1) Récupération des idjobs par caisses maladie MACTLJOB (idjob)
        // ---------------------------------------------------------------------------
        SimpleControleurJobSearch searchJob = new SimpleControleurJobSearch();
        searchJob.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
        searchJob.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
        searchJob.setDefinedSearchSize(0);
        try {
            searchJob = AmalImplServiceLocator.getSimpleControleurJobService().search(searchJob);
            for (int iJob = 0; iJob < searchJob.getSize(); iJob++) {
                SimpleControleurJob currentJob = (SimpleControleurJob) searchJob.getSearchResults()[iJob];
                String currentIdTiersCM = currentJob.getSubTypeJob();
                // Récupération des id subsides par caisse-maladie
                List<String> subsidesForCM = subsidesToAnnounce.get(currentIdTiersCM);
                if (subsidesForCM == null) {
                    // Pas de subsides à annoncer pour cette caisse maladie - on supprime le job
                    // AmalImplServiceLocator.getSimpleControleurJobService().delete(currentJob);
                    jobToDelete.add(currentJob);
                } else {
                    if (subsidesForCM.size() <= 0) {
                        // Pas de subsides à annoncer pour cette caisse maladie - on supprime le job
                        // AmalImplServiceLocator.getSimpleControleurJobService().delete(currentJob);
                        jobToDelete.add(currentJob);
                    }
                    Iterator<String> iteratorSubsides = subsidesForCM.iterator();
                    while (iteratorSubsides.hasNext()) {
                        String initialSubsideId = iteratorSubsides.next();
                        // -----------------------------------------------------------
                        // 3) Générer les objets cosama subsides
                        // -----------------------------------------------------------
                        AMCosamaRecord recordDetail = generateCosamaRecordDetail(initialSubsideId);
                        if (recordDetail != null) {
                            List<AMCosamaRecord> recordsForCurrentCM = returnMap.get(currentIdTiersCM);
                            if (recordsForCurrentCM == null) {
                                recordsForCurrentCM = new ArrayList<AMCosamaRecord>();
                            } else {
                                returnMap.remove(currentIdTiersCM);
                            }
                            recordsForCurrentCM.add(recordDetail);
                            returnMap.put(currentIdTiersCM, recordsForCurrentCM);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Suppression des jobs vides
        Iterator<SimpleControleurJob> jobToDeleteIterator = jobToDelete.iterator();
        while (jobToDeleteIterator.hasNext()) {
            SimpleControleurJob currentJob = jobToDeleteIterator.next();
            try {
                AmalImplServiceLocator.getSimpleControleurJobService().delete(currentJob);
            } catch (Exception ex) {
                JadeLogger.error(null, "Error deleting job in generateSimulationCosamaRecords :" + ex.toString());
            }
        }
        try {
            JadeThread.commitSession();
        } catch (Exception ex) {
            JadeLogger.error(null, "Error commit session in generateSimulationCosamaRecords :" + ex.toString());
        }
        return returnMap;
    }

    /**
     * @return the anneeHistorique
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Génération des annonces CM AMAL";
    }

    /**
     * @return the idTiersCM
     */
    public List<String> getIdTiersCM() {
        return idTiersCM;
    }

    /**
     * @return the idTiersGroupe
     */
    public String getIdTiersGroupe() {
        return idTiersGroupe;
    }

    /**
     * @return the isSimulation
     */
    public boolean getIsSimulation() {
        return isSimulation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.AbstractJadeJob#jobQueueName()
     */
    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    /**
     * Organisation d'un lot de records par année
     * 
     * @param currentCMRecords
     * @return
     */
    private HashMap<String, List<AMCosamaRecord>> organizeAnnoncesByYear(List<AMCosamaRecord> currentCMRecords) {
        HashMap<String, List<AMCosamaRecord>> recordsByYear = new HashMap<String, List<AMCosamaRecord>>();
        Iterator<AMCosamaRecord> iteratorRecords = currentCMRecords.iterator();
        while (iteratorRecords.hasNext()) {
            AMCosamaRecord currentRecord = iteratorRecords.next();
            if (currentRecord instanceof AMCosamaRecordDetail) {
                String currentYear = ((AMCosamaRecordDetail) currentRecord).getDateDebutSubside();
                if (currentYear.length() > 3) {
                    currentYear = currentYear.substring(0, 4);
                    List<AMCosamaRecord> currentRecordsByYear = recordsByYear.get(currentYear);
                    if (currentRecordsByYear == null) {
                        currentRecordsByYear = new ArrayList<AMCosamaRecord>();
                    } else {
                        recordsByYear.remove(currentYear);
                    }
                    currentRecordsByYear.add(currentRecord);
                    // Trier par nom prénom et période
                    try {
                        Collections.sort(currentRecordsByYear);
                    } catch (Exception exSort) {
                        exSort.printStackTrace();
                    }
                    recordsByYear.put(currentYear, currentRecordsByYear);
                }
            }
        }
        return recordsByYear;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.amal.process.AMALabstractProcess#process()
     */
    @Override
    protected void process() {

        /*
         * Map<String, List<String>> periodesTiers = new HashMap<String, List<String>>();
         * List<String> tiersPeriode1 = new ArrayList<String>();
         * tiersPeriode1.add("162166");
         * tiersPeriode1.add("189943");
         * tiersPeriode1.add("189989");
         * tiersPeriode1.add("189990");
         * tiersPeriode1.add("189996");
         * tiersPeriode1.add("189997");
         * tiersPeriode1.add("190005");
         * periodesTiers.put("08.2009", tiersPeriode1);
         * List<String> tiersPeriode2 = new ArrayList<String>();
         * tiersPeriode2.add("162166");
         * tiersPeriode2.add("189943");
         * tiersPeriode2.add("189989");
         * tiersPeriode2.add("189990");
         * tiersPeriode2.add("189996");
         * periodesTiers.put("08.2010", tiersPeriode2);
         * List<String> tiersPeriode3 = new ArrayList<String>();
         * tiersPeriode3.add("162166");
         * tiersPeriode3.add("189943");
         * tiersPeriode3.add("189989");
         * tiersPeriode3.add("189990");
         * tiersPeriode3.add("190005");
         * periodesTiers.put("08.2011", tiersPeriode3);
         * 
         * try {
         * Map<String, Map<String, List<SimpleDetailFamille>>> subsidesByPeriodes = AmalInterApplicationServiceLocator
         * .getPCCustomerService().getAmalSubsidesByPeriodes(periodesTiers);
         * Iterator<String> periodesIterator = subsidesByPeriodes.keySet().iterator();
         * JadeLogger
         * .info(null,
         * "-----------------------------------------------------                                            ");
         * JadeLogger.info(null, " NbPeriodes : " + subsidesByPeriodes.size());
         * JadeLogger
         * .info(null,
         * "-----------------------------------------------------                                            ");
         * while (periodesIterator.hasNext()) {
         * String currentPeriode = periodesIterator.next();
         * JadeLogger
         * .info(null,
         * "                                                                                             ");
         * JadeLogger.info(null, " Période courante : " + currentPeriode
         * + "                                            ");
         * Map<String, List<SimpleDetailFamille>> subsidesByTiers = subsidesByPeriodes.get(currentPeriode);
         * Iterator<String> tiersIterator = subsidesByTiers.keySet().iterator();
         * JadeLogger.info(null, "          NbTiers : " + subsidesByTiers.size()
         * + "                                            ");
         * while (tiersIterator.hasNext()) {
         * String currentTiers = tiersIterator.next();
         * List<SimpleDetailFamille> subsides = subsidesByTiers.get(currentTiers);
         * Iterator<SimpleDetailFamille> subsidesIterator = subsides.iterator();
         * JadeLogger.info(null, "                   Tiers : " + currentTiers + "  -   " + subsides.size()
         * + " subside(s)" + "                                            ");
         * while (subsidesIterator.hasNext()) {
         * SimpleDetailFamille currentSubside = subsidesIterator.next();
         * JadeLogger.info(null, "                          Subside : " + currentSubside.getDateEnvoi()
         * + " - " + currentSubside.getMontantContributionAvecSupplExtra()
         * + "                                            ");
         * }
         * }
         * }
         * } catch (Exception ex) {
         * ex.printStackTrace();
         * }
         */

        // ------------------------------------------------------------------------
        // 0) Enregistrement des inputs avant process
        // ------------------------------------------------------------------------
        List<String> idTiersCMInput = getIdTiersCM();
        String idTiersGroupeInput = getIdTiersGroupe();
        // ------------------------------------------------------------------------
        // 1) Traitement des annonces ou des simulations
        // ------------------------------------------------------------------------
        if (getIsSimulation()) {
            processSimulation();
        } else {
            processAnnonces();
        }
        // ------------------------------------------------------------------------
        // 2) Envoi du mail avec fichier zip
        // ------------------------------------------------------------------------
        createMail(idTiersCMInput, idTiersGroupeInput);

    }

    /**
     * Process mode Annonce
     */

    private void processAnnonces() {
        try {
            // ------------------------------------------------------------------------
            // 1) Récupération des id subsides des annonces à créer
            // ------------------------------------------------------------------------
            HashMap<String, List<String>> subsidesToAnnounce = AmalServiceLocator.getAnnonceService()
                    .getAnnoncesToCreate(idTiersGroupe, idTiersCM, anneeHistorique);
            // ------------------------------------------------------------------------
            // 2) Enregistrement en base des annonces et création des objets cosama
            // ------------------------------------------------------------------------
            errorMessages = AmalServiceLocator.getAnnonceService().writeAnnoncesInTables(subsidesToAnnounce);
            HashMap<String, List<AMCosamaRecord>> recordsByCaisseMaladie = generateAnnonceCosamaRecords();

            // ------------------------------------------------------------------------
            // 3) Organisation des records par année et création fichiers physiques
            // ------------------------------------------------------------------------
            Iterator<String> cmIterator = recordsByCaisseMaladie.keySet().iterator();
            int iCurrent = 1;
            super.getProgressHelper().setMax(recordsByCaisseMaladie.keySet().size());
            while (cmIterator.hasNext()) {
                super.getProgressHelper().setCurrent(iCurrent++);
                String currentCM = cmIterator.next();
                String noCaisseMaladie = currentCM;
                String nomCaisseMaladie = "";
                try {
                    AdministrationComplexModel caisseModel = TIBusinessServiceLocator.getAdministrationService().read(
                            currentCM);
                    noCaisseMaladie = caisseModel.getAdmin().getCodeAdministration();
                    noCaisseMaladie = JadeStringUtil.fillWithZeroes(noCaisseMaladie, 4);
                    nomCaisseMaladie = caisseModel.getTiers().getDesignation1();
                } catch (Exception exAdmin) {
                    exAdmin.printStackTrace();
                }
                // Records pour une caisse maladie
                List<AMCosamaRecord> currentCMRecords = recordsByCaisseMaladie.get(currentCM);
                HashMap<String, List<AMCosamaRecord>> annoncesByYear = organizeAnnoncesByYear(currentCMRecords);
                annoncesByYear = generateHeaderAndTotalRows(annoncesByYear, noCaisseMaladie);
                // Records par année, pour cette caisse maladie
                Iterator<String> annoncesIterator = annoncesByYear.keySet().iterator();
                while (annoncesIterator.hasNext()) {
                    String currentYear = annoncesIterator.next();
                    String shortFileName = "Liste_" + nomCaisseMaladie + "_" + currentYear + noCaisseMaladie;
                    shortFileName = JadeStringUtil.convertSpecialChars(shortFileName);
                    List<AMCosamaRecord> records = annoncesByYear.get(currentYear);
                    if (listesFileName == null) {
                        listesFileName = new HashMap<String, List<String>>();
                    }
                    // ------------------------------------------------------------------------
                    // 4) Génération des listes - grouper par année
                    // ------------------------------------------------------------------------
                    AnnonceCMProcessFileHelper csvFileHelper = AnnonceCMProcessFileHelper
                            .getInstance(AnnonceCMProcessFileHelper.FILE_TYPE_CSV);
                    if ((csvFileHelper != null) && (csvFileHelper instanceof AnnonceCMProcessCsvFileHelper)) {
                        csvFileHelper.setShortFileName(shortFileName.toUpperCase());
                        csvFileHelper.writeFile(records);
                        String keyZip = noCaisseMaladie + "/" + currentYear + "/";
                        List<String> fileNames = listesFileName.get(keyZip);
                        if (fileNames == null) {
                            fileNames = new ArrayList<String>();
                        }
                        fileNames.add(csvFileHelper.getFullFileName());
                        listesFileName.put(keyZip, fileNames);
                    }
                    shortFileName = currentYear + noCaisseMaladie;
                    // ------------------------------------------------------------------------
                    // 5) Génération des fichiers COSAMA
                    // ------------------------------------------------------------------------
                    AnnonceCMProcessFileHelper cosamaFileHelper = AnnonceCMProcessFileHelper
                            .getInstance(AnnonceCMProcessFileHelper.FILE_TYPE_COSAMA);
                    if ((cosamaFileHelper != null) && (cosamaFileHelper instanceof AnnonceCMProcessCosamaFileHelper)) {
                        cosamaFileHelper.setShortFileName(shortFileName.toUpperCase());
                        cosamaFileHelper.writeFile(records);
                        String keyZip = noCaisseMaladie + "/" + currentYear + "/";
                        List<String> fileNames = listesFileName.get(keyZip);
                        if (fileNames == null) {
                            fileNames = new ArrayList<String>();
                        }
                        fileNames.add(cosamaFileHelper.getFullFileName());
                        listesFileName.put(keyZip, fileNames);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            // ------------------------------------------------------------------------
            // 5) Remise des status des jobs annonces à SENT
            // ------------------------------------------------------------------------
            JadeThread.logClear();
            setJobStatusSent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Process mode simulation (listes)
     */
    private void processSimulation() {
        try {
            // ------------------------------------------------------------------------
            // 1) Récupération des id subsides des annonces à créer et création des objets cosama
            // ------------------------------------------------------------------------
            HashMap<String, List<String>> subsidesToAnnounce = AmalServiceLocator.getAnnonceService()
                    .getAnnoncesToCreate(idTiersGroupe, idTiersCM, anneeHistorique);
            HashMap<String, List<AMCosamaRecord>> recordsByCaisseMaladie = generateSimulationCosamaRecords(subsidesToAnnounce);
            // ------------------------------------------------------------------------
            // 2) Organisation des records par année et création fichiers physiques
            // ------------------------------------------------------------------------
            Iterator<String> cmIterator = recordsByCaisseMaladie.keySet().iterator();
            int iCurrent = 1;
            super.getProgressHelper().setMax(recordsByCaisseMaladie.keySet().size());
            while (cmIterator.hasNext()) {
                super.getProgressHelper().setCurrent(iCurrent++);
                String currentCM = cmIterator.next();
                String noCaisseMaladie = currentCM;
                String nomCaisseMaladie = "";
                try {
                    AdministrationComplexModel caisseModel = TIBusinessServiceLocator.getAdministrationService().read(
                            currentCM);
                    noCaisseMaladie = caisseModel.getAdmin().getCodeAdministration();
                    noCaisseMaladie = JadeStringUtil.fillWithZeroes(noCaisseMaladie, 4);
                    nomCaisseMaladie = caisseModel.getTiers().getDesignation1();
                } catch (Exception exAdmin) {
                    exAdmin.printStackTrace();
                }
                // Records pour une caisse maladie
                List<AMCosamaRecord> currentCMRecords = recordsByCaisseMaladie.get(currentCM);
                HashMap<String, List<AMCosamaRecord>> annoncesByYear = organizeAnnoncesByYear(currentCMRecords);
                annoncesByYear = generateHeaderAndTotalRows(annoncesByYear, noCaisseMaladie);
                // Records par année, pour cette caisse maladie
                Iterator<String> annoncesIterator = annoncesByYear.keySet().iterator();
                while (annoncesIterator.hasNext()) {
                    String currentYear = annoncesIterator.next();
                    String shortFileName = "Simulation_" + nomCaisseMaladie + "_" + currentYear + noCaisseMaladie;
                    shortFileName = JadeStringUtil.convertSpecialChars(shortFileName);
                    List<AMCosamaRecord> records = annoncesByYear.get(currentYear);
                    if (listesFileName == null) {
                        listesFileName = new HashMap<String, List<String>>();
                    }
                    // ------------------------------------------------------------------------
                    // 4) Génération des listes - grouper par année
                    // ------------------------------------------------------------------------
                    AnnonceCMProcessFileHelper csvFileHelper = AnnonceCMProcessFileHelper
                            .getInstance(AnnonceCMProcessFileHelper.FILE_TYPE_CSV);
                    if ((csvFileHelper != null) && (csvFileHelper instanceof AnnonceCMProcessCsvFileHelper)) {
                        if (shortFileName.indexOf("Simulation_Conc") == 0) {
                            JadeLogger.info(this, "Attention, concordia");
                        }
                        csvFileHelper.setShortFileName(shortFileName.toUpperCase());
                        csvFileHelper.writeFile(records);
                        String keyZip = noCaisseMaladie + "/" + currentYear + "/";
                        List<String> fileNames = listesFileName.get(keyZip);
                        if (fileNames == null) {
                            fileNames = new ArrayList<String>();
                        }
                        fileNames.add(csvFileHelper.getFullFileName());
                        listesFileName.put(keyZip, fileNames);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // ------------------------------------------------------------------------
            // 5) Remise des status des jobs annonces à SENT
            // ------------------------------------------------------------------------
            setJobStatusSent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param anneeHistorique
     *            the anneeHistorique to set
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param idTiersCM
     *            the idTiersCM to set
     */
    public void setIdTiersCM(List<String> idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

    /**
     * @param idTiersGroupe
     *            the idTiersGroupe to set
     */
    public void setIdTiersGroupe(String idTiersGroupe) {
        this.idTiersGroupe = idTiersGroupe;
    }

    /**
     * @param isSimulation
     *            the isSimulation to set
     */
    public void setIsSimulation(boolean isSimulation) {
        this.isSimulation = isSimulation;
    }

    /**
     * Reset du status du job en cours à sent
     */
    private void setJobStatusSent() {

        if (JadeThread.logHasMessages()) {
            JadeBusinessMessage[] allMessages = JadeThread.logMessages();
            for (int iMessage = 0; iMessage < allMessages.length; iMessage++) {
                String currentMessage = allMessages[iMessage].getContents(JadeThread.currentLanguage());
                if (errorMessages == null) {
                    errorMessages = new ArrayList<String>();
                }
                errorMessages.add(currentMessage);
            }
            JadeThread.logClear();
        }

        SimpleControleurJobSearch currentJobSearch = new SimpleControleurJobSearch();
        currentJobSearch.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
        currentJobSearch.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
        currentJobSearch.setDefinedSearchSize(0);
        try {
            currentJobSearch = AmalServiceLocator.getControleurEnvoiService().search(currentJobSearch);
            for (int iJob = 0; iJob < currentJobSearch.getSize(); iJob++) {
                SimpleControleurJob currentJob = (SimpleControleurJob) currentJobSearch.getSearchResults()[iJob];
                currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.SENT.getValue());
                currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
            }
        } catch (Exception ex) {
            JadeLogger.error(null, "Error reseting status job annonce à SENT :" + ex.toString());
            ex.printStackTrace();
        }
        try {
            JadeThread.commitSession();
        } catch (Exception ex) {
            JadeLogger.error(null, "Error commiting session when reseting status job annonce à SENT :" + ex.toString());
            ex.printStackTrace();
        }

    }

}
