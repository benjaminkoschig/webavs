package ch.globaz.al.business.services.affiliation;

import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service métier lié aux affiliation
 *
 * @author jts
 *
 */
public interface AffiliationBusinessService extends JadeApplicationService {
    /**
     * Récupère les informations de l'assurance à laquelle est liée le dossier à la date donnée en paramètre. Si la date
     * est vide, la date du jour est utilisée
     *
     * @param dossierModel
     *            Dossier pour lequel récupérer les informations de l'affilié
     * @param date
     *            Permet de récupérer les données active à cette dates
     * @return Une instance de <code>AssuranceInfo</code> contenant les données de l'affilié
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     *
     * @see ch.globaz.naos.business.data.AssuranceInfo
     */
    public AssuranceInfo getAssuranceInfo(DossierModel dossierModel, String date)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Récupère les informations de l'assurance à laquelle est liée le dossier à la date donnée en paramètre. Si la date
     * est vide, la date du jour est utilisée
     *
     * @param numAffilie
     *            le numéro d'affilié
     * @param activite
     *            l'activité de l'allocataire (CS)
     * @param date
     *            la date à laquelle on veut les informations
     * @return Une instance de <code>AssuranceInfo</code> contenant les données de l'affilié
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     *
     */
    public AssuranceInfo getAssuranceInfo(String numAffilie, String activite, String date)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * indique si les affiliés requiert un lien sur les agences communales sur les documents
     *
     * @return true ou false
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     *
     */
    public boolean requireDocumentLienAgenceCommunale() throws JadePersistenceException, JadeApplicationException;

    public AffiliationSimpleModel getAffiliation(String numAffilie)
            throws JadePersistenceException, JadeApplicationException;
}
