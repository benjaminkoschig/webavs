package ch.globaz.pegasus.businessimpl.services.process.adaptation;

import ch.globaz.common.domaine.Adresse;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.CodeSystemeConverter;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.NullValue;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter(MontantConverter.class)
public class PcaForImpression {

    private Pca nouvellePca;
    private Pca anciennePca;
    private Adresse adresseCourrier;
    private Adresse adresseDomicile;
    private InfoAdaptation infoAdaptation;
    private PersonneAVS beneficiaire;
    private Long idTiersBeneficiaire;
    private Home home;
    private Adresse adresseHome;

    public PcaForImpression(Pca nouvellePca, Pca anciennePca, Adresse adresseCourrier, Adresse adresseDomicile,
            InfoAdaptation infoAdaptation, MembreFamille beneficiaire, String idTiers, Home home, Adresse adresseHome) {
        this.nouvellePca = nouvellePca;
        this.anciennePca = anciennePca;
        this.adresseCourrier = adresseCourrier;
        this.infoAdaptation = infoAdaptation;
        this.beneficiaire = beneficiaire.getPersonne();
        this.adresseDomicile = adresseDomicile;
        this.home = home;
        this.adresseHome = adresseHome;
        if (idTiers != null && !idTiers.trim().isEmpty()) {
            idTiersBeneficiaire = Long.valueOf(idTiers);
        }
    }

    @Column(name = "NSS", order = 1)
    public String getNss() {
        try {
            return beneficiaire.getNss().toString();
        } catch (IllegalStateException e) {
            return "";
        }
    }

    @Column(name = "Nom", order = 2)
    public String getNom() {
        return beneficiaire.getNom();
    }

    @Column(name = "Prénom", order = 3)
    public String getPrenom() {
        return beneficiaire.getPrenom();
    }

    @Column(name = "EtatAncienCalcul", order = 30)
    public PcaEtatCalcul getEtatAncienCalcul() {
        return anciennePca.getEtatCalcul();
    }

    @Column(name = "EtatNouveauCalcul", order = 31)
    public PcaEtatCalcul getEtatNouveauCalcul() {
        return nouvellePca.getEtatCalcul();
    }

    @Column(name = "ChangementSituation", order = 32)
    public Boolean getChangmentSituation() {
        return !anciennePca.getEtatCalcul().equals(nouvellePca.getEtatCalcul());
    }

    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "MontantAnciennePca", order = 33, nullValue = NullValue.ZERO)
    public Montant getAncienMontant() {
        return anciennePca.getMontant();
    }

    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "MontantNouvellePca", order = 34, nullValue = NullValue.ZERO)
    public Montant getMontant() {
        return nouvellePca.getMontant();
    }

    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "EcartAbsolu", order = 35)
    public Float getEcart() {
        Float montant = anciennePca.getMontant().substract(nouvellePca.getMontant()).getMontantAbsolu()
                .getBigDecimalValue().setScale(2).floatValue();

        return montant;
    }

    @Column(name = "TypePca", order = 40)
    public String getType() {
        return nouvellePca.getType().toString();
    }

    @ColumnValueConverter(SousCodeGenrePresationConverter.class)
    @Column(name = "SASH_SPAS", order = 41)
    public String getSousCode() {
        return nouvellePca.getSousCode();
    }

    @Column(name = "GenrePca", order = 50)
    public String getGenre() {
        return nouvellePca.getGenre().toString();
    }

    @Column(name = "AdressseDomicileRue", order = 60)
    public String getAdresseDomicileRue() {
        return adresseDomicile.getRue();
    }

    @Column(name = "AdressseDomicileNumero", order = 61)
    public String getAdresseDomicileNumero() {
        return adresseDomicile.getRueNumero();
    }

    @Column(name = "AdressseDomicileNPA", order = 62)
    public String getAdresseDomicileNpa() {
        return adresseDomicile.getNpa();
    }

    @Column(name = "AdressseDomicileLocalite", order = 63)
    public String getAdresseDomicileLocalite() {
        return adresseDomicile.getLocalite();
    }

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "AdressseDomicileTitre", order = 64)
    public String getAdresseDomicileTitre() {
        return adresseDomicile.resolveCsTitre();
    }

    @Column(name = "AdresseDomicileAttention", order = 65)
    public String getAdresseDomicileAttention() {
        return adresseDomicile.getAttention();
    }

    @Column(name = "AdressseDomicileCasePostale", order = 66)
    public String getAdresseDomicileCasePostal() {
        return adresseDomicile.getCasePostale();
    }

    @Column(name = "AdressseDomicileNom", order = 67)
    public String getAdresseDomicileNom() {
        return adresseDomicile.resolveDesignation1();
    }

    @Column(name = "AdressseDomicilePrénom", order = 68)
    public String getAdresseDomicilePrenom() {
        return adresseDomicile.resolveDesignation2();
    }

    @Column(name = "AdressseCourrierRue", order = 70)
    public String getAdresseCourrierRue() {
        return adresseCourrier.getRue();
    }

    @Column(name = "AdressseCourrierNumero", order = 71)
    public String getAdresseCourrierNumero() {
        return adresseCourrier.getRueNumero();
    }

    @Column(name = "AdressseCourrierNPA", order = 72)
    public String getAdresseCourrierNpa() {
        return adresseCourrier.getNpa();
    }

    @Column(name = "AdressseCourrierLocalite", order = 73)
    public String getAdresseCourrierLocalite() {
        return adresseCourrier.getLocalite();
    }

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "AdressseCourrierTitre", order = 74)
    public String getAdresseCourrierTitre() {
        return adresseCourrier.resolveCsTitre();
    }

    @Column(name = "AdresseCourierAttention", order = 75)
    public String getAdresseCourrierAttentionn() {
        return adresseCourrier.getAttention();
    }

    @Column(name = "CourrierCasePostale", order = 76)
    public String getAdresseCourrierCasePostal() {
        return adresseCourrier.getCasePostale();
    }

    @Column(name = "CourrierDesignation1", order = 77)
    public String getAdresseCourrierDes1() {
        return adresseCourrier.resolveDesignation1();
    }

    @Column(name = "CourrierDesignation2", order = 78)
    public String getAdresseCourrierDes2() {
        return adresseCourrier.resolveDesignation2();
    }

    @Column(name = "CourrierDesignation3", order = 79)
    public String getAdresseCourrierDes3() {
        return adresseCourrier.resolveDesignation3();
    }

    @Column(name = "CourrierDesignation4", order = 80)
    public String getAdresseCourrierDes4() {
        return adresseCourrier.resolveDesignation4();
    }

    @Column(name = "IdAdresseCourrier", order = 81)
    public Long getIdAdresseCourrier() {
        return toLong(adresseCourrier.getIdAdresse());
    }

    @Column(name = "IdAdresseDomicile", order = 82)
    public Long getIdAdresseDomicile() {
        return toLong(adresseDomicile.getIdAdresse());
    }

    @Column(name = "IsSameAdresse", order = 83)
    public Boolean getIsSameAdresse() {
        return adresseDomicile.getIdAdresse().equals(adresseCourrier.getIdAdresse());
    }

    @Column(name = "LibelleHome", order = 100)
    public String getLibelleHome() {
        return home.getLibelle();
    }

    @Column(name = "NumeroHome", order = 100)
    public String getNumeroHome() {
        return home.getNumero();
    }

    @Column(name = "HomeAdressseCourrierRue", order = 110)
    public String getHomeAdresseCourrierRue() {
        return adresseHome.getRue();
    }

    @Column(name = "HomeAdressseNumero", order = 111)
    public String getHomeAdresseCourrierNumero() {
        return adresseHome.getRueNumero();
    }

    @Column(name = "HomeAdressseNPA", order = 112)
    public String getHomeAdresseNpa() {
        return adresseHome.getNpa();
    }

    @Column(name = "HomeAdressseLocalite", order = 113)
    public String getHomeAdresseCourrierLocalite() {
        return adresseHome.getLocalite();
    }

    // @ColumnValueConverter(CodeSystemeConverter.class)
    // @Column(name = "HomeAdressseTitre", order = 114)
    // public String getHomeAdresseTitre() {
    // return adresseHome.getCsTitre();
    // }

    @Column(name = "HomeAdresseAttention", order = 115)
    public String getHomeAdresseAttentionn() {
        return adresseHome.getAttention();
    }

    @Column(name = "HomeCasePostale", order = 116)
    public String getHomeAdresseCasePostal() {
        return adresseHome.getCasePostale();
    }

    @Column(name = "HomeDesignation1", order = 117)
    public String getHomeDesignation1() {
        return adresseHome.resolveDesignation1();
    }

    @Column(name = "HomeDesignation2", order = 118)
    public String getHomeDesignation2() {
        return adresseHome.resolveDesignation2();
    }

    @Column(name = "IdPlanCalcul", order = 121)
    public Long getIdPlanCalcul() {
        return toLong(infoAdaptation.getIdPlanCalcul());
    }

    @Column(name = "IdAnciennePca", order = 122)
    public Long idAncienne() {
        return toLong(anciennePca.getId());
    }

    @Column(name = "IdNouvellePca", order = 123)
    public Long idNouvellePca() {
        return toLong(nouvellePca.getId());
    }

    @Column(name = "IdVersionDroit", order = 124)
    public Long getIdVersionDroit() {
        return toLong(nouvellePca.getIdVersionDroit());
    }

    @Column(name = "IdTiers", order = 125)
    public Long getIdTiers() {
        return idTiersBeneficiaire;
    }

    @Column(name = "IdTiersHome", order = 126)
    public Long getIdTiersHome() {
        return toLong(home.getIdTiersHome());
    }

    private Long toLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Long.valueOf(value);
    }
}
