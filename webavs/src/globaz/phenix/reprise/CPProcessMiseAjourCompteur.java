package globaz.phenix.reprise;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteur;
import globaz.phenix.db.principale.CPDecisionAffiliationCotisation;
import globaz.phenix.db.principale.CPDecisionAffiliationCotisationManager;
import java.math.BigDecimal;

/**
 * Process de g�naration d'une liste excel pour les concordances entre Cot.Pers. et CI Date de cr�ation : (17.06.2007
 * 08:34:14)
 * 
 * @author: jpa
 */
public class CPProcessMiseAjourCompteur extends BProcess {
    public static void main(String[] args) {
        CPProcessMiseAjourCompteur process = null;
        String user = "";
        String pwd = "";
        String email = "hna@globaz.ch";
        try {
            user = args[0];
            pwd = args[1];
            email = args[2];
            System.out.println("User : " + user);
            System.out.println("Password : " + pwd);
            System.out.println("Email : " + email);
            System.out.println("depuis l'affili� : " + args[3]);
            System.out.println("jusqu'� l'affili� : " + args[4]);
            System.out.println("depuis l'ann�e : " + args[5]);
            System.out.println("jusqu'� l'ann�e : " + args[6]);
            System.out.println("pour la d�cision n� : " + args[7]);
            BSession session = (BSession) GlobazSystem.getApplication("PHENIX").newSession(user, pwd);
            System.out.println("Reprise started...");
            session.connect(user, pwd);
            process = new CPProcessMiseAjourCompteur();
            process.setSession(session);
            process.setEMailAddress(email);
            if (!"0".equalsIgnoreCase(args[3])) {
                process.setFromAffilieDebut(args[3]);
            }
            if (!"0".equalsIgnoreCase(args[4])) {
                process.setTillAffilieFin(args[4]);
            }
            if (!"0".equalsIgnoreCase(args[5])) {
                process.setFromAnneeDecision(args[5]);
            }
            if (!"0".equalsIgnoreCase(args[6])) {
                process.setToAnneeDecision(args[6]);
            }
            if (!"0".equalsIgnoreCase(args[7])) {
                process.setForIdDecision(args[7]);
            }
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("La mise � jour des compteurs est termin�e");
        }
        System.exit(0);
    }

    private String forIdDecision = "";
    // Num�ro d'affili� de d�part
    private String fromAffilieDebut = "";

    // Ann�e de d�but de la recherche
    private String fromAnneeDecision = "";

    // Num�ro d'affili� de fin
    private String tillAffilieFin = "";
    // Ann�e de fin de la recherche
    private String toAnneeDecision = "";

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     */
    public CPProcessMiseAjourCompteur() {
        super();
    }

    /**
     * Commentaire relatif au constructeur TIExportProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessMiseAjourCompteur(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public CPProcessMiseAjourCompteur(BSession session) {
        super(session);
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionAffiliationCotisationManager manager = new CPDecisionAffiliationCotisationManager();
            manager.setSession(getSession());
            manager.setFromAnneeDecision(getFromAnneeDecision());
            manager.setTillAnneeDecision(getToAnneeDecision());
            manager.setFromNoAffilie(getFromAffilieDebut());
            manager.setTillNoAffilie(getTillAffilieFin());
            manager.setForIdDecision(getForIdDecision());
            manager.setIsActive(Boolean.TRUE);
            // !!! Mettre en premier l'ordre par idTiers � cause des affili�s
            // qui changent de n�
            manager.orderByIdTiers();
            manager.orderByNoAffilie();
            manager.orderByAnnee();
            manager.orderByIdDecision();
            manager.changeManagerSize(0);
            manager.find();
            String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                    getSession().getApplication());
            for (int i = 0; i < manager.getSize(); i++) {
                CPDecisionAffiliationCotisation dec = (CPDecisionAffiliationCotisation) manager.getEntity(i);
                // Recherche du compte annexe
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.setIdRole(role);
                compte.setIdExterneRole(dec.getNumAffilie());
                compte.wantCallMethodBefore(false);
                compte.retrieve(getTransaction());

                String idRub = AFCotisation._getRubrique(dec.getIdCotiAffiliation(), getSession());
                if (!JadeStringUtil.isEmpty(idRub)) {
                    String montantCompteur = "0";
                    CACompteur compteur = new CACompteur();
                    compteur.setSession(getSession());
                    compteur.setAlternateKey(1);
                    compteur.setAnnee(dec.getAnneeDecision());
                    compteur.setIdCompteAnnexe(compte.getIdCompteAnnexe());
                    compteur.setIdRubrique(idRub);
                    compteur.retrieve();
                    if (!compteur.isNew() && !compteur.hasErrors()) {
                        BigDecimal montantCoti = new BigDecimal(JANumberFormatter.deQuote(dec.getMontantAnnuel()));
                        BigDecimal montantCpt = new BigDecimal(JANumberFormatter.deQuote(montantCompteur));
                        if (montantCoti.compareTo(montantCpt) != 0) {
                            compteur.setCumulCotisation(dec.getMontantAnnuel());
                            compteur.update();
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires) On va compter le nombre d'inscriptions
     */
    @Override
    protected void _validate() throws Exception {
        // Contr�le du mail
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        // Si aucun crit�re de s�lection => erreur
        // if(JadeStringUtil.isEmpty(getFromAnneeDecision())
        // &&JadeStringUtil.isEmpty(getToAnneeDecision())
        // &&JadeStringUtil.isEmpty(getFromAffilieDebut())
        // &&JadeStringUtil.isEmpty(getFromAffilieFin())){
        // getSession().addError(getSession().getLabel("SELECTION_INCOMPLETE"));
        // }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Return le sujet de l'email Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return "Pb lors de la mise � jour des compteurs";
        } else {
            return "Mise a jour compteur effectu�";
        }
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * Returns le num�ro d'affili� de d�part
     * 
     * @return String
     */
    public String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    /**
     * Returns le num�ro d'affili� de fin.
     * 
     * @return String
     */
    public String getTillAffilieFin() {
        return tillAffilieFin;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    /**
     * Sets the genreDecision.
     * 
     * @param num�ro
     *            d'affili� de d�part
     */
    public void setFromAffilieDebut(String fromNumAffilie) {
        fromAffilieDebut = fromNumAffilie;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    /**
     * Sets the genreDecision.
     * 
     * @param NumAffilie
     *            le num�ro d'affili� de fin
     */
    public void setTillAffilieFin(String toNumAffilie) {
        tillAffilieFin = toNumAffilie;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }
}
