package globaz.ij.regles;

import ch.globaz.common.util.Dates;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prestations.IJGrandeIJCalculeeManager;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJPetiteIJCalculeeManager;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.db.prononces.IJFpi;
import globaz.ij.db.prononces.IJGrandeIJ;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.utils.IJUtils;
import globaz.ij.vb.prononces.IJAbstractPrononceProxyViewBean;
import globaz.ij.vb.prononces.IJFpiViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.PRCloneFactory;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import java.time.LocalDate;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui propose des méthodes pour gérer le cycle de vie des prononcé.
 * </p>
 * 
 * <p>
 * Les prononcés peuvent être dans quatre états (famille IJETATPRO):
 * </p>
 * 
 * <dl>
 * <dt>CS_ATTENTE</dt>
 * <dd>Etat initial d'un prononcé</dd>
 * 
 * <dt>CS_VALIDE</dt>
 * <dd>Etat lorsque l'une au moins de ses bases d'indemnisations passe dans l'état valide</dd>
 * 
 * <dt>CS_COMMUNIQUE</dt>
 * <dd>Etat lorsque l'une au moins de ses bases d'indemnisations passe dans l'état communiqué</dd>
 * 
 * <dt>CS_ANNULE</dt>
 * <dd>Etat lorsque le prononcé a été corrigé et que les ij de sa correction ont été calculées</dd>
 * </dl>
 * 
 * <p>
 * Le cycle de vie général d'un prononcé est le suivant:
 * </p>
 * 
 * <p>
 * <img src="doc-files/prononce.gif" alt="cycle de vie général d'un prononcé"
 * title="cycle de vie général d'un prononcé">
 * </p>
 * 
 * <p>
 * Lors du passage d'un état à l'autre, les méthodes de cette classe peuvent être appelées pour déclencher les actions
 * conséquentes à ce changement. Ces actions sont les suivantes:
 * </p>
 * 
 * <dl>
 * <dt>Calculer les IJ d'un prononcé PRO (permis si PRO.csEtat == CS_ATTENTE)</dt>
 * <dd>méthode {@link #restitutionEtCorrection(BSession, BITransaction, IJPrononce, IJPrononce, List)
 * restitutionEtCorrection(PRO)}</dd>
 * 
 * <dt>Modifier un prononcé PRO (permis si PRO.csEtat == VALIDE OU pro.csEtat == CS_OUVERT)</dt>
 * <dd>méthode {@link #reinitialiser(BSession, BITransaction, IJPrononce) reinitialiser(PRO)}</dd>
 * 
 * <dt>Corriger un prononcé PRO (permis si PRO.csEtat == COMMUNIQUE OU (PRO.csEtat == CS_ANNULE ET PRO.loadEnfantActif
 * == null))</dt>
 * <dd>Méthode {@link #creerCorrection(BSession, BITransaction, IJPrononce) creerCorrection(PRO)}</dd>
 * 
 * <dt>Supprimer un prononcé PRO (permis si PRO.csEtat == CS_ATTENTE OU PRO.csEtat == CS_VALIDE)</dt>
 * <dd>Pour toutes les prestations PRE de toutes les bases du prononcé,
 * {@link globaz.ij.regles.IJPrestationRegles#annulerMiseEnLot(BSession, BITransaction, IJPrestation) annuler la mise en
 * lot}</dd>
 * </dl>
 * 
 * @author vre
 */
public final class IJPrononceRegles {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static String CLONE_DEFINITION;
    private static String CLONE_GRANDEIJ_COPIE;
    private static String CLONE_GRANDEIJ_COR;
    private static String CLONE_IJ_AA_COPIE;
    private static String CLONE_IJ_AA_COR;
    private static String CLONE_IJ_AIT_COPIE;
    private static String CLONE_IJ_AIT_COR;
    private static String CLONE_PETITEIJ_COPIE;
    private static String CLONE_PETITEIJ_COR;

    private static int AGE_CHANGEMENT_FPI = 25;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Copie un prononcé égal au prononcé donné. Le prononcé copié se trouve dans l'état en attente. Si le prononcé que
     * l'on souhaite copié est une correction, le prononcé copié sera dans tout les cas un prononcé parent.
     * 
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */

    public static final IJPrononce creerCopie(BSession session, BITransaction transaction, IJPrononce prononce)
            throws Exception {

        // Vérification du no de décision du parent
        if (IJPrononceRegles.validateNoDecison(prononce, session, transaction)) {

            PRCloneFactory cf = PRCloneFactory.getInstance();
            IJPrononce copie = (IJPrononce) cf.clone(IJPrononceRegles.getCloneDefinition(session), session,
                    transaction, prononce, IJPrononceRegles.getCloneCopieBaseId(session, prononce),
                    IIJPrononce.CLONE_NORMAL);

            if (copie.isNew()) {
                copie.add(transaction);
            }

            return copie;

        } else {
            return null;
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée un prononcé de correction pour le prononcé donné.
     * 
     * <ol>
     * <li>Si le prononcé est annulé, les actions suivantes sont effectuées sur son enfant actif.</li>
     * <li>Si le prononcé a déjà une correction on ne fait rien</li>
     * <li>Créer un clone du prononcé et le rajouter comme enfant.</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IJPrononce creerCorrection(BSession session, BITransaction transaction, IJPrononce prononce)
            throws Exception {

        // Vérification du no de décision du parent
        if (IJPrononceRegles.validateNoDecison(prononce, session, transaction)) {

            // charger la correction du prononce d'origine si celui-ci a déjà
            // été corrigée une fois
            if (IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {
                prononce = IJPrononceRegles.loadEnfantActif(prononce);
            }

            // si ce prononce a déjà une correction, on ne fait rien
            if (prononce.loadEnfantCorrection(transaction) == null) {
                PRCloneFactory cf = PRCloneFactory.getInstance();
                IJPrononce correction = (IJPrononce) cf.clone(IJPrononceRegles.getCloneDefinition(session), session,
                        transaction, prononce, IJPrononceRegles.getCloneCorBaseId(session, prononce),
                        IIJPrononce.CLONE_FILS);

                if (correction.isNew()) {
                    correction.add(transaction);
                }

                return correction;
            } else {
                return prononce.loadEnfantCorrection(transaction);
            }

        } else {
            return null;
        }
    }

    private static final String getCloneCopieBaseId(BSession session, IJPrononce prononce) throws Exception {
        if (IIJPrononce.CS_GRANDE_IJ.equals(prononce.getCsTypeIJ())) {
            if (IJPrononceRegles.CLONE_GRANDEIJ_COPIE == null) {
                IJPrononceRegles.CLONE_GRANDEIJ_COPIE = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_GRANDEIJ_COPIE);
            }

            return IJPrononceRegles.CLONE_GRANDEIJ_COPIE;
        } else if (IIJPrononce.CS_PETITE_IJ.equals(prononce.getCsTypeIJ())) {
            if (IJPrononceRegles.CLONE_PETITEIJ_COPIE == null) {
                IJPrononceRegles.CLONE_PETITEIJ_COPIE = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_PETITEIJ_COPIE);
            }

            return IJPrononceRegles.CLONE_PETITEIJ_COPIE;
        } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(prononce.getCsTypeIJ())) {
            if (IJPrononceRegles.CLONE_IJ_AIT_COPIE == null) {
                IJPrononceRegles.CLONE_IJ_AIT_COPIE = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_IJ_AIT_COPIE);
            }

            return IJPrononceRegles.CLONE_IJ_AIT_COPIE;
        } else {
            if (IJPrononceRegles.CLONE_IJ_AA_COPIE == null) {
                IJPrononceRegles.CLONE_IJ_AA_COPIE = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_IJ_AA_COPIE);
            }

            return IJPrononceRegles.CLONE_IJ_AA_COPIE;
        }
    }

    private static final String getCloneCorBaseId(BSession session, IJPrononce prononce) throws Exception {
        if (IIJPrononce.CS_GRANDE_IJ.equals(prononce.getCsTypeIJ())) {
            if (IJPrononceRegles.CLONE_GRANDEIJ_COR == null) {
                IJPrononceRegles.CLONE_GRANDEIJ_COR = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_GRANDEIJ_COR);
            }

            return IJPrononceRegles.CLONE_GRANDEIJ_COR;
        } else if (IIJPrononce.CS_PETITE_IJ.equals(prononce.getCsTypeIJ())) {
            if (IJPrononceRegles.CLONE_PETITEIJ_COR == null) {
                IJPrononceRegles.CLONE_PETITEIJ_COR = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_PETITEIJ_COR);
            }

            return IJPrononceRegles.CLONE_PETITEIJ_COR;
        } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(prononce.getCsTypeIJ())) {
            if (IJPrononceRegles.CLONE_IJ_AIT_COR == null) {
                IJPrononceRegles.CLONE_IJ_AIT_COR = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_IJ_AIT_COR);
            }

            return IJPrononceRegles.CLONE_IJ_AIT_COR;
        } else {
            if (IJPrononceRegles.CLONE_IJ_AA_COR == null) {
                IJPrononceRegles.CLONE_IJ_AA_COR = session.getApplication().getProperty(
                        IJApplication.PROPERTY_CLONE_IJ_AA_COR);
            }

            return IJPrononceRegles.CLONE_IJ_AA_COR;
        }
    }

    private static final String getCloneDefinition(BSession session) throws Exception {
        if (IJPrononceRegles.CLONE_DEFINITION == null) {
            IJPrononceRegles.CLONE_DEFINITION = session.getApplication().getProperty(
                    IJApplication.PROPERTY_CLONE_DEFINITION);
        }

        return IJPrononceRegles.CLONE_DEFINITION;
    }

    /**
     * retourne vrai s'il est permis de calculer les ij pour le prononce donné.
     * 
     * <p>
     * Cela est permis uniquement si le prononce est en mode attente.
     * </p>
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut calculer IJPermis
     */
    public static final boolean isCalculerPermis(IJPrononce prononce) {
        return IIJPrononce.CS_ATTENTE.equals(prononce.getCsEtat());
    }

    /**
     * retourne vrai s'il est permis de calculer les ij pour le prononce donné ou pour son (seul possible) enfant actif.
     * 
     * <p>
     * Cela est permis uniquement si le prononce est en mode attente.
     * </p>
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut calculer IJPermis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCalculerPermisExt(IJPrononce prononce) throws Exception {
        if (IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {
            prononce = IJPrononceRegles.loadEnfantActif(prononce);
        }

        return (prononce != null) && IJPrononceRegles.isCalculerPermis(prononce);
    }

    /**
     * retourne vrai si le prononce peut etre corrigé.
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut corriger permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCorrigerPermis(IJPrononce prononce) throws Exception {
        if (IIJPrononce.CS_COMMUNIQUE.equals(prononce.getCsEtat())
                || IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {
            return prononce.loadEnfantActif(null) == null;
        }

        return false;
    }

    /**
     * retourne vrai si le prononce (ou son seul enfant actif possible) peut etre corrigé.
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut corriger permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCorrigerPermisExt(IJPrononce prononce) throws Exception {
        if (IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {
            prononce = IJPrononceRegles.loadEnfantActif(prononce);
        }

        return (prononce != null) && IJPrononceRegles.isCorrigerPermis(prononce);
    }

    /**
     * retourne vrai s'il est permis de modifier un prononce.
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public static final boolean isModifierPermis(IJPrononce prononce) {

        // S'il s'agit d'un prononce enfant, il est uniquement permis de le
        // modifier tant que
        // son etat est égal à ATTENTE. Autrement, risque de générer des
        // incohérance au niveau des
        // bases d'indemnisations et des prestations.
        if (!JadeStringUtil.isIntegerEmpty(prononce.getIdParent())
                && !IIJPrononce.CS_ATTENTE.equals(prononce.getCsEtat())) {
            return false;
        }

        return IIJPrononce.CS_ATTENTE.equals(prononce.getCsEtat())
                || IIJPrononce.CS_VALIDE.equals(prononce.getCsEtat())
                || IIJPrononce.CS_DECIDE.equals(prononce.getCsEtat());
    }

    /**
     * retourne vrai s'il est permis de modifier un prononce ou son (seul possible) enfant actif.
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut modifier permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isModifierPermisExt(IJPrononce prononce) throws Exception {
        if (IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {
            prononce = IJPrononceRegles.loadEnfantActif(prononce);
        }

        return (prononce != null) && IJPrononceRegles.isModifierPermis(prononce);
    }

    /**
     * retourne vrai si le prononce peut etre supprime.
     * 
     * <p>
     * Cela est permis uniquement si le prononce est dans les états attente ou validé.
     * </p>
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut supprimer permis
     */
    public static final boolean isSupprimerPermis(IJPrononce prononce) {
        return IIJPrononce.CS_ATTENTE.equals(prononce.getCsEtat())
                || IIJPrononce.CS_VALIDE.equals(prononce.getCsEtat())
                || IIJPrononce.CS_DECIDE.equals(prononce.getCsEtat());
    }

    /**
     * retourne vrai si le prononce (ou son seul enfant actif possible) peut etre supprime.
     * 
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut supprimer permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isSupprimerPermisExt(IJPrononce prononce) throws Exception {
        if (IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {
            prononce = IJPrononceRegles.loadEnfantActif(prononce);
        }

        return (prononce != null) && IJPrononceRegles.isSupprimerPermis(prononce);
    }

    private static final IJPrononce loadEnfantActif(IJPrononce prononce) throws Exception {
        IJPrononce enfant = prononce.loadEnfantActif(null);

        if (enfant == null) {
            return prononce;
        } else {
            return enfant;
        }
    }

    /**
     * réinitialise l'état d'un prononcé (remet dans état attente).
     * 
     * <ol>
     * <li>Effacer toutes les ij de 'prononce'</li>
     * <li>Pour toutes les bases BASE de 'prononce',
     * {@link IJBaseIndemnisationRegles#reinitialiser(BSession, BITransaction, IJBaseIndemnisation) reinitialiser(BASE)}
     * </li>
     * <li>prononce.csEtat = CS_ATTENTE</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void reinitialiser(BSession session, BITransaction transaction, IJPrononce prononce)
            throws Exception {
        // effacer les les IJ calculées
        prononce.effacerIJCalculees((BTransaction) transaction);

        // réinitialiser les bases d'indemnisation
        IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

        bases.setForIdPrononce(prononce.getIdPrononce());
        bases.setSession(session);
        bases.find();

        for (int idBase = 0; idBase < bases.size(); ++idBase) {
            IJBaseIndemnisation base = (IJBaseIndemnisation) bases.get(idBase);

            IJBaseIndemnisationRegles.reinitialiser(session, transaction, base);
            base.update(transaction);
        }

        prononce.setCsEtat(IIJPrononce.CS_ATTENTE);
    }

    /**
     * En comparant les ijs du prononcé d'origine et celui de correction, restitue, annule ou déplace les bases du
     * prononcé d'origine, annule le prononce d'origine (nouvel etat pas sauve dans la base).
     * 
     * <ol>
     * <li>Pour toutes les bases BASE du prononcé d'origine,
     * {@link IJBaseIndemnisationRegles#copier(BSession, BITransaction, IJBaseIndemnisation, List, IJIJCalculeeAdapter, IJPrononce)
     * copier la base dans le nouveau prononcé}</li>
     * <li>prononceOrigine.csEtat = CS_ANNULE</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononceOrigine
     *            DOCUMENT ME!
     * @param prononceCorrection
     *            DOCUMENT ME!
     * @param ijCalculeesCorrection
     *            La liste des ij calculees du prononce de correction.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void restitutionEtCorrection(BSession session, BITransaction transaction,
            IJPrononce prononceOrigine, IJPrononce prononceCorrection, List ijCalculeesCorrection) throws Exception {
        // charger les ij calculees du prononce d'origine
        IJIJCalculeeManager ijs = null;

        if (IIJPrononce.CS_GRANDE_IJ.equals(prononceOrigine.getCsTypeIJ())) {
            ijs = new IJGrandeIJCalculeeManager();
        } else if (IIJPrononce.CS_PETITE_IJ.equals(prononceOrigine.getCsTypeIJ())) {
            ijs = new IJPetiteIJCalculeeManager();
        } else {
            ijs = new IJIJCalculeeManager();
        }

        ijs.setForIdPrononce(prononceOrigine.getIdPrononce());
        ijs.setOrderBy(IJIJCalculee.FIELDNAME_DATE_DEBUT_DROIT);
        ijs.setSession(session);
        ijs.find();

        // charger les bases d'origine
        IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

        bases.setForIdPrononce(prononceOrigine.getIdPrononce());
        bases.setParentOnly(Boolean.TRUE.toString());
        bases.setSession(session);
        bases.find();

        // itérer sur les bases du prononcé d'origine et cloner les bases
        IJIJCalculeeAdapter ijsCorrectionAdapter = new IJIJCalculeeAdapter(ijCalculeesCorrection);

        for (int idBase = 0; idBase < bases.size(); ++idBase) {
            IJBaseIndemnisation baseOrigine = (IJBaseIndemnisation) bases.get(idBase);

            IJBaseIndemnisationRegles.copier(session, transaction, baseOrigine, ijs.getContainer(),
                    ijsCorrectionAdapter, prononceCorrection);
        }

        prononceOrigine.setCsEtat(IIJPrononce.CS_ANNULE);
    }

    /**
     * fait passer un prononcé dans l'état communiqué.
     * 
     * <p>
     * Note: le nouvel état du prononcé n'est pas sauvegardé.
     * </p>
     * 
     * @param session
     *            une nouvelle valeur pour cet attribut
     * @param transaction
     *            une nouvelle valeur pour cet attribut
     * @param prononce
     *            une nouvelle valeur pour cet attribut
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void setEtatCommunique(BSession session, BITransaction transaction, IJPrononce prononce)
            throws Exception {
        prononce.setCsEtat(IIJPrononce.CS_COMMUNIQUE);
    }

    /**
     * Supprime un prononce enfant (possible si etat == Ouvert ou Valide) et remet a jours l'état des bases
     * d'indemnisations de son parent. Nécessaire si l'on veut pouvoir recorriger une nouvelle fois le parent. Seul les
     * bases du prononcé parent ayant des prestations dans l'état 'Communiqué' et de type Normal seront remises dans
     * l'état 'communiqué'
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononceOrigine
     *            DOCUMENT ME!
     * @param prononceCorrection
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void supprimerPrononceEnfant(BSession session, BITransaction transaction,
            IJPrononce prononceOrigine, IJPrononce prononceEnfant) throws Exception {

        if (IIJPrononce.CS_COMMUNIQUE.equals(prononceEnfant.getCsEtat())
                || IIJPrononce.CS_ANNULE.equals(prononceEnfant.getCsEtat())) {
            throw new Exception(session.getLabel("SUPPR_PRONONCE_ENF_ERR"));
        } else {
            // On reprend toutes les bases d'indemnisations du prononcé enfant.
            IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

            bases.setForIdPrononce(prononceEnfant.getIdPrononce());
            bases.setSession(session);
            bases.setForCsEtats(new String[] { IIJBaseIndemnisation.CS_VALIDE, IIJBaseIndemnisation.CS_OUVERT,
                    IIJBaseIndemnisation.CS_COMMUNIQUE });
            bases.find(transaction);

            for (int idBase = 0; idBase < bases.size(); ++idBase) {
                IJBaseIndemnisation base = (IJBaseIndemnisation) bases.get(idBase);

                // On récupère la base originale
                IJBaseIndemnisation bo = new IJBaseIndemnisation();
                bo.setSession(session);
                bo.setIdBaseIndemisation(base.getIdCorrection());
                bo.retrieve(transaction);

                if ((bo != null) && IIJBaseIndemnisation.CS_ANNULE.equals(bo.getCsEtat())) {
                    // On récupère les prestations de la base originale
                    IJPrestationManager mgr = new IJPrestationManager();
                    mgr.setSession(session);
                    mgr.setForIdBaseIndemnisation(bo.getIdBaseIndemisation());
                    mgr.find(transaction);
                    for (int i = 0; i < mgr.size(); i++) {
                        IJPrestation prst = (IJPrestation) mgr.getEntity(i);
                        // Pour chaque base, on controle s'il y a des
                        // prestations dans l'état DEFINITIF de type NORMAL
                        if (IIJPrestation.CS_DEFINITIF.equals(prst.getCsEtat())
                                && IIJPrestation.CS_NORMAL.equals(prst.getCsType())) {
                            // On remet la base originale dans l'état COMMUNIQUE
                            bo.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                            bo.update(transaction);
                            break;
                        }
                    }
                }
            }

            // Suppression du prononce enfant
            prononceEnfant.delete(transaction);

            // //On remet l'état du prononce d'origine à COMMUNIQUE
            prononceOrigine.setCsEtat(IIJPrononce.CS_COMMUNIQUE);
            prononceOrigine.update(transaction);
        }
    }

    private static final boolean validateNoDecison(IJPrononce prononce, BSession session, BITransaction transaction)
            throws Exception {

        int officeAI = Integer.parseInt(prononce.getOfficeAI());

        if ((officeAI < 301) || ((officeAI > 325) && (officeAI != 327) && (officeAI != 350))) {
            throw new Exception(session.getLabel("NO_DECISION_ERR"));
        }

        if (!JadeStringUtil.isBlankOrZero(prononce.getNoDecisionAI())) {
            // si le numéro de décision est rempli, vérification de l'intégrité
            // du chiffre rentré

            // Format du chiffre : AAAANNNNNNC
            // AAAA => Année
            // NNNNNN => Incrément de 6
            // C => Chiffre clé

            // On créer la clé

            if (prononce.getNoDecisionAI().length() == 11) {

                String idTiers = "";
                if (null != transaction) {
                    idTiers = prononce.loadDemande(transaction).getIdTiers();
                } else {
                    idTiers = prononce.loadDemande((session).newTransaction()).getIdTiers();
                }

                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(prononce.getSession(), idTiers);

                String cleCrée = IJUtils.getChiffreCleDecision(session, tiers
                        .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), prononce.getOfficeAI(), prononce
                        .getNoDecisionAI().substring(0, 10));

                // on compare la clé saisie et la clé crée
                String cleSaisie = prononce.getNoDecisionAI().substring(10);

                if (!cleCrée.equals(cleSaisie)) {
                    throw new Exception(session.getLabel("NO_DECISION_ERR"));
                }

            } else {
                throw new Exception(session.getLabel("NO_DECISION_ERR"));
            }

        } else {
            throw new Exception(session.getLabel("NO_DECISION_OBLIGATOIRE"));
        }

        return true;

    }

    /**
     * Si un prononce est dans l'état valide ou decide, verifie si toutes ses bases sont dans l'etat ouvert et le cas
     * echeant, remet le prononce dans l'etat attente.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void verifierEtatAttente(BSession session, BITransaction transaction, IJPrononce prononce)
            throws Exception {
        // point ouvert 00658
        // ajout etat decide
        if (IIJPrononce.CS_VALIDE.equals(prononce.getCsEtat()) && IIJPrononce.CS_DECIDE.equals(prononce.getCsEtat())) {
            IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

            bases.setForIdPrononce(prononce.getIdPrononce());
            bases.setForCsEtats(new String[] { IIJBaseIndemnisation.CS_VALIDE, IIJBaseIndemnisation.CS_COMMUNIQUE });
            bases.setSession(session);

            if (bases.getCount() > 0) {
                prononce.setCsEtat(IIJPrononce.CS_ATTENTE);
                prononce.update(transaction);
            }
        }
    }

    public static boolean verifieAge(IJAbstractPrononceProxyViewBean viewBean) {
        if(viewBean.getAfficheWarning() && viewBean instanceof IJFpiViewBean) {
            LocalDate dateNaissance = Dates.toDate(((IJFpiViewBean)viewBean).getDateNaissance());
            LocalDate dateChangement = dateNaissance.plusYears(AGE_CHANGEMENT_FPI);
            // prendre le 1er jours du mois suivant
            dateChangement = dateChangement.plusMonths(1).withDayOfMonth(1);
            LocalDate dateDebut = Dates.toDate(viewBean.getPrononce().getDateDebutPrononce());
            LocalDate dateFin = Dates.toDate(viewBean.getPrononce().getDateFinPrononce());
            if(dateDebut != null && dateFin != null && (dateDebut.isBefore(dateChangement) && dateFin.isAfter(dateChangement))) {
                viewBean.setAfficheWarning(true);
                return false;
            }
        }
        return true;
    }

    private IJPrononceRegles() {
        // peut pas creer d'instances
    }
}
