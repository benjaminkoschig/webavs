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
     * D�valide un d�compte en changeant son �tat en OUVERT et son historique en OUVERT
     * 
     * @param idDecompte String id du d�compte � d�valider
     */
    void devalider(String idDecompte);

    /**
     * D�valide un d�compte en changeant son historique et son �tat en OUVERT.
     * 
     * @param decompte D�compte � modifier
     */
    void devaliderDecompte(Decompte decompte);

    /**
     * Modifier l'�tat du d�compte et le met � annul�.
     * 
     * @param idDecompte
     * @throws NullPointerException
     */
    void annuler(String idDecompte);

    void modifierControleErrone(String idDecompte);

    void rectifierControleErrone(String idDecompte, String remarqueRectificatif);

    /**
     * R�ception d'un d�compte (changement d'�tat � r�ceptionn�) et mise � jour de la date de r�ception.
     * 
     * @param idDecompte String repr�sentant l'id du d�comtpe � mettre � jour
     * @param date String repr�sentant la date de r�ception d'un d�compte
     * @return Nouvel �tat du d�compte
     */
    DecompteReceptionEtat receptionner(String idDecompte, String date);

    /**
     * Reinitialisation d'un d�compte si �tat <> COMPTABILISE :</br>
     * <ul>
     * <li>Suppression de la date de r�ception</li>
     * <li>Changement de l'�tat � 'G�n�r�'</li>
     * </ul>
     * 
     * @param decompte Decompte � mettre � jour
     * @return Nouvel �tat du d�compte
     */
    void reinitialiseDecompte(Decompte decompte);

    /**
     * R�ception d'un d�compte (changement d'�tat � r�ceptionn�) et mise � jour de la date de r�ception.
     * 
     * @param idDecompte String repr�sentant l'id du d�comtpe � mettre � jour
     * @param date Date de r�ception du d�compte
     * @return Nouvel �tat du d�compte
     */
    DecompteReceptionEtat receptionner(String idDecompte, Date date);

    /**
     * R�ception d'un d�compte (changement d'�tat � r�ceptionn�) et mise � jour de la date de r�ception.
     * 
     * @param decompte Decompte � mettre � jour
     * @param date Date de r�ception du d�compte
     * @return Nouvel �tat du d�compte
     */
    DecompteReceptionEtat receptionner(Decompte decompte, Date date);

    /**
     * Retourne le dernier num�ro de d�compte g�n�r� pour cet employeur, type de d�compte, date et p�riodicit�.
     * 
     * @param employeur
     * @param typeDecompte
     * @param date
     * @param periodicite
     * @return Retourne le dernier no de d�compte ou null si aucun d�compte ne poss�de
     */
    NumeroDecompte getLastNoDecompte(final Employeur employeur, final TypeDecompte typeDecompte, final Date date,
            final String periodicite);

    /**
     * Lorsque l'on ajoute un poste de travail du d�compte, il faut recalculer les cotisations
     */
    void ajoutCotisationsPourPoste(DecompteSalaire decompteSalaire);

    void ajoutCotisationPourLigneAPG(DecompteSalaire decompteSalaire);

    void ajoutCotisationsPourPoste(DecompteSalaire decompteSalaire, Montant masseAC2);

    List<CotisationDecompte> getCotisationsPosteTravail(DecompteSalaire decompteSalaire);

    Decompte update(Decompte decompte) throws UnsatisfiedSpecificationException, JadePersistenceException;

    Collection<Decompte> rechercheDecomptesRectificatif();

    /**
     * Recherche du d�compte pr�c�dent au d�compte envoy�. Ce dernier ne doit pas �tre au statut OUVERT, GENERE
     * 
     * @param decompte Decompte � partir duquel le pr�c�dent doit �tre recherch�
     * @return Decompte pr�c�dent le d�compte pass� en param�tre
     */
    Decompte findDecomptePrecedent(Decompte decompte);

    Montant getPlafond(Assurance assurance, Date date);

    public void imprimerDecompteVideEtBVR(Decompte decompte, String email) throws Exception;

    /**
     * Recherche des diff�rents taux pour les types de cotisations AVS, AC et
     * AC2 de l'employeur sous la forme d'un objet {@link CotisationsInfo}.
     * 
     * @return {@link CotisationsInfo}
     */
    CotisationsInfo retrieveCotisationsInfo(String idEmployeur, Date date);

    /**
     * Recherche des �l�ments relatives aux d�comptes, notamment l'adresse de l'employeur ainsi que ses cotisations.
     * 
     * @param decompte
     *            d�compte � compl�ter
     * @param date
     *            Date � laquelle l'activit� doit �tre d�termin�e
     * @return Decompte complet
     */
    Decompte retrieveDecompteInfos(Decompte decompte);

    /**
     * Retourne le prochain num�ro de d�compte disponible pour cet employeur, ce type de d�compte, cette date et cette
     * p�riodicit�.
     * 
     * @param employeur Employeur pour lequel cr�� le num�ro de d�compte
     * @param typeDecompte Type de d�compte
     * @param date Date � laquelle sera g�n�r� le num�ro
     * @param periodicite Periodicit� de l'employeur
     * @return Premier num�ro de d�compte disponible
     */
    NumeroDecompte getNumeroDecompte(Employeur employeur, TypeDecompte typeDecompte, Date date, String periodicite);

    CotisationDecompte createCotisationDecompte(AdhesionCotisationPosteTravail adhesionCotisation, Taux taux,
            String nextId);

    /**
     * Retourne les cotisations actives pour le d�compte pass� en param�tres.
     * 
     * @param decompte D�compteSalaire pour lequel les cotisations doivent �tre d�duites. Certaines cotisations ne sont
     *            pas
     *            actives pour les d�comptes de type CP et on v�rifie que le travailleur soit bien en �ge de cotiser.
     * @return Liste des adhesions actives
     */
    List<AdhesionCotisationPosteTravail> findAdhesionsForDecompteSalaire(DecompteSalaire decompteSalaire);

    List<AdhesionCotisationPosteTravail> findAdhesionsForDecompteSalaireCP(DecompteSalaire decompteSalaire);

    void annulerDecompteForParticularite(String idAffilie, String dateDebut, String dateFin);

    /**
     * Retourne le texte de la personne de r�f�rence pour le d�compte pass� en param�tre.
     * 
     * @param decompte D�compte pour lequel rechercher la personne de r�f�rence
     * @return Texte du rectificatif
     */
    String findTextePersonneReference(Decompte decompte);

    /**
     * Ajoute une entr�e dans la table de synchronisation avec EBU afin de pouvoir le notifier � ServiceNow
     * 
     * @param idDecompte id du d�compte
     * @throws JadePersistenceException
     */
    public void notifierSynchronisationEbu(String idDecompte, String idEmployeur, TypeDecompte type)
            throws JadePersistenceException;

    public void actualiserTauxCotisations(DecompteSalaire decompteSalaire, Annee annee);

    /**
     * G�n�ration des cotisations pour un d�compte selon le type de salaire.
     * <ul>
     * <li>Si le d�compte est de type CPP : On prend les cotisations de l'affili�
     * <li>Si le d�compte est d'un autre type : On prend les cotisations du poste de travail
     * 
     * @param decompteSalaire D�compte salaire pour lequel g�n�rer les cotisations
     * @return Liste de cotisations
     */
    List<CotisationDecompte> genererTableCotisation(DecompteSalaire decompteSalaire,
            TauxCotisationDecompteLoader tauxCotisationDecompteLoader);

    /**
     * Retourne true si l'affili� li� au d�compte poss�de au moins un dossier avec une prestation AF entre la date de
     * d�but du d�compte et la date de fin du d�compte.
     * Dans le cas o� l'affili� ne poss�de pas de dossiers AF, on retourne �galement true.
     * 
     * @param decompte D�compte pour lequel rechercher les prestations AF
     * @return true si poss�de des prestations ou que l'affili� ne poss�de pas de dossier
     */
    boolean hasPrestationsAFOuPasDossier(Decompte decompte);

    public void actualiserDecompteSelonPoste(PosteTravail posteTravailCreated) throws JadePersistenceException,
            UnsatisfiedSpecificationException;

    public void actualiserDecompteForExternalServices(String idEmployeur, String dateATraiter)
            throws JadePersistenceException, UnsatisfiedSpecificationException;

}
