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
     * Controle si un rappel LIBRA de type csFormule est en cours pour un idDetailFamille donné
     * 
     * @param idDetailFamille
     * @param csFormule
     * @return null si pas de rappel actif, idJournalisation LIBRA le cas contraire
     */
    public String checkRappelInProgress(String idDetailFamille, String csFormule);

    /**
     * Fonction de suppression d'un lot de rappel pour un jour donné
     * 
     * @param idAsDateRappel
     *            la date du jour à supprimer selon le format yyyyMMdd
     * 
     */
    public void deleteRappel(String idAsDateRappel);

    /**
     * Fonction de suppression d'un rappel libra particulier, en fonction de son id
     * 
     * @param idLibraJournalisation
     *            id de l'élément libra (journalisation) à supprimer
     */
    public void deleteRappelLibra(String idLibraJournalisation);

    /**
     * Fonction de génération du job d'impression des rappels, pour une date donnée
     * 
     * @param idAsDateRappel
     *            date du rappel à générer, format yyyyMMdd
     */
    public void generateRappel(String idAsDateRappel);

    /**
     * Fonction de génération d'un rappel Libra particulier, en fonction de son id
     * 
     * @param idLibraJournalisation
     *            id de l'élément libra (journalisation) à générer
     * 
     */
    public void generateRappelLibra(String idLibraJournalisation);

    /**
     * Récupération de la liste des formules qui sont des formules de rappels
     * 
     * @return liste des formules (code système)
     * 
     */
    public ArrayList<String> getFormulesRappel();

    /**
     * Récupération de l'échéance ouverte pour un detailFamille particulier
     * 
     * 
     * @param idDetailFamille
     * @return idJournalisation si une échéance est ouverte, null sinon
     * 
     */
    public String getLIBRARappel(String idDetailFamille);

    /**
     * Récupération des données de LIBRA et renseignement d'une HashMap de travail
     * 
     * Récupération de toutes les échéances rappel ouvertes pour AMAL
     * 
     * @return
     */
    public HashMap<String, ArrayList<AMControleurRappelDetail>> getLIBRARappels();

    /**
     * Récupération des données de LIBRA et renseignement d'une HashMap de travail
     * 
     * @param dateRequested
     *            rappel à traiter à la date dateRequested
     * @return
     */
    public HashMap<String, ArrayList<AMControleurRappelDetail>> getLIBRARappels(String dateRequested);

    /**
     * Suppression d'un rappel en cours de traitement (dans les éléments modifiables) Remet Libra à jour comme étant non
     * plus une journalisation, mais une échéance de type rappel
     * 
     * @param idDetailFamille
     * @param csFormule
     */
    public void rollbackRappelInProgressToRappel(String idDetailFamille, String csFormule);

    /**
     * update la journalisation LIBRA idJournalisation en status rappel, date de réception devient la date de génération
     * du rappel
     * 
     * @param idJournalisation
     */
    public void updateRappelLibraRappelGenerated(String idJournalisation);

    /**
     * update la journalisatio LIBRA idJournalisation en status reception manuelle, la date de réception devient la date
     * de génération du nouveau document
     * 
     * @param idJournalisation
     */
    public void updateRappelLibraReceptionManuelle(String idJournalisation);

}
