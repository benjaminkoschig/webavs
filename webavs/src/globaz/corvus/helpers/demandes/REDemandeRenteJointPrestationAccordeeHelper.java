package globaz.corvus.helpers.demandes;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.itext.REListeDemandeRente;
import globaz.corvus.regles.REDemandeRegles;
import globaz.corvus.utils.REDemandeUtils;
import globaz.corvus.utils.REPostItsFilteringUtils;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeListViewBean;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeViewBean;
import globaz.corvus.vb.demandes.REDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;

public class REDemandeRenteJointPrestationAccordeeHelper extends PRAbstractHelper {

    public FWViewBeanInterface actionCopierDemande(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            REDemandeRenteViewBean vb = (REDemandeRenteViewBean) viewBean;
            REDemandeRente dr = new REDemandeRente();
            dr.setSession(session);
            dr.setIdDemandeRente(vb.getIdDemandeRente());
            dr.retrieve(transaction);

            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(dr.getCsTypeDemandeRente())
                    || IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(dr.getCsTypeDemandeRente())) {

                REDemandeRente demande = REDemandeRente.loadDemandeRente(session, transaction, dr.getIdDemandeRente(),
                        dr.getCsTypeDemandeRente());
                REDemandeRente newDemande = REDemandeRegles.copierDemandeRente(session, transaction, demande);
                newDemande.retrieve(transaction);
                newDemande.setIdRenteCalculee("");
                newDemande.setDateDepot(null);
                newDemande.setDateReception(null);
                newDemande.setDateDebut(null);
                newDemande.setDateFin(null);
                newDemande.setIdGestionnaire(session.getUserId());
                newDemande.update(transaction);

                vb.setIdDemandeRenteCopiee(newDemande.getIdDemandeRente());
            }
            return viewBean;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            return viewBean;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            viewBean.setMessage(transaction.getErrors().toString());
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (transaction != null) {
                        transaction.closeTransaction();
                    }
                }
            }
        }

    }

    /**
     * Mandat Inforom 499 : création d'une prestation transitoire Une prestation transitoire se créer par le clonage
     * d'une demande de rente INVALIDITE uniquement Entités de base clonés : [REDemandeRenteInvalidite] // Depuis id
     * reçu de viewBean.getIdDemandeRente() REDemandeRenteInvalidite ---(idRenteCalculee) (0->1)--> [RERenteCalculee]
     * RERECAL REDemandeRenteInvalidite ---(idRenteCalculee) (0->N)--> [REBaseCalcul] REBACAL REDemandeRenteInvalidite
     * ---(idDemandeRente) (0->N)--> [REPeriodeInvalidite] REPEINV Comment sa se passe ici : 1-> On récupère l'entité
     * REdemandeRenteInvalidité qu'on va cloner 2-> On récupère l'idTiers du requérant 3-> On va rechercher sa rente 50
     * (invalidité) (si il y en plusieurs, on prend la dernière donc, sans date de fin ou avec la date de fin la plus
     * récente) 4-> Depuis cette rente 50, on récupère la base de calcul (1 seule) 5-> On créé une nouvelle entité
     * RERenteCalcule and more.....
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionCopierDemandePourPrestTrans(FWViewBeanInterface vb, FWAction action,
            BSession session) throws Exception {

        BITransaction transaction = null;
        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            REDemandeRenteViewBean viewBean = (REDemandeRenteViewBean) vb;

            REDemandeRente demandeOriginal = new REDemandeRente();
            demandeOriginal.setSession(session);
            demandeOriginal.setIdDemandeRente(viewBean.getIdDemandeRente());
            demandeOriginal.retrieve(transaction);
            if (demandeOriginal.isNew()) {
                throw new Exception(translate(session, "PRESTATION_TRANSITOIRE_IMPOSSIBLE_TROUVER_DEMANDE_A_COPIER"));
            }

            // Si la demande n'est pas du bon type (invalidité) ou est indéfinie, on arrête là.
            if ((demandeOriginal.getCsTypeDemandeRente() == null)
                    || (!demandeOriginal.getCsTypeDemandeRente().equals(
                            IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE))) {
                throw new Exception(translate(session, "PRESTATION_TRANSITOIRE_DEMANDE_ISNOT_TYPE_INVALIDITE"));
            }

            /**
             * 1-> On récupère l'entité REdemandeRenteInvalidité qui sera clonée
             */
            // Charge la demande de rente et retourne un object spécialisé de
            // type REDemandeRenteInvalidite
            REDemandeRenteInvalidite demandeInvalidite = (REDemandeRenteInvalidite) REDemandeRente.loadDemandeRente(
                    session, transaction, demandeOriginal.getIdDemandeRente(), demandeOriginal.getCsTypeDemandeRente());

            // Ne devrais jamais arriver mais bon, on est jamais trop prudent !!
            if (!(demandeInvalidite instanceof REDemandeRenteInvalidite)) {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_ERREUR_INTERNE_IMPOSSIBLE_RECUPERER_DEMANDE_TYPE_INVALIDITE"));
            }

            /**
             * 2-> On récupère l'idTiers du requérant Il faut passer par la table demande de prestation PRDEMAP
             */
            PRDemande demandeDePrestation = new PRDemande();
            demandeDePrestation.setSession(session);
            demandeDePrestation.setId(demandeInvalidite.getIdDemandePrestation());
            demandeDePrestation.retrieve(transaction);
            if (demandeDePrestation.isNew()) {
                throw new Exception(
                        translate(session, "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_DEMANDE_PRESTATION"));
            }
            String idTiersRequerant = demandeDePrestation.getIdTiers();
            if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_IMPOSSIBLE_ID_TIERS_REQUERANT_DEPUIS_DEMANDE_PRESTATION"));
            }

            /**
             * 3-> On va rechercher sa rente 50 (invalidité) (si il y en plusieurs, on prend la dernière donc, sans date
             * de fin ou avec la date de fin la plus récente)
             */
            RERenteAccordee rente50DuTiers = REDemandeUtils.getDerniereRenteInvaliditeDuTiers(session, transaction,
                    idTiersRequerant);

            // Voilà, on à récupéré la rente 50 du tiers la plus récente. On s'en assure
            if (rente50DuTiers == null) {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_ERREUR_INTERNE_IMPOSSIBLE_RECUPERER_RENTE_INVALIDITE_LA_PLUS_RECENTE"));
            }

            /**
             * 4-> Depuis cette rente 50, on récupère la base de calcul (une seule) dans le but unique de connaître
             * quelle version du droit est appliqué
             */
            REBasesCalcul baseDeCalculRente50DuTiers = new REBasesCalcul();
            baseDeCalculRente50DuTiers.setSession(session);
            baseDeCalculRente50DuTiers.setIdBasesCalcul(rente50DuTiers.getIdBaseCalcul());
            baseDeCalculRente50DuTiers.retrieve(transaction);

            if (baseDeCalculRente50DuTiers.isNew()) {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_BASE_CALCUL_LIEE_RENTE_INVALIDITE"));
            }

            String droitApplique = baseDeCalculRente50DuTiers.getDroitApplique();
            if (JadeStringUtil.isBlankOrZero(droitApplique)) {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_VERSION_DROIT_APPLIQUE_BASE_CALCUL"));
            }

            long versionDroit = Integer.MAX_VALUE;
            try {
                versionDroit = Integer.valueOf(droitApplique);
            } catch (Exception exception) {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_VERSION_DROIT_APPLIQUE_BASE_CALCUL"));
            }

            if (versionDroit == 9) {
                baseDeCalculRente50DuTiers = new REBasesCalculNeuviemeRevision();
                baseDeCalculRente50DuTiers.setSession(session);
                baseDeCalculRente50DuTiers.setIdBasesCalcul(rente50DuTiers.getIdBaseCalcul());
                baseDeCalculRente50DuTiers.retrieve(transaction);
            } else if (versionDroit == 10) {
                baseDeCalculRente50DuTiers = new REBasesCalculDixiemeRevision();
                baseDeCalculRente50DuTiers.setSession(session);
                baseDeCalculRente50DuTiers.setIdBasesCalcul(rente50DuTiers.getIdBaseCalcul());
                baseDeCalculRente50DuTiers.retrieve(transaction);
            } else {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_VERSION_DROIT_APPLIQUE_BASE_CALCUL_INEXISTANTE"));
            }

            /**
             * Ok, maintenant on à déjà quelque entités à cloner dont : -> La demande de rente invalidité :
             * demandeInvalidite -> La base de calcul : baseDeCalculRente50DuTiers -> On va créer une nouvelle
             * renteCalculee Donc : ATTENTION !!! Depuis ici on commence à cloner nos entités
             */

            // On créé une nouvelle entité RERenteCalcule
            RERenteCalculee renteCalculee = new RERenteCalculee();
            renteCalculee.setSession(session);
            renteCalculee.add(transaction);

            demandeInvalidite.setIdDemandeRente(null);
            demandeInvalidite.setIdRenteCalculee(renteCalculee.getId());
            demandeInvalidite.setCsTypeCalcul(IREDemandeRente.CS_TYPE_CALCUL_TRANSITOIRE);
            demandeInvalidite.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
            demandeInvalidite.setIdInfoComplementaire(String.valueOf(0));
            demandeInvalidite.setIdDemandeRenteParent(String.valueOf(0));
            demandeInvalidite.add(transaction);

            baseDeCalculRente50DuTiers.setIdBasesCalcul(null);
            baseDeCalculRente50DuTiers.setIdRenteCalculee(renteCalculee.getId());
            baseDeCalculRente50DuTiers.add(transaction);

            // On renseigne l'id de la nouvelle demande de rente
            ((REDemandeRenteViewBean) vb).setIdDemandeRenteCopiee(demandeInvalidite.getIdDemandeRente());
            ((REDemandeRenteViewBean) vb).setIdDemandeRente(demandeInvalidite.getIdDemandeRente());
            return vb;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            vb.setMessage("Error : " + e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
            return vb;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            vb.setMessage(transaction.getErrors().toString());
                            vb.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (transaction != null) {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        super._find(persistentList, action, session);

        REPostItsFilteringUtils
                .keepPostItsForLastDemandeOnly((Iterable<REDemandeRenteJointPrestationAccordeeViewBean>) persistentList);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    public FWViewBeanInterface imprimerListeDemandeRente(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        REDemandeRenteJointPrestationAccordeeListViewBean dViewBean = (REDemandeRenteJointPrestationAccordeeListViewBean) viewBean;

        REListeDemandeRente listeDemandeRente = new REListeDemandeRente();
        listeDemandeRente.setSession(session);
        listeDemandeRente.setForCsEtatDemande(dViewBean.getForCsEtatDemande());
        listeDemandeRente.setForCsSexe(dViewBean.getForCsSexe());
        listeDemandeRente.setForCsType(dViewBean.getForCsType());
        listeDemandeRente.setForCsTypeCalcul(dViewBean.getForCsTypeCalcul());
        listeDemandeRente.setForDateDebut(dViewBean.getForDateDebut());
        listeDemandeRente.setForDateNaissance(dViewBean.getForDateNaissance());
        listeDemandeRente.setForDroitAu(dViewBean.getForDroitAu());
        listeDemandeRente.setForDroitDu(dViewBean.getForDroitDu());
        listeDemandeRente.setForIdGestionnaire(dViewBean.getForIdGestionnaire());
        listeDemandeRente.setIsRechercheFamille(dViewBean.getIsRechercheFamille());
        listeDemandeRente.setIsEnCours(dViewBean.isEnCours());
        listeDemandeRente.setLikeNom(dViewBean.getLikeNom());
        listeDemandeRente.setLikeNumeroAVS(dViewBean.getLikeNumeroAVS());
        listeDemandeRente.setLikeNumeroAVSNSS(dViewBean.getLikeNumeroAVSNNSS());
        listeDemandeRente.setLikePrenom(dViewBean.getLikePrenom());
        listeDemandeRente.setOrderBy(dViewBean.getOrderBy());

        listeDemandeRente.executeProcess();

        if ((listeDemandeRente.getAttachedDocuments() != null) && (listeDemandeRente.getAttachedDocuments().size() > 0)) {
            dViewBean.setAttachedDocuments(listeDemandeRente.getAttachedDocuments());
        } else {
            dViewBean.setAttachedDocuments(null);
        }
        return viewBean;
    }

    /**
     * Return the translated label from the provided textKey
     * 
     * @param session
     * @param textKey
     * @return
     */
    private String translate(BSession session, String textKey) {
        return session.getLabel(textKey);
    }
}
