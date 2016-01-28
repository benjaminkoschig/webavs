package globaz.tucana.statistiques;

import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.tucana.exception.process.TUProcessJournalException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Classe représentant une catégorie
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUCategorie implements Comparable {
    public static final String ABREVIATION_9_MOIS = "09m";
    public static final String ABREVIATION_PREMIER_SEMESTRE = "06s";
    public static final String RANG_9_MOIS = "09";
    public static final String RANG_PREMIER_SEMESTRE = "06";

    private String abreviation = null;
    private TreeMap entries = null;
    private String identifiant = null;
    private String libelle = null;
    private String ordre = null;
    private BigDecimal total = null;

    /**
     * Constructeur de la categorie
     * 
     * @param _identifiant
     * @param _libelleCategorie
     */
    public TUCategorie(String _ordre, String _identifiant, String _abreviation, String _libelleCategorie) {
        ordre = _ordre;
        identifiant = _identifiant;
        abreviation = _abreviation;
        libelle = _libelleCategorie;
        init();
    }

    /**
     * Insère une entrée
     * 
     * @param transaction
     * @param colonne
     * @param nombreMontant
     */
    public void addEntry(BTransaction transaction, TUColonne colonne, BigDecimal nombreMontant)
            throws TUProcessJournalException {
        TUEntry current = null;
        if (entries.containsKey(colonne)) {
            current = (TUEntry) entries.get(colonne);
            current.addToTotal(nombreMontant);
        } else {
            current = new TUEntry(colonne, nombreMontant);
        }

        entries.put(colonne, current);
        // ajoute au total si pas "total 1er semestre" et pas 9ème mois
        if (!colonne.getRang().equals(TUCategorie.RANG_PREMIER_SEMESTRE)
                && !colonne.getRang().equals(TUCategorie.RANG_9_MOIS)) {
            total = colonne.isCumul() ? total.add(nombreMontant) : total;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object arg0) {
        if (arg0 instanceof TUCategorie) {
            if (ordre.compareTo(((TUCategorie) arg0).getOrdre()) == 0) {
                return getIdentifiant().compareToIgnoreCase(((TUCategorie) arg0).getIdentifiant());
            }
            return ordre.compareTo(((TUCategorie) arg0).getOrdre());
        } else {
            return 1;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        return (arg0 instanceof TUCategorie) && identifiant.equals(((TUCategorie) arg0).getIdentifiant());
    }

    /**
     * Récupère l'abréviation
     * 
     * @return
     */
    public String getAbreviation() {
        return abreviation;
    }

    /**
     * Retoune un TreeMap d'entries
     * 
     * @return
     */
    public TreeMap getEntries() {
        return entries;
    }

    /**
     * Récupère l'identifiant
     * 
     * @return
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Récupère le libellé
     * 
     * @return
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Récupère l'ordre
     * 
     * @return
     */
    public String getOrdre() {
        return ordre;
    }

    /**
     * Récupère le total
     * 
     * @return
     */
    public BigDecimal getTotal() {
        return total;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return ordre.hashCode() + identifiant.hashCode();
    }

    /**
     * Initialisation des propriétés de la classe
     * 
     */
    private void init() {
        total = JAUtil.createBigDecimal("0", 3);
        entries = new TreeMap();
    }

    public void toCsv(StringBuffer csvString, String libelleGroupe, TreeMap tree) {
        csvString.append(libelleGroupe).append(libelle).append(";");
        Iterator it = entries.keySet().iterator();
        while (it.hasNext()) {
            ((TUEntry) entries.get(it.next())).toCsv(tree);
        }
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n Catégories");
        str.append("\n Identifiant : ").append(identifiant);
        str.append("\n Ordre : ").append(ordre);
        str.append("\n Abréviation : ").append(abreviation);
        str.append("\n Libellé : ").append(libelle);
        str.append("\n Nombre - montant : ").append(total);
        str.append("\n - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        Iterator iter = entries.keySet().iterator();
        // while (iter.hasNext()) {
        // Object element = iter.next();
        // str.append(element.toString());
        // }
        while (iter.hasNext()) {
            str.append(((TUEntry) entries.get(iter.next())).toString());
        }

        return str.toString();
    }

    /**
     * Génère la calsse en balise XML
     * 
     * @param xmlString
     */
    public void toXml(StringBuffer xmlString) {
        xmlString.append("<categorie ");
        xmlString.append("identifiant=\"").append(identifiant).append("\" ");
        xmlString.append("ordre=\"").append(ordre).append("\" ");
        xmlString.append("abreviation=\"").append(abreviation).append("\" ");
        xmlString.append("libelle=\"").append(libelle).append("\" ");
        xmlString.append("total=\"").append(total).append("\" ");
        xmlString.append("totalFmt=\"")
                .append(JANumberFormatter.format(total.doubleValue(), 0.005, 2, JANumberFormatter.NEAR)).append("\" ");
        ;
        xmlString
                .append("totalFmtPositif=\"")
                .append(JANumberFormatter.format(
                        total.signum() == -1 ? total.negate().doubleValue() : total.doubleValue(), 0.005, 2,
                        JANumberFormatter.NEAR)).append("\">");

        Iterator it = entries.keySet().iterator();
        while (it.hasNext()) {
            ((TUEntry) entries.get(it.next())).toXml(xmlString);
        }

        xmlString.append("</categorie>");

    }
}
