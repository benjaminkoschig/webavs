package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import java.math.BigDecimal;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public class OrdreVersementCompta {

    private final CompteAnnexeSimpleModel compteAnnexe;
    private final String csRoleFamille;
    private final String csTypeOv;
    private final String idDomaineApplication;
    private final String idTiers;
    private final String idTiersAdressePaiement;
    private final String referencePaiement;

    public String getReferencePaiement() {
        return referencePaiement;
    }

    private BigDecimal montant;
    private SectionPegasus section;
    private SectionSimpleModel simpleSection;

    public OrdreVersementCompta(CompteAnnexeSimpleModel compteAnnexeSimpleModel, String idAdressePaiement,
            String idDomaineApplication, BigDecimal montant, SectionPegasus section, String idTiers, String csTypeOv,
            String csRoleFamille, String referencePaiement) {
        super();
        compteAnnexe = compteAnnexeSimpleModel;
        idTiersAdressePaiement = idAdressePaiement;
        this.idDomaineApplication = idDomaineApplication;
        this.montant = montant;
        this.section = section;
        this.idTiers = idTiers;
        this.csTypeOv = csTypeOv;
        this.csRoleFamille = csRoleFamille;
        this.referencePaiement = referencePaiement;
    }

    public OrdreVersementCompta(CompteAnnexeSimpleModel compteAnnexeSimpleModel, String idAdressePaiement,
            String idDomaineApplication, BigDecimal montant, SectionSimpleModel section, String idTiers,
            String csTypeOv, String csRoleFamille, String referencePaiement) {
        super();
        compteAnnexe = compteAnnexeSimpleModel;
        idTiersAdressePaiement = idAdressePaiement;
        this.idDomaineApplication = idDomaineApplication;
        this.montant = montant;
        simpleSection = section;
        this.idTiers = idTiers;
        this.csTypeOv = csTypeOv;
        this.csRoleFamille = csRoleFamille;
        this.referencePaiement = referencePaiement;
    }

    public CompteAnnexeSimpleModel getCompteAnnexe() {
        return compteAnnexe;
    }

    public String getCsRoleFamille() {
        return csRoleFamille;
    }

    public String getCsTypeOv() {
        return csTypeOv;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public SectionPegasus getSection() {
        return section;
    }

    public SectionSimpleModel getSimpleSection() {
        return simpleSection;
    }

    public boolean isRequerant() {
        return IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(csRoleFamille);
    }

    public boolean isTypeBeneficiaire() {
        return IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(csTypeOv);
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

}
