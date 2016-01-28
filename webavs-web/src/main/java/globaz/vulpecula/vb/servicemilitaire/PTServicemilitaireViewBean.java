package globaz.vulpecula.vb.servicemilitaire;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.prestations.PrestationStatus;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.servicemilitaire.GenreSM;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.models.servicemilitaire.TauxServiceMilitaire;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.web.gson.TauxPrestationGSON;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import ch.globaz.vulpecula.web.util.DateViewService;
import ch.globaz.vulpecula.web.views.decomptesalaire.DecompteSalaireViewService;
import ch.globaz.vulpecula.web.views.prestations.PrestationsViewService;
import com.google.gson.Gson;

public class PTServicemilitaireViewBean extends BJadePersistentObjectViewBean {
    private ServiceMilitaire servicemilitaire;
    private Travailleur travailleur;

    private PrestationStatus prestationStatus = new PrestationStatus();

    private Date dateDebutAsString;
    private Date dateFinAsString;

    private String ajoutSucces = "";

    public String getAjoutSucces() {
        return ajoutSucces;
    }

    public void setAjoutSucces(String ajoutSucces) {
        this.ajoutSucces = ajoutSucces;
    }

    public PTServicemilitaireViewBean() {
        servicemilitaire = new ServiceMilitaire();
    }

    private List<PosteTravail> posteTravailsPossibles;

    public List<PosteTravail> getPosteTravailsPossibles() {
        return posteTravailsPossibles;
    }

    public void setPosteTravailsPossibles(List<PosteTravail> posteTravailsPossibles) {
        this.posteTravailsPossibles = posteTravailsPossibles;
    }

    /**
     * Retourne les codes syst�mes relatifs au groupe de codes syst�mes b�n�ficiaire.
     * 
     * @return Liste de codes syst�mes
     */
    public List<CodeSystem> getBeneficiaires() {
        return PrestationsViewService.getBeneficiairesForSM();
    }

    public List<CodeSystem> getGenrePrestations() {
        return CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_GENRE_PRESTATIONS_SERVICE_MILITAIRE);
    }

    @Override
    public void add() throws Exception {
        VulpeculaServiceLocator.getServiceMilitaireService().create(servicemilitaire);
    }

    @Override
    public void delete() throws Exception {
        VulpeculaRepositoryLocator.getServiceMilitaireRepository().delete(servicemilitaire);
    }

    @Override
    public String getId() {
        return servicemilitaire.getId();
    }

    @Override
    public void retrieve() throws Exception {
        servicemilitaire = VulpeculaRepositoryLocator.getServiceMilitaireRepository().findById(getId());
        setTravailleur(servicemilitaire.getPosteTravail().getTravailleur());
    }

    @Override
    public void setId(String newId) {
        servicemilitaire.setId(newId);
    }

    @Override
    public void update() throws Exception {
        VulpeculaRepositoryLocator.getServiceMilitaireRepository().update(servicemilitaire);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(servicemilitaire.getSpy());
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    /**
     * Retourne si le service militaire est une cr�ation.
     * 
     * @return true si id est vide
     */
    public boolean isNouveau() {
        return servicemilitaire.getId() == null;
    }

    /**
     * M�thode utilis�e par le framework pour mettre le nombre de jours dans l'�cran
     */
    public String getNombreDeJours() {
        return String.valueOf(servicemilitaire.getNbJours());
    }

    /**
     * M�thode utilis�e par le framework pour mettre le nombre d'heures par jours � l'�cran.
     * 
     * @return String repr�sentant le nombre d'heures par jour
     */
    public String getNombreHeuresParJour() {
        return String.valueOf(servicemilitaire.getNbHeuresParJour());
    }

    public ServiceMilitaire getServicemilitaire() {
        return servicemilitaire;
    }

    /**
     * M�thode utilis�e par le framework pour setter le nombre de jours.
     * 
     * @param nbJours Nombre de jours
     */
    public void setNombreDeJours(String nbJours) {
        servicemilitaire.setNbJours(Double.parseDouble(nbJours));
    }

    /**
     * M�thode utilis�e par le framework pour setter le nombre d'heures par jour.
     * 
     * @param nbHeuresParJour Nombre d'heures par jour
     */
    public void setNombreHeuresParJour(String nbHeuresParJour) {
        servicemilitaire.setNbHeuresParJour(Double.valueOf(nbHeuresParJour));
    }

    /**
     * Retourne le nom de la classe du d�compte salaire utilis� pour effectuer des appels AJAX depuis la page JSP
     * associ�e.
     * 
     * @return String repr�sentant le nom de la classe du service
     */
    public String getDecompteSalaireViewService() {
        return DecompteSalaireViewService.class.getName();
    }

    /**
     * Retourne la classe de service pour les prestations.
     * 
     * @return String repr�sentant la classe d'impl�mentation
     */
    public String getPrestationsViewService() {
        return PrestationsViewService.class.getName();
    }

    /**
     * Retourne la classe de service pour les utilitaires de date.
     * 
     * @return String repr�sentant la classe d'impl�mentation
     */
    public String getDateViewService() {
        return DateViewService.class.getName();
    }

    /**
     * M�thode utilis�e par le framework pour setter la date de d�but d'absence.
     * 
     * @param debutAbsence
     */
    public void setDebutAbsence(String debutAbsence) {
        if (Date.isValid(debutAbsence)) {
            dateDebutAsString = new Date(debutAbsence);
        }
        setPeriode();
    }

    /**
     * M�thode utilis�e par le framework pour setter l'id du passage de facturation.
     * 
     * @param idPassageFacturation Id du passage de facturation
     */
    public void setIdPassageFacturation(String idPassageFacturation) {
        servicemilitaire.setIdPassageFacturation(idPassageFacturation);
    }

    /**
     * M�thode utilis�e par le framework pour setter la date de fin d'absence.
     * 
     * @param debutAbsence
     */
    public void setFinAbsence(String finAbsence) {
        if (Date.isValid(finAbsence)) {
            dateFinAsString = new Date(finAbsence);
        }
        setPeriode();
    }

    private void setPeriode() {
        if (dateDebutAsString != null) {
            servicemilitaire.setPeriode(new Periode(dateDebutAsString, dateFinAsString));
        }
    }

    /**
     * M�thode utilis�e par le framework pour setter le genre de prestations.
     * 
     * @param csCode String repr�sentant un code syst�me pour le genre de prestations
     */
    public void setGenrePrestations(String csCode) {
        servicemilitaire.setGenre(GenreSM.fromValue(csCode));
    }

    /**
     * M�thode utilis�e par le framework pour setter le champ base salaire
     * 
     * @param baseSalaire String repr�sentant un code syst�me pour le champ base salaire
     */
    public void setBaseSalaire(String baseSalaire) {
        servicemilitaire.setBaseSalaire(new Montant(baseSalaire));
    }

    /**
     * M�thode utilis�e par le framework pour setter le taux de CP
     * 
     * @param csCode String repr�sentant un code syst�me pour le taux de CP
     */
    public void setTauxCP(String tauxCP) {
        servicemilitaire.setTauxCP(new Taux(tauxCP));
    }

    /**
     * M�thode utilis�e par le framework pour setter le taux de gratification
     * 
     * @param csCode String repr�sentant un code syst�me pour le taux de gratification
     */
    public void setTauxGratification(String tauxGratification) {
        servicemilitaire.setTauxGratification(new Taux(tauxGratification));
    }

    /**
     * M�thode utilis�e par le framework pour setter le poste de travail.
     * 
     * @return String repr�sentant le poste de travail
     */
    public void setIdPosteTravail(String idPosteTravail) {
        PosteTravail poste = new PosteTravail();
        poste.setId(idPosteTravail);
        servicemilitaire.setPosteTravail(poste);
    }

    /**
     * M�thode utilis�e par le framework pour setter le montant � verser.
     * 
     * @param montantVerse String repr�sentant un montant
     */
    public void setMontantVerse(String montantAVerser) {
        servicemilitaire.setMontantAVerser(new Montant(montantAVerser));
    }

    /**
     * M�thode utilis�e par le framework pour setter le montant brut.
     * 
     * @param montantBrut String repr�sentant un montant
     */
    public void setMontantBrut(String montantBrut) {
        servicemilitaire.setMontantBrut(new Montant(montantBrut));
    }

    /**
     * M�thode utilis�e par le framework pour setter le b�n�ficiaire.
     * 
     * @param beneficiaire String repr�sentant un code syst�me B�n�ficiaire
     */
    public void setBeneficiaire(String beneficiaire) {
        servicemilitaire.setBeneficiaire(Beneficiaire.fromValue(beneficiaire));
    }

    /** M�thode utilis� par le helper pour setter le passage de facturation */
    public void setPassage(Passage passage) {
        servicemilitaire.setPassage(passage);
    }

    /**
     * M�thode utilis�e par le framework pour setter la couverture APG
     * 
     * @param couvertureAPG String repr�sentant le taux de couverture APG
     */
    public void setTauxCouvertureAPG(String couvertureAPG) {
        servicemilitaire.setCouvertureAPG(new Taux(couvertureAPG));
    }

    /**
     * M�thode utilis�e par le framework pour setter le versement APG
     * 
     * @param versementAPG String repr�sentant le montant du versement
     */
    public void setVersementAPG(String versementAPG) {
        servicemilitaire.setVersementAPG(new Montant(versementAPG));
    }

    /**
     * M�thode utilis�e par le framework pour setter le montant de la compensation
     * 
     * @param compensationAPG String repr�sentant le montant de la compensation APG
     */
    public void setCompensationAPG(String compensationAPG) {
        servicemilitaire.setCompensationAPG(new Montant(compensationAPG));
    }

    /**
     * M�thode utilis�e par le framework pour setter le salaire horaire
     * 
     * @param salaireHoraire
     */
    public void setSalaireHoraire(String salaireHoraire) {
        servicemilitaire.setSalaireHoraire(new Montant(salaireHoraire));
    }

    /**
     * M�thode utilis�e par le framework pour setter les taux.
     * 
     * @param taux
     */
    public void setTaux(String taux) {
        Gson gson = new Gson();
        List<TauxServiceMilitaire> tauxServiceMilitaires = new ArrayList<TauxServiceMilitaire>();
        List<TauxPrestationGSON> tauxPrestationsGSON = Arrays.asList(gson.fromJson(taux, TauxPrestationGSON[].class));
        for (TauxPrestationGSON tauxPrestationGSON : tauxPrestationsGSON) {
            tauxServiceMilitaires.add(tauxPrestationGSON.convertToDomain());
        }
        servicemilitaire.setTauxServicesMilitaires(tauxServiceMilitaires);
    }

    public String getMessagePeriodeNonVide() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.SM_PERIODE_NON_VIDE);
    }

    public String getMessagePeriodeFinNonSaisie() {
        return SpecificationMessage
                .getMessage(I18NUtil.getUserLocale(), SpecificationMessage.SM_PERIODE_FIN_NON_SAISIE);
    }

    public String getMessagePeriodeDebutPlusGrandePeriodeFin() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.SM_PERIODE_DEBUT_PLUS_GRANDE_PERIODE_FIN);
    }

    public String getMessageAucuneCaisseMetier() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.SM_AUCUNE_CAISSE_METIER);
    }

    public String getMessageNombreJourMinimum() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.SM_NOMBRE_JOURS_MINIMUM);
    }

    public String getMessageNombreJourMaximum() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.SM_NOMBRE_JOURS_MAXIMUM);
    }

    public String getMessageVersementAPG() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.SM_VERSEMENT_APG_NON_SAISIE);
    }

    public String getBeneficiaireTravailleur() {
        return Beneficiaire.TRAVAILLEUR.getValue();
    }

    /**
     * Retourne les taux des services militaires contenus dans le service militaire pour le r�affichage.
     * 
     * @return Liste de taux
     */
    public List<TauxServiceMilitaire> getTauxServiceMilitaire() {
        return servicemilitaire.getTauxServicesMilitaires();
    }

    /**
     * Retourne la locale utilis� pour r�cup�rer le nom de l'assurance en fonction de la langue de l'utiliasteur
     * actuellement connect�.
     * 
     * @return Locale de l'utilisateur connect�
     */
    public Locale getLocale() {
        return I18NUtil.getUserLocale();
    }

    public boolean isModifiable() {
        return prestationStatus.isModifiable(servicemilitaire);
    }
}
