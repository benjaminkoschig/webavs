package globaz.tucana.statistiques;

import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;
import java.util.TreeMap;

/**
 * Classe representant une entrée de catégorie
 * 
 * @author fgo date de création : 27 juin 06
 * @version : version 1.0
 * 
 */
public class TUEntry implements Comparable {
    private TUColonne colonne = null;
    private BigDecimal total = null;

    /**
     * * Constructeur
     * 
     * @param _colonne
     * @param _total
     */
    public TUEntry(TUColonne _colonne, BigDecimal _total) {
        colonne = _colonne;
        total = _total;
    }

    public void addToTotal(BigDecimal montant) {
        total = total.add(montant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    /**
     * Renvoie un TUColonne
     * 
     * @return
     */
    public TUColonne getColonne() {
        return colonne;
    }

    /**
     * Retourne le total de l'entry
     * 
     * @return
     */
    public BigDecimal getTotal() {
        return total;
    }

    public void toCsv(TreeMap tree) {
        tree.put(getColonne().getAbreviation(), getTotal().toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n Entry");
        str.append("\n Total : ").append(total);
        str.append(colonne.toString());
        return str.toString();
    }

    /**
     * Génère la classe en balise XML
     * 
     * @param xmlString
     */
    public void toXml(StringBuffer xmlString) {
        xmlString
                .append("<entry total=\"")
                .append(total)
                .append("\" totalFmt=\"")
                .append(total.compareTo(new BigDecimal("0.00")) == 0 ? "&#160;" : JANumberFormatter.format(
                        total.doubleValue(), 0.005, 2, JANumberFormatter.NEAR))
                .append("\" totalFmtPositif=\"")
                .append(JANumberFormatter.format(
                        total.signum() == -1 ? total.negate().doubleValue() : total.doubleValue(), 0.005, 2,
                        JANumberFormatter.NEAR)).append("\">");

        colonne.toXml(xmlString);
        xmlString.append("</entry>");
    }

}
