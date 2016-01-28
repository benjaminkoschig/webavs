package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public class Ecriture {
    private String codeDebitCredit;
    private CompteAnnexeSimpleModel compteAnnexe;
    private String csTypeRoleFamille;
    private String idRefRubrique;
    private BigDecimal montant;
    private Integer noPeriode;
    private OrdreVersement ordreVersement;
    private SectionPegasus section;
    private SectionSimpleModel sectionSimple;
    private TypeEcriture typeEcriture;

    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public CompteAnnexeSimpleModel getCompteAnnexe() {
        return compteAnnexe;
    }

    public String getCsTypeOv() {
        if (ordreVersement == null) {
            return null;
        }
        return ordreVersement.getCsType();
    }

    public String getCsTypeRoleFamille() {
        return csTypeRoleFamille;
    }

    public String getId() {
        if (ordreVersement == null) {
            return null;
        }
        return ordreVersement.getId();
    }

    public String getIdCompteAnnexe() {
        if (compteAnnexe == null) {
            return null;
        }
        return compteAnnexe.getIdCompteAnnexe();
    }

    public String getIdDomaineApplication() {
        if (ordreVersement == null) {
            return null;
        }
        if (isDom2RConjoint()) {
            return ordreVersement.getIdDomaineApplicationConjoint();
        } else {
            return ordreVersement.getIdDomaineApplication();
        }
    }

    public String getIdRefRubrique() {
        return idRefRubrique;
    }

    public String getIdTiersAdressePaiement() {
        if (ordreVersement == null) {
            return null;
        }
        if (isDom2RConjoint()) {
            return ordreVersement.getIdTiersAdressePaiementConjoint();
        } else {
            return ordreVersement.getIdTiersAdressePaiement();
        }
    }

    public String getIdTiersOv() {
        if (ordreVersement == null) {
            return null;
        }
        if (isDom2RConjoint()) {
            return ordreVersement.getIdTiersConjoint();
        } else {
            return ordreVersement.getIdTiers();
        }
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public Integer getNoPeriode() {
        return noPeriode;
    }

    public OrdreVersement getOrdreVersement() {
        return ordreVersement;
    }

    public SectionPegasus getSection() {
        return section;
    }

    public SectionSimpleModel getSectionSimple() {
        return sectionSimple;
    }

    public TypeEcriture getTypeEcriture() {
        return typeEcriture;
    }

    public boolean isCojoint() {
        return IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(csTypeRoleFamille);
    }

    public boolean isDom2R() {
        if (ordreVersement != null) {
            return ordreVersement.isDom2R();
        } else {
            return false;
        }
    }

    private boolean isDom2RConjoint() {
        return isDom2R() & isCojoint();
    }

    public boolean isRequerant() {
        return IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(csTypeRoleFamille);
    }

    public void setCodeDebitCredit(String codeDebitCredit) {
        this.codeDebitCredit = codeDebitCredit;
    }

    public void setCompteAnnexe(CompteAnnexeSimpleModel compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    public void setCsTypeRoleFamille(String csTypeRoleFamille) {
        this.csTypeRoleFamille = csTypeRoleFamille;
    }

    public void setIdRefRubrique(String idRefRubrique) {
        this.idRefRubrique = idRefRubrique;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setNoPeriode(Integer noPeriode) {
        this.noPeriode = noPeriode;
    }

    public void setOrdreVersement(OrdreVersement ordreVersement) {
        this.ordreVersement = ordreVersement;
    }

    public void setSection(SectionPegasus section) {
        this.section = section;
    }

    public void setSectionSimple(SectionSimpleModel sectionSimple) {
        this.sectionSimple = sectionSimple;
    }

    public void setTypeEcriture(TypeEcriture typeEcriture) {
        this.typeEcriture = typeEcriture;
    }

    @Override
    public String toString() {
        return "Ecriture [codeDebitCredit=" + codeDebitCredit + ",  montant=" + montant + ", idRefRubrique="
                + idRefRubrique + ", idCompteAnnexe=" + compteAnnexe.getIdCompteAnnexe() + " , section=" + section
                + ", sectionSimple=" + sectionSimple + "]";
    }

}
