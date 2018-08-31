package ch.globaz.vulpecula.domain.models.postetravail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.models.registre.TypeQualification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailAdhesionChevauchantesSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailAdhesionCotisationDateValideSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailEmployeurRequisSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailOccupationsSansDoublonsSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailPeriodeActiviteRequisSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailPeriodeActiviteValide;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailQualificationRequisSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailTauxOccupationInPeriodeActivite;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailTravailleurRequisSpecification;
import ch.globaz.vulpecula.domain.specifications.postetravail.PosteTravailTypeSalaireRequisSpecification;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.CotisationChecker;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/***
 * Le poste de travail correspond � la relation entre un travailleur et un
 * employeur.
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 17 d�c. 2013
 */
public class PosteTravail implements DomainEntity {
    public static final int CAISSE_METIER_INVALIDE = 0;

    private String idPosteTravail;
    private Employeur employeur;
    private Boolean franchiseAVS;
    private List<Occupation> occupations;
    private Periode periodeActivite;
    private Qualification qualification;
    private Date dateValiditeQualification;
    private Travailleur travailleur;
    private TypeSalaire typeSalaire;
    private Date dateValiditeTypeSalaire;
    private String spy;
    private String idTiersCM;
    private List<AdhesionCotisationPosteTravail> adhesionsCotisations;
    private List<ConventionQualification> parametresQualifications;
    private String idPortail;
    private String correlationId = "";
    private String posteCorrelationId;

    public PosteTravail() {
        this(null);
    }

    public PosteTravail(String id) {
        idPosteTravail = id;
        franchiseAVS = false;
        occupations = new ArrayList<Occupation>();
        adhesionsCotisations = new ArrayList<AdhesionCotisationPosteTravail>();
    }

    public String getIdPortail() {
        return idPortail;
    }

    public void setIdPortail(String idPortail) {
        this.idPortail = idPortail;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public List<ConventionQualification> getParametresQualifications() {
        return parametresQualifications;
    }

    public String getIdTiersCM() {
        return idTiersCM;
    }

    public void setIdTiersCM(String idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

    @Override
    public String getId() {
        return idPosteTravail;
    }

    @Override
    public void setId(final String id) {
        idPosteTravail = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(final String spy) {
        this.spy = spy;
    }

    public String getIdEmployeur() {
        if (employeur != null) {
            return employeur.getId();
        }
        return null;
    }

    public String getIdTiersEmployeur() {
        if (employeur != null) {
            return employeur.getIdTiers();
        }
        return null;
    }

    public String getIdTravailleur() {
        if (travailleur != null) {
            return travailleur.getId();
        }
        return null;
    }

    public String getAffilieNumero() {
        if (employeur != null) {
            return employeur.getAffilieNumero();
        }
        return null;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    /**
     * Retourne la raison sociale de l'employeur auquel le poste de travail est attach�.
     * 
     * @return String repr�sentant la raison sociale du poste de travail ou null si inexistant
     */
    public String getRaisonSocialeEmployeur() {
        if (employeur == null) {
            return null;
        }
        return employeur.getRaisonSociale();
    }

    public void setEmployeur(final Employeur employeur) {
        this.employeur = employeur;
    }

    public boolean isFranchiseAVS() {
        return franchiseAVS;
    }

    public void setFranchiseAVS(final Boolean franchiseAVS) {
        this.franchiseAVS = franchiseAVS;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public void setOccupations(final List<Occupation> occupations) {
        this.occupations = occupations;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(final Qualification qualification) {
        this.qualification = qualification;
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(final Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public TypeSalaire getTypeSalaire() {
        return typeSalaire;
    }

    public Date getDateValiditeQualification() {
        return dateValiditeQualification;
    }

    public void setDateValiditeQualification(Date dateValiditeQualification) {
        this.dateValiditeQualification = dateValiditeQualification;
    }

    public Date getDateValiditeTypeSalaire() {
        return dateValiditeTypeSalaire;
    }

    public void setDateValiditeTypeSalaire(Date dateValiditeTypeSalaire) {
        this.dateValiditeTypeSalaire = dateValiditeTypeSalaire;
    }

    /**
     * Retourne le code syst�me correspondant au type de salaire.
     * Si le type de salaire est null, alors null est retourn�e.
     * 
     * @return Le code syst�me repr�sentant le type de salaire, ou null si null.
     */
    public String getTypeSalaireAsValue() {
        if (typeSalaire == null) {
            return null;
        }
        return typeSalaire.getValue();
    }

    public void setTypeSalaire(final TypeSalaire type) {
        typeSalaire = type;
    }

    public Periode getPeriodeActivite() {
        return periodeActivite;
    }

    /**
     * Retourne la date de d�but d'activit� au format dd.mm.YYYY
     * 
     * @return String repr�sentnant la date de d�but d'activit� ou null si elle
     *         n'existe pas.
     */
    public String getDebutActiviteAsSwissValue() {
        Date date = getDebutActivite();
        if (date != null) {
            return date.getSwissValue();
        }
        return null;
    }

    /**
     * Retourne la date de fin d'activit� au format dd.mm.YYY
     * 
     * @return String repr�sentant la date de fin d'activit� ou null si elle
     *         n'exsite pas
     */
    public String getFinActiviteAsSwissValue() {
        Date date = getFinActivite();
        if (date != null) {
            return date.getSwissValue();
        }
        return null;
    }

    public String getDateValiditeQualificationAsSwissValue() {
        Date date = getDateValiditeQualification();
        if (date != null) {
            return date.getSwissValue();
        }
        return null;
    }

    public String getDateValiditeTypeSalaireAsSwissValue() {
        Date date = getDateValiditeTypeSalaire();
        if (date != null) {
            return date.getSwissValue();
        }
        return null;
    }

    /**
     * Retourne le code syst�me repr�sentant la qualification
     * 
     * @return String repr�sentant un code syst�me ou null si inexistante
     */
    public String getQualificationAsValue() {
        if (qualification != null) {
            return qualification.getValue();
        }
        return null;
    }

    public Date getDebutActivite() {
        if (periodeActivite != null) {
            return periodeActivite.getDateDebut();
        }
        return null;
    }

    public Date getFinActivite() {
        if (periodeActivite != null) {
            return periodeActivite.getDateFin();
        }
        return null;
    }

    public void setPeriodeActivite(final Periode periodeActivite) {
        this.periodeActivite = periodeActivite;
    }

    public boolean isActif() {
        return isActif(new Date(new java.util.Date()));
    }

    /**
     * Contr�le si le poste est actif dans la p�riode demand� en poss�dant soit
     * - pas de date de fin mais une date de d�but d'activ� avant la date de fin de la p�riode demand�
     * ou
     * - une p�riode d'activit� (avec une date de fin diff�rente de NULL) qui chevauche la p�riode demand�
     * 
     * @return true si le poste de travail est actif
     * @param periodeDemande Periode dans laquel le poste doit avoir une activit�
     */
    public boolean isActif(final Periode periodeDemande) {
        return periodeDemande.isActif(periodeActivite);
    }

    /**
     * Contr�le si le poste est actuellement actif en ne poss�dant soit pas de
     * date de fin d'activit� ou que la date de fin d'activit� est plus grande
     * que la date actuelle.
     * 
     * @return true si le poste de travail est actif
     * @param dateActuel
     *            java.util.Date � partir de laquelle l'activit� est calcul�
     */
    public boolean isActif(final Date dateActuel) {
        if (periodeActivite == null) {
            // FIXME: Par convention, une poste qui ne contient pas de p�riode
            // est consid�r� comme actif. Peut-�tre � l'avenir faudra-t-il
            // revoir cet axiome.
            return true;
        }
        return periodeActivite.contains(dateActuel);
    }

    /**
     * Retourne si l'employeur est soumis � l'AVS.
     * 
     * @return true si soumis � l'AVS, false si l'employeur est null ou non soumis
     */
    public boolean isEmployeurSoumisAVS() {
        if (employeur == null) {
            throw new IllegalStateException(
                    "Le poste de travail est dans un �tat inconstant car il ne poss�de pas d'employeur");
        }
        return employeur.isSoumisAVS();
    }

    /**
     * Retourne si l'employeur est soumis � l'AC.
     * 
     * @return true si soumis � l'AC, false si l'employeur est null ou non soumis
     */
    public boolean isEmployeurSoumisAC() {
        if (employeur == null) {
            return false;
        }
        return employeur.isSoumisAC();
    }

    /**
     * Retourne si l'employeur est soumis � l'AC2.
     * 
     * @return true si soumis � l'AC2, false si l'employeur est null ou non soumis
     */
    public boolean isEmployeurSoumisAC2() {
        if (employeur == null) {
            return false;
        }
        return employeur.isSoumisAC2();
    }

    /**
     * Retourne la liste des adh�sions aux plan de caisses, soit une agr�gation
     * des groupes de cotisation.
     * 
     * @return
     */
    public List<PlanCaisse> getAdhesionsCaisses() {
        Set<PlanCaisse> planCaisses = new HashSet<PlanCaisse>();

        for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : getAdhesionsCotisations()) {
            planCaisses.add(adhesionCotisationPosteTravail.getPlanCaisse());
        }

        return new ArrayList<PlanCaisse>(planCaisses);
    }

    /**
     * Retourne l'occupation du poste de travail par rapport � la date actuelle.
     * (courante)
     * 
     * @return Occupation repr�sentant le taux auquel un travailleur est affect�
     *         au poste
     */
    public Occupation getOccupationActuel() {
        Date date = new Date(new java.util.Date());
        return getOccupation(date);
    }

    public Occupation getLatestOccupation() {
        Occupation lastOccupation = null;
        for (Occupation occupation : occupations) {

            if (lastOccupation == null
                    || occupation.getDateValidite().getTime() > lastOccupation.getDateValidite().getTime()) {
                lastOccupation = occupation;
            }

        }

        return lastOccupation;
    }

    /**
     * Retourne l'occupation du poste de travail par rapport � la date sp�cifi�e.
     * 
     * @param date
     *            � laquelle l'occupation sera d�duite
     * @return Occupation du poste de travail par rapport � une date
     */
    public Occupation getOccupation(final Date date) {
        Occupation current = null;
        for (Occupation occupation : occupations) {
            if (occupation.getDateValidite().getTime() <= date.getTime()) {
                if (current == null || occupation.getDateValidite().getTime() > current.getDateValidite().getTime()) {
                    current = occupation;
                }
            }
        }

        // Si pas de poste, on force l'occupation � 100%
        if (current == null && (occupations.size() > 1 || occupations.isEmpty())) {
            current = Occupation.valueOf100At(date);
        } else if (occupations.size() == 1) {
            return occupations.get(0);
        }
        return current;
    }

    /**
     * Retourne le taux d'occupation du poste de travail par rapport � la date sp�cifi�e.
     * 
     * @param date � laquelle l'occupation sera d�duite
     */
    public Taux getTauxOccupation(final Date date) {
        return getOccupation(date).getTaux();
    }

    /**
     * Retourne la liste des cotisations rattach�es au poste de travail.
     * 
     * @return {@link List} d'{@link AdhesionCotisationPosteTravail}
     */
    public List<AdhesionCotisationPosteTravail> getAdhesionsCotisations() {
        return adhesionsCotisations;
    }

    /**
     * Retourne la liste des cotisations rattach�es au poste de travail hormis l'AC2.
     * 
     * @return Liste de adh�sions
     */
    public List<AdhesionCotisationPosteTravail> getAdhesionsCotisationsSansAC2() {
        List<AdhesionCotisationPosteTravail> adhesionsSansAC2 = new ArrayList<AdhesionCotisationPosteTravail>();
        for (AdhesionCotisationPosteTravail adhesion : adhesionsCotisations) {
            if (!adhesion.isAssuranceAC2()) {
                adhesionsSansAC2.add(adhesion);
            }
        }
        return adhesionsSansAC2;
    }

    /**
     * Mise � jour de la liste des cotisations.
     * 
     * @param adhesionsCaisses
     *            Nouvelle liste d'adh�sions aux cotisations
     */
    public void setAdhesionsCotisations(final List<AdhesionCotisationPosteTravail> adhesionsCotisations) {
        this.adhesionsCotisations = adhesionsCotisations;
    }

    /**
     * Retourne la description du travailleur.
     */
    public String getDescriptionTravailleur() {
        if (travailleur != null) {
            return travailleur.getDescription();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Travailleur :").append(getTravailleur().getDesignation1()).append(" ")
                .append(getTravailleur().getDesignation2()).append("\n").append("Periode activit� : ")
                .append(getDebutActivite()).append(" ").append(getFinActivite()).append("\n").append("Employeur : ")
                .append(getEmployeur().getId()).append(" ").append(getEmployeur().getDesignation1()).append(" ")
                .append(getEmployeur().getDesignation2());
        return sb.toString();
    }

    /**
     * @see #validate
     * @return true si l'objet r�pond aux crit�res, false si les crit�res ne
     *         sont pas remplies
     */
    public boolean isValid() {
        try {
            return validate();
        } catch (UnsatisfiedSpecificationException e) {
            return false;
        }
    }

    /**
     * V�rifie que l'objet m�tier est valide, soit :
     * <ul>
     * <li>Que le poste est li� � un employeur
     * <li>Que le poste est li� � un travailleur
     * <li>Que le poste dispose d'une p�riode d'activit�
     * <li>Que le poste dispose d'une qualification
     * <li>Que le poste poss�de un genre/type de salaire
     * <li>Que la liste des taux d'occupations soit vide ou remplie avec des occupations � des dates diff�rentes.
     * <li>Que les adh�sions aux cotisations disposent toutes d'une p�riode
     * <li>Que les m�mes adh�sions ne se chevauchenet pas
     * </ul>
     * 
     * @throws UnsatisfiedSpecificationException
     *             Dans le cas o� une des sp�cifications n'est pas respect�e,
     *             dans l'ordre d�crit ci-dessus
     */
    public boolean validate() throws UnsatisfiedSpecificationException {
        final PosteTravailEmployeurRequisSpecification employeurRequisSpecification = new PosteTravailEmployeurRequisSpecification();
        final PosteTravailTravailleurRequisSpecification travailleurRequisSpecification = new PosteTravailTravailleurRequisSpecification();
        final PosteTravailPeriodeActiviteRequisSpecification periodeActiviteRequisSpecification = new PosteTravailPeriodeActiviteRequisSpecification();
        final PosteTravailQualificationRequisSpecification qualificationRequisSpecification = new PosteTravailQualificationRequisSpecification();
        final PosteTravailTypeSalaireRequisSpecification typeSalaireRequisSpecification = new PosteTravailTypeSalaireRequisSpecification();
        final PosteTravailOccupationsSansDoublonsSpecification occupationsSansDoublonsSpecification = new PosteTravailOccupationsSansDoublonsSpecification();
        final PosteTravailAdhesionCotisationDateValideSpecification adhesionCotisationDateRequiseSpecification = new PosteTravailAdhesionCotisationDateValideSpecification();
        final PosteTravailAdhesionChevauchantesSpecification adhesionChevauchantesSpecification = new PosteTravailAdhesionChevauchantesSpecification();

        final PosteTravailPeriodeActiviteValide periodeActiviteValide = new PosteTravailPeriodeActiviteValide();
        final PosteTravailTauxOccupationInPeriodeActivite posteTravailTauxOccupationInPeriodeActivite = new PosteTravailTauxOccupationInPeriodeActivite();

        final Specification<PosteTravail> posteTravailSpecification = employeurRequisSpecification
                .and(travailleurRequisSpecification).and(periodeActiviteRequisSpecification)
                .and(qualificationRequisSpecification).and(typeSalaireRequisSpecification)
                .and(occupationsSansDoublonsSpecification).and(adhesionCotisationDateRequiseSpecification)
                .and(periodeActiviteValide).and(posteTravailTauxOccupationInPeriodeActivite)
                .and(adhesionChevauchantesSpecification);

        return posteTravailSpecification.isSatisfiedBy(this);
    }

    /**
     * Retourne le taux contribuable du poste de travail, soit l'aggr�gaton du taux de toutes les cotisations except�
     * celles les cotisations AVS, AC, ACII.
     * 
     * @return double repr�sentant le taux contribuable total pour les caisses sociales.
     */
    public Taux getTauxContribuable() {
        Taux tauxContribuable = Taux.ZERO();
        for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : adhesionsCotisations) {
            TypeAssurance typeAssurance = adhesionCotisationPosteTravail.getTypeAssurance();
            if (!Assurance.NOT_CAISSES_SOCIALES.contains(typeAssurance)) {
                tauxContribuable = tauxContribuable.addTaux(adhesionCotisationPosteTravail.getCotisation().getTaux());
            }
        }
        return tauxContribuable;
    }

    public void addAdhesionCotisation(final AdhesionCotisationPosteTravail adhesionCotisationPosteTravail) {
        adhesionsCotisations.add(adhesionCotisationPosteTravail);
    }

    /**
     * Ajout d'une occupation au poste de travail.
     * 
     * @param occupation Occupation � rajouter
     */
    public void addTauxOccupation(Occupation occupation) {
        occupations.add(occupation);
    }

    public Convention getConvention() {
        Employeur employeur1 = getEmployeur();
        if (employeur1 == null) {
            return null;
        }

        return employeur1.getConvention();
    }

    public String getIdConvention() {
        Convention convention = getConvention();
        if (convention == null) {
            return null;
        }
        return convention.getId();
    }

    public String getDesignationConvention() {
        Convention convention = getConvention();
        if (convention == null) {
            return null;
        }
        return convention.getDesignation();
    }

    /**
     * Retourne le nom et pr�nom du travailleur.
     * 
     * @return String repr�sentant le nom et le pr�nom du travailleur, ou null si inexistants
     */
    public String getNomPrenomTravailleur() {
        if (travailleur == null) {
            return null;
        }
        return travailleur.getDesignation1() + " " + travailleur.getDesignation2();
    }

    /**
     * Retourne la date de naissacne du travailleur.
     * 
     * @return String repr�sentant la date de naissance du tavailleur, ou null si inexistante.
     */
    public String getDateNaissanceTravailleur() {
        return travailleur.getDateNaissance();
    }

    /**
     * Retourne le no de la convention ou null si employeur null.
     * 
     * @return String repr�sentant le no� de la convention
     */
    public String getConventionNo() {
        if (employeur == null) {
            return null;
        }
        return employeur.getNo();
    }

    private TypeAssurance[] getTypesAssurancesArray() {
        List<TypeAssurance> typeAssurance = new ArrayList<TypeAssurance>();
        for (AdhesionCotisationPosteTravail cotisation : adhesionsCotisations) {
            typeAssurance.add(cotisation.getTypeAssurance());
        }

        return typeAssurance.toArray(new TypeAssurance[typeAssurance.size()]);
    }

    private TypeAssurance[] getTypesAssurancesArray(Date date) {
        List<TypeAssurance> typeAssurance = new ArrayList<TypeAssurance>();
        for (AdhesionCotisationPosteTravail cotisation : adhesionsCotisations) {
            if (cotisation.getPeriode().contains(date)) {
                typeAssurance.add(cotisation.getTypeAssurance());
            }
        }

        return typeAssurance.toArray(new TypeAssurance[typeAssurance.size()]);
    }

    /**
     * M�thode qui retourne si le poste de travail a eu une fois dans son existant le droit � des absences justifi�es.
     * 
     * @return boolean A droit aux AJ
     */
    public boolean hasDroitAJ(int idCaisseMetier) {
        return TableParametrage.getInstance().hasDroitAJ(idCaisseMetier, getTypesAssurancesArray());
    }

    /**
     * Retourne si le poste de travail a le droit au AJ � la date pass�e en param�tre.
     * 
     * @param idCaisseMetier Id de la caisse m�tier
     * @param date Date � laquelle d�terminer si le poste � le droit aux AJ
     * @return true si droit au AJ
     */
    public boolean hasDroitAJ(int idCaisseMetier, Date date) {
        return TableParametrage.getInstance(date.getAnnee()).hasDroitAJ(idCaisseMetier, getTypesAssurancesArray(date));
    }

    /**
     * M�thode qui retourne si le poste de travail � le droits aux prestations Cong�s Pay�s.
     * 
     * @return boolean A droit aux CP
     */
    public boolean hasDroitCP(int idCaisseMetier) {
        return TableParametrage.getInstance().hasDroitCP(idCaisseMetier, getTypesAssurancesArray());
    }

    /**
     * Retourne si le poste de travail a le droit au CP � la date pass�e en param�tre.
     * 
     * @param idCaisseMetier Id de la caisse m�tier
     * @param date Date � laquelle d�terminer si le poste � le droit aux AJ
     * @return true si droit au AJ
     */
    public boolean hasDroitCP(int idCaisseMetier, Date date) {
        return TableParametrage.getInstance(date.getAnnee()).hasDroitCP(idCaisseMetier, getTypesAssurancesArray(date));
    }

    /**
     * M�thode qui v�rifie si le poste de travail � droit aux prestations Service Militaire.
     * 
     * @return boolean A droit aux prestations SM
     */
    public boolean hasDroitSM(int idCaisseMetier) {
        return TableParametrage.getInstance().hasDroitSM(idCaisseMetier, getTypesAssurancesArray());
    }

    /**
     * Retourne si le poste de travail a le droit au SM � la date pass�e en param�tre.
     * 
     * @param idCaisseMetier Id de la caisse m�tier
     * @param date Date � laquelle d�terminer si le poste � le droit aux AJ
     * @return true si droit au AJ
     */
    public boolean hasDroitSM(int idCaisseMetier, Date date) {
        return TableParametrage.getInstance(date.getAnnee()).hasDroitSM(idCaisseMetier, getTypesAssurancesArray(date));
    }

    /**
     * @return l'age du travailleur
     */
    public int getAgeTravailleur() {
        if (getTravailleur() != null) {
            return nbAnneesDepuis(getTravailleur().getDateNaissance());
        } else {
            throw new NullPointerException("travailleur is NULL for PosteTravail : " + idPosteTravail);
        }
    }

    /**
     * Retourne l'�ge du travailleur.
     * 
     * @param dateDeCalcul Date � partir de laquelle l'�ge est d�duite
     * @return age du travailleur
     */
    public int getAgeTravailleur(Date dateDeCalcul) {
        return travailleur.getAge(dateDeCalcul);
    }

    public int getAgeTravailleurForDate(String dateDeCalcul) {
        return travailleur.getAge(new Date(dateDeCalcul));
    }

    public int getAgeTravailleurForDate(Date dateDeCalcul) {
        return travailleur.getAge(dateDeCalcul);
    }

    /**
     * retourne le nombre d'ann�e de services ininterrompues pour le poste de travail
     * 
     * @return le nombre d'ann�e de service ininterrompues
     */
    public int getAnneesService() {
        int anneeDebut = Integer.parseInt(getDebutActivite().getAnnee());
        int anneeFin = Date.getCurrentYear();
        if (getFinActivite() != null) {
            anneeFin = Integer.parseInt(getFinActivite().getAnnee());
        }

        return anneeFin - anneeDebut;
    }

    /**
     * Retourne l'id tiers de l'employeur
     * 
     * @return String retournant l'id du tiers ou null si employeur inexistant
     */
    public String getEmployeurIdTiers() {
        if (employeur == null) {
            return null;
        }
        return employeur.getIdTiers();
    }

    /**
     * Retourne l'id tiers du travailleur
     * 
     * @return String retournant l'id du tiers ou null si travailleur inexistant
     */
    public String getTravailleurIdTiers() {
        if (travailleur == null) {
            return null;
        }
        return travailleur.getIdTiers();
    }

    /**
     * Retourne le NSS du travailleur
     * 
     * @return String retournant le NSS ou null si travailleur inexistant
     */
    public String getTravailleurNss() {
        if (travailleur == null) {
            return null;
        }
        return travailleur.getNumAvsActuel();
    }

    /**
     * Retourne si la qualification est de type employ�.
     * 
     * @return true si type employ�, false dans le cas contraire
     * @throws IllegalStateException Si la qualification/conventionQualification n'est pas charg�e
     */
    public boolean isQualificationEmploye() {
        throwExceptionIfQualificationInvalid();
        return isTypeQualification(TypeQualification.EMPLOYE);
    }

    /**
     * Retourne si la qualification est de type ouvrier.
     * 
     * @return true si type ouvrier, false dans le cas contraire
     * @throws IllegalStateException Si la qualification/conventionQualifiaction n'est pas charg�e
     */
    public boolean isQualificationOuvrier() {
        throwExceptionIfQualificationInvalid();
        return isTypeQualification(TypeQualification.OUVRIER);
    }

    private boolean isTypeQualification(TypeQualification typeQualification) {
        for (ConventionQualification conventionQualification : parametresQualifications) {
            if (conventionQualification.getTypeQualification().equals(typeQualification)) {
                return true;
            }
        }
        return false;
    }

    private void throwExceptionIfQualificationInvalid() {
        if (qualification == null) {
            throw new IllegalStateException("La qualification est null");
        }
        if (parametresQualifications == null || parametresQualifications.size() == 0) {
            throw new IllegalStateException("La qualification " + qualification.toString() + " pour la convention "
                    + getIdConvention() + " est non configur�e/charg�s avec un type de qualification");
        }
        if (parametresQualifications.size() > 1) {
            throw new IllegalStateException("La qualification " + qualification.toString() + " pour la convention "
                    + getIdConvention() + " dispose de multiples configurations, v�rifier votre param�trage");
        }
    }

    /**
     * Retourne le nombre d'ann�es �coul�es depuis la date pass�e en param�tre.
     * 
     * @param date une date au format JJ.MM.AAAA
     * @return le nombre d'ann�es �coul�es
     */
    private int nbAnneesDepuis(String date) {
        java.util.Date dbDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try {
            dbDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }

        long MILLI_SECONDS_PER_YEAR = 31558464000L;
        long milliSecondsSinceDate = System.currentTimeMillis() - dbDate.getTime();

        return (int) (milliSecondsSinceDate / MILLI_SECONDS_PER_YEAR);
    }

    public boolean isMensuel() {
        return TypeSalaire.MOIS.equals(typeSalaire);
    }

    public boolean isHoraire() {
        return TypeSalaire.HEURES.equals(typeSalaire);
    }

    public boolean isConstant() {
        return TypeSalaire.CONSTANT.equals(typeSalaire);
    }

    public boolean hasMoreThan18Ans(Date dateReference) {
        return travailleur.hasMoreThan18Ans(dateReference);
    }

    public boolean hasMoreThanOrEquals18Ans(Date dateReference) {
        return travailleur.hasMoreThanOrEquals18Ans(dateReference);
    }

    /**
     * Retourne si le poste de travail est soumis � l'AVS.
     * 
     * @param date Date de r�f�rence
     * @return true si soumis � l'AVS
     */
    public boolean cotiseAVS(Date date) {
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        for (AdhesionCotisationPosteTravail adhesionCotisation : adhesionsCotisations) {
            cotisations.add(adhesionCotisation.getCotisation());
        }
        return CotisationChecker.isSoumisAVS(cotisations, new Date(31, 12, Integer.valueOf(date.getAnnee())));
    }

    /**
     * D�finit si l'entit� a �t� charg�e.
     * 
     * @return true si doit �tre charg�
     */
    public boolean mustBeFetched() {
        return idPosteTravail != null && spy == null;
    }

    public boolean isActifIn(Annee annee) {
        return getPeriodeActivite().isActifIn(annee);
    }

    public boolean isActifInAnneeOrPrecedente(Annee annee) {
        return (getPeriodeActivite().isActifIn(annee) || getPeriodeActivite().isActifIn(annee.previous()));
    }

    public void setParametresQualifications(List<ConventionQualification> parametresQualifications) {
        this.parametresQualifications = parametresQualifications;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPosteTravail == null) ? 0 : idPosteTravail.hashCode());
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
        PosteTravail other = (PosteTravail) obj;
        if (idPosteTravail == null || other.idPosteTravail == null) {
            return false;
        }

        if (idPosteTravail.equals(other.idPosteTravail)) {
            return true;
        }
        return false;
    }

    public String getIdLocaliteEmployeur() {
        return employeur.getAdressePrincipale().getIdLocalite();
    }

    public boolean isEnAgeAvs(Date date) {
        return travailleur.isEnAgeAvs(date);
    }

    public boolean isElectricite() {
        return employeur.isElectricite();
    }

    public boolean hasCotisationAVS() {
        return hasCotisation(TypeAssurance.COTISATION_AVS_AI);
    }

    public boolean hasCotisationFA() {
        return hasCotisation(TypeAssurance.FRAIS_ADMINISTRATION);
    }

    public boolean hasAssuranceMaladie() {
        return hasCotisation(TypeAssurance.ASSURANCE_MALADIE);
    }

    private boolean hasCotisation(TypeAssurance assurance) {
        for (AdhesionCotisationPosteTravail adhesion : adhesionsCotisations) {
            if (adhesion.getTypeAssurance().equals(assurance)) {
                return true;
            }
        }
        return false;
    }

    private List<AdhesionCotisationPosteTravail> getAssurancesMaladies() {
        List<AdhesionCotisationPosteTravail> adhesions = new ArrayList<AdhesionCotisationPosteTravail>();
        for (AdhesionCotisationPosteTravail adhesion : adhesionsCotisations) {
            if (TypeAssurance.ASSURANCE_MALADIE.equals(adhesion.getTypeAssurance())) {
                adhesions.add(adhesion);
            }
        }
        return adhesions;
    }

    /**
     * Retourne si le poste de travail poss�de des assurances maladies qui sont toutes cl�tur�es (disposent d'une date
     * de fin).
     * Dans le cas on le poste ne poss�de pas de cotisation de type Assurance Maladie, la valeur false est retourn�e.
     * 
     * @return true si poss�de au moins une assurance maladie et que toutes les caisses sont cl�tur�es
     */
    public boolean hasAssurancesMaladiesToutesCloturees() {
        return hasAssuranceMaladie() && hasAllAssurancesMaladiesCloturees();
    }

    private AdhesionCotisationPosteTravail getLastDateDebut(List<AdhesionCotisationPosteTravail> adhesions) {
        Date dateDebut = adhesions.get(0).getPeriode().getDateDebut();
        AdhesionCotisationPosteTravail adhesionToReturn = adhesions.get(0);
        for (AdhesionCotisationPosteTravail adhesion : adhesions) {
            if (adhesion.getPeriode().getDateDebut().after(dateDebut)) {
                dateDebut = adhesion.getPeriode().getDateDebut();
                adhesionToReturn = adhesion;
            }
        }
        return adhesionToReturn;
    }

    /**
     * Retourne la derni�re assurance maladie active.
     * 
     * @return Assurance maladie
     */
    public AdhesionCotisationPosteTravail getLastAssuranceMaladie() {
        List<AdhesionCotisationPosteTravail> assurancesMaladies = getAssurancesMaladies();
        if (assurancesMaladies.isEmpty()) {
            return null;
        }
        // Collections.sort(assurancesMaladies);
        return getLastDateDebut(assurancesMaladies);
    }

    /**
     * Retourne si les assurances maladies sont toutes cl�tur�es.
     * 
     * @return true si toutes les cotisations assurance maladie
     */
    private boolean hasAllAssurancesMaladiesCloturees() {
        return Iterables.all(getAssurancesMaladies(), new Predicate<AdhesionCotisationPosteTravail>() {
            @Override
            public boolean apply(AdhesionCotisationPosteTravail adhesion) {
                return adhesion.getPeriode().getDateFin() != null;
            }
        });
    }

    public Periode getPeriodeLastAssuranceMaladie() {
        List<AdhesionCotisationPosteTravail> adhesions = getAssurancesMaladies();
        if (adhesions.isEmpty()) {
            return null;
        }
        return getLastAssuranceMaladie().getPeriode();
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrealtionId) {
        posteCorrelationId = posteCorrealtionId;
    }

}
