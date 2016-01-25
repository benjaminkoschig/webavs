package globaz.ij.acor.parser.rev3;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.acor.adapter.IJAttestationsJoursAdapter;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.tools.PRCalcul;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * un parser qui parse le fichier annonce.pay de globaz.ij.acor et en extrait les prestations (IJPrestation)
 * </p>
 * 
 * @author vre
 * @see globaz.ij.db.prestations.IJPrestation
 */
public class IJACORPrestationParser extends IJACORAbstractFlatFileParser {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * ajouter deux nombres codes en string.
     * 
     * @param op1
     * @param op2
     * 
     * @return
     */
    private static final String add(String op1, String op2) {
        FWCurrency currency = new FWCurrency(op1);

        currency.add(op2);

        return currency.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * multiplie deux nombres, arrondit le resultat a 2 decimales et 5 centimes et retourne cette valeur en tant que
     * chaine.
     * 
     * @param op1
     * @param op2
     * 
     * @return
     */
    private static final String multiply(String op1, String op2) {

        if (JadeStringUtil.isEmpty(op1)) {
            op1 = "0.0";
        }
        if (JadeStringUtil.isEmpty(op2)) {
            op2 = "0.0";
        }

        return JANumberFormatter.format(PRCalcul.multiply(op1, op2), 0.05, 2, JANumberFormatter.NEAR);
    }

    /**
     * Parse le fichier annonce.pay (reader) et insere dans la base les prestations qui s'y trouvent.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param ijCalculee
     *            DOCUMENT ME!
     * @param reader
     *            DOCUMENT ME!
     * 
     * @return une liste jamais nulle, peut-etre vide des prestations qui ont ete importees.
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final List parse(BSession session, IJPrononce prononce, IJBaseIndemnisation baseIndemnisation,
            IJIJCalculee ijCalculee, Reader reader) throws PRACORException {
        LinkedList retValue = new LinkedList();
        BufferedReader bufferedReader = new BufferedReader(reader);
        HashMap fields;

        loadConfigurations();

        try {
            String line = bufferedReader.readLine();
            String indemniteExt = "0.0", indemniteInt = "0.0";
            IJAttestationsJoursAdapter attestationsJours = new IJAttestationsJoursAdapter(baseIndemnisation, ijCalculee);

            do {
                // HACK: comme ACOR ne donne que le montant total, on recherche
                // les montants int./ext. pour recalculer
                if (line.startsWith(CODE_PRESTATION)) {
                    fields = getConfiguration(CODE_PRESTATION);

                    if (PRACORConst.CA_TYPE_MESURE_INTERNE.equals(getField(line, fields, "SUPPLEMENT_READAPTATION"))) {
                        indemniteInt = getField(line, fields, "MONTANT_INDEMNITE");
                    } else {
                        indemniteExt = getField(line, fields, "MONTANT_INDEMNITE");
                    }
                }

                // importer le decompte
                if (line.startsWith(CODE_DECOMPTE)) {
                    fields = getConfiguration(CODE_DECOMPTE);

                    // ne se preoccuper que du montant global et pas des
                    // cotisations ou impots
                    if (PRACORConst.CA_MONTANT_GLOBAL.equals(getField(line, fields, "TYPE_MONTANT"))) {
                        // creer une sous-prestation
                        IJPrestation prestation = new IJPrestation();

                        retValue.add(prestation);

                        // on recopie les montants journaliers
                        prestation.setMontantJournalierExterne(indemniteExt);
                        prestation.setMontantJournalierInterne(indemniteInt);

                        prestation.setDateDebut(getDate(line, fields, "DEBUT_PERIODE"));
                        prestation.setDateFin(getDate(line, fields, "FIN_PERIODE"));
                        prestation.setIdIJCalculee(ijCalculee.getIdIJCalculee());
                        prestation.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
                        prestation
                                .setMontantBrutExterne(multiply(indemniteExt, attestationsJours.getNbJoursExternes()));
                        prestation
                                .setMontantBrutInterne(multiply(indemniteInt, attestationsJours.getNbJoursInternes()));
                        prestation.setNombreJoursExt(attestationsJours.getNbJoursExternes());
                        prestation.setNombreJoursInt(attestationsJours.getNbJoursInternes());
                        prestation.setDateDecompte(JACalendar.todayJJsMMsAAAA());
                        prestation.setMontantBrut(add(prestation.getMontantBrut(), getField(line, fields, "MONTANT")));

                        // sauver la sous prestation dans la base
                        prestation.setSession(session);
                        prestation.add();
                    }
                }

                line = bufferedReader.readLine();
            } while (line != null);
        } catch (Exception e) {
            throw new PRACORException("impossible de parser", e);
        }

        return retValue;
    }

    /**
     * Crée une nouvelle instance de la classe IJACORPrestationParser.
     */
    private IJACORPrestationParser() {
    }
}
