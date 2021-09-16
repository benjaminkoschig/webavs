/*
 * Créé le 13 sept. 05
 */
package globaz.ij.helpers.prononces;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.prononces.*;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntRole;
import globaz.pavo.api.ICICompteIndividuel;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.ci.PRCompteIndividuelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRequerantHelper extends PRAbstractHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ERREUR_NOM_OU_PRENOM_INVALIDE = "NOM_OU_PRENOM_INVALIDE";
    private static final String ERREUR_NUMERO_AVS_REQUIS = "NUMERO_AVS_REQUIS";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJRequerantViewBean droitVB = (IJRequerantViewBean) viewBean;

        if (droitVB.isCallValidate()) {
            preparerDroitAvantAjout(droitVB, (BSession) session);
        }

        if (!FWViewBeanInterface.ERROR.equals(droitVB.getMsgType())) {
            IJAbstractPrononceProxyViewBean prononceViewBean = null;

            if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_GRANDE_IJ)) {
                prononceViewBean = new IJGrandeIJPViewBean();
            } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_PETITE_IJ)) {
                prononceViewBean = new IJPetiteIJPViewBean();
            } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_INIT_TRAVAIL)) {
                prononceViewBean = new IJPrononceAitViewBean();
            } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_ASSIST)) {
                prononceViewBean = new IJPrononceAllocAssistanceViewBean();
            } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_FPI)) {
                prononceViewBean = new IJFpiViewBean();
            } else {
                throw new Exception("type ij introuvable");
            }

            prononceViewBean.setISession(session);

            prononceViewBean.setIdDemande(droitVB.getIdDemande());
            prononceViewBean.setIdGestionnaire(droitVB.getIdGestionnaire());
            prononceViewBean.getPrononce().setSoumisImpotSource(droitVB.getSoumisImpotSource());
            prononceViewBean.getPrononce().setAvecDecision(droitVB.getAvecDecision());

            if (droitVB.getSoumisImpotSource() != null && droitVB.getSoumisImpotSource().booleanValue()) {
                prononceViewBean.getPrononce().setCsCantonImpositionSource(droitVB.getCsCantonImpositionSource());
                prononceViewBean.getPrononce().setTauxImpositionSource(droitVB.getTauxImpositionSource());
            } else {
                prononceViewBean.getPrononce().setCsCantonImpositionSource("");
                prononceViewBean.getPrononce().setTauxImpositionSource("0");
            }

            prononceViewBean.add(((BSession) session).getCurrentThreadTransaction());

            droitVB.setIdPrononce(prononceViewBean.getIdPrononce());
        }
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJRequerantViewBean droitVB = (IJRequerantViewBean) viewBean;

        if (droitVB.isModifie()) {
            BITransaction transaction = null;

            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                if (droitVB.isCallValidate()) {
                    preparerDroitAvantAjout(droitVB, (BSession) session);
                }

                if (!FWViewBeanInterface.ERROR.equals(droitVB.getMsgType())) {
                    IJAbstractPrononceProxyViewBean prononceViewBean = null;

                    if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_GRANDE_IJ)) {
                        prononceViewBean = new IJGrandeIJPViewBean();
                    } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_PETITE_IJ)) {
                        prononceViewBean = new IJPetiteIJPViewBean();
                    } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_INIT_TRAVAIL)) {
                        prononceViewBean = new IJPrononceAitViewBean();
                    } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_ASSIST)) {
                        prononceViewBean = new IJPrononceAllocAssistanceViewBean();
                    } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_FPI)) {
                        prononceViewBean = new IJFpiViewBean();
                    } else {
                        throw new Exception("type ij introuvable");
                    }

                    prononceViewBean.setISession(session);
                    prononceViewBean.setIdPrononce(droitVB.getIdPrononce());

                    prononceViewBean.retrieve();
                    IJPrononceRegles.reinitialiser((BSession) session, transaction, prononceViewBean.getPrononce());

                    prononceViewBean.setISession(session);

                    prononceViewBean.setIdDemande(droitVB.getIdDemande());
                    prononceViewBean.setIdGestionnaire(droitVB.getIdGestionnaire());
                    prononceViewBean.getPrononce().setSoumisImpotSource(droitVB.getSoumisImpotSource());
                    prononceViewBean.getPrononce().setAvecDecision(droitVB.getAvecDecision());

                    if (droitVB.getSoumisImpotSource() != null && droitVB.getSoumisImpotSource().booleanValue()) {
                        prononceViewBean.getPrononce().setCsCantonImpositionSource(
                                droitVB.getCsCantonImpositionSource());
                        prononceViewBean.getPrononce().setTauxImpositionSource(droitVB.getTauxImpositionSource());
                    } else {
                        prononceViewBean.getPrononce().setCsCantonImpositionSource("");
                        prononceViewBean.getPrononce().setTauxImpositionSource("0");
                    }
                    prononceViewBean.update(transaction);
                    if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_FPI)) {
                        prononceViewBean = new IJFpiViewBean();
                        ((IJFpiViewBean)prononceViewBean).setDateNaissance(((IJRequerantViewBean)viewBean).getDateNaissance());
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
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
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
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionFindIdTiersByNoAvs(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJRequerantViewBean reqViewBean = (IJRequerantViewBean) viewBean;
        String idTiers = PRDemande.ID_TIERS_DEMANDE_BIDON;

        // recherche dans les tiers
        PRTiersWrapper tiers = PRTiersHelper.getTiers(session, reqViewBean.getNss());

        if (tiers != null) {
            // le no AVS existe dans les tiers, écrase les champs
            idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            reqViewBean.setNom(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
            reqViewBean.setPrenom(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            reqViewBean.setTrouveDansTiers(true);
        } else {
            // le no AVS n'existe pas dans les tiers, on cherche dans les CI
            ICICompteIndividuel ci = PRCompteIndividuelHelper.getCompteIndividuel(session, reqViewBean.getNss());

            if (!ci.isNew()) {
                // existe dans les CI, on écrase les nom et prénom et la date de
                // naissance
                reqViewBean.extraireNomPrenomCI(ci.getNomPrenom());
            }
        }

        setDemande(reqViewBean, idTiers, session, false);

        return viewBean;
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
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface arreterEtape1(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJRequerantViewBean droitVB = (IJRequerantViewBean) viewBean;

        IJAbstractPrononceProxyViewBean prononceViewBean = null;

        if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_GRANDE_IJ)) {
            prononceViewBean = new IJGrandeIJPViewBean();
        } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_PETITE_IJ)) {
            prononceViewBean = new IJPetiteIJPViewBean();
        } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_INIT_TRAVAIL)) {
            prononceViewBean = new IJPrononceAitViewBean();
        } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_ASSIST)) {
            prononceViewBean = new IJPrononceAllocAssistanceViewBean();
        } else if (droitVB.getCsTypeIJ().equals(IIJPrononce.CS_FPI)) {
            prononceViewBean = new IJFpiViewBean();
        } else {
            throw new Exception("type ij introuvable");
        }

        prononceViewBean.setISession(session);

        if (!"null".equals(droitVB.getIdPrononce()) && !JadeStringUtil.isIntegerEmpty(droitVB.getIdPrononce())) {
            prononceViewBean.setIdPrononce(droitVB.getIdPrononce());
            prononceViewBean.retrieve(session.getCurrentThreadTransaction());
        }

        preparerDroitAvantAjout(droitVB, session);
        prononceViewBean.getPrononce().wantCallValidate(false);

        prononceViewBean.setIdDemande(droitVB.getIdDemande());
        prononceViewBean.setIdGestionnaire(droitVB.getIdGestionnaire());
        prononceViewBean.getPrononce().setSoumisImpotSource(droitVB.getSoumisImpotSource());

        if (droitVB.getSoumisImpotSource() != null && droitVB.getSoumisImpotSource().booleanValue()) {
            prononceViewBean.getPrononce().setCsCantonImpositionSource(droitVB.getCsCantonImpositionSource());
            prononceViewBean.getPrononce().setTauxImpositionSource(droitVB.getTauxImpositionSource());
        } else {
            prononceViewBean.getPrononce().setCsCantonImpositionSource("");
            prononceViewBean.getPrononce().setTauxImpositionSource("0");
        }

        if (prononceViewBean.getPrononce().isNew()) {
            prononceViewBean.add(session.getCurrentThreadTransaction());
        } else {
            prononceViewBean.update(session.getCurrentThreadTransaction());
        }

        return droitVB;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private PRTiersWrapper insererNouveauTiers(IJRequerantViewBean droitPViewBean, BSession session) throws Exception {
        // bien qu'il soit parfaitement inutile de checker le no AVS ici, on le
        // fait pour des raisons de performances
        try {

            if (NSUtil.unFormatAVS(droitPViewBean.getNss()).length() > 11) {
                if (!NSUtil.nssCheckDigit(droitPViewBean.getNss())) {
                    throw new JAException(session.getLabel(ERREUR_NUMERO_AVS_REQUIS));
                }
            } else {
                JAUtil.checkAvs(droitPViewBean.getNss());
            }

        } catch (JAException e) {
            // le no AVS est invalide, ne pas insérer dans les tiers
            session.addError(session.getLabel(ERREUR_NUMERO_AVS_REQUIS));

            return null;
        }

        // les noms et prenoms doivent être renseignés pour insérer un nouveau
        // tiers
        if (JadeStringUtil.isEmpty(droitPViewBean.getNom()) || JadeStringUtil.isEmpty(droitPViewBean.getPrenom())) {
            session.addError(session.getLabel(ERREUR_NOM_OU_PRENOM_INVALIDE));

            return null;
        }

        // // date naissance obligatoire pour inserer
        // if (JAUtil.isDateEmpty(droitPViewBean.getDateNaissance())) {
        // session.addError(session.getLabel("DATE_NAISSANCE_INCORRECTE"));
        //
        // return null;
        // }

        // // Etat Civil obligatoire pour inserer
        // if (JAUtil.isIntegerEmpty(droitPViewBean.getEtatCivil())) {
        // session.addError(session.getLabel("ETAT_CIVIL_INCORRECT"));
        //
        // return null;
        // }

        // recherche du canton si le npa est renseigné
        // String canton = "";
        //
        // if (!JAUtil.isIntegerEmpty(droitPViewBean.getNpa())) {
        // try {
        // canton = PRTiersHelper.getCanton(session, droitPViewBean.getNpa());
        //
        // if (canton == null) {
        // // canton non trouvé
        // canton = "";
        // }
        // } catch (Exception e1) {
        // droitPViewBean._addError(session.getLabel("CANTON_INTROUVABLE"));
        // }
        // }

        // insertion du tiers
        // si son numero AVS est suisse on lui met suisse comme pays, sinon on
        // lui met un pays bidon qu'on pourrait
        // interpreter comme "etranger"

        // PRAVSUtils avsUtils =
        // PRAVSUtils.getInstance(droitPViewBean.getNss());

        String idTiers = PRTiersHelper.addTiers(session, droitPViewBean.getNss(), droitPViewBean.getNom(),
                droitPViewBean.getPrenom(), droitPViewBean.getCsSexe(), droitPViewBean.getDateNaissance(),
                droitPViewBean.getDateDeces(), droitPViewBean.getCsNationalite(), droitPViewBean.getCsCantonDomicile(),
                "", "");

        // on remonte les erreurs dans le viewBean si jamais.
        if (session.hasErrors()) {
            droitPViewBean.setMsgType(FWViewBeanInterface.ERROR);
            droitPViewBean.setMessage(session.getErrors().toString());
        }

        return PRTiersHelper.getTiersParId(session, idTiers);
    }

    private void preparerDroitAvantAjout(IJRequerantViewBean droitPViewBean, BSession session) throws Exception {
        // préparation du tiers
        // ----------------------------------------------------------------------------------------
        String idTiers = PRDemande.ID_TIERS_DEMANDE_BIDON;

        if (droitPViewBean.isTrouveDansCI()) {
            // le tiers a précédemment été trouvé dans les CI, il faut maintant
            // l'insérer dans les tiers
            PRTiersWrapper tiers = insererNouveauTiers(droitPViewBean, session);

            // si l'insertion dans les tiers a réussi, mettre à jour l'id du
            // tiers courant
            if (tiers != null) {
                idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
            } else {
                throw new Exception(session.getLabel("ADD_TIERS_ERR"));
            }
        }

        // Avec la recherche du nss, on ne devrait plus arriver dans ce bloc.
        // ------------------------------------------------------------------

        // else if (droitPViewBean.isNoAVSmodifieDepuisDerniereRecherche() ||
        // JAUtil.isIntegerEmpty(droitPViewBean.getIdDemande()) ||
        // JAUtil.isIntegerEmpty(droitPViewBean.loadDemande().getIdTiers())) {
        // // le tiers a été modifié ou il faut désormais sauver un droit valide
        // droitPViewBean.resetFlags();
        //
        // // on cherche dans les tiers
        // PRTiersWrapper tiers = PRTiersHelper.getTiers(session,
        // droitPViewBean.getNss());
        //
        // if (tiers != null) {
        // // le no AVS existe dans les tiers
        // idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        // droitPViewBean.setTrouveDansTiers(true);
        // } else {
        // // le no AVS n'existe pas dans les tiers, on cherche dans les CI
        // ICICompteIndividuel ci =
        // PRCompteIndividuelHelper.getCompteIndividuel(session,
        // droitPViewBean.getNss());
        //
        // if (ci.isNew()) {
        // // n'existe pas dans les CI, on tente d'inserer un nouveau tiers
        // tiers = insererNouveauTiers(droitPViewBean, session);
        //
        // if (tiers != null) {
        // idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        // }
        // } else {
        // // existe dans les CI, on écrase les nom et prénom et on demande une
        // confirmation à l'utilisateur
        // String nomComplet = ci.getNomPrenom();
        //
        // if (JAUtil.isStringEmpty(nomComplet)) {
        // /*
        // * dans certains cas, PAVO retourne un value object ou le nom n'est
        // pas renseigné malgré le fait
        // * qu'une inscription existe (peut-etre que notre base de
        // développement était incomplète).
        // * dans ce cas on tente d'insérer dans les tiers.
        // */
        // tiers = insererNouveauTiers(droitPViewBean, session);
        //
        // if (tiers != null) {
        // idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        // }
        // } else {
        // if
        // (droitPViewBean.extraireNomPrenomCI(PRStringUtils.toWordsFirstLetterUppercase(nomComplet)))
        // {
        // // on avertit l'utilisateur qu'il doit controler ce qu'on a trouve
        // dans les CI
        // droitPViewBean.setMsgType(FWViewBeanInterface.ERROR);
        // droitPViewBean.setMessage(session.getLabel("AVS_TROUVE_DANS_CI"));
        // }
        // }
        // }
        // }
        //
        // // préparation de l'adresse du tiers
        // -----------------------------------------------------------------------
        // if (tiers != null) {
        // // on écrase les nom et prénom, etat civil et date de naissance
        // droitPViewBean.setNom(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
        // droitPViewBean.setPrenom(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        //
        // // if (!TIPays.CS_SUISSE.equals(droitPViewBean.getPays())) {
        // // } else if (JAUtil.isStringEmpty(droitPViewBean.getNpa())) {
        // // // on n'écrase pas le numéro postal
        // // rechercheAdresseCourrierOuDomicile(droitPViewBean, idTiers,
        // session);
        // // }
        // }
        // }
        else if (droitPViewBean.isTrouveDansTiers()) {
            idTiers = droitPViewBean.getIdAssure();

            TIRoleManager roleManager = new TIRoleManager();
            roleManager.setSession(session);
            roleManager.setForIdTiers(idTiers);
            BStatement statement = null;
            BTransaction trans = (BTransaction) session.newTransaction();
            try {
                trans.openTransaction();
                statement = roleManager.cursorOpen(session.getCurrentThreadTransaction());
                TIRole role = null;
                boolean isRolePresent = false;

                while ((role = (TIRole) roleManager.cursorReadNext(statement)) != null) {
                    if (IntRole.ROLE_IJAI.equals(role.getRole())) {
                        isRolePresent = true;
                    }
                }

                if (!isRolePresent) {
                    // on ajoute le rôle APG au Tier si il ne l'a pas deja
                    ITIRole newRole = (ITIRole) session.getAPIFor(ITIRole.class);
                    newRole.setIdTiers(idTiers);
                    newRole.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));
                    newRole.setRole(IntRole.ROLE_IJAI);
                    newRole.add(session.getCurrentThreadTransaction());
                }
                trans.commit();
            } catch (Exception e) {
                if (trans != null) {
                    trans.rollback();
                }
                throw e;
            } finally {
                try {
                    if (statement != null) {
                        try {
                            roleManager.cursorClose(statement);
                        } finally {
                            statement.closeStatement();
                        }
                    }
                } finally {
                    if (trans != null) {
                        trans.closeTransaction();
                    }
                }
            }

        }

        else {
            // sinon on se base sur l'ancien id du tiers contenu dans la
            // demande.
            idTiers = droitPViewBean.loadDemande(null).getIdTiers();
            // Dernier contrôle, en cas d'erreur d'insertion du tiers, ou autre
            // erreur due à une mauvaise saisie,
            // idTiers de la demande peut être vide. On va donc essayer de le
            // réinséré dans les tiers.
            if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                PRTiersWrapper tiers = insererNouveauTiers(droitPViewBean, session);
                if (tiers != null) {
                    idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                } else {
                    throw new Exception(session.getLabel("ADD_TIERS_ERR"));
                }
            }

        }

        // on set une demande de toutes facons pour ce droit, qu'il soit déjà
        // sauvé ou non.
        setDemande(droitPViewBean, idTiers, session, true);
    }

    /**
     * setter pour l'attribut demande.
     * 
     * @param prononce
     *            une nouvelle valeur pour cet attribut
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     * @param session
     *            une nouvelle valeur pour cet attribut
     * @param creeSiNecessaire
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected PRDemande setDemande(IJPrononce prononce, String idTiers, BSession session, boolean creeSiNecessaire)
            throws Exception {
        PRDemande retValue = null;
        String typeDemande = IPRDemande.CS_TYPE_IJ;

        if (PRDemande.ID_TIERS_DEMANDE_BIDON.equals(idTiers)
                && (PRDemande.getDemandeBidon(session, typeDemande) != null)) {
            retValue = PRDemande.getDemandeBidon(session, typeDemande);
        } else {
            PRDemandeManager mgr = new PRDemandeManager();

            mgr.setSession(session);
            mgr.setForIdTiers(JadeStringUtil.isIntegerEmpty(idTiers) ? PRDemande.ID_TIERS_DEMANDE_BIDON : idTiers);
            mgr.setForTypeDemande(typeDemande);
            mgr.find();

            if (mgr.isEmpty() && creeSiNecessaire) {
                retValue = new PRDemande();
                retValue.setIdTiers(idTiers);
                retValue.setEtat(IPRDemande.CS_ETAT_OUVERT);
                retValue.setTypeDemande(typeDemande);
                retValue.setSession(session);
                retValue.add();
            } else if (!mgr.isEmpty()) {
                retValue = (PRDemande) mgr.get(0);

                // if (PRDemande.ID_TIERS_DEMANDE_BIDON.equals(idTiers)) {
                // demandeBidon = retValue;
                // }
            }
        }

        if (retValue != null) {
            prononce.setDemande(retValue);
        }

        return retValue;
    }
}
