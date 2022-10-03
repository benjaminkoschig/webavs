package globaz.corvus.process.cron;

import java.util.ArrayList;
import java.util.List;

public class REProtocoleErreurAdaptationsRentes {

    List<String> annonces53enErreur;
    List<String> annonces51enErreur;
    List<String> annonces61enErreur;
    String fichier;

    public REProtocoleErreurAdaptationsRentes(String aFile) {
        annonces53enErreur = new ArrayList<>();
        annonces51enErreur = new ArrayList<>();
        annonces61enErreur = new ArrayList<>();
        fichier = aFile;
    }

    public void addAnnonces53enErreur(String annonce53) {
        annonces53enErreur.add(annonce53);
    }

    public void addAnnonces51enErreur(String annonce51) {
        annonces51enErreur.add(annonce51);
    }

    public void addAnnonces61enErreur(String annonce61) {
        annonces61enErreur.add(annonce61);
    }

    public boolean hasErrors() {
        return !annonces53enErreur.isEmpty() || !annonces51enErreur.isEmpty();
    }

    public String getAnnonces53enErreur() {
        StringBuilder annonces = new StringBuilder();
        for (int i = 0 ; i < annonces53enErreur.size(); i++) {
            annonces.append(annonces53enErreur.get(i));
            if (i < annonces53enErreur.size() - 1) {
                annonces.append(", ");
            }
        }
        return annonces.toString();
    }

    public String getAnnonces51enErreur() {
        StringBuilder annonces = new StringBuilder();
        for (int i = 0 ; i < annonces51enErreur.size(); i++) {
            annonces.append(annonces51enErreur.get(i));
            if (i < annonces51enErreur.size() - 1) {
                annonces.append(", ");
            }
        }
        return annonces.toString();
    }

    public String getAnnonces61enErreur() {
        StringBuilder annonces = new StringBuilder();
        for (int i = 0 ; i < annonces61enErreur.size(); i++) {
            annonces.append(annonces61enErreur.get(i));
            if (i < annonces61enErreur.size() - 1) {
                annonces.append(", ");
            }
        }
        return annonces.toString();
    }

    public String getFichier() {
        return fichier;
    }
}
