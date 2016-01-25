/*
 * Créé le 10 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Interface que doivent implémenter des classes afin de permettre leur tri en fonction de leur hiérarchie
 * (PRHierarchiqueComparator) et l'itération hiérarchique (PRHierarchiqueIterateur):
 * </p>
 * 
 * @author vre
 */
public interface PRHierarchique {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id majeur
     * 
     * @return l'identifiant (unique) de l'ensemble de données que représente cette instance.
     */
    public String getIdMajeur();

    /**
     * getter pour l'attribut id parent
     * 
     * @return l'identifiant du parent dans la hiérarchie.
     */
    public String getIdParent();
}
