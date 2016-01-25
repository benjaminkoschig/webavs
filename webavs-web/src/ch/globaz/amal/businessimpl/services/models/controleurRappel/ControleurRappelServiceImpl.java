/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.controleurRappel;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.journalisation.access.JOComplementJournal;
import globaz.journalisation.db.journalisation.access.JOComplementJournalManager;
import globaz.journalisation.db.journalisation.access.JOGroupeJournal;
import globaz.journalisation.db.journalisation.access.JOGroupeJournalManager;
import globaz.journalisation.db.journalisation.access.JOJournalisation;
import globaz.journalisation.db.journalisation.access.JOJournalisationManager;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.domaines.LIDomainesManager;
import globaz.libra.db.dossiers.LIDossiersJointTiers;
import globaz.libra.db.dossiers.LIDossiersJointTiersManager;
import globaz.libra.db.journalisations.LIEcheancesJointDossiersManager;
import globaz.libra.db.journalisations.LIJournalisationsJointDossiers;
import globaz.libra.db.journalisations.LIJournalisationsJointDossiersManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.echeances.AMControleurRappelDetail;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleRappel;
import ch.globaz.envoi.business.services.ENServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * @author DHI
 * 
 */
public class ControleurRappelServiceImpl implements ControleurRappelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#checkRappelInProgress ()
     */
    @Override
    public String checkRappelInProgress(String idDetailFamille) {
        // To Find :
        // ------------------------------------------------------------------
        // Libra Journalisation : 32000016 CS_JO_JOURNALISATION
        // Complement Journalisation : 32000010 CS_JO_FMT_FORMULE_RAPPEL
        // Libellé : contient | et [1] == idDetailFamille et [2] == idFormule

        // ------------------------------------------------------------------
        // Récupération du dossier LIBRA
        // ------------------------------------------------------------------
        String forIdTiers = null;
        String forIdDossier = null;
        String forIdDomaine = null;
        String forIdExterne = null;

        try {
            SimpleDetailFamille detailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                    idDetailFamille);
            SimpleFamille famille = AmalImplServiceLocator.getSimpleFamilleService().read(detailFamille.getIdFamille());
            forIdTiers = famille.getIdTier();
            forIdExterne = famille.getIdFamille();

            // Find the domaine ID
            LIDomainesManager domaineManager = new LIDomainesManager();
            domaineManager.setForCsDomaine(ILIConstantesExternes.CS_DOMAINE_AMAL);
            domaineManager.find();
            for (int iDomaine = 0; iDomaine < domaineManager.getSize(); iDomaine++) {
                LIDomaines domaines = (LIDomaines) domaineManager.getEntity(iDomaine);
                forIdDomaine = domaines.getIdDomaine();
            }

            // Find the dossier
            LIDossiersJointTiersManager dossierManager = new LIDossiersJointTiersManager();
            dossierManager.setForIdDomaine(forIdDomaine);
            dossierManager.setForIdExterne(forIdExterne);
            dossierManager.setForIdTiers(forIdTiers);
            dossierManager.find();
            if (dossierManager.size() > 1) {
                JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records (" + dossierManager.size()
                        + ") found with id " + forIdTiers);
                return null;
            }
            for (int iDossier = 0; iDossier < dossierManager.getSize(); iDossier++) {
                LIDossiersJointTiers dossier = (LIDossiersJointTiers) dossierManager.get(iDossier);
                forIdDossier = dossier.getIdDossier();
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in rollbackRappelInProgressToRappel :" + e.toString());
        }

        // ------------------------------------------------------------------
        // Cherchons des dossiers selon critère susmentionnés
        // --------------------------------------------------
        LIJournalisationsJointDossiersManager managerDossier = new LIJournalisationsJointDossiersManager();
        managerDossier.setForCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        managerDossier.setForValeurCodeSysteme(JOConstantes.CS_JO_FMT_FORMULE_RAPPEL);
        managerDossier.changeManagerSize(BManager.SIZE_NOLIMIT);
        managerDossier.setJointureFichier(1);
        if (forIdDossier != null) {
            managerDossier.setForIdDossier(forIdDossier);
        }
        if ((forIdTiers != null) && (forIdTiers.length() > 0)) {
            managerDossier.setForIdTiers(forIdTiers);
        }
        try {
            managerDossier.find();
            for (int iDossier = 0; iDossier < managerDossier.size(); iDossier++) {
                LIJournalisationsJointDossiers dossier = (LIJournalisationsJointDossiers) managerDossier.get(iDossier);
                // Controle du libelle
                // doit être du format blabla | idDetailFamille | csFormule | status
                // ------------------------------------------------------------------
                String libelle = dossier.getLibelle();
                if ((libelle != null) && (libelle.length() > 0)) {
                    if (libelle.contains("|")) {
                        String[] libelleValues = libelle.split("\\|");
                        if (libelleValues.length >= 3) {
                            String checkIdDetailFamille = libelleValues[1];
                            // ----------------------------------------------------------------------------------
                            // We find it !
                            // ----------------------------------------------------------------------------------
                            if (checkIdDetailFamille.equals(idDetailFamille)) {
                                String idJournalisation = dossier.getIdJournalisation();
                                return idJournalisation;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#checkRappelInProgress ()
     */
    @Override
    public String checkRappelInProgress(String idDetailFamille, String csFormule) {
        // To Find :
        // ------------------------------------------------------------------
        // Libra Journalisation : 32000016 CS_JO_JOURNALISATION
        // Complement Journalisation : 32000010 CS_JO_FMT_FORMULE_RAPPEL
        // Libellé : contient | et [1] == idDetailFamille et [2] == idFormule
        // ------------------------------------------------------------------

        // ------------------------------------------------------------------
        // Récupération du dossier LIBRA
        // ------------------------------------------------------------------
        String forIdTiers = null;
        String forIdDossier = null;
        String forIdDomaine = null;
        String forIdExterne = null;

        try {
            SimpleDetailFamille detailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                    idDetailFamille);
            SimpleFamille famille = AmalImplServiceLocator.getSimpleFamilleService().read(detailFamille.getIdFamille());
            forIdTiers = famille.getIdTier();
            forIdExterne = famille.getIdFamille();

            // Find the domaine ID
            LIDomainesManager domaineManager = new LIDomainesManager();
            domaineManager.setForCsDomaine(ILIConstantesExternes.CS_DOMAINE_AMAL);
            domaineManager.find();
            for (int iDomaine = 0; iDomaine < domaineManager.getSize(); iDomaine++) {
                LIDomaines domaines = (LIDomaines) domaineManager.getEntity(iDomaine);
                forIdDomaine = domaines.getIdDomaine();
            }

            // Find the dossier
            LIDossiersJointTiersManager dossierManager = new LIDossiersJointTiersManager();
            dossierManager.setForIdDomaine(forIdDomaine);
            dossierManager.setForIdExterne(forIdExterne);
            dossierManager.setForIdTiers(forIdTiers);
            dossierManager.find();
            if (dossierManager.size() > 1) {
                JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records (" + dossierManager.size()
                        + ") found with id " + forIdTiers);
                return null;
            }
            for (int iDossier = 0; iDossier < dossierManager.getSize(); iDossier++) {
                LIDossiersJointTiers dossier = (LIDossiersJointTiers) dossierManager.get(iDossier);
                forIdDossier = dossier.getIdDossier();
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in rollbackRappelInProgressToRappel :" + e.toString());
        }

        // --------------------------------------------------
        // Cherchons des dossiers selon critère susmentionnés
        // --------------------------------------------------
        LIJournalisationsJointDossiersManager managerDossier = new LIJournalisationsJointDossiersManager();
        managerDossier.setForCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        managerDossier.setForValeurCodeSysteme(JOConstantes.CS_JO_FMT_FORMULE_RAPPEL);
        managerDossier.changeManagerSize(BManager.SIZE_NOLIMIT);
        managerDossier.setJointureFichier(1);
        if (forIdDossier != null) {
            managerDossier.setForIdDossier(forIdDossier);
        }
        if ((forIdTiers != null) && (forIdTiers.length() > 0)) {
            managerDossier.setForIdTiers(forIdTiers);
        }
        try {
            managerDossier.find();
            for (int iDossier = 0; iDossier < managerDossier.size(); iDossier++) {
                LIJournalisationsJointDossiers dossier = (LIJournalisationsJointDossiers) managerDossier.get(iDossier);
                // Controle du libelle
                // doit être du format blabla | idDetailFamille | csFormule | status
                // ------------------------------------------------------------------
                String libelle = dossier.getLibelle();
                if ((libelle != null) && (libelle.length() > 0)) {
                    if (libelle.contains("|")) {
                        String[] libelleValues = libelle.split("\\|");
                        if (libelleValues.length >= 3) {
                            String checkIdDetailFamille = libelleValues[1];
                            String checkCsFormule = libelleValues[2];
                            // ----------------------------------------------------------------------------------
                            // We find it !
                            // ----------------------------------------------------------------------------------
                            if (checkIdDetailFamille.equals(idDetailFamille) && checkCsFormule.equals(csFormule)) {
                                String idJournalisation = dossier.getIdJournalisation();
                                return idJournalisation;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#deleteRappel()
     */
    @Override
    public void deleteRappel(String idAsDateRappel) {
        HashMap<String, ArrayList<AMControleurRappelDetail>> fullRappels = this.getLIBRARappels();

        // idAsDateRappel format : yyyyMMdd
        // key : dd.MM.yyyy

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date dateRappel = sdf.parse(idAsDateRappel);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
            String csKey = sdf2.format(dateRappel);
            ArrayList<AMControleurRappelDetail> libraRappels = fullRappels.get(csKey);
            for (int iLibra = 0; iLibra < libraRappels.size(); iLibra++) {
                AMControleurRappelDetail currentLibraRappel = libraRappels.get(iLibra);
                deleteRappelLibra(currentLibraRappel.getIdJournalisationLibra());
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error getting rappels for key : " + idAsDateRappel);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#deleteRappelLibra
     */
    @Override
    public void deleteRappelLibra(String idLibraJournalisation) {
        JOJournalisationManager managerJournalisation = new JOJournalisationManager();
        managerJournalisation.setForIdJournalisation(idLibraJournalisation);
        try {
            managerJournalisation.find();
            if (managerJournalisation.size() > 1) {
                JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records (" + managerJournalisation.size()
                        + ") found with id " + idLibraJournalisation);
                return;
            }
            for (int iJournalisation = 0; iJournalisation < managerJournalisation.size(); iJournalisation++) {
                // -------------------------------------------------------------------------------------------
                // Mise à jour journalisation (JOJPJOU) avec status journalisation et libellé cancelled
                // -------------------------------------------------------------------------------------------
                JOJournalisation journalisation = (JOJournalisation) managerJournalisation.get(iJournalisation);
                String[] libelles = journalisation.getLibelle().split("\\|");
                if (libelles.length > 1) {
                    journalisation.setLibelle(libelles[0] + " - CANCELLED");
                }
                journalisation.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
                journalisation.update();
                // -------------------------------------------------------------------------------------------
                // Mise à jour complement journal (JOJPCJO) avec type de contrôle manuel
                // -------------------------------------------------------------------------------------------
                JOComplementJournalManager managerComplement = new JOComplementJournalManager();
                managerComplement.setForIdJournalisation(idLibraJournalisation);
                try {
                    managerComplement.find();
                    if (managerComplement.size() > 1) {
                        JadeLogger.error(this,
                                "Complement Journal (JOJPCJO) >> Many records (" + managerComplement.size()
                                        + ") found with id " + idLibraJournalisation);
                        return;
                    }
                    for (int iComplement = 0; iComplement < managerComplement.size(); iComplement++) {
                        JOComplementJournal complement = (JOComplementJournal) managerComplement.get(iComplement);
                        complement.setValeurCodeSysteme(JOConstantes.CS_JO_FMT_MANUELLE);
                        complement.update();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error updating LIBRA in deleteRappelLibra :" + e.toString());
                }
                // -------------------------------------------------------------------------------------------
                // Mise à jour groupe journal (JOJPGJO) avec date de rappel à 0 et date de réception à aujourd'hui
                // -------------------------------------------------------------------------------------------
                JOGroupeJournalManager managerGroupe = new JOGroupeJournalManager();
                managerGroupe.setForIdGroupeJournal(journalisation.getIdGroupeJournal());
                try {
                    managerGroupe.find();
                    if (managerGroupe.size() > 1) {
                        JadeLogger.error(this, "Complement Journal (JOJPGJO) >> Many records (" + managerGroupe.size()
                                + ") found with id " + idLibraJournalisation);
                        return;
                    }
                    for (int iGroupe = 0; iGroupe < managerGroupe.size(); iGroupe++) {
                        JOGroupeJournal groupe = (JOGroupeJournal) managerGroupe.get(iGroupe);
                        // Récupération de la date du jour
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String csDateDuJour = sdf.format(date);
                        groupe.setDateReception(csDateDuJour);
                        groupe.setDateRappel("0");
                        groupe.update();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error updating LIBRA in deleteRappelLibra :" + e.toString());
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in deleteRappelLibra :" + e.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#generateRappel()
     */
    @Override
    public void generateRappel(String idAsDateRappel) {
        HashMap<String, ArrayList<AMControleurRappelDetail>> fullRappels = this.getLIBRARappels();

        // idAsDateRappel format : 0 à x

        try {
            /*
             * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
             * Date dateRappel = sdf.parse(idAsDateRappel);
             * SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
             * String csKey = sdf2.format(dateRappel);
             * ArrayList<AMControleurRappelDetail> libraRappels = fullRappels.get(csKey);
             */
            List<String> allKeys = new ArrayList<String>(fullRappels.keySet());
            Collections.sort(allKeys);
            for (int iKey = 0; iKey < allKeys.size(); iKey++) {
                if (Integer.toString(iKey).equals(idAsDateRappel)) {
                    String currentKey = (String) allKeys.toArray()[iKey];

                    ArrayList<AMControleurRappelDetail> libraRappels = fullRappels.get(currentKey);
                    for (int iLibra = 0; iLibra < libraRappels.size(); iLibra++) {
                        AMControleurRappelDetail currentLibraRappel = libraRappels.get(iLibra);
                        generateRappelLibra(currentLibraRappel.getIdJournalisationLibra());
                    }
                }
            }

        } catch (Exception ex) {
            JadeLogger.error(this, "Error getting rappels for key : " + idAsDateRappel);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#generateRappelLibra()
     */
    @Override
    public void generateRappelLibra(String idLibraJournalisation) {
        JOJournalisationManager managerJournalisation = new JOJournalisationManager();
        managerJournalisation.setForIdJournalisation(idLibraJournalisation);
        try {
            managerJournalisation.find();
            if (managerJournalisation.size() > 1) {
                JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records (" + managerJournalisation.size()
                        + ") found with id " + idLibraJournalisation);
                return;
            }
            for (int iJournalisation = 0; iJournalisation < managerJournalisation.size(); iJournalisation++) {
                // -------------------------------------------------------------------------------------------
                // Mise à jour journalisation (JOJPJOU) avec status journalisation et libellé cancelled
                // -------------------------------------------------------------------------------------------
                JOJournalisation journalisation = (JOJournalisation) managerJournalisation.get(iJournalisation);
                journalisation.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
                journalisation.update();
                // -------------------------------------------------------------------------------------------
                // Mise à jour complement journal (JOJPCJO) avec type formule-rappel
                // -------------------------------------------------------------------------------------------
                JOComplementJournalManager managerComplement = new JOComplementJournalManager();
                managerComplement.setForIdJournalisation(idLibraJournalisation);
                try {
                    managerComplement.find();
                    if (managerComplement.size() > 1) {
                        JadeLogger.error(this,
                                "Complement Journal (JOJPCJO) >> Many records (" + managerComplement.size()
                                        + ") found with id " + idLibraJournalisation);
                        return;
                    }
                    for (int iComplement = 0; iComplement < managerComplement.size(); iComplement++) {
                        JOComplementJournal complement = (JOComplementJournal) managerComplement.get(iComplement);
                        complement.setValeurCodeSysteme(JOConstantes.CS_JO_FMT_FORMULE_RAPPEL);
                        complement.update();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error updating LIBRA in deleteRappelLibra :" + e.toString());
                }
                // -------------------------------------------------------------------------------------------
                // Mise à jour des tables MAENVDOC, MACTLSTS, MACTLJOB
                // -------------------------------------------------------------------------------------------
                // Attente d'un libellé : blabla | idDetailFamille | csFormule | csStatus
                if (journalisation.getLibelle().split("\\|").length >= 3) {
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
                    String csDateComplete = sdf.format(date);
                    String csModele = journalisation.getLibelle().split("\\|")[2];
                    String idDetailFamille = journalisation.getLibelle().split("\\|")[1];
                    String csJobType = IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue();
                    AmalServiceLocator.getDetailFamilleService().writeInJobTable(csDateComplete, csModele, csJobType,
                            idDetailFamille, 0);
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in deleteRappelLibra :" + e.toString());
        }
    }

    /**
     * @param id
     *            ID du code système
     * 
     * @return libelle général du code système correspondant
     * 
     */
    private String getDocumentLibelle(String csCodeUser, BSession session) {
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        String csRetour = "";

        cm.setSession(session);
        cm.setForCodeUtilisateur(csCodeUser);
        cm.setForIdGroupe("AMMODELES");
        cm.setForIdLangue(session.getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
            return csRetour;
        }
        JAVector containerCS = cm.getContainer();
        if ((containerCS == null) || (containerCS.size() > 1)) {
            return csRetour;
        }
        for (Iterator it = containerCS.iterator(); it.hasNext();) {
            FWParametersCode code = (FWParametersCode) it.next();
            // Création de la string visible dans la combobox
            String documentValeur = code.getLibelle().trim();

            // String documentCodeUtilisateur = code.getCurrentCodeUtilisateur().getCodeUtilisateur().trim();
            // String csCodeLibelle = code.getCurrentCodeUtilisateur().getLibelle().trim();

            String csCodeLibelle = code.getCodeUtilisateur(session.getIdLangue()).getLibelle().trim();
            // csRetour += documentCodeUtilisateur + " - ";
            // csRetour += documentValeur + " - ";
            // csRetour += csCodeLibelle;
            csRetour = documentValeur;
        }
        return csRetour;
    }

    /**
     * Récupération du code système de la formule de rappel en fonction du code système de la formule de base
     * 
     * @param noModeleSubside
     * @return
     */
    private String getFormuleRappel(String noModeleSubside) {
        try {
            // récupération de la formule de rappel
            // ---------------------------------------------------------------------
            if (!noModeleSubside.equals("")) {
                // --------------------------------------------------------------------
                // Récupération des informations de la formule courante
                // --------------------------------------------------------------------
                FormuleListSearch currentFormuleSearch = new FormuleListSearch();
                currentFormuleSearch.setForlibelle(noModeleSubside);
                try {
                    currentFormuleSearch = ENServiceLocator.getFormuleListService().search(currentFormuleSearch);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    currentFormuleSearch = null;
                }
                // --------------------------------------------------------------------
                // Récupération des informations du rappel lié à la formule courante
                // --------------------------------------------------------------------
                if ((currentFormuleSearch != null) && (currentFormuleSearch.getSize() > 0)) {
                    FormuleList currentFormule = (FormuleList) currentFormuleSearch.getSearchResults()[0];
                    SimpleRappel currentRappel = currentFormule.getRappel();
                    // --------------------------------------------------------------------
                    // rappel trouvé, récupération de la formule du RAPPEL
                    // --------------------------------------------------------------------
                    if ((currentRappel.getIdDefinitionFormule() != null)
                            && !"".equals(currentRappel.getIdDefinitionFormule())) {

                        // Récupération de la formule de rappel
                        FormuleListSearch formuleSearch = new FormuleListSearch();
                        formuleSearch.setForIdDefinitionFormule(currentRappel.getIdDefinitionFormule());
                        try {
                            formuleSearch = ENServiceLocator.getFormuleListService().search(formuleSearch);
                        } catch (Exception e) {
                            e.printStackTrace();
                            formuleSearch = null;
                        }
                        if ((formuleSearch != null) && (formuleSearch.getSize() > 0)) {
                            FormuleList formule = (FormuleList) formuleSearch.getSearchResults()[0];
                            String rappelCSFormule = formule.getDefinitionformule().getCsDocument();
                            return rappelCSFormule;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#getFormulesRappel()
     */
    @Override
    public ArrayList<String> getFormulesRappel() {

        ArrayList<String> formulesRappel = new ArrayList<String>();

        ParametreModelComplexSearch modelSearch = new ParametreModelComplexSearch();
        modelSearch.setWhereKey("basic");
        modelSearch.setDefinedSearchSize(0);
        try {
            modelSearch = AmalServiceLocator.getParametreModelService().search(modelSearch);

            for (int iModel = 0; iModel < modelSearch.getSize(); iModel++) {
                ParametreModelComplex currentModel = (ParametreModelComplex) modelSearch.getSearchResults()[iModel];
                String idDefinitionFormule = currentModel.getFormuleList().getRappel().getIdDefinitionFormule();
                if ((idDefinitionFormule != null) && (idDefinitionFormule.length() > 0)) {
                    FormuleListSearch formuleListRappelSearch = new FormuleListSearch();
                    formuleListRappelSearch.setForIdDefinitionFormule(idDefinitionFormule);
                    formuleListRappelSearch = ENServiceLocator.getFormuleListService().search(formuleListRappelSearch);
                    for (int iFormuleRappel = 0; iFormuleRappel < formuleListRappelSearch.getSize(); iFormuleRappel++) {
                        FormuleList formuleRappel = (FormuleList) formuleListRappelSearch.getSearchResults()[iFormuleRappel];
                        // Get it
                        // --------------------------------
                        String csFormule = formuleRappel.getCsDocument();
                        if ((csFormule != null) && (csFormule.length() > 0)) {
                            if (!formulesRappel.contains(csFormule)) {
                                formulesRappel.add(csFormule);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formulesRappel;
    }

    /**
     * Récupération du couple formule - formule de rappel
     * 
     * @return
     */
    private HashMap<String, String> getFormulesRappelByFormule() {

        HashMap<String, String> formulesRappel = new HashMap<String, String>();

        ParametreModelComplexSearch modelSearch = new ParametreModelComplexSearch();
        modelSearch.setWhereKey("basic");
        modelSearch.setDefinedSearchSize(0);
        try {
            modelSearch = AmalServiceLocator.getParametreModelService().search(modelSearch);

            for (int iModel = 0; iModel < modelSearch.getSize(); iModel++) {
                ParametreModelComplex currentModel = (ParametreModelComplex) modelSearch.getSearchResults()[iModel];
                String idDefinitionFormule = currentModel.getFormuleList().getRappel().getIdDefinitionFormule();
                if ((idDefinitionFormule != null) && (idDefinitionFormule.length() > 0)) {
                    FormuleListSearch formuleListRappelSearch = new FormuleListSearch();
                    formuleListRappelSearch.setForIdDefinitionFormule(idDefinitionFormule);
                    formuleListRappelSearch = ENServiceLocator.getFormuleListService().search(formuleListRappelSearch);
                    for (int iFormuleRappel = 0; iFormuleRappel < formuleListRappelSearch.getSize(); iFormuleRappel++) {
                        FormuleList formuleRappel = (FormuleList) formuleListRappelSearch.getSearchResults()[iFormuleRappel];
                        // Get it
                        // --------------------------------
                        String csFormule = formuleRappel.getCsDocument();
                        if ((csFormule != null) && (csFormule.length() > 0)) {
                            formulesRappel.put(currentModel.getSimpleParametreModel().getCodeSystemeFormule(),
                                    csFormule);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return formulesRappel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#getLIBRARappel(String
     * idDetailFamille) ()
     */
    @Override
    public String getLIBRARappel(String idDetailFamille) {

        HashMap<String, ArrayList<AMControleurRappelDetail>> results = this.getLIBRARappels();
        if (results != null) {
            Set<String> allKeys = results.keySet();
            for (int iKey = 0; iKey < allKeys.size(); iKey++) {
                String currentKey = (String) allKeys.toArray()[iKey];
                ArrayList<AMControleurRappelDetail> values = results.get(currentKey);
                for (int iValue = 0; iValue < values.size(); iValue++) {
                    AMControleurRappelDetail currentDetail = values.get(iValue);
                    if (currentDetail.getDetailFamille().getIdDetailFamille().equals(idDetailFamille)) {
                        return currentDetail.getIdJournalisationLibra();
                    }
                }
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#getLIBRARappels()
     */
    @Override
    public HashMap<String, ArrayList<AMControleurRappelDetail>> getLIBRARappels() {
        return this.getLIBRARappels("");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#getLIBRARappels(java.lang.String
     * )
     */
    @Override
    public HashMap<String, ArrayList<AMControleurRappelDetail>> getLIBRARappels(String dateRequested) {

        Date dateToday = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String csDateToday = sdf.format(dateToday);

        // Création de la HashMap <date rappel, object detail>
        HashMap<String, ArrayList<AMControleurRappelDetail>> rappelDetails = new HashMap<String, ArrayList<AMControleurRappelDetail>>();

        // Récupération des formules de rappels en fonction des formules standards
        // Key formule standard, value : formule de rappel
        HashMap<String, String> formulesRappel = getFormulesRappelByFormule();

        // Cherchons pour le domaine AMAL les échéances enregistrées
        // ---------------------------------------------------------
        LIDomainesManager domaineManager = new LIDomainesManager();
        domaineManager.setForCsDomaine(ILIConstantesExternes.CS_DOMAINE_AMAL);
        try {
            domaineManager.find();
            for (int iDomaine = 0; iDomaine < domaineManager.getSize(); iDomaine++) {
                LIDomaines domaines = (LIDomaines) domaineManager.getEntity(iDomaine);

                LIEcheancesJointDossiersManager managerEcheance = new LIEcheancesJointDossiersManager();
                managerEcheance.setForIdDomaine(domaines.getId());

                if (!JadeStringUtil.isEmpty(dateRequested)) {
                    // managerEcheance.setForDateFin(csDateToday);
                }
                try {
                    // par défaut le find prend en compte uniquement les rappels (surprise)
                    managerEcheance.changeManagerSize(BManager.SIZE_NOLIMIT);
                    managerEcheance.find();
                    for (int iElement = 0; iElement < managerEcheance.getSize(); iElement++) {
                        LIJournalisationsJointDossiers currentEcheance = (LIJournalisationsJointDossiers) managerEcheance
                                .getEntity(iElement);
                        // Travail sur le libelle
                        // String LAMAL attendue : 15-ACREPC2-Opposition|26561|42000013|0
                        // Signification : blablablablablablabla|idDetailFamille|csFormule|envoiStatus
                        String libelle = currentEcheance.getLibelle();
                        // l'espace du juste si pas de libellé
                        libelle = " " + libelle;
                        String idDetailFamille = "";
                        String csFormule = "";
                        String idJournalisation = currentEcheance.getIdJournalisation();
                        String[] extractedInfos = libelle.split("\\|");

                        if ((extractedInfos.length > 3) && (currentEcheance.getDateRappel() != null)
                                && (currentEcheance.getDateRappel().length() >= 8)) {
                            idDetailFamille = extractedInfos[1];
                            csFormule = extractedInfos[2];
                            SimpleDetailFamille detailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                                    .read(idDetailFamille);
                            SimpleFamille famille = null;
                            String libelleFormuleRappel = "";
                            if (detailFamille.getIdFamille() == null) {
                                // Récupération du code utilisateur
                                BSession currentSession = BSessionUtil.getSessionFromThreadContext();
                                String codeUtilisateur = currentSession.getCode(csFormule);
                                String document = getDocumentLibelle(codeUtilisateur, currentSession);
                                libelleFormuleRappel = currentEcheance.getNom() + ", " + currentEcheance.getPrenom()
                                        + " - " + document;
                            } else {
                                famille = AmalImplServiceLocator.getSimpleFamilleService().read(
                                        detailFamille.getIdFamille());
                                libelleFormuleRappel = extractedInfos[0].trim();
                            }
                            // Create currentDetail object
                            AMControleurRappelDetail currentDetail = new AMControleurRappelDetail();
                            currentDetail.setCsFormuleRappel(csFormule);
                            currentDetail.setDateRappel(currentEcheance.getDateRappel());
                            currentDetail.setDetailFamille(detailFamille);
                            currentDetail.setIdJournalisationLibra(idJournalisation);
                            currentDetail.setLibelleFormuleRappel(libelleFormuleRappel);
                            currentDetail.setSimpleFamille(famille);

                            // Enregistrement
                            /*
                             * ArrayList<AMControleurRappelDetail> currentArray = rappelDetails.get(csDateToday);
                             * if (currentArray == null) {
                             * currentArray = new ArrayList<AMControleurRappelDetail>();
                             * }
                             * currentArray.add(currentDetail);
                             * rappelDetails.put(csDateToday, currentArray);
                             */
                            boolean bCheckRappel = false;
                            if (famille != null) {
                                // Check dernier document envoyé du subside et son rappel, si différent du rappel à
                                // faire
                                String supposedRappel = formulesRappel.get(detailFamille.getNoModeles());
                                // Récupération du code utilisateur
                                BSession currentSession = BSessionUtil.getSessionFromThreadContext();
                                String codeUtilisateur = currentSession.getCode(detailFamille.getNoModeles());
                                String document = getDocumentLibelle(codeUtilisateur, currentSession);
                                if ((supposedRappel != null) && supposedRappel.equals(csFormule)) {
                                    // OK, le rappel doit être créé
                                    bCheckRappel = false;
                                    currentDetail.setLibelleFormuleRappel(currentEcheance.getDate() + " - "
                                            + libelleFormuleRappel + " - " + document);
                                } else {
                                    // NOK, le rappel devrait être supprimé
                                    bCheckRappel = true;
                                    currentDetail.setLibelleFormuleRappel(currentEcheance.getDate()
                                            + " - Dernier document envoyé : " + document);
                                }
                            }
                            if ((famille != null) && (bCheckRappel == false)) {
                                ArrayList<AMControleurRappelDetail> currentArray = rappelDetails
                                        .get(libelleFormuleRappel);
                                if (currentArray == null) {
                                    currentArray = new ArrayList<AMControleurRappelDetail>();
                                }
                                currentArray.add(currentDetail);
                                rappelDetails.put(libelleFormuleRappel, currentArray);
                            } else {
                                ArrayList<AMControleurRappelDetail> currentArray = rappelDetails.get("Others");
                                if (currentArray == null) {
                                    currentArray = new ArrayList<AMControleurRappelDetail>();
                                }
                                currentArray.add(currentDetail);
                                rappelDetails.put("Others", currentArray);
                            }
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // DEBUG, list all Others id
        List<AMControleurRappelDetail> allOthers = rappelDetails.get("Others");
        for (int iOther = 0; iOther < allOthers.size(); iOther++) {
            AMControleurRappelDetail currentOther = allOthers.get(iOther);
            System.out.println(currentOther.getIdJournalisationLibra());
        }

        return rappelDetails;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#rollbackRappelInProgressToRappel
     * ()
     */
    @Override
    public void rollbackRappelInProgressToRappel(String idDetailFamille, String csFormule) {
        // To Find :
        // ------------------------------------------------------------------
        // Libra Journalisation : 32000016 CS_JO_JOURNALISATION
        // Complement Journalisation : 32000010 CS_JO_FMT_FORMULE_RAPPEL
        // Libellé : contient | et [1] == idDetailFamille et [2] == idFormule
        // ------------------------------------------------------------------
        // Changement de valeur to :
        // Libra Journalisation : 32000017 CS_JO_RAPPEL
        // Complement Journalisation : 32000110 CS_JO_AVS_FMT_RAPPEL
        //

        // ------------------------------------------------------------------
        // Récupération du dossier LIBRA
        // ------------------------------------------------------------------
        String forIdTiers = null;
        String forIdDossier = null;
        String forIdDomaine = null;
        String forIdExterne = null;

        try {
            SimpleDetailFamille detailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                    idDetailFamille);
            SimpleFamille famille = AmalImplServiceLocator.getSimpleFamilleService().read(detailFamille.getIdFamille());
            forIdTiers = famille.getIdTier();
            forIdExterne = famille.getIdFamille();

            // Find the domaine ID
            LIDomainesManager domaineManager = new LIDomainesManager();
            domaineManager.setForCsDomaine(ILIConstantesExternes.CS_DOMAINE_AMAL);
            domaineManager.find();
            for (int iDomaine = 0; iDomaine < domaineManager.getSize(); iDomaine++) {
                LIDomaines domaines = (LIDomaines) domaineManager.getEntity(iDomaine);
                forIdDomaine = domaines.getIdDomaine();
            }

            // Find the dossier
            LIDossiersJointTiersManager dossierManager = new LIDossiersJointTiersManager();
            dossierManager.setForIdDomaine(forIdDomaine);
            dossierManager.setForIdExterne(forIdExterne);
            dossierManager.setForIdTiers(forIdTiers);
            dossierManager.find();
            if (dossierManager.size() > 1) {
                JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records (" + dossierManager.size()
                        + ") found with id " + forIdTiers);
                return;
            }
            for (int iDossier = 0; iDossier < dossierManager.getSize(); iDossier++) {
                LIDossiersJointTiers dossier = (LIDossiersJointTiers) dossierManager.get(iDossier);
                forIdDossier = dossier.getIdDossier();
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in rollbackRappelInProgressToRappel :" + e.toString());
        }

        // --------------------------------------------------------------------
        // Cherchons les journalisation selon critère susmentionnés
        // --------------------------------------------------------------------
        LIJournalisationsJointDossiersManager managerDossier = new LIJournalisationsJointDossiersManager();
        managerDossier.setForCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        managerDossier.setForValeurCodeSysteme(JOConstantes.CS_JO_FMT_FORMULE_RAPPEL);
        managerDossier.changeManagerSize(BManager.SIZE_NOLIMIT);
        managerDossier.setJointureFichier(1);
        if (forIdDossier != null) {
            managerDossier.setForIdDossier(forIdDossier);
        }
        if ((forIdTiers != null) && (forIdTiers.length() > 0)) {
            managerDossier.setForIdTiers(forIdTiers);
        }
        try {
            managerDossier.find();
            for (int iDossier = 0; iDossier < managerDossier.size(); iDossier++) {
                LIJournalisationsJointDossiers dossier = (LIJournalisationsJointDossiers) managerDossier.get(iDossier);
                // Controle du libelle
                // doit être du format blabla | idDetailFamille | csFormule | status
                // ------------------------------------------------------------------
                String libelle = dossier.getLibelle();
                if ((libelle != null) && (libelle.length() > 0)) {
                    if (libelle.contains("|")) {
                        String[] libelleValues = libelle.split("\\|");
                        if (libelleValues.length >= 3) {
                            String checkIdDetailFamille = libelleValues[1];
                            String checkCsFormule = libelleValues[2];
                            // ----------------------------------------------------------------------------------
                            // We find it !
                            // ----------------------------------------------------------------------------------
                            if (checkIdDetailFamille.equals(idDetailFamille) && checkCsFormule.equals(csFormule)) {
                                String idJournalisation = dossier.getIdJournalisation();
                                // String idComplementJournal = dossier.getIdComplementJournal();

                                JOJournalisationManager managerJournalisation = new JOJournalisationManager();
                                managerJournalisation.setForIdJournalisation(idJournalisation);
                                try {
                                    managerJournalisation.find();
                                    if (managerJournalisation.size() > 1) {
                                        JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records ("
                                                + managerJournalisation.size() + ") found with id " + idJournalisation);
                                        return;
                                    }
                                    for (int iJournalisation = 0; iJournalisation < managerJournalisation.size(); iJournalisation++) {
                                        // -------------------------------------------------------------------------------------------
                                        // Mise à jour journalisation (JOJPJOU) avec status RAPPEL
                                        // -------------------------------------------------------------------------------------------
                                        JOJournalisation journalisation = (JOJournalisation) managerJournalisation
                                                .get(iJournalisation);
                                        journalisation.setCsTypeJournal(JOConstantes.CS_JO_RAPPEL);
                                        journalisation.update();
                                        // -------------------------------------------------------------------------------------------
                                        // Mise à jour complement journal (JOJPCJO) avec type
                                        // CS_JO_AVS_FMT_RAPPEL
                                        // -------------------------------------------------------------------------------------------
                                        JOComplementJournalManager managerComplement = new JOComplementJournalManager();
                                        // managerComplement.setForIdComplementJournal(idComplementJournal);
                                        managerComplement.setForIdJournalisation(idJournalisation);
                                        try {
                                            managerComplement.find();
                                            if (managerComplement.size() > 1) {
                                                JadeLogger.error(this, "Complement Journal (JOJPCJO) >> Many records ("
                                                        + managerComplement.size() + ") found with id "
                                                        + idJournalisation);
                                                return;
                                            }
                                            for (int iComplement = 0; iComplement < managerComplement.size(); iComplement++) {
                                                JOComplementJournal complement = (JOComplementJournal) managerComplement
                                                        .get(iComplement);
                                                complement.setValeurCodeSysteme(JOConstantes.CS_JO_AVS_FMT_RAPPEL);
                                                complement.update();
                                            }
                                        } catch (Exception e) {
                                            JadeLogger.error(
                                                    this,
                                                    "Error updating LIBRA in rollbackRappelInProgressToRappel :"
                                                            + e.toString());
                                        }
                                    }
                                } catch (Exception e) {
                                    JadeLogger.error(this, "Error updating LIBRA in rollbackRappelInProgressToRappel :"
                                            + e.toString());
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in rollbackRappelInProgressToRappel :" + e.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#updateRappelLibraRappelGenerated
     * ()
     */
    @Override
    public void updateRappelLibraRappelGenerated(String idJournalisation) {
        // le rappel courant a été généré
        // Journalisation : 32000016 CS_JO_JOURNALISATION
        // Complement journal : 32000110 CS_JO_AVS_FMT_RAPPEL
        // groupe journal : date rappel à 0, date réception à today

        JOJournalisationManager managerJournalisation = new JOJournalisationManager();
        managerJournalisation.setForIdJournalisation(idJournalisation);
        try {
            managerJournalisation.find();
            if (managerJournalisation.size() > 1) {
                JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records (" + managerJournalisation.size()
                        + ") found with id " + idJournalisation);
                return;
            }
            for (int iJournalisation = 0; iJournalisation < managerJournalisation.size(); iJournalisation++) {
                // -------------------------------------------------------------------------------------------
                // Mise à jour journalisation (JOJPJOU) avec status journalisation
                // -------------------------------------------------------------------------------------------
                JOJournalisation journalisation = (JOJournalisation) managerJournalisation.get(iJournalisation);
                journalisation.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
                journalisation.update();
                // -------------------------------------------------------------------------------------------
                // Mise à jour complement journal (JOJPCJO) avec type rappel
                // -------------------------------------------------------------------------------------------
                JOComplementJournalManager managerComplement = new JOComplementJournalManager();
                managerComplement.setForIdJournalisation(idJournalisation);
                try {
                    managerComplement.find();
                    if (managerComplement.size() > 1) {
                        JadeLogger.error(this,
                                "Complement Journal (JOJPCJO) >> Many records (" + managerComplement.size()
                                        + ") found with id " + idJournalisation);
                        return;
                    }
                    for (int iComplement = 0; iComplement < managerComplement.size(); iComplement++) {
                        JOComplementJournal complement = (JOComplementJournal) managerComplement.get(iComplement);
                        complement.setValeurCodeSysteme(JOConstantes.CS_JO_AVS_FMT_RAPPEL);
                        complement.update();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error updating LIBRA in updateRappelLibraRappelGenerated :" + e.toString());
                }
                // -------------------------------------------------------------------------------------------
                // Mise à jour groupe journal (JOJPGJO) avec date de rappel à 0 et date de réception à aujourd'hui
                // -------------------------------------------------------------------------------------------
                JOGroupeJournalManager managerGroupe = new JOGroupeJournalManager();
                managerGroupe.setForIdGroupeJournal(journalisation.getIdGroupeJournal());
                try {
                    managerGroupe.find();
                    if (managerGroupe.size() > 1) {
                        JadeLogger.error(this, "Complement Journal (JOJPGJO) >> Many records (" + managerGroupe.size()
                                + ") found with id " + idJournalisation);
                        return;
                    }
                    for (int iGroupe = 0; iGroupe < managerGroupe.size(); iGroupe++) {
                        JOGroupeJournal groupe = (JOGroupeJournal) managerGroupe.get(iGroupe);
                        // Récupération de la date du jour
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String csDateDuJour = sdf.format(date);
                        groupe.setDateReception(csDateDuJour);
                        groupe.setDateRappel("0");
                        groupe.update();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error updating LIBRA in updateRappelLibraRappelGenerated :" + e.toString());
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in updateRappelLibraRappelGenerated :" + e.toString());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurRappel.ControleurRappelService#updateRappelLibraReceptionManuelle
     * ()
     */
    @Override
    public void updateRappelLibraReceptionManuelle(String idJournalisation) {
        // le rappel n'a pas été généré, nous avons reçu les documents demandés
        // Journalisation : 32000016 CS_JO_JOURNALISATION
        // complement journal : 32000086 CS_JO_FMT_MANUELLE_RECEPTION
        // groupe journal : date rappel à 0, date réception à today
        JOJournalisationManager managerJournalisation = new JOJournalisationManager();
        managerJournalisation.setForIdJournalisation(idJournalisation);
        try {
            managerJournalisation.find();
            if (managerJournalisation.size() > 1) {
                JadeLogger.error(this, "Journalisation (JOJPJOU) >> Many records (" + managerJournalisation.size()
                        + ") found with id " + idJournalisation);
                return;
            }
            for (int iJournalisation = 0; iJournalisation < managerJournalisation.size(); iJournalisation++) {
                // -------------------------------------------------------------------------------------------
                // Mise à jour journalisation (JOJPJOU) avec status journalisation
                // -------------------------------------------------------------------------------------------
                JOJournalisation journalisation = (JOJournalisation) managerJournalisation.get(iJournalisation);
                journalisation.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
                journalisation.update();
                // -------------------------------------------------------------------------------------------
                // Mise à jour complement journal (JOJPCJO) avec type rappel
                // -------------------------------------------------------------------------------------------
                JOComplementJournalManager managerComplement = new JOComplementJournalManager();
                managerComplement.setForIdJournalisation(idJournalisation);
                try {
                    managerComplement.find();
                    if (managerComplement.size() > 1) {
                        JadeLogger.error(this,
                                "Complement Journal (JOJPCJO) >> Many records (" + managerComplement.size()
                                        + ") found with id " + idJournalisation);
                        return;
                    }
                    for (int iComplement = 0; iComplement < managerComplement.size(); iComplement++) {
                        JOComplementJournal complement = (JOComplementJournal) managerComplement.get(iComplement);
                        complement.setValeurCodeSysteme(JOConstantes.CS_JO_FMT_MANUELLE_RECEPTION);
                        complement.update();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error updating LIBRA in updateRappelLibraRappelGenerated :" + e.toString());
                }
                // -------------------------------------------------------------------------------------------
                // Mise à jour groupe journal (JOJPGJO) avec date de rappel à 0 et date de réception à aujourd'hui
                // -------------------------------------------------------------------------------------------
                JOGroupeJournalManager managerGroupe = new JOGroupeJournalManager();
                managerGroupe.setForIdGroupeJournal(journalisation.getIdGroupeJournal());
                try {
                    managerGroupe.find();
                    if (managerGroupe.size() > 1) {
                        JadeLogger.error(this, "Complement Journal (JOJPGJO) >> Many records (" + managerGroupe.size()
                                + ") found with id " + idJournalisation);
                        return;
                    }
                    for (int iGroupe = 0; iGroupe < managerGroupe.size(); iGroupe++) {
                        JOGroupeJournal groupe = (JOGroupeJournal) managerGroupe.get(iGroupe);
                        // Récupération de la date du jour
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        String csDateDuJour = sdf.format(date);
                        groupe.setDateReception(csDateDuJour);
                        groupe.setDateRappel("0");
                        groupe.update();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, "Error updating LIBRA in updateRappelLibraRappelGenerated :" + e.toString());
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error updating LIBRA in updateRappelLibraRappelGenerated :" + e.toString());
        }

    }
}
