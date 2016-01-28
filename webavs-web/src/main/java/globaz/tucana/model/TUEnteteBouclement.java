package globaz.tucana.model;

import globaz.itucana.model.ITUEntete;

/**
 * Classe représentatn une entete de bouclement
 * 
 * @author fgo
 * 
 */
public class TUEnteteBouclement implements ITUEntete {
    private String anneeComptable = null;
    private String moisComptable = null;
    private String noPassage = null;

    /**
     * Constructeur par défaut de la classe
     */
    public TUEnteteBouclement() {
        super();
    }

    /**
     * Construit la classe TUEneteBouclement avec tous les paramètres nécessaires
     */
    public TUEnteteBouclement(String _anneeComptable, String _moisComptable, String _noPassage) {
        super();
        anneeComptable = _anneeComptable;
        moisComptable = _moisComptable;
        noPassage = _noPassage;
    }

    /**
     * Construit la classe en passant un entête bouclement en parametre
     */
    public TUEnteteBouclement(TUEnteteBouclement entete) {
        super();
        anneeComptable = entete.getAnneeComptable();
        moisComptable = entete.getMoisComptable();
        noPassage = entete.getNoPassage();
    }

    /**
     * Retroune l'attribut anneeComptable.
     * 
     * @return Retroune l'attribut anneeComptable.
     */
    @Override
    public String getAnneeComptable() {
        return anneeComptable;
    }

    /**
     * Retroune l'attribut moisComptable.
     * 
     * @return Retroune l'attribut moisComptable.
     */
    @Override
    public String getMoisComptable() {
        return moisComptable;
    }

    /**
     * Retroune l'attribut noPassage.
     * 
     * @return Retroune l'attribut noPassage.
     */
    @Override
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * la propriété anneeComptable à modifier
     * 
     * @param anneeComptable
     *            la propriété anneeComptable à modifier
     */
    public void setAnneeComptable(String anneeComptable) {
        this.anneeComptable = anneeComptable;
    }

    /**
     * la propriété moisComptable à modifier
     * 
     * @param moisComptable
     *            la propriété moisComptable à modifier
     */
    public void setMoisComptable(String moisComptable) {
        this.moisComptable = moisComptable;
    }

    /**
     * la propriété noPassage à modifier
     * 
     * @param noPassage
     *            la propriété noPassage à modifier
     */
    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer ligne = new StringBuffer();
        ligne.append("************************************************** \n");
        ligne.append("           Entête bouclement \n");
        ligne.append("************************************************** \n");
        ligne.append("annee comptable : ").append(anneeComptable).append("\n");
        ligne.append("mois comptable : ").append(moisComptable).append("\n");
        ligne.append("no passage : ").append(noPassage).append("\n");
        return ligne.toString();
    }
}
