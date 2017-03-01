package globaz.osiris.db.ordres;

import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.db.ordres.sepa.utils.CASepaOVConverterUtils;
import globaz.osiris.external.IntAdressePaiement;
import ch.globaz.common.domaine.Montant;

public class CAOVforOR {

    private String beneficiaire;
    private String compteAnnexe;
    private String adresseVersement;
    private String natureVersement;
    private Montant montant;
    private String numTrans;

    public CAOVforOR(APICommonOdreVersement op) throws Exception {
        IntAdressePaiement adp = op.getAdressePaiement();
        beneficiaire = CASepaOVConverterUtils.getBeneficiaire(op) + ", " + adp.getAdresseCourrier().getPaysISO() + "-"
                + adp.getAdresseCourrier().getNumPostal() + " " + adp.getAdresseCourrier().getLocalite();
        compteAnnexe = op.getNomPrenom();
        adresseVersement = adp.getNumCompte();
        natureVersement = op.getMotif();
        Montant montantOp = new Montant(op.getMontant());
        setMontant(montantOp);
        numTrans = op.getNumTransaction();
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getCompteAnnexe() {
        return compteAnnexe;
    }

    public void setCompteAnnexe(String compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    public String getAdresseVersement() {
        return adresseVersement;
    }

    public void setAdresseVersement(String adresseVersement) {
        this.adresseVersement = adresseVersement;
    }

    public String getNatureVersement() {
        return natureVersement;
    }

    public void setNatureVersement(String natureVersement) {
        this.natureVersement = natureVersement;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getNumTrans() {
        return numTrans;
    }

    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }

}
