/*
 * Cr�� le 14 ao�t 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.pojo.wrapper;

import globaz.apg.api.alfa.IAPBouclementAlfa;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class APBouclementAlfaWrapper implements IAPBouclementAlfa {

    private String idAffilie;
    private String idDroit;
    private String idPrestation;
    private String montantBrutACM;
    private String montantCotisationsACM;
    private String montantImpotsACM;
    private String nombreJoursCouvertsACM;
    private String type;

    /**
     * @return
     */
    @Override
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * @return
     */
    @Override
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return
     */
    @Override
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * @return
     */
    @Override
    public String getMontantBrutACM() {
        return montantBrutACM;
    }

    /**
     * @return
     */
    @Override
    public String getMontantCotisationsACM() {
        return montantCotisationsACM;
    }

    /**
     * @return
     */
    @Override
    public String getMontantImpotsACM() {
        return montantImpotsACM;
    }

    /**
     * @return
     */
    @Override
    public String getNombreJoursCouvertsACM() {
        return nombreJoursCouvertsACM;
    }

    /**
     * @return
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @param string
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * @param string
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * @param string
     */
    public void setIdPrestation(String string) {
        idPrestation = string;
    }

    /**
     * @param string
     */
    public void setMontantBrutACM(String string) {
        montantBrutACM = string;
    }

    /**
     * @param string
     */
    public void setMontantCotisationsACM(String string) {
        montantCotisationsACM = string;
    }

    /**
     * @param string
     */
    public void setMontantImpotsACM(String string) {
        montantImpotsACM = string;
    }

    /**
     * @param string
     */
    public void setNombreJoursCouvertsACM(String string) {
        nombreJoursCouvertsACM = string;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

}
