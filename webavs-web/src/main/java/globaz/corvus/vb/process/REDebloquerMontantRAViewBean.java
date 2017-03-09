package globaz.corvus.vb.process;

import globaz.corvus.db.deblocage.ReRetour;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Montant;

public class REDebloquerMontantRAViewBean extends PRAbstractViewBeanSupport {

    private static final Object[] METHODES_SEL_ADRESSE = new Object[] {
            new String[] { "idTiersAdressePaiementDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineAdrPmt", "idApplication" },
            new String[] { "numAffilieEmployeur", "idExterneAvoirPaiement" } };

    private String idTiersBeneficiaire;
    private String adresseFormattee;
    private String idAffilieAdrPmt;
    private String idRenteAccordee;
    private Set<String> idTiersFamille = new HashSet<String>();
    private String montantADebloquer;
    private String montantBloque;
    private String montantDebloque;
    private String montantRente;
    private boolean retourDepuisPyxis = false;
    private String tiersBeneficiaireInfo;
    private String idTiersAdrPmt;
    private String idDomaineAdrPmt;
    private String idCompteAnnexe;
    private String periode;
    private String genre;
    private List<ReRetour> retours;

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

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    public void setIdTiersAdrPmt(String idTiersAdrPmt) {
        this.idTiersAdrPmt = idTiersAdrPmt;
    }

    public String getIdDomaineAdrPmt() {
        return idDomaineAdrPmt;
    }

    public void setIdDomaineAdrPmt(String idDomaineAdrPmt) {
        this.idDomaineAdrPmt = idDomaineAdrPmt;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public Set<String> getIdTiersFamille() {
        return idTiersFamille;
    }

    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    public String getIdAffilieAdrPmt() {
        return idAffilieAdrPmt;
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

    public Object[] getMethodesSelectionAdresse() {
        return REDebloquerMontantRAViewBean.METHODES_SEL_ADRESSE;
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

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getTiersBeneficiaireInfo() {
        return tiersBeneficiaireInfo;
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public void setAdresseFormattee(String adresseFormattee) {
        this.adresseFormattee = adresseFormattee;
    }

    public void setIdAffilieAdrPmt(String idAffilieAdrPmt) {
        this.idAffilieAdrPmt = idAffilieAdrPmt;
    }

    public void setIdRenteAccordee(String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    public void setIdTiersFamille(Set<String> idTiersFamille) {
        this.idTiersFamille = idTiersFamille;
    }

    public void setMontantADebloquer(String montantADebloquer) {
        this.montantADebloquer = montantADebloquer;
    }

    public void setMontantBloque(String montantBloque) {
        this.montantBloque = montantBloque;
    }

    public void setMontantDebloque(String montantDebloque) {
        this.montantDebloque = montantDebloque;
    }

    /**
     * MAJ du no affilié, après le retour de tiers. Met également à jour l'id affilié sur l'employeur est un affilié.
     * 
     * @param string
     */
    public void setNumAffilieEmployeur(String string) throws Exception {
        String numAffilieEmployeur = string;
        IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), numAffilieEmployeur);
        if (affilie != null) {
            setIdAffilieAdrPmt(affilie.getIdAffilie());
        } else {
            setIdAffilieAdrPmt("");
        }
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    public void setTiersBeneficiaireInfo(String newTiersBeneficiaireInfo) {
        tiersBeneficiaireInfo = newTiersBeneficiaireInfo;
    }

    public String getIdTiersRequerant() {
        return null;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public boolean getIsDevalidable() {
        return true;
    }

    public boolean getIsUpdatable() {
        return true;
    }

    @Override
    public String getAction() {
        return null;
    }

    public Montant getMontantToUsedForDeblocage() {
        return null;
    }

    public String getDescriptionRequerant() {
        return null;
    }

    public String getCsDomaineApplicationRente() {
        return IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;
    }

    public String getCsTypeBlocageDette() {
        return null;
    }

    public String getCsTypeBlocageCreancier() {
        return null;
    }

    public String getCsTypeBlocageBeneficiare() {
        return null;
    }

    public String getClassAdministrationService() {
        return ch.globaz.pyxis.business.service.AdministrationService.class.getName();
    }

    public String getClassPersonneService() {
        return ch.globaz.pyxis.business.service.PersonneEtendueService.class.getName();
    }

    public List<?> getDettesUpdateable() {
        return new ArrayList();
    }

    public Map<?, ?> getDettes() {
        return new HashMap();
    }

    public List<ReRetour> getRetours() {
        return retours;
    }

    public void setRetours(List<ReRetour> retours) {
        this.retours = retours;
    }

    public Map<?, ?> getCreanciers() {
        return new HashMap();
    }

    public List<?> getVersementBeneficiaires() {
        return new ArrayList();
    }

    public List<?> getVersementBeneficiairesUpdateable() {
        return new ArrayList();
    }

    public String getMontantLiberer() {
        return null;
    }

    public String getMontantRente() {
        return montantRente;
    }

    public void setMontantRente(String montantRente) {
        this.montantRente = montantRente;
    }

}
