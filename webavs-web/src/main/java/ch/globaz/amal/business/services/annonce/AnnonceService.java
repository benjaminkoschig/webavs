/**
 * 
 */
package ch.globaz.amal.business.services.annonce;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import java.util.List;

/**
 * @author dhi
 * 
 */
public interface AnnonceService extends JadeApplicationService {

    /**
     * Création des jobs annonces/simulation pour accueillir les résultats des process
     * 
     * @param idTiersGroupe
     *            à renseigner pour traiter un groupe
     * @param idTiersCM
     *            liste des caisses maladies à traiter si pas groupe
     */
    public void createAnnoncesSimulationJobs(String idTiersGroupe, List<String> idTiersCM);

    /**
     * Récupération des annonces à créer, sous forme de liste d'id detail famille par caisse, qui sont touchés par
     * l'annonce
     * 
     * @param idTiersGroupe
     *            à renseigner pour traiter un groupe
     * @param idTiersCM
     *            liste des caisses maladies à traiter si pas de groupe
     * @param anneeHistorique
     *            année historique si limitation à une année donnée (fin d'année)
     * @return
     *         List d'id detail famille par caisse maladie
     */
    public HashMap<String, List<String>> getAnnoncesToCreate(String idTiersGroupe, List<String> idTiersCM,
            String anneeHistorique);

    /**
     * Récupération des idTiers CM dont un travail d'annonce est en cours
     * 
     * @return
     */
    public List<String> getIdTiersCMAnnonceInProgress();

    /**
     * Récupération des idTiers CM dont un travail de simulation d'annonce est en cours
     * 
     * @return
     */
    public List<String> getIdTiersCMSimulationInProgress();

    /**
     * Ecriture des annonces dans les tables de job, status, annonces caisses
     * 
     * @param annoncesToCreate
     *            Liste des idDetailFamille à traiter, par caisse maladie (idTiers)
     */
    public List<String> writeAnnoncesInTables(HashMap<String, List<String>> annoncesToCreate);

    /**
     * Ecritures des annonces dans les tables de job, status et annonces caisses
     * 
     * @param idTiersGroupe
     *            à renseigner pour traiter un groupe
     * @param idTiersCM
     *            liste des caisses maladies à traiter si pas de groupe
     * @param anneeHistorique
     *            année historique si limitation à une année donnée (fin d'année)
     */
    public void writeAnnoncesInTables(String idTiersGroupe, List<String> idTiersCM, String anneeHistorique);

}
