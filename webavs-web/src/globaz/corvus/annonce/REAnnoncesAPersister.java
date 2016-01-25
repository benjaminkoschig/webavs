package globaz.corvus.annonce;

import globaz.corvus.db.annonces.REAnnonceHeader;

public class REAnnoncesAPersister {

    private REAnnonceHeader annonce01;
    private REAnnonceHeader annonce02;

    public REAnnoncesAPersister(REAnnonceHeader annonce01, REAnnonceHeader annonce02) {
        if (annonce01 == null) {
            throw new IllegalArgumentException("annonce01 is null");
        }
        if (annonce02 == null) {
            throw new IllegalArgumentException("annonce02 is null");
        }
        this.annonce01 = annonce01;
        this.annonce02 = annonce02;
    }

    public REAnnonceHeader getAnnonce01() {
        return annonce01;
    }

    public REAnnonceHeader getAnnonce02() {
        return annonce02;
    }

}
