package globaz.aquila.process.elp;

import globaz.aquila.print.list.elp.COMotifMessageELP;
import globaz.aquila.print.list.elp.COTypeMessageELP;
import globaz.globall.db.BSession;
import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;

public abstract class COAbstractELP {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    String fichier;
    COMotifMessageELP motif;
    String dateReception;
    String motifAdditional;
    String numeroStatut;

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

    public String getNumeroStatut(){
        return numeroStatut;
    }

    public abstract String getRemarque();

    public abstract COTypeMessageELP getTypemessage();

    protected String getDate(XMLGregorianCalendar calendar) {
        return calendar == null ? StringUtils.EMPTY : new SimpleDateFormat(DATE_FORMAT).format(calendar.toGregorianCalendar().getTime());
    }
}
