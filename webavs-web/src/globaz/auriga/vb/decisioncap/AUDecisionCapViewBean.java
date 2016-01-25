package globaz.auriga.vb.decisioncap;

import globaz.auriga.bean.EnfantDecisionCapBean;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.constantes.AUDecisionType;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.auriga.business.models.SimpleEnfantDecisionCAP;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.auriga.exceptions.AurigaNotImplementedException;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AUDecisionCapViewBean extends BJadePersistentObjectViewBean {

    private AUDecisionCapAjaxViewBean ajaxViewBean = new AUDecisionCapAjaxViewBean();
    private String dateFacturationPassage = null;
    private HashSet<String> hashSetTypeDecisionExcept = new HashSet<String>();
    private String idAffilie; // recu en param via l'action "afficher"
    private String idDecisionCap;
    private String idDecisionCapRectifiee;
    private String idPassageFacturation = null;
    private String idTiersAffilie;
    private boolean lectureSeule = false;
    private String nomAffilie;
    private String numAffilie;
    private boolean optionDefinitive = false;
    private String typeAffilie;
    private String typeDecision;
    private boolean typeDecisionEnLectureSeule = false;
    private String warningAucunPassageDecisionCAPOuvert = null;

    @Override
    public void add() throws Exception {
        throw new AurigaNotImplementedException();
    }

    @Override
    public void delete() throws Exception {
        throw new AurigaNotImplementedException();
    }

    public String getAssuranceLibelle() {
        return ajaxViewBean.getAssuranceLibelle();
    }

    public String getDateFacturationPassage() {
        return dateFacturationPassage;
    }

    public SimpleDecisionCAP getDecisionCap() {
        return ajaxViewBean.getDecisionCap();
    }

    public List<EnfantDecisionCapBean> getEnfants() {
        return ajaxViewBean.getListEnfantDecisionCapBean();
    }

    public HashSet<String> getHashSetTypeDecisionExcept() {
        return hashSetTypeDecisionExcept;
    }

    @Override
    public String getId() {
        return getIdDecisionCap();
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdDecisionCap() {
        return idDecisionCap;
    }

    public String getIdDecisionCapRectifiee() {
        return idDecisionCapRectifiee;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdTiersAffilie() {
        return idTiersAffilie;
    }

    public String getListIdEnfants() {
        return ajaxViewBean.getListIdEnfants();
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getTypeAffilie() {
        return typeAffilie;
    }

    public String getTypeAffilieLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(getTypeAffilie());
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    public String getWarningAucunPassageDecisionCAPOuvert() {
        return warningAucunPassageDecisionCAPOuvert;
    }

    public boolean isLectureSeule() {
        return lectureSeule;
    }

    public boolean isOptionDefinitive() {
        return optionDefinitive;
    }

    public boolean isTypeDecisionEnLectureSeule() {
        return typeDecisionEnLectureSeule;
    }

    @Override
    public void retrieve() throws Exception {
        // par défaut la décision est définitive
        typeDecision = AUDecisionType.DEFINITIVE.getCodeSystem();

        if (!JadeStringUtil.isBlankOrZero(idDecisionCap)) {
            SimpleDecisionCAP decisionCap = AurigaServiceLocator.getDecisionCAPService().read(idDecisionCap);
            lectureSeule = !AUDecisionEtat.isEtatModifiable(decisionCap.getEtat());
        }

        hashSetTypeDecisionExcept.add(AUDecisionType.PROVISOIRE_RECTIFICATIVE.getCodeSystem());
        hashSetTypeDecisionExcept.add(AUDecisionType.DEFINITIVE_RECTIFICATIVE.getCodeSystem());
        hashSetTypeDecisionExcept.add(AUDecisionType.PROVISOIRE_DEFINITIVE.getCodeSystem());

        typeDecisionEnLectureSeule = false;
        if (!JadeStringUtil.isBlankOrZero(getIdDecisionCapRectifiee())) {
            typeDecisionEnLectureSeule = true;
            hashSetTypeDecisionExcept.clear();
        }

        // si on a un id affilié on charge l'affilié (create décision)
        if (!JadeStringUtil.isBlankOrZero(idAffilie)) {
            System.out.println("chargement des informations pour l'id affilié : " + idAffilie);
            AffiliationSimpleModel affiliation = AFBusinessServiceLocator.getAffiliationService().read(idAffilie);

            setIdTiersAffilie(affiliation.getIdTiers());
            setNumAffilie(affiliation.getAffilieNumero());
            setNomAffilie(affiliation.getRaisonSocialeCourt());
            setTypeAffilie(affiliation.getTypeAffiliation());
        }

        // si on a un idDecision on charge l'id de la décision dans l'ajaxViewBean
        if (!JadeStringUtil.isBlankOrZero(idDecisionCap)) {
            ajaxViewBean.getDecisionCap().setId(idDecisionCap);
            ajaxViewBean.retrieve();
        } else {
            // si il s'agit de la création d'un rectificatif on choisi le bon type de décision et on charge
            // éventuellement les enfants
            if (!JadeStringUtil.isBlankOrZero(getIdDecisionCapRectifiee())) {
                SimpleDecisionCAP decisionCapRectifiee = AurigaServiceLocator.getDecisionCAPService().read(
                        getIdDecisionCapRectifiee());

                if (AUDecisionType.PROVISOIRE.equals(decisionCapRectifiee.getType())
                        || AUDecisionType.PROVISOIRE_RECTIFICATIVE.equals(decisionCapRectifiee.getType())) {
                    setTypeDecision(AUDecisionType.PROVISOIRE_RECTIFICATIVE.getCodeSystem());
                    if (optionDefinitive) {
                        setTypeDecision(AUDecisionType.PROVISOIRE_DEFINITIVE.getCodeSystem());
                    }
                } else if (AUDecisionType.DEFINITIVE.equals(decisionCapRectifiee.getType())
                        || AUDecisionType.DEFINITIVE_RECTIFICATIVE.equals(decisionCapRectifiee.getType())
                        || AUDecisionType.PROVISOIRE_DEFINITIVE.equals(decisionCapRectifiee.getType())) {

                    setTypeDecisionEnLectureSeule(false);
                    hashSetTypeDecisionExcept.add(AUDecisionType.PROVISOIRE.getCodeSystem());
                    hashSetTypeDecisionExcept.add(AUDecisionType.DEFINITIVE.getCodeSystem());
                    hashSetTypeDecisionExcept.add(AUDecisionType.PROVISOIRE_DEFINITIVE.getCodeSystem());

                } else {
                    throw new AurigaNotImplementedException();
                }

                if (CodeSystem.TYPE_ASS_CAP_10.equalsIgnoreCase(decisionCapRectifiee.getCategorie())
                        || CodeSystem.TYPE_ASS_CAP_20.equalsIgnoreCase(decisionCapRectifiee.getCategorie())) {

                    // chargement des enfants de la décision
                    List<SimpleEnfantDecisionCAP> listEnfantDecisionCap = new ArrayList<SimpleEnfantDecisionCAP>();
                    listEnfantDecisionCap = AurigaServiceLocator.getEnfantDecisionCAPService()
                            .getListEnfantDecisionCap(decisionCapRectifiee.getIdDecision());

                    // recherche des informations sur les enfants
                    if (listEnfantDecisionCap.size() > 0) {
                        for (SimpleEnfantDecisionCAP enfantDecisionCap : listEnfantDecisionCap) {
                            // recherche du tiers complex pour les informations sur les enfants
                            PersonneEtendueComplexModel tiersComplex = TIBusinessServiceLocator
                                    .getPersonneEtendueService().read(enfantDecisionCap.getIdTiers());

                            // création du bean
                            EnfantDecisionCapBean enfantDecisionCapBean = new EnfantDecisionCapBean(
                                    enfantDecisionCap.getIdEnfantDecision(), enfantDecisionCap.getIdDecision(),
                                    enfantDecisionCap.getIdTiers(), enfantDecisionCap.getDateNaissance(),
                                    enfantDecisionCap.getDateRadiation(), enfantDecisionCap.getMontant(), tiersComplex
                                            .getTiers().getDesignation1(), tiersComplex.getTiers().getDesignation2());

                            ajaxViewBean.getListEnfantDecisionCapBean().add(enfantDecisionCapBean);
                            ajaxViewBean.putIdEnfantToListIdEnfants(enfantDecisionCap.getIdTiers());
                        }
                    }

                }

            }

            FAPassageModuleManager modulePassageManager = new FAPassageModuleManager();
            modulePassageManager.setSession(BSessionUtil.getSessionFromThreadContext());
            modulePassageManager.setForStatus(FAPassage.CS_ETAT_OUVERT);
            modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);

            modulePassageManager.find();

            if (modulePassageManager.size() >= 1) {
                FAPassageModule thePassageModule = (FAPassageModule) modulePassageManager.getFirstEntity();
                idPassageFacturation = thePassageModule.getIdPassage();

                PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(idPassageFacturation);
                dateFacturationPassage = thePassage.getDateFacturation();
            }

            if (JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
                warningAucunPassageDecisionCAPOuvert = JadeThread
                        .getMessage("auriga.decisioncap.avertissement.pas.passage.facturation.ouvert");
            }
        }
    }

    public void setHashSetTypeDecisionExcept(HashSet<String> hashSetTypeDecisionExcept) {
        this.hashSetTypeDecisionExcept = hashSetTypeDecisionExcept;
    }

    @Override
    public void setId(String newId) {
        setIdDecisionCap(newId);
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdDecisionCap(String idDecisionCap) {
        this.idDecisionCap = idDecisionCap;
    }

    public void setIdDecisionCapRectifiee(String idDecisionCapRectifiee) {
        this.idDecisionCapRectifiee = idDecisionCapRectifiee;
    }

    public void setIdTiersAffilie(String idTiersAffilie) {
        this.idTiersAffilie = idTiersAffilie;
    }

    public void setLectureSeule(boolean lectureSeule) {
        this.lectureSeule = lectureSeule;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setOptionDefinitive(boolean optionDefinitive) {
        this.optionDefinitive = optionDefinitive;
    }

    public void setTypeAffilie(String typeAffilie) {
        this.typeAffilie = typeAffilie;
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    public void setTypeDecisionEnLectureSeule(boolean typeDecisionEnLectureSeule) {
        this.typeDecisionEnLectureSeule = typeDecisionEnLectureSeule;
    }

    @Override
    public void update() throws Exception {
        throw new AurigaNotImplementedException();
    }

}
