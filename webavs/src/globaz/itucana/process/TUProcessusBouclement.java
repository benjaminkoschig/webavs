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
 * Classe abstraite devant être étendue pour la génération d'un bouclement
 * 
 * @author fgo date de création : 13 juin 06
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
     * Récupère l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee != null ? annee : "";
    }

    /**
     * Récupère le bouclement
     * 
     * @return le bouclement
     */
    public ITUModelBouclement getBouclement() {
        return bouclement;
    }

    /**
     * Récupère une modèle de bouclement
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    protected final ITUModelBouclement getBouclementImpl() throws TUInitTucanaInterfaceException {
        return TUModelLoader.getNewBouclementModel();
    }

    /**
     * Récupère l'adresse e-mail
     * 
     * @return peut retourner vide si pas passé dans le constructeur
     */
    public String getEMail() {
        return eMail != null ? eMail : "";
    }

    /**
     * Récupère le mémory log
     * 
     * @return
     */
    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    /**
     * Récupère le mois
     * 
     * @return
     */
    public String getMois() {
        return mois != null ? mois : "";
    }

    /**
     * Récupère la session
     * 
     * @return
     */
    public final BSession getSession() {
        return session;
    }

    /**
     * Récuère le type de bouclement
     * 
     * @return
     */
    public TUTypesBouclement getTypeBouclement() {
        return typeBouclement;
    }

    /**
     * Initialisation du bouclement devant être implémentée
     * 
     * @param bouclement
     * @param annee
     * @param mois
     * @throws TUModelInstanciationException
     */
    protected abstract void initBouclement(ITUModelBouclement bouclement) throws TUModelInstanciationException;

    /**
     * Méthode récupérant l'implémentation du bouclement et exécutant le bouclement
     * 
     * @param transaction
     * @throws TUInterfaceException
     */
    public final void processBouclement(BTransaction transaction) throws TUInterfaceException {
        // Récupère l'instance de l'implémentation définie pour le modèle de
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
     * Ajoute le mémory log du process
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
