/*
 * Créé le 19 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
 * Manager permettant de faire la requête qui sortira les affiliés pour lesquels un rappel pour les relevés doit être
 * envoyé
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFGestionReleveManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Période pour laquelle on veut générer les rappels
    private String periode;
    // Périodicité de l'affilié (Annuel, Trimestriel, mensuel)
    private String periodicite;

    public AFGestionReleveManager() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    // Construit la clause from de la requête
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFAFFIP";
    }

    // Retourne la clause orderBy de la requête
    @Override
    protected String _getOrder(BStatement statement) {
        return "MALNAF";
    }

    // Construit la clause where de la requête
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

    // Retourne le premier jour de la période
    private String getDebutPeriodeFormate() {
        return "01." + getPeriode();
    }

    // Retourne le dernier jour de la période
    private String getFinPeriodeFormate() {
        JACalendarGregorian cal = new JACalendarGregorian();
        int periode = -1;
        try {
            // On sort le mois de la période
            periode = JADate.getMonth(getPeriode()).intValue();
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }
        int annee = -1;
        try {
            // On sort l'année de la période
            annee = JADate.getYear(getPeriode()).intValue();
        } catch (JAException e1) {
            JadeLogger.error(this, e1);
        }
        // On retourne le dernier jour du mois
        return cal.daysInMonth(periode, annee) + "." + getPeriode();
    }

    /**
     * Renvoie la période
     * 
     * @return periode
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * Renvoie la périodicité
     * 
     * @return periodicite
     */
    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * Permet de setter la période
     * 
     * @param string
     *            periode
     */
    public void setPeriode(String string) {
        periode = string;
    }

    /**
     * Permet de setter la périodicité
     * 
     * @param string
     *            periodicite
     */
    public void setPeriodicite(String string) {
        periodicite = string;
    }

}
