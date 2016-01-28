/*
 * Cr�� le 18 janv. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * Interface d'acc�s aux s�quences du contentieux Aquila
 * </p>
 * 
 * @author vre
 */
public interface ICOSequence {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * si utilis� pour la m�thode {@link #load(Map) load(Map)}, retourne toutes les s�quences.
     */
    Map CRITERES_TOUTES_LES_SEQUENCES = new HashMap();
    String CS_FAMILLE_SEQUENCES = "COSEQP";
    String CS_SEQUENCE_ARD = "5100002";
    String CS_SEQUENCE_AVS = "5100001";

    String CS_SEQUENCE_PP = "5100003";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne le code syst�me de la s�quence.
     * 
     * @return un code syst�me de la famille COSEQP
     */
    String getCsSequence();

    /**
     * retourne l'identifiant de la s�quence.
     * 
     * @return un identifiant.
     */
    String getIdSequence();

    /**
     * charge la liste des s�quences qui correspondent aux crit�res transmis.
     * 
     * @param criteres
     *            une map non nulle de crit�res (utiliser les membres constants d�finis dans cette interface)
     * @return une liste non nulle, peut-�tre vide d'instances de ICOSequence.
     * @throws Exception
     *             si le chargement �choue
     */
    Collection /* ICOSequence */load(Map criteres) throws Exception;
}
