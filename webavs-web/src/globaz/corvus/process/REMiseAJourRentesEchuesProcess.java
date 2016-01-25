package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.process.exception.REProcessException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.prestation.tools.PRDateFormater;

/**
 * Process permettant de mettre à jour le flag des rentes bloquées pour celle ayant une date d'échéance échues, dans la
 * classe RERenteAccordee (rereacc)
 * 
 * @author SCR
 */
public class REMiseAJourRentesEchuesProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean succeed;

    /**
     * Constructeur
     */
    public REMiseAJourRentesEchuesProcess() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param parent
     */
    public REMiseAJourRentesEchuesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public REMiseAJourRentesEchuesProcess(BSession session) {
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

    public boolean doTraitement(BTransaction transaction) throws Exception {
        try {

            // chargement du manager des données à mettre à jour

            JADate dateProchainPmt = new JADate(REPmtMensuel.getDateProchainPmt(getSession()));

            REPrestationsAccordees entity = null;
            REPrestationAccordeeManager manager = new REPrestationAccordeeManager();
            manager.setSession(getSession());
            manager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_VALIDE
                    + ", " + IREPrestationAccordee.CS_ETAT_DIMINUE);
            manager.setForIsPrestationBloquee(Boolean.FALSE);
            manager.setForEnCoursAtMois(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateProchainPmt.toStrAMJ()));
            manager.setWantLevelField(false);
            manager.setForEchuesADate(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateProchainPmt.toStrAMJ()));

            BStatement st = manager.cursorOpen(transaction);
            while ((entity = (REPrestationsAccordees) manager.cursorReadNext(st)) != null) {

                // Pas de maj si les flags sont déjà setté (pour des questions
                // d'optimisations).
                // Devrait en principe toujours être le cas lorsque appelé lors
                // du traitement du grand pmt, car les flags sont déjà
                // setté lors de la génération de la liste de vérififaction.
                // Cependant, ce test est nécessaire, car il se pourrait que qqn
                // rajoute
                // un blocagesur une RA après l'impression de la liste de
                // vérification, et dans
                // ce cas, elle devrait être remise à jours.

                if (Boolean.TRUE.equals(entity.getIsPrestationBloquee())
                        && Boolean.TRUE.equals(entity.getIsAttenteMajBlocage())) {

                    ;
                } else {

                    entity.setIsPrestationBloquee(Boolean.TRUE);
                    entity.setIsAttenteMajBlocage(Boolean.TRUE);
                    entity.setTypeDeMiseAJours(IREPrestationAccordee.CS_MAJ_BLOCAGE_AUTOMATIQUE);
                    entity.update(getTransaction());
                }
            }
            manager.cursorClose(st);

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
            str = new StringBuffer(getSession().getLabel("PROCESS_MAJ_RENTES_ECHUES_FAILED"));
        } else {
            str = new StringBuffer(getSession().getLabel("PROCESS_MAJ_RENTES_ECHUES_SUCCESS"));
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

}
