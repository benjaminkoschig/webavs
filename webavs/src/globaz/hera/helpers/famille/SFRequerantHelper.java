package globaz.hera.helpers.famille;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuRelationConjoint;
import globaz.hera.db.famille.SFApercuRelationConjointManager;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFApplicationContextDTO;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFEnfantManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFMembreFamilleManager;
import globaz.hera.db.famille.SFRelationFamilialeRequerant;
import globaz.hera.db.famille.SFRequerant;
import globaz.hera.db.famille.SFRequerantDTO;
import globaz.hera.db.famille.SFRequerantManager;
import globaz.hera.exception.SFBusinessException;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.SFSessionDataContainerHelper;
import globaz.hera.tools.nss.SFUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Classe contenant quelques méthodes sur le requérant et les membres de famille.
 * 
 * @author JPA
 */
public class SFRequerantHelper extends FWHelper {

    /**
     * @return Retourne le membre de la famille ajouté.
     */
    public SFMembreFamille ajouterMembre(String idTiers, String numAvs, String nom, String prenom, String sexe,
            String dateNaissance, String dateDeces, String nationalite, String cantonDomicile, String csDomaine,
            String codePays, BSession session, BITransaction transaction) throws Exception {

        SFMembreFamille membre = new SFMembreFamille();
        membre.setSession(session);
        String errorMessage = "";

        // Si idTiers renseigné, tous les autres valeurs sont inutiles, excepté le canton (et le pays de domicile si pas
        // de canton et d'adresse de courier dans les tiers).
        // Il n'est pas possible de mettre à jours le canton dans les tiers, on le stocke donc en local.
        // On contrôle que le membre n'existe pas déjà
        if (idTiers != null) {
            membre.setSession(session);
            membre.setIdTiers(idTiers);
            membre.setCsDomaineApplication(csDomaine);
            membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            membre.retrieve(transaction);
            if (membre.isNew()) {
                SFTiersWrapper tw = SFTiersHelper.getTiersParId(session, idTiers);

                // Gestion du canton
                if (tw != null) {
                    String idCanton = tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON);
                    if (JadeStringUtil.isIntegerEmpty(idCanton)) {
                        membre.setCsCantonDomicile(cantonDomicile);
                    }
                }

                // Gestion du pays de domicile (si canton vide et pas d'adresse de domicile dans les tiers)
                String adresseCourier = SFTiersHelper.getAdresseCourrierFormatee(session, idTiers, null, csDomaine);
                if (JadeStringUtil.isIntegerEmpty(cantonDomicile)) {
                    if (JadeStringUtil.isBlank(adresseCourier)) {
                        membre.setPays(codePays);
                    }
                }

                // on crée le membre
                membre.add(transaction);
            }
        } else {
            if (!JadeStringUtil.isEmpty(numAvs)) {
                // on crée le tiers et on sauvegarde l'id tiers dans la
                // table membres de famille
                String idTiers2 = SFTiersHelper.addTiers(session, numAvs, nom, prenom, sexe, dateNaissance, dateDeces,
                        nationalite, cantonDomicile, null, null);

                // On contrôle que le tiers ait bien été inséré, car les
                // exception ne sont pas remontée par les interfaces.
                if (!JadeStringUtil.isIntegerEmpty(idTiers2)) {
                    SFTiersWrapper tWrapper = SFTiersHelper.getTiersParId(session, idTiers2);
                    if (tWrapper == null) {
                        errorMessage = session.getLabel("ERROR_ADD_TIERS");
                    } else {
                        membre.setIdTiers(idTiers2);
                        membre.setCsCantonDomicile(cantonDomicile);
                        membre.setPays(codePays);
                        membre.setCsDomaineApplication(csDomaine);
                        membre.add(transaction);
                    }
                } else {
                    errorMessage = session.getLabel("ERROR_ADD_TIERS");
                }
                if (session.hasErrors()) {
                    errorMessage = session.getErrors().toString();
                }
            }
            // On stocke en tant que membre de la famille uniquement, aucune
            // référence dans les tiers
            else {
                // Les autres champs rentrent en compte pour la recherche on a
                // aucune info sur le numéro avs
                // Il faut au minimum le nom, le prénom et la date de naissance
                // soit renseignées
                if ((JadeStringUtil.isNull(nom)) || (JadeStringUtil.isNull(prenom))
                        || (JadeStringUtil.isNull(dateNaissance))) {
                    // ajout de messages d'erreur

                    if (JadeStringUtil.isEmpty(nom)) {
                        errorMessage = session.getLabel("ERROR_NOM_MANQUANT");
                    }
                    if (JadeStringUtil.isEmpty(prenom)) {
                        errorMessage += session.getLabel("ERROR_PRENOM_MANQUANT");
                    }
                    if (JadeStringUtil.isEmpty(dateNaissance)) {
                        errorMessage += session.getLabel("ERROR_DATE_NAISSANCE_MANQUANT");
                    }
                } else {
                    membre = new SFMembreFamille();
                    membre.setSession(session);
                    membre.setNom(nom);
                    membre.setPrenom(prenom);
                    membre.setCsSexe(sexe);
                    membre.setDateNaissance(dateNaissance);
                    if (dateDeces != null) {
                        membre.setDateDeces(dateDeces);
                    }
                    membre.setCsNationalite(nationalite);
                    membre.setCsCantonDomicile(cantonDomicile);
                    membre.setPays(codePays);
                    membre.setCsDomaineApplication(csDomaine);
                    membre.add(transaction);
                }
            }
        }
        if (errorMessage.length() > 0) {
            membre.setMsgType(FWViewBeanInterface.ERROR);
            membre.setMessage(errorMessage);
        }
        return membre;
    }

    /**
     * Ajout dans les tiers avec les info du membre de famille
     */
    private void ajouterTiersDepuisMembreFamille(BSession session, BITransaction transaction, SFMembreFamille membre,
            String cantonDomicile, String nationalite, String sexe, String dateDeces, String dateNaissance, String nom,
            String prenom, String pays, String nss) throws Exception {

        if (!JadeStringUtil.isEmpty(nss)) {
            // on crée le tiers et on sauvegarde l'id tiers dans la table membres de famille
            String idTiers = SFTiersHelper.addTiers(session, transaction, nss, nom, prenom, sexe, dateNaissance,
                    dateDeces, nationalite, cantonDomicile, null, null);

            // On contrôle que le tiers ait bien été inséré, car les exception
            // ne sont pas remontée par les interfaces.
            if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
                SFTiersWrapper tWrapper = SFTiersHelper.getTiersParId(session, idTiers);
                if (tWrapper == null) {
                    throw new SFBusinessException(session.getLabel("ERROR_ADD_TIERS"));
                } else {
                    membre.setIdTiers(idTiers);
                    membre.update(transaction);
                }
            } else {
                throw new SFBusinessException(session.getLabel("ERROR_ADD_TIERS"));
            }
            if (session.hasErrors()) {
                throw new SFBusinessException(session.getErrors().toString());
            }
        }
    }

    /**
     * getter pour l'attribut detenteur
     * 
     * @param idMembreFamille
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return méthode pour les périodes les conjoints pour un certains membre afin de setter le detenteur lors d'une
     *         période de type garde BTE
     */
    public Vector getDetenteur(String idMembreFamille, BSession session) {
        return this.getDetenteur(idMembreFamille, "", session);
    }

    /**
     * getter pour l'attribut detenteur
     * 
     * @param idMembreFamille
     *            DOCUMENT ME!
     * @param idDetenteurBTE
     *            détenteur de la période
     * @param session
     *            DOCUMENT ME!
     * 
     * @return méthode pour les périodes les conjoints pour un certains membre afin de setter le detenteur lors d'une
     *         période de type garde BTE
     */
    public Vector getDetenteur(String idMembreFamille, String idDetenteurBTE, BSession session) {

        SFEnfantManager enfMgr = new SFEnfantManager();
        SFEnfant enf = null;
        BITransaction biTransaction = null;
        // si idMembreFamille est un enfant, on le set avec l'id d'un parent
        enfMgr.setSession(session);
        enfMgr.setForIdMembreFamille(idMembreFamille);
        try {
            biTransaction = session.newTransaction();
            enfMgr.find(biTransaction, 1);

            if (enfMgr.getSize() > 0) {
                enf = (SFEnfant) enfMgr.getEntity(0);
                // est un enfant
                if (enf != null) {
                    SFConjoint conj = enf.getConjoints(biTransaction);
                    // a un parent
                    if (conj != null) {
                        idMembreFamille = conj.getIdConjoint1();
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(session.getLabel("ERROR_RECHERCHE_PARENT"), e);
        }

        Vector listDetenteur = new Vector();
        SFConjointManager conjointsManager = new SFConjointManager();
        SFMembreFamilleManager membreManager;
        SFMembreFamille membre;
        conjointsManager.setSession(session);
        conjointsManager.setForIdConjoint(idMembreFamille);

        SFConjoint conjoint;
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        listDetenteur.add(list);

        try {
            conjointsManager.find();

            if (!conjointsManager.isEmpty()) {
                for (int i = 0; i < conjointsManager.size(); i++) {
                    list = new String[2];
                    conjoint = (SFConjoint) conjointsManager.get(i);
                    membreManager = new SFMembreFamilleManager();
                    membreManager.setSession(session);
                    membreManager.setForIdMembreFamille(conjoint.getIdConjoint1());
                    membreManager.find();
                    membre = (SFMembreFamille) membreManager.getFirstEntity();
                    if (membre != null) {
                        list[0] = conjoint.getIdConjoint1();
                        list[1] = membre.getNom() + " " + membre.getPrenom();

                        if (!isDejaStocke(list[0], listDetenteur)) {
                            // ajoute le membre à la liste: en première position
                            // s'il est déjà le détenteur
                            if (!JadeStringUtil.isEmpty(conjoint.getIdConjoint1())
                                    && conjoint.getIdConjoint1().equals(idDetenteurBTE)) {
                                listDetenteur.add(0, list);
                            } else {
                                listDetenteur.add(list);
                            }
                        }
                    }

                    list = new String[2];
                    membreManager = new SFMembreFamilleManager();
                    membreManager.setSession(session);
                    membreManager.setForIdMembreFamille(conjoint.getIdConjoint2());
                    membreManager.find();
                    membre = (SFMembreFamille) membreManager.getFirstEntity();

                    if (membre != null) {
                        list[0] = conjoint.getIdConjoint2();
                        list[1] = membre.getNom() + " " + membre.getPrenom();

                        if (!isDejaStocke(list[0], listDetenteur)) {
                            // ajoute le membre à la liste: en première position
                            // s'il est déjà le détenteur
                            if (!JadeStringUtil.isEmpty(conjoint.getIdConjoint2())
                                    && conjoint.getIdConjoint2().equals(idDetenteurBTE)) {
                                listDetenteur.add(0, list);
                            } else {
                                listDetenteur.add(list);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(session.getLabel("ERROR_RECHERCHE_DETENTEURS"), e);
        }

        return listDetenteur;
    }

    /**
     * Revoie l'Id du domaine de l'application d'apprès le DTO context, Le domaine standard est donné si aucun context
     * n'est trouvé
     * 
     * @param session
     * @return
     */
    public String getDomaine(HttpSession session) {
        Serializable object = (Serializable) SFSessionDataContainerHelper.getData(session,
                SFSessionDataContainerHelper.KEY_VALEUR_RETOUR);
        String idDomaine;
        if (object instanceof SFApplicationContextDTO) {
            SFApplicationContextDTO context = (SFApplicationContextDTO) object;
            idDomaine = context.getIdDomaineApplication();
        } else {
            // si le context n'existe pas le domaine est standard
            idDomaine = ISFSituationFamiliale.CS_DOMAINE_STANDARD;
        }
        return idDomaine;
    }

    /**
     * getter pour l'attribut nom prenom membre
     * 
     * @param idMembre
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return le nom et prénom d'un membre pour un idMembreFamille
     */
    public String getNomPrenomMembre(String idMembre, BSession session) {
        if (!JadeStringUtil.isEmpty(idMembre)) {
            SFMembreFamille membre;
            SFMembreFamilleManager membreManager = new SFMembreFamilleManager();
            membreManager.setSession(session);
            membreManager.setForIdMembreFamille(idMembre);

            try {
                membreManager.find();
                membre = (SFMembreFamille) membreManager.getFirstEntity();

                return membre.getNom() + " " + membre.getPrenom();
            } catch (Exception e) {
                JadeLogger.warn(session.getLabel("ERROR_RECHERCHE_NOM_PRENOM"), e);

                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Revoie l'Id du domaine de l'application d'apprès le DTO context, Le domaine standard est donné si aucun context
     * n'est trouvé
     * 
     * @param session
     * @return null pas de requerant en session
     */
    public SFRequerantDTO getRequerantDTO(HttpSession session) {
        Serializable object = (Serializable) SFSessionDataContainerHelper.getData(session,
                SFSessionDataContainerHelper.KEY_REQUERANT_DTO);
        if (object instanceof SFRequerantDTO) {
            SFRequerantDTO context = (SFRequerantDTO) object;
            return context;
        } else {
            // si le requerant n'est pas trouvé
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param idMembreConjoint1
     * @param idMembreConjoint2
     * @param session
     *            DOCUMENT ME!
     * 
     * @return true si les deux membres sont conjoints
     */
    public boolean isConjoint(String idMembreConjoint1, String idMembreConjoint2, BSession session) {
        try {
            SFApercuRelationConjointManager conjoint = new SFApercuRelationConjointManager();
            conjoint.setSession(session);
            conjoint.forIdConjoints[0] = idMembreConjoint2;
            conjoint.forIdConjoints[1] = idMembreConjoint1;
            conjoint.find();

            if (conjoint.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            JadeLogger.warn(session.getLabel("ERROR_RECHERCHE_RELATION_REQ_CON"), e);
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param idDetenteur
     * @param listDetenteur
     * 
     * @return true si l'id est déjà stocké dans le vecteur utils pour la méthode getDetenteur ci-dessus
     */
    private boolean isDejaStocke(String idDetenteur, Vector listDetenteur) {
        String[] compare = new String[2];
        boolean isDejaStocke = false;

        for (int i = 0; i < listDetenteur.size(); i++) {
            compare = (String[]) listDetenteur.get(i);

            if (compare[0].equals(idDetenteur)) {
                isDejaStocke = true;
            }
        }

        return isDejaStocke;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param idEnfant
     * @param idMembreFamille
     * @param session
     * 
     * @return true si l'idEnfant est un enfant du membre de famille
     */
    public boolean isEnfant(String idEnfant, String idMembreFamille, BSession session) {
        // on va voir si le membreFamille est un enfant du requérant
        boolean isEnfant = false;
        SFEnfantManager enfantManager = new SFEnfantManager();
        SFEnfant enfant;
        enfantManager.setSession(session);
        enfantManager.setForIdMembreFamille(idEnfant);
        try {
            enfantManager.find();
            for (int i = 0; i < enfantManager.size(); i++) {
                enfant = (SFEnfant) enfantManager.get(i);
                SFConjoint c;
                SFConjointManager conjoint = new SFConjointManager();
                conjoint.setSession(session);
                conjoint.setForIdDesConjoints(enfant.getIdConjoint());
                conjoint.find();

                for (int j = 0; j < conjoint.size(); i++) {
                    c = (SFConjoint) conjoint.get(j);

                    if ((c.getIdConjoint1().equals(idMembreFamille)) || (c.getIdConjoint2().equals(idMembreFamille))) {
                        isEnfant = true;
                        return true;
                    } else {
                        isEnfant = false;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(session.getLabel("ERROR_RECHERCHE_ENFANT_REQ"), e);
            return false;
        }

        return isEnfant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param requerant
     * @param idMembreFamille
     * @param session
     * 
     * @return true si l'idParent est un parent du membre de la famille
     */
    public boolean isParent(String idParent, String idMembreFamille, BSession session) {
        // on va voir si le requerant est un parent du membre de famille
        boolean isParent = false;
        SFEnfant enfant;
        SFApercuRelationConjointManager conjoint = new SFApercuRelationConjointManager();
        SFApercuRelationConjoint c;
        conjoint.setSession(session);
        conjoint.setForIdConjoint(idParent);
        try {
            conjoint.find();
            for (int i = 0; i < conjoint.size(); i++) {
                c = (SFApercuRelationConjoint) conjoint.get(i);
                SFEnfantManager enfantManager = new SFEnfantManager();
                enfantManager.setSession(session);
                enfantManager.setForIdConjoints(c.getIdConjoints());
                enfantManager.find();
                for (int j = 0; j < enfantManager.size(); j++) {
                    enfant = (SFEnfant) enfantManager.get(j);
                    if (enfant.getIdMembreFamille().equals(idMembreFamille)) {
                        isParent = true;
                        return true;
                    } else {
                        isParent = false;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(session.getLabel("ERROR_RECHERCHE_PARENT_REQ"), e);
            return false;
        }
        return isParent;
    }

    /**
     * Mise à jour des info du membre de famille, remet l'ID tiers à zéro
     */
    private void mettreAJourMembreFamille(BSession session, BITransaction transaction, SFMembreFamille membre,
            String cantonDomicile, String nationalite, String sexe, String dateDeces, String dateNaissance, String nom,
            String prenom, String pays, String nss) throws Exception {
        membre.setCsCantonDomicile(cantonDomicile);
        membre.setCsNationalite(nationalite);
        membre.setCsSexe(sexe);
        membre.setDateDeces(dateDeces);
        membre.setDateNaissance(dateNaissance);
        membre.setNom(nom);
        membre.setPays(pays);
        membre.setPrenom(prenom);
        membre.setIdTiers("");

        membre.update(transaction);
    }

    /**
     * Mise à jour des info du membre de famille afin de le lier à un tiers
     * 
     * @param hasAdresseDomicile
     */
    private void mettreAJourMembreFamilleDepuisTiers(BSession session, BITransaction transaction,
            SFMembreFamille membre, String cantonDomicile, String pays, String idTiers, String hasAdresseDomicile)
            throws Exception {
        // Dans ce cas, le seul champ modifiable est le canton et le pays de domicile
        // if ("false".equalsIgnoreCase(hasAdresseDomicile)) {
        // membre.setCsCantonDomicile(cantonDomicile);
        //
        // } else {
        // membre.setCsCantonDomicile(null);
        // }
        membre.setCsCantonDomicile(cantonDomicile);
        membre.setPays(pays);

        if (JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
            membre.setIdTiers(idTiers);
        }

        membre.update(transaction);
    }

    public void modifierMembre(SFRequerantDTO requerant, boolean isMAJEnfant, SFMembreFamille membre, String idAssure,
            String provenance, String numAvs, String nom, String prenom, String sexe, String dateNaissance,
            String dateDeces, String nationalite, String pays, String cantonDomicile, String idTiers,
            String hasAdresseDomicile, BSession session, BITransaction transaction) throws Exception {

        if ((requerant == null) || JadeStringUtil.isBlankOrZero(requerant.getIdRequerant())) {
            throw new Exception(session.getLabel("ERROR_AUCUN_REQUERANT_SELECTIONNE"));
        }

        if (JadeStringUtil.isBlankOrZero(membre.getCsDomaineApplication())) {
            throw new Exception(session.getLabel("ERROR_DOMAINE_AUCUN"));
        }

        // ce membre de famille est déjà défini dans les tiers
        if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
            mettreAJourMembreFamilleDepuisTiers(session, transaction, membre, cantonDomicile, pays,
                    membre.getIdTiers(), hasAdresseDomicile);
        } else {
            // ce membre de famille n'est pas encore défini dans les tiers

            // vérification que l'utilisateur n'essaie pas de modifier le conjoint inconnu
            if (!isMAJEnfant
                    && ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(membre.getIdMembreFamille())) {
                throw new Exception(
                        "Unauthorized action exception. Authorized action flow using SFApercuRelationFamilialeAction.actionModifierConjointInconnu");
            }

            if (JadeStringUtil.isBlankOrZero(idAssure)) {
                // ce membre de famille n'est ni dans les tiers, ni dans les CI
                if (JadeStringUtil.isBlankOrZero(numAvs)) {
                    // pas de numéro AVS, on ne crée rien dans les tiers mais on met à jour le membre de famille
                    mettreAJourMembreFamille(session, transaction, membre, cantonDomicile, nationalite, sexe,
                            dateDeces, dateNaissance, nom, prenom, pays, numAvs);
                } else {
                    // numéro AVS renseigné, on crée l'entrée dans les tiers
                    ajouterTiersDepuisMembreFamille(session, transaction, membre, cantonDomicile, nationalite, sexe,
                            dateDeces, dateNaissance, nom, prenom, pays, numAvs);
                }
            } else {
                if (SFUtil.PROVENANCE_TIERS.equals(provenance)) {
                    // ce membre de famille a été trouvé dans les tiers (grâce à son numéro AVS)
                    mettreAJourMembreFamilleDepuisTiers(session, transaction, membre, cantonDomicile, pays, idAssure,
                            hasAdresseDomicile);
                } else if (SFUtil.PROVENANCE_CI.equals(provenance)) {
                    // ce membre de famille a été trouvé dans les CI (grâce à son numéro AVS), mais n'existe pas encore
                    // dans les tiers
                    ajouterTiersDepuisMembreFamille(session, transaction, membre, cantonDomicile, nationalite, sexe,
                            dateDeces, dateNaissance, nom, prenom, pays, numAvs);
                }
            }
        }

        // bz-
        // Final check / Workaround
        // Si le membre modifié contient un idTiers, on contrôle que ce tiers existe dans pyxis.
        // Pour une raison inconnue, il arrive que le membre de famille ait un idTiers ne pointant sur aucun tiers.
        if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, membre.getIdTiers());
            if (tw == null) {
                throw new Exception(MessageFormat.format(session.getLabel("ERROR_TIERS_INTROUVABLE"),
                        membre.getIdTiers()));
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param request
     *            la requete a parser
     * @param field
     *            le champ à récupérer
     * 
     * @return la valeur du champ désirée à l'intérieur de la requête
     */
    public String parseRequest(HttpServletRequest request, String field) {
        return this.parseRequest(request, field, null);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param request
     *            la requete a parser
     * @param field
     *            le champ à récupérer
     * 
     * @return la valeur du champ désirée à l'intérieur de la requête
     */
    public String parseRequest(HttpServletRequest request, String field, String defaultValue) {
        if (!JadeStringUtil.isEmpty(request.getParameter(field))) {
            return request.getParameter(field);
        } else {
            return defaultValue;
        }
    }

    /**
     * Instancie le DT du nouveau requerant et le place dans la session
     * 
     * @param session
     * @param requerant
     * @throws Exception
     */
    public void setRequerantToDTO(HttpSession session, SFRequerant requerant) throws Exception {

        SFApercuRequerant req = new SFApercuRequerant();
        req.setSession(requerant.getSession());
        req.setIdRequerant(requerant.getIdRequerant());
        req.retrieve();

        SFRequerantDTO reqDTO = new SFRequerantDTO(req);
        SFSessionDataContainerHelper.setData(session, SFSessionDataContainerHelper.KEY_REQUERANT_DTO, reqDTO);
    }

    /**
     * Instancie le DT du nouveau requerant et le place dans la session
     * 
     * @param session
     * @param requerant
     * @throws Exception
     */
    public void setRequerantToDTO(HttpSession session, SFRequerant requerant, SFMembreFamille membre) throws Exception {
        SFRequerantDTO reqDTO = new SFRequerantDTO(requerant, membre);
        SFSessionDataContainerHelper.setData(session, SFSessionDataContainerHelper.KEY_REQUERANT_DTO, reqDTO);
    }

    /**
     * A partir d'un BEntity sur lequel on a effectué une opération (add,...) Lance une exception avec comme message
     * l'erreur du BEntity La transaction, la session, et le Bentity sont analysés Si pas d'erreur, n'effectue rien
     * 
     * @param entity
     * @throws Exception
     */
    public void throwExceptionIfError(BTransaction transaction, BEntity entity) throws Exception {
        if (transaction != null) {
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }
            if (transaction.getISession().hasErrors()) {
                throw new Exception(transaction.getISession().getErrors().toString());
            }
        }
        if (entity.hasErrors()) {
            throw new Exception(entity.getErrors().toString());
        }
        if (entity.getSession().hasErrors()) {
            throw new Exception(entity.getSession().getErrors().toString());
        }
        if (FWViewBeanInterface.ERROR.equals(entity.getMsgType())) {
            throw new Exception(entity.getMessage());
        }
    }

    /**
     * ajoute ou supprime la relation entre un requerant et un autre membre de famille
     * 
     * @param idMembreRequerant
     *            , id membre famille du requerant
     * @param idMembreFamille
     * @param boolean add-->true delete-->false
     * @param session
     * @param transaction
     * @throws Exception
     *             si le traitement n'a pas eu le résultat attendu
     */

    public void updateRelationMembreRequerant(String idMembreRequerant, String idMembreFamille, boolean add,
            BSession session, BITransaction transaction) throws Exception {
        SFRequerantManager reqMgr = new SFRequerantManager();
        reqMgr.setSession(session);
        reqMgr.setForIdMembreFamille(idMembreRequerant);
        reqMgr.find(transaction);
        // Autre requerant dans tous les domaines
        for (Iterator it = reqMgr.iterator(); it.hasNext();) {
            SFRequerant requerant = (SFRequerant) it.next();
            updateRelationRequerant(requerant.getIdRequerant(), idMembreFamille, add, session, transaction);
        }
    }

    /*
     * 20) - Requerant = 756.1111 Jean Requer - MF1 = 756.2222 Jeanne Jeanne - ENF1 = ________ Junior Junior
     * 
     * Junior Junior -> 756.3333 Junior Junior
     * 
     * - Requerant = 756.1111 Jean Requer - MF1 = 756.2222 Jeanne Jeanne - ENF1 = ________ Juniorette Juniorette
     * 
     * Traitement : Mise à jours du membre de famille ENF1 (nom + prénom...)
     */

    /**
     * Ajoute ou supprime la relation entre un membre et un requérant
     * 
     * @param idRequerant
     * @param idMembreFamille
     * @param add
     * @param session
     * @param transaction
     * @throws Exception
     */
    public void updateRelationRequerant(String idRequerant, String idMembreFamille, boolean add, BSession session,
            BITransaction transaction) throws Exception {
        SFRelationFamilialeRequerant rel = new SFRelationFamilialeRequerant();
        rel.setSession(session);
        rel.setIdRequerant(idRequerant);
        rel.setIdMembreFamille(idMembreFamille);
        rel.setAlternateKey(SFRelationFamilialeRequerant.ALTERNATE_KEY_MEMBRE_REQ);
        rel.retrieve(transaction);
        // Enfant ajouté --> on ajoute la relation
        if (add && rel.isNew()) {
            rel.add(transaction);
            throwExceptionIfError((BTransaction) transaction, rel);
        } // Enfant supprimé --> on ajoute la relation
        else if (!add && !rel.isNew()) {
            rel.delete(transaction);
        }

    }

}
