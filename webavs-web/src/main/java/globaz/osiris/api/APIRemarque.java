/*
 * Créé le Aug 17, 2005
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

import globaz.globall.api.BIEntity;

/**
 * @author dda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface APIRemarque extends BIEntity {

    public String getIdRemarque();

    public String getTexte();

    public void setIdRemarque(String newIdRemarque);

    public void setTexte(String newTexte);

}
