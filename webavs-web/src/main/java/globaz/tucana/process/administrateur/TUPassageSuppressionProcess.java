package globaz.tucana.process.administrateur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.itucana.constantes.TUTypesBouclement;
import globaz.itucana.exception.TUInitProcessTucanaInterfaceException;
import globaz.itucana.exception.TUInterfaceException;
import globaz.itucana.process.TUProcessusBouclementAF;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.administration.TUSuppressionNoPassage;
import globaz.tucana.constantes.IPropertiesNames;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.exception.TUException;
import globaz.tucana.exception.process.TUProcessException;
import globaz.tucana.exception.process.TUProcessNoRecordFound;
import globaz.tucana.process.TUMessageAttach;
import globaz.tucana.process.message.TUMessagesContainer;
import globaz.tucana.transaction.TUTransactionHandler;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

/**
 * Process de suppression d'un passage
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUPassageSuppressionProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csApplication = "";

    private String csSuppressionReferenceAF = "";

    private String noPassage = "";

    public TUPassageSuppressionProcess() {
        super();
    }

    public TUPassageSuppressionProcess(BProcess parent) {
        super(parent);
    }

    public TUPassageSuppressionProcess(BSession session) {
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
     * Récupère le code système application
     * 
     * @return
     */
    public String getCsApplication() {
        return csApplication;
    }

    /**
     * Retourne le fait de supprimer les références AF
     * 
     * @return
     */
    public String getCsSuppressionReferenceAF() {
        return csSuppressionReferenceAF;
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
            str = new StringBuffer(getSession().getLabel("PRO_TIT_SUPPRESSION_PASSAGE_ERROR"));
        } else {
            str = new StringBuffer(getSession().getLabel("PRO_TIT_SUPPRESSION_PASSAGE"));
        }

        str.append(" : ").append(noPassage).append(" - (");
        str.append(JACalendar.todayJJsMMsAAAA()).append(")");
        return str.toString();
    }

    /**
     * Récupère le numéro de passage
     * 
     * @return
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * Lance un type de bouclement
     * 
     * @param string
     */
    private void handle(BTransaction transaction, String propertyClassName, TUTypesBouclement typeBouclement)
            throws TUInterfaceException {
        try {
            String className = null;
            try {
                className = getSession().getApplication().getProperty(propertyClassName);
            } catch (Exception e) {
                // Problème framework à récupérer la propriété
                throw new TUInitProcessTucanaInterfaceException(e);
            }
            if (className != null) {

                Constructor constructor = getClass().getClassLoader().loadClass(className)
                        .getConstructor(new Class[] { String.class, String.class, String.class });
                String annee;
                String mois;
                try {
                    annee = getNoPassage().substring(0, 4);
                    mois = getNoPassage().substring(4, 6);
                } catch (java.lang.StringIndexOutOfBoundsException e) {
                    throw new TUInitProcessTucanaInterfaceException("No passage number defined " + propertyClassName);
                }

                TUProcessusBouclementAF processus = (TUProcessusBouclementAF) constructor.newInstance(new Object[] {
                        annee, mois, getEMailAddress() });
                processus.setSession(getSession());
                processus.setTypeBouclement(typeBouclement);
                processus.processBouclementDelete(transaction);
            } else {
                throw new TUInitProcessTucanaInterfaceException("No class defined for property " + propertyClassName);
            }
        } catch (InstantiationException e) {
            // Exception levée à l'intérieur même du constructeur de la classe
            // instanciée
            throw new TUInitProcessTucanaInterfaceException(e);
        } catch (IllegalAccessException e) {
            // Constructeur private ou protected
            throw new TUInitProcessTucanaInterfaceException(e);
        } catch (ClassNotFoundException e) {
            // La propriété définie ne correspond pas à une classe permettant
            // l'instanciation
            throw new TUInitProcessTucanaInterfaceException(e);
        } catch (ClassCastException e) {
            // Signifie que la classe que l'on t'a spécifié n'est pas de type
            // TUProcessusBouclement!
            throw new TUInitProcessTucanaInterfaceException(e);
        } catch (SecurityException e) {
            // Problème de sécurité sur le constructeur -> Ne doit jamais
            // arriver :)
            throw new TUInitProcessTucanaInterfaceException(e);
        } catch (NoSuchMethodException e) {
            // Ne trouve pas le constructeur défini
            throw new TUInitProcessTucanaInterfaceException(e);
        } catch (IllegalArgumentException e) {
            // Argument non valide (problème de type de paramètres passés au
            // constructeur
            throw new TUInitProcessTucanaInterfaceException(e);
        } catch (InvocationTargetException e) {
            // Levée en cas d'exception levée en cours d'exécution du
            // constructeur
            throw new TUInitProcessTucanaInterfaceException(e);
        }
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
     * Exécute le process de suppression
     * 
     * @param transaction
     * @return
     */
    public boolean process(BTransaction transaction) {
        boolean processValid = true;

        getMemoryLog().logMessage(Calendar.getInstance().getTime().toString(), FWMessage.INFORMATION,
                transaction.getSession().getLabel("INFO_DEBUT"));

        TUMessagesContainer messages = new TUMessagesContainer();
        // supprime le numéro de passage
        try {
            // vérifie que le numéro de passage a ete saisi
            if (JadeNumericUtil.isEmptyOrZero(getNoPassage())) {
                getMemoryLog().logMessage(transaction.getSession().getLabel("ERR_SUPPR_PASSAGE"), FWMessage.ERREUR,
                        "--> ");
                JadeCodingUtil.catchException(this, "process", new TUProcessException(transaction.getSession()
                        .getLabel("ERR_SUPPR_PASSAGE")));
                processValid = false;
            } else {
                TUSuppressionNoPassage.suppression(transaction, csApplication, noPassage, messages);

                if (getCsSuppressionReferenceAF().equals(ITUCSConstantes.CS_OUI)
                        && (JadeStringUtil.isEmpty(getCsApplication()) || ITUCSConstantes.CS_APPLICATION_AF
                                .equals(getCsApplication()))) {
                    handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_AF_CLASS_PROPERTY,
                            TUTypesBouclement.BOUCLEMENT_AF);
                }

            }
        } catch (TUInterfaceException e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "! - ");
            JadeCodingUtil.catchException(this, "process", e);
            processValid = false;
        } catch (TUProcessNoRecordFound e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "! - ");
            JadeCodingUtil.catchException(this, "process", e);
            processValid = false;
        } catch (TUException e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "! - ");
            JadeCodingUtil.catchException(this, "process", e);
            processValid = false;
        } finally {
            try {
                if (!messages.isEmpty()) {
                    registerAttachedDocument(TUMessageAttach.build(messages, TUMessageAttach.EXTENTION_TXT));
                }
            } catch (IOException e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "--> ");
                JadeCodingUtil.catchException(this, "process", e);
                processValid = false;
            }
        }

        return processValid;
    }

    /**
     * Modification du code système application
     * 
     * @param string
     */
    public void setCsApplication(String string) {
        csApplication = string;
    }

    /**
     * Modifie le fait de supprimer ou non les références AF
     * 
     * @param newCsSuppressionReferenceAF
     */
    public void setCsSuppressionReferenceAF(String newCsSuppressionReferenceAF) {
        csSuppressionReferenceAF = newCsSuppressionReferenceAF;
    }

    /**
     * Modifie le numéro de passage
     * 
     * @param string
     */
    public void setNoPassage(String string) {
        noPassage = string;
    }
}
