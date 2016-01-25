package globaz.aquila.application;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.api.ICOGestionContentieuxExterne;
import globaz.aquila.externe.COGestionContentieuxExterne;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.webavs.common.CommonProperties;
import java.rmi.RemoteException;

/**
 * Représente l'application aquila
 * 
 * @author Pascal Lovy, 01-oct-2004
 */
public class COApplication extends BApplication implements ICOApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Prefix de l'application */
    public static final String APPLICATION_AQUILA_PREFIX = "CO";
    /** Racine de l'application */
    public static final String APPLICATION_AQUILA_ROOT = "aquilaRoot";
    /** application PAVO, comptes individuels */
    public final static String DEFAULT_APPLICATION_PAVO = "PAVO";
    /** application PYXIS, tiers */
    public final static String DEFAULT_APPLICATION_PYXIS = "PYXIS";
    /** application OSIRIS, compta aux */
    public static final String KEY_SESSION_OSIRIS = "OSIRIS";

    /**
     * Renvoie une instance de l'application enregistrée dans le système
     * 
     * @return l'instance de l'application, év. null
     */
    public final static COApplication getApplicationAquila() {
        try {
            return (COApplication) GlobazServer.getCurrentSystem().getApplication(
                    ICOApplication.DEFAULT_APPLICATION_AQUILA);
        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }

    private BIApplication appOsiris = null;

    private BISession sessionOsiris = null;

    /**
     * Initialise l'application aquila
     * 
     * @throws Exception
     *             Si l'initialisation de l'application a échoué
     */
    public COApplication() throws Exception {
        super(ICOApplication.DEFAULT_APPLICATION_AQUILA);
    }

    /**
     * Initialise l'application aquila
     * 
     * @param id
     *            Paramètre ignoré
     * @throws Exception
     *             Si l'initialisation de l'application a échoué
     */
    public COApplication(String id) throws Exception {
        super(id);
    }

    /**
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
        _addAPI(ICOGestionContentieuxExterne.class, COGestionContentieuxExterne.class);
    }

    /**
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("AQUILAmenu.xml");
    }

    /**
     * @see BApplication#_initializeCustomActions()
     */
    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("aquila.batch.transition.annulerdernieretransition", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.suiviprocedure.annulerEtape.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.batch.transition.effectuertransition", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.batch.transition.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("aquila.process.processContentieux.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("aquila.process.executerJournal.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.process.annulerJournal.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.process.validerJournal.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("aquila.irrecouvrables.sections.repartir", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.irrecouvrables.sections.passerIrrecouvrable", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.irrecouvrables.sections.executer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.irrecouvrables.compteAnnexe.chercher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("aquila.irrecouvrables.recouvrementCompteAnnexe.chercher",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("naos.affiliation.affiliation.gedafficherdossier", FWSecureConstants.READ);
    }

    /**
     * @return le formater du numéro d'affilié
     * @throws Exception
     */
    public IFormatData getAffileFormater() throws Exception {
        // le format du numéro d'affilié
        IFormatData affileFormater = null;
        if (affileFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isBlank(className)) {
                affileFormater = (IFormatData) Class.forName(className).newInstance();
            }
        }
        return affileFormater;

    }

    /**
     * Retourne l'instance de l'application demandée
     * 
     * @return L'instance de l'application
     * @param id
     *            L'id de l'application
     * @exception Exception
     *                Si l'instance de l'application n'est pas trouvée
     * @deprecated utiliser les méthodes : getApplicationAquila(), getApplicationOsiris(), ...
     */
    @Deprecated
    public BIApplication getApplication(String id) throws Exception {
        return GlobazSystem.getApplication(id);
    }

    /**
     * méthode permettant d'obtenir un objet application Aquila Date de création : (15.03.2006 12:12:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationOsiris() {
        // Si application pas ouverte
        if (appOsiris == null) {
            try {
                appOsiris = GlobazSystem.getApplication("OSIRIS");
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appOsiris;
    }

    /**
     * Retourne une session permettant l'accès à une application distante
     * 
     * @param local
     *            La session locale
     * @param sessionKey
     *            La clé de l'application distante
     * @return La session distante
     * @throws Exception
     *             Si la session n'existe pas et ne peut pas être créée
     * @deprecated utiliser getSessionOsiris, ...
     */
    @Deprecated
    public BISession getRemoteSession(BSession local, String sessionKey) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(sessionKey);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(sessionKey).newSession(local);
            local.setAttribute(sessionKey, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * méthode permettant d'obetnir une session Aquila Date de création : (15.03.2006 12:15:58)
     * 
     * @return globaz.globall.api.BISession
     */
    public BISession getSessionOsiris(BISession session) {

        // Si session pas ouverte
        try {
            if ((sessionOsiris == null) || !sessionOsiris.getIdLangueISO().equalsIgnoreCase(session.getIdLangueISO())) {
                try {
                    sessionOsiris = getApplicationOsiris().newSession(session);
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                }
            }
        } catch (RemoteException e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
        }

        return sessionOsiris;
    }

    /**
     * Utiliser à la création du numéro d'un administrateur. <br/>
     * Si true, remplace le -00 de la fin par -01 de l'administrateur (FER-CIAM) <br/>
     * Si false, ajoute à la suite du numéro le -01 de l'administrateur (Autres) <br/>
     * Par défaut : true <br/>
     * 
     * @return <code>true</code> s'il faut remplacer -XX par le numéro de l'administrateur.
     * @throws Exception
     * @throws RemoteException
     */
    public boolean numeroAdministrateurReplaceTiret() throws RemoteException, Exception {
        return !"false".equalsIgnoreCase(GlobazServer.getCurrentSystem()
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA)
                .getProperty("numeroAdministrateurReplaceTiret"));
    }

    /**
     * @return <code>true</code> si les échéances doivent être validées
     * @throws Exception
     * @throws RemoteException
     */
    public boolean validerEcheance() throws RemoteException, Exception {
        return !"false".equalsIgnoreCase(GlobazServer.getCurrentSystem()
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA).getProperty("validerEcheance"));
    }
}
