/**
 * 
 */
package ch.globaz.amal.business.services.models.controleurRappel;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.amal.business.echeances.AMControleurRappelDetail;

/**
 * @author DHI
 * 
 */
public interface ControleurRappelService extends JadeApplicationService {

    /**
     * Controle si un rappel LIBRA n'est pas en cours pour un idDetailFamille particulier
     * 
     * @param idDetailFamille
     * @return null si pas de rappel actif, idJournalisation LIBRA le cas contraire
     */
    public String checkRappelInProgress(String idDetailFamille);

    /**
     * 
     * Controle si un rappel LIBRA de type csFormule est en cours pour un idDetailFamille donn�
     * 
     * @param idDetailFamille
     * @param csFormule
     * @return null si pas de rappel actif, idJournalisation LIBRA le cas contraire
     */
    public String checkRappelInProgress(String idDetailFamille, String csFormule);

    /**
     * Fonction de suppression d'un lot de rappel pour un jour donn�
     * 
     * @param idAsDateRappel
     *            la date du jour � supprimer selon le format yyyyMMdd
     * 
     */
    public void deleteRappel(String idAsDateRappel);

    /**
     * Fonction de suppression d'un rappel libra particulier, en fonction de son id
     * 
     * @param idLibraJournalisation
     *            id de l'�l�ment libra (journalisation) � supprimer
     */
    public void deleteRappelLibra(String idLibraJournalisation);

    /**
     * Fonction de g�n�ration du job d'impression des rappels, pour une date donn�e
     * 
     * @param idAsDateRappel
     *            date du rappel � g�n�rer, format yyyyMMdd
     */
    public void generateRappel(String idAsDateRappel);

    /**
     * Fonction de g�n�ration d'un rappel Libra particulier, en fonction de son id
     * 
     * @param idLibraJournalisation
     *            id de l'�l�ment libra (journalisation) � g�n�rer
     * 
     */
    public void generateRappelLibra(String idLibraJournalisation);

    /**
     * R�cup�ration de la liste des formules qui sont des formules de rappels
     * 
     * @return liste des formules (code syst�me)
     * 
     */
    public ArrayList<String> getFormulesRappel();

    /**
     * R�cup�ration de l'�ch�ance ouverte pour un detailFamille particulier
     * 
     * 
     * @param idDetailFamille
     * @return idJournalisation si une �ch�ance est ouverte, null sinon
     * 
     */
    public String getLIBRARappel(String idDetailFamille);

    /**
     * R�cup�ration des donn�es de LIBRA et renseignement d'une HashMap de travail
     * 
     * R�cup�ration de toutes les �ch�ances rappel ouvertes pour AMAL
     * 
     * @return
     */
    public HashMap<String, ArrayList<AMControleurRappelDetail>> getLIBRARappels();

    /**
     * R�cup�ration des donn�es de LIBRA et renseignement d'une HashMap de travail
     * 
     * @param dateRequested
     *            rappel � traiter � la date dateRequested
     * @return
     */
    public HashMap<String, ArrayList<AMControleurRappelDetail>> getLIBRARappels(String dateRequested);

    /**
     * Suppression d'un rappel en cours de traitement (dans les �l�ments modifiables) Remet Libra � jour comme �tant non
     * plus une journalisation, mais une �ch�ance de type rappel
     * 
     * @param idDetailFamille
     * @param csFormule
     */
    public void rollbackRappelInProgressToRappel(String idDetailFamille, String csFormule);

    /**
     * update la journalisation LIBRA idJournalisation en status rappel, date de r�ception devient la date de g�n�ration
     * du rappel
     * 
     * @param idJournalisation
     */
    public void updateRappelLibraRappelGenerated(String idJournalisation);

    /**
     * update la journalisatio LIBRA idJournalisation en status reception manuelle, la date de r�ception devient la date
     * de g�n�ration du nouveau document
     * 
     * @param idJournalisation
     */
    public void updateRappelLibraReceptionManuelle(String idJournalisation);

}
