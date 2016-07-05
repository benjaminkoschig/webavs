package globaz.campus.process.validationAnnonces;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.etudiants.GEEtudiants;
import globaz.campus.db.etudiants.GEEtudiantsManager;
import globaz.campus.util.GEUtil;
import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAdresseManager;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;

public class GECreationMiseajourDonnees {
    private int modeArrondiFadCotPers = 0;
    private boolean reutiliseAdresse = false;
    private BSession sessionCampus = null;
    private BSession sessionNaos = null;
    private BSession sessionPhenix = null;
    private BSession sessionPyxis = null;
    private BTransaction transaction = null;

    // Constructeurs
    public GECreationMiseajourDonnees(BSession session, BTransaction transactionLocal) {
        sessionCampus = session;
        transaction = transactionLocal;
    }

    // Création des adresses
    public void creationAdresses(GEAnnonces annonce, String idTiers, String annee, BProcess process) throws Exception {
        TIAvoirAdresseManager adresseLegaleMng = null;
        TIAvoirAdresse avoirAdresseLegale = null;
        TIAdresse adresseLegale = null;
        TIAvoirAdresse avoirAdresseEtudeDomicile = null;
        TIAvoirAdresse avoirAdresseEtudeCourrier = null;
        TIAdresse adresseEtude = null;
        TILocalite localite = null;
        /*
         * Création ou mise a jour de l'adresse de domicile avec l'adresse légale.
         */
        try {
            // Si il existe une adresse active et plus récente, ne pas mettre à jour l'adresse
            if (!existeAdresseActivePlusRecente(sessionPyxis, idTiers, annee, IConstantes.CS_AVOIR_ADRESSE_DOMICILE)) {
                if (!JadeStringUtil.isBlankOrZero(annonce.getNpaLegal())) {
                    // Recherche de la Localité
                    TILocaliteManager localiteMng = new TILocaliteManager();
                    localiteMng.setSession(sessionPyxis);
                    localiteMng.setInclureInactif(new Boolean(false));
                    if (JadeStringUtil.isBlankOrZero(annonce.getSuffixePostalLegal())) {
                        localiteMng.setForNumPostal(annonce.getNpaLegal() + "00");
                    } else {
                        localiteMng.setForNumPostal(annonce.getNpaLegal() + annonce.getSuffixePostalLegal());
                    }
                    localiteMng.setForIdPays(IConstantes.ID_PAYS_SUISSE);
                    localiteMng.find(transaction);
                    if (localiteMng.size() >= 1) {
                        localite = (TILocalite) localiteMng.getFirstEntity();
                    } else {
                        throw new Exception("Le NPA ne correspond à aucune localité dans les tiers.");
                    }
                    // Recherche s'il existe déjà une adresse de domicile
                    adresseLegaleMng = new TIAvoirAdresseManager();
                    adresseLegaleMng.setSession(sessionPyxis);
                    adresseLegaleMng.setForIdTiers(idTiers);
                    adresseLegaleMng.setForDateEntreDebutEtFin("01.01." + annee);
                    adresseLegaleMng.setForTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
                    adresseLegaleMng.find(transaction);

                    avoirAdresseEtudeDomicile = rechercheExistenceAdresseDomicile(annonce, avoirAdresseEtudeDomicile,
                            localite, idTiers, annee);

                    if (adresseLegaleMng.size() >= 1) {
                        // Mettre à jour l'adresse car déjà existante
                        if ((localite != null) && !localite.isNew()) {
                            adresseLegale = new TIAdresse();
                            adresseLegale.setSession(sessionPyxis);
                            adresseLegale.setIdLocalite(localite.getIdLocalite());
                            if ((annonce.getRueLegal().toUpperCase().indexOf("CASE POSTALE") != -1)
                                    || (annonce.getRueLegal().toUpperCase().indexOf("POSTFACH") != -1)
                                    || (annonce.getRueLegal().toUpperCase().indexOf("CASELLA POSTALE") != -1)) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Le champ rue de l'adresse de domicile comportait une case postale",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseLegale.setRue("");
                            } else {
                                adresseLegale.setRue(annonce.getRueLegal());
                            }
                            adresseLegale.setAttention(annonce.getAdresseLegale());
                            // Test de l'existance de l'adresse
                            String idAdresse = "";
                            if (reutiliseAdresse) {
                                TIAdresseManager adrMgr = new TIAdresseManager();
                                adrMgr.setSession(sessionPyxis);
                                adrMgr.setForLigneAdresse1N(adresseLegale.getLigneAdresse1());
                                adrMgr.setForLigneAdresse2(adresseLegale.getLigneAdresse2());
                                adrMgr.setForLigneAdresse3(adresseLegale.getLigneAdresse3());
                                adrMgr.setForLigneAdresse4(adresseLegale.getLigneAdresse4());
                                adrMgr.setForTitreAdresseN(adresseLegale.getTitreAdresse());
                                adrMgr.setForAttention(adresseLegale.getAttention());
                                adrMgr.setForCasePostale(adresseLegale.getCasePostale());
                                adrMgr.setForRue(adresseLegale.getRue());
                                adrMgr.setForNumeroRue(adresseLegale.getNumeroRue());
                                adrMgr.setForIdLocalite(adresseLegale.getIdLocalite());
                                // adrMgr.setForLangue(langueDocument);
                                adrMgr.find();
                                if (adrMgr.size() > 0) {
                                    TIAdresse adr = (TIAdresse) adrMgr.getFirstEntity();
                                    idAdresse = adr.getIdAdresse();
                                } else {
                                    adresseLegale.add(transaction);
                                    idAdresse = adresseLegale.getIdAdresse();
                                }
                            } else {
                                adresseLegale.add(transaction);
                                idAdresse = adresseLegale.getIdAdresse();
                            }
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            }
                            avoirAdresseLegale = (TIAvoirAdresse) adresseLegaleMng.getFirstEntity();
                            avoirAdresseLegale.setIdTiers(idTiers);
                            avoirAdresseLegale.setIdAdresse(idAdresse);
                            avoirAdresseLegale.setIdLocalite(localite.getIdLocalite());
                            avoirAdresseLegale.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
                            avoirAdresseLegale.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                            avoirAdresseLegale.update(transaction);
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            } else {
                                transaction.commit();
                            }
                        }
                    } else {
                        // Création de l'adresse
                        if ((localite != null) && !localite.isNew()) {
                            adresseLegale = new TIAdresse();
                            adresseLegale.setSession(sessionPyxis);
                            adresseLegale.setIdLocalite(localite.getIdLocalite());
                            if ((annonce.getRueLegal().toUpperCase().indexOf("CASE POSTALE") != -1)
                                    || (annonce.getRueLegal().toUpperCase().indexOf("POSTFACH") != -1)
                                    || (annonce.getRueLegal().toUpperCase().indexOf("CASELLA POSTALE") != -1)) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Le champ rue de l'adresse de domicile comportait une case postale",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseLegale.setRue("");
                            } else {
                                adresseLegale.setRue(annonce.getRueLegal());
                            }
                            adresseLegale.setAttention(annonce.getAdresseLegale());
                            // Test de l'existance de l'adresse
                            String idAdresse = "";
                            if (reutiliseAdresse) {
                                TIAdresseManager adrMgr = new TIAdresseManager();
                                adrMgr.setSession(sessionPyxis);
                                adrMgr.setForLigneAdresse1N(adresseLegale.getLigneAdresse1());
                                adrMgr.setForLigneAdresse2(adresseLegale.getLigneAdresse2());
                                adrMgr.setForLigneAdresse3(adresseLegale.getLigneAdresse3());
                                adrMgr.setForLigneAdresse4(adresseLegale.getLigneAdresse4());
                                adrMgr.setForTitreAdresseN(adresseLegale.getTitreAdresse());
                                adrMgr.setForAttention(adresseLegale.getAttention());
                                adrMgr.setForCasePostale(adresseLegale.getCasePostale());
                                adrMgr.setForRue(adresseLegale.getRue());
                                adrMgr.setForNumeroRue(adresseLegale.getNumeroRue());
                                adrMgr.setForIdLocalite(adresseLegale.getIdLocalite());
                                // adrMgr.setForLangue(langueDocument);
                                adrMgr.find();
                                if (adrMgr.size() > 0) {
                                    TIAdresse adr = (TIAdresse) adrMgr.getFirstEntity();
                                    idAdresse = adr.getIdAdresse();
                                } else {
                                    adresseLegale.add(transaction);
                                    idAdresse = adresseLegale.getIdAdresse();
                                }
                            } else {
                                adresseLegale.add(transaction);
                                idAdresse = adresseLegale.getIdAdresse();
                            }
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            }
                            avoirAdresseLegale = new TIAvoirAdresse();
                            avoirAdresseLegale.setSession(sessionPyxis);
                            avoirAdresseLegale.setIdTiers(idTiers);
                            avoirAdresseLegale.setIdAdresse(idAdresse);
                            if ((annonce.getRueLegal().toUpperCase().indexOf("CASE POSTALE") != -1)
                                    || (annonce.getRueLegal().toUpperCase().indexOf("POSTFACH") != -1)
                                    || (annonce.getRueLegal().toUpperCase().indexOf("CASELLA POSTALE") != -1)) {
                                // process.getMemoryLog().logMessage(annonce.getNom()+" "+annonce.getPrenom()+" : Le champ rue de l'adresse de domicile comportait une case postale",
                                // FWMessage.INFORMATION, "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseLegale.setRue("");
                            } else {
                                adresseLegale.setRue(annonce.getRueLegal());
                            }
                            avoirAdresseLegale.setIdLocalite(localite.getIdLocalite());
                            avoirAdresseLegale.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
                            avoirAdresseLegale.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                            avoirAdresseLegale.add(transaction);
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            } else {
                                transaction.commit();
                            }
                        }
                    }
                    if (transaction.hasErrors()) {
                        throw new Exception(transaction.getErrors().toString());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("(CreationAdresseLegal) " + e.getMessage());
        }
        /*
         * Création ou mise a jour de l'adresse de courrier avec l'adresse étude.
         */
        try {
            localite = null;
            if (!existeAdresseActivePlusRecente(sessionPyxis, idTiers, annee, IConstantes.CS_AVOIR_ADRESSE_COURRIER)) {
                if (!JadeStringUtil.isBlankOrZero(annonce.getNpaEtude())) {
                    // Recherche de la Localité
                    TILocaliteManager localiteMng = new TILocaliteManager();
                    localiteMng.setSession(sessionPyxis);
                    localiteMng.setInclureInactif(new Boolean(false));
                    if (JadeStringUtil.isBlankOrZero(annonce.getSuffixePostalEtude())) {
                        localiteMng.setForNumPostal(annonce.getNpaEtude() + "00");
                    } else {
                        localiteMng.setForNumPostal(annonce.getNpaEtude() + annonce.getSuffixePostalEtude());
                    }
                    localiteMng.setForIdPays(IConstantes.ID_PAYS_SUISSE);
                    localiteMng.find(transaction);
                    if (localiteMng.size() >= 1) {
                        localite = (TILocalite) localiteMng.getFirstEntity();
                    } else {
                        throw new Exception("Le NPA ne correspond à aucune localité dans les tiers.");
                    }
                    // Recherche s'il existe déjà une adresse de courrier
                    TITiersViewBean tiers = new TITiersViewBean();
                    tiers.setSession(sessionPyxis);
                    tiers.setIdTiers(idTiers);
                    tiers.retrieve();

                    avoirAdresseEtudeCourrier = rechercheExistenceAdresseCourrier(annonce, avoirAdresseEtudeCourrier,
                            localite, idTiers, annee);

                    if (avoirAdresseEtudeCourrier != null) {
                        // Mise à jour l'adresse
                        if ((localite != null) && !localite.isNew()) {
                            adresseEtude = new TIAdresse();
                            adresseEtude.setSession(sessionPyxis);
                            adresseEtude.setIdLocalite(localite.getIdLocalite());
                            if (annonce.getRueEtude().toUpperCase().indexOf("CASE POSTALE") != -1) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Case Postale a été remplacé par c.p. pour l'adresse de courrier.",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseEtude.setRue(annonce.getRueEtude().toUpperCase()
                                        .replaceAll("CASE POSTALE", "c.p."));
                            } else if (annonce.getRueEtude().toUpperCase().indexOf("POSTFACH") != -1) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Case Postale a été remplacé par c.p. pour l'adresse de courrier.",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseEtude.setRue(annonce.getRueEtude().toUpperCase().replaceAll("POSTFACH", "c.p."));
                            } else if (annonce.getRueEtude().toUpperCase().indexOf("CASELLA POSTALE") != -1) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Case Postale a été remplacé par c.p. pour l'adresse de courrier.",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseEtude.setRue(annonce.getRueEtude().toUpperCase()
                                        .replaceAll("CASELLA POSTALE", "c.p."));
                            } else {
                                adresseEtude.setRue(annonce.getRueEtude());
                            }
                            adresseEtude.setAttention(annonce.getAdresseEtude());
                            // Test de l'existance de l'adresse
                            String idAdresse = "";
                            if (reutiliseAdresse) {
                                TIAdresseManager adrMgr = new TIAdresseManager();
                                adrMgr.setSession(sessionPyxis);
                                adrMgr.setForLigneAdresse1N(adresseEtude.getLigneAdresse1());
                                adrMgr.setForLigneAdresse2(adresseEtude.getLigneAdresse2());
                                adrMgr.setForLigneAdresse3(adresseEtude.getLigneAdresse3());
                                adrMgr.setForLigneAdresse4(adresseEtude.getLigneAdresse4());
                                adrMgr.setForTitreAdresseN(adresseEtude.getTitreAdresse());
                                adrMgr.setForAttention(adresseEtude.getAttention());
                                adrMgr.setForCasePostale(adresseEtude.getCasePostale());
                                adrMgr.setForRue(adresseEtude.getRue());
                                adrMgr.setForNumeroRue(adresseEtude.getNumeroRue());
                                adrMgr.setForIdLocalite(adresseEtude.getIdLocalite());
                                // adrMgr.setForLangue(langueDocument);
                                adrMgr.find();
                                if (adrMgr.size() > 0) {
                                    TIAdresse adr = (TIAdresse) adrMgr.getFirstEntity();
                                    idAdresse = adr.getIdAdresse();
                                } else {
                                    adresseEtude.add(transaction);
                                    idAdresse = adresseEtude.getIdAdresse();
                                }
                            } else {
                                adresseEtude.add(transaction);
                                idAdresse = adresseEtude.getIdAdresse();
                            }
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            }
                            avoirAdresseEtudeCourrier.setIdAdresse(idAdresse);
                            avoirAdresseEtudeCourrier.setIdLocalite(localite.getIdLocalite());
                            avoirAdresseEtudeCourrier.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                            avoirAdresseEtudeCourrier.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                            avoirAdresseEtudeCourrier.update(transaction);
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            } else {
                                transaction.commit();
                            }
                        }
                    } else {
                        // Création de l'adresse
                        if ((localite != null) && !localite.isNew()) {
                            adresseEtude = new TIAdresse();
                            adresseEtude.setSession(sessionPyxis);
                            adresseEtude.setIdLocalite(localite.getIdLocalite());
                            if (annonce.getRueEtude().toUpperCase().indexOf("CASE POSTALE") != -1) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Case Postale a été remplacé par c.p. pour l'adresse de courrier.",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseEtude.setRue(annonce.getRueEtude().toUpperCase()
                                        .replaceAll("CASE POSTALE", "c.p."));
                            } else if (annonce.getRueEtude().toUpperCase().indexOf("POSTFACH") != -1) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Case Postale a été remplacé par c.p. pour l'adresse de courrier.",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseEtude.setRue(annonce.getRueEtude().toUpperCase().replaceAll("POSTFACH", "c.p."));
                            } else if (annonce.getRueEtude().toUpperCase().indexOf("CASELLA POSTALE") != -1) {
                                process.getMemoryLog()
                                        .logMessage(
                                                annonce.getNom()
                                                        + " "
                                                        + annonce.getPrenom()
                                                        + " : Case Postale a été remplacé par c.p. pour l'adresse de courrier.",
                                                FWMessage.INFORMATION,
                                                "GECreationMiseajourDonnees.creationAdresses(..)");
                                adresseEtude.setRue(annonce.getRueEtude().toUpperCase()
                                        .replaceAll("CASELLA POSTALE", "c.p."));
                            } else {
                                adresseEtude.setRue(annonce.getRueEtude());
                            }
                            adresseEtude.setAttention(annonce.getAdresseEtude());
                            // Test de l'existance de l'adresse
                            String idAdresse = "";
                            if (reutiliseAdresse) {
                                TIAdresseManager adrMgr = new TIAdresseManager();
                                adrMgr.setSession(sessionPyxis);
                                adrMgr.setForLigneAdresse1N(adresseEtude.getLigneAdresse1());
                                adrMgr.setForLigneAdresse2(adresseEtude.getLigneAdresse2());
                                adrMgr.setForLigneAdresse3(adresseEtude.getLigneAdresse3());
                                adrMgr.setForLigneAdresse4(adresseEtude.getLigneAdresse4());
                                adrMgr.setForTitreAdresseN(adresseEtude.getTitreAdresse());
                                adrMgr.setForAttention(adresseEtude.getAttention());
                                adrMgr.setForCasePostale(adresseEtude.getCasePostale());
                                adrMgr.setForRue(adresseEtude.getRue());
                                adrMgr.setForNumeroRue(adresseEtude.getNumeroRue());
                                adrMgr.setForIdLocalite(adresseEtude.getIdLocalite());
                                // adrMgr.setForLangue(langueDocument);
                                adrMgr.find();
                                if (adrMgr.size() > 0) {
                                    TIAdresse adr = (TIAdresse) adrMgr.getFirstEntity();
                                    idAdresse = adr.getIdAdresse();
                                } else {
                                    adresseEtude.add(transaction);
                                    idAdresse = adresseEtude.getIdAdresse();
                                }
                            } else {
                                adresseEtude.add(transaction);
                                idAdresse = adresseEtude.getIdAdresse();
                            }
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            }
                            avoirAdresseEtudeCourrier = new TIAvoirAdresse();
                            avoirAdresseEtudeCourrier.setSession(sessionPyxis);
                            avoirAdresseEtudeCourrier.setIdTiers(idTiers);
                            avoirAdresseEtudeCourrier.setIdAdresse(idAdresse);
                            avoirAdresseEtudeCourrier.setIdLocalite(localite.getIdLocalite());
                            avoirAdresseEtudeCourrier.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                            avoirAdresseEtudeCourrier.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                            avoirAdresseEtudeCourrier.add(transaction);
                            if (transaction.hasErrors()) {
                                throw new Exception(transaction.getErrors().toString());
                            } else {
                                transaction.commit();
                            }
                        }
                    }
                    if (transaction.hasErrors()) {
                        throw new Exception(transaction.getErrors().toString());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("(CreationAdresseEtude) " + e.getMessage());
        }
    }

    // Crétation de l'affiliation
    public AFAffiliation creationAffiliation(GEAnnonces annonce, TITiersViewBean tiers, String annee,
            String idplanCaisse) throws Exception {
        AFAffiliation affiliation = null;
        AFAdhesion adhesion = null;
        if (annonce == null) {
            throw new Exception("CreationAffiliation: annonce nulle !");
        }
        if (tiers == null) {
            throw new Exception("CreationAffiliation: idTiers obligatoire.");
        }
        if (JadeStringUtil.isBlankOrZero(annee)) {
            throw new Exception("CreationAffiliation: année obligatoire.");
        }
        if (JadeStringUtil.isBlankOrZero(idplanCaisse)) {
            throw new Exception("CreationAffiliation: le plan caisse est obligatoire.");
        }
        try {
            // Recherche de l'affiliation pour ce tiers
            AFAffiliationManager affiliationMng = new AFAffiliationManager();
            affiliationMng.setSession(sessionNaos);
            affiliationMng.setForIdTiers(tiers.getIdTiers());
            affiliationMng.setForDateDebut("01.01." + annee);
            affiliationMng.setForDateFin("31.12." + annee);
            affiliationMng.setForBranchBrancheEconomique(CodeSystem.BRANCHE_ECO_ETUDIANTS);
            affiliationMng.find(transaction);
            if (affiliationMng.getSize() >= 1) {
                affiliation = (AFAffiliation) affiliationMng.getFirstEntity();
            } else {
                // Création de l'affiliation si elle n'existe pas
                affiliation = new AFAffiliation();
                affiliation.setSession(sessionNaos);
                affiliation.setIdTiers(tiers.getIdTiers());
                affiliation.setDateDebut("01.01." + annee);
                affiliation.setDateFin("31.12." + annee);
                affiliation.setMotifFin(CodeSystem.MOTIF_FIN_DIV);
                affiliation.setMotifCreation(CodeSystem.MOTIF_AFFIL_LISTES_ECOLES);
                affiliation.setRaisonSociale(tiers.getDesignation1() + " " + tiers.getDesignation2());
                String raisonSocialeCourt = tiers.getDesignation1() + " " + tiers.getDesignation2();
                if (raisonSocialeCourt.length() > 30) {
                    raisonSocialeCourt = raisonSocialeCourt.substring(0, 30);
                }
                affiliation.setRaisonSocialeCourt(raisonSocialeCourt);
                // branche économique 89 pour étudiant
                affiliation.setBrancheEconomique(CodeSystem.BRANCHE_ECO_ETUDIANTS);
                affiliation.setTypeAffiliation(CodeSystem.TYPE_AFFILI_NON_ACTIF);
                affiliation.setPersonnaliteJuridique(CodeSystem.PERS_JURIDIQUE_NA);
                affiliation.setPeriodicite(CodeSystem.PERIODICITE_ANNUELLE);
                affiliation.add(transaction);
                if (transaction.hasErrors()) {
                    throw new Exception(transaction.getErrors().toString());
                }
                // Création du plan d'affiliation
                AFPlanAffiliation planAffiliation = new AFPlanAffiliation();
                planAffiliation.setSession(sessionNaos);
                planAffiliation.setAffiliationId(affiliation.getAffiliationId());
                String planAffiliationLibelle = affiliation.getAffilieNumero() + "_" + affiliation.getDateDebut();
                planAffiliation.setLibelle(planAffiliationLibelle);
                planAffiliation.add(transaction);
                // Creation de l'adhesion
                adhesion = new AFAdhesion();
                adhesion.setSession(sessionNaos);
                adhesion.setAffiliationId(affiliation.getAffiliationId());
                adhesion.setDateDebut(affiliation.getDateDebut());
                adhesion.setDateFin(affiliation.getDateFin());
                adhesion.setIdTiers(affiliation.getIdTiers());
                adhesion.setTypeAdhesion(CodeSystem.TYPE_ADHESION_CAISSE);
                adhesion.setPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                adhesion.setPlanCaisseId(idplanCaisse);
                adhesion.add(transaction);
                if (transaction.hasErrors()) {
                    throw new Exception(transaction.getErrors().toString());
                }
                transaction.commit();
            }
            // Creation Particularité
            creationParticulariteAffiliation(affiliation);
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("CreationAffiliation: " + e.getMessage());
        }
        return affiliation;
    }

    public String creationDecisionCotisation(AFAffiliation affiliation, TITiersViewBean tiers, String idPassage,
            GEAnnonces annonce) throws Exception {
        String idDecision = null;
        if (affiliation == null) {
            throw new Exception("CreationDecision: Affiliation obligatoire.");
        }
        try {
            // Création décision
            CPDecisionViewBean decision = new CPDecisionViewBean();
            decision.setSession(sessionPhenix);
            decision.setAffiliation(affiliation);
            decision.setIdTiers(affiliation.getIdTiers());
            decision.setIdAffiliation(affiliation.getAffiliationId());
            decision.setDebutDecision(affiliation.getDateDebut());
            decision.setFinDecision(affiliation.getDateFin());
            decision.setAnneeDecision(JADate.getYear(affiliation.getDateDebut()) + "");
            decision.setAnneeRevenuDebut(JADate.getYear(affiliation.getDateDebut()) + "");
            decision.setAnneeRevenuFin(JADate.getYear(affiliation.getDateFin()) + "");
            decision.setDateInformation(JACalendar.todayJJsMMsAAAA());
            decision.setDateFortune(affiliation.getDateFin());
            decision.setDebutExercice1(affiliation.getDateDebut());
            decision.setFinExercice1(affiliation.getDateFin());
            if (annonce.getIsImputation().booleanValue()) {
                decision.setTypeDecision(CPDecision.CS_IMPUTATION);
                decision.setCotisation1(annonce.getCotisation());
                decision.setRevenu1(annonce.getMontantCI());
            } else {
                decision.setTypeDecision(CPDecision.CS_DEFINITIVE);
            }
            decision.setGenreAffilie(CPDecision.CS_ETUDIANT);
            decision.setFacturation(new Boolean(true));
            decision.setImpression(new Boolean(true));
            decision.setInteret(CAInteretMoratoire.CS_AUTOMATIQUE);
            decision.setNumAffilie(affiliation.getAffilieNumero());
            decision.setPeriodicite(affiliation.getPeriodicite());
            decision.setResponsable(sessionPhenix.getUserId());
            decision.setSourceInformation(CPDonneesBase.CS_TAX_OFFICE);
            decision.setTaxation("N");
            decision.setTiers(tiers);
            decision._initPeriodeFiscale(decision, JADate.getYear(affiliation.getDateDebut()) + "",
                    Integer.parseInt(decision.getAnneeDecision()));
            decision._controle(transaction);
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }
            decision.add(transaction);
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            } else {
                transaction.commit();
            }
            idDecision = decision.getIdDecision();
            // Calcul de coti
            CPProcessCalculCotisation calculProcess = new CPProcessCalculCotisation();
            calculProcess.setSession(sessionPhenix);
            calculProcess.setIdDecision(idDecision);
            calculProcess.setSendMailOnError(true);
            calculProcess.setSendCompletionMail(false);
            calculProcess.setModeArrondiFad(getModeArrondiFadCotPers());
            calculProcess.executeProcess();
            if (transaction.hasErrors() || calculProcess.isOnError() || calculProcess.isAborted()) {
                throw new Exception(transaction.getErrors().toString() + ", " + calculProcess.getMessage());
            } else {
                transaction.commit();
            }
            // Validation de la décision
            decision.retrieve(transaction);
            decision.setDernierEtat(CPDecision.CS_VALIDATION);
            decision.setIdPassage(idPassage);
            decision.update(transaction);
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("CreationDecisionCotisation: " + e.getMessage());
        }
        return idDecision;
    }

    // Création etudiant
    public String creationEtudiant(String idTiersEtudiant, String idTiersEcole, String numImmatriculation)
            throws Exception {
        String idEtudiant = null;
        if (JadeStringUtil.isBlankOrZero(idTiersEtudiant)) {
            throw new Exception("CreationEtudiant: IdTiersEtudiant obligatoire.");
        }
        if (JadeStringUtil.isBlankOrZero(idTiersEcole)) {
            throw new Exception("CreationEtudiant: IdTiersEcole obligatoire.");
        }
        try {
            GEEtudiantsManager etudiantsMng = new GEEtudiantsManager();
            etudiantsMng.setSession(sessionCampus);
            if (!JadeStringUtil.isBlankOrZero(numImmatriculation)) {
                etudiantsMng.setForNumImmatriculation(numImmatriculation);
            } else {
                etudiantsMng.setForNumImmatriculationVide(new Boolean(true));
            }
            etudiantsMng.setForIdTiersEtudiant(idTiersEtudiant);
            etudiantsMng.setForIdTiersEcole(idTiersEcole);
            etudiantsMng.find(transaction);
            if (etudiantsMng.size() >= 1) {
                idEtudiant = ((GEEtudiants) etudiantsMng.getFirstEntity()).getIdEtudiant();
            } else {
                GEEtudiants etudiant = new GEEtudiants();
                etudiant.setSession(sessionCampus);
                etudiant.setIdTiersEtudiant(idTiersEtudiant);
                etudiant.setIdTiersEcole(idTiersEcole);
                etudiant.setNumImmatriculation(numImmatriculation);
                etudiant.add(transaction);
                idEtudiant = etudiant.getIdEtudiant();
            }
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }
            transaction.commit();
        } catch (Exception e) {
            throw new Exception("CreationEtudiant:" + e.getMessage());
        }
        return idEtudiant;
    }

    // Crétation du Tiers ou mise à jour du Tiers s'il existe déjà
    public TITiersViewBean creationMiseAJourTiers(GEAnnonces annonce, String idTiers, String annee, BProcess process)
            throws Exception {

        TITiersViewBean tiers = null;
        if (annonce == null) {
            throw new Exception("CreationMiseAJourTiers: annonce nulle !");
        }
        try {
            tiers = new TITiersViewBean();
            tiers.setSession(sessionPyxis);

            // check si le tiers est modifiable d'après les conditions métiers
            // @see GECreationAnnoncesRules

            boolean isTiersMofifiable = GECreationAnnoncesRules.checkIfModifiable(idTiers, sessionCampus);

            if (JadeStringUtil.isBlankOrZero(idTiers)) {
                // Création du Tiers
                tiers.setDesignation1(annonce.getNom());
                tiers.setDesignation2(annonce.getPrenom());
                tiers.setEtatCivil(annonce.getCsEtatCivil());
                tiers.setSexe(annonce.getCsSexe());
                tiers.setNumAvsActuel(annonce.getNumAvs());
                tiers.setDateNaissance(annonce.getDateNaissance());

                if (!JadeStringUtil.isEmpty(annonce.getNumAvs())) {
                    String numAvs = NSUtil.unFormatAVS(annonce.getNumAvs());
                    if (numAvs.length() == 11) {
                        if (numAvs.charAt(9) >= '5') {
                            tiers.setIdPays("");
                        } else {
                            tiers.setIdPays("100");
                        }
                    }
                }

                if (isTiersMofifiable) {
                    tiers.add(transaction);
                }
            } else {
                // Mise à jour du tiers
                tiers.setIdTiers(idTiers);
                tiers.retrieve(transaction);
                if (!tiers.isNew() && (tiers != null)) {
                    if (!JadeStringUtil.isBlank(annonce.getNom())) {
                        tiers.setDesignation1(annonce.getNom());
                    }
                    if (!JadeStringUtil.isBlank(annonce.getPrenom())) {
                        tiers.setDesignation2(annonce.getPrenom());
                    }
                    if (!JadeStringUtil.isBlank(annonce.getCsEtatCivil())) {
                        tiers.setEtatCivil(annonce.getCsEtatCivil());
                    }
                    if (!JadeStringUtil.isBlank(annonce.getCsSexe())) {
                        tiers.setSexe(annonce.getCsSexe());
                    }
                    // On ne va plus mettre à jour le numAvs
                    // PO : 2397
                    /*
                     * if (!JadeStringUtil.isBlank(annonce.getNumAvs())){
                     * tiers.setNumAvsActuel(annonce.getNumAvs()); }
                     */
                    if (!JadeStringUtil.isBlank(annonce.getDateNaissance())) {
                        tiers.setDateNaissance(annonce.getDateNaissance());
                    }
                    if (isTiersMofifiable) {
                        tiers.update(transaction);
                    }
                }
            }
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }

            // Création des adresses
            if (isTiersMofifiable) {
                creationAdresses(annonce, tiers.getIdTiers(), annee, process);
            }

        } catch (Exception e) {
            throw new Exception("CreationMiseAJourTiers: " + e.getMessage());
        }
        return tiers;
    }

    public void creationParticulariteAffiliation(AFAffiliation affiliation) throws Exception {
        if (affiliation == null) {
            throw new Exception("creationParticulariteAffiliation: idAffiliation obligatoire.");
        }
        try {
            // Recherche de la particularité. Si elle existe pas, on la crée
            AFParticulariteAffiliationManager particulariteMng = new AFParticulariteAffiliationManager();
            particulariteMng.setSession(sessionNaos);
            particulariteMng.setForAffiliationId(affiliation.getAffiliationId());
            particulariteMng.setForDateDebut(affiliation.getDateDebut());
            particulariteMng.setForDateFin(affiliation.getDateFin());
            particulariteMng.setForParticularite(CodeSystem.PARTIC_AFFILIE_SANS_COMM_FISC);
            if (particulariteMng.getCount(transaction) == 0) {
                AFParticulariteAffiliation particularite = new AFParticulariteAffiliation();
                particularite.setSession(sessionNaos);
                particularite.setAffiliationId(affiliation.getAffiliationId());
                particularite.setDateDebut(affiliation.getDateDebut());
                particularite.setDateFin(affiliation.getDateFin());
                particularite.setParticularite(CodeSystem.PARTIC_AFFILIE_SANS_COMM_FISC);
                particularite.add(transaction);
                if (transaction.hasErrors()) {
                    throw new Exception(transaction.getErrors().toString());
                }
            }
        } catch (Exception e) {
            throw new Exception("CreationParticulariteAffiliation: " + e.getMessage());
        }
    }

    // Création des sessions
    public void creationSession(BSession local) throws Exception {
        // Création de la session pyxis
        sessionPyxis = GEUtil.creationSessionPyxis(local);
        TIApplication appPyxis = (TIApplication) sessionPyxis.getApplication();
        reutiliseAdresse = appPyxis.hasReutiliseAdresse();
        // Création de la session naos
        sessionNaos = GEUtil.creationSessionNaos(local);
        // Création de la session phenix
        sessionPhenix = GEUtil.creationSessionPhenix(local);

        ;
    }

    private boolean existeAdresseActivePlusRecente(BSession sessionPyxis2, String idTiers, String annee,
            String typeAdresse) throws Exception {
        TIAvoirAdresseManager adresseLegaleMng = new TIAvoirAdresseManager();
        adresseLegaleMng.setSession(sessionPyxis);
        adresseLegaleMng.setForIdTiers(idTiers);
        adresseLegaleMng.setFromDateDebutRelation("01.01." + annee);
        adresseLegaleMng.setForTypeAdresse(typeAdresse);
        adresseLegaleMng.setForIdApplication(ICommonConstantes.CS_APPLICATION_COTISATION);
        adresseLegaleMng.find();
        if (adresseLegaleMng.size() > 0) {
            return true;
        } else {
            adresseLegaleMng.setForIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
            adresseLegaleMng.find();
            if (adresseLegaleMng.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public int getModeArrondiFadCotPers() {
        return modeArrondiFadCotPers;
    }

    private TIAvoirAdresse rechercheExistenceAdresseCourrier(GEAnnonces annonce, TIAvoirAdresse avoirAdresseEtude,
            TILocalite localite, String idTiers, String annee) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(annonce.getNpaEtude())) {
            // Recherche de la Localité
            TILocaliteManager localiteMng = new TILocaliteManager();
            localiteMng.setSession(sessionPyxis);
            localiteMng.setInclureInactif(new Boolean(false));
            if (JadeStringUtil.isBlankOrZero(annonce.getSuffixePostalEtude())) {
                localiteMng.setForNumPostal(annonce.getNpaEtude() + "00");
            } else {
                localiteMng.setForNumPostal(annonce.getNpaEtude() + annonce.getSuffixePostalEtude());
            }
            localiteMng.setForIdPays(IConstantes.ID_PAYS_SUISSE);
            localiteMng.find(transaction);
            if (localiteMng.size() >= 1) {
                localite = (TILocalite) localiteMng.getFirstEntity();
            } else {
                throw new Exception("Le NPA ne correspond à aucune localité dans les tiers.");
            }
            // Recherche s'il existe déjà une adresse de courrier
            TITiersViewBean tiers = new TITiersViewBean();
            tiers.setSession(sessionPyxis);
            tiers.setIdTiers(idTiers);
            tiers.retrieve();
            avoirAdresseEtude = tiers.findAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, "01.01." + annee);

            // K141201_006
            // Pyxis fait le fallback sur Domicile si Courrier est null, on ne veux pas de ce comportement
            if (!avoirAdresseEtude.getTypeAdresse().equalsIgnoreCase(IConstantes.CS_AVOIR_ADRESSE_COURRIER)) {
                avoirAdresseEtude = null;
                return null;
            }
            return avoirAdresseEtude;
        } else {
            return null;
        }

    }

    private TIAvoirAdresse rechercheExistenceAdresseDomicile(GEAnnonces annonce, TIAvoirAdresse avoirAdresseEtude,
            TILocalite localite, String idTiers, String annee) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(annonce.getNpaEtude())) {
            // Recherche de la Localité
            TILocaliteManager localiteMng = new TILocaliteManager();
            localiteMng.setSession(sessionPyxis);
            localiteMng.setInclureInactif(new Boolean(false));
            if (JadeStringUtil.isBlankOrZero(annonce.getSuffixePostalEtude())) {
                localiteMng.setForNumPostal(annonce.getNpaEtude() + "00");
            } else {
                localiteMng.setForNumPostal(annonce.getNpaEtude() + annonce.getSuffixePostalEtude());
            }
            localiteMng.setForIdPays(IConstantes.ID_PAYS_SUISSE);
            localiteMng.find(transaction);
            if (localiteMng.size() >= 1) {
                localite = (TILocalite) localiteMng.getFirstEntity();
            } else {
                throw new Exception("Le NPA ne correspond à aucune localité dans les tiers.");
            }
            // Recherche s'il existe déjà une adresse de domicile
            TITiersViewBean tiers = new TITiersViewBean();
            tiers.setSession(sessionPyxis);
            tiers.setIdTiers(idTiers);
            tiers.retrieve();
            avoirAdresseEtude = tiers.findAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    ICommonConstantes.CS_APPLICATION_COTISATION, "01.01." + annee);

            return avoirAdresseEtude;
        } else {
            return null;
        }

    }

    public void setModeArrondiFadCotPers(int modeArrondiFadCotPers) {
        this.modeArrondiFadCotPers = modeArrondiFadCotPers;
    }
}
