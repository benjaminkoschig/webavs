package ch.globaz.al.business.services.models.periodeAF;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFSearchModel;

/**
 * Service de gestion de persistance des donn�es du mod�le <code>PeriodeAFModel</code>
 * 
 * @author gmo
 * 
 * @see ch.globaz.al.business.models.periodeAF.PeriodeAFModel
 */
public interface PeriodeAFModelService extends JadeApplicationService {

    /**
     * 
     * @param periodeAFModel
     *            Mod�le de la p�riode AF
     * @return le mod�le cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel create(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'une p�riode selon son id pass� en param�tre
     * 
     * @param idPeriodeAF
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel read(String idPeriodeAF) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les p�riodes correspondant aux crit�res d�finis dans le mod�le de recherche
     * 
     * @param periodeSearchModel
     *            Le mod�le de recherche
     * @return Le mod�le de recherche contenant les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFSearchModel search(PeriodeAFSearchModel periodeSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise � jour du mod�le
     * 
     * @param periodeAFModel
     *            Mod�le de la p�riode AF
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PeriodeAFModel upate(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException;
}
