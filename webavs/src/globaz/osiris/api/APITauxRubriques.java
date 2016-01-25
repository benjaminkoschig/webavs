package globaz.osiris.api;

import globaz.globall.api.BISession;

/**
 * Insérez la description du type ici. Date de création : (20.09.2002 16:35:45)
 * 
 * @author: Administrator
 */
public interface APITauxRubriques {
    public java.lang.String getDate();

    public java.lang.String getIdRubrique();

    /**
     * Getter
     */
    public java.lang.String getIdTauxRubrique();

    public java.lang.String getTauxEmployeur();

    public java.lang.String getTauxSalarie();

    public void setDate(java.lang.String newDate);

    public void setIdRubrique(java.lang.String newIdRubrique);

    /**
     * Setter
     */
    public void setIdTauxRubrique(java.lang.String newIdTauxRubrique);

    /**
     * Modifie la session en cours
     * 
     * @param newSession
     *            la nouvelle session
     */
    public void setISession(BISession newSession);

    public void setTauxEmployeur(java.lang.String newTaux);

    public void setTauxSalarie(java.lang.String newTaux);
}
