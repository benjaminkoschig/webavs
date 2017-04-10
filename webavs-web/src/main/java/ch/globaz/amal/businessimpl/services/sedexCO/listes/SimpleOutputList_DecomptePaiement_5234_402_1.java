package ch.globaz.amal.businessimpl.services.sedexCO.listes;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.CodeSystemeConverter;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ MontantConverterToDouble.class })
public class SimpleOutputList_DecomptePaiement_5234_402_1 {
    private String nssDebiteur = null;
    private String nomPrenomDebiteur = null;
    private Montant paiementDebiteur = null;
    private Montant rpRetro = null;
    private Montant annulation = null;
    private String typeActe = null;
    private String message = null;

    @Column(name = "NSS débiteur", order = 0)
    public String getNssDebiteur() {
        return nssDebiteur;
    }

    public void setNssDebiteur(String nssDebiteur) {
        this.nssDebiteur = nssDebiteur;
    }

    @Column(name = "Débiteur", order = 1)
    public String getNomPrenomDebiteur() {
        return nomPrenomDebiteur;
    }

    public void setNomPrenomDebiteur(String nomPrenomDebiteur) {
        this.nomPrenomDebiteur = nomPrenomDebiteur;
    }

    @Column(name = "Paiement débiteur", order = 3)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getPaiementDebiteur() {
        return paiementDebiteur;
    }

    public void setPaiementDebiteur(Montant paiementDebiteur) {
        this.paiementDebiteur = paiementDebiteur;
    }

    @Column(name = "RP rétroactive", order = 4)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRpRetro() {
        return rpRetro;
    }

    public void setRpRetro(Montant rpRetro) {
        this.rpRetro = rpRetro;
    }

    @Column(name = "Annulation", order = 5)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getAnnulation() {
        return annulation;
    }

    public void setAnnulation(Montant annulation) {
        this.annulation = annulation;
    }

    @Column(name = "Type", order = 6)
    @ColumnValueConverter(CodeSystemeConverter.class)
    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    @Column(name = "Message", order = 7)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
