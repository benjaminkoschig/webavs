package globaz.osiris.api;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (13.02.2002 10:22:45)
 * 
 * @author: Administrator
 */
public interface APIVersement extends APIPaiement {
    public String getIdOrdreVersement();

    public APIOperationOrdreVersement getOrdreVersement();

    public void setIdOrdreVersement(String newIdOrdreVersement);
}
