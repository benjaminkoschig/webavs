package globaz.aquila.print.list.elp;

import globaz.globall.db.BSession;

public abstract class COAbstractResultELP {

    String fichier;
    COMotifMessageELP motif;
    String dateReception;
    String motifAdditional;

    public COMotifMessageELP getMotif() {
        return motif;
    }

    public void setMotif(COMotifMessageELP motif) {
        this.motif = motif;
    }

    public String getDateReception() {
        return dateReception;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getMotifAdditional() {
        return motifAdditional;
    }

    public void setMotifAdditional(String motifAdditional) {
        this.motifAdditional = motifAdditional;
    }

    public String getMotif(BSession session) {
        String motifString = session.getLabel(motif.getValue());
        if(motifAdditional != null) {
            motifString+= motifAdditional;
        }
        return motifString;
    }

    public abstract String getRemarque();

    public abstract COTypeMessageELP getTypemessage();
}
