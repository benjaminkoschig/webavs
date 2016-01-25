/*
 * Créé le 18 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * Interface d'accès aux séquences du contentieux Aquila
 * </p>
 * 
 * @author vre
 */
public interface ICOSequence {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * si utilisé pour la méthode {@link #load(Map) load(Map)}, retourne toutes les séquences.
     */
    Map CRITERES_TOUTES_LES_SEQUENCES = new HashMap();
    String CS_FAMILLE_SEQUENCES = "COSEQP";
    String CS_SEQUENCE_ARD = "5100002";
    String CS_SEQUENCE_AVS = "5100001";

    String CS_SEQUENCE_PP = "5100003";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne le code système de la séquence.
     * 
     * @return un code système de la famille COSEQP
     */
    String getCsSequence();

    /**
     * retourne l'identifiant de la séquence.
     * 
     * @return un identifiant.
     */
    String getIdSequence();

    /**
     * charge la liste des séquences qui correspondent aux critères transmis.
     * 
     * @param criteres
     *            une map non nulle de critères (utiliser les membres constants définis dans cette interface)
     * @return une liste non nulle, peut-être vide d'instances de ICOSequence.
     * @throws Exception
     *             si le chargement échoue
     */
    Collection /* ICOSequence */load(Map criteres) throws Exception;
}
