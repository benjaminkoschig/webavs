package ch.globaz.pegasus.business.domaine.revisionquadriennale;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.pegasus.business.domaine.pca.PcaSituation;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ MontantConverterToDouble.class })
public interface RevisionCsv {

    @Column(name = "NSS R", order = 1)
    public String getNss();

    @Column(name = "Nom Prénom R", order = 2)
    public String getNomPrenom();

    @Column(name = "Date de naissance R", order = 3)
    public String getDateDeNaissance();

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "Sexe R", order = 4)
    public String getSexe();

    @ColumnValueConverter(SexeConverter.class)
    @Column(name = "Titre R", order = 5)
    public String getSexeTitre();

    @Column(name = "Nationalité R", order = 6)
    public String getNationalite();

    @Column(name = "NSS C", order = 7)
    public String getNssConjoint();

    @Column(name = "Nom Prénom C", order = 8)
    public String getNomPrenomConjoint();

    @Column(name = "Date de naissance C", order = 9)
    public String getDateDeNaissanceConjoint();

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "Sexe C", order = 10)
    public String getSexeConjoint();

    @ColumnValueConverter(SexeConverter.class)
    @Column(name = "Titre C", order = 11)
    public String getSexeTitreConjoint();

    @Column(name = "Gestionnaire", order = 12)
    public String getGestionnaire();

    @Column(name = "Date révision", order = 13)
    public String getDateRevision();

    @Column(name = "Agence communale", order = 14)
    public String getAgenceCommunale();

    @Column(name = "Motif révision", order = 17)
    public String getMotifRevision();

    @Column(name = "Courrier Rue", order = 18)
    public String getAdresseCourrierRue();

    @Column(name = "Courrier Adresse numero", order = 19)
    public String getAdresseCourrierNumero();

    @Column(name = "Courrier Npa", order = 20)
    public String getAdresseCourrierNpa();

    @Column(name = "Courrier Localite", order = 21)
    public String getAdresseCourrierLocalite();

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "Courrier Titre", order = 22)
    public String getAdresseCourrierTitre();

    @Column(name = "Courrier NomAdresse", order = 23)
    public String getAdresseCourrierNom();

    @Column(name = "Courrier PrenomAdresse", order = 24)
    public String getAdresseCourrierPrenom();

    @Column(name = "Courrier Case Postale", order = 25)
    public String getAdresseCourrierCasePostal();

    @Column(name = "Domicile Rue", order = 26)
    public String getAdresseDomicileRue();

    @Column(name = "Domicile Adresse numero", order = 27)
    public String getAdresseDomicileNumero();

    @Column(name = "Domicile Npa", order = 28)
    public String getAdresseDomicileNpa();

    @Column(name = "Domicile Localite", order = 29)
    public String getAdresseDomicileLocalite();

    @ColumnValueConverter(CodeSystemeConverter.class)
    @Column(name = "Domicile Titre", order = 30)
    public String getAdresseDomicileTitre();

    @Column(name = "Domicile NomAdresse", order = 31)
    public String getAdresseDomicileNom();

    @Column(name = "Domicile PrenomAdresse", order = 32)
    public String getAdresseDomicilePrenom();

    @Column(name = "Domicile Case Postale", order = 33)
    public String getAdresseDomicileCasePostal();

    @Column(name = "CasPca", order = 34)
    public PcaSituation getCasPca();

    @Column(name = "Nb Adulte", order = 35)
    public Integer getNbAdulte();

    @Column(name = "Nb enfant", order = 36)
    public Integer getNbEnfant();

    @Column(name = "A1a_r", order = 37)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneArgentRequerant();

    @Column(name = "A1a_ce", order = 38)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneArgentCojnEnf();

    @Column(name = "A1b_r", order = 39)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneValeurRachatAssuranceVieRequerant();

    @Column(name = "A1b_ce", order = 40)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneValeurRachatAssuranceVieConjEnf();

    @Column(name = "A1c_r", order = 41)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneBienDessaisiRequerant();

    @Column(name = "A1c_ce", order = 42)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneBienDessaisiConjEnfant();

    // @Column(name = "A1d_r", order = 16)
    // public Montant getFortuneValeurPartSuccessionNonPartageeRequerant();
    //
    // @Column(name = "A1d_ce", order = 17)
    // public Montant getFortuneValeurPartSuccessionNonPartageeConjEnf();

    @Column(name = "A1e_r", order = 43)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneAutreBienRequerant();

    @Column(name = "A1e_ce", order = 44)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneAutreBienConjEnfant();

    @Column(name = "A2a_r", order = 45)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneImmeublePrincipaleNonAgricoleRequerant();

    @Column(name = "A2a_ce", order = 46)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneImmeublePrincipaleNonAgricoleConEnf();

    @Column(name = "A2b_r", order = 47)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneImmeubleNonPrincipalNonAgricoleRequerant();

    @Column(name = "A2b_ce", order = 48)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneImmeubleNonPrincipalNonAgricoleConjEnf();

    @Column(name = "A2c_r", order = 49)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneImmeubleAgricoleRequerant();

    @Column(name = "A2c_ce", order = 50)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneImmeubleAgricoleConjEnf();

    // @Column(name = "A2x_r", order = 26)
    // public Montant getFortuneImmeubleValeurEstimeeParLesPcRequerant();
    //
    // @Column(name = "A2x_ce", order = 27)
    // public Montant getFortuneImmeubleValeurEstimeeParLesPcConjEnf();

    @Column(name = "A3_r", order = 51)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneDetteHypothecaireRequerant();

    @Column(name = "A3_ce", order = 52)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneDetteHypothecaireConjEnf();

    @Column(name = "A4_r", order = 53)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneAutreDetteRequerant();

    @Column(name = "A4_ce", order = 54)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getFortuneAutreDetteConjEnf();

    @Column(name = "B1a_r", order = 55)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuActiviteLucrativeDependanteRequerant();

    @Column(name = "B1a_ce", order = 56)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuActiviteLucrativeDependanteConEnf();

    @Column(name = "B1b_r", order = 57)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuHypothetiqueRequerant();

    @Column(name = "B1b_ce", order = 58)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuHypothetiqueConjEnfant();

    @Column(name = "B1c_r", order = 59)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuLucrativeIndependanteRequerant();

    @Column(name = "B1c_ce", order = 60)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuLucrativeIndependanteConjEnf();

    @Column(name = "B1d_r", order = 61)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuSalaireNatureRequerant();

    @Column(name = "B1d_ce", order = 62)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuSalaireNatureConjoint();

    @Column(name = "B1e_r", order = 63)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAllocationPourPerteDeGainRequerant();

    @Column(name = "B1e_ce", order = 64)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAllocationPourPerteDeGainConjEnf();

    // @Column(name = "B1f_r", order = 42)
    // public Montant getRevenuSousLocationsAvecPensionRequerant();
    //
    // @Column(name = "B1f_ce", order = 43)
    // public Montant getRevenuSousLocationsAvecPensionConjEnf();

    @Column(name = "B1g_r", order = 65)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAllocationFamillialeRequant();

    @Column(name = "B1g_ce", order = 66)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAllocationFamillialeConjEnf();

    @Column(name = "B2a_r", order = 67)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteAnnuelleAvsAiRequant();

    @Column(name = "B2a_ce", order = 68)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteAnnuelleAvsAiConjEnf();

    @Column(name = "B2b_r", order = 69)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuIndemniteJournaliereAiRequant();

    @Column(name = "B2b_ce", order = 70)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuIndemniteJournaliereAiConjEnf();

    @Column(name = "B2c_r", order = 71)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAllocationPourImpotantRequant();

    @Column(name = "B2c_ce", order = 72)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAllocationPourImpotantConjEnft();

    @Column(name = "B3a_r", order = 73)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteEtrangereRequerant();

    @Column(name = "B3a_ce", order = 74)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteEtrangereConjEnf();

    @Column(name = "B3b_r", order = 75)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteLaaRequerant();

    @Column(name = "B3b_ce", order = 76)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteLaaConjEnf();

    @Column(name = "B3c_r", order = 77)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteAssurenceMilitaireRequerant();

    @Column(name = "B3c_ce", order = 78)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteAssurenceMilitaireConjEnf();

    @Column(name = "B3d_r", order = 79)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteLppRequerant();

    @Column(name = "B3d_ce", order = 80)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteLppConjEnf();

    @Column(name = "B3e_r", order = 81)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteAssuranceVolontaireRequerant();

    @Column(name = "B3e_ce", order = 82)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRenteAssuranceVolontaireConjEnf();

    @Column(name = "B3f_r", order = 83)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuContratEnteretienViagerRequerant();

    @Column(name = "B3f_ce", order = 84)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuContratEnteretienViagerConjEnf();

    @Column(name = "B3g_r", order = 85)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutreRenteRequerant();

    @Column(name = "B3g_ce", order = 86)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutreRenteConjEnf();

    @Column(name = "B4a_r", order = 87)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRendementLoyerRequerant();

    @Column(name = "B4a_ce", order = 88)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRendementLoyerConjEnf();

    @Column(name = "B4b_r", order = 89)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRendementValeurLocativeRequerant();

    @Column(name = "B4b_ce", order = 90)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRendementValeurLocativeConjEnf();

    @Column(name = "B4c_r", order = 91)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRendementInteretsRequerant();

    @Column(name = "B4c_ce", order = 92)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuRendementInteretsConjEnf();

    @Column(name = "B5a_r", order = 93)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusValeurUsufruitReq();

    @Column(name = "B5a_ce", order = 94)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusValeurUsufruitConjEnf();

    @Column(name = "B5b_r", order = 95)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusDroitHabitationReq();

    @Column(name = "B5b_ce", order = 96)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusDroitHabitationConjEnf();

    @Column(name = "B5c_r", order = 97)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusSousLocationsSansPensionReq();

    @Column(name = "B5c_ce", order = 98)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusSousLocationsSansPensionConjEnf();

    // enTetesColonesCSV.add(new ColoneCSV("B5d_r"));
    // enTetesColonesCSV.add(new ColoneCSV("B5d_ce"));

    @Column(name = "B5e_r", order = 99)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusPensionAlimentaireReqDivorceeReq();

    @Column(name = "B5e_ce", order = 100)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusPensionAlimentaireReqDivorceeConjEnf();

    // enTetesColonesCSV.add(new ColoneCSV("B5f_r"));
    // enTetesColonesCSV.add(new ColoneCSV("B5f_ce"));

    @Column(name = "B5g_r", order = 101)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiReq();

    @Column(name = "B5g_ce", order = 102)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusRessourcesOuInteretsFortuneDessaisiConjEnf();

    // enTetesColonesCSV.add(new ColoneCSV("B5h_r"));
    // enTetesColonesCSV.add(new ColoneCSV("B5h_ce"));

    @Column(name = "B5i_r", order = 103)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusAutresCreancesVersTiersReq();

    @Column(name = "B5i_ce", order = 104)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getRevenuAutresRevenusAutresCreancesVersTiersConjEnf();

    @Column(name = "C1a_r", order = 105)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionLoyerAnnuellesRequerant();

    @Column(name = "C1a_ce", order = 106)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionLoyerAnnuellesConjEnf();

    @Column(name = "C1b_r", order = 107)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionChargesAnnuelesRequerant();

    @Column(name = "C1b_ce", order = 108)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionChargesAnnuelesConjEnf();

    @Column(name = "C1c_r", order = 109)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Integer getDeductionNombrePeronneRequerant();

    @Column(name = "C1c_ce", order = 110)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Integer getDeductionNombrePeronneConjEnf();

    @Column(name = "C3_r", order = 111)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionCotisationPsalRequerant();

    @Column(name = "C3_ce", order = 112)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionCotisationPsalPeronneConjEnf();

    @Column(name = "C6a_r", order = 113)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionInteretsHypothecaireRequerant();

    @Column(name = "C6a_ce", order = 114)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionInteretsHypothecaireConjEnfant();

    @Column(name = "C6b_r", order = 115)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionFraisEntretiensImmeubleRequerant();

    @Column(name = "C6b_ce", order = 116)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionFraisEntretiensImmeubleConjEnf();

    @Column(name = "C7_r", order = 117)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionPensionAlimentairesVerseeRequerant();

    @Column(name = "C7_ce", order = 118)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionPensionAlimentairesVerseeConjEnf();

    @Column(name = "C8_r", order = 119)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionRegimeRfmRequerant();

    @Column(name = "C8_ce", order = 120)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    public Montant getDeductionRegimeRfmConjEnf();

}
