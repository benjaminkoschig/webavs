package ch.globaz.al.business.services.models.attribut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;

/**
 * Les services m�tier des attribut entit�
 * 
 * @author GMO
 * 
 */
public interface AttributEntiteBusinessService extends JadeApplicationService {

    /**
     * D�finit un attribut pour une entit�, cr�� ou m�j selon l'attribut et sa valeur. Si la valeur correspond � celle
     * par d�faut, aucun changement en base pour les attributs sp�cifiques � l'entit�. C'est le param�tre par d�faut qui
     * implicement li� � l'entit�
     * 
     * @param idEntite
     *            id de l'entit� dont on veut modifier l'attribut
     * @param typeEntite
     *            type de l'entit� (nom complet de la classe du mod�le simple)
     * @param updatedAttribut
     *            nouvelle valeur de l'attribut
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void changeAttributValueForEntite(String idEntite, String typeEntite, AttributEntiteModel updatedAttribut)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne les listes des attributs li�s � l'entit�, qu'ils soient sp�cifiques � l'entit� ou ceux par d�faut
     * 
     * @param idEntite
     *            id de l'entit� dont on veut les attributs
     * @param typeEntite
     *            type de l'entit� (nom complet de la classe du mod�le simple)
     * @return liste de AttributEntiteModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList getAttributListForEntite(String idEntite, String typeEntite) throws JadeApplicationException,
            JadePersistenceException;

}
