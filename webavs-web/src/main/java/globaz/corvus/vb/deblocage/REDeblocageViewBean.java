package globaz.corvus.vb.deblocage;

import globaz.corvus.db.deblocage.REDeblocage;
import globaz.corvus.db.deblocage.REDeblocageService;
import globaz.corvus.db.deblocage.ReRetour;
import globaz.corvus.db.lignedeblocage.RELigneDeblocage;
import globaz.corvus.db.lignedeblocage.RELigneDeblocageCreancier;
import globaz.corvus.db.lignedeblocage.RELigneDeblocageDette;
import globaz.corvus.db.lignedeblocage.RELigneDeblocageVersement;
import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDues;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pyxis.business.service.AdministrationService;
import ch.globaz.pyxis.business.service.PersonneEtendueService;

public class REDeblocageViewBean extends PRAbstractViewBeanSupport {

    private String idTiersBeneficiaire;
    private String idRenteAccordee;
    private Set<String> idTiersFamille = new HashSet<String>();
    private String montantADebloquer;
    private String montantBloque;
    private String montantDebloque;
    private String montantRente;
    private boolean retourDepuisPyxis = false;
    private String tiersBeneficiaireInfo;
    private String periode;
    private String genre;
    private List<ReRetour> retours;
    private RELigneDeblocages deblocages;
    private RERenteAccordeeJoinInfoComptaJoinPrstDues pracc;

    @Override
    public void retrieve() throws Exception {
        REDeblocageService service = new REDeblocageService(getSession());
        REDeblocage deblocage = service.read(Long.valueOf(getId()));
        deblocages = deblocage.getLignesDeblocages();
        pracc = deblocage.getPracc();

        idTiersBeneficiaire = deblocage.getBeneficiaire().getIdTiers();
        tiersBeneficiaireInfo = deblocage.getDescriptonTier();
        periode = deblocage.getPracc().getDateDebutDroit() + " - " + deblocage.getPracc().getDateDebutDroit();
        genre = deblocage.getPracc().getCodePrestation();
        retours = deblocage.getRetours();

        montantRente = new Montant(deblocage.getPracc().getMontantPrestation()).toStringFormat();
        montantBloque = new Montant(deblocage.getEnteteBlocage().getMontantBloque()).toStringFormat();
        montantDebloque = new Montant(deblocage.getEnteteBlocage().getMontantDebloque()).toStringFormat();
        montantADebloquer = deblocage.computeMontantToUseForDeblocage().toStringFormat();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Set<String> getIdTiersFamille() {
        return idTiersFamille;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public String getCsDomaineApplicationRente() {
        return IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;
    }

    /**
     * Retourne les ID tiers de la famille en une ligne séparés par des virgules.<br/>
     * Pour une utilisation avec le {@link CASectionJoinCompteAnnexeJoinTiersManager} dans le widget
     * 
     * @return les ID's dans une chaîne de caractères, séparés par des virgules
     * @see {@link CASectionJoinCompteAnnexeJoinTiersManager#setForIdTiersIn(String)}
     */
    public String getIdTiersFamilleInline() {
        if (idTiersFamille == null) {
            return "";
        }

        StringBuilder concat = new StringBuilder();
        boolean isFirstId = true;
        for (String unIdTiers : idTiersFamille) {
            if (!JadeNumericUtil.isInteger(unIdTiers)) {
                continue;
            }
            if (isFirstId) {
                isFirstId = false;
            } else {
                // voir la méthode setForIdTiersIn de CACompteAnnexeManager
                concat.append("|");
            }
            concat.append(unIdTiers);
        }
        return concat.toString();
    }

    public String getMontantADebloquer() {
        return montantADebloquer;
    }

    public String getMontantBloque() {
        return montantBloque;
    }

    public String getMontantDebloque() {
        return montantDebloque;
    }

    public List<RELigneDeblocageVersement> getVersementBeneficiaires() {
        return deblocages.getLigneDeblocageVersementBeneficiaire().filtreValidesAndComptabilises().toListVersement();
    }

    public String getMontantLiberer() {
        return null;
    }

    public String getMontantRenteAccordee() {
        return montantRente;
    }

    public String getIdTiersRequerant() {
        return null;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public boolean getIsDevalidable() {
        return deblocages.isDevalidable();
    }

    @Override
    public String getAction() {
        return "corvus.deblocage.deblocageAjax";
    }

    public String getMontantToUsedForDeblocage() {
        return montantADebloquer;
    }

    public String getCsTypeDeBlocageDette() {
        return RELigneDeblocageType.DETTE_EN_COMPTA.getValue();
    }

    public String getCsTypeDeBlocageCreancier() {
        return RELigneDeblocageType.CREANCIER.getValue();
    }

    public String getCsTypeDelocageImpot() {
        return RELigneDeblocageType.IMPOTS_SOURCE.getValue();
    }

    public String getTypeDelocageBeneficiaire() {
        return RELigneDeblocageType.VERSEMENT_BENEFICIAIRE.getValue();
    }

    public String getClassAdministrationService() {
        return AdministrationService.class.getName();
    }

    public String getClassPersonneService() {
        return PersonneEtendueService.class.getName();
    }

    public List<RELigneDeblocageCreancier> getLignesDeblocageCreancier() {
        return deblocages.getLigneDeblocageCreancier().toListCreancier();
    }

    public List<RELigneDeblocageDette> getDettes() {
        return deblocages.filtreValides().getLigneDeblocageDetteEnCompta().toListDette();
    }

    public List<RELigneDeblocageDette> getDettesUpdateable() {
        return deblocages.filtreEnregistresAndNone().getLigneDeblocageDetteEnCompta().toListDette();
    }

    public RELigneDeblocage getImpotSource() {
        if (deblocages.filtreEnregistres().getLigneDeblocageImpotsSource().toList().isEmpty()) {
            return new RELigneDeblocage();
        }
        return deblocages.filtreEnregistres().getLigneDeblocageImpotsSource().toList().get(0);
    }

    public RELigneDeblocageVersement getVersementBeneficiaire() {
        if (deblocages.filtreEnregistres().getLigneDeblocageVersementBeneficiaire().toList().isEmpty()) {
            RELigneDeblocageVersement versement = new RELigneDeblocageVersement();
            versement.setMontant(Montant.ZERO);
            versement.setDescriptionTiers(tiersBeneficiaireInfo);
            return versement;
        }
        return deblocages.getLigneDeblocageVersementBeneficiaire().filtreEnregistres().toListVersement().get(0);
    }

    public List<RELigneDeblocage> getImpotSources() {
        return deblocages.getLigneDeblocageImpotsSource().filtreValidesAndComptabilises().toList();
    }

    public String getTiersBeneficiaireInfo() {
        return tiersBeneficiaireInfo;
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public void setIdRenteAccordee(String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    public void setTiersBeneficiaireInfo(String newTiersBeneficiaireInfo) {
        tiersBeneficiaireInfo = newTiersBeneficiaireInfo;
    }

    public List<ReRetour> getRetours() {
        return retours;
    }

    public void setRetours(List<ReRetour> retours) {
        this.retours = retours;
    }

    public boolean isRenteBloque() {
        return pracc.getIsPrestationBloquee();
    }

    public boolean isLiberable() {
        return deblocages.isLiberable();
    }

    public String getParamActionLiberer() {
        return "liberer";
    }

    public String getParamActionDeValider() {
        return "devalider";
    }

}
