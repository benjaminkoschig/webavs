package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.PrestationTarifModel;
import ch.globaz.al.business.models.tarif.PrestationTarifSearchModel;

/**
 * Service de gestion de persistance des donn�es de PrestationTarif
 * 
 * @author PTA
 */
public interface PrestationTarifModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param search
     *            mod�le contenant les crit�res de recherche
     * @return nombre de lignes retourn� par la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(PrestationTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Cr�ation d'une prestation de tarif selon le mod�le pass� en param�tre
     * 
     * @param prestationTarifModel
     *            mod�le � enregistrer
     * @return mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifModel create(PrestationTarifModel prestationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'une prestation de tarif selon l'identifiant pass� en param�tre
     * 
     * @param idPrestationTarifModel
     *            Id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifModel read(String idPrestationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'une prestation de tarif selon le mod�le pass� en param�tre
     * 
     * @param search
     *            mod�le contenant les crit�res de recherche
     * @return mod�le contenant le r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifSearchModel search(PrestationTarifSearchModel search) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mis � jour d'une prestation de tarif selon le mod�le pass� en param�tre
     * 
     * @param prestationTarifModel
     *            mod�le contenant les donn�es mise � jour
     * @return le mod�le apr�s mise � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifModel update(PrestationTarifModel prestationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

}
