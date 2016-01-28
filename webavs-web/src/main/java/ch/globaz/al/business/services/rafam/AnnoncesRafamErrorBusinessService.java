package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.List;
import ch.ech.xmlns.ech_0104._3.Error;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;

/**
 * Service permettant la gestion des erreurs RAFam
 * 
 * @author jts
 * 
 */
public interface AnnoncesRafamErrorBusinessService extends JadeApplicationService {

    /**
     * Ajoute les erreurs contenues dans le paramètre <code>errors</code> à l'annonce <code>annonce</code>.
     * 
     * Si la liste contient une seule erreur, elle est ajouté dans le champ <code>codeErreur</code> de l'annonce.
     * 
     * En cas d'erreurs multiples, elles sont ajoutées dans le champ <code>internalErrorMessage</code>. La dernière
     * erreur de la liste est aussi enregistrée dans le champ <code>codeErreur</code>.
     * 
     * @param errors
     *            Liste d'erreurs provenant d'un message de la centrale
     * @param annonce
     *            annonce en persistance
     * @return l'annonce mise à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void addErrors(List<Error> errors, AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Récupère les erreurs d'une annonce
     * 
     * @param idAnnonce
     *            annonce pour laquelle récupérer les erreurs
     * @return Résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AnnonceRafamErrorComplexSearchModel getErrorsForAnnonce(String idAnnonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne la liste des codes d'erreurs d'un message de la centrale
     * 
     * @param errors
     *            Liste d'erreurs provenant d'un message de la centrale
     * @return Liste des erreurs
     */
    public ArrayList<String> getErrorsFromList(List<Error> errors);

    /**
     * Vérifie si l'annonce <code>idAnnonce</code> a des erreurs de type <code>code</code>. Si <code>code</code> est
     * <code>null</code>, tous les types d'erreurs sont recherchés
     * 
     * @param code
     *            code de l'annonce (211, 212, ...)
     * @param idAnnonce
     *            id de l'annonce à contrôler
     * @return <code>true</code> si l'annonce a au moins une erreur du code indiqué, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean hasError(String idAnnonce, String code) throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si l'annonce <code>idAnnonce</code> a des erreurs d'un type différent de <code>code</code>.
     * 
     * 
     * @param idAnnonce
     *            id de l'annonce à contrôler
     * @param code
     *            code de l'erreur (211, 212, ...)
     * 
     * @return <code>true</code> si l'annonce a au moins une erreur différente du code indiqué, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean hasErrorOfOtherType(String idAnnonce, String code) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Valide les annonces en erreur de moindre importance.
     * 
     * 
     * @param nbDays
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void validateInsignificance(int nbDays) throws JadeApplicationException, JadePersistenceException;
}
