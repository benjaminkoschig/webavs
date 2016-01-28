package globaz.osiris.db.ordres.format.opt;

public class CAOPAE24MandatPmt extends CAOPAEServiceInterne {

    @Override
    protected void _build(long nrTx, StringBuffer buffer, CAOperationAdrPmt data, String nomPrenom, String complement,
            String rue, String npa, String lieu) throws Exception {

        // compte
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 15); // pos 73
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 34); // pos 88
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 1); // pos 122 - pas de mention payé perso.
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
