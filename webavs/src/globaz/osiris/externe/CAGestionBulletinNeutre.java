package globaz.osiris.externe;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionBulletinNeutre;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APIOperationBulletinNeutre;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.bulletinneutre.CABulletinNeutreTaxeSommation;
import globaz.osiris.db.bulletinneutre.CABulletinNeutreTaxeSommationManager;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationBulletinNeutre;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import globaz.osiris.process.journal.CAUtilsJournal;

public class CAGestionBulletinNeutre implements APIGestionBulletinNeutre {

    private CAJournal journal = null;

    /**
     * @see {@link APIGestionBulletinNeutre#addOperationBulletinNeutre(BSession, BTransaction, APIOperationBulletinNeutre)}
     */
    @Override
    public void addOperationBulletinNeutre(BSession session, BTransaction transaction,
            APIOperationBulletinNeutre operation) throws Exception {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            throw new Exception(session.getLabel("5157"));
        }

        if (operation == null) {
            throw new Exception(session.getLabel("5012") + " {addOperation}");
        }

        CAOperationBulletinNeutre bulletinNeutre = (CAOperationBulletinNeutre) operation;

        // La créer (l'opération before call a déjà été effectuée, on ne
        // l'effectue pas une deuxième fois)
        bulletinNeutre.wantCallMethodBefore(false);
        bulletinNeutre.add(transaction);
        bulletinNeutre.wantCallMethodBefore(true);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (bulletinNeutre.isNew()) {
            throw new Exception(session.getLabel("IMPOSSIBLE_CREER_BULLETIN_NEUTRE"));
        }
    }

    /**
     * @see {@link APIGestionBulletinNeutre#comptabiliser(BSession, BProcess)}
     */
    @Override
    public void comptabiliser(BSession session, BProcess parent) throws Exception {
        if (!new CAComptabiliserJournal().comptabiliser(parent, journal)) {
            throw new Exception(session.getLabel("5008"));
        }
    }

    /**
     * Création d'un compte annexe.
     * 
     * @param idTiers
     * @param idRole
     * @param idExterneRole
     * @return
     */
    private APICompteAnnexe createCompteAnnexe(BSession session, BTransaction transaction, String idTiers,
            String idRole, String idExterneRole) throws Exception {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(session);

        compteAnnexe.setIdJournal(journal.getIdJournal());
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);

        compteAnnexe.add(transaction);

        if (compteAnnexe.hasErrors()) {
            throw new Exception(compteAnnexe.getErrors().toString());
        }

        if (compteAnnexe.isNew()) {
            throw new Exception(session.getLabel("5004"));
        }

        return compteAnnexe;

    }

    /**
     * @see {@link APIGestionBulletinNeutre#createJournal(BSession, BTransaction, String, String)}
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
        jrn.setTypeJournal(CAJournal.TYPE_BULLETIN_NEUTRE);

        jrn.add(transaction);

        if (jrn.isNew() || jrn.hasErrors()) {
            throw new Exception(session.getLabel("5157"));
        }

        return jrn;
    }

    /**
     * @see {@link APIGestionBulletinNeutre#createOperationBulletinNeutre()}
     */
    @Override
    public APIOperationBulletinNeutre createOperationBulletinNeutre(BSession session, BTransaction transaction)
            throws Exception {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            throw new Exception(session.getLabel("5157"));
        }

        CAOperationBulletinNeutre bulletinNeutre = new CAOperationBulletinNeutre();
        bulletinNeutre.setSession(session);

        bulletinNeutre.setIdJournal(journal.getIdJournal());

        bulletinNeutre.beforeAdd(transaction);

        return bulletinNeutre;
    }

    /**
     * @see {@link APIGestionBulletinNeutre#createSection(BSession, BTransaction, String, String, String, String, Boolean, String)}
     */
    @Override
    public APISection createSection(BSession session, BTransaction transaction, String idCompteAnnexe,
            String idExterne, String domaine, String typeAdresse, Boolean nonImprimable, String idCaisseProf)
            throws Exception {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            throw new Exception(session.getLabel("5157"));
        }

        if (JadeStringUtil.isIntegerEmpty(idExterne) || JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            throw new Exception(session.getLabel("5012") + " {getSectionByIdExterne}");
        }

        CASection section = new CASection();
        section.setSession(session);

        section.setIdJournal(journal.getIdJournal());
        section.setIdTypeSection(APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE);
        section.setIdCompteAnnexe(idCompteAnnexe);
        section.setIdExterne(idExterne);
        section.setDateSection(journal.getDateValeurCG());

        if (!JadeStringUtil.isBlankOrZero(idCaisseProf)) {
            section.setIdCaisseProfessionnelle(idCaisseProf);
        }

        section.setTypeAdresse(typeAdresse);
        section.setDomaine(domaine);

        section.setNonImprimable(nonImprimable);

        section.add(transaction);

        if (section.hasErrors()) {
            throw new Exception(section.getErrors().toString());
        }

        if (section.isNew()) {
            throw new Exception(session.getLabel("IMPOSSIBLE_CREER_SECTION"));
        }

        return section;
    }

    /**
     * @see {@link APIGestionBulletinNeutre#getCompteAnnexeByRole(BSession session, BTransaction transaction, String idTiers, String idRole, String idExterneRole)}
     */
    @Override
    public APICompteAnnexe getCompteAnnexeByRole(BSession session, BTransaction transaction, String idTiers,
            String idRole, String idExterneRole) throws Exception {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            throw new Exception(session.getLabel("5157"));
        }

        if (JadeStringUtil.isIntegerEmpty(idRole) || JadeStringUtil.isBlank(idExterneRole)
                || JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new Exception(session.getLabel("5012") + " {getCompteAnnexeByRole}");
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(session);
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(idRole);
        compteAnnexe.setIdExterneRole(idExterneRole);

        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compteAnnexe.retrieve(transaction);

        compteAnnexe.setAlternateKey(0);

        if (compteAnnexe.isNew()) {
            return createCompteAnnexe(session, transaction, idTiers, idRole, idExterneRole);
        }

        return compteAnnexe;
    }

    /**
     * @see {@link APIGestionBulletinNeutre#getIdRubriquesTaxeSommation(BSession, BTransaction)}
     */
    @Override
    public String getIdRubriquesTaxeSommation(BSession session, BTransaction transaction) throws Exception {
        CAReferenceRubrique ref = new CAReferenceRubrique();
        ref.setSession(session);
        APIRubrique rubrique = ref.getRubriqueByCodeReference(APIReferenceRubrique.TAXE_SOMMATION_BULLETIN_NEUTRE);

        if (rubrique == null) {
            throw new Exception(session.getLabel("CODE_REFERENCE_NON_ATTRIBUE")
                    + APIReferenceRubrique.TAXE_SOMMATION_BULLETIN_NEUTRE);
        }

        return rubrique.getIdRubrique();
    }

    /**
     * @see {@link APIGestionBulletinNeutre#getJournal()}
     */
    @Override
    public APIJournal getJournal(BSession session) throws Exception {
        if (journal.isNew() || !(journal.isOuvert() || journal.isTraitement())) {
            throw new Exception(session.getLabel("5157"));
        }

        return journal;
    }

    /**
     * Return le dernier compteur antérieure à l'année passée en paramètre.
     * 
     * @param session
     * @param transaction
     * @param idCompteAnnexe
     * @param idRubriqueCompteur
     * @param annee
     * @return
     * @throws Exception
     */
    private CACompteur getLastCompteur(BSession session, BTransaction transaction, String idCompteAnnexe,
            String idRubriqueCompteur, String annee) throws Exception {
        CACompteurManager manager = new CACompteurManager();
        manager.setSession(session);

        manager.setForIdCompteAnnexe(idCompteAnnexe);
        manager.setForIdRubrique(idRubriqueCompteur);
        manager.setForAnneeBefore(annee);

        manager.setForSelectionTri(CACompteurManager.ORDER_ANNEE);

        manager.find(transaction);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (manager.isEmpty()) {
            throw new Exception(session.getLabel("AUCUN_COMPTEUR"));
        }

        return (CACompteur) manager.getFirstEntity();
    }

    /**
     * @see {@link APIGestionBulletinNeutre#getMontantTaxeSommation(BSession, BTransaction, String, String, String)}
     */
    @Override
    public String getMontantTaxeSommation(BSession session, BTransaction transaction, String idCompteAnnexe,
            String idRubriqueCompteur, String annee) throws Exception {
        CACompteur compteur = getLastCompteur(session, transaction, idCompteAnnexe, idRubriqueCompteur, annee);

        CABulletinNeutreTaxeSommationManager bulletinManager = new CABulletinNeutreTaxeSommationManager();
        bulletinManager.setSession(session);
        bulletinManager.setForMasseBetween(compteur.getCumulMasse());
        bulletinManager.find(transaction);

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (bulletinManager.isEmpty()) {
            throw new Exception(session.getLabel("AUCUNE_TAXE_SOMMATION"));
        }

        return ((CABulletinNeutreTaxeSommation) bulletinManager.getFirstEntity()).getMontant();
    }

}
