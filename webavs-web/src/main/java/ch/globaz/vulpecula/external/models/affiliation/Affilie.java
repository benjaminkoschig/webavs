package ch.globaz.vulpecula.external.models.affiliation;

import java.util.ArrayList;
import java.util.List;

import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/**
 * Représentation métier d'un Affilie selon le module Naos
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public class Affilie extends Tiers {

    /** Périodicité annuelle */
    public final static String PERIODICITE_ANNUELLE = "802004";
    /** Périodicité mensuelle */
    public final static String PERIODICITE_MENSUELLE = "802001";
    /** Périodicité semestrielle */
    public final static String PERIODICITE_SEMESTRIELLE = "802003";
    /** Périodicité trimestrielle */
    public final static String PERIODICITE_TRIMESTRIELLE = "802002";

    protected String idAffilie;
    protected String affilieNumero;
    protected String brancheEconomique;
    protected String dateDebut;
    protected String dateFin;
    protected Convention convention;

    protected String motifFin;
    protected String periodicite;
    protected String personnaliteJuridique;
    protected String raisonSociale;
    protected String raisonSocialeCourt;
    protected Boolean releveParitaire;
    protected Boolean relevePersonnel;
    protected String typeAffiliation;
    protected String accesSecurite;
    protected String declarationSalaire;

    protected List<Cotisation> cotisations;
    protected List<Particularite> particularites;

    public Affilie() {
        convention = new Convention();
        cotisations = new ArrayList<Cotisation>();
        particularites = new ArrayList<Particularite>();
    }

    public Affilie(Tiers tiers) {
        super(tiers);
    }

    public Affilie(Affilie affilie) {
        super(affilie);
        idAffilie = affilie.getId();
        affilieNumero = affilie.getAffilieNumero();
        brancheEconomique = affilie.getBrancheEconomique();
        dateDebut = affilie.getDateDebut();
        dateFin = affilie.getDateFin();
        motifFin = affilie.getMotifFin();
        periodicite = affilie.getPeriodicite();
        personnaliteJuridique = affilie.getPersonnaliteJuridique();
        raisonSociale = affilie.getRaisonSociale();
        raisonSocialeCourt = affilie.getRaisonSocialeCourt();
        releveParitaire = affilie.getReleveParitaire();
        relevePersonnel = affilie.getRelevePersonnel();
        typeAffiliation = affilie.getTypeAffiliation();
        convention = affilie.getConvention();
        accesSecurite = affilie.getAccesSecurite();
        declarationSalaire = affilie.getDeclarationSalaire();
    }

    @Override
    public String getId() {
        return idAffilie;
    }

    @Override
    public void setId(String id) {
        idAffilie = id;
    }

    /**
     * Retourne le no° de l'affilié
     * 
     * @return no° de l'affilié au format XXXXXXX-XX
     */
    public String getAffilieNumero() {
        return affilieNumero;
    }

    /**
     * Mise à jour du no° de l'affilié
     * 
     * @param affilieNumero
     *            no° de l'affilié
     */
    public void setAffilieNumero(String affilieNumero) {
        this.affilieNumero = affilieNumero;
    }

    /**
     * Retourne la branche économique de l'affilié, sous forme de code système
     * 
     * @return Code système représentant la branche économique de l'affilié (ex
     *         : Construction, Agriculture, Horticulture...)
     */
    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * Mise à jour de la branche économique de l'affilié
     * 
     * @param brancheEconomique
     *            Code système représentant la branche économique de l'affilié
     */
    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    /**
     * Retourne un String représentant la date depuis laquelle l'affilié est
     * actif
     * 
     * @return String représentant une date
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * Mise à jour de la date depuis laquelle l'affilié est actif
     * 
     * @param dateDebut
     *            String représentant une date
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Retourne un String représentant la date jusqu'à laquelle l'affilié est
     * actif
     * 
     * @return String représentant une date
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * Mise à jour de la date jusqu'à laquelle l'affilié est actif
     * 
     * @param dateFin
     *            String représentant une date
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Retourne l'id tiers
     * 
     * @return id du tiers
     */
    @Override
    public String getIdTiers() {
        return super.getId();
    }

    /**
     * Mise à jour de l'id tiers
     * 
     * @param idTiers
     *            String représentant l'id du tiers
     */
    public void setIdTiers(String idTiers) {
        super.setId(idTiers);
    }

    /**
     * Retourne le code système représentant le motif de fin
     * 
     * @return Code système représentant le motif de fin (ex : Cessation
     *         d'activité, Changement de caisse, Divers...)
     */
    public String getMotifFin() {
        return motifFin;
    }

    /**
     * Mise à jour du code système représentant le motif de fin
     * 
     * @param motifFin
     *            Code système représentant le motif de fin
     */
    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    /**
     * Retourne le code système représentant la périodicité
     * 
     * @return Code système représentant la periodicité (ex : Mensuelle,
     *         Trimestrielle)
     */
    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * Mise à jour du code système représentant la périodicité
     * 
     * @param periodicite
     *            Code système représentant la périodicité
     */
    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    /**
     * Retourne le code système représentant la personnalité juridique
     * (VEPERSONNA)
     * 
     * @return Code système représentant la personnalité juridique (ex : Raison
     *         individuelle, Societe en nom collectif, Société en commandite
     *         simple...)
     */
    public String getPersonnaliteJuridique() {
        return personnaliteJuridique;
    }

    /**
     * Mise à jour du code système représentant la personnalité juridique
     * 
     * @param personnaliteJuridique
     *            Code système représentant la personnalité juridique
     */
    public void setPersonnaliteJuridique(String personnaliteJuridique) {
        this.personnaliteJuridique = personnaliteJuridique;
    }

    /**
     * Retourne la raison sociale de l'affilié
     * 
     * @return Raison sociale de l'affilié
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Mise à jour de la raison sociale de l'affilité
     * 
     * @param raisonSociale
     *            Nouvelle raison sociale de l'affilié
     */
    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    /**
     * Retourne la raison sociale de l'affilié au format court. Le plus souvent,
     * celle-ci correspond à la raison sociale long.
     * 
     * @return Raison sociale abrégée de l'affilié
     */
    public String getRaisonSocialeCourt() {
        return raisonSocialeCourt;
    }

    /**
     * Mise à jour de la raison sociale abrégé de l'affilié
     * 
     * @param raisonSocialeCourt
     *            Nouvelle raison sociale abrégée de l'affilié
     */
    public void setRaisonSocialeCourt(String raisonSocialeCourt) {
        this.raisonSocialeCourt = raisonSocialeCourt;
    }

    // FIXME: AGE : Je ne sais pas du tout à quoi selon correspond. A compléter
    // s'il vous plait
    /**
     * Retourne si l'affilié a un relevé paritaire
     * 
     * @return true si l'affilié a un relevé paritaire
     */
    public Boolean getReleveParitaire() {
        return releveParitaire;
    }

    // FIXEME: AGE : A compléter
    /**
     * Mise à jour du relevé paritaire de l'affilié
     * 
     * @param releveParitaire
     *            true ou false
     */
    public void setReleveParitaire(Boolean releveParitaire) {
        this.releveParitaire = releveParitaire;
    }

    // FIXME: AGE: A compléter
    /**
     * Retourne si l'affilié a un relevé paritaire
     * 
     * @return true si l'affilié à un relevé personnel
     */
    public Boolean getRelevePersonnel() {
        return relevePersonnel;
    }

    // FIXME: AGE: Acompléter
    /**
     * Mise à jour du relevé personnel de l'affilié
     * 
     * @param relevePersonnel
     *            true ou false
     */
    public void setRelevePersonnel(Boolean relevePersonnel) {
        this.relevePersonnel = relevePersonnel;
    }

    /**
     * Retourne un code système représentant le type d'affiliation de l'affilié
     * 
     * @return Code système représentant le type d'affiliation (ex :
     *         Indépendant, Employeur)
     */
    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    /**
     * Mise à jour du code système représentant le type d'affiliation
     * 
     * @param typeAffiliation
     *            Code système représentant le type d'affiliation
     */
    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    /**
     * Retourne la convention à laquelle l'affilié est affecté
     * 
     * @return {@link Convention} à laquelle l'affilié est affecté
     */
    public Convention getConvention() {
        return convention;
    }

    public String getNomConvention() {
        if (convention != null) {
            return convention.getDesignation();
        }
        return null;
    }

    /**
     * Mise à jour de la convention à laquelle l'affilié est affecté
     * 
     * @param convention
     *            Nouvelle convention de l'affilié
     */
    public void setConvention(Convention convention) {
        this.convention = convention;
    }

    public String getAccesSecurite() {
        return accesSecurite;
    }

    public void setAccesSecurite(String accesSecurite) {
        this.accesSecurite = accesSecurite;
    }

    /**
     * Retourne l'adresse principale formattée pour les entêtes de lettres
     * <p>
     * Raison sociale <br/>
     * RueNumero Rue <br/>
     * Npa Localite
     * <p>
     * 
     * @return
     */
    @Override
    @Deprecated
    public String getAdressePrincipaleFormatee() {
        StringBuilder sb = new StringBuilder();
        sb.append(raisonSociale);
        sb.append("\n");
        sb.append(adressePrincipale.getRue());
        sb.append(" ");
        sb.append(adressePrincipale.getRueNumero());
        sb.append("\n");
        sb.append(adressePrincipale.getNpa());
        sb.append(" ");
        sb.append(adressePrincipale.getLocalite());
        return sb.toString();
    }

    public List<Cotisation> getCotisations() {
        return cotisations;
    }

    public void setCotisations(List<Cotisation> cotisations) {
        this.cotisations = cotisations;
    }

    public boolean isTrimestriel() {
        return Affilie.PERIODICITE_TRIMESTRIELLE.equals(periodicite);
    }

    public boolean isMensuel() {
        return Affilie.PERIODICITE_MENSUELLE.equals(periodicite);
    }

    public boolean isSemestriel() {
        return Affilie.PERIODICITE_SEMESTRIELLE.equals(periodicite);
    }

    public boolean isAnnuel() {
        return Affilie.PERIODICITE_ANNUELLE.equals(periodicite);
    }

    public List<Particularite> getParticularites() {
        return particularites;
    }

    public void setParticularites(List<Particularite> particularites) {
        this.particularites = particularites;
    }

	/**
	 * @return the declarationSalaire
	 */
	public String getDeclarationSalaire() {
		return declarationSalaire;
	}

	/**
	 * @param declarationSalaire the declarationSalaire to set
	 */
	public void setDeclarationSalaire(String declarationSalaire) {
		this.declarationSalaire = declarationSalaire;
	}
}
