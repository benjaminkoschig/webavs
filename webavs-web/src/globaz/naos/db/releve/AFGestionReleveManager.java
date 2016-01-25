/*
 * Cr�� le 19 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.releve;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * Manager permettant de faire la requ�te qui sortira les affili�s pour lesquels un rappel pour les relev�s doit �tre
 * envoy�
 * 
 * @author sda
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AFGestionReleveManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // P�riode pour laquelle on veut g�n�rer les rappels
    private String periode;
    // P�riodicit� de l'affili� (Annuel, Trimestriel, mensuel)
    private String periodicite;

    public AFGestionReleveManager() {
        super();
        // TODO Raccord de constructeur auto-g�n�r�
    }

    // Construit la clause from de la requ�te
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFAFFIP";
    }

    // Retourne la clause orderBy de la requ�te
    @Override
    protected String _getOrder(BStatement statement) {
        return "MALNAF";
    }

    // Construit la clause where de la requ�te
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "(MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFinPeriodeFormate())
                + " OR MADFIN=0) AND MATPER IN (" + getPeriodicite() + ") AND ( " + _getCollection()
                + "AFAFFIP.MABREP = 1 OR " + _getCollection()
                + "AFAFFIP.MABREI = 1) AND MALNAF NOT IN (SELECT MALNAF FROM " + _getCollection()
                + "AFREVEP WHERE (MMDFIN <= "
                + this._dbWriteDateAMJ(statement.getTransaction(), getFinPeriodeFormate()) + " AND MMDFIN >"
                + this._dbWriteDateAMJ(statement.getTransaction(), getDebutPeriodeFormate()) + ")AND MMTYRE ="
                + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_RELEVE_PERIODIQUE) + ")";

        return where;
    }

    // Pour sortir l'entity
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliation();
    }

    // Retourne le premier jour de la p�riode
    private String getDebutPeriodeFormate() {
        return "01." + getPeriode();
    }

    // Retourne le dernier jour de la p�riode
    private String getFinPeriodeFormate() {
        JACalendarGregorian cal = new JACalendarGregorian();
        int periode = -1;
        try {
            // On sort le mois de la p�riode
            periode = JADate.getMonth(getPeriode()).intValue();
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }
        int annee = -1;
        try {
            // On sort l'ann�e de la p�riode
            annee = JADate.getYear(getPeriode()).intValue();
        } catch (JAException e1) {
            JadeLogger.error(this, e1);
        }
        // On retourne le dernier jour du mois
        return cal.daysInMonth(periode, annee) + "." + getPeriode();
    }

    /**
     * Renvoie la p�riode
     * 
     * @return periode
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * Renvoie la p�riodicit�
     * 
     * @return periodicite
     */
    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * Permet de setter la p�riode
     * 
     * @param string
     *            periode
     */
    public void setPeriode(String string) {
        periode = string;
    }

    /**
     * Permet de setter la p�riodicit�
     * 
     * @param string
     *            periodicite
     */
    public void setPeriodicite(String string) {
        periodicite = string;
    }

}
