package globaz.osiris.impl.helios;

import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.helios.api.ICGPlanComptable;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntCompteCG;

public class CAHeliosCompte implements IntCompteCG {
    private int ak = 0;
    private ICGPlanComptable compte = null;
    private String idCompte = new String();
    private String idExerciceComptable = new String();
    private String idExterneCompte = new String();
    public boolean onError = false;
    private BISession sessionHelios = null;

    /**
     * Commentaire relatif au constructeur CAHeliosCompte.
     */
    public CAHeliosCompte() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.05.2003 14:16:15)
     * 
     * @param session
     *            BISession
     */
    public CAHeliosCompte(BISession session) {
        sessionHelios = session;
    }

    /**
     * R�cup�re la description du compte dans la langue de l'utilisateur Date de cr�ation : (28.10.2002 08:57:35)
     * 
     * @return String la description du compte
     */
    @Override
    public String getDescription() {
        if (compte == null) {
            return new String();
        } else {
            return compte.getLibelle();
        }
    }

    /**
     * Ins�rez la description du compte dans la langue indiqu�e par le code ISO Date de cr�ation : (28.10.2002 09:01:57)
     * 
     * @param codeISOLangue
     *            String le code ISO de la langue
     * @return String La description du compte
     */
    @Override
    public String getDescription(String codeISOLangue) {
        if (compte == null) {
            return new String();
        } else {
            if (JadeStringUtil.isBlank(codeISOLangue)) {
                return compte.getLibelle();
            } else if (codeISOLangue.equalsIgnoreCase("FR")) {
                return compte.getLibelleFr();
            } else if (codeISOLangue.equalsIgnoreCase("DE")) {
                return compte.getLibelleDe();
            } else if (codeISOLangue.equalsIgnoreCase("IT")) {
                return compte.getLibelleIt();
            } else {
                return compte.getLibelle();
            }
        }
    }

    /**
     * R�cup�re l'identifiant du compte Date de cr�ation : (28.10.2002 08:57:02)
     * 
     * @return String l'identifiant du compte
     */
    @Override
    public String getIdCompte() {
        if (compte == null) {
            return idCompte;
        } else {
            return compte.getIdCompte();
        }
    }

    /**
     * R�cupre l'identifiant externe du compte Date de cr�ation : (28.10.2002 08:57:19)
     * 
     * @return String l'identifiant externe du compte
     */
    @Override
    public String getIdExterneCompte() {
        if (compte == null) {
            return idExterneCompte;
        } else {
            return compte.getIdExterne();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.05.2003 17:25:29)
     * 
     * @return globaz.globall.db.BSession
     */
    private BISession getSessionHelios(BISession session) throws Exception {
        // Ouvrir une nouvelle session
        if (sessionHelios == null) {
            BIApplication remoteApplication = GlobazSystem.getApplication("HELIOS");
            sessionHelios = remoteApplication.newSession(session);
        }
        return sessionHelios;
    }

    @Override
    public boolean isNew() {
        if (compte == null) {
            return false;
        } else {
            return compte.isNew();
        }
    }

    /**
     * Indiquer si l'entit� est en erreur Date de cr�ation : (24.10.2002 16:37:32)
     * 
     * @return boolean l'�tat de l'indicateur d'erreur
     */
    @Override
    public boolean isOnError() {
        return onError;
    }

    /**
     * R�cup�re un compte CG Date de cr�ation : (24.10.2002 16:32:33)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction la transaction
     * @param shouldAddErrors
     *            boolean propager les erreurs
     */
    @Override
    public void retrieve(BITransaction transaction, boolean shouldAddErrors) throws Exception {
        // Instancier un nouveau journal
        compte = (ICGPlanComptable) getSessionHelios(transaction.getISession()).getAPIFor(ICGPlanComptable.class);
        onError = false;
        // Charger les valeurs
        compte.setIdExterne(idExterneCompte);
        compte.setIdCompte(idCompte);
        compte.setIdExerciceComptable(idExerciceComptable);
        compte.setAlternateKey(ak);
        // R�cup�rer le compte
        compte.retrieve(transaction);
        // V�rifier les erreurs
        if (compte.isNew() || transaction.hasErrors()) {
            onError = true;
        }
    }

    /**
     * S�lectionne une cl� altern�e pour la r�cup�ration du compte Date de cr�ation : (28.10.2002 08:59:05)
     * 
     * @param alternateKey
     *            int le num�ro de la cl� altern�e
     */
    @Override
    public void setAlternateKey(int alternateKey) {
        ak = alternateKey;
    }

    /**
     * Affecte l'identifiant du compte Date de cr�ation : (28.10.2002 08:57:59)
     * 
     * @param newIdCompte
     *            String l'identifiant du compte
     */
    @Override
    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.05.2003 11:36:20)
     * 
     * @param newIdExerciceComptable
     *            String
     */
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    /**
     * Affecte l'identifiant externe du compte Date de cr�ation : (28.10.2002 08:58:22)
     * 
     * @param newIdExterneCompte
     *            String l'identifiant externe du compte
     */
    @Override
    public void setIdExterneCompte(String newIdExterneCompte) {
        idExterneCompte = newIdExterneCompte;
    }
}
