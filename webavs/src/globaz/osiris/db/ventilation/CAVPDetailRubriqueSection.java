package globaz.osiris.db.ventilation;

public class CAVPDetailRubriqueSection {
    private int col = 0;
    private int colEmployeur = 0;
    private int colSalarie = 0;
    private String idRubrique = "";
    private boolean montantSimple = true;
    private String ordre = "";

    public CAVPDetailRubriqueSection() {
    }

    public int getCol() {
        return col;
    }

    public int getColEmployeur() {
        return colEmployeur;
    }

    public int getColSalarie() {
        return colSalarie;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getOrdre() {
        return ordre;
    }

    public boolean isMontantSimple() {
        return montantSimple;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setColEmployeur(int colEmployeur) {
        this.colEmployeur = colEmployeur;
    }

    public void setColSalarie(int colSalarie) {
        this.colSalarie = colSalarie;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setMontantSimple(boolean montantSimple) {
        this.montantSimple = montantSimple;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

}
