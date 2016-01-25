/*
 * Créé le 6 juin 05
 */
package globaz.apg.process;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.prestation.api.IPRDemande;
import java.text.MessageFormat;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererLotProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description = "";
    private String prestationDateFin = "";
    private String typePrestation = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererLotProcess.
     */
    public APGenererLotProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APGenererLotProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APGenererLotProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererLotProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APGenererLotProcess(BSession session) {
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
        APLot lot = null;

        try {
            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(getSession());

            if (typePrestation.equals(IPRDemande.CS_TYPE_APG)) {
                prestationManager.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
            } else if (typePrestation.equals(IPRDemande.CS_TYPE_MATERNITE)) {
                prestationManager.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
                prestationManager.setToDateFin(getPrestationDateFin());
                prestationManager.setForNoRevision(IAPDroitMaternite.CS_REVISION_MATERNITE_2005);
            } else {
                // On n'est pas dans la mouise....
                throw new Exception("Type de prestation introuvable");
            }

            BStatement statement = prestationManager.cursorOpen(getTransaction());
            APPrestation prestation = null;

            // Creation du lot seulement si il y a au moins un élément à mettre
            // dedans.
            if ((prestation = (APPrestation) (prestationManager.cursorReadNext(statement))) != null) {
                lot = new APLot();
                lot.setSession(getSession());
                lot.setEtat(IAPLot.CS_OUVERT);
                lot.setDateCreation(JACalendar.todayJJsMMsAAAA());
                lot.setDescription(getDescription());
                lot.setTypeLot(typePrestation);
                lot.add();

                // mise en etat mis_lot du premier element
                prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
                prestation.setIdLot(lot.getIdLot());
                prestation.update();
                getMemoryLog().logMessage(
                        MessageFormat.format(getSession().getLabel("PRESTATION_MISE_A_JOUR"),
                                new Object[] { prestation.getIdPrestationApg() }), FWMessage.INFORMATION,
                        getSession().getLabel("GENERER_LOT_PROCESS"));
            } else {
                getMemoryLog().logMessage(getSession().getLabel("LOT_PAS_CREE"), FWMessage.AVERTISSEMENT,
                        getSession().getLabel("GENERER_LOT_PROCESS"));
            }

            // mise en etat mis_lot des suivants
            while ((prestation = (APPrestation) (prestationManager.cursorReadNext(statement))) != null) {
                prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
                prestation.setIdLot(lot.getIdLot());
                prestation.update();
                getMemoryLog().logMessage(
                        MessageFormat.format(getSession().getLabel("PRESTATION_MISE_A_JOUR"),
                                new Object[] { prestation.getIdPrestationApg() }), FWMessage.INFORMATION,
                        getSession().getLabel("GENERER_LOT_PROCESS"));
            }

            prestationManager.cursorClose(statement);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getSession().getLabel("GENERER_LOT_PROCESS"));

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
        return getSession().getLabel("GENERER_LOT_PROCESS");
    }

    /**
     * @return
     */
    public String getPrestationDateFin() {
        return prestationDateFin;
    }

    /**
     * getter pour l'attribut type prestation
     * 
     * @return la valeur courante de l'attribut type prestation
     */
    public String getTypePrestation() {
        return typePrestation;
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

    /**
     * @param string
     */
    public void setPrestationDateFin(String string) {
        prestationDateFin = string;
    }

    /**
     * setter pour l'attribut type prestation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypePrestation(String string) {
        typePrestation = string;
    }

}
