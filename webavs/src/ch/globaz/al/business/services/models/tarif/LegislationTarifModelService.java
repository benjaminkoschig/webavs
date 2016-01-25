package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.LegislationTarifModel;
import ch.globaz.al.business.models.tarif.LegislationTarifSearchModel;

/**
 * Service de gestion de persistance des donn�es de LegislationTarif
 * 
 * @author PTA
 */
public interface LegislationTarifModelService extends JadeApplicationService {

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
    public int count(LegislationTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Cr�ation d'une l�gislation de tarif selon le mod�le pass� en param�tre
     * 
     * @param legislationTarifModel
     *            mod�le � enregistrer
     * @return mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifModel create(LegislationTarifModel legislationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * Lecture d'une l�gislation de tarif selon l'id pass� en param�tre
     * 
     * @param idLegislationtarifModel
     *            Id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifModel read(String idLegislationtarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'une l�gislation de tarif selon le mod�le pass� en param�tre
     * 
     * @param legislationTarifSearch
     *            mod�le contenant les crit�res de recherche
     * @return mod�le contenant le r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifSearchModel search(LegislationTarifSearchModel legislationTarifSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise � jour d'une l�gislation de tarif selon le mod�le pass� en param�tre
     * 
     * @param legislationTarifModel
     *            contenant les donn�es mise � jour
     * @return le mod�le apr�s mise � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifModel update(LegislationTarifModel legislationTarifModel) throws JadeApplicationException,
            JadePersistenceException;
}
