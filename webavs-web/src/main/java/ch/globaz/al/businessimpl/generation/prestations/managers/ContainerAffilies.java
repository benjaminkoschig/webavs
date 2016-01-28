package ch.globaz.al.businessimpl.generation.prestations.managers;

import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextGlobal;

/**
 * Conteneurs d'affiliés
 * 
 * @author jts
 */
public class ContainerAffilies {

    /** Taille maximale du conteneur */
    private static final int CONTAINER_MAX_SIZE = 5000;

    /**
     * Conteneur d'instances de {@link ch.globaz.al.businessimpl.generation.prestations.managers.ContainerAffilies}
     */
    private static HashMap<String, ContainerAffilies> containers = new HashMap<String, ContainerAffilies>();

    /**
     * Verrou pour l'initialisation
     */
    private static final Object tokenInit = new Object();

    /**
     * Retourne une instance unique de <code>ImportDossierContainer</code>
     * 
     * @param uniqueID
     *            Identifiant unique permettant d'identifier le process ayant lancé la génération
     * 
     * @return instance de <code>ContainerAffilies</code> correspondant à <code>uniqueID</code>
     */
    public static ContainerAffilies getInstance(String uniqueID) {
        synchronized (ContainerAffilies.tokenInit) {
            if (ContainerAffilies.containers.get(uniqueID) == null) {
                ContainerAffilies.containers.put(uniqueID, new ContainerAffilies());
            }
        }
        return ContainerAffilies.containers.get(uniqueID);
    }

    /**
     * Indique que des données ont été ajoutées dans le conteneur
     */
    private boolean canConsuming = false;

    /**
     * Liste des affiliés à traiter
     */
    private ArrayList<ContextGlobal> dossiersForAffilies = null;
    /**
     * Indique si le producteur a terminé d'ajouter
     */
    private boolean finishedProducing;
    private int nbDossier = 0;

    /**
     * Verrou
     */
    private final Object token = new Object();

    /**
     * Constructeur
     */
    private ContainerAffilies() {
        super();
        dossiersForAffilies = new ArrayList<ContextGlobal>();
    }

    /**
     * Retourne les dossiers à traiter pour un affilié
     * 
     * @return les dossier d'un affilié
     */
    public ContextGlobal getDossiers() {
        ContextGlobal next = null;
        synchronized (token) {
            if (dossiersForAffilies.size() > 0) {
                next = dossiersForAffilies.get(0);
                nbDossier = nbDossier - next.countDossiers();
                dossiersForAffilies.remove(0);
            }
        }
        return next;
    }

    /**
     * Vérifie si le nombre d'élément dans le conteneur à atteint ou dépassé le nombre maximal d'élément
     * 
     * @return <code>true</code>
     */
    public boolean hasReachedMaxSize() {
        return nbOfWaitingItems() >= ContainerAffilies.CONTAINER_MAX_SIZE;
    }

    /**
     * Indique si des données peuvent être récupérées dans le conteneur
     * 
     * @return <code>true</code> si des données peuvent être récupérées
     */
    public boolean isAbleToConsume() {
        return (canConsuming && (dossiersForAffilies.size() > 0));
    }

    /**
     * Indique si le producteur à terminé de remplir le conteneur
     * 
     * @return <code>true</code> si le producteur a terminé, <code>false</code> sinon
     */
    public boolean isFinishedProducing() {
        return finishedProducing;
    }

    /**
     * Nombre d'affiliés en attente
     * 
     * @return Nombre d'affiliés en attente
     */
    public int nbOfWaitingItems() {
        int nb = 0;
        synchronized (token) {
            nb = dossiersForAffilies.size();
        }
        return nb;
    }

    public int nbOfWatingDossiers() {
        return nbDossier;
    }

    /**
     * Indique au conteneur qu'il n'y a plus d'éléments à ajouter
     */
    protected void notifyFinishedProducing() {
        finishedProducing = true;
    }

    /**
     * Ajoute les dossiers d'un affilié au conteneur
     * 
     * @param globalContext
     *            Résultat d'une recherche de dossiers pour un affilié
     */
    public void registerDossiers(ContextGlobal globalContext) {
        synchronized (token) {
            canConsuming = true;
            nbDossier = nbDossier + globalContext.countDossiers();
            dossiersForAffilies.add(globalContext);
        }
    }
}