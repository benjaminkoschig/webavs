/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.db.listes;

import globaz.draco.application.DSApplication;
import globaz.draco.process.DSProcessListeDSImportee;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Permet de controler les valeurs entrées par l'utilisateur
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DSListeDSImporteeViewBean extends DSProcessListeDSImportee implements BIPersistentObject,
        FWViewBeanInterface {

    private static final long serialVersionUID = 8577684908222175134L;
    private String id = null;

    public DSListeDSImporteeViewBean() throws java.lang.Exception {
        super(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
    }

    public void _init() {
    }

    @Override
    protected void _validate() throws Exception {
        if (BSessionUtil.compareDateFirstGreater(getSession(), getPeriodeReferenceDebut(), getPeriodeReferenceFin())) {
            this._addError(getSession().getLabel("PERIODE_ERRONEE"));
        }
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("MSG_EMAILADDRESS_VIDE"));
        }
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void update() throws Exception {
    }

}
