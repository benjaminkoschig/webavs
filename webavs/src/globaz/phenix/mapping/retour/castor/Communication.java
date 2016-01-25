/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.9M1</a>, using an XML Schema.
 * $Id: Communication.java,v 1.6 2008/09/15 13:49:13 hna Exp $
 */

package globaz.phenix.mapping.retour.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * La communication de retour du fisc
 * 
 * @version $Revision: 1.6 $ $Date: 2008/09/15 13:49:13 $
 */
public class Communication implements java.io.Serializable {

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Communication
     */
    public static globaz.phenix.mapping.retour.castor.Communication unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (globaz.phenix.mapping.retour.castor.Communication) Unmarshaller.unmarshal(
                globaz.phenix.mapping.retour.castor.Communication.class, reader);
    } // -- globaz.phenix.mapping.retour.castor.Communication

    /**
     * Annee fiscale 1
     */
    private java.lang.String _annee1;

    /**
     * Annee fiscale 2
     */
    private java.lang.String _annee2;

    /**
     * Field _capital
     */
    private java.lang.String _capital;
    /**
     * Field _cotisation1
     */
    private java.lang.String _cotisation1;
    /**
     * Field _cotisation2
     */
    private java.lang.String _cotisation2;

    /**
     * Field _dateFortune
     */
    private java.lang.String _dateFortune;

    /**
     * Date début exercice1
     */
    private java.lang.String _debutExercice1;

    /**
     * Date début exercice2
     */
    private java.lang.String _debutExercice2;
    /**
     * Contient toues les messages de génération en cas d'erreur
     */
    private globaz.phenix.mapping.retour.castor.ErrorMessage _errorMessage;
    /**
     * Date fin exercice1
     */
    private java.lang.String _finExercice1;

    /**
     * Date fin exercice2
     */
    private java.lang.String _finExercice2;
    /**
     * Field _fortune
     */
    private java.lang.String _fortune;

    private java.lang.String _geBourses;

    private java.lang.String _geDateTransfertMAD;

    private java.lang.String _geDivers;

    private java.lang.String _geExplicationDivers;

    private java.lang.String _geGenreAffilie;
    private java.lang.String _geImpositionSelonDepense;
    private java.lang.String _geImpotSource;
    private java.lang.String _geIndemniteJournaliere;
    private java.lang.String _geNNSS;

    private java.lang.String _geNom;

    private String _geNomAFC;
    private java.lang.String _geNomConjoint;
    private java.lang.String _geNonAssujettiIBO;

    private java.lang.String _geNonAssujettiIFD;

    /**
     * Genre d'affiliation: Non actif, Indépendant...
     */
    private java.lang.String _genreAffilie;

    /**
     * Field _genreTaxation
     */
    private java.lang.String _genreTaxation;

    private java.lang.String _geNumAffilie;

    private java.lang.String _geNumAvs;

    private java.lang.String _geNumAvsConjoint;

    /**
     * Field for GE
     */
    private java.lang.String _geNumCaisse;

    private java.lang.String _geNumCommunication;

    private java.lang.String _geNumContribuable;

    private java.lang.String _geNumDemande;

    private java.lang.String _geObservations;

    private java.lang.String _gePasActiviteDeclaree;

    private java.lang.String _gePension;

    private java.lang.String _gePensionAlimentaire;

    private java.lang.String _gePersonneNonIdentifiee;

    private java.lang.String _gePrenom;

    private java.lang.String _gePrenomAFC;

    private java.lang.String _gePrenomConjoint;

    private java.lang.String _geRenteInvalidite;

    private java.lang.String _geRenteViagere;

    private java.lang.String _geRenteVieillesse;

    private java.lang.String _geRetraite;

    private java.lang.String _geTaxationOffice;

    private java.lang.String _idConjoint;

    /**
     * Identification de la demande ayant engendré cette réponse
     */
    private java.lang.String _idDemande;

    /**
     * Field _juCodeApplication
     */
    private java.lang.String _juCodeApplication;

    /**
     * Field _juDateNaissance
     */
    private java.lang.String _juDateNaissance;

    /**
     * Field _juEpoux
     */
    private java.lang.String _juEpoux;

    /**
     * Field _juFiller
     */
    private java.lang.String _juFiller;

    /**
     * Genre d'affiliation: Non actif, Indépendant...
     */
    private java.lang.String _juGenreAffilie;
    /**
     * Field _genreTaxation
     */
    private java.lang.String _juGenreTaxation;
    /**
     * Field _juLot
     */
    private java.lang.String _juLot;
    /**
     * Field _juNbrJour1
     */
    private java.lang.String _juNbrJour1;
    /**
     * Field _juNbrJour2
     */
    private java.lang.String _juNbrJour2;
    /**
     * Field _juNewNumContribuable
     */
    private java.lang.String _juNewNumContribuable;
    /**
     * Numéro de contribuable
     */
    private java.lang.String _juNumContribuable;
    /**
     * Field _juTaxeMan
     */
    private java.lang.String _juTaxeMan;
    /**
     * Field _neDateDebutAssuj
     */
    private java.lang.String _neDateDebutAssuj;
    /**
     * Field _neDateValeur
     */
    private java.lang.String _neDateValeur;
    /**
     * Field _neDossierTaxe
     */
    private java.lang.String _neDossierTaxe;
    /**
     * Field _neDossierTrouve
     */
    private java.lang.String _neDossierTrouve;
    /**
     * Field _neFiller
     */
    private java.lang.String _neFiller;
    /**
     * Fortune de l'année 1
     */
    private java.lang.String _neFortuneAnnee1;
    /**
     * Genre d'affiliation: Non actif, Indépendant...
     */
    private java.lang.String _neGenreAffilie;
    /**
     * Field _genreTaxation
     */
    private java.lang.String _neGenreTaxation;
    /**
     * Field _neIndemniteJour
     */
    private java.lang.String _neIndemniteJour;
    /**
     * Indémnité journalière de l'année 1
     */
    private java.lang.String _neIndemniteJour1;
    /**
     * Numéro AVS du contribuable
     */
    private java.lang.String _neNumAvs;
    /**
     * Field _neNumBDP
     */
    private java.lang.String _neNumBDP;
    /**
     * Numéro Caisse Avs du contribuable
     */
    private java.lang.String _neNumCaisse;
    /**
     * Numéro de client
     */
    private java.lang.String _neNumClient;
    /**
     * Numéro de commune
     */
    private java.lang.String _neNumCommune;
    /**
     * Numéro de contribuable NE
     */
    private java.lang.String _neNumContribuable;
    /**
     * Field _nePension
     */
    private java.lang.String _nePension;
    /**
     * Field _nePensionAlimentaire
     */
    private java.lang.String _nePensionAlimentaire;
    /**
     * Pension alimentaire de l'année 1
     */
    private java.lang.String _nePensionAlimentaire1;
    /**
     * Pension de l'année 1
     */
    private java.lang.String _nePensionAnnee1;
    /**
     * Field _nePremiereLettreNom
     */
    private java.lang.String _nePremiereLettreNom;
    /**
     * Field _neRenteTotale
     */
    private java.lang.String _neRenteTotale;
    /**
     * Rente totale de l'année 1
     */
    private java.lang.String _neRenteTotale1;
    /**
     * Field _neRenteViagere
     */
    private java.lang.String _neRenteViagere;
    /**
     * Rente viagère de l'année 1
     */
    private java.lang.String _neRenteViagere1;
    /**
     * Field _neTaxationRectificative
     */
    private java.lang.String _neTaxationRectificative;
    /**
     * Période IFD
     */
    private java.lang.String _periodeIFD;

    /**
     * Revenu principal
     */
    private java.lang.String _revenu1;
    /**
     * Revenu secondaire
     */
    private java.lang.String _revenu2;
    private String majNumContribuable = "";
    private String vsAdresseAffilie1 = "";
    private String vsAdresseAffilie2 = "";
    private String vsAdresseAffilie3 = "";
    private String vsAdresseAffilie4 = "";
    private String vsAdresseConjoint1 = "";
    private String vsAdresseConjoint2 = "";
    private String vsAdresseConjoint3 = "";
    private String vsAdresseConjoint4 = "";
    private String vsAdresseCtb1 = "";
    private String vsAdresseCtb2 = "";
    private String vsAdresseCtb3 = "";
    private String vsAdresseCtb4 = "";
    private String vsAnneeTaxation = "";
    private String vsAutresRevenusConjoint = "";
    private String vsAutresRevenusCtb = "";
    private String vsCapitalPropreEngageEntrepriseConjoint = "";
    private String vsCapitalPropreEngageEntrepriseCtb = "";
    private String vsCodeTaxation1 = "";
    private String vsCodeTaxation2 = "";
    private String vsCotisationAvsAffilie = "";
    private String vsCotisationAvsConjoint = "";
    private String vsDateCommunication = "";
    private String vsDateDebutAffiliation = "";
    private String vsDateDebutAffiliationCaisseProfessionnelle = "";
    private String vsDateDebutAffiliationConjoint = "";
    private String vsDateDebutAffiliationConjointCaisseProfessionnelle = "";
    private String vsDateDecesCtb = "";
    private String vsDateDemandeCommunication = "";
    private String vsDateFinAffiliation = "";
    private String vsDateFinAffiliationCaisseProfessionnelle = "";
    private String vsDateFinAffiliationConjoint = "";
    private String vsDateFinAffiliationConjointCaisseProfessionnelle = "";
    private String vsDateNaissanceAffilie = "";
    private String vsDateNaissanceConjoint = "";
    private String vsDateNaissanceCtb = "";
    private String vsDateTaxation = "";
    private String vsDebutActiviteConjoint = "";
    private String vsDebutActiviteCtb = "";
    private String vsEtatCivilAffilie = "";
    private String vsEtatCivilCtb = "";
    private String vsFinActiviteConjoint = "";
    private String vsFinActiviteCtb = "";
    private String vsFortunePriveeConjoint = "";
    private String vsFortunePriveeCtb = "";
    private String vsLangue = "";
    private String vsLibre3 = "";
    private String vsLibre4 = "";
    private String vsNbJoursTaxation = "";
    private String vsNoCaisseAgenceAffilie = "";
    private String vsNoCaisseProfessionnelleAffilie = "";
    private String vsNomAffilie = "";
    private String vsNomConjoint = "";
    private String vsNomPrenomContribuableAnnee = "";
    private String vsNoPostalLocalite = "";
    private String vsNumAffilie = "";
    private String vsNumAffilieConjoint = "";
    private String vsNumAffilieInterneCaisseProfessionnelle = "";
    private String vsNumAffilieInterneConjointCaisseProfessionnelle = "";
    private String vsNumAvsAffilie = "";
    private String vsNumAvsConjoint = "";
    private String vsNumAvsCtb = "";
    private String vsNumCaisseAgenceConjoint = "";
    private String vsNumCaisseProfessionnelleConjoint = "";
    /**
     * Field for VS
     */
    private String vsNumCtb = "";
    private String vsNumCtbSuivant = "";
    private String vsNumPostalLocaliteConjoint = "";
    private String vsNumPostalLocaliteCtb = "";
    private String vsRachatLpp = "";
    private String vsRachatLppCjt = "";
    private String vsReserve = "";
    private String vsReserveDateNaissanceConjoint = "";
    private String vsReserveFichierImpression = "";
    private String vsReserveTriNumCaisse = "";
    private String vsRevenuAgricoleConjoint = "";
    private String vsRevenuAgricoleCtb = "";
    private String vsRevenuNonAgricoleConjoint = "";
    private String vsRevenuNonAgricoleCtb = "";
    private String vsRevenuRenteConjoint = "";
    private String vsRevenuRenteCtb = "";
    private String vsSalairesConjoint = "";
    private String vsSalairesContribuable = "";
    private String vsSexeAffilie = "";

    private String vsSexeCtb = "";

    // ----------------/
    // - Constructors -/
    // ----------------/

    private String vsTypeTaxation = "";

    public Communication() {
        super();
    } // -- globaz.phenix.mapping.retour.castor.Communication()

    /**
     * Returns the value of field 'annee1'. The field 'annee1' has the following description: Annee fiscale 1
     * 
     * @return String
     * @return the value of field 'annee1'.
     */
    public java.lang.String getAnnee1() {
        return _annee1;
    } // -- java.lang.String getAnnee1()

    /**
     * Returns the value of field 'annee2'. The field 'annee2' has the following description: Annee fiscale 2
     * 
     * @return String
     * @return the value of field 'annee2'.
     */
    public java.lang.String getAnnee2() {
        return _annee2;
    } // -- java.lang.String getAnnee2()

    /**
     * Returns the value of field 'capital'.
     * 
     * @return String
     * @return the value of field 'capital'.
     */
    public java.lang.String getCapital() {
        return _capital;
    } // -- java.lang.String getCapital()

    /**
     * Returns the value of field 'cotisation1'.
     * 
     * @return String
     * @return the value of field 'cotisation1'.
     */
    public java.lang.String getCotisation1() {
        return _cotisation1;
    } // -- java.lang.String getCotisation1()

    /**
     * Returns the value of field 'cotisation2'.
     * 
     * @return String
     * @return the value of field 'cotisation2'.
     */
    public java.lang.String getCotisation2() {
        return _cotisation2;
    } // -- java.lang.String getCotisation2()

    /**
     * Returns the value of field 'dateFortune'.
     * 
     * @return String
     * @return the value of field 'dateFortune'.
     */
    public java.lang.String getDateFortune() {
        return _dateFortune;
    } // -- java.lang.String getDateFortune()

    /**
     * Returns the value of field 'debutExercice1'. The field 'debutExercice1' has the following description: Date
     * début exercice1
     * 
     * @return String
     * @return the value of field 'debutExercice1'.
     */
    public java.lang.String getDebutExercice1() {
        return _debutExercice1;
    } // -- java.lang.String getDebutExercice1()

    /**
     * Returns the value of field 'debutExercice2'. The field 'debutExercice2' has the following description: Date
     * début exercice2
     * 
     * @return String
     * @return the value of field 'debutExercice2'.
     */
    public java.lang.String getDebutExercice2() {
        return _debutExercice2;
    } // -- java.lang.String getDebutExercice2()

    /**
     * Returns the value of field 'errorMessage'. The field 'errorMessage' has the following description: Contient toues
     * les messages de génération en cas d'erreur
     * 
     * @return ErrorMessage
     * @return the value of field 'errorMessage'.
     */
    public globaz.phenix.mapping.retour.castor.ErrorMessage getErrorMessage() {
        return _errorMessage;
    } // -- globaz.phenix.mapping.retour.castor.ErrorMessage getErrorMessage()

    /**
     * Returns the value of field 'finExercice1'. The field 'finExercice1' has the following description: Date fin
     * exercice1
     * 
     * @return String
     * @return the value of field 'finExercice1'.
     */
    public java.lang.String getFinExercice1() {
        return _finExercice1;
    } // -- java.lang.String getFinExercice1()

    /**
     * Returns the value of field 'finExercice2'. The field 'finExercice2' has the following description: Date fin
     * exercice2
     * 
     * @return String
     * @return the value of field 'finExercice2'.
     */
    public java.lang.String getFinExercice2() {
        return _finExercice2;
    } // -- java.lang.String getFinExercice2()

    /**
     * Returns the value of field 'fortune'.
     * 
     * @return String
     * @return the value of field 'fortune'.
     */
    public java.lang.String getFortune() {
        return _fortune;
    } // -- java.lang.String getFortune()

    /**
     * @return
     */
    public java.lang.String getGeBourses() {
        return _geBourses;
    }

    /**
     * @return
     */
    public java.lang.String getGeDateTransfertMAD() {
        return _geDateTransfertMAD;
    }

    /**
     * @return
     */
    public java.lang.String getGeDivers() {
        return _geDivers;
    }

    /**
     * @return
     */
    public java.lang.String getGeExplicationDivers() {
        return _geExplicationDivers;
    }

    /**
     * @return
     */
    public java.lang.String getGeGenreAffilie() {
        return _geGenreAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getGeImpositionSelonDepense() {
        return _geImpositionSelonDepense;
    }

    /**
     * @return
     */
    public java.lang.String getGeImpotSource() {
        return _geImpotSource;
    }

    /**
     * @return
     */
    public java.lang.String getGeIndemniteJournaliere() {
        return _geIndemniteJournaliere;
    }

    public java.lang.String getGeNNSS() {
        return _geNNSS;
    }

    /**
     * @return
     */
    public java.lang.String getGeNom() {
        return _geNom;
    }

    public String getGeNomAFC() {
        return _geNomAFC;
    }

    /**
     * @return
     */
    public java.lang.String getGeNomConjoint() {
        return _geNomConjoint;
    }

    /**
     * @return
     */
    public java.lang.String getGeNonAssujettiIBO() {
        return _geNonAssujettiIBO;
    }

    /**
     * @return
     */
    public java.lang.String getGeNonAssujettiIFD() {
        return _geNonAssujettiIFD;
    }

    /**
     * Returns the value of field 'genreAffilie'. The field 'genreAffilie' has the following description: Genre
     * d'affiliation: Non actif, Indépendant...
     * 
     * @return String
     * @return the value of field 'genreAffilie'.
     */
    public java.lang.String getGenreAffilie() {
        return _genreAffilie;
    } // -- java.lang.String getGenreAffilie()

    /**
     * Returns the value of field 'genreTaxation'.
     * 
     * @return String
     * @return the value of field 'genreTaxation'.
     */
    public java.lang.String getGenreTaxation() {
        return _genreTaxation;
    } // -- java.lang.String getGenreTaxation()

    /**
     * @return
     */
    public java.lang.String getGeNumAffilie() {
        return _geNumAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getGeNumAvs() {
        return _geNumAvs;
    }

    /**
     * @return
     */
    public java.lang.String getGeNumAvsConjoint() {
        return _geNumAvsConjoint;
    }

    /**
     * @return
     */
    public java.lang.String getGeNumCaisse() {
        return _geNumCaisse;
    }

    /**
     * @return
     */
    public java.lang.String getGeNumCommunication() {
        return _geNumCommunication;
    }

    /**
     * @return
     */
    public java.lang.String getGeNumContribuable() {
        return _geNumContribuable;
    }

    /**
     * @return
     */
    public java.lang.String getGeNumDemande() {
        return _geNumDemande;
    }

    /**
     * @return
     */
    public java.lang.String getGeObservations() {
        return _geObservations;
    }

    /**
     * @return
     */
    public java.lang.String getGePasActiviteDeclaree() {
        return _gePasActiviteDeclaree;
    }

    /**
     * @return
     */
    public java.lang.String getGePension() {
        return _gePension;
    }

    /**
     * @return
     */
    public java.lang.String getGePensionAlimentaire() {
        return _gePensionAlimentaire;
    }

    public java.lang.String getGePersonneNonIdentifiee() {
        return _gePersonneNonIdentifiee;
    }

    /**
     * @return
     */
    public java.lang.String getGePrenom() {
        return _gePrenom;
    }

    public java.lang.String getGePrenomAFC() {
        return _gePrenomAFC;
    }

    /**
     * @return
     */
    public java.lang.String getGePrenomConjoint() {
        return _gePrenomConjoint;
    }

    /**
     * @return
     */
    public java.lang.String getGeRenteInvalidite() {
        return _geRenteInvalidite;
    }

    /**
     * @return
     */
    public java.lang.String getGeRenteViagere() {
        return _geRenteViagere;
    }

    /**
     * @return
     */
    public java.lang.String getGeRenteVieillesse() {
        return _geRenteVieillesse;
    }

    /**
     * @return
     */
    public java.lang.String getGeRetraite() {
        return _geRetraite;
    }

    /**
     * @return
     */
    public java.lang.String getGeTaxationOffice() {
        return _geTaxationOffice;
    }

    /**
     * Returns the value of field 'idConjoint'. T
     * 
     * @return String
     * @return the value of field 'idConjoint'.
     */
    public java.lang.String getIdConjoint() {
        return _idConjoint;
    }

    /**
     * Returns the value of field 'idDemande'. The field 'idDemande' has the following description: Identification de la
     * demande ayant engendré cette réponse
     * 
     * @return String
     * @return the value of field 'idDemande'.
     */
    public java.lang.String getIdDemande() {
        return _idDemande;
    } // -- java.lang.String getIdDemande()

    /**
     * Returns the value of field 'codeJu'.
     * 
     * @return String
     * @return the value of field 'codeJu'.
     */
    public java.lang.String getJuCodeApplication() {
        return _juCodeApplication;
    } // -- java.lang.String getCodeJu()

    public java.lang.String getJuDateNaissance() {
        return _juDateNaissance;
    } //

    /**
     * Returns the value of field 'epoux'.
     * 
     * @return String
     * @return the value of field 'epoux'.
     */
    public java.lang.String getJuEpoux() {
        return _juEpoux;
    } // -- java.lang.String getEpoux()

    /**
     * Returns the value of field 'fillerJu'.
     * 
     * @return String
     * @return the value of field 'fillerJu'.
     */
    public java.lang.String getJuFiller() {
        return _juFiller;
    } // -- java.lang.String getFillerJu()

    /**
     * Returns the value of field 'genreAffilie'. The field 'genreAffilie' has the following description: Genre
     * d'affiliation: Non actif, Indépendant...
     * 
     * @return String
     * @return the value of field 'genreAffilie'.
     */
    public java.lang.String getJuGenreAffilie() {
        return _juGenreAffilie;
    } // -- java.lang.String getGenreAffilie()

    /**
     * Returns the value of field 'genreTaxation'.
     * 
     * @return String
     * @return the value of field 'genreTaxation'.
     */
    public java.lang.String getJuGenreTaxation() {
        return _juGenreTaxation;
    } // -- java.lang.String getGenreTaxation()

    /**
     * Returns the value of field 'lot'.
     * 
     * @return String
     * @return the value of field 'lot'.
     */
    public java.lang.String getJuLot() {
        return _juLot;
    } // -- java.lang.String getLot()

    /**
     * Returns the value of field 'nbrJour1'.
     * 
     * @return String
     * @return the value of field 'nbrJour1'.
     */
    public java.lang.String getJuNbrJour1() {
        return _juNbrJour1;
    } // -- java.lang.String getNbrJour1()

    /**
     * Returns the value of field 'nbrJour2'.
     * 
     * @return String
     * @return the value of field 'nbrJour2'.
     */
    public java.lang.String getJuNbrJour2() {
        return _juNbrJour2;
    } // -- java.lang.String getNbrJour2()

    public java.lang.String getJuNewNumContribuable() {
        return _juNewNumContribuable;
    }

    public java.lang.String getJuNumContribuable() {
        return _juNumContribuable;
    }

    /**
     * Returns the value of field 'taxeMan'.
     * 
     * @return String
     * @return the value of field 'taxeMan'.
     */
    public java.lang.String getJuTaxeMan() {
        return _juTaxeMan;
    } // -- java.lang.String getTaxeMan()

    public String getMajNumContribuable() {
        return majNumContribuable;
    }

    /**
     * Returns the value of field 'dateDebutAssuj'.
     * 
     * @return String
     * @return the value of field 'dateDebutAssuj'.
     */
    public java.lang.String getNeDateDebutAssuj() {
        return _neDateDebutAssuj;
    } // -- java.lang.String getDateDebutAssuj()

    /**
     * Returns the value of field 'dateValeur'.
     * 
     * @return String
     * @return the value of field 'dateValeur'.
     */
    public java.lang.String getNeDateValeur() {
        return _neDateValeur;
    } // -- java.lang.String getDateValeur()

    /**
     * Returns the value of field 'dossierTaxe'.
     * 
     * @return String
     * @return the value of field 'dossierTaxe'.
     */
    public java.lang.String getNeDossierTaxe() {
        return _neDossierTaxe;
    } // -- java.lang.String getDossierTaxe()

    /**
     * Returns the value of field 'dossierTrouve'.
     * 
     * @return String
     * @return the value of field 'dossierTrouve'.
     */
    public java.lang.String getNeDossierTrouve() {
        return _neDossierTrouve;
    } // -- java.lang.String getDossierTrouve()

    /**
     * Returns the value of field 'fillerNe'.
     * 
     * @return String
     * @return the value of field 'fillerNe'.
     */
    public java.lang.String getNeFiller() {
        return _neFiller;
    } // -- java.lang.String getFillerNe()

    /**
     * Returns the value of field 'fortuneAnnee1'. The field 'fortuneAnnee1' has the following description: Fortune de
     * l'année 1
     * 
     * @return String
     * @return the value of field 'fortuneAnnee1'.
     */
    public java.lang.String getNeFortuneAnnee1() {
        return _neFortuneAnnee1;
    } // -- java.lang.String getFortuneAnnee1()

    /**
     * Returns the value of field 'genreAffilie'. The field 'genreAffilie' has the following description: Genre
     * d'affiliation: Non actif, Indépendant...
     * 
     * @return String
     * @return the value of field 'genreAffilie'.
     */
    public java.lang.String getNeGenreAffilie() {
        return _neGenreAffilie;
    } // -- java.lang.String getGenreAffilie()

    /**
     * Returns the value of field 'genreTaxation'.
     * 
     * @return String
     * @return the value of field 'genreTaxation'.
     */
    public java.lang.String getNeGenreTaxation() {
        return _neGenreTaxation;
    } // -- java.lang.String getGenreTaxation()

    /**
     * Returns the value of field 'indemniteJour'.
     * 
     * @return String
     * @return the value of field 'indemniteJour'.
     */
    public java.lang.String getNeIndemniteJour() {
        return _neIndemniteJour;
    } // -- java.lang.String getIndemniteJour()

    /**
     * Returns the value of field 'indemniteJour1'. The field 'indemniteJour1' has the following description:
     * Indémnité journalière de l'année 1
     * 
     * @return String
     * @return the value of field 'indemniteJour1'.
     */
    public java.lang.String getNeIndemniteJour1() {
        return _neIndemniteJour1;
    } // -- java.lang.String getIndemniteJour1()

    /**
     * Returns the value of field 'numAvsCtb'. The field 'numAvsCtb' has the following description: Numéro AVS du
     * contribuable
     * 
     * @return String
     * @return the value of field 'numAvsCtb'.
     */
    public java.lang.String getNeNumAvs() {
        return _neNumAvs;
    } // -- java.lang.String getNumAvsCtb()

    // setErrorMessage(globaz.phenix.mapping.retour.castor.ErrorMessage)

    public java.lang.String getNeNumBDP() {
        return _neNumBDP;
    }

    /**
     * Returns the value of field 'numCaiAvsCtb'. The field 'numCaiAvsCtb' has the following description: Numéro Caisse
     * Avs du contribuable
     * 
     * @return String
     * @return the value of field 'numCaiAvsCtb'.
     */
    public java.lang.String getNeNumCaisse() {
        return _neNumCaisse;
    } // -- java.lang.String getNumCaiAvsCtb()

    /**
     * Returns the value of field 'numClient'. The field 'numClient' has the following description: Numéro de client
     * 
     * @return String
     * @return the value of field 'numClient'.
     */
    public java.lang.String getNeNumClient() {
        return _neNumClient;
    } // -- java.lang.String getNumClient()

    /**
     * Returns the value of field 'numCommune'. The field 'numCommune' has the following description: Numéro de commune
     * 
     * @return String
     * @return the value of field 'numCommune'.
     */
    public java.lang.String getNeNumCommune() {
        return _neNumCommune;
    } // -- java.lang.String getNumCommune()

    /**
     * Returns the value of field 'numCtb'. The field 'numCtb' has the following description: Numéro de contribuable
     * 
     * @return String
     * @return the value of field 'numCtb'.
     */
    public java.lang.String getNeNumContribuable() {
        return _neNumContribuable;
    } // -- java.lang.String getNumCtb()

    /**
     * Returns the value of field 'pension'.
     * 
     * @return String
     * @return the value of field 'pension'.
     */
    public java.lang.String getNePension() {
        return _nePension;
    } // -- java.lang.String getPension()

    /**
     * Returns the value of field 'pensionAlimentaire'.
     * 
     * @return String
     * @return the value of field 'pensionAlimentaire'.
     */
    public java.lang.String getNePensionAlimentaire() {
        return _nePensionAlimentaire;
    } // -- java.lang.String getPensionAlimentaire()

    /**
     * Returns the value of field 'pensionAlimentaire1'. The field 'pensionAlimentaire1' has the following description:
     * Pension alimentaire de l'année 1
     * 
     * @return String
     * @return the value of field 'pensionAlimentaire1'.
     */
    public java.lang.String getNePensionAlimentaire1() {
        return _nePensionAlimentaire1;
    } // -- java.lang.String getPensionAlimentaire1()

    /**
     * Returns the value of field 'pensionAnnee1'. The field 'pensionAnnee1' has the following description: Pension de
     * l'année 1
     * 
     * @return String
     * @return the value of field 'pensionAnnee1'.
     */
    public java.lang.String getNePensionAnnee1() {
        return _nePensionAnnee1;
    } // -- java.lang.String getPensionAnnee1()

    public java.lang.String getNePremiereLettreNom() {
        return _nePremiereLettreNom;
    }

    /**
     * Returns the value of field 'renteTotale'.
     * 
     * @return String
     * @return the value of field 'renteTotale'.
     */
    public java.lang.String getNeRenteTotale() {
        return _neRenteTotale;
    } // -- java.lang.String getRenteTotale()

    /**
     * Returns the value of field 'renteTotale1'. The field 'renteTotale1' has the following description: Rente totale
     * de l'année 1
     * 
     * @return String
     * @return the value of field 'renteTotale1'.
     */
    public java.lang.String getNeRenteTotale1() {
        return _neRenteTotale1;
    } // -- java.lang.String getRenteTotale1()

    /**
     * Returns the value of field 'renteViagere'.
     * 
     * @return String
     * @return the value of field 'renteViagere'.
     */
    public java.lang.String getNeRenteViagere() {
        return _neRenteViagere;
    } // -- java.lang.String getRenteViagere()

    /**
     * Returns the value of field 'renteViagere1'. The field 'renteViagere1' has the following description: Rente
     * viagère de l'année 1
     * 
     * @return String
     * @return the value of field 'renteViagere1'.
     */
    public java.lang.String getNeRenteViagere1() {
        return _neRenteViagere1;
    } // -- java.lang.String getRenteViagere1()

    /**
     * Returns the value of field 'taxationRectificative'.
     * 
     * @return boolean
     * @return the value of field 'taxationRectificative'.
     */
    public java.lang.String getNeTaxationRectificative() {
        return _neTaxationRectificative;
    } // -- boolean getTaxationRectificative()

    /**
     * Returns the value of field 'periodeIFD'. The field 'periodeIFD' has the following description: Période IFD
     * 
     * @return String
     * @return the value of field 'periodeIFD'.
     */
    public java.lang.String getPeriodeIFD() {
        return _periodeIFD;
    } // -- java.lang.String getPeriodeIFD()

    /**
     * Returns the value of field 'revenu1'. The field 'revenu1' has the following description: Revenu principal
     * 
     * @return String
     * @return the value of field 'revenu1'.
     */
    public java.lang.String getRevenu1() {
        return _revenu1;
    } // -- java.lang.String getRevenu1()

    /**
     * Returns the value of field 'revenu2'. The field 'revenu2' has the following description: Revenu secondaire
     * 
     * @return String
     * @return the value of field 'revenu2'.
     */
    public java.lang.String getRevenu2() {
        return _revenu2;
    } // -- java.lang.String getRevenu2()

    public String getVsAdresseAffilie1() {
        return vsAdresseAffilie1;
    }

    public String getVsAdresseAffilie2() {
        return vsAdresseAffilie2;
    }

    public String getVsAdresseAffilie3() {
        return vsAdresseAffilie3;
    }

    public String getVsAdresseAffilie4() {
        return vsAdresseAffilie4;
    }

    public String getVsAdresseConjoint1() {
        return vsAdresseConjoint1;
    }

    public String getVsAdresseConjoint2() {
        return vsAdresseConjoint2;
    }

    public String getVsAdresseConjoint3() {
        return vsAdresseConjoint3;
    }

    public String getVsAdresseConjoint4() {
        return vsAdresseConjoint4;
    }

    public String getVsAdresseCtb1() {
        return vsAdresseCtb1;
    }

    public String getVsAdresseCtb2() {
        return vsAdresseCtb2;
    }

    public String getVsAdresseCtb3() {
        return vsAdresseCtb3;
    }

    public String getVsAdresseCtb4() {
        return vsAdresseCtb4;
    }

    public String getVsAnneeTaxation() {
        return vsAnneeTaxation;
    }

    public String getVsAutresRevenusConjoint() {
        return vsAutresRevenusConjoint;
    }

    public String getVsAutresRevenusCtb() {
        return vsAutresRevenusCtb;
    }

    public String getVsCapitalPropreEngageEntrepriseConjoint() {
        return vsCapitalPropreEngageEntrepriseConjoint;
    }

    public String getVsCapitalPropreEngageEntrepriseCtb() {
        return vsCapitalPropreEngageEntrepriseCtb;
    }

    public String getVsCodeTaxation1() {
        return vsCodeTaxation1;
    }

    public String getVsCodeTaxation2() {
        return vsCodeTaxation2;
    }

    public String getVsCotisationAvsAffilie() {
        return vsCotisationAvsAffilie;
    }

    public String getVsCotisationAvsConjoint() {
        return vsCotisationAvsConjoint;
    }

    public String getVsDateCommunication() {
        return vsDateCommunication;
    }

    public String getVsDateDebutAffiliation() {
        return vsDateDebutAffiliation;
    }

    public String getVsDateDebutAffiliationCaisseProfessionnelle() {
        return vsDateDebutAffiliationCaisseProfessionnelle;
    }

    public String getVsDateDebutAffiliationConjoint() {
        return vsDateDebutAffiliationConjoint;
    }

    // unmarshal(java.io.Reader)

    public String getVsDateDebutAffiliationConjointCaisseProfessionnelle() {
        return vsDateDebutAffiliationConjointCaisseProfessionnelle;
    }

    public String getVsDateDecesCtb() {
        return vsDateDecesCtb;
    }

    public String getVsDateDemandeCommunication() {
        return vsDateDemandeCommunication;
    }

    public String getVsDateFinAffiliation() {
        return vsDateFinAffiliation;
    }

    public String getVsDateFinAffiliationCaisseProfessionnelle() {
        return vsDateFinAffiliationCaisseProfessionnelle;
    }

    public String getVsDateFinAffiliationConjoint() {
        return vsDateFinAffiliationConjoint;
    }

    public String getVsDateFinAffiliationConjointCaisseProfessionnelle() {
        return vsDateFinAffiliationConjointCaisseProfessionnelle;
    }

    public String getVsDateNaissanceAffilie() {
        return vsDateNaissanceAffilie;
    }

    public String getVsDateNaissanceConjoint() {
        return vsDateNaissanceConjoint;
    }

    public String getVsDateNaissanceCtb() {
        return vsDateNaissanceCtb;
    }

    public String getVsDateTaxation() {
        return vsDateTaxation;
    }

    public String getVsDebutActiviteConjoint() {
        return vsDebutActiviteConjoint;
    }

    public String getVsDebutActiviteCtb() {
        return vsDebutActiviteCtb;
    }

    public String getVsEtatCivilAffilie() {
        return vsEtatCivilAffilie;
    }

    public String getVsEtatCivilCtb() {
        return vsEtatCivilCtb;
    }

    public String getVsFinActiviteConjoint() {
        return vsFinActiviteConjoint;
    }

    public String getVsFinActiviteCtb() {
        return vsFinActiviteCtb;
    }

    public String getVsFortunePriveeConjoint() {
        return vsFortunePriveeConjoint;
    }

    public String getVsFortunePriveeCtb() {
        return vsFortunePriveeCtb;
    }

    public String getVsLangue() {
        return vsLangue;
    }

    public String getVsLibre3() {
        return vsLibre3;
    }

    public String getVsLibre4() {
        return vsLibre4;
    }

    public String getVsNbJoursTaxation() {
        return vsNbJoursTaxation;
    }

    public String getVsNoCaisseAgenceAffilie() {
        return vsNoCaisseAgenceAffilie;
    }

    public String getVsNoCaisseProfessionnelleAffilie() {
        return vsNoCaisseProfessionnelleAffilie;
    }

    public String getVsNomAffilie() {
        return vsNomAffilie;
    }

    public String getVsNomConjoint() {
        return vsNomConjoint;
    }

    public String getVsNomPrenomContribuableAnnee() {
        return vsNomPrenomContribuableAnnee;
    }

    public String getVsNoPostalLocalite() {
        return vsNoPostalLocalite;
    }

    public String getVsNumAffilie() {
        return vsNumAffilie;
    }

    public String getVsNumAffilieConjoint() {
        return vsNumAffilieConjoint;
    }

    public String getVsNumAffilieInterneCaisseProfessionnelle() {
        return vsNumAffilieInterneCaisseProfessionnelle;
    }

    public String getVsNumAffilieInterneConjointCaisseProfessionnelle() {
        return vsNumAffilieInterneConjointCaisseProfessionnelle;
    }

    public String getVsNumAvsAffilie() {
        return vsNumAvsAffilie;
    }

    public String getVsNumAvsConjoint() {
        return vsNumAvsConjoint;
    }

    public String getVsNumAvsCtb() {
        return vsNumAvsCtb;
    }

    public String getVsNumCaisseAgenceConjoint() {
        return vsNumCaisseAgenceConjoint;
    }

    public String getVsNumCaisseProfessionnelleConjoint() {
        return vsNumCaisseProfessionnelleConjoint;
    }

    public String getVsNumCtb() {
        return vsNumCtb;
    }

    public String getVsNumCtbSuivant() {
        return vsNumCtbSuivant;
    }

    public String getVsNumPostalLocaliteConjoint() {
        return vsNumPostalLocaliteConjoint;
    }

    public String getVsNumPostalLocaliteCtb() {
        return vsNumPostalLocaliteCtb;
    }

    public String getVsRachatLpp() {
        return vsRachatLpp;
    }

    public String getVsRachatLppCjt() {
        return vsRachatLppCjt;
    }

    public String getVsReserve() {
        return vsReserve;
    }

    public String getVsReserveDateNaissanceConjoint() {
        return vsReserveDateNaissanceConjoint;
    }

    public String getVsReserveFichierImpression() {
        return vsReserveFichierImpression;
    }

    public String getVsReserveTriNumCaisse() {
        return vsReserveTriNumCaisse;
    }

    public String getVsRevenuAgricoleConjoint() {
        return vsRevenuAgricoleConjoint;
    }

    public String getVsRevenuAgricoleCtb() {
        return vsRevenuAgricoleCtb;
    }

    public String getVsRevenuNonAgricoleConjoint() {
        return vsRevenuNonAgricoleConjoint;
    }

    public String getVsRevenuNonAgricoleCtb() {
        return vsRevenuNonAgricoleCtb;
    }

    public String getVsRevenuRenteConjoint() {
        return vsRevenuRenteConjoint;
    }

    public String getVsRevenuRenteCtb() {
        return vsRevenuRenteCtb;
    }

    public String getVsSalairesConjoint() {
        return vsSalairesConjoint;
    }

    public String getVsSalairesContribuable() {
        return vsSalairesContribuable;
    }

    public String getVsSexeAffilie() {
        return vsSexeAffilie;
    }

    public String getVsSexeCtb() {
        return vsSexeCtb;
    }

    public String getVsTypeTaxation() {
        return vsTypeTaxation;
    }

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } // -- boolean isValid()

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException,
            org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, out);
    } // -- void marshal(java.io.Writer)

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler) throws java.io.IOException,
            org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, handler);
    } // -- void marshal(org.xml.sax.ContentHandler)

    /**
     * Sets the value of field 'annee1'. The field 'annee1' has the following description: Annee fiscale 1
     * 
     * @param annee1
     *            the value of field 'annee1'.
     */
    public void setAnnee1(java.lang.String annee1) {
        _annee1 = annee1;
    } // -- void setAnnee1(java.lang.String)

    /**
     * Sets the value of field 'annee2'. The field 'annee2' has the following description: Annee fiscale 2
     * 
     * @param annee2
     *            the value of field 'annee2'.
     */
    public void setAnnee2(java.lang.String annee2) {
        _annee2 = annee2;
    } // -- void setAnnee2(java.lang.String)

    /**
     * Sets the value of field 'capital'.
     * 
     * @param capital
     *            the value of field 'capital'.
     */
    public void setCapital(java.lang.String capital) {
        _capital = capital;
    } // -- void setCapital(java.lang.String)

    /**
     * Sets the value of field 'cotisation1'.
     * 
     * @param cotisation1
     *            the value of field 'cotisation1'.
     */
    public void setCotisation1(java.lang.String cotisation1) {
        _cotisation1 = cotisation1;
    } // -- void setCotisation1(java.lang.String)

    /**
     * Sets the value of field 'cotisation2'.
     * 
     * @param cotisation2
     *            the value of field 'cotisation2'.
     */
    public void setCotisation2(java.lang.String cotisation2) {
        _cotisation2 = cotisation2;
    } // -- void setCotisation2(java.lang.String)

    /**
     * Sets the value of field 'dateFortune'.
     * 
     * @param dateFortune
     *            the value of field 'dateFortune'.
     */
    public void setDateFortune(java.lang.String dateFortune) {
        _dateFortune = dateFortune;
    } // -- void setDateFortune(java.lang.String)

    /**
     * Sets the value of field 'debutExercice1'. The field 'debutExercice1' has the following description: Date début
     * exercice1
     * 
     * @param debutExercice1
     *            the value of field 'debutExercice1'.
     */
    public void setDebutExercice1(java.lang.String debutExercice1) {
        _debutExercice1 = debutExercice1;
    } // -- void setDebutExercice1(java.lang.String)

    /**
     * Sets the value of field 'debutExercice2'. The field 'debutExercice2' has the following description: Date début
     * exercice2
     * 
     * @param debutExercice2
     *            the value of field 'debutExercice2'.
     */
    public void setDebutExercice2(java.lang.String debutExercice2) {
        _debutExercice2 = debutExercice2;
    } // -- void setDebutExercice2(java.lang.String)

    /**
     * Sets the value of field 'errorMessage'. The field 'errorMessage' has the following description: Contient toues
     * les messages de génération en cas d'erreur
     * 
     * @param errorMessage
     *            the value of field 'errorMessage'.
     */
    public void setErrorMessage(globaz.phenix.mapping.retour.castor.ErrorMessage errorMessage) {
        _errorMessage = errorMessage;
    } // -- void

    /**
     * Sets the value of field 'finExercice1'. The field 'finExercice1' has the following description: Date fin
     * exercice1
     * 
     * @param finExercice1
     *            the value of field 'finExercice1'.
     */
    public void setFinExercice1(java.lang.String finExercice1) {
        _finExercice1 = finExercice1;
    } // -- void setFinExercice1(java.lang.String)

    /**
     * Sets the value of field 'finExercice2'. The field 'finExercice2' has the following description: Date fin
     * exercice2
     * 
     * @param finExercice2
     *            the value of field 'finExercice2'.
     */
    public void setFinExercice2(java.lang.String finExercice2) {
        _finExercice2 = finExercice2;
    } // -- void setFinExercice2(java.lang.String)

    /**
     * Sets the value of field 'fortune'.
     * 
     * @param fortune
     *            the value of field 'fortune'.
     */
    public void setFortune(java.lang.String fortune) {
        _fortune = fortune;
    } // -- void setFortune(java.lang.String)

    /**
     * @param string
     */
    public void setGeBourses(java.lang.String string) {
        _geBourses = string;
    }

    /**
     * @param string
     */
    public void setGeDateTransfertMAD(java.lang.String string) {
        _geDateTransfertMAD = string;
    }

    /**
     * @param string
     */
    public void setGeDivers(java.lang.String string) {
        _geDivers = string;
    }

    /**
     * @param string
     */
    public void setGeExplicationDivers(java.lang.String string) {
        _geExplicationDivers = string;
    }

    /**
     * @param string
     */
    public void setGeGenreAffilie(java.lang.String string) {
        _geGenreAffilie = string;
    }

    /**
     * @param string
     */
    public void setGeImpositionSelonDepense(java.lang.String string) {
        _geImpositionSelonDepense = string;
    }

    /**
     * @param string
     */
    public void setGeImpotSource(java.lang.String string) {
        _geImpotSource = string;
    }

    /**
     * @param string
     */
    public void setGeIndemniteJournaliere(java.lang.String string) {
        _geIndemniteJournaliere = string;
    }

    public void setGeNNSS(java.lang.String string) {
        _geNNSS = string;
    }

    /**
     * @param string
     */
    public void setGeNom(java.lang.String string) {
        _geNom = string;
    }

    public void setGeNomAFC(String nomAFC) {
        _geNomAFC = nomAFC;
    }

    /**
     * @param string
     */
    public void setGeNomConjoint(java.lang.String string) {
        _geNomConjoint = string;
    }

    /**
     * @param string
     */
    public void setGeNonAssujettiIBO(java.lang.String string) {
        _geNonAssujettiIBO = string;
    }

    /**
     * @param string
     */
    public void setGeNonAssujettiIFD(java.lang.String string) {
        _geNonAssujettiIFD = string;
    }

    /**
     * Sets the value of field 'genreAffilie'. The field 'genreAffilie' has the following description: Genre
     * d'affiliation: Non actif, Indépendant...
     * 
     * @param genreAffilie
     *            the value of field 'genreAffilie'.
     */
    public void setGenreAffilie(java.lang.String genreAffilie) {
        _genreAffilie = genreAffilie;
    } // -- void setGenreAffilie(java.lang.String)

    /**
     * Sets the value of field 'genreTaxation'.
     * 
     * @param genreTaxation
     *            the value of field 'genreTaxation'.
     */
    public void setGenreTaxation(java.lang.String genreTaxation) {
        _genreTaxation = genreTaxation;
    } // -- void setGenreTaxation(java.lang.String)

    /**
     * @param string
     */
    public void setGeNumAffilie(java.lang.String string) {
        _geNumAffilie = string;
    }

    /**
     * @param string
     */
    public void setGeNumAvs(java.lang.String string) {
        _geNumAvs = string;
    }

    /**
     * @param string
     */
    public void setGeNumAvsConjoint(java.lang.String string) {
        _geNumAvsConjoint = string;
    }

    /**
     * @param string
     */
    public void setGeNumCaisse(java.lang.String string) {
        _geNumCaisse = string;
    }

    /**
     * @param string
     */
    public void setGeNumCommunication(java.lang.String string) {
        _geNumCommunication = string;
    }

    /**
     * @param string
     */
    public void setGeNumContribuable(java.lang.String string) {
        _geNumContribuable = string;
    }

    /**
     * @param string
     */
    public void setGeNumDemande(java.lang.String string) {
        _geNumDemande = string;
    }

    /**
     * @param string
     */
    public void setGeObservations(java.lang.String string) {
        _geObservations = string;
    }

    /**
     * @param string
     */
    public void setGePasActiviteDeclaree(java.lang.String string) {
        _gePasActiviteDeclaree = string;
    }

    /**
     * @param string
     */
    public void setGePension(java.lang.String string) {
        _gePension = string;
    }

    /**
     * @param string
     */
    public void setGePensionAlimentaire(java.lang.String string) {
        _gePensionAlimentaire = string;
    }

    public void setGePersonneNonIdentifiee(java.lang.String personneNonIdentifiee) {
        _gePersonneNonIdentifiee = personneNonIdentifiee;
    }

    /**
     * @param string
     */
    public void setGePrenom(java.lang.String string) {
        _gePrenom = string;
    }

    public void setGePrenomAFC(java.lang.String prenomAFC) {
        _gePrenomAFC = prenomAFC;
    }

    /**
     * @param string
     */
    public void setGePrenomConjoint(java.lang.String string) {
        _gePrenomConjoint = string;
    }

    /**
     * @param string
     */
    public void setGeRenteInvalidite(java.lang.String string) {
        _geRenteInvalidite = string;
    }

    /**
     * @param string
     */
    public void setGeRenteViagere(java.lang.String string) {
        _geRenteViagere = string;
    }

    /**
     * @param string
     */
    public void setGeRenteVieillesse(java.lang.String string) {
        _geRenteVieillesse = string;
    }

    /**
     * @param string
     */
    public void setGeRetraite(java.lang.String string) {
        _geRetraite = string;
    }

    /**
     * @param string
     */
    public void setGeTaxationOffice(java.lang.String string) {
        _geTaxationOffice = string;
    }

    /**
     * Sets the value of field 'idConjoint'.
     * 
     * @param idConjoint
     *            the value of field 'idConjoint'.
     */
    public void setIdConjoint(java.lang.String idConjoint) {
        _idConjoint = idConjoint;
    }

    /**
     * Sets the value of field 'idDemande'. The field 'idDemande' has the following description: Identification de la
     * demande ayant engendré cette réponse
     * 
     * @param idDemande
     *            the value of field 'idDemande'.
     */
    public void setIdDemande(java.lang.String idDemande) {
        _idDemande = idDemande;
    } // -- void setIdDemande(java.lang.String)

    /**
     * Sets the value of field 'codeJu'.
     * 
     * @param codeJu
     *            the value of field 'codeJu'.
     */
    public void setJuCodeApplication(java.lang.String codeJu) {
        _juCodeApplication = codeJu;
    } // -- void setCodeJu(java.lang.String)

    public void setJuDateNaissance(java.lang.String dateNaissance) {
        _juDateNaissance = dateNaissance;
    }

    /**
     * Sets the value of field 'epoux'.
     * 
     * @param epoux
     *            the value of field 'epoux'.
     */
    public void setJuEpoux(java.lang.String epoux) {
        _juEpoux = epoux;
    } // -- void setEpoux(java.lang.String)

    /**
     * Sets the value of field 'fillerJu'.
     * 
     * @param fillerJu
     *            the value of field 'fillerJu'.
     */
    public void setJuFiller(java.lang.String fillerJu) {
        _juFiller = fillerJu;
    } // -- void setFillerJu(java.lang.String)

    public void setJuGenreAffilie(java.lang.String genreAffilie) {
        _juGenreAffilie = genreAffilie;
    } // -- void setGenreAffilie(java.lang.String)

    public void setJuGenreTaxation(java.lang.String genreTaxation) {
        _juGenreTaxation = genreTaxation;
    }

    /**
     * Sets the value of field 'lot'.
     * 
     * @param lot
     *            the value of field 'lot'.
     */
    public void setJuLot(java.lang.String lot) {
        _juLot = lot;
    } // -- void setLot(java.lang.String)

    /**
     * Sets the value of field 'nbrJour1'.
     * 
     * @param nbrJour1
     *            the value of field 'nbrJour1'.
     */
    public void setJuNbrJour1(java.lang.String nbrJour1) {
        _juNbrJour1 = nbrJour1;
    } // -- void setNbrJour1(java.lang.String)

    /**
     * Sets the value of field 'nbrJour2'.
     * 
     * @param nbrJour2
     *            the value of field 'nbrJour2'.
     */
    public void setJuNbrJour2(java.lang.String nbrJour2) {
        _juNbrJour2 = nbrJour2;
    } // -- void setNbrJour2(java.lang.String)

    public void setJuNewNumContribuable(java.lang.String newNumContribuable) {
        _juNewNumContribuable = newNumContribuable;
    }

    public void setJuNumContribuable(java.lang.String numCtb) {
        _juNumContribuable = numCtb;
    }

    /**
     * Sets the value of field 'taxeMan'.
     * 
     * @param taxeMan
     *            the value of field 'taxeMan'.
     */
    public void setJuTaxeMan(java.lang.String taxeMan) {
        _juTaxeMan = taxeMan;
    } // -- void setTaxeMan(java.lang.String)

    public void setMajNumContribuable(String majNumContribuable) {
        this.majNumContribuable = majNumContribuable;
    }

    /**
     * Sets the value of field 'dateDebutAssuj'.
     * 
     * @param dateDebutAssuj
     *            the value of field 'dateDebutAssuj'.
     */
    public void setNeDateDebutAssuj(java.lang.String dateDebutAssuj) {
        _neDateDebutAssuj = dateDebutAssuj;
    } // -- void setDateDebutAssuj(java.lang.String)

    /**
     * Sets the value of field 'dateValeur'.
     * 
     * @param dateValeur
     *            the value of field 'dateValeur'.
     */
    public void setNeDateValeur(java.lang.String dateValeur) {
        _neDateValeur = dateValeur;
    } // -- void setDateValeur(java.lang.String)

    /**
     * Sets the value of field 'dossierTaxe'.
     * 
     * @param dossierTaxe
     *            the value of field 'dossierTaxe'.
     */
    public void setNeDossierTaxe(java.lang.String dossierTaxe) {
        _neDossierTaxe = dossierTaxe;
    } // -- void setDossierTaxe(java.lang.String)

    /**
     * Sets the value of field 'dossierTrouve'.
     * 
     * @param dossierTrouve
     *            the value of field 'dossierTrouve'.
     */
    public void setNeDossierTrouve(java.lang.String dossierTrouve) {
        _neDossierTrouve = dossierTrouve;
    } // -- void setDossierTrouve(java.lang.String)

    /**
     * Sets the value of field 'fillerNe'.
     * 
     * @param fillerNe
     *            the value of field 'fillerNe'.
     */
    public void setNeFiller(java.lang.String fillerNe) {
        _neFiller = fillerNe;
    } // -- void setFillerNe(java.lang.String)

    /**
     * Sets the value of field 'fortuneAnnee1'. The field 'fortuneAnnee1' has the following description: Fortune de
     * l'année 1
     * 
     * @param fortuneAnnee1
     *            the value of field 'fortuneAnnee1'.
     */
    public void setNeFortuneAnnee1(java.lang.String fortuneAnnee1) {
        _neFortuneAnnee1 = fortuneAnnee1;
    } // -- void setFortuneAnnee1(java.lang.String)

    public void setNeGenreAffilie(java.lang.String genreAffilie) {
        _neGenreAffilie = genreAffilie;
    } // -- void setGenreAffilie(java.lang.String)

    public void setNeGenreTaxation(java.lang.String genreTaxation) {
        _neGenreTaxation = genreTaxation;
    }

    /**
     * Sets the value of field 'indemniteJour'.
     * 
     * @param indemniteJour
     *            the value of field 'indemniteJour'.
     */
    public void setNeIndemniteJour(java.lang.String indemniteJour) {
        _neIndemniteJour = indemniteJour;
    } // -- void setIndemniteJour(java.lang.String)

    /**
     * Sets the value of field 'indemniteJour1'. The field 'indemniteJour1' has the following description: Indémnité
     * journalière de l'année 1
     * 
     * @param indemniteJour1
     *            the value of field 'indemniteJour1'.
     */
    public void setNeIndemniteJour1(java.lang.String indemniteJour1) {
        _neIndemniteJour1 = indemniteJour1;
    } // -- void setIndemniteJour1(java.lang.String)

    /**
     * Sets the value of field 'numAvsCtb'. The field 'numAvsCtb' has the following description: Numéro AVS du
     * contribuable
     * 
     * @param numAvsCtb
     *            the value of field 'numAvsCtb'.
     */
    public void setNeNumAvs(java.lang.String numAvsCtb) {
        _neNumAvs = numAvsCtb;
    } // -- void setNumAvsCtb(java.lang.String)

    public void setNeNumBDP(java.lang.String numBDP) {
        _neNumBDP = numBDP;
    }

    /**
     * Sets the value of field 'numCaiAvsCtb'. The field 'numCaiAvsCtb' has the following description: Numéro Caisse
     * Avs du contribuable
     * 
     * @param numCaiAvsCtb
     *            the value of field 'numCaiAvsCtb'.
     */
    public void setNeNumCaisse(java.lang.String numCaiAvsCtb) {
        _neNumCaisse = numCaiAvsCtb;
    } // -- void setNumCaiAvsCtb(java.lang.String)

    /**
     * Sets the value of field 'numClient'. The field 'numClient' has the following description: Numéro de client
     * 
     * @param numClient
     *            the value of field 'numClient'.
     */
    public void setNeNumClient(java.lang.String numClient) {
        _neNumClient = numClient;
    } // -- void setNumClient(java.lang.String)

    /**
     * Sets the value of field 'numCommune'. The field 'numCommune' has the following description: Numéro de commune
     * 
     * @param numCommune
     *            the value of field 'numCommune'.
     */
    public void setNeNumCommune(java.lang.String numCommune) {
        _neNumCommune = numCommune;
    } // -- void setNumCommune(java.lang.String)

    /**
     * Sets the value of field 'numCtb'. The field 'numCtb' has the following description: Numéro de contribuable
     * 
     * @param numCtb
     *            the value of field 'numCtb'.
     */
    public void setNeNumContribuable(java.lang.String numCtb) {
        _neNumContribuable = numCtb;
    } // -- void setNumCtb(java.lang.String)

    /**
     * Sets the value of field 'pension'.
     * 
     * @param pension
     *            the value of field 'pension'.
     */
    public void setNePension(java.lang.String pension) {
        _nePension = pension;
    } // -- void setPension(java.lang.String)

    /**
     * Sets the value of field 'pensionAlimentaire'.
     * 
     * @param pensionAlimentaire
     *            the value of field 'pensionAlimentaire'.
     */
    public void setNePensionAlimentaire(java.lang.String pensionAlimentaire) {
        _nePensionAlimentaire = pensionAlimentaire;
    } // -- void setPensionAlimentaire(java.lang.String)

    /**
     * Sets the value of field 'pensionAlimentaire1'. The field 'pensionAlimentaire1' has the following description:
     * Pension alimentaire de l'année 1
     * 
     * @param pensionAlimentaire1
     *            the value of field 'pensionAlimentaire1'.
     */
    public void setNePensionAlimentaire1(java.lang.String pensionAlimentaire1) {
        _nePensionAlimentaire1 = pensionAlimentaire1;
    } // -- void setPensionAlimentaire1(java.lang.String)

    /**
     * Sets the value of field 'pensionAnnee1'. The field 'pensionAnnee1' has the following description: Pension de
     * l'année 1
     * 
     * @param pensionAnnee1
     *            the value of field 'pensionAnnee1'.
     */
    public void setNePensionAnnee1(java.lang.String pensionAnnee1) {
        _nePensionAnnee1 = pensionAnnee1;
    } // -- void setPensionAnnee1(java.lang.String)

    public void setNePremiereLettreNom(java.lang.String nePremiereLettreNom) {
        _nePremiereLettreNom = nePremiereLettreNom;
    }

    /**
     * Sets the value of field 'renteTotale'.
     * 
     * @param renteTotale
     *            the value of field 'renteTotale'.
     */
    public void setNeRenteTotale(java.lang.String renteTotale) {
        _neRenteTotale = renteTotale;
    } // -- void setRenteTotale(java.lang.String)

    /**
     * Sets the value of field 'renteTotale1'. The field 'renteTotale1' has the following description: Rente totale de
     * l'année 1
     * 
     * @param renteTotale1
     *            the value of field 'renteTotale1'.
     */
    public void setNeRenteTotale1(java.lang.String renteTotale1) {
        _neRenteTotale1 = renteTotale1;
    } // -- void setRenteTotale1(java.lang.String)

    /**
     * Sets the value of field 'renteViagere'.
     * 
     * @param renteViagere
     *            the value of field 'renteViagere'.
     */
    public void setNeRenteViagere(java.lang.String renteViagere) {
        _neRenteViagere = renteViagere;
    } // -- void setRenteViagere(java.lang.String)

    /**
     * Sets the value of field 'renteViagere1'. The field 'renteViagere1' has the following description: Rente viagère
     * de l'année 1
     * 
     * @param renteViagere1
     *            the value of field 'renteViagere1'.
     */
    public void setNeRenteViagere1(java.lang.String renteViagere1) {
        _neRenteViagere1 = renteViagere1;
    } // -- void setRenteViagere1(java.lang.String)

    /**
     * Sets the value of field 'taxationRectificative'.
     * 
     * @param taxationRectificative
     *            the value of field 'taxationRectificative'.
     */
    public void setNeTaxationRectificative(java.lang.String taxationRectificative) {
        _neTaxationRectificative = taxationRectificative;
    } // -- void setTaxationRectificative(boolean)

    /**
     * Sets the value of field 'periodeIFD'. The field 'periodeIFD' has the following description: Période IFD
     * 
     * @param periodeIFD
     *            the value of field 'periodeIFD'.
     */
    public void setPeriodeIFD(java.lang.String periodeIFD) {
        _periodeIFD = periodeIFD;
    } // -- void setPeriodeIFD(java.lang.String)

    /**
     * Sets the value of field 'revenu1'. The field 'revenu1' has the following description: Revenu principal
     * 
     * @param revenu1
     *            the value of field 'revenu1'.
     */
    public void setRevenu1(java.lang.String revenu1) {
        _revenu1 = revenu1;
    } // -- void setRevenu1(java.lang.String)

    /**
     * Sets the value of field 'revenu2'. The field 'revenu2' has the following description: Revenu secondaire
     * 
     * @param revenu2
     *            the value of field 'revenu2'.
     */
    public void setRevenu2(java.lang.String revenu2) {
        _revenu2 = revenu2;
    } // -- void setRevenu2(java.lang.String)

    public void setVsAdresseAffilie1(String vsAdresseAffilie1) {
        this.vsAdresseAffilie1 = vsAdresseAffilie1;
    }

    public void setVsAdresseAffilie2(String vsAdresseAffilie2) {
        this.vsAdresseAffilie2 = vsAdresseAffilie2;
    }

    public void setVsAdresseAffilie3(String vsAdresseAffilie3) {
        this.vsAdresseAffilie3 = vsAdresseAffilie3;
    }

    public void setVsAdresseAffilie4(String vsAdresseAffilie4) {
        this.vsAdresseAffilie4 = vsAdresseAffilie4;
    }

    public void setVsAdresseConjoint1(String vsAdresseConjoint1) {
        this.vsAdresseConjoint1 = vsAdresseConjoint1;
    }

    public void setVsAdresseConjoint2(String vsAdresseConjoint2) {
        this.vsAdresseConjoint2 = vsAdresseConjoint2;
    }

    public void setVsAdresseConjoint3(String vsAdresseConjoint3) {
        this.vsAdresseConjoint3 = vsAdresseConjoint3;
    }

    public void setVsAdresseConjoint4(String vsAdresseConjoint4) {
        this.vsAdresseConjoint4 = vsAdresseConjoint4;
    }

    public void setVsAdresseCtb1(String vsAdresseCtb1) {
        this.vsAdresseCtb1 = vsAdresseCtb1;
    }

    public void setVsAdresseCtb2(String vsAdresseCtb2) {
        this.vsAdresseCtb2 = vsAdresseCtb2;
    }

    public void setVsAdresseCtb3(String vsAdresseCtb3) {
        this.vsAdresseCtb3 = vsAdresseCtb3;
    }

    public void setVsAdresseCtb4(String vsAdresseCtb4) {
        this.vsAdresseCtb4 = vsAdresseCtb4;
    }

    public void setVsAnneeTaxation(String vsAnneeTaxation) {
        this.vsAnneeTaxation = vsAnneeTaxation;
    }

    public void setVsAutresRevenusConjoint(String vsAutresRevenusConjoint) {
        this.vsAutresRevenusConjoint = vsAutresRevenusConjoint;
    }

    public void setVsAutresRevenusCtb(String vsAutresRevenusCtb) {
        this.vsAutresRevenusCtb = vsAutresRevenusCtb;
    }

    public void setVsCapitalPropreEngageEntrepriseConjoint(String vsCapitalPropreEngageEntrepriseConjoint) {
        this.vsCapitalPropreEngageEntrepriseConjoint = vsCapitalPropreEngageEntrepriseConjoint;
    }

    public void setVsCapitalPropreEngageEntrepriseCtb(String vsCapitalPropreEngageEntrepriseCtb) {
        this.vsCapitalPropreEngageEntrepriseCtb = vsCapitalPropreEngageEntrepriseCtb;
    }

    public void setVsCodeTaxation1(String vsCodeTaxation1) {
        this.vsCodeTaxation1 = vsCodeTaxation1;
    }

    public void setVsCodeTaxation2(String vsCodeTaxation2) {
        this.vsCodeTaxation2 = vsCodeTaxation2;
    }

    public void setVsCotisationAvsAffilie(String vsCotisationAvsAffilie) {
        this.vsCotisationAvsAffilie = vsCotisationAvsAffilie;
    }

    public void setVsCotisationAvsConjoint(String vsCotisationAvsConjoint) {
        this.vsCotisationAvsConjoint = vsCotisationAvsConjoint;
    }

    public void setVsDateCommunication(String vsDateCommunication) {
        this.vsDateCommunication = vsDateCommunication;
    }

    public void setVsDateDebutAffiliation(String vsDateDebutAffiliation) {
        this.vsDateDebutAffiliation = vsDateDebutAffiliation;
    }

    public void setVsDateDebutAffiliationCaisseProfessionnelle(String vsDateDebutAffiliationCaisseProfessionnelle) {
        this.vsDateDebutAffiliationCaisseProfessionnelle = vsDateDebutAffiliationCaisseProfessionnelle;
    }

    public void setVsDateDebutAffiliationConjoint(String vsDateDebutAffiliationConjoint) {
        this.vsDateDebutAffiliationConjoint = vsDateDebutAffiliationConjoint;
    }

    public void setVsDateDebutAffiliationConjointCaisseProfessionnelle(
            String vsDateDebutAffiliationConjointCaisseProfessionnelle) {
        this.vsDateDebutAffiliationConjointCaisseProfessionnelle = vsDateDebutAffiliationConjointCaisseProfessionnelle;
    }

    public void setVsDateDecesCtb(String vsDateDecesCtb) {
        this.vsDateDecesCtb = vsDateDecesCtb;
    }

    public void setVsDateDemandeCommunication(String vsDateDemandeCommunication) {
        this.vsDateDemandeCommunication = vsDateDemandeCommunication;
    }

    public void setVsDateFinAffiliation(String vsDateFinAffiliation) {
        this.vsDateFinAffiliation = vsDateFinAffiliation;
    }

    public void setVsDateFinAffiliationCaisseProfessionnelle(String vsDateFinAffiliationCaisseProfessionnelle) {
        this.vsDateFinAffiliationCaisseProfessionnelle = vsDateFinAffiliationCaisseProfessionnelle;
    }

    public void setVsDateFinAffiliationConjoint(String vsDateFinAffiliationConjoint) {
        this.vsDateFinAffiliationConjoint = vsDateFinAffiliationConjoint;
    }

    public void setVsDateFinAffiliationConjointCaisseProfessionnelle(
            String vsDateFinAffiliationConjointCaisseProfessionnelle) {
        this.vsDateFinAffiliationConjointCaisseProfessionnelle = vsDateFinAffiliationConjointCaisseProfessionnelle;
    }

    public void setVsDateNaissanceAffilie(String vsDateNaissanceAffilie) {
        this.vsDateNaissanceAffilie = vsDateNaissanceAffilie;
    }

    public void setVsDateNaissanceConjoint(String vsDateNaissanceConjoint) {
        this.vsDateNaissanceConjoint = vsDateNaissanceConjoint;
    }

    public void setVsDateNaissanceCtb(String vsDateNaissanceCtb) {
        this.vsDateNaissanceCtb = vsDateNaissanceCtb;
    }

    public void setVsDateTaxation(String vsDateTaxation) {
        this.vsDateTaxation = vsDateTaxation;
    }

    public void setVsDebutActiviteConjoint(String vsDebutActiviteConjoint) {
        this.vsDebutActiviteConjoint = vsDebutActiviteConjoint;
    }

    public void setVsDebutActiviteCtb(String vsDebutActiviteCtb) {
        this.vsDebutActiviteCtb = vsDebutActiviteCtb;
    }

    public void setVsEtatCivilAffilie(String vsEtatCivilAffilie) {
        this.vsEtatCivilAffilie = vsEtatCivilAffilie;
    }

    public void setVsEtatCivilCtb(String vsEtatCivilCtb) {
        this.vsEtatCivilCtb = vsEtatCivilCtb;
    }

    public void setVsFinActiviteConjoint(String vsFinActiviteConjoint) {
        this.vsFinActiviteConjoint = vsFinActiviteConjoint;
    }

    public void setVsFinActiviteCtb(String vsFinActiviteCtb) {
        this.vsFinActiviteCtb = vsFinActiviteCtb;
    }

    public void setVsFortunePriveeConjoint(String vsFortunePriveeConjoint) {
        this.vsFortunePriveeConjoint = vsFortunePriveeConjoint;
    }

    public void setVsFortunePriveeCtb(String vsFortunePriveeCtb) {
        this.vsFortunePriveeCtb = vsFortunePriveeCtb;
    }

    public void setVsLangue(String vsLangue) {
        this.vsLangue = vsLangue;
    }

    public void setVsLibre3(String vsLibre3) {
        this.vsLibre3 = vsLibre3;
    }

    public void setVsLibre4(String vsLibre4) {
        this.vsLibre4 = vsLibre4;
    }

    public void setVsNbJoursTaxation(String vsNbJoursTaxation) {
        this.vsNbJoursTaxation = vsNbJoursTaxation;
    }

    public void setVsNoCaisseAgenceAffilie(String vsNoCaisseAgenceAffilie) {
        this.vsNoCaisseAgenceAffilie = vsNoCaisseAgenceAffilie;
    }

    public void setVsNoCaisseProfessionnelleAffilie(String vsNoCaisseProfessionnelleAffilie) {
        this.vsNoCaisseProfessionnelleAffilie = vsNoCaisseProfessionnelleAffilie;
    }

    public void setVsNomAffilie(String vsNomAffilie) {
        this.vsNomAffilie = vsNomAffilie;
    }

    public void setVsNomConjoint(String vsNomConjoint) {
        this.vsNomConjoint = vsNomConjoint;
    }

    public void setVsNomPrenomContribuableAnnee(String vsNomPrenomContribuableAnnee) {
        this.vsNomPrenomContribuableAnnee = vsNomPrenomContribuableAnnee;
    }

    public void setVsNoPostalLocalite(String vsNoPostalLocalite) {
        this.vsNoPostalLocalite = vsNoPostalLocalite;
    }

    public void setVsNumAffilie(String vsNumAffilie) {
        this.vsNumAffilie = vsNumAffilie;
    }

    public void setVsNumAffilieConjoint(String vsNumAffilieConjoint) {
        this.vsNumAffilieConjoint = vsNumAffilieConjoint;
    }

    public void setVsNumAffilieInterneCaisseProfessionnelle(String vsNumAffilieInterneCaisseProfessionnelle) {
        this.vsNumAffilieInterneCaisseProfessionnelle = vsNumAffilieInterneCaisseProfessionnelle;
    }

    public void setVsNumAffilieInterneConjointCaisseProfessionnelle(
            String vsNumAffilieInterneConjointCaisseProfessionnelle) {
        this.vsNumAffilieInterneConjointCaisseProfessionnelle = vsNumAffilieInterneConjointCaisseProfessionnelle;
    }

    public void setVsNumAvsAffilie(String vsNumAvsAffilie) {
        this.vsNumAvsAffilie = vsNumAvsAffilie;
    }

    public void setVsNumAvsConjoint(String vsNumAvsConjoint) {
        this.vsNumAvsConjoint = vsNumAvsConjoint;
    }

    public void setVsNumAvsCtb(String vsNumAvsCtb) {
        this.vsNumAvsCtb = vsNumAvsCtb;
    }

    public void setVsNumCaisseAgenceConjoint(String vsNumCaisseAgenceConjoint) {
        this.vsNumCaisseAgenceConjoint = vsNumCaisseAgenceConjoint;
    }

    public void setVsNumCaisseProfessionnelleConjoint(String vsNumCaisseProfessionnelleConjoint) {
        this.vsNumCaisseProfessionnelleConjoint = vsNumCaisseProfessionnelleConjoint;
    }

    public void setVsNumCtb(String vsNumCtb) {
        this.vsNumCtb = vsNumCtb;
    }

    public void setVsNumCtbSuivant(String vsNumCtbSuivant) {
        this.vsNumCtbSuivant = vsNumCtbSuivant;
    }

    public void setVsNumPostalLocaliteConjoint(String vsNumPostalLocaliteConjoint) {
        this.vsNumPostalLocaliteConjoint = vsNumPostalLocaliteConjoint;
    }

    public void setVsNumPostalLocaliteCtb(String vsNumPostalLocaliteCtb) {
        this.vsNumPostalLocaliteCtb = vsNumPostalLocaliteCtb;
    }

    public void setVsRachatLpp(String vsRachatLpp) {
        this.vsRachatLpp = vsRachatLpp;
    }

    public void setVsRachatLppCjt(String vsRachatLppCjt) {
        this.vsRachatLppCjt = vsRachatLppCjt;
    }

    public void setVsReserve(String vsReserve) {
        this.vsReserve = vsReserve;
    }

    public void setVsReserveDateNaissanceConjoint(String vsReserveDateNaissanceConjoint) {
        this.vsReserveDateNaissanceConjoint = vsReserveDateNaissanceConjoint;
    }

    public void setVsReserveFichierImpression(String vsReserveFichierImpression) {
        this.vsReserveFichierImpression = vsReserveFichierImpression;
    }

    public void setVsReserveTriNumCaisse(String vsReserveTriNumCaisse) {
        this.vsReserveTriNumCaisse = vsReserveTriNumCaisse;
    }

    public void setVsRevenuAgricoleConjoint(String vsRevenuAgricoleConjoint) {
        this.vsRevenuAgricoleConjoint = vsRevenuAgricoleConjoint;
    }

    public void setVsRevenuAgricoleCtb(String vsRevenuAgricoleCtb) {
        this.vsRevenuAgricoleCtb = vsRevenuAgricoleCtb;
    }

    public void setVsRevenuNonAgricoleConjoint(String vsRevenuNonAgricoleConjoint) {
        this.vsRevenuNonAgricoleConjoint = vsRevenuNonAgricoleConjoint;
    }

    public void setVsRevenuNonAgricoleCtb(String vsRevenuNonAgricoleCtb) {
        this.vsRevenuNonAgricoleCtb = vsRevenuNonAgricoleCtb;
    }

    public void setVsRevenuRenteConjoint(String vsRevenuRenteConjoint) {
        this.vsRevenuRenteConjoint = vsRevenuRenteConjoint;
    }

    public void setVsRevenuRenteCtb(String vsRevenuRenteCtb) {
        this.vsRevenuRenteCtb = vsRevenuRenteCtb;
    }

    public void setVsSalairesConjoint(String vsSalairesConjoint) {
        this.vsSalairesConjoint = vsSalairesConjoint;
    }

    public void setVsSalairesContribuable(String vsSalairesContribuable) {
        this.vsSalairesContribuable = vsSalairesContribuable;
    }

    public void setVsSexeAffilie(String vsSexeAffilie) {
        this.vsSexeAffilie = vsSexeAffilie;
    }

    public void setVsSexeCtb(String vsSexeCtb) {
        this.vsSexeCtb = vsSexeCtb;
    }

    public void setVsTypeTaxation(String vsTypeTaxation) {
        this.vsTypeTaxation = vsTypeTaxation;
    }

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
