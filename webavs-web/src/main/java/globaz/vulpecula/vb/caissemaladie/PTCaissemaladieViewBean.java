package globaz.vulpecula.vb.caissemaladie;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.caissemaladie.AffiliationCaisseMaladieService;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.domain.models.caissemaladie.TypeDocumentCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.web.gson.SuiviDocumentCaisseMaladieGSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PTCaissemaladieViewBean extends BJadePersistentObjectViewBean {
    List<SuiviCaisseMaladie> listeSuivis = new ArrayList<SuiviCaisseMaladie>();
    List<SuiviDocumentCaisseMaladieGSON> listeSuiviGSON = new ArrayList<SuiviDocumentCaisseMaladieGSON>();
    List<SuiviCaisseMaladie> listeSuivisForPersistance = new ArrayList<SuiviCaisseMaladie>();

    private AffiliationCaisseMaladie affiliationCaisseMaladie = new AffiliationCaisseMaladie();

    private AffiliationCaisseMaladieRepository affiliationCaisseMaladieRepository = VulpeculaRepositoryLocator
            .getAffiliationCaisseMaladieRepository();
    private AffiliationCaisseMaladieService affiliationCaisseMaladieService = VulpeculaServiceLocator
            .getAffiliationCaisseMaladieService();

    /**
     * Variable permettant de stocker le résultat du widget
     */
    private String tempNomCaisseMaladie;
    private String suiviDocumentString;

    private String idPosteTravail;

    public void setTempNomCaisseMaladie(String tempNomCaisseMaladie) {
        this.tempNomCaisseMaladie = tempNomCaisseMaladie;
    }

    @Override
    public void add() throws Exception {
        try {
            ajoutSuivi();
            affiliationCaisseMaladie.setIdPosteTravail(getIdPosteTravail());
            affiliationCaisseMaladieService.create(affiliationCaisseMaladie);
        } catch (UnsatisfiedSpecificationException ex) {
            throw new ViewException(ex);
        }
    }

    @Override
    public void delete() throws Exception {
        affiliationCaisseMaladieService.delete(affiliationCaisseMaladie);
    }

    @Override
    public String getId() {
        return affiliationCaisseMaladie.getId();
    }

    @Override
    public void retrieve() throws Exception {
        affiliationCaisseMaladie = affiliationCaisseMaladieRepository.findById(getId());
        setIdPosteTravail(affiliationCaisseMaladie.getIdPosteTravail());
        // On va rechercher les documents de caisse maladie
        listeSuivis = VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().findByIdTravailleurAndCaisseMaladie(
                affiliationCaisseMaladie.getIdTravailleur(), affiliationCaisseMaladie.getIdCaisseMaladie());
        for (SuiviCaisseMaladie suivi : listeSuivis) {
            SuiviDocumentCaisseMaladieGSON suiviGSON = new SuiviDocumentCaisseMaladieGSON();
            suiviGSON.setTypeDocument(suivi.getTypeDocument().getValue());
            if (suivi.isEnvoye()) {
                suiviGSON.setEnvoye("true");
            } else {
                suiviGSON.setEnvoye("false");
            }
            suiviGSON.setId(suivi.getId());
            suiviGSON.setDateEnvoi(suivi.getDateEnvoi().getSwissValue());
            listeSuiviGSON.add(suiviGSON);
        }
    }

    @Override
    public void setId(String id) {
        affiliationCaisseMaladie.setId(id);
    }

    @Override
    public void update() throws Exception {
        try {
            ajoutSuivi();
            affiliationCaisseMaladie.setIdPosteTravail(getIdPosteTravail());
            affiliationCaisseMaladieService.update(affiliationCaisseMaladie);
        } catch (UnsatisfiedSpecificationException ex) {
            affiliationCaisseMaladie.getSuiviDocument().clear();
            throw new ViewException(ex);
        }
    }

    private void ajoutSuivi() throws Exception {
        Gson gson = new Gson();
        listeSuivisForPersistance.clear();
        List<SuiviDocumentCaisseMaladieGSON> suiviGson = gson.fromJson(getSuiviDocumentString(),
                new TypeToken<List<SuiviDocumentCaisseMaladieGSON>>() {
                }.getType());

        for (SuiviDocumentCaisseMaladieGSON suivi : suiviGson) {
            SuiviCaisseMaladie s = new SuiviCaisseMaladie();
            s.setTravailleur(affiliationCaisseMaladie.getTravailleur());
            s.setCaisseMaladie(affiliationCaisseMaladie.getCaisseMaladie());
            s.setId(suivi.getId());
            s.setTypeDocument(TypeDocumentCaisseMaladie.fromValue(suivi.getTypeDocument()));
            if (suivi.getEnvoye().equals("true")) {
                s.setEnvoye(true);
            } else {
                s.setEnvoye(false);
            }
            s.setDateEnvoi(new Date(suivi.getDateEnvoi()));
            listeSuivisForPersistance.add(s);
        }
        affiliationCaisseMaladie.setSuiviDocument(listeSuivisForPersistance);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(affiliationCaisseMaladie.getSpy());
    }

    public String getIdCaisseMaladie() {
        if (isCaisseMaladieNull()) {
            return null;
        }
        return affiliationCaisseMaladie.getCaisseMaladie().getIdTiers();
    }

    public void setIdCaisseMaladie(String idCaisseMaladie) {
        Administration caisseMaladie = new Administration();
        caisseMaladie.setId(idCaisseMaladie);
        affiliationCaisseMaladie.setCaisseMaladie(caisseMaladie);
    }

    public void setMoisDebut(String moisDebut) {
        if (!JadeStringUtil.isEmpty(moisDebut)) {
            affiliationCaisseMaladie.setMoisDebut(new Date(moisDebut));
        }
    }

    public String getDateDebutAnnonce() {
        if (affiliationCaisseMaladie.getDateAnnonceDebut() != null) {
            return affiliationCaisseMaladie.getDateAnnonceDebut().getSwissValue();
        }
        return "";
    }

    public String getDateFinAnnonce() {
        if (affiliationCaisseMaladie.getDateAnnonceFin() != null) {
            return affiliationCaisseMaladie.getDateAnnonceFin().getSwissValue();
        }
        return "";
    }

    public void setMoisFin(String moisFin) {
        if (!JadeStringUtil.isEmpty(moisFin)) {
            affiliationCaisseMaladie.setMoisFin(new Date(moisFin));
        } else {
            affiliationCaisseMaladie.setMoisFin(null);
        }
    }

    public void setTravailleur(Travailleur travailleur) {
        affiliationCaisseMaladie.setTravailleur(travailleur);
    }

    public Travailleur getTravailleur() {
        return affiliationCaisseMaladie.getTravailleur();
    }

    public String getDateAnnonceDebutAsSwissValue() {
        if (affiliationCaisseMaladie.getDateAnnonceDebut() == null) {
            return null;
        }
        return affiliationCaisseMaladie.getDateAnnonceDebut().getSwissValue();
    }

    public String getDateAnnonceFinAsSwissValue() {
        if (affiliationCaisseMaladie.getDateAnnonceFin() == null) {
            return null;
        }
        return affiliationCaisseMaladie.getDateAnnonceFin().getSwissValue();
    }

    public String getMoisDebut() {
        return affiliationCaisseMaladie.getMoisAnneeDebut();
    }

    public String getMoisFin() {
        return affiliationCaisseMaladie.getMoisAnneeFin();
    }

    public String getMoisDebutFormatte() {
        return affiliationCaisseMaladie.getMoisAnneeDebutFormatte();
    }

    public String getMoisFinFormatte() {
        return affiliationCaisseMaladie.getMoisAnneeFinFormatte();
    }

    public String getLibelleCaisseMaladie() {
        if (isCaisseMaladieNull()) {
            return null;
        }
        // Si la désignation de la caisse maladie n'est pas existante, c'est que l'on est en cours de création. Prendre
        // dans ce cas le libelle fourni par le widget.
        if (JadeStringUtil.isEmpty(affiliationCaisseMaladie.getCaisseMaladie().getDesignation1())) {
            if (JadeStringUtil.isEmpty(tempNomCaisseMaladie) && JadeNumericUtil.isEmptyOrZero(getIdCaisseMaladie())) {
                Administration adm = VulpeculaRepositoryLocator.getAdministrationRepository().findById(
                        getIdCaisseMaladie());
                String designation = adm.getDesignation1();
                if (!JadeStringUtil.isEmpty(adm.getDesignation2())) {
                    designation += " " + adm.getDesignation2();
                }
                return designation;
            }
            return tempNomCaisseMaladie;
        } else {
            String designation = affiliationCaisseMaladie.getCaisseMaladie().getDesignation1();
            if (!JadeStringUtil.isEmpty(affiliationCaisseMaladie.getCaisseMaladie().getDesignation2())) {
                designation += " " + affiliationCaisseMaladie.getCaisseMaladie().getDesignation2();
            }
            return designation;
        }
    }

    private boolean isCaisseMaladieNull() {
        return affiliationCaisseMaladie.getCaisseMaladie() == null;
    }

    public String getIdTravailleur() {
        return affiliationCaisseMaladie.getIdTravailleur();
    }

    public boolean isModifiable() {
        return affiliationCaisseMaladie.isModifiable();
    }

    public boolean isSupprimable() {
        return affiliationCaisseMaladie.isSupprimable();
    }

    public boolean isNouveau() {
        return !JadeNumericUtil.isEmptyOrZero(affiliationCaisseMaladie.getId());
    }

    public String getListeSuiviGSON() {
        Gson gson = new Gson();
        return gson.toJson(listeSuiviGSON);
    }

    public String getSuiviDocumentString() {
        return suiviDocumentString;
    }

    public void setSuiviDocumentString(String suiviDocumentString) {
        this.suiviDocumentString = suiviDocumentString;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    /**
     * Retourne la liste des postes actifs possibles auxquels l'absence justifiée peut être rattaché pour le travailleur
     * actuellement sélectionné.
     * 
     * @return Liste de postes de travail
     */
    public List<PosteTravail> getPosteTravails() {
        // On va rechercher les postes de travail
        List<PosteTravail> postes = VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdTravailleur(
                getTravailleur().getId());
        return postes;
    }
}
