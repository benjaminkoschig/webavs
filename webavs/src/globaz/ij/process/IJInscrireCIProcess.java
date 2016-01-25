/*
 * Créé le 20 juin 05
 */
package globaz.ij.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.ij.api.prestations.IIJInscriptionCI;
import globaz.ij.db.prestations.IJGenerationInscriptionCI;
import globaz.ij.db.prestations.IJGenerationInscriptionCIManager;
import globaz.ij.db.prestations.IJInscriptionCI;
import globaz.ij.db.prestations.IJRepartitionPaiements;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJInscrireCIProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJInscrireCIProcess.
     */
    public IJInscrireCIProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJInscrireCIProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJInscrireCIProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe IJInscrireCIProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJInscrireCIProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        BSession session = getSession();
        BTransaction transaction = getTransaction();
        BStatement statement = null;

        try {
            IJGenerationInscriptionCIManager generationInscriptionCIManager = new IJGenerationInscriptionCIManager();
            generationInscriptionCIManager.setSession(session);

            generationInscriptionCIManager.setParentOnly(true);
            generationInscriptionCIManager.setSansIdInscriptionCi(true);
            generationInscriptionCIManager.setForIdLot(forIdLot);

            statement = generationInscriptionCIManager.cursorOpen(transaction);

            IJGenerationInscriptionCI generationInscriptionCI = null;

            while (((generationInscriptionCI = ((IJGenerationInscriptionCI) (generationInscriptionCIManager
                    .cursorReadNext(statement)))) != null)) {

                if (!generationInscriptionCI.isInscriptionOK()) {
                    continue;
                }

                // pas de montant brut a zero
                if (new FWCurrency(generationInscriptionCI.getMontantBrut()).isZero()) {
                    continue;
                }

                IJInscriptionCI inscriptionCI = new IJInscriptionCI();
                inscriptionCI.setNoAVS(generationInscriptionCI.getNoAVS());
                inscriptionCI.setMoisDebut(JADate.getMonth(generationInscriptionCI.getDateDebut()).toString());
                inscriptionCI.setMoisFin(JADate.getMonth(generationInscriptionCI.getDateFin()).toString());
                inscriptionCI.setAnnee(JADate.getYear(generationInscriptionCI.getDateFin()).toString());
                inscriptionCI.setMontantBrut(generationInscriptionCI.getMontantBrut());
                inscriptionCI.setStatut(IIJInscriptionCI.CS_OUVERT);
                inscriptionCI.add(transaction);

                // update de l'idci de la repartition
                IJRepartitionPaiements repartitionPaiements = new IJRepartitionPaiements();
                repartitionPaiements.setIdRepartitionPaiement(generationInscriptionCI.getIdRepartinionPaiement());
                repartitionPaiements.setSession(session);
                repartitionPaiements.retrieve(transaction);
                repartitionPaiements.setIdInscriptionCI(inscriptionCI.getIdInscriptionCI());
                repartitionPaiements.wantMiseAJourLot(false);
                repartitionPaiements.update(transaction);
            }

            generationInscriptionCIManager.cursorClose(statement);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getSession().getLabel("INSCRIPTION_CI"));

            return false;
        }

        return true;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
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

        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return "inscription CI";
    }

    /**
     * getter pour l'attribut for id lot
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * setter pour l'attribut for id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }
}
