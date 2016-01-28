package globaz.tucana.statistiques;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Référance du canton
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUColonne implements Comparable {
    private String abreviation = null;
    private boolean cumul = true;
    private String libelle = null;
    private String rang = null;

    /**
     * Constructeur de la classe colonne Par défaut la propriété cumul est à "true"
     */
    public TUColonne(String _rang, String _abreviation, String _libelle) {
        this(_rang, _abreviation, _libelle, true);
    }

    /**
     * Constructeur de la classe colonne
     */
    public TUColonne(String _rang, String _abreviation, String _libelle, boolean _cumul) {
        super();
        rang = _rang;
        libelle = _libelle;
        abreviation = _abreviation;
        cumul = _cumul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object _obj) {
        TUColonne colonne = null;
        if (_obj instanceof TUColonne) {
            colonne = (TUColonne) _obj;
        }
        if (colonne != null) {
            if (getAbreviation().compareToIgnoreCase(colonne.getAbreviation()) == 0) {
                if (rang != null) {
                    return getRang().compareToIgnoreCase(colonne.getRang());
                } else {
                    return 1;
                }
            } else {
                return getAbreviation().compareToIgnoreCase(colonne.getAbreviation());
            }
        } else {
            return 1;
        }
        // if (colonne != null) {
        // return
        // getAbreviation().compareToIgnoreCase(colonne.getAbreviation());
        //
        // } else {
        // return 1;
        // }

    }

    /**
     * Récupère l'abréviation du canton
     * 
     * @return
     */
    public String getAbreviation() {
        return abreviation;
    }

    /**
     * Récupère le libellé du canton
     * 
     * @return
     */
    public String getLibelle() {
        return libelle;
    }

    public String getRang() {
        return rang;
    }

    public boolean isCumul() {
        return cumul;
    }

    public void toCsv(StringBuffer csvString) {
        csvString.append(libelle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n Colonne");
        str.append("\n Abréviation : ").append(abreviation);
        str.append("\n rang : ").append(rang);
        str.append("\n Libellé : ").append(libelle);
        return str.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.db.journal.ITUColonne#toXml(java.lang.StringBuffer)
     */
    public void toXml(StringBuffer xmlString) {
        xmlString.append("<colonne ");
        xmlString.append("abreviation=\"&#160;").append(JadeStringUtil.isEmpty(abreviation) ? "&#160;" : abreviation)
                .append("\" ");
        xmlString.append("rang=\"").append(rang).append("\" ");
        xmlString.append("libelle=\"&#160;").append(libelle).append("\"/>");
    }
}