package globaz.prestation.beans;

import globaz.jade.client.util.JadeDateUtil;
import java.io.Serializable;

public class PRTiersDateTime implements Serializable {
    private static final long serialVersionUID = -1771470687772965682L;

    private String idTiers;
    private Long dateTime;

    public PRTiersDateTime(final String idTiers) {
        this.idTiers = idTiers;
        dateTime = JadeDateUtil.getCurrentTime();
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Savoir si la date donnée est comprise entre 0 à 5 secondes avec la date de l'objet.
     * 
     * @param dateTimeToCompare Le datetime a comparer avec la date de l'objet.
     * @return True si comprise entre 0 à 5 secondes.
     */
    public boolean isDateBetween0To5Seconds(final Long dateTimeToCompare) {
        if (dateTimeToCompare == null || dateTime == null) {
            return false;
        }

        return dateTimeToCompare - dateTime < 5000;
    }
}
