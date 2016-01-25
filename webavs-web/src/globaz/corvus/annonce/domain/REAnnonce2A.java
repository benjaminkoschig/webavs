package globaz.corvus.annonce.domain;

import globaz.globall.util.JADate;

/**
 * REAAL2A
 * Représente le niveau 2A des annonces de rentes
 * 
 * @author lga
 * 
 */
public abstract class REAnnonce2A extends REAnnonce1A {

    private Boolean ageDebutInvalidite;
    private Integer anneeCotClasseAge;
    private Integer anneeNiveau;
    private Integer casSpecial1;
    private Integer casSpecial2;
    private Integer casSpecial3;
    private Integer casSpecial4;
    private Integer casSpecial5;
    private Integer codeAtteinteFonctionnelle;
    private Integer codeInfirmite;
    private JADate dateRevocationAjournement;
    private Integer degreInvalidite;
    private Integer dureeAjournementValeurEntiere;
    private Integer dureeAjournementValeurDecimal;
    private Integer dureeCoEchelleRenteAv73_nombreAnnee;
    private Integer dureeCoEchelleRenteAv73_nombreMois;
    private Integer dureeCoEchelleRenteDes73_nombreAnnee;
    private Integer dureeCoEchelleRenteDes73_nombreMois;
    private Integer dureeCotManquante48_72;
    private Integer dureeCotManquante73_78;
    private Integer dureeCotPourDetRAM_nombreAnnee;
    private Integer dureeCotPourDetRAM_nombreMois;
    private Integer echelleRente;
    private Integer genreDroitAPI;
    private Integer nombreAnneeBTE_valeurEntiere;
    private Integer nombreAnneeBTE_valeurDecimal;
    private Integer officeAICompetent;
    private Integer ramDeterminant;
    private Integer reduction;
    private Integer supplementAjournement;
    private JADate survenanceEvenAssure;

    /**
     * <code>true</code> si invalide avant 25 ans
     * 
     * @return the ageDebutInvalidite
     */
    public final Boolean getAgeDebutInvalidite() {
        return ageDebutInvalidite;
    }

    /**
     * Si invalide avant 25 ans
     * 
     * @param ageDebutInvalidite the ageDebutInvalidite to set
     */
    public final void setAgeDebutInvalidite(Boolean ageDebutInvalidite) {
        this.ageDebutInvalidite = ageDebutInvalidite;
    }

    /**
     * @return the anneeCotClasseAge
     */
    public final Integer getAnneeCotClasseAge() {
        return anneeCotClasseAge;
    }

    /**
     * @param anneeCotClasseAge the anneeCotClasseAge to set
     */
    public final void setAnneeCotClasseAge(Integer anneeCotClasseAge) {
        this.anneeCotClasseAge = anneeCotClasseAge;
    }

    /**
     * @return the anneeNiveau
     */
    public final Integer getAnneeNiveau() {
        return anneeNiveau;
    }

    /**
     * @param anneeNiveau the anneeNiveau to set
     */
    public final void setAnneeNiveau(Integer anneeNiveau) {
        this.anneeNiveau = anneeNiveau;
    }

    /**
     * @return the casSpecial1
     */
    public final Integer getCasSpecial1() {
        return casSpecial1;
    }

    /**
     * @param casSpecial1 the casSpecial1 to set
     */
    public final void setCasSpecial1(Integer casSpecial1) {
        this.casSpecial1 = casSpecial1;
    }

    /**
     * @return the casSpecial2
     */
    public final Integer getCasSpecial2() {
        return casSpecial2;
    }

    /**
     * @param casSpecial2 the casSpecial2 to set
     */
    public final void setCasSpecial2(Integer casSpecial2) {
        this.casSpecial2 = casSpecial2;
    }

    /**
     * @return the casSpecial3
     */
    public final Integer getCasSpecial3() {
        return casSpecial3;
    }

    /**
     * @param casSpecial3 the casSpecial3 to set
     */
    public final void setCasSpecial3(Integer casSpecial3) {
        this.casSpecial3 = casSpecial3;
    }

    /**
     * @return the casSpecial4
     */
    public final Integer getCasSpecial4() {
        return casSpecial4;
    }

    /**
     * @param casSpecial4 the casSpecial4 to set
     */
    public final void setCasSpecial4(Integer casSpecial4) {
        this.casSpecial4 = casSpecial4;
    }

    /**
     * @return the casSpecial5
     */
    public final Integer getCasSpecial5() {
        return casSpecial5;
    }

    /**
     * @param casSpecial5 the casSpecial5 to set
     */
    public final void setCasSpecial5(Integer casSpecial5) {
        this.casSpecial5 = casSpecial5;
    }

    /**
     * Retourne le code infirmité simple (ex : 738)
     * Simple car en base de données, le code infirmité est agrégé avec le code atteinte fonctionnelle
     * 
     * @return le code infirmité simple (ex : 738)
     */
    public final Integer getCodeInfirmite() {
        return codeInfirmite;
    }

    /**
     * Définit le code infirmité simple ( ex : 738)
     * Simple car en base de données, le code infirmité est agrégé avec le code atteinte fonctionnelle
     * 
     * @param codeInfirmite le code infirmité simple ( ex : 738)
     */
    public final void setCodeInfirmite(Integer codeInfirmite) {
        this.codeInfirmite = codeInfirmite;
    }

    /**
     * @return the dateRevocationAjournement
     */
    public final JADate getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    /**
     * @param dateRevocationAjournement the dateRevocationAjournement to set
     */
    public final void setDateRevocationAjournement(JADate dateRevocationAjournement) {
        this.dateRevocationAjournement = dateRevocationAjournement;
    }

    /**
     * @return the degreInvalidite
     */
    public final Integer getDegreInvalidite() {
        return degreInvalidite;
    }

    /**
     * @param degreInvalidite the degreInvalidite to set
     */
    public final void setDegreInvalidite(Integer degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    /**
     * @return la valeur décimal de la durée d'ajournememnt
     */
    public final Integer getDureeAjournementValeurEntiere() {
        return dureeAjournementValeurEntiere;
    }

    /**
     * @return la valeur entière de la durée d'ajournememnt
     */
    public final Integer getDureeAjournementValeurDecimal() {
        return dureeAjournementValeurDecimal;
    }

    /**
     * @param dureeAjournement the dureeAjournement to set
     */
    public final void setDureeAjournement(Integer dureeAjournementValeurEntiere, Integer dureeAjournementValeurDecimal) {
        this.dureeAjournementValeurEntiere = dureeAjournementValeurEntiere;
        this.dureeAjournementValeurDecimal = dureeAjournementValeurDecimal;
    }

    /**
     * @return le nombre d'année de la durée de cotisation échelle rente avant 1973
     */
    public final Integer getDureeCoEchelleRenteAv73_nombreAnnee() {
        return dureeCoEchelleRenteAv73_nombreAnnee;
    }

    /**
     * @return le nombre de mois de la durée de cotisation échelle rente avant 1973
     */
    public final Integer getDureeCoEchelleRenteAv73_nombreMois() {
        return dureeCoEchelleRenteAv73_nombreMois;
    }

    /**
     * @param dureeCoEchelleRenteAv73 the dureeCoEchelleRenteAv73 to set
     */
    public final void setDureeCoEchelleRenteAv73(Integer nombreAnnee, Integer nombreMois) {
        dureeCoEchelleRenteAv73_nombreAnnee = nombreAnnee;
        dureeCoEchelleRenteAv73_nombreMois = nombreMois;
    }

    /**
     * @return la durée en année de la cotisation à l'échelle des rente dès 19773
     */
    public final Integer getDureeCoEchelleRenteDes73_nombreAnnee() {
        return dureeCoEchelleRenteDes73_nombreAnnee;
    }

    /**
     * @return la durée en année de la cotisation à l'échelle des rente dès 19773
     */
    public final Integer getDureeCoEchelleRenteDes73_nombreMois() {
        return dureeCoEchelleRenteDes73_nombreMois;
    }

    /**
     * @param dureeCoEchelleRenteDes73 the dureeCoEchelleRenteDes73 to set
     */
    public final void setDureeCoEchelleRenteDes73(Integer nombreAnnee, Integer nombreMois) {
        dureeCoEchelleRenteDes73_nombreAnnee = nombreAnnee;
        dureeCoEchelleRenteDes73_nombreMois = nombreMois;
    }

    /**
     * @return the dureeCotManquante48_72
     */
    public final Integer getDureeCotManquante48_72() {
        return dureeCotManquante48_72;
    }

    /**
     * @param dureeCotManquante48_72 the dureeCotManquante48_72 to set
     */
    public final void setDureeCotManquante48_72(Integer dureeCotManquante48_72) {
        this.dureeCotManquante48_72 = dureeCotManquante48_72;
    }

    /**
     * @return the dureeCotManquante73_78
     */
    public final Integer getDureeCotManquante73_78() {
        return dureeCotManquante73_78;
    }

    /**
     * @param dureeCotManquante73_78 the dureeCotManquante73_78 to set
     */
    public final void setDureeCotManquante73_78(Integer dureeCotManquante73_78) {
        this.dureeCotManquante73_78 = dureeCotManquante73_78;
    }

    /**
     * @return le nombre d'année de la dureeCotPourDetRAM, valeur complétée par le nombre de mois
     */
    public final Integer getDureeCotPourDetRAM_nombreAnnee() {
        return dureeCotPourDetRAM_nombreAnnee;
    }

    /**
     * @return le nombre de mois de la dureeCotPourDetRAM, valeur complétée par le nombre d'année
     */
    public final Integer getDureeCotPourDetRAM_nombreMois() {
        return dureeCotPourDetRAM_nombreMois;
    }

    /**
     * @param dureeCotPourDetRAM the dureeCotPourDetRAM to set
     */
    public final void setDureeCotPourDetRAM(Integer nombreAnnee, Integer nombreMois) {
        dureeCotPourDetRAM_nombreAnnee = nombreAnnee;
        dureeCotPourDetRAM_nombreMois = nombreMois;
    }

    /**
     * @return the echelleRente
     */
    public final Integer getEchelleRente() {
        return echelleRente;
    }

    /**
     * @param echelleRente the echelleRente to set
     */
    public final void setEchelleRente(Integer echelleRente) {
        this.echelleRente = echelleRente;
    }

    /**
     * @return the genreDroitAPI
     */
    public final Integer getGenreDroitAPI() {
        return genreDroitAPI;
    }

    /**
     * @param genreDroitAPI the genreDroitAPI to set
     */
    public final void setGenreDroitAPI(Integer genreDroitAPI) {
        this.genreDroitAPI = genreDroitAPI;
    }

    /**
     * @return la valeur entière du nombre d'année de bonification pour tâche éducative
     */
    public final Integer getNombreAnneeBTE_valeurEntiere() {
        return nombreAnneeBTE_valeurEntiere;
    }

    /**
     * @return la valeur décimal du nombre d'année de bonification pour tâche éducative
     */
    public final Integer getNombreAnneeBTE_valeurDecimal() {
        return nombreAnneeBTE_valeurDecimal;
    }

    /**
     * Set le nombre d'année de bonification pour tâche éducative
     * 
     * @param valeurEntiere le nombre d'année de bonification pour tâche éducative
     * @param fractionAnnee la valeur de la fraction d'année (ex : 1066 => [10, 66])
     */
    public final void setNombreAnneeBTE(Integer valeurEntiere, Integer fractionAnnee) {
        nombreAnneeBTE_valeurEntiere = valeurEntiere;
        nombreAnneeBTE_valeurDecimal = fractionAnnee;
    }

    /**
     * @return the officeAICompetent
     */
    public final Integer getOfficeAICompetent() {
        return officeAICompetent;
    }

    /**
     * @param officeAICompetent the officeAICompetent to set
     */
    public final void setOfficeAICompetent(Integer officeAICompetent) {
        this.officeAICompetent = officeAICompetent;
    }

    /**
     * @return the ramDeterminant
     */
    public final Integer getRamDeterminant() {
        return ramDeterminant;
    }

    /**
     * @param ramDeterminant the ramDeterminant to set
     */
    public final void setRamDeterminant(Integer ramDeterminant) {
        this.ramDeterminant = ramDeterminant;
    }

    /**
     * @return the reduction
     */
    public final Integer getReduction() {
        return reduction;
    }

    /**
     * @param reduction the reduction to set
     */
    public final void setReduction(Integer reduction) {
        this.reduction = reduction;
    }

    /**
     * @return the supplementAjournement
     */
    public final Integer getSupplementAjournement() {
        return supplementAjournement;
    }

    /**
     * @param supplementAjournement the supplementAjournement to set
     */
    public final void setSupplementAjournement(Integer supplementAjournement) {
        this.supplementAjournement = supplementAjournement;
    }

    /**
     * @return the survenanceEvenAssure
     */
    public final JADate getSurvenanceEvenAssure() {
        return survenanceEvenAssure;
    }

    /**
     * @param survenanceEvenAssure the survenanceEvenAssure to set
     */
    public final void setSurvenanceEvenAssure(JADate survenanceEvenAssure) {
        this.survenanceEvenAssure = survenanceEvenAssure;
    }

    /**
     * Retourne le code atteinte fonctionnelle uniquement (ex : 2 pour un cas ??)
     * En base de données, le code infirmité est agrégé avec le code atteinte fonctionnelle
     * 
     * @return le code atteinte fonctionnelle uniquement (ex : 2)
     */
    public final Integer getCodeAtteinteFonctionnelle() {
        return codeAtteinteFonctionnelle;
    }

    /**
     * Renseigne le code d'atteinte fonctionnelle (ex : 2 pour un cas ??)
     * En base de données, le code infirmité est agrégé avec le code atteinte fonctionnelle
     * 
     * @param codeAtteinteFonctionnelle le code d'atteinte fonctionnelle (ex : 2)
     */
    public final void setCodeAtteinteFonctionnelle(Integer codeAtteinteFonctionnelle) {
        this.codeAtteinteFonctionnelle = codeAtteinteFonctionnelle;
    }

}
