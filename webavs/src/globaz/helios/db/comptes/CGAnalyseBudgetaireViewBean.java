package globaz.helios.db.comptes;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 17 mai 04
 * 
 * @author scr
 * 
 */
public class CGAnalyseBudgetaireViewBean implements globaz.framework.bean.FWViewBeanInterface, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class PeriodeHelper {
        public String codePeriode = "";
        public String idSolde = "";
        public String idTypePeriode = "";
        public String montantBudget = "";
    }

    private String idCompte = null;
    private String idExerciceComptable = null;
    private String idExterne = null;

    private String idMandat = null;
    private String message = "";
    private String montantAnalyseBudgetaireAnnuelle = null;
    private String msgType = "";
    private List periodesBudgetees = null;
    private BSession session = null;

    /**
     * Constructor for CGAnalyseBudgetaireViewBean.
     */
    public CGAnalyseBudgetaireViewBean() {
        super();
        periodesBudgetees = new ArrayList();
    }

    public void addBudgetPeriode(String idSolde, String code, String idTypePeriode, String montantBudget) {
        PeriodeHelper entry = new PeriodeHelper();
        entry.idSolde = idSolde;
        entry.codePeriode = code;
        entry.idTypePeriode = idTypePeriode;
        entry.montantBudget = montantBudget;
        periodesBudgetees.add(entry);
    }

    public String geIdSoldePeriode(int index) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        return ph.idSolde;
    }

    public String getCodePeriode(int index) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        return ph.codePeriode;
    }

    /**
     * Returns the idCompte.
     * 
     * @return String
     */
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idExterne.
     * 
     * @return String
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    public String getIdTypePeriode(int index) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        return ph.idTypePeriode;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Returns the montantAnalyseBudgetaireAnnuelle.
     * 
     * @return String
     */
    public String getMontantAnalyseBudgetaireAnnuelle() {
        return montantAnalyseBudgetaireAnnuelle == null ? "" : montantAnalyseBudgetaireAnnuelle;
    }

    public String getMontantBudgetePeriode(int index) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        return ph.montantBudget;
    }

    /**
     * Renvoie le type de message contenu dans le bean (utilisé seulement si l'application utilise des View Beans)
     * 
     * @return le type de message
     */
    @Override
    public final String getMsgType() {
        return msgType;
    }

    public int getPeriodeSize() {
        return periodesBudgetees.size();
    }

    /**
     * Dummy methode.
     * 
     * Utilisé pour comptatibilité, car les pages jsp font appel à cette methode bien qu'un viewBean n'ai pas à
     * implémenter cette méthode !!!.
     * 
     */
    public BSpy getSpy() {
        return null;
    }

    public void seIdSoldePeriode(int index, String idSolde) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        ph.idSolde = idSolde;
        periodesBudgetees.remove(index);
        periodesBudgetees.add(index, ph);
    }

    public void setCodePeriode(int index, String code) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        ph.codePeriode = code;
        periodesBudgetees.remove(index);
        periodesBudgetees.add(index, ph);
    }

    /**
     * Sets the idCompte.
     * 
     * @param idCompte
     *            The idCompte to set
     */
    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Sets the idExterne.
     * 
     * @param idExterne
     *            The idExterne to set
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    public void setIdTypePeriode(int index, String idTypePeriode) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        ph.idTypePeriode = idTypePeriode;
        periodesBudgetees.remove(index);
        periodesBudgetees.add(index, ph);
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        if (newSession instanceof BSession) {
            setSession((BSession) newSession);
        } else {
            try {
                setSession(new BSession(newSession.getApplicationId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Définit le message d'erreur du bean (utilisé seulement si l'application utilise des View Beans)
     * 
     * @param newMessage
     *            le nouveau message d'erreur
     */
    @Override
    public final void setMessage(String newMessage) {
        message = newMessage;
    }

    /**
     * Sets the montantAnalyseBudgetaireAnnuelle.
     * 
     * @param montantAnalyseBudgetaireAnnuelle
     *            The montantAnalyseBudgetaireAnnuelle to set
     */
    public void setMontantAnalyseBudgetaireAnnuelle(String montantAnnuel) {
        montantAnalyseBudgetaireAnnuelle = montantAnnuel;
    }

    public void setMontantBudgetePeriode(int index, String montant) {
        PeriodeHelper ph = (PeriodeHelper) periodesBudgetees.get(index);
        ph.montantBudget = montant;
        periodesBudgetees.remove(index);
        periodesBudgetees.add(index, ph);
    }

    /**
     * Définit le type de message du bean (utilisé seulement si l'application utilise des View Beans)
     * 
     * @param newMessage
     *            le nouveau type de message
     */
    @Override
    public final void setMsgType(String newMsgType) {
        msgType = newMsgType;
    }

    /**
     * Modifie la session en cours
     * 
     * @param newSession
     *            la nouvelle session
     */
    public void setSession(BSession newSession) {
        session = newSession;
    }

}