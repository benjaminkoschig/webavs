/*
 * Créé le 20 juin 05
 */
package globaz.apg.process;

import globaz.apg.api.prestation.IAPInscriptionCI;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.prestation.APGenerationInscriptionCI;
import globaz.apg.db.prestation.APGenerationInscriptionCIManager;
import globaz.apg.db.prestation.APInscriptionCI;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APInscrireCIProcess extends BProcess {

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
     * Crée une nouvelle instance de la classe APInscrireCIProcess.
     */
    public APInscrireCIProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APInscrireCIProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APInscrireCIProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APInscrireCIProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APInscrireCIProcess(BSession session) {
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
            APGenerationInscriptionCIManager generationInscriptionCIManager = new APGenerationInscriptionCIManager();
            generationInscriptionCIManager.setSession(session);

            generationInscriptionCIManager.setParentOnly(true);
            generationInscriptionCIManager.setForTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
            generationInscriptionCIManager.setSansIdInscriptionCi(true);
            generationInscriptionCIManager.setForIdLot(forIdLot);
            generationInscriptionCIManager.setOnlyStandardPrestation(true);

            statement = generationInscriptionCIManager.cursorOpen(transaction);

            APGenerationInscriptionCI generationInscriptionCI = null;

            while (((generationInscriptionCI = ((APGenerationInscriptionCI) (generationInscriptionCIManager
                    .cursorReadNext(statement)))) != null)) {

                if ((new FWCurrency(generationInscriptionCI.getMontantBrut()).isPositive() && new FWCurrency(
                        generationInscriptionCI.getTotalCotisation()).isNegative())
                        || (new FWCurrency(generationInscriptionCI.getMontantBrut()).isNegative() && new FWCurrency(
                                generationInscriptionCI.getTotalCotisation()).isPositive())) {

                    // normalement il ne sert a rien
                    if (new FWCurrency(generationInscriptionCI.getMontantBrut()).isZero()) {
                        continue;
                    }

                    APInscriptionCI inscriptionCI = new APInscriptionCI();
                    inscriptionCI.setNoAVS(generationInscriptionCI.getNoAVS());
                    inscriptionCI.setMoisDebut(JADate.getMonth(generationInscriptionCI.getDateDebut()).toString());
                    inscriptionCI.setMoisFin(JADate.getMonth(generationInscriptionCI.getDateFin()).toString());
                    inscriptionCI.setAnnee(JADate.getYear(generationInscriptionCI.getDateFin()).toString());
                    inscriptionCI.setMontantBrut(generationInscriptionCI.getMontantBrut());
                    inscriptionCI.setStatut(IAPInscriptionCI.CS_OUVERT);
                    inscriptionCI.add(transaction);

                    // update de l'idci de la repartition
                    APRepartitionPaiements repartitionPaiements = new APRepartitionPaiements();
                    repartitionPaiements.setIdRepartitionBeneficiairePaiement(generationInscriptionCI
                            .getIdRepartinionPaiement());
                    repartitionPaiements.setSession(session);
                    repartitionPaiements.retrieve(transaction);
                    repartitionPaiements.setIdInscriptionCI(inscriptionCI.getIdInscription());
                    repartitionPaiements.wantMiseAJourLot(false);
                    repartitionPaiements.wantCallValidate(false);
                    repartitionPaiements.update(transaction);
                }

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
        return getSession().getLabel("INSCRIPTION_CI");
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
