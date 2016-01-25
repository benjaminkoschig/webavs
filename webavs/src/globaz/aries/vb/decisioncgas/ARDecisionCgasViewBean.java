package globaz.aries.vb.decisioncgas;

import globaz.aries.vb.ARAbstractDefaultViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;
import java.util.HashSet;
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.constantes.ARDecisionEtat;
import ch.globaz.aries.business.constantes.ARDecisionType;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.aries.exceptions.AriesNotImplementedException;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

public class ARDecisionCgasViewBean extends ARAbstractDefaultViewBean {

    private AffiliationSimpleModel affiliation = null;
    private String dateFacturationPassage = null;

    private HashSet<String> hashSetTypeDecisionExcept = new HashSet<String>();
    /** L'identifiant de la décision CGAS */
    private String id = null;
    private String idAffiliation = null;
    private String idDecisionCgasRectifiee;
    private String idPassageFacturation = null;
    private boolean lectureSeule = false;
    private String nomAffilie;
    private String numAffilie;
    private boolean optionDefinitive = false;
    private String typeAffilie;
    private String typeDecision;
    private boolean typeDecisionEnLectureSeule = false;
    private String warningAucunPassageDecisionCGASOuvert = null;

    public String getDateFacturationPassage() {
        return dateFacturationPassage;
    }

    public HashSet<String> getHashSetTypeDecisionExcept() {
        return hashSetTypeDecisionExcept;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdDecisionCgasRectifiee() {
        return idDecisionCgasRectifiee;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getInfoAffiliation() {
        return affiliation.getAffilieNumero() + " " + affiliation.getRaisonSocialeCourt();
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

    public String getWarningAucunPassageDecisionCGASOuvert() {
        return warningAucunPassageDecisionCGASOuvert;
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
        typeDecision = ARDecisionType.DEFINITIVE.getCodeSystem();

        hashSetTypeDecisionExcept.add(ARDecisionType.PROVISOIRE_RECTIFICATIVE.getCodeSystem());
        hashSetTypeDecisionExcept.add(ARDecisionType.DEFINITIVE_RECTIFICATIVE.getCodeSystem());
        hashSetTypeDecisionExcept.add(ARDecisionType.PROVISOIRE_DEFINITIVE.getCodeSystem());

        if (!JadeStringUtil.isBlankOrZero(id)) {
            DecisionCGASBean decisionCGASBean = AriesServiceLocator.getDecisionCGASService().read(id);
            lectureSeule = !ARDecisionEtat.isEtatModifiable(decisionCGASBean.getDecisionCGAS().getEtat());
        }

        typeDecisionEnLectureSeule = false;
        if (!JadeStringUtil.isBlankOrZero(getIdDecisionCgasRectifiee())) {
            typeDecisionEnLectureSeule = true;
            hashSetTypeDecisionExcept.clear();
        }

        if (!JadeStringUtil.isBlankOrZero(idAffiliation)) {
            affiliation = AFBusinessServiceLocator.getAffiliationService().read(getIdAffiliation());

            setNumAffilie(affiliation.getAffilieNumero());
            setNomAffilie(affiliation.getRaisonSocialeCourt());
            setTypeAffilie(affiliation.getTypeAffiliation());
        }

        if (JadeStringUtil.isBlankOrZero(id)) {
            // si il s'agit de la création d'un rectificatif on ajout l'id de la décision à rectifiée
            if (!JadeStringUtil.isBlankOrZero(getIdDecisionCgasRectifiee())) {

                DecisionCGASBean decisionCGASBeanRectifiee = AriesServiceLocator.getDecisionCGASService().read(
                        getIdDecisionCgasRectifiee());

                if (ARDecisionType.PROVISOIRE.equals(decisionCGASBeanRectifiee.getDecisionCGAS().getType())
                        || ARDecisionType.PROVISOIRE_RECTIFICATIVE.equals(decisionCGASBeanRectifiee.getDecisionCGAS()
                                .getType())) {
                    setTypeDecision(ARDecisionType.PROVISOIRE_RECTIFICATIVE.getCodeSystem());
                    if (optionDefinitive) {
                        setTypeDecision(ARDecisionType.PROVISOIRE_DEFINITIVE.getCodeSystem());
                    }
                } else if (ARDecisionType.DEFINITIVE.equals(decisionCGASBeanRectifiee.getDecisionCGAS().getType())
                        || ARDecisionType.DEFINITIVE_RECTIFICATIVE.equals(decisionCGASBeanRectifiee.getDecisionCGAS()
                                .getType())
                        || ARDecisionType.PROVISOIRE_DEFINITIVE.equals(decisionCGASBeanRectifiee.getDecisionCGAS()
                                .getType())) {
                    setTypeDecision(ARDecisionType.DEFINITIVE_RECTIFICATIVE.getCodeSystem());
                } else {
                    throw new AriesNotImplementedException();
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
                warningAucunPassageDecisionCGASOuvert = JadeThread
                        .getMessage("aries.decisioncgas.avertissement.pas.passage.facturation.ouvert");
            }
        }

    }

    @Override
    public void setId(String newId) {
        id = newId;

    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdDecisionCgasRectifiee(String idDecisionCgasRectifiee) {
        this.idDecisionCgasRectifiee = idDecisionCgasRectifiee;
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

}
