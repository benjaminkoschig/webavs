package globaz.osiris.db.ordres;

import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.db.ordres.sepa.utils.CASepaOVConverterUtils;
import java.math.BigDecimal;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.simpleoutputlist.annotation.Aggregate;
import ch.globaz.simpleoutputlist.annotation.AggregateFunction;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter(MontantConverter.class)
@Translater(value = LabelTranslater.class, identifier = "OSIRIS_LISTE_ORDREREJETE")
public class ContainerOrdreRejete {
    private String beneficiaire;
    private String compteAnnexe;
    private String adresseVersement;
    private String natureVersement;
    private Montant montant;
    private String numTrans;
    private String motifErreur;
    private int count = 0;

    public ContainerOrdreRejete(List<CAOrdreRejete> ordreRejetes, APICommonOdreVersement op) throws Exception {
        beneficiaire = CASepaOVConverterUtils.getBeneficiaire(op) + ", "
                + op.getAdressePaiement().getAdresseCourrier().getPaysISO() + "-"
                + op.getAdressePaiement().getAdresseCourrier().getNumPostal() + " "
                + op.getAdressePaiement().getAdresseCourrier().getLocalite();
        compteAnnexe = op.getNomPrenom();
        adresseVersement = op.getAdressePaiement().getNumCompte();
        natureVersement = op.getMotif();
        Montant montant = new Montant(op.getMontant());
        setMontant(montant);
        numTrans = op.getNumTransaction();
        motifErreur = "";
        for (CAOrdreRejete ordreRejete : ordreRejetes) {
            if (!motifErreur.isEmpty()) {
                motifErreur += " | ";
            }
            motifErreur += ordreRejete.getCode() + " - " + ordreRejete.getAdditionalInformations();
        }
        count++;
    }

    @Column(name = "Beneficiaire", order = 1)
    public String getBeneficiaire() {
        return beneficiaire;
    }

    @Column(name = "CompteAnnexe", order = 2)
    public String getCompteAnnexe() {
        return compteAnnexe;
    }

    @Column(name = "AdresseVersement", order = 3)
    public String getAdresseVersement() {
        return adresseVersement;
    }

    @Column(name = "NatureVersement", order = 4)
    public String getNatureVersement() {
        return natureVersement;
    }

    @Column(name = "Montant", order = 5)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getMontant() {
        return montant.getBigDecimalValue();
    }

    @Column(name = "NumTrans", order = 6)
    @ColumnStyle(align = Align.RIGHT)
    public String getNumTrans() {
        return numTrans;
    }

    @Column(name = "motifErreur", order = 7)
    public String getMotifErreur() {
        return motifErreur;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public void setBeneficiaire(String beneficiaire) {
        beneficiaire = beneficiaire;
    }

    public void setNatureVersement(String natureVersement) {
        natureVersement = natureVersement;
    }

    public void setAdresseVersement(String adresseVersement) {
        adresseVersement = adresseVersement;
    }

    public void setCompteAnnexe(String compteAnnexe) {
        compteAnnexe = compteAnnexe;
    }

    public void setNumTrans(String numTrans) {
        numTrans = numTrans;
    }

    public void setMotifErreur(String motifErreur) {
        this.motifErreur = motifErreur;
    }

}