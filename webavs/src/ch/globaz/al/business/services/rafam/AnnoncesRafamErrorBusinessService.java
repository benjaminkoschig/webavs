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
     * Ajoute les erreurs contenues dans le param�tre <code>errors</code> � l'annonce <code>annonce</code>.
     * 
     * Si la liste contient une seule erreur, elle est ajout� dans le champ <code>codeErreur</code> de l'annonce.
     * 
     * En cas d'erreurs multiples, elles sont ajout�es dans le champ <code>internalErrorMessage</code>. La derni�re
     * erreur de la liste est aussi enregistr�e dans le champ <code>codeErreur</code>.
     * 
     * @param errors
     *            Liste d'erreurs provenant d'un message de la centrale
     * @param annonce
     *            annonce en persistance
     * @return l'annonce mise � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void addErrors(List<Error> errors, AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * R�cup�re les erreurs d'une annonce
     * 
     * @param idAnnonce
     *            annonce pour laquelle r�cup�rer les erreurs
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * V�rifie si l'annonce <code>idAnnonce</code> a des erreurs de type <code>code</code>. Si <code>code</code> est
     * <code>null</code>, tous les types d'erreurs sont recherch�s
     * 
     * @param code
     *            code de l'annonce (211, 212, ...)
     * @param idAnnonce
     *            id de l'annonce � contr�ler
     * @return <code>true</code> si l'annonce a au moins une erreur du code indiqu�, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean hasError(String idAnnonce, String code) throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si l'annonce <code>idAnnonce</code> a des erreurs d'un type diff�rent de <code>code</code>.
     * 
     * 
     * @param idAnnonce
     *            id de l'annonce � contr�ler
     * @param code
     *            code de l'erreur (211, 212, ...)
     * 
     * @return <code>true</code> si l'annonce a au moins une erreur diff�rente du code indiqu�, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void validateInsignificance(int nbDays) throws JadeApplicationException, JadePersistenceException;
}
