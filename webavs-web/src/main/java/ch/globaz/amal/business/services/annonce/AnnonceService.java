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
     * Cr�ation des jobs annonces/simulation pour accueillir les r�sultats des process
     * 
     * @param idTiersGroupe
     *            � renseigner pour traiter un groupe
     * @param idTiersCM
     *            liste des caisses maladies � traiter si pas groupe
     */
    public void createAnnoncesSimulationJobs(String idTiersGroupe, List<String> idTiersCM);

    /**
     * R�cup�ration des annonces � cr�er, sous forme de liste d'id detail famille par caisse, qui sont touch�s par
     * l'annonce
     * 
     * @param idTiersGroupe
     *            � renseigner pour traiter un groupe
     * @param idTiersCM
     *            liste des caisses maladies � traiter si pas de groupe
     * @param anneeHistorique
     *            ann�e historique si limitation � une ann�e donn�e (fin d'ann�e)
     * @return
     *         List d'id detail famille par caisse maladie
     */
    public HashMap<String, List<String>> getAnnoncesToCreate(String idTiersGroupe, List<String> idTiersCM,
            String anneeHistorique);

    /**
     * R�cup�ration des idTiers CM dont un travail d'annonce est en cours
     * 
     * @return
     */
    public List<String> getIdTiersCMAnnonceInProgress();

    /**
     * R�cup�ration des idTiers CM dont un travail de simulation d'annonce est en cours
     * 
     * @return
     */
    public List<String> getIdTiersCMSimulationInProgress();

    /**
     * Ecriture des annonces dans les tables de job, status, annonces caisses
     * 
     * @param annoncesToCreate
     *            Liste des idDetailFamille � traiter, par caisse maladie (idTiers)
     */
    public List<String> writeAnnoncesInTables(HashMap<String, List<String>> annoncesToCreate);

    /**
     * Ecritures des annonces dans les tables de job, status et annonces caisses
     * 
     * @param idTiersGroupe
     *            � renseigner pour traiter un groupe
     * @param idTiersCM
     *            liste des caisses maladies � traiter si pas de groupe
     * @param anneeHistorique
     *            ann�e historique si limitation � une ann�e donn�e (fin d'ann�e)
     */
    public void writeAnnoncesInTables(String idTiersGroupe, List<String> idTiersCM, String anneeHistorique);

}
