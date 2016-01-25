package ch.globaz.al.businessimpl.services.paiement;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.i18n.JadeI18n;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.prestations.ALPaiementPrestationException;
import ch.globaz.al.business.paiement.PaiementBusinessModel;
import ch.globaz.al.business.services.paiement.ProtocoleCSVPaiementDirect;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class ProtocoleCSVPaiementDirectImpl extends ALAbstractBusinessServiceImpl implements ProtocoleCSVPaiementDirect {

    /**
     * Prépare le contenu du protocole CSV
     * 
     * @param listPrest
     *            Liste des prestations
     * @param limite
     *            Limite au-delà de laquelle les montants doivent apparaitre dans le CSV
     * 
     * @return Contenu du CSV
     */
    private String buildCSVMontantSuperieurALimite(ArrayList<PaiementBusinessModel> listPrest, String limite,
            HashMap<String, String> params) {

        BigDecimal max = new BigDecimal(limite);
        StringBuffer csv = new StringBuffer();

        csv.append("Processus : ").append(params.get(ALConstProtocoles.INFO_PROCESSUS)).append(";");
        csv.append(";");
        csv.append(";");
        csv.append("Traitement : ").append(params.get(ALConstProtocoles.INFO_TRAITEMENT))
                .append(" prestation(s) supérieure(s) à " + limite + " CHF ");
        csv.append("\n");

        csv.append("Utilisateur : ").append(params.get(ALConstProtocoles.INFO_UTILISATEUR)).append(";");
        csv.append(";");
        csv.append(";");
        csv.append("No passage : ").append(params.get(ALConstProtocoles.INFO_PASSAGE)).append(";");
        csv.append("\n");

        csv.append("Date/Heure : ").append(params.get(ALConstProtocoles.INFO_DATEHEURE)).append(";");
        csv.append(";");
        csv.append(";");

        csv.append("Période : ").append(params.get(ALConstProtocoles.INFO_PERIODE)).append(";");
        csv.append("\n");

        csv.append("\n");
        csv.append("\n");

        csv.append("Allocataire").append(";");
        csv.append("Solde initial").append(";");
        csv.append("Débit").append(";");
        csv.append("Crédit").append(";");
        csv.append("Nouveau solde").append(";");
        csv.append("Ordre Versement").append(";").append("\n");

        int compteur = 0;
        for (PaiementBusinessModel item : listPrest) {

            if (item.getCredit().compareTo(max) >= 0) {
                compteur++;
                csv.append(item.getNomAllocataire()).append(" - ").append(item.getNssAllocataire()).append(";");
                csv.append(item.getSoldeInitial().toPlainString()).append(";");
                csv.append(item.getDebit().toPlainString()).append(";");
                csv.append(item.getCredit().toPlainString()).append(";");
                csv.append(item.getNouveauSolde().toPlainString()).append(";");
                csv.append(item.getOrdreVersement().toPlainString()).append("\n");
            }
        }

        if (compteur == 0) {
            csv.append("Aucune prestation par allocataire supérieure à " + limite + " CHF");
        }

        return csv.toString();
    }

    /**
     * Construit la liste des paiement des allocataires
     * 
     * @param listePaiement
     * @return
     */
    private StringBuilder buildListeAllocataires(Collection<PaiementBusinessModel> listePaiement) {

        StringBuilder csv = new StringBuilder();
        StringBuilder csvPaiements = new StringBuilder();
        StringBuilder csvRestitutions = new StringBuilder();

        BigDecimal totalCreditVersement = new BigDecimal("0");
        BigDecimal totalDebitVersement = new BigDecimal("0");
        BigDecimal totalDebitRestitution = new BigDecimal("0");
        BigDecimal totalOrdreVersement = new BigDecimal("0");

        for (PaiementBusinessModel paiement : listePaiement) {

            StringBuffer ligne = new StringBuffer();
            ligne.append(paiement.getNomAllocataire()).append(" - ").append(paiement.getNssAllocataire()).append(";");
            ligne.append(paiement.getSoldeInitial().toPlainString()).append(";");
            ligne.append(paiement.getDebit().toPlainString()).append(";");
            ligne.append(paiement.getCredit().toPlainString()).append(";");
            ligne.append(paiement.getNouveauSolde().toPlainString()).append(";");
            ligne.append(paiement.getOrdreVersement().toPlainString()).append("\n");

            if (paiement.isRestitution()) {
                csvRestitutions.append(ligne);
                totalDebitRestitution = totalDebitRestitution.add(paiement.getDebit());
            } else {
                csvPaiements.append(ligne);
                totalCreditVersement = totalCreditVersement.add(paiement.getCredit());
                totalDebitVersement = totalDebitVersement.add(paiement.getDebit());
                totalOrdreVersement = totalOrdreVersement.add(paiement.getOrdreVersement());
            }
        }

        csv.append(csvPaiements).append("\n");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.total.verseCompensation.label")).append(";;")
                .append(totalDebitVersement).append(";").append(totalCreditVersement).append(";;")
                .append(totalOrdreVersement).append("\n\n");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.restitution.entete")).append("\n");
        csv.append(csvRestitutions).append("\n");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.total.restitution.label")).append(";;")
                .append(totalDebitRestitution).append("\n\n");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.total.general.label")).append(";;")
                .append(totalDebitRestitution.add(totalDebitVersement)).append(";").append(totalCreditVersement)
                .append(";;").append(totalOrdreVersement).append("\n\n");

        return csv;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.paiement.ProtocoleCSVPaiementDirect#getCSVListeAllocataire(java.util.Collection,
     * java.util.HashMap)
     */
    @Override
    public String getCSVListeAllocataire(Collection<PaiementBusinessModel> listePaiement, HashMap<String, String> params)
            throws JadeApplicationException {

        if (listePaiement == null) {
            throw new ALPaiementPrestationException("ProtocoleCSVPaiementDirectImpl#getCSV : listeRecap is null");
        }

        StringBuilder csv = new StringBuilder(initHeaderListeAllocataire(params));
        csv.append(buildListeAllocataires(listePaiement));

        return csv.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.paiement.ProtocoleCSVPaiementDirect#getCSVMontantSuperieurALimite(java.util.Collection
     * , java.lang.String, java.util.HashMap)
     */
    @Override
    public String getCSVMontantSuperieurALimite(Collection<PaiementBusinessModel> listeRecap, String limite,
            HashMap<String, String> params) throws JadeApplicationException {

        if (listeRecap == null) {
            throw new ALPaiementPrestationException("ProtocoleCSVPaiementDirectImpl#getCSV : listeRecap is null");
        }

        if (!JadeNumericUtil.isNumeric(limite)) {
            throw new ALPaiementPrestationException(
                    "ProtocoleCSVPaiementDirectImpl#getCSV : limite is not a numeric value");
        }

        HashMap<String, PaiementBusinessModel> prestations = new HashMap<String, PaiementBusinessModel>();

        for (PaiementBusinessModel item : listeRecap) {

            PaiementBusinessModel prest = prestations.get(item.getNssAllocataire());
            if (prest == null) {
                prest = new PaiementBusinessModel(null, null, null, item.getSoldeInitial(),
                        item.getIdTiersAllocataire(), item.getNomAllocataire(), item.getNssAllocataire(), null, null,
                        false, 0);
            }

            if (!item.isRestitution()) {
                prest.addMontant(item.getMontant());
            }

            if (prest.getMontant().compareTo(new BigDecimal("0")) != 0) {
                prestations.put(prest.getNssAllocataire(), prest);
            }
        }

        ArrayList<PaiementBusinessModel> listPrest = new ArrayList<PaiementBusinessModel>(prestations.values());
        Collections.sort(listPrest);

        return buildCSVMontantSuperieurALimite(listPrest, limite, params);
    }

    /**
     * Initialise les info et les en-têtes de la liste de prestation détaillée par allocataire
     * 
     * @param params
     *            Map contenant les informations du traitement/processus en cours
     * @return Chaine en format CSV des infos et en-têtes
     */
    private StringBuilder initHeaderListeAllocataire(HashMap<String, String> params) {

        StringBuilder csvString = new StringBuilder();

        csvString
                .append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.commun.info.processus.label")).append(" : ")
                .append(params.get(ALConstProtocoles.INFO_PROCESSUS)).append(";;;");
        csvString
                .append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.commun.info.traitement.label")).append(" : ")
                .append(params.get(ALConstProtocoles.INFO_TRAITEMENT)).append("\n");
        csvString
                .append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.commun.info.utilisateur.label")).append(" : ")
                .append(params.get(ALConstProtocoles.INFO_UTILISATEUR)).append(";;;");
        csvString
                .append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.commun.info.passage.label")).append(" : ")
                .append(params.get(ALConstProtocoles.INFO_PASSAGE)).append(";\n");
        csvString
                .append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.commun.info.dateheure.label")).append(" : ")
                .append(params.get(ALConstProtocoles.INFO_DATEHEURE)).append(";;;");
        csvString
                .append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.commun.info.periode.label")).append(" : ")
                .append(params.get(ALConstProtocoles.INFO_PERIODE)).append(";\n\n\n");

        csvString.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.allocataire.entete")).append(";");
        csvString.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.soldeInitial.entete")).append(";");
        csvString.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.debit.entete")).append(";");
        csvString.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.credit.entete")).append(";");
        csvString.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.nouveauSolde.entete")).append(";");
        csvString.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.ordreVersement.entete")).append(";\n");
        csvString.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.protocoles.paiementDirect.detail.verseCompensation.entete")).append("\n\n");

        return csvString;
    }
}