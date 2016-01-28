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
import globaz.itucana.process.TUProcessusBouclement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.constantes.IPropertiesNames;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.transaction.TUTransactionHandler;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Classe process lancant un bouclement
 * 
 * @author fgo date de création : 13 juin 06
 * @version : version 1.0
 */
public class TUImportationBouclementProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String annee = null;

    private String csAgence = "";

    private String csApplication = "";

    private String mois = null;

    /**
	 * 
	 */
    public TUImportationBouclementProcess() {
        super();
    }

    /**
     * @param parent
     */
    public TUImportationBouclementProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public TUImportationBouclementProcess(BSession session) {
        super(session);
    }

    /**
     * Constructeur
     */
    public TUImportationBouclementProcess(String _annee, String _mois) {
        super();
        annee = _annee;
        mois = _mois;

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
                if (transaction.isOpened()) {
                    transaction.commit();
                }
            }
        };

        try {
            transactionHandler.execute();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "process", e);
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ERROR"), FWViewBeanInterface.ERROR, "-->");
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, "-->");
            creeDocAttache(e);
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

    private void creeDocAttache(Exception exception) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
        StringBuffer sauvegardeTxt = new StringBuffer(Jade.getInstance().getHomeDir()).append("logs/")
                .append(format.format(Calendar.getInstance().getTime())).append(".txt");

        java.io.FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(sauvegardeTxt.toString());
        } catch (FileNotFoundException e3) {
            getMemoryLog().logMessage(e3.toString(), FWViewBeanInterface.ERROR, "Erreur création fichier joint");
        }
        try {
            fos.write(exception.toString().getBytes());
        } catch (IOException e4) {
            getMemoryLog().logMessage(e4.toString(), FWViewBeanInterface.ERROR, "Erreur création fichier joint");
        }
        try {
            fos.flush();
        } catch (IOException e5) {
            getMemoryLog().logMessage(e5.toString(), FWViewBeanInterface.ERROR, "Erreur création fichier joint");
        }
        try {
            fos.close();
        } catch (IOException e6) {
            getMemoryLog().logMessage(e6.toString(), FWViewBeanInterface.ERROR, "Erreur création fichier joint");
        }
        try {
            registerAttachedDocument(sauvegardeTxt.toString());
        } catch (IOException e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, "Erreur création fichier joint");
        }
    }

    /**
     * Récupère l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Récupère le code système de l'agence
     * 
     * @return
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * Récupération du code système application
     * 
     * @return
     */
    public String getCsApplication() {
        return csApplication;
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
            str = new StringBuffer(getSession().getLabel("PRO_TIT_IMPORTATION_BOUCLEMENT_ERROR"));
        } else {
            str = new StringBuffer(getSession().getLabel("PRO_TIT_IMPORTATION_BOUCLEMENT"));
        }
        str.append(" pour l'année : ").append(getAnnee());
        str.append(" pour le mois : ").append(getMois());
        str.append(" - (");
        str.append(JACalendar.todayJJsMMsAAAA()).append(")");
        return str.toString();

    }

    /**
     * Récupère le mois
     * 
     * @return
     */
    public String getMois() {
        return mois;
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
                TUProcessusBouclement processus = null;
                if (TUTypesBouclement.BOUCLEMENT_AF.getTypeBouclement().equals(typeBouclement.getTypeBouclement())) {
                    Constructor constructor = getClass().getClassLoader().loadClass(className)
                            .getConstructor(new Class[] { String.class, String.class, String.class });
                    processus = (TUProcessusBouclement) constructor.newInstance(new Object[] { annee, mois,
                            getEMailAddress() });
                } else {
                    Constructor constructor = getClass().getClassLoader().loadClass(className)
                            .getConstructor(new Class[] { String.class, String.class, });
                    processus = (TUProcessusBouclement) constructor.newInstance(new Object[] { annee, mois });
                }

                processus.setSession(getSession());
                processus.setMemoryLog(getMemoryLog());
                processus.setTypeBouclement(typeBouclement);
                processus.processBouclement(transaction);
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
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Lance le bouclement pour chaque type de bouclement (AF, CA, CG et ACM)
     * 
     * @param transaction
     * @return
     */
    private boolean process(BTransaction transaction) {
        boolean processValid = true;
        getMemoryLog().logMessage(Calendar.getInstance().getTime().toString(), FWMessage.INFORMATION,
                transaction.getSession().getLabel("INFO_DEBUT"));

        try {
            // A lancer l'ensemble du bouclement
            if (JadeStringUtil.isEmpty(getCsApplication())) {
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_AF_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_AF);
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_CG_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_CG);
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_CA_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_CA);
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_ACM_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_ACM);
            } else if (ITUCSConstantes.CS_APPLICATION_AF.equals(getCsApplication())) {
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_AF_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_AF);
            } else if (ITUCSConstantes.CS_APPLICATION_CG.equals(getCsApplication())) {
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_CG_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_CG);
            } else if (ITUCSConstantes.CS_APPLICATION_CA.equals(getCsApplication())) {
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_CA_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_CA);
            } else if (ITUCSConstantes.CS_APPLICATION_ACM.equals(getCsApplication())) {
                handle(transaction, IPropertiesNames.BOUCLEMENT_PROCESSUS_ACM_CLASS_PROPERTY,
                        TUTypesBouclement.BOUCLEMENT_ACM);
            }
        } catch (TUInterfaceException e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
                processValid = false;
            }
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "--> ");
            JadeCodingUtil.catchException(this, "process", e);
            processValid = false;
        }
        return processValid;
    }

    /**
     * Modification de l'année
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * Modifie le code système de l'agence
     * 
     * @param csAgence
     */
    public void setCsAgence(String csAgence) {
        this.csAgence = csAgence;
    }

    /**
     * Modification de l'application
     * 
     * @param string
     */
    public void setCsApplication(String string) {
        csApplication = string;
    }

    /**
     * Modification du mois
     * 
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }

}
