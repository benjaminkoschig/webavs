package globaz.itucana.process;

import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.itucana.constantes.TUTypesBouclement;
import globaz.itucana.exception.TUInitTucanaInterfaceException;
import globaz.itucana.exception.TUInterfaceException;
import globaz.itucana.exception.TUModelInstanciationException;
import globaz.itucana.model.ITUModelBouclement;
import globaz.itucana.model.TUModelLoader;
import globaz.itucana.service.TUServiceLocator;

/**
 * Classe abstraite devant �tre �tendue pour la g�n�ration d'un bouclement
 * 
 * @author fgo date de cr�ation : 13 juin 06
 * @version : version 1.0
 * 
 */
public abstract class TUProcessusBouclement {

    private String annee = null;

    private ITUModelBouclement bouclement = null;

    private String eMail = null;

    private FWMemoryLog memoryLog = null;

    private String mois = null;

    private BSession session = null;

    private TUTypesBouclement typeBouclement = null;

    /**
     * Constructeur de la classe process
     * 
     * @param _annee
     * @param _mois
     */
    public TUProcessusBouclement(String _annee, String _mois) {
        this(_annee, _mois, "");
    }

    /**
     * Constructeur
     * 
     * @param _annee
     * @param _mois
     * @param _eMail
     */
    public TUProcessusBouclement(String _annee, String _mois, String _eMail) {
        super();
        annee = _annee;
        mois = _mois;
        eMail = _eMail;
    }

    /**
     * R�cup�re l'ann�e
     * 
     * @return
     */
    public String getAnnee() {
        return annee != null ? annee : "";
    }

    /**
     * R�cup�re le bouclement
     * 
     * @return le bouclement
     */
    public ITUModelBouclement getBouclement() {
        return bouclement;
    }

    /**
     * R�cup�re une mod�le de bouclement
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    protected final ITUModelBouclement getBouclementImpl() throws TUInitTucanaInterfaceException {
        return TUModelLoader.getNewBouclementModel();
    }

    /**
     * R�cup�re l'adresse e-mail
     * 
     * @return peut retourner vide si pas pass� dans le constructeur
     */
    public String getEMail() {
        return eMail != null ? eMail : "";
    }

    /**
     * R�cup�re le m�mory log
     * 
     * @return
     */
    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    /**
     * R�cup�re le mois
     * 
     * @return
     */
    public String getMois() {
        return mois != null ? mois : "";
    }

    /**
     * R�cup�re la session
     * 
     * @return
     */
    public final BSession getSession() {
        return session;
    }

    /**
     * R�cu�re le type de bouclement
     * 
     * @return
     */
    public TUTypesBouclement getTypeBouclement() {
        return typeBouclement;
    }

    /**
     * Initialisation du bouclement devant �tre impl�ment�e
     * 
     * @param bouclement
     * @param annee
     * @param mois
     * @throws TUModelInstanciationException
     */
    protected abstract void initBouclement(ITUModelBouclement bouclement) throws TUModelInstanciationException;

    /**
     * M�thode r�cup�rant l'impl�mentation du bouclement et ex�cutant le bouclement
     * 
     * @param transaction
     * @throws TUInterfaceException
     */
    public final void processBouclement(BTransaction transaction) throws TUInterfaceException {
        // R�cup�re l'instance de l'impl�mentation d�finie pour le mod�le de
        // bouclement
        bouclement = getBouclementImpl();
        // Initialise le contenu
        initBouclement(bouclement);

        TUServiceLocator.getService(typeBouclement).handleBouclement(transaction, bouclement);
    }

    /**
     * Modifie le bouclement
     * 
     * @param bouclement
     */
    protected void setBouclement(ITUModelBouclement bouclement) {
        this.bouclement = bouclement;
    }

    /**
     * Ajoute le m�mory log du process
     * 
     * @param log
     */
    public void setMemoryLog(FWMemoryLog log) {
        memoryLog = log;
    }

    /**
     * Modifie la session
     * 
     * @param _session
     */
    public final void setSession(BSession _session) {
        session = _session;
    }

    /**
     * Modifie le type de bouclement
     * 
     * @param bouclement
     */
    public void setTypeBouclement(TUTypesBouclement bouclement) {
        typeBouclement = bouclement;
    }
}
