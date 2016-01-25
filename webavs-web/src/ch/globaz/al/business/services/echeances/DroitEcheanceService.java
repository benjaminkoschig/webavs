package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashSet;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;

/**
 * Service permettant la gestion des listes des échéances liées aux droits
 * 
 * @author PTA
 * 
 */
public interface DroitEcheanceService extends JadeApplicationService {
    /**
     * Méthode qui retourne le libellé à utiliser pour le motif de fin échéance
     * 
     * @param droit
     *            DroitModele
     * @param langue
     *            à utiliser
     * @return String motif de fin d'échéance
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getLibelleMotif(DroitComplexModel droit, String langue) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Méthode qui retourne le libellé à utiliser pour le motif de fin échéance
     * 
     * @param droit
     *            DroitModele
     * @param langue
     *            à utiliser
     * @return String motif de fin d'échéance
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getLibelleMotif(DroitEcheanceComplexModel droit, String langue) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Méthode qui retourne une collection comprenant les motifs de fin qui doivent traiter d'un avis d'échéances
     * 
     * @return Collection
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public HashSet getListMotifsAutres() throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui retourne une collection comprenant les motifs de fin relatifs à un changement de tarif
     * 
     * @return Collection
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public HashSet getListMotifsAvis() throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui retourne une collection comprenant les types de droits
     * 
     * @return Collection
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public HashSet getListTypeDroit() throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui retourne un booléen pour type de paiement (true direct, false indirect)
     * 
     * @param droitEcheance
     *            modèle de droit passé en paramètres
     * @return Boolean
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean getTypePaiement(DroitEcheanceComplexModel droitEcheance) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * méthode qui retourne la liste des droits (étendus) arrivant à échéances avant ou en même temps que la date passée
     * en paramètre
     * 
     * @param dateEcheance
     *            date de l'échéance du droit
     * @param motifFin
     *            : collection des motifs de fin à passer en paramètre
     * @param typeDroit
     *            types de droit
     * @param typeBonification
     *            type de bonification (direct et indirect)
     * @param typeListe
     *            type de liste (avis échéances, autres échéances
     * @return liste des droits avec données requises pour
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public ArrayList<DroitEcheanceComplexModel> searchDroitsForEcheance(HashSet motifFin, HashSet typeDroit,
            String dateEcheance, String typeBonification, String typeListe, Boolean adi)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui met à jour le champs imprimerEcheance (booléen à false) lorsque l'avis d'échéance a été envoyé
     * 
     * @param droits
     *            liste des droits à mettre à jour
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void updateDroitImprimerEcheance(ArrayList<DroitEcheanceComplexModel> droits, ProtocoleLogger logger)
            throws JadeApplicationException, JadePersistenceException;
}
