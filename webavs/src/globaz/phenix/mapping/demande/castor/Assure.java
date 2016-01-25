/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: Assure.java,v 1.1 2006/06/01 11:02:21 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Assurés pour lesquels la demande est établie
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:21 $
 */
public class Assure implements java.io.Serializable {

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
     * @return Object
     */
    public static java.lang.Object unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException,
            org.exolab.castor.xml.ValidationException {
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.Assure.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * Zone adresse du contribuable, en principe la rue Canton VD AdrGrp:adrLigne3 Canton ZH et SG: StrasseSteupfl
     */
    private java.lang.String _adresseRueCtb;

    /**
     * Ligne d'Adresse supplépmentaire Canton VD AdrGrp:adrLigne2 Canton ZH et SG: AdrZusSteupfl
     */
    private java.lang.String _adresseSupCtb;

    /**
     * Année concernée Canton ZH et SG: BemJahr Canton Vd: inutile
     */
    private java.lang.String _anneeConcernee;

    /**
     * Canton du contribuable canton ZH et SG: EmpfSteu, obligatoire canton VD: inutile
     */
    private java.lang.String _canton;

    /**
     * Catégorie de l'assuré. L'une des 4 valeurs : "PCI" = Indépendants ou exploitants "PSA" = Sans activité
     * lucrative "SAL" = Salariés affiliés indépendants "AGR" = Collaborateurs agricoles Canton ZH et SG: BeitrArt
     */
    private globaz.phenix.mapping.demande.castor.CatAssure _catAssure;

    /**
     * Code de l'affiliation Canton ZH et SG: ErfAlsC
     */
    private java.lang.String _codeAffiliation;

    /**
     * Date de début de l'affiliation Canton ZH et SG: ErfSeitD
     */
    private java.lang.String _dateDebutAffiliation;

    /**
     * Date de fin de l'affiliation Canton ZH et SG: AbgDat
     */
    private java.lang.String _dateFinAffiliation;

    /**
     * Date de naissance de l'épouse Canton ZH et SG: GeburtsDatEhefrau
     */
    private java.lang.String _dateNaiEpouse;

    /**
     * Date à laquelle l'affiliation a été saisie Canton ZH et SG: ErfAlsD
     */
    private java.lang.String _dateSaisieAffiliation;

    /**
     * Mois (mm) de début de la période pour laquelle la demande est faite. Par défaut 01 = janvier
     */
    private int _debPrdConcernee;

    /**
     * Mois (mm) de fin de la période pour laquelle la demande est faite. Par défaut 12 = décembre
     */
    private int _finPrdConcernee;

    /**
     * keeps track of state for field: _debPrdConcernee
     */
    private boolean _has_debPrdConcernee;

    /**
     * keeps track of state for field: _finPrdConcernee
     */
    private boolean _has_finPrdConcernee;

    /**
     * Domicile du contribuable Canton VD AdrGrp:adrLigne4 concaténée Canton ZH et SG: OrtSteupfl
     */
    private java.lang.String _lieuCtb;

    /**
     * Nom du contribuable Canton VD AdrGrp:adrLigne1 Canton ZH et SG: NameSteupfl
     */
    private java.lang.String _nomCtb;

    /**
     * Nom de l'entreprise Canton ZH et SG: NameBetr
     */
    private java.lang.String _nomEntreprise;

    /**
     * Code postal du lieu de domicile du contribuable Canton VD AdrGrp:adrLigne4 Canton ZH et SG: PLZSteupfl
     */
    private java.lang.String _npaCtb;

    /**
     * Code postal du lieu de l'entreprise Canton ZH et SG: PLZOrtBetr
     */
    private java.lang.String _npaEntreprise;

    /**
     * Numéro d'affilié de l'assuré Canton ZH et SG: AbrNr
     */
    private java.lang.String _numAffilie;

    /**
     * Numéro Avs de l'assuré Canton ZH et SG: VersNr
     */
    private java.lang.String _numAvs;

    /**
     * Numéro Avs alternatif, par exemple num Avs de l'entreprise Canton ZH et SG: VersNrBi
     */
    private java.lang.String _numAvsAlt;

    /**
     * Numéro de la caisse Avs du contribuable Canton ZH et SG: AbsKasse
     */
    private java.lang.String _numCaiAvsCtb;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Numéro de contribuable de l'assuré Canton ZH et SG: inutile
     */
    private java.lang.String _numCtb;

    // -----------/
    // - Methods -/
    // -----------/

    public Assure() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.Assure()

    /**
     * Method deleteDebPrdConcernee
     * 
     */
    public void deleteDebPrdConcernee() {
        _has_debPrdConcernee = false;
    } // -- void deleteDebPrdConcernee()

    /**
     * Method deleteFinPrdConcernee
     * 
     */
    public void deleteFinPrdConcernee() {
        _has_finPrdConcernee = false;
    } // -- void deleteFinPrdConcernee()

    /**
     * Returns the value of field 'adresseRueCtb'. The field 'adresseRueCtb' has the following description: Zone adresse
     * du contribuable, en principe la rue Canton VD AdrGrp:adrLigne3 Canton ZH et SG: StrasseSteupfl
     * 
     * @return String
     * @return the value of field 'adresseRueCtb'.
     */
    public java.lang.String getAdresseRueCtb() {
        return _adresseRueCtb;
    } // -- java.lang.String getAdresseRueCtb()

    /**
     * Returns the value of field 'adresseSupCtb'. The field 'adresseSupCtb' has the following description: Ligne
     * d'Adresse supplépmentaire Canton VD AdrGrp:adrLigne2 Canton ZH et SG: AdrZusSteupfl
     * 
     * @return String
     * @return the value of field 'adresseSupCtb'.
     */
    public java.lang.String getAdresseSupCtb() {
        return _adresseSupCtb;
    } // -- java.lang.String getAdresseSupCtb()

    /**
     * Returns the value of field 'anneeConcernee'. The field 'anneeConcernee' has the following description: Année
     * concernée Canton ZH et SG: BemJahr Canton Vd: inutile
     * 
     * @return String
     * @return the value of field 'anneeConcernee'.
     */
    public java.lang.String getAnneeConcernee() {
        return _anneeConcernee;
    } // -- java.lang.String getAnneeConcernee()

    /**
     * Returns the value of field 'canton'. The field 'canton' has the following description: Canton du contribuable
     * canton ZH et SG: EmpfSteu, obligatoire canton VD: inutile
     * 
     * @return String
     * @return the value of field 'canton'.
     */
    public java.lang.String getCanton() {
        return _canton;
    } // -- java.lang.String getCanton()

    /**
     * Returns the value of field 'catAssure'. The field 'catAssure' has the following description: Catégorie de
     * l'assuré. L'une des 4 valeurs : "PCI" = Indépendants ou exploitants "PSA" = Sans activité lucrative "SAL" =
     * Salariés affiliés indépendants "AGR" = Collaborateurs agricoles Canton ZH et SG: BeitrArt
     * 
     * @return CatAssure
     * @return the value of field 'catAssure'.
     */
    public globaz.phenix.mapping.demande.castor.CatAssure getCatAssure() {
        return _catAssure;
    } // -- globaz.phenix.mapping.demande.castor.CatAssure getCatAssure()

    /**
     * Returns the value of field 'codeAffiliation'. The field 'codeAffiliation' has the following description: Code de
     * l'affiliation Canton ZH et SG: ErfAlsC
     * 
     * @return String
     * @return the value of field 'codeAffiliation'.
     */
    public java.lang.String getCodeAffiliation() {
        return _codeAffiliation;
    } // -- java.lang.String getCodeAffiliation()

    /**
     * Returns the value of field 'dateDebutAffiliation'. The field 'dateDebutAffiliation' has the following
     * description: Date de début de l'affiliation Canton ZH et SG: ErfSeitD
     * 
     * @return String
     * @return the value of field 'dateDebutAffiliation'.
     */
    public java.lang.String getDateDebutAffiliation() {
        return _dateDebutAffiliation;
    } // -- java.lang.String getDateDebutAffiliation()

    /**
     * Returns the value of field 'dateFinAffiliation'. The field 'dateFinAffiliation' has the following description:
     * Date de fin de l'affiliation Canton ZH et SG: AbgDat
     * 
     * @return String
     * @return the value of field 'dateFinAffiliation'.
     */
    public java.lang.String getDateFinAffiliation() {
        return _dateFinAffiliation;
    } // -- java.lang.String getDateFinAffiliation()

    /**
     * Returns the value of field 'dateNaiEpouse'. The field 'dateNaiEpouse' has the following description: Date de
     * naissance de l'épouse Canton ZH et SG: GeburtsDatEhefrau
     * 
     * @return String
     * @return the value of field 'dateNaiEpouse'.
     */
    public java.lang.String getDateNaiEpouse() {
        return _dateNaiEpouse;
    } // -- java.lang.String getDateNaiEpouse()

    /**
     * Returns the value of field 'dateSaisieAffiliation'. The field 'dateSaisieAffiliation' has the following
     * description: Date à laquelle l'affiliation a été saisie Canton ZH et SG: ErfAlsD
     * 
     * @return String
     * @return the value of field 'dateSaisieAffiliation'.
     */
    public java.lang.String getDateSaisieAffiliation() {
        return _dateSaisieAffiliation;
    } // -- java.lang.String getDateSaisieAffiliation()

    /**
     * Returns the value of field 'debPrdConcernee'. The field 'debPrdConcernee' has the following description: Mois
     * (mm) de début de la période pour laquelle la demande est faite. Par défaut 01 = janvier
     * 
     * @return int
     * @return the value of field 'debPrdConcernee'.
     */
    public int getDebPrdConcernee() {
        return _debPrdConcernee;
    } // -- int getDebPrdConcernee()

    /**
     * Returns the value of field 'finPrdConcernee'. The field 'finPrdConcernee' has the following description: Mois
     * (mm) de fin de la période pour laquelle la demande est faite. Par défaut 12 = décembre
     * 
     * @return int
     * @return the value of field 'finPrdConcernee'.
     */
    public int getFinPrdConcernee() {
        return _finPrdConcernee;
    } // -- int getFinPrdConcernee()

    /**
     * Returns the value of field 'lieuCtb'. The field 'lieuCtb' has the following description: Domicile du contribuable
     * Canton VD AdrGrp:adrLigne4 concaténée Canton ZH et SG: OrtSteupfl
     * 
     * @return String
     * @return the value of field 'lieuCtb'.
     */
    public java.lang.String getLieuCtb() {
        return _lieuCtb;
    } // -- java.lang.String getLieuCtb()

    /**
     * Returns the value of field 'nomCtb'. The field 'nomCtb' has the following description: Nom du contribuable Canton
     * VD AdrGrp:adrLigne1 Canton ZH et SG: NameSteupfl
     * 
     * @return String
     * @return the value of field 'nomCtb'.
     */
    public java.lang.String getNomCtb() {
        return _nomCtb;
    } // -- java.lang.String getNomCtb()

    /**
     * Returns the value of field 'nomEntreprise'. The field 'nomEntreprise' has the following description: Nom de
     * l'entreprise Canton ZH et SG: NameBetr
     * 
     * @return String
     * @return the value of field 'nomEntreprise'.
     */
    public java.lang.String getNomEntreprise() {
        return _nomEntreprise;
    } // -- java.lang.String getNomEntreprise()

    /**
     * Returns the value of field 'npaCtb'. The field 'npaCtb' has the following description: Code postal du lieu de
     * domicile du contribuable Canton VD AdrGrp:adrLigne4 Canton ZH et SG: PLZSteupfl
     * 
     * @return String
     * @return the value of field 'npaCtb'.
     */
    public java.lang.String getNpaCtb() {
        return _npaCtb;
    } // -- java.lang.String getNpaCtb()

    /**
     * Returns the value of field 'npaEntreprise'. The field 'npaEntreprise' has the following description: Code postal
     * du lieu de l'entreprise Canton ZH et SG: PLZOrtBetr
     * 
     * @return String
     * @return the value of field 'npaEntreprise'.
     */
    public java.lang.String getNpaEntreprise() {
        return _npaEntreprise;
    } // -- java.lang.String getNpaEntreprise()

    /**
     * Returns the value of field 'numAffilie'. The field 'numAffilie' has the following description: Numéro d'affilié
     * de l'assuré Canton ZH et SG: AbrNr
     * 
     * @return String
     * @return the value of field 'numAffilie'.
     */
    public java.lang.String getNumAffilie() {
        return _numAffilie;
    } // -- java.lang.String getNumAffilie()

    /**
     * Returns the value of field 'numAvs'. The field 'numAvs' has the following description: Numéro Avs de l'assuré
     * Canton ZH et SG: VersNr
     * 
     * @return String
     * @return the value of field 'numAvs'.
     */
    public java.lang.String getNumAvs() {
        return _numAvs;
    } // -- java.lang.String getNumAvs()

    /**
     * Returns the value of field 'numAvsAlt'. The field 'numAvsAlt' has the following description: Numéro Avs
     * alternatif, par exemple num Avs de l'entreprise Canton ZH et SG: VersNrBi
     * 
     * @return String
     * @return the value of field 'numAvsAlt'.
     */
    public java.lang.String getNumAvsAlt() {
        return _numAvsAlt;
    } // -- java.lang.String getNumAvsAlt()

    /**
     * Returns the value of field 'numCaiAvsCtb'. The field 'numCaiAvsCtb' has the following description: Numéro de la
     * caisse Avs du contribuable Canton ZH et SG: AbsKasse
     * 
     * @return String
     * @return the value of field 'numCaiAvsCtb'.
     */
    public java.lang.String getNumCaiAvsCtb() {
        return _numCaiAvsCtb;
    } // -- java.lang.String getNumCaiAvsCtb()

    /**
     * Returns the value of field 'numCtb'. The field 'numCtb' has the following description: Numéro de contribuable de
     * l'assuré Canton ZH et SG: inutile
     * 
     * @return String
     * @return the value of field 'numCtb'.
     */
    public java.lang.String getNumCtb() {
        return _numCtb;
    } // -- java.lang.String getNumCtb()

    /**
     * Method hasDebPrdConcernee
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDebPrdConcernee() {
        return _has_debPrdConcernee;
    } // -- boolean hasDebPrdConcernee()

    /**
     * Method hasFinPrdConcernee
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasFinPrdConcernee() {
        return _has_finPrdConcernee;
    } // -- boolean hasFinPrdConcernee()

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
     * Sets the value of field 'adresseRueCtb'. The field 'adresseRueCtb' has the following description: Zone adresse du
     * contribuable, en principe la rue Canton VD AdrGrp:adrLigne3 Canton ZH et SG: StrasseSteupfl
     * 
     * @param adresseRueCtb
     *            the value of field 'adresseRueCtb'.
     */
    public void setAdresseRueCtb(java.lang.String adresseRueCtb) {
        _adresseRueCtb = adresseRueCtb;
    } // -- void setAdresseRueCtb(java.lang.String)

    /**
     * Sets the value of field 'adresseSupCtb'. The field 'adresseSupCtb' has the following description: Ligne d'Adresse
     * supplépmentaire Canton VD AdrGrp:adrLigne2 Canton ZH et SG: AdrZusSteupfl
     * 
     * @param adresseSupCtb
     *            the value of field 'adresseSupCtb'.
     */
    public void setAdresseSupCtb(java.lang.String adresseSupCtb) {
        _adresseSupCtb = adresseSupCtb;
    } // -- void setAdresseSupCtb(java.lang.String)

    /**
     * Sets the value of field 'anneeConcernee'. The field 'anneeConcernee' has the following description: Année
     * concernée Canton ZH et SG: BemJahr Canton Vd: inutile
     * 
     * @param anneeConcernee
     *            the value of field 'anneeConcernee'.
     */
    public void setAnneeConcernee(java.lang.String anneeConcernee) {
        _anneeConcernee = anneeConcernee;
    } // -- void setAnneeConcernee(java.lang.String)

    /**
     * Sets the value of field 'canton'. The field 'canton' has the following description: Canton du contribuable canton
     * ZH et SG: EmpfSteu, obligatoire canton VD: inutile
     * 
     * @param canton
     *            the value of field 'canton'.
     */
    public void setCanton(java.lang.String canton) {
        _canton = canton;
    } // -- void setCanton(java.lang.String)

    /**
     * Sets the value of field 'catAssure'. The field 'catAssure' has the following description: Catégorie de
     * l'assuré. L'une des 4 valeurs : "PCI" = Indépendants ou exploitants "PSA" = Sans activité lucrative "SAL" =
     * Salariés affiliés indépendants "AGR" = Collaborateurs agricoles Canton ZH et SG: BeitrArt
     * 
     * @param catAssure
     *            the value of field 'catAssure'.
     */
    public void setCatAssure(globaz.phenix.mapping.demande.castor.CatAssure catAssure) {
        _catAssure = catAssure;
    } // -- void setCatAssure(globaz.phenix.mapping.demande.castor.CatAssure)

    /**
     * Sets the value of field 'codeAffiliation'. The field 'codeAffiliation' has the following description: Code de
     * l'affiliation Canton ZH et SG: ErfAlsC
     * 
     * @param codeAffiliation
     *            the value of field 'codeAffiliation'.
     */
    public void setCodeAffiliation(java.lang.String codeAffiliation) {
        _codeAffiliation = codeAffiliation;
    } // -- void setCodeAffiliation(java.lang.String)

    /**
     * Sets the value of field 'dateDebutAffiliation'. The field 'dateDebutAffiliation' has the following description:
     * Date de début de l'affiliation Canton ZH et SG: ErfSeitD
     * 
     * @param dateDebutAffiliation
     *            the value of field 'dateDebutAffiliation'.
     */
    public void setDateDebutAffiliation(java.lang.String dateDebutAffiliation) {
        _dateDebutAffiliation = dateDebutAffiliation;
    } // -- void setDateDebutAffiliation(java.lang.String)

    /**
     * Sets the value of field 'dateFinAffiliation'. The field 'dateFinAffiliation' has the following description: Date
     * de fin de l'affiliation Canton ZH et SG: AbgDat
     * 
     * @param dateFinAffiliation
     *            the value of field 'dateFinAffiliation'.
     */
    public void setDateFinAffiliation(java.lang.String dateFinAffiliation) {
        _dateFinAffiliation = dateFinAffiliation;
    } // -- void setDateFinAffiliation(java.lang.String)

    /**
     * Sets the value of field 'dateNaiEpouse'. The field 'dateNaiEpouse' has the following description: Date de
     * naissance de l'épouse Canton ZH et SG: GeburtsDatEhefrau
     * 
     * @param dateNaiEpouse
     *            the value of field 'dateNaiEpouse'.
     */
    public void setDateNaiEpouse(java.lang.String dateNaiEpouse) {
        _dateNaiEpouse = dateNaiEpouse;
    } // -- void setDateNaiEpouse(java.lang.String)

    /**
     * Sets the value of field 'dateSaisieAffiliation'. The field 'dateSaisieAffiliation' has the following description:
     * Date à laquelle l'affiliation a été saisie Canton ZH et SG: ErfAlsD
     * 
     * @param dateSaisieAffiliation
     *            the value of field 'dateSaisieAffiliation'.
     */
    public void setDateSaisieAffiliation(java.lang.String dateSaisieAffiliation) {
        _dateSaisieAffiliation = dateSaisieAffiliation;
    } // -- void setDateSaisieAffiliation(java.lang.String)

    /**
     * Sets the value of field 'debPrdConcernee'. The field 'debPrdConcernee' has the following description: Mois (mm)
     * de début de la période pour laquelle la demande est faite. Par défaut 01 = janvier
     * 
     * @param debPrdConcernee
     *            the value of field 'debPrdConcernee'.
     */
    public void setDebPrdConcernee(int debPrdConcernee) {
        _debPrdConcernee = debPrdConcernee;
        _has_debPrdConcernee = true;
    } // -- void setDebPrdConcernee(int)

    /**
     * Sets the value of field 'finPrdConcernee'. The field 'finPrdConcernee' has the following description: Mois (mm)
     * de fin de la période pour laquelle la demande est faite. Par défaut 12 = décembre
     * 
     * @param finPrdConcernee
     *            the value of field 'finPrdConcernee'.
     */
    public void setFinPrdConcernee(int finPrdConcernee) {
        _finPrdConcernee = finPrdConcernee;
        _has_finPrdConcernee = true;
    } // -- void setFinPrdConcernee(int)

    /**
     * Sets the value of field 'lieuCtb'. The field 'lieuCtb' has the following description: Domicile du contribuable
     * Canton VD AdrGrp:adrLigne4 concaténée Canton ZH et SG: OrtSteupfl
     * 
     * @param lieuCtb
     *            the value of field 'lieuCtb'.
     */
    public void setLieuCtb(java.lang.String lieuCtb) {
        _lieuCtb = lieuCtb;
    } // -- void setLieuCtb(java.lang.String)

    /**
     * Sets the value of field 'nomCtb'. The field 'nomCtb' has the following description: Nom du contribuable Canton VD
     * AdrGrp:adrLigne1 Canton ZH et SG: NameSteupfl
     * 
     * @param nomCtb
     *            the value of field 'nomCtb'.
     */
    public void setNomCtb(java.lang.String nomCtb) {
        _nomCtb = nomCtb;
    } // -- void setNomCtb(java.lang.String)

    /**
     * Sets the value of field 'nomEntreprise'. The field 'nomEntreprise' has the following description: Nom de
     * l'entreprise Canton ZH et SG: NameBetr
     * 
     * @param nomEntreprise
     *            the value of field 'nomEntreprise'.
     */
    public void setNomEntreprise(java.lang.String nomEntreprise) {
        _nomEntreprise = nomEntreprise;
    } // -- void setNomEntreprise(java.lang.String)

    /**
     * Sets the value of field 'npaCtb'. The field 'npaCtb' has the following description: Code postal du lieu de
     * domicile du contribuable Canton VD AdrGrp:adrLigne4 Canton ZH et SG: PLZSteupfl
     * 
     * @param npaCtb
     *            the value of field 'npaCtb'.
     */
    public void setNpaCtb(java.lang.String npaCtb) {
        _npaCtb = npaCtb;
    } // -- void setNpaCtb(java.lang.String)

    /**
     * Sets the value of field 'npaEntreprise'. The field 'npaEntreprise' has the following description: Code postal du
     * lieu de l'entreprise Canton ZH et SG: PLZOrtBetr
     * 
     * @param npaEntreprise
     *            the value of field 'npaEntreprise'.
     */
    public void setNpaEntreprise(java.lang.String npaEntreprise) {
        _npaEntreprise = npaEntreprise;
    } // -- void setNpaEntreprise(java.lang.String)

    /**
     * Sets the value of field 'numAffilie'. The field 'numAffilie' has the following description: Numéro d'affilié de
     * l'assuré Canton ZH et SG: AbrNr
     * 
     * @param numAffilie
     *            the value of field 'numAffilie'.
     */
    public void setNumAffilie(java.lang.String numAffilie) {
        _numAffilie = numAffilie;
    } // -- void setNumAffilie(java.lang.String)

    /**
     * Sets the value of field 'numAvs'. The field 'numAvs' has the following description: Numéro Avs de l'assuré
     * Canton ZH et SG: VersNr
     * 
     * @param numAvs
     *            the value of field 'numAvs'.
     */
    public void setNumAvs(java.lang.String numAvs) {
        _numAvs = numAvs;
    } // -- void setNumAvs(java.lang.String)

    /**
     * Sets the value of field 'numAvsAlt'. The field 'numAvsAlt' has the following description: Numéro Avs alternatif,
     * par exemple num Avs de l'entreprise Canton ZH et SG: VersNrBi
     * 
     * @param numAvsAlt
     *            the value of field 'numAvsAlt'.
     */
    public void setNumAvsAlt(java.lang.String numAvsAlt) {
        _numAvsAlt = numAvsAlt;
    } // -- void setNumAvsAlt(java.lang.String)

    /**
     * Sets the value of field 'numCaiAvsCtb'. The field 'numCaiAvsCtb' has the following description: Numéro de la
     * caisse Avs du contribuable Canton ZH et SG: AbsKasse
     * 
     * @param numCaiAvsCtb
     *            the value of field 'numCaiAvsCtb'.
     */
    public void setNumCaiAvsCtb(java.lang.String numCaiAvsCtb) {
        _numCaiAvsCtb = numCaiAvsCtb;
    } // -- void setNumCaiAvsCtb(java.lang.String)

    /**
     * Sets the value of field 'numCtb'. The field 'numCtb' has the following description: Numéro de contribuable de
     * l'assuré Canton ZH et SG: inutile
     * 
     * @param numCtb
     *            the value of field 'numCtb'.
     */
    public void setNumCtb(java.lang.String numCtb) {
        _numCtb = numCtb;
    } // -- void setNumCtb(java.lang.String)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
