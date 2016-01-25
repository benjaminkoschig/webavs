/*
 * créé le 01/06/2010
 */
package globaz.cygnus.helpers.conventions;

import globaz.cygnus.api.conventions.IRFConventions;
import globaz.cygnus.db.conventions.RFAssConventionFournisseurSousTypeDeSoin;
import globaz.cygnus.db.conventions.RFAssConventionFournisseurSousTypeDeSoinManager;
import globaz.cygnus.db.conventions.RFConventionAssure;
import globaz.cygnus.db.conventions.RFConventionAssureManager;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointConventionAssure;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointConventionAssureManager;
import globaz.cygnus.db.conventions.RFSaisieSoinFournisseur;
import globaz.cygnus.db.conventions.RFSaisieSoinFournisseurManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.conventions.RFAssureConvention;
import globaz.cygnus.vb.conventions.RFConventionViewBean;
import globaz.cygnus.vb.conventions.RFFournisseurType;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * author fha
 */
public class RFSaisieSoinFournisseurConventionHelper extends PRAbstractHelper {

    /**
     * Annulation générale On annule seulement les lignes qui ne sont pas en BDD (indiqués grace au champ
     * isChargeDepuisLaBD)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

    }

    /**
     * initialisation de la page : si isAjout vaut false alors on entre en edition et donc on charge les tableaux depuis
     * la BDD sinon il n'y a rien à faire
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFConventionViewBean outputViewBean = (RFConventionViewBean) viewBean;

        outputViewBean.setNomEcranCourant(IRFConventions.ECRAN_CONVENTION);

        // si la convention existe déjà mais qu'elle n'a pas encore de
        // couple FT il ne faut pas afficher de tableaux
        if (!outputViewBean.getIsAjout()) {
            // pour bien commencer on vide les tableaux des éléments déja en bdd
            clearFournisseurSoinDBLines(outputViewBean.getFournisseurTypeArray());
            clearAssureDBLines(outputViewBean.getAssureArray());
            /*
             * chargement du tableau contenant les couples fournisseur / type de soin de cette convention
             */
            chargerCouplesFournisseurTypeSoin((BSession) session, outputViewBean);
        }

    }

    // affichage d'un message d'erreur si un assure n'est pas valide (pas de
    // NSS)
    private void afficheErreurAssureIncomplet(RFConventionViewBean soinFournisseurVB) {
        RFUtils.setMsgErreurViewBean(soinFournisseurVB, "ERREUR_RF_SAISIE_FOURNISSEUR_CONVENTION_INCOMPLET_ASSURE");
    }

    // affichage d'un message d'erreur si il n'y a pas de couple fournisseur -
    // type de soin
    private void afficheErreurCoupleFTManquant(RFConventionViewBean soinFournisseurVB) {
        RFUtils.setMsgErreurViewBean(soinFournisseurVB, "ERREUR_RF_SAISIE_FOURNISSEUR_CONVENTION_MANQUANT");
    }

    // affichage d'un message d'erreur si un couple fournisseur/soin n'est pas
    // complet
    private void afficheErreurCoupleIncomplet(RFConventionViewBean soinFournisseurVB) {
        RFUtils.setMsgErreurViewBean(soinFournisseurVB, "ERREUR_RF_SAISIE_FOURNISSEUR_CONVENTION_INCOMPLET_COUPLEFT");
    }

    // affichage d'un message d'erreur si le couple fournisseur soin existe déjà
    private void afficheErreurIntegrite(RFConventionViewBean soinFournisseurVB) {
        RFUtils.setMsgErreurViewBean(soinFournisseurVB, "ERREUR_RF_SAISIE_FOURNISSEUR_CONVENTION_DOUBLE");
    }

    // affichage d'un message d'erreur si l'assuré existe déjà
    private void afficheErreurIntegriteAssure(RFConventionViewBean soinFournisseurVB) {
        RFUtils.setMsgErreurViewBean(soinFournisseurVB, "ERREUR_RF_SAISIE_ASSURE_CONVENTION_DOUBLE");
    }

    protected RFAssureConvention ajoutAssure(RFSaisieSoinFournisseur conventionRequest, int i) throws Exception {

        return new RFAssureConvention(Integer.toString(i), conventionRequest.getMontantAssure(),
                conventionRequest.getNss(), conventionRequest.getNom(), conventionRequest.getPrenom(),
                conventionRequest.getAdresse(), conventionRequest.getDateDebut(), conventionRequest.getDateFin(),
                Boolean.TRUE, Boolean.FALSE);
    }

    protected RFFournisseurType ajoutCoupleFournisseurTypeSoin(RFSaisieSoinFournisseur conventionRequest, int i) {

        return new RFFournisseurType(i, conventionRequest.getFournisseur() + " "
                + conventionRequest.getFournisseurSuite(), conventionRequest.getIdAdressePaiement(), conventionRequest
                .getTypeSoin().length() == 1 ? "0" + conventionRequest.getTypeSoin() : conventionRequest.getTypeSoin(),
                conventionRequest.getSousTypeSoin().length() == 1 ? "0" + conventionRequest.getSousTypeSoin()
                        : conventionRequest.getSousTypeSoin(), conventionRequest.getIdFournisseur(),
                conventionRequest.getIdSousTypeSoin(), Boolean.TRUE, Boolean.FALSE);
    }

    /**
     * Ajout d'un assuré
     */
    public void ajouterAssure(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            RFConventionViewBean soinFournisseurVB = (RFConventionViewBean) viewBean;

            // ajout de l'assuré
            ArrayList assureArray = new ArrayList();
            // création d'un objet RFFournisseurType
            try {

                PRTiersWrapper prTiersWrapper = PRTiersHelper.getPersonneAVS(session, soinFournisseurVB.getIdAssure());
                soinFournisseurVB.setNssTiers(prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                soinFournisseurVB.setNom(prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM));
                soinFournisseurVB.setPrenom(prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                soinFournisseurVB.setAdresse(prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA + " "
                        + PRTiersWrapper.PROPERTY_NPA_SUP));

                RFAssureConvention assure = new RFAssureConvention(soinFournisseurVB.getIdAssure(),
                        soinFournisseurVB.getForMontantAssure(), soinFournisseurVB.getNssTiers(),
                        soinFournisseurVB.getNom(), soinFournisseurVB.getPrenom(), soinFournisseurVB.getAdresse(),
                        soinFournisseurVB.getForDateDebut(), soinFournisseurVB.getForDateFin(), Boolean.FALSE,
                        Boolean.FALSE);

                assureArray = soinFournisseurVB.getAssureArray();

                if (validationAssure(assureArray, assure, soinFournisseurVB)) {
                    soinFournisseurVB.getAssureArray().add(assure);
                    supprimerInformationsAssure(soinFournisseurVB);
                }
            } catch (Exception e) {
                afficheErreurAssureIncomplet(soinFournisseurVB);
            }
        }
    }

    /**
     * Validation vers la DB si on est update On ajoute seulement les lignes qui ne sont pas en BDD (indiqués grace au
     * champ isChargeDepuisLaBD) sinon on ajoute tout
     */
    public void ajouterBDD(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {
        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = (session).newTransaction();
                transaction.openTransaction();

                RFConventionViewBean SFViewBean = (RFConventionViewBean) viewBean;

                if (validationPourDB(session, SFViewBean)) {

                    clearIdSuppressionFournisseurArray(SFViewBean, session);
                    clearIdSuppressionAssureArray(SFViewBean, session);

                    // On vérifie encore une fois qu'il n'y ai pas de doublons dans les tableaux
                    RFFournisseurType ligneFT = null;
                    // renvoi le plus grand idCFS de la table
                    // RFAssConventionFournisseurSousTypeDeSoin
                    Long numLineCofos = calculateNumLineFT(SFViewBean, session, transaction);
                    RFAssureConvention ligneAss = null;
                    Long numLineAss = null;
                    // ajout d'un enregistrement à la table association RFCOFOS
                    for (Iterator it = SFViewBean.getFournisseurTypeArray().iterator(); it.hasNext();) {
                        ligneFT = (RFFournisseurType) it.next();
                        // test si il n'est pas déjà en BDD
                        if (!ligneFT.getIsChargeDepuisDB()) {

                            RFAssConventionFournisseurSousTypeDeSoin cofos = new RFAssConventionFournisseurSousTypeDeSoin();
                            cofos.setSession(SFViewBean.getSession());
                            cofos.setIdConvention(SFViewBean.getIdConvention());
                            cofos.setIdFournisseur(ligneFT.getIdFournisseur());
                            cofos.setIdSousTypeDeSoin(ligneFT.getIdSousTypeDeSoin());
                            cofos.setIdConvFouSts(numLineCofos.toString());
                            cofos.setIdAdressePaiement(ligneFT.getIdAdressePaiement());
                            cofos.add(transaction);
                            numLineCofos++;
                        }
                    }

                    // renvoi le plus grand idConas de la table
                    // RFConventionAssure
                    numLineAss = calculateNumLineAss(SFViewBean, session, transaction);
                    // ajout d'un enregistrement à la table association RFCONAS
                    // pour CHAQUE enregistrement de la table association
                    Long numLigne = new Long(0);
                    for (Iterator it2 = SFViewBean.getAssureArray().iterator(); it2.hasNext();) {
                        ligneAss = (RFAssureConvention) it2.next();
                        // test si il n'est pas déjà en BDD
                        if (!ligneAss.getIsChargeDepuisDB()) {
                            RFConventionAssure conas = new RFConventionAssure();
                            conas.setSession(SFViewBean.getSession());
                            conas.setDateDebut(ligneAss.getDateDebut());
                            conas.setDateFin(ligneAss.getDateFin());
                            conas.setIdAssure(ligneAss.getIdAssure());
                            numLineAss += numLigne;
                            conas.setIdConventionAssure(numLineAss.toString());// il
                            conas.setIdConvention(SFViewBean.getIdConvention());
                            conas.setMontantAssure(ligneAss.getMontant());
                            conas.add(transaction);
                            numLigne++;
                        }
                    }
                    // suppression des elements des tables
                    SFViewBean.getFournisseurTypeArray().clear();
                    SFViewBean.getAssureArray().clear();
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                            transaction.rollback();
                        } else {
                            transaction.commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    /**
     * Ajout d'un couple fournisseur/soin
     */
    public void ajouterFournisseurType(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            RFConventionViewBean soinFournisseurVB = (RFConventionViewBean) viewBean;
            // récupération du ArrayList
            ArrayList fournisseurArray = new ArrayList();
            // création d'un objet RFFournisseurType
            fournisseurArray = soinFournisseurVB.getFournisseurTypeArray();

            try {
                // récupération de l'idSousTypeDeSoin
                String idSts = RFUtils.getIdSousTypeDeSoin(soinFournisseurVB.getCodeTypeDeSoinList(),
                        soinFournisseurVB.getCodeSousTypeDeSoinList(), session);
                RFFournisseurType coupleFT = new RFFournisseurType(new Integer(0),
                        soinFournisseurVB.getDescFournisseur(), soinFournisseurVB.getIdAdressePaiement(),
                        soinFournisseurVB.getCodeTypeDeSoinList(), soinFournisseurVB.getCodeSousTypeDeSoinList(),
                        soinFournisseurVB.getIdFournisseur(), idSts, Boolean.FALSE, Boolean.FALSE);
                if (validationSoinFournisseur(fournisseurArray, coupleFT, soinFournisseurVB, session)) {
                    Integer idArray;
                    RFFournisseurType fournisseurType = null;
                    // création du prochain id
                    if (fournisseurArray.size() > 0) {
                        for (Iterator it = fournisseurArray.iterator(); it.hasNext();) {
                            fournisseurType = (RFFournisseurType) it.next();
                        }
                        idArray = new Integer(fournisseurType.getIdFourType().intValue() + 1);
                    } else {
                        idArray = new Integer(fournisseurArray.size() + 1);
                    }
                    // ajout d'une ligne au tableau
                    soinFournisseurVB.getFournisseurTypeArray().add(
                            new RFFournisseurType(idArray, soinFournisseurVB.getDescFournisseur(), soinFournisseurVB
                                    .getIdAdressePaiement(), soinFournisseurVB.getCodeTypeDeSoinList(),
                                    soinFournisseurVB.getCodeSousTypeDeSoinList(),
                                    soinFournisseurVB.getIdFournisseur(), idSts, Boolean.FALSE, Boolean.FALSE));

                    supprimerInformationsFournisseur(soinFournisseurVB);
                }
            } catch (Exception e) {
                afficheErreurCoupleIncomplet(soinFournisseurVB);
            }
        }
    }

    protected boolean appartientAutreConvention(RFFournisseurType fournisseurType,
            RFConventionViewBean soinFournisseurVB, BSession session) throws Exception {

        // recherche de toutes les convention - couple fournisseurs type de soin
        RFAssConventionFournisseurSousTypeDeSoinManager conventionTypeFournisseur = new RFAssConventionFournisseurSousTypeDeSoinManager();
        conventionTypeFournisseur.setSession(session);
        conventionTypeFournisseur.setForIdFournisseur(fournisseurType.getIdFournisseur());
        conventionTypeFournisseur.setForIdSousTypeDeSoin(fournisseurType.getIdSousTypeDeSoin());
        conventionTypeFournisseur.changeManagerSize(0);
        conventionTypeFournisseur.find();

        // on vérifie que les resultats n'appartiennent pas à la convention
        Vector<RFAssConventionFournisseurSousTypeDeSoin> convCoupleFT = conventionTypeFournisseur.getContainer();
        if (conventionTypeFournisseur.size() > 0) {
            for (RFAssConventionFournisseurSousTypeDeSoin i : convCoupleFT) {
                if (!i.getIdConvention().equals(soinFournisseurVB.getIdConvention())) {
                    RFUtils.setMsgErreurViewBean(soinFournisseurVB,
                            "ERREUR_RF_SAISIE_FOURNISSEUR_CONVENTION_DOUBLET_CONVENTION_COUPLEFT");
                    return true;
                }
            }
        }
        return false;
    }

    private Long calculateNumLineAss(RFConventionViewBean SFViewBean, BSession session, BITransaction transaction)
            throws Exception {

        // charger la table COFOS grace au manager
        RFConventionAssureManager CONAS = new RFConventionAssureManager();
        CONAS.setSession(session);
        CONAS.changeManagerSize(0);
        CONAS.find(transaction);

        // on parcours la table pour récupérer le plus grand idConventionAssure
        Long idMax = new Long(0);
        RFConventionAssure elemCONAS = null;
        for (Iterator it = CONAS.iterator(); it.hasNext();) {
            elemCONAS = (RFConventionAssure) it.next();
            Long currentId = new Long(elemCONAS.getIdConventionAssure());
            if (currentId.compareTo(idMax) > 0) {
                idMax = currentId;
            }
        }
        return new Long(idMax.longValue() + 1);

    }

    private Long calculateNumLineFT(RFConventionViewBean SFViewBean, BSession session, BITransaction transaction)
            throws Exception {

        // charger la table CONAS grace au manager
        RFAssConventionFournisseurSousTypeDeSoinManager COFOS = new RFAssConventionFournisseurSousTypeDeSoinManager();
        COFOS.setSession(session);
        COFOS.changeManagerSize(0);
        COFOS.find();
        COFOS.getContainer();

        // on parcours la table pour récupérer le plus grand idConventionAssure
        Long idMax = new Long(0);
        RFAssConventionFournisseurSousTypeDeSoin elemCOFOS = null;
        for (Iterator it = COFOS.iterator(); it.hasNext();) {
            elemCOFOS = (RFAssConventionFournisseurSousTypeDeSoin) it.next();
            Long currentId = new Long(elemCOFOS.getIdConvFouSts());
            if (currentId.compareTo(idMax) > 0) {
                idMax = currentId;
            }
        }
        return new Long(idMax.longValue() + 1);
    }

    protected void chargerCouplesFournisseurTypeSoin(BSession session, RFConventionViewBean viewBean) throws Exception {
        RFSaisieSoinFournisseurManager couplesFournisseurTypeSoin = null;
        couplesFournisseurTypeSoin = requeteCouplesFournisseurTypeSoinDB(session, viewBean);

        // ajout des résultats au tableau FournisseurTypeSoinArray
        if (couplesFournisseurTypeSoin.getSize() > 0) {
            remplirTableauFournisseurTypeSoin(couplesFournisseurTypeSoin, viewBean);
        }
    }

    private void clearAssureDBLines(ArrayList<RFAssureConvention> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getIsChargeDepuisDB()) {
                array.remove(i);
                i--;
            }
        }
    }

    private void clearFournisseurSoinDBLines(ArrayList<RFFournisseurType> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getIsChargeDepuisDB()) {
                array.remove(i);
                i--;// car le suivant est à présent le courant
            }
        }
    }

    /**
     * supprime les enregistrements des assurés déjà en BDD que l'utilisateur a souhaité supprimer
     * 
     * @param SFViewBean
     * @param session
     * @throws Exception
     */
    protected void clearIdSuppressionAssureArray(RFConventionViewBean viewBean, BSession session) throws Exception {
        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();
            RFConventionAssure conventionAssure = null;

            for (Iterator it = viewBean.getIdSuppressionAssureArray().iterator(); it.hasNext();) {

                conventionAssure = new RFConventionAssure();
                conventionAssure.setSession(session);
                conventionAssure.setIdConventionAssure((String) it.next());
                conventionAssure.retrieve();
                if (!conventionAssure.isNew()) {
                    conventionAssure.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(conventionAssure, "_delete()",
                            "RFSaisieSoinFournisseurConventionHelper");
                }

            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * supprime les enregistrements des couples fournisseur-type de soin déjà en BDD que l'utilisateur a souhaité
     * supprimer
     * 
     * @param SFViewBean
     * @param session
     * @throws Exception
     */
    protected void clearIdSuppressionFournisseurArray(RFConventionViewBean viewBean, BSession session) throws Exception {
        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            for (Iterator it = viewBean.getIdSuppressionFournisseurArray().iterator(); it.hasNext();) {
                // suppression de l'idCOFOS
                RFAssConventionFournisseurSousTypeDeSoin cofos = new RFAssConventionFournisseurSousTypeDeSoin();
                cofos.setSession(session);
                cofos.setIdConvFouSts((String) it.next());
                cofos.retrieve();

                if (!cofos.isNew()) {
                    cofos.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(cofos, "_delete()", "RFSaisieSoinFournisseurConventinHelper");
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private Boolean existeAssure(RFAssureConvention assure, RFSaisieSoinFournisseurManager convention, int i,
            String idConvention) {
        for (int j = 0; j < i; j++) {
            if (((RFSaisieSoinFournisseur) convention.get(j)).getNss().equals(assure.getNSS())
                    && ((RFSaisieSoinFournisseur) convention.get(j)).getDateDebut().equals(assure.getDateDebut())
                    && ((RFSaisieSoinFournisseur) convention.get(j)).getDateFin().equals(assure.getDateFin())
                    && ((RFSaisieSoinFournisseur) convention.get(j)).getIdConvention().equals(idConvention)) {
                return true;
            }
        }
        return false;
    }

    private Boolean existeCoupleFT(RFFournisseurType coupleFT, RFSaisieSoinFournisseurManager convention, int i,
            String idConvention) {
        for (int j = 0; j < i; j++) {
            if (((RFSaisieSoinFournisseur) convention.get(j)).getIdFournisseur().equals(coupleFT.getIdFournisseur())
                    && ((RFSaisieSoinFournisseur) convention.get(j)).getIdSousTypeSoin().equals(
                            coupleFT.getIdSousTypeDeSoin())
                    && ((RFSaisieSoinFournisseur) convention.get(j)).getIdConvention().equals(idConvention)) {
                return true;
            }
        }
        return false;
    }

    /*
     * fonction qui teste la contrainte d'unicite d'un assuré renvoie vrai si la contrainte est respectée Attention si
     * la période de l'assuré à ajouter chevauche ou inclus la date d'un assuré de même NSS on renvoi faux!
     */
    private boolean isVerifContrainteUniciteAssure(ArrayList assureArray, RFAssureConvention assure) {

        RFAssureConvention elemAssure = null;
        // parcours le tableau d'assuré et compare le NSS et les dates avec
        // l'assure
        for (Iterator it = assureArray.iterator(); it.hasNext();) {
            elemAssure = (RFAssureConvention) it.next();
            if (elemAssure.getNSS().equals(assure.getNSS())
                    && !((JadeDateUtil.isDateAfter(assure.getDateDebut(), elemAssure.getDateFin())) || (JadeDateUtil
                            .isDateAfter(elemAssure.getDateDebut(), assure.getDateFin())))) {
                return false;
            }
        }
        return true;

    }

    protected void remplirTableauFournisseurTypeSoin(RFSaisieSoinFournisseurManager couplesFournisseurTypeSoin,
            RFConventionViewBean outputViewBean) throws Exception {
        // on parcours les éléments retournés
        int i = 0;
        for (Iterator it = couplesFournisseurTypeSoin.iterator(); it.hasNext();) {
            RFSaisieSoinFournisseur conventionRequest = (RFSaisieSoinFournisseur) it.next();
            // ajout de l'enregistrement au tableau FournisseurTypeArray
            // ET assureArray si il a un assuré
            if (outputViewBean.getIdConvention().equals(conventionRequest.getIdConvention())
                    && !JadeStringUtil.isEmpty(conventionRequest.getFournisseur())) {

                RFFournisseurType coupleFT = ajoutCoupleFournisseurTypeSoin(conventionRequest, i);

                // si on vient de la recherche des montants de
                // convention il faut reafficher les elements en BDD
                if (!outputViewBean.getIdSuppressionFournisseurArray().contains(conventionRequest.getIdCfs())
                        && !existeCoupleFT(coupleFT, couplesFournisseurTypeSoin, i, conventionRequest.getIdConvention())) {
                    outputViewBean.getFournisseurTypeArray().add(coupleFT);
                }
            }
            if (outputViewBean.getIdConvention().equals(conventionRequest.getIdConvention())
                    && !JadeStringUtil.isEmpty(conventionRequest.getNss())) {

                RFAssureConvention assure = ajoutAssure(conventionRequest, i);

                if (!outputViewBean.getIdSuppressionAssureArray().contains(conventionRequest.getIdConas())
                        && !existeAssure(assure, couplesFournisseurTypeSoin, i, conventionRequest.getIdConvention())) {

                    outputViewBean.getAssureArray().add(assure);

                    if (!outputViewBean.getConcerneAssure()) {
                        outputViewBean.setConcerneAssure(Boolean.TRUE);
                    }
                }
            }
            i++;
        }
    }

    protected RFSaisieSoinFournisseurManager requeteCouplesFournisseurTypeSoinDB(BSession session,
            RFConventionViewBean outputViewBean) throws Exception {

        RFSaisieSoinFournisseurManager couplesFournisseurTypeSoin = new RFSaisieSoinFournisseurManager();
        couplesFournisseurTypeSoin.setSession(session);

        // initialiser l'idConvention avec le viewBean
        couplesFournisseurTypeSoin.setForIdConvention(outputViewBean.getIdConvention());
        couplesFournisseurTypeSoin.changeManagerSize(0);
        couplesFournisseurTypeSoin.find();
        return couplesFournisseurTypeSoin;
    }

    /**
     * Suppression d'un couple assuré Si il est déjà en BDD on le supprime de la DB et du tableau Sinon on le supprime
     * que du tableau
     */
    public void supprimerAssure(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {
        // on récupére du viewBean le NSS, la date de début et de fin de la
        // ligne que l'on veut supprimer
        RFConventionViewBean outputViewBean = (RFConventionViewBean) viewBean;

        for (int i = 0; i < outputViewBean.getAssureArray().size(); i++) {
            RFAssureConvention assure = (RFAssureConvention) outputViewBean.getAssureArray().get(i);

            if (assure.getNSS().equals(outputViewBean.getNss())
                    && assure.getDateDebut().equals(outputViewBean.getDateDebut())
                    && assure.getDateFin().equals(outputViewBean.getDateFin())) {
                // est-elle en DB?
                if (assure.getIsChargeDepuisDB()) {
                    supprimerAssureEnDB(session, assure, outputViewBean);
                }
                outputViewBean.getAssureArray().remove(i);
                i--;
            }
        }
        // on vide les informations de l'assuré du viewBean
        supprimerInformationsAssure(outputViewBean);
    }

    /*
     * On supprime la ligne passé en paramètre (fournisseurType) de la DB
     */
    protected void supprimerAssureEnDB(BSession session, RFAssureConvention assure, RFConventionViewBean viewBean)
            throws Exception {

        // récupération de COFOSJOINCONAS avec idConvention = et idFournisseur
        RFSaisieSoinFournisseurManager conventionManager = new RFSaisieSoinFournisseurManager();
        conventionManager.setSession(session);
        conventionManager.setForIdConvention(viewBean.getIdConvention());
        conventionManager.changeManagerSize(0);
        conventionManager.find();

        // si on a des resultats suppression du CONAS avec NSS / dateDebut /
        // dateFin
        for (Iterator it = conventionManager.iterator(); it.hasNext();) {
            RFConventionJointAssConFouTsJointConventionAssure convention = (RFConventionJointAssConFouTsJointConventionAssure) it
                    .next();
            if (convention.getNSS().equals(assure.getNSS()) && convention.getDateFin().equals(assure.getDateFin())
                    && convention.getDateDebut().equals(assure.getDateDebut())) {
                viewBean.getIdSuppressionAssureArray().add(convention.getIdConas());
            }
        }
    }

    /**
     * Suppression d'un couple fournisseur/soin Si il est déjà en BDD on le supprime de la DB et du tableau Sinon on le
     * supprime que du tableau
     */
    public void supprimerCoupleFournisseurSTS(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        // on récupére du viewBean l'idFournisseur et l'idSousTypeSoin de la
        // ligne que l'on veut supprimer
        RFConventionViewBean outputViewBean = (RFConventionViewBean) viewBean;

        for (int i = 0; i < outputViewBean.getFournisseurTypeArray().size(); i++) {
            RFFournisseurType fournisseurType = (RFFournisseurType) outputViewBean.getFournisseurTypeArray().get(i);

            if (fournisseurType.getIdFournisseur().equals(outputViewBean.getIdFournisseur())
                    && fournisseurType.getIdSousTypeDeSoin().equals(outputViewBean.getIdSousTypeDeSoin())) {
                // est-elle en DB?
                if (fournisseurType.getIsChargeDepuisDB()) {
                    supprimerFournisseurSoinEnDB(session, fournisseurType, outputViewBean);
                }
                outputViewBean.getFournisseurTypeArray().remove(i);
                i--;
            }
        }
        // on vide le nom et l'adresse de paiement du viewBean
        supprimerInformationsFournisseur(outputViewBean);
    }

    /*
     * On supprime la ligne passé en paramètre (fournisseurType) de la DB
     */
    protected void supprimerFournisseurSoinEnDB(BSession session, RFFournisseurType fournisseurType,
            RFConventionViewBean viewBean) throws Exception {
        RFConventionJointAssConFouTsJointConventionAssureManager conventionManager = new RFConventionJointAssConFouTsJointConventionAssureManager();
        conventionManager.setSession(session);
        conventionManager.setForIdConvention(viewBean.getIdConvention());
        conventionManager.changeManagerSize(0);
        conventionManager.find();

        // si on a des resultats suppression du COFOS avec idFournisseur et
        // idSts correspondant.
        // On récupére idCOFOS pour supprimer les CONAS qui ont cet id.
        String idCOFOS = "";
        // voir si on ne peut pas faire un select - delete dans un manager?
        for (Iterator it = conventionManager.iterator(); it.hasNext();) {
            RFConventionJointAssConFouTsJointConventionAssure convention = (RFConventionJointAssConFouTsJointConventionAssure) it
                    .next();
            if (convention.getIdFournisseur().equals(fournisseurType.getIdFournisseur())
                    && convention.getIdSousTypeSoin().equals(fournisseurType.getIdSousTypeDeSoin())) {

                idCOFOS = convention.getIdCofos();
                // suppression de l'idCOFOS
                viewBean.getIdSuppressionFournisseurArray().add(idCOFOS);
                break;
            }
        }
    }

    /*
     * vide les champs de la page : information sur les fournisseurs et les assurés éventuels.
     */
    private void supprimerInformationsAssure(RFConventionViewBean vb) {
        // on vide les infos de l'assuré (nss, nom, dates, montant)
        vb.setIdAssure("");
        vb.setDescAssure("");
        vb.setNssTiers("");

        vb.setForDateDebut("");
        vb.setForDateFin("");
        vb.setForMontantAssure("");
    }

    /*
     * vide les champs de la page : information sur les fournisseurs et les assurés éventuels.
     */
    private void supprimerInformationsFournisseur(RFConventionViewBean vb) {
        // on vide les informations du fournisseur (nom et adresse)
        vb.setIdFournisseur("");
        vb.setIdAdressePaiement("");
        vb.setDescFournisseur("");
        vb.setDescAdressePaiement("");

        // on vide les codes type et sous type de soin
        vb.setCodeSousTypeDeSoinList("");
        vb.setCodeTypeDeSoinList("");
        vb.setCodeTypeDeSoin("");
        vb.setCodeSousTypeDeSoin("");
    }

    // vérification des données du tableau Assuré
    private boolean validationAssure(ArrayList assureArray, RFAssureConvention assure,
            RFConventionViewBean soinFournisseurVB) {
        if (verifierChampNonVideAssure(assure)) {
            if (isVerifContrainteUniciteAssure(assureArray, assure)) {
                return true;
            } else {
                afficheErreurIntegriteAssure(soinFournisseurVB);
            }
        } else {
            afficheErreurAssureIncomplet(soinFournisseurVB);
        }
        return false;
    }

    // vérifiation des données à entrer en BDD
    private boolean validationPourDB(BSession session, RFConventionViewBean SFViewBean) throws Exception {
        if (SFViewBean.getFournisseurTypeArray().size() == 0) {
            afficheErreurCoupleFTManquant(SFViewBean);
            return false;
        } else {
            return true;
        }
    }

    // vérification des données du tableau Type de soin/Fournisseurs
    private boolean validationSoinFournisseur(ArrayList<RFFournisseurType> fournisseurArray,
            RFFournisseurType fournisseurType, RFConventionViewBean soinFournisseurVB, BSession session)
            throws Exception {
        if (fournisseurType.estValide()) {
            // on check idAdressePaiement - idFournisseur - idSousTypeDeSoin
            for (RFFournisseurType ft : fournisseurArray) {
                if (ft.getIdSousTypeDeSoin().equals(fournisseurType.getIdSousTypeDeSoin())
                        && ft.getIdFournisseur().equals(fournisseurType.getIdFournisseur())
                        && ft.getIdAdressePaiement().equals(fournisseurType.getIdAdressePaiement())) {
                    afficheErreurIntegrite(soinFournisseurVB);
                    return false;
                }
            }
            // on vérifie que le couple fournisseur - type de soin n'est pas
            // déjà associé à une autre convention
            return !appartientAutreConvention(fournisseurType, soinFournisseurVB, session);
        } else {
            afficheErreurCoupleIncomplet(soinFournisseurVB);
        }
        return false;

    }

    /**
     * On vérifie qu'un couple fournisseur/type ai tous ces champs non vide
     */
    private boolean verifierChampNonVideAssure(RFAssureConvention assure) {
        return assure.estValide();
    }

}
