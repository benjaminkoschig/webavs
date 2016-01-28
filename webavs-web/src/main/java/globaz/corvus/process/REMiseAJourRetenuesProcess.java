package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordee;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordeeManager;
import globaz.corvus.process.exception.REProcessChargementJointRenteAccordeeException;
import globaz.corvus.process.exception.REProcessChargementRenteAccordeeException;
import globaz.corvus.process.exception.REProcessException;
import globaz.corvus.process.exception.REProcessMajRetenueAccordeeException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.retenues.RERetenuesUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.prestation.tools.PRDateFormater;

/**
 * Process permettant de mettre à jour le flag des retenue dans la classe REPrestationsAccordees (rereacc) 28.11.2012 :
 * LGA La mise à jour des retenues s'effectue non pas sur les RERenteAccordee mais sur REPrestationAccordee afin de
 * pouvoir traiter tous les types de prestation (API, AVS, PC, RFM, etc)
 * 
 * @author FGO
 */
public class REMiseAJourRetenuesProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean succeed;

    /**
     * Constructeur
     */
    public REMiseAJourRetenuesProcess() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param parent
     */
    public REMiseAJourRetenuesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public REMiseAJourRetenuesProcess(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        return doTraitement(getTransaction());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

    }

    /**
     * Chargement du manager RERetenuesJointPrestationAccordeeManager
     * 
     * @return
     * @throws REProcessChargementJointRenteAccordeeException
     */
    private RERetenuesJointPrestationAccordeeManager chargeDonnees()
            throws REProcessChargementJointRenteAccordeeException {
        RERetenuesJointPrestationAccordeeManager manager;
        try {
            manager = new RERetenuesJointPrestationAccordeeManager();
            manager.changeManagerSize(BManager.SIZE_NOLIMIT);
            manager.setSession(getSession());
            manager.setForRenteEnCoursAtDate(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel
                    .getDateProchainPmt(getSession())));
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_VALIDE);
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_PARTIEL);
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_DIMINUE);
            manager.setOrderBy(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        } catch (Exception e) {
            throw new REProcessChargementJointRenteAccordeeException(
                    this.getClass().getName().concat("chargeDonnees("), e);
        }
        return manager;
    }

    /**
     * Chargement de l'enregistrement renteAccordée
     * 
     * @param prestationAccordee
     * @param idPrestationAccordee
     * @throws REProcessChargementRenteAccordeeException
     */
    private void chargementPrestationAccordee(REPrestationsAccordees prestationAccordee, String idPrestationAccordee)
            throws REProcessChargementRenteAccordeeException {

        prestationAccordee.setSession(getSession());
        prestationAccordee.setIdPrestationAccordee(idPrestationAccordee);
        try {
            prestationAccordee.retrieve();

            // Test ajouté par acquis de conscience car c'est toujours mieux de s'assurer que l'entitée à bien été
            // retrouvée
            if (prestationAccordee.isNew()) {
                String message = "la prestation accordée avec l'id : " + idPrestationAccordee
                        + "n'as pas été trouvée en base de données";
                getMemoryLog().logMessage(message, FWMessage.INFORMATION,
                        this.getClass().getName().concat("chargementRenteAccordee()"));
                throw new REProcessChargementRenteAccordeeException(message);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "l'id de la prestation accordée " + idPrestationAccordee + " ne peut être chargé ",
                    FWMessage.INFORMATION, this.getClass().getName().concat("chargementRenteAccordee()"));
            throw new REProcessChargementRenteAccordeeException(this.getClass().getName()
                    .concat("chargementRenteAccordee()"), e);
        }
    }

    public boolean doTraitement(BTransaction transaction) throws Exception {
        try {
            boolean active = false;
            // chargement du manager des données à mettre à jour
            RERetenuesJointPrestationAccordee entity = null;
            RERetenuesJointPrestationAccordee memEntity = new RERetenuesJointPrestationAccordee();
            RERetenuesJointPrestationAccordeeManager manager = chargeDonnees();
            int cpt = 0;
            BStatement st = manager.cursorOpen(transaction);
            while ((entity = (RERetenuesJointPrestationAccordee) manager.cursorReadNext(st)) != null) {
                if (!entity.getIdRenteAccordee().equalsIgnoreCase(memEntity.getIdRenteAccordee()) && (cpt > 0)) {
                    traitementPrestationAccordee(active, memEntity.getIdRenteAccordee());
                    active = false;
                }
                // test de la retenue
                if (RERetenuesUtil.isRetenueActive(getSession(), entity, false)) {
                    active = true;
                }
                cpt++;
                memEntity.copyDataFromEntity(entity);
            }
            manager.cursorClose(st);

            // Mise à jour de la dernière renteAccordee
            traitementPrestationAccordee(active, memEntity.getIdRenteAccordee());

            succeed = true;

        } catch (REProcessException e) {
            succeed = false;
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ENVOI_FAILED"), FWMessage.ERREUR, e.toString());
        }
        return succeed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        StringBuffer str = new StringBuffer();
        if (getMemoryLog().isOnErrorLevel() || !succeed) {
            str = new StringBuffer(getSession().getLabel("PROCESS_MAJ_RETENUE_FAILED"));
        } else {
            str = new StringBuffer(getSession().getLabel("PROCESS_MAJ_RETENUE_SUCCESS"));
        }
        str.append(" - (").append(JACalendar.todayJJsMMsAAAA()).append(")");
        return str.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Mise à jour de l'enregistrement prestation accordee
     * 
     * @param active
     * @param prestationAccordee
     * @throws REProcessMajRetenueAccordeeException
     */
    private void majPrestationAccordee(boolean active, REPrestationsAccordees prestationAccordee)
            throws REProcessMajRetenueAccordeeException {

        try {
            if (!prestationAccordee.isNew()) {

                Boolean b = new Boolean(active);

                // Pas de maj si les flags sont déjà setté.
                // Devrait en principe toujours être le cas, car les flags sont
                // déjà
                // setté lors de la génération de la liste de vérififaction.
                // Cependant, ce test est nécessaire, car il se pourrait que qqn
                // rajoute
                // une retenue sur une RA après l'impression de la liste de
                // vérification, et dans
                // ce cas, elle devrait être remise à jours.
                if (b.equals(prestationAccordee.getIsRetenues())
                        && b.equals(prestationAccordee.getIsAttenteMajRetenue())) {
                    return;
                } else {
                    prestationAccordee.setIsRetenues(new Boolean(active));
                    prestationAccordee.setIsAttenteMajRetenue(new Boolean(active));
                    prestationAccordee.update(getTransaction());
                }

            }
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ENVOI_FAILED"), FWMessage.ERREUR, e.toString());
            throw new REProcessMajRetenueAccordeeException(this.getClass().getName().concat("majPrestationAccordee()"),
                    e);
        }
    }

    /**
     * @param active
     * @param idPrestationAccordee
     * @throws REProcessException
     */
    private void traitementPrestationAccordee(boolean active, String idPrestationAccordee) throws REProcessException {
        REPrestationsAccordees prestationAccordee = new REPrestationsAccordees();
        try {
            // chargement de la prestation accordée
            chargementPrestationAccordee(prestationAccordee, idPrestationAccordee);
            // mise à jour de la prestation
            majPrestationAccordee(active, prestationAccordee);
        } catch (REProcessChargementRenteAccordeeException e) {
            getMemoryLog().logMessage("la prestation accordée n'à pas pu être mise à jour", FWMessage.INFORMATION,
                    this.getClass().getName().concat("majPrestationAccordee()"));
        } catch (REProcessMajRetenueAccordeeException e) {
            throw new REProcessException(e);
        }
    }
}
