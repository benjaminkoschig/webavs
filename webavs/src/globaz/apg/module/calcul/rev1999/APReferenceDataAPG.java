/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul.rev1999;

import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;

/**
 * Description
 * 
 * @author scr
 */
public class APReferenceDataAPG implements IAPReferenceDataPrestation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWCurrency Amax = null;
    private FWCurrency Amin = null;
    private FWCurrency BKmax = null;
    private FWCurrency Bmin = null;
    private FWCurrency BZ = null;
    private String calculateurClassName = null;
    private JADate dateDebut = null;
    private JADate dateFin = null;
    private FWCurrency GE = null;
    private FWCurrency KAmin = null;

    private FWCurrency KBmin = null;
    private FWCurrency KZ1 = null;
    private FWCurrency KZ2 = null;

    private String noRevision = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APReferenceDataAPG.
     */
    public APReferenceDataAPG() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public FWCurrency getAmax() {
        return Amax;
    }

    /**
     * @return
     */
    public FWCurrency getAmin() {
        return Amin;
    }

    /**
     * @return
     */
    public FWCurrency getBKmax() {
        return BKmax;
    }

    /**
     * @return
     */
    public FWCurrency getBmin() {
        return Bmin;
    }

    /**
     * @return
     */
    public FWCurrency getBZ() {
        return BZ;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#getCalculateurClassName()
     * 
     * @return la valeur courante de l'attribut calculateur class name
     */
    @Override
    public String getCalculateurClassName() {
        return calculateurClassName;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#getDateDebut()
     * 
     * @return la valeur courante de l'attribut date debut
     */
    @Override
    public JADate getDateDebut() {
        return dateDebut;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#getDateFin()
     * 
     * @return la valeur courante de l'attribut date fin
     */
    @Override
    public JADate getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public FWCurrency getGE() {
        return GE;
    }

    /**
     * @return
     */
    public FWCurrency getKAmin() {
        return KAmin;
    }

    /**
     * @return
     */
    public FWCurrency getKBmin() {
        return KBmin;
    }

    /**
     * @return
     */
    public FWCurrency getKZ1() {
        return KZ1;
    }

    /**
     * @return
     */
    public FWCurrency getKZ2() {
        return KZ2;
    }

    @Override
    public FWCurrency getMontantJournalierMax() {
        return getGE();
    }

    /**
     * getter pour l'attribut montant max frais garde
     * 
     * @return la valeur courante de l'attribut montant max frais garde
     */
    @Override
    public FWCurrency getMontantMaxFraisGarde() {
        return getBKmax();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation#getNoRevision()
     * 
     * @return la valeur courante de l'attribut no revision
     */
    @Override
    public String getNoRevision() {
        return noRevision;
    }

    /**
     * @param currency
     */
    public void setAmax(FWCurrency currency) {
        Amax = currency;
    }

    /**
     * @param currency
     */
    public void setAmin(FWCurrency currency) {
        Amin = currency;
    }

    /**
     * @param currency
     */
    public void setBKmax(FWCurrency currency) {
        BKmax = currency;
    }

    /**
     * @param currency
     */
    public void setBmin(FWCurrency currency) {
        Bmin = currency;
    }

    /**
     * @param currency
     */
    public void setBZ(FWCurrency currency) {
        BZ = currency;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#setCalculateurClassName(java.lang.String)
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCalculateurClassName(String s) {
        calculateurClassName = s;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#setDateDebut(globaz.globall.util.JADate)
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDateDebut(JADate date) {
        dateDebut = date;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#setDateFin(globaz.globall.util.JADate)
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDateFin(JADate date) {
        dateFin = date;
    }

    /**
     * @param currency
     */
    public void setGE(FWCurrency currency) {
        GE = currency;
    }

    /**
     * @param currency
     */
    public void setKAmin(FWCurrency currency) {
        KAmin = currency;
    }

    /**
     * @param currency
     */
    public void setKBmin(FWCurrency currency) {
        KBmin = currency;
    }

    /**
     * @param currency
     */
    public void setKZ1(FWCurrency currency) {
        KZ1 = currency;
    }

    /**
     * @param currency
     */
    public void setKZ2(FWCurrency currency) {
        KZ2 = currency;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation#setNoRevision()
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setNoRevision(String s) {
        noRevision = s;
    }
}
