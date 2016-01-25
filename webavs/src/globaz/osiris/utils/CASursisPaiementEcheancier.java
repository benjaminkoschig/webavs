package globaz.osiris.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CASection;
import java.math.BigDecimal;

/**
 * @author SEL <br>
 *         Date : 15 déc. 2008
 */
public class CASursisPaiementEcheancier {
    /**
     * Ajoute le nombre de mois ou de jours nécessaire au calendrier pour couvrir la prochaine échéance.
     * 
     * @param session
     * @param dateSource
     * @param recouvrement
     * @param count
     * @return
     */
    public static JADate addToCalendar(BSession session, JADate dateSource, CAPlanRecouvrement recouvrement, int count)
            throws Exception {
        JACalendar cal = session.getApplication().getCalendar();

        if (CAPlanRecouvrement.CS_ECHEANCE_ANNUELLE.equals(recouvrement.getIdTypeEcheance())) {
            return cal.addMonths(dateSource, 12);
        } else if (CAPlanRecouvrement.CS_ECHEANCE_TRIMESTRIELLE.equals(recouvrement.getIdTypeEcheance())) {
            return cal.addMonths(dateSource, 3);
        } else if (CAPlanRecouvrement.CS_ECHEANCE_MENSUELLE.equals(recouvrement.getIdTypeEcheance())) {
            return cal.addMonths(dateSource, 1);
        } else if (CAPlanRecouvrement.CS_ECHEANCE_BIMESTRIELLE.equals(recouvrement.getIdTypeEcheance())) {
            return cal.addMonths(dateSource, 2);
        } else if (CAPlanRecouvrement.CS_ECHEANCE_SEMESTRIELLE.equals(recouvrement.getIdTypeEcheance())) {
            return cal.addMonths(dateSource, 6);
        } else if (CAPlanRecouvrement.CS_ECHEANCE_2_MOIS_SUR_3.equals(recouvrement.getIdTypeEcheance())) {
            if ((count % 2) == 0) {
                return cal.addMonths(dateSource, 1);
            } else {
                return cal.addMonths(dateSource, 2);
            }
        } else if (CAPlanRecouvrement.CS_ECHEANCE_HEBDOMADAIRE.equals(recouvrement.getIdTypeEcheance())) {
            return cal.addDays(dateSource, 7);
        } else if (CAPlanRecouvrement.CS_ECHEANCE_QUINZAINE.equals(recouvrement.getIdTypeEcheance())) {
            return cal.addDays(dateSource, 14);
        } else {
            return cal.addDays(dateSource, 0);
        }
    }

    /**
     * @param transaction
     * @param aPaiementRecuAnnule
     *            : montant du paiement ou montant restant du paiement reçu
     * @param premierPassage
     *            : premier passage, prend la première échéance sans date effective
     * @param idPlan
     * @param section
     *            : pemet de remettre l'idPlan dans la section
     * @throws Exception
     */
    public static void annulerVentilationPmtEcheance(BTransaction transaction, String aPaiementRecuAnnule,
            Boolean premierPassage, String idPlan, CASection section) throws Exception {
        CAEcheancePlanManager manager = null;
        if (premierPassage.booleanValue()) {
            // Premier passage, prend la première échéance sans date effective
            manager = CASursisPaiementEcheancier.findNextEcheance(transaction, idPlan);
            if (manager.isEmpty()) {
                // Aucune échéance trouvée, c'est que le plan est soldé.
                CAPlanRecouvrement plan = new CAPlanRecouvrement();
                plan.setIdPlanRecouvrement(idPlan);
                plan.retrieve(transaction);
                if (plan.isNew() || plan.hasErrors()) {
                    throw new Exception(transaction.getSession().getLabel("PLAN_NON_RESOLU"));
                }
                // On active le plan
                if (CAPlanRecouvrement.CS_SOLDE.equals(plan.getIdEtat())) {
                    plan.setIdEtat(CAPlanRecouvrement.CS_ACTIF);
                    plan.update(transaction);

                    // Remet l'idPlan dans la section
                    if (JadeStringUtil.isBlankOrZero(section.getIdPlanRecouvrement())) {
                        CASection sectionUpdate = section;
                        sectionUpdate.setIdPlanRecouvrement(plan.getIdPlanRecouvrement());
                        sectionUpdate.update(transaction);
                    }
                }
            }
        }
        // Prend la dernière échéance soldée
        if (!premierPassage.booleanValue() || manager.isEmpty()) {
            manager = CASursisPaiementEcheancier.findEcheancesSoldeDesc(transaction, idPlan);
        }
        CAEcheancePlan echeance = (CAEcheancePlan) manager.getFirstEntity();
        if (echeance == null) {
            return;
        }

        // Montant du paiement ou montant restant du paiement reçu
        FWCurrency paiementRecuAnnule = new FWCurrency(aPaiementRecuAnnule);
        // Montant de l'échéance déjà payé
        FWCurrency montantEcheancePaye = new FWCurrency(echeance.getMontantPaye());
        FWCurrency montantEcheanceRestant = new FWCurrency(echeance.getMontant());
        montantEcheanceRestant.add(montantEcheancePaye);

        // La date effective doit dans tous les cas être réinitialisée à ""
        echeance.setDateEffective("");

        // paiementRecu > montantEcheancePaye : Paiement plus grand que le montant de l'échéance payée.
        if (paiementRecuAnnule.getBigDecimalValue().negate()
                .compareTo(montantEcheancePaye.getBigDecimalValue().negate()) > 0) {
            // Soustrait le montant de l'échéance au montant payé
            paiementRecuAnnule.sub(montantEcheancePaye);
            // Met à zero le montant payé
            echeance.setMontantPayeNegatif(new FWCurrency(0).toStringFormat());
            echeance.update(transaction);

            // On annule la ventilation du paiement restant.
            CASursisPaiementEcheancier.annulerVentilationPmtEcheance(transaction, paiementRecuAnnule.toString(),
                    Boolean.FALSE, idPlan, section);
        } else {
            // Si Paiement à annuler est plus petit que le montant payé de la dernière échéance,
            // on soustrait le paiement annulé au montant payé
            montantEcheancePaye.sub(paiementRecuAnnule);
            // On met à jour l'échéance.
            echeance.setMontantPayeNegatif(montantEcheancePaye.toStringFormat());
            echeance.update(transaction);
        }
    }

    /**
     * Prend la dernière échéance payée
     * 
     * @param transaction
     * @param idPlan
     * @return CAEcheancePlanManager pour dateEffective is not null, order by dateExigibilite DESC
     * @throws Exception
     */
    public static CAEcheancePlanManager findEcheancesSoldeDesc(BTransaction transaction, String idPlan)
            throws Exception {
        CAEcheancePlanManager manager = CASursisPaiementEcheancier.initManager(transaction, idPlan);
        // On enlève la condition
        manager.setForDateEffectiveIsNull("");
        manager.setForDateEffectiveIsNotNull();
        // On prend le dernier
        manager.setOrder(CAEcheancePlan.FIELD_DATEEXIGIBILITE + CAEcheancePlanManager.DESC);
        manager.find(transaction);
        return manager;
    }

    /**
     * Prend la première échéance sans date effective <br>
     * Initialise le manager avec : l'idPlan, dateEffective Is Null, order by dateExigibilite. <br>
     * Fait le find.
     * 
     * @param transaction
     * @param idPlan
     * @return CAEcheancePlanManager après le find.
     * @throws Exception
     */
    public static CAEcheancePlanManager findNextEcheance(BTransaction transaction, String idPlan) throws Exception {
        // initialise avec l'id du plan
        CAEcheancePlanManager manager = CASursisPaiementEcheancier.initManager(transaction, idPlan);

        manager.setForDateEffectiveIsNull();
        manager.setOrder(CAEcheancePlan.FIELD_DATEEXIGIBILITE);
        manager.find(transaction);
        return manager;
    }

    /**
     * Mise à jour de la date d'exigibilitée de la prochaine echéance.
     * 
     * @param session
     * @param dateSource
     * @param echeancesSize
     * @param recouvrement
     * @return la prochaine date d'échéance en tenant compte des week-ends et des jours fériés.
     * @throws Exception
     */
    public static JADate getNextDateExigibilite(BSession session, JADate dateSource, int echeancesSize,
            CAPlanRecouvrement recouvrement) throws Exception {
        if (echeancesSize != 0) {
            // pas la permière échéance
            // Enregistre la date
            dateSource = CASursisPaiementEcheancier.addToCalendar(session, dateSource, recouvrement, echeancesSize - 1);
        }

        if (!CAPlanRecouvrement.CS_ECHEANCE_HEBDOMADAIRE.equals(recouvrement.getIdTypeEcheance())
                && !CAPlanRecouvrement.CS_ECHEANCE_QUINZAINE.equals(recouvrement.getIdTypeEcheance())) {
            dateSource = CADateUtil.getDateOuvrableMoisCourant(dateSource);
        } else {
            dateSource = CADateUtil.getDateOuvrable(dateSource);
        }

        return dateSource;
    }

    /**
     * Initialise avec l'id du plan
     * 
     * @param transaction
     * @param idPlan
     * @return CAEcheancePlanManager setter avec l'idPlan
     * @throws Exception
     */
    private static CAEcheancePlanManager initManager(BTransaction transaction, String idPlan) throws Exception {
        CAEcheancePlanManager manager = new CAEcheancePlanManager();
        manager.setSession(transaction.getSession());
        manager.setForIdPlanRecouvrement(idPlan);
        return manager;
    }

    /**
     * @param transaction
     * @param idPlan
     *            : null possible au premier passage
     * @param idEcheance
     * @param aPaiementRecu
     * @param datePaiement
     * @throws Exception
     */
    public static void ventilationPaiementEcheance(BTransaction transaction, String aPaiementRecu, String idPlan,
            String datePaiement) throws Exception {
        Boolean lastEcheance = new Boolean(false);
        // Prend la première échéance sans date effective
        CAEcheancePlanManager manager = CASursisPaiementEcheancier.findNextEcheance(transaction, idPlan);
        if (manager.isEmpty()) {
            lastEcheance = Boolean.TRUE;
            // Prend la dernière échéance payée
            manager = CASursisPaiementEcheancier.findEcheancesSoldeDesc(transaction, idPlan);
        }
        // Bug 6114
        if (manager.size() == 0) {
            throw new Exception(transaction.getSession().getLabel("AUCUNE_ECHEANCE") + " : " + idPlan);
        }

        CAEcheancePlan echeance = (CAEcheancePlan) manager.getFirstEntity();

        /* Montant du paiement ou montant restant du paiement reçu */
        FWCurrency paiementRecu = new FWCurrency(aPaiementRecu);
        /* Montant de l'échéance déjà payé */
        FWCurrency montantEcheancePaye = new FWCurrency(echeance.getMontantPaye());
        /* Montant de l'échéance */
        FWCurrency montantEcheanceRestant = new FWCurrency(echeance.getMontant());
        montantEcheanceRestant.add(montantEcheancePaye);

        // paiementRecu < montantEcheance : Paiement plus bas que le solde de l'échéance
        if (paiementRecu.getBigDecimalValue().negate().compareTo(montantEcheanceRestant.getBigDecimalValue()) == -1) {
            // On ajoute le paiement au montant déja payé
            montantEcheancePaye.add(aPaiementRecu);
            echeance.setMontantPayeNegatif(montantEcheancePaye.toStringFormat());
            echeance.update(transaction);
        } else if (lastEcheance.booleanValue()) {
            // S'il reste encore un montant (aPaiementRecu) après la dernière échéance, on met tout sur cette dernière.
            echeance.setMontantPayeNegatif(new BigDecimal(echeance.getMontant()).negate()
                    .add(paiementRecu.getBigDecimalValue()).toString());
            echeance.update(transaction);
        } else {
            // Si paiement égale ou plus grand que l'échéance
            // On renseigne le montant payé et la date effective de l'échéance courante
            echeance.setMontantPayeNegatif(new BigDecimal(echeance.getMontant()).negate().toString());
            echeance.setDateEffective(datePaiement);

            paiementRecu.add(montantEcheanceRestant);
            echeance.update(transaction);
            // Et on met la différence sur la prochaine échéance s'il y en a une. Sinon, on fait rien.
            CASursisPaiementEcheancier.ventilationPaiementEcheance(transaction, paiementRecu.toString(), idPlan,
                    datePaiement);
        }
    }

}
