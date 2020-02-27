package globaz.aquila.process.elp;

import globaz.globall.util.JACalendar;
import globaz.osiris.external.IntRole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class COInfoFileELP {

    private static final String NAME_FILE_REGEX = "^(.{10})-(.{3})-(.{9})-(.{1})(.*)";
    private static final String ROLE_PERSONNEL = "IND";
    private static final String ROLE_PARITAIRE = "PAR";
    private static final String ROLE_ADMIN = "ADM";

    private String noAffilie;
    private String genreAffiliation;
    private String noSection;
    private String nbPoursuite;
    private String date;
    private String fichier;


    public static COInfoFileELP getInfosFromFile(String file) {
        Pattern pattern = Pattern.compile(NAME_FILE_REGEX);
        Matcher matcher = pattern.matcher(file);
        if (!matcher.matches()) {
            return null;
        }
        COInfoFileELP info = new COInfoFileELP();
        info.setFichier(file);
        info.setDate(JACalendar.todayJJsMMsAAAA());
        info.setNoAffilie(matcher.group(1));
        String genreAffiliationNum = getGenreAffiliationCS(matcher.group(2));
        info.setGenreAffiliation(genreAffiliationNum);
        info.setNoSection(matcher.group(3));
        info.setNbPoursuite(matcher.group(4));
        return info;
    }

    private static String getGenreAffiliationCS(String group) {
        String genreAffiliationCS;
        switch (group) {
            case ROLE_PERSONNEL:
                genreAffiliationCS = IntRole.ROLE_AFFILIE_PERSONNEL;
                break;
            case ROLE_PARITAIRE:
                genreAffiliationCS = IntRole.ROLE_AFFILIE_PARITAIRE;
                break;
            case ROLE_ADMIN:
                genreAffiliationCS = IntRole.ROLE_ADMINISTRATEUR;
                break;
            default:
                genreAffiliationCS = "-1";
                break;
        }
        return genreAffiliationCS;
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    public String getGenreAffiliation() {
        return genreAffiliation;
    }

    public void setGenreAffiliation(String genreAffiliation) {
        this.genreAffiliation = genreAffiliation;
    }

    public String getNoSection() {
        return noSection;
    }

    public void setNoSection(String noSection) {
        this.noSection = noSection;
    }

    public String getNbPoursuite() {
        return nbPoursuite;
    }

    public void setNbPoursuite(String nbPoursuite) {
        this.nbPoursuite = nbPoursuite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }
}
