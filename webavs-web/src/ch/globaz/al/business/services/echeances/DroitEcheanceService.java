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
 * Service permettant la gestion des listes des �ch�ances li�es aux droits
 * 
 * @author PTA
 * 
 */
public interface DroitEcheanceService extends JadeApplicationService {
    /**
     * M�thode qui retourne le libell� � utiliser pour le motif de fin �ch�ance
     * 
     * @param droit
     *            DroitModele
     * @param langue
     *            � utiliser
     * @return String motif de fin d'�ch�ance
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getLibelleMotif(DroitComplexModel droit, String langue) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�thode qui retourne le libell� � utiliser pour le motif de fin �ch�ance
     * 
     * @param droit
     *            DroitModele
     * @param langue
     *            � utiliser
     * @return String motif de fin d'�ch�ance
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getLibelleMotif(DroitEcheanceComplexModel droit, String langue) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�thode qui retourne une collection comprenant les motifs de fin qui doivent traiter d'un avis d'�ch�ances
     * 
     * @return Collection
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public HashSet getListMotifsAutres() throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode qui retourne une collection comprenant les motifs de fin relatifs � un changement de tarif
     * 
     * @return Collection
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public HashSet getListMotifsAvis() throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode qui retourne une collection comprenant les types de droits
     * 
     * @return Collection
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public HashSet getListTypeDroit() throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode qui retourne un bool�en pour type de paiement (true direct, false indirect)
     * 
     * @param droitEcheance
     *            mod�le de droit pass� en param�tres
     * @return Boolean
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean getTypePaiement(DroitEcheanceComplexModel droitEcheance) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * m�thode qui retourne la liste des droits (�tendus) arrivant � �ch�ances avant ou en m�me temps que la date pass�e
     * en param�tre
     * 
     * @param dateEcheance
     *            date de l'�ch�ance du droit
     * @param motifFin
     *            : collection des motifs de fin � passer en param�tre
     * @param typeDroit
     *            types de droit
     * @param typeBonification
     *            type de bonification (direct et indirect)
     * @param typeListe
     *            type de liste (avis �ch�ances, autres �ch�ances
     * @return liste des droits avec donn�es requises pour
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public ArrayList<DroitEcheanceComplexModel> searchDroitsForEcheance(HashSet motifFin, HashSet typeDroit,
            String dateEcheance, String typeBonification, String typeListe, Boolean adi)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode qui met � jour le champs imprimerEcheance (bool�en � false) lorsque l'avis d'�ch�ance a �t� envoy�
     * 
     * @param droits
     *            liste des droits � mettre � jour
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void updateDroitImprimerEcheance(ArrayList<DroitEcheanceComplexModel> droits, ProtocoleLogger logger)
            throws JadeApplicationException, JadePersistenceException;
}
