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
     * Retourne le n° du processus lié au passage de facturation
     * 
     * @param idPassage
     *            le passage de facturation
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return le numéro de processus lié
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getNumProcessusAFLie(String idPassage, BTransaction transaction) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne la période à traiter
     * 
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return Période à traiter (format MM.AAAA)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @deprecated n'est plus utilisé, effectuer un test factu sans avant de définitivement virer
     */
    @Deprecated
    public String getPeriode(BTransaction transaction) throws JadeApplicationException;

    /**
     * Génère les protocoles définitif de la compensation
     * 
     * @param idPassage
     *            Numéro de passage de la compensation
     * @param dateFacturation
     *            Date de la facturation
     * @param periode
     *            Période traitée (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param logger
     *            Logger contenant d'éventuelles erreur survenue pendant la compensation
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return Conteneur devant être envoyé au serveur d'impression
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentDataContainer getProtocoles(String idPassage, String dateFacturation, String periode,
            String typeCoti, String email, ProtocoleLogger logger, BTransaction transaction)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère les récaps à compenser (état TR) pour la période et le type de cotisation passés en paramètre
     * 
     * @param idProcessus
     *            le processus lié aux récaps à sélectionner
     * @param periodeA
     *            Période pour laquelle récupérer les récap (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param transaction
     *            Transaction provenant du module de facturation
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws BJadeMultipleJdbcConnectionInSameThreadException
     *             Exception levée si plusieurs connexion on été ouverte
     * @throws SQLException
     *             Exception levée en cas d'erreur SQL
     */
    public Collection<CompensationBusinessModel> getRecaps(String idProcessus, String periodeA, String typeCoti,
            BTransaction transaction) throws JadeApplicationException,
            BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadePersistenceException;

    /**
     * Passe l'état des récaps et prestations ayant le numéro de passage <code>idPassage</code>en TR
     * 
     * @param idPassage
     *            Numéro de passage de la compensation
     * @param transaction
     *            Transaction provenant du module de facturation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws BJadeMultipleJdbcConnectionInSameThreadException
     *             Exception levée si plusieurs connexion on été ouverte
     * @throws SQLException
     *             Exception levée en cas d'erreur SQL
     */
    public void rollbackRecaps(String idPassage, BTransaction transaction)
            throws BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadeApplicationException,
            JadePersistenceException;

    /**
     * Mets à jour l'état, l'id de passage et la date de versement des prestations ainsi que l'état de la récap pour
     * l'id de récap passé en paramètre
     * 
     * @param idRecap
     *            Numéro de récap AF
     * @param idPassage
     *            Numéro de passage de la compensation
     * @param date
     *            Date de versement
     * @param transaction
     *            Transaction provenant du module de facturation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws BJadeMultipleJdbcConnectionInSameThreadException
     *             Exception levée si plusieurs connexion on été ouverte
     * @throws SQLException
     *             Exception levée en cas d'erreur SQL
     */
    public void updateRecap(String idRecap, String idPassage, String date, BTransaction transaction)
            throws BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadeApplicationException,
            JadePersistenceException;
}
