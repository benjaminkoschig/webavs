/**
 * 
 */
package globaz.amal.process.annonce.cosama;

/**
 * @author dhi
 * 
 */
public class AMCosamaRecordEnTete extends AMCosamaRecord {

    private String annee = "";
    private String canton = "";

    private String dateCreation = "";

    private String moisDebut = "";

    private String moisFin = "";

    private String noCaisseMaladie = "";

    private String noOrdre = "";

    private String noPartition = "";

    private String typeTransmission = "";

    /**
     * Default constructor (empêche la création)
     */
    private AMCosamaRecordEnTete() {

    }

    /**
     * @param _typeEnregistrement
     */
    protected AMCosamaRecordEnTete(String _typeEnregistrement) {
        setTypeEnregistrement(_typeEnregistrement);
    }

    @Override
    public int compareTo(AMCosamaRecord o) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.AMCosamaRecord#formatFields()
     */
    @Override
    public void formatFields() {
        setTypeEnregistrement(fillField(getTypeEnregistrement(), '0', 1, true));
        noCaisseMaladie = fillField(noCaisseMaladie, '0', 4, true);
        canton = fillField(canton, ' ', 8, false);
        annee = fillField(annee, '0', 4, true);
        moisDebut = fillField(moisDebut, '0', 2, true);
        moisFin = fillField(moisFin, '0', 2, true);
        dateCreation = fillField(dateCreation, '0', 8, false);
        noOrdre = fillField(noOrdre, '0', 4, true);
        typeTransmission = fillField(typeTransmission, '0', 2, true);
        noPartition = fillField(noPartition, '0', 2, true);
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return the canton
     */
    public String getCanton() {
        return canton;
    }

    /**
     * @return the dateCreation
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * @return the moisDebut
     */
    public String getMoisDebut() {
        return moisDebut;
    }

    /**
     * @return the moisFin
     */
    public String getMoisFin() {
        return moisFin;
    }

    /**
     * @return the noCaisseMaladie
     */
    public String getNoCaisseMaladie() {
        return noCaisseMaladie;
    }

    /**
     * @return the noOrdre
     */
    public String getNoOrdre() {
        return noOrdre;
    }

    /**
     * @return the noPartition
     */
    public String getNoPartition() {
        return noPartition;
    }

    /**
     * @return the typeTransmission
     */
    public String getTypeTransmission() {
        return typeTransmission;
    }

    @Override
    public void parseLigne(String currentLigne) {
        // Numéro OFAS de la caisse-maladie
        if (currentLigne.length() > 4) {
            noCaisseMaladie = currentLigne.substring(1, 5);
        }
        // Canton
        if (currentLigne.length() > 12) {
            canton = currentLigne.substring(5, 13);
        }
        // Année
        if (currentLigne.length() > 16) {
            annee = currentLigne.substring(13, 17);
        }
        // Début
        if (currentLigne.length() > 18) {
            moisDebut = currentLigne.substring(17, 19);
        }
        // Fin
        if (currentLigne.length() > 20) {
            moisFin = currentLigne.substring(19, 21);
        }
        // Date de création
        if (currentLigne.length() > 28) {
            dateCreation = currentLigne.substring(21, 29);
        }
        // Numéro de l'ordre
        if (currentLigne.length() > 32) {
            noOrdre = currentLigne.substring(29, 33);
        }
        // Type de transmission
        if (currentLigne.length() > 34) {
            typeTransmission = currentLigne.substring(33, 35);
            // Numéro de partition
            noPartition = currentLigne.substring(35);
        }
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param canton
     *            the canton to set
     */
    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * @param dateCreation
     *            the dateCreation to set
     */
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * @param moisDebut
     *            the moisDebut to set
     */
    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    /**
     * @param moisFin
     *            the moisFin to set
     */
    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    /**
     * @param noCaisseMaladie
     *            the noCaisseMaladie to set
     */
    public void setNoCaisseMaladie(String noCaisseMaladie) {
        this.noCaisseMaladie = noCaisseMaladie;
    }

    /**
     * @param noOrdre
     *            the noOrdre to set
     */
    public void setNoOrdre(String noOrdre) {
        this.noOrdre = noOrdre;
    }

    /**
     * @param noPartition
     *            the noPartition to set
     */
    public void setNoPartition(String noPartition) {
        this.noPartition = noPartition;
    }

    /**
     * @param typeTransmission
     *            the typeTransmission to set
     */
    public void setTypeTransmission(String typeTransmission) {
        this.typeTransmission = typeTransmission;
    }

    @Override
    public String writeLigne() {
        return this.writeLigne(false);
    }

    @Override
    public String writeLigne(boolean bWithSeparator) {
        String fieldSeparator = "";
        String currencySeparator = "";
        if (bWithSeparator) {
            fieldSeparator = IAMCosamaRecord._FieldSeparator;
            currencySeparator = IAMCosamaRecord._CurrencySeparator;
        }
        String finalLine = "";

        finalLine += getTypeEnregistrement();
        finalLine += fieldSeparator;

        finalLine += getNoCaisseMaladie();
        finalLine += fieldSeparator;

        finalLine += getCanton();
        finalLine += fieldSeparator;

        finalLine += getAnnee();
        finalLine += fieldSeparator;

        finalLine += getMoisDebut();
        finalLine += fieldSeparator;

        finalLine += getMoisFin();
        finalLine += fieldSeparator;

        finalLine += getDateCreation();
        finalLine += fieldSeparator;

        finalLine += getNoOrdre();
        finalLine += fieldSeparator;

        finalLine += getTypeTransmission();
        finalLine += fieldSeparator;

        finalLine += getNoPartition();
        finalLine += fieldSeparator;

        return finalLine;
    }

}
