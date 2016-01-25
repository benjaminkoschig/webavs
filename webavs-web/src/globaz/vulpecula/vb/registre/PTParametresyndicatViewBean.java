package globaz.vulpecula.vb.registre;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.registre.ParametreSyndicatService;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.domain.repositories.syndicat.ParametreSyndicatRepository;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;

public class PTParametresyndicatViewBean extends BJadePersistentObjectViewBean {
    private final ParametreSyndicatRepository parametreSyndicatRepository = VulpeculaRepositoryLocator
            .getParametreSyndicatRepository();
    private final ParametreSyndicatService parametreSyndicatService = VulpeculaServiceLocator
            .getParametreSyndicatService();

    private ParametreSyndicat parametreSyndicat = new ParametreSyndicat();
    private Adresse adresse;

    private List<Administration> conventions;
    private List<Administration> caissesMetiers;

    @Override
    public void add() throws Exception {
        try {
            parametreSyndicatService.create(parametreSyndicat);
        } catch (UnsatisfiedSpecificationException ex) {
            throw new ViewException(ex);
        }
    }

    @Override
    public void delete() throws Exception {
        parametreSyndicatService.delete(parametreSyndicat);
    }

    @Override
    public String getId() {
        return parametreSyndicat.getId();
    }

    public String getPourcentage() {
        if (parametreSyndicat.getPourcentage() == null) {
            return null;
        }
        return parametreSyndicat.getPourcentage().getValue();
    }

    public String getDesignation() {
        if (parametreSyndicat.getSyndicat() == null) {
            return null;
        }
        return parametreSyndicat.getSyndicat().getDesignation1();
    }

    public String getMontantParTravailleur() {
        if (parametreSyndicat.getMontantParTravailleur() == null) {
            return null;
        }
        return parametreSyndicat.getMontantParTravailleur().getValue();
    }

    public String getAdresse() {
        if (adresse == null) {
            return null;
        }
        return adresse.getAdresseFormatte();
    }

    public void setSyndicat(Administration syndicat) {
        parametreSyndicat.setSyndicat(syndicat);
    }

    public String getIdSyndicat() {
        return parametreSyndicat.getIdSyndicat();
    }

    public List<Administration> getConventions() {
        return conventions;
    }

    public void setConventions(List<Administration> conventions) {
        this.conventions = conventions;
    }

    public List<Administration> getCaissesMetiers() {
        return caissesMetiers;
    }

    public void setCaissesMetiers(List<Administration> caissesMetiers) {
        this.caissesMetiers = caissesMetiers;
    }

    public void setPourcentage(String pourcentage) {
        if (Taux.isValid(pourcentage)) {
            parametreSyndicat.setPourcentage(new Taux(pourcentage));
        } else {
            parametreSyndicat.setPourcentage(new Taux(0));
        }
    }

    public void setMontantParTravailleur(String montantParTravailleur) {
        if (Montant.isValid(montantParTravailleur)) {
            parametreSyndicat.setMontantParTravailleur(new Montant(montantParTravailleur));
        } else {
            parametreSyndicat.setMontantParTravailleur(Montant.ZERO);
        }
    }

    public void setDateDebut(String dateDebut) {
        if (Date.isValid(dateDebut)) {
            parametreSyndicat.setDateDebut(new Date(dateDebut));
        } else {
            parametreSyndicat.setDateDebut(null);
        }
    }

    public String getDateDebut() {
        if (parametreSyndicat.getDateDebut() == null) {
            return null;
        }
        return parametreSyndicat.getDateDebut().getSwissValue();
    }

    public void setDateFin(String dateFin) {
        if (Date.isValid(dateFin)) {
            parametreSyndicat.setDateFin(new Date(dateFin));
        } else {
            parametreSyndicat.setDateFin(null);
        }
    }

    public String getDateFin() {
        if (parametreSyndicat.getDateFin() == null) {
            return null;
        }
        return parametreSyndicat.getDateFin().getSwissValue();
    }

    public String getIdCaisseMetier() {
        return parametreSyndicat.getIdCaisseMetier();
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        parametreSyndicat.setIdCaisseMetier(idCaisseMetier);
    }

    @Override
    public void retrieve() throws Exception {
        parametreSyndicat = parametreSyndicatRepository.findById(getId());
        adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                parametreSyndicat.getIdSyndicat());
    }

    @Override
    public void setId(String id) {
        parametreSyndicat.setId(id);
    }

    @Override
    public void update() throws Exception {
        try {
            parametreSyndicatService.update(parametreSyndicat);
        } catch (UnsatisfiedSpecificationException ex) {
            throw new ViewException(ex);
        }
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(parametreSyndicat.getSpy());
    }

}
