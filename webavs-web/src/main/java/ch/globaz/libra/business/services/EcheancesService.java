package ch.globaz.libra.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.model.Echeance;
import ch.globaz.libra.business.model.EcheanceSearch;

public interface EcheancesService extends JadeApplicationService {

    /**
     * [2] Création d'échéance simple.
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
     * 
     * @param dateRappel
     * @param idExterne
     * @param libelle
     * @param idTiers
     * @param csDomaine
     * @param isDossier
     * 
     * @throws Exception
     */
    public String createManuellWithTestDossier(String dateRappel, String idExterne, String libelle, String remarque,
            String idTiers, String csDomaine, boolean isDossier) throws Exception;

    /**
     * [1] Création d'échéance simple.
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
     * 
     * @param dateRappel
     * @param idExterne
     * @param libelle
     * @param isDossier
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void createRappel(String dateRappel, String idExterne, String libelle, boolean isDossier)
            throws LibraException;

    /**
     * [2] Création d'échéance simple.
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
     * 
     * @param dateRappel
     * @param idExterne
     * @param libelle
     * @param idTiers
     * @param csDomaine
     * @param isDossier
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void createRappelWithTestDossier(String dateRappel, String idExterne, String libelle, String idTiers,
            String csDomaine, boolean isDossier) throws LibraException;

    /**
     * @param search
     * @return
     * @throws LibraException
     */
    public List<Echeance> search(EcheanceSearch search) throws LibraException;

    /**
     * @param search
     * @return
     * @throws LibraException
     */
    public List<Echeance> search(EcheanceSearch search, int mgr_size) throws LibraException;

}
