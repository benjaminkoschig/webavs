package ch.globaz.libra.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.model.Journalisation;
import ch.globaz.libra.business.model.JournalisationSearch;

public interface JournalisationService extends JadeApplicationService {

    /**
     * [1] Création de journalisation simple
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
     * 
     * @param idExterne
     * @param libelle
     * @param isDossier
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void createJournalisation(String idExterne, String libelle, boolean isDossier) throws LibraException;

    /**
     * [3] Création de journalisation avec remarque
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité -> Création d'une remarque
     * dans la journalisation
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
     * 
     * @param idExterne
     * @param libelle
     * @param remarque
     * @param isDossier
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void createJournalisationAvecRemarque(String idExterne, String libelle, String remarque, boolean isDossier)
            throws LibraException;

    /**
     * [4] Création de journalisation avec remarque
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité -> Création d'une remarque
     * dans la journalisation
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
     * 
     * @param idExterne
     * @param libelle
     * @param remarque
     * @param idTiers
     * @param csDomaine
     * @param isDossier
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void createJournalisationAvecRemarqueWithTestDossier(String idExterne, String libelle, String remarque,
            String idTiers, String csDomaine, boolean isDossier) throws LibraException;

    /**
     * [2] Création de journalisation simple
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
     * 
     * @param idExterne
     * @param libelle
     * @param idTiers
     * @param csDomaine
     * @param isDossier
     * 
     * @throws LibraException
     *             Levée en cas de problème lors de la création de...
     */
    public void createJournalisationWithTestDossier(String idExterne, String libelle, String idTiers, String csDomaine,
            boolean isDossier) throws LibraException;

    /**
     * @param search
     * @return
     * @throws LibraException
     */
    public List<Journalisation> search(JournalisationSearch search, int mgr_size) throws LibraException;

}
