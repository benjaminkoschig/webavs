/*
 * Créé le 10 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

import java.io.Serializable;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
     * Mise a jour de l'id affilié de l'intervenant
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
