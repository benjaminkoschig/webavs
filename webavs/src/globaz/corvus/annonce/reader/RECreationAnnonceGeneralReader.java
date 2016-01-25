package globaz.corvus.annonce.reader;

import globaz.corvus.annonce.REIllegalNSSFormatException;
import globaz.corvus.annonce.RENSS;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Classe utilitaire pour la création d'annonce.
 * S'occupe de lire et convertir les valeurs provenant des écrans, fichier plat, fichiers xml, etc
 * et de typer les valeurs pour la création d'annonce
 * 
 * D'autres classe 'reader' dédiées existent pour la lecture des champs des RERenteAccordee, REbaseDeCalcul, etc
 * 
 * @see REBaseDeCalcul9EmeRevisionReader
 * @see RERenteAccordeeReader
 * 
 * 
 * 
 * @author lga
 * 
 */
// TODO compléter la docs
public class RECreationAnnonceGeneralReader extends REAbstractBEntityValueReader {

    public Integer convertNumeroCaisse(String numeroCaisse) {
        return convertToInteger(numeroCaisse);
    }

    public Integer convertNumeroAgence(String numeroAgence) {
        return convertToInteger(numeroAgence);
    }

    public RENSS convertFormatedNss(String nssFormate) throws REIllegalNSSFormatException {
        RENSS value = null;
        if (!JadeStringUtil.isBlank(nssFormate)) {
            value = RENSS.convertFormattedNSS(nssFormate);
        }
        return value;
    }

    public Integer convertCantonEtatDomicile(String codeCanton) {
        return convertToInteger(codeCanton);
    }

    public Integer convertMensualitePrestationsFrancs(String mensualitePrestationsFrancs) {
        Integer result = null;
        if (mensualitePrestationsFrancs.contains(".")) {
            String[] values = mensualitePrestationsFrancs.split("\\.");
            result = convertToInteger(values[0]);
        } else {
            result = convertToInteger(mensualitePrestationsFrancs);
        }
        return result;
    }

    public Integer convertGenreDroitAPI(String code) {
        return convertToInteger(code);
    }

    public Integer convertDureeAjournement(String dureeAjournement) {
        return convertToInteger(dureeAjournement);
    }

}
