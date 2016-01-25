package globaz.osiris.db.ordres.format.opt;

import globaz.globall.util.JACCP;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

public abstract class CAOPAEServiceInterne {
    public static String __formatCommunication(String motifOrdreVersement) {
        return JadeStringUtil.fillWithSpaces(CAOPAEServiceInterne.__getMotifFormatOPAE(motifOrdreVersement), 3 * 35);
    }

    private static String __formatComplementNom(String designation1Adr, String designation2Adr, String designation3Tiers) {
        // String designation1Adr n'est utilisé que pour savoir si on a une adresse complête (avec designation)
        String complement = "";
        if (!JadeStringUtil.isEmpty(designation1Adr)) {
            complement = designation2Adr; // si designation1Adr n'est pas vide, on prends la 2ème ligne de l'adresse
                                          // comme complément
        } else {
            complement = designation3Tiers;
        }
        return JadeStringUtil.fillWithSpaces(complement, 35);
    }

    private static String __formatDateAAMMJJ(String date) throws Exception {
        String sDate = new JADate(date).toStrAMJ();
        return sDate.substring(2);
    }

    private static String __formatLieu(String localite) {
        return JadeStringUtil.fillWithSpaces(localite, 25);
    }

    private static String __formatMontant(String montant) throws Exception {
        if (Double.parseDouble(montant) >= 100000000000.0) {
            // préventif
            throw new Exception("Montant trop élévé. [" + montant + "]");
        }
        List<String> parts = JadeStringUtil.tokenize(montant, ".");
        Integer.parseInt(parts.get(0)); // ensure numéric
        int centime = Integer.parseInt(parts.get(1)); // ensure numéric
        if (centime > 99) {
            throw new Exception("Problème dans le montant");
        }
        return JadeStringUtil.rightJustify(parts.get(0), 11, '0') + parts.get(1);
    }

    private static String __formatNomPrenom(String designation1Adr, String designation1Tiers, String designation2Tiers) {
        if (!JadeStringUtil.isEmpty(designation1Adr)) {
            return JadeStringUtil.fillWithSpaces(designation1Adr, 35);
        } else {
            return JadeStringUtil.fillWithSpaces(designation1Tiers + " " + designation2Tiers, 35);
        }
    }

    private static String __formatNPA(String npa, String paysIso) {
        if ("CH".equals(paysIso)) {
            npa = npa.substring(0, 4);
        }

        return JadeStringUtil.fillWithSpaces(npa, 10);
    }

    private static String __formatRue(String rue) {
        return JadeStringUtil.fillWithSpaces(rue, 35);
    }

    public static String __getMotifFormatOPAE(String motifOpae) {
        String motif = CAOPAEServiceInterne._removeBlankLines(motifOpae);
        if (motif.length() >= 13) {
            // Contrôler que le début du numéro ressemble au format AVS
            String d = motif.substring(0, 13);
            // Si c'est le cas on prend le motif tel quel
            if ((d.charAt(3) == '.') && (d.charAt(6) == '.')) {
                return motif;
            } else {
                return CAOPAEServiceInterne.__getMotifFormatOPAEBloc2(motifOpae);
            }
        } else {
            return CAOPAEServiceInterne.__getMotifFormatOPAEBloc2(motifOpae);
        }
    }

    /**
     * Renovis le motif décalé sur le bloc 2.
     * 
     * @return
     */
    private static String __getMotifFormatOPAEBloc2(String motifOpae) {
        // Sinon on part au bloc de communication 2
        String motif = new String();
        for (int i = 0; i <= 34; i++) {
            motif = motif + " ";
        }

        String tmp = CAOPAEServiceInterne._removeBlankLines(motifOpae);
        if (tmp.length() > 104) {
            motif = motif + tmp.substring(0, 104);
        } else {
            motif = motif + tmp.substring(0, tmp.length());
        }

        return motif;
    }

    private static String _removeBlankLines(String line) {
        if (line != null) {
            String tmp = JadeStringUtil.change(line, "\r\n", " ");
            return JadeStringUtil.change(tmp, "\n", " ");
        } else {
            return line;
        }
    }

    protected static void _secureAppend(long nrTx, StringBuffer line, int i) {
        // nrTx is not used in that case, keep it anyway to be similar to the other _secureAppend method
        line.append(JadeStringUtil.createBlankString(i));
    }

    protected static void _secureAppend(long nrTx, StringBuffer line, int expectedNbChar, String str) throws Exception {
        // str = JadeStringUtil.convertSpecialChars(str);
        if (str.length() != expectedNbChar) {
            // préventif, ne devrait jamais arriver
            throw new Exception("Transaction nr : " + nrTx + " - Invalid number of charater for string [" + str
                    + "] found : " + str.length() + " expected : " + expectedNbChar + " in string :" + str);
        }
        line.append(str);

    }

    private static void _validatePaysIsoForServiceInterieur(String paysIso) throws Exception {
        if (!(("CH".equals(paysIso) || ("LI".equals(paysIso))))) {
            throw new Exception("Le pays doit être CH ou LI");
        }
    }

    public static String buildRecord97(long nrTx, String codeIsoMonnaieBonification, CAOPAEState state)
            throws Exception {
        StringBuffer buffer = new StringBuffer();
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 3, codeIsoMonnaieBonification); // pos 51
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 6,
                JadeStringUtil.rightJustify(state.getNbTransation() + "", 6, '0'));
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 13,
                CAOPAEServiceInterne.__formatMontant(state.getSomme().toString()));

        return buffer.toString() + JadeStringUtil.copies("0", 14 * 22) + JadeStringUtil.createBlankString(320);
    }

    public static String buildSecteurControl(long nrTx, String dateEcheance, String compteDebit, String compteTaxe,
            String numeroOrdre, int genreTransaction) throws Exception {

        String dateEchAAMMJJ = CAOPAEServiceInterne.__formatDateAAMMJJ(dateEcheance);

        StringBuffer buffer = new StringBuffer();
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 3, "036"); // pos 1
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 6, dateEchAAMMJJ); // pos 4
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 5, "00000"); // pos 10
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 1, "1"); // pos 15
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 9, JACCP.formatNoDash(compteDebit)); // pos 16
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 9, JACCP.formatNoDash(compteTaxe)); // pos 25
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 2, (Integer.parseInt(numeroOrdre) > 9) ? numeroOrdre : "0"
                + Integer.parseInt(numeroOrdre)); // pos 34
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 2, JadeStringUtil.rightJustify(genreTransaction + "", 2, '0')); // pos
                                                                                                                         // 36
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 6, JadeStringUtil.rightJustify(nrTx + "", 6, '0')); // pos 38
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 2, "00"); // pos 44
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 1, "0"); // pos 46
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 4, "0000"); // pos 47
        return buffer.toString();

    }

    protected void _build(long nrTx, StringBuffer line, CAOperationAdrPmt data, String nomPrenom, String complement,
            String rue, String npa, String lieu) throws Exception {

    }

    public final String buildLine(long nrTx, CAOperationAdrPmt data) throws Exception {
        StringBuffer buffer = new StringBuffer();
        /*
         * validations
         */
        // CAOPAEServiceInterne._validatePaysIsoForServiceInterieur(data.getPaysIso());

        /*
         * formatage
         */
        String montantDepot = CAOPAEServiceInterne.__formatMontant(data.getMontant());

        String nomPrenom = CAOPAEServiceInterne.__formatNomPrenom(data.getDesignation1_adr(),
                data.getDesignation1_tiers(), data.getDesignation2_tiers());
        String complement = CAOPAEServiceInterne.__formatComplementNom(data.getDesignation1_adr(),
                data.getDesignation2_adr(), data.getDesignation3_tiers());
        String rue = CAOPAEServiceInterne.__formatRue(data.getRue() + " " + data.getNumero());
        String npa = CAOPAEServiceInterne.__formatNPA(data.getNpa(), data.getPaysIso());
        String lieu = CAOPAEServiceInterne.__formatLieu(data.getLocalite());
        String communication = CAOPAEServiceInterne.__formatCommunication(data.getMotifOrdreVersement());

        /*
         * creation de la ligne
         */
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 3, data.getCodeIsoMonnaieDepot()); // pos 51
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 13, montantDepot); // pos 54
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 1); // pos 67
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 3, data.getCodeIsoMonnaieBonification()); // pos 68

        String p = "CH";
        if ("LI".equals(data.getPaysIso())) {
            p = "LI";
        }
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 2, p); // pos 71
        // compte
        _build(nrTx, buffer, data, nomPrenom, complement, rue, npa, lieu);

        // communications
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 403 (globaz n'utilise pas le champ 1 de
                                                              // communication...)
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 3 * 35, communication); // pos 438,473,508
        // réserve
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 3); // pos 543
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 1); // pos 546
        // donneur d'ordre
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 547
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 582
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 35); // pos 617
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 10); // pos 652
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 25); // pos 662
        // réserve
        CAOPAEServiceInterne._secureAppend(nrTx, buffer, 14); // pos 687

        return buffer.toString();

    }
}
