package globaz.tucana.model;

import globaz.itucana.model.ITUEntete;

/**
 * Classe repr�sentatn une entete de bouclement
 * 
 * @author fgo
 * 
 */
public class TUEnteteBouclement implements ITUEntete {
    private String anneeComptable = null;
    private String moisComptable = null;
    private String noPassage = null;

    /**
     * Constructeur par d�faut de la classe
     */
    public TUEnteteBouclement() {
        super();
    }

    /**
     * Construit la classe TUEneteBouclement avec tous les param�tres n�cessaires
     */
    public TUEnteteBouclement(String _anneeComptable, String _moisComptable, String _noPassage) {
        super();
        anneeComptable = _anneeComptable;
        moisComptable = _moisComptable;
        noPassage = _noPassage;
    }

    /**
     * Construit la classe en passant un ent�te bouclement en parametre
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
     * la propri�t� anneeComptable � modifier
     * 
     * @param anneeComptable
     *            la propri�t� anneeComptable � modifier
     */
    public void setAnneeComptable(String anneeComptable) {
        this.anneeComptable = anneeComptable;
    }

    /**
     * la propri�t� moisComptable � modifier
     * 
     * @param moisComptable
     *            la propri�t� moisComptable � modifier
     */
    public void setMoisComptable(String moisComptable) {
        this.moisComptable = moisComptable;
    }

    /**
     * la propri�t� noPassage � modifier
     * 
     * @param noPassage
     *            la propri�t� noPassage � modifier
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
        ligne.append("           Ent�te bouclement \n");
        ligne.append("************************************************** \n");
        ligne.append("annee comptable : ").append(anneeComptable).append("\n");
        ligne.append("mois comptable : ").append(moisComptable).append("\n");
        ligne.append("no passage : ").append(noPassage).append("\n");
        return ligne.toString();
    }
}
