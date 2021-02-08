package globaz.pavo.db.compte;

import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIEnvoiEmailGroupes;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.adresse.formater.TILocaliteLongFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiersManager;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Wrapper de l'annonce en suspens. Date de création : (20.12.2002 09:43:48)
 * 
 * @author dgi
 * @version $Id: CIAnnonceWrapper.java,v 1.21 2010/09/02 08:15:38 jmc Exp $
 */
public abstract class CIAnnonceWrapper {
    // liste des CI annoncés
    protected static HashSet listeCIAnnonces;

    // l'annonce en suspens
    protected CIAnnonceSuspens annonceSuspens;

    // l'application PAVO
    protected CIApplication application;

    // flag de modification des attributs
    private boolean changed = false;
    protected CICompteIndividuel ciConjoint;
    // ci
    protected CICompteIndividuel compte;
    // Emails groupés
    private CIEnvoiEmailGroupes groupEmails;
    // l'annonce HERMES
    protected IHEOutputAnnonce remoteAnnonce;
    // l'annonce HERMES complémentaire 03 pour splitting
    protected IHEOutputAnnonce remoteAnnonce03;
    // l'annonce HERMES complémentaire (02 pour clôture, 03 pour splitting)
    protected IHEOutputAnnonce remoteAnnonceCompl;
    // session remote
    protected BISession remoteSession;
    // transasction remote
    protected BITransaction remoteTransaction;

    public static String changeName(String nom) {
        char car;
        for (int i = 0; i < nom.length(); i++) {
            car = nom.charAt(i);
            if (car == 'ä') {
                nom = nom.substring(0, i) + "AE" + nom.substring(i + 1);
            }
            if (car == 'ö') {
                nom = nom.substring(0, i) + "OE" + nom.substring(i + 1);
            }
            if (car == 'ü') {
                nom = nom.substring(0, i) + "UE" + nom.substring(i + 1);
            }
            if (car == 'à') {
                nom = nom.substring(0, i) + "A" + nom.substring(i + 1);
            }
            if ((car == 'é') || (car == 'è')) {
                nom = nom.substring(0, i) + "E" + nom.substring(i + 1);
            }

        }

        return nom;

    }

    /**
     * Supprime la liste des annonce traitées. Date de création : (06.01.2003 16:38:27)
     */
    public static void reset() {
        CIAnnonceWrapper.listeCIAnnonces = null;
    }

    /**
     * Constructeur par défaut.
     * 
     * @param annonce
     *            l'annonce en suspens du type {@link CIAnnonceSuspens}
     */
    public CIAnnonceWrapper(CIAnnonceSuspens annonce) {
        super();
        if (annonce != null) {
            annonceSuspens = annonce;
        } else {
            annonceSuspens = new CIAnnonceSuspens();
        }
    }

    /**
     * Traitement de l'annonce d'un extrait de CI dans le cadre d'un splitting, de la clôture ou du rassemblement. Date
     * de création : (18.12.2002 13:49:18)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param ecritures
     *            liste des écritures à annoncer ou null si toutes les écritures au Ci doivent être annoncées.
     * @param forConjoint
     *            mettre à true si le CI du conjoint est à annoncer (dans les cas de splitting)
     */
    public void annonceExtraitCI(BTransaction transaction, ArrayList ecritures, boolean forConjoint) throws Exception {
        // test si l'annonce doit être effectuée
        // pour le splitting, une liste doit être maintenue afin de l'envoyer
        // seulement une fois
        if ((transaction != null) && (transaction.hasErrors())) {
            System.out.println("transaction en erreur dans annonce extrait CI: " + transaction.getErrors());
            return;
        }
        CICompteIndividuel ciToUse = null;
        if (forConjoint) {
            ciToUse = ciConjoint;
        } else {
            ciToUse = compte;
        }
        if (ciToUse == null) {
            return;
        }
        // annoncer le CI si pas déjà effectué. Exception pour les CI vides pour
        // les différentes agences
        if ((ecritures != null) || (CIAnnonceWrapper.listeCIAnnonces == null)
                || (!CIAnnonceWrapper.listeCIAnnonces.contains(ciToUse.getCompteIndividuelId()))) {
            // recherche des inscriptions
            if (ecritures == null) {
                CIEcritureManager ecrituresMgr = new CIEcritureManager();
                ecrituresMgr.setSession(getSession());
                ecrituresMgr.setForCompteIndividuelId(ciToUse.getCompteIndividuelId());
                ecrituresMgr.setForIdTypeCompte(CIEcriture.CS_CI);
                ecrituresMgr.setForRassemblementOuvertureId("0");
                ecrituresMgr.orderByAnnee();
                ecrituresMgr.find(transaction, BManager.SIZE_NOLIMIT);
                ecritures = new ArrayList();
                for (int i = 0; i < ecrituresMgr.size(); i++) {
                    ecritures.add(ecrituresMgr.getEntity(i));
                }
            }
            // création d'un transaction remote
            // création de l'API pour l'annonce
            IHEInputAnnonce remoteEcritureAnnonce = (IHEInputAnnonce) remoteSession.getAPIFor(IHEInputAnnonce.class);
            // création de l'API pour la lecture (enregistrement 02 et 03)
            remoteEcritureAnnonce.setIdProgramme(CIApplication.DEFAULT_APPLICATION_PAVO);
            remoteEcritureAnnonce.setUtilisateur(getSession().getUserId());
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "38");
            long total = 0;
            boolean genre8Existe = false;
            ;
            application.getCalendar();
            String dateJJMMAA = JACalendar.today().toString();
            dateJJMMAA = dateJJMMAA.substring(0, 4) + dateJJMMAA.substring(6);
            // pour toutes les annonces
            // caisse commettante
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE,
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE));
            // agence commettante
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE,
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
            // référence interne
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE,
                    remoteAnnonce.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
            // ayant droit
            if (CIAnnonceSuspens.CS_ORDRE_SPLITTING.equals(annonceSuspens.getIdTypeTraitement())) {
                // splitting -> recherche enregistrement 03
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE,
                        remoteAnnonce03.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE));
            } else {
                // pas un splitting -> recherche enregistrement 02
                String avs = remoteAnnonceCompl.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT);
                if (!JAUtil.isStringEmpty(avs)) {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE, avs);
                } else {
                    avs = remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE);
                    if (!JAUtil.isStringEmpty(avs)) {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE, avs);
                    } else {
                        avs = remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE, avs);
                    }
                }
            }
            // motif
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.MOTIF_ANNONCE,
                    remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            // date clôture
            if (!CIAnnonceSuspens.CS_ORDRE_CLOTURE.equals(annonceSuspens.getIdTypeTraitement())) {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA, "");
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA,
                        remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA));
            }
            // date de l'ordre
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA,
                    remoteAnnonceCompl.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA));
            // caisse tenant le CI
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE__CI,
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI));
            // agence tenant le CI
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE_CI,
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI));
            // assuré
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE, ciToUse.getNumeroAvs());
            int nbrAnnonces = 0;
            for (int i = 0; i < ecritures.size(); i++) {
                boolean annonceCode2 = false;
                CIEcriture ecriture = (CIEcriture) ecritures.get(i);

                // recherche no affilié avec id tiers
                AFAffiliation aff = null;
                if (!JAUtil.isIntegerEmpty(ecriture.getEmployeur())) {
                    aff = application.getAffilie(transaction, ecriture.getEmployeur(), null);
                }
                // enregistrement
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                        String.valueOf((nbrAnnonces % 999) + 1));
                nbrAnnonces++;
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_1_OU_2, "1");
                // affilié

                if (CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
                    // flag genre 8 trouvé
                    genre8Existe = true;
                    // recherche du ci partenaire
                    CICompteIndividuel ciPartenaire = new CICompteIndividuel();
                    ciPartenaire.setSession(getSession());
                    ciPartenaire.setCompteIndividuelId(ecriture.getPartenaireId());
                    ciPartenaire.retrieve(transaction);
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE,
                            ciPartenaire.getNumeroAvsForSplitting());
                } else {
                    // recherche journal
                    CIJournal journal = new CIJournal();
                    journal.setSession(getSession());
                    journal.setIdJournal(ecriture.getIdJournal());
                    journal.retrieve(transaction);
                    // no affilié
                    boolean affilieFound = false;
                    if (!journal.isNew()) {
                        if (CIJournal.CS_APG.equals(journal.getIdTypeInscription())) {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, "77777777777");
                            affilieFound = true;
                        } else if (CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription())) {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, "55555555555");
                            affilieFound = true;
                        } else if (CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())) {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, "88888888888");
                            affilieFound = true;
                        } else if (CIJournal.CS_ASSURANCE_CHOMAGE.equals(journal.getIdTypeInscription())) {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE,
                                    ecriture.getCaisseChomageFormattee());
                            affilieFound = true;
                        } else if (CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription())) {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, "66666666666");
                            affilieFound = true;
                        } else if (!JadeStringUtil.isIntegerEmpty(ecriture.getPartBta())) {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, "11111111111");
                            affilieFound = true;
                        }
                    }
                    if (!affilieFound) {
                        String numAff = "";
                        if (!JadeStringUtil.isBlank(ecriture.getAffHist())) {
                            numAff = ecriture.getAffHist();
                            if (CIUtil.UnFormatNumeroAffilie(getSession(), numAff).trim().length() > 11) {
                                numAff = numAff.substring(0, 10);
                            }
                            if (!"93".equals(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                                    && !"95".equals(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {
                                if (!JadeStringUtil.isBlank(ecriture.getLibelleAff())) {
                                    annonceCode2 = true;
                                }
                            }
                        } else if (aff != null) {
                            numAff = CIUtil.UnFormatNumeroAffilie(getSession(), aff.getAffilieNumero());
                            // Modif 5.3 pour CCVS, si le num d'aff est >11 on
                            // subtr
                            if (numAff.length() > 11) {
                                numAff = numAff.substring(0, 10);
                            }
                            if (!"93".equals(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                                    && !"95".equals(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))) {
                                annonceCode2 = true;
                            }
                        }
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, numAff);
                    }
                }
                // chiffre clé extourne
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES,
                        CodeSystem.getCodeUtilisateur(ecriture.getExtourne(), getSession()));
                // chiffre clé genre cotisation
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS,
                        CodeSystem.getCodeUtilisateur(ecriture.getGenreEcriture(), getSession()));
                // chiffre clé particulier
                if (CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())
                        && !JAUtil.isIntegerEmpty(ecriture.getParticulier())) {
                    String part = CodeSystem.getCodeUtilisateur(ecriture.getParticulier(), getSession());
                    if (!JAUtil.isIntegerEmpty(part)) {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER, part);
                    } else {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER, "");
                    }
                } else {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER, "");
                }
                // part bta
                if (JAUtil.isIntegerEmpty(ecriture.getPartBta())) {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PART_BONIFICATIONS_ASSISTANCES, "");
                } else {
                    remoteEcritureAnnonce
                            .put(IHEAnnoncesViewBean.PART_BONIFICATIONS_ASSISTANCES, ecriture.getPartBta());
                }
                // code spécial
                if (JAUtil.isIntegerEmpty(ecriture.getCodeSpecial())) {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_SPECIAL, "");
                } else {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_SPECIAL,
                            CodeSystem.getCodeUtilisateur(ecriture.getCodeSpecial(), getSession()));
                }
                // début
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DUREE_COTISATIONS_DEBUT, ecriture.getMoisDebut());
                // fin
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DUREE_COTISATIONS_FIN, ecriture.getMoisFin());
                // année
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.ANNEE_COTISATIONS,
                        ecriture.getAnnee().length() == 4 ? ecriture.getAnnee().substring(2) : "");
                // revenu
                int revenuTr = (int) Double.parseDouble(ecriture.getRevenu());
                if (JAUtil.isIntegerEmpty(ecriture.getExtourne())
                        || CIEcriture.CS_EXTOURNE_2.equals(ecriture.getExtourne())
                        || CIEcriture.CS_EXTOURNE_6.equals(ecriture.getExtourne())
                        || CIEcriture.CS_EXTOURNE_8.equals(ecriture.getExtourne())) {
                    total += revenuTr;
                } else {
                    total -= revenuTr;
                }
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.REVENU, String.valueOf(revenuTr));
                // amortissement
                if (JAUtil.isIntegerEmpty(ecriture.getCode())
                        || CIEcriture.CS_CODE_PROVISOIRE.equals(ecriture.getCode())
                        || CIEcriture.CS_CODE_MIS_EN_COMTE.equals(ecriture.getCode())) {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_A_D_S, "");
                } else {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_A_D_S,
                            CodeSystem.getCodeUtilisateur(ecriture.getCode(), getSession()));
                }
                // branche économique
                if (JAUtil.isIntegerEmpty(ecriture.getBrancheEconomique())) {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.BRANCHE_ECONOMIQUE, "");
                } else {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.BRANCHE_ECONOMIQUE,
                            CodeSystem.getCodeUtilisateur(ecriture.getBrancheEconomique(), getSession()));
                }
                // année cotisation
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA, ecriture.getAnnee());
                remoteEcritureAnnonce.add(remoteTransaction);
                if (remoteTransaction.hasErrors()) {
                    try {
                        this.envoiEmail(application.getEmailAdmin(),
                                "Erreur Annonce 38 01 - " + ciToUse.getNumeroAvs(), remoteTransaction.getErrors()
                                        .toString());
                    } finally {
                        return;
                    }
                }
                // 38 code 2 nécessaire?
                if (annonceCode2) {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_1_OU_2, "2");
                    // parite information
                    StringBuffer infoAff = new StringBuffer(42);
                    try {
                        if ((aff != null) && (aff.getTiers() != null)) {
                            String nomHist = "";
                            String prenomHist = "";
                            String dateSaisie = "31.12." + ecriture.getAnnee();
                            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                                    CIApplication.DEFAULT_APPLICATION_PAVO);
                            TIHistoriqueTiersManager histMgr = new TIHistoriqueTiersManager();
                            histMgr.setSession((BSession) application.getSessionTiers(getSession()));
                            histMgr.setForIdTiers(aff.getIdTiers());
                            histMgr.setForChamp(TITiers.FIELD_DESIGNATION1);
                            histMgr.setForDateDebutLowerOrEqualTo(dateSaisie);
                            histMgr.find();
                            if (histMgr.size() > 0) {
                                nomHist = ((TIHistoriqueTiers) histMgr.getEntity(histMgr.size() - 1)).getValeur();
                            } else {
                                histMgr.setForDateDebutLowerOrEqualTo("");
                                histMgr.find();
                                if (histMgr.size() > 0) {
                                    nomHist = ((TIHistoriqueTiers) histMgr.getFirstEntity()).getValeur();
                                }
                            }

                            histMgr.setForChamp(TITiers.FIELD_DESIGNATION2);
                            histMgr.setForDateDebutLowerOrEqualTo(dateSaisie);
                            histMgr.find();
                            if (histMgr.size() > 0) {
                                prenomHist = ((TIHistoriqueTiers) histMgr.getEntity(histMgr.size() - 1)).getValeur();
                            } else {
                                histMgr.setForDateDebutLowerOrEqualTo("");
                                histMgr.find();
                                if (histMgr.size() > 0) {
                                    prenomHist = ((TIHistoriqueTiers) histMgr.getFirstEntity()).getValeur();
                                }
                            }
                            if (JadeStringUtil.isBlankOrZero(ecriture.getLibelleAff())) {
                                if (!JadeStringUtil.isBlankOrZero(nomHist)) {
                                    infoAff.insert(0, JAUtil.padString(getNom(nomHist, prenomHist), 42));
                                }
                                if (JadeStringUtil.isBlankOrZero(infoAff.toString())) {
                                    infoAff.insert(0, JAUtil.padString(aff.getTiers().getNom(), 42));
                                }

                                String localiteHist = aff.getTiers().getAdresseAsString(null,
                                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                                        dateSaisie, new TILocaliteLongFormater());
                                String loc = "";
                                if (!JadeStringUtil.isBlankOrZero(localiteHist)) {
                                    loc = localiteHist;
                                } else {
                                    loc = aff.getTiers().getLocaliteLong();
                                }
                                if (loc != null) {
                                    if (infoAff.toString().trim().length() > 24) {
                                        infoAff.replace(24, 42, JAUtil.padString("," + loc, 42));
                                    } else {
                                        infoAff.replace(25, 42, JAUtil.padString(loc, 42));
                                    }
                                }
                            } else {
                                infoAff.insert(0, ecriture.getLibelleAff());
                            }
                            if (infoAff.length() > 42) {
                                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PARTIE_INFORMATION,
                                        infoAff.substring(0, 42).toUpperCase());
                            } else {
                                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PARTIE_INFORMATION, infoAff.toString()
                                        .toUpperCase());
                            }
                        } else if (!JadeStringUtil.isBlankOrZero(ecriture.getLibelleAff())) {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PARTIE_INFORMATION, ecriture.getLibelleAff());
                        }
                    } catch (Exception ex) {
                        System.out.println("erreur extrait ci pour -" + infoAff.toString() + "-");
                    }
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                            String.valueOf((nbrAnnonces % 999) + 1));
                    nbrAnnonces++;
                    remoteEcritureAnnonce.add(remoteTransaction);
                    if (remoteTransaction.hasErrors()) {
                        try {
                            this.envoiEmail(application.getEmailAdmin(),
                                    "Erreur Annonce 38 02 - " + ciToUse.getNumeroAvs(), remoteTransaction.getErrors()
                                            .toString());
                        } finally {
                            return;
                        }
                    }
                }
            } // pour toutes les écritures
              // annonce 39 01
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "39");
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "1");
            // total
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.TOTAL_REVENUS, String.valueOf(Math.abs(total)));
            // positif/négatif
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_VALEUR_CHAMP_15, total < 0 ? "1" : "0");
            // nombre d'écritures
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI, String.valueOf(ecritures.size()));
            // ci additionnel
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CI_ADDITIONNEL, "0");
            // cas divorce
            if (CIAnnonceSuspens.CS_ORDRE_SPLITTING.equals(annonceSuspens.getIdTypeTraitement()) && genre8Existe) {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.SPLITTING_CAS_DIVORCE, "1");
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.SPLITTING_CAS_DIVORCE, "0");
            }
            // date de transmission
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_TRANSMISSION, dateJJMMAA);
            // envoi
            remoteEcritureAnnonce.add(remoteTransaction);
            if (remoteTransaction.hasErrors()) {
                try {
                    this.envoiEmail(application.getEmailAdmin(), "Erreur Annonce 39 01 - " + ciToUse.getNumeroAvs(),
                            remoteTransaction.getErrors().toString());
                } finally {
                    return;
                }
            }
            // annonce 39 02
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "2");
            if (forConjoint) {
                String nomPrenomConjoint = ciToUse.getNomPrenom();
                if (nomPrenomConjoint.length() > 40) {
                    nomPrenomConjoint = nomPrenomConjoint.substring(0, 40);
                }
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.ETAT_NOMINATIF, nomPrenomConjoint);
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.ETAT_NOMINATIF,
                        remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
            }
            // envoi
            remoteEcritureAnnonce.add(remoteTransaction);
            if (remoteTransaction.hasErrors()) {
                try {
                    this.envoiEmail(application.getEmailAdmin(), "Erreur Annonce 39 02 - " + ciToUse.getNumeroAvs(),
                            remoteTransaction.getErrors().toString());
                } finally {
                    return;
                }
            }
            //
            if (CIAnnonceWrapper.listeCIAnnonces == null) {
                CIAnnonceWrapper.listeCIAnnonces = new HashSet();
            }
            CIAnnonceWrapper.listeCIAnnonces.add(ciToUse.getCompteIndividuelId());
        }
    }

    /**
     * Efface l'annonce qui vient d'être traitée.<br>
     * 
     * @param transaction
     *            la transaction à utiliser. Date de création : (20.12.2002 15:09:59)
     * @excpetion si un problème survient lors de l'effacement.
     */
    protected void annonceTraitee(BTransaction transaction) throws Exception {
        if (!transaction.hasErrors() && !remoteTransaction.hasErrors()) {
            annonceSuspens.wantCallMethodBefore(false);
            annonceSuspens.delete(transaction);
        }
    }

    /**
     * Test si les deux Boolean données sont identiques et change le flag <tt>changed</tt> de cet object en conséquence.
     * Date de création : (27.11.2002 16:29:35)
     * 
     * @return <tt>value</tt> si non vide, sinon retourne target (flag inchangé)
     * @param target
     *            valeur initiale
     * @param value
     *            nouvelle valeur
     */
    protected Boolean checkAndSet(Boolean target, Boolean value) {
        if ((target != null) && (value != null)) {
            if (target.booleanValue() != value.booleanValue()) {
                changed = true;
            }
        }
        return value;
    }

    /**
     * Test si les deux valeurs données sont identiques et change le flag <tt>changed</tt> de cet object en conséquence.
     * Date de création : (27.11.2002 16:29:35)
     * 
     * @return <tt>value</tt> si non vide, sinon retourne target (flag inchangé)
     * @param target
     *            valeur initiale
     * @param value
     *            nouvelle valeur
     */
    protected String checkAndSet(String target, String value) {
        if (target != null) {
            if (JAUtil.isStringEmpty(value)) {
                return target;
            }
            if (!target.trim().equals(value.trim())) {
                changed = true;
            }
        }
        return value;
    }

    /**
     * Modifie l'état du CI avec copie si nécessaire.
     * 
     * @param transaction
     *            la transaction à utiliser. Date de création : (27.11.2002 14:24:57)
     */
    public void checkAndUpdateCI(BTransaction transaction) throws Exception {
        if (compte != null) {
            // copie du l'entity en cours
            CICompteIndividuel copie = new CICompteIndividuel();
            copie.setSession(getSession());
            copie.setPlausiNumAvs(true);
            compte.copyDataToEntity(copie);
            copie.setCompteIndividuelIdReference("0"); // a confirmer
            changed = false;
            // appel de l'implémentation du wrapper
            updateCI(transaction);
            if (changed && !compte.isNew()) {
                // changements trouvés
                // Sauve la copie originale si l'entity n'est pas nouveau
                copie.setCompteIndividuelId("");
                copie.setRegistre(CICompteIndividuel.CS_REGISTRE_HISTORIQUE);
                copie.add(transaction);
            }
            // sauvegarde de l'entity (modifié)
            compte.setPlausiNumAvs(true);
            compte.save(transaction);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.07.2003 14:52:58)
     */
    public void ciChanged() {
        changed = true;
    }

    /**
     * Crée un log pour l'annonce. Date de création : (20.12.2002 16:04:16)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param message
     *            le message du log.
     * @exception si
     *                une erreur survient
     */
    protected void createLog(BTransaction transaction, String message) {
        FWLog log;
        try {
            log = annonceSuspens.getLog(transaction);
        } catch (Exception inEx) {
            System.out.println("error getting log:");
            return;
        }
        if (transaction.hasErrors()) {
            System.out.println("impossible de créer un log \"" + message + "\", transaction en erreur: "
                    + transaction.getErrors());
        } else if ((log == null) || JAUtil.isIntegerEmpty(log.getIdLog())) {
            System.out.println("impossible de créer un log\"" + message + "\"");
        } else {
            if (!JAUtil.isStringEmpty(message)) {
                log.logMessage(message, FWMessage.FATAL, annonceSuspens.getClass().getName());
                try {
                    log.update(transaction);
                } catch (Exception inEx) {
                    System.out.println("error in update:");
                }
            } else {
                System.out.println("impossible de créer un log avec un message vide.");
            }
        }
    }

    public void envoiEmail(ArrayList to, String sujet, String message) throws Exception {
        if (groupEmails != null) {
            if (to == null) {
                groupEmails.addEmail(null, sujet, message);
            } else {
                for (int i = 0; i < to.size(); i++) {
                    groupEmails.addEmail((String) to.get(i), sujet, message);
                }
            }
        } else {
            if (to == null) {
                // adresse non trouvée -> envoie à admin
                application
                        .sendEmailToAdmin(getSession().getLabel("MSG_ANNONCE_EMAIL_ERREUR"), message, annonceSuspens);
            } else {
                for (int i = 0; i < to.size(); i++) {
                    JadeSmtpClient.getInstance().sendMail((String) to.get(i), sujet, message, null);
                }
            }
        }
    }

    /**
     * Envoie un email. Date de création : (23.12.2002 07:42:50)
     * 
     * @param to
     *            le destinataire.
     * @param sujet
     *            le sujet du mail.
     * @param message
     *            le contenu de l'email.
     * @exception si
     *                une erreur survient.
     */
    public void envoiEmail(String to, String sujet, String message) throws Exception {
        if (groupEmails != null) {
            groupEmails.addEmail(to, sujet, message);
        } else {
            if (to == null) {
                // adresse non trouvée -> envoie à admin
                application
                        .sendEmailToAdmin(getSession().getLabel("MSG_ANNONCE_EMAIL_ERREUR"), message, annonceSuspens);
            } else {
                JadeSmtpClient.getInstance().sendMail(to, sujet, message, null);
            }
        }
    }

    /**
     * Envoi un email au responsable CI si une erreur apparait dans la transaction
     */
    public void envoiEmailErreurTr(BTransaction transaction, String description) throws Exception {
        if (transaction.hasErrors()) {
            String message = description + ": " + transaction.getErrors().toString();
            ArrayList to = application.getEMailResponsableCI(transaction);
            this.envoiEmail(to, getSession().getLabel("MSG_ANNONCE_ERR_PROCESS_SUJET"), message);
        }
    }

    public String getAgenceCommise() throws Exception {
        return CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI));

    }

    /**
     * Bidouille l'année donnée du type AA pour générer une année du type AAAA.<br>
     * Régle: si AA >= 48, retourner 19AA, sinon retourner 20AA Note: cette méthode va bien évidemment fonctionner que
     * jusqu'en 2047 :-(. Date de création : (13.12.2002 14:11:12)
     * 
     * @return l'année bidouillée
     * @param annee
     *            l'année à bidouiller
     */
    protected String getAnneeBidouillee(String anneeStr) {
        if (anneeStr.trim().length() != 2) {
            return "";
        }
        int annee = Integer.parseInt(anneeStr);
        if (annee >= 48) {
            return "19" + anneeStr;
        } else {
            return "20" + anneeStr;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:55:00)
     * 
     * @return globaz.pavo.application.CIApplication
     */
    public globaz.pavo.application.CIApplication getApplication() {
        return application;
    }

    /**
     * Bidouille la date donnée du type MMAA ou JJMMAA pour générer une date du type JJMMAAAA.<br>
     * Régle: si AA >= 48, retourner JJMM19AA(01MM19AA), sinon retourner JJMM20AA(01MM20AA) Note: cette méthode va bien
     * évidemment fonctionner que jusqu'en 2047 :-(. Date de création : (13.12.2002 14:11:12)
     * 
     * @return la date bidouillée
     * @param date
     *            la date à bidouiller
     */
    protected String getDateBidouillee(String date) {
        if (date == null) {
            return "";
        }
        int posAnnee = 0;
        String prefix = "";
        if (date.trim().length() == 4) {
            posAnnee = 2;
            prefix = "01";
        } else if (date.trim().length() == 6) {
            posAnnee = 4;
        } else {
            return "";
        }
        int annee = Integer.parseInt(date.substring(posAnnee));
        if (annee >= 48) {
            return prefix + date.substring(0, posAnnee) + "19" + date.substring(posAnnee);
        } else {
            return prefix + date.substring(0, posAnnee) + "20" + date.substring(posAnnee);
        }
    }

    protected String getDateBidouilleeForNaissance(String date) {
        // fixme marche jusqu'en 2015

        if (date == null) {
            return "";
        }
        int posAnnee = 0;
        String prefix = "";
        if (date.trim().length() == 4) {
            posAnnee = 2;
            prefix = "01";
        } else if (date.trim().length() == 6) {
            posAnnee = 4;
        } else {
            return "";
        }
        int annee = Integer.parseInt(date.substring(posAnnee));
        String anneeEnCours = String.valueOf(JACalendar.today().getYear());
        if (!JadeStringUtil.isBlankOrZero(anneeEnCours)) {
            anneeEnCours = anneeEnCours.substring(2);
        } else {
            anneeEnCours = "15";
        }
        if (annee >= Integer.parseInt(anneeEnCours)) {
            return prefix + date.substring(0, posAnnee) + "19" + date.substring(posAnnee);
        } else {
            return prefix + date.substring(0, posAnnee) + "20" + date.substring(posAnnee);
        }
    }

    /**
     * Retourne l'adresse email de l'utilisateur. Date de création : (26.11.2002 08:29:01)
     * 
     * @return l'adresse email ou null si inexistante.
     */
    protected String getEMailUser(BTransaction transaction) {
        String email;
        try {
            email = application.getProperty("mailToDebug");
            if (email.length() == 0) {
                FWSecureUserDetail user = new FWSecureUserDetail();
                user.setSession(getSession());
                user.setUser(compte.getUtilisateurCreation());
                user.setLabel(application.getEmailKey());
                user.retrieve(transaction);
                if (!user.isNew()) {
                    if (email.length() != 0) {
                        email += ", ";
                    }
                    email += user.getData();
                }
                email = annonceSuspens.getSpy().getUser();
            }
        } catch (Exception e) {
            return null;
        }
        return email;
    }

    /**
     * Determine si une information concernant l'employeur se trouve dans la référence interne de l'annonce 21 <br>
     * Format de la référence interne: <tt>xx/id_employeur</tt>). Exemple DG/703.1002
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @param referenceInterne
     *            la valeur de la référence interne provenant de l'annonce 21 Date de création : (17.12.2002 16:45:44)
     * @return l'id de l'affilié ou null si aucune information n'a été trouvée
     */
    protected String getEmployeurDeReference(BTransaction transaction, String referenceInterne) {
        int slash = referenceInterne.indexOf('/');
        if (slash == 2) {
            // slash trouvé
            String employeur = referenceInterne.substring(slash + 1);
            try {
                return application.getAffilieByNo(transaction.getSession(), employeur, false, false, null, null, null,
                        "", "").getIdTiers();
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 10:29:00)
     * 
     * @return globaz.pavo.util.CIEnvoiEmailGroupes
     */
    public globaz.pavo.util.CIEnvoiEmailGroupes getGroupEmails() {
        return groupEmails;
    }

    /**
     * Retourne le type de traitement. Date de création : (23.12.2002 15:34:01)
     * 
     * @return le type de traitement.
     */
    public String getIdTypeTraitement() {
        return annonceSuspens.getIdTypeTraitement();
    }

    public java.lang.String getNom(String nom, String prenom) {

        String d1 = nom;
        String d2 = prenom;
        if (d1 == null) {
            d1 = "";
        } else {
            d1 = d1.trim();
        }

        if (d2 == null) {
            d2 = "";
        } else {
            d2 = d2.trim();
        }

        String tmp = d1;
        if (!JadeStringUtil.isBlank(d2)) {

            if (!JadeStringUtil.isBlank(d1)) {
                tmp += " ";
            }
            tmp += d2;
        }

        return tmp;
    }

    /**
     * Retourne le noméro AVS de l'annonce en suspens. Date de création : (23.12.2002 15:32:07)
     * 
     * @return le noméro AVS de l'annonce en suspens.java.lang.String
     */
    public String getNumeroAvs() {
        return annonceSuspens.getNumeroAvs();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:55:00)
     * 
     * @return globaz.hermes.api.IHEOutputAnnonce
     */
    public globaz.hermes.api.IHEOutputAnnonce getRemoteAnnonce() {
        return remoteAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:55:00)
     * 
     * @return globaz.globall.api.BISession
     */
    public globaz.globall.api.BISession getRemoteSession() {
        return remoteSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.07.2003 15:07:12)
     * 
     * @return globaz.globall.api.BITransaction
     */
    public globaz.globall.api.BITransaction getRemoteTransaction() {
        return remoteTransaction;
    }

    /**
     * Retourne la session de l'annonce. Date de création : (20.12.2002 10:36:33)
     * 
     * @return la session de l'annonce.
     */
    protected BSession getSession() {
        if (annonceSuspens != null) {
            return annonceSuspens.getSession();
        }
        return null;
    }

    /**
     * Retourne true si l'annonce est en suspens. Date de création : (20.12.2002 16:10:11)
     * 
     * @return true si l'annonce est en suspens.
     */
    public boolean isAnnonceSuspens() {
        return annonceSuspens.isAnnonceSuspens().booleanValue();
    }

    public boolean isCaissePrincipale() throws Exception {
        if (remoteAnnonce == null) {
            remoteAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteAnnonce.setIdAnnonce(annonceSuspens.getIdAnnonce());
            remoteAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable", "getUtilisateur" });
            remoteAnnonce.retrieve(remoteTransaction);
        }
        // Modif 1-5-8 => Modif pour FPV, pas seulement agence fusionnée mais
        // caisse fusionnée
        if (CIUtil.isCaisseDifferente(getSession())) {
            if (CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI)).equals(
                    application.getProperty(CIApplication.CODE_CAISSE))) {
                return true;
            } else {
                return false;
            }
        }

        if (CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI)).equals(
                application.getProperty(CIApplication.CODE_AGENCE))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:55:00)
     * 
     * @param newApplication
     *            globaz.pavo.application.CIApplication
     */
    public void setApplication(globaz.pavo.application.CIApplication newApplication) {
        application = newApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 10:29:00)
     * 
     * @param newGroupEmails
     *            globaz.pavo.util.CIEnvoiEmailGroupes
     */
    public void setGroupEmails(globaz.pavo.util.CIEnvoiEmailGroupes newGroupEmails) {
        groupEmails = newGroupEmails;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:55:00)
     * 
     * @param newRemoteAnnonce
     *            globaz.hermes.api.IHEOutputAnnonce
     */
    public void setRemoteAnnonce(globaz.hermes.api.IHEOutputAnnonce newRemoteAnnonce) {
        remoteAnnonce = newRemoteAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 08:55:00)
     * 
     * @param newRemoteSession
     *            globaz.globall.api.BISession
     */
    public void setRemoteSession(globaz.globall.api.BISession newRemoteSession) {
        remoteSession = newRemoteSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.07.2003 15:07:12)
     * 
     * @param newRemoteTransaction
     *            globaz.globall.api.BITransaction
     */
    public void setRemoteTransaction(globaz.globall.api.BITransaction newRemoteTransaction) {
        remoteTransaction = newRemoteTransaction;
    }

    /**
     * Suspens l'annonce. Date de création : (20.12.2002 15:47:36)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @exception si
     *                une erreur survient.
     */
    protected void suspendreAnnonce(BTransaction transaction) throws Exception {
        annonceSuspens.setAnnonceSuspens(new Boolean(true));
        annonceSuspens.update(transaction);
    }

    /**
     * Effectue les dernières opérations sur l'annonce.<br>
     * Par défaut, cette méthode n'effectue rien. Elle est à implémeter dans les sous-classes si nécessaire. Date de
     * création : (06.01.2003 15:25:17)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param allAnnonces
     *            liste des annonces qui doivent appeler ou ont appelés cette méthode
     * @exception si
     *                une erreur survient
     */
    public void terminer(BTransaction transaction, ArrayList allAnonces) throws Exception {
        // n'effectue rien par défaut
    }

    /**
     * Traîte l'annonce en suspens, implémenté dans la sous-classe. Date de création : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param testFinal
     *            doit être à true pour signifier si les tests doivent être effectués.
     */
    protected abstract void traitementAnnonce(BTransaction transaction, boolean testFinal) throws Exception;

    /**
     * Traîte l'annonce en suspens. Date de création : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param testFinal
     *            doit être à true pour signifier si les tests doivent être effectués.
     */
    public void traiter(BTransaction transaction, boolean testFinal) throws Exception {
        // création de l'API
        remoteAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
        remoteAnnonce.setIdAnnonce(annonceSuspens.getIdAnnonce());
        remoteAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable", "getUtilisateur" });
        remoteAnnonce.retrieve(remoteTransaction);
        if (remoteAnnonce.isNew()) {
            // Changement, on bloque l'annonce en loggant le fait qu'elle n'existe pas
            createLog(transaction, getSession().getLabel("ANNONCE_INEXISTANTE"));
            suspendreAnnonce(transaction);
            transaction.commit();
            return;
        }
        // appel du traitement spécifique
        traitementAnnonce(transaction, testFinal);
        if (!transaction.hasErrors() && !remoteTransaction.hasErrors()) {
            transaction.commit();
            remoteTransaction.commit();
        } else {
            transaction.rollback();
            remoteTransaction.rollback();
            String error = "";
            if (transaction.hasErrors()) {
                error += transaction.getErrors().toString();
                transaction.clearErrorBuffer();
            }
            if (remoteTransaction.hasErrors()) {
                error += remoteTransaction.getErrors().toString();
            }
            if (!JAUtil.isStringEmpty(error)) {
                // ajout des erreur de transaction de le log de l'annonce
                createLog(transaction, error);
            }
            suspendreAnnonce(transaction);
            transaction.commit();
        }
    }

    /**
     * Modifie l'état du CI avec copie si nécessaire.
     * 
     * @param transaction
     *            la transaction à utiliser. Date de création : (27.11.2002 14:24:57)
     */
    public abstract void updateCI(BTransaction transaction) throws Exception;

}
