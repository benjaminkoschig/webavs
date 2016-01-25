/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.vb.cat;

import globaz.babel.db.cat.CTTexte;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTTexte
 */
public class CTTexteViewBean extends CTTexte implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * masque les balises contenues dans le texte de la description en les transformant en commentaires html.
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescriptionEscaped() {
        return JadeStringUtil.change(JadeStringUtil.change(getDescription(), "<", "<!--"), ">", "-->");
    }
}
