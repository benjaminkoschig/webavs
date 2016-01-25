/*
 * Créé le 11 juin 07
 */

package globaz.corvus.external;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.process.REGenererAttestationProlongationEtudeProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.hera.vb.famille.SFPeriodeViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.prof.JadeProfiler;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author SCR
 * 
 *         ExternalService appelé lors de la modification d'une période, mise à jours automatique des date d'échéances
 *         pour les rentes accordée du tiers concernés.
 * 
 * 
 */
public class ExternalServicePeriodeEtudeViewBean extends BAbstractEntityExternalService {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Constructeur du type ExternalServicePeriodeEtudeViewBean.
     */
    public ExternalServicePeriodeEtudeViewBean() {
        super();
    }

    // ~ Methodes
    // -------------------------------------------------------------------------------------------------------------------

    /**
     * Execute un deuxième afterAdd
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterAdd(BEntity arg0) throws Throwable {
    }

    /**
     * Exécute un service externe après suppression d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterDelete(BEntity arg0) throws Throwable {
    }

    /**
     * Exécute un service externe après chargement d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterRetrieve(BEntity arg0) throws Throwable {
    }

    /**
     * Exécute un service externe après mise à jour d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void afterUpdate(BEntity arg0) throws Throwable {
    }

    /**
     * Exécute un service externe avant ajout d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeAdd(BEntity arg0) throws Throwable {
        doMAJDateEcheance(arg0);
    }

    /**
     * Exécute un service externe avant suppression d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // Globaz try catch, toute les erreurs sont encapsulées.
        // Ceci permet de mettre ce service a tous les clients, même ceux
        // n'ayant pas les rentes !!!!
        // Les evt. exceptions ne génèreront pas d'erreur !!!
        BITransaction transaction = null;

        try {

            JadeLogger.trace(this, "ExternalServicePeriodeEtudeViewBean.doMAJDateEcheance(" + entity + ")");
            JadeProfiler.begin(this, "ExternalServicePeriodeEtudeViewBean.doMAJDateEcheance()");
            SFPeriodeViewBean pViewBean = (SFPeriodeViewBean) entity;

            if (!pViewBean.getSession().hasErrors()) {

                try {
                    transaction = pViewBean.getSession().newTransaction();
                    transaction.openTransaction();

                    if (ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE.equals(pViewBean.getType())) {

                        // Recherche des rentes en cours pour le tiers.
                        SFMembreFamille mFam = new SFMembreFamille();
                        mFam.setSession(entity.getSession());
                        mFam.setIdMembreFamille(pViewBean.getIdMembreFamille());
                        mFam.retrieve(transaction);

                        RERenteAccordeeManager mgr2 = new RERenteAccordeeManager();
                        mgr2.setSession(entity.getSession());
                        mgr2.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_CALCULE + ", "
                                + IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_VALIDE);
                        // mgr2.setForEnCoursAtMois(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dEch.toStrAMJ()));
                        mgr2.setForIdTiersBeneficiaire(mFam.getIdTiers());
                        mgr2.find(transaction);

                        for (int i = 0; i < mgr2.size(); i++) {
                            RERenteAccordee ra = (RERenteAccordee) mgr2.getEntity(i);
                            // cf. bz 4005.
                            ra.setDateEcheance("");
                            ra.update(transaction);
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
                                String err = null;
                                if (transaction.hasErrors()) {
                                    err = transaction.getErrors().toString();
                                }
                                transaction.rollback();
                                throw new Throwable(err);

                            } else {
                                transaction.commit();
                            }

                        } catch (Exception e) {
                            throw e;
                        } finally {
                            transaction.closeTransaction();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e2) {
            e2.printStackTrace();
        } finally {
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    /**
     * Exécute un service externe avant chargement d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeRetrieve(BEntity arg0) throws Throwable {
    }

    /**
     * Exécute un service externe avant mise à jour d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        doMAJDateEcheance(entity);
    }

    private void doMAJDateEcheance(BEntity entity) throws Throwable {

        // Globaz try catch, toute les erreurs sont encapsulées.
        // Ceci permet de mettre ce service a tous les clients, même ceux
        // n'ayant pas les rentes !!!!
        // Les evt. exceptions ne génèreront pas d'erreur !!!
        BITransaction transaction = null;

        try {
            JadeLogger.trace(this, "ExternalServicePeriodeEtudeViewBean.doMAJDateEcheance(" + entity + ")");
            JadeProfiler.begin(this, "ExternalServicePeriodeEtudeViewBean.doMAJDateEcheance()");
            SFPeriodeViewBean pViewBean = (SFPeriodeViewBean) entity;

            if (JadeStringUtil.isBlankOrZero(pViewBean.getDateFin())) {
                return;
            }

            if (!pViewBean.getSession().hasErrors()) {

                try {
                    transaction = pViewBean.getSession().newTransaction();
                    transaction.openTransaction();

                    if (ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE.equals(pViewBean.getType())) {

                        JACalendar cal = new JACalendarGregorian();

                        JADate dEch = null;
                        if (!JadeStringUtil.isBlankOrZero(pViewBean.getDateFin())) {
                            dEch = new JADate(pViewBean.getDateFin());
                        }

                        // Calcul de la date de fin d'étude
                        // Si plusieurs périodes d'études, prendre celle avec la
                        // plus grande date de fin...
                        String idMF = pViewBean.getIdMembreFamille();
                        SFPeriodeManager mgr = new SFPeriodeManager();
                        mgr.setSession(entity.getSession());
                        mgr.setForIdMembreFamille(idMF);
                        mgr.setForType(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE);
                        mgr.find(transaction);
                        for (int i = 0; i < mgr.size(); i++) {
                            SFPeriode per = (SFPeriode) mgr.getEntity(i);
                            // On skip ce cas !!!! car la date de fin est dans
                            // le viewBean, et pourrait être différente
                            // de celle de la DB en cas de modification.
                            if (per.getIdPeriode().equals(pViewBean.getIdPeriode())) {
                                continue;
                            }
                            JADate df = null;
                            if (!JadeStringUtil.isBlankOrZero(per.getDateFin())) {
                                df = new JADate(per.getDateFin());
                            }
                            if ((dEch == null) && (df != null)) {
                                dEch = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(df.toStrAMJ()));
                            } else if ((dEch != null) && (df != null)) {
                                if (cal.compare(dEch, df) == JACalendar.COMPARE_FIRSTLOWER) {
                                    dEch = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(df.toStrAMJ()));
                                }
                            }
                        }

                        // Pas de date de fin, on stop le traitement.
                        if (dEch == null) {
                            return;
                        }

                        // Recherche des rentes en cours pour le tiers.
                        SFMembreFamille mFam = new SFMembreFamille();
                        mFam.setSession(entity.getSession());
                        mFam.setIdMembreFamille(pViewBean.getIdMembreFamille());
                        mFam.retrieve(transaction);

                        if (!mFam.isNew() && !JadeStringUtil.isBlankOrZero(mFam.getIdTiers())) {

                            // Contrôle si l'assuré a 25 ans avant la date
                            // d'échéance!!!
                            // Si c'est le cas, la date d'échéance sera égale à
                            // la date de ses 25 ans.

                            PRTiersWrapper tw = PRTiersHelper.getTiersParId(entity.getSession(), mFam.getIdTiers());
                            if (tw != null) {
                                String dn = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
                                JADate d25Ans = new JADate(dn);
                                d25Ans = cal.addYears(d25Ans, 25);
                                if (cal.compare(d25Ans, dEch) == JACalendar.COMPARE_FIRSTLOWER) {
                                    dEch = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(d25Ans
                                            .toStrAMJ()));

                                    pViewBean.setMessage(java.text.MessageFormat.format(
                                            entity.getSession().getLabel("SF_PERIODE_ECHEANCE_25_ANS_ERR"),
                                            new Object[] { PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(d25Ans
                                                    .toStrAMJ()) }));

                                    pViewBean.setMsgType(FWViewBeanInterface.WARNING);
                                    pViewBean
                                            .setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dEch.toStrAMJ()));
                                }
                            }

                            // BZ 5169 la génération du document ce fait avant les tests sur les blocages, car
                            // dans le cas contraire le document n'était pas imprimer à chaque fois
                            // à cause du return qu'il y a dans ces tests.
                            String dateEcheance = JACalendar.format(dEch, JACalendar.FORMAT_MMsYYYY);

                            JACalendarGregorian j = new JACalendarGregorian();
                            int resultCompar = j.compare(dateEcheance,
                                    REPmtMensuel.getDateDernierPmt(entity.getSession()));

                            if (((JACalendar.COMPARE_FIRSTUPPER == resultCompar) || (JACalendar.COMPARE_EQUALS == resultCompar))) {
                                REGenererAttestationProlongationEtudeProcess process = new REGenererAttestationProlongationEtudeProcess();
                                process.setSession(entity.getSession());
                                process.setIdTiers(mFam.getIdTiers());
                                process.setDateEcheanceEtude(dEch);
                                process.setEmailAdresse(entity.getSession().getUserEMail());
                                BProcessLauncher.start(process, false);
                            }

                            RERenteAccordeeManager mgr2 = new RERenteAccordeeManager();
                            mgr2.setSession(entity.getSession());
                            mgr2.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_CALCULE + ", "
                                    + IREPrestationAccordee.CS_ETAT_PARTIEL + ", "
                                    + IREPrestationAccordee.CS_ETAT_VALIDE);
                            mgr2.setForEnCoursAtMois(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dEch.toStrAMJ()));
                            mgr2.setForIdTiersBeneficiaire(mFam.getIdTiers());
                            mgr2.find(transaction);

                            // bz-5169
                            boolean stopTraitement = false;
                            String warningMessage = "";

                            for (int i = 0; i < mgr2.size(); i++) {
                                RERenteAccordee ra = (RERenteAccordee) mgr2.getEntity(i);

                                // bz-4229
                                /**
                                 * Au moment de la màj de la date d'échéance de la rente accordée, si le code "B"
                                 * (blocage) est actif, le rendre inactif.
                                 * 
                                 * Si un montant est à débloquer, afficher un message "Le montant de CHF xxx.xx est à
                                 * débloquer manuellement pour "756.xxxx.xxxx.xx Kiki Cucu"".
                                 * 
                                 **/

                                if (ra.getIsPrestationBloquee()) {
                                    // recherche du montant a débloquer !!!
                                    REEnteteBlocage entete = new REEnteteBlocage();
                                    entete.setSession(entity.getSession());
                                    entete.setIdEnteteBlocage(ra.getIdEnteteBlocage());
                                    entete.retrieve(transaction);

                                    if (!entete.isNew()) {
                                        FWCurrency mntBolque = new FWCurrency(entete.getMontantBloque());
                                        FWCurrency mntDebolque = new FWCurrency(entete.getMontantDebloque());
                                        mntBolque.sub(mntDebolque);

                                        // On stoppe le traitement et informe
                                        // l'utilisateur !!!!
                                        if (mntBolque.isPositive()) {
                                            // // bz-5169
                                            stopTraitement = true;
                                            warningMessage += java.text.MessageFormat.format(
                                                    entity.getSession().getLabel("SF_MAJ_PERIODE_ETUDE_ERR1"),
                                                    new Object[] {
                                                            mntBolque.toStringFormat(),
                                                            tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
                                                                    + " " + tw.getProperty(PRTiersWrapper.PROPERTY_NOM)
                                                                    + " "
                                                                    + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM) })
                                                    + "<br/>";

                                            ra.setDateEcheance(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dEch
                                                    .toStrAMJ()));
                                            ra.update(transaction);
                                        }

                                    }

                                }

                            }

                            // bz-5169
                            if (stopTraitement) {
                                pViewBean.setMsgType(FWViewBeanInterface.WARNING);
                                pViewBean.setMessage(warningMessage);
                                return;
                            }

                            // SI on arrive ici, c'est que la maj peut
                            // s'effectuer !!!!
                            for (int i = 0; i < mgr2.size(); i++) {
                                RERenteAccordee ra = (RERenteAccordee) mgr2.getEntity(i);
                                // cf. bz 4005.
                                ra.setDateEcheance(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dEch.toStrAMJ()));
                                // bz-4229
                                // On enlève le blocage !!
                                if (ra.getIsPrestationBloquee()) {
                                    ra.setIsPrestationBloquee(Boolean.FALSE);
                                }
                                ra.update(transaction);
                            }
                        }
                    } else {
                        SFPeriode periode = new SFPeriode();
                        periode.setSession(entity.getSession());
                        periode.setIdPeriode(pViewBean.getIdPeriode());
                        periode.retrieve(transaction);

                        // La période est passée de ETUDE a qqch de
                        // différent....
                        // Dans ce cas, il faut remettre a zéro la date
                        // d'échéance de la RA
                        if (!periode.isNew() && ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE.equals(periode.getType())) {

                            // Recherche des rentes en cours pour le tiers.
                            SFMembreFamille mFam = new SFMembreFamille();
                            mFam.setSession(entity.getSession());
                            mFam.setIdMembreFamille(pViewBean.getIdMembreFamille());
                            mFam.retrieve(transaction);

                            RERenteAccordeeManager mgr2 = new RERenteAccordeeManager();
                            mgr2.setSession(entity.getSession());
                            mgr2.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_CALCULE + ", "
                                    + IREPrestationAccordee.CS_ETAT_PARTIEL + ", "
                                    + IREPrestationAccordee.CS_ETAT_VALIDE);
                            // mgr2.setForEnCoursAtMois(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dEch.toStrAMJ()));
                            mgr2.setForIdTiersBeneficiaire(mFam.getIdTiers());
                            mgr2.find(transaction);

                            for (int i = 0; i < mgr2.size(); i++) {
                                RERenteAccordee ra = (RERenteAccordee) mgr2.getEntity(i);
                                // cf. bz 4005.
                                ra.setDateEcheance("");
                                ra.update(transaction);
                            }
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
                                String err = null;
                                if (transaction.hasErrors()) {
                                    err = transaction.getErrors().toString();
                                }
                                transaction.rollback();
                                throw new Throwable(err);

                            } else {
                                transaction.commit();
                            }

                        } catch (Exception e) {
                            throw e;
                        } finally {
                            transaction.closeTransaction();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e2) {
            e2.printStackTrace();
        } finally {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    /**
     * Exécute un service externe pour initialiser une entité
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void init(BEntity arg0) throws Throwable {
    }

    /**
     * Exécute un service externe pour valider le contenu d'une entité
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void validate(BEntity arg0) throws Throwable {
    }

}
