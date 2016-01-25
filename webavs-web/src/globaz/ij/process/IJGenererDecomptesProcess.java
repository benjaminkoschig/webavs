/*
 * Créé le 7 oct. 05
 */
package globaz.ij.process;

import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.itext.IJDecomptes;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.regles.IJPrestationRegles;
import globaz.ij.regles.IJPrononceRegles;

/**
 * <H1>Process effectuant la logique de génération de communications.</H1>
 * 
 * <p>
 * Si communication définitive, effectue les opérations suivante :
 * </p>
 * 
 * <ul>
 * <li>Mise à jour du lot en état définitif</li>
 * <li>Impression de la communication</li>
 * <li>Mise à jour des prestations à l'état définitif. Si une annonce est en type restitution, passe toutes les annonces
 * dont l'idRestitution = id de cette prestation en ANNULER et passe cette même prestation en ANNULER</li>
 * <li>Mise à jour de l'état des droits. Si un droit a toutes ses prestations en DEFINITIF, alors il passe en DEFINITIF,
 * sinon en PARTIEL</li>
 * <li>Lance le process de génération d'annonce pour les prestations du lot choisi</li>
 * <li>Lance le process d'inscription aux CI pour les répartitions des prestations du lot choisi</li>
 * <li>Ecritures comptable</li>
 * </ul>
 * 
 * <p>
 * Si Communication provisoire, ne fait qu'imprimer la communication
 * </p>
 * 
 * @author dvh
 */
public class IJGenererDecomptesProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateSurDocument = "";
    private String dateValeurComptable = "";
    private String descriptionLot = "";
    private String emailObject = "";
    private String idLot = "";
    private Boolean isDefinitif = null;

    private Boolean isSendToGed = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererDecomptesProcess.
     */
    public IJGenererDecomptesProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererDecomptesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJGenererDecomptesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererDecomptesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererDecomptesProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {

        boolean succes = false;
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        try {
            _validate();

            if (getTransaction().hasErrors() || getSession().hasErrors()) {
                succes = false;
                throw new Exception("Validation failed !!!");
            }

            getMemoryLog().setTransaction(transaction);

            if (isDefinitif.booleanValue()) {

                genereAnnonces(transaction);
                if (getMemoryLog().hasErrors()) {
                    throw new Exception(getSession().getLabel("GENERER_ARC_KO"));
                }

                getMemoryLog().logMessage(getSession().getLabel("GENERER_ARC_OK"), FWMessage.INFORMATION, "");
                doInscriptionsCI(transaction);
                if (getMemoryLog().hasErrors()) {
                    throw new Exception(getSession().getLabel("GENERER_CI_KO"));
                }

                getMemoryLog().logMessage(getSession().getLabel("GENERER_CI_OK"), FWMessage.INFORMATION, "");
                metAJourPrestations(transaction, session);
                if (getMemoryLog().hasErrors()) {
                    throw new Exception(getSession().getLabel("GENERER_PRST_KO"));
                }

                getMemoryLog().logMessage(getSession().getLabel("GENERER_PREST_OK"), FWMessage.INFORMATION, "");
                valideLot(transaction);
                if (getMemoryLog().hasErrors()) {
                    throw new Exception(getSession().getLabel("VALIDER_LOT_KO"));
                }

                getMemoryLog().logMessage(getSession().getLabel("VALIDER_LOT_OK"), FWMessage.INFORMATION, "");
                imprimerCommunication();
                if (getMemoryLog().hasErrors()) {
                    throw new Exception(getSession().getLabel("IMPRESSION_DECOMPTES_KO"));
                }

                getMemoryLog().logMessage(getSession().getLabel("IMPRESSION_DECOMPTES_OK"), FWMessage.INFORMATION, "");

                // Les ecritures comptable sont faites en dernier car on commit
                // les ecritures dans ce process.
                // Comme cela on est sur de gerer de mainiere atomique toutes
                // les operations
                // point ouvert 00540
                doEcrituresComptables(transaction);
                if (getMemoryLog().hasErrors()) {
                    throw new Exception(getSession().getLabel("GENERER_ECRIT_COMPT_KO"));
                }

                getMemoryLog().logMessage(getSession().getLabel("GENERER_ECRIT_COMPT_OK"), FWMessage.INFORMATION, "");
            } else {
                imprimerCommunication();
                if (getMemoryLog().hasErrors()) {
                    throw new Exception(getSession().getLabel("VALIDER_LOT_KO"));
                }
            }

            if (transaction.hasErrors() || getSession().hasErrors()) {
                throw new Exception();
            }

            transaction.commit();
            succes = true;
        } catch (Exception e) {

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());

            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(session.getErrors().toString(), FWMessage.ERREUR, this.getClass().toString());
            }

            try {
                transaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {

            if (succes) {
                emailObject = getSession().getLabel("COMPTABILISER_LOT_OK") + " " + getIdLot();
            } else {
                emailObject = getSession().getLabel("COMPTABILISER_LOT_KO") + " " + getIdLot();
            }
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    /**
     * (non-Javadoc).
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        super._validate();

        IJLot lot = new IJLot();
        lot.setSession(getSession());
        lot.setIdLot(getIdLot());
        lot.retrieve(getTransaction());
        if (lot.isNew()) {
            _addError(getTransaction(), "Erreur : lot non renseigné !!!");
        } else if (!IIJLot.CS_COMPENSE.equals(lot.getCsEtat()) && getIsDefinitif().booleanValue()) {
            _addError(getTransaction(), getSession().getLabel("GENERATION_COMMUNICATIONS_IMPOSSIBLE"));
        }
    }

    private void doEcrituresComptables(BITransaction transaction) throws Exception {
        IJGenererEcrituresComptablesProcess genererEcrituresComptablesProcess = new IJGenererEcrituresComptablesProcess(
                this);

        genererEcrituresComptablesProcess.setIdLot(idLot);
        genererEcrituresComptablesProcess.setDateComptable(dateValeurComptable);
        genererEcrituresComptablesProcess.setDateSurDocument(getDateSurDocument());
        genererEcrituresComptablesProcess.setTransaction(transaction);
        genererEcrituresComptablesProcess.executeProcess();
    }

    private void doInscriptionsCI(BITransaction transaction) throws Exception {
        IJInscrireCIProcess inscrireCIProcess = new IJInscrireCIProcess(this);

        inscrireCIProcess.setForIdLot(idLot);
        inscrireCIProcess.setTransaction(transaction);
        inscrireCIProcess.executeProcess();
    }

    private void genereAnnonces(BITransaction transaction) throws Exception {
        IJGenererAnnoncesProcess genererAnnoncesProcess = new IJGenererAnnoncesProcess(this);

        genererAnnoncesProcess.setIdLot(idLot);
        genererAnnoncesProcess.setMoisAnneeComptable(JADate.getMonth(dateValeurComptable) + "."
                + JADate.getYear(dateValeurComptable));
        genererAnnoncesProcess.setTransaction(transaction);
        genererAnnoncesProcess.executeProcess();
    }

    /**
     * getter pour l'attribut date sur document.
     * 
     * @return la valeur courante de l'attribut date sur document
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * getter pour l'attribut date valeur comptable.
     * 
     * @return la valeur courante de l'attribut date valeur comptable
     */
    public String getDateValeurComptable() {
        return dateValeurComptable;
    }

    /**
     * getter pour l'attribut description lot.
     * 
     * @return la valeur courante de l'attribut description lot
     */
    public String getDescriptionLot() {
        return descriptionLot;
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {

        return emailObject;
    }

    /**
     * getter pour l'attribut no lot.
     * 
     * @return la valeur courante de l'attribut no lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * getter pour l'attribut is definitif.
     * 
     * @return la valeur courante de l'attribut is definitif
     */
    public Boolean getIsDefinitif() {
        return isDefinitif;
    }

    /**
     * @return
     */
    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    private void imprimerCommunication() throws Exception {
        IJDecomptes decomptes = new IJDecomptes(getSession());
        decomptes.setIdLot(idLot);
        decomptes.setEMailAddress(getEMailAddress());

        decomptes.setDate(new JADate(dateSurDocument));
        decomptes.setIsSendToGED(isSendToGed);
        decomptes.setIsDecompteDefinitif(getIsDefinitif());
        // decomptes.setDate(new
        // SimpleDateFormat("dd.MM.yyyy").parse(dateSurDocument));
        decomptes.start();
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void metAJourPrestations(BTransaction transaction, BSession session) throws Exception {
        IJPrestationManager prestationManager = new IJPrestationManager();
        prestationManager.setSession(session);
        prestationManager.setForIdLot(idLot);

        BStatement statement = prestationManager.cursorOpen(transaction);
        IJPrestation prestation = null;

        while ((prestation = (IJPrestation) prestationManager.cursorReadNext(statement)) != null) {
            IJPrestationRegles.setEtatDefinitif(session, transaction, prestation);
            prestation.update(transaction);
            IJBaseIndemnisation bi = prestation.loadBaseIndemnisation(transaction);
            IJBaseIndemnisationRegles.setEtatCommunique(session, transaction, bi);
            bi.update(transaction);

            IJPrononce prononce = bi.loadPrononce(transaction);
            IJPrononceRegles.setEtatCommunique(session, transaction, prononce);
            prononce.update(transaction);
        }
    }

    /**
     * setter pour l'attribut date sur document.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSurDocument(String string) {
        dateSurDocument = string;
    }

    /**
     * setter pour l'attribut date valeur comptable.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateValeurComptable(String string) {
        dateValeurComptable = string;
    }

    /**
     * setter pour l'attribut description lot.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionLot(String string) {
        descriptionLot = string;
    }

    /**
     * setter pour l'attribut no lot.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * setter pour l'attribut is definitif.
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsDefinitif(Boolean boolean1) {
        isDefinitif = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setIsSendToGed(Boolean boolean1) {
        isSendToGed = boolean1;
    }

    private void valideLot(BTransaction transaction) throws Exception {
        IJLot lot = new IJLot();
        lot.setSession(getSession());
        lot.setIdLot(idLot);
        lot.retrieve(transaction);
        lot.setCsEtat(IIJLot.CS_VALIDE);
        lot.setDateImpressionCommunication(JACalendar.todayJJsMMsAAAA());
        lot.setDateComptable(dateValeurComptable);
        lot.update(transaction);
    }
}
