package ch.globaz.al.businessimpl.services.compensation;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.prestations.ALPaiementPrestationException;
import ch.globaz.al.business.services.compensation.ProtocoleCSVCompensation;

public class ProtocoleCSVCompensationImpl implements ProtocoleCSVCompensation {

    private String buildListeAfffilieCSV(String activite, Collection<CompensationBusinessModel> recapsForOneActivite) {

        StringBuffer csv = new StringBuffer();

        Iterator it = recapsForOneActivite.iterator();

        while (it.hasNext()) {
            CompensationBusinessModel recap = (CompensationBusinessModel) it.next();

            csv.append(recap.getNomAffilie() + " - " + recap.getNumeroAffilie()).append(";");
            csv.append(recap.getIdRecap()).append(";");
            csv.append(JadeCodesSystemsUtil.getCodeLibelle(activite)).append(";");
            csv.append(recap.getPeriodeRecapDe() + " - " + recap.getPeriodeRecapA()).append(";");
            csv.append(recap.getMontant().toPlainString()).append("\n");

        }

        return csv.toString();

    }

    @Override
    public String getCSV(HashMap<String, Collection<CompensationBusinessModel>> recapsByActivites,
            HashMap<String, String> params) throws JadeApplicationException {

        StringBuffer csv = new StringBuffer();

        csv.append("Processus : ").append(params.get(ALConstProtocoles.INFO_PROCESSUS)).append(";");
        csv.append(";");
        csv.append(";");
        csv.append("Traitement : ").append(params.get(ALConstProtocoles.INFO_TRAITEMENT));
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

        csv.append("Nom").append(";");
        csv.append("N° récap").append(";");
        csv.append("Type ayant-droit").append(";");
        csv.append("Période").append(";");
        csv.append("Montant").append(";");

        csv.append("\n");
        csv.append("\n");

        if (recapsByActivites == null) {
            throw new ALPaiementPrestationException("ProtocoleCSVCompensationImpl#getCSV : listePrestations is null");
        }

        for (String activite : recapsByActivites.keySet()) {
            HashMap<String, CompensationBusinessModel> prestations = new HashMap<String, CompensationBusinessModel>();
            for (CompensationBusinessModel item : recapsByActivites.get(activite)) {
                CompensationBusinessModel prest = prestations.get(item.getIdRecap());
                if (prest == null) {
                    prest = new CompensationBusinessModel(item.getIdRecap(), item.getNomAffilie(),
                            item.getNumeroAffilie(), item.getNumeroFacture(), item.getNumeroCompte(),
                            item.getPeriodeRecapDe(), item.getPeriodeRecapA(), item.getGenreAssurance());

                }

                prest.addMontant(item.getMontant().toString());

                if (prest.getMontant().compareTo(new BigDecimal("0")) != 0) {
                    prestations.put(prest.getIdRecap(), prest);
                }
            }

            ArrayList<CompensationBusinessModel> listPrest = new ArrayList<CompensationBusinessModel>(
                    prestations.values());
            Collections.sort(listPrest);

            csv.append(buildListeAfffilieCSV(activite, listPrest));

        }

        return csv.toString();

    }
}
