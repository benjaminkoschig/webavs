package globaz.vulpecula.vb.comptabilite;

import ch.globaz.vulpecula.domain.models.common.Date;
import globaz.vulpecula.vb.listes.PTListeProcessViewBean;

public class PTListesInternesViewBean extends PTListeProcessViewBean {
    public static enum GenreListeInterne {
        RECAP_CONVENTION,
        RECAP_CAISSE,
        RECAP_GENRE_CAISSE,
        RECAP_CAISSE_CONVENTION
    }

    private String annee;
    private GenreListeInterne genre;
    private boolean avecTo = true;

    public String getAnnee() {
        if (annee == null) {
            return new Date().getAnnee();
        }
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public GenreListeInterne getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = GenreListeInterne.valueOf(genre);
    }

    public boolean isAvecTo() {
        return avecTo;
    }

    public void setAvecTo(boolean avecTo) {
        this.avecTo = avecTo;
    }
}
