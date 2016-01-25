package globaz.corvus.vb.process;

import globaz.corvus.db.ci.TypeListeCiAdditionnels;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author BSC
 */
public class REGenererListeCiAdditionnelsViewBean extends PRAbstractViewBeanSupport {

    private String dateDebut = "";
    private String dateFin = "";
    private String eMailAddress = "";
    private TypeListeCiAdditionnels genreCiAdd = TypeListeCiAdditionnels.RECEPTIONNES;

    public REGenererListeCiAdditionnelsViewBean() {
        super();
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public TypeListeCiAdditionnels getGenreCiAdd() {
        return genreCiAdd;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setGenreCiAdd(String genreCiAdd) {
        this.genreCiAdd = TypeListeCiAdditionnels.valueOf(genreCiAdd);
    }

    public void setGenreCiAdd(TypeListeCiAdditionnels genreCiAdd) {
        this.genreCiAdd = genreCiAdd;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
