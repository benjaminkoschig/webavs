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
import java.util.HashMap;
import java.util.List;
import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.ebusiness.NouveauTravailleurServiceCRUD;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.views.ebusiness.ModificationTravailleurViewService;
import ch.globaz.vulpecula.web.views.ebusiness.NouveauTravailleurViewService;
import ch.globaz.vulpecula.web.views.postetravail.PosteTravailAjaxService;
import ch.globaz.vulpecula.ws.bean.PermisTravailEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;
import ch.globaz.vulpecula.ws.bean.StatusEbu;
import com.google.gson.Gson;

/**
 * @author JPA
 * 
 */
public class PTModificationTravailleurDetailViewBean extends DomainPersistentObjectViewBean<TravailleurEbuDomain> {

    private TravailleurEbuDomain travailleur = new TravailleurEbuDomain();
    private Employeur employeur;
    private Travailleur travailleurExistant = null;
    private String idTravailleur;
    private PosteTravail posteTravail = null;
    private boolean posteHasDateFin = false;
    private String iban;
    private String localiteBanque;
    private String tauxOccupation;
    private String dateTauxOccupation;
    private String isoPays;
    private String telephoneExistant;
    private String modifTraite;
    private boolean tiersTraite = false;
    private boolean posteTraite = false;
    private boolean travailleurTraite = false;
    private boolean banqueTraite = false;
    private int existingTiers;
    private String telephoneExistantFormatted;

    public PTModificationTravailleurDetailViewBean() {
        super();
        travailleur = new TravailleurEbuDomain();
    }

    public boolean getExistPostePourQualif() {
        PosteTravail ptToCheck = resolvePtToCheck();
        Periode periodeToCheck = resolvePeriodeToCheck(new Date().addMonth(-9), new Date());
        return ptToCheck != null && ptToCheck.isActif(periodeToCheck);
    }

    protected Periode resolvePeriodeToCheck(Date date, Date date2) {
        return new Periode(date, date2);
    }

    protected PosteTravail resolvePtToCheck() {
        if (travailleurExistant != null) {
            List<Qualification> listeQualif = new ArrayList<Qualification>();
            listeQualif.add(Qualification.fromValue(travailleur.getCodeProfessionnel().getValue()));
            return VulpeculaRepositoryLocator.getPosteTravailRepository().findByTravailleurEmployeurEtQualification(
                    travailleurExistant.getId(), employeur.getId(), listeQualif);
        }
        return null;
    }

    public TravailleurEbuDomain getTravailleur() {
        return travailleur;
    }

    public boolean getIsPosteReactivable() {
        if (posteTravail == null) {
            return false;
        }

        return new PosteTravailAjaxService().getIsPosteReactivable(travailleur.getDateDebutActivite(), posteTravail
                .getPeriodeActivite().getDateFinAsSwissValue());

    }

    public String getTravailleurJson() {
        Gson gson = new Gson();
        return gson.toJson(travailleur);
    }

    public String getTravailleurExistantJson() {
        Gson gson = new Gson();
        return gson.toJson(travailleurExistant);
    }

    public String getPosteTravailJson() {
        Gson gson = new Gson();
        return gson.toJson(posteTravail);
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

        // retrieve travailleurExistant from travailleurEbu
        if (travailleur != null) {

            existingTiers = NouveauTravailleurViewService.getCountForTiers(travailleur.getNom(),
                    travailleur.getPrenom(), travailleur.getDateNaissance());
            if (existingTiers == 0 && !JadeStringUtil.isEmpty(travailleur.getNss())) {
                existingTiers = NouveauTravailleurViewService.getCountForTiersNSS(travailleur.getNss());
            }
            if (travailleur.getIdEmployeur() != null && !travailleur.getIdEmployeur().isEmpty()) {
                employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(travailleur.getIdEmployeur());
            }

            if (travailleur.getNss() != null && !travailleur.getNss().isEmpty()) {
                travailleurExistant = VulpeculaRepositoryLocator.getTravailleurRepository().findByNss(
                        travailleur.getNss());
            }

            if (travailleurExistant == null && travailleur.getCorrelationId() != null
                    && !travailleur.getCorrelationId().isEmpty()) {
                travailleurExistant = VulpeculaRepositoryLocator.getTravailleurRepository().findByCorrelationId(
                        travailleur.getCorrelationId());
            }

            if (travailleurExistant == null && travailleur.getNom() != null && !travailleur.getNom().isEmpty()
                    && travailleur.getPrenom() != null && !travailleur.getPrenom().isEmpty()
                    && travailleur.getDateNaissance() != null && !travailleur.getDateNaissance().isEmpty()) {
                travailleurExistant = VulpeculaServiceLocator.getTravailleurService().findByNomPrenomDateNaissance(
                        travailleur.getNom(), travailleur.getPrenom(), travailleur.getDateNaissance());
            }

            if (travailleurExistant == null && travailleur.getNom() != null && !travailleur.getNom().isEmpty()
                    && travailleur.getPrenom() != null && !travailleur.getPrenom().isEmpty()
                    && travailleur.getDateNaissance() != null && !travailleur.getDateNaissance().isEmpty()) {
                travailleurExistant = VulpeculaServiceLocator.getTravailleurService()
                        .findByNomPrenomDateNaissanceEmployeur(travailleur.getNom(), travailleur.getPrenom(),
                                travailleur.getDateNaissance(), travailleur.getIdEmployeur());
            }

            if (isTravailleurExiste()) {
                travailleur.setIdTravailleur(travailleurExistant.getId());
                iban = VulpeculaRepositoryLocator.getAdresseRepository().findIbanByIdTiers(
                        travailleurExistant.getIdTiers(), new Date());
                if (JadeStringUtil.isEmpty(iban)) {
                    iban = "non trouvé";
                }
                localiteBanque = VulpeculaRepositoryLocator.getAdresseRepository().findLocaliteBanqueByIdTiers(
                        travailleurExistant.getIdTiers(), new Date());

                if (travailleurExistant.getNumAvsActuel() != null && !travailleurExistant.getNumAvsActuel().isEmpty()
                        && JadeStringUtil.isEmpty(travailleur.getNss())) {
                    travailleur.setNss(travailleurExistant.getNumAvsActuel());
                }

                if (travailleurExistant.getPays() != null) {
                    isoPays = travailleurExistant.getPays().getCodeIso();
                }

                if (!JadeStringUtil.isEmpty(iban) && JadeStringUtil.isEmpty(travailleur.getCompteBancaire().getIban())) {
                    travailleur.getCompteBancaire().setIban(iban);
                }

                HashMap<TypeContact, MoyenContact> mapContact = VulpeculaRepositoryLocator.getContactRepository()
                        .findMoyenContactForIdTiers(travailleurExistant.getIdTiers());

                MoyenContact moyen = mapContact.get(TypeContact.PRIVE);
                if (moyen != null && !JadeStringUtil.isEmpty(moyen.getValeur())) {
                    telephoneExistant = moyen.getValeur();
                    telephoneExistantFormatted = getDigitFromString(moyen.getValeur());
                }

                if (JadeStringUtil.isEmpty(travailleur.getTelephone())) {
                    travailleur.setTelephone(telephoneExistant);
                }

            }

            // retrieve PosteTravail
            if (!JadeStringUtil.isBlankOrZero(travailleur.getIdPosteTravail())) {
                posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdWithOccupations(
                        travailleur.getIdPosteTravail());
            }

            // Retrieve post when no IdPoste
            if (posteTravail == null && travailleurExistant != null && !getIsNewPoste()) {
                List<Qualification> listeQualif = new ArrayList<Qualification>();
                listeQualif.add(Qualification.fromValue(travailleur.getCodeProfessionnel().getValue()));
                posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository()
                        .findByTravailleurEmployeurEtQualification(travailleurExistant.getId(), employeur.getId(),
                                listeQualif);
            }

            // Recherche de poste sans idPosteTravail
            if (posteTravail == null && travailleurExistant != null && !getIsNewPoste()) {
                posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findByTravailleurEtEmployeur(
                        travailleurExistant.getId(), travailleur.getIdEmployeur());
            }

            if (posteTravail != null && !posteTravail.getIdEmployeur().isEmpty()) {
                if (posteTravail != null && posteTravail.getPeriodeActivite().getDateFin() != null
                        && posteTravail.getPeriodeActivite().getDateFin().beforeOrEquals(new Date())) {
                    posteHasDateFin = true;
                }
                tauxOccupation = formatTaux(posteTravail.getOccupationActuel().getTauxAsValue());
                if (posteTravail.getLatestOccupation() != null) {
                    dateTauxOccupation = posteTravail.getLatestOccupation().getDateValiditeAsValue();
                }
            }

            modifTraite = travailleur.getStatus().getValue();

            if (travailleur.getTiersStatus() == StatusEbu.TRAITE || travailleur.getTiersStatus() == StatusEbu.NO_DIFF
                    || travailleur.getTiersStatus() == StatusEbu.REFUSE
                    || travailleur.getTiersStatus() == StatusEbu.MODIFIE) {
                tiersTraite = true;
            }
            if (travailleur.getPosteStatus() == StatusEbu.TRAITE || travailleur.getPosteStatus() == StatusEbu.NO_DIFF
                    || travailleur.getPosteStatus() == StatusEbu.REFUSE
                    || travailleur.getPosteStatus() == StatusEbu.MODIFIE) {
                posteTraite = true;
            }
            if (travailleur.getBanqueStatus() == StatusEbu.TRAITE || travailleur.getBanqueStatus() == StatusEbu.NO_DIFF
                    || travailleur.getBanqueStatus() == StatusEbu.REFUSE
                    || travailleur.getBanqueStatus() == StatusEbu.MODIFIE) {
                banqueTraite = true;
            }
            if (travailleur.getTravailleurStatus() == StatusEbu.TRAITE
                    || travailleur.getTravailleurStatus() == StatusEbu.NO_DIFF
                    || travailleur.getTravailleurStatus() == StatusEbu.REFUSE
                    || travailleur.getTravailleurStatus() == StatusEbu.MODIFIE) {
                travailleurTraite = true;
            }
        }
    }

    private String formatTaux(String str) {
        if (str != null && str.length() > 0) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public String getIdPosteTravail() {
        if (posteTravail != null) {
            return posteTravail.getId();
        } else {
            return "";
        }

    }

    public String getIdPosteTravailExistant() {
        if (posteTravail != null) {
            return posteTravail.getId();
        } else {
            return "";
        }

    }

    public String getIdAffiliation() {
        return employeur.getId();
    }

    public String getNouveauTravailleurService() {
        return NouveauTravailleurViewService.class.getName();
    }

    public String getPosteTravailAjaxService() {
        return PosteTravailAjaxService.class.getName();
    }

    public String getTravailleurServiceCRUD() {
        return NouveauTravailleurServiceCRUD.class.getName();
    }

    public String getModificationTravailleurService() {
        return ModificationTravailleurViewService.class.getName();
    }

    @Override
    public void update() throws Exception {
        VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleur);
    }

    @Override
    public TravailleurEbuDomain getEntity() {
        return travailleur;
    }

    public boolean isTraite() {
        return travailleur.isTraite();
    }

    public boolean isTravailleurExiste() {
        return travailleurExistant != null;
    }

    /**
     * @return the employeur
     */
    public Employeur getEmployeur() {
        return employeur;
    }

    /**
     * @return the travailleurExistant
     */
    public Travailleur getTravailleurExistant() {
        return travailleurExistant;
    }

    public String getIdTravailleurExistant() {
        if (travailleurExistant != null) {
            return travailleurExistant.getId();
        }
        return "";
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

    public PosteTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getLocaliteBanque() {
        return localiteBanque;
    }

    public void setLocaliteBanque(String localiteBanque) {
        this.localiteBanque = localiteBanque;
    }

    public String getTauxOccupation() {
        return tauxOccupation;
    }

    public void setTauxOccupation(String tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

    public String getDateTauxOccupation() {
        return dateTauxOccupation;
    }

    public void setDateTauxOccupation(String dateTauxOccupation) {
        this.dateTauxOccupation = dateTauxOccupation;
    }

    public boolean getPosteHasDateFin() {
        return posteHasDateFin;
    }

    public void setPosteHasDateFin(boolean posteHasDateFin) {
        this.posteHasDateFin = posteHasDateFin;
    }

    public String getCorrelationId() {
        if (travailleur != null) {
            return travailleur.getCorrelationId();
        }
        return "";

    }

    public String getIsoPays() {
        return isoPays;
    }

    public void setIsoPays(String isoPays) {
        this.isoPays = isoPays;
    }

    public String getModifTraite() {
        String toReturn = "";
        if (StatusAnnonceTravailleurEbu.fromValue(modifTraite) == StatusAnnonceTravailleurEbu.EN_COURS) {
            toReturn = "ENCOURS";
        } else if (StatusAnnonceTravailleurEbu.fromValue(modifTraite) == StatusAnnonceTravailleurEbu.REFUSE) {
            toReturn = "REFUSE";
        } else if (StatusAnnonceTravailleurEbu.fromValue(modifTraite) == StatusAnnonceTravailleurEbu.TRAITE) {
            toReturn = "VALIDE";
        } else if (StatusAnnonceTravailleurEbu.fromValue(modifTraite) == StatusAnnonceTravailleurEbu.AUTO) {
            toReturn = "REFU_AUTO";
        }

        return toReturn;
    }

    public boolean isTiersTraite() {
        return tiersTraite;
    }

    public void setTiersTraite(boolean tiersTraite) {
        this.tiersTraite = tiersTraite;
    }

    public boolean isPosteTraite() {
        return posteTraite;
    }

    public void setPosteTraite(boolean posteTraite) {
        this.posteTraite = posteTraite;
    }

    public boolean isTravailleurTraite() {
        return travailleurTraite;
    }

    public void setTravailleurTraite(boolean travailleurTraite) {
        this.travailleurTraite = travailleurTraite;
    }

    public boolean isBanqueTraite() {
        return banqueTraite;
    }

    public void setBanqueTraite(boolean banqueTraite) {
        this.banqueTraite = banqueTraite;
    }

    public String getNssFormate() {
        if (travailleur.getNss().length() > 15) {
            return travailleur.getNss().substring(4);
        }
        return "";
    }

    public String getTelephoneExistant() {
        return telephoneExistant;
    }

    public void setTelephoneExistant(String telephoneExistant) {
        this.telephoneExistant = telephoneExistant;
    }

    public String getPosteCorrelationId() {
        return travailleur.getPosteCorrelationId();
    }

    public boolean getPosteCorrelationAllreadyExists() {
        if (posteTravail == null) {
            return false;
        }
        return travailleur.getPosteCorrelationId().equals(posteTravail.getPosteCorrelationId());
    }

    public boolean getHasLinkedWMPosteTravail() {
        return posteTravail != null;
    }

    public String getPartialNssExistant() {
        String nss = travailleurExistant.getNumAvsActuel();
        if (nss != null && !JadeStringUtil.isEmpty(nss)) {
            return travailleurExistant.getNumAvsActuel().substring(4);
        }
        return "";
    }

    public int getExistingTiers() {
        return existingTiers;
    }

    public void setExistingTiers(int existingTiers) {
        this.existingTiers = existingTiers;
    }

    public String getTelephoneFormatted() {
        if (travailleur.getTelephone() != null) {
            return getDigitFromString(travailleur.getTelephone());
        }
        return "";
    }

    public String getTelephoneExistantFormatted() {
        return telephoneExistantFormatted;
    }

    public void setTelephoneExistantFormatted(String telephoneExistantFormatted) {
        this.telephoneExistantFormatted = telephoneExistantFormatted;
    }

    private String getDigitFromString(String stringToFormat) {
        return stringToFormat.replaceAll("\\D+", "");
    }

    public boolean getIsNewPoste() {
        if (travailleur.getIdPosteTravail() != null && !JadeStringUtil.isBlankOrZero(travailleur.getIdPosteTravail())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getIsAdresseNull() {
        if (travailleurExistant != null) {
            return travailleurExistant.getAdressePrincipale() == null;
        }
        return true;
    }
}
