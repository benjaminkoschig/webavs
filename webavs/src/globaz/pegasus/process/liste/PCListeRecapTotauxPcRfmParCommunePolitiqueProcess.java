package globaz.pegasus.process.liste;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Une liste récapitulative des totaux par commune PC et RFM est générée au format Excel (xmlml).
 * La liste contient la somme des prestations versées et restituées par commune durant toute la période concernée
 * 
 * @author sco
 * 
 */
public class PCListeRecapTotauxPcRfmParCommunePolitiqueProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private String dateMonthDebut;
    private String dateMonthFin;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean status = true;

        try {

            // 1. Récupération des données
            // a. décision PC
            // b. Paiement PC
            // c. décision RFM

            // 2. Consolidation

            // 3. Impression de la liste

        } catch (Exception e) {

            status = false;
        }

        return status;

    }

    @Override
    protected void _validate() throws Exception {

        if (dateMonthDebut == null || JadeStringUtil.isBlankOrZero(dateMonthDebut)) {
            throw new IllegalArgumentException("La date de début de période ne peut être vide");
        }
        // if (dateMonthFin == null || JadeStringUtil.isBlankOrZero(dateMonthFin)) {
        // throw new IllegalArgumentException("La date de fin de période ne peut être vide");
        // }

        // Test si date de fin plus petite que date de début

        // Une liste récapitulative dans le futur n'a aucun sens.
        // Dès lors, une message d'erreur peut être ajouté lorsque la date de fin de période est plus grande ou égale au
        // prochaine paiement.
        // Récupération date du prochain paiement
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return "Erreur";
        } else {
            return "OK";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public String getDateMonthDebut() {
        return dateMonthDebut;
    }

    public void setDateMonthDebut(String dateMonthDebut) {
        this.dateMonthDebut = dateMonthDebut;
    }

    public String getDateMonthFin() {
        return dateMonthFin;
    }

    public void setDateMonthFin(String dateMonthFin) {
        this.dateMonthFin = dateMonthFin;
    }

}
