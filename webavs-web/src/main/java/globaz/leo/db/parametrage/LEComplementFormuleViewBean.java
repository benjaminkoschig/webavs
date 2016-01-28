/*
 * Créé le 11 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENComplementFormule;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEComplementFormuleViewBean extends ENComplementFormule implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * definitionDoc - définition du document - code systeme du document (PDEFDO)
     */
    private String definitionDoc = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        definitionDoc = statement.dbReadNumeric("PDEFDO");

    }

    /**
     * @return
     */
    public String getDefinitionDoc() {
        return definitionDoc;
    }

    /**
     * @param string
     */
    public void setDefinitionDoc(String string) {
        definitionDoc = string;
    }

}
