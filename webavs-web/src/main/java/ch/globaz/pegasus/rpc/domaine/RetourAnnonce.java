package ch.globaz.pegasus.rpc.domaine;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.rpc.business.models.SimpleLienAnnonceDecision;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.RetourAnnonceConverter;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;
import ch.globaz.pegasus.utils.RpcUtil;

/**
 * equivalent domaine du DecisionReturn
 * 
 * @author cel
 * 
 */
public class RetourAnnonce implements RetourAnnonceRpc {
    private String id;
    private String idLien;
    private SimpleLienAnnonceDecision idLienAnnonceDecision;
    private String periode;
    private RpcPlausiCategory categoriePlausi;
    private RpcPlausiType typePlausi;
    private TypeViolationPlausi typeViolationPlausi;
    private StatusRetourAnnonce status;
    private AnnonceRpc annonce;
    private String codePlausi;
    private String officePC;
    private String officePCConflit;
    private String nssAnnonce;
    private String nssPersonne;
    private String caseIdConflit;
    private String decisionIdConflit;
    private Date validFromConflit;
    private Date validToConflit;
    private Periode periodeDecision;
    private String remarque;
    private String spy;

    BSession session;

    public RetourAnnonce() {
        spy = new String();
    }

    public RetourAnnonce(String code, RpcPlausiCategory categorie, RpcPlausiType type, TypeViolationPlausi typeViolation) {
        codePlausi = code;
        categoriePlausi = categorie;
        typePlausi = type;
        typeViolationPlausi = typeViolation;
        status = StatusRetourAnnonce.A_TRAITER;

        session = BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void changeStatusTo(StatusRetourAnnonce etat) {
        setStatus(etat);
    }

    @Override
    public AnnonceRpc getAnnonce() {
        return annonce;
    }

    @Override
    public String getIdLien() {
        return idLien;
    }

    @Override
    public void setIdLien(String idLien) {
        this.idLien = idLien;
    }

    @Override
    public RpcPlausiType getType() {
        return typePlausi;
    }

    @Override
    public StatusRetourAnnonce getStatus() {
        return status;
    }

    @Override
    public RpcPlausiCategory getCategorie() {
        return categoriePlausi;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    @Override
    public String getNssPersonne() {
        return nssPersonne;
    }

    public String getNssPersonneFormatted() {
        return RpcUtil.formatNss(nssPersonne);
    }

    public void setNssPersonne(String nssPersonne) {
        this.nssPersonne = nssPersonne;
    }

    @Override
    public String getCaseIdConflit() {
        return caseIdConflit;
    }

    public void setCaseIdConflit(String caseIdConflit) {
        this.caseIdConflit = caseIdConflit;
    }

    @Override
    public String getDecisionIdConflit() {
        return decisionIdConflit;
    }

    public void setDecisionIdConflit(String decisionIdConflit) {
        this.decisionIdConflit = decisionIdConflit;
    }

    @Override
    public String getRemarque() {
        return remarque;
    }

    @Override
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    @Override
    public Date getValidFromConflit() {
        return validFromConflit;
    }

    public void setValidFromConflit(Date validFromConflit) {
        this.validFromConflit = validFromConflit;
    }

    @Override
    public Date getValidToConflit() {
        return validToConflit;
    }

    public void setValidToConflit(Date validToConflit) {
        this.validToConflit = validToConflit;
    }

    public RpcPlausiCategory getCategoriePlausi() {
        return categoriePlausi;
    }

    public void setCategoriePlausi(RpcPlausiCategory categoriePlausi) {
        this.categoriePlausi = categoriePlausi;
    }

    public RpcPlausiType getTypePlausi() {
        return typePlausi;
    }

    public void setTypePlausi(RpcPlausiType typePlausi) {
        this.typePlausi = typePlausi;
    }

    public void setTypeViolationPlausi(TypeViolationPlausi typeViolationPlausi) {
        this.typeViolationPlausi = typeViolationPlausi;
    }

    public void setStatus(StatusRetourAnnonce status) {
        this.status = status;
    }

    @Override
    public TypeViolationPlausi getTypeViolationPlausi() {
        return typeViolationPlausi;
    }

    @Override
    public String getOfficePC() {
        return officePC;
    }

    @Override
    public String getOfficePCConflit() {
        return officePCConflit;
    }

    @Override
    public String getNssAnnonce() {
        return nssAnnonce;
    }

    public String getNssAnnonceFormatted() {
        return RpcUtil.formatNss(nssAnnonce);
    }

    public void setOfficePC(String officePC) {
        this.officePC = officePC;
    }

    public void setOfficePCConflit(String officePCConflit) {
        this.officePCConflit = officePCConflit;
    }

    public void setNssAnnonce(String nssAnnonce) {
        this.nssAnnonce = nssAnnonce;
    }

    @Override
    public SimpleLienAnnonceDecision getIdLienAnnonceDecision() {
        return idLienAnnonceDecision;
    }

    @Override
    public void setIdLienAnnonceDecision(SimpleLienAnnonceDecision idLienAnnonceDecision) {
        this.idLienAnnonceDecision = idLienAnnonceDecision;
    }

    @Override
    public String getCodePlausi() {
        return codePlausi;
    }

    public boolean getCanTakeAction() {
        return getCategorie() == RpcPlausiCategory.WARNING;
    }

    public boolean getHasRemarque() {
        return !getRemarque().isEmpty();
    }

    private Periode getPeriodeDecision() {
        if (periodeDecision == null) {
            periodeDecision = new Periode(validFromConflit != null ? validFromConflit.getSwissValue() : "",
                    validToConflit != null ? validToConflit.getSwissValue() : "");
        }
        return periodeDecision;
    }

    public String getPeriode() {
        return getPeriodeDecision().getDateDebut() + " - " + getPeriodeDecision().getDateFin();
    }

    public String getEtat() {
        return RetourAnnonceConverter.translate(status, session);
    }

    public String getTypeViolation() {
        return RetourAnnonceConverter.translate(typeViolationPlausi, session);
    }

    public String getCategorieString() {
        return RetourAnnonceConverter.translate(categoriePlausi, session);
    }

    public String getTypeViolationTooltip() {
        switch (typeViolationPlausi) {
            case PERSON:
                return "vn:" + getNssPersonneFormatted();
            case OVERLAP:
                return "RPC case id:" + getCaseIdConflit() + " / decision:" + getDecisionIdConflit() + " / office:"
                        + getOfficePCConflit() + " / periode " + getPeriode().toString();
            default:
                return "";
        }
    }

    public String getIdDecision() {
        if (idLienAnnonceDecision != null) {
            return idLienAnnonceDecision.getIdDecision();
        } else {
            return "";
        }
    }

    public String getIdPlanCalcul() {
        if (idLienAnnonceDecision != null) {
            return idLienAnnonceDecision.getIdPlanCalcul();
        } else {
            return "";
        }
    }
}
