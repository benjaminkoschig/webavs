package ch.globaz.vulpecula.business.services.decompte;

import globaz.jade.exception.JadePersistenceException;
import java.util.Collection;
import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.businessimpl.services.decompte.TauxCotisationDecompteLoader;
import ch.globaz.vulpecula.documents.decompte.CotisationsInfo;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteReceptionEtat;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.DifferenceControle;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;

public interface DecompteService {

    public DifferenceControle controler(Decompte decompte);

    public DifferenceControle controler(String idDecompte, boolean pasDeControle);

    boolean updateEtat(final String idDecompte, EtatDecompte etat);

    String genererSommationManuel(String vide) throws Exception;

    /**
     * Dévalide un décompte en changeant son état en OUVERT et son historique en OUVERT
     * 
     * @param idDecompte String id du décompte à dévalider
     */
    void devalider(String idDecompte);

    /**
     * Dévalide un décompte en changeant son historique et son état en OUVERT.
     * 
     * @param decompte Décompte à modifier
     */
    void devaliderDecompte(Decompte decompte);

    /**
     * Modifier l'état du décompte et le met à annulé.
     * 
     * @param idDecompte
     * @throws NullPointerException
     */
    void annuler(String idDecompte);

    void modifierControleErrone(String idDecompte);

    void rectifierControleErrone(String idDecompte, String remarqueRectificatif);

    /**
     * Réception d'un décompte (changement d'état à réceptionné) et mise à jour de la date de réception.
     * 
     * @param idDecompte String représentant l'id du décomtpe à mettre à jour
     * @param date String représentant la date de réception d'un décompte
     * @return Nouvel état du décompte
     */
    DecompteReceptionEtat receptionner(String idDecompte, String date);

    /**
     * Reinitialisation d'un décompte si état <> COMPTABILISE :</br>
     * <ul>
     * <li>Suppression de la date de réception</li>
     * <li>Changement de l'état à 'Généré'</li>
     * </ul>
     * 
     * @param decompte Decompte à mettre à jour
     * @return Nouvel état du décompte
     */
    void reinitialiseDecompte(Decompte decompte);

    /**
     * Réception d'un décompte (changement d'état à réceptionné) et mise à jour de la date de réception.
     * 
     * @param idDecompte String représentant l'id du décomtpe à mettre à jour
     * @param date Date de réception du décompte
     * @return Nouvel état du décompte
     */
    DecompteReceptionEtat receptionner(String idDecompte, Date date);

    /**
     * Réception d'un décompte (changement d'état à réceptionné) et mise à jour de la date de réception.
     * 
     * @param decompte Decompte à mettre à jour
     * @param date Date de réception du décompte
     * @return Nouvel état du décompte
     */
    DecompteReceptionEtat receptionner(Decompte decompte, Date date);

    /**
     * Retourne le dernier numéro de décompte généré pour cet employeur, type de décompte, date et périodicité.
     * 
     * @param employeur
     * @param typeDecompte
     * @param date
     * @param periodicite
     * @return Retourne le dernier no de décompte ou null si aucun décompte ne possède
     */
    NumeroDecompte getLastNoDecompte(final Employeur employeur, final TypeDecompte typeDecompte, final Date date,
            final String periodicite);

    /**
     * Lorsque l'on ajoute un poste de travail du décompte, il faut recalculer les cotisations
     */
    void ajoutCotisationsPourPoste(DecompteSalaire decompteSalaire);

    void ajoutCotisationPourLigneAPG(DecompteSalaire decompteSalaire);

    void ajoutCotisationsPourPoste(DecompteSalaire decompteSalaire, Montant masseAC2);

    List<CotisationDecompte> getCotisationsPosteTravail(DecompteSalaire decompteSalaire);

    Decompte update(Decompte decompte) throws UnsatisfiedSpecificationException, JadePersistenceException;

    Collection<Decompte> rechercheDecomptesRectificatif();

    /**
     * Recherche du décompte précédent au décompte envoyé. Ce dernier ne doit pas être au statut OUVERT, GENERE
     * 
     * @param decompte Decompte à partir duquel le précédent doit être recherché
     * @return Decompte précédent le décompte passé en paramètre
     */
    Decompte findDecomptePrecedent(Decompte decompte);

    Montant getPlafond(Assurance assurance, Date date);

    public void imprimerDecompteVideEtBVR(Decompte decompte, String email) throws Exception;

    /**
     * Recherche des différents taux pour les types de cotisations AVS, AC et
     * AC2 de l'employeur sous la forme d'un objet {@link CotisationsInfo}.
     * 
     * @return {@link CotisationsInfo}
     */
    CotisationsInfo retrieveCotisationsInfo(String idEmployeur, Date date);

    /**
     * Recherche des éléments relatives aux décomptes, notamment l'adresse de l'employeur ainsi que ses cotisations.
     * 
     * @param decompte
     *            décompte à compléter
     * @param date
     *            Date à laquelle l'activité doit être déterminée
     * @return Decompte complet
     */
    Decompte retrieveDecompteInfos(Decompte decompte);

    /**
     * Retourne le prochain numéro de décompte disponible pour cet employeur, ce type de décompte, cette date et cette
     * périodicité.
     * 
     * @param employeur Employeur pour lequel créé le numéro de décompte
     * @param typeDecompte Type de décompte
     * @param date Date à laquelle sera généré le numéro
     * @param periodicite Periodicité de l'employeur
     * @return Premier numéro de décompte disponible
     */
    NumeroDecompte getNumeroDecompte(Employeur employeur, TypeDecompte typeDecompte, Date date, String periodicite);

    CotisationDecompte createCotisationDecompte(AdhesionCotisationPosteTravail adhesionCotisation, Taux taux,
            String nextId);

    /**
     * Retourne les cotisations actives pour le décompte passé en paramètres.
     * 
     * @param decompte DécompteSalaire pour lequel les cotisations doivent être déduites. Certaines cotisations ne sont
     *            pas
     *            actives pour les décomptes de type CP et on vérifie que le travailleur soit bien en âge de cotiser.
     * @return Liste des adhesions actives
     */
    List<AdhesionCotisationPosteTravail> findAdhesionsForDecompteSalaire(DecompteSalaire decompteSalaire);

    List<AdhesionCotisationPosteTravail> findAdhesionsForDecompteSalaireCP(DecompteSalaire decompteSalaire);

    void annulerDecompteForParticularite(String idAffilie, String dateDebut, String dateFin);

    /**
     * Retourne le texte de la personne de référence pour le décompte passé en paramètre.
     * 
     * @param decompte Décompte pour lequel rechercher la personne de référence
     * @return Texte du rectificatif
     */
    String findTextePersonneReference(Decompte decompte);

    /**
     * Ajoute une entrée dans la table de synchronisation avec EBU afin de pouvoir le notifier à ServiceNow
     * 
     * @param idDecompte id du décompte
     * @throws JadePersistenceException
     */
    public void notifierSynchronisationEbu(String idDecompte, String idEmployeur, TypeDecompte type)
            throws JadePersistenceException;

    public void actualiserTauxCotisations(DecompteSalaire decompteSalaire, Annee annee);

    /**
     * Génération des cotisations pour un décompte selon le type de salaire.
     * <ul>
     * <li>Si le décompte est de type CPP : On prend les cotisations de l'affilié
     * <li>Si le décompte est d'un autre type : On prend les cotisations du poste de travail
     * 
     * @param decompteSalaire Décompte salaire pour lequel générer les cotisations
     * @return Liste de cotisations
     */
    List<CotisationDecompte> genererTableCotisation(DecompteSalaire decompteSalaire,
            TauxCotisationDecompteLoader tauxCotisationDecompteLoader);

    /**
     * Retourne true si l'affilié lié au décompte possède au moins un dossier avec une prestation AF entre la date de
     * début du décompte et la date de fin du décompte.
     * Dans le cas où l'affilié ne possède pas de dossiers AF, on retourne également true.
     * 
     * @param decompte Décompte pour lequel rechercher les prestations AF
     * @return true si possède des prestations ou que l'affilié ne possède pas de dossier
     */
    boolean hasPrestationsAFOuPasDossier(Decompte decompte);

    public void actualiserDecompteSelonPoste(PosteTravail posteTravailCreated) throws JadePersistenceException,
            UnsatisfiedSpecificationException;

    public void actualiserDecompteForExternalServices(String idEmployeur, String dateATraiter)
            throws JadePersistenceException, UnsatisfiedSpecificationException;

}
