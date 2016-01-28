package globaz.corvus.vb.process;

import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.HashSet;
import java.util.Set;

/**
 * @author SCR
 */
public class REDebloquerMontantRAViewBean extends PRAbstractViewBeanSupport {

    private static final Object[] METHODES_SEL_ADRESSE = new Object[] {
            new String[] { "idTiersAdressePaiementDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineAdrPmt", "idApplication" },
            new String[] { "numAffilieEmployeur", "idExterneAvoirPaiement" } };

    private String adresseFormattee;
    private String ccpOuBanqueFormatte;
    private String eMailAdress;
    private String idAffilieAdrPmt;
    private String idCompteAnnexe;
    private String idDomaineAdrPmt;
    private String idRenteAccordee;
    private String idSection;
    private String idTiersAdrPmt;
    private String idTiersBeneficiaire;
    private Set<String> idTiersFamille;
    private String montantADebloquer;
    private String montantBloque;
    private String montantDebloque;
    private String refPmt;
    private boolean retourDepuisPyxis;
    private String tiersBeneficiaireInfo;

    public REDebloquerMontantRAViewBean() {
        super();

        adresseFormattee = "";
        ccpOuBanqueFormatte = "";
        eMailAdress = "";
        idAffilieAdrPmt = "";
        idCompteAnnexe = "";
        idDomaineAdrPmt = "";
        idRenteAccordee = "";
        idSection = "";
        idTiersAdrPmt = "";
        idTiersBeneficiaire = "";
        montantADebloquer = "";
        montantBloque = "";
        montantDebloque = "";
        refPmt = "";
        retourDepuisPyxis = false;
        tiersBeneficiaireInfo = "";
        idTiersFamille = new HashSet<String>();
    }

    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    public String getEMailAdress() {
        return eMailAdress;
    }

    public String getIdAffilieAdrPmt() {
        return idAffilieAdrPmt;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdDomaineAdrPmt() {
        return idDomaineAdrPmt;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Set<String> getIdTiersFamille() {
        return idTiersFamille;
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

    public String getRefPmt() {
        return refPmt;
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

    /**
     * charge l'adresse de paiement.
     * 
     * @return une adresse de paiement ou null.
     */
    public void loadAdressePaiement(String dateValeurCompta) throws Exception {

        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                .getCurrentThreadTransaction(), getIdTiersAdrPmt(), getIdDomaineAdrPmt(), getIdAffilieAdrPmt(),
                dateValeurCompta);

        // formatter les infos de l'adresse pour l'affichage correct dans
        // l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
            source.load(adresse);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
        } else {
            setCcpOuBanqueFormatte("");
            setAdresseFormattee("");
        }
    }

    public void setAdresseFormattee(String adresseFormattee) {
        this.adresseFormattee = adresseFormattee;
    }

    public void setCcpOuBanqueFormatte(String ccpOuBanqueFormatte) {
        this.ccpOuBanqueFormatte = ccpOuBanqueFormatte;
    }

    public void setEMailAdress(String mailAdress) {
        eMailAdress = mailAdress;
    }

    public void setIdAffilieAdrPmt(String idAffilieAdrPmt) {
        this.idAffilieAdrPmt = idAffilieAdrPmt;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdDomaineAdrPmt(String idDomaineAdrPmt) {
        this.idDomaineAdrPmt = idDomaineAdrPmt;
    }

    public void setIdRenteAccordee(String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiersAdressePaiementDepuisPyxis(String idAdressePaiement) {
        setIdTiersAdrPmt(idAdressePaiement);
        retourDepuisPyxis = true;
    }

    public void setIdTiersAdrPmt(String idTiersAdrPmt) {
        this.idTiersAdrPmt = idTiersAdrPmt;
    }

    public void setIdTiersBeneficiaire(String newIdTiersBeneficiaire) {
        idTiersBeneficiaire = newIdTiersBeneficiaire;
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

    public void setRefPmt(String refPmt) {
        this.refPmt = refPmt;
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    public void setTiersBeneficiaireInfo(String newTiersBeneficiaireInfo) {
        tiersBeneficiaireInfo = newTiersBeneficiaireInfo;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
