package globaz.pavo.process;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.bind.ValidationEvent;
import ch.admin.zas.rc.IKStatistikMeldungType.Aufzeichnungen;
import globaz.jade.exception.JadeApplicationException;

public class CIAnnonceCentraleException extends JadeApplicationException {
    HashMap<Aufzeichnungen, ArrayList<ValidationEvent>> listError;
    boolean isPathError = false;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CIAnnonceCentraleException() {
        super();
    }

    public CIAnnonceCentraleException(String m) {
        super(m);
    }

    public CIAnnonceCentraleException(String m, Throwable t) {
        super(m, t);
    }

    public void setIsPatchError(boolean isPathError) {
        this.isPathError = isPathError;
    }

    public boolean isPathError() {
        return isPathError;
    }

    public String getErrorMessages() {
        StringBuffer buf = new StringBuffer();
        for (Aufzeichnungen compteCI : listError.keySet()) {
            buf.append("NSS : " + compteCI.getIKKopf().getVersichertennummer().get(0));
            buf.append("\n");
            if (compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getAnfangsmonat() != 0) {
                if (compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getAnfangsmonat() < 10) {
                    buf.append("Période début : 0"
                            + compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getAnfangsmonat() + "."
                            + compteCI.getEintragungIKMeldung().get(0).getBeitragsjahr().getYear());
                } else {
                    buf.append("Période début : "
                            + compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getAnfangsmonat() + "."
                            + compteCI.getEintragungIKMeldung().get(0).getBeitragsjahr().getYear());
                }
            } else {
                buf.append("Période début : -");
            }
            buf.append("\n");
            if (compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getEndmonat() != 0) {
                if (compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getEndmonat() < 10) {
                    buf.append("Période fin : 0"
                            + compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getEndmonat() + "."
                            + compteCI.getEintragungIKMeldung().get(0).getBeitragsjahr().getYear());
                } else {
                    buf.append(
                            "Période fin : " + compteCI.getEintragungIKMeldung().get(0).getBeitragsdauer().getEndmonat()
                                    + "." + compteCI.getEintragungIKMeldung().get(0).getBeitragsjahr().getYear());
                }
            } else {
                buf.append("Période fin : -");
            }
            buf.append("\n");
            buf.append("Genre prestation : " + compteCI.getEintragungIKMeldung().get(0).getSchluesselzahlStornoeintrag()
                    + compteCI.getEintragungIKMeldung().get(0).getSchluesselzahlBeitragsart());
            buf.append("\n");
            buf.append("Données(s) en erreur(s) :");
            buf.append("\n");
            for (ValidationEvent event : listError.get(compteCI)) {
                buf.append("Erreur : " + event.getMessage());
                buf.append("\n");
            }

            buf.append("\n");

        }

        return buf.toString();
    }

    public void setErrors(HashMap<Aufzeichnungen, ArrayList<ValidationEvent>> listError) {
        this.listError = listError;

    }

    public String getFTPError() {
        return "Link FTP not correct : " + toString();
    }

}
