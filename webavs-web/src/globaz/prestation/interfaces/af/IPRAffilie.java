/*
 * Cr�� le 26 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.interfaces.af;

import globaz.prestation.interfaces.tiers.IPRTiers;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public interface IPRAffilie extends IPRTiers {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut branche economique
     * 
     * @return la valeur courante de l'attribut branche economique
     */
    public String getBrancheEconomique();

    public String getDateDebut();

    public String getDateFin();

    /**
     * getter pour l'attribut id affilie
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie();

    /**
     * getter pour l'attribut num affilie
     * 
     * @return la valeur courante de l'attribut num affilie
     */
    public String getNumAffilie();

    public String getTypeAffiliation();

    public String getNumeroIDE();

    public String getIdeStatut();
}
