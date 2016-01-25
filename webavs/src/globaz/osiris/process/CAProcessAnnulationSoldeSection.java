package globaz.osiris.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptecourant.CASoldesMinimesParSecteur;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.process.journal.CAAnnulerJournal;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import java.util.Hashtable;

/**
 * @author dda
 */
public class CAProcessAnnulationSoldeSection extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CHF_50_MAX = "50";
    private static final String LABEL_5115 = "5115";
    private static final String LABEL_5327 = "5327";
    private static final String LABEL_ANNULATIONSOLDE_FAILED = "ANNULATIONSOLDE_FAILED";
    private static final String LABEL_ANNULATIONSOLDE_LIMITE50 = "ANNULATIONSOLDE_LIMITE50";
    private static final String LABEL_ANNULATIONSOLDE_MONTANT = "ANNULATIONSOLDE_MONTANT";
    private static final String LABEL_ANNULATIONSOLDE_STAT = "ANNULATIONSOLDE_STAT";
    private static final String LABEL_ANNULATIONSOLDE_SUCCESS = "ANNULATIONSOLDE_SUCCESS";

    private static final String LABEL_ANNULATIONSOLDE_TEXTE = "ANNULATIONSOLDE_TEXTE";

    private static final String LABEL_ANNULATIONSOLDE_VIDE = "ANNULATIONSOLDE_VIDE";
    private static final String LABEL_MODESIMULATION = "MODESIMULATION";

    private String date = new String();
    private Boolean forCompteAnnexeActif = new Boolean(true);
    private Boolean forCompteAnnexeRadie = new Boolean(true);
    private String forSelectionRole = "";
    private String idCompte = new String();
    private String idExterneRubriqueEcran = new String();
    private String idTypeSection = new String();

    private CAJournal journal = null;
    private String montantMinime = new String();

    private CARubrique rubriqueAnnulation;
    // membres pour table des rubriques par secteur
    private Hashtable<CACompteCourant, String> rubriqueParSecteur;

    private Boolean simulation = new Boolean(false);
    private long statSoldeCompteAnnexeCanceled = 0;
    private long statSoldeSectionCanceled = 0;

    /**
     * Constructor for CAProcessAnnulationSoldeSection.
     */
    public CAProcessAnnulationSoldeSection() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessAnnulationSoldeSection.
     * 
     * @param parent
     */
    public CAProcessAnnulationSoldeSection(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessAnnulationSoldeSection.
     * 
     * @param session
     */
    public CAProcessAnnulationSoldeSection(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        if (isSimulation()) {
            getMemoryLog().logMessage(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_MODESIMULATION),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

        try {
            processAllSectionsMontantMinimum();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return !isOnError();
    }

    /**
     * Récupère le compte d'annulation selon le compte courant
     * 
     * @param cc
     *            le compte courant
     * @return l'id de la rubrique
     * @throws Exception
     *             si la rubrique n'existe pas
     */
    private String _getIdRubriqueFromSecteur(CACompteCourant cc) throws Exception {
        // Initialiser hashtable
        if (rubriqueParSecteur == null) {
            rubriqueParSecteur = new Hashtable<CACompteCourant, String>();
        }
        // Récupérer la rubrique
        if (rubriqueAnnulation == null) {
            rubriqueAnnulation = new CARubrique();
            rubriqueAnnulation.setSession(getSession());
            rubriqueAnnulation.setIdRubrique(getIdCompte());
            rubriqueAnnulation.reserve(getTransaction());
            if (rubriqueAnnulation.isNew()) {
                throw new Exception(getSession().getLabel("5016") + getIdCompte());
            }
        }
        // Vérifier si le compte courant est déjà trouvé
        String idRubriqueFromSecteur = rubriqueParSecteur.get(cc);
        if (idRubriqueFromSecteur == null) {
            String idExterne = cc.getIdExterne().substring(0, 4);
            idExterne = idExterne + rubriqueAnnulation.getIdExterne().substring(4);
            CARubrique rub = new CARubrique();
            rub.setSession(getSession());
            rub.setIdExterne(idExterne);
            rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            rub.retrieve(getTransaction());
            if (rub.isNew()) {
                throw new Exception(getSession().getLabel("5016") + idExterne);
            }
            idRubriqueFromSecteur = rub.getIdRubrique();
            rubriqueParSecteur.put(cc, idRubriqueFromSecteur);
        }
        // Retourne l'identifiant
        return idRubriqueFromSecteur;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
            } else {
                setSendCompletionMail(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        // Vérifier le montant minime
        if (JadeStringUtil.isDecimalEmpty(getMontantMinime())) {
            this._addError(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_MONTANT));
        }

        // Limite de 50 francs
        FWCurrency limite = new FWCurrency(getMontantMinime());
        limite.abs();

        if (limite.compareTo(new FWCurrency(CAProcessAnnulationSoldeSection.CHF_50_MAX)) > 0) {
            this._addError(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_LIMITE50));
        }

        // Vérifier la date
        if (JAUtil.isDateEmpty(getDate())) {
            this._addError(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_5327));
        }

        // Vérifier la rubrique
        if (JadeStringUtil.isIntegerEmpty(getIdExterneRubriqueEcran()) && JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            this._addError(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_5115));
        } else {
            CARubrique rub = new CARubrique();
            rub.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getIdExterneRubriqueEcran())) {
                rub.setIdExterne(getIdExterneRubriqueEcran());
                rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            } else {
                rub.setIdRubrique(getIdCompte());
            }
            rub.retrieve(getTransaction());
            if (rub.isNew()) {
                this._addError(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_5115));
            } else {
                setIdCompte(rub.getIdRubrique());
            }
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Annuler le solde de la section dont le montant est minime.
     * 
     * @param section
     *            la section
     * @param entity
     *            entité en cas d'annulation par secteur
     * @return
     * @throws Exception
     */
    private boolean annulerSolde(CASection section, CASoldesMinimesParSecteur entity) throws Exception {
        if ((section == null) || section.isSectionAuxPoursuites(false)) {
            return false;
        }

        // Indiquer le cas si simulation
        if (isSimulation()) {
            getMemoryLog().logMessage(
                    getSession().getLabel("COMPTEANNEXE") + " / " + getSession().getLabel("SECTION") + " : "
                            + section.getCompteAnnexe().getIdExterneRole() + " / " + section.getIdExterne(),
                    FWMessage.INFORMATION, this.getClass().getName());
            return true;
        }

        if (!shouldProcessCompteAnnexe((CACompteAnnexe) section.getCompteAnnexe())) {
            return false;
        }
        // Ecriture d'extourne
        CAEcriture ecriture = new CAEcriture();
        ecriture.setSession(getSession());
        ecriture.setDate(getDate());
        ecriture.setIdJournal(getJournal().getIdJournal());
        ecriture.setIdSection(section.getIdSection());
        ecriture.setIdCompteAnnexe(section.getIdCompteAnnexe());
        ecriture.setLibelle(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_TEXTE));

        // Si l'annulation est faite par compte courant (caisses horlogères)
        if (entity != null) {
            // Récupérer le compte courant
            CACompteCourant cc = new CACompteCourant();
            cc.setSession(getSession());
            cc.setIdCompteCourant(entity.getIdCompteCourant());
            cc.retrieve(getTransaction());
            if (cc.isNew()) {
                return false;
            }
            // Si le secteur n'est pas ventilé, on utilise l'identifiant de la
            // rubrique de base
            if (cc.getAccepterVentilation().booleanValue()) {
                ecriture.setIdCompte(getIdCompte());
                // Sinon, on construit une rubrique en utilisant les 4 1ères
                // positions du secteur
            } else {
                ecriture.setIdCompte(_getIdRubriqueFromSecteur(cc));
            }
            // Le montant provient de l'entity ainsi que le compte courant
            ecriture.setMontant(entity.getMontant());
            ecriture.setIdCompteCourant(entity.getIdCompteCourant());

            FWCurrency cMontant = new FWCurrency(entity.getMontant());
            if (cMontant.isNegative()) {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            }
            // Sinon, annulation standard sur l'ensemble de la section
        } else {
            ecriture.setIdCompte(getIdCompte());
            ecriture.setMontant(section.getSolde());

            if (section.getSoldeToCurrency().isNegative()) {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            }
        }

        ecriture.add(getTransaction());

        return commit(getTransaction(), ecriture, FWMessage.ERREUR);
    }

    /**
     * Clôture du journal. Comptabilisation ou annulation.
     * 
     * @throws Exception
     */
    private void closeJournal() throws Exception {
        if (!isSimulation()) {
            if ((statSoldeSectionCanceled > 0) || ((statSoldeCompteAnnexeCanceled) > 0)) {
                new CAComptabiliserJournal().comptabiliser(this, getJournal());
            } else {
                new CAAnnulerJournal().annuler(this, getJournal());
            }

            if (!getTransaction().hasErrors()) {
                getJournal().update(getTransaction());
            }

            commit(getTransaction(), getJournal(), FWMessage.FATAL);

            if (!isOnError() && !getSession().hasErrors() && !getMemoryLog().hasErrors()) {

                getMemoryLog().logMessage(
                        String.valueOf(statSoldeSectionCanceled) + " "
                                + getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_STAT),
                        FWMessage.INFORMATION, this.getClass().getName());
            }
        }
    }

    /**
     * Exécute le commit ou le rollback de la transaction + log si nécessaire des erreurs.
     * 
     * @param transaction
     * @param entity
     * @param gravite
     * @return
     * @throws Exception
     */
    private boolean commit(BTransaction transaction, BEntity entity, String gravite) throws Exception {
        if (isSimulation()) {
            return true;
        }

        if (!transaction.hasErrors()) {
            transaction.commit();
            return true;
        } else {
            if (entity != null) {
                getMemoryLog().logMessage(entity.toString(), FWMessage.INFORMATION, this.getClass().getName());
            }

            getMemoryLog().logStringBuffer(transaction.getErrors(), gravite);

            transaction.rollback();

            // Si l'erreur est fatale, on renvoie une exception
            if (gravite.equals(FWMessage.FATAL)) {
                throw new Exception(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_FAILED));
            } else {
                return false;
            }
        }
    }

    /**
     * Returns the date.
     * 
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_FAILED);
        } else {
            return getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_SUCCESS);
        }
    }

    /**
     * @return
     */
    public Boolean getForCompteAnnexeActif() {
        return forCompteAnnexeActif;
    }

    /**
     * @return
     */
    public Boolean getForCompteAnnexeRadie() {
        return forCompteAnnexeRadie;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Returns the idCompte.
     * 
     * @return String
     */
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Returns the idExterneRubriqueEcran.
     * 
     * @return String
     */
    public String getIdExterneRubriqueEcran() {
        return idExterneRubriqueEcran;
    }

    /**
     * Returns the idTypeSection.
     * 
     * @return String
     */
    public String getIdTypeSection() {
        return idTypeSection;
    }

    private CAJournal getJournal() throws Exception {
        if (journal == null) {
            journal = new CAJournal();
            journal.setSession(getSession());

            journal.setDateValeurCG(getDate());
            journal.setLibelle(getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_TEXTE));
            journal.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);
            journal.setEstVisibleImmediatement(new Boolean(true));

            if (!isSimulation()) {
                journal.add(getTransaction());

                commit(getTransaction(), journal, FWMessage.FATAL);
            }
        }

        return journal;
    }

    /**
     * Returns the montantMinime.
     * 
     * @return String
     */
    public String getMontantMinime() {
        return montantMinime;
    }

    /**
     * @return
     */
    public Boolean getSimulation() {
        return simulation;
    }

    /**
     * Returns the simulation.
     * 
     * @return boolean
     */
    public boolean isSimulation() {
        return getSimulation().booleanValue();
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Solde les sections dont le montant est minimum en utilisant la rubrique passée en paramètre par l'utilisateur. <br/>
     * Recherche des sections de tous les comptes annexes.
     * 
     * @throws Exception
     */
    private void processAllSectionsMontantMinimum() throws Exception {
        processSectionMontantMinimum(null);
    }

    /**
     * Solde les sections dont le montant est minimum en utilisant la rubrique passée en paramètre par l'utilisateur.
     * 
     * @param idCompteAnnexe
     *            Si idCompteAnnexe est spécifié effectue l'annulation du solde minime sur le compte annexe spécifié
     *            uniquement. Sinon recherche les sections de tous les comptes annexes.
     * @throws Exception
     */
    private void processSectionMontantMinimum(String idCompteAnnexe) throws Exception {
        CASectionManager manager = new CASectionManager();
        manager.setSession(getSession());
        manager.setForMontantMinime(getMontantMinime());
        manager.setForIdTypeSection(getIdTypeSection());

        if (!JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            manager.setForIdCompteAnnexe(idCompteAnnexe);
        }

        if (manager.getCount() == 0) {
            getMemoryLog().logMessage(
                    getSession().getLabel(CAProcessAnnulationSoldeSection.LABEL_ANNULATIONSOLDE_VIDE), FWMessage.FATAL,
                    this.getClass().getName());
            return;
        }

        setProgressScaleValue(manager.getCount());

        BStatement statement = null;
        BTransaction cursorTransaction = null;
        try {
            cursorTransaction = (BTransaction) getSession().newTransaction();
            cursorTransaction.openTransaction();

            statement = manager.cursorOpen(cursorTransaction);

            CASection section = null;
            while ((section = (CASection) manager.cursorReadNext(statement)) != null) {
                if (shouldProcessCompteAnnexe((CACompteAnnexe) section.getCompteAnnexe())
                        && annulerSolde(section, null)) {
                    statSoldeSectionCanceled++;
                }

                incProgressCounter();
            }
        } catch (Exception e) {
            if (!getTransaction().hasErrors()) {
                getTransaction().addErrors(e.getMessage());
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception e) {
            } finally {
                if (cursorTransaction != null) {
                    cursorTransaction.closeTransaction();
                }
            }
            commit(getTransaction(), null, FWMessage.FATAL);
        }

        if (JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            closeJournal();
        }
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            The date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param boolean1
     */
    public void setForCompteAnnexeActif(Boolean b) {
        forCompteAnnexeActif = b;
    }

    /**
     * @param boolean1
     */
    public void setForCompteAnnexeRadie(Boolean b) {
        forCompteAnnexeRadie = b;
    }

    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * Sets the idCompte.
     * 
     * @param idCompte
     *            The idCompte to set
     */
    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Sets the idExterneRubriqueEcran.
     * 
     * @param idExterneRubriqueEcran
     *            The idExterneRubriqueEcran to set
     */
    public void setIdExterneRubriqueEcran(String idRubrique) {
        idExterneRubriqueEcran = idRubrique;
    }

    /**
     * Sets the idTypeSection.
     * 
     * @param idTypeSection
     *            The idTypeSection to set
     */
    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    /**
     * Sets the montantMinime.
     * 
     * @param montantMinime
     *            The montantMinime to set
     */
    public void setMontantMinime(String montantMinime) {
        this.montantMinime = montantMinime;
    }

    /**
     * Sets the simulation.
     * 
     * @param simulation
     *            The simulation to set
     */
    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

    /**
     * Le compte annexe doit-il être soldé ?<br/>
     * Bzw. les sections du compte annexe doivent-elles être soldés ?
     * 
     * @param compteAnnexe
     * @return
     */
    private boolean shouldProcessCompteAnnexe(CACompteAnnexe compteAnnexe) {
        // si la sélection des rôles est renseignée, le rôle du compte annexe
        // doit correspondre
        if (!JadeStringUtil.isEmpty(getForSelectionRole())
                && (getForSelectionRole().indexOf(compteAnnexe.getIdRole()) == -1)) {
            return false;
        }

        if ((getForCompteAnnexeActif().booleanValue()) && (JAUtil.isDateEmpty(compteAnnexe.getRole().getDateFin()))) {
            return true;
        } else if ((getForCompteAnnexeRadie().booleanValue())
                && (!JAUtil.isDateEmpty(compteAnnexe.getRole().getDateFin()))) {
            return true;
        } else {
            return false;
        }
    }
}
