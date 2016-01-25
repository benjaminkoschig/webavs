package globaz.ij.helpers.basesindemnisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.calendar.IJCalendar;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationAitAaViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Calendar;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJBaseIndemnisationAitAaHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * Si la base d'indemnisation est dans l'état validé ou ouvert, q'il s'agit d'une base enfant (correction) et que la
     * base parente se trouve dans l'état Annulé, remettre la base parente dans l'état communiqué.
     * 
     * Il est possible d'avoir la base parent ANNULEE et la base enfant OUVERT dans le cas suivant : 1) Parent ANNULE
     * Enfant VALIDE
     * 
     * 2) modification de l'enfant, ce que fait repasser l'enfant en ouvert, d'ou 3)
     * 
     * 3) Parent ANNULE ENFANT OUVERT
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        try {
            IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            // Il s'agit d'une base enfant dans l'état validé.
            if (!JadeStringUtil.isIntegerEmpty(biViewBean.getIdParent())
                    && (IIJBaseIndemnisation.CS_VALIDE.equals(biViewBean.getCsEtat()) || IIJBaseIndemnisation.CS_OUVERT
                            .equals(biViewBean.getCsEtat()))) {

                // s'il y a plusieurs enfants pour un parent, il faut reprendre
                // la base avant celle que l'on supprime
                // non-javadoc
                //
                // Exemple : | idBaseInd | idParent |
                // | --------- | -------- |
                // 1) | 842 | 0 |
                // 2) | 979 | 842 |
                // 3) | 980 | 842 |
                //
                // Si par exemple, 1) et 2) sont dans l'état annulé et que 3)
                // est dans l'état validé. Et que l'on veut
                // supprimer la base 3), alors on va laisser la 1) telle quelle,
                // mais par contre, on va mettre la 2) dans l'état
                // communiqué après la suppression de la 3)
                //

                // Pour cela, on regarde tout d'abord s'il y a plusieurs bases
                // avec l'idParent de la base
                IJBaseIndemnisationManager baseIndMan = new IJBaseIndemnisationManager();
                baseIndMan.setSession((BSession) session);
                baseIndMan.setForIdParent(biViewBean.getIdParent());
                baseIndMan.setOrderBy(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION + " DESC");
                baseIndMan.find(transaction);

                // S'il y en a plusieurs
                if (baseIndMan.size() > 1) {

                    // orderbypardefaut --> Id BaseIndemnisations DESC.
                    // Donc, il faut prendre le deuxième de la liste
                    int nb = 0;
                    for (Iterator iter = baseIndMan.iterator(); iter.hasNext();) {
                        IJBaseIndemnisation baseInd = (IJBaseIndemnisation) iter.next();
                        nb++;
                        if (nb == 2) {
                            if (!baseInd.isNew() && IIJBaseIndemnisation.CS_ANNULE.equals(baseInd.getCsEtat())) {
                                baseInd.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                                baseInd.update(transaction);
                            }
                        }
                    }

                    // S'il n'y a pas plusieurs enfants, on va donc modifier le
                    // parent !
                } else {

                    // On récupére la base parente
                    IJBaseIndemnisation parent = new IJBaseIndemnisation();
                    parent.setSession((BSession) session);
                    parent.setIdBaseIndemisation(biViewBean.getIdParent());
                    parent.retrieve(transaction);
                    if (!parent.isNew() && IIJBaseIndemnisation.CS_ANNULE.equals(parent.getCsEtat())) {
                        parent.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                        parent.update(transaction);
                    }

                }

            }
            // On supprime le bean.
            if (!transaction.hasErrors()) {
                super._delete(viewBean, action, session);
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
        } finally {
            if (transaction != null) {
                try {
                    if (!transaction.hasErrors() && !transaction.isRollbackOnly()) {
                        transaction.commit();
                    } else {
                        transaction.rollback();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
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
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);

        // initialisation du calendrier
        IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;
        Calendar dateDebut = Calendar.getInstance();

        dateDebut.set(Calendar.DAY_OF_MONTH, 1);
        biViewBean.setDateDebut(dateDebut);
        biViewBean.setIjCalendar(new IJCalendar((BSession) session, dateDebut.get(Calendar.MONTH) + 1, dateDebut
                .get(Calendar.YEAR), dateDebut.get(Calendar.DAY_OF_MONTH), dateDebut
                .getActualMaximum(Calendar.DAY_OF_MONTH)));
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
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;

        // Même dans le cas de période non étendue, il faudra initialiser le
        // calendrier
        // pour ne pas avoir de null pointer exception...
        // Il sera donc initialiser en fonction de la date de début de la
        // période étendue.
        // CAD, si date début période étendue == 10.10.2010
        // le calendrier sera initialiser avec dd = 10.10.2010 et df =
        // 10.10.2010

        if (biViewBean.getIsPeriodeEtendue().booleanValue()) {

            // initialisation du calendrier
            Calendar dateDebut = Calendar.getInstance();

            // date debut
            JADate date = new JADate(biViewBean.getDateDebutPeriode());

            dateDebut.set(Calendar.DAY_OF_MONTH, date.getDay());
            dateDebut.set(Calendar.MONTH, date.getMonth() - 1);
            dateDebut.set(Calendar.YEAR, date.getYear());

            biViewBean.setDateDebut(dateDebut);
            biViewBean.setJourFin(String.valueOf(date.getDay()));

            // calendar
            biViewBean.setIjCalendar(new IJCalendar((BSession) session, dateDebut.get(Calendar.MONTH) + 1, dateDebut
                    .get(Calendar.YEAR), dateDebut.get(Calendar.DAY_OF_MONTH), date.getDay()));
            biViewBean.getIjCalendar().setValeur(".");

        }

        else {
            // initialisation du calendrier
            Calendar dateDebut = Calendar.getInstance();

            // date debut
            JADate date = new JADate(biViewBean.getDateDebutPeriode());

            dateDebut.set(Calendar.DAY_OF_MONTH, date.getDay());
            dateDebut.set(Calendar.MONTH, date.getMonth() - 1);
            dateDebut.set(Calendar.YEAR, date.getYear());

            biViewBean.setDateDebut(dateDebut);

            // date fin
            date = new JADate(biViewBean.getDateFinPeriode());

            biViewBean.setJourFin(String.valueOf(date.getDay()));

            // calendar
            biViewBean.setIjCalendar(new IJCalendar((BSession) session, dateDebut.get(Calendar.MONTH) + 1, dateDebut
                    .get(Calendar.YEAR), dateDebut.get(Calendar.DAY_OF_MONTH), date.getDay()));
            biViewBean.getIjCalendar().setValeur(biViewBean.getAttestationJours());

        }
    }

    /**
     * redéfini pour réinitialiser l'état de la base si nécessaire.
     * 
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
        IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;
        BITransaction transaction = ((BSession) session).newTransaction();

        try {
            transaction.openTransaction();
            // reinitialiser la base
            IJBaseIndemnisationRegles.reinitialiser((BSession) session, transaction, biViewBean);
            biViewBean.update(transaction);

            // reinitialiser l'etat du prononce si necessaire
            IJPrononce prononce = IJPrononce.loadPrononce((BSession) session, transaction, biViewBean.getIdPrononce(),
                    biViewBean.getCsTypeIJ());

            IJPrononceRegles.verifierEtatAttente((BSession) session, transaction, prononce);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
        } finally {
            if (transaction != null) {
                try {
                    if (!transaction.hasErrors() && !transaction.isRollbackOnly()) {
                        transaction.commit();
                    } else {
                        transaction.rollback();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * cree ou charge la base d'indemnisation de correction pour celle qui est transmise et retourne celle qui est
     * transmise.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur de l'argument viewBean.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface creerCorrection(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJBaseIndemnisationAitAaViewBean baseOrigine = (IJBaseIndemnisationAitAaViewBean) viewBean;

        baseOrigine.retrieve();

        if (IJBaseIndemnisationRegles.isCorrigerPermisExt(baseOrigine)) {
            IJBaseIndemnisationRegles.creerCorrection(session, session.getCurrentThreadTransaction(), baseOrigine);
        }

        return baseOrigine;
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }
}
