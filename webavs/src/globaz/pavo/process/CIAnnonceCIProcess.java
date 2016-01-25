package globaz.pavo.process;

import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.utils.StringUtils;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceSuspens;
import globaz.pavo.db.compte.CIAnnonceWrapper;

/**
 * Annonce d'un CI. En développement, ne pas utiliser Date de création : (25.11.2002 11:52:37)
 * 
 * @author: Administrator
 */
public class CIAnnonceCIProcess extends BProcess {

    private static final long serialVersionUID = -5874170214005451459L;
    private boolean assure = true;
    private boolean echoToConsole = false;

    private String idAnnonceHermes = "";

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIAnnonceCIProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public CIAnnonceCIProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        BITransaction remoteTransaction = null;
        try {
            if (echoToConsole) {
                System.out.println("starting CI process...");
            }
            // init
            CIApplication application = (CIApplication) getSession().getApplication();
            BISession remoteSession = application.getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();
            IHEOutputAnnonce remoteAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteAnnonce.setIdAnnonce(idAnnonceHermes);
            remoteAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable", "getUtilisateur" });
            remoteAnnonce.retrieve(remoteTransaction);
            String motif = remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE);
            String numero_assure = remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
            String numero_caisse = StringUtils.formatCaisseAgence(
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE));
            String numero_caisse_commettante = StringUtils.formatCaisseAgence(
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
            String numero_assure_complete = remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE);
            // String date =
            // DateUtils.convertDate(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAAAA),
            // DateUtils.AAAAMMJJ, DateUtils.JJMMAAAA_DOTS);
            CIAnnonceSuspens ciSus = new CIAnnonceSuspens();
            ciSus.setSession(getSession());
            ciSus.setIdAnnonce(remoteAnnonce.getIdAnnonce());
            ciSus.setAnnonceSuspens(new Boolean(false));
            switch (Integer.parseInt(remoteAnnonce.getField(IHEAnnoncesViewBean.CODE_APPLICATION))) {
                case 21: {
                    ciSus.setCodeApplication("21");
                    ciSus.setNumeroAvs(numero_assure);
                    ciSus.setIdMotifArc(motif);
                    ciSus.setNumeroCaisse(numero_caisse);
                    // ciSus.setDateReception(date);
                    break;
                }
                case 22: {
                    ciSus.setCodeApplication("22");
                    ciSus.setNumeroAvs(numero_assure);
                    ciSus.setIdMotifArc(motif);
                    // numero_caisse_commettante =
                    // StringUtils.formatCaisseAgence(inputAnnonce.getField(inputAnnonce.NUMERO_CAISSE_COMMETTANTE),
                    // inputAnnonce.getField(inputAnnonce.NUMERO_AGENCE_COMMETTANTE));
                    ciSus.setNumeroCaisse(numero_caisse_commettante);
                    // ciSus.setDateReception(date);
                    break;
                }
                case 23: {
                    ciSus.setCodeApplication("23");
                    ciSus.setNumeroAvs(numero_assure_complete);
                    ciSus.setIdMotifArc(motif);
                    ciSus.setNumeroCaisse(numero_caisse);
                    // ciSus.setDateReception(date);
                    break;
                }
                case 29: {
                    ciSus.setCodeApplication("29");
                    ciSus.setNumeroAvs(numero_assure);
                    ciSus.setIdMotifArc(motif);
                    ciSus.setNumeroCaisse(numero_caisse_commettante);
                    // ciSus.setDateReception(date);
                    break;
                }
            }

            CIAnnonceWrapper annonceWrapper = ciSus.getWrapper();
            annonceWrapper.setApplication(application);
            annonceWrapper.setRemoteSession(remoteSession);
            annonceWrapper.setRemoteTransaction(remoteTransaction);
            annonceWrapper.setRemoteAnnonce(remoteAnnonce);
            if (assure) {
                // assuré
                annonceWrapper.annonceExtraitCI(getTransaction(), null, false);
            } else {
                // conjoint
                annonceWrapper.annonceExtraitCI(getTransaction(), null, true);
            }
        } catch (Exception e) {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.rollback();
                } catch (Exception ex) {
                }
            }
            if (echoToConsole) {
                e.printStackTrace();
            }
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
        } finally {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception ex) {
                }
            }
        }
        if (echoToConsole) {
            System.out.println("Process done.");
        }
        return !isAborted();
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (isOnError()) {
            return "Le traitement journalier des comptes individuels a echoué!";
        } else {
            return "Le traitement journalier des comptes individuels s'est effectué avec succès.";
        }
    }

    /**
     * Returns the idAnnonceHermes.
     * 
     * @return String
     */
    public String getIdAnnonceHermes() {
        return idAnnonceHermes;
    }

    /**
     * Returns the assure.
     * 
     * @return boolean
     */
    public boolean isAssure() {
        return assure;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Sets the assure.
     * 
     * @param assure
     *            The assure to set
     */
    public void setAssure(boolean assure) {
        this.assure = assure;
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     * 
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /**
     * Sets the idAnnonceHermes.
     * 
     * @param idAnnonceHermes
     *            The idAnnonceHermes to set
     */
    public void setIdAnnonceHermes(String idAnnonceHermes) {
        this.idAnnonceHermes = idAnnonceHermes;
    }

}
