package ch.globaz.vulpecula.domain.models.association;

import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public class ModeleEntete implements Comparable<ModeleEntete> {
    private final String id;
    private final String idTiers;
    private final String jasperFr;
    private final String jasperDe;
    private final String libelle;

    public ModeleEntete(String id) {
        this(id, null, null, null, null);
    }

    public ModeleEntete(String id, String idTiers, String jasper, String jasperDe, String libelle) {
        this.id = id;
        this.idTiers = idTiers;
        jasperFr = jasper;
        this.jasperDe = jasperDe;
        this.libelle = libelle;
    }

    public String getId() {
        return id;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getJasper(CodeLangue langue) {
        if (CodeLangue.DE.equals(langue)) {
            return jasperDe.trim();
        }
        return jasperFr.trim();
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public int compareTo(ModeleEntete o) {
        return libelle.compareTo(o.libelle);
    }
}
