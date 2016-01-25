package globaz.pegasus.vb.blocage;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.pegasus.utils.PCUserHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.blocage.Creancier;
import ch.globaz.pegasus.business.vo.blocage.DeblocageDetail;
import ch.globaz.pegasus.business.vo.blocage.DetteComptat;
import ch.globaz.pegasus.business.vo.blocage.SoldeCompteCouranSection;
import ch.globaz.pegasus.business.vo.blocage.VersementBeneficiaire;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCDeblocageViewBean extends BJadePersistentObjectViewBean {

    private DeblocageDetail deblocageDetail;
    private List<DetteComptat> dettes = new ArrayList<DetteComptat>();
    private List<DetteComptat> dettesUpdateable = new ArrayList<DetteComptat>();
    private String id;
    private List<VersementBeneficiaire> versementBeneficiaires = new ArrayList<VersementBeneficiaire>();
    private List<VersementBeneficiaire> versementBeneficiairesUpdateable = new ArrayList<VersementBeneficiaire>();

    public PCDeblocageViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
    }

    private float computeSoldeToUsed() {
        return deblocageDetail.getMontantTotalADebloque().floatValue();
    }

    @Override
    public void delete() throws Exception {
    }

    private PersonneEtendueComplexModel findRequerant() {
        for (VersementBeneficiaire versement : deblocageDetail.getVersementBeneficiaire()) {
            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(versement.getCsRoleMembreFamille())) {
                return versement.getPersonne();
            }
        }
        return null;
    }

    public String getAction() {
        return IPCActions.ACTION_BLOCAGE_DEBLOCAGE_AJAX;
    }

    public String getClassAdministrationService() {
        return ch.globaz.pyxis.business.service.AdministrationService.class.getName();
    }

    public String getClassPersonneService() {
        return ch.globaz.pyxis.business.service.PersonneEtendueService.class.getName();
    }

    public List<SoldeCompteCouranSection> getComptesBlocage() {
        return deblocageDetail.getComptesBlocage();
    }

    public List<Creancier> getCreanciers() {
        return deblocageDetail.getCreanciers();
    }

    public String getCsDomaineApplicationRente() {
        return IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;
    }

    public String getCsTypeBlocageBeneficiare() {
        return EPCTypeDeblocage.CS_VERSEMENT_BENEFICIAIRE.getCsCode();
    }

    public String getCsTypeBlocageCreancier() {
        return EPCTypeDeblocage.CS_CREANCIER.getCsCode();
    }

    public String getCsTypeBlocageDette() {
        return EPCTypeDeblocage.CS_DETTE_EN_COMPTA.getCsCode();
    }

    public String getDescriptionRequerant() {
        PersonneEtendueComplexModel personne = findRequerant();
        if (personne != null) {
            return PCUserHelper.getDetailAssure(findRequerant()).replace("<br />", " / ");
        }
        return null;
    }

    public List<DetteComptat> getDettes() {
        return dettes;
    }

    public List<DetteComptat> getDettesUpdateable() {
        return dettesUpdateable;
    }

    public EPCEtatDeblocage getEtatComptabilis() {
        return EPCEtatDeblocage.COMPTABILISE;
    }

    public EPCEtatDeblocage getEtatEnregistre() {
        return EPCEtatDeblocage.ENREGISTRE;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdTiersRequerant() {
        PersonneEtendueComplexModel personne = findRequerant();
        if (personne != null) {
            return personne.getTiers().getIdTiers();
        }
        return null;
    }

    public Boolean getIsDevalidable() {
        return deblocageDetail.getIsDevalidable();
    }

    public Boolean getIsSoldeDesynchroniseDeLaCompta() {
        return !deblocageDetail.getSoldeCompteBlocage().abs().equals(deblocageDetail.getMontantTotalADebloque().abs());
    }

    public boolean getIsSoldePositif() {
        return ((new BigDecimal(computeSoldeToUsed())).compareTo(new BigDecimal(0)) == 0);
    }

    public boolean getIsUpdatable() {
        return deblocageDetail.getMontantTotalADebloque().compareTo(new BigDecimal(0)) != 0;
    }

    public String getLableGenre() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(getPcaBloque().getCsGenrPca());
    }

    public String getLableType() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(getPcaBloque().getCsTypePca());
    }

    public String getMontantBloque() {
        return new FWCurrency(deblocageDetail.getPcaBloque().getMontantBloque()).toStringFormat();
    }

    public String getMontantLiberer() {
        return deblocageDetail.getMontantLiberer().toString();
    }

    public String getMontantPca() {
        return new FWCurrency(deblocageDetail.getPcaBloque().getMontantPca()).toStringFormat();
    }

    public String getMontantToUsedForDeblocage() {
        return new FWCurrency(computeSoldeToUsed()).toStringFormat();
    }

    public String getParamActionDeLiberer() {
        return IPCActions.PARAM_ACTION_DEBLOCAGE_DEVALIDER_LIBERATION;
    }

    public String getParamActionLiberer() {
        return IPCActions.PARAM_ACTION_DEBLOCAGE_VALIDER_LIBERATION;
    }

    public PcaBloque getPcaBloque() {
        return deblocageDetail.getPcaBloque();
    }

    public String getSolde() {
        return new FWCurrency(deblocageDetail.getSolde().toString()).toStringFormat();
    }

    public String getSoldeCompteBlocage() {
        return new FWCurrency(deblocageDetail.getSoldeCompteBlocage().negate().toString()).toStringFormat();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public List<VersementBeneficiaire> getVersementBeneficiaires() {
        return versementBeneficiaires;
    }

    public List<VersementBeneficiaire> getVersementBeneficiairesUpdateable() {
        return versementBeneficiairesUpdateable;
    }

    @Override
    public void retrieve() throws Exception {
        deblocageDetail = PegasusServiceLocator.getBlocageService().readForDeblocage(id);
        splitDettesEnCompta();
        // this.splitCreancier();
        splitBeneficiaire();
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    private void splitBeneficiaire() {
        for (VersementBeneficiaire versement : deblocageDetail.getVersementBeneficiaire()) {
            versement.setDescription(PCUserHelper.getDetailAssure(versement.getPersonne()).replace("<br />", " / "));
            if (getIsUpdatable()
                    && ((EPCEtatDeblocage.ENREGISTRE.equals(versement.getEtatDeblocage())) || (versement
                            .getEtatDeblocage() == null))) {
                versementBeneficiairesUpdateable.add(versement);
            } else if (!(EPCEtatDeblocage.ENREGISTRE.equals(versement.getEtatDeblocage()) || (versement
                    .getEtatDeblocage() == null))) {
                versementBeneficiaires.add(versement);
            }
        }
    }

    public void splitDettesEnCompta() {
        for (DetteComptat dette : deblocageDetail.getDetteEnCompta()) {
            if (getIsUpdatable()
                    && (EPCEtatDeblocage.ENREGISTRE.equals(dette.getEtatDeblocage()) || (dette.getEtatDeblocage() == null))) {
                dettesUpdateable.add(dette);
            } else if (!(EPCEtatDeblocage.ENREGISTRE.equals(dette.getEtatDeblocage()) || (dette.getEtatDeblocage() == null))) {
                dettes.add(dette);
            }
        }
    }

    @Override
    public void update() throws Exception {
    }

}
