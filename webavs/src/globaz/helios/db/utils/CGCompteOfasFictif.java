package globaz.helios.db.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (21.08.2003)
 * 
 * @author: scr
 */

public class CGCompteOfasFictif {

    private List<String> classesCompte;
    private List<String> exceptionsComptes = new ArrayList<String>();
    private String secteur;

    /**
     * Constructor for CGCompteOfasFictif.
     */
    public CGCompteOfasFictif() {
        classesCompte = new ArrayList<String>();
    }

    public CGCompteOfasFictif(String secteur, List<String> classesCompte) {
        this.secteur = secteur;
        this.classesCompte = classesCompte;
    }

    /**
     * Sets the classesCompte.
     * 
     * @param classesCompte
     *            The classesCompte to set
     */
    public void addClasseCompte(String classesCompte) {
        this.classesCompte.add(classesCompte);
    }

    /**
     * @param exceptionsComptes
     *            the exceptionsComptes to set
     */
    public void addExceptionsComptes(String exceptionsComptes) {
        this.exceptionsComptes.add(exceptionsComptes);
    }

    /**
     * Returns the classesCompte.
     * 
     * @return List
     */
    public List<String> getClassesCompte() {
        return classesCompte;
    }

    /**
     * @return the exceptionsComptes
     */
    public List<String> getExceptionsComptes() {
        return exceptionsComptes;
    }

    /**
     * Returns the secteur.
     * 
     * @return String
     */
    public String getSecteur() {
        return secteur;
    }

    /**
     * Sets the secteur.
     * 
     * @param secteur
     *            The secteur to set
     */
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

}
