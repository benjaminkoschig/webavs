package globaz.cygnus.helpers.decisions;

import globaz.cygnus.db.decisions.RFCopieDecision;
import globaz.cygnus.db.decisions.RFCopieDecisionManager;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.decisions.RFDecisionJointCopie;
import globaz.cygnus.db.decisions.RFDecisionJointCopieManager;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.services.validerDecision.RFDevaliderDecisionService;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.cygnus.vb.decisions.RFDecisionJointTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * author fha
 */
public class RFDecisionJointTiersHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFDecisionJointTiersViewBean vb = (RFDecisionJointTiersViewBean) viewBean;

        RFDecision rfDecision = new RFDecision();
        rfDecision.setSession((BSession) session);
        rfDecision.setIdDecision(vb.getIdDecision());

        rfDecision.retrieve();

        if (!rfDecision.isNew()) {

            vb.setAnneeQD(rfDecision.getAnneeQD());
            vb.setEtatDecision(rfDecision.getEtatDecision());
            vb.setNumeroDecision(rfDecision.getNumeroDecision());
            vb.setDatePreparation(rfDecision.getDatePreparation());
            vb.setDateValidation(rfDecision.getDateValidation());
            vb.setIdAdresseDomicile(rfDecision.getIdAdresseDomicile());
            vb.setIdPreparePar(rfDecision.getIdPreparePar());
            vb.setIdValidePar(rfDecision.getIdValidePar());
            vb.setIncitationDepotNouvelleDemande(rfDecision.getIncitationDepotNouvelleDemande());
            vb.setRetourBV(rfDecision.getRetourBV());
            vb.setDecompteFactureRetour(rfDecision.getDecompteFactureRetour());
            vb.setBulletinVersementRetour(rfDecision.getBulletinVersementRetour());
            vb.setBordereauAccompagnement(rfDecision.getBordereauAccompagnement());
            vb.setIdGestionnaire(rfDecision.getIdGestionnaire());
            vb.setGenreDecision(rfDecision.getGenreDecision());
            vb.setTexteRemarque(rfDecision.getTexteRemarque());
            vb.setTexteAnnexe(rfDecision.getTexteAnnexe());
            vb.setMontantDepassementQd(rfDecision.getMontantDepassementQd());
            vb.setMontantExcedentDeRecette(rfDecision.getMontantExcedentDeRecette());
            vb.setMontantARembourserParLeDsas(rfDecision.getMontantARembourserParLeDsas());
            vb.setMontantTotalRFM(rfDecision.getMontantTotalRFM());
            vb.setIdQdPrincipale(rfDecision.getIdQdPrincipale());

        } else {
            RFUtils.setMsgErreurInattendueViewBean(viewBean, "retrieve()", "RFSaisieQdViewBean");
        }

        // Recherche du requerant si la décision est reliée à une grande Qd
        if (!JadeStringUtil.isBlankOrZero(vb.getIdQdPrincipale())) {

            RFAssQdDossierJointDossierJointTiersManager rfAssQdDosJoiDosJoiTieMgr = new RFAssQdDossierJointDossierJointTiersManager();

            rfAssQdDosJoiDosJoiTieMgr.setSession(vb.getSession());
            rfAssQdDosJoiDosJoiTieMgr.setForIdQd(vb.getIdQdPrincipale());
            rfAssQdDosJoiDosJoiTieMgr.setForCsTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            rfAssQdDosJoiDosJoiTieMgr.changeManagerSize(0);
            rfAssQdDosJoiDosJoiTieMgr.find();

            if (rfAssQdDosJoiDosJoiTieMgr.size() == 1) {
                RFAssQdDossierJointDossierJointTiers rfAssQdDosJoiDosJoiTie = (RFAssQdDossierJointDossierJointTiers) rfAssQdDosJoiDosJoiTieMgr
                        .getFirstEntity();
                if (rfAssQdDosJoiDosJoiTie != null) {
                    vb.setCsCanton(rfAssQdDosJoiDosJoiTie.getCsCanton());
                    vb.setCsNationalite(rfAssQdDosJoiDosJoiTie.getCsNationalite());
                    vb.setCsSexe(rfAssQdDosJoiDosJoiTie.getCsSexe());
                    vb.setDateDeces(rfAssQdDosJoiDosJoiTie.getDateDeces());
                    vb.setDateNaissance(rfAssQdDosJoiDosJoiTie.getDateNaissance());
                    vb.setNom(rfAssQdDosJoiDosJoiTie.getNom());
                    // vb.setNomTiers(rfAssQdDosJoiDosJoiTie.getNom());
                    vb.setNss(rfAssQdDosJoiDosJoiTie.getNss());
                    // vb.setNssTiers(rfAssQdDosJoiDosJoiTie.getNss());
                    vb.setPrenom(rfAssQdDosJoiDosJoiTie.getPrenom());
                    // vb.setPrenomTiers(rfAssQdDosJoiDosJoiTie.getPrenom());
                    vb.setIdTiers(rfAssQdDosJoiDosJoiTie.getIdTiers());
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(viewBean, "retrieve()", "RFSaisieQdViewBean");
                }

            } else {
                RFUtils.setMsgErreurInattendueViewBean(viewBean, "retrieve()", "RFSaisieQdViewBean");
            }

        }

        // Suppressions des tableaux déja en bdd
        clearCopieDecisionDBLines(vb.getCopieDecisionArray());

        // Chargement des copies liées à la décision
        RFDecisionJointCopieManager decisionCopie = new RFDecisionJointCopieManager();
        decisionCopie.setSession(vb.getSession());
        decisionCopie.setForIdDecision(vb.getIdDecision());
        decisionCopie.changeManagerSize(0);
        decisionCopie.find();

        if (vb.getCopieDecisionArray() == null) {
            vb.setCopieDecisionArray(new ArrayList<RFCopieDecisionsValidationData>());
        }

        // on charge les copies dans la table
        for (Iterator<RFDecisionJointCopie> it = decisionCopie.iterator(); it.hasNext();) {
            RFDecisionJointCopie copie = it.next();

            // si elle n'est pas dans le tableau des elts supprimés
            if (contains(vb.getCopieDecisionArray(), copie.getIdTiers())
                    && !RFUtils.containsIdArrayList(copie.getIdCopie(), vb.getIdSuppressionFournisseurArray())) {
                vb.getCopieDecisionArray().add(
                        new RFCopieDecisionsValidationData(copie.getIdCopie(), copie.getIdTiers(), descTiers(
                                copie.getIdTiers(), session), copie.getHasPageGarde(), copie.getHasVersement(), copie
                                .getHasDecompte(), copie.getHasRemarques(), copie.getHasMoyensDroit(), copie
                                .getHasSignature(), copie.getHasAnnexes(), copie.getHasCopies(), true));
            }
        }

        // si retourBV coché on ajoute une phrase aux remarques et on coche "bulletin de versement en retour"
        if (vb.getRetourBV()) {
            vb.setBulletinVersementRetour(true);
        }

        vb.setMontantAPayerPlusExcedentDeRecettePlusForcerPaiement(new BigDecimal(vb.getMontantTotalRFM())
                .add(new BigDecimal(vb.getMontantExcedentDeRecette()))
                .add(new BigDecimal(vb.getMontantDepassementQd())).toString());

        vb.setMontantTotalARembourser(new BigDecimal(vb.getMontantRembourserRFM()).add(
                new BigDecimal(vb.getMontantARembourserParLeDsas())).toString());

    }

    /**
     * Modification d'une décision - à voir si on garde
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // il faut récupérer le contenu du tableau
        BITransaction transaction = null;

        List<RFCopieDecisionsValidationData> listCopie = deserializedCopie(viewBean);

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            // on arrive avec RFDecisionJointTiersViewBean qu'il faut updater
            RFDecisionJointTiersViewBean rfDecisionVB = (RFDecisionJointTiersViewBean) viewBean;
            // suppression en base des copies
            clearIdSuppressionCopieArray(rfDecisionVB, (BSession) session);

            // initialisation des champs à updater : il faut update chaque table
            // qui subit des modifications
            // table decision
            RFDecision decision = new RFDecision();
            decision.setSession(rfDecisionVB.getSession());
            decision.setIdDecision(rfDecisionVB.getIdDecision());
            decision.retrieve(transaction);

            if (!decision.isNew()) {
                decision.setIdGestionnaire(rfDecisionVB.getIdGestionnaire());
                // decision.setAnneeQD(rfDecisionVB.getAnneeQD());
                // decision.setDatePreparation(rfDecisionVB.getDatePreparation());
                // decision.setDateValidation(rfDecisionVB.getDateValidation());
                // decision.setIdPreparePar(rfDecisionVB.getIdPreparePar());
                // decision.setIdValidePar(rfDecisionVB.getIdValidePar());
                // decision.setNumeroDecision(rfDecisionVB.getNumeroDecision());
                decision.setTexteRemarque(rfDecisionVB.getTexteRemarque());
                decision.setTexteAnnexe(rfDecisionVB.getTexteAnnexe());
                decision.setIncitationDepotNouvelleDemande(rfDecisionVB.getIncitationDepotNouvelleDemande());
                decision.setRetourBV(rfDecisionVB.getRetourBV());
                decision.setDecompteFactureRetour(rfDecisionVB.getDecompteFactureRetour());
                decision.setBulletinVersementRetour(rfDecisionVB.getBulletinVersementRetour());
                decision.setBordereauAccompagnement(rfDecisionVB.getBordereauAccompagnement());
                decision.setGenreDecision(rfDecisionVB.getGenreDecision());
                // decision.setMontantDepassementQd(rfDecisionVB.getMontantDepassementQd());
                // decision.setMontantExcedentDeRecette(rfDecisionVB.getMontantExcedentDeRecette());

                decision.update(transaction);
                // on ajoute chaque copie qui n'est pas déja en bdd du tableau
                // de copies à la table RFCopie
                // -> problème certaines en update d'autres en ajout!!!!
                for (RFCopieDecisionsValidationData copie : listCopie /* rfDecisionVB.getCopieDecisionArray() */) {
                    RFCopieDecision copieDecision = new RFCopieDecision();
                    copieDecision.setSession((BSession) session);

                    if (!JadeStringUtil.isBlank(copie.getIdCopie())) {
                        copieDecision.setIdCopie(copie.getIdCopie());
                        copieDecision.retrieve(transaction);
                    }

                    copieDecision.setIdTiers(copie.getIdDestinataire());
                    copieDecision.setIdDecision(rfDecisionVB.getIdDecision());
                    copieDecision.setHasAnnexes(copie.getHasAnnexes());
                    copieDecision.setHasCopies(copie.getHasCopies());
                    copieDecision.setHasDecompte(copie.getIsDecompte());
                    copieDecision.setHasMoyensDroit(copie.getHasMoyensDroit());
                    copieDecision.setHasPageGarde(copie.getHasPageDeGarde());
                    copieDecision.setHasRemarques(copie.getHasRemarque());
                    copieDecision.setHasSignature(copie.getHasSignature());
                    copieDecision.setHasVersement(copie.getIsVersement());

                    if (JadeStringUtil.isBlank(copie.getIdCopie())) {
                        copieDecision.add(transaction);
                    } else {
                        copieDecision.update(transaction);
                    }
                }
            } else {
                RFUtils.setMsgErreurInattendueViewBean(decision, "_delete()", "RFDecisionJointTiersHelper");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()
                            || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

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

    private void clearCopieDecisionDBLines(ArrayList<RFCopieDecisionsValidationData> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getIsEnDB()) {
                array.remove(i);
                i--;// car le suivant est à présent le courant
            }
        }
    }

    /**
     * supprime les enregistrements des copies de décisions déjà en BDD que l'utilisateur a souhaité supprimer
     * 
     * @param SFViewBean
     * @param session
     * @throws Exception
     */
    protected void clearIdSuppressionCopieArray(RFDecisionJointTiersViewBean viewBean, BSession session)
            throws Exception {
        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();
            for (Iterator it = viewBean.getIdSuppressionFournisseurArray().iterator(); it.hasNext();) {
                RFCopieDecision copie = new RFCopieDecision();
                copie.setSession(session);
                copie.setIdCopie((String) it.next());
                copie.retrieve();
                if (!copie.isNew()) {
                    copie.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(copie, "_delete()", "RFDecisionJointTiersHelper");
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

    // cherche si ce fournisseur est déjà en copie
    protected boolean contains(ArrayList<RFCopieDecisionsValidationData> copiesArray, String idTiers) {

        for (RFCopieDecisionsValidationData cd : copiesArray) {
            if (cd.getIdDestinataire().equals(idTiers)) {
                return false;
            }
        }

        return true;
    }

    protected String descTiers(String idTiers, BISession session) throws Exception {

        PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersParId(session, idTiers);
        if (null == tiersWrapper) {
            tiersWrapper = PRTiersHelper.getAdministrationParId(session, idTiers);
        }

        if (null != tiersWrapper) {
            // infos du tiers
            String nom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);
            String prenom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            return nom + " " + prenom;
        }
        return "";
    }

    private List<RFCopieDecisionsValidationData> deserializedCopie(FWViewBeanInterface viewBean) {
        Type collectionType = new TypeToken<List<RFCopieDecisionsValidationData>>() {
        }.getType();

        Gson gson = new Gson();
        String json = ((RFDecisionJointTiersViewBean) viewBean).getJsonCopie();
        List<RFCopieDecisionsValidationData> list = gson.fromJson(json, collectionType);

        return list;
    }

    /**
     * Edition d'une copie d'une décision
     */
    // public void editerCopie(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {
    // // on récupére du viewBean l'idFournisseur et l'idSousTypeSoin de la
    // // ligne que l'on veut supprimer
    // RFDecisionJointTiersViewBean outputViewBean = (RFDecisionJointTiersViewBean) viewBean;
    //
    // for (int i = 0; i < outputViewBean.getCopieDecisionArray().size(); i++) {
    // RFCopieDecisionsValidationData copieDecision = outputViewBean.getCopieDecisionArray().get(i);
    //
    // if (copieDecision.getIdDestinataire().equals(outputViewBean.getIdTiers())) {
    // // est-elle en DB?
    //
    // if (copieDecision.getIsEnDB()) {
    // // this.editerCopieEnDB(session, copieDecision, outputViewBean);
    // }
    // // switch()
    // outputViewBean.getCopieDecisionArray().get(i).setHasPageDeGarde(Boolean.FALSE);
    // }
    // }
    // // simule un retour depuis pyxis
    // outputViewBean.setAutreRetour(true);
    // }

    public FWViewBeanInterface devalider(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RFDecisionJointTiersViewBean vb = (RFDecisionJointTiersViewBean) viewBean;

        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();

            RFDevaliderDecisionService.setIdGestionnaire(session.getUserId());
            RFDevaliderDecisionService.setSession(session);
            RFDevaliderDecisionService.setTransaction(transaction);

            RFDevaliderDecisionService.devaliderDecision(vb.getIdDecision(), vb);

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(vb, e.getMessage().toString());
        } finally {
            if ((transaction != null) && !vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
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

        return viewBean;

    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Suppression d'une copie d'une décision Si il est déjà en BDD on le supprime de la DB et du tableau Sinon on le
     * supprime que du tableau
     */
    public void supprimerCopie(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {
        // on récupére du viewBean l'idFournisseur et l'idSousTypeSoin de la
        // ligne que l'on veut supprimer
        RFDecisionJointTiersViewBean outputViewBean = (RFDecisionJointTiersViewBean) viewBean;

        for (int i = 0; i < outputViewBean.getCopieDecisionArray().size(); i++) {
            RFCopieDecisionsValidationData copieDecision = outputViewBean.getCopieDecisionArray().get(i);

            if (copieDecision.getIdDestinataire().equals(outputViewBean.getIdTiers())) {
                // est-elle en DB?
                if (copieDecision.getIsEnDB()) {// on met la ppté supprimer =
                                                // true
                    supprimerCopieEnDB(session, copieDecision, outputViewBean);
                }
                outputViewBean.getCopieDecisionArray().remove(i);
                i--;
            }
        }
        // simule un retour depuis pyxis
        outputViewBean.setAutreRetour(true);
    }

    /*
     * On supprime la ligne passé en paramètre (fournisseurType) de la DB
     */
    protected void supprimerCopieEnDB(BSession session, RFCopieDecisionsValidationData copieDecision,
            RFDecisionJointTiersViewBean viewBean) throws Exception {

        RFCopieDecisionManager copieManager = new RFCopieDecisionManager();
        copieManager.setSession(session);
        copieManager.setForIdTiers(viewBean.getIdDestinataire());
        copieManager.changeManagerSize(0);
        copieManager.find();

        for (Iterator<RFCopieDecision> it = copieManager.iterator(); it.hasNext();) {
            RFCopieDecision copie = it.next();
            if (copie.getIdTiers().equals(copieDecision.getIdDestinataire())) {
                viewBean.getIdSuppressionFournisseurArray().add(copie.getIdCopie());
                break;
            }
        }
    }

}
