package globaz.pavo.db.inscriptions.declaration;

import java.util.ArrayList;

/**
 * Structure contenant les informations nécessaires au traitement des déclarations
 * 
 * @author oca
 * 
 * 
 */
public class CIDeclarationRecord {

    private String agence = "";
    private String annee = "";
    private String categoriePers = "";
    private ArrayList<?> ciAdd = new ArrayList<Object>();
    private String codeCanton = "";
    private String debutAffiliation = null; // doit être a null pour la getsion
    // pour stockage des erreurs/infos qui arrivent pendant le process pour ce
    // record
    private ArrayList<?> errors = new ArrayList<Object>();
    // des erreur
    private String finAffiliation = null; // doit être a null pour la getsion
    private String genreEcriture = "";
    private String idAffiliation = "";
    private ArrayList<?> info = new ArrayList<Object>();
    private int jourDebut = 0;
    private int jourFin = 0;
    private int moisDebut = 0;
    private int moisFin = 0;
    private String montantAc = "";
    private String montantAc2 = "";
    private String montantAf = "";
    // des erreur
    private String montantEcr = "";
    private boolean montantPositif = true;
    private String nomAffilie = "";
    private String nomPrenom = "";

    private String numeroAffilie = "";
    private String numeroAvs = "";
    private String reserve = "";

    // Info pour swissdec sur l'affilié transmis dans le fichier
    private String affilieFichierNom = "";
    private String affilieFichierStreet = "";
    private String affilieFichierNpa = "";
    private String affilieFichierCity = "";

    private String affilieContactPersonName = "";
    private String affilieContactPersonEmail = "";
    private String affilieContactPersonPhone = "";
    private String categorieAffilie = "";

    private String returnCode = "";

    /**
     * Returns the agence.
     * 
     * @return String
     */
    public String getAgence() {
        return agence;
    }

    /**
     * Returns the annee.
     * 
     * @return String
     */
    public String getAnnee() {
        return annee;
    }

    public String getCategoriePers() {
        return categoriePers;
    }

    public String getAffilieFichierNom() {
        return affilieFichierNom;
    }

    public String getAffilieFichierStreet() {
        return affilieFichierStreet;
    }

    public String getAffilieFichierNpa() {
        return affilieFichierNpa;
    }

    public String getAffilieContactPersonEmail() {
        return affilieContactPersonEmail;
    }

    public String getAffilieContactPersonPhone() {
        return affilieContactPersonPhone;
    }

    public String getAffilieFichierCity() {
        return affilieFichierCity;
    }

    public String getAffilieContactPersonName() {
        return affilieContactPersonName;
    }

    public String getCategorieAffilie() {
        return categorieAffilie;
    }

    /**
     * Returns the ciAdd.
     * 
     * @return ArrayList
     */
    public ArrayList<?> getCiAdd() {
        return ciAdd;
    }

    /**
     * @return
     */
    public String getCodeCanton() {
        return codeCanton;
    }

    /**
     * Returns the debutAffiliation.
     * 
     * @return String
     */
    public String getDebutAffiliation() {
        return debutAffiliation;
    }

    /**
     * Returns the errors.
     * 
     * @return ArrayList
     */
    public ArrayList<?> getErrors() {
        return errors;
    }

    /**
     * Returns the finAffiliation.
     * 
     * @return String
     */
    public String getFinAffiliation() {
        return finAffiliation;
    }

    /**
     * Returns the genreEcriture.
     * 
     * @return String
     */
    public String getGenreEcriture() {
        return genreEcriture;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Returns the info.
     * 
     * @return ArrayList
     */
    public ArrayList<?> getInfo() {
        return info;
    }

    /**
     * @return
     */
    public int getJourDebut() {
        return jourDebut;
    }

    /**
     * @return
     */
    public int getJourFin() {
        return jourFin;
    }

    /**
     * Returns the moisDebut.
     * 
     * @return int
     */
    public int getMoisDebut() {
        return moisDebut;
    }

    /**
     * Returns the moisFin.
     * 
     * @return int
     */
    public int getMoisFin() {
        return moisFin;
    }

    public String getMontantAc() {
        return montantAc.trim();
    }

    public String getMontantAc2() {
        return montantAc2.trim();
    }

    public String getMontantAf() {
        return montantAf.trim();
    }

    /**
     * Returns the montantEcr.
     * 
     * @return String
     */
    public String getMontantEcr() {
        return montantEcr.trim();
    }

    /**
     * Returns the nomAffilie.
     * 
     * @return String
     */
    public String getNomAffilie() {
        return nomAffilie;
    }

    /**
     * Returns the nomPrenom.
     * 
     * @return String
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * Returns the numeroAffilie.
     * 
     * @return String
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * Returns the numeroAvs.
     * 
     * @return String
     */
    public String getNumeroAvs() {
        return numeroAvs;
    }

    /**
     * Returns the reserve.
     * 
     * @return String
     */
    public String getReserve() {
        return reserve;
    }

    public String getReturnCode() {
        return returnCode;
    }

    /**
     * Returns the montantPositif.
     * 
     * @return boolean
     */
    public boolean isMontantPositif() {
        return montantPositif;
    }

    /**
     * Sets the agence.
     * 
     * @param agence
     *            The agence to set
     */
    public void setAgence(String agence) {
        this.agence = agence;
    }

    /**
     * Sets the annee.
     * 
     * @param annee
     *            The annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCategoriePers(String categoriePers) {
        this.categoriePers = categoriePers;
    }

    /**
     * Sets the ciAdd.
     * 
     * @param ciAdd
     *            The ciAdd to set
     */
    public void setCiAdd(ArrayList<?> ciAdd) {
        this.ciAdd = ciAdd;
    }

    /**
     * @param string
     */
    public void setCodeCanton(String string) {
        codeCanton = string;
    }

    /**
     * Sets the debutAffiliation.
     * 
     * @param debutAffiliation
     *            The debutAffiliation to set
     */
    public void setDebutAffiliation(String debutAffiliation) {
        this.debutAffiliation = debutAffiliation;
    }

    /**
     * Sets the errors.
     * 
     * @param errors
     *            The errors to set
     */
    public void setErrors(ArrayList<?> errors) {
        this.errors = errors;
    }

    /**
     * Sets the finAffiliation.
     * 
     * @param finAffiliation
     *            The finAffiliation to set
     */
    public void setFinAffiliation(String finAffiliation) {
        this.finAffiliation = finAffiliation;
    }

    /**
     * Sets the genreEcriture.
     * 
     * @param genreEcriture
     *            The genreEcriture to set
     */
    public void setGenreEcriture(String genreEcriture) {
        this.genreEcriture = genreEcriture;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    /**
     * Sets the info.
     * 
     * @param info
     *            The info to set
     */
    public void setInfo(ArrayList<?> info) {
        this.info = info;
    }

    /**
     * @param i
     */
    public void setJourDebut(int i) {
        jourDebut = i;
    }

    /**
     * @param i
     */
    public void setJourFin(int i) {
        jourFin = i;
    }

    /**
     * Sets the moisDebut.
     * 
     * @param moisDebut
     *            The moisDebut to set
     */
    public void setMoisDebut(int moisDebut) {
        this.moisDebut = moisDebut;
    }

    /**
     * Sets the moisFin.
     * 
     * @param moisFin
     *            The moisFin to set
     */
    public void setMoisFin(int moisFin) {
        this.moisFin = moisFin;
    }

    public void setMontantAc(String montantAc) {
        this.montantAc = montantAc;
    }

    public void setMontantAc2(String montantAc2) {
        this.montantAc2 = montantAc2;
    }

    public void setMontantAf(String montantAf) {
        this.montantAf = montantAf;
    }

    /**
     * Sets the montantEcr.
     * 
     * @param montantEcr
     *            The montantEcr to set
     */
    public void setMontantEcr(String montantEcr) {
        this.montantEcr = montantEcr;
    }

    /**
     * Sets the montantPositif.
     * 
     * @param montantPositif
     *            The montantPositif to set
     */
    public void setMontantPositif(boolean montantPositif) {
        this.montantPositif = montantPositif;
    }

    /**
     * Sets the nomAffilie.
     * 
     * @param nomAffilie
     *            The nomAffilie to set
     */
    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    /**
     * Sets the nomPrenom.
     * 
     * @param nomPrenom
     *            The nomPrenom to set
     */
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * Sets the numeroAffilie.
     * 
     * @param numeroAffilie
     *            The numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * Sets the numeroAvs.
     * 
     * @param numeroAvs
     *            The numeroAvs to set
     */
    public void setNumeroAvs(String numeroAvs) {
        this.numeroAvs = numeroAvs;
    }

    /**
     * Sets the reserve.
     * 
     * @param reserve
     *            The reserve to set
     */
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setAffilieFichierNom(String affilieFichierNom) {
        this.affilieFichierNom = affilieFichierNom;
    }

    public void setAffilieFichierStreet(String affilieFichierStreet) {
        this.affilieFichierStreet = affilieFichierStreet;
    }

    public void setAffilieContactPersonName(String affilieContactPersonName) {
        this.affilieContactPersonName = affilieContactPersonName;
    }

    public void setAffilieContactPersonEmail(String affilieContactPersonEmail) {
        this.affilieContactPersonEmail = affilieContactPersonEmail;
    }

    public void setAffilieContactPersonPhone(String affilieContactPersonPhone) {
        this.affilieContactPersonPhone = affilieContactPersonPhone;
    }

    public void setAffilieFichierNpa(String affilieFichierNpa) {
        this.affilieFichierNpa = affilieFichierNpa;
    }

    public void setAffilieFichierCity(String affilieFichierCity) {
        this.affilieFichierCity = affilieFichierCity;
    }

    public void setCategorieAffilie(String categorieAffilie) {
        this.categorieAffilie = categorieAffilie;
    }

}
