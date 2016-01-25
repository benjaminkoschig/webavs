package globaz.helios.helpers.ecritures.detteavoir;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.avs.CGSecteurAVS;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureListViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;

public class CGGestionEcritureDetteAvoir {

    private static final String COMPTE_BEGIN_12 = "12";
    private static final String COMPTE_BEGIN_22 = "22";
    private static final String IDEXTERNE_1990_120 = "1990.120";
    private static final String IDEXTERNE_1990_220 = "1990.220";

    private static final String IDEXTERNE_END_1201_0000 = ".1201.0000";

    private static final String IDEXTERNE_END_2201_0000 = ".2201.0000";
    protected final static int LIBELLE_LENGTH = 50;

    private static final char SECTEUR_BEGIN_1 = '1';

    /**
     * Créer une nouvelle entete de dette et avoir.
     * 
     * @param session
     * @param transaction
     * @param ecriture
     * @param secteurBilan
     * @return
     * @throws Exception
     */
    private static CGEnteteEcritureViewBean createEnteteDetteAvoir(BSession session, BTransaction transaction,
            CGEcritureViewBean ecriture, String secteurBilan) throws Exception {
        CGEnteteEcritureViewBean entete = new CGEnteteEcritureViewBean();
        entete.setSession(session);

        entete.setLibelle(getFormattedLibelle(session.getLabel("ADD_ECRITURE_DETTE_AVOIR_LABEL_ENTETE") + " "
                + ecriture.getDate()));
        entete.setDate(ecriture.getDate());
        entete.setDateValeur(ecriture.getDateValeur());
        entete.setIdTypeEcriture(CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR);
        entete.setIdSecteurAvs(secteurBilan);
        entete.setIdJournal(ecriture.getIdJournal());

        entete.setNombreDoit("1");
        entete.setNombreAvoir("1");

        entete.add(transaction);

        if (entete.hasErrors() || entete.isNew()) {
            throw new Exception(session.getLabel("GESTION_ECRITURES_ENTETE_CREATION_PROBLEME"));
        }

        return entete;
    }

    /**
     * Format le libelle de dette et avoir d'une longueur max LIBELLE_SIZE;
     * 
     * @param libelle
     * @return
     */
    private static String getFormattedLibelle(String libelle) {
        if (libelle != null && libelle.length() <= LIBELLE_LENGTH) {
            return libelle;
        } else {
            return libelle.substring(0, LIBELLE_LENGTH - 1);
        }
    }

    /**
     * Retourne une nouvelle ecriture de dette et avoir (sans ajout db).
     * 
     * @param session
     * @param journal
     * @param ecriture
     * @param idExterne
     * @param idEntete
     * @param wantDebit
     * @return
     * @throws Exception
     */
    private static CGEcritureViewBean getNewEcritureDetteAvoir(BSession session, CGJournal journal,
            CGEcritureViewBean ecriture, String idExterne, String idEntete, boolean wantDebit) throws Exception {
        CGEcritureViewBean newEcritureDetteAvoir = new CGEcritureViewBean();
        newEcritureDetteAvoir.setSession(session);

        newEcritureDetteAvoir.setIdEnteteEcriture(idEntete);
        newEcritureDetteAvoir.setIdJournal(ecriture.getIdJournal());
        newEcritureDetteAvoir.setIdExerciceComptable(journal.getIdExerciceComptable());
        newEcritureDetteAvoir.setIdMandat(ecriture.getExerciceComptable().getIdMandat());

        newEcritureDetteAvoir.wantEstActive(new Boolean(true));

        newEcritureDetteAvoir.setDate(ecriture.getDate());
        newEcritureDetteAvoir.setDateValeur(ecriture.getDateValeur());

        if (wantDebit) {
            newEcritureDetteAvoir.setCodeDebitCredit(CodeSystem.CS_DEBIT);
        } else {
            newEcritureDetteAvoir.setCodeDebitCredit(CodeSystem.CS_CREDIT);
        }
        newEcritureDetteAvoir.setLibelle(getFormattedLibelle(session
                .getLabel("ADD_ECRITURE_DETTE_AVOIR_LABEL_ECRITURE_DEBIT") + " " + ecriture.getDate()));
        newEcritureDetteAvoir.setIdExterneCompte(idExterne);

        return newEcritureDetteAvoir;
    }

    /**
     * Retourne le secteur bilan pour l'id compte. Si idExterne n'est pas de secteur 1 alors retourne null.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param ecriture
     * @return
     * @throws Exception
     */
    private static String getSecteurBilan(BSession session, BTransaction transaction, CGJournal journal,
            CGEcritureViewBean ecriture) throws Exception {
        // Pas d'écriture de dette/avoir sur le secteur 1 et sur une écriture
        // qui est déjà elle même de type dette et avoir
        if (ecriture.getIdExterneCompte().charAt(0) == SECTEUR_BEGIN_1
                || ecriture.getIdExterneCompte().substring(5, 9).startsWith(COMPTE_BEGIN_12)
                || ecriture.getIdExterneCompte().substring(5, 9).startsWith(COMPTE_BEGIN_22)) {
            return null;
        }

        CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
        compte.setSession(session);
        compte.setIdCompte(ecriture.getIdCompte());
        compte.setIdExerciceComptable(journal.getIdExerciceComptable());
        compte.retrieve(transaction);

        if (compte.isNew()) {
            throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_1"));
        }

        CGSecteurAVS secteurAVS = new CGSecteurAVS();
        secteurAVS.setSession(session);
        secteurAVS.setIdSecteurAVS(compte.getIdSecteurAVS());
        secteurAVS.setIdMandat(journal.getExerciceComptable().getIdMandat());
        secteurAVS.retrieve(transaction);

        if (secteurAVS.isNew()) {
            throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_2"));
        }

        if (JadeStringUtil.isIntegerEmpty(secteurAVS.getIdSecteurBilan())) {
            throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_3") + " id = " + secteurAVS.getIdSecteurAVS());
        }

        return secteurAVS.getIdSecteurBilan();
    }

    /**
     * Ajout/suprrime des dettes et avoir.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param ecriture
     * @param addMode
     * @throws Exception
     */
    public static void manageDetteAvoir(BSession session, BTransaction transaction, CGJournal journal,
            CGEcritureViewBean ecriture, boolean addMode) throws Exception {
        if (!journal.getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()
                || CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR.equals(ecriture.getEntete().getIdTypeEcriture())) {
            return;
        }

        String secteurBilan = getSecteurBilan(session, transaction, journal, ecriture);
        if (secteurBilan == null) {
            return;
        }

        CGEnteteEcritureViewBean entete = new CGEnteteEcritureViewBean();
        CGEcritureViewBean ecritureDebit = new CGEcritureViewBean();
        CGEcritureViewBean ecritureCredit = new CGEcritureViewBean();

        String compteDebitType1 = secteurBilan + IDEXTERNE_END_1201_0000;
        String compteCreditType1 = IDEXTERNE_1990_220 + secteurBilan.charAt(0) + "." + secteurBilan.charAt(1)
                + secteurBilan.charAt(2) + secteurBilan.charAt(3) + "0";

        String compteDebitType2 = IDEXTERNE_1990_120 + secteurBilan.charAt(0) + "." + secteurBilan.charAt(1)
                + secteurBilan.charAt(2) + secteurBilan.charAt(3) + "0";
        String compteCreditType2 = secteurBilan + IDEXTERNE_END_2201_0000;

        CGEnteteEcritureListViewBean manager = new CGEnteteEcritureListViewBean();
        manager.setSession(session);
        manager.setForIdJournal(journal.getIdJournal());
        manager.setForIdSecteurAVS(secteurBilan);
        manager.setForIdTypeEcriture(CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR);
        manager.setForDate(ecriture.getDate());
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.size() > 1) {
            throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_4"));
        } else if (manager.isEmpty()) {
            entete = createEnteteDetteAvoir(session, transaction, ecriture, secteurBilan);
            ecritureDebit = getNewEcritureDetteAvoir(session, journal, ecriture, compteDebitType2,
                    entete.getIdEnteteEcriture(), true);
            ecritureCredit = getNewEcritureDetteAvoir(session, journal, ecriture, compteCreditType2,
                    entete.getIdEnteteEcriture(), false);

            setMontantEcrituresDebitCredit(session, transaction, ecriture, ecritureDebit, ecritureCredit,
                    compteDebitType1, compteCreditType1, compteDebitType2, compteCreditType2, addMode);

            ecritureDebit.add(transaction);
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString() + " " + ecritureDebit.getIdExterneCompte());
            }

            ecritureCredit.add(transaction);
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString() + " " + ecritureCredit.getIdExterneCompte());
            }

            entete.setTotalDoit(ecritureDebit.getMontantBase());
            entete.setTotalAvoir(ecritureCredit.getMontantBase());

            entete.setIdContrepartieDoit(ecritureDebit.getIdEcriture());
            entete.setIdContrepartieAvoir(ecritureCredit.getIdEcriture());

            entete.update(transaction);

            CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, journal, ecritureDebit);
            CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, journal, ecritureCredit);
        } else {
            entete = (CGEnteteEcritureViewBean) manager.getFirstEntity();

            if (JadeStringUtil.isIntegerEmpty(entete.getIdContrepartieAvoir())) {
                throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_12"));
            }
            if (JadeStringUtil.isIntegerEmpty(entete.getIdContrepartieDoit())) {
                throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_13"));
            }

            ecritureDebit.setSession(session);
            ecritureDebit.setIdEcriture(entete.getIdContrepartieDoit());
            ecritureDebit.retrieve(transaction);

            if (ecritureDebit.hasErrors() || ecritureDebit.isNew()) {
                throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_5"));
            }

            ecritureCredit.setSession(session);
            ecritureCredit.setIdEcriture(entete.getIdContrepartieAvoir());
            ecritureCredit.retrieve(transaction);

            if (ecritureCredit.hasErrors() || ecritureCredit.isNew()) {
                throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_7"));
            }

            String tmpIdCompteDebit = ecritureDebit.getIdCompte();
            String tmpIdCompteCrebit = ecritureCredit.getIdCompte();

            setMontantEcrituresDebitCredit(session, transaction, ecriture, ecritureDebit, ecritureCredit,
                    compteDebitType1, compteCreditType1, compteDebitType2, compteCreditType2, addMode);

            if (new FWCurrency(ecritureDebit.getMontantBase()).isZero()
                    && new FWCurrency(ecritureCredit.getMontantBase()).isZero()) {
                ecritureDebit.delete(transaction);
                ecritureCredit.delete(transaction);

                entete.delete(transaction);
            } else {
                if ((JadeStringUtil.isIntegerEmpty(ecritureDebit.getIdCompte()))
                        || (JadeStringUtil.isIntegerEmpty(ecritureCredit.getIdCompte()))) {
                    ecritureDebit.setIdCompte(tmpIdCompteDebit);
                    ecritureCredit.setIdCompte(tmpIdCompteCrebit);

                    String tmpMontantDebit = ecritureDebit.getMontantBase();
                    String tmpMontantCrebit = ecritureCredit.getMontantBase();

                    ecritureDebit.setMontantBase("0.00");
                    ecritureCredit.setMontantBase("0.00");

                    ecritureDebit.update(transaction);
                    ecritureCredit.update(transaction);

                    CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, journal, ecritureDebit);
                    CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, journal, ecritureCredit);

                    ecritureDebit.setIdCompte("");
                    ecritureCredit.setIdCompte("");

                    ecritureDebit.setMontantBase(tmpMontantDebit);
                    ecritureCredit.setMontantBase(tmpMontantCrebit);
                }

                ecritureDebit.update(transaction);
                ecritureCredit.update(transaction);

                entete.setTotalDoit(ecritureDebit.getMontantBase());
                entete.setTotalAvoir(ecritureCredit.getMontantBase());

                entete.update(transaction);
            }

            CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, journal, ecritureDebit);
            CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, journal, ecritureCredit);
        }
    }

    /**
     * Assigne le montant de débit et de crédit pour les écritures dette et avoir.
     * 
     * @param session
     * @param transaction
     * @param ecriture
     * @param ecritureDebit
     * @param ecritureCredit
     * @param compteDebitType1
     * @param compteCreditType1
     * @param compteDebitType2
     * @param compteCreditType2
     * @param addMode
     * @throws Exception
     */
    private static void setMontantEcrituresDebitCredit(BSession session, BTransaction transaction,
            CGEcritureViewBean ecriture, CGEcritureViewBean ecritureDebit, CGEcritureViewBean ecritureCredit,
            String compteDebitType1, String compteCreditType1, String compteDebitType2, String compteCreditType2,
            boolean addMode) throws Exception {
        FWCurrency montantEcriDebit = new FWCurrency(ecritureDebit.getMontantBase());
        FWCurrency montantEcriCredit = new FWCurrency(ecritureCredit.getMontantBase());
        FWCurrency montantTemp = new FWCurrency(montantEcriDebit.toString());
        montantTemp.add(montantEcriCredit);
        if (!montantTemp.isZero()) {
            throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_10"));
        }

        if (JadeStringUtil.isBlank(ecritureDebit.getIdExterneCompte())) {
            ecritureDebit.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(session, ecritureDebit));
        }

        if (JadeStringUtil.isBlank(ecritureCredit.getIdExterneCompte())) {
            ecritureCredit.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(session, ecritureCredit));
        }

        FWCurrency montant = new FWCurrency(ecriture.getMontantBase());
        if (ecritureDebit.getIdExterneCompte().equals(compteDebitType1)
                && ecritureCredit.getIdExterneCompte().equals(compteCreditType1)) {
            if (addMode) {
                montantEcriDebit.sub(montant);
                montantEcriCredit.add(montant);
            } else {
                montantEcriDebit.add(montant);
                montantEcriCredit.sub(montant);
            }

            if (montantEcriDebit.isNegative()) {
                montantEcriDebit.negate();
                montantEcriCredit.negate();
                // On force la récupération de l'ID du nouveau compte en mettant
                // à vide idCompte et en précisant le numéro externe
                ecritureDebit.setIdCompte("");
                ecritureDebit.setIdExterneCompte(compteDebitType2);
                // idem
                ecritureCredit.setIdCompte("");
                ecritureCredit.setIdExterneCompte(compteCreditType2);
            }
        } else if (ecritureDebit.getIdExterneCompte().equals(compteDebitType2)
                && ecritureCredit.getIdExterneCompte().equals(compteCreditType2)) {
            if (addMode) {
                montantEcriDebit.add(montant);
                montantEcriCredit.sub(montant);
            } else {
                montantEcriDebit.sub(montant);
                montantEcriCredit.add(montant);
            }

            if (montantEcriDebit.isNegative()) {
                montantEcriDebit.negate();
                montantEcriCredit.negate();
                // On force la récupération de l'ID du nouveau compte en mettant
                // à vide idCompte et en précisant le numéro externe
                ecritureDebit.setIdCompte("");
                ecritureDebit.setIdExterneCompte(compteDebitType1);
                // idem
                ecritureCredit.setIdCompte("");
                ecritureCredit.setIdExterneCompte(compteCreditType1);
            }
        } else {
            throw new Exception(session.getLabel("JOURNAL_ADD_DA_ERROR_11"));
        }

        ecritureDebit.setMontantBase(montantEcriDebit.toString());
        ecritureCredit.setMontantBase(montantEcriCredit.toString());
    }

}
