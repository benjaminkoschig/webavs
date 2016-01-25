package globaz.ij.regles;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.PRCloneFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui propose des méthodes pour gérer le cycle de vie des bases d'indemnisations.
 * </p>
 * 
 * <p>
 * Les bases sont de trois types (famille de cs IJTYPBASIN):
 * </p>
 * 
 * <dl>
 * <dt>CS_NORMAL</dt>
 * <dd>pour toutes les bases, y compris celles qui corrigent une base d'un même prononcé.</dd>
 * 
 * <dt>CS_CORRECTION</dt>
 * <dd>Pour toutes les bases qui corrigent une base d'un prononcé parent.</dd>
 * 
 * <dt>CS_DUPLICATA</dt>
 * <dd>Pour toutes les bases qui sont la répétition d'une base d'un prononcé parent dont les prestations sont dans
 * l'état définitif.</dd>
 * </dl>
 * 
 * <p>
 * Il y a quatre états possibles pour une base (famille IJETABASIN):
 * </p>
 * 
 * <dl>
 * <dt>CS_OUVERT</dt>
 * <dd>Etat initial d'une base.</dd>
 * 
 * <dt>CS_VALIDE</dt>
 * <dd>Etat d'une base après que les prestations pour cette base ont été calculées.</dd>
 * 
 * <dt>CS_COMMUNIQUE</dt>
 * <dd>Etat de la base après que l'une au moins de ses prestations est passée dans l'état définitif.</dd>
 * 
 * <dt>CS_ANNULE</dt>
 * <dd>Etat d'une base qui a été corrigée.</dd>
 * </dl>
 * 
 * <p>
 * Le cycle de vie général d'une base est le suivant:
 * </p>
 * 
 * <p>
 * <img src="doc-files/baseind.gif" alt="Cycle de vie général d'une base d'indemnisation" title="Cycle de vie général
 * d'une base d'indemnisation">
 * </p>
 * 
 * <p>
 * Lors du passage d'un état à l'autre, les méthodes de cette classe peuvent être appelées pour déclencher les actions
 * conséquentes à ce changement. Ces actions sont les suivantes:
 * </p>
 * 
 * <dl>
 * <dt>Calculer les prestations d'une base BASE (permis si BASE.csEtat == CS_OUVERT ET BASE.dateFin >=
 * BASE.loadPrononce.dateDebut)</dt>
 * <dd>
 * <ul>
 * <li>méthode {@link #correction(BSession, BITransaction, IJBaseIndemnisation) corriger(BASE)}</li>
 * <li>BASE.csEtat = CS_VALIDE</li>
 * </ul>
 * </dd>
 * 
 * <dt>Modifier la base BASE (permis si BASE.csEtat == CS_VALIDE OU BASE.csEtat == CS_OUVERT)</dt>
 * <dd>méthode {@link #reinitialiser(BSession, BITransaction, IJBaseIndemnisation) reinitialiser(BASE)}</dd>
 * 
 * <dt>Corriger la base BASE (permis si BASE.etat == CS_COMMUNIQUE OU (BASE.csEtat == CS_ANNULE ET BASE.loadEnfantActif
 * == null ET BASE.loadPrononce.csEtat != CS_ANNULE ET base.dateFin >= base.loadPrononce.dateDebut))</dt>
 * <dd>méthode {@link #creerCorrection(BSession, BITransaction, IJBaseIndemnisation) creerCorrection(BASE)}</dd>
 * 
 * <dt>Supprimer la base BASE (permis si BASE.csType == CS_NORMAL ET (BASE.csEtat == CS_OUVERT OU BASE.csEtat ==
 * CS_VALIDE))</dt>
 * <dd>Pour toutes les prestations PRE de BASE qui sont dans l'état CS_MIS_EN_LOT,
 * {@link globaz.ij.regles.IJPrestationRegles#annulerMiseEnLot(BSession, BITransaction, IJPrestation) annuler la mise en
 * lot}</dd>
 * </dl>
 * 
 * @author vre
 */
public final class IJBaseIndemnisationRegles {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final JACalendar CALENDAR = new JACalendarGregorian();

    private static String CLONE_BASE_COR;

    private static String CLONE_DEFINITION;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Annule les prestations de cette base ou de toutes ses bases enfants et met ces bases dans l'état annulé.
     * 
     * <ol>
     * <li>S'il n'est pas permis d'annuler la base, rien ne se passe.</li>
     * <li>Si la base est annulée et qu'elle a un enfant actif, les actions suivantes sont effectuées sur l'enfant.</li>
     * <li>Pour toutes les prestations PRE qui ne sont pas des restitutions:<br>
     * 
     * <ol>
     * <li>
     * {@link globaz.ij.regles.IJPrestationRegles#annulerMiseEnLot(BSession, BITransaction, IJPrestation) annuler la
     * mise en lot de PRE}</li>
     * <li>
     * {@link globaz.ij.regles.IJPrestationRegles#annuler(BSession, BITransaction, IJPrestation) annuler PRE} .</li>
     * </ol>
     * </li>
     * <li>baseIndemnisation.csEtat = CS_ANNULE</li>
     * <li>Si la base a un enfant de correction (forcément dans l'état ouvert), celui-ci est annulé.</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void annuler(BSession session, BITransaction transaction, IJBaseIndemnisation baseIndemnisation)
            throws Exception {
        // trop de risques ici, on fait un peu de programmation defensive...
        if (!isAnnulerPermisExt(baseIndemnisation)) {
            return;
        }

        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = loadEnfantActif(baseIndemnisation);
        }

        // charger les prestations de la base d'origine
        IJPrestationManager prestations = new IJPrestationManager();
        HashMap lotToPrestation = new HashMap();

        prestations.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        prestations.setNotForCsType(IIJPrestation.CS_RESTITUTION);
        prestations.setSession(session);
        prestations.find();

        // annuler ces prestations
        for (int idPrestation = 0; idPrestation < prestations.size(); ++idPrestation) {
            IJPrestation prestation = (IJPrestation) prestations.get(idPrestation);

            if (!JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {
                lotToPrestation.put(prestation.getIdLot(), prestation);
            }

            IJPrestationRegles.annuler(session, transaction, prestation);
        }

        // remise des lots modifies dans l'etat ouvert
        for (Iterator idsLots = lotToPrestation.keySet().iterator(); idsLots.hasNext();) {
            IJPrestationRegles.annulerMiseEnLot(session, transaction,
                    (IJPrestation) lotToPrestation.get(idsLots.next()));
        }

        // mettre la base dans l'état annulé
        baseIndemnisation.setCsEtat(IIJBaseIndemnisation.CS_ANNULE);
        baseIndemnisation.update(transaction);

        // annuler une correction encore ouverte si elle existe
        IJBaseIndemnisation correction = baseIndemnisation.loadEnfantCorrection();

        if ((correction != null) && baseIndemnisation.getIdBaseIndemisation().equals(correction.getIdParent())) {
            correction.setCsEtat(IIJBaseIndemnisation.CS_ANNULE);
            correction.update(transaction);
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Copie une base d'indemnisation vers le prononcé 'prononceDest', corrige les prestations de la base pour qu'elles
     * pointent vers les ij de 'ijsDest'.
     * 
     * <ol>
     * <li>Si la base est annulée, les actions suivantes sont effectuées sur son enfant actif.</li>
     * <li>Un clone CLONE de baseOri est créé pour le prononcé prononceDest :<br>
     * 
     * <ol>
     * <li>Si baseOri.csEtat == CS_COMMUNIQUE:<br>
     * 
     * <ol>
     * <li>CLONE.csEtat = CS_COMMUNIQUE</li>
     * <li>CLONE.csTypeBase = CS_DUPLICATA</li>
     * <li>Si, durant la période de la base, les ij pour le prononcé parent sont différentes de celles pour le prononcé
     * enfant, les prestations de la base du prononcé parent
     * {@link #restituer(BSession, BITransaction, IJBaseIndemnisation, IJBaseIndemnisation) sont restitutées dans le
     * prononcé enfant} .</li>
     * </ol>
     * </li>
     * <li>sinon:<br>
     * 
     * <ol>
     * <li>CLONE.csEtat = CS_OUVERT</li>
     * <li>CLONE.csTypeBase = CS_CORRECTION</li>
     * <li>S'il existe une prestation de restitution dans la base du prononcé parent, cette prestation est
     * {@link IJPrestationRegles#copier(BSession, BITransaction, IJPrestation, IJBaseIndemnisation) copiée} dans la base
     * du prononcé enfant.</li>
     * <li>{@link #annuler(BSession, BITransaction, IJBaseIndemnisation) annuler(baseOri)}</li>
     * </ol>
     * </li>
     * </ol>
     * </li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param baseOri
     *            DOCUMENT ME!
     * @param ijsOri
     *            DOCUMENT ME!
     * @param ijsDest
     *            DOCUMENT ME!
     * @param prononceDest
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void copier(BSession session, BITransaction transaction, IJBaseIndemnisation baseOri,
            List ijsOri, IJIJCalculeeAdapter ijsDest, IJPrononce prononceDest) throws Exception {
        PRCloneFactory cf = PRCloneFactory.getInstance();

        // charger l'enfant si necessaire
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseOri.getCsEtat())) {
            baseOri = loadEnfantActif(baseOri);
        }

        // cloner la base
        IJBaseIndemnisation baseDest = (IJBaseIndemnisation) cf.clone(getCloneDefinition(session), session,
                transaction, baseOri, getCloneBaseId(session), IIJBaseIndemnisation.CLONE_NORMAL);

        // deplacer dans le nouveau prononce
        baseDest.setIdParent("0");
        baseDest.setIdCorrection(baseOri.getIdBaseIndemisation());
        baseDest.setIdPrononce(prononceDest.getIdPrononce());
        baseDest.setCsTypeIJ(prononceDest.getCsTypeIJ());

        baseDest.add(transaction);

        if (IIJBaseIndemnisation.CS_COMMUNIQUE.equals(baseOri.getCsEtat())) {
            // des versements ont ete effectues pour cette base, il faut savoir
            // si les montants peuvent etre differents
            switch (ijsDest.compareIJsPourCalcul(baseOri.getDateDebutPeriode(), baseOri.getDateFinPeriode(), ijsOri)) {
                case IJIJCalculeeAdapter.COMPARE_DESUETE:
                    baseDest.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                    baseDest.setCsTypeBase(IIJBaseIndemnisation.CS_DUPLICATA);
                    restituer(session, transaction, baseOri, baseDest);

                    break;
                case IJIJCalculeeAdapter.COMPARE_DIFFERENTES:
                    baseDest.setCsEtat(IIJBaseIndemnisation.CS_OUVERT);
                    baseDest.setCsTypeBase(IIJBaseIndemnisation.CS_CORRECTION);
                    restituer(session, transaction, baseOri, baseDest);

                    break;
                default:
                    baseDest.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                    baseDest.setCsTypeBase(IIJBaseIndemnisation.CS_DUPLICATA);
                    copierPrestationsNonTraites(session, transaction, baseOri, baseDest, false);

                    break;
            }
        } else {
            // aucun versement effectué, on va annuler la base d'origine, de
            // toutes facons elle doit etre tres recente.
            baseDest.setCsEtat(IIJBaseIndemnisation.CS_OUVERT);
            baseDest.setCsTypeBase(IIJBaseIndemnisation.CS_CORRECTION);

            // copier les prestations non traitees
            copierPrestationsNonTraites(session, transaction, baseOri, baseDest, false);
            annuler(session, transaction, baseOri);
        }

        baseDest.update(transaction);
    }

    private static final void copierPrestationsNonTraites(BSession session, BITransaction transaction,
            IJBaseIndemnisation baseOri, IJBaseIndemnisation baseDest, boolean restitution) throws Exception {
        IJPrestationManager prestations = new IJPrestationManager();

        prestations.setForIdBaseIndemnisation(baseOri.getIdBaseIndemisation());
        prestations.setSession(session);
        prestations.find();

        // restituer ou annuler les prestations
        for (int idPrestation = 0; idPrestation < prestations.size(); ++idPrestation) {
            IJPrestation prestation = (IJPrestation) prestations.get(idPrestation);

            if (IIJPrestation.CS_RESTITUTION.equals(prestation.getCsType())) {
                if (!IIJPrestation.CS_DEFINITIF.equals(prestation.getCsEtat())
                        && !IIJPrestation.CS_ANNULE.equals(prestation.getCsEtat())) {
                    IJPrestationRegles.copier(session, transaction, prestation, baseDest);
                    IJPrestationRegles.annulerMiseEnLot(session, transaction, prestation);
                    IJPrestationRegles.annuler(session, transaction, prestation);
                }
            } else {
                if (restitution && IIJPrestation.CS_DEFINITIF.equals(prestation.getCsEtat())) {
                    IJPrestationRegles.restituer(session, transaction, prestation, baseDest.getIdBaseIndemisation());
                } else if (!restitution && !IIJPrestation.CS_DEFINITIF.equals(prestation.getCsEtat())) {
                    IJPrestationRegles.annulerMiseEnLot(session, transaction, prestation);
                    IJPrestationRegles.annuler(session, transaction, prestation);
                }
            }
        }
    }

    /**
     * Si la base 'base' est la correction d'une autre base, effectue les actions nécessaires à l'annulation de la base
     * d'origine.
     * 
     * <p>
     * Si la base d'origine {@link #isAnnulerPermisExt(IJBaseIndemnisation) peut être annulée}, cette base
     * {@link #annuler(BSession, BITransaction, IJBaseIndemnisation) est annulée} sinon
     * {@link #restituer(BSession, BITransaction, IJBaseIndemnisation, IJBaseIndemnisation) elle est restituée}.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param base
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void correction(BSession session, BITransaction transaction, IJBaseIndemnisation base)
            throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(base.getIdCorrection())
                && !JadeStringUtil.isIntegerEmpty(base.getIdParent())) {
            IJBaseIndemnisation baseOrigine = new IJBaseIndemnisation();

            baseOrigine.setIdBaseIndemisation(base.getIdCorrection());
            baseOrigine.setSession(session);
            baseOrigine.retrieve();

            if (IJBaseIndemnisationRegles.isAnnulerPermisExt(baseOrigine)) {
                IJBaseIndemnisationRegles.annuler(session, transaction, baseOrigine);
            } else {
                IJBaseIndemnisationRegles.restituer(session, transaction, baseOrigine, base);
            }
        }
    }

    /**
     * cree une base de correction de la base 'baseIndemnisation' ou charge celle qui existe déjà.
     * 
     * <ol>
     * <li>Si la base est un duplicata d'un prononcé parent, les actions suivantes sont effectuées sur son parent.</li>
     * <li>Si la base est annulée, les actions suivantes sont effectuées sur son enfant actif.</li>
     * <li>Si la base a déjà une correction, rien ne se passe</li>
     * <li>La base est clonée, son clone lui est rajouté comme enfant</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return la base de correctiou
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IJBaseIndemnisation creerCorrection(BSession session, BITransaction transaction,
            IJBaseIndemnisation baseIndemnisation) throws Exception {
        // si la base est un duplicata d'une base d'un autre prononcé, on charge
        // cette base.
        if (IIJBaseIndemnisation.CS_DUPLICATA.equals(baseIndemnisation.getCsTypeBase())) {
            IJBaseIndemnisation baseParent = new IJBaseIndemnisation();

            baseParent.setIdBaseIndemisation(baseIndemnisation.getIdCorrection());
            baseParent.setSession(session);
            baseParent.retrieve();

            PRCloneFactory cf = PRCloneFactory.getInstance();
            IJBaseIndemnisation correction = (IJBaseIndemnisation) cf.clone(getCloneDefinition(session), session,
                    transaction, baseParent, getCloneBaseId(session), IIJBaseIndemnisation.CLONE_FILS);

            correction.setCsEtat(IIJBaseIndemnisation.CS_OUVERT);
            correction.setCsTypeBase(IIJBaseIndemnisation.CS_NORMAL);
            correction.setIdParent(baseIndemnisation.getIdBaseIndemisation());
            correction.setIdCorrection(baseIndemnisation.getIdBaseIndemisation());
            correction.setIdPrononce(baseIndemnisation.getIdPrononce());

            // insérer dans la base
            correction.setSession(session);
            correction.add(transaction);

            return correction;
        }

        // charger la correction de la base d'origine si celle-ci a déjà été
        // corrigée une fois
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = baseIndemnisation.loadEnfantActif(transaction);
        }

        // si cette base a déjà une correction, on ne fait rien
        if (baseIndemnisation.loadEnfantCorrection() == null) {
            PRCloneFactory cf = PRCloneFactory.getInstance();
            IJBaseIndemnisation correction = (IJBaseIndemnisation) cf.clone(getCloneDefinition(session), session,
                    transaction, baseIndemnisation, getCloneBaseId(session), IIJBaseIndemnisation.CLONE_FILS);

            correction.setCsEtat(IIJBaseIndemnisation.CS_OUVERT);
            correction.setCsTypeBase(IIJBaseIndemnisation.CS_NORMAL);

            // insérer dans la base
            correction.setSession(session);
            correction.add(transaction);

            return correction;
        } else {
            return baseIndemnisation.loadEnfantCorrection();
        }
    }

    private static final String getCloneBaseId(BSession session) throws Exception {
        if (CLONE_BASE_COR == null) {
            CLONE_BASE_COR = session.getApplication().getProperty(IJApplication.PROPERTY_CLONE_BASE_COR);
        }

        return CLONE_BASE_COR;
    }

    private static final String getCloneDefinition(BSession session) throws Exception {
        if (CLONE_DEFINITION == null) {
            CLONE_DEFINITION = session.getApplication().getProperty(IJApplication.PROPERTY_CLONE_DEFINITION);
        }

        return CLONE_DEFINITION;
    }

    /**
     * retourne vrai si la base (et celle-ci uniquement) peut être annulée.
     * 
     * @param baseIndemnisation
     *            une base avec une session bvalide
     * 
     * @return DOCUMENT ME!
     */
    public static final boolean isAnnulerPermis(IJBaseIndemnisation baseIndemnisation) {
        return (!IIJBaseIndemnisation.CS_DUPLICATA.equals(baseIndemnisation.getCsTypeBase()) && IIJBaseIndemnisation.CS_VALIDE
                .equals(baseIndemnisation.getCsEtat()))
                || IIJBaseIndemnisation.CS_OUVERT.equals(baseIndemnisation.getCsEtat());
    }

    /**
     * retourne vrai si la base ou son enfant actif peut être annulée.
     * 
     * @param baseIndemnisation
     *            une base avec une session bvalide
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isAnnulerPermisExt(IJBaseIndemnisation baseIndemnisation) throws Exception {
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = loadEnfantActif(baseIndemnisation);
        }

        return isAnnulerPermis(baseIndemnisation);
    }

    /**
     * retourne vrai s'il est permis de calculer les prestations pour la base d'indemnisation donnée (et celle-ci
     * uniquement).
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut calculer prestations permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCalculerPermis(IJBaseIndemnisation baseIndemnisation, IJPrononce prononce)
            throws Exception {

        // Pas permis de calculer si le prononcé est annulé
        if (prononce.getCsEtat().equals(IIJPrononce.CS_ANNULE)) {
            return false;
        }

        if (isModifierPermis(baseIndemnisation)) {
            return CALENDAR.compare(baseIndemnisation.getDateFinPeriode(), prononce.getDateDebutPrononce()) != JACalendar.COMPARE_FIRSTLOWER;
        }

        return false;
    }

    /**
     * retourne vrai s'il est permis de calculer les prestations pour la base d'indemnisation donnée (ou son seul
     * possible enfant actif).
     * 
     * <p>
     * Cela est permis uniquement si la base est en etat ouvert et s'il existe des ij calculees pour le prononce.
     * </p>
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut calculer prestations permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCalculerPermisExt(IJBaseIndemnisation baseIndemnisation, IJPrononce prononce)
            throws Exception {
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = loadEnfantActif(baseIndemnisation);
        }

        return isCalculerPermis(baseIndemnisation, prononce);
    }

    /**
     * retourne vrai si cette base (et celle-ci uniquement) peut être corrigee.
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut corriger permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCorrigerPermis(IJBaseIndemnisation baseIndemnisation) throws Exception {
        if (IIJBaseIndemnisation.CS_COMMUNIQUE.equals(baseIndemnisation.getCsEtat())
                || IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            return (baseIndemnisation.loadEnfantActif(null) == null)
                    && !IIJPrononce.CS_ANNULE.equals(baseIndemnisation.loadPrononce(null).getCsEtat())
                    && (CALENDAR.compare(baseIndemnisation.getDateFinPeriode(), baseIndemnisation.loadPrononce(null)
                            .getDateDebutPrononce()) != JACalendar.COMPARE_FIRSTLOWER);
        }

        return false;
    }

    /**
     * retourne vrai si cette base ou son (seul possible) enfant actif peut etre corrigee.
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut corriger permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCorrigerPermisExt(IJBaseIndemnisation baseIndemnisation) throws Exception {
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = loadEnfantActif(baseIndemnisation);
        }

        return isCorrigerPermis(baseIndemnisation);
    }

    /**
     * retourne vrais si cette base (et celle-ci uniquement) peut etre modifiee.
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public static final boolean isModifierPermis(IJBaseIndemnisation baseIndemnisation) {
        return IIJBaseIndemnisation.CS_OUVERT.equals(baseIndemnisation.getCsEtat())
                || IIJBaseIndemnisation.CS_VALIDE.equals(baseIndemnisation.getCsEtat());
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut modifier permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isModifierPermisExt(IJBaseIndemnisation baseIndemnisation) throws Exception {
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = loadEnfantActif(baseIndemnisation);
        }

        return isModifierPermis(baseIndemnisation);
    }

    /**
     * retourne vrai si la base peut être restituée.
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isRestituerPermisExt(IJBaseIndemnisation baseIndemnisation) throws Exception {
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = loadEnfantActif(baseIndemnisation);
        }

        return IIJBaseIndemnisation.CS_COMMUNIQUE.equals(baseIndemnisation.getCsEtat());
    }

    /**
     * retourne vrai si cette base (et celle-ci uniquement) peut etre supprime.
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut supprimer permis
     */
    public static final boolean isSupprimerPermis(IJBaseIndemnisation baseIndemnisation) {
        return IIJBaseIndemnisation.CS_NORMAL.equals(baseIndemnisation.getCsTypeBase())
                && (IIJBaseIndemnisation.CS_VALIDE.equals(baseIndemnisation.getCsEtat()) || IIJBaseIndemnisation.CS_OUVERT
                        .equals(baseIndemnisation.getCsEtat()));
    }

    /**
     * retourne vrai si cette base ou son (seul possible) enfant actif peut etre supprime.
     * 
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut supprimer permis
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isSupprimerPermisExt(IJBaseIndemnisation baseIndemnisation) throws Exception {
        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = loadEnfantActif(baseIndemnisation);
        }

        return isSupprimerPermis(baseIndemnisation);
    }

    private static final IJBaseIndemnisation loadEnfantActif(IJBaseIndemnisation base) throws Exception {
        IJBaseIndemnisation enfant = base.loadEnfantActif(null);

        if (enfant == null) {
            return base;
        } else {
            return enfant;
        }
    }

    /**
     * réinitialise cette base ou son (seul possible) enfant actif.
     * 
     * <ol>
     * <li>S'il n'est pas permis de modifier la base, rien ne se passe.</li>
     * <li>Pour toutes les prestations PRE de la base 'baseIndemnisation':<br>
     * 
     * <ol>
     * <li>Si PRE.csEtat == CS_MIS_EN_LOT,
     * {@link globaz.ij.regles.IJPrestationRegles#annulerMiseEnLot(BSession, BITransaction, IJPrestation) annuler la
     * mise en lot}</li>
     * <li>Effacer la prestation PRE</li>
     * </ol>
     * </li>
     * <li>baseIndemnisation.csEtat = CS_OUVERT (pas sauvé dans la base)</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            la base à rouvrir.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void reinitialiser(BSession session, BITransaction transaction,
            IJBaseIndemnisation baseIndemnisation) throws Exception {
        reinitialiser(session, transaction, baseIndemnisation, "");
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param idIJCalculee
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void reinitialiser(BSession session, BITransaction transaction,
            IJBaseIndemnisation baseIndemnisation, String idIJCalculee) throws Exception {
        if (!isModifierPermisExt(baseIndemnisation)) {
            return;
        }

        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseIndemnisation.getCsEtat())) {
            baseIndemnisation = baseIndemnisation.loadEnfantActif(transaction);
        }

        // effacer les prestations
        IJPrestationManager prestations = new IJPrestationManager();
        HashMap lotToPrestation = new HashMap();

        prestations.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        prestations.setNotForCsType(IIJPrestation.CS_RESTITUTION);
        prestations.setForIdIJCalculee(idIJCalculee);
        prestations.setSession(session);
        prestations.find(transaction);

        for (int idPrestation = 0; idPrestation < prestations.size(); ++idPrestation) {
            IJPrestation prestation = (IJPrestation) prestations.get(idPrestation);

            if (!JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {
                lotToPrestation.put(prestation.getIdLot(), prestation);
            }

            prestation.delete(transaction);
        }

        // remise des lots modifies dans l'etat ouvert
        for (Iterator idsLots = lotToPrestation.keySet().iterator(); idsLots.hasNext();) {
            IJPrestationRegles.annulerMiseEnLot(session, transaction,
                    (IJPrestation) lotToPrestation.get(idsLots.next()));
        }

        baseIndemnisation.setCsEtat(IIJBaseIndemnisation.CS_OUVERT);
    }

    /**
     * Restitue les prestations de la base d'origine ou celles de son (seul possible) enfant actif et met toutes les
     * bases dans l'état annulé.
     * 
     * <ol>
     * <li>Si 'baseOrigine' ne peut pas être restituée, rien ne se passe</li>
     * <li>Si 'baseOrigine' est annulée, les actions suivantes sont effectuées sur son enfant</li>
     * <li>Pour toutes les prestations PRE de 'baseOrigine':
     * 
     * <dl>
     * <dt>Si PRE est une restitution et que PRE.etat == ATTENTE ou PRE.etat == OUVERT</dt>
     * <dd>
     * {@link IJPrestationRegles#copier(BSession, BITransaction, IJPrestation, IJBaseIndemnisation) copier la prestation
     * dans la base de correction}</dd>
     * 
     * <dt>Si PRE n'est pas une restitution et que PRE.etat == DEFINITIF</dt>
     * <dd>
     * {@link globaz.ij.regles.IJPrestationRegles#restituer(BSession, BITransaction, IJPrestation, String) restituer PRE
     * en créant une prestation de restitution dans la base 'baseCorrection'}</dd>
     * 
     * <dt>Si PRE.etat != DEFINITIF</dt>
     * <dd>PRE.etat == ANNULE.</dd>
     * </dl>
     * </li>
     * <li>baseOrigine.csEtat = CS_ANNULE</li>
     * <li>Si 'baseOrigine' a un enfant de correction (forcément dans l'état ouvert), celui-ci est annulé.</li>
     * </ol>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param baseOrigine
     *            DOCUMENT ME!
     * @param baseCorrection
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void restituer(BSession session, BITransaction transaction, IJBaseIndemnisation baseOrigine,
            IJBaseIndemnisation baseCorrection) throws Exception {
        // trop de risques ici, on fait un peu de programmation defensive...
        if (!isRestituerPermisExt(baseOrigine)) {
            return;
        }

        if (IIJBaseIndemnisation.CS_ANNULE.equals(baseOrigine.getCsEtat())) {
            baseOrigine = baseOrigine.loadEnfantActif(transaction);
        }

        // traiter les prestations
        copierPrestationsNonTraites(session, transaction, baseOrigine, baseCorrection, true);

        baseOrigine.setCsEtat(IIJBaseIndemnisation.CS_ANNULE);
        baseOrigine.update(transaction);

        // annuler une correction encore ouverte si elle existe
        IJBaseIndemnisation correction = baseOrigine.loadEnfantCorrection();

        if (!correction.getIdBaseIndemisation().equals(correction.getIdBaseIndemisation()) && (correction != null)) {
            correction.setCsEtat(IIJBaseIndemnisation.CS_ANNULE);
            correction.update(transaction);
        }
    }

    /**
     * Fait passer une base d'indemnisation en état communiqué.
     * 
     * <p>
     * Attention: Cette méthode ne FAIT PAS passer le prononcé dont la base dépend dans l'état communiqué !
     * </p>
     * 
     * <p>
     * Note: le nouvel etat de la base n'est pas sauvegardé.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void setEtatCommunique(BSession session, BITransaction transaction,
            IJBaseIndemnisation baseIndemnisation) throws Exception {
        baseIndemnisation.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
    }

    private IJBaseIndemnisationRegles() {
        // peut pas creer d'instances
    }
}
