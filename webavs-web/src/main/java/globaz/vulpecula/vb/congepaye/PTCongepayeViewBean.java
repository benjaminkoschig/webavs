package globaz.vulpecula.vb.congepaye;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.JAXBException;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.compteur.CompteurServiceImpl;
import ch.globaz.vulpecula.businessimpl.services.prestations.PrestationStatus;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.TauxCongePaye;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.web.gson.CotisationsGSON;
import ch.globaz.vulpecula.web.views.prestations.PrestationsViewService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PTCongepayeViewBean extends BJadePersistentObjectViewBean {
    private Travailleur travailleur;
    private CongePaye congePaye;

    private List<PosteTravail> posteTravailsPossibles;

    private String ajoutSucces = "";

    private PrestationStatus prestationStatus = new PrestationStatus();

    public String getAjoutSucces() {
        return ajoutSucces;
    }

    public void setAjoutSucces(String ajoutSucces) {
        this.ajoutSucces = ajoutSucces;
    }

    public List<PosteTravail> getPosteTravailsPossibles() {
        return posteTravailsPossibles;
    }

    public void setPosteTravailsPossibles(List<PosteTravail> posteTravailsPossibles) {
        this.posteTravailsPossibles = posteTravailsPossibles;
    }

    /**
     * Retourne les codes systèmes relatifs au groupe de codes systèmes bénéficiaire.
     * 
     * @return Liste de codes systèmes
     */
    public List<CodeSystem> getBeneficiaires() {
        return PrestationsViewService.getBeneficiairesForCP();
    }

    public PTCongepayeViewBean() {
        congePaye = new CongePaye();
    }

    @Override
    public void add() throws Exception {
        try {
            VulpeculaServiceLocator.getCongePayeService().create(congePaye);
        } catch (UnsatisfiedSpecificationException ex) {
            throw new ViewException(ex);
        }
    }

    @Override
    public void delete() throws Exception {
        VulpeculaRepositoryLocator.getCongePayeRepository().deleteById(getId());
    }

    @Override
    public String getId() {
        return congePaye.getId();
    }

    /**
     * Retourne si l'absence justifiée est une création.
     * 
     * @return true si id est vide
     */
    public boolean isNouveau() {
        return congePaye.getId() == null;
    }

    /**
     * Détermine si on doit ajouter les cotisations du poste de travail
     * 
     * @return wantCotisations
     * @throws URISyntaxException
     * @throws JAXBException
     */
    public boolean isCotisations() throws JAXBException, URISyntaxException {
        return VulpeculaServiceLocator.getCongePayeService().tenirCompteDesCotisations(congePaye.getIdPosteTravail());
    }

    @Override
    public void retrieve() throws Exception {
        congePaye = VulpeculaRepositoryLocator.getCongePayeRepository().findByIdWithDependancies(getId());
        setTravailleur(congePaye.getPosteTravail().getTravailleur());
    }

    @Override
    public void setId(String newId) {
        congePaye.setId(newId);
    }

    @Override
    public void update() throws Exception {
        VulpeculaRepositoryLocator.getCongePayeRepository().update(congePaye);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(congePaye.getSpy());
    }

    /**
     * @return the travailleur
     */
    public Travailleur getTravailleur() {
        return travailleur;
    }

    /**
     * @param travailleur the travailleur to set
     */
    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    /**
     * @return the congePaye
     */
    public CongePaye getCongePaye() {
        return congePaye;
    }

    /**
     * @param congePaye the congePaye to set
     */
    public void setCongePaye(CongePaye congePaye) {
        this.congePaye = congePaye;
    }

    /**
     * Assigne le passage de facturation à l'absence justifiée.
     * 
     * @param passage Passage de facturation à assigner
     */
    public void setPassage(Passage passage) {
        congePaye.setPassage(passage);
    }

    /**
     * Setter de l'année pour utiliser le mechanisme du FW.
     */
    public void setAnneeDebut(String anneeDebut) {
        congePaye.setAnneeDebut(new Annee(anneeDebut));
    }

    /**
     * Getter de l'année pour initialiser à l'année courante, si vide dans congé payé
     */
    public String getAnneeDebut() {
        if (congePaye.getAnneeDebut() != null) {
            return String.valueOf(congePaye.getAnneeDebut().getValue());
        } else {
            return getCurrentYearAsString();
        }
    }

    /**
     * Setter de l'année pour utiliser le mechanisme du FW.
     */
    public void setAnneeFin(String anneeFin) {
        congePaye.setAnneeFin(new Annee(anneeFin));
    }

    /**
     * Getter de l'année pour initialiser à l'année courante, si vide dans congé payé
     */
    public String getAnneeFin() {
        if (congePaye.getAnneeFin() != null) {
            return String.valueOf(congePaye.getAnneeFin().getValue());
        } else {
            return getCurrentYearAsString();
        }
    }

    private String getCurrentYearAsString() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    /**
     * Méthode utilisée par le framework pour setter le bénéficiaire.
     * 
     * @param beneficiaire String représentant un code système
     */
    public void setBeneficiaire(String beneficiaire) {
        congePaye.setBeneficiaire(Beneficiaire.fromValue(beneficiaire));
    }

    /**
     * Méthode utilisée par le framework pour setter le poste de travail.
     * 
     * @return String représentant le poste de travail
     */
    public void setIdPosteTravail(String idPosteTravail) {
        PosteTravail poste = new PosteTravail();
        poste.setId(idPosteTravail);
        congePaye.setPosteTravail(poste);
    }

    /**
     * Retourne le nom de la classe du compteur service
     * 
     * @return String représentant le nom de la classe du service
     */
    public String getCompteurService() {
        return CompteurServiceImpl.class.getName();
    }

    /**
     * Retourne le nom de la classe contenant le service pour les prestations
     * 
     * @return String représentant le nom de la classe du service
     */
    public String getPrestationsViewService() {
        return PrestationsViewService.class.getName();
    }

    public String getDateSalaireNonDeclare() {
        return congePaye.getDateSalaireNonDeclare().getValue();
    }

    public void setDateSalaireNonDeclare(String dateSalaireNonDeclare) {
        if (dateSalaireNonDeclare != null && dateSalaireNonDeclare.length() > 0) {
            congePaye.setDateSalaireNonDeclare(new Date(dateSalaireNonDeclare));
        }
    }

    public String getTotalSalaire() {
        if (congePaye.getTotalSalaire() != null) {
            return congePaye.getTotalSalaire().getValue();
        } else {
            return "0";
        }

    }

    public String getTauxCP() {
        return congePaye.getTauxCP().getValueWith(2);
    }

    public void setTauxCP(String tauxCP) {
        congePaye.setTauxCP(new Taux(tauxCP));
    }

    public String getMontantNet() {
        return congePaye.getMontantNet().getValue();
    }

    public void setMontantNet(String montantNet) {
        congePaye.setMontantNet(new Montant(montantNet));
    }

    public String getSalaireDeclare() {
        return congePaye.getSalaireDeclare().getValue();
    }

    public void setSalaireDeclare(String salaireDeclare) {
        congePaye.setSalaireDeclare(new Montant(salaireDeclare));
    }

    public String getSalaireNonDeclare() {
        return congePaye.getSalaireNonDeclare().getValue();
    }

    public void setSalaireNonDeclare(String salaireNonDeclare) {
        congePaye.setSalaireNonDeclare(new Montant(salaireNonDeclare));
    }

    public List<CotisationsGSON> getListeCotisations() {
        List<CotisationsGSON> listeCotisations = new ArrayList<CotisationsGSON>();
        Taux total = Taux.ZERO();
        for (TauxCongePaye taux : congePaye.getTauxCongePayes()) {
            CotisationsGSON coti = new CotisationsGSON(taux.getIdAssurance(), taux.getTaux().getValue(),
                    taux.getAssuranceLibelle(I18NUtil.getUserLocale()), taux.getTypeAssurance());
            total = total.addTaux(taux.getTaux());
            listeCotisations.add(coti);
        }
        String libelleTotal = (((BSession) getISession()).getLabel("TOTAL"));
        String tauxTotal = total.getValueWith(2);
        CotisationsGSON coti = new CotisationsGSON(null, tauxTotal, libelleTotal, null);
        listeCotisations.add(coti);
        return listeCotisations;
    }

    public void setListeCotisations(String listeCotisations) {
        Gson gson = new Gson();

        List<CotisationsGSON> cotisations = gson.fromJson(listeCotisations, new TypeToken<List<CotisationsGSON>>() {
        }.getType());

        List<TauxCongePaye> liste = new ArrayList<TauxCongePaye>();
        if (cotisations != null) {
            for (CotisationsGSON cotisationsGSON : cotisations) {
                TauxCongePaye taux = new TauxCongePaye();
                Assurance assurance = new Assurance();
                assurance.setId(cotisationsGSON.getIdAssurance());
                taux.setTaux(new Taux(cotisationsGSON.getTaux()));
                taux.setIdCongePaye(congePaye.getIdCongePaye());
                taux.setAssurance(assurance);
                liste.add(taux);
            }
        }
        congePaye.setTauxCongePayes(liste);
    }

    public String getMessageEmptyNSS() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.EMPTY_NSS);
    }

    public String getMessageAnneeDebutNonVide() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.CP_ANNEE_DEBUT_NON_VIDE);
    }

    public String getMessageAnneeFinNonVide() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.CP_ANNEE_FIN_NON_VIDE);
    }

    public String getMessageAnneeFinPlusGrandAnneeDebut() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.CP_ANNEE_FIN_PLUS_GRAND_ANNEE_DEBUT);
    }

    public String getMessageDateRequise() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.CP_SALAIRE_NON_DECLARE_DATE_REQUISE);
    }

    public String getMessageAucuneCaisseMetier() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.CP_AUCUNE_CAISSE_METIER);
    }

    public String getMessageAucunCompteur() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.CP_AUCUN_COMPTEUR);
    }

    /**
     * 
     * @return
     */
    public boolean isMustSubstractCotisations() {
        return true;
    }

    public Locale getLocale() {
        return I18NUtil.getUserLocale();
    }

    public boolean isModifiable() {
        return prestationStatus.isModifiable(congePaye);
    }
}
