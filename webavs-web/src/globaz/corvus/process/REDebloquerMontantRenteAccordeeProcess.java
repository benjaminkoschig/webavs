package globaz.corvus.process;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.helpers.process.REDebloquerMontantRAHandler;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;

public class REDebloquerMontantRenteAccordeeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String emailObject = "";
    private String idDomaine = "";
    private String idRenteAccordee = "";
    private String idSection = "";
    private String idTiersAdrPmt = "";
    private String montantADebloque = "";
    private String refPaiement = "";

    /**
     * Constructor for REImprimerDecisionProcess.
     */
    public REDebloquerMontantRenteAccordeeProcess() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
    }

    /**
     * Constructor for REImprimerDecisionProcess.
     * 
     * @param parent
     */
    public REDebloquerMontantRenteAccordeeProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for REImprimerDecisionProcess.
     * 
     * @param session
     */
    public REDebloquerMontantRenteAccordeeProcess(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BTransaction transaction = getTransaction();
        BSession session = getSession();
        boolean succes = false;

        try {

            doValidation();

            // bz-5328
            // Si l'on valide le déblocage du montant avec un montant à débloquer à '0'
            // (indépendemment d'une section à débloquer ou non), ne rien faire en compta et
            // simplement remettre le flag isRABloquée à '0'.

            if (JadeStringUtil.isBlankOrZero(getMontantADebloque())) {
                REPrestationsAccordees pa = new REPrestationsAccordees();
                pa.setSession(session);
                pa.setIdPrestationAccordee(getIdRenteAccordee());
                pa.retrieve(transaction);

                pa.setIsPrestationBloquee(Boolean.FALSE);
                pa.update(transaction);
            } else {
                REDebloquerMontantRAHandler handler = new REDebloquerMontantRAHandler(this, getSession(), transaction);
                handler.setIdDomaine(getIdDomaine());
                handler.setIdRenteAccordee(getIdRenteAccordee());
                handler.setIdSection(getIdSection());
                handler.setIdTiersAdrPmt(getIdTiersAdrPmt());
                handler.setMontantADebloque(getMontantADebloque());
                handler.setRefPaiement(getRefPaiement());
                FWMemoryLog logCompta = handler.doTraitement(this, true);
                handler.comptabiliser();

                if (logCompta.hasErrors()) {
                    throw new Exception(session.getLabel("ERREUR_GENERATION_ECRITURES_COMPTABLES"));
                }
            }
            succes = true;

        } catch (Exception e) {
            transaction.addErrors(e.toString());
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
            succes = false;
            return false;
        } finally {

            if (succes) {
                emailObject = session.getLabel("EMAIL_OBJECT_DEBLOQUER_MNT_RA_SUCCESS");
            } else {
                emailObject = session.getLabel("EMAIL_OBJECT_DEBLOQUER_MNT_RA_ERREUR");
            }
        }

        return true;
    }

    private void doValidation() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }
            setControleTransaction(getTransaction() == null);
        }
    }

    @Override
    public String getEMailObject() {
        return emailObject;
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * Récupère l'id de la rente accordée
     * 
     * @return
     */
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    public String getMontantADebloque() {
        return montantADebloque;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    /**
     * @return
     */
    public BSpy getSpy() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setEMailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    /**
     * Modification de l'id rente accordée
     * 
     * @param newIdRenteAccordee
     */
    public void setIdRenteAccordee(String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiersAdrPmt(String idTiersAdrPmt) {
        this.idTiersAdrPmt = idTiersAdrPmt;
    }

    public void setMontantADebloque(String montantADebloque) {
        this.montantADebloque = montantADebloque;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

}
