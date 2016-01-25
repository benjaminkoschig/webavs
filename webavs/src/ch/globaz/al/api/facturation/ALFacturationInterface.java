package ch.globaz.al.api.facturation;

import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BTransaction;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.sql.SQLException;
import java.util.Collection;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Interface permettant l'utilisation des services AF de compensation sur facture depuis le module de facturation de Web@AVS
 * 
 * @author jts
 */
public interface ALFacturationInterface {
    /**
     * Retourne le n� du processus li� au passage de facturation
     * 
     * @param idPassage
     *            le passage de facturation
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return le num�ro de processus li�
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getNumProcessusAFLie(String idPassage, BTransaction transaction) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne la p�riode � traiter
     * 
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return P�riode � traiter (format MM.AAAA)
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @deprecated n'est plus utilis�, effectuer un test factu sans avant de d�finitivement virer
     */
    @Deprecated
    public String getPeriode(BTransaction transaction) throws JadeApplicationException;

    /**
     * G�n�re les protocoles d�finitif de la compensation
     * 
     * @param idPassage
     *            Num�ro de passage de la compensation
     * @param dateFacturation
     *            Date de la facturation
     * @param periode
     *            P�riode trait�e (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param logger
     *            Logger contenant d'�ventuelles erreur survenue pendant la compensation
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return Conteneur devant �tre envoy� au serveur d'impression
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentDataContainer getProtocoles(String idPassage, String dateFacturation, String periode,
            String typeCoti, String email, ProtocoleLogger logger, BTransaction transaction)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re les r�caps � compenser (�tat TR) pour la p�riode et le type de cotisation pass�s en param�tre
     * 
     * @param idProcessus
     *            le processus li� aux r�caps � s�lectionner
     * @param periodeA
     *            P�riode pour laquelle r�cup�rer les r�cap (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws BJadeMultipleJdbcConnectionInSameThreadException
     *             Exception lev�e si plusieurs connexion on �t� ouverte
     * @throws SQLException
     *             Exception lev�e en cas d'erreur SQL
     */
    public Collection<CompensationBusinessModel> getRecaps(String idProcessus, String periodeA, String typeCoti,
            BTransaction transaction) throws JadeApplicationException,
            BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadePersistenceException;

    /**
     * Passe l'�tat des r�caps et prestations ayant le num�ro de passage <code>idPassage</code>en TR
     * 
     * @param idPassage
     *            Num�ro de passage de la compensation
     * @param transaction
     *            Transaction provenant du module de facturation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws BJadeMultipleJdbcConnectionInSameThreadException
     *             Exception lev�e si plusieurs connexion on �t� ouverte
     * @throws SQLException
     *             Exception lev�e en cas d'erreur SQL
     */
    public void rollbackRecaps(String idPassage, BTransaction transaction)
            throws BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadeApplicationException,
            JadePersistenceException;

    /**
     * Mets � jour l'�tat, l'id de passage et la date de versement des prestations ainsi que l'�tat de la r�cap pour
     * l'id de r�cap pass� en param�tre
     * 
     * @param idRecap
     *            Num�ro de r�cap AF
     * @param idPassage
     *            Num�ro de passage de la compensation
     * @param date
     *            Date de versement
     * @param transaction
     *            Transaction provenant du module de facturation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws BJadeMultipleJdbcConnectionInSameThreadException
     *             Exception lev�e si plusieurs connexion on �t� ouverte
     * @throws SQLException
     *             Exception lev�e en cas d'erreur SQL
     */
    public void updateRecap(String idRecap, String idPassage, String date, BTransaction transaction)
            throws BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadeApplicationException,
            JadePersistenceException;
}
