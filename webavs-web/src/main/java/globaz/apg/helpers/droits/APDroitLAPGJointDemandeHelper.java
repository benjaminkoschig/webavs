/*
 * Créé le 3 juin 05
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.droits;

import ch.globaz.common.exceptions.Exceptions;
import globaz.apg.ApgServiceLocator;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.application.APApplication;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.annonces.APAnnonceAPGManager;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APDroitPandemie;
import globaz.apg.db.droits.APDroitPaternite;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.apg.vb.droits.APDroitLAPGJointDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.clone.factory.PRCloneFactory;
import globaz.prestation.helpers.PRAbstractHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <H1>Description</H1>
 *
 * @author scre
 */
public class APDroitLAPGJointDemandeHelper extends PRAbstractHelper {

    public static final String CALCULER_ACM = "calculerACM";
    public static final String CALCULER_DROIT_MATERNITE_CANTONALE = "calculerDroitMaterniteCantonale";
    public static final String COPIER_DROIT = "copierDroit";
    public static final String CORRIGER_DROIT = "corrigerDroit";
    public static final String NEW_DROIT_ID = "newDroitId";
    public static final String RESTITUER_DROIT = "restituerDroit";
    private static final String CLONE_COPIER_DROIT_PANDEMIE = "prestation-PAN-copie-droit-PAN";
    private static final String CLONE_COPIER_DROIT_PATERNITE = "prestation-APG-copie-droit-PATERNITE";
    private static final String CLONE_COPIER_DROIT_PROCHE_AIDANT = "prestation-APG-copie-droit-PROCHE_AIDANT";
    private static final String CLONE_COPIER_CORRIGER_DROIT_PANDEMIE = "prestation-PAN-correction-droit-PAN";
    private static final String CLONE_COPIER_CORRIGER_DROIT_PATERNITE = "prestation-APG-correction-droit-PATERNITE";
    private static final String CLONE_COPIER_CORRIGER_DROIT_PROCHE_AIDANT = "prestation-APG-correction-droit-PROCHE_AIDANT";


    public void calculerACM(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        APDroitLAPGJointDemandeViewBean vbDroit = vaChercherGenreServiceOuEtatSiAbsent(
                (APDroitLAPGJointDemandeViewBean) viewBean, session);
        try {

            // Le calcul des ACM implique que le droit doit être en attente.
            if (vbDroit.getEtatDroit().equals(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE)) {

                APDroitLAPG droit = APGUtils.loadDroit(session, vbDroit.getIdDroit(), vbDroit.getGenreService());

                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List basesCalcul = APBasesCalculBuilder.of(session, droit).createBasesCalcul();

                // Ce test se fait lors du calcul de montant de la prestation
                //
                //
                // // on ne calcul les ACM que si on verse la prestation a
                // l'employeur et
                // // que le tier n'est pas un indépendant
                // Iterator iter = basesCalcul.iterator();
                // while(iter.hasNext()){
                // APBaseCalcul baseCalcul = (APBaseCalcul)iter.next();
                // List situationsProf =
                // baseCalcul.getBasesCalculSituationProfessionnel();
                //
                // Iterator iter2 = situationsProf.iterator();
                // while(iter2.hasNext()){
                // APBaseCalculSituationProfessionnel situationProf =
                // (APBaseCalculSituationProfessionnel)iter2.next();
                //
                // // on retire les bases de calcul qui ne correspondes pas aux
                // criteres
                // if(baseCalcul.isIndependant() ||
                // !situationProf.isPaiementEmployeur()){
                // basesCalcul.remove(baseCalcul);
                // iter = basesCalcul.iterator();
                // }
                // }
                // }

                if (basesCalcul.size() > 0) {
                    calculateur.calculerAcmAlpha(session, droit, basesCalcul);
                }

            } else {
                vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                vbDroit.setMessage(session.getLabel("ACTION_IMPOSSIBLE_POUR_DROIT_NON_ATTENTE"));
            }
        } catch (Exception e) {
            vbDroit.setMsgType(FWViewBeanInterface.ERROR);
            vbDroit.setMessage(e.getMessage());
            throw e;
        }
    }

    /**
     * Calcul d'une nouvelle prestation rétroactive pour ajouter le supplément alloué par certaines caisses cantonales
     *
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void calculerDroitMaterniteCantonale(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        APDroitLAPGJointDemandeViewBean vbDroit = vaChercherGenreServiceOuEtatSiAbsent(
                (APDroitLAPGJointDemandeViewBean) viewBean, session);

        // le droit est dans l'état Attente
        if (vbDroit.getEtatDroit().equals(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE)) {
            // le droit est un droit maternite
            if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(vbDroit.getGenreService())) {
                APDroitMaternite droit = new APDroitMaternite();

                droit.setSession(session);
                droit.setIdDroit(vbDroit.getIdDroit());
                droit.retrieve(session.getCurrentThreadTransaction());

                // calculer le droit maternité cantonale
                APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
                List basesCalcul = APBasesCalculBuilder.of(session, droit).createBasesCalcul();

                calculateur.calculerDroitMatCantonale(session, droit, basesCalcul);

            } else {
                vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                vbDroit.setMessage(session.getLabel("ACTION_IMPOSSIBLE_POUR_DROIT_APG"));
            }
        } else {
            vbDroit.setMsgType(FWViewBeanInterface.ERROR);
            vbDroit.setMessage(session.getLabel("ACTION_IMPOSSIBLE_POUR_DROIT_NON_ATTENTE"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APDroitLAPGJointDemandeViewBean copierDroit(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        APDroitLAPGJointDemandeViewBean vbDroit = vaChercherGenreServiceOuEtatSiAbsent(
                (APDroitLAPGJointDemandeViewBean) viewBean, session);
        IPRCloneable clone = null;
        if (APGUtils.isTypeProcheAidant(vbDroit.getGenreService())) {
            APDroitProcheAidant droit = new APDroitProcheAidant();

            droit.setSession(session);
            droit.setIdDroit(vbDroit.getIdDroit());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.wantCallExternalServices(false);

            clone = PRCloneFactory.getInstance().clone(
                    session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME), session,
                    droit, CLONE_COPIER_DROIT_PROCHE_AIDANT,
                    IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT);

            // on met à jour la date de reception et la date de depot avec la
            // date du jour.
            droit = new APDroitProcheAidant();
            droit.setSession(session);
            droit.setIdDroit(clone.getUniquePrimaryKey());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.setDateDepot(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.wantCallValidate(false);
            droit.update(session.getCurrentThreadTransaction());

            updateNbJourIndemniseAZeroDansSituationProfessionnel(session, droit.getIdDroit());

            vbDroit.setDto(new APDroitAPGDTO(droit));
        } else if (APGUtils.isTypeAllocationPandemie(vbDroit.getGenreService())) {
            APDroitPandemie droit = new APDroitPandemie();

            droit.setSession(session);
            droit.setIdDroit(vbDroit.getIdDroit());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.wantCallExternalServices(false);

            clone = PRCloneFactory.getInstance().clone(
                    session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME), session,
                    droit, CLONE_COPIER_DROIT_PANDEMIE,
                    IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT);

            // on met à jour la date de reception et la date de depot avec la
            // date du jour.
            droit = new APDroitPandemie();
            droit.setSession(session);
            droit.setIdDroit(clone.getUniquePrimaryKey());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.setDateDepot(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.wantCallValidate(false);
            droit.update(session.getCurrentThreadTransaction());

            vbDroit.setDto(new APDroitAPGDTO(droit));
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(vbDroit.getGenreService())) {
            APDroitPaternite droit = new APDroitPaternite();

            droit.setSession(session);
            droit.setIdDroit(vbDroit.getIdDroit());
            droit.retrieve(session.getCurrentThreadTransaction());

            clone = PRCloneFactory.getInstance().clone(
                    session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME), session,
                    droit, CLONE_COPIER_DROIT_PATERNITE,
                    IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT);
            // on met à jour la date de reception et la date de depot avec la
            // date du jour.
            droit = new APDroitPaternite();
            droit.setSession(session);
            droit.setIdDroit(clone.getUniquePrimaryKey());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.setDateDepot(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.wantCallValidate(false);
            droit.update(session.getCurrentThreadTransaction());

            vbDroit.setDto(new APDroitAPGDTO(droit));
        } else if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(vbDroit.getGenreService())) {
            APDroitAPG droit = new APDroitAPG();

            droit.setSession(session);
            droit.setIdDroit(vbDroit.getIdDroit());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.wantCallExternalServices(false);

            clone = PRCloneFactory.getInstance().clone(
                    session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME), session,
                    droit, session.getApplication().getProperty(APApplication.PROPERTY_CLONE_COPIER_DROIT_APG),
                    IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT);

            // on met à jour la date de reception et la date de depot avec la
            // date du jour.
            droit = new APDroitAPG();
            droit.setSession(session);
            droit.setIdDroit(clone.getUniquePrimaryKey());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.setDateDepot(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.wantCallValidate(false);
            droit.update(session.getCurrentThreadTransaction());

            vbDroit.setDto(new APDroitAPGDTO(droit));

        } else {
            APDroitMaternite droit = new APDroitMaternite();

            droit.setSession(session);
            droit.setIdDroit(vbDroit.getIdDroit());
            droit.retrieve(session.getCurrentThreadTransaction());

            clone = PRCloneFactory.getInstance().clone(
                    session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME), session,
                    droit, session.getApplication().getProperty(APApplication.PROPERTY_CLONE_COPIER_DROIT_MATERNITE),
                    IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT);
            // on met à jour la date de reception et la date de depot avec la
            // date du jour.
            droit = new APDroitMaternite();
            droit.setSession(session);
            droit.setIdDroit(clone.getUniquePrimaryKey());
            droit.retrieve(session.getCurrentThreadTransaction());
            droit.setDateDepot(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.setDateReception(JadeDateUtil.getGlobazFormattedDate(new Date()));
            droit.wantCallValidate(false);
            droit.update(session.getCurrentThreadTransaction());

            vbDroit.setDto(new APDroitAPGDTO(droit));
        }

        // pour rediriger sur le detail du nouveau droit
        APDroitLAPGJointDemandeViewBean nvDroit = new APDroitLAPGJointDemandeViewBean();
        nvDroit.setIdDroit(clone.getUniquePrimaryKey());
        return nvDroit;
    }

    private void updateNbJourIndemniseAZeroDansSituationProfessionnel(final BSession session, final String idDroit) throws Exception {
        APSituationProfessionnelleManager situationProfessionnelleManager = new APSituationProfessionnelleManager();
        situationProfessionnelleManager.setSession(session);
        situationProfessionnelleManager.setForIdDroit(idDroit);
        situationProfessionnelleManager.find(BManager.SIZE_NOLIMIT);
        situationProfessionnelleManager
                .<APSituationProfessionnelle>getContainerAsList()
                .forEach(situationProfessionnelle -> Exceptions.checkedToUnChecked(() -> {
                        situationProfessionnelle.wantCallValidate(false);
                        situationProfessionnelle.setSession(session);
                        situationProfessionnelle.setNbJourIndemnise(0);
                        situationProfessionnelle.update(session.getCurrentThreadTransaction());
                    })
                );
    }

    /**
     * DOCUMENT ME!
     *
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void corrigerDroit(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {
        APDroitLAPGJointDemandeViewBean vbDroit = vaChercherGenreServiceOuEtatSiAbsent(
                (APDroitLAPGJointDemandeViewBean) viewBean, session);
        // corriger un droit dans l'etat definitif ou partiel
        if (vbDroit.getEtatDroit().equals(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF)
                || vbDroit.getEtatDroit().equals(IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL)) {

            BStatement statement = null;
            BTransaction bTrans = (BTransaction) session.newTransaction();
            APPrestationManager prestMan = null;
            try {

                prestMan = new APPrestationManager();
                prestMan.setForIdDroit(vbDroit.getIdDroit());
                prestMan.setSession(session);

                bTrans.openTransaction();
                statement = prestMan.cursorOpen(bTrans);
                APPrestation prestation = null;

                while ((prestation = (APPrestation) prestMan.cursorReadNext(statement)) != null) {
                    // les prestations non-versees sont annulee
                    // point ouvert 00616
                    // les prestations ne sont plus supprimees, mais mises
                    // dans l'etat annule
                    if (!IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestation.getEtat())) {
                        prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_ANNULE);
                        prestation.update(bTrans);
                    }
                }
                prestMan.cursorClose(statement);

                // on clone le droit parent
                if (APGUtils.isTypeProcheAidant(vbDroit.getGenreService())) {
                    APDroitProcheAidant droit = new APDroitProcheAidant();
                    droit.setSession(session);
                    droit.setIdDroit(vbDroit.getIdDroit());
                    droit.retrieve(bTrans);

                    PRCloneFactory.getInstance().clone(
                            session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                            session, bTrans, droit,
                            CLONE_COPIER_CORRIGER_DROIT_PROCHE_AIDANT,
                            IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_FILS);

                    vbDroit.setDto(new APDroitAPGDTO(droit));
                    droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
                    droit.update(bTrans);
                } else if (APGUtils.isTypeAllocationPandemie(vbDroit.getGenreService())) {
                    APDroitPandemie droit = new APDroitPandemie();
                    droit.setSession(session);
                    droit.setIdDroit(vbDroit.getIdDroit());
                    droit.retrieve(bTrans);

                    PRCloneFactory.getInstance().clone(
                            session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                            session, bTrans, droit,
                            CLONE_COPIER_CORRIGER_DROIT_PANDEMIE,
                            IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_FILS);

                    vbDroit.setDto(new APDroitAPGDTO(droit));
                    droit.setValiderPeriodes(false);
                    droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
                    droit.update(bTrans);
                } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(vbDroit.getGenreService())) {
                    APDroitPaternite droit = new APDroitPaternite();

                    droit.setSession(session);
                    droit.setIdDroit(vbDroit.getIdDroit());
                    droit.retrieve(session.getCurrentThreadTransaction());

                    PRCloneFactory.getInstance().clone(
                            session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                            session, bTrans, droit,
                            CLONE_COPIER_CORRIGER_DROIT_PATERNITE,
                            IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_FILS);

                    // le droit parent passe dans l'etat definitif
                    droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
                    droit.update(bTrans);

                    vbDroit.setDto(new APDroitAPGDTO(droit));
                } else if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(vbDroit.getGenreService())) {
                    APDroitAPG droit = new APDroitAPG();

                    droit.setSession(session);
                    droit.setIdDroit(vbDroit.getIdDroit());
                    droit.retrieve(bTrans);

                    PRCloneFactory.getInstance().clone(
                            session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                            session, bTrans, droit,
                            session.getApplication().getProperty(APApplication.PROPERTY_CLONE_CORRIGER_DROIT_APG),
                            IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_FILS);

                    vbDroit.setDto(new APDroitAPGDTO(droit));
                    droit.setValiderPeriodes(false);
                    droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
                    droit.update(bTrans);
                } else {
                    APDroitMaternite droit = new APDroitMaternite();

                    droit.setSession(session);
                    droit.setIdDroit(vbDroit.getIdDroit());
                    droit.retrieve(session.getCurrentThreadTransaction());

                    PRCloneFactory.getInstance().clone(
                            session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                            session, bTrans, droit,
                            session.getApplication().getProperty(APApplication.PROPERTY_CLONE_COPIER_DROIT_MATERNITE),
                            IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_FILS);

                    // le droit parent passe dans l'etat definitif
                    droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
                    droit.update(bTrans);

                    vbDroit.setDto(new APDroitAPGDTO(droit));
                }

                if (bTrans.hasErrors()) {
                    throw new Exception(bTrans.getErrors().toString());
                }
            } catch (Exception e) {
                if (bTrans != null) {
                    bTrans.rollback();
                }
                throw e;
            } finally {
                try {
                    if (bTrans.isOpened()) {
                        bTrans.commit();
                    }
                    if (statement != null) {
                        try {
                            prestMan.cursorClose(statement);
                        } finally {
                            statement.closeStatement();
                        }
                    }
                } finally {
                    bTrans.closeTransaction();
                }
            }

        } else {
            // implossible de corriger un droit dans l'etat en attente
            vbDroit.setMsgType(FWViewBeanInterface.ERROR);
            vbDroit.setMessage(session.getLabel("ACTION_IMPOSSIBLE_POUR_DROIT_EN_ATTENTE"));
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * DOCUMENT ME!
     *
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void restituerDroit(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {
        APDroitLAPGJointDemandeViewBean vbDroit = vaChercherGenreServiceOuEtatSiAbsent(
                (APDroitLAPGJointDemandeViewBean) viewBean, session);

        if (vbDroit.getEtatDroit().equals(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF)
                || (APGUtils.isTypeAllocationPandemie(vbDroit.getGenreService()) && vbDroit.getEtatDroit()
                                                                                           .equals(IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL))) {
            IPRCloneable clone = null;

            BStatement statement = null;
            BTransaction bTrans = (BTransaction) session.newTransaction();
            APPrestationManager prestMan = null;
            // on met les prestations
            // valides ou ouvertes pour ce droit dans l'état ANNULE

            try {
                prestMan = new APPrestationManager();
                prestMan.setForIdDroit(vbDroit.getIdDroit());
                prestMan.setSession(session);

                bTrans.openTransaction();
                statement = prestMan.cursorOpen(bTrans);
                APPrestation prestation = null;

                while ((prestation = (APPrestation) prestMan.cursorReadNext(statement)) != null) {
                    // les prestations non-versees sont annulee
                    if (!IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestation.getEtat())) {
                        prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_ANNULE);
                        prestation.update(bTrans);
                    }
                }
                prestMan.cursorClose(statement);
            } catch (Exception e) {
                if (bTrans != null) {
                    bTrans.rollback();
                }
                throw e;
            } finally {
                try {
                    if (bTrans.isOpened()) {
                        bTrans.commit();
                    }
                    if (statement != null) {
                        try {
                            prestMan.cursorClose(statement);
                        } finally {
                            statement.closeStatement();
                        }
                    }
                } finally {
                    bTrans.closeTransaction();
                }
            }


            if (APGUtils.isTypeProcheAidant(vbDroit.getGenreService())) {
                APDroitProcheAidant droit = new APDroitProcheAidant();

                droit.setSession(session);
                droit.setIdDroit(vbDroit.getIdDroit());
                droit.retrieve(session.getCurrentThreadTransaction());

                clone = PRCloneFactory.getInstance().clone(
                        session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                        session, droit,
                        CLONE_COPIER_CORRIGER_DROIT_PROCHE_AIDANT,
                        IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_PANDEMIE_FILS);

                vbDroit.setDto(new APDroitAPGDTO(droit));
            } else if (APGUtils.isTypeAllocationPandemie(vbDroit.getGenreService())) {
                APDroitPandemie droit = new APDroitPandemie();

                droit.setSession(session);
                droit.setIdDroit(vbDroit.getIdDroit());
                droit.retrieve(session.getCurrentThreadTransaction());

                clone = PRCloneFactory.getInstance().clone(
                        session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                        session, droit,
                        CLONE_COPIER_CORRIGER_DROIT_PANDEMIE,
                        IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_PANDEMIE_FILS);

                vbDroit.setDto(new APDroitAPGDTO(droit));
            } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(vbDroit.getGenreService())) {
                APDroitPaternite droit = new APDroitPaternite();

                droit.setSession(session);
                droit.setIdDroit(vbDroit.getIdDroit());
                droit.retrieve(session.getCurrentThreadTransaction());
                clone = PRCloneFactory.getInstance().clone(
                        session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                        session, droit,
                        CLONE_COPIER_CORRIGER_DROIT_PATERNITE,
                        IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_PATERNITE_RESTI_FILS);
                vbDroit.setDto(new APDroitAPGDTO(droit));

            } else if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(vbDroit.getGenreService())) {
                APDroitAPG droit = new APDroitAPG();

                droit.setSession(session);
                droit.setIdDroit(vbDroit.getIdDroit());
                droit.retrieve(session.getCurrentThreadTransaction());

                clone = PRCloneFactory.getInstance().clone(
                        session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                        session, droit,
                        session.getApplication().getProperty(APApplication.PROPERTY_CLONE_CORRIGER_DROIT_APG),
                        IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_FILS);

                vbDroit.setDto(new APDroitAPGDTO(droit));

            } else {
                APDroitMaternite droit = new APDroitMaternite();

                droit.setSession(session);
                droit.setIdDroit(vbDroit.getIdDroit());
                droit.retrieve(session.getCurrentThreadTransaction());

                clone = PRCloneFactory.getInstance().clone(
                        session.getApplication().getProperty(APApplication.PROPERTY_CLONE_DEFINITION_FILENAME),
                        session, droit,
                        session.getApplication().getProperty(APApplication.PROPERTY_CLONE_COPIER_DROIT_MATERNITE),
                        IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_FILS);

                vbDroit.setDto(new APDroitAPGDTO(droit));
            }

            APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();

            calculateur.restituerPrestations(session, (APDroitLAPG) clone);
        } else {
            vbDroit.setMsgType(FWViewBeanInterface.ERROR);
            vbDroit.setMessage(session.getLabel("ACTION_IMPOSSIBLE_POUR_DROIT_NON_DEFINITIF"));
        }
    }

    /**
     * Supprime toutes les prestations du droit, et classes liées.
     *
     * @param session
     * @param transaction
     * @param droit
     * @throws Exception
     */
    // private void removePrestationsDroit(BSession session,
    // BTransaction transaction,
    // APDroitLAPG droit) throws Exception {
    // APPrestationManager mgr = new APPrestationManager();
    // mgr.setSession(session);
    // mgr.setForIdDroit(droit.getIdDroit());
    // mgr.find(BManager.SIZE_NOLIMIT);
    //
    // for (int i = 0; i < mgr.getSize(); i++) {
    // APPrestation prestation = (APPrestation) mgr.getEntity(i);
    // prestation.delete(transaction);
    // }
    // }

    /**
     * Action custom permettant de simuler le paiement d'un droit. Est utilisé pour récupéré des anciens cas de l'AS400
     * qui doivent être restitué. 1) Creation d'un lot a l'état "Définitif" contenant les prestations du droit 2) Mise à
     * jours des prestations du droit à l'état "Définitif" 3) Mise à jours de l'état du droit à "Définitif"
     *
     * @param viewBean
     * @param action
     * @param session
     * @throws Exception
     */
    public void simulerPaiementDroit(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        APDroitLAPGJointDemandeViewBean vbDroit = (APDroitLAPGJointDemandeViewBean) viewBean;

        /**
         * LGA : 03.04.2013 Version 1.11.05+ ACK caisse CIFA Lors de la reprise des données de la CIFA, CIGA et ???? les
         * données APG (Droit, Prestation et Annonces) n'ont pas pu être reprise. Afin de leur permettre d'effectuer des
         * corrections, ils devront : - Recréer le droit et calculer les prestations - Simuler le paiement sur ce droit
         * si l'annonce est antérieure au 09.2012 - Simuler le paiement AVEC PID sur ce droit si l'annonce est
         * postérieure au 09.2012 LE BPID doit être renseigné dans les annonces générée après 09.2012
         */
        String businessProcessId = vbDroit.getPidAnnonce();
        boolean insertPidAnnonce = !JadeStringUtil.isEmpty(businessProcessId);

        BTransaction transaction = (BTransaction) session.newTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
        String idDroit = vbDroit.getIdDroit();

        try {
            if (JadeStringUtil.isEmpty(idDroit)) {
                String message = session.getLabel("SIMULER_PAIEMENT_ID_DROIT_NON_TROUVE");
                message = message.replace("{0}", idDroit);
                vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                vbDroit.setMessage(message);
                throw new Exception(message);
            }

            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(session);
            droit.setIdDroit(idDroit);
            droit.retrieve(transaction);

            if (droit.isNew()) {
                String message = session.getLabel("SIMULER_PAIEMENT_DROIT_NON_TROUVE");
                message = message.replace("{0}", idDroit);
                vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                vbDroit.setMessage(message);
                throw new Exception(message);
            }

            if (APGUtils.isDroitModifiable(droit.getEtat())) {

                // creation du lot
                APLot lot = new APLot();
                lot.setSession(session);
                lot.setDateCreation(JACalendar.today().toStr("."));
                lot.setDescription(java.text.MessageFormat.format(
                        session.getLabel("LOT_REPRISE"),
                        new Object[]{JACalendar.today().toStr(".")}));
                lot.setEtat(IAPLot.CS_OUVERT);

                if (APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
                    lot.setTypeLot(IPRDemande.CS_TYPE_PANDEMIE);
                } else if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
                    lot.setTypeLot(IPRDemande.CS_TYPE_APG);
                } else {
                    lot.setTypeLot(IPRDemande.CS_TYPE_MATERNITE);
                }

                lot.add(transaction);

                // mise a jour des prestations
                APPrestationManager prestationManager = new APPrestationManager();
                prestationManager.setSession(session);
                prestationManager.setForIdDroit(droit.getIdDroit());
                prestationManager.find(transaction);

                for (Object o : prestationManager.getContainer()) {
                    APPrestation prestation = (APPrestation) o;
                    prestation.setIdLot(lot.getIdLot());
                    prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
                    prestation.update(transaction);
                }

                // mise a jour du droit
                droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
                droit.update(transaction);

                // mise a jour su lot
                lot.setEtat(IAPLot.CS_VALIDE);
                lot.update(transaction);

                if (transaction.hasErrors()) {
                    String message = session.getLabel("SIMULER_PAIEMENT_ERREUR_INTERNE_SURVENUE");
                    message += transaction.getErrors().toString();
                    vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                    vbDroit.setMessage(message);
                    throw new Exception(message);
                }

                // ACK REPRISE DE DONNEES
                // Si le BPID est renseigné, on vas s'assurer :
                // - qu'aucune annonce n'existe pour cette prestation
                // - que le BPID n'existe pas déjà dans une annonce en DB
                if (insertPidAnnonce) {

                    // On trim les espaces avant et après le BPID saisi en cas de copier-coller
                    businessProcessId = businessProcessId.trim();

                    APAnnonceAPG annonce = null;
                    // Contrôle qu'aucune annonce existe déjà avec le BPID
                    APAnnonceAPGManager annonceAPGManager = new APAnnonceAPGManager();
                    annonceAPGManager.setSession(session);
                    annonceAPGManager.setForBPID(businessProcessId);
                    annonceAPGManager.find(transaction, BManager.SIZE_NOLIMIT);
                    int nombreAnnonce = annonceAPGManager.getContainer().size();
                    if (nombreAnnonce != 0) {
                        String message = session.getLabel("SIMULER_PAIEMENT_ANNONCE_EXIST_DEJA_AVEC_BPID");
                        message = message.replace("[{0}]", businessProcessId);
                        vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                        vbDroit.setMessage(message);
                        throw new Exception(message);
                    }

                    // Création des annonces ET contrôle qu'aucune annonce exist pour la prestation
                    for (Object o : prestationManager.getContainer()) {
                        APPrestation prestation = (APPrestation) o;

                        if (!JadeStringUtil.isBlankOrZero(prestation.getIdAnnonce())) {
                            String message = session.getLabel("SIMULER_PAIEMENT_PRESTATION_POSSEDE_DEJA_ANNONCE");
                            message = message.replace("[{0}]", prestation.getIdPrestation());
                            vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                            vbDroit.setMessage(message);
                            throw new Exception(message);
                        }

                        annonce = new APAnnonceAPG();
                        annonce.setTypeAnnonce(IAPAnnonce.CS_APGSEDEX);
                        annonce.setBusinessProcessId(businessProcessId);
                        annonce.setEtat(IAPAnnonce.CS_ENVOYE);
                        annonce.add(transaction);

                        // Mis à jour de la prestation
                        prestation.setIdAnnonce(annonce.getIdAnnonce());
                        prestation.update(transaction);
                    }

                }

            } else {
                String message = session.getLabel("ACTION_IMPOSSIBLE_POUR_DROIT_NON_EN_ATTENTE");
                vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                vbDroit.setMessage(message);
                throw new Exception(message);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            // On ne throw pas les Exception plus loin car le viewBean est déjà en erreur et le message est déja
            // renseigné
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * Il se peut que le viewBean dans copierdroit corriger ou restituer n'ait pas de genre service, ce qui pose un
     * problème pour savoir quel type de copie faire. Cette méthode va rechercher le genre de service de ce viewBean si
     * il est absent.
     *
     * @param viewBean
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return le viewBean éventuellement mis à jour.
     * @throws Exception
     */
    private APDroitLAPGJointDemandeViewBean vaChercherGenreServiceOuEtatSiAbsent(
            APDroitLAPGJointDemandeViewBean viewBean, BSession session) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(viewBean.getGenreService())
                || JadeStringUtil.isIntegerEmpty(viewBean.getEtatDroit())) {
            APDroitLAPG droitLAPG = new APDroitLAPG();
            droitLAPG.setSession(session);
            droitLAPG.setIdDroit(viewBean.getIdDroit());
            droitLAPG.retrieve(session.getCurrentThreadTransaction());
            viewBean.setEtatDroit(droitLAPG.getEtat());
            viewBean.setGenreService(droitLAPG.getGenreService());
        }

        return viewBean;
    }

    /**
     * Passage du droit au statut "attente de réponse".
     *
     * @param vb
     * @param action
     * @param iSession
     * @throws Exception
     */
    public void actionAttenteReponse(FWViewBeanInterface vb, FWAction action, BSession iSession)
            throws Exception {

        if (!(vb instanceof APDroitLAPGJointDemandeViewBean)) {
            throw new APWrongViewBeanTypeException(
                    "Wrong viewBean type received for doing action finaliserCreationDroit. it must be from type APValidationPrestationViewBean");
        }
        BSession session = iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            APDroitLAPGJointDemandeViewBean vbDroit = vaChercherGenreServiceOuEtatSiAbsent(
                    (APDroitLAPGJointDemandeViewBean) vb, session);
            IPRCloneable clone = null;
            if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(vbDroit.getGenreService())) {
                // Si le traitement s'est bien passé, on retourne le bon viewBean pour aller sur l'affichage du droit
                APDroitPaternite droit = ApgServiceLocator.getEntityService().getDroitPaternite(session, transaction,
                                                                                                vbDroit.getIdDroit());
                // On met à jour l'état du droit
                droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE_REPONSE);
                droit.update();

                final List<APPrestation> prestations = ApgServiceLocator.getEntityService().getPrestationDuDroit(session,
                                                                                                                 transaction, droit.getIdDroit());
                for (APPrestation eachPrestation : prestations) {
                    eachPrestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_OUVERT);
                    eachPrestation.setIdLot("");
                    eachPrestation.update();
                }
                transaction.commit();

                vbDroit.setDto(new APDroitAPGDTO(droit));
            } else {
                // Si le traitement s'est bien passé, on retourne le bon viewBean pour aller sur l'affichage du droit
                APDroitPandemie droit = ApgServiceLocator.getEntityService().getDroitPandemie(session, transaction,
                                                                                              vbDroit.getIdDroit());
                // On met à jour l'état du droit
                droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE_REPONSE);
                droit.update();
                final List<APPrestation> prestations = ApgServiceLocator.getEntityService().getPrestationDuDroit(session,
                                                                                                                 transaction, droit.getIdDroit());
                for (APPrestation eachPrestation : prestations) {
                    eachPrestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_OUVERT);
                    eachPrestation.setIdLot("");
                    eachPrestation.update();
                }
                transaction.commit();

                vbDroit.setDto(new APDroitAPGDTO(droit));
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            // On ne throw pas les Exception plus loin car le viewBean est déjà en erreur et le message est déja
            // renseigné
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * Passage du droit au statut "refusé".
     *
     * @param vb
     * @param action
     * @param iSession
     * @throws Exception
     */
    public void actionRefuser(FWViewBeanInterface vb, FWAction action, BSession iSession)
            throws Exception {

        if (!(vb instanceof APDroitLAPGJointDemandeViewBean)) {
            throw new APWrongViewBeanTypeException(
                    "Wrong viewBean type received for doing action finaliserCreationDroit. it must be from type APValidationPrestationViewBean");
        }
        BSession session = iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            APDroitLAPGJointDemandeViewBean vbDroit = vaChercherGenreServiceOuEtatSiAbsent(
                    (APDroitLAPGJointDemandeViewBean) vb, session);
            if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(vbDroit.getGenreService())) {
                // Si le traitement s'est bien passé, on retourne le bon viewBean pour aller sur l'affichage du droit
                APDroitPaternite droit = ApgServiceLocator.getEntityService().getDroitPaternite(session, transaction,
                                                                                                vbDroit.getIdDroit());
                // On met à jour l'état du droit
                droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
                droit.update();

                // Suppression des prestations non définitives
                final List<APPrestation> prestations = ApgServiceLocator.getEntityService().getPrestationDuDroit((BSession) session,
                                                                                                                 transaction, droit.getIdDroit());
                for (APPrestation eachPrestation : prestations) {
                    if (!StringUtils.equals(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF, eachPrestation.getEtat())) {
                        if (StringUtils.isNotEmpty(eachPrestation.getIdLot()) && eachPrestation.getIdLot() != "0") {
                            APLot lot = new APLot();
                            lot.setSession(session);
                            lot.setIdLot(eachPrestation.getIdLot());
                            lot.retrieve(transaction);

                            if (IAPLot.CS_COMPENSE.equals(lot.getEtat())) {
                                lot.setEtat(IAPLot.CS_OUVERT);
                                lot.update(transaction);
                            }
                        }
                        eachPrestation.delete(transaction);
                    }
                }
                transaction.commit();
                vbDroit.setDto(new APDroitAPGDTO(droit));
            } else {
                // Si le traitement s'est bien passé, on retourne le bon viewBean pour aller sur l'affichage du droit
                APDroitPandemie droit = ApgServiceLocator.getEntityService().getDroitPandemie(session, transaction,
                                                                                              vbDroit.getIdDroit());
                // On met à jour l'état du droit
                droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
                droit.update();

                // Suppression des prestations non définitives
                final List<APPrestation> prestations = ApgServiceLocator.getEntityService().getPrestationDuDroit((BSession) session,
                                                                                                                 transaction, droit.getIdDroit());
                for (APPrestation eachPrestation : prestations) {
                    if (!StringUtils.equals(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF, eachPrestation.getEtat())) {
                        if (StringUtils.isNotEmpty(eachPrestation.getIdLot()) && eachPrestation.getIdLot() != "0") {
                            APLot lot = new APLot();
                            lot.setSession(session);
                            lot.setIdLot(eachPrestation.getIdLot());
                            lot.retrieve(transaction);

                            if (IAPLot.CS_COMPENSE.equals(lot.getEtat())) {
                                lot.setEtat(IAPLot.CS_OUVERT);
                                lot.update(transaction);
                            }
                        }
                        eachPrestation.delete(transaction);
                    }
                }
                transaction.commit();
                vbDroit.setDto(new APDroitAPGDTO(droit));
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            // On ne throw pas les Exception plus loin car le viewBean est déjà en erreur et le message est déja
            // renseigné
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }


}
