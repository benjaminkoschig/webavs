package globaz.phenix.reprise;

import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author btc
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPProcessRepriseCIVAF extends BProcess {
    /**
     * Lancement de la facturation pour un passage Date de création : (29.04.2003 09:14:04)
     * 
     * @param args
     *            java.lang.String[] - N° de passage
     */
    public static void main(String[] args) {
        CPProcessRepriseCIVAF process = null;
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
            System.out.println("id assurance AF: " + args[3]);
            System.out.println("forIdAffiliation: " + args[4]);
            System.out.println("num caisse AF: " + args[5]);
            BSession session = (BSession) GlobazSystem.getApplication("NAOS").newSession(user, pwd);
            System.out.println("Reprise started...");
            session.connect(user, pwd);
            process = new CPProcessRepriseCIVAF();
            process.setSession(session);
            process.setEMailAddress(email);
            if (!"0".equalsIgnoreCase(args[3])) {
                process.setIdAssuranceAF(args[3]);
            }
            if (!"0".equalsIgnoreCase(args[4])) {
                process.setForIdAffiliation(args[4]);
            }
            if (!"0".equalsIgnoreCase(args[5])) {
                process.setNumCaisseAF(args[5]);
            }
            if (!JadeStringUtil.isBlankOrZero(process.getIdAssuranceAF())) {
                process.executeProcess();
            } else {
                System.out.println("l'id assurance AF n'est pas renseignée");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("La reprise CIVAF est terminée.");
        }
        System.exit(0);
    }

    private java.lang.String descriptionTiers = "";
    private java.lang.String forIdAffiliation = "";
    private java.lang.String idAssuranceAF = "";
    private Vector messages = new Vector();

    private java.lang.String numCaisseAF = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessRepriseCIVAF() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessRepriseCIVAF(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessRepriseCIVAF(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * @param manager
     * @return
     */
    public boolean _executeBoucleRetour(AFSuiviCaisseAffiliationManager manager) throws Exception {

        BStatement statement = null;
        // Sous controle d'exceptions
        BTransaction transactionLecture = (BTransaction) getSession().newTransaction();
        try {
            int maxScale = manager.getCount(getTransaction());
            if (maxScale > 0) {
                setProgressScaleValue(maxScale);
            } else {
                setProgressScaleValue(1);
            }
            // disabler le spy
            getTransaction().disableSpy();
            // itérer sur toutes les affiliations
            statement = manager.cursorOpen(getTransaction());
            // Recherche si revenuAutre = revenu AF (ex cas spéciaux AF pour GE)
            AFSuiviCaisseAffiliation suivi = null;
            transactionLecture = (BTransaction) getSession().newTransaction();
            transactionLecture.openTransaction();
            statement = manager.cursorOpen(transactionLecture);
            while (((suivi = (AFSuiviCaisseAffiliation) manager.cursorReadNext(statement)) != null) && (!suivi.isNew())
                    && !isAborted()) {
                try {
                    //
                    AFAffiliation aff = new AFAffiliation();
                    aff.setAffiliationId(suivi.getAffiliationId());
                    aff.setSession(getSession());
                    aff.retrieve();
                    if (!aff.isNew() && (aff != null)) {
                        // Recherche coti AVS paritaire
                        AFCotisation cotiAf = aff._cotisation(getTransaction(), aff.getAffiliationId(),
                                CodeSystem.GENRE_ASS_PARITAIRE, CodeSystem.TYPE_ASS_COTISATION_AVS_AI, "01.01.2011",
                                "31.12.2011", 1);
                        if (cotiAf != null) {
                            // Suppression ancienne assurance AF
                            AFCotisationManager afMng = new AFCotisationManager();
                            afMng.setSession(getSession());
                            afMng.setForAffiliationId(cotiAf.getAffiliationId());
                            afMng.setForPlanAffiliationId(cotiAf.getPlanAffiliationId());
                            afMng.setForAssuranceId(getIdAssuranceAF());
                            afMng.find();
                            int nb = afMng.getCount();
                            if (nb > 1) {
                                System.out.println("Plusieurs asurances AF trouvées:" + aff.getAffilieNumero());
                                this._addError("Plusieurs asurances AF trouvées:" + aff.getAffilieNumero());
                            } else if (nb == 1) {
                                ((AFCotisation) afMng.getFirstEntity()).delete();
                            }
                            cotiAf.setAssuranceId(getIdAssuranceAF());
                            cotiAf.setTauxAssuranceId("");
                            if (BSessionUtil.compareDateFirstLower(getSession(), cotiAf.getDateDebut(),
                                    "01.01.2011")) {
                                cotiAf.setDateDebut("01.01.2011");
                            }
                            cotiAf.add(getTransaction());
                        } else {
                            System.out.println("Coti AVS Paritaire non trouvée:" + aff.getAffilieNumero());
                            this._addError("Coti AVS Paritaire non trouvée - " + aff.getAffilieNumero());
                        }
                        // Mise à jour affiliation
                        if (CodeSystem.TYPE_AFFILI_FICHIER_CENT.equalsIgnoreCase(aff.getTypeAffiliation())) {
                            aff.setTypeAffiliation(CodeSystem.TYPE_AFFILI_EMPLOY);
                            aff.update(getTransaction());
                        }
                    }
                    incProgressCounter();
                    if (getSession().hasErrors()) {
                        getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    }
                    if (getTransaction().hasErrors()) {
                        getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    } else if (isAborted()) {
                        getTransaction().rollback();
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage() + "\n" + suivi.getSuiviCaisseId(), FWMessage.FATAL,
                            this.getClass().getName());
                    getTransaction().rollback();
                } finally {
                    for (Iterator iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                        messages.add(iter.next());
                    }
                    getMemoryLog().clear();
                }
            }
            // rétablir le contrôle du spy
            getTransaction().enableSpy();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            System.out.println(e.toString());
        } // Fin de la procédure
        catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                try {
                    manager.cursorClose(statement);
                } finally {
                    if (transactionLecture != null) {
                        transactionLecture.closeTransaction();
                    }
                    manager = null;
                    statement = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return !isOnError();
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        TIAdministrationManager admMng = new TIAdministrationManager();
        admMng.setSession(getSession());
        admMng.setForCodeAdministration(getNumCaisseAF());
        admMng.setForGenreAdministration("509030");
        admMng.find();
        if (admMng.getCount() == 1) {
            String idCaisse = ((TIAdministrationViewBean) admMng.getFirstEntity()).getIdTiersAdministration();
            if (!JadeStringUtil.isBlankOrZero(idCaisse)) {
                AFSuiviCaisseAffiliationManager suiManager = new AFSuiviCaisseAffiliationManager();
                suiManager.setSession(getSession());
                suiManager.setForGenreCaisse(CodeSystem.GENRE_CAISSE_AF);
                suiManager.setForIdTiersCaisse(idCaisse);
                suiManager.setForAffiliationId(getForIdAffiliation());
                suiManager.setForAnneeActive("2011");
                suiManager.changeManagerSize(0);
                _executeBoucleRetour(suiManager);
            }
        }
        // Remettre les erreurs des process dans le log
        for (Iterator iter = messages.iterator(); iter.hasNext();) {
            getMemoryLog().getMessagesToVector().add(iter.next());
        }
        // Arrêter le traitement à la première exception de la transaction
        if (getTransaction().hasErrors()) {
            return false;
        }
        return !isOnError();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionTiers() {
        return descriptionTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";
        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("PROCRECEPTGENDEC_EMAIL_OBJECT_FAILED");
        } else {
            obj = getSession().getLabel("SUJET_EMAIL_RECEPTION_CALCUL");
        }
        // Restituer l'objet
        return obj;
    }

    /**
     * @return
     */
    public java.lang.String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public java.lang.String getIdAssuranceAF() {
        return idAssuranceAF;
    }

    public java.lang.String getNumCaisseAF() {
        return numCaisseAF;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @param newDescriptionTiers
     *            java.lang.String
     */
    public void setDescriptionTiers(java.lang.String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    /**
     * @param string
     */
    public void setForIdAffiliation(java.lang.String string) {
        forIdAffiliation = string;
    }

    public void setIdAssuranceAF(java.lang.String idAssuranceAF) {
        this.idAssuranceAF = idAssuranceAF;
    }

    public void setNumCaisseAF(java.lang.String numCaisseAF) {
        this.numCaisseAF = numCaisseAF;
    }
}
