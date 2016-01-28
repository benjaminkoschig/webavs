package globaz.pegasus.process.liste;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Une liste r�capitulative des totaux par commune PC et RFM est g�n�r�e au format Excel (xmlml).
 * La liste contient la somme des prestations vers�es et restitu�es par commune durant toute la p�riode concern�e
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

            // 1. R�cup�ration des donn�es
            // a. d�cision PC
            // b. Paiement PC
            // c. d�cision RFM

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
            throw new IllegalArgumentException("La date de d�but de p�riode ne peut �tre vide");
        }
        // if (dateMonthFin == null || JadeStringUtil.isBlankOrZero(dateMonthFin)) {
        // throw new IllegalArgumentException("La date de fin de p�riode ne peut �tre vide");
        // }

        // Test si date de fin plus petite que date de d�but

        // Une liste r�capitulative dans le futur n'a aucun sens.
        // D�s lors, une message d'erreur peut �tre ajout� lorsque la date de fin de p�riode est plus grande ou �gale au
        // prochaine paiement.
        // R�cup�ration date du prochain paiement
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
