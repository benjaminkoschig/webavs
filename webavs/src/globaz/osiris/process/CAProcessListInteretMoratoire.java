/*
 * Créé le 23 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.interets.CAApercuInteretMoratoireManager;
import globaz.osiris.print.list.CAListInteretMoratoire;

/**
 * @author jts 23 mars 05 10:31:12
 */
public class CAProcessListInteretMoratoire extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateCalculDebut = new String();
    private String dateCalculFin = new String();
    private String forSelectionTri = new String();
    private String idGenreInteret = new String();
    private String idJournalCalcul = new String();
    private String idJournalFacturation = new String();

    private String idMotifCalcul = new String();

    public CAProcessListInteretMoratoire() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CAApercuInteretMoratoireManager manager = new CAApercuInteretMoratoireManager();
            manager.setSession(getSession());

            manager.setForIdGenreInteret(getIdGenreInteret());
            manager.setForDateCalculDebut(getDateCalculDebut());
            manager.setForDateCalculFin(getDateCalculFin());
            manager.setForIdJournalCalcul(getIdJournalCalcul());
            manager.setForIdMotifCalcul(getIdMotifCalcul());
            manager.setForIdJournalFacturation(getIdJournalFacturation());
            manager.setForIdRole(CARole.listeIdsRolesPourUtilisateurCourant(getSession()));
            manager.setForSelectionTri(getForSelectionTri());

            manager.find();

            // Création du document
            CAListInteretMoratoire excelDoc = new CAListInteretMoratoire(getSession());

            excelDoc.setIdGenreInteret(getIdGenreInteret());
            excelDoc.setDateCalculDebut(getDateCalculDebut());
            excelDoc.setDateCalculFin(getDateCalculFin());
            excelDoc.setIdJournalCalcul(getIdJournalCalcul());
            excelDoc.setIdMotifCalcul(getIdMotifCalcul());
            excelDoc.setIdJournalFacturation(getIdJournalFacturation());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheetListe(manager, getTransaction());

            manager.setStatsParGenreInteret(true);
            manager.find();

            excelDoc.populateSheetStats(manager, getTransaction());
            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * @return
     */
    public String getDateCalculDebut() {
        return dateCalculDebut;
    }

    /**
     * @return
     */
    public String getDateCalculFin() {
        return dateCalculFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("IM_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("IM_SUJETMAIL_OK");
        }
    }

    /**
     * @return
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * @return
     */
    public String getIdGenreInteret() {
        return idGenreInteret;
    }

    /**
     * @return
     */
    public String getIdJournalCalcul() {
        return idJournalCalcul;
    }

    /**
     * @return
     */
    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    /**
     * @return
     */
    public String getIdMotifCalcul() {
        return idMotifCalcul;
    }

    /**
     * @return En suspens si le journal de facturation = 0
     */
    public String getSuspens() {
        return idJournalFacturation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param string
     */
    public void setDateCalculDebut(String string) {
        dateCalculDebut = string;
    }

    /**
     * @param string
     */
    public void setDateCalculFin(String string) {
        dateCalculFin = string;
    }

    /**
     * @param string
     */
    public void setForSelectionTri(String string) {
        forSelectionTri = string;
    }

    /**
     * @param string
     */
    public void setIdGenreInteret(String string) {
        idGenreInteret = string;
    }

    /**
     * @param string
     */
    public void setIdJournalCalcul(String string) {
        idJournalCalcul = string;
    }

    /**
     * @param string
     */
    public void setIdJournalFacturation(String string) {
        idJournalFacturation = string;
    }

    /**
     * @param string
     */
    public void setIdMotifCalcul(String string) {
        idMotifCalcul = string;
    }

    /**
     * @param string
     *            En suspens si le journal de facturation = 0
     */
    public void setSuspens(String string) {
        idJournalFacturation = string;
    }

}
