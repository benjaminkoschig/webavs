package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class RPCDecionsPriseDansLeMois extends JadeComplexModel {

    private static final long serialVersionUID = 1L;

    private String idDemande;
    private Boolean isFratrie;
    private String idDossier;
    private String csRoleMembreFamille;
    private String csMotif;
    private String nssTiersBeneficiaire;
    private String idTiersRequerant;
    private String idPlanDeCalculParent;
    private String idPCAccordee;

    private SimpleDecisionHeader simpleDecisionHeader = new SimpleDecisionHeader();
    private SimpleVersionDroit simpleVersionDroit = new SimpleVersionDroit();
    private SimplePCAccordee simplePCAccordee = new SimplePCAccordee();
    private SimplePlanDeCalcul simplePlanDeCalcul = new SimplePlanDeCalcul();
    private PersonneSimpleModel simplePersonne = new PersonneSimpleModel();
    private TiersSimpleModel simpleTiers = new TiersSimpleModel();

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getCsRoleMembreFamille() {
        return csRoleMembreFamille;
    }

    public void setCsRoleMembreFamille(String csRoleMembreFamille) {
        this.csRoleMembreFamille = csRoleMembreFamille;
    }

    public String getIdPlanDeCalculParent() {
        return idPlanDeCalculParent;
    }

    public void setIdPlanDeCalculParent(String idPlanDeCalculParent) {
        this.idPlanDeCalculParent = idPlanDeCalculParent;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    public SimpleDecisionHeader getSimpleDecisionHeader() {
        return simpleDecisionHeader;
    }

    public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
        this.simpleDecisionHeader = simpleDecisionHeader;
    }

    public String getNssTiersBeneficiaire() {
        return nssTiersBeneficiaire;
    }

    public void setNssTiersBeneficiaire(String nssTiersBeneficiaire) {
        this.nssTiersBeneficiaire = nssTiersBeneficiaire;
    }

    public PersonneSimpleModel getSimplePersonne() {
        return simplePersonne;
    }

    public void setSimplePersonne(PersonneSimpleModel simplePersonne) {
        this.simplePersonne = simplePersonne;
    }

    public TiersSimpleModel getSimpleTiers() {
        return simpleTiers;
    }

    public void setSimpleTiers(TiersSimpleModel simpleTiers) {
        this.simpleTiers = simpleTiers;
    }

    public String getCsMotif() {
        return csMotif;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public String keyForGroup() {
        return simpleVersionDroit.getIdVersionDroit() + "_" + simpleDecisionHeader.getDateDebutDecision() + "_"
                + simpleDecisionHeader.getDateFinDecision();
    }

    public Boolean getIsFratrie() {
        return isFratrie;
    }

    public void setIsFratrie(Boolean isFratrie) {
        this.isFratrie = isFratrie;
    }

    @Override
    public String getId() {
        return simpleDecisionHeader.getId();
    }

    @Override
    public void setId(String id) {
        simpleDecisionHeader.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        simpleDecisionHeader.setSpy(spy);
    }

    @Override
    public String getSpy() {
        return simpleDecisionHeader.getSpy();
    }
}
