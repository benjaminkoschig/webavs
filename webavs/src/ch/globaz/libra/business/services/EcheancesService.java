package ch.globaz.libra.business.services;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.model.Echeance;
import ch.globaz.libra.business.model.EcheanceSearch;

public interface EcheancesService extends JadeApplicationService {

    /**
     * [2] Cr�ation d'�ch�ance simple.
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de li� au dossier si souhait�
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
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
     * [1] Cr�ation d'�ch�ance simple.
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de li� au dossier si souhait�
     * 
     * ==> Cette m�thode n�cessite qu'un dossier existe d�j� dans LIBRA
     * 
     * @param dateRappel
     * @param idExterne
     * @param libelle
     * @param isDossier
     * 
     * @throws LibraException
     *             Lev�e en cas de probl�me lors de la cr�ation de...
     */
    public void createRappel(String dateRappel, String idExterne, String libelle, boolean isDossier)
            throws LibraException;

    /**
     * [2] Cr�ation d'�ch�ance simple.
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de li� au dossier si souhait�
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
     * 
     * @param dateRappel
     * @param idExterne
     * @param libelle
     * @param idTiers
     * @param csDomaine
     * @param isDossier
     * 
     * @throws LibraException
     *             Lev�e en cas de probl�me lors de la cr�ation de...
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
