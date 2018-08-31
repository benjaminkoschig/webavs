package ch.globaz.vulpecula.domain.models.postetravail;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.external.models.pyxis.PersonneEtendue;

/***
 * Le travailleur correspond � un Tiers (Pyxis).
 * 
 */
public class Travailleur extends PersonneEtendue {
    private String correlationId;
    private String idTravailleur;
    private Boolean annonceMeroba;
    private Date dateAnnonceMeroba;
    private PermisTravail permisTravail;
    private List<PosteTravail> postesTravail;
    private List<DecompteSalaire> salaires;
    private String referencePermis;

    public Travailleur() {
        postesTravail = new ArrayList<PosteTravail>();
        salaires = new ArrayList<DecompteSalaire>();
    }

    public Travailleur(final PersonneEtendue personneEtendue) {
        super(personneEtendue);
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String getId() {
        return idTravailleur;
    }

    @Override
    public void setId(final String id) {
        idTravailleur = id;
    }

    /**
     * Retourne un String repr�sentant le nom et le pr�nom du travailleur.
     * 
     * @return un String repr�sentant le nom et le pr�nom du travailleur
     */
    public String getNomPrenomTravailleur() {
        StringBuilder nomPrenom = new StringBuilder();
        if (getDesignation1() != null) {
            nomPrenom.append(getDesignation1());
            if (getDesignation2() != null) {
                nomPrenom.append(" ");
                nomPrenom.append(getDesignation2());
            }
        }
        return nomPrenom.toString();
    }

    /**
     * Retourne si le travailleur a d�j� �t� annonc� � Meroba.
     * 
     * @return true si d�j� annonc�
     */
    public Boolean getAnnonceMeroba() {
        return annonceMeroba;
    }

    /**
     * Retourne la date � laquelle l'annonce � Meroba a �t� effectu�e.
     * 
     * @return Date � laquelle le travailleur a �t� annonc� � Meroba
     */
    public Date getDateAnnonceMeroba() {
        return dateAnnonceMeroba;
    }

    /**
     * Retourne l'id du tiers.
     * 
     * @return id du tiers
     */
    @Override
    public String getIdTiers() {
        return super.getId();
    }

    /**
     * Retourne le permis de travail du travailleur.
     * 
     * @return {@link PermisTravail} repr�sentant le permis de travail
     */
    public PermisTravail getPermisTravail() {
        return permisTravail;
    }

    /**
     * Retourne une liste des postes de travail auxquels le travailleur est affect�.
     * 
     * @return List<PosteTravail> auquel le travailleur est affect�s
     */
    public List<PosteTravail> getPostesTravail() {
        return postesTravail;
    }

    /**
     * Retourne la liste des salaires du travailleur.
     * 
     * @return List<LigneDecompte>
     */
    public List<DecompteSalaire> getSalaires() {
        return salaires;
    }

    /**
     * Mise � jour de la liste des salaires du travailleur.
     * 
     * @param salaires
     *            Liste des nouveaux salaires du travailleur
     */
    public void setSalaires(final List<DecompteSalaire> salaires) {
        this.salaires = salaires;
    }

    /**
     * Retourne la r�f�rence du permis de travail.
     * 
     * @return String repr�sentant la r�f�rence du permis de travail
     */
    public String getReferencePermis() {
        return referencePermis;
    }

    /**
     * D�finition de si le travailleur a d�j� �t� annonc� � Meroba.
     * 
     * @param annonceMeroba
     */
    public void setAnnonceMeroba(final Boolean annonceMeroba) {
        this.annonceMeroba = annonceMeroba;
    }

    /**
     * Mise � jour de la date � laquelle l'annonce � Meroba a �t� effectu�e.
     * 
     * @param dateAnnonceMeroba
     *            Nouvelle date � laquelle le travailleur a �t� annonc� � Meroba
     */
    public void setDateAnnonceMeroba(final Date dateAnnonceMeroba) {
        this.dateAnnonceMeroba = dateAnnonceMeroba;
    }

    /**
     * Mise � jour de la date � laquelle l'annonce � Meroba a �t� effect�e.
     * 
     * @param dateAnnonceMeroba
     *            String au format yyyyMMdd ou dd.MM.yyyy l'annonce a �t�
     *            effectu�e
     * @throws GlobazBusinessException si la date n'est pas valide
     */
    public void setDateAnnonceMeroba(final String dateAnnonceMeroba) throws GlobazBusinessException {
        try {
            this.dateAnnonceMeroba = new Date(dateAnnonceMeroba);
        } catch (IllegalArgumentException e) {
            throw new GlobazBusinessException(ExceptionMessage.ERREUR_INCONNUE);
        }
    }

    /**
     * Mise � jour de l'id du tiers.
     * 
     * @param id
     *            nouvel id du tiers
     */
    public void setIdTiers(final String id) {
        super.setId(id);
    }

    /**
     * Mise � jour du permis de travail du travailleur.
     * 
     * @param permisTravail
     *            Etat repr�sentant le nouveau permis de travail
     */
    public void setPermisTravail(final PermisTravail permisTravail) {
        this.permisTravail = permisTravail;
    }

    /**
     * Mise � jour du permis de travail du travailleur.
     * 
     * @param permisTravail
     *            String sous forme de code syst�me repr�sentant le permis de
     *            travail du travailleur
     * @throws IllegalArgumentException si le String pass� en param�tre ne correspond pas � la valeur attendue
     */
    public void setPermisTravail(final String permisTravail) {
        this.permisTravail = PermisTravail.fromValue(permisTravail);
    }

    /**
     * Mise � jour des postes de travail.
     * 
     * @param postesDeTravail
     *            Nouvelle liste de postes de travail
     */
    public void setPostesTravail(final List<PosteTravail> postesTravail) {
        this.postesTravail = postesTravail;
    }

    /**
     * Mise � jour de la r�f�rence du permis de travail.
     * 
     * @param referencePermis
     *            String repr�sentant la nouvelle r�f�rence du permis de travail
     */
    public void setReferencePermis(final String referencePermis) {
        this.referencePermis = referencePermis;
    }

    /**
     * Retourne la description du travailleur, soit la concat�nation de son nom
     * et de son pr�nom.
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        if (getDesignation1() != null) {
            sb.append(getDesignation1());
        }
        if (getDesignation2() != null) {
            sb.append(" ").append(getDesignation2());
        }
        return sb.toString();
    }

    /**
     * Retourne les postes de travail atifs du travailleur.
     * 
     * @return Liste des postes de travail actifs
     * 
     */
    public List<PosteTravail> getPostesTravailActifs() {
        return getPostesTravailActifs(new Date());
    }

    /**
     * Retourne les postes de travail actifs du travailleur � une date
     * sp�cifique.
     * 
     * @param date
     *            Date � laquelle les postes de travails actifs doivent �tre
     *            d�termin�s
     * @return Liste de postes de travail
     */
    public List<PosteTravail> getPostesTravailActifs(final Date date) {
        List<PosteTravail> postesTravail = new ArrayList<PosteTravail>();

        for (PosteTravail posteTravail : getPostesTravail()) {
            if (posteTravail.isActif(date)) {
                postesTravail.add(posteTravail);
            }
        }

        return postesTravail;
    }

    /**
     * Ajout d'un poste de travail au travailleur.
     * 
     * @param posteTravail
     */
    public void addPosteTravail(final PosteTravail posteTravail) {
        postesTravail.add(posteTravail);
    }

    /**
     * Calcul l'�ge (en ann�e) du travailleur � une date sp�cifique.
     * 
     * @param dateReference la date de r�f�rence pour laquelle on d�sire savoir l'�ge du travailleur
     * @return int l'�ge du travailleur (en ann�e) � la date donn�e
     */
    public int getAge(Date dateReference) {
        int anneeReference = Integer.valueOf(dateReference.getAnnee());
        Date dateNaissance = new Date(getDateNaissance());
        int anneeNaissance = Integer.valueOf(dateNaissance.getAnnee());
        int age = anneeReference - anneeNaissance;

        Date dateReferencePourComparaison = new Date(dateReference.getJour() + "." + dateReference.getMois() + "."
                + "2000");
        Date dateNaissancePourComparaison = new Date(dateNaissance.getJour() + "." + dateNaissance.getMois() + "."
                + "2000");
        if (dateReferencePourComparaison.before(dateNaissancePourComparaison)) {
            age--;
        }
        return age;
    }

    public boolean hasMoreThan18Ans(Date dateReference) {
        return getAge(dateReference) > 18;
    }

    public int getAgeAVS(Date dateReference) {
        int anneeReference = Integer.valueOf(dateReference.getAnnee());
        Date date = new Date(getDateNaissance());
        Date dateNaissance = new Date("01.01." + date.getAnnee());
        int anneeNaissance = Integer.valueOf(dateNaissance.getAnnee());
        int age = anneeReference - anneeNaissance;

        Date dateReferencePourComparaison = new Date(dateReference.getJour() + "." + dateReference.getMois() + "."
                + "2000");
        Date dateNaissancePourComparaison = new Date(dateNaissance.getJour() + "." + dateNaissance.getMois() + "."
                + "2000");
        if (dateReferencePourComparaison.before(dateNaissancePourComparaison)) {
            age--;
        }
        return age;
    }

    public boolean isEnAgeAvs(Date date) {
        return getAgeAVS(date) >= 18;
    }

    public boolean hasMoreThanOrEquals18Ans(Date dateReference) {
        return getAge(dateReference) >= 18;
    }

    public int getAgeNonRevolu(Date date) {
        int anneeReference = Integer.valueOf(date.getAnnee());
        Date dateNaissance = new Date(getDateNaissance());
        int anneeNaissance = Integer.valueOf(dateNaissance.getAnnee());
        int age = anneeReference - anneeNaissance;
        return age;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annonceMeroba == null) ? 0 : annonceMeroba.hashCode());
        result = prime * result + ((dateAnnonceMeroba == null) ? 0 : dateAnnonceMeroba.hashCode());
        result = prime * result + ((idTravailleur == null) ? 0 : idTravailleur.hashCode());
        result = prime * result + ((permisTravail == null) ? 0 : permisTravail.hashCode());
        result = prime * result + ((postesTravail == null) ? 0 : postesTravail.hashCode());
        result = prime * result + ((referencePermis == null) ? 0 : referencePermis.hashCode());
        result = prime * result + ((salaires == null) ? 0 : salaires.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Travailleur other = (Travailleur) obj;
        if (annonceMeroba == null) {
            if (other.annonceMeroba != null) {
                return false;
            }
        } else if (!annonceMeroba.equals(other.annonceMeroba)) {
            return false;
        }
        if (dateAnnonceMeroba == null) {
            if (other.dateAnnonceMeroba != null) {
                return false;
            }
        } else if (!dateAnnonceMeroba.equals(other.dateAnnonceMeroba)) {
            return false;
        }
        if (idTravailleur == null) {
            if (other.idTravailleur != null) {
                return false;
            }
        } else if (!idTravailleur.equals(other.idTravailleur)) {
            return false;
        }
        if (permisTravail != other.permisTravail) {
            return false;
        }
        if (postesTravail == null) {
            if (other.postesTravail != null) {
                return false;
            }
        } else if (!postesTravail.equals(other.postesTravail)) {
            return false;
        }
        if (referencePermis == null) {
            if (other.referencePermis != null) {
                return false;
            }
        } else if (!referencePermis.equals(other.referencePermis)) {
            return false;
        }
        if (salaires == null) {
            if (other.salaires != null) {
                return false;
            }
        } else if (!salaires.equals(other.salaires)) {
            return false;
        }
        return true;
    }

    public boolean isRetraiter(Date dateReference) {
        if (getDateNaissance() != null && !JadeStringUtil.isEmpty(getDateNaissance())) {

            Date dateRetraite = VulpeculaServiceLocator.getTravailleurService().giveDateRentier(getDateNaissance(),
                    getSexe());

            if (dateRetraite.beforeOrEquals(dateReference)) {
                return true;
            }
        }
        return false;
    }

}
