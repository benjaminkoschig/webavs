package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.ChambreMedicaliseeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.MembreFamilleHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.HomeSearch;
import ch.globaz.pegasus.business.models.home.MembreFamilleHome;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;

public interface HomeService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(HomeSearch search) throws HomeException, JadePersistenceException;

    /**
     * Retourne la liste des chambres m�dicalis�es
     * 
     * @param chambreMedicaliseeSearch
     *            Mod�le de recherche des chambres m�dicalis�es
     * @return Mod�le de recherche avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ChambreMedicaliseeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des chambres m�dicalis�es
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des homes
     */
    public int countChambreMedicalisee(String idTiers, String dateDonneeFinanciere, String idVersionDroit)
            throws ChambreMedicaliseeException, JadePersistenceException, HomeException;

    /**
     * Cr�e un nouvel home
     * 
     * @param home
     *            mod�le du home � cr�er
     * @return instance de l'home cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Home create(Home home) throws JadePersistenceException, HomeException;

    /**
     * Permet de cr�er une p�riode de service etat d'un home. Le champ idHome du mod�le pass� en param�tre doit �tre
     * initialis� � l'id d'un home valide.
     * 
     * @param periodeServiceEtat
     *            mod�le de la p�riode � cr�er
     * @return instance de la p�riode cr��e
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PeriodeServiceEtat createPeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException;

    /**
     * Permet de cr�er une p�riode de service etat d'un home
     * 
     * @param home
     *            home dont d�pend la p�riode � cr�er
     * @param periodeServiceEtat
     *            mod�le de la p�riode � cr�er
     * @return instance de la p�riode cr��e
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PeriodeServiceEtat createPeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Cr�e un nouveau prix de chambre pour un home
     * 
     * @param home
     *            mod�le du home
     * @param prixChambre
     *            mod�le du prix de chambre � cr�er
     * @return instance du prix de chambre cr��
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des prix chambre
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des homes
     */
    public PrixChambre createPrixChambre(Home home, PrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException, HomeException;

    /**
     * Cr�e un nouveau prix de chambre pour un home.
     * 
     * @param prixChambre
     *            mod�le du prix de chambre � cr�er. Il doit avoir son champ idHome initialis� � l'id d'un home valide
     * @return instance du prix de chambre cr��
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des prix chambre
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des homes
     */
    public PrixChambre createPrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    /**
     * Permet de cr�er un type de chambre d'un home
     * 
     * @param home
     *            home dont le type de chambre � cr�er appartient
     * @param typeChambre
     *            mod�le du type de chambre � cr�er
     * @return type de chambre cr��
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TypeChambre createTypeChambre(Home home, TypeChambre typeChambre) throws TypeChambreException,
            HomeException, JadePersistenceException;

    /**
     * Permet de cr�er un type de chambre d'un home
     * 
     * @param typeChambre
     *            mod�le du type de chambre � cr�er. Il doit avoir le champ idHome initialis� � l'id d'un home valide
     * @return type de chambre cr��
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TypeChambre createTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException;

    /**
     * Permet la suppression d'une entit� home
     * 
     * @param home
     * 
     * 
     *            Le home � supprimer
     * @return Le home supprim�
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     *             Lev�e en cas de probl�me dans le framework Jade
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Home delete(Home home) throws HomeException, JadePersistenceException, TypeChambreException,
            PeriodeServiceEtatException, JadeNoBusinessLogSessionError;

    /**
     * Permet de supprime une p�riode service �tat d'un home
     * 
     * @param periodeServiceEtat
     *            la p�riode � supprimer
     * @return la p�riode supprim�e
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PeriodeServiceEtat deletePeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException;

    /**
     * Permet de supprime une p�riode service �tat d'un home
     * 
     * @param home
     *            home dont d�pend la p�riode
     * @param periodeServiceEtat
     *            la p�riode � supprimer
     * @return la p�riode supprim�e
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PeriodeServiceEtat deletePeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Permet la suppression d'un prix de chambre d'un home
     * 
     * @param home
     * @param prixChambre
     *            Le prix de chambre � supprimer
     * @return le prix de chambre supprim�
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PrixChambre deletePrixChambre(Home home, PrixChambre prixChambre) throws HomeException,
            PrixChambreException, JadePersistenceException;

    /**
     * Permet la suppression d'un prix de chambre d'un home
     * 
     * @param prixChambre
     *            Le prix de chambre � supprimer
     * @return le prix de chambre supprim�
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PrixChambre deletePrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    /**
     * Permet la suppression d'un type de chambre d'un home
     * 
     * @param home
     * @param typeChambre
     *            Le type de chambre � supprimer
     * @return le type de chambre supprim�
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeChambre deleteTypeChambre(Home home, TypeChambre typeChambre) throws HomeException,
            JadePersistenceException, TypeChambreException;

    /**
     * Permet la suppression d'un type de chambre d'un home
     * 
     * @param typeChambre
     *            Le type de chambre � supprimer
     * @return le type de chambre supprim�
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeChambre deleteTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException;

    /**
     * Retourne la liste des prix de chambre d'un home
     * 
     * @param home
     *            Le home dont le prix de chambre est d�pendant
     * @param prixChambreSearch
     *            Mod�le de recherche des prix de chambre du home
     * @return Mod�le de recherche avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des prix de chambre
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des homes
     */
    public PrixChambreSearch findPrixChambre(Home home, PrixChambreSearch prixChambreSearch)
            throws PrixChambreException, JadePersistenceException, HomeException;

    /**
     * Retourne la liste des prix de chambre d'un home
     * 
     * @param prixChambreSearch
     *            Mod�le de recherche des prix de chambre du home
     * @return Mod�le de recherche avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des prix de chambre
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des homes
     */
    public PrixChambreSearch findPrixChambre(PrixChambreSearch prixChambreSearch) throws PrixChambreException,
            JadePersistenceException, HomeException;

    /**
     * Permet de charger en m�moire un home
     * 
     * @param idHome
     *            L'identifiant du home � charger en m�moire
     * @return Le home charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Home read(String idHome) throws JadePersistenceException, HomeException;

    public PeriodeServiceEtat readPeriode(String id) throws PeriodeServiceEtatException, JadePersistenceException,
            HomeException;

    /**
     * Permet de charger en m�moire un prix de chambre d'un home
     * 
     * @param idPrixChambre
     * @return
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des homes
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des prix de chambre
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PrixChambre readPrixChambre(String idPrixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    public TypeChambre readTypeChambre(String id) throws TypeChambreException, JadePersistenceException, HomeException;

    /**
     * Permet de retrouver le type de home (SASH,SPAS) d'un tiers. Utilis� uniquement par les RFM
     * 
     * @param idTiers
     *            L'idTiers du b�n�ficiaire
     * @param idVersionDroit
     *            La version du droit PC
     * @param date
     *            La date de la p�riode du type de home
     * @return Le mod�le de crit�re avec les r�sultats
     * 
     * @throws MembreFamilleHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     */
    public MembreFamilleHome retrieveTypeHome(String idTiers, String idVersionDroit, String date)
            throws MembreFamilleHomeException, JadePersistenceException;

    /**
     * Permet de chercher des homes selon un mod�le de crit�res.
     * 
     * @param homeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public HomeSearch search(HomeSearch homeSearch) throws JadePersistenceException, HomeException;

    /**
     * Permet de chercher des p�riodes service �tat d'un home
     * 
     * @param periodeSearch
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PeriodeServiceEtatSearch searchPeriode(PeriodeServiceEtatSearch periodeSearch)
            throws PeriodeServiceEtatException, JadePersistenceException, HomeException;

    /**
     * Permet de chercher des prix de chambre de homes selon un mod�le de crit�res.
     * 
     * @param typeChambreSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws HomeException
     * @throws JadePersistenceException
     * @throws TypeChambreException
     */
    public TypeChambreSearch searchTypeChambre(TypeChambreSearch typeChambreSearch) throws HomeException,
            TypeChambreException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� home
     * 
     * @param home
     *            Le home � mettre � jour
     * @return Le home mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Home update(Home home) throws JadePersistenceException, HomeException;

    /**
     * Permet de mettre � jour une p�riode service etat
     * 
     * @param periodeServiceEtat
     *            La p�riode � mettre � jour. Elle doit avoir le champ idHome initialis� � l'id d'un home valide
     * @return la p�riode mise � jour
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PeriodeServiceEtat updatePeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException;

    /**
     * Permet de mettre � jour une p�riode service etat
     * 
     * @param home
     *            home dont la p�riode est affect�e ou doit �tre affect�e
     * @param periodeServiceEtat
     *            La p�riode � mettre � jour
     * @return la p�riode mise � jour
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PeriodeServiceEtat updatePeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Permet la mise � jour d'une prix de chambre d'une entit� home
     * 
     * @param home
     * @param prixChambre
     *            Le prix de chambre � mettre � jour
     * @return Le prix de chambre mis � jour
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PrixChambre updatePrixChambre(Home home, PrixChambre prixChambre) throws HomeException,
            PrixChambreException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une prix de chambre d'une entit� home. L'entit� PrixChambre doit avoir son champ idHome
     * initialis� � l'id d'un home existant
     * 
     * @param prixChambre
     *            Le prix de chambre � mettre � jour
     * @return Le prix de chambre mis � jour
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PrixChambre updatePrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    /**
     * Permet la mise � jour d'un type de chambre d'un home
     * 
     * @param home
     *            Home dont d�pend le type de chambre
     * @param typeChambre
     *            le type de chambre � modifier
     * @return type de chambre modifi�
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des types de chambre
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service dese homes
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TypeChambre updateTypeChambre(Home home, TypeChambre typeChambre) throws TypeChambreException,
            HomeException, JadePersistenceException;

    /**
     * Permet la mise � jour d'un type de chambre d'un home. Le type de chambre doit avoir le champ idHome initialis� �
     * l'id d'un home existant.
     * 
     * @param typeChambre
     *            le type de chambre � modifier
     * @return type de chambre modifi�
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service des types de chambre
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service dese homes
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TypeChambre updateTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException;

}
