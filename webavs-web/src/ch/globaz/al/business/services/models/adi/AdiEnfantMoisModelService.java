package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisSearchModel;

/**
 * interface de d�claration des services de gestion de persistance des donn�es de ADI - Prestations par enfant / mois
 * 
 * @author PTA
 */
public interface AdiEnfantMoisModelService extends JadeApplicationService {

    /**
     * m�thode de cr�ation ADI - Prestations par enfant / mois
     * 
     * @param adiEnfantMois
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel create(AdiEnfantMoisModel adiEnfantMois) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * m�thode de suppression ADI - Prestations par enfant / mois
     * 
     * @param adiEnfantMois
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel delete(AdiEnfantMoisModel adiEnfantMois) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * m�thode de lecture ADI - Prestations par enfant / mois
     * 
     * @param idAdiEnfantMoisModel
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel read(String idAdiEnfantMoisModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�thode de recherche sur pour AdiEnfantMoisModel
     * 
     * @param adiEnfantMoisSearch
     *            selon mod�le de recherche AdiEnfantSearchModel
     * @return le mo�dle charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisSearchModel search(AdiEnfantMoisSearchModel adiEnfantMoisSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * m�thode de mise � jour ADI - Prestations par enfant / mois
     * 
     * @param adiEnfantMois
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisModel update(AdiEnfantMoisModel adiEnfantMois) throws JadeApplicationException,
            JadePersistenceException;
}
