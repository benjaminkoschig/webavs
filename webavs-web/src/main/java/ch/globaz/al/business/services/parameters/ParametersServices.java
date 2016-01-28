package ch.globaz.al.business.services.parameters;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de gestion de paramètres AF.
 * 
 * Service permettant de récupérer des paramètres propres aux allocations familiales
 * 
 * @author jts
 * 
 */
public interface ParametersServices extends JadeApplicationService {
    /**
     * Récupère le nom de la caisse (paramètre NOMCAISSE) pour la date courante
     * 
     * @return Le nom de la caisse
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNomCaisse() throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère le nom de la caisse (paramètre NOMCAISSE) pour la date passée en paramètre
     * 
     * @param date
     *            Date pour laquelle récupérer le paramètre
     * 
     * @return Le nom de la caisse
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNomCaisse(String date) throws JadeApplicationException, JadePersistenceException;

    /**
     * Cette méthode retourne true pour les caisses cantonales où il faut tester si l'allocataire PSA touche des PC.
     * 
     * @return true s'il faut tester les PC avant de créer un droit pour un PSA
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public Boolean isCheckPCFamille() throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère le paramètre indiquant si le supp. fnb est activé (paramètre FNB_IS_ENABLED) pour la date passée en
     * paramètre
     * 
     * @param date
     *            Date pour laquelle récupérer le paramètre
     * 
     * @return true / false
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isFnbEnabled(String date) throws JadeApplicationException, JadePersistenceException;
}
