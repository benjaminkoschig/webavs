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
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(HomeSearch search) throws HomeException, JadePersistenceException;

    /**
     * Retourne la liste des chambres médicalisées
     * 
     * @param chambreMedicaliseeSearch
     *            Modèle de recherche des chambres médicalisées
     * @return Modèle de recherche avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ChambreMedicaliseeException
     *             Levée en cas de problème métier dans l'exécution du service des chambres médicalisées
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service des homes
     */
    public int countChambreMedicalisee(String idTiers, String dateDonneeFinanciere, String idVersionDroit)
            throws ChambreMedicaliseeException, JadePersistenceException, HomeException;

    /**
     * Crée un nouvel home
     * 
     * @param home
     *            modèle du home à créer
     * @return instance de l'home créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Home create(Home home) throws JadePersistenceException, HomeException;

    /**
     * Permet de créer une période de service etat d'un home. Le champ idHome du modèle passé en paramètre doit être
     * initialisé à l'id d'un home valide.
     * 
     * @param periodeServiceEtat
     *            modèle de la période à créer
     * @return instance de la période créée
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PeriodeServiceEtat createPeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException;

    /**
     * Permet de créer une période de service etat d'un home
     * 
     * @param home
     *            home dont dépend la période à créer
     * @param periodeServiceEtat
     *            modèle de la période à créer
     * @return instance de la période créée
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PeriodeServiceEtat createPeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Crée un nouveau prix de chambre pour un home
     * 
     * @param home
     *            modèle du home
     * @param prixChambre
     *            modèle du prix de chambre à créer
     * @return instance du prix de chambre créé
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service des prix chambre
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service des homes
     */
    public PrixChambre createPrixChambre(Home home, PrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException, HomeException;

    /**
     * Crée un nouveau prix de chambre pour un home.
     * 
     * @param prixChambre
     *            modèle du prix de chambre à créer. Il doit avoir son champ idHome initialisé à l'id d'un home valide
     * @return instance du prix de chambre créé
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service des prix chambre
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service des homes
     */
    public PrixChambre createPrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    /**
     * Permet de créer un type de chambre d'un home
     * 
     * @param home
     *            home dont le type de chambre à créer appartient
     * @param typeChambre
     *            modèle du type de chambre à créer
     * @return type de chambre créé
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TypeChambre createTypeChambre(Home home, TypeChambre typeChambre) throws TypeChambreException,
            HomeException, JadePersistenceException;

    /**
     * Permet de créer un type de chambre d'un home
     * 
     * @param typeChambre
     *            modèle du type de chambre à créer. Il doit avoir le champ idHome initialisé à l'id d'un home valide
     * @return type de chambre créé
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TypeChambre createTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException;

    /**
     * Permet la suppression d'une entité home
     * 
     * @param home
     * 
     * 
     *            Le home à supprimer
     * @return Le home supprimé
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     *             Levée en cas de problème dans le framework Jade
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Home delete(Home home) throws HomeException, JadePersistenceException, TypeChambreException,
            PeriodeServiceEtatException, JadeNoBusinessLogSessionError;

    /**
     * Permet de supprime une période service état d'un home
     * 
     * @param periodeServiceEtat
     *            la période à supprimer
     * @return la période supprimée
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PeriodeServiceEtat deletePeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException;

    /**
     * Permet de supprime une période service état d'un home
     * 
     * @param home
     *            home dont dépend la période
     * @param periodeServiceEtat
     *            la période à supprimer
     * @return la période supprimée
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PeriodeServiceEtat deletePeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Permet la suppression d'un prix de chambre d'un home
     * 
     * @param home
     * @param prixChambre
     *            Le prix de chambre à supprimer
     * @return le prix de chambre supprimé
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PrixChambre deletePrixChambre(Home home, PrixChambre prixChambre) throws HomeException,
            PrixChambreException, JadePersistenceException;

    /**
     * Permet la suppression d'un prix de chambre d'un home
     * 
     * @param prixChambre
     *            Le prix de chambre à supprimer
     * @return le prix de chambre supprimé
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PrixChambre deletePrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    /**
     * Permet la suppression d'un type de chambre d'un home
     * 
     * @param home
     * @param typeChambre
     *            Le type de chambre à supprimer
     * @return le type de chambre supprimé
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeChambre deleteTypeChambre(Home home, TypeChambre typeChambre) throws HomeException,
            JadePersistenceException, TypeChambreException;

    /**
     * Permet la suppression d'un type de chambre d'un home
     * 
     * @param typeChambre
     *            Le type de chambre à supprimer
     * @return le type de chambre supprimé
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeChambre deleteTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException;

    /**
     * Retourne la liste des prix de chambre d'un home
     * 
     * @param home
     *            Le home dont le prix de chambre est dépendant
     * @param prixChambreSearch
     *            Modèle de recherche des prix de chambre du home
     * @return Modèle de recherche avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service des prix de chambre
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service des homes
     */
    public PrixChambreSearch findPrixChambre(Home home, PrixChambreSearch prixChambreSearch)
            throws PrixChambreException, JadePersistenceException, HomeException;

    /**
     * Retourne la liste des prix de chambre d'un home
     * 
     * @param prixChambreSearch
     *            Modèle de recherche des prix de chambre du home
     * @return Modèle de recherche avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service des prix de chambre
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service des homes
     */
    public PrixChambreSearch findPrixChambre(PrixChambreSearch prixChambreSearch) throws PrixChambreException,
            JadePersistenceException, HomeException;

    /**
     * Permet de charger en mémoire un home
     * 
     * @param idHome
     *            L'identifiant du home à charger en mémoire
     * @return Le home chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Home read(String idHome) throws JadePersistenceException, HomeException;

    public PeriodeServiceEtat readPeriode(String id) throws PeriodeServiceEtatException, JadePersistenceException,
            HomeException;

    /**
     * Permet de charger en mémoire un prix de chambre d'un home
     * 
     * @param idPrixChambre
     * @return
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service des homes
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service des prix de chambre
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PrixChambre readPrixChambre(String idPrixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    public TypeChambre readTypeChambre(String id) throws TypeChambreException, JadePersistenceException, HomeException;

    /**
     * Permet de retrouver le type de home (SASH,SPAS) d'un tiers. Utilisé uniquement par les RFM
     * 
     * @param idTiers
     *            L'idTiers du bénéficiaire
     * @param idVersionDroit
     *            La version du droit PC
     * @param date
     *            La date de la période du type de home
     * @return Le modèle de critère avec les résultats
     * 
     * @throws MembreFamilleHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     */
    public MembreFamilleHome retrieveTypeHome(String idTiers, String idVersionDroit, String date)
            throws MembreFamilleHomeException, JadePersistenceException;

    /**
     * Permet de chercher des homes selon un modèle de critères.
     * 
     * @param homeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public HomeSearch search(HomeSearch homeSearch) throws JadePersistenceException, HomeException;

    /**
     * Permet de chercher des périodes service état d'un home
     * 
     * @param periodeSearch
     * @return Le modèle de critère avec les résultats
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PeriodeServiceEtatSearch searchPeriode(PeriodeServiceEtatSearch periodeSearch)
            throws PeriodeServiceEtatException, JadePersistenceException, HomeException;

    /**
     * Permet de chercher des prix de chambre de homes selon un modèle de critères.
     * 
     * @param typeChambreSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws HomeException
     * @throws JadePersistenceException
     * @throws TypeChambreException
     */
    public TypeChambreSearch searchTypeChambre(TypeChambreSearch typeChambreSearch) throws HomeException,
            TypeChambreException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité home
     * 
     * @param home
     *            Le home à mettre à jour
     * @return Le home mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Home update(Home home) throws JadePersistenceException, HomeException;

    /**
     * Permet de mettre à jour une période service etat
     * 
     * @param periodeServiceEtat
     *            La période à mettre à jour. Elle doit avoir le champ idHome initialisé à l'id d'un home valide
     * @return la période mise à jour
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PeriodeServiceEtat updatePeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException;

    /**
     * Permet de mettre à jour une période service etat
     * 
     * @param home
     *            home dont la période est affectée ou doit être affectée
     * @param periodeServiceEtat
     *            La période à mettre à jour
     * @return la période mise à jour
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PeriodeServiceEtat updatePeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Permet la mise à jour d'une prix de chambre d'une entité home
     * 
     * @param home
     * @param prixChambre
     *            Le prix de chambre à mettre à jour
     * @return Le prix de chambre mis à jour
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PrixChambre updatePrixChambre(Home home, PrixChambre prixChambre) throws HomeException,
            PrixChambreException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une prix de chambre d'une entité home. L'entité PrixChambre doit avoir son champ idHome
     * initialisé à l'id d'un home existant
     * 
     * @param prixChambre
     *            Le prix de chambre à mettre à jour
     * @return Le prix de chambre mis à jour
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PrixChambre updatePrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'un type de chambre d'un home
     * 
     * @param home
     *            Home dont dépend le type de chambre
     * @param typeChambre
     *            le type de chambre à modifier
     * @return type de chambre modifié
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service des types de chambre
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service dese homes
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TypeChambre updateTypeChambre(Home home, TypeChambre typeChambre) throws TypeChambreException,
            HomeException, JadePersistenceException;

    /**
     * Permet la mise à jour d'un type de chambre d'un home. Le type de chambre doit avoir le champ idHome initialisé à
     * l'id d'un home existant.
     * 
     * @param typeChambre
     *            le type de chambre à modifier
     * @return type de chambre modifié
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service des types de chambre
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service dese homes
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TypeChambre updateTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException;

}
