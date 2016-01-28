package globaz.osiris.db.bulletinneutre;

import globaz.aquila.db.process.COSectionBulletinNeutreABloquerManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.process.AFProcessFacturationBvrNeutre;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CAGroupement;
import globaz.osiris.db.comptes.CAGroupementManager;
import globaz.osiris.db.comptes.CAOperationBulletinNeutre;
import globaz.osiris.db.comptes.CAOperationBulletinNeutreManager;
import globaz.osiris.db.comptes.CAPaiementManager;
import globaz.osiris.externe.CAGestionBulletinNeutre;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CAComptabiliserBulletinNeutre {

    /**
     * L'activation des paiements sur section de type bulletin neutre permet de récupérer les opérations (de type
     * bulletin neutre) et de créer les écritures (factures) en fonction de leurs taux.
     * 
     * @param session
     * @param transaction
     * @param ecriture
     */
    public static void activer(BTransaction transaction, CAEcriture ecriture) {
        try {
            // L'écriture fait déjà partie d'un groupement
            CAGroupementManager grpManager = new CAGroupementManager();
            grpManager.setSession(ecriture.getSession());
            grpManager.setForIdOperationMaster(ecriture.getIdOperation());
            if (grpManager.getCount(transaction) > 0) {
                return;
            }

            // Test si un décompte final, un bouclement d'acompte ou une taxation d'office (qui s'apparente à un
            // décompte 13) figure dans le compte pour la période couverte par le bulletin neutre.
            if (!APISection.STATUTBN_DECOMPTE_FINAL.equals(ecriture.getSection().getStatutBN())
                    && !APISection.STATUTBN_ANNULE.equals(ecriture.getSection().getStatutBN())) {
                CAOperationBulletinNeutreManager manager = CAComptabiliserBulletinNeutre
                        .findBulletinNeutreOperationManager(transaction, ecriture);

                FWCurrency montantARepartir = new FWCurrency(ecriture.getMontant());
                montantARepartir.abs();
                montantARepartir.sub(ecriture.getSection().getSolde());

                // Sortir s'il n'y a rien à répartir
                if (!montantARepartir.isPositive()) {
                    return;
                }

                ArrayList<CAOperationBulletinNeutre> listOperationBN;

                // Charger les opérations dans la liste
                if (!manager.isEmpty()) {
                    listOperationBN = new ArrayList<CAOperationBulletinNeutre>();
                    for (int i = 0; i < manager.size(); i++) {
                        listOperationBN.add((CAOperationBulletinNeutre) manager.get(i));
                    }
                } else {
                    // Si aucune opération de type Bulletin Neutre ne sont résolues
                    // => nouvelle tentative
                    // en se basant sur le plan d'affiliation
                    listOperationBN = CAComptabiliserBulletinNeutre.loadPlanAffiliation(transaction, ecriture);
                }
                // S'il n'y a rien à ventiler -> Erreur
                if ((listOperationBN == null) || listOperationBN.isEmpty()) {
                    transaction.addErrors(transaction.getSession().getLabel("7400")
                            + ecriture.getCompteAnnexe().getIdExterneRole());
                    return;
                }
                BigDecimal sumTaux = CAComptabiliserBulletinNeutre.getTotalTaux(transaction.getSession(),
                        listOperationBN);
                FWCurrency totalMontantsRepartis = new FWCurrency();
                CAGroupement groupement = null;
                groupement = ecriture.createGroupement(transaction);
                for (int i = 0; i < listOperationBN.size(); i++) {
                    CAOperationBulletinNeutre bulletinNeutre = listOperationBN.get(i);

                    CAEcriture newEcriture = CAComptabiliserBulletinNeutre.createEcriture(transaction, ecriture,
                            montantARepartir, sumTaux, groupement, bulletinNeutre, totalMontantsRepartis,
                            i == listOperationBN.size() - 1);

                    totalMontantsRepartis.add(newEcriture.getMontant());
                }
            }
        } catch (Exception e) {
            transaction.addErrors(e.getMessage());
        }
    }

    /**
     * Créer l'écriture (la facture) pour une paiement d'un bulletin neutre. <br/>
     * Calcul la masse par rapport au montant total payé et le taux total des opération de bulletins neutres.
     * 
     * @param session
     * @param transaction
     * @param ecriture
     * @param montantARepartir
     * @param sumTaux
     * @param groupement
     * @param bulletinNeutre
     * @return
     * @throws Exception
     */
    private static CAEcriture createEcriture(BTransaction transaction, CAEcriture ecriture,
            FWCurrency montantARepartir, BigDecimal sumTaux, CAGroupement groupement,
            CAOperationBulletinNeutre bulletinNeutre, FWCurrency total, boolean isLastRecord) throws Exception {
        BigDecimal masse = sumTaux.multiply(montantARepartir.getBigDecimalValue());

        CAEcriture newEcriture = new CAEcriture();
        newEcriture.setSession(transaction.getSession());
        newEcriture.setIdJournal(ecriture.getIdJournal());
        newEcriture.setIdCompteAnnexe(ecriture.getIdCompteAnnexe());
        newEcriture.setIdCaisseProfessionnelle(bulletinNeutre.getIdCaisseProfessionnelle());
        newEcriture.setIdSection(ecriture.getIdSection());

        newEcriture.setIdCompte(bulletinNeutre.getIdCompte());
        newEcriture.setAnneeCotisation(bulletinNeutre.getAnneeCotisation());

        newEcriture.setDate(bulletinNeutre.getDate());
        newEcriture.setCodeMaster(APIOperation.SINGLE);

        newEcriture.setMasse(masse.toString());
        newEcriture.setTaux(bulletinNeutre.getTaux());

        if (masse.signum() == 1) {
            newEcriture.setCodeDebitCredit(APIEcriture.DEBIT);
        } else {
            newEcriture.setCodeDebitCredit(APIEcriture.CREDIT);
        }
        // Calcul de la cotisation
        BigDecimal taux = new BigDecimal(bulletinNeutre.getTaux()).divide(new BigDecimal(100),
                BigDecimal.ROUND_HALF_EVEN);
        FWCurrency cotisation = new FWCurrency(masse.multiply(taux).doubleValue());
        cotisation.round(FWCurrency.ROUND_5CT);
        // Ajout de la différence d'arrondi éventuelle si dernière ligne
        if (isLastRecord) {
            FWCurrency solde = new FWCurrency(montantARepartir.doubleValue());
            solde.sub(total.doubleValue());
            solde.sub(cotisation.doubleValue());
            if (!solde.isZero()) {
                cotisation.add(solde.doubleValue());
                newEcriture.setMontant(cotisation.toString());
            }
        }
        newEcriture.activer(transaction);
        newEcriture.add(transaction);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (newEcriture.isNew()) {
            throw new Exception(transaction.getSession().getLabel("5033"));
        }

        ecriture.addEcritureToGroupement(transaction, groupement, newEcriture);

        return newEcriture;
    }

    /**
     * @param transaction
     * @param dateReference
     * @return
     * @throws Exception
     */
    public static COSectionBulletinNeutreABloquerManager findBulletinNeutreAvecDecompteFinal(BTransaction transaction,
            String idCompteAnnexe, String idSection) throws Exception {
        COSectionBulletinNeutreABloquerManager manager = new COSectionBulletinNeutreABloquerManager();
        manager.setSession(transaction.getSession());
        manager.setForIdCompteAnnexe(idCompteAnnexe);
        manager.setTesterSoldeEtPmtCmpZero(false);
        manager.setForIdSection(idSection);
        manager.find(transaction);
        return manager;
    }

    /**
     * Recherche les opérations Bulletin Neutre affectants la section.
     * 
     * @param session
     * @param transaction
     * @param ecriture
     * @return
     * @throws Exception
     */
    private static CAOperationBulletinNeutreManager findBulletinNeutreOperationManager(BTransaction transaction,
            CAEcriture ecriture) throws Exception {
        CAOperationBulletinNeutreManager manager = new CAOperationBulletinNeutreManager();
        manager.setSession(transaction.getSession());

        manager.setForIdSection(ecriture.getIdSection());

        ArrayList<String> etat = new ArrayList<String>();
        etat.add(APIOperation.ETAT_COMPTABILISE);
        etat.add(APIOperation.ETAT_PROVISOIRE);
        manager.setForEtatIn(etat);

        manager.find(transaction);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        return manager;
    }

    /**
     * Return le montant des taxes de sommation déjà comptabilisées sur la section.
     * 
     * @param session
     * @param transaction
     * @param idSection
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantTaxeSommation(BSession session, BTransaction transaction, String idSection)
            throws Exception {
        CAEcritureManager taxeManager = new CAEcritureManager();
        taxeManager.setSession(session);
        taxeManager.setForIdCompte(new CAGestionBulletinNeutre().getIdRubriquesTaxeSommation(session, transaction));

        ArrayList<String> etat = new ArrayList<String>();
        etat.add(APIOperation.ETAT_COMPTABILISE);
        etat.add(APIOperation.ETAT_PROVISOIRE);
        taxeManager.setForEtatIn(etat);

        taxeManager.setForIdSection(idSection);

        taxeManager.find(transaction);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        FWCurrency totalTaxeSommation = new FWCurrency();

        for (int i = 0; i < taxeManager.size(); i++) {
            CAEcriture taxe = (CAEcriture) taxeManager.get(i);
            totalTaxeSommation.add(taxe.getMontant());
        }

        return totalTaxeSommation;
    }

    /**
     * Return le total des taux des opérations bulletin neutre.<br/>
     * Servira de base (100%) pour le calcul des écritures à générer.
     * 
     * @param session
     * @param manager
     * @return
     */
    private static BigDecimal getTotalTaux(BSession session, ArrayList<CAOperationBulletinNeutre> listCotisationsBN)
            throws Exception {
        BigDecimal sumTaux = new BigDecimal("0.00");
        for (int i = 0; i < listCotisationsBN.size(); i++) {
            CAOperationBulletinNeutre bulletinNeutre = listCotisationsBN.get(i);
            sumTaux = sumTaux.add(new BigDecimal(bulletinNeutre.getTaux()));
        }

        if (sumTaux.compareTo(new BigDecimal("0.00")) == 0) {
            throw new Exception(session.getLabel("SOMME_TAUX_NON_VALIDE"));
        }

        return new BigDecimal("100.00").divide(sumTaux, 5, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Des paiements ont-ils déjà été comptabilisés sur cette section ? Si oui aucune processus de bulletin neutre.
     * 
     * @param session
     * @param transaction
     * @param ecriture
     * @return
     * @throws Exception
     */
    private static boolean hasPaiements(BSession session, BTransaction transaction, CAEcriture ecriture)
            throws Exception {
        CAPaiementManager paiementManager = new CAPaiementManager();
        paiementManager.setSession(session);
        paiementManager.setForIdSection(ecriture.getIdSection());

        ArrayList<String> etat = new ArrayList<String>();
        etat.add(APIOperation.ETAT_COMPTABILISE);
        etat.add(APIOperation.ETAT_PROVISOIRE);
        paiementManager.setForEtatIn(etat);

        paiementManager.find(transaction);

        return (!paiementManager.isEmpty());
    }

    /**
     * @param session
     * @param transaction
     * @param idSection
     * @return
     * @throws Exception
     */
    public static ArrayList<CAOperationBulletinNeutre> loadOperationsBN(BTransaction transaction, String idSection)
            throws Exception {
        // Validation des paramètres
        if ((transaction == null) || (transaction.getSession() == null)) {
            throw new Exception("Transaction and session required in loadOperationsBN");
        }
        if (JadeStringUtil.isBlankOrZero(idSection)) {
            throw new Exception("idSection required in loadOperationBN");
        }
        ArrayList<CAOperationBulletinNeutre> operationsBN = new ArrayList<CAOperationBulletinNeutre>();
        //
        BSession sessionOsiris = new BSession(transaction.getSession());
        CAOperationBulletinNeutreManager mgr = new CAOperationBulletinNeutreManager();
        mgr.setSession(sessionOsiris);
        mgr.setForIdSection(idSection);
        ArrayList<String> etat = new ArrayList<String>();
        etat.add(APIOperation.ETAT_COMPTABILISE);
        etat.add(APIOperation.ETAT_PROVISOIRE);
        mgr.setForEtatIn(etat);
        mgr.find(transaction);
        //
        if (!mgr.isEmpty()) {
            for (int i = 0; i < mgr.size(); i++) {
                operationsBN.add((CAOperationBulletinNeutre) mgr.get(i));
            }
        }
        //
        return operationsBN;
    }

    /**
     * Ventilation sur la base du plan d'affiliation
     * 
     * @throws Exception
     */
    private static ArrayList<CAOperationBulletinNeutre> loadPlanAffiliation(BTransaction transaction,
            CAEcriture ecriture) throws Exception {
        // Récupérer les lignes à facturer à partir de la facturation
        AFProcessFacturationBvrNeutre process = new AFProcessFacturationBvrNeutre();
        process.setSession(transaction.getSession());
        process.setTransaction(transaction);
        return process.listCotisationsForBulletinNeutre(transaction, ecriture.getSection(), ecriture.getJournal());
    }

    /**
     * Non instanciable
     */
    private CAComptabiliserBulletinNeutre() {
    }
}
