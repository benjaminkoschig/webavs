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
 *         Service permettant de g�n�rer une liste de dossier ayant fait objet d'une d�cisions
 */
public interface DecisionListService extends JadeApplicationService {

    /**
     * M�thode qui alimente un stringBuffer
     * 
     * @param listDossier
     *            liste des dossiers
     * @param dateDebut
     *            d�but de la p�riode
     * @param dateFin
     *            fin de la p�riode
     * @param periodicite
     *            String qui permet d'ajouter dans le fichier la notion de periodicite
     * @return StringBuffer contenant des donn��es des dossiers
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public StringBuffer getDonneesListDossier(DossierComplexSearchModel listDossier, String dateDebut, String dateFin,
            String periodicite) throws JadePersistenceException, JadeApplicationException;

    /**
     * M�thode qui retourne la liste de dossier ayant fait l'objet d'une journalisation d�cision GED dans l'intervalle
     * donn�, sauf ceux dont date d�but de validit� d�butant le mois suivant la derni�re factu
     * 
     * @param dateDebut
     *            date limite inf�rieure
     * @param dateFin
     *            date limite sup�rieure
     * @return ArrayList<String> liste de dossier ayant fait l'objet d'une journalisation dans la p�riode des deux dates
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public ArrayList<String> getListDossierJournaliser(String dateDebut, String dateFin)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * M�thode qui retourne la liste de dossier qui doivent faire l'objet de r�troactif
     * 
     * @param dateDebut
     *            Date limite inf�rieure
     * @param dateFin
     *            Date limmite sup�rieure
     * @param listDossier
     *            lsit des dossiers � prendre en compte
     * @param csPeriodicite
     *            code systeme repr�sentant la p�riodicit� d'une Affiliation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public DossierComplexSearchModel getListDossierRetroActif(String dateDebut, String dateFin,
            ArrayList<String> listDossier, String csPeriodicite) throws JadePersistenceException,
            JadeApplicationException;

}
