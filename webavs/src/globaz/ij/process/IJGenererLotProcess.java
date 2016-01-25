/*
 * Créé le 6 juin 05
 */
package globaz.ij.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGenererLotProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description = "";
    private boolean prestationLot = true;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererLotProcess.
     */
    public IJGenererLotProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererLotProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJGenererLotProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererLotProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererLotProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
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
        IJLot lot = null;

        try {
            IJPrestationManager prestationManager = new IJPrestationManager();
            prestationManager.setSession(getSession());
            prestationManager.setForCsEtat(IIJPrestation.CS_VALIDE);

            BStatement statement = prestationManager.cursorOpen(getTransaction());
            IJPrestation prestation = null;

            // Creation du lot seulement si il y a au moins un élément à mettre
            // dedans.
            if ((prestation = (IJPrestation) (prestationManager.cursorReadNext(statement))) != null) {
                lot = new IJLot();
                lot.setSession(getSession());
                lot.setCsEtat(IIJLot.CS_OUVERT);
                lot.setDateCreation(JACalendar.todayJJsMMsAAAA());
                lot.setDescription(description);
                lot.add(getTransaction());

                // mise en etat mis_lot du premier element
                prestation.setCsEtat(IIJPrestation.CS_MIS_EN_LOT);
                prestation.setIdLot(lot.getIdLot());
                prestation.update();
                getMemoryLog().logMessage(
                        getSession().getLabel("MISE_EN_LOT_PRST") + " - " + prestation.getIdPrestation(), "",
                        "IJGenererLotProcess");
            } else {
                prestationLot = false;
            }

            // mise en etat mis_lot des suivants
            while ((prestation = (IJPrestation) (prestationManager.cursorReadNext(statement))) != null) {
                prestation.setCsEtat(IIJPrestation.CS_MIS_EN_LOT);
                prestation.setIdLot(lot.getIdLot());
                prestation.update();
                getMemoryLog().logMessage(
                        getSession().getLabel("MISE_EN_LOT_PRST") + " - " + prestation.getIdPrestation(), "",
                        "IJGenererLotProcess");
            }

            prestationManager.cursorClose(statement);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "IJGenererLotProcess");

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
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {

        if (prestationLot) {
            return getSession().getLabel("JSP_GENERER_LOT");
        } else {
            return getSession().getLabel("LOT_AUCUNE_PREST");
        }

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
     * setter pour l'attribut description
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescription(String string) {
        description = string;
    }
}
