package globaz.draco.print.itext;

/**
 * Représente une ligne d'un NSS pas valide dans la lettre de réclamation des NSS pas valide.
 * 
 * @author sco
 * @since 23 aout. 2011
 */
public class DSLettreReclamationNssBean {

    public String COL1 = null;
    public String COL2 = null;
    public String COL3 = null;
    public String COL4 = null;

    public DSLettreReclamationNssBean() {
        COL1 = "";
        COL3 = "";
        COL4 = "";
        COL2 = "";
    }

    public DSLettreReclamationNssBean(String nom, String periode, String revenu, String nss) {
        COL1 = nom;
        COL3 = periode;
        COL4 = revenu;
        COL2 = nss;
    }

    public String getCOL1() {
        return COL1;
    }

    public String getCOL2() {
        return COL2;
    }

    public String getCOL3() {
        return COL3;
    }

    public String getCOL4() {
        return COL4;
    }

    public void setCOL1(String COL1) {
        this.COL1 = COL1;
    }

    public void setCOL2(String COL2) {
        this.COL2 = COL2;
    }

    public void setCOL3(String COL3) {
        this.COL3 = COL3;
    }

    public void setCOL4(String COL4) {
        this.COL4 = COL4;
    }
}
