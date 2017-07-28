package globaz.osiris.externe.rentes;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIGestionRentesExterne;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.journal.operation.CAUpdateOperationOrdreVersementInEtatVerse;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.sepa.utils.CASepaCommonUtils;
import globaz.osiris.db.rentes.check.montantpargenre.CARentesCheckMontantParGenre;
import globaz.osiris.db.rentes.check.montantpargenre.CARentesCheckMontantParGenreManager;
import globaz.osiris.db.rentes.check.operation.CARentesCheckOperationManager;
import globaz.osiris.db.rentes.check.section.CARentesCheckSectionManager;
import globaz.osiris.db.rentes.operation.ecriture.CARentesEcriture;
import globaz.osiris.db.rentes.operation.ordreversement.CARentesOperationOrdreVersement;
import globaz.osiris.db.rentes.operation.ordreversement.CARentesOrdreVersement;
import globaz.osiris.db.rentes.operation.versement.CARentesVersement;
import globaz.osiris.db.rentes.section.CARentesSection;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import globaz.osiris.process.journal.CAUtilsJournal;
import globaz.osiris.utils.CAUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ch.globaz.osiris.business.constantes.CAProperties;

/**
 * Class permettant l'ajout d'opération ACTIVEE et d'ordre de versement PREPARE.<br/>
 * Uniquement utilisé par le module corvus.
 * 
 * @author DDA
 */
public class CAGestionRentesExterne implements APIGestionRentesExterne {

    int countVersement = 0;

    private CARentesEcriture ecriture;
    private CAJournal journal = null;

    private CAJournal journalVersement = null;

    private CARentesOperationOrdreVersement operationOrdreVersement;

    private CAOrdreGroupe ordreGroupe = null;
    private CARentesOrdreVersement ordreVersement;

    private CARentesSection section;

    FWCurrency totalVersement = new FWCurrency();
    private CARentesVersement versement;
    private long maxOVforThisOG = Long.MAX_VALUE;
    private List<String> listOg = new ArrayList<String>();

    /**
     * @see {@link APIGestionRentesExterne#addEcriture(BSession, BTransaction, String, String, String, String, String, String, String, String)}
     */
    @Override
    public void addEcriture(BSession session, BTransaction transaction, String idOperation, String idCompteAnnexe,
            String idSection, String idRubrique, String idCompteCourant, String montant, String date, String libelle)
            throws Exception {
        if ((journal == null) || journal.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        if (ecriture == null) {
            ecriture = new CARentesEcriture(transaction);
        }

        ecriture.setIdOperation(idOperation);
        ecriture.setIdCompteAnnexe(idCompteAnnexe);
        ecriture.setIdSection(idSection);
        ecriture.setIdRubrique(idRubrique);
        ecriture.setIdCompteCourant(idCompteCourant);
        ecriture.setMontant(montant);
        ecriture.setDate(date);
        ecriture.setLibelle(libelle);
        ecriture.setIdJournal(journal.getIdJournal());

        ecriture.fillVariables();
        ecriture.executeQuery(transaction);
    }

    /**
     * see
     * {@link APIGestionRentesExterne#addVersement(BSession, BTransaction, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     */
    @Override
    public void addVersement(BSession session, BTransaction transaction, String idOperation, String idCompteAnnexe,
            String idSection, String idRubrique, String idCompteCourant, String montant, String date, String libelle,
            String idOperationVersement, String idAdressePaiement, String idOrganeExecution, String motif,
            String nomCache) throws Exception {
        if ((journal == null) || journal.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        if ((journalVersement == null) || journalVersement.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        if ((ordreGroupe == null) || ordreGroupe.isNew()) {
            throw new Exception(session.getLabel("5200"));
        }

        if (operationOrdreVersement == null) {
            operationOrdreVersement = new CARentesOperationOrdreVersement(transaction);
        }

        if (ordreVersement == null) {
            ordreVersement = new CARentesOrdreVersement(transaction);
        }

        if (versement == null) {
            versement = new CARentesVersement(transaction);
        }

        countVersement++;
        totalVersement.add(montant);

        operationOrdreVersement.setIdOperation(idOperation);
        operationOrdreVersement.setIdCompteAnnexe(idCompteAnnexe);
        operationOrdreVersement.setIdSection(idSection);
        operationOrdreVersement.setDate(date);
        operationOrdreVersement.setMontant(montant);

        operationOrdreVersement.setIdJournal(journal.getIdJournal());

        operationOrdreVersement.fillVariables();
        operationOrdreVersement.executeQuery(transaction);

        ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setIdOrdre(idOperation);
        ordreVersement.setIdOrdreGroupe(ordreGroupe.getIdOrdreGroupe());
        ordreVersement.setIdOrganeExecution(idOrganeExecution);
        ordreVersement.setMotif(motif);
        ordreVersement.setNomCache(nomCache);
        ordreVersement.setNumeroTransaction("" + countVersement);
        ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_RENTES_AVS_AI);

        ordreVersement.fillVariables();
        ordreVersement.executeQuery(transaction);

        versement.setIdOperation(idOperationVersement);
        versement.setIdCompteAnnexe(idCompteAnnexe);
        versement.setIdSection(idSection);
        versement.setIdRubrique(idRubrique);
        versement.setIdCompteCourant(idCompteCourant);
        versement.setMontant(montant);
        versement.setDate(date);
        versement.setLibelle(libelle);

        versement.setIdOrdreVersement(idOperation);

        versement.setIdJournal(journalVersement.getIdJournal());

        versement.fillVariables();
        versement.executeQuery(transaction);

        // Multi OG
        processMaxOvLimitControl(session, transaction);

    }

    /**
     * check if the current OG reached the max amount of OV recommended and create a new one in this case
     * 
     * @throws Exception
     */
    private void processMaxOvLimitControl(BSession session, BTransaction transaction) throws Exception {
        if (countVersement == maxOVforThisOG) {

            closeUpdateOG(session, transaction);
            totalVersement = new FWCurrency();
            countVersement = 0;

            prepareForNext(session, transaction);

            listOg.add(ordreGroupe.getIsoNumLivraison());
        }
    }

    /**
     * use by attacherOrdre for case where number of OV to link to OG is higher than specified property nbmax.ovparog
     * 
     * @param transaction current transaction
     * @throws Exception exception can occur during transaction attachement to the new entity
     */
    private void prepareForNext(BSession session, BTransaction transaction) throws Exception {

        if ((1 + listOg.size()) >= Integer.parseInt(CAProperties.ISO_SEPA_MAX_MULTIOG.getValue())) {
            throw new Exception(session.getLabel("SEPA_PREPARATION_MAX_MULTI_OG"));
        }
        // Ajout d'un indicatif d'OG multiple
        String suffixMultiOG = "";
        if (!listOg.isEmpty()) {
            suffixMultiOG = "(" + listOg.size() + 1 + ") ";
        }

        CAOrdreGroupe newOrdreGroupe = new CAOrdreGroupe();
        newOrdreGroupe.setSession(session);
        newOrdreGroupe.setIdOrganeExecution(ordreGroupe.getIdOrganeExecution());
        newOrdreGroupe.setNumeroOG(ordreGroupe.getNumeroOG());
        newOrdreGroupe.setDateEcheance(ordreGroupe.getDateEcheance());
        newOrdreGroupe.setTypeOrdreGroupe(ordreGroupe.getTypeOrdreGroupe());
        newOrdreGroupe.setNatureOrdresLivres(ordreGroupe.getNatureOrdresLivres());
        newOrdreGroupe.setIsoGestionnaire(ordreGroupe.getIsoGestionnaire());
        newOrdreGroupe.setIsoHighPriority(ordreGroupe.getIsoHighPriority());
        newOrdreGroupe.setMotif(ordreGroupe.getMotif() + suffixMultiOG);

        newOrdreGroupe.setDateCreation(JACalendar.todayJJsMMsAAAA());
        newOrdreGroupe.setEtat(CAOrdreGroupe.TRAITEMENT);
        newOrdreGroupe.setIdJournal(journalVersement.getIdJournal());

        try {
            newOrdreGroupe.add(transaction);

            if (newOrdreGroupe.hasErrors()) {
                throw new Exception(session.getLabel("POG_ADD_ORDRE_GROUPE"));
            }

        } catch (Exception e) {
            throw new Exception(session.getLabel("5002"), e);
        }
        ordreGroupe = newOrdreGroupe;

    }

    /**
     * @see APIGestionRentesExterne#checkIntegrity(BSession, BTransaction)
     */
    @Override
    public void checkIntegrity(BSession session, BTransaction transaction, HashMap rentes) throws Exception {
        if ((journal == null) || journal.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        if ((journalVersement == null) || journalVersement.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        checkOperationIntegrity(session, transaction);
        checkSectionIntegrity(session, transaction);
        checkMontantParGenreIntegrity(session, transaction, rentes);
    }

    /**
     * Compare les montants des rentes par rubrique avec les montants par rubrique insérés en comptabilité auxiliaire.
     * 
     * @param session
     * @param transaction
     * @param rentes
     * @throws Exception
     */
    private void checkMontantParGenreIntegrity(BSession session, BTransaction transaction, HashMap rentes)
            throws Exception {
        CARentesCheckMontantParGenreManager manager = new CARentesCheckMontantParGenreManager();
        manager.setSession(session);

        ArrayList tmp = new ArrayList();
        tmp.add(journal.getIdJournal());
        tmp.add(journalVersement.getIdJournal());
        manager.setForIdJournalIn(tmp);

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.isEmpty() || manager.hasErrors()) {
            throw new Exception(session.getLabel("RENTES_MONTANT_NON_VALIDE"));
        }

        for (int i = 0; i < manager.size(); i++) {
            CARentesCheckMontantParGenre renteCheck = (CARentesCheckMontantParGenre) manager.get(i);

            FWCurrency c1 = new FWCurrency(renteCheck.getMontant());
            FWCurrency c2 = new FWCurrency((String) rentes.get(renteCheck.getIdRubrique()));

            if (c1.compareTo(c2) != 0) {
                throw new Exception(session.getLabel("RENTES_MONTANT_NON_VALIDE") + " " + renteCheck.getIdExterne()
                        + "/" + renteCheck.getMontant());
            }
        }
    }

    /**
     * Contrôle la somme des opération touchés par les journaux des rentes.
     * 
     * @param session
     * @param transaction
     * @throws Exception
     */
    private void checkOperationIntegrity(BSession session, BTransaction transaction) throws Exception {
        CARentesCheckOperationManager manager = new CARentesCheckOperationManager();
        manager.setSession(session);

        ArrayList tmp = new ArrayList();
        tmp.add(journal.getIdJournal());
        tmp.add(journalVersement.getIdJournal());
        manager.setForIdJournalIn(tmp);

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.isEmpty() || manager.hasErrors()) {
            throw new Exception(session.getLabel("RENTES_MONTANT_OPERATIONS_NON_VALIDE"));
        }

        CAOperation op = (CAOperation) manager.getFirstEntity();
        if (!(new FWCurrency(op.getMontant())).isZero()) {
            throw new Exception(session.getLabel("RENTES_MONTANT_OPERATIONS_NON_VALIDE") + " " + op.getMontant());
        }
    }

    /**
     * Contrôle la somme des soldes, bases et pmtcpt des sections touchés par les journaux des rentes.
     * 
     * @param session
     * @param transaction
     * @throws Exception
     */
    public void checkSectionIntegrity(BSession session, BTransaction transaction) throws Exception {
        CARentesCheckSectionManager manager = new CARentesCheckSectionManager();
        manager.setSession(session);

        ArrayList tmp = new ArrayList();
        tmp.add(journal.getIdJournal());
        tmp.add(journalVersement.getIdJournal());
        manager.setForIdJournalIn(tmp);

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.isEmpty() || manager.hasErrors()) {
            throw new Exception(session.getLabel("ANNULATIONSOLDE_VIDE"));
        }

        CASection section = (CASection) manager.getFirstEntity();
        if (!(new FWCurrency(section.getSolde())).isZero()) {
            throw new Exception(session.getLabel("RENTES_SOLDE_SECTIONS_NON_ZERO") + " " + section.getSolde());
        }

        FWCurrency tmpPm = new FWCurrency(section.getPmtCmp());
        tmpPm.negate();
        if (new FWCurrency(section.getBase()).compareTo(tmpPm) == 0) {
            ;// OK
        } else {
            throw new Exception(session.getLabel("RENTES_BASE_SECTIONS_NON_ZERO") + " " + section.getBase());
        }
    }

    /**
     * @see {@link APIGestionRentesExterne#closeAllStatements()}
     */
    @Override
    public void closeAllStatements() throws Exception {
        if (ecriture != null) {
            ecriture.closeStatement();
        }

        if (operationOrdreVersement != null) {
            operationOrdreVersement.closeStatement();
        }

        if (ordreVersement != null) {
            ordreVersement.closeStatement();
        }

        if (versement != null) {
            versement.closeStatement();
        }

        if (section != null) {
            section.closeStatement();
        }
    }

    /**
     * @see {@link APIGestionRentesExterne#createJournal(BSession, BTransaction, String, String)}
     */
    @Override
    public void createJournal(BSession session, BTransaction transaction, String libelle, String dateValeur)
            throws Exception {
        if (!new CAUtilsJournal().isPeriodeComptableOuverte(session, transaction, dateValeur)) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (journal == null) {
            journal = createNewJournal(session, transaction, libelle, dateValeur);
        }
    }

    /**
     * Créer un nouveau journal.
     * 
     * @param session
     * @param transaction
     * @param libelle
     * @param dateValeur
     * @return
     * @throws Exception
     */
    private CAJournal createNewJournal(BSession session, BTransaction transaction, String libelle, String dateValeur)
            throws Exception {
        CAJournal jrn = new CAJournal();
        jrn.setSession(session);
        jrn.setLibelle(libelle);
        jrn.setDate(JACalendar.todayJJsMMsAAAA());
        jrn.setDateValeurCG(dateValeur);

        jrn.add(transaction);

        if (jrn.isNew() || jrn.hasErrors()) {
            throw new Exception(session.getLabel("5157"));
        }

        return jrn;
    }

    /**
     * Créer l'ordre groupé ainsi que le journal de destination pour les versements.
     * 
     * @param session
     * @param transaction
     * @param libelle
     * @param dateValeur
     * @param numeroOG
     * @return
     * @throws Exception
     */
    private CAOrdreGroupe createNewOrdreGroupe(BSession session, BTransaction transaction, String libelle,
            String dateValeur, String numeroOG, String idOrganeExecution) throws Exception {

        CAOrdreGroupe org = new CAOrdreGroupe();
        org.setSession(session);

        org.setDateCreation(JACalendar.todayJJsMMsAAAA());
        org.setDateEcheance(dateValeur);
        org.setMotif(libelle);
        org.setTypeOrdreGroupe(CAOrdreGroupe.VERSEMENT);
        org.setIdOrganeExecution(idOrganeExecution);
        org.setEtat(CAOrdreGroupe.TRAITEMENT);
        org.setIdJournal(journalVersement.getIdJournal());
        org.setNatureOrdresLivres(CAOrdreGroupe.NATURE_RENTES_AVS_AI);

        if (!JadeStringUtil.isBlank(numeroOG)) {
            org.setNumeroOG(numeroOG);
        }

        org.add(transaction);

        if (org.isNew() || org.hasErrors()) {
            throw new Exception(session.getLabel("5147"));
        }

        maxOVforThisOG = CASepaCommonUtils.getOvMaxByOG(org);

        return org;
    }

    /**
     * @see {@link APIGestionRentesExterne#createOrdreGroupe(BSession, BTransaction, String, String, String)}
     */
    @Override
    public void createOrdreGroupe(BSession session, BTransaction transaction, String libelle, String dateValeur,
            String numeroOG, String idOrganeExecution) throws Exception {
        if (journalVersement == null) {
            journalVersement = createNewJournal(session, transaction, libelle, dateValeur);
        }

        if (ordreGroupe == null) {
            ordreGroupe = createNewOrdreGroupe(session, transaction, libelle, dateValeur, numeroOG, idOrganeExecution);
        }
    }

    /**
     * @see APIGestionRentesExterne#createSection(BSession, BTransaction, String, String, String, String, String,
     *      String, String, String, String)
     */
    @Override
    public void createSection(BSession session, BTransaction transaction, String idCompteAnnexe, String idSection,
            String idExterne, String dateCreation, String dateEcheance, String dateDebutPeriode, String dateFinPeriode,
            String montantBase, String montantPaiment, String domaine, String typeAdresse) throws Exception {
        if ((journal == null) || journal.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        if (section == null) {
            section = new CARentesSection(transaction);
        }

        section.setIdSection(idSection);
        section.setIdCompteAnnexe(idCompteAnnexe);
        section.setIdExterne(idExterne);
        section.setDateCreation(dateCreation);
        section.setDateEcheance(dateEcheance);
        section.setDateDebutPeriode(dateDebutPeriode);
        section.setDateFinPeriode(dateFinPeriode);
        section.setMontantBase(montantBase);
        section.setMontantPaiment(montantPaiment);
        section.setIdJournal(journal.getIdJournal());
        section.setDomaine(domaine);
        section.setTypeAdresse(typeAdresse);

        section.fillVariables();
        section.executeQuery(transaction);
    }

    /**
     * Mise à jour de l'ordre groupé (montant total et nombre de transactions).
     * 
     * (old finalize renamed to not use reserved finalize method name on object)
     * 
     * new : return list of "numero livraison" of multiple og created
     * 
     * @param parent
     * @param session
     * @param transaction
     * @throws Exception
     */
    @Override
    public List<String> bouclerOG(BProcess parent, BSession session, BTransaction transaction) throws Exception {
        if ((ordreGroupe == null) || ordreGroupe.isNew()) {
            throw new Exception(session.getLabel("5200"));
        }

        if ((journal == null) || journal.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        if ((journalVersement == null) || journalVersement.isNew()) {
            throw new Exception(session.getLabel("5157"));
        }

        closeUpdateOG(session, transaction);

        /*
         * journal.setEtat(CAJournal.TRAITEMENT); journal.update(transaction);
         * 
         * if (journal.isNew() || journal.hasErrors()) { throw new Exception(session.getLabel("5157")); }
         * 
         * 
         * journalVersement.setEtat(CAJournal.TRAITEMENT); journalVersement.update(transaction);
         * 
         * 
         * 
         * if (journalVersement.isNew() || journalVersement.hasErrors()) { throw new
         * Exception(session.getLabel("5157")); }
         */

        if (!new CAComptabiliserJournal().comptabiliser(parent, journal)) {
            throw new Exception(session.getLabel("5008"));
        }

        if (!new CAComptabiliserJournal().comptabiliser(parent, journalVersement)) {
            throw new Exception(session.getLabel("5008"));
        }
        return listOg;
    }

    /**
     * atomic close and update an OG in case of multiple OG due to ISO20022 limit
     * 
     * @param session
     * @param transaction
     * @throws Exception
     */
    private void closeUpdateOG(BSession session, BTransaction transaction) throws Exception {
        ordreGroupe.setTotal(totalVersement.toString());
        ordreGroupe.setNombreTransactions(Integer.toString(countVersement));
        ordreGroupe.update(transaction);

        if (ordreGroupe.hasErrors()) {
            throw new Exception(session.getLabel("5200"));
        }
    }

    /**
     * @see APIGestionRentesExterne#getOrCreateLastSectionForPrestationsBloquees(BSession, BTransaction, String, String,
     *      String, String)
     */
    @Override
    public APISection getOrCreateLastSectionForPrestationsBloquees(BSession session, BTransaction transaction,
            APIGestionComptabiliteExterne comptaExterne, String idCompteAnnexe, String idExterneRole, String idRole,
            String annee) throws Exception {
        CASectionManager manager = new CASectionManager();
        manager.setSession(session);

        manager.setForCategorieSection(APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES);
        manager.setLikeIdExterne(annee);
        manager.setForIdCompteAnnexe(idCompteAnnexe);
        manager.setOrderBy(CASectionManager.ORDER_IDEXTERNE_DESCEND);
        manager.find(transaction);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            return comptaExterne.createSection(idCompteAnnexe, APISection.ID_TYPE_SECTION_BLOCAGE, annee + "74000",
                    null, null);
        } else {
            CASection section = (CASection) manager.getFirstEntity();

            if (section.getSoldeToCurrency().isZero()) {
                String newIdExterne = CAUtil
                        .creerNumeroSectionUnique(session, transaction, idRole, idExterneRole,
                                APISection.ID_TYPE_SECTION_BLOCAGE, annee,
                                APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES);
                return comptaExterne.createSection(idCompteAnnexe, APISection.ID_TYPE_SECTION_BLOCAGE, newIdExterne,
                        null, null);
            } else {
                return section;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.osiris.api.APIGestionRentesExterne#updateOperationOrdreVersementInEtatVerse(globaz.globall.db.BSession)
     */
    @Override
    public void updateOperationOrdreVersementInEtatVerse(BSession session) throws Exception {
        if (session == null) {
            throw new NullPointerException("Session is null");
        }
        if ((journal == null) || journal.isNew()) {
            throw new Exception(session.getLabel("7399"));
        }
        if (journal.getIdJournal().isEmpty()) {
            throw new Exception(session.getLabel("7402"));
        }

        BTransaction transaction = null;
        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();
            CAUpdateOperationOrdreVersementInEtatVerse updateOperation = new CAUpdateOperationOrdreVersementInEtatVerse();
            updateOperation.setForIdJournal(journal.getIdJournal());
            if (updateOperation.update(journal.getSession(), transaction)) {
                transaction.commit();
            } else {
                transaction.rollback();
            }

        } catch (Exception e) {
            if ((transaction != null) && transaction.isOpened()) {
                transaction.rollback();
            }
            throw new Exception(session.getLabel("7398") + e.toString());
        } finally {
            try {
                if ((transaction != null) && transaction.isOpened()) {
                    transaction.closeTransaction();
                }
            } catch (Exception e1) {
                JadeLogger.error(this, e1);
            }
        }

    }
}
