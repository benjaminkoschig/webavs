package ch.globaz.al.business.services.affiliation;

import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service m�tier li� aux affiliation
 *
 * @author jts
 *
 */
public interface AffiliationBusinessService extends JadeApplicationService {
    /**
     * R�cup�re les informations de l'assurance � laquelle est li�e le dossier � la date donn�e en param�tre. Si la date
     * est vide, la date du jour est utilis�e
     *
     * @param dossierModel
     *            Dossier pour lequel r�cup�rer les informations de l'affili�
     * @param date
     *            Permet de r�cup�rer les donn�es active � cette dates
     * @return Une instance de <code>AssuranceInfo</code> contenant les donn�es de l'affili�
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     *
     * @see ch.globaz.naos.business.data.AssuranceInfo
     */
    public AssuranceInfo getAssuranceInfo(DossierModel dossierModel, String date)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * R�cup�re les informations de l'assurance � laquelle est li�e le dossier � la date donn�e en param�tre. Si la date
     * est vide, la date du jour est utilis�e
     *
     * @param numAffilie
     *            le num�ro d'affili�
     * @param activite
     *            l'activit� de l'allocataire (CS)
     * @param date
     *            la date � laquelle on veut les informations
     * @return Une instance de <code>AssuranceInfo</code> contenant les donn�es de l'affili�
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     *
     */
    public AssuranceInfo getAssuranceInfo(String numAffilie, String activite, String date)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * indique si les affili�s requiert un lien sur les agences communales sur les documents
     *
     * @return true ou false
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     *
     */
    public boolean requireDocumentLienAgenceCommunale() throws JadePersistenceException, JadeApplicationException;

    public AffiliationSimpleModel getAffiliation(String numAffilie)
            throws JadePersistenceException, JadeApplicationException;
}
