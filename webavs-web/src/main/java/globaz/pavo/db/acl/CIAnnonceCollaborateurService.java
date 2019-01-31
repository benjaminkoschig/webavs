package globaz.pavo.db.acl;

import globaz.commons.nss.NSUtil;
import globaz.commons.nss.db.NSSinfo;
import globaz.commons.nss.db.NSSinfoManager;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.print.itext.HEDocumentRemiseAttestCA;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIExceptions;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.constantes.IConstantes;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author BJO Cette classe offre des services pour les annonces de collaborateurs (ACL - EBusiness)
 */
public abstract class CIAnnonceCollaborateurService {

    // MOTIFS POUR LES ARC
    private final static String ARC_MOTIF_31 = "31";
    private final static String ARC_MOTIF_33 = "33";
    private final static String ARC_MOTIF_61 = "61";
    private final static String ARC_MOTIF_67 = "67";

    // Statut de l'ARC
    public final static String ARC_STATUT_PROBLEME = IHEAnnoncesViewBean.CS_PROBLEME;
    public final static String ARC_STATUT_TERMINE = IHEAnnoncesViewBean.CS_TERMINE;

    // REFERENCE INTERNE POUR LES ARC
    private final static String REFINTERNE_ACL = "ACL";
    private final static String REFINTERNE_EXCEPTION = "EX";

    /**
     * Détermine si un CI correspondant au nss existe dans la caisse et le retourne le cas échéant
     * 
     * @param session
     * @param nss
     * @return le Ci si il existe dans la caisse
     * @throws Exception
     */
    private static CICompteIndividuel CiNnssExist(BSession session, String nss) throws Exception {
        CICompteIndividuel compteIndividuel = null;

        // Test qu'on a bien la session et le nss valide
        if (session == null) {
            throw new Exception("The session cannot be null : ");
        }
        if (JadeStringUtil.isBlank(nss)) {
            throw new Exception("The nss cannot be null or empty : ");
        }

        // Recherche du CI
        try {
            CICompteIndividuelManager manager = new CICompteIndividuelManager();
            manager.setSession(session);
            manager.setForNumeroAvs(NSUtil.unFormatAVS(nss));
            manager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            manager.find();
            if (manager.size() > 0) {
                compteIndividuel = (CICompteIndividuel) manager.getFirstEntity();
            }
        } catch (Exception e) {
            throw new Exception("Unable to search CI for the nss : " + nss, e);
        }

        return compteIndividuel;
    }

    /**
     * Crée une CIException (permet de lier un assuré à un affilié pour le pré-remplissage de la DS de l'affilié)
     * 
     * @param session
     * @param compteIndividuel
     * @param numeroAffilie
     * @param dateEngagement
     * @return le chemin de l'attestation d'assurance générée
     * @throws Exception
     */
    private static String createException(BSession session, CICompteIndividuel compteIndividuel, String numeroAffilie,
            String dateEngagement, String noEmploye, String noSuccursale) throws Exception {
        if ((session == null) || session.hasErrors()) {
            throw new Exception("The session is on Error!!!");
        }

        if ((compteIndividuel == null) || JadeStringUtil.isBlank(compteIndividuel.getId())) {
            throw new Exception("idCompteIndividuel cannot be null");
        }
        String idAffilie = CIAnnonceCollaborateurService.getIdAffilie(session, numeroAffilie);
        if (JadeStringUtil.isBlank(idAffilie)) {
            throw new Exception("idAffilie cannot be null");
        }

        // Création de l'exception
        CIExceptions ciException = new CIExceptions();
        ciException.setSession(session);
        ciException.setIdCompteIndividuel(compteIndividuel.getId());
        ciException.setIdAffiliation(idAffilie);
        ciException.setDateEngagement(dateEngagement);
        ciException.wantCallMethodAfter(false);
        ciException.wantCallMethodBefore(true);
        ciException.wantCallValidate(false);

        // Ajout de l'exception et impression du document
        try {
            ciException.add();
            String attestationAssuranceLocation = CIAnnonceCollaborateurService.imprimerAttestationAssurance(
                    CIAnnonceCollaborateurService.getSessionHermes(session), ciException, noEmploye, noSuccursale);

            if (!JadeStringUtil.isBlank(attestationAssuranceLocation)) {
                return attestationAssuranceLocation;
            } else {
                throw new Exception("docLocation is null");
            }
        } catch (Exception e) {
            throw new Exception("Unable to add CIException", e);
        }
    }

    /**
     * Recherche dans la table NSSRA en fonction du nss
     * 
     * @param session
     * @param nss
     * @return un objet de type NSSinfo
     * @throws Exception
     */
    private static NSSinfo findInNssra(BSession session, String nss) throws Exception {
        NSSinfo nssInfo = null;

        // Test qu'on a bien la session et le nss
        if (session == null) {
            throw new Exception("The session cannot be null : ");
        }
        if (JadeStringUtil.isBlank(nss)) {
            throw new Exception("The navs cannot be null or empty : ");
        }

        // Recherche en fonction du nss
        try {
            NSSinfoManager manager = new NSSinfoManager();
            manager.setSession(session);
            if (CIAnnonceCollaborateurService.isNavs13(nss)) {
                manager.setForNNSS(NSUtil.unFormatAVS(nss));

            } else {
                manager.setForNAVS(NSUtil.unFormatAVS(nss));
            }
            manager.setForCodeMutation("0");
            manager.setForValidite("1");
            manager.find();
            if (manager.size() > 0) {
                nssInfo = (NSSinfo) manager.getFirstEntity();
            } else {
                manager.setForValidite("0");
                manager.find();
                if (manager.size() > 0) {
                    // Récupération du nouveau numéro
                    nssInfo = (NSSinfo) manager.getFirstEntity();
                }
            }
        } catch (Exception e) {
            throw new Exception("Unable to find nss in the table NSSRA for the navs : " + nss, e);
        }

        return nssInfo;
    }

    /**
     * Permet de générer un ARC (dans HERMES). Attention les Exceptions sont parfois traitées par HERMES et donc pas
     * remontées
     * 
     * @param session
     * @param nss
     * @param numeroAffilie
     * @param dateEngagement
     * @param motifArc
     * @param refInterne
     * @throws Exception
     */
    private static void genererArc(BSession session, String nss, String numeroAffilie, String dateEngagement,
            String motifArc, String refInterne, String noEmploye, String noSuccursale) throws Exception {
        try {
            // Création d'une session Hermes
            BSession sessionHermes = CIAnnonceCollaborateurService.getSessionHermes(session);
            IHEInputAnnonce inputAnnonce = (IHEInputAnnonce) session.getAPIFor(IHEInputAnnonce.class);

            inputAnnonce.setISession(sessionHermes);

            inputAnnonce.wantCheckCiOuvert(IHEAnnoncesViewBean.WANT_CHECK_CI_OUVERT_FALSE);
            inputAnnonce.wantCheckNumAffilie(IHEAnnoncesViewBean.WANT_CHECK_NUM_AFF_FALSE);

            inputAnnonce.setCategorie(IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR);
            inputAnnonce.setIdProgramme("PAVO");
            inputAnnonce.setUtilisateur(session.getUserId());
            inputAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);
            inputAnnonce.setPriorite(IHELotViewBean.LOT_PTY_BASSE);
            inputAnnonce.setNumeroAffilie(numeroAffilie);
            inputAnnonce.setDateEngagement(dateEngagement);
            inputAnnonce.setNumeroEmploye(noEmploye);
            inputAnnonce.setNumeroSuccursale(noSuccursale);

            inputAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
            inputAnnonce.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, motifArc);
            inputAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE, nss);
            inputAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
            inputAnnonce.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, refInterne);
            inputAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE,
                    CIAnnonceCollaborateurService.getPropertyPavo("noCaisse"));
            inputAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE,
                    CIAnnonceCollaborateurService.getPropertyPavo("noAgence"));

            // Ajout de l'ARC
            inputAnnonce.add(sessionHermes.getCurrentThreadTransaction());
            // BZ 8907 - si il y a une erreur dans HERMES il faut la faire suivre dans la session ORION afin que l'ACL
            // puisse être mise en problème
            if (sessionHermes.hasErrors() || sessionHermes.getCurrentThreadTransaction().hasErrors()) {
                session.addError(sessionHermes.getCurrentThreadTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Unable to generate ARC", e);
        }
    }

    /**
     * Renvoi l'affiliation associé à l'id
     * 
     * @param idAffiliation
     * @return l'affiliation
     * @throws Exception
     */
    private static AFAffiliation getAffiliation(BSession session, String idAffiliation) throws Exception {
        // recherche du ci
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(session);
        affiliation.setId(idAffiliation);
        try {
            affiliation.retrieve();
            return affiliation;
        } catch (Exception e) {
            throw new Exception("Unable to retrieve entity AFAffiliation", e);
        }
    }

    /**
     * Renvoi le compteIndividuel associé à l'id
     * 
     * @param idCompteIndividuel
     * @return le compte individuel
     * @throws Exception
     */
    private static CICompteIndividuel getCompteIndividuel(BSession session, String idCompteIndividuel) throws Exception {
        // recherche du ci
        CICompteIndividuel compteIndividuel = new CICompteIndividuel();
        compteIndividuel.setSession(session);
        compteIndividuel.setId(idCompteIndividuel);
        try {
            compteIndividuel.retrieve();
            return compteIndividuel;
        } catch (Exception e) {
            throw new Exception("Unable to retrieve entity CICompteIndividuel", e);
        }
    }

    /**
     * Retourne l'id de l'affilié en fonction de son numéro d'affilié
     * 
     * @param session
     * @param numeroAffilie
     * @return L'id de l'affilié
     * @throws Exception
     */
    private static String getIdAffilie(BSession session, String numeroAffilie) throws Exception {
        if (JadeStringUtil.isBlank(numeroAffilie)) {
            throw new Exception("NumeroAffilie cannot be null");
        }

        String idAffilie = null;

        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(session);
        manager.setForAffilieNumero(numeroAffilie);
        manager.setForTypesAffParitaires();
        try {
            manager.find();
        } catch (Exception e) {
            throw new Exception("Unable to search idAffilie for " + numeroAffilie, e);
        }

        if (manager.size() > 0) {
            AFAffiliation affilie = (AFAffiliation) manager.getFirstEntity();
            idAffilie = affilie.getId();
        } else {
            throw new Exception("idAffilie not found for " + numeroAffilie);
        }

        return idAffilie;
    }

    /**
     * Permet d'obtenir une propriété de PAVO.properties
     * 
     * @param propertyName
     * @return la valeur de la propriété
     * @throws Exception
     */
    private static String getPropertyPavo(String propertyName) throws Exception {
        try {
            BSession session = new BSession();
            session.setApplication("PAVO");
            CIApplication application = (CIApplication) session.getApplication();
            return application.getProperty(propertyName);
        } catch (Exception e) {
            throw new Exception("Unable to find property : " + propertyName, e);
        }
    }

    /**
     * Retourne une session HERMES
     * 
     * @param session
     * @return une session HERMES
     * @throws RemoteException
     * @throws Exception
     */
    private static BSession getSessionHermes(BSession session) throws RemoteException, Exception {
        return (BSession) GlobazSystem.getApplication("HERMES").newSession(session);
    }

    /**
     * Retourne une session PAVO
     * 
     * @param session
     * @return une session PAVO
     * @throws RemoteException
     * @throws Exception
     */
    private static BSession getSessionPavo(BSession session) throws RemoteException, Exception {
        return (BSession) GlobazSystem.getApplication("PAVO").newSession(session);
    }

    /**
     * Imprime une attestation d'assurance et retourne le chemin du fichier généré
     * 
     * @param sessionHermes
     * @param ciException
     * @return le chemin du fichier généré
     * @throws Exception
     */
    private static String imprimerAttestationAssurance(BSession sessionHermes, CIExceptions ciException,
            String noEmploye, String noSuccursale) throws Exception {
        if (!JadeStringUtil.isBlank(ciException.getIdCompteIndividuel())
                && !JadeStringUtil.isBlank(ciException.getIdAffiliation())) {
            try {
                // Récupération du CI et de l'affiliation
                CICompteIndividuel compteIndividuel = CIAnnonceCollaborateurService.getCompteIndividuel(sessionHermes,
                        ciException.getIdCompteIndividuel());
                AFAffiliation affiliation = CIAnnonceCollaborateurService.getAffiliation(sessionHermes,
                        ciException.getIdAffiliation());

                // Création du document
                HEDocumentRemiseAttestCA docRemiseCa = new HEDocumentRemiseAttestCA();
                docRemiseCa.setSingle("true");
                String[] nomPrenomTab = CIAnnonceCollaborateurService.separerNomPrenom(compteIndividuel.getNomPrenom());
                if (nomPrenomTab.length > 1) {
                    docRemiseCa.setNom(nomPrenomTab[0]);
                    docRemiseCa.setPrenom(nomPrenomTab[1]);
                }
                docRemiseCa.setDateNaiss(compteIndividuel.getDateNaissance());
                docRemiseCa.setNnss(NSUtil.formatAVSUnknown(compteIndividuel.getNumeroAvs()));
                docRemiseCa.setAnneeCot(ciException.getDateEngagement());
                docRemiseCa.setNAffilie(affiliation.getAffilieNumero());
                docRemiseCa.setNoEmploye(noEmploye);
                docRemiseCa.setNoSuccursale(noSuccursale);

                if (affiliation != null) {
                    // PO 9217
                    docRemiseCa.setAdresse(affiliation.getTiers().getAdresseAsString(null,
                            IConstantes.CS_AVOIR_ADRESSE_COURRIER, CIApplication.CS_DOMAINE_ADRESSE_CI_ARC,
                            JACalendar.todayJJsMMsAAAA(), affiliation.getAffilieNumero()));

                    String titre = affiliation.getTiers().getFormulePolitesse(affiliation.getTiers().getLangueIso());
                    docRemiseCa.setLangueSingle(affiliation.getTiers().getLangueIso());
                    docRemiseCa.setPolitesse(titre);
                }

                docRemiseCa.setSession(sessionHermes);
                docRemiseCa.setDocumentTitle(sessionHermes.getLabel("HERMES_10058"));

                // Création du document
                docRemiseCa.executeProcess();

                // Récupération de l'url du fichier généré
                /*
                 * String docRemiseCaUrlFileName = docRemiseCa.getDocumentInfo().getCurrentPathName();
                 * System.out.println(docRemiseCaUrlFileName);
                 */

                List attachedDocuments = docRemiseCa.getAttachedDocuments();// getAttachedDocuments ne retourne jamais
                                                                            // null mais une liste vide
                if ((attachedDocuments.size() > 0) && (attachedDocuments.get(0) != null)) {
                    JadePublishDocument doc = (JadePublishDocument) attachedDocuments.get(0);
                    String docRemiseCaLocation = doc.getDocumentLocation();
                    if (!JadeStringUtil.isBlank(docRemiseCaLocation)) {
                        return docRemiseCaLocation;
                    } else {
                        throw new Exception("docLocation is null!");
                    }
                } else {
                    throw new Exception("No attachedDocuments");
                }
            } catch (Exception e) {
                throw new Exception("Unable to create document HEDocumentRemiseAttestCA", e);
            }
        } else {
            throw new Exception("idCompteIndividuel or idAffiliation is null!");
        }
    }

    /**
     * Détermine si il s'agit d'un numéro AVS à 13 chiffres
     * 
     * @param nss
     * @return true si il s'agit d'un NAVS13
     * @throws Exception
     */
    private static boolean isNavs13(String nss) throws Exception {
        String nssUnformated = "";
        try {
            nssUnformated = NSUtil.unFormatAVS(nss);
            if ((nssUnformated.length() == 13) && nssUnformated.substring(0, 3).trim().equals("756")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception("Unable to determine if nss is a NAVS13", e);
        }
    }

    /**
     * Permet de retourner le nom et prénom sépraré par une virgule dans un tableau : tab[0]=nom tab[1]=prenom
     * 
     * @param nomPrenom
     * @return un tableau contenant le nom et le prénom
     * @throws Exception
     */
    private static String[] separerNomPrenom(String nomPrenom) throws Exception {
        if (JadeStringUtil.indexOf(nomPrenom, ',') != -1) {
            return nomPrenom.split(",");
        } else {
            return nomPrenom.split(" ");
        }
    }

    /**
     * Vérifie si la centrale à répondu à l'ARC et crée une CIException si nécessaire
     * 
     * @param transaction
     * @param idAnnonceCollaborateur
     * @param nss
     * @param dateEngagement
     * @param duplicata
     * @param numeroAffilie
     * @return un objet de type CITreatAclEnCoursResult
     * @throws Exception
     */
    public static CITreatAclEnCoursResult treatAclEnCours(BTransaction transaction, String idAnnonceCollaborateur,
            String nss, String nouveauNss, String dateEngagement, Boolean duplicata, String numeroAffilie,
            String noEmploye, String noSuccursale) throws Exception {
        try {
            String refInterne = CIAnnonceCollaborateurService.REFINTERNE_ACL + "/" + idAnnonceCollaborateur + "//";
            CITreatAclEnCoursResult resultAclContainer = new CITreatAclEnCoursResult();

            // Recherche l'ARC correspondant à l'id de l'ACL dans la table
            // HEANNOP
            HEOutputAnnonceListViewBean heOutputArc = new HEOutputAnnonceListViewBean();
            heOutputArc.setSession(transaction.getSession());
            heOutputArc.setForCodeApplication("11");
            // pour optimisation des performances
            if (!JadeStringUtil.isBlankOrZero(nouveauNss)) {
                heOutputArc.setForNumeroAVS(NSUtil.unFormatAVS(nouveauNss));
            }
            heOutputArc.setForReferenceInterne(refInterne);
            heOutputArc.find();

            // Si un seul arc est trouvé
            if (heOutputArc.size() == 1) {
                HEOutputAnnonceViewBean arc = (HEOutputAnnonceViewBean) heOutputArc.getFirstEntity();

                // Si le statut de l'ARC est à terminé => ACL TERMINE
                if ((arc != null) && arc.getStatut().equals(CIAnnonceCollaborateurService.ARC_STATUT_TERMINE)) {
                    resultAclContainer.setArcStatut(CIAnnonceCollaborateurService.ARC_STATUT_TERMINE);
                    // Regarder si la référence interne contient l'abréviation EX indiquant qu'il faut créer une CIEX
                    if (arc.getChampEnregistrement().contains(
                            refInterne + CIAnnonceCollaborateurService.REFINTERNE_EXCEPTION)) {
                        // Recherche du CI
                        CICompteIndividuel compteIndividuel = CIAnnonceCollaborateurService.CiNnssExist(
                                transaction.getSession(), arc.getNumeroAVS());
                        if (compteIndividuel == null) {
                            compteIndividuel = CIAnnonceCollaborateurService.CiNnssExist(transaction.getSession(), nss);
                        }
                        if (compteIndividuel != null) {
                            // Création de l'exception et impression de l'attestation
                            String attestationAssuranceLocation = CIAnnonceCollaborateurService.createException(
                                    transaction.getSession(), compteIndividuel, numeroAffilie, dateEngagement,
                                    noEmploye, noSuccursale);
                            if (!JadeStringUtil.isBlank(attestationAssuranceLocation)) {
                                resultAclContainer.setAttestationAssuranceLocation(attestationAssuranceLocation);
                            }
                        }
                    }
                }
                // Si le statut de l'arc est à problème => ACL PROBLEME
                if ((arc != null) && arc.getStatut().equals(CIAnnonceCollaborateurService.ARC_STATUT_PROBLEME)) {
                    resultAclContainer.setArcStatut(CIAnnonceCollaborateurService.ARC_STATUT_PROBLEME);
                }
            }
            // Si plusieurs arcs sont trouvés, on doit vérifier que tous les arc soient à l'état terminé
            if (heOutputArc.size() > 1) {
                boolean termined = true;
                for (int i = 0; i < heOutputArc.size(); i++) {
                    HEOutputAnnonceViewBean arc = (HEOutputAnnonceViewBean) heOutputArc.getEntity(i);
                    if (termined && arc.getStatut().equals(CIAnnonceCollaborateurService.ARC_STATUT_TERMINE)) {
                        continue;
                    } else if (arc.getStatut().equals(CIAnnonceCollaborateurService.ARC_STATUT_PROBLEME)) {
                        termined = false;
                        resultAclContainer.setArcStatut(CIAnnonceCollaborateurService.ARC_STATUT_PROBLEME);
                        break;
                    } else {
                        termined = false;
                        continue;
                    }
                }
                if (termined) {
                    resultAclContainer.setArcStatut(CIAnnonceCollaborateurService.ARC_STATUT_TERMINE);

                    // Regarder si la référence interne contient l'abréviation EX indiquant qu'il faut créer une CIEX
                    HEOutputAnnonceViewBean firstArc = (HEOutputAnnonceViewBean) heOutputArc.getFirstEntity();
                    if (firstArc.getChampEnregistrement().contains(
                            refInterne + CIAnnonceCollaborateurService.REFINTERNE_EXCEPTION)) {
                        // Recherche du CI
                        CICompteIndividuel compteIndividuel = CIAnnonceCollaborateurService.CiNnssExist(
                                transaction.getSession(), firstArc.getNumeroAVS());
                        if (compteIndividuel == null) {
                            compteIndividuel = CIAnnonceCollaborateurService.CiNnssExist(transaction.getSession(), nss);
                        }
                        if (compteIndividuel != null) {
                            // Création de l'exception et impression de l'attestation
                            String attestationAssuranceLocation = CIAnnonceCollaborateurService.createException(
                                    transaction.getSession(), compteIndividuel, numeroAffilie, dateEngagement,
                                    noEmploye, noSuccursale);
                            if (!JadeStringUtil.isBlank(attestationAssuranceLocation)) {
                                resultAclContainer.setAttestationAssuranceLocation(attestationAssuranceLocation);
                            }
                        }
                    }
                }
            }
            return resultAclContainer;
        } catch (Exception e) {
            throw new Exception("Unable to treat ACL en cours", e);
        }
    }

    /**
     * Détermine le motif de l'ARC et génère l'ARC (dans HERMES) Détermine si il faut générer une exception dans PAVO en
     * l'indiquant dans la référence interne de l'ARC (permet de lier un assuré à un affilié pour le pré-remplissage de
     * la DS de l'affilié)
     * 
     * @param transaction
     * @param idAnnonceCollaborateur
     * @param nss
     * @param dateEngagement
     * @param duplicata
     * @param numeroAffilie
     * @return un objet de type CITreatAclSaisieResult
     * @throws Exception
     */
    public static CITreatAclSaisieResult treatAclSaisie(BTransaction transaction, String idAnnonceCollaborateur,
            String nss, String dateEngagement, Boolean duplicata, String numeroAffilie, String noEmploye,
            String noSuccursale) throws Exception {
        try {
            boolean success = false;
            String typeArc = "";
            String refInterne = CIAnnonceCollaborateurService.REFINTERNE_ACL + "/" + idAnnonceCollaborateur + "//";

            // Création de l'objet à retourner à la fin du service
            CITreatAclSaisieResult resultAclContainer = new CITreatAclSaisieResult();

            if (CIAnnonceCollaborateurService.isNavs13(nss)) {
                if (duplicata.booleanValue()) {
                    CICompteIndividuel compteIndividuel = CIAnnonceCollaborateurService.CiNnssExist(
                            transaction.getSession(), nss);
                    if (compteIndividuel != null) {
                        // arc motif = 31 + CIException (S190124_003)
                        resultAclContainer.loadInfoFromCi(compteIndividuel, transaction.getSession());
                        typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_31 + " + Exception";
                        CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_31, refInterne
                                        + CIAnnonceCollaborateurService.REFINTERNE_EXCEPTION, noEmploye, noSuccursale);
                    } else {
                        NSSinfo nssInfo = CIAnnonceCollaborateurService.findInNssra(transaction.getSession(), nss);
                        if (nssInfo != null) {
                            resultAclContainer.loadInfoFromNssInfo(nssInfo, transaction.getSession());
                        }
                        if ((nssInfo != null)
                                && CIUtil.isRetraite(new JADate(nssInfo.getDateNaissance()), nssInfo.getSexe(),
                                        Integer.parseInt(JACalendar.todayJJsMMsAAAA().substring(6)))) {
                            // arc motif = 67 + 31 (S190124_003)
                            typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_67 + " + "
                                    + CIAnnonceCollaborateurService.ARC_MOTIF_31;
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_67, refInterne, noEmploye,
                                    noSuccursale);
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_31, refInterne, noEmploye,
                                    noSuccursale);
                        } else {
                            // arc motif = 43 -> Remplacé par l'ARC 31 le 01.01.19 oui, mais il faut aussi l'ouverture
                            // (S190124_003)
                            typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_61 + " + "
                                    + CIAnnonceCollaborateurService.ARC_MOTIF_31;
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_61, refInterne, noEmploye,
                                    noSuccursale);
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_31, refInterne, noEmploye,
                                    noSuccursale);
                        }
                    }
                } else {
                    CICompteIndividuel compteIndividuel = CIAnnonceCollaborateurService.CiNnssExist(
                            transaction.getSession(), nss);
                    if (compteIndividuel != null) {
                        // CIException (si ouvert) ou arc motif = 65/67 (si
                        // cloturé)
                        resultAclContainer.loadInfoFromCi(compteIndividuel, transaction.getSession());
                        typeArc = "Exception";
                        if (compteIndividuel.isCiOuvert()) {
                            // Création d'une CIException
                            String attestationAssuranceLocation = CIAnnonceCollaborateurService.createException(
                                    transaction.getSession(), compteIndividuel, numeroAffilie, dateEngagement,
                                    noEmploye, noSuccursale);
                            resultAclContainer.setAttestationAssuranceLocation(attestationAssuranceLocation);
                        } else {
                            if (CIUtil.isRetraite(new JADate(compteIndividuel.getDateNaissance()),
                                    compteIndividuel.getSexe(),
                                    Integer.parseInt(JACalendar.todayJJsMMsAAAA().substring(6)))) {
                                // arc motif = 67
                                typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_67;
                                CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                        dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_67, refInterne,
                                        noEmploye, noSuccursale);
                            } else {
                                // arc motif = 65 -> Remplacé par l'ARC 61 le 01.01.19
                                typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_61;
                                CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                        dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_61, refInterne,
                                        noEmploye, noSuccursale);
                            }
                        }
                    } else {
                        NSSinfo nssInfo = CIAnnonceCollaborateurService.findInNssra(transaction.getSession(), nss);
                        if (nssInfo != null) {
                            resultAclContainer.loadInfoFromNssInfo(nssInfo, transaction.getSession());
                        }
                        if ((nssInfo != null)
                                && CIUtil.isRetraite(new JADate(nssInfo.getDateNaissance()), nssInfo.getSexe(),
                                        Integer.parseInt(JACalendar.todayJJsMMsAAAA().substring(6)))) {
                            // arc motif = 67
                            typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_67;
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_67, refInterne, noEmploye,
                                    noSuccursale);
                        } else {
                            // arc motif = 61
                            typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_61;
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_61, refInterne, noEmploye,
                                    noSuccursale);
                        }
                    }
                }
            } else {
                NSSinfo nssInfo = CIAnnonceCollaborateurService.findInNssra(transaction.getSession(), nss);

                if ((nssInfo != null) && !JadeStringUtil.isBlank(nssInfo.getNNSS())) {
                    String navs13 = nssInfo.getNNSS();
                    CICompteIndividuel compteIndividuel = CIAnnonceCollaborateurService.CiNnssExist(
                            transaction.getSession(), navs13);
                    if (compteIndividuel != null) {
                        // arc motif = 31 + CIException (S190124_003)
                        resultAclContainer.loadInfoFromCi(compteIndividuel, transaction.getSession());
                        typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_31 + " + Exception";
                        CIAnnonceCollaborateurService.genererArc(transaction.getSession(), navs13, numeroAffilie,
                                dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_31, refInterne
                                        + CIAnnonceCollaborateurService.REFINTERNE_EXCEPTION, noEmploye, noSuccursale);
                    } else {
                        resultAclContainer.loadInfoFromNssInfo(nssInfo, transaction.getSession());
                        if (CIUtil.isRetraite(new JADate(nssInfo.getDateNaissance()), nssInfo.getSexe(),
                                Integer.parseInt(JACalendar.todayJJsMMsAAAA().substring(6)))) {
                            // arc motif = 67 + 31 (S190124_003)
                            typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_67 + " + "
                                    + CIAnnonceCollaborateurService.ARC_MOTIF_31;
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), navs13, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_67, refInterne, noEmploye,
                                    noSuccursale);
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), navs13, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_31, refInterne, noEmploye,
                                    noSuccursale);
                        } else {
                            // arc motif = 43 -> Remplacé par l'ARC 31 le 01.01.19 oui, mais il faut aussi faire
                            // l'ouverture (S190124_003)
                            typeArc = CIAnnonceCollaborateurService.ARC_MOTIF_61 + " + "
                                    + CIAnnonceCollaborateurService.ARC_MOTIF_31;
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), nss, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_61, refInterne, noEmploye,
                                    noSuccursale);
                            CIAnnonceCollaborateurService.genererArc(transaction.getSession(), navs13, numeroAffilie,
                                    dateEngagement, CIAnnonceCollaborateurService.ARC_MOTIF_31, refInterne, noEmploye,
                                    noSuccursale);
                        }
                    }
                } else {
                    // Erreur de l'utilisateur
                    resultAclContainer.setSuccess(false);
                    resultAclContainer.setTypeArc(CIAnnonceCollaborateurService
                            .getSessionPavo(transaction.getSession()).getLabel("ERREUR_UTILISATEUR"));
                    return resultAclContainer;
                }
            }

            // Vérifie si la transaction ou la session est en erreur (car HERMES
            // ne remonte pas toutes les exceptions, dans la méthode
            // genererArc())
            if (transaction.hasErrors() || transaction.getSession().hasErrors()) {
                success = false;
            } else {
                success = true;
            }

            resultAclContainer.setSuccess(success);
            resultAclContainer.setTypeArc(typeArc);

            return resultAclContainer;
        } catch (Exception e) {
            throw new Exception("Unable to treat ACL saisie");
        }
    }
}
