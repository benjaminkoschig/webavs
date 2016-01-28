package globaz.osiris.db.ordres.format.opt;

import globaz.globall.util.JACCP;
import globaz.jade.client.util.JadeStringUtil;

public class CAOPAE28PosteReferenceBVR extends CAOPAEServiceInterne {

    @Override
    protected void _build(long nrTx, StringBuffer buffer, CAOperationAdrPmt data, String nomPrenom, String complement,
            String rue, String npa, String lieu) throws Exception {

        // Formater le numéro d'adhérent de la manière : 99-ZZZZZ9-C
        String numAdherent = JACCP.formatNoDash(data.getCcp());
        String referenceBvr = JadeStringUtil.rightJustify(data.getReferenceBvr(), 27, '0');

        // Reference expéditeur
        String referenceExpediteur;
        if (nomPrenom.length() >= 35) {
            referenceExpediteur = nomPrenom.substring(0, 35);
        } else {
            referenceExpediteur = nomPrenom;
        }

        // compte
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 2); // pos 73
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 9, numAdherent); // pos 75
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 27, referenceBvr); // pos 84
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, referenceExpediteur); // pos 111
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 257); // pos 146
    }

}
