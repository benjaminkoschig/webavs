package ch.globaz.al.business.services.parameters;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de gestion de param�tres AF.
 * 
 * Service permettant de r�cup�rer des param�tres propres aux allocations familiales
 * 
 * @author jts
 * 
 */
public interface ParametersServices extends JadeApplicationService {
    /**
     * R�cup�re le nom de la caisse (param�tre NOMCAISSE) pour la date courante
     * 
     * @return Le nom de la caisse
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNomCaisse() throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re le nom de la caisse (param�tre NOMCAISSE) pour la date pass�e en param�tre
     * 
     * @param date
     *            Date pour laquelle r�cup�rer le param�tre
     * 
     * @return Le nom de la caisse
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNomCaisse(String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Cette m�thode retourne true pour les caisses cantonales o� il faut tester si l'allocataire PSA touche des PC.
     * 
     * @return true s'il faut tester les PC avant de cr�er un droit pour un PSA
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public Boolean isCheckPCFamille() throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re le param�tre indiquant si le supp. fnb est activ� (param�tre FNB_IS_ENABLED) pour la date pass�e en
     * param�tre
     * 
     * @param date
     *            Date pour laquelle r�cup�rer le param�tre
     * 
     * @return true / false
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isFnbEnabled(String date) throws JadeApplicationException, JadePersistenceException;
}
