package globaz.osiris.db.ordres.format.opt;

import globaz.globall.util.JACCP;

public class CAOPAE22Poste extends CAOPAEServiceInterne {

    private static String __formatCCP(String ccp) throws Exception {
        return JACCP.formatNoDash(ccp);
    }

    @Override
    protected void _build(long nrTx, StringBuffer buffer, CAOperationAdrPmt data, String nomPrenom, String complement,
            String rue, String npa, String lieu) throws Exception {

        String ccp = CAOPAE22Poste.__formatCCP(data.getCcp());

        // compte
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 9, ccp); // pos 73
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 6); // pos 82
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 88 limitation pas de check si ccp d'une
                                                              // banque...(ce qui rendrait ce champ obligatoire...)
        // destinataire
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, nomPrenom); // pos 123
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, complement); // pos 158
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35, rue); // pos 193
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 10, npa); // pos 228
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 25, lieu); // pos 238
        // beneficiaire
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 263
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 298
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 333
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 10); // pos 368
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 25); // pos 378
    }

}
