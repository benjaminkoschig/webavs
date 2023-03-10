package globaz.naos.db.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.properties.AFProperties;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceCallUtil;
import java.util.List;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.businessimpl.services.partnerWeb.PartnerWebServiceImpl;
import ch.globaz.orion.ws.service.UtilsService;

public class AFAffiliationViewBean extends AFAffiliation implements FWViewBeanInterface {
    private static final long serialVersionUID = 1L;
    private String action;
    private String message;
    private String msgType;
    private String ideRaisonSocialeFromWebServcice = "";

    private Boolean noAVSChange = Boolean.FALSE;
    private Boolean nomChange = Boolean.FALSE;

    private boolean isIdeReadOnly = false;
    private boolean isInMutationOnly = false;
    private boolean isIDEPartage = false;

    private boolean isMessageAnnonceIdeCreationAjouteeToDisplay = false;

    private boolean isEbusinessConnected = false;
    private boolean isActivAffilieEBusiness = false;
    private boolean wantDisplayIsAffilieEbusiness = false;

    // commence par soulign? afin de ne pas ?tre prise par jspSetBeanProperties
    public void _setMessageAnnonceIdeCreationAjouteeToDisplay(boolean isMessageAnnonceIdeCreationAjouteeToDisplay) {
        this.isMessageAnnonceIdeCreationAjouteeToDisplay = isMessageAnnonceIdeCreationAjouteeToDisplay;
    }

    public boolean isInMutationOnly() {
        return isInMutationOnly;
    }

    /**
     * commence par '_' (soulign?) afin de ne pas ?tre prise par jspSetBeanProperties
     */
    public void _setInMutationOnly(boolean isMutationOnly) {
        isInMutationOnly = isMutationOnly;
    }

    public boolean isIdeReadOnly() {
        return isIdeReadOnly;
    }

    // commence par soulign? afin de ne pas ?tre prise par jspSetBeanProperties
    public void _setIdeReadOnly(boolean isIdeReadOnly) {
        this.isIdeReadOnly = isIdeReadOnly;
    }

    public boolean isIDEPartage() {
        return isIDEPartage;
    }

    // commence par soulign? afin de ne pas ?tre prise par jspSetBeanProperties
    public void _setIDEPartage(boolean isIDEPartage) {
        this.isIDEPartage = isIDEPartage;
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        int nbAnnonce = AFIDEUtil.countAffiliationAnnonceEnCours(getSession(), getAffiliationId());
        boolean hasAffiliationAnnonceLieeEnCours = AFIDEUtil.hasAffiliationAnnonceLieeEnCours(getSession(),
                getAffiliationId());
        isIDEPartage = AFIDEUtil.hasIDEMultipleAff(getSession(), getNumeroIDE());
        isInMutationOnly = false;
        isIdeReadOnly = false;
        if (nbAnnonce >= 1 || hasAffiliationAnnonceLieeEnCours) {
            if (nbAnnonce == 1
                    && AFIDEUtil.hasAnnonceEnCours(getSession(), getAffiliationId(),
                            CodeSystem.TYPE_ANNONCE_IDE_MUTATION, CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI)
                    && !hasAffiliationAnnonceLieeEnCours) {
                isInMutationOnly = true;
            } else {
                isIdeReadOnly = true;
            }
        }

        // si un EBusiness est connect? on regarde si l'affili? ? un compte EBusiness actif
        isEbusinessConnected = CommonProperties.EBUSINESS_CONNECTED.getBooleanValue();
        wantDisplayIsAffilieEbusiness = AFProperties.DISPLAY_IS_AFFILIE_EBUSINESS.getBooleanValue();
        if (isEbusinessConnected && wantDisplayIsAffilieEbusiness) {
            // renseigne l'attribut indiquant si l'affili? est inscrit et actif dans l'EBusiness
            isActivAffilieEBusiness = PartnerWebServiceImpl.isExistingAndActivAffilieEbusiness(
                    UtilsService.getSessionUserGeneric(getSession()), getAffilieNumero());
        }
    }

    /**
     * Commentaire relatif au constructeur AFAffiliationViewBean.
     */
    public AFAffiliationViewBean() {
        super();
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    public String getDebutAffiliationReelle() {
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }
        AFParticulariteAffiliationManager particulariteManager = new AFParticulariteAffiliationManager();
        particulariteManager.setForAffiliationId(getAffiliationId());
        particulariteManager.setForParticularite(CodeSystem.PARTIC_AFFILIE_PERIODE_AFFILIATION);
        particulariteManager.setSession(getSession());
        try {
            particulariteManager.find();
            if (!particulariteManager.isEmpty()) {
                // une date existe, retourner cette inforamtion
                return ((AFParticulariteAffiliation) particulariteManager.getFirstEntity()).getDateDebut();
            }
        } catch (Exception ex) {
            // exception, retourner null
            return null;
        }
        return null;
    }

    public String initInfoIDEPourRechercheTrouvee(BSession session, String numeroIDE) throws Exception {
        List<IDEDataBean> rechercheIDE = IDEServiceCallUtil.searchForNumeroIDE(numeroIDE, session);
        if (rechercheIDE.size() == 1) {
            String codeSystemStatutIde = rechercheIDE.get(0).getStatut();
            return rechercheIDE.get(0).getNumeroIDE() + "@" + rechercheIDE.get(0).getRaisonSociale() + '@'
                    + codeSystemStatutIde + '@' + CodeSystem.getLibelle(session, codeSystemStatutIde) + '@';
        }
        return null;
    }

    /**
     * @return
     */
    public Boolean getNoAVSChange() {
        return noAVSChange;
    }

    /**
     * @return
     */
    public Boolean getNomChange() {
        return nomChange;
    }

    /**
     * Retourne le libell? de la raison sociale fourni par le webService IDE
     * 
     * @return
     */
    public String getIdeRaisonSocialeFromWebServcice() {
        return ideRaisonSocialeFromWebServcice;
    }

    /**
     * retourne true si l'affili? existe et est actif dans l'EBusiness
     * 
     * @return
     */
    public boolean isActivAffilieEBusiness() {
        return isActivAffilieEBusiness;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @param boolean1
     */
    public void setNoAVSChange(Boolean boolean1) {
        noAVSChange = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setNomChange(Boolean boolean1) {
        nomChange = boolean1;
    }

    /**
     * Permet de setter la raison sociale fourni par le webservice
     * Utilis en retour de selection deu webservice IDE
     * 
     * @param raisonSocialeFromWebServcice
     */
    public void setIdeRaisonSocialeFromWebServcice(String raisonSocialeFromWebServcice) {
        ideRaisonSocialeFromWebServcice = raisonSocialeFromWebServcice;
    }

    @Override
    public void setIdeRaisonSocialeb64(String iDE_raisonSociale) {
        try {
            isIDEPartage = AFIDEUtil.hasIDEAllreadyAff(getSession(), getNumeroIDE(), getAffilieNumero());
        } catch (Exception e) {
            isIDEPartage = false;
        } finally {
            super.setIdeRaisonSocialeb64(iDE_raisonSociale);
        }

    }

    public boolean isMessageAnnonceIdeCreationAjouteeToDisplay() {
        return isMessageAnnonceIdeCreationAjouteeToDisplay;
    }

    /**
     * Retourne true si WebAVS est connect? ? un EBusiness
     * 
     * @return
     * @throws PropertiesException
     */
    public boolean isEbusinessConnected() throws PropertiesException {
        return isEbusinessConnected;
    }

    /**
     * true si on souhaite afficher l'information qui indique si l'affili? a un compte EBusiness actif
     * 
     * @return
     */
    public boolean isWantDisplayIsAffilieEbusiness() {
        return wantDisplayIsAffilieEbusiness;
    }

    public void setWantDisplayIsAffilieEbusiness(boolean wantDisplayIsAffilieEbusiness) {
        this.wantDisplayIsAffilieEbusiness = wantDisplayIsAffilieEbusiness;
    }

}
