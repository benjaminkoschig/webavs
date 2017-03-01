package globaz.osiris.db.ordres;

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

    public ContainerOrdreRejete(List<CAOrdreRejete> ordreRejetes, CAOVforOR op) {
        beneficiaire = op.getBeneficiaire();
        compteAnnexe = op.getCompteAnnexe();
        adresseVersement = op.getAdresseVersement();
        natureVersement = op.getNatureVersement();
        montant = op.getMontant();
        numTrans = op.getNumTrans();

        StringBuilder motifErreurSB = new StringBuilder();
        for (CAOrdreRejete ordreRejete : ordreRejetes) {
            if (motifErreurSB.length() != 0) {
                motifErreurSB.append(" | ");
            }
            motifErreurSB.append(ordreRejete.getCode() + " - " + ordreRejete.getAdditionalInformations());
        }
        motifErreur = motifErreurSB.toString();
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
        this.beneficiaire = beneficiaire;
    }

    public void setNatureVersement(String natureVersement) {
        this.natureVersement = natureVersement;
    }

    public void setAdresseVersement(String adresseVersement) {
        this.adresseVersement = adresseVersement;
    }

    public void setCompteAnnexe(String compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }

    public void setMotifErreur(String motifErreur) {
        this.motifErreur = motifErreur;
    }

}