/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul.rev2005;

import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class APReferenceDataAPG implements IAPReferenceDataPrestation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWCurrency Amax = null;
    private FWCurrency BKmax = null;
    private FWCurrency BZ = null;
    private String calculateurClassName = null;
    private JADate dateDebut = null;
    private JADate dateFin = null;
    private FWCurrency GE = null;
    private FWCurrency KA0min = null;
    private FWCurrency KA1min = null;
    private FWCurrency KA2min = null;
    private FWCurrency KB0min = null;
    private FWCurrency KB1min = null;
    private FWCurrency KB2min = null;
    private FWCurrency KC0min = null;

    private FWCurrency KC1min = null;
    private FWCurrency KC2min = null;
    private FWCurrency KZ = null;

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
     * getter pour l'attribut amax
     * 
     * @return la valeur courante de l'attribut amax
     */
    public FWCurrency getAmax() {
        return Amax;
    }

    /**
     * getter pour l'attribut BKmax
     * 
     * @return la valeur courante de l'attribut BKmax
     */
    public FWCurrency getBKmax() {
        return BKmax;
    }

    /**
     * getter pour l'attribut BZ
     * 
     * @return la valeur courante de l'attribut BZ
     */
    public FWCurrency getBZ() {
        return BZ;
    }

    /**
     * (non-Javadoc)
     * 
     * @return la valeur courante de l'attribut calculateur class name
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#getCalculateurClassName()
     */
    @Override
    public String getCalculateurClassName() {
        return calculateurClassName;
    }

    /**
     * (non-Javadoc)
     * 
     * @return la valeur courante de l'attribut date debut
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#getDateDebut()
     */
    @Override
    public JADate getDateDebut() {
        return dateDebut;
    }

    /**
     * (non-Javadoc)
     * 
     * @return la valeur courante de l'attribut date fin
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#getDateFin()
     */
    @Override
    public JADate getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut GE
     * 
     * @return la valeur courante de l'attribut GE
     */
    public FWCurrency getGE() {
        return GE;
    }

    /**
     * getter pour l'attribut KA0min
     * 
     * @return la valeur courante de l'attribut KA0min
     */
    public FWCurrency getKA0min() {
        return KA0min;
    }

    /**
     * getter pour l'attribut KA1min
     * 
     * @return la valeur courante de l'attribut KA1min
     */
    public FWCurrency getKA1min() {
        return KA1min;
    }

    /**
     * getter pour l'attribut KA2min
     * 
     * @return la valeur courante de l'attribut KA2min
     */
    public FWCurrency getKA2min() {
        return KA2min;
    }

    /**
     * getter pour l'attribut KB0min
     * 
     * @return la valeur courante de l'attribut KB0min
     */
    public FWCurrency getKB0min() {
        return KB0min;
    }

    /**
     * getter pour l'attribut KB1min
     * 
     * @return la valeur courante de l'attribut KB1min
     */
    public FWCurrency getKB1min() {
        return KB1min;
    }

    /**
     * getter pour l'attribut KB2min
     * 
     * @return la valeur courante de l'attribut KB2min
     */
    public FWCurrency getKB2min() {
        return KB2min;
    }

    /**
     * getter pour l'attribut KC0min
     * 
     * @return la valeur courante de l'attribut KC0min
     */
    public FWCurrency getKC0min() {
        return KC0min;
    }

    /**
     * getter pour l'attribut KC1min
     * 
     * @return la valeur courante de l'attribut KC1min
     */
    public FWCurrency getKC1min() {
        return KC1min;
    }

    /**
     * getter pour l'attribut KC2min
     * 
     * @return la valeur courante de l'attribut KC2min
     */
    public FWCurrency getKC2min() {
        return KC2min;
    }

    /**
     * getter pour l'attribut KZ
     * 
     * @return la valeur courante de l'attribut KZ
     */
    public FWCurrency getKZ() {
        return KZ;
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
     * @return la valeur courante de l'attribut no revision
     * 
     * @see globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation#getNoRevision()
     */
    @Override
    public String getNoRevision() {
        return noRevision;
    }

    /**
     * setter pour l'attribut amax
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setAmax(FWCurrency currency) {
        Amax = currency;
    }

    /**
     * setter pour l'attribut BKmax
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setBKmax(FWCurrency currency) {
        BKmax = currency;
    }

    /**
     * setter pour l'attribut BZ
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setBZ(FWCurrency currency) {
        BZ = currency;
    }

    /**
     * (non-Javadoc)
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#setCalculateurClassName(java.lang.String)
     */
    @Override
    public void setCalculateurClassName(String s) {
        calculateurClassName = s;
    }

    /**
     * (non-Javadoc)
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#setDateDebut(globaz.globall.util.JADate)
     */
    @Override
    public void setDateDebut(JADate date) {
        dateDebut = date;
    }

    /**
     * (non-Javadoc)
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.apg.module.calcul.IAPReferenceDataPrestation#setDateFin(globaz.globall.util.JADate)
     */
    @Override
    public void setDateFin(JADate date) {
        dateFin = date;
    }

    /**
     * setter pour l'attribut GE
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setGE(FWCurrency currency) {
        GE = currency;
    }

    /**
     * setter pour l'attribut KA0min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKA0min(FWCurrency currency) {
        KA0min = currency;
    }

    /**
     * setter pour l'attribut KA1min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKA1min(FWCurrency currency) {
        KA1min = currency;
    }

    /**
     * setter pour l'attribut KA2min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKA2min(FWCurrency currency) {
        KA2min = currency;
    }

    /**
     * setter pour l'attribut KB0min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKB0min(FWCurrency currency) {
        KB0min = currency;
    }

    /**
     * setter pour l'attribut KB1min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKB1min(FWCurrency currency) {
        KB1min = currency;
    }

    /**
     * setter pour l'attribut KB2min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKB2min(FWCurrency currency) {
        KB2min = currency;
    }

    /**
     * setter pour l'attribut KC0min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKC0min(FWCurrency currency) {
        KC0min = currency;
    }

    /**
     * setter pour l'attribut KC1min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKC1min(FWCurrency currency) {
        KC1min = currency;
    }

    /**
     * setter pour l'attribut KC2min
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKC2min(FWCurrency currency) {
        KC2min = currency;
    }

    /**
     * setter pour l'attribut KZ
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setKZ(FWCurrency currency) {
        KZ = currency;
    }

    /**
     * (non-Javadoc)
     * 
     * @param s
     *            une nouvelle valeur pour cet attribut
     * 
     * @see globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation#setNoRevision()
     */
    @Override
    public void setNoRevision(String s) {
        noRevision = s;
    }
}
