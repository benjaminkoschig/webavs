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
import globaz.globall.util.JACalendar;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.print.list.CAListComptesAnnexesMotifBlocageContentieux;
import globaz.osiris.print.list.CAListComptesAnnexesMotifBlocageContentieuxAquila;

/**
 * @author jts 23 mars 05 10:31:12
 */
public class CAProcessListComptesAnnexesMotifBlocageContentieux extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean blocageInactif = new Boolean(false);
    private String forIdCategorie = "";
    private String forIdGenreCompte = new String();
    private String forSelectionCompte = new String();
    private String forSelectionRole = new String();
    private String forSelectionTri = new String();
    private String idContMotifBloque = new String();

    public CAProcessListComptesAnnexesMotifBlocageContentieux() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                manager.setForSelectionTri(getForSelectionTri());
                manager.setForSelectionRole(getForSelectionRole());
                manager.setForIdGenreCompte(getForIdGenreCompte());
                manager.setMotifBloqueDefini(true);
                manager.setForSelectionCompte(getForSelectionCompte());
                manager.setForIdCategorie(getForIdCategorie());

                if (!getBlocageInactif().booleanValue()) {
                    manager.setForIdContMotifBloque(getIdContMotifBloque());
                    manager.setBloque(true);
                    manager.setForDateReferenceBlocage(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD));
                }

                manager.find();

                // Création du document
                CAListComptesAnnexesMotifBlocageContentieux excelDoc = new CAListComptesAnnexesMotifBlocageContentieux(
                        getSession());
                excelDoc.setForSelectionTri(getForSelectionTri());
                excelDoc.setForSelectionRole(getForSelectionRole());
                excelDoc.setForSelectionCompte(getForSelectionCompte());
                excelDoc.setForIdGenreCompte(getForIdGenreCompte());
                excelDoc.setIdContMotifBloque(getIdContMotifBloque());
                excelDoc.setBlocageInactif(getBlocageInactif());
                excelDoc.setDocumentInfo(createDocumentInfo());
                excelDoc.setProcessAppelant(this);
                excelDoc.populateSheetListe(manager, getTransaction());

                this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
                return true;
                // Contentieux Aquila
            } else {

                manager.setForSelectionTri(getForSelectionTri());
                manager.setForSelectionRole(getForSelectionRole());
                manager.setForIdGenreCompte(getForIdGenreCompte());
                manager.setForSelectionCompte(getForSelectionCompte());
                manager.setForIdCategorie(getForIdCategorie());

                if (!getBlocageInactif().booleanValue()) {
                    manager.setBloque(true);
                }

                manager.find();
                // Création du document
                CAListComptesAnnexesMotifBlocageContentieuxAquila excelDoc = new CAListComptesAnnexesMotifBlocageContentieuxAquila(
                        getSession());
                excelDoc.setForSelectionTri(getForSelectionTri());
                excelDoc.setForSelectionRole(getForSelectionRole());
                excelDoc.setForSelectionCompte(getForSelectionCompte());
                excelDoc.setForIdGenreCompte(getForIdGenreCompte());
                excelDoc.setIdContMotifBloque(getIdContMotifBloque());
                excelDoc.setBlocageInactif(getBlocageInactif());
                excelDoc.setDocumentInfo(createDocumentInfo());
                excelDoc.setProcessAppelant(this);
                excelDoc.populateSheetListe(manager, getTransaction());

                this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
                return true;
            }
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
    public Boolean getBlocageInactif() {
        return blocageInactif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("CAMBC_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("CAMBC_SUJETMAIL_OK");
        }
    }

    /**
     * @return the forIdCategorie
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return
     */
    public String getForSelectionCompte() {
        return forSelectionCompte;
    }

    /**
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
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
    public String getIdContMotifBloque() {
        return idContMotifBloque;
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
    public void setBlocageInactif(Boolean b) {
        blocageInactif = b;
    }

    /**
     * @param forIdCategorie
     *            the forIdCategorie to set
     */
    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionCompte(String string) {
        forSelectionCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
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
    public void setIdContMotifBloque(String string) {
        idContMotifBloque = string;
    }

}
