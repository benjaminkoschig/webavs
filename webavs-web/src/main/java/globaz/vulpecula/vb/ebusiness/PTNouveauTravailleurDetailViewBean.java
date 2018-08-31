/**
 * 
 */
package globaz.vulpecula.vb.ebusiness;

import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.ebusiness.NouveauTravailleurServiceCRUD;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.views.ebusiness.NouveauTravailleurViewService;
import ch.globaz.vulpecula.ws.bean.PermisTravailEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;

/**
 * @author JPA
 * 
 */
public class PTNouveauTravailleurDetailViewBean extends DomainPersistentObjectViewBean<TravailleurEbuDomain> {

    private TravailleurEbuDomain travailleur = new TravailleurEbuDomain();
    private Employeur employeur;
    private String idTravailleur;
    private int existingTiers;
    private String modifTraite;

    public PTNouveauTravailleurDetailViewBean() {
        super();
        travailleur = new TravailleurEbuDomain();
    }

    public TravailleurEbuDomain getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(TravailleurEbuDomain travailleur) {
        this.travailleur = travailleur;
    }

    @Override
    public void add() throws Exception {
        VulpeculaServiceLocator.getNouveauTravailleurService().create(travailleur);
    }

    @Override
    public void delete() throws Exception {
        VulpeculaServiceLocator.getNouveauTravailleurService().delete(travailleur);
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(travailleur.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        idTravailleur = travailleur.getId();
        travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findByIdSansQuittance(idTravailleur);
        if (travailleur == null) {
            travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository().findById(idTravailleur);
        }
        if (travailleur != null) {
            if (travailleur.getIdEmployeur() != null && !travailleur.getIdEmployeur().isEmpty()) {
                employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(travailleur.getIdEmployeur());
            }
            existingTiers = NouveauTravailleurViewService.getCountForTiers(travailleur.getNom(),
                    travailleur.getPrenom(), travailleur.getDateNaissance());
            if (existingTiers == 0 && !JadeStringUtil.isEmpty(travailleur.getNss())) {
                existingTiers = NouveauTravailleurViewService.getCountForTiersNSS(travailleur.getNss());
            }
        }

        if (travailleur != null) {
            modifTraite = travailleur.getStatus().getValue();
        }

    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public String getNouveauTravailleurService() {
        return NouveauTravailleurViewService.class.getName();
    }

    public String getTravailleurServiceCRUD() {
        return NouveauTravailleurServiceCRUD.class.getName();
    }

    public String getNssFormate() {
        if (travailleur.getNss().length() > 15) {
            return travailleur.getNss().substring(4);
        }
        return "";
    }

    @Override
    public void update() throws Exception {
        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleur);
    }

    @Override
    public TravailleurEbuDomain getEntity() {
        return travailleur;
    }

    public String getTauxActivite() {
        return String.valueOf(travailleur.getTauxActivite());
    }

    public String getDateTauxActivite() {
        return travailleur.getDateTauxActivite();
    }

    public boolean isTraite() {
        return travailleur.isTraite();
    }

    /**
     * @return the employeur
     */
    public Employeur getEmployeur() {
        return employeur;
    }

    public List<CodeSystem> getSexes() {
        return CodeSystemUtil.getCodesSystemesForFamille("PYSEXE");
    }

    public List<CodeSystem> getEtatsCivils() {
        return CodeSystemUtil.getCodesSystemesForFamille("PYETATCIVI");
    }

    public List<CodeSystem> getPermisTravails() {
        return CodeSystemUtil.getCodesSystemesForFamille("PTGENREPER");
    }

    public List<TIPays> getPays() throws Exception {
        List<TIPays> pays = new ArrayList<TIPays>();
        TIPaysManager manager = new TIPaysManager();
        manager.setSession(BSessionUtil.getSessionFromThreadContext());
        manager.find(BManager.SIZE_NOLIMIT);
        for (int i = 0; i < manager.size(); i++) {
            pays.add((TIPays) manager.getEntity(i));
        }
        return pays;
    }

    public PermisTravailEbu getPermisTravail() {
        return travailleur.getPermisSejour();
    }

    public String getCorrelationId() {
        return travailleur.getCorrelationId();
    }

    public String getPosteCorrelationId() {
        return travailleur.getPosteCorrelationId();
    }

    public int getExistingTiers() {
        return existingTiers;
    }

    public void setExistingTiers(int existingTiers) {
        this.existingTiers = existingTiers;
    }

    public String getModifTraite() {
        String toReturn = "";
        if (StatusAnnonceTravailleurEbu.fromValue(modifTraite) == StatusAnnonceTravailleurEbu.EN_COURS) {
            toReturn = "ENCOURS";
        } else if (StatusAnnonceTravailleurEbu.fromValue(modifTraite) == StatusAnnonceTravailleurEbu.REFUSE) {
            toReturn = "REFUSE";
        } else if (StatusAnnonceTravailleurEbu.fromValue(modifTraite) == StatusAnnonceTravailleurEbu.TRAITE) {
            toReturn = "VALIDE";
        }
        return toReturn;
    }

    public String getIdTiers() {
        String idTiers = "";
        if (travailleur != null) {
            Travailleur tiersInfo = null;
            if (!JadeStringUtil.isBlankOrZero(travailleur.getIdTravailleur())) {
                tiersInfo = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                        travailleur.getIdTravailleur());
            } else if (!JadeStringUtil.isEmpty(travailleur.getNss())) {
                tiersInfo = VulpeculaRepositoryLocator.getTravailleurRepository().findByNss(travailleur.getNss());
            } else if (!JadeStringUtil.isEmpty(travailleur.getCorrelationId())) {
                tiersInfo = VulpeculaRepositoryLocator.getTravailleurRepository().findByCorrelationId(
                        travailleur.getCorrelationId());
            }
            if (tiersInfo != null) {
                idTiers = tiersInfo.getIdTiers();
                idTravailleur = tiersInfo.getId();
            }
        }
        return idTiers;
    }
}
