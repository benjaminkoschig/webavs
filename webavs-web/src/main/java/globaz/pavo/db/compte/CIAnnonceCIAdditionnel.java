package globaz.pavo.db.compte;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWPKProvider;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.adresse.formater.TILocaliteLongFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiersManager;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;

/**
 * Classe d'envoi de CI additionnel. Date de création : (23.04.2003 11:42:35)
 * 
 * @author: David Girardin
 */
public class CIAnnonceCIAdditionnel {
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

    public static final String ID_FIELD_INCREMENT_PROVIDER = "INCREMENT_PROVIDER";
    public static final String ID_FIELD_REFERENCE_UNIQUE_PROVIDER = "REFERENCE_UNIQUE_PROVIDER";

    private CIApplication application;
    private CICompteIndividuel ci;
    private JADate dateCloture;
    private String dateJJMMAA;
    private CIRassemblementOuverture dCloture = null;
    private boolean genre8Existe = false;
    private int nbrAnnonces = 0;
    private IHEInputAnnonce remoteEcritureAnnonce;
    private BITransaction remoteTransaction;
    private BSession session;

    private long totalEcr = 0;

    /**
     * Commentaire relatif au constructeur CIAnnonceCIAdditionnel.
     */
    public CIAnnonceCIAdditionnel(CICompteIndividuel ciAAnnoncer) throws Exception {
        super();
        ci = ciAAnnoncer;
        if (ci != null) {
            session = ci.getSession();
        }
        init();
    }

    /**
     * Annonce le CI en fonction de l'annonce donnée
     * 
     * @param annonce
     *            l'annonce dans l'historique CI
     * @param transaction
     *            la transaction à utiliser
     * @param assure
     *            True pour annoncer le CI de l'assuré, False pour le conjoint. Null si inaplicable
     */

    public void annonceCI(CIRassemblementOuverture annonce, BTransaction transaction, String assure) throws Exception {
        if ((transaction == null) || (annonce == null)) {
            return;
        }
        boolean transactionOpened = false;
        // si assure est null, l'appel provient du process de création de ci
        // additionnel
        // sinon, il provient d'un réenvoi de CI -> même si vide, envoyer le 39
        boolean need39 = true;
        // assuré/conjoint
        boolean annonceAssure = true;
        try {
            if (remoteTransaction == null) {
                BISession remoteSession = application.getSessionAnnonce(session);
                remoteTransaction = ((BSession) remoteSession).newTransaction();
                remoteTransaction.openTransaction();
                transactionOpened = true;
                initAnnonce(remoteSession, remoteTransaction);
            }
            if (assure == null) {
                // défaut: assuré et pas de 39 si aucune écriture trouvées (pas
                // un réenvoi)
                need39 = false;
                assure = "True";
            } else if (!"True".equals(assure)) {
                // annonce des écriture du conjoint
                annonceAssure = false;
                // l'en-tête donnée n'est ici pas valide -> reset
                ci = null;
            }
            dCloture = annonce;
            // recherche des écritures
            CIEcritureAnnonceManager ecrMgr = new CIEcritureAnnonceManager();
            ecrMgr.setSession(session);
            ecrMgr.setForRassemblementOuvertureId(annonce.getRassemblementOuvertureId());
            ecrMgr.setForAssure(assure);
            ecrMgr.find(transaction);
            int ecritureInscrites = 0;

            FWPKProvider pkProvider = null;
            HEAnnoncesViewBean annoncevb = new HEAnnoncesViewBean(session);
            pkProvider = annoncevb.getNewPrimaryKeyProvider(((ecrMgr.size() * 2) + 2));

            // pour toutes les inscriptions
            for (int i = 0; i < ecrMgr.size(); i++) {
                CIEcriture ecriture = new CIEcriture();
                ecriture.setSession(session);
                if (annonceAssure) {
                    ecriture.setEcritureId(((CIEcritureAnnonce) ecrMgr.getEntity(i)).getIdEcritureAssure());
                } else {
                    ecriture.setEcritureId(((CIEcritureAnnonce) ecrMgr.getEntity(i)).getIdEcritureConjoint());
                }
                ecriture.retrieve(transaction);
                if (ci == null) {
                    ci = new CICompteIndividuel();
                    ci.setSession(session);
                    ci.setCompteIndividuelId(ecriture.getCompteIndividuelId());
                    ci.retrieve(transaction);
                }
                if (!ecriture.isNew() && !transaction.hasErrors()) {
                    if (annonceEcriture(transaction, ecriture, pkProvider)) {
                        ecritureInscrites++;
                    }
                }
            }
            // créer annonce de fin si ci additionnel non vide ou si réenvoi
            // dans tous les cas
            // if (nbrAnnonces != 0 ) {
            if ((nbrAnnonces != 0) || need39) {
                // nécessité d'avoir une en-tête, sinon, pas d'envoi possible.
                if (ci != null) {
                    annonceFin(ecritureInscrites, pkProvider);
                } else {
                    transaction.addErrors(session.getLabel("MSG_REENVOI_SANS_CI"));
                }
            }
            if (remoteTransaction.hasErrors() || transaction.hasErrors()) {
                remoteTransaction.rollback();
            } else {
                remoteTransaction.commit();
            }
        } finally {
            if (transactionOpened) {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception ex) {
                }
            }
        }
    }

    private void provideNewPkForAnnonceAndUpdateRefUnique(FWPKProvider pkProvider, boolean updateReferenceUnique)
            throws Exception {
        String newPK = pkProvider.getNextPrimaryKey();

        remoteEcritureAnnonce.put(ID_FIELD_INCREMENT_PROVIDER, newPK);

        if (updateReferenceUnique) {
            remoteEcritureAnnonce.put(ID_FIELD_REFERENCE_UNIQUE_PROVIDER, newPK);
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 12:23:59)
     * 
     * @return boolean
     * @param transaction
     *            globaz.globall.db.BTransaction
     * @param ecriture
     *            globaz.pavo.db.compte.CIEcriture
     */
    private boolean annonceEcriture(BTransaction transaction, CIEcriture ecriture, FWPKProvider pkProvider)
            throws Exception {

        boolean clotureFound = false;
        if (ecriture != null) {
            // tiers affilié
            AFAffiliation aff = null;
            if (!JAUtil.isStringEmpty(ecriture.getEmployeur())) {
                // recherche affilié
                aff = application.getAffilie(transaction, ecriture.getEmployeur(), null);
                /*
                 * application.getTiers( transaction, ecriture.getEmployeur(), new String[] { "getNom",
                 * "getLocaliteLong", "getNumAffilieActuel" });
                 */
            }
            // 38
            // enregistrement
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, String.valueOf((nbrAnnonces % 999) + 1));
            nbrAnnonces++;
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_1_OU_2, "1");
            // construction de la permière partie de l'annonce (identique pour
            // le 38 et le 39)
            buildHeader();
            // affilié
            boolean annonceCode2 = false;
            if (CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
                // recherche du ci partenaire
                CICompteIndividuel ciPartenaire = new CICompteIndividuel();
                ciPartenaire.setSession(session);
                ciPartenaire.setCompteIndividuelId(ecriture.getPartenaireId());
                ciPartenaire.retrieve(transaction);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, ciPartenaire.getNumeroAvsForSplitting());
                genre8Existe = true;
            } else {
                CIJournal journal = ecriture.getJournal(transaction, false);
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
                if (!affilieFound) { // recherche affilié
                    String numAff = "";

                    if (!JadeStringUtil.isBlank(ecriture.getAffHist())) {
                        if (!"93".equals(dCloture.getMotifArc()) && !"95".equals(dCloture.getMotifArc())) {
                            if (!JadeStringUtil.isBlank(ecriture.getLibelleAff())) {
                                annonceCode2 = true;
                            }
                        }
                        numAff = ecriture.getAffHist();
                        if (CIUtil.UnFormatNumeroAffilie(session, numAff).trim().length() > 11) {
                            numAff = numAff.substring(10);

                        }
                    } else if (aff != null) {
                        // numAff = aff.getNumAffilieActuel();
                        numAff = aff.getAffilieNumero();
                        // Modif 5.3 pour CCVS, si le num d'aff est >11 on subtr
                        if (numAff.length() > 11) {
                            numAff = numAff.substring(0, 10);
                        }
                        if (!"93".equals(dCloture.getMotifArc()) && !"95".equals(dCloture.getMotifArc())) {
                            annonceCode2 = true;
                        }
                    }
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AFILLIE, numAff);
                }
            } // chiffre clé extourne
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES,
                    CodeSystem.getCodeUtilisateur(ecriture.getExtourne(), session));
            // chiffre clé genre cotisation
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS,
                    CodeSystem.getCodeUtilisateur(ecriture.getGenreEcriture(), session));
            // chiffre clé particulier
            if (CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())
                    && !JAUtil.isIntegerEmpty(ecriture.getParticulier())) {
                String part = CodeSystem.getCodeUtilisateur(ecriture.getParticulier(), session);
                if (!JAUtil.isIntegerEmpty(part)) {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER, part);
                } else {
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER, "");
                }
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CHIFFRE_CLEF_PARTICULIER, "");
            } // part bta
            if (JAUtil.isIntegerEmpty(ecriture.getPartBta())) {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PART_BONIFICATIONS_ASSISTANCES, "");
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PART_BONIFICATIONS_ASSISTANCES, ecriture.getPartBta());
            } // code spécial
            if (JAUtil.isIntegerEmpty(ecriture.getCodeSpecial())) {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_SPECIAL, "");
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_SPECIAL,
                        CodeSystem.getCodeUtilisateur(ecriture.getCodeSpecial(), session));
            } // début
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
                totalEcr += revenuTr;
            } else {
                totalEcr -= revenuTr;
            }
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.REVENU, String.valueOf(revenuTr));
            // amortissement
            if (JAUtil.isIntegerEmpty(ecriture.getCode()) || CIEcriture.CS_CODE_PROVISOIRE.equals(ecriture.getCode())
                    || CIEcriture.CS_CODE_MIS_EN_COMTE.equals(ecriture.getCode())) {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_A_D_S, "");
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_A_D_S,
                        CodeSystem.getCodeUtilisateur(ecriture.getCode(), session));
            }
            // branche économique
            if (JAUtil.isIntegerEmpty(ecriture.getBrancheEconomique())) {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.BRANCHE_ECONOMIQUE, "");
            } else {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.BRANCHE_ECONOMIQUE,
                        CodeSystem.getCodeUtilisateur(ecriture.getBrancheEconomique(), session));
            }
            // année cotisation
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA, ecriture.getAnnee());
            // remoteEcritureAnnonce.
            provideNewPkForAnnonceAndUpdateRefUnique(pkProvider,
                    JadeStringUtil.isBlankOrZero(remoteEcritureAnnonce.getField(ID_FIELD_REFERENCE_UNIQUE_PROVIDER)));
            remoteEcritureAnnonce.add(remoteTransaction);
            // 38 code 2 nécessaire? : todo, pkoi table vide après add?
            if (annonceCode2) {
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_1_OU_2, "2");
                // parite information
                StringBuffer infoAff = new StringBuffer();
                if ((aff != null) && (aff.getTiers() != null)) {
                    String nomHist = "";
                    String prenomHist = "";
                    String dateSaisie = "31.12." + ecriture.getAnnee();
                    CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                            CIApplication.DEFAULT_APPLICATION_PAVO);
                    TIHistoriqueTiersManager histMgr = new TIHistoriqueTiersManager();
                    histMgr.setSession((BSession) application.getSessionTiers(session));
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
                    histMgr.setForDateDebutLowerOrEqualTo(dateSaisie);
                    histMgr.setForChamp(TITiers.FIELD_DESIGNATION2);
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
                                IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT, dateSaisie,
                                new TILocaliteLongFormater());
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
                            // infoAff.replace(25, 42, JAUtil.padString(loc,
                            // 42));
                        }
                    } else {
                        infoAff.insert(0, ecriture.getLibelleAff());
                    }
                    if (infoAff.length() > 42) {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PARTIE_INFORMATION, infoAff.substring(0, 42)
                                .toUpperCase());
                    } else {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.PARTIE_INFORMATION, infoAff.toString()
                                .toUpperCase());
                    }
                }
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                        String.valueOf((nbrAnnonces % 999) + 1));
                nbrAnnonces++;
                provideNewPkForAnnonceAndUpdateRefUnique(pkProvider, JadeStringUtil.isBlankOrZero(remoteEcritureAnnonce
                        .getField(ID_FIELD_REFERENCE_UNIQUE_PROVIDER)));
                remoteEcritureAnnonce.add(remoteTransaction);
            }
        }
        // }
        // }
        // }
        // return clotureFound;
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 13:10:20)
     */
    private void annonceFin(int nbrOfEcritures, FWPKProvider pkProvider) throws Exception {
        // annonce 39 01
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "39");
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "1");
        // première partie si vide
        if (nbrAnnonces == 0) {
            buildHeader();
        }
        // total
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.TOTAL_REVENUS, String.valueOf(Math.abs(totalEcr)));
        // positif/négatif
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_VALEUR_CHAMP_15, totalEcr < 0 ? "1" : "0");
        // nombre d'écritures
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI, String.valueOf(nbrOfEcritures));
        // ci additionnel ou ci normal
        if (CIRassemblementOuverture.CS_CI_ADDITIONNEL.equals(dCloture.getTypeEnregistrementWA())
                || CIRassemblementOuverture.CS_CI_ADDITIONNEL_SUSPENS.equals(dCloture.getTypeEnregistrementWA())) {
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CI_ADDITIONNEL, "1");
        } else {
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CI_ADDITIONNEL, "0");
        }
        // cas divorce

        if (CIRassemblementOuverture.CS_SPLITTING.equals(dCloture.getTypeEnregistrementWA())) {
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.SPLITTING_CAS_DIVORCE, "1");
        } else {
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.SPLITTING_CAS_DIVORCE, "0");
        }

        // date de transmission
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_TRANSMISSION, dateJJMMAA);
        // envoi
        try {
            provideNewPkForAnnonceAndUpdateRefUnique(pkProvider, true);
            remoteEcritureAnnonce.add(remoteTransaction);
        } catch (Exception inEx) {
            // System.out.println("Erreur");
        }
        // annonce 39 02: tot pkoi vide dans certains cas

        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "2");
        String nomPrenom = ci.getNomPrenom();
        // Modif nra upi, les annonces ne permettent pas une annonce > à 40
        if (nomPrenom.length() > 40) {
            nomPrenom = nomPrenom.substring(0, 40);
        }
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.ETAT_NOMINATIF, nomPrenom);
        // envoi
        provideNewPkForAnnonceAndUpdateRefUnique(pkProvider, false);
        remoteEcritureAnnonce.add(remoteTransaction);

    }

    private void buildHeader() {
        // caisse et agence
        String caisseTenant;
        String agenceTenant;
        if (application.isAnnoncesWA()) {
            caisseTenant = application.getProperty(CIApplication.CODE_CAISSE);
            agenceTenant = dCloture.getTypeEnregistrement().substring(3, 4);
            if (application.isAnciennesAnnonces()) {
                if ("0".equals(agenceTenant)) {
                    agenceTenant = "1";
                }
            }
        } else {
            // TODO recherche caisse et agence selon
            // dColture.getCaisseTenantCI()
            // temporaire
            if (application.isCaisseDifferente() && !JadeStringUtil.isBlankOrZero(dCloture.getRealCaisse())) {
                caisseTenant = dCloture.getRealCaisse();
            } else {
                caisseTenant = application.getProperty(CIApplication.CODE_CAISSE);
            }

            agenceTenant = dCloture.getCaisseTenantCI();
            if (application.isAnciennesAnnonces()) {
                if ("0".equals(agenceTenant)) {
                    agenceTenant = "1";
                }
            }
        }
        String caisseAgenceCommettante = application.getAdministration(session, dCloture.getCaisseCommettante(),
                new String[] { "getCodeAdministration" }).getCodeAdministration();
        String caisseCommettante = "";
        String agenceCommettante = "";
        if (caisseAgenceCommettante != null) {
            int sep = caisseAgenceCommettante.indexOf('.');
            if (sep != -1) {
                caisseCommettante = caisseAgenceCommettante.substring(0, sep);
                agenceCommettante = caisseAgenceCommettante.substring(sep + 1);
            } else {
                caisseCommettante = caisseAgenceCommettante;
            }
        }
        // caisse commettante
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE, caisseCommettante);
        // agence commettante
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE, agenceCommettante);
        // ayant droit
        if (CIRassemblementOuverture.CS_SPLITTING.equals(dCloture.getTypeEnregistrementWA())) {
            // splitting
            // TODO: recherche du conjoint
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE, ci.getNumeroAvs());
        } else {
            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE, ci.getNumeroAvs());
        }
        // motif
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, dCloture.getMotifArc());
        // reference interne
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE,
                dCloture.getReferenceInterne());
        // date clôture
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA,
                CIUtil.formatDateMMAA(dCloture.getDateCloture()));
        // date de l'ordre
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA,
                CIUtil.formatDateJJMMAA(dCloture.getDateOrdre()));
        // caisse tenant le CI
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE__CI, caisseTenant);
        // agence tenant le CI
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE_CI, agenceTenant);
        // assuré
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE, ci.getNumeroAvs());
    }

    /**
     * Envoie d'un CI additionnel. Peut être appelé pour un journal comptabilisé ou par un 21. Toutes les écritures
     * associées au ci spécifié sont lues afin d'identifier celles qui doivent être cloturées. Ces dernières seront
     * liées à la clôture et inclues dans un 38.
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @param ci
     *            le ci concerné
     * @param ecrituresAuCI
     *            est à true si les écritures à tester sont celles au CI, false sinon.
     * @param journalInscrit
     *            le journal lors d'un inscription au CI de ce dernier ou null si non applicable. Date de création :
     *            (20.12.2002 08:19:35)
     */
    public void doCIAdditionnel(BTransaction transaction, ArrayList listEcritures) throws Exception {
        dCloture = ci.getDerniereBClotureForCiAdditionnel(transaction);
        if (dCloture == null) {
            // pas de cloture
            return;
        }
        ArrayList ecritures = dCloture.addCIAdditionnel(transaction, listEcritures, false);
        // ne plus effectuer l'annonce maintenant
        /*
         * if (ecritures.size() == 0) { // pas d'écriture à clôturer return; } try {
         * remoteTransaction.openTransaction(); int ecritureInscrites = 0; // pour toutes les inscriptions for (int i =
         * 0; i < ecritures.size(); i++) { //CIEcriture ecriture = // ((CIEcriture)
         * ecritures.get(i)).aCloturer(transaction, dateCloture); CIEcriture ecriture = (CIEcriture) ecritures.get(i);
         * if (annonceEcriture(transaction, ecriture)) { ecritureInscrites++; } } // pour toutes les écritures if
         * (nbrAnnonces != 0) { annonceFin(ecritureInscrites); } if (remoteTransaction.hasErrors() ||
         * transaction.hasErrors()) { remoteTransaction.rollback(); } else { remoteTransaction.commit(); } } finally {
         * remoteTransaction.closeTransaction(); }
         */
    }

    /**
     * Commentaire relatif au constructeur CIAnnonceCIAdditionnel.
     * 
     * public CIAnnonceCIAdditionnel(BSession sessionParam) throws Exception { super(); ci = null; session =
     * sessionParam; init(); }
     */
    /**
     * Envoie d'un CI additionnel. Peut être appelé par une écriture comptabilisée. Toutes les écritures associées au ci
     * spécifié sont lues afin d'identifier celles qui doivent être cloturées. Ces dernières seront liées à la clôture
     * et inclues dans un 38.
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @param ci
     *            le ci concerné
     * @param ecrituresAuCI
     *            est à true si les écritures à tester sont celles au CI, false sinon.
     * @param journalInscrit
     *            le journal lors d'un inscription au CI de ce dernier ou null si non applicable. Date de création :
     *            (20.12.2002 08:19:35)
     */
    public void doCIAdditionnel(BTransaction transaction, CIEcriture ecriture) throws Exception {
        dCloture = ci.getDerniereBClotureForCiAdditionnel(transaction);
        if (dCloture == null) {
            // pas de cloture
            return;
        }
        ArrayList ecritureArray = new ArrayList();
        ecritureArray.add(ecriture);
        ArrayList ecritures = dCloture.addCIAdditionnel(transaction, ecritureArray, false);
        // ne plsu effectuer l'annonce maintenant
        /*
         * try { remoteTransaction.openTransaction(); annonceEcriture(transaction, ecriture); if (nbrAnnonces != 0) {
         * annonceFin(1); } if (remoteTransaction.hasErrors() || transaction.hasErrors()) {
         * remoteTransaction.rollback(); } else { remoteTransaction.commit(); } } finally {
         * remoteTransaction.closeTransaction(); }
         */
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
     * Insérez la description de la méthode ici. Date de création : (23.04.2003 12:53:39)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    private void init() throws java.lang.Exception {
        // dateCloture = new JADate(ci.getDerniereCloture(true));
        application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        dateJJMMAA = JACalendar.today().toString();
        dateJJMMAA = dateJJMMAA.substring(0, 4) + dateJJMMAA.substring(6);
    }

    /**
     * Initialisation des annonces dans HERMES
     */
    public void initAnnonce(BISession rSession, BITransaction rTransaction) throws Exception {
        // création d'un transaction remote
        remoteTransaction = rTransaction;
        // création de l'API pour l'annonce
        remoteEcritureAnnonce = (IHEInputAnnonce) rSession.getAPIFor(IHEInputAnnonce.class);
        remoteEcritureAnnonce.setIdProgramme(CIApplication.DEFAULT_APPLICATION_PAVO);
        remoteEcritureAnnonce.setUtilisateur(session.getUserId());
        remoteEcritureAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);
        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "38");
    }
}
