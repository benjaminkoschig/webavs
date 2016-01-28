/*
 * Cr�� le 10 nov. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

import java.io.Serializable;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICTScalableDocumentCopie extends Serializable {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Donne le CS de l'intervenant
     */
    public String getCsIntervenant();

    /**
     * Donne l'idAffilie de l'intervenant
     */
    public String getIdAffilie();

    /**
     * Donne l'id tiers de l'intervenant
     */
    public String getIdTiers();

    /**
     * Donne l'identifiant de la copie
     */
    public String getKey();

    /**
     * Donne le PrenomNom du tiers de l'intervenant
     */
    public String getPrenomNomTiers();

    /**
     * Mise a jour du CS de l'intervenant
     * 
     * @param csIntervenant
     *            le nouveau CS de l'intervenant
     */
    public void setCsIntervenant(String csIntervenant);

    /**
     * Mise a jour de l'id affili� de l'intervenant
     * 
     * @param csIntervenant
     *            le nouveau CS de l'intervenant
     */
    public void setIdAffilie(String idAffilie);

    /**
     * Mise a jour de l'id tiers de l'intervenant
     * 
     * @param csIntervenant
     *            le nouveau CS de l'intervenant
     */
    public void setIdTiers(String idTiers);

    /**
     * Mise a jour du PrenomNom de l'intervenant
     * 
     * @param csIntervenant
     *            le nouveau CS de l'intervenant
     */
    public void setPrenomNomTiers(String prenomNomTiers);

}
