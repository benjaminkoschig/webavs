/**
 * 
 */
package ch.globaz.al.liste.process;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import com.google.common.base.Joiner;

/**
 * Classe permettant de retourner un string d'un ensemble d'objet de type ALRevenuMinimal affin de l'utiliser dans la
 * requête SQL pour créer une table temporaire.
 * 
 * @author est
 */
public class GenerateurAnneeRevenuMinimal {

    private int maxYear;

    public GenerateurAnneeRevenuMinimal() {
        maxYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    public GenerateurAnneeRevenuMinimal(int maxYear) {
        this.maxYear = maxYear;
    }

    /**
     * Méthode retournant les entrées spécifiées dans la propriété
     * de manière formaté utile pour la requête sql.
     * 
     * @param propertieValues
     * @return string formaté séparant les entrées de la liste avec des virgules
     */
    protected String genererStringAnneeRevenuMinimal(String propertieValues) {

        final Pattern pattern = Pattern.compile("(\\d{4}:\\d{1,},?)*");

        if (!pattern.matcher(propertieValues).matches() || propertieValues.isEmpty()) {
            throw new IllegalArgumentException("Properties : '" + propertieValues
                    + "' format is wrong somehow. Exemple of expected format : 2013:1170,2015:1175");
        }

        String[] pairs = propertieValues.split(",");
        List<ALRevenuMinAnnee> listeRevenuMinAnnee = new ArrayList<ALRevenuMinAnnee>();

        for (int i = 0; i < pairs.length; i++) {
            String[] keyValue = pairs[i].split(":");
            listeRevenuMinAnnee.add(new ALRevenuMinAnnee(Integer.valueOf(keyValue[0]), Integer.valueOf(keyValue[1])));
        }

        if (listeRevenuMinAnnee.get(listeRevenuMinAnnee.size() - 1).getAnnee() > maxYear) {
            maxYear = listeRevenuMinAnnee.get(listeRevenuMinAnnee.size() - 1).getAnnee();
        }

        // Populate list
        while (listeRevenuMinAnnee.size() != (maxYear - listeRevenuMinAnnee.get(0).getAnnee()) + 1) {
            int sizeListeAvantPeuplage = listeRevenuMinAnnee.size();
            for (int i = 0; i < sizeListeAvantPeuplage; i++) {
                ALRevenuMinAnnee anneeEntree = listeRevenuMinAnnee.get(i);
                ALRevenuMinAnnee anneeEntreeSuivante = new ALRevenuMinAnnee(0, 0);

                // Dernière entrée de la liste
                if (i + 1 == listeRevenuMinAnnee.size()) {
                    anneeEntreeSuivante.setAnnee(anneeEntree.getAnnee() + 1);
                    anneeEntreeSuivante.setRevenuMinimal(anneeEntree.getRevenuMinimal());
                    listeRevenuMinAnnee.add(anneeEntreeSuivante);
                } else {
                    anneeEntreeSuivante = listeRevenuMinAnnee.get(i + 1);
                }

                // Si l'année de l'entrée est différente de l'entrée suivante -1, on peuple la liste
                if (anneeEntree.getAnnee() != anneeEntreeSuivante.getAnnee() - 1) {
                    listeRevenuMinAnnee.add(anneeEntree.generateProchainRevenuMinAnnee());
                }
                Collections.sort(listeRevenuMinAnnee);
            }
        }
        return toStringForSql(listeRevenuMinAnnee);
    }

    public String toStringForSql(List<ALRevenuMinAnnee> listeRevenuMinAnnee) {
        return Joiner.on(",").join(listeRevenuMinAnnee);
    }
}
