/*
 * Cr�� le 6 f�vr. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.babel.db;

/**
 * @author vre
 */
public interface ICTCompteurBorne {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut borne inferieure
     * 
     * @return la valeur courante de l'attribut borne inferieure
     */
    public String getBorneInferieure();

    /**
     * setter pour l'attribut borne inferieure
     * 
     * @param borneInferieure
     *            une nouvelle valeur pour cet attribut
     */
    public void setBorneInferieure(String borneInferieure);
}
