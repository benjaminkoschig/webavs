package globaz.tucana.process.administrateur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.administration.TUValidationBouclement;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.exception.TUException;
import globaz.tucana.exception.fw.TUFWUpdateException;
import globaz.tucana.exception.process.TUProcessException;
import globaz.tucana.process.TUMessageAttach;
import globaz.tucana.process.message.TUMessagesContainer;
import globaz.tucana.transaction.TUTransactionHandler;
import java.io.IOException;
import java.util.Calendar;

/**
 * Process de validation d'un bouclement
 * 
 * @author fgo date de création : 14.09.06
 * @version : version 1.0
 * 
 */
public class TUValidationBouclementProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String idBouclement = "";
    private String mois = "";

    public TUValidationBouclementProcess() {
        super();
    }

    public TUValidationBouclementProcess(BProcess parent) {
        super(parent);
    }

    public TUValidationBouclementProcess(BSession session) {
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
        boolean succeed = true;
        TUTransactionHandler transactionHandler = new TUTransactionHandler(getSession()) {
            @Override
            protected void handleBean(BTransaction transaction) throws Exception {
                if (process(transaction)) {
                    ;
                }
                getMemoryLog().logMessage(getSession().getLabel("PROCESS_SUCCES"), FWMessage.INFORMATION, "-->");
                transaction.commit();
            }
        };

        try {
            transactionHandler.execute();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "process", e);
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ERROR"), FWViewBeanInterface.ERROR, "-->");

            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, "-->");
            succeed = false;
        }
        return succeed;
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
     * Récupère l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        StringBuffer str = new StringBuffer();
        if (getMemoryLog().isOnErrorLevel()) {
            str = new StringBuffer(getSession().getLabel("PRO_TIT_VALIDATION_BOUCLEMENT_ERROR"));
        } else {
            str = new StringBuffer(getSession().getLabel("PRO_TIT_VALIDATION_BOUCLEMENT"));
        }

        str.append(" : ").append(getMois()).append(".").append(getAnnee()).append(" - (");
        str.append(JACalendar.todayJJsMMsAAAA()).append(")");
        return str.toString();
    }

    /**
     * Récupère l'id du bouclement
     * 
     * @return
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Récupère le mois
     * 
     * @return
     */
    public String getMois() {
        return mois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Classe de mise à jour du bouclement si tous les tests sont validés
     * 
     * @param transaction
     * @param bouclement
     * @param messages
     * @throws TUFWUpdateException
     */
    private void majBouclement(BTransaction transaction, TUBouclement bouclement, TUMessagesContainer messages)
            throws TUFWUpdateException {
        bouclement.setCsEtat(ITUCSConstantes.CS_ETAT_BOUCLE);
        bouclement.setDateEtat(JACalendar.todayJJsMMsAAAA());
        try {
            bouclement.update(transaction);
        } catch (Exception e) {
            throw new TUFWUpdateException(this.getClass().getName(), e);
        }

    }

    public boolean process(BTransaction transaction) {
        boolean processValid = true;

        getMemoryLog().logMessage(Calendar.getInstance().getTime().toString(), FWMessage.INFORMATION,
                transaction.getSession().getLabel("INFO_DEBUT"));

        TUMessagesContainer messages = new TUMessagesContainer();
        // valide le bouclement
        // try {
        // vérifie que le numéro de passage a ete saisi
        if (JadeNumericUtil.isEmptyOrZero(getAnnee()) || JadeNumericUtil.isEmptyOrZero(getMois())) {
            getMemoryLog().logMessage(transaction.getSession().getLabel("ERR_MOIS_ANNEE_VIDE"), FWMessage.ERREUR,
                    "--> ");
            JadeCodingUtil.catchException(this, "process",
                    new TUProcessException(transaction.getSession().getLabel("ERR_MOIS_ANNEE_VIDE")));
            processValid = false;
        } else {

            try {
                // appel de la classe de validation d'un bouclement
                TUBouclement bouclement = TUValidationBouclement.validation(transaction, idBouclement, annee, mois,
                        messages);
                if (bouclement != null) {
                    majBouclement(transaction, bouclement, messages);
                } else {
                    processValid = false;
                    transaction.addErrors(messages.toString());
                    getMemoryLog().logMessage(messages.toString(), FWMessage.ERREUR, "--> ");
                    JadeCodingUtil.catchException(this, "process", new Exception(messages.toString()));
                    try {
                        transaction.rollback();
                    } catch (Exception e1) {
                        JadeCodingUtil.catchException(this, "process", e1);
                    }
                }

            } catch (TUException e) {
                transaction.addErrors(e.toString());
                messages.addMessage(e.getMessage(), FWViewBeanInterface.ERROR, e.getError());
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    JadeCodingUtil.catchException(this, "process", e1);
                }
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "--> ");
                JadeCodingUtil.catchException(this, "process", e);
                processValid = false;
            }
            try {
                // écriture des messages si existent
                if (!messages.isEmpty()) {
                    registerAttachedDocument(TUMessageAttach.build(messages, TUMessageAttach.EXTENTION_TXT));
                }
            } catch (IOException e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "--> ");
                transaction.addErrors(e.toString());
                JadeCodingUtil.catchException(this, "process", e);
                processValid = false;
            }
        }

        return processValid;
    }

    /**
     * Modification de l'annee
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * Modification de l'id du bouclement
     * 
     * @param idBouclement
     */
    public void setIdBouclement(String idBouclement) {
        this.idBouclement = idBouclement;
    }

    /**
     * Modifie le mois
     * 
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }

}
