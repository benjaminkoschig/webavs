package globaz.tucana.statistiques;

public class TUGroupeCategoriesKey implements Comparable {
    private Integer idCategorie = null;
    private Integer ordre = null;

    private TUGroupeCategoriesKey() {
        super();
    }

    private TUGroupeCategoriesKey(Integer _idCategorie, Integer _ordre) {
        idCategorie = _idCategorie;
        ordre = _ordre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object obj) {
        if (obj instanceof TUGroupeCategoriesKey) {
            if (ordre.compareTo(((TUGroupeCategoriesKey) obj).getOrdre()) == 0) {
                return idCategorie.compareTo(((TUGroupeCategoriesKey) obj).getOrdre());
            }
            return ordre.compareTo(((TUGroupeCategoriesKey) obj).getOrdre());
        }
        return -1;
    }

    public Integer getIdCategorie() {
        return idCategorie;
    }

    public Integer getOrdre() {
        return ordre;
    }

}
