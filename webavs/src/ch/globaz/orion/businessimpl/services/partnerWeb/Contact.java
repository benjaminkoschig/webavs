package ch.globaz.orion.businessimpl.services.partnerWeb;

import ch.globaz.common.listoutput.converterImplemented.CodeSystemeConverter;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.common.listoutput.converterImplemented.PhoneConverter;
import ch.globaz.common.listoutput.converterImplemented.YesNoConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@Translater(value = LabelTranslater.class, identifier = "liste_contact")
@ColumnValueConverter(YesNoConverter.class)
public interface Contact {
    public static final String NUMERO_INFOROM = "0312CEB";

    @Column(name = "NoAffilie", order = 1, addEquals = true)
    public String getNumeroAffilie();

    @Column(name = "Nom", order = 2)
    public String getNom();

    @ColumnStyle(align = Align.CENTER)
    @Column(name = "DateDeRadaiation", order = 3)
    public String getDateRadiation();

    @Column(name = "CodeDeclaration", order = 4)
    @ColumnValueConverter(CodeSystemeConverter.class)
    public String getCodeDeclaration();

    @Column(name = "Contact", order = 5)
    public String getEmail();

    @Column(name = "NomUtilisateur", order = 6)
    public String getNomUser();

    @Column(name = "PrenomUtilisateur", order = 7)
    public String getPrenomUser();

    @ColumnValueConverter(PhoneConverter.class)
    @Column(name = "Telephone", order = 8)
    public String getTelephoneUser();

    @Column(name = "Utilisateur", order = 9)
    public String getUser();

    @ColumnStyle(align = Align.CENTER)
    @Column(name = "Actif", order = 10)
    public Boolean isActif();

    @ColumnStyle(align = Align.CENTER)
    @Column(name = "Administrateur", order = 11)
    public Boolean isAdministrateur();

    @Column(name = "NomMandataire", order = 12)
    public String getNomMandataire();

    @ColumnStyle(align = Align.CENTER)
    @Column(name = "Mandataire", order = 13)
    public Boolean isMandataire();

}
