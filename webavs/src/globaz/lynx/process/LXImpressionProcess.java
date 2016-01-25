package globaz.lynx.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;

public class LXImpressionProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCategorie;
    private String csMotifBlocage;

    private String dateDebut;
    private String dateFin;
    private Boolean estBloque = new Boolean(false);
    private String etat;
    private String fournisseurBorneInf;
    private String fournisseurBorneSup;
    private String fournisseurIdBorneInf;
    private String fournisseurIdBorneSup;
    private String idSociete;
    private String montantMaxi;
    private String montantMini;
    private String selectedId;
    private String selection;

    private LXSocieteDebitrice societe = null;

    public LXImpressionProcess() {
        super();
    }

    public LXImpressionProcess(BProcess parent) {
        super(parent);
    }

    public LXImpressionProcess(BSession session) {
        super(session);
    }

    /**
     * @see BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        return false;
    }

    public String getCsCategorie() {
        return csCategorie;
    }

    public String getCsMotifBlocage() {
        return csMotifBlocage;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public String getEtat() {
        return etat;
    }

    public String getFournisseurBorneInf() {
        return fournisseurBorneInf;
    }

    public String getFournisseurBorneSup() {
        return fournisseurBorneSup;
    }

    public String getFournisseurIdBorneInf() {
        return fournisseurIdBorneInf;
    }

    public String getFournisseurIdBorneSup() {
        return fournisseurIdBorneSup;
    }

    public String getIdSociete() {
        return idSociete;
    }

    public String getMontantMaxi() {
        return montantMaxi;
    }

    public String getMontantMini() {
        return montantMini;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public String getSelection() {
        return selection;
    }

    /**
     * Retrouve la societe si pas encore chargée.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }

        if (societe == null) {
            societe = new LXSocieteDebitrice();
            societe.setSession(getSession());
            societe.setIdSociete(getIdSociete());
            societe.retrieve(getTransaction());

            if (societe.hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

            if (societe.isNew()) {
                throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            }
        }

        return societe;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setCsCategorie(String csCategorie) {
        this.csCategorie = csCategorie;
    }

    public void setCsMotifBlocage(String csMotifBlocage) {
        this.csMotifBlocage = csMotifBlocage;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setFournisseurBorneInf(String fournisseurBorneInf) {
        this.fournisseurBorneInf = fournisseurBorneInf;
    }

    public void setFournisseurBorneSup(String fournisseurBorneSup) {
        this.fournisseurBorneSup = fournisseurBorneSup;
    }

    public void setFournisseurIdBorneInf(String fournisseurIdBorneInf) {
        this.fournisseurIdBorneInf = fournisseurIdBorneInf;
    }

    public void setFournisseurIdBorneSup(String fournisseurIdBorneSup) {
        this.fournisseurIdBorneSup = fournisseurIdBorneSup;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setMontantMaxi(String montantMaxi) {
        this.montantMaxi = montantMaxi;
    }

    public void setMontantMini(String montantMini) {
        this.montantMini = montantMini;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }
}
