package ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.models.lot.OrdreversementTiers;

public class OrdreVersementStandardDisplay extends OrdreVersementDisplay {

    private String id;
    private String idDomainApplication;
    private String idRefRurbrique;
    private String idTiersAdressePaiement;
    private boolean isDom2R;
    private OrdreversementTiers tiersBeneficiaire;

    public OrdreVersementStandardDisplay(String idDomainApplication, String idTiersAdressePaiement, BigDecimal montant,
            OrdreversementTiers tiersBeneficiaire, String id, String csTypeOv, String idRefRurbrique, boolean isDom2R) {
        super(csTypeOv, montant);
        this.idDomainApplication = idDomainApplication;
        this.idTiersAdressePaiement = idTiersAdressePaiement;
        this.tiersBeneficiaire = tiersBeneficiaire;
        this.idRefRurbrique = idRefRurbrique;
        this.isDom2R = isDom2R;
        this.id = id;
    }

    public OrdreVersementStandardDisplay(String idDomainApplication, String idTiersAdressePaiement, BigDecimal montant,
            OrdreversementTiers tiersBeneficiaire, String id, String csTypeOv, String idRefRurbrique, boolean isDom2R,
            String noPeriode, boolean isRequerant) {
        super(csTypeOv, montant, noPeriode, isRequerant);
        this.idDomainApplication = idDomainApplication;
        this.idTiersAdressePaiement = idTiersAdressePaiement;
        this.tiersBeneficiaire = tiersBeneficiaire;
        this.idRefRurbrique = idRefRurbrique;
        this.isDom2R = isDom2R;
        this.id = id;
    }

    @Override
    public String getDescriptionOv() {
        if (tiersBeneficiaire == null) {
            return null;
        }

        if (JadeStringUtil.isBlankOrZero(tiersBeneficiaire.getNumAvs())) {
            return tiersBeneficiaire.getDesignation1() + " " + tiersBeneficiaire.getDesignation2();
        } else {
            return tiersBeneficiaire.getNumAvs() + " / " + tiersBeneficiaire.getDesignation1() + " "
                    + tiersBeneficiaire.getDesignation2();
        }

    }

    @Override
    public String getId() {
        return idTiersAdressePaiement + "_" + tiersBeneficiaire.getIdTiers() + "_" + getCsTypeOv() + "_"
                + idRefRurbrique + "_id:" + id;
    }

    public String getIdDomainApplication() {
        return idDomainApplication;
    }

    public String getIdRefRurbrique() {
        return idRefRurbrique;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public boolean getIsDom2R() {
        return isDom2R;
    }

}
