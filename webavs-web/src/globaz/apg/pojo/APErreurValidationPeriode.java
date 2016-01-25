package globaz.apg.pojo;

import globaz.apg.db.droits.APPeriodeAPG;

/**
 * Simple container d'infos. Utilis� lors de validation de p�riodes pour remonter un message d'erreur ainsi que les date
 * de d�but et de fin de la p�riode
 * 
 * @author lga
 */
public class APErreurValidationPeriode {

    private APPeriodeAPG periode;
    private String messageErreur;

    public APErreurValidationPeriode(APPeriodeAPG periode, String messageErreur) {
        this.periode = periode;
        this.messageErreur = messageErreur;
    }

    public APPeriodeAPG getPeriode() {
        return periode;
    }

    public void setPeriode(APPeriodeAPG periode) {
        this.periode = periode;
    }

    public final String getMessageErreur() {
        return messageErreur;
    }

    public final void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

}
