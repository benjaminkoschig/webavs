/*
 * Créé le 2 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process;

import globaz.framework.process.FWProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.leo.listes.LEListeEnvoi;
import globaz.leo.listes.excel.LEListeFormuleAttenteExcel;
import java.util.List;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEGenererListeFormulesEnAttente extends FWProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categorie;
    private List csFormule;
    private String dateReference;
    private Boolean isFormatExcel = new Boolean(false);
    private Boolean isFormatIText = new Boolean(false);
    private int nbreMaxPageParDoc = 500;
    private String order1;
    private String order2;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // setSendCompletionMail(false);
            // ArrayList order = new ArrayList();
            String categorie = new String();
            if ((getCategorie() != null) && (!JAUtil.isStringEmpty(getCategorie()))) {
                categorie = getCategorie();
            } else {
                // dans le cas où la catégorie n'est pas setter, laisser la
                // possibilité de toutes les catégories
                categorie = "";
            }

            if (getIsFormatExcel().booleanValue()) {
                LEEtapesSuivantesListViewBean manager = new LEEtapesSuivantesListViewBean();
                if (!JadeStringUtil.isEmpty(getOrder1())) {
                    manager.setOrderBy1(getOrder1());
                }
                if (!JadeStringUtil.isEmpty(getOrder2())) {
                    manager.setOrderBy2(getOrder2());
                }
                if (!JadeStringUtil.isEmpty(getDateReference())) {
                    manager.setDatePriseEnCompte(getDateReference());
                }
                if (getCsFormule() != null) {
                    manager.setForCsFormule(getCsFormule());
                }
                if (!JadeStringUtil.isEmpty(getCategorie())) {
                    manager.setForCategories(getCategorie());
                }

                JadePublishDocumentInfo theDocInfoForExcelDocument = createDocumentInfo();
                theDocInfoForExcelDocument.setDocumentTypeNumber("0234GEN");

                LEListeFormuleAttenteExcel excelDoc = new LEListeFormuleAttenteExcel(getSession(), this);
                excelDoc.populateSheetListe(manager, getTransaction());
                this.registerAttachedDocument(theDocInfoForExcelDocument, excelDoc.getOutputFile());
            }
            if (getIsFormatIText().booleanValue()) {
                LEListeEnvoi liste = new LEListeEnvoi(getSession());
                liste.setSession(getSession());
                liste.setParent(this);
                if ((getOrder1() != null) && (!JAUtil.isStringEmpty(getOrder1()))) {
                    liste.setOrderBy1(getOrder1());
                }
                if ((getOrder2() != null) && (!JAUtil.isStringEmpty(getOrder2()))) {
                    liste.setOrderBy2(getOrder2());
                }
                liste.setEMailAddress(getEMailAddress());
                liste.setUntilDate(getDateReference());
                // liste.setOrderByProvenance(order);
                liste.setCategorie(categorie);
                liste.setCsFormule(getCsFormule());
                liste.executeProcess();
            }
            return true;

        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * @return
     */
    public String getCategorie() {
        return categorie;
    };

    /**
     * @return
     */
    public List getCsFormule() {
        return csFormule;
    }

    /**
     * @return
     */
    public String getDateReference() {
        return dateReference;
    }

    @Override
    protected String getEMailObject() {
        return "Résultat du processus liste des formules en attente";
    }

    public Boolean getIsFormatExcel() {
        return isFormatExcel;
    }

    public Boolean getIsFormatIText() {
        return isFormatIText;
    }

    /**
     * @return
     */
    public String getOrder1() {
        return order1;
    }

    /**
     * @return
     */
    public String getOrder2() {
        return order2;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param string
     */
    public void setCategorie(String string) {
        categorie = string;
    }

    /**
     * @param list
     */
    public void setCsFormule(List list) {
        csFormule = list;
    }

    /**
     * @param string
     */
    public void setDateReference(String string) {
        dateReference = string;
    }

    public void setIsFormatExcel(Boolean isFormatExcel) {
        this.isFormatExcel = isFormatExcel;
    }

    public void setIsFormatIText(Boolean isFormatIText) {
        this.isFormatIText = isFormatIText;
    }

    /**
     * @param string
     */
    public void setOrder1(String string) {
        order1 = string;
    }

    /**
     * @param string
     */
    public void setOrder2(String string) {
        order2 = string;
    }

    @Override
    protected void _validate() throws Exception {

        // Date obligatoire
        if (JadeStringUtil.isBlank(getDateReference())) {
            _addError(getTransaction(), getSession().getLabel("OBLIGATOIRE_DATE"));
        }

        // Catégorie obligatoire
        if (JadeStringUtil.isBlank(getCategorie())) {
            _addError(getTransaction(), getSession().getLabel("OBLIGATOIRE_CATEGORIE"));
        }

        // Formule obligatoire
        if (getCsFormule().size() == 0 || JadeStringUtil.isBlank("" + getCsFormule().get(0))) {
            _addError(getTransaction(), getSession().getLabel("OBLIGATOIRE_FORMULE"));
        }

    }

}
