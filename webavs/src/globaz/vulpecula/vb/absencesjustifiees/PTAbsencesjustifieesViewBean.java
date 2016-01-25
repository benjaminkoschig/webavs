package globaz.vulpecula.vb.absencesjustifiees;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.prestations.PrestationStatus;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.absencejustifiee.LienParente;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.web.servlet.PTAbsencesJustifieesAction;
import ch.globaz.vulpecula.web.views.decomptesalaire.DecompteSalaireViewService;
import ch.globaz.vulpecula.web.views.prestations.PrestationsViewService;

public class PTAbsencesjustifieesViewBean extends BJadePersistentObjectViewBean {
    private String ajoutSucces = "";

    public String getAjoutSucces() {
        return ajoutSucces;
    }

    public void setAjoutSucces(String ajoutSucces) {
        this.ajoutSucces = ajoutSucces;
    }

    private Travailleur travailleur;
    private AbsenceJustifiee absenceJustifiee;

    private Date dateDebut;
    private Date dateFin;

    private String idTravailleur;

    private List<PosteTravail> posteTravailsPossibles;

    private List<CodeSystem> genrePrestations;
    private List<CodeSystem> parentes;

    private PrestationStatus prestationStatus = new PrestationStatus();

    public PTAbsencesjustifieesViewBean() {
        absenceJustifiee = new AbsenceJustifiee();
    }

    @Override
    public void add() throws Exception {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                absenceJustifiee.getIdPosteTravail());
        absenceJustifiee.setPosteTravail(posteTravail);
        VulpeculaServiceLocator.getAbsenceJustifieeService().create(absenceJustifiee);
    }

    @Override
    public void delete() throws Exception {
        VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().delete(absenceJustifiee);
    }

    @Override
    public String getId() {
        return absenceJustifiee.getId();
    }

    @Override
    public void retrieve() throws Exception {
        absenceJustifiee = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findById(getId());
        setTravailleur(absenceJustifiee.getPosteTravail().getTravailleur());
    }

    @Override
    public void setId(String id) {
        absenceJustifiee.setId(id);
    }

    @Override
    public void update() throws Exception {
        VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().update(absenceJustifiee);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(absenceJustifiee.getSpy());
    }

    public void setDebutAbsence(String debutAbsence) {
        if (Date.isValid(debutAbsence)) {
            dateDebut = new Date(debutAbsence);
        }
        setPeriode();
    }

    public void setFinAbsence(String finAbsence) {
        if (Date.isValid(finAbsence)) {
            dateFin = new Date(finAbsence);
        }
        setPeriode();
    }

    private void setPeriode() {
        if (dateDebut != null) {
            absenceJustifiee.setPeriode(new Periode(dateDebut, dateFin));
        }
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public AbsenceJustifiee getAbsenceJustifiee() {
        return absenceJustifiee;
    }

    public void setAbsenceJustifiee(AbsenceJustifiee absenceJustifiee) {
        this.absenceJustifiee = absenceJustifiee;
    }

    /**
     * Méthode utilisée par le framework pour setter le genre de prestations.
     * 
     * @param csCode String représentant un code système pour le genre de prestations
     */
    public void setGenrePrestations(String csCode) {
        absenceJustifiee.setType(TypeAbsenceJustifiee.fromValue(csCode));
    }

    /**
     * Méthode utilisée par le framework pour setter le lien de parenté pour le deuil.
     * 
     * @param parente String représentant un code système pour le lien de parenté
     */
    public void setParente(String parente) {
        absenceJustifiee.setLienParente(LienParente.fromValue(parente));
    }

    /**
     * Méthode utilisée par le framework pour setter le poste de travail.
     * 
     * @return String représentant le poste de travail
     */
    public void setIdPosteTravail(String idPosteTravail) {
        PosteTravail poste = new PosteTravail();
        poste.setId(idPosteTravail);
        absenceJustifiee.setPosteTravail(poste);
    }

    /**
     * Méthode utilisée par le framework pour setter le montant à verser.
     * 
     * @param montantVerse String représentant un montant
     */
    public void setMontantVerse(String montantVerse) {
        absenceJustifiee.setMontantVerse(new Montant(montantVerse));
    }

    /**
     * Méthode utilisée par le framework pour setter le montant brut.
     * 
     * @param montantBrut String représentant un montant
     */
    public void setMontantBrut(String montantBrut) {
        absenceJustifiee.setMontantBrut(new Montant(montantBrut));
    }

    /**
     * Méthode utilisée par le framework pour setter le bénéficiaire.
     * 
     * @param beneficiaire String représentant un code système
     */
    public void setBeneficiaire(String beneficiaire) {
        absenceJustifiee.setBeneficiaire(Beneficiaire.fromValue(beneficiaire));
    }

    /**
     * Méthode utilisée par le framework pour mettre le nombre de jours dans l'écran
     */
    public String getNombreDeJours() {
        return String.valueOf(absenceJustifiee.getNombreDeJours());
    }

    /**
     * Méthode utilisée par le framework pour mettre le nombre d'heures par jours à l'écran.
     * 
     * @return String représentant le nombre d'heures par jour
     */
    public String getNombreHeuresParJour() {
        return String.valueOf(absenceJustifiee.getNombreHeuresParJour());
    }

    /**
     * Méthode utilisée par le framework pour setter le nombre de jours.
     * 
     * @param nombreDeJours Nombre de jours
     */
    public void setNombreDeJours(String nombreDeJours) {
        absenceJustifiee.setNombreDeJours(Double.valueOf(nombreDeJours));
    }

    /**
     * Méthode utilisée par le framework pour setter le nombre d'heures par jour.
     * 
     * @param nombreHeuresParjour Nombre d'heures par jour
     */
    public void setNombreHeuresParJour(String nombreHeuresParJour) {
        absenceJustifiee.setNombreHeuresParJour(Double.valueOf(nombreHeuresParJour));
    }

    /**
     * Méthode utilisée par le framework pour setter le taux AVS.
     * 
     * @param tauxAVS Taux AVS
     */
    public void setTauxAVS(String tauxAVS) {
        absenceJustifiee.setTauxAVS(new Taux(tauxAVS));
    }

    /**
     * Méthode utilisée par le framework pour setter le taux AC.
     * 
     * @param tauxAC Taux AC
     */
    public void setTauxAC(String tauxAC) {
        absenceJustifiee.setTauxAC(new Taux(tauxAC));
    }

    /**
     * Méthode utilisée par le framework pour setter le salaire horaire.
     * 
     * @param salaireHoraire String représentant le salaire horaire
     */
    public void setSalaireHoraire(String salaireHoraire) {
        absenceJustifiee.setSalaireHoraire(new Montant(salaireHoraire));
    }

    /**
     * Méthode utilisée par le framework pour setter l'id du passage de facturation.
     * 
     * @param idPassageFacturation Id du passage de facturation
     */
    public void setIdPassageFacturation(String idPassageFacturation) {
        absenceJustifiee.setIdPassageFacturation(idPassageFacturation);
    }

    /**
     * Retourne l'id du travailleur qui a précédemment été stocké dans l'URL.
     * 
     * @return String de l'id travailleur
     */
    public String getIdTravailleur() {
        return idTravailleur;
    }

    /**
     * Utilisé dans le {@link PTAbsencesJustifieesAction#beforeAfficher}, il s'agit d'un paramètre passé dans l'URL.
     * 
     * @param idTravailleur String représentant l'id travailleur
     */
    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    /**
     * Retourne la liste des postes actifs possibles auxquels l'absence justifiée peut être rattaché pour le travailleur
     * actuellement sélectionné.
     * 
     * @return Liste de postes de travail
     */
    public List<PosteTravail> getPosteTravailsPossibles() {
        return posteTravailsPossibles;
    }

    /**
     * Utilisé dans le {@link PTAbsencesjustifieesViewBean#beforeAfficher} afin de setter les postes de travail
     * possibles.
     * 
     * @param posteTravailsPossibles Liste représentant les postes de travail possibles
     */
    public void setPosteTravailsPossibles(List<PosteTravail> posteTravailsPossibles) {
        this.posteTravailsPossibles = posteTravailsPossibles;
    }

    /**
     * Retourne si l'absence justifiée est une création.
     * 
     * @return true si id est vide
     */
    public boolean isNouveau() {
        return absenceJustifiee.getId() == null;
    }

    /**
     * Retourne le nom de la classe du décompte salaire utilisé pour effectuer des appels AJAX depuis la page JSP
     * associée.
     * 
     * @return String représentant le nom de la classe du service
     */
    public String getDecompteSalaireViewService() {
        return DecompteSalaireViewService.class.getName();
    }

    /**
     * Retourne les codes systèmes relatifs au groupe de codes systèmes bénéficiaire.
     * 
     * @return Liste de codes systèmes
     */
    public List<CodeSystem> getBeneficiaires() {
        return PrestationsViewService.getBeneficiairesForAJ();
    }

    /**
     * Assigne le passage de facturation à l'absence justifiée.
     * 
     * @param passage Passage de facturation à assigner
     */
    public void setPassage(Passage passage) {
        absenceJustifiee.setPassage(passage);
    }

    /**
     * Retourne la classe de service pour les prestations.
     * 
     * @return String représentant la classe d'implémentation
     */
    public String getPrestationsViewService() {
        return PrestationsViewService.class.getName();
    }

    public String getCsDeuil() {
        return TypeAbsenceJustifiee.DEUIL.getValue();
    }

    public String getMessagePeriodeNonVide() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.AJ_PERIODE_NON_VIDE);
    }

    public String getMessagePeriodeFinNonSaisie() {
        return SpecificationMessage
                .getMessage(I18NUtil.getUserLocale(), SpecificationMessage.AJ_PERIODE_FIN_NON_SAISIE);
    }

    public String getMessagePeriodeDebutPlusGrandePeriodeFin() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.AJ_PERIODE_DEBUT_PLUS_GRANDE_PERIODE_FIN);
    }

    public List<CodeSystem> getGenrePrestations() {
        return genrePrestations;
    }

    public void setGenrePrestations(List<CodeSystem> genrePrestations) {
        Collections.sort(genrePrestations, new Comparator<CodeSystem>() {
            @Override
            public int compare(CodeSystem o1, CodeSystem o2) {
                return o1.getLibelle().compareTo(o2.getLibelle());
            }
        });
        this.genrePrestations = genrePrestations;
    }

    public List<CodeSystem> getParentes() {
        return parentes;
    }

    public void setParentes(List<CodeSystem> parentes) {
        Collections.sort(parentes, new Comparator<CodeSystem>() {
            @Override
            public int compare(CodeSystem o1, CodeSystem o2) {
                return o1.getLibelle().compareTo(o2.getLibelle());
            }
        });
        this.parentes = parentes;
    }

    public boolean isModifiable() {
        return prestationStatus.isModifiable(absenceJustifiee);
    }
}