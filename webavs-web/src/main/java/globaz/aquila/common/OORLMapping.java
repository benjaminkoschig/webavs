package globaz.aquila.common;

import java.util.Map;

/**
 * @author Marco Lessard, 17-nov-2004
 */
public interface OORLMapping {

    /**
     * Permet d'ajouter un mapping BD -> OO entre les champs clé : String, nom du champ BD, valeur : String, nom de la
     * méthode de classe SANS get ou set
     * 
     * @param flds
     *            Map
     */
    void addRLOOMapping(Map flds);

    /**
     * Permet d'ajouter un mapping BD -> OO entre les champs
     * 
     * @param dbfld
     *            String Nom du champ dans la BD
     * @param oofld
     *            String Nom du champ dans la classe
     */
    void addRLOOMapping(String dbfld, String oofld);

    /**
     * Retourne un Map de correspondances BD -> OO. Il faut encore ajouter un get ou set pour appeler la méthode
     * correspondante.
     * 
     * @return Map
     */
    Map getOOfromRL();

    /**
     * Retourne le nom de champ Objet correspondant au champ de la BD. Il faut encore ajouter un get ou set pour appeler
     * la méthode correspondante.
     * 
     * @param fld
     *            Nom du champ dans la classe
     * @return String
     */
    String getOOfromRL(String fld);

    /**
     * Retourne le nom de la table BD correspondant à l'objet
     * 
     * @return String nom de la table dans la BD
     */
    String getRLName();

    /**
     * Assigne le nom de la table BD correspondant à l'objet
     * 
     * @param name
     *            String nom de la table dans la BD
     */
    void setRLName(String name);

    /**
     * Permet de définir un mapping BD -> OO entre les champs clé : String, nom du champ BD, valeur : String, nom de la
     * méthode de classe SANS get ou set
     * 
     * @param flds
     *            Map
     */
    void setRLOOMapping(Map flds);

}
