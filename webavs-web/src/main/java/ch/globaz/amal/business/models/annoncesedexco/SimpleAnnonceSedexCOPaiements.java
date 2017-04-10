package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonceSedexCOPaiements extends JadeSimpleModel {
    private static final long serialVersionUID = 4965952421457591723L;
    private String idAnnonceSedexCOPaiement = null;
    private String idAnnonceSedexCODebiteur = null;
    private String paiementTotalAmout = null;
    private String paiementCategory = null;

    @Override
    public String getId() {
        return idAnnonceSedexCOPaiement;
    }

    @Override
    public void setId(String id) {
        idAnnonceSedexCOPaiement = id;
    }

    public String getIdAnnonceSedexCOPaiement() {
        return idAnnonceSedexCOPaiement;
    }

    public void setIdAnnonceSedexCOPaiement(String idAnnonceSedexCOPaiement) {
        this.idAnnonceSedexCOPaiement = idAnnonceSedexCOPaiement;
    }

    public String getIdAnnonceSedexCODebiteur() {
        return idAnnonceSedexCODebiteur;
    }

    public void setIdAnnonceSedexCODebiteur(String idAnnonceSedexCODebiteur) {
        this.idAnnonceSedexCODebiteur = idAnnonceSedexCODebiteur;
    }

    public String getPaiementTotalAmout() {
        return paiementTotalAmout;
    }

    public void setPaiementTotalAmout(String paiementTotalAmout) {
        this.paiementTotalAmout = paiementTotalAmout;
    }

    public String getPaiementCategory() {
        return paiementCategory;
    }

    public void setPaiementCategory(String paiementCategory) {
        this.paiementCategory = paiementCategory;
    }

}
