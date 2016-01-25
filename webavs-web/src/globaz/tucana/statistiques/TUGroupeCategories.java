package globaz.tucana.statistiques;

import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.exception.process.TUProcessJournalException;
import globaz.tucana.statistiques.config.TUCategorieRubriqueStatistiqueConfig;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Classe représentant un groupe de catégorie
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 */
public class TUGroupeCategories implements Comparable {
    private String abreviation = null;
    private List categories = null;
    private TUCategorieRubriqueStatistiqueConfig configStatistique = null;
    private String identifiant = null;
    private String idGroupeCategorie = null;
    private String libelle = null;
    private String ordre = null;

    /**
     * Constructeur de la categorie
     * 
     * @param _identifiant
     * @param _ordre
     * @param _abreviation
     * @param _libelleCategorie
     * @param _idGroupeCategorie
     * @param _configStatistique
     */
    public TUGroupeCategories(String _identifiant, String _ordre, String _abreviation, String _libelleCategorie,
            String _idGroupeCategorie, TUCategorieRubriqueStatistiqueConfig _configStatistique) {
        identifiant = _identifiant;
        ordre = _ordre;
        abreviation = _abreviation;
        libelle = _libelleCategorie;
        idGroupeCategorie = _idGroupeCategorie;
        configStatistique = _configStatistique;
        init();
    }

    /**
     * Ajout d'une categorie
     * 
     * @param transaction
     * @param colonne
     * @param idCategorie
     * @param libelleCategorie
     * @param ordre
     * @param nombreMontant
     * @throws TUProcessJournalException
     */
    public void addGroupeCategorie(BTransaction transaction, TUColonne colonne, String ordre, String idCategorie,
            String abreviationCategorie, String libelleCategorie, BigDecimal nombreMontant)
            throws TUProcessJournalException {

        TUCategorie categorie = findCategorie(new TUCategorie(ordre, JadeStringUtil.fillWithZeroes(idCategorie, 3),
                abreviationCategorie, libelleCategorie));
        categorie.addEntry(transaction, colonne, nombreMontant);

        // categories.put(JadeStringUtil.fillWithZeroes(idCategorie,3),
        // categorie);
        try {
            categories.remove(categorie);
        } catch (Exception e) {
            // ne fait rien simplement au cas ou la catégorie n'existait pas...
        }
        categories.add(categorie);

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object arg0) {
        if (arg0 instanceof TUGroupeCategories) {
            if (ordre.compareToIgnoreCase(((TUGroupeCategories) arg0).getOrdre()) == 0) {
                return getIdentifiant().compareToIgnoreCase(((TUGroupeCategories) arg0).getIdentifiant());
            } else {
                return ordre.compareToIgnoreCase(((TUGroupeCategories) arg0).getOrdre());
            }
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
        return (arg0 instanceof TUGroupeCategories) && identifiant.equals(((TUGroupeCategories) arg0).getIdentifiant());
    }

    /**
     * Retroune la categorie
     * 
     * @param idCategorie
     * @param libelleCategorie
     * @return
     */
    private TUCategorie findCategorie(TUCategorie categorie) {
        if (categories.contains(categorie)) {
            return (TUCategorie) categories.get(categories.indexOf(categorie));
        } else {
            return categorie;
        }
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
     * Retourne le Map des catégories
     * 
     * @return
     */
    public List getCategories() {
        return categories;
    }

    /**
     * Retourne la configuration statistique pour le groupe catégorie en question
     * 
     * @return
     */
    public TUCategorieRubriqueStatistiqueConfig getConfigStatistique() {
        return configStatistique;
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
     * Récupère l'id du groupe catégorie
     * 
     * @return
     */
    public String getIdGroupeCategorie() {
        return idGroupeCategorie;
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
     * Initialisation des autres propriétés
     */
    private void init() {
        categories = new ArrayList();
    }

    public void toCsv(StringBuffer csvString, TreeMap tree) {
        // libelle
        // csvString.append(libelle).append(";");
        Collections.sort(categories);
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            ((TUCategorie) it.next()).toCsv(csvString, libelle, tree);
            Iterator iterator = tree.keySet().iterator();
            while (iterator.hasNext()) {
                csvString.append(tree.get(iterator.next()));
                csvString.append(";");
            }
            csvString.append("\n");
        }
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n Groupe catégories");
        str.append("\n Id groupe catégorie : ").append(idGroupeCategorie);
        str.append("\n Identifiant : ").append(identifiant);
        str.append("\n Abreviation : ").append(abreviation);
        str.append("\n Libellé : ").append(libelle);
        if (configStatistique != null) {
            str.append("\n --> id config :").append(configStatistique.getId());
            str.append("\n --> nom config :").append(configStatistique.getNom());
            str.append("\n --> order config :").append(configStatistique.getOrder());
            str.append("\n --> group config :").append(configStatistique.getGroup());
            str.append("\n --> signe config :").append(configStatistique.getSigne());
            str.append("\n --> affiché config :").append(configStatistique.isAffiche());
        }
        str.append("\n - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        // Collections.sort(list)
        // Iterator iter = categories.values().iterator();
        // while (iter.hasNext()) {
        // TUCategorie element = (TUCategorie) categories.get(iter.next());
        // str.append(element.toString());
        // }

        return str.toString();

    }

    public BigDecimal totalGroupeCategories() {
        BigDecimal total = new BigDecimal("0.00");
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            total = total.add(((TUCategorie) it.next()).getTotal());
        }
        return total;
    }

    /**
     * Génère la classe en balise XML
     * 
     * @param xmlString
     */
    public void toXml(StringBuffer xmlString) {
        xmlString.append("<groupeCategories ");
        xmlString.append("identifiant=\"").append(identifiant).append("\" ");
        xmlString.append("ordre=\"").append(ordre).append("\" ");
        xmlString.append("abreviation=\"").append(abreviation).append("\" ");
        xmlString.append("libelle=\"").append(libelle).append("\" ");
        if (configStatistique != null) {
            xmlString.append("signe=\"").append(configStatistique.getSigne()).append("\" ");
            xmlString.append("affiche=\"").append(configStatistique.isAffiche()).append("\" ");
        }
        xmlString.append(">");
        // Iterator it = categories.keySet().iterator();
        Collections.sort(categories);
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            ((TUCategorie) it.next()).toXml(xmlString);
        }

        xmlString.append("</groupeCategories>");
    }
}
