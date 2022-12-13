package globaz.corvus.vb.avances;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.vb.rentesaccordees.REAdressePmtUtil;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIReferencePaiementManager;

import java.util.*;

/**
 * @author SCR
 */
public class REAvanceViewBean extends REAvance implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE = new Object[] {
            new String[] { "idTiersAdressePmtDepuisPyxis", "idTiers" }, new String[] { "csDomaine", "idApplication" },
            new String[] { "numAffilie", "idExterneAvoirPaiement" } };

    private static final Object[] METHODES_SEL_REFERENCE_PAIEMENT_QR = new Object[]{
            new String[]{"setIdReferenceQRDepuisPyxis", "getIdReferenceQR"}};

    private static final Object[] PARAMS_CHERCHER_REFERENCE_PAIEMENT = new Object[]{
            new String[]{"forIdTiers", "forIdTiers"}, new String[]{"forIdAdressePaiement", "forIdAdressePaiement"}, new String[]{"forCompteQRLike", "forCompteQRLike"}};

    private REAdressePmtUtil adressePaiement;
    private String idTiersAdressePmtDepuisPyxis;
    private String idReferenceQRDepuisPyxis;
    private String infoDuTierFormatee;
    private Map<String, REAdressePmtUtil> mapAdressePaiementMembresFamille;
    private Set<PRTiersWrapper> membresFamilles;
    private String numAffilie;
    private boolean retourDepuisPyxis;
    private PRTiersWrapper tiersBeneficiaire;
    private String referenceQRFormattee = "";
    private TIAdressePaiementData adressePaiementData = null;

    public REAvanceViewBean() {
        super();

        adressePaiement = new REAdressePmtUtil();
        idTiersAdressePmtDepuisPyxis = "";
        infoDuTierFormatee = "";
        mapAdressePaiementMembresFamille = new HashMap<String, REAdressePmtUtil>();
        membresFamilles = new HashSet<PRTiersWrapper>();
        numAffilie = "";
        retourDepuisPyxis = false;
        tiersBeneficiaire = null;
    }

    public String getAdresseFormattee() throws Exception {
        if (adressePaiement != null) {
            return adressePaiement.getAdresseFormattee();
        } else {
            // retour pyxis
            adressePaiement = loadAdrPmt(idTiersAdressePmtDepuisPyxis);
        }
        return adressePaiement.getAdresseFormattee();
    }

    public String getBeneficiaireInfo() {
        return tiersBeneficiaire.getDescription(getSession());
    }

    public String getCcpOuBanqueFormatte() {
        return adressePaiement.getCcpOuBanqueFormatte();
    }

    public String getConstantCsDomaineAvanceRente() {
        return IREAvances.CS_DOMAINE_AVANCE_RENTE;
    }

    public String getCsEtat1AcompteLibelle() {
        return getSession().getCodeLibelle(getCsEtat1erAcompte());
    }

    public String getCsEtatAcomptesLibelle() {
        return getSession().getCodeLibelle(getCsEtatAcomptes());
    }

    @Override
    public String getDetailRequerant() {
        return getInfoDuTiersFormatted();
    }

    public String getIdTiersAdpmt() {
        if (!JadeStringUtil.isBlank(idTiersAdressePmtDepuisPyxis)) {
            return idTiersAdressePmtDepuisPyxis;
        } else {
            return getIdTiersAdrPmt();
        }
    }

    public String getIdTiersAdressePmtDepuisPyxis() {
        return idTiersAdressePmtDepuisPyxis;
    }

    public String getInfoDuTiersFormatted() {
        return infoDuTierFormatee;
    }

    public Map<String, REAdressePmtUtil> getMapAdrPmtMbresFamille(String idTiers) {
        return mapAdressePaiementMembresFamille;
    }

    public Set<PRTiersWrapper> getMembresFamilles() {
        return membresFamilles;
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return REAvanceViewBean.METHODES_SEL_ADRESSE;
    }

    public Object[] getMethodesSelectionReferencePaiement() {
        return REAvanceViewBean.METHODES_SEL_REFERENCE_PAIEMENT_QR;
    }

    public Object[] getParamsChercherReferencePaiement() {
        return REAvanceViewBean.PARAMS_CHERCHER_REFERENCE_PAIEMENT;
    }

    @Override
    public String getNom() {
        if (tiersBeneficiaire == null) {
            return "";
        }
        return tiersBeneficiaire.getNom();

    }

    @Override
    public String getNss() {
        if (tiersBeneficiaire == null) {
            return "";
        }
        return tiersBeneficiaire.getNSS();
    }

    public String getNssSansPrefixe() {
        if (tiersBeneficiaire == null) {
            return "";
        }
        if (!JadeStringUtil.isBlankOrZero(tiersBeneficiaire.getNSS())) {
            return tiersBeneficiaire.getNSS().substring(3);
        } else {
            return "";
        }
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getPageTitle() {

        String csDomaineAvance = getSession().getLabel("JSP_AVANCE_D_TITRE");

        if (!JadeStringUtil.isBlank(getCsDomaineAvance())) {
            csDomaineAvance += (" - " + getSession().getCodeLibelle(getCsDomaineAvance()));
        }

        return csDomaineAvance;

    }

    @Override
    public String getPrenom() {
        if (tiersBeneficiaire == null) {
            return "";
        }
        return tiersBeneficiaire.getPrenom();
    }

    public PRTiersWrapper getTiersWrapper() {
        return tiersBeneficiaire;
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public REAdressePmtUtil loadAdrPmt(String idTiers) throws Exception {

        // charcher une adresse de paiement pour ce beneficiaire

        String csDomaine = getCsDomaine();
        if (JadeStringUtil.isBlankOrZero(csDomaine)) {
            csDomaine = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;
        }

        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                .getCurrentThreadTransaction(), idTiers, csDomaine, "", JACalendar.todayJJsMMsAAAA());

        setCsDomaine(adresse.getIdApplication());

        setAdressePaiementData(adresse);

        REAdressePmtUtil adpmt = new REAdressePmtUtil();
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
            source.load(adresse);
            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                adpmt.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                adpmt.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }
            // formatter l'adresse
            adpmt.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
            adpmt.setNom(getTiersWrapper().getProperty(PRTiersWrapper.PROPERTY_NOM));
            adpmt.setPrenom(getTiersWrapper().getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            if (TIAdressePaiement.isQRIban(adresse.getCompte())) {
                setReferenceQRFormattee(TIReferencePaiementManager.getReferencePaiementPourAffichage(getSession(), getIdReferenceQR()));
            } else {
                setReferenceQRFormattee("");
            }
        }

        return adpmt;
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlankOrZero(this.getIdReferenceQR()) && Objects.nonNull(this.getAdressePaiementData()) && TIAdressePaiement.isQRIban(this.getAdressePaiementData().getCompte())) {
            _addError(statement.getTransaction(), getSession().getLabel("JSP_REFERENCE_QR_EMPTY"));
        }
    }

    public void setAdpmt(REAdressePmtUtil adpmt) {
        adressePaiement = adpmt;
    }

    public void setIdTiersAdressePmtDepuisPyxis(String idt) {
        idTiersAdressePmtDepuisPyxis = idt;
        setIdTiersAdrPmt(idTiersAdressePmtDepuisPyxis);
        setIdReferenceQR("");
        retourDepuisPyxis = true;
    }

    public void setInfoDuTiersFormatted(String info) {
        infoDuTierFormatee = info;
    }

    public void setMapAdrPmtMbresFamille(Map<String, REAdressePmtUtil> mapAdrPmtMbresFamille) {
        mapAdressePaiementMembresFamille = mapAdrPmtMbresFamille;
    }

    public void setMembresFamille(Set<PRTiersWrapper> membresFamilles) {
        this.membresFamilles = membresFamilles;
    }

    /**
     * MAJ du no affilié, après le retour de tiers. Met également à jour l'id affilié sur l'employeur est un affilié.
     * 
     * @param numAffilie
     */
    public void setNumAffilie(String numAffilie) throws Exception {
        this.numAffilie = numAffilie;
        IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), this.numAffilie);
        if (affilie != null) {
            setIdAffilie(affilie.getIdAffilie());
        } else {
            setIdAffilie("");
        }
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    public void setTiersBeneficiaireWrapper(PRTiersWrapper tiersBeneficiaire) {
        this.tiersBeneficiaire = tiersBeneficiaire;
    }

    public String getIdReferenceQRDepuisPyxis() {
        return idReferenceQRDepuisPyxis;
    }

    public void setIdReferenceQRDepuisPyxis(String value) {
        this.idReferenceQRDepuisPyxis = value;
        setIdReferenceQR(value);
        retourDepuisPyxis = true;
    }

    public String getReferenceQRFormattee() {
        return referenceQRFormattee;
    }

    public void setReferenceQRFormattee(String referenceQRFormattee) {
        this.referenceQRFormattee = referenceQRFormattee;
    }

    public TIAdressePaiementData getAdressePaiementData() {
        return adressePaiementData;
    }

    public void setAdressePaiementData(TIAdressePaiementData adressePaiementData) {
        this.adressePaiementData = adressePaiementData;
    }
}
