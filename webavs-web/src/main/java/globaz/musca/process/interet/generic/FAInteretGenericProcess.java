package globaz.musca.process.interet.generic;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlan;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlanManager;
import globaz.musca.process.interet.util.FAInteretGenericUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CADetailInteretMoratoireManager;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAPlanCalculInteret;
import globaz.osiris.db.interets.CAPlanCalculInteretManager;

public abstract class FAInteretGenericProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FAPassage passage = null;

    /**
     * Commentaire relatif au constructeur CAProcessInteretSurCotArr.
     */
    public FAInteretGenericProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CAProcessInteretSurCotArr.
     * 
     * @param parent
     *            BProcess
     */
    public FAInteretGenericProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Cette méthode exécute le processus de facturation des intérêts moratoires tardifs. Date de création : (14.02.2002
     * 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        BTransaction osirisTransaction = null;

        try {
            JACalendarGregorian calendar = new JACalendarGregorian();

            BSession osirisSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession(getSession());
            osirisTransaction = (BTransaction) osirisSession.newTransaction();
            osirisTransaction.openTransaction();

            FWCurrency limiteExempte = getLimiteExempte(osirisSession);

            CAPlanCalculInteretManager planManager = new CAPlanCalculInteretManager();
            planManager.setSession(osirisSession);

            planManager.find(osirisTransaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < planManager.size(); i++) {
                CAPlanCalculInteret plan = (CAPlanCalculInteret) planManager.get(i);

                FASumMontantSoumisParPlanManager manager = getSumMontantSoumisParPlanManager(plan);

                manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                FWCurrency montantInteretSoumis = new FWCurrency();
                FWCurrency montantInteretCumule = new FWCurrency();
                CAInteretMoratoire interet = new CAInteretMoratoire();

                for (int j = 0; j < manager.size(); j++) {
                    FASumMontantSoumisParPlan montantSoumisPlanEntete = (FASumMontantSoumisParPlan) manager.get(j);

                    FASumMontantSoumisParPlan next = null;
                    if (j < manager.size() - 1) {
                        next = (FASumMontantSoumisParPlan) manager.get(j + 1);
                    }

                    FASumMontantSoumisParPlan prev = null;
                    if (j > 0) {
                        prev = (FASumMontantSoumisParPlan) manager.get(j + -1);

                        if (!prev.getIdEnteteFacture().equals(montantSoumisPlanEntete.getIdEnteteFacture())) {
                            interet = new CAInteretMoratoire();
                            montantInteretCumule = new FWCurrency();
                        }
                    }

                    montantInteretSoumis = new FWCurrency();

                    JADate dateFinCalcul = getDateFinCalcul(j, manager.size(), montantSoumisPlanEntete,
                            montantSoumisPlanEntete);

                    if (isLigneCandidatAInteret(calendar, montantSoumisPlanEntete)) {

                        montantInteretSoumis = new FWCurrency();
                        montantInteretSoumis.add(montantSoumisPlanEntete.getMontant());

                        double taux = CAInteretUtil.getTaux(getTransaction(),
                                getDateDebutLigneDetailInteret(calendar, montantSoumisPlanEntete));
                        FWCurrency montantInteret = CAInteretUtil.getMontantInteret(osirisSession,
                                montantInteretSoumis, dateFinCalcul,
                                new JADate(getDateDebutLigneDetailInteret(calendar, montantSoumisPlanEntete)), taux);

                        if ((montantInteret != null) && !montantInteret.isZero()) {
                            montantInteretCumule.add(montantInteret);

                            interet = addLigneDetailInteret(osirisSession, plan, interet, dateFinCalcul, taux,
                                    limiteExempte, montantInteretSoumis, montantInteretCumule, montantSoumisPlanEntete,
                                    montantInteret, calendar);

                            if (!getTransaction().hasErrors()) {
                                getTransaction().commit();
                            } else {
                                throw new Exception(getTransaction().getErrors().toString() + " "
                                        + montantSoumisPlanEntete.getIdExterneFacture() + " / "
                                        + montantSoumisPlanEntete.getIdEnteteFacture());
                            }

                            // this.addRemarqueEnteteFacture(montantSoumisPlanEntete.getIdEnteteFacture());
                            // // Bug 5678 - Bugfix 5678
                            // // if (((CPApplication) CAApplication.getApplicationPhenix()).isFactureParAnnee()) {
                            // if (((next != null) && (!next.getIdEnteteFacture().equals(
                            // montantSoumisPlanEntete.getIdEnteteFacture())))
                            // || (j == manager.size() - 1)) {
                            // // Si une entête facture par année, nous pouvons garder le système de sommer le
                            // // détail de l'intérêt du décompte
                            // this.factureInteret(osirisSession, interet, montantSoumisPlanEntete, null);
                            // }
                            // // } else {
                            // // this.factureInteret(osirisSession, interet, montantSoumisPlanEntete, montantInteret);
                            // // }
                        }

                    } else if ((prev != null)
                            && prev.getIdEnteteFacture().equals(montantSoumisPlanEntete.getIdEnteteFacture())
                            && !interet.isNew()) {
                        updateDateFin(interet, montantSoumisPlanEntete, dateFinCalcul);
                    }

                    // Bugfix 5678 : Facture l'intérêt
                    if ((montantInteretCumule != null) && !montantInteretCumule.isZero()) {
                        addRemarqueEnteteFacture(montantSoumisPlanEntete.getIdEnteteFacture());
                        if (((next != null) && (!next.getIdEnteteFacture().equals(
                                montantSoumisPlanEntete.getIdEnteteFacture())))
                                || (j == manager.size() - 1)) {
                            // Si une entête facture par année, nous pouvons garder le système de sommer le
                            // détail de l'intérêt du décompte
                            factureInteret(osirisSession, interet, montantSoumisPlanEntete, null);
                        }
                    }

                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());

            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            }

            if (osirisTransaction != null) {
                try {
                    osirisTransaction.rollback();
                } catch (Exception eTransactionRollback) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                }
            }
        } finally {
            if (osirisTransaction != null) {
                try {
                    if (osirisTransaction.hasErrors()) {
                        osirisTransaction.rollback();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                } finally {
                    try {
                        osirisTransaction.closeTransaction();
                    } catch (Exception e) {
                        getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                    }
                }
            }
        }

        return ((getMemoryLog() != null) && !getMemoryLog().hasErrors());
    }

    /**
     * Ajoute une ligne de détail à l'intérêt.
     * 
     * @param osirisSession
     * @param plan
     * @param interet
     * @param dateFinCalcul
     * @param taux
     * @param limiteExempte
     * @param montantInteretSoumisCumule
     * @param montantInteretCumule
     * @param montantSoumisPlanEntete
     * @param montantInteret
     * @return CAInteretMoratoire
     * @return calendar
     * @throws Exception
     */
    private CAInteretMoratoire addLigneDetailInteret(BSession osirisSession, CAPlanCalculInteret plan,
            CAInteretMoratoire interet, JADate dateFinCalcul, double taux, FWCurrency limiteExempte,
            FWCurrency montantInteretSoumisCumule, FWCurrency montantInteretCumule,
            FASumMontantSoumisParPlan montantSoumisPlanEntete, FWCurrency montantInteret, JACalendarGregorian calendar)
            throws Exception {
        CADetailInteretMoratoire ligne = getLigneDetailInteret(osirisSession, montantInteretSoumisCumule,
                montantSoumisPlanEntete, montantInteret, dateFinCalcul, taux, calendar);

        if (interet.isNew()) {
            interet = getInteretMoratoire(osirisSession, getTransaction(), plan.getIdPlanCalculInteret(),
                    montantSoumisPlanEntete.getIdEnteteFacture());

            interet = setMotifCalculInteret(interet, limiteExempte, montantInteretCumule, montantSoumisPlanEntete);

            interet.add(getTransaction());
        } else {
            interet = setMotifCalculInteret(interet, limiteExempte, montantInteretCumule, montantSoumisPlanEntete);

            interet.update(getTransaction());
        }

        ligne.setIdInteretMoratoire(interet.getIdInteretMoratoire());
        ligne.add(getTransaction());

        return interet;
    }

    /**
     * Ajoute (si nécessaire) une remarque à l'entête de facture. Cette remarque sera ensuite imprimé sur la facture.
     * 
     * @param idEntete
     * @throws Exception
     */
    protected abstract void addRemarqueEnteteFacture(String idEntete) throws Exception;

    private void factureInteret(BSession osirisSession, CAInteretMoratoire interet,
            FASumMontantSoumisParPlan montantSoumisPlanEntete, FWCurrency montantInteret) throws Exception {
        if (interet.isSoumis()) {
            FAInteretGenericUtil.facturer(getSession(), getTransaction(), osirisSession, getPassage(),
                    montantSoumisPlanEntete.getIdEnteteFacture(), interet, montantInteret);
        }

        if (!getTransaction().hasErrors()) {
            getTransaction().commit();
        } else {
            throw new Exception(getTransaction().getErrors().toString() + montantSoumisPlanEntete.getIdExterneFacture()
                    + "/" + montantSoumisPlanEntete.getIdEnteteFacture());
        }
    }

    /**
     * Return la date (format jour.mois.année) de debut d'une ligne de détail d'un intérêt moratoire.
     * 
     * @param calendar
     * @param montantSoumisPlanEntete
     * @return
     */
    protected abstract String getDateDebutLigneDetailInteret(JACalendarGregorian calendar,
            FASumMontantSoumisParPlan montantSoumisPlanEntete) throws Exception;

    /**
     * Return la date de fin pour le calcul des intérêts pour une ligne.
     * 
     * @param count
     * @param total
     * @param montantSoumisPlanEntete
     * @param next
     * @return
     * @throws Exception
     */
    protected abstract JADate getDateFinCalcul(int count, int total, FASumMontantSoumisParPlan montantSoumisPlanEntete,
            FASumMontantSoumisParPlan next) throws Exception;

    /**
     * Return le motif de calcul par défaut de l'intérêt moratoire.<br/>
     * Pour tout les types le motif est CAInteretMoratoire.CS_EXEMPTE
     * 
     * @see CAInteretMoratoire.CS_EXEMPTE
     * @return
     */
    protected abstract String getDefaultMotifCalcul();

    /**
     * @see BProcess.getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("5031") + " " + getPassage().getIdPassage();
        } else {
            return getSession().getLabel("5030") + " " + getPassage().getIdPassage();
        }
    }

    /**
     * Return l'id genre d'interet à générer.
     * 
     * @see CAGenreInteret.CS_TYPE_DECOMPTE_FINAL
     * @return
     */
    protected abstract String getIdGenreInteret();

    /**
     * Return un nouvel intérêt (sans ajout dans la base de données).
     * 
     * @param session
     * @param transaction
     * @param idPlan
     * @param idEntete
     * @return
     * @throws Exception
     */
    private CAInteretMoratoire getInteretMoratoire(BSession session, BTransaction transaction, String idPlan,
            String idEntete) throws Exception {
        CAInteretMoratoire interet = new CAInteretMoratoire();
        interet.setSession(session);

        interet.setIdSection(idEntete);

        interet.setDateCalcul(JACalendar.today().toStr("."));
        interet.setDateFacturation(getPassage().getDateFacturation());

        interet.setMotifcalcul(getDefaultMotifCalcul());
        interet.setIdGenreInteret(getIdGenreInteret());

        interet.setIdJournalFacturation(getPassage().getIdPassage());
        interet.setIdJournalCalcul(getPassage().getIdPassage());

        interet.setIdSectionFacture(idEntete);

        interet.setIdPlan(idPlan);
        interet.setIdRubrique(CAInteretUtil.getIdContrePartie(session, transaction, idPlan, getIdGenreInteret()));

        return interet;
    }

    /**
     * Return une nouvelle ligne de détail d'intérêt (sans ajout dans la base de données).
     * 
     * @param osirisSession
     * @param montantSoumisCumuleParInteret
     * @param montantSoumisPlanEntete
     * @param montantInteret
     * @param dateFinCalcul
     * @param taux
     * @param calendar
     * @return
     */
    private CADetailInteretMoratoire getLigneDetailInteret(BSession osirisSession,
            FWCurrency montantSoumisCumuleParInteret, FASumMontantSoumisParPlan montantSoumisPlanEntete,
            FWCurrency montantInteret, JADate dateFinCalcul, double taux, JACalendarGregorian calendar)
            throws Exception {
        CADetailInteretMoratoire ligne = new CADetailInteretMoratoire();
        ligne.setSession(osirisSession);

        ligne.setIdJournalFacturation(getPassage().getIdPassage());

        ligne.setMontantInteret(montantInteret.toString());

        ligne.setMontantSoumis(montantSoumisCumuleParInteret.toString());

        ligne.setDateDebut(getDateDebutLigneDetailInteret(calendar, montantSoumisPlanEntete));

        ligne.setTaux(String.valueOf(taux));

        ligne.setDateFin(dateFinCalcul.toStr("."));

        ligne.setAnneeCotisation(montantSoumisPlanEntete.getAnneeCotisation());

        return ligne;
    }

    /**
     * Return la limite exempté d'un intérêt moratoire ou rénumératoire.
     * 
     * @param osirisSession
     * @return
     * @throws Exception
     */
    protected abstract FWCurrency getLimiteExempte(BSession osirisSession) throws Exception;

    public FAPassage getPassage() {
        return passage;
    }

    /**
     * Return le manager qui groupe les entete et afacts et somme montant en fonction d'un plan.
     * 
     * @param plan
     * @return
     */
    protected abstract FASumMontantSoumisParPlanManager getSumMontantSoumisParPlanManager(CAPlanCalculInteret plan);

    /**
     * Return true si la ligne (FASumMontantSoumisParPlan) est candidate à intérêt.
     * 
     * @param calendar
     * @param plan
     * @return
     */
    protected abstract boolean isLigneCandidatAInteret(JACalendarGregorian calendar,
            FASumMontantSoumisParPlan montantSoumisPlanEntete) throws Exception;

    /**
     * Le montant de l'intérêt (cumulé) est-il supérieur (inférieur pour rénumératoire) par rapport a la limite exempté
     * ?
     * 
     * @param limiteExempte
     * @param montantInteretCumule
     * @return
     */
    protected abstract boolean isMontantSoumis(FWCurrency limiteExempte, FWCurrency montantInteretCumule);

    /**
     * @see BProcess.jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Mise à jour du motif de calcul de l'intérêt.
     * 
     * @param interet
     * @param limiteExempte
     * @param montantInteretCumule
     * @param planEntete
     * @return
     */
    private CAInteretMoratoire setMotifCalculInteret(CAInteretMoratoire interet, FWCurrency limiteExempte,
            FWCurrency montantInteretCumule, FASumMontantSoumisParPlan planEntete) {
        if ((interet.isExempte() && !planEntete.isExempte() && isMontantSoumis(limiteExempte, montantInteretCumule))
                || (planEntete.isSoumis())) {
            interet.setMotifcalcul(CAInteretMoratoire.CS_SOUMIS);
        }

        return interet;
    }

    /**
     * Method setPassage. Utilise le passage passé en paramètre depuis la facturation
     * 
     * @param passage
     *            passage
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    /**
     * Lorsque l'afact n'est pas soumis à intérêt, si le prédecesseur à trait à la même entête de facture, mettre à jour
     * la date de fin de la dernière ligne de détail de l'intérêt. <br/>
     * Résout problème, si dans un décompte plusieurs années de cotisations sont touchés, mixant soumis et non soumis à
     * intérêt.
     * 
     * @param interet
     * @param montantSoumisPlanEntete
     * @param dateFinCalcul
     * @throws Exception
     */
    private void updateDateFin(CAInteretMoratoire interet, FASumMontantSoumisParPlan montantSoumisPlanEntete,
            JADate dateFinCalcul) throws Exception {
        CADetailInteretMoratoireManager detailManager = new CADetailInteretMoratoireManager();
        detailManager.setSession(getSession());

        detailManager.setForIdInteretMoratoire(interet.getIdInteretMoratoire());
        detailManager.setOrderBy(CADetailInteretMoratoire.FIELD_DATEFIN + " desc");

        detailManager.find(getTransaction());

        if (!detailManager.isEmpty()) {
            CADetailInteretMoratoire detail = (CADetailInteretMoratoire) detailManager.getFirstEntity();
            detail.setSession(getSession());

            detail.setDateFin(dateFinCalcul.toStr("."));

            // Remise à vide du montant, la fonction _validate recalculera le
            // montant en fonction du montant soumis, du nombre de jours et du
            // taux.
            detail.setMontantInteret("");

            detail.update(getTransaction());

            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                throw new Exception(getTransaction().getErrors().toString()
                        + montantSoumisPlanEntete.getIdExterneFacture() + "/"
                        + montantSoumisPlanEntete.getIdEnteteFacture());
            }
        }
    }

}
