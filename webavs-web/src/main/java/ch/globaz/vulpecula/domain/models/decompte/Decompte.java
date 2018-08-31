package ch.globaz.vulpecula.domain.models.decompte;

import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.specifications.decompte.DecompteCTNumeroRequis;
import ch.globaz.vulpecula.domain.specifications.decompte.DecompteDateDecompteRequiseSpecification;
import ch.globaz.vulpecula.domain.specifications.decompte.DecompteEmployeurRequisSpecification;
import ch.globaz.vulpecula.domain.specifications.decompte.DecomptePeriodeRequiseSpecification;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;
import ch.globaz.vulpecula.external.models.hercule.InteretsMoratoires;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public class Decompte implements DomainEntity, Serializable {
    private static final long serialVersionUID = 1L;

    private String idDecompte;
    private Date dateEtablissement;
    private Date dateReception;
    private transient Employeur employeur;
    private NumeroDecompte numeroDecompte;
    private Montant montantVerse;
    private PeriodeMensuelle periode;
    private transient List<DecompteSalaire> lignes;
    private TypeDecompte type;
    private EtatDecompte etat;
    private Date dateRappel;
    private String idPassageFacturation;
    private String idRapportControle;
    private InteretsMoratoires interetsMoratoires;
    private String remarqueRectification;
    private String spy;
    private boolean manuel;
    private MotifProlongation motifProlongation;
    private boolean rectifie;
    private transient final List<HistoriqueDecompte> historiques;
    private String typeDecompteLibelle;
    private boolean controleAC2;
    private TaxationOffice taxationOfficeModel;
    private TypeProvenance typeProvenance;

    /**
     * Passage de facturation auquel le d�compte est li�.
     * Attention, celui-ci n'est pas automatiquement charg� depuis la persistence (uniquement DecompteComplexModel)
     */
    private Passage passage;

    public String getTypeDecompteLibelle() {
        return typeDecompteLibelle;
    }

    public void setTypeDecompteLibelle(String typeDecompteLibelle) {
        this.typeDecompteLibelle = typeDecompteLibelle;
    }

    /**
     * Retourne l'etat de la taxation d'office, null, si il n'y a pas de taxation
     * 
     * @return {@link EtatTaxation} etat de la taxation d'office
     */
    public EtatTaxation getEtatTaxationOffice() {
        TaxationOffice to = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findByIdDecompte(idDecompte);
        if (to == null) {
            return null;
        } else {
            return to.getEtat();
        }

    }

    /**
     * @return
     */
    public boolean isRectifie() {
        return rectifie;
    }

    /**
     * @param rectifie
     */
    public void setRectifie(boolean rectifie) {
        this.rectifie = rectifie;
    }

    /**
     * 
     */
    public Decompte() {
        lignes = new ArrayList<DecompteSalaire>();
        historiques = new ArrayList<HistoriqueDecompte>();
    }

    /**
     * @param differenceProperties
     * @param differenceControle
     * @return
     */
    public DifferenceControle controler(Montant differenceProperties, DifferenceControle differenceControle) {
        // BMS-1593 : Pouvoir cr�er des d�compte � z�ro (test si possible)
        // if (getMasseSalarialeTotal().isZero()) {
        // throw new IllegalArgumentException("Lignes de salaires non renseign�es !");
        // }

        Montant differenceAdmise = Montant.ZERO;
        differenceAdmise = differenceProperties;

        Montant difference = getMontantDifference();
        differenceControle.setDifference(difference.getValue());
        differenceControle.setMontantControle(getMontantControle().getValue());
        differenceControle.setTotalContributions(getMontantContributionTotal().getValue());
        if (difference.getMontantAbsolu().greater(differenceAdmise)) {
            differenceControle.setValid(false);
            return differenceControle;
        } else {
            differenceControle.setValid(true);
            return differenceControle;
        }
    }

    public Annee getAnnee() {
        return new Annee(periode.getAnneeDebut());
    }

    /**
     * @return
     */
    public String getAnneePeriodeDebut() {
        if (periode != null) {
            return periode.getAnneeDebut();
        }
        return null;
    }

    /**
     * @return
     */
    public String getAnneePeriodeFin() {
        if (periode != null) {
            return periode.getAnneeFin();
        }
        return null;
    }

    public String getNomMoisPeriodeDebut(Locale locale) {
        int noMois = periode.getPeriodeDebut().getNumeroMois();
        return Date.getMonthName(noMois, locale);
    }

    /**
     * @return
     */
    public String getPeriodeDebutFormate() {
        return periode.getPeriodeDebutWithDay();
    }

    /**
     * Retourne la p�riode de fin du d�compte.
     * 
     * @return Date repr�sentant la pr�iode de fin du d�compte
     */
    public Date getPeriodeFin() {
        if (periode == null) {
            return null;
        }
        return periode.getPeriodeFin();
    }

    /**
     * Retourne la p�riode de fin au format suisse (mm.YYYY)
     * 
     * @return La p�riode de fin en String
     */
    public String getPeriodeFinAsSwissValue() {
        if (periode == null) {
            return null;
        }
        return periode.getPeriodeFinAsSwissValue();
    }

    /**
     * @return
     */
    public String getPeriodeFinFormate() {
        if (periode == null) {
            return null;
        }
        return periode.getPeriodeFinWithDay();
    }

    /**
     * @return
     */
    public Date getDateEtablissement() {
        return dateEtablissement;
    }

    /**
     * @return
     */
    public String getDateEtablissementAsSwissValue() {
        if (dateEtablissement != null) {
            return dateEtablissement.getSwissValue();
        }
        return null;
    }

    /**
     * @return
     */
    public Date getDateReception() {
        return dateReception;
    }

    /**
     * @return
     */
    public String getDateReceptionAsSwissValue() {
        if (dateReception != null) {
            return dateReception.getSwissValue();
        }
        return null;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public EtatDecompte getEtat() {
        return etat;
    }

    @Override
    public String getId() {
        return idDecompte;
    }

    public String getIdGed() {
        if (getNumeroDecompte().toString().startsWith("0")) {
            return getNumeroDecompte().toString();
        } else {
            return getId();
        }
    }

    public Boolean getGedMyProdis() {
        return VulpeculaServiceLocator.getPropertiesService().isGedMyProdis();
    }

    /**
     * Retourne l'id de l'employeur auquel le d�compte est rattach�.
     * 
     * @return String repr�sentant l'id de l'employeur
     */
    public String getIdEmployeur() {
        if (employeur != null) {
            return employeur.getId();
        }
        return null;
    }

    /**
     * Retourne l'id du tiers auquel le d�compte est rattach�.
     * 
     * @return String repr�sentant l'id du tiers
     */
    public String getIdTiers() {
        if (employeur == null) {
            return null;
        }
        return employeur.getIdTiers();
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdRapportControle() {
        return idRapportControle;
    }

    public InteretsMoratoires getInteretsMoratoires() {
        return interetsMoratoires;
    }

    public List<DecompteSalaire> getLignes() {
        return lignes;
    }

    public String getMoisPeriodeDebut() {
        if (periode != null) {
            return periode.getMoisDebut();
        }
        return null;
    }

    public String getMoisPeriodeFin() {
        if (periode != null) {
            return periode.getMoisFin();
        }
        return null;
    }

    /**
     * Retourne la date de rappel pour lequel le d�compte a �t� pass� � l'�tat Sommation
     * 
     * @return Retourne la date de rappel du d�compte
     */
    public Date getDateRappel() {
        return dateRappel;
    }

    /**
     * Retourne la date de rappel pour lequel le d�compte a �t� pass� � l'�tat Sommation au format suisse
     * 
     * @return Retourne la date de rappel du d�compte au format dd.MM.yyyy
     */
    public String getDateRappelAsSwissValue() {
        if (dateRappel == null) {
            return null;
        }
        return dateRappel.getSwissValue();
    }

    /**
     * Mise � jour de la date du rappel pour lequel le d�compte a �t� pass� � l'�tat Sommation
     * 
     * @param dateRappel Nouvelle date de rappel
     */
    public void setDateRappel(final Date dateRappel) {
        this.dateRappel = dateRappel;
    }

    /**
     * Retourne le montant de contr�le qui a �t� d�fini sur ce d�compte.
     * 
     * @return Montant sur lequel le d�compte sera contr�l�
     */
    public Montant getMontantControle() {
        return montantVerse;
    }

    /**
     * Retourne le montant (masse salariale) des d�comptes salaires associ�s � ce d�compte, soit le montant total du
     * d�compte.
     * 
     * @return Montant du d�compte
     */
    public Montant getMasseSalarialeTotal() {
        Montant montant = new Montant(0);
        for (DecompteSalaire ligneSalaire : getLignes()) {
            montant = montant.add(ligneSalaire.getSalaireTotal());
        }
        return montant;
    }

    /**
     * Retourne le montant de diff�rence, soit (le montant total des cotisations lignes de salaires) - (le montant
     * vers� appel� de contr�le ).
     * 
     * @return Montant
     */
    public Montant getMontantDifference() {
        Montant montantContributionTotal = getMontantContributionTotal();
        if (montantVerse == null) {
            return montantContributionTotal;
        }

        if (montantVerse.equals(new Montant(-1))) {
            return new Montant(0);
        }

        return montantContributionTotal.substract(montantVerse);
    }

    /**
     * @return
     */
    public Montant getMontantContributionTotal() {
        Montant total = Montant.ZERO;
        for (CotisationCalculee cotisation : getTableCotisationsCalculees()) {
            total = total.add(cotisation.getMontantCalculee().normalize());
        }

        return total.normalize();
    }

    /**
     * @return the numeroDecompte
     */
    public NumeroDecompte getNumeroDecompte() {
        return numeroDecompte;
    }

    /**
     * Retourne l'ann�e du num�ro de d�compte associ�.
     * 
     * @return String repr�sentant l'ann�e du d�compte ou null si num�ro non existant
     */
    public String getAnneeNumeroDecompte() {
        if (numeroDecompte == null) {
            return null;
        }
        return numeroDecompte.getAnnee();
    }

    /**
     * Retourne le code du num�ro de d�compte associ�.
     * 
     * @return String repr�sentant le code du num�ro de d�compte ou null si num�ro non existant
     */
    public String getCodeNumeroDecompte() {
        if (numeroDecompte == null) {
            return null;
        }
        return numeroDecompte.getCode();
    }

    /**
     * Retourne l'offset du num�ro de d�compte associ�.
     * 
     * @return String repr�sentant l'offet actuel du num�ro de d�compte ou null si num�ro non existant
     */
    public String getOffsetNumeroDecompte() {
        if (numeroDecompte == null) {
            return null;
        }
        return numeroDecompte.getOffset();
    }

    public PeriodeMensuelle getPeriode() {
        return periode;
    }

    public List<PosteTravail> getPostesTravail() {
        if (employeur != null) {
            return employeur.getPostesTravail();
        }
        return new ArrayList<PosteTravail>();
    }

    public String getRemarqueRectification() {
        return remarqueRectification;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    public List<Taux> getTauxContribuablesDifferentsDesPostes() {
        return new ArrayList<Taux>(employeur.getTauxContribuablesDifferentsDesPostes());
    }

    public List<Taux> getTauxContribuablesDifferents() {
        Set<Taux> taux = new HashSet<Taux>();
        for (DecompteSalaire decompteSalaire : lignes) {
            taux.add(decompteSalaire.getTauxContribuableForCaissesSociales(false));
        }
        return new ArrayList<Taux>(taux);
    }

    public TypeDecompte getType() {
        return type;
    }

    public String getTypeAsValue() {
        if (type != null) {
            return type.getValue();
        }
        return null;
    }

    public MotifProlongation getMotifProlongation() {
        return motifProlongation;
    }

    public boolean isComplementaire() {
        return TypeDecompte.COMPLEMENTAIRE.equals(type);
    }

    public boolean isControleEmployeur() {
        return TypeDecompte.CONTROLE_EMPLOYEUR.equals(type);
    }

    public boolean isNotEbusiness() {
        return !TypeProvenance.EBUSINESS.equals(getTypeProvenance());
    }

    public boolean isPeriodique() {
        return TypeDecompte.PERIODIQUE.equals(type);
    }

    public boolean isSpecial() {
        return isSpecialCaisse() || isSpecialSalaire();
    }

    public boolean isCPP() {
        return TypeDecompte.CPP.equals(type);
    }

    public boolean isSpecialCaisse() {
        return TypeDecompte.SPECIAL_CAISSE.equals(type);
    }

    public boolean isSoumisAC() {
        return employeur.isSoumisAC();
    }

    public boolean isSoumisAC2() {
        return employeur.isSoumisAC2();
    }

    public boolean isSoumisAVS() {
        return employeur.isSoumisAVS();
    }

    public boolean isSpecialSalaire() {
        return type.isSpecialSalaire();
    }

    public boolean isTraiterAsSpecial() {
        return type.isTraiterAsSpecial();
    }

    public void setDateEtablissement(final Date dateEtablissement) {
        this.dateEtablissement = dateEtablissement;
    }

    public void setDateReception(final Date dateReception) {
        this.dateReception = dateReception;
    }

    public void setEmployeur(final Employeur employeur) {
        this.employeur = employeur;
    }

    public void setEtat(final EtatDecompte etat) {
        this.etat = etat;
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }

    @Override
    public void setId(final String id) {
        idDecompte = id;
    }

    public void setIdPassageFacturation(final String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setIdRapportControle(final String idRapportControle) {
        this.idRapportControle = idRapportControle;
    }

    public void setInteretsMoratoires(final InteretsMoratoires interetsMoratoires) {
        this.interetsMoratoires = interetsMoratoires;
    }

    public void setLignes(final List<DecompteSalaire> lignes) {
        this.lignes = lignes;
    }

    /**
     * Ajoute un d�compte salaire dans la liste des d�comptes salaire du d�compte.
     * 
     * @param decompteSalaire D�compte salaire � ajouter
     */
    public void addDecompteSalaire(final DecompteSalaire decompteSalaire) {
        lignes.add(decompteSalaire);
    }

    /**
     * Ajout d'une ligne d'historique dans le d�compte.
     * 
     * @param historiqueDecompte Ligne d'historique � rajouter dans le d�compte
     */
    public void addHistorique(final HistoriqueDecompte historiqueDecompte) {
        historiques.add(historiqueDecompte);
    }

    public void setMontantVerse(final Montant montantVerse) {
        this.montantVerse = montantVerse;
    }

    /**
     * @param numeroDecompte
     *            the numeroDecompte to set
     */
    public void setNumeroDecompte(final NumeroDecompte numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public void setPeriode(final PeriodeMensuelle periode) {
        this.periode = periode;
    }

    public void setRemarqueRectification(final String remarqueRectification) {
        this.remarqueRectification = remarqueRectification;
    }

    @Override
    public void setSpy(final String spy) {
        this.spy = spy;
    }

    public void setType(final TypeDecompte type) {
        this.type = type;
    }

    public void setHistoriques(final List<HistoriqueDecompte> historiques) {
        if (historiques == null) {
            throw new NullPointerException("La variable historiques est null");
        }
        this.historiques.clear();
        this.historiques.addAll(historiques);

    }

    public void validate() throws UnsatisfiedSpecificationException {
        final DecompteDateDecompteRequiseSpecification dateRequise = new DecompteDateDecompteRequiseSpecification();
        final DecompteEmployeurRequisSpecification employeurRequis = new DecompteEmployeurRequisSpecification();
        final DecomptePeriodeRequiseSpecification periodeRequise = new DecomptePeriodeRequiseSpecification();
        final DecompteCTNumeroRequis ctNumeroRequis = new DecompteCTNumeroRequis();
        final Specification<Decompte> decompteSpecification = dateRequise.and(employeurRequis).and(periodeRequise)
                .and(ctNumeroRequis);
        decompteSpecification.isSatisfiedBy(this);
    }

    /**
     * Retourne si le d�compte peut �tre r�ceptionn�. Un d�compte pouvant �tre r�ceptionn� doit �tre dans un �tat
     * G�n�r�.
     * 
     * @return true si {@link EtatDecompte#GENERE}
     */
    public boolean isReceptionnable() {
        if (EtatDecompte.GENERE.equals(etat) || EtatDecompte.SOMMATION.equals(etat)) {
            return true;
        }
        if (EtatDecompte.OUVERT.equals(etat)
                && (TypeDecompte.PERIODIQUE.equals(getType()) || TypeDecompte.COMPLEMENTAIRE.equals(getType()))) {
            return true;
        }
        return false;
    }

    /**
     * Retourne si le d�compte poss�de une date de rappel
     * 
     * @return true si il poss�de une date de rappel
     */
    public boolean getHasDateRappel() {
        if (dateRappel != null) {
            return true;
        }
        return false;
    }

    /**
     * Retourne si le d�compte peut �tre contr�l�. Un d�compte pouvant �tre contr�l� doit �tre dans un �tat Ouvert,
     * R�ceptionn� ou en Erreur
     * 
     * @return true si {@link EtatDecompte#OUVERT}, {@link EtatDecompte#ERREUR} ou {@link EtatDecompte#RECEPTIONNE}
     */
    public boolean isControlable() {
        if (EtatDecompte.A_TRAITER.equals(etat)) {
            List<DecompteSalaire> listeDecompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findByIdDecompte(getId());
            for (DecompteSalaire ligne : listeDecompteSalaire) {
                if (ligne.isaTraiter()) {
                    return false;
                }
            }
        }

        switch (etat) {
            case OUVERT:
            case ERREUR:
            case RECEPTIONNE:
            case A_TRAITER:
                return true;
            default:
                return false;
        }
    }

    /**
     * Retourne si le d�compte peut �tre annul�. Un d�compte pouvant �tre annul� doit �tre dans un �tat Ouvert ou g�n�r�
     * 
     * @return true si {@link EtatDecompte#OUVERT} ou {@link EtatDecompte#GENERE}
     */
    public boolean isAnnulable() {
        switch (etat) {
            case OUVERT:
            case GENERE:
            case SOMMATION:
            case A_TRAITER:
                return true;
            default:
                return false;
        }
    }

    /**
     * Retourne si le d�compte peut �tre d�valid�. Un d�compte pouvant �tre d�valid� doit �tre dans un �tat Valid� ou
     * Rectifi�
     * 
     * @return true si {@link EtatDecompte#VALIDE} ou {@link EtatDecompte#RECTIFIE}
     */
    public boolean isDevalidable() {
        switch (etat) {
            case VALIDE:
            case RECTIFIE:
                // case A_TRAITER:
                return true;
            default:
                return false;
        }
    }

    public boolean isValideOuComptablise() {
        switch (etat) {
            case VALIDE:
            case COMPTABILISE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Retourne la date de comptabilisation dans l'historique au format Suisse. Dans le cas o� l'on retrouve plusieurs
     * dates, la derni�re sera reprise.
     * 
     * @return String repr�sentant la date de la derni�re comptabilisation au format dd.MM.yyyy
     */
    public String getDateComptabilisationAsSwissValue() {
        Date date = getDateComptabilisation();
        if (date == null) {
            return null;
        }
        return date.getSwissValue();
    }

    /**
     * Retourne la date de la comptabilisation dans l'historique. Dans le cas o� l'on retrouve plusieurs dates, la
     * derni�re sera reprise.
     * 
     * @return Retourne la date de la derni�re comptabilisation
     */
    public Date getDateComptabilisation() {
        return getDateForEtat(EtatDecompte.COMPTABILISE);
    }

    /**
     * Retourne la date de la rectification dans l'historique au format Suisse. Dans le cas o� l'on retrouve plusieurs
     * dates, la
     * derni�re sera reprise.
     * 
     * @return Retourne la date de la derni�re rectification au format dd.MM.yyyy
     */
    public String getDateRectificationAsSwissValue() {
        Date date = getDateRectification();
        if (date == null) {
            return null;
        }
        return date.getSwissValue();
    }

    /**
     * Retourne la date de la rectification dans l'historique. Dans le cas o� l'on retrouve plusieurs dates, la
     * derni�re sera reprise.
     * 
     * @return Retourne la date de la derni�re rectification
     */
    public Date getDateRectification() {
        return getDateForEtat(EtatDecompte.RECTIFIE);
    }

    private Date getDateForEtat(final EtatDecompte etatDecompte) {
        Date dateComptabilisation = null;
        for (HistoriqueDecompte historiqueDecompte : historiques) {
            if (etatDecompte.equals(historiqueDecompte.getEtat())) {
                if (dateComptabilisation == null || dateComptabilisation.before(historiqueDecompte.getDate())) {
                    dateComptabilisation = historiqueDecompte.getDate();
                }
            }
        }
        return dateComptabilisation;
    }

    /**
     * Retourne une table de cotisations agr�g�es.
     * On retrouvera donc une liste de cette forme :
     * <table>
     * <tr>
     * <th>Cotisation
     * <th>Montant
     * <th>Taux
     * </tr>
     * <tr>
     * <td>AF CAFAB
     * <td>1000.-
     * <td>2.5
     * </tr>
     */
    public List<CotisationCalculee> getTableCotisationsCalculees() {
        List<CotisationCalculee> cotisationsMergees = new ArrayList<CotisationCalculee>();
        for (CotisationCalculee cotisationCalculee : getAllCotisationsCalculees()) {
            boolean found = false;
            for (int i = 0; i < cotisationsMergees.size(); i++) {
                CotisationCalculee cotisationMergee = cotisationsMergees.get(i);
                if (cotisationMergee.hasSameAssuranceAndTaux(cotisationCalculee)) {
                    CotisationCalculee newCotisationMergee = cotisationMergee.add(cotisationCalculee);
                    cotisationsMergees.set(i, newCotisationMergee);
                    found = true;
                }
            }
            if (!found) {
                cotisationsMergees.add(cotisationCalculee);
            }
        }
        return cotisationsMergees;
    }

    /**
     * 
     * @return {@link Montant} Masse de toutes les lignes de d�comptes pour un type d'assurance
     */
    public Montant getMasseCotisationCalculees(TypeAssurance typeAssurance) {
        Montant montantTotal = Montant.ZERO;
        List<CotisationCalculee> cotisationsMergees = new ArrayList<CotisationCalculee>();
        for (CotisationCalculee cotisationCalculee : getAllCotisationsCalculees()) {
            boolean found = false;
            for (int i = 0; i < cotisationsMergees.size(); i++) {
                CotisationCalculee cotisationMergee = cotisationsMergees.get(i);
                if (cotisationMergee.hasSameAssuranceAndTaux(cotisationCalculee)) {
                    CotisationCalculee newCotisationMergee = cotisationMergee.add(cotisationCalculee);
                    cotisationsMergees.set(i, newCotisationMergee);
                    found = true;
                }
            }
            if (!found) {
                cotisationsMergees.add(cotisationCalculee);
            }
        }
        for (CotisationCalculee cotisation : cotisationsMergees) {
            if (cotisation.getCotisation().getTypeAssurance().getValue().equals(typeAssurance.getValue())) {
                montantTotal = montantTotal.add(cotisation.getMontant());
            }
        }
        return montantTotal;
    }

    /**
     * Retourne une table de cotisations calculees group�es par leur plan caisse.
     * 
     * @return La table de cotisation calculees group�s par {@link PlanCaisse} sous forme de Map
     */
    public Map<PlanCaisse, List<CotisationCalculee>> getTableCotisationsCalculeesGroupByCaisse() {
        Map<PlanCaisse, List<CotisationCalculee>> map = new HashMap<PlanCaisse, List<CotisationCalculee>>();
        for (CotisationCalculee cotisationCalculee : getTableCotisationsCalculees()) {
            List<CotisationCalculee> cotisationsCalculees = null;
            PlanCaisse planCaisse = cotisationCalculee.getPlanCaisse();
            if (map.containsKey(planCaisse)) {
                cotisationsCalculees = map.get(planCaisse);
            } else {
                cotisationsCalculees = new ArrayList<CotisationCalculee>();
                map.put(planCaisse, cotisationsCalculees);
            }
            cotisationsCalculees.add(cotisationCalculee);
        }
        return map;
    }

    /**
     * Retourne une liste de cotisations calcul�es. Il s'agit uniquement d'une repr�sentation sous forme de
     * "Cotisation, Montant, Taux" des diff�rentes lignes de salaires (DTO).
     * 
     * @return Liste de cotisations calcul�es
     */
    private List<CotisationCalculee> getAllCotisationsCalculees() {
        List<CotisationCalculee> cotisationsCalculees = new ArrayList<CotisationCalculee>();
        for (DecompteSalaire decompteSalaire : lignes) {
            cotisationsCalculees.addAll(decompteSalaire.getCotisationCalculees());
        }
        return cotisationsCalculees;
    }

    public List<CotisationCalculee> getAllCotisationsCalculeesGroupByAnnee() {
        List<CotisationCalculee> cotisationsMergees = new ArrayList<CotisationCalculee>();
        List<CotisationCalculee> cotisations = getAllCotisationsCalculees();
        Collections.sort(cotisations, new Comparator<CotisationCalculee>() {
            @Override
            public int compare(CotisationCalculee c1, CotisationCalculee c2) {
                if (c1.getAnnee().isBefore(c2.getAnnee())) {
                    return -1;
                } else if (c1.getAnnee().isAfter(c2.getAnnee())) {
                    return 1;
                } else {
                    return c1.getAssuranceId().compareTo(c2.getAssuranceId());
                }
            }
        });

        for (CotisationCalculee cotisationCalculee : cotisations) {
            boolean found = false;
            for (int i = 0; i < cotisationsMergees.size(); i++) {
                CotisationCalculee cotisationMergee = cotisationsMergees.get(i);
                if (cotisationMergee.hasSameAssuranceTauxAndAnnee(cotisationCalculee)) {
                    CotisationCalculee newCotisationMergee = cotisationMergee.add(cotisationCalculee);
                    cotisationsMergees.set(i, newCotisationMergee);
                    found = true;
                }
            }
            if (!found) {
                cotisationsMergees.add(cotisationCalculee);
            }
        }
        return cotisationsMergees;
    }

    public boolean isManuel() {
        return manuel;
    }

    /**
     * Retourne true si le d�compte salaire peut �tre �dit�.
     * 
     * @return true si {@link EtatDecompte#OUVERT}, {@link EtatDecompte#GENERE}, {@link EtatDecompte#ERREUR},
     *         {@link EtatDecompte#SOMMATION} ou {@link EtatDecompte#RECEPTIONNE}
     */
    public boolean isEditable() {
        switch (getEtat()) {
            case OUVERT:
            case GENERE:
            case ERREUR:
            case SOMMATION:
            case RECEPTIONNE:
            case A_TRAITER:
                return true;
            default:
                return false;
        }
    }

    public void setManuel(final boolean manuel) {
        this.manuel = manuel;
    }

    public void setMotifProlongation(MotifProlongation motifProlongation) {
        this.motifProlongation = motifProlongation;
    }

    public boolean isTaxationOffice() {
        if (EtatDecompte.TAXATION_DOFFICE.equals(etat)) {
            return true;
        }
        return false;
    }

    /**
     * Retourne si le d�compte est du type pass� en param�ter
     * 
     * @param type Type de d�compte
     * @return true si m�me type
     */
    public boolean isType(final TypeDecompte type) {
        return this.type.equals(type);
    }

    /**
     * Retourne la p�riode de d�but du d�compte si existante.
     * 
     * @return La date de d�but ou null si inexistante
     */
    public Date getPeriodeDebut() {
        if (periode == null) {
            return null;
        }
        return periode.getPeriodeDebut();
    }

    /**
     * Retourne la p�riode de d�but au format suisse (dd.MM.yyyy)
     */
    public String getPeriodeDebutAsSwissValue() {
        if (periode == null) {
            return null;
        }
        return periode.getPeriodeDebutAsSwissValue();
    }

    /**
     * Retourne la p�riodicit� de l'employeur si existant.
     * 
     * @return String repr�sentant la p�riodicit� (code syst�me) ou null si inexistant
     */
    public String getPeriodiciteEmployeur() {
        if (employeur == null) {
            return null;
        }
        return employeur.getPeriodicite();
    }

    public void calculerAndSetRappel(int nbjours) {
        dateRappel = calculerRappel(nbjours);
    }

    /**
     * Calcul de la date de taxation d'office et set dans l'attribut dateRappel
     * Si la fin de la p�riode est sup�rieure ou �gale � la date d'�tablissement, alors on rajoute X jours � la date de
     * rappel.
     * Si la fin de la p�riode est inf�rieure � la date du d�compte, alors on ajout Y jours � la date de rappel.
     * 
     * @param x Le nombre de jours � rajouter (X)
     * @param y Le nombre de jours � rajouter (Y)
     * 
     */
    public void calculerAndSetTaxation(int x, int y, Date dateReference) {
        dateRappel = calculerTaxation(x, y, dateReference);
    }

    /**
     * Calcul de la date de rappel
     * Si la fin de la p�riode du d�compte est sup�rieure/�gal � la date d'�tablissement, alors on ajoute X jours � la
     * date de fin de p�riode.
     * Si la fin de la p�riode du d�compte est inf�rieure � la date d'�tablissement, alors on ajoute 30 jours � la date
     * d'�tablissement.
     * 
     * @param Le nombre de jours (X) � ajouter pour la date de rappel
     * @return Un nouvel objet date calcul�
     */
    public Date calculerRappel(final int jours) {
        Date periodeFin = periode.getPeriodeFin();
        Date dateRappel = null;

        if (periodeFin.beforeOrEquals(dateEtablissement)) {
            // BMS-1962 Sommation: R�vision du calcul de la date de rappel pour les d�comptes dont la p�riode de fin est
            // < � la date du jour
            dateRappel = dateEtablissement.addDays(30);
        } else {
            dateRappel = periodeFin.addDays(jours);
        }

        return dateRappel;
    }

    /**
     * Calcul de la date de sommation
     * Si la fin de la p�riode est sup�rieure ou �gale � la date d'�tablissement, alors on rajoute X jours � la date de
     * rappel.
     * Si la fin de la p�riode est inf�rieure � la date du d�compte, alors on ajout Y jours � la date de rappel.
     * 
     * @param x Le nombre de jours � rajouter (X)
     * @param y Le nombre de jours � rajouter (Y)
     * 
     * @return Un nouvel objet date
     */
    public Date calculerTaxation(final int x, final int y, Date dateReference) {
        Date periodeFin = periode.getPeriodeFin();

        if (periodeFin.beforeOrEquals(dateEtablissement)) {
            return dateReference.addDays(y);
        } else {
            return dateReference.addDays(x);
        }
    }

    /**
     * @return Code syst�me repr�sentant la langue de l'employeur
     */
    public String getEmployeurCSLangue() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur (" + getIdEmployeur() + ") du d�compte " + getId()
                    + " est null !");
        }

        return employeur.getLangue();
    }

    /**
     * @return Code syst�me repr�sentant la langue de l'employeur
     */
    public CodeLangue getEmployeurLangue() {
        return CodeLangue.fromValue(getEmployeurCSLangue());
    }

    /**
     * @return l'id tiers de l'employeur
     */
    public String getEmployeurIdTiers() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur (" + getIdEmployeur() + ") du d�compte " + getId()
                    + " est null !");
        }

        return employeur.getIdTiers();
    }

    /**
     * @return le num�ro d'affili� de l'employeur
     */
    public String getEmployeurAffilieNumero() {
        if (employeur == null) {
            throw new NullPointerException("L'employeur (" + getIdEmployeur() + ") du d�compte " + getId()
                    + " est null !");
        }
        return employeur.getAffilieNumero();
    }

    /**
     * Set de l'adresse principale de l'employeur
     */
    public void setAdressePrincipale(Adresse adresse) {
        if (employeur == null) {
            throw new NullPointerException("L'employeur (" + getIdEmployeur() + ") du d�compte " + getId()
                    + " est null !");
        }
        employeur.setAdressePrincipale(adresse);
    }

    /**
     * Retourne l'adresse principale format�e pour les ent�tes de lettres, soit :
     * <ul>
     * <li>Raison sociale
     * <li>Rue
     * <li>NPA - Localite
     * 
     * 
     * @return String repr�sentant l'adresse principale formatt�e
     */
    public String getAdressePrincipaleFormatte() {
        return employeur.getAdressePrincipaleFormatee();
    }

    /**
     * Retourne toutes les lignes de l'adresse principale format�e
     * 
     * @return String repr�sentant l'adresse principale formatt�e
     */
    public String getFullAdressePrincipaleFormatte() {
        return employeur.getAdressePrincipale().getAdresseFormatte();
    }

    /**
     * Retourne le titre de l'employeur sous forme de code syst�me.
     * 
     * @return String repr�sentant le titre de l'employeur
     */
    public String getTitreEmployeur() {
        return employeur.getTitreTiers();
    }

    /**
     * Retourne la poltiesse sp�cifique � l'employeur.
     * 
     * @return String repr�sentant la politesse sp�cifique � l'employeur ou null si inexistante
     */
    public String getPolitesseSpecEmployeur(CodeLangue codeLangue) {
        return employeur.getPolitesseSpec(codeLangue);
    }

    /**
     * Retourne la p�riode de d�but formatt� avec le mois en toute lettre suivi de l'ann�e.
     * Par ex : mars 2014, janvier 2014
     * 
     * @param locale Locale correspondant � la langue dans laquelle le mois doit �tre retourn�e
     * @return String repr�sentant la p�riode formatt�e, si la p�riode est inexistant, une cha�ne vide est retourn�e.
     */
    public String getPeriodeMoisAnnee(Locale locale) {
        if (periode == null) {
            return "";
        }
        return periode.getPeriodeDebutMoisAnnee(locale);
    }

    /**
     * Retourne si l'attribut est isBVR, soit que l'impression ressort uniquement le BVR et non pas le d�compte.
     * 
     * @return true si l'employeur dispose de l'attribut, false si null ou ne dispose pas de l'attribut
     */
    public boolean isBVR() {
        if (employeur == null) {
            return false;
        }
        return employeur.isBvr();
    }

    public boolean isControleAC2() {
        return controleAC2;
    }

    public void setControleAC2(boolean controleAC2) {
        this.controleAC2 = controleAC2;
    }

    public boolean getControleAC2() {
        return controleAC2;
    }

    /**
     * Retourne si le d�compte est comptabilis�e
     * 
     * @return true si comptabilis�
     */
    public boolean isComptabilise() {
        return EtatDecompte.COMPTABILISE.equals(etat);
    }

    /**
     * Retourne le montant AC d'un d�compte par rapport � un d�compte salaire.
     * Cette m�thode retournera le montant tous les d�comptes salaires qui poss�dent le m�me poste de travail que celui
     * pass� en
     * param�tre et pour la m�me ann�e.
     * Le d�compte salaire pass�e en param�tre ne sera pas inclus dans le calcul.
     * 
     * @param decompteSalaire D�compte salaire de r�f�rence
     */
    public Montant getMontantAC(DecompteSalaire decompteSalaireReference) {
        Montant montantAC = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : lignes) {
            if (!decompteSalaire.getId().equals(decompteSalaireReference.getId())
                    && decompteSalaire.getAnnee().equals(getAnnee())
                    && decompteSalaire.getIdPosteTravail().equals(decompteSalaireReference.getIdPosteTravail())) {
                montantAC = montantAC.add(decompteSalaire.getMasseAC());
            }
        }
        return montantAC;
    }

    /**
     * Retourne le montant AC2 d'un d�compte par rapport � un d�compte salaire.
     * Cette m�thode retournera le montant tous les d�comptes salaires qui poss�dent le m�me poste de travail que celui
     * pass� en param�tre et pour la m�me ann�e.
     * Le d�compte salaire pass� en param�tre ne sera pas inclus dans le calcul.
     * 
     * @param decompteSalaire D�compte salaire de r�f�rence
     */
    public Montant getMontantAC2(DecompteSalaire decompteSalaireReference) {
        Montant montantAC2 = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : lignes) {
            if (!decompteSalaire.getId().equals(decompteSalaireReference.getId())
                    && decompteSalaire.getAnnee().equals(getAnnee())) {
                montantAC2 = montantAC2.add(decompteSalaire.getMasseAC2());
            }
        }
        return montantAC2;
    }

    public boolean mustBeFetched() {
        if (spy == null && idDecompte != null) {
            return true;
        } else {
            return false;
        }
    }

    public Convention getConvention() {
        return employeur.getConvention();
    }

    // On affiche le mois courant si la p�riode n'est que d'un mois, dans le cas contraire, on affiche les deux mois
    public String getDescription(Locale locale) {
        String annee = periode.getAnneeDebut();
        String anneeFin = periode.getAnneeFin();

        String smois = "";
        if (periode.isLongerThanOneMonth()) {
            StringBuilder mois = new StringBuilder();
            mois.append(upperCaseFirstLetter(Date.getMonthName(Integer.valueOf(periode.getMoisDebut()), locale)));
            if (!annee.equals(anneeFin)) {
                mois.append(" ");
                mois.append(annee);
            }
            mois.append(" - ");
            mois.append(upperCaseFirstLetter(Date.getMonthName(Integer.valueOf(periode.getMoisFin()), locale)));

            smois = mois.toString();
        } else {
            smois = upperCaseFirstLetter(Date.getMonthName(Integer.valueOf(periode.getMoisDebut()), locale));
        }

        smois = firstLetterUpperCase(smois);

        return smois + " " + anneeFin;
    }

    private String upperCaseFirstLetter(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private String firstLetterUpperCase(String smois) {
        smois = smois.substring(0, 1).toUpperCase() + smois.substring(1);
        return smois;
    }

    /**
     * @return l'id type section correspondant au d�compte
     */
    public TypeSection getTypeSection() {
        if (isReprise()) {
            return TypeSection.DECOMPTE_COTISATION;
        }
        if (isTraiterAsSpecial() || TypeDecompte.CONTROLE_EMPLOYEUR.equals(getType())
                || EtatDecompte.TAXATION_DOFFICE.equals(getEtat())) {
            return TypeSection.DECOMPTE_COTISATION;
        } else {
            return TypeSection.BULLETIN_NEUTRE;
        }
    }

    /** Retourne si c'est un d�compte de reprise. */
    public boolean isReprise() {
        return numeroDecompte.getValue().startsWith("0");
    }

    /**
     * Retourne le num�ro de la section. Dans le cas d'un d�compte de reprise (qui commence par 0, on supprime 2 z�ros)
     * 
     * @param decompte D�compte sur lequel d�terminer le num�ro de section
     * @return String repr�sentant un num�ro de section
     */
    public String getNumeroSection() {
        if (isReprise()) {
            return getNumeroDecompte().getValue().substring(2);
        } else {
            return getNumeroDecompte().getValue();
        }
    }

    public TaxationOffice getTaxationOfficeModel() {
        return taxationOfficeModel;
    }

    public void setTaxationOfficeModel(TaxationOffice taxationOfficeModel) {
        this.taxationOfficeModel = taxationOfficeModel;
    }

    public boolean isSansSalaire() {
        for (DecompteSalaire ligneSalaire : getLignes()) {
            if (!ligneSalaire.getSalaireTotal().isZero()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the provenance
     */
    public TypeProvenance getTypeProvenance() {
        return typeProvenance;
    }

    /**
     * @param provenance the provenance to set
     */
    public void setTypeProvenance(TypeProvenance provenance) {
        typeProvenance = provenance;
    }

    /**
     * Retourne le nombre de lignes de l'adresse
     * 
     * @return nombre de lignes
     */
    public int getNbLignesAdresse() {
        return employeur.getAdressePrincipale().nbLignes();
    }

    public void calculerAndSetControleAC2() {
        if (periode.isPeriodeFinDecembre()) {
            controleAC2 = true;
        } else if (isComplementaire()) {
            controleAC2 = true;
        } else {
            if (!JadeStringUtil.isBlankOrZero(getEmployeur().getDateFin())) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    java.util.Date dateFin = formatter.parse(getEmployeur().getDateFin());
                    if (dateFin.getMonth() + 1 == new Integer(periode.getMoisFin())) {
                        controleAC2 = true;
                    }
                } catch (Exception e) {
                    controleAC2 = false;
                }
            }
        }
    }

    public boolean isEmployeurEBusiness() {
        return employeur.isEBusiness();
    }

    public boolean isEBusiness() {
        return TypeProvenance.EBUSINESS.equals(typeProvenance);
    }
}