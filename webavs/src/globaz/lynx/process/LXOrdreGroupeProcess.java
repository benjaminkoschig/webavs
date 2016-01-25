package globaz.lynx.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.ordregroupe.LXOrdreGroupe;
import globaz.lynx.db.organeexecution.LXOrganeExecution;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;

public class LXOrdreGroupeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected static final String CENT_POUR_CENT = "100.00";

    private String idOrdreGroupe;
    private String idOrganeExecution;
    private String idSociete;

    private LXOrdreGroupe ordreGroupe = null;
    private LXOrganeExecution organeExecution = null;
    private LXSocieteDebitrice societe = null;

    /**
     * Commentaire relatif au constructeur LXOrdreGroupeProcess.
     */
    public LXOrdreGroupeProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur LXOrdreGroupeProcess.
     * 
     * @param parent
     *            BProcess
     */
    public LXOrdreGroupeProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur LXOrdreGroupeProcess.
     * 
     * @param session
     *            BSession
     */
    public LXOrdreGroupeProcess(BSession session) {
        super(session);
    }

    /**
     * @see BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // Do nothing
        return false;
    }

    @Override
    protected String getEMailObject() {
        // Do nothing
        return null;
    }

    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getIdSociete() {
        return idSociete;
    }

    /**
     * Retrouve l'ordre groupé si pas encore chargée.
     */
    public LXOrdreGroupe getOrdreGroupe() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_ORDRE_GROUPE"));
        }

        if (ordreGroupe == null) {
            ordreGroupe = new LXOrdreGroupe();
            ordreGroupe.setSession(getSession());
            ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());

            ordreGroupe.retrieve(getTransaction());

            if (ordreGroupe.hasErrors()) {
                throw new Exception(ordreGroupe.getErrors().toString());
            }

            if (ordreGroupe.isNew()) {
                throw new Exception(getSession().getLabel("RETRIEVE_ORDRE_GROUPE_IMPOSSIBLE"));
            }
        }

        return ordreGroupe;
    }

    /**
     * Retrouve l'ordre groupé si pas encore chargée.
     */
    public LXOrganeExecution getOrganeExecution() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_ORGANE_EXECUTION"));
        }

        if (organeExecution == null) {
            organeExecution = new LXOrganeExecution();
            organeExecution.setSession(getSession());
            organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            organeExecution.retrieve(getTransaction());

            if (organeExecution.hasErrors()) {
                throw new Exception(organeExecution.getErrors().toString());
            }

            if (organeExecution.isNew()) {
                throw new Exception(getSession().getLabel("RETRIEVE_ORGANEEXECUTION_IMPOSSIBLE"));
            }
        }

        return organeExecution;
    }

    /**
     * Retrouve la societe si pas encore chargée.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }

        if (societe == null) {
            societe = new LXSocieteDebitrice();
            societe.setSession(getSession());
            societe.setIdSociete(getIdSociete());
            societe.retrieve(getTransaction());

            if (societe.hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

            if (societe.isNew()) {
                throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            }
        }

        return societe;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdOrdreGroupe(String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setOrdreGroupe(LXOrdreGroupe ordreGroupe) {
        this.ordreGroupe = ordreGroupe;
    }

    public void setOrganeExecution(LXOrganeExecution organeExecution) {
        this.organeExecution = organeExecution;
    }

    public void setSociete(LXSocieteDebitrice societe) {
        this.societe = societe;
    }

    /**
     * Permet le changement d'etat d'une operation
     * 
     * @param csEtatOrdreGroupe
     * @throws Exception
     */
    protected void updateEtatOperation(LXOperation ope, String csEtatOperation) throws Exception {

        ope.setCsEtatOperation(csEtatOperation);
        ope.update(getTransaction());

        if (ope.hasErrors() || getTransaction().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("MAJ_OPERATION_IMPOSSIBLE"));
        } else {
            getTransaction().commit();
        }
    }

    /**
     * Permet le changement d'etat de l'ordre groupe
     * 
     * @param csEtatOrdreGroupe
     * @throws Exception
     */
    protected void updateEtatOrdreGroupe(String csEtatOrdreGroupe) throws Exception {
        LXOrdreGroupe ordreGroupe = getOrdreGroupe();

        ordreGroupe.setCsEtat(csEtatOrdreGroupe);
        ordreGroupe.update(getTransaction());

        if (ordreGroupe.hasErrors() || getTransaction().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("MAJ_ORDREGROUPE_IMPOSSIBLE"));
        } else {
            getTransaction().commit();
        }
    }
}
