/**
 * 
 */
package ch.globaz.al.business.services.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;

/**
 * @author pta
 * 
 *         Service permettant de générer une liste de dossier ayant fait objet d'une décisions
 */
public interface DecisionListService extends JadeApplicationService {

    /**
     * Méthode qui alimente un stringBuffer
     * 
     * @param listDossier
     *            liste des dossiers
     * @param dateDebut
     *            début de la période
     * @param dateFin
     *            fin de la période
     * @param periodicite
     *            String qui permet d'ajouter dans le fichier la notion de periodicite
     * @return StringBuffer contenant des donnéées des dossiers
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public StringBuffer getDonneesListDossier(DossierComplexSearchModel listDossier, String dateDebut, String dateFin,
            String periodicite) throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode qui retourne la liste de dossier ayant fait l'objet d'une journalisation décision GED dans l'intervalle
     * donné, sauf ceux dont date début de validité débutant le mois suivant la dernière factu
     * 
     * @param dateDebut
     *            date limite inférieure
     * @param dateFin
     *            date limite supérieure
     * @return ArrayList<String> liste de dossier ayant fait l'objet d'une journalisation dans la période des deux dates
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public ArrayList<String> getListDossierJournaliser(String dateDebut, String dateFin)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode qui retourne la liste de dossier qui doivent faire l'objet de rétroactif
     * 
     * @param dateDebut
     *            Date limite inférieure
     * @param dateFin
     *            Date limmite supérieure
     * @param listDossier
     *            lsit des dossiers à prendre en compte
     * @param csPeriodicite
     *            code systeme représentant la périodicité d'une Affiliation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public DossierComplexSearchModel getListDossierRetroActif(String dateDebut, String dateFin,
            ArrayList<String> listDossier, String csPeriodicite) throws JadePersistenceException,
            JadeApplicationException;

}
