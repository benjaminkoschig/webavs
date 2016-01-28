package globaz.hercule.mappingXmlml.line;

import globaz.hercule.mappingXmlml.ICEListeColumns;
import globaz.hercule.utils.HerculeContainer;

/**
 * Classe représentant une ligne de données dans la liste des employeurs avec leur masse salariale
 * 
 * @author Sullivann Corneille
 * @since 3 avr. 2014
 */
public class CEXmlmlLineEmployeurMasseSalariale extends CEXmlmlLine {

    public String numAffilie = "";
    public String nom = "";
    public String groupe = "";
    public String periodeAffiliation = "";
    public String caisseAvs = "";
    public String categorie = "";
    public String masseAvs1 = "";
    public String masseAvs2 = "";
    public String masseAvs3 = "";
    public String masseAvs4 = "";
    public String masseAvs5 = "";
    public String ci1 = "";
    public String ci2 = "";
    public String ci3 = "";
    public String ci4 = "";
    public String ci5 = "";
    public String masseAf = "";
    public String codeSuva = "";
    public String libelle = "";

    @Override
    public void remplirContainerWithLineData(HerculeContainer container) {

        // Infos affiliés
        container.put(ICEListeColumns.NUM_AFFILIE, numAffilie);
        container.put(ICEListeColumns.NOM, nom);
        container.put(ICEListeColumns.PERIODE_AFFILIATION, periodeAffiliation);

        // Infos masses
        container.put(ICEListeColumns.NOM_GROUPE, groupe);
        container.put(ICEListeColumns.MASSE_1, masseAvs1);
        container.put(ICEListeColumns.MASSE_2, masseAvs2);
        container.put(ICEListeColumns.MASSE_3, masseAvs3);
        container.put(ICEListeColumns.MASSE_4, masseAvs4);
        container.put(ICEListeColumns.MASSE_5, masseAvs5);
        container.put(ICEListeColumns.MASSE_AF, masseAf);
        container.put(ICEListeColumns.CAISSE_AVS, caisseAvs);
        container.put(ICEListeColumns.CATEGORIE, categorie);

        // Infos CI
        container.put(ICEListeColumns.CI_1, ci1);
        container.put(ICEListeColumns.CI_2, ci2);
        container.put(ICEListeColumns.CI_3, ci3);
        container.put(ICEListeColumns.CI_4, ci4);
        container.put(ICEListeColumns.CI_5, ci5);

        // Infos suva
        container.put(ICEListeColumns.CODE_SUVA, codeSuva);
        container.put(ICEListeColumns.LIBELLE_SUVA, libelle);

        super.remplirContainerWithLineData(container);
    }

    /**
     * Getter de numAffilie
     * 
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Setter de numAffilie
     * 
     * @param numAffilie the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * Getter de nom
     * 
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter de nom
     * 
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter de groupe
     * 
     * @return the groupe
     */
    public String getGroupe() {
        return groupe;
    }

    /**
     * Setter de groupe
     * 
     * @param groupe the groupe to set
     */
    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    /**
     * Getter de periodeAffiliation
     * 
     * @return the periodeAffiliation
     */
    public String getPeriodeAffiliation() {
        return periodeAffiliation;
    }

    /**
     * Setter de periodeAffiliation
     * 
     * @param periodeAffiliation the periodeAffiliation to set
     */
    public void setPeriodeAffiliation(String periodeAffiliation) {
        this.periodeAffiliation = periodeAffiliation;
    }

    /**
     * Getter de caisseAvs
     * 
     * @return the caisseAvs
     */
    public String getCaisseAvs() {
        return caisseAvs;
    }

    /**
     * Setter de caisseAvs
     * 
     * @param caisseAvs the caisseAvs to set
     */
    public void setCaisseAvs(String caisseAvs) {
        this.caisseAvs = caisseAvs;
    }

    /**
     * Getter de categorie
     * 
     * @return the categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Setter de categorie
     * 
     * @param categorie the categorie to set
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Getter de masseAvs1
     * 
     * @return the masseAvs1
     */
    public String getMasseAvs1() {
        return masseAvs1;
    }

    /**
     * Setter de masseAvs1
     * 
     * @param masseAvs1 the masseAvs1 to set
     */
    public void setMasseAvs1(String masseAvs1) {
        this.masseAvs1 = masseAvs1;
    }

    /**
     * Getter de masseAvs2
     * 
     * @return the masseAvs2
     */
    public String getMasseAvs2() {
        return masseAvs2;
    }

    /**
     * Setter de masseAvs2
     * 
     * @param masseAvs2 the masseAvs2 to set
     */
    public void setMasseAvs2(String masseAvs2) {
        this.masseAvs2 = masseAvs2;
    }

    /**
     * Getter de masseAvs3
     * 
     * @return the masseAvs3
     */
    public String getMasseAvs3() {
        return masseAvs3;
    }

    /**
     * Setter de masseAvs3
     * 
     * @param masseAvs3 the masseAvs3 to set
     */
    public void setMasseAvs3(String masseAvs3) {
        this.masseAvs3 = masseAvs3;
    }

    /**
     * Getter de masseAvs4
     * 
     * @return the masseAvs4
     */
    public String getMasseAvs4() {
        return masseAvs4;
    }

    /**
     * Setter de masseAvs4
     * 
     * @param masseAvs4 the masseAvs4 to set
     */
    public void setMasseAvs4(String masseAvs4) {
        this.masseAvs4 = masseAvs4;
    }

    /**
     * Getter de masseAvs5
     * 
     * @return the masseAvs5
     */
    public String getMasseAvs5() {
        return masseAvs5;
    }

    /**
     * Setter de masseAvs5
     * 
     * @param masseAvs5 the masseAvs5 to set
     */
    public void setMasseAvs5(String masseAvs5) {
        this.masseAvs5 = masseAvs5;
    }

    /**
     * Getter de ci1
     * 
     * @return the ci1
     */
    public String getCi1() {
        return ci1;
    }

    /**
     * Setter de ci1
     * 
     * @param ci1 the ci1 to set
     */
    public void setCi1(String ci1) {
        this.ci1 = ci1;
    }

    /**
     * Getter de ci2
     * 
     * @return the ci2
     */
    public String getCi2() {
        return ci2;
    }

    /**
     * Setter de ci2
     * 
     * @param ci2 the ci2 to set
     */
    public void setCi2(String ci2) {
        this.ci2 = ci2;
    }

    /**
     * Getter de ci3
     * 
     * @return the ci3
     */
    public String getCi3() {
        return ci3;
    }

    /**
     * Setter de ci3
     * 
     * @param ci3 the ci3 to set
     */
    public void setCi3(String ci3) {
        this.ci3 = ci3;
    }

    /**
     * Getter de ci4
     * 
     * @return the ci4
     */
    public String getCi4() {
        return ci4;
    }

    /**
     * Setter de ci4
     * 
     * @param ci4 the ci4 to set
     */
    public void setCi4(String ci4) {
        this.ci4 = ci4;
    }

    /**
     * Getter de ci5
     * 
     * @return the ci5
     */
    public String getCi5() {
        return ci5;
    }

    /**
     * Setter de ci5
     * 
     * @param ci5 the ci5 to set
     */
    public void setCi5(String ci5) {
        this.ci5 = ci5;
    }

    /**
     * Getter de masseAf
     * 
     * @return the masseAf
     */
    public String getMasseAf() {
        return masseAf;
    }

    /**
     * Setter de masseAf
     * 
     * @param masseAf the masseAf to set
     */
    public void setMasseAf(String masseAf) {
        this.masseAf = masseAf;
    }

    /**
     * Getter de codeSuva
     * 
     * @return the codeSuva
     */
    public String getCodeSuva() {
        return codeSuva;
    }

    /**
     * Setter de codeSuva
     * 
     * @param codeSuva the codeSuva to set
     */
    public void setCodeSuva(String codeSuva) {
        this.codeSuva = codeSuva;
    }

    /**
     * Getter de libelle
     * 
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Setter de libelle
     * 
     * @param libelle the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}
