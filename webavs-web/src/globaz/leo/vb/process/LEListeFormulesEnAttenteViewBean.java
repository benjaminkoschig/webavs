/*
 * Créé le 2 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.vb.process;

import globaz.globall.db.BIPersistentObject;
import globaz.leo.util.LEViewBeanSupport;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEListeFormulesEnAttenteViewBean extends LEViewBeanSupport implements BIPersistentObject {
    String forCsFormule1 = new String();
    String forCsFormule2 = new String();
    private Boolean isFormatExcel = new Boolean(false);
    private Boolean isFormatIText = new Boolean(false);
    String order1 = new String();
    String order2 = new String();

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return
     */
    public String getForCsFormule1() {
        return forCsFormule1;
    }

    /**
     * @return
     */
    public String getForCsFormule2() {
        return forCsFormule2;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
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
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @param string
     */
    public void setForCsFormule1(String string) {
        forCsFormule1 = string;
    }

    /**
     * @param string
     */
    public void setForCsFormule2(String string) {
        forCsFormule2 = string;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

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
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
