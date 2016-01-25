package globaz.lynx.helpers.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.paiement.LXPaiementViewBean;
import java.util.ArrayList;

public class LXPaiementUtils {

    /**
     * Retrouve la facture de base qui a généré le paiement.
     * 
     * @param session
     * @param transaction
     * @param idOperationSrc
     * @return
     * @throws Exception
     */
    public static LXOperation getFactureBase(BISession session, BTransaction transaction, String idOperationSrc)
            throws Exception {
        LXOperation factureBase = new LXOperation();
        factureBase.setSession((BSession) session);

        factureBase.setIdOperation(idOperationSrc);

        factureBase.retrieve(transaction);

        if (factureBase.hasErrors()) {
            throw new Exception(factureBase.getErrors().toString());
        }

        if (factureBase.isNew()) {
            throw new Exception(((BSession) session).getLabel("FACTURE_BASE_NON_RENSEIGNEE"));
        }
        return factureBase;
    }

    /**
     * Retourne la liste des paiements et escomptes liés à la facture en excluant le paiement en cours.
     * 
     * @param session
     * @param idOperationSrc
     * @param idOperationPaiement
     * @return
     */
    public static LXOperationManager getOtherPaiementEscompte(BISession session, String idOperationSrc,
            String idOperationPaiement) {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession((BSession) session);

        manager.setForIdOperationSrc(idOperationSrc);
        manager.setForIdOperationNot(idOperationPaiement);

        ArrayList<String> listeEtat = new ArrayList<String>();
        listeEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
        listeEtat.add(LXOperation.CS_ETAT_OUVERT);
        manager.setForCsEtatIn(listeEtat);

        return manager;
    }

    /**
     * Si l'on modifie le paiement afin de le bloqué, son statut passe à ANNULER, respectivement OUVERT.
     * 
     * @param session
     * @param transaction
     * @param paiement
     * @throws Exception
     */
    public static void updateEtatBloque(BISession session, BTransaction transaction, LXPaiementViewBean paiement)
            throws Exception {
        if (paiement.getEstBloque().booleanValue() && LXOperation.CS_ETAT_OUVERT.equals(paiement.getCsEtat())) {
            paiement.setCsEtat(LXOperation.CS_ETAT_ANNULE);
        } else if (!paiement.getEstBloque().booleanValue() && LXOperation.CS_ETAT_ANNULE.equals(paiement.getCsEtat())) {
            paiement.setCsEtat(LXOperation.CS_ETAT_OUVERT);
        }
    }

    /**
     * Mise à jour de l'état de la facture de base. PREPARE si soldé, sinon COMPTABILISE afin de pouvoir la payer en
     * plusieurs fois.
     * 
     * @param session
     * @param transaction
     * @param factureBase
     * @param base
     * @param pmtAndEscompte
     * @throws Exception
     */
    private static void updateEtatFactureBase(BISession session, BTransaction transaction, LXOperation factureBase,
            FWCurrency base, FWCurrency pmtAndEscompte) throws Exception {
        if (pmtAndEscompte.compareTo(base) > 0) {
            throw new Exception(((BSession) session).getLabel("SOMME_PAIEMENTS_ESCOMPTE_FACTURE"));
        } else if (pmtAndEscompte.compareTo(base) < 0) {
            factureBase.setCsEtatOperation(LXOperation.CS_ETAT_COMPTABILISE);
            factureBase.update(transaction);

            if (factureBase.hasErrors()) {
                throw new Exception(factureBase.getErrors().toString());
            }
        } else {
            factureBase.setCsEtatOperation(LXOperation.CS_ETAT_PREPARE);
            factureBase.update(transaction);

            if (factureBase.hasErrors()) {
                throw new Exception(factureBase.getErrors().toString());
            }
        }
    }

    /**
     * Contrôle des paiements et des escomptes par rapport au montant de la facture. Si le montant du paiement est
     * modifié et que les totaux sont inférieur à la facture cette dernière est remise en état comptabilisé. <br/>
     * Nécessaire afin de gérer les factures payées en plusieurs fois.
     * 
     * @param session
     * @param transaction
     * @param paiement
     * @throws Exception
     */
    public static void updateFactureBase(BISession session, BTransaction transaction, LXPaiementViewBean paiement)
            throws Exception {
        LXOperation factureBase = LXPaiementUtils.getFactureBase(session, transaction, paiement.getIdOperationSrc());

        FWCurrency base = new FWCurrency(factureBase.getMontant());

        LXOperationManager manager = LXPaiementUtils.getOtherPaiementEscompte(session, paiement.getIdOperationSrc(),
                paiement.getIdOperation());

        FWCurrency pmtAndEscompte = new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).toString());
        pmtAndEscompte.negate();
        pmtAndEscompte.add(paiement.getMontant());

        if (pmtAndEscompte.isZero() || manager.hasErrors()) {
            throw new Exception(((BSession) session).getLabel("SOMME_PAIEMENTS_ESCOMPTE_ZERO"));
        }

        LXPaiementUtils.updateEtatFactureBase(session, transaction, factureBase, base, pmtAndEscompte);
    }

    /**
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void validate(BISession session, BTransaction transaction, LXPaiementViewBean paiement)
            throws Exception {

        if (JadeStringUtil.isBlank(paiement.getIdOrdreGroupe())
                || JadeStringUtil.isIntegerEmpty(paiement.getIdOrdreGroupe())) {
            throw new Exception(((BSession) session).getLabel("VAL_ORDRE_GROUPE_INCONNU"));
        }

        // TODO SCO : controle de l'ordre groupé dans un état particulier ?

        if (JadeStringUtil.isBlank(paiement.getIdSociete()) || JadeStringUtil.isIntegerEmpty(paiement.getIdSociete())) {
            throw new Exception(((BSession) session).getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }

        if (JadeStringUtil.isBlank(paiement.getIdFournisseur())
                || JadeStringUtil.isIntegerEmpty(paiement.getIdFournisseur())) {
            throw new Exception(((BSession) session).getLabel("VAL_IDENTIFIANT_FOURNISSEUR"));
        }

        LXApplication application = (LXApplication) GlobazServer.getCurrentSystem().getApplication(
                LXApplication.DEFAULT_APPLICATION_LYNX);
        application.getNumeroFactureFormatter().checkIdExterne(session, paiement.getIdExterne());

        if (JadeStringUtil.isBlank(paiement.getDateFacture())) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_DATE_FACTURE"));
        }

        if (JadeStringUtil.isBlank(paiement.getLibelle())) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_LIBELLE"));
        }

        if (JadeStringUtil.isBlank(paiement.getMontant())
                || JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(paiement.getMontant()))) {
            throw new Exception(((BSession) session).getLabel("VALIDATE_MONTANT"));
        }

        LXHelperUtils.testMinimumDebitCredit(session, paiement);
        LXHelperUtils.testTotalDebitCredit(session, paiement);
    }

    /**
     * Constructeur
     */
    protected LXPaiementUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
