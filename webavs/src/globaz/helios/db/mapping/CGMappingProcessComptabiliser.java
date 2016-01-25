package globaz.helios.db.mapping;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGJournalManager;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * @author dda
 * 
 */
public class CGMappingProcessComptabiliser {

    private static final String LABEL_INFO_MAPPING = "[Mapping]";

    private static final String PREFIX_REFERENCE_EXTERNE = "MAPJRNSRC";
    private static final String PROPERTY_USE_MAPPING_COMPTABILISATION = "useMappingComptabilisation";

    /**
     * Set l'état des journaux générés de destination a comptabilisé.
     * 
     * @param session
     * @param transaction
     * @param journalSource
     * @throws Exception
     */
    public static void comptabiliserJournauxDestination(BSession session, BTransaction transaction,
            CGJournal journalSource) throws Exception {
        CGMappingProcessComptabiliser process = new CGMappingProcessComptabiliser(session, transaction, journalSource,
                null);

        if (process.useMappingComptabilisation()) {
            CGJournalManager manager = process.initJournalDestinationManager(null);

            manager.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                CGJournal journalDestination = (CGJournal) manager.get(i);
                journalDestination.comptabiliser(transaction);
                if (transaction.hasErrors()) {
                    throw new Exception(LABEL_INFO_MAPPING + " " + session.getLabel("COMPTABILISER_JOURNAL_ERROR_2"));
                }
            }
        }
    }

    /**
     * Main method de l'éxécution du Mapping de comptabilisation pour une écriture. <br/>
     * 1. Check si la fonction est activée dans les properties <br/>
     * 2. Retrouve tout les mappings pour le compte source et itère <br/>
     * 3. Ajout l'écriture double sur le compte de destination (directement comptabilisée) <br/>
     * 4. Set l'état du journal de destination a comptabilisé
     * 
     * @param session
     * @param transaction
     * @param journalSource
     * @param ecritureSource
     * @throws Exception
     */
    public static void execute(BSession session, BTransaction transaction, CGJournal journalSource,
            CGEcritureViewBean ecritureSource) throws Exception {
        CGMappingProcessComptabiliser process = new CGMappingProcessComptabiliser(session, transaction, journalSource,
                ecritureSource);

        if (process.useMappingComptabilisation()) {

            CGMappingComptabiliserManager manager = new CGMappingComptabiliserManager();
            manager.setSession(session);

            manager.setForIdCompteSource(ecritureSource.getIdCompte());
            manager.setForIdMandatSource(ecritureSource.getIdMandat());

            if (!JadeStringUtil.isIntegerEmpty(ecritureSource.getIdCentreCharge())) {
                manager.setForIdCentreChargeSource(ecritureSource.getIdCentreCharge());
            } else {
                manager.setForIdCentreChargeSource(CGMappingComptabiliserManager.AUCUN_CENTRE_CHARGE);
            }

            manager.find(BManager.SIZE_NOLIMIT);

            if (!manager.isEmpty()) {
                // Parcourir les écritures (sauf écriture en cours) et
                // déterminer
                // S'il existe un compte de type clôture/résultat ou ouverture
                // Ne pas déclencher le mapping dans ce cas
                CGEcritureListViewBean listEcritures = new CGEcritureListViewBean();
                listEcritures.setSession(session);
                listEcritures.setForIdEnteteEcriture(ecritureSource.getIdEnteteEcriture());
                listEcritures.find();
                for (int i = 0; i < listEcritures.size(); i++) {
                    CGEcritureViewBean ecr = (CGEcritureViewBean) listEcritures.get(i);
                    if (!ecr.getIdEcriture().equals(ecritureSource.getIdEcriture())) {
                        // Fin si la contrepartie est une écriture double
                        // (multiple -> accepté d'office)
                        CGPlanComptableManager mgrPlan = new CGPlanComptableManager();
                        mgrPlan.setSession(session);
                        mgrPlan.setForIdCompte(ecr.getIdCompte());
                        mgrPlan.setForIdExerciceComptable(ecr.getIdExerciceComptable());
                        mgrPlan.find();
                        if (mgrPlan.isEmpty()) {
                            return;
                        }
                        CGPlanComptableViewBean plan = (CGPlanComptableViewBean) mgrPlan.getFirstEntity();
                        // Sortir si la contrepartie est un compte d'ouverture,
                        // clôture ou résultat
                        if (CGCompte.CS_GENRE_CLOTURE.equals(plan.getIdGenre())
                                || CGCompte.CS_GENRE_OUVERTURE.equals(plan.getIdGenre())
                                || CGCompte.CS_GENRE_RESULTAT.equals(plan.getIdGenre())) {
                            return;
                        }
                    }
                }
            }
            for (int i = 0; i < manager.size(); i++) {
                CGMappingComptabiliser mapping = (CGMappingComptabiliser) manager.get(i);

                process.addEcritureDoubleDestination(mapping);
            }
        }
    }

    private CGEcritureViewBean ecritureSource;
    private CGJournal journalSource;

    private BSession session;

    private BTransaction transaction;

    /**
     * Constructeur.
     * 
     * @param session
     * @param transaction
     * @param journalSource
     * @param ecritureSource
     */
    public CGMappingProcessComptabiliser(BSession session, BTransaction transaction, CGJournal journalSource,
            CGEcritureViewBean ecritureSource) {
        this.session = session;
        this.transaction = transaction;
        this.journalSource = journalSource;
        this.ecritureSource = ecritureSource;
    }

    /**
     * Ajout l'écriture double sur le compte de destination (directement comptabilisée).
     * 
     * @param mapping
     * @throws Exception
     */
    private void addEcritureDoubleDestination(CGMappingComptabiliser mapping) throws Exception {
        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(session);

        CGExerciceComptable exerciceComptableDestination = getExerciceComptableDestination(mapping
                .getIdMandatDestination());
        CGJournal journalDestination = getJournalDestination(exerciceComptableDestination.getIdExerciceComptable());
        ecritures.setIdJournal(journalDestination.getIdJournal());
        ecritures.setDateValeur(ecritureSource.getDateValeur());
        ecritures.setPiece(ecritureSource.getPiece());
        ecritures.setRemarque(ecritureSource.getRemarque());

        ArrayList ecrituresList = new ArrayList();

        CGEcritureViewBean ecritureCrebit = new CGEcritureViewBean();
        CGEcritureViewBean ecritureDebit = new CGEcritureViewBean();

        ecritureCrebit.setLibelle(ecritureSource.getLibelle());
        ecritureDebit.setLibelle(ecritureSource.getLibelle());

        FWCurrency montant = new FWCurrency(ecritureSource.getMontantBase());
        if (montant.isPositive()) {
            ecritureCrebit.setIdCompte(mapping.getIdContreEcritureDestination());
            ecritureDebit.setIdCompte(mapping.getIdCompteDestination());

            if (!JadeStringUtil.isIntegerEmpty(mapping.getIdCentreChargeDestination())) {
                ecritureDebit.setIdCentreCharge(mapping.getIdCentreChargeDestination());
            }
        } else {
            montant.negate();
            ecritureCrebit.setIdCompte(mapping.getIdCompteDestination());
            ecritureDebit.setIdCompte(mapping.getIdContreEcritureDestination());

            if (!JadeStringUtil.isIntegerEmpty(mapping.getIdCentreChargeDestination())) {
                ecritureCrebit.setIdCentreCharge(mapping.getIdCentreChargeDestination());
            }
        }

        ecritureCrebit.setCodeDebitCredit(CodeSystem.CS_CREDIT);
        ecritureCrebit.setMontant(montant.toString());

        ecritureDebit.setCodeDebitCredit(CodeSystem.CS_DEBIT);
        ecritureDebit.setMontant(montant.toString());

        if (!JadeStringUtil.isDecimalEmpty(ecritureSource.getCoursMonnaie())) {
            ecritureCrebit.setCoursMonnaie(ecritureSource.getCoursMonnaie());
            ecritureDebit.setCoursMonnaie(ecritureSource.getCoursMonnaie());
        }

        if (!JadeStringUtil.isDecimalEmpty(ecritureSource.getMontantBaseMonnaie())) {
            FWCurrency montantEtranger = new FWCurrency(ecritureSource.getMontantBaseMonnaie());
            ecritureCrebit.setMontantMonnaie(montantEtranger.toString());
            montantEtranger.negate();
            ecritureDebit.setMontantMonnaie(montantEtranger.toString());
        }

        ecritureCrebit.setIdExerciceComptable(exerciceComptableDestination.getIdExerciceComptable());
        ecritureDebit.setIdExerciceComptable(exerciceComptableDestination.getIdExerciceComptable());
        ecritureCrebit.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(session, ecritureCrebit));
        ecritureDebit.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(session, ecritureDebit));

        ecrituresList.add(ecritureCrebit);
        ecrituresList.add(ecritureDebit);

        ecritures.setEcritures(ecrituresList);

        CGGestionEcritureAdd.addEcritures(session, transaction, ecritures, true);
    }

    /**
     * Return l'exercice comptable de destination.
     * 
     * @param idMandatDestination
     * @return
     * @throws Exception
     */
    private CGExerciceComptable getExerciceComptableDestination(String idMandatDestination) throws Exception {
        CGExerciceComptableManager manager = new CGExerciceComptableManager();
        manager.setSession(session);

        manager.setForIdMandat(idMandatDestination);
        manager.setForExerciceOuvert(new Boolean(true));
        manager.setBetweenDateDebutDateFin(journalSource.getDateValeur());

        manager.find(transaction);

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(LABEL_INFO_MAPPING + " " + session.getLabel("NEED_EXERCICE_COMPTABLE_ERREUR")
                    + " [idMandat = " + idMandatDestination + "]");
        }

        return (CGExerciceComptable) manager.getFirstEntity();
    }

    /**
     * Return le journal de destination. <br/>
     * 1. Check si le journal n'éxiste pas déjà en tentant de le résoudre par la clef alternée sur la reference externe
     * composée de la constante PREFIX_REFERENCE_EXTERNE + l'id du journal source. <br/>
     * 2. Si le journal n'est pas résolu => le journal sera créé avec les informations du journal source.
     * 
     * @param mapping
     * @return
     * @throws Exception
     */
    public CGJournal getJournalDestination(String idExerciceComptableDestination) throws Exception {
        CGJournalManager manager = initJournalDestinationManager(idExerciceComptableDestination);

        manager.find(transaction);

        if (manager.hasErrors()) {
            throw new Exception(LABEL_INFO_MAPPING + " " + session.getLabel("JOURNAL_ERROR_1"));
        }

        if (manager.isEmpty()) {
            CGJournal journalDestination = new CGJournal();
            journalDestination.setSession(session);

            journalDestination.setReferenceExterne(PREFIX_REFERENCE_EXTERNE + journalSource.getIdJournal());
            journalDestination.setIdExerciceComptable(idExerciceComptableDestination);

            journalDestination.setDate(journalSource.getDate());
            journalDestination.setDateValeur(journalSource.getDateValeur());
            journalDestination.setLibelle(journalSource.getLibelle());

            journalDestination.setIdEtat(ICGJournal.CS_ETAT_OUVERT);
            journalDestination.setIdPeriodeComptable(getPeriodeComptableDestination(idExerciceComptableDestination)
                    .getIdPeriodeComptable());
            journalDestination.setIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);
            journalDestination.setEstPublic(new Boolean(false));
            journalDestination.setEstConfidentiel(new Boolean(false));
            journalDestination.setProprietaire(session.getUserId());

            journalDestination.add(transaction);

            return journalDestination;
        } else {
            return (CGJournal) manager.getFirstEntity();
        }
    }

    /**
     * Return la période comptable de destination (utilisé pour la création du journal de destination).
     * 
     * @param mapping
     * @return
     * @throws Exception
     */
    private CGPeriodeComptable getPeriodeComptableDestination(String idExerciceComptableDestination) throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(session);

        manager.setForIdExerciceComptable(idExerciceComptableDestination);
        manager.setForDateInPeriode(journalSource.getDateValeur());
        manager.setForPeriodeOuverte(true);

        manager.find(transaction);

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(LABEL_INFO_MAPPING + " " + session.getLabel("NO_PERIODE_OUVERTE_POUR_DATE"));
        }

        return (CGPeriodeComptable) manager.getFirstEntity();
    }

    /**
     * Initialise le manager de recherche des journaux de destination.
     * 
     * @param idExerciceComptableDestination
     *            Si null => Inter mandat.
     * @return
     */
    private CGJournalManager initJournalDestinationManager(String idExerciceComptableDestination) {
        CGJournalManager manager = new CGJournalManager();
        manager.setSession(session);

        if (!JadeStringUtil.isIntegerEmpty(idExerciceComptableDestination)) {
            manager.setForIdExerciceComptable(idExerciceComptableDestination);
        }

        manager.setForReferenceExterne(PREFIX_REFERENCE_EXTERNE + journalSource.getIdJournal());
        manager.setForIdEtat(ICGJournal.CS_ETAT_OUVERT);
        manager.setForIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);

        manager.setOrderby(CGJournal.FIELD_DATEVALEUR);
        return manager;
    }

    /**
     * La fonction de mapping comptabilisation est-elle activée dans les properties d'HELIOS ?
     * 
     * @return
     * @throws Exception
     */
    public boolean useMappingComptabilisation() throws Exception {
        return new Boolean(session.getApplication().getProperty(PROPERTY_USE_MAPPING_COMPTABILISATION)).booleanValue();
    }

}
