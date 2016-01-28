package globaz.osiris.db.ordres.format.opt;

import globaz.jade.client.util.JadeStringUtil;

public class CAOPAE27Banque extends CAOPAEServiceInterne {

    private static String __formatNumeroClearingOrBIC(String clearing, String monnaieBonnification) throws Exception {
        if (!"CHF".equals(monnaieBonnification)) {
            throw new Exception("La monnaie de bonnification doit être CHF");
        }
        return "  " + JadeStringUtil.leftJustify(JadeStringUtil.leftJustify(clearing, 6, '0'), 13);
    }

    private static String __formatNumeroCompteBancaire(String compte) {
        return JadeStringUtil.fillWithSpaces(compte, 35);
    }

    @Override
    protected void _build(long nrTx, StringBuffer buffer, CAOperationAdrPmt data, String nomPrenom, String complement,
            String rue, String npa, String lieu) throws Exception {

        String numeroClearingOrBIC = CAOPAE27Banque.__formatNumeroClearingOrBIC(data.getClearing(),
                data.getCodeIsoMonnaieBonification());
        String compteBancaire = CAOPAE27Banque.__formatNumeroCompteBancaire(data.getCompte());

        // compte
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 15, numeroClearingOrBIC); // pos 73
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, compteBancaire); // pos 88
        // destinataire
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 123
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 158
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 193
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 10); // pos 228
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 25); // pos 238
        // béneficiaire
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, nomPrenom); // pos 263
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, complement); // pos 298
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, rue); // pos 333
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 10, npa); // pos 368
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 25, lieu); // pos 378
    }

}
