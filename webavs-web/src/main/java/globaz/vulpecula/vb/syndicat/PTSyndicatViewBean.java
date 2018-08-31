package globaz.vulpecula.vb.syndicat;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.domain.repositories.syndicat.AffiliationSyndicatRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.web.views.decomptesalaire.DecompteSalaireDetailsAnnuelView;
import ch.globaz.vulpecula.web.views.decomptesalaire.DecompteSalaireViewService;

public class PTSyndicatViewBean extends BJadePersistentObjectViewBean {

    private AffiliationSyndicat affiliationSyndicat = new AffiliationSyndicat();
    private AffiliationSyndicatRepository affiliationSyndicatRepository = VulpeculaRepositoryLocator
            .getAffiliationSyndicatRepository();
    private List<DecompteSalaireDetailsAnnuelView> detailsDecomptesSalairesParAnnee;

    @Override
    public void add() throws Exception {
        setPeriode();
        affiliationSyndicatRepository.create(affiliationSyndicat);
    }

    @Override
    public void delete() throws Exception {
        affiliationSyndicatRepository.delete(affiliationSyndicat);
    }

    @Override
    public String getId() {
        return affiliationSyndicat.getId();
    }

    @Override
    public void retrieve() throws Exception {
        affiliationSyndicat = affiliationSyndicatRepository.findById(getId());
        Date dateFin;
        if (JadeStringUtil.isBlankOrZero(getDateFinAsSwissValue())) {
            dateFin = Date.now();
        } else {
            dateFin = new Date(getDateFinAsSwissValue());
        }
        detailsDecomptesSalairesParAnnee = VulpeculaServiceLocator.getDecompteSalaireService()
                .calculDetailsDecompteSalaire(getTravailleur().getId(), getIdSyndicat(),
                        new Date(getDateDebutAsSwissValue()), dateFin);
    }

    @Override
    public void setId(String id) {
        affiliationSyndicat.setId(id);
    }

    @Override
    public void update() throws Exception {
        setPeriode();
        affiliationSyndicatRepository.update(affiliationSyndicat);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(affiliationSyndicat.getSpy());
    }

    public String getLibelleSyndicat() {
        return affiliationSyndicat.getLibelleSyndicat();
    }

    public String getIdTravailleur() {
        return affiliationSyndicat.getIdTravailleur();
    }

    public String getIdSyndicat() {
        return affiliationSyndicat.getIdSyndicat();
    }

    public Travailleur getTravailleur() {
        return affiliationSyndicat.getTravailleur();
    }

    public void setTravailleur(Travailleur travailleur) {
        affiliationSyndicat.setTravailleur(travailleur);
    }

    public String getDateDebutAsSwissValue() {
        return affiliationSyndicat.getDateDebutAsSwissValue();
    }

    public String getDateFinAsSwissValue() {
        return affiliationSyndicat.getDateFinAsSwissValue();
    }

    // METHODSE UTILISEES PAR LE FRAMEWORK
    private String dateDebut = null;
    private String dateFin = null;

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    private void setPeriode() {
        if (dateDebut != null && dateFin != null) {
            if (JadeStringUtil.isEmpty(dateFin)) {
                affiliationSyndicat.setPeriode(new Periode(dateDebut, null));
            } else {
                affiliationSyndicat.setPeriode(new Periode(dateDebut, dateFin));
            }
        }
    }

    public void setIdSyndicat(String idSyndicat) {
        Administration syndicat = new Administration();
        syndicat.setId(idSyndicat);
        affiliationSyndicat.setSyndicat(syndicat);
    }

    public String getDecompteSalaireViewSerivce() {
        return DecompteSalaireViewService.class.getName();
    }

    public boolean isNouveau() {
        return JadeStringUtil.isEmpty(affiliationSyndicat.getId());
    }

    public List<DecompteSalaireDetailsAnnuelView> getDetailsDecomptesSalairesParAnnee() {
        return detailsDecomptesSalairesParAnnee;
    }

}
