/*
 * Cr�� le 10 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Interface que doivent impl�menter des classes afin de permettre leur tri en fonction de leur hi�rarchie
 * (PRHierarchiqueComparator) et l'it�ration hi�rarchique (PRHierarchiqueIterateur):
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
     * @return l'identifiant (unique) de l'ensemble de donn�es que repr�sente cette instance.
     */
    public String getIdMajeur();

    /**
     * getter pour l'attribut id parent
     * 
     * @return l'identifiant du parent dans la hi�rarchie.
     */
    public String getIdParent();
}
