package ch.globaz.al.business.services.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashSet;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextGlobal;

/**
 * Service de g�n�ration de prestations
 * 
 * @author jts
 * 
 */
public interface GenerationService extends JadeApplicationService {

    /**
     * G�n�re les prestations selon les donn�es contenues dans le contexte pass� en param�tre
     * 
     * @param context
     *            Contexte global contenant le contexte d'affili� et la liste des dossiers � traiter
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void generationAffilie(ContextGlobal context) throws JadeApplicationException;

    /**
     * G�n�re les prestations des dossiers li�s � l'affili� <code>numAffilie</code> pour une p�riode d'un mois
     * 
     * @param numAffilie
     *            Num�ro de l'affili� pour lequel les prestations doivent �tre g�n�r�e
     * @param periode
     *            P�riode (mois) � g�n�rer sous la forme MM.AAAA
     * @param numGeneration
     *            Num�ro de g�n�ration. G�n�ralement l'id du traitement ayant ex�cut� la g�n�ration
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la g�n�ration
     * @return logger contenant les messages d'erreur
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProtocoleLogger generationAffilie(String numAffilie, String periode, String numGeneration,
            ProtocoleLogger logger) throws JadeApplicationException, JadePersistenceException;

    /**
     * G�n�re les prestations pour la totalit� des dossiers (type cot. pers.) pour une p�riode d'un mois.
     * 
     * @param periode
     *            P�riode (mois) � g�n�rer sous la forme MM.AAAA
     * @param type
     *            Type de cotisation/g�n�ration
     * @param numGeneration
     *            Num�ro de g�n�ration. G�n�ralement l'id du traitement ayant ex�cut� la g�n�ration
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la g�n�ration
     * @return logger contenant les messages d'erreur
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProtocoleLogger generationGlobale(String periode, String type, String numGeneration, ProtocoleLogger logger)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * R�cup�re la liste des p�riodicit� a prendre en compte lors d'une g�n�ration globale. Les p�riodicit�s mensuelles
     * sont trait�es tous les mois. Les p�riodicit�s trimestrielles sont trait�s les mois 3, 6, 9 et 12. Les
     * p�riodicit�s annuelles sont trait�es le mois 12
     * 
     * @param periode
     *            p�riode � g�n�rer
     * @return liste des codes syst�me correspondant aux p�riodicit�s � traiter
     * 
     * @throws JadeApplicationException
     *             Exception lev�e si la p�riode indiqu�e n'est pas valide
     */
    public HashSet<String> getPeriodicites(String periode) throws JadeApplicationException;

    /**
     * Initialise le mod�le de recherche des affili�s � traiter
     * 
     * @param debutPeriode
     *            d�but de la p�riode � g�n�rer (MM.AAAA).
     * @param typeCoti
     *            Type(s) de cotisations � traiter ({@link ch.globaz.al.business.constantes.ALConstPrestations})
     * 
     * @return le mod�le initialis� (non ex�cut�)
     * @throws JadeApplicationException
     *             Exception lev�e si la p�riode pass�e au manager par le constructeur n'est pas valide ou si le type de
     *             cotisation n'est pas valide
     * @throws JadePersistenceException
     */
    public AffilieListComplexSearchModel initSearchAffilies(String periode, String typeCot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise le mod�le de recherche des dossiers � trait�
     * 
     * @param debutPeriode
     *            d�but de la p�riode � g�n�rer (MM.AAAA). Elle permet de g�rer les cas de dossier radi�
     * @param typeCoti
     *            Type(s) de cotisations � traiter ({@link ch.globaz.al.business.constantes.ALConstPrestations}). Si le
     *            param�tre est <code>null</code>, tous les types de dossiers seront trait�s
     * 
     * @return le mod�le initialis� (non ex�cut�)
     * @throws JadeApplicationException
     *             Exception lev�e si l'un des param�tres n'est pas valide
     * @throws JadePersistenceException
     */
    public DossierComplexSearchModel initSearchDossiers(String debutPeriode, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param idRecap
     *            identifaint de la r�cap
     * @param periodeValidite
     * @return le montant total des prestation
     * @throws JadeApplicationException
     *             Exception lev�e si la p�riode pass�e au manager par le constructeur n'est pas valide ou si le type de
     *             cotisation n'est pas valide
     * @throws JadePersistenceException
     */
    public String totalMontantPrestaGenereDossierPeriode(String idDossier, String idRecap, String periodeValidite)
            throws JadeApplicationException, JadePersistenceException;
}
