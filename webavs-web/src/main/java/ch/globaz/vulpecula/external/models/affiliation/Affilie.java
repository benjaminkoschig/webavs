package ch.globaz.vulpecula.external.models.affiliation;

import java.util.ArrayList;
import java.util.List;

import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;

/**
 * Repr�sentation m�tier d'un Affilie selon le module Naos
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public class Affilie extends Tiers {

    /** P�riodicit� annuelle */
    public final static String PERIODICITE_ANNUELLE = "802004";
    /** P�riodicit� mensuelle */
    public final static String PERIODICITE_MENSUELLE = "802001";
    /** P�riodicit� semestrielle */
    public final static String PERIODICITE_SEMESTRIELLE = "802003";
    /** P�riodicit� trimestrielle */
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
     * Retourne le no� de l'affili�
     * 
     * @return no� de l'affili� au format XXXXXXX-XX
     */
    public String getAffilieNumero() {
        return affilieNumero;
    }

    /**
     * Mise � jour du no� de l'affili�
     * 
     * @param affilieNumero
     *            no� de l'affili�
     */
    public void setAffilieNumero(String affilieNumero) {
        this.affilieNumero = affilieNumero;
    }

    /**
     * Retourne la branche �conomique de l'affili�, sous forme de code syst�me
     * 
     * @return Code syst�me repr�sentant la branche �conomique de l'affili� (ex
     *         : Construction, Agriculture, Horticulture...)
     */
    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * Mise � jour de la branche �conomique de l'affili�
     * 
     * @param brancheEconomique
     *            Code syst�me repr�sentant la branche �conomique de l'affili�
     */
    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    /**
     * Retourne un String repr�sentant la date depuis laquelle l'affili� est
     * actif
     * 
     * @return String repr�sentant une date
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * Mise � jour de la date depuis laquelle l'affili� est actif
     * 
     * @param dateDebut
     *            String repr�sentant une date
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Retourne un String repr�sentant la date jusqu'� laquelle l'affili� est
     * actif
     * 
     * @return String repr�sentant une date
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * Mise � jour de la date jusqu'� laquelle l'affili� est actif
     * 
     * @param dateFin
     *            String repr�sentant une date
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
     * Mise � jour de l'id tiers
     * 
     * @param idTiers
     *            String repr�sentant l'id du tiers
     */
    public void setIdTiers(String idTiers) {
        super.setId(idTiers);
    }

    /**
     * Retourne le code syst�me repr�sentant le motif de fin
     * 
     * @return Code syst�me repr�sentant le motif de fin (ex : Cessation
     *         d'activit�, Changement de caisse, Divers...)
     */
    public String getMotifFin() {
        return motifFin;
    }

    /**
     * Mise � jour du code syst�me repr�sentant le motif de fin
     * 
     * @param motifFin
     *            Code syst�me repr�sentant le motif de fin
     */
    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    /**
     * Retourne le code syst�me repr�sentant la p�riodicit�
     * 
     * @return Code syst�me repr�sentant la periodicit� (ex : Mensuelle,
     *         Trimestrielle)
     */
    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * Mise � jour du code syst�me repr�sentant la p�riodicit�
     * 
     * @param periodicite
     *            Code syst�me repr�sentant la p�riodicit�
     */
    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    /**
     * Retourne le code syst�me repr�sentant la personnalit� juridique
     * (VEPERSONNA)
     * 
     * @return Code syst�me repr�sentant la personnalit� juridique (ex : Raison
     *         individuelle, Societe en nom collectif, Soci�t� en commandite
     *         simple...)
     */
    public String getPersonnaliteJuridique() {
        return personnaliteJuridique;
    }

    /**
     * Mise � jour du code syst�me repr�sentant la personnalit� juridique
     * 
     * @param personnaliteJuridique
     *            Code syst�me repr�sentant la personnalit� juridique
     */
    public void setPersonnaliteJuridique(String personnaliteJuridique) {
        this.personnaliteJuridique = personnaliteJuridique;
    }

    /**
     * Retourne la raison sociale de l'affili�
     * 
     * @return Raison sociale de l'affili�
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Mise � jour de la raison sociale de l'affilit�
     * 
     * @param raisonSociale
     *            Nouvelle raison sociale de l'affili�
     */
    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    /**
     * Retourne la raison sociale de l'affili� au format court. Le plus souvent,
     * celle-ci correspond � la raison sociale long.
     * 
     * @return Raison sociale abr�g�e de l'affili�
     */
    public String getRaisonSocialeCourt() {
        return raisonSocialeCourt;
    }

    /**
     * Mise � jour de la raison sociale abr�g� de l'affili�
     * 
     * @param raisonSocialeCourt
     *            Nouvelle raison sociale abr�g�e de l'affili�
     */
    public void setRaisonSocialeCourt(String raisonSocialeCourt) {
        this.raisonSocialeCourt = raisonSocialeCourt;
    }

    // FIXME: AGE : Je ne sais pas du tout � quoi selon correspond. A compl�ter
    // s'il vous plait
    /**
     * Retourne si l'affili� a un relev� paritaire
     * 
     * @return true si l'affili� a un relev� paritaire
     */
    public Boolean getReleveParitaire() {
        return releveParitaire;
    }

    // FIXEME: AGE : A compl�ter
    /**
     * Mise � jour du relev� paritaire de l'affili�
     * 
     * @param releveParitaire
     *            true ou false
     */
    public void setReleveParitaire(Boolean releveParitaire) {
        this.releveParitaire = releveParitaire;
    }

    // FIXME: AGE: A compl�ter
    /**
     * Retourne si l'affili� a un relev� paritaire
     * 
     * @return true si l'affili� � un relev� personnel
     */
    public Boolean getRelevePersonnel() {
        return relevePersonnel;
    }

    // FIXME: AGE: Acompl�ter
    /**
     * Mise � jour du relev� personnel de l'affili�
     * 
     * @param relevePersonnel
     *            true ou false
     */
    public void setRelevePersonnel(Boolean relevePersonnel) {
        this.relevePersonnel = relevePersonnel;
    }

    /**
     * Retourne un code syst�me repr�sentant le type d'affiliation de l'affili�
     * 
     * @return Code syst�me repr�sentant le type d'affiliation (ex :
     *         Ind�pendant, Employeur)
     */
    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    /**
     * Mise � jour du code syst�me repr�sentant le type d'affiliation
     * 
     * @param typeAffiliation
     *            Code syst�me repr�sentant le type d'affiliation
     */
    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    /**
     * Retourne la convention � laquelle l'affili� est affect�
     * 
     * @return {@link Convention} � laquelle l'affili� est affect�
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
     * Mise � jour de la convention � laquelle l'affili� est affect�
     * 
     * @param convention
     *            Nouvelle convention de l'affili�
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
     * Retourne l'adresse principale formatt�e pour les ent�tes de lettres
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
