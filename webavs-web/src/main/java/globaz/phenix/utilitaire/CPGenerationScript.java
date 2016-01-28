package globaz.phenix.utilitaire;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;

/**
 * @author btc
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPGenerationScript {

    public static void main(String[] args) {
        CPGenerationScript process = null;
        try {
            process = new CPGenerationScript();
            process.setCotisationMinimale(478); // cotisation minimum annuelle de la tabelle
            process.setRevenuCiMinimal(4667); // revenu ci minimun de la tabelle
            process.setRevenuCiFinal(233333); // Correspond au dernier revenu ci de la tabelle
            process.setRevenuDebut(300000); // Premier revenu de la tabelle
            process.setRevenuInt(1750000); // Revenu ou le supplément change
            process.setRevenuFin(8400000); // Revenu max de la tabelle
            process.setCotiBase(Float.parseFloat("512.5")); // Première cotisation de la tabelle
            process.setSupplement(Float.parseFloat("102.5")); // 1er supplément décrit dans la tabelle
            process.setSupplement1(Float.parseFloat("153.75")); // 2ème supplément décrit dans la tabelle
            process.setAnnee(2016); // Année de changement
            //
            process.setTauxRentier(Float.parseFloat("5.196"));
            process.setRevenuMaxrentier(9300);
            process.generationScript();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    private int annee = 0;
    private float cotiBase = 0;

    private int cotisationMinimale = 0;

    private float revenuCiFinal = 0;
    private int revenuCiMinimal = 0;

    private int revenuDebut = 0;

    private int revenuFin = 0;
    private int revenuInt = 0;
    private float supplement = 0;
    private float supplement1 = 0;

    private float tauxRentier = 0;

    private float revenuMaxrentier = 0;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPGenerationScript() {
        super();
    }

    protected void generationScript() {
        generateScriptNonActif();
        generateScriptRentier();
    }

    public void generateScriptNonActif() {
        String spy = "globazf" + JACalendar.today().toStrAMJ() + "120000";
        float cotiMensuelle = JANumberFormatter.round(cotisationMinimale, 0.1, 1, JANumberFormatter.NEAR);

        System.out.println("DELETE from SCHEMA.cptnacp where jaanne=" + annee + ";");

        // Script pour cération coti minimale
        String insert = "INSERT INTO SCHEMA.CPTNACP VALUES((SELECT COALESCE(MAX(JAITNA)+1,0) FROM SCHEMA.CPTNACP),";
        System.out.println(insert + annee + ",0," + cotiMensuelle + "," + cotisationMinimale + "," + revenuCiMinimal
                + ",'" + spy + "');");
        float revenuCi = JANumberFormatter.round(cotiBase * 10, 1000, 0, JANumberFormatter.INF);
        while (revenuDebut < revenuFin) {
            cotiMensuelle = JANumberFormatter.round(cotiBase / 12, 0.1, 1, JANumberFormatter.NEAR);
            System.out.println(insert + annee + "," + revenuDebut + "," + cotiMensuelle + "," + cotiBase + ","
                    + revenuCi + ",'" + spy + "');");
            if (revenuDebut < revenuInt) {
                cotiBase = cotiBase + supplement;
                revenuCi = revenuCi + 1000;
            } else {
                cotiBase = cotiBase + supplement1;
                revenuCi = revenuCi + 1500;
            }
            revenuDebut = revenuDebut + 50000;
        }
        // Dernier enreg qui n'a pas de logique point de vu ci...
        BigDecimal varDouble = new BigDecimal(cotisationMinimale * 50);
        varDouble = varDouble.divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_EVEN);
        System.out.println(insert + annee + "," + revenuDebut + ","
                + JANumberFormatter.round(varDouble, 0.1, 1, JANumberFormatter.NEAR) + "," + cotisationMinimale * 50
                + "," + revenuCiFinal + ",'" + spy + "');");
        System.out
                .println("UPDATE SCHEMA.FWINCP SET PINCVA = (SELECT COALESCE(MAX(JAITNA),0) FROM SCHEMA.CPTNACP) WHERE PINCID = 'CPTNACP'");
    }

    public void generateScriptRentier() {
        String spy = "globazf" + JACalendar.today().toStrAMJ() + "120000";
        float cotiMensuelle = 0;
        float revenuCi = 100;

        System.out.println("DELETE from SCHEMA.cptrenp where jcanne=" + annee + ";");

        // Script pour cération coti minimale
        String insert = "INSERT INTO SCHEMA.CPTRENP VALUES((SELECT COALESCE(MAX(JCITRE)+1,0) FROM SCHEMA.CPTRENP),";

        while (revenuCi <= getRevenuMaxrentier()) {
            cotiMensuelle = JANumberFormatter
                    .round((revenuCi * getTauxRentier()) / 1200, 0.1, 1, JANumberFormatter.INF);
            System.out.println(insert + annee + "," + revenuCi + "," + new FWCurrency(cotiMensuelle * 12) + ",'" + spy
                    + "');");
            revenuCi = revenuCi + 100;
        }
        System.out
                .println("UPDATE SCHEMA.FWINCP SET PINCVA = (SELECT COALESCE(MAX(JCITRE),0) FROM SCHEMA.CPTRENP) WHERE PINCID = 'CPTRENP'");
    }

    public int getAnnee() {
        return annee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public float getCotiBase() {
        return cotiBase;
    }

    public int getCotisationMinimale() {
        return cotisationMinimale;
    }

    public float getRevenuCiFinal() {
        return revenuCiFinal;
    }

    public int getRevenuCiMinimal() {
        return revenuCiMinimal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    public int getRevenuDebut() {
        return revenuDebut;
    }

    public int getRevenuFin() {
        return revenuFin;
    }

    public int getRevenuInt() {
        return revenuInt;
    }

    public float getSupplement() {
        return supplement;
    }

    public float getSupplement1() {
        return supplement1;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public void setCotiBase(float cotiBase) {
        this.cotiBase = cotiBase;
    }

    public void setCotisationMinimale(int cotisationMinimale) {
        this.cotisationMinimale = cotisationMinimale;
    }

    public void setRevenuCiFinal(float revenuCiFinal) {
        this.revenuCiFinal = revenuCiFinal;
    }

    public void setRevenuCiMinimal(int revenuCiMinimal) {
        this.revenuCiMinimal = revenuCiMinimal;
    }

    public void setRevenuDebut(int revenuDebut) {
        this.revenuDebut = revenuDebut;
    }

    public void setRevenuFin(int revenuFin) {
        this.revenuFin = revenuFin;
    }

    public void setRevenuInt(int revenuInt) {
        this.revenuInt = revenuInt;
    }

    public void setSupplement(float supplement) {
        this.supplement = supplement;
    }

    public void setSupplement1(float supplement1) {
        this.supplement1 = supplement1;
    }

    public float getTauxRentier() {
        return tauxRentier;
    }

    public void setTauxRentier(float tauxRentier) {
        this.tauxRentier = tauxRentier;
    }

    public float getRevenuMaxrentier() {
        return revenuMaxrentier;
    }

    public void setRevenuMaxrentier(float revenuMaxrentier) {
        this.revenuMaxrentier = revenuMaxrentier;
    }
}
