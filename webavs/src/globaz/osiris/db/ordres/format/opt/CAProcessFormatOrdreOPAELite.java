package globaz.osiris.db.ordres.format.opt;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.format.CAOrdreFormateur;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.process.CAProcessOrdre;
import globaz.pyxis.util.TIToolBox;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CAProcessFormatOrdreOPAELite extends CAOrdreFormateur {

    final private Map<Integer, Integer> nbTransParGenre = new HashMap<Integer, Integer>(); // pour test

    private static CAOPAEServiceInterne _getBuilder(int genre) {
        switch (genre) {
            case 22:
                return new CAOPAE22Poste();
            case 24:
                return new CAOPAE24MandatPmt();
            case 27:
                return new CAOPAE27Banque();
            case 28:
                return new CAOPAE28PosteReferenceBVR();
            default:
                return null;
        }
    }

    // @formatter:off
	/*
	 * 
	 * Determine le genre d'enregistrement opae pays (adresse omt) | CCP | IDTIERSBANQUE
	 * -------------------------------------------------+-------+---------------- 22 service intérieur (BV) CH | 1 |
	 * 0..1 32 Giro poste (PGI) étanger | 1 | 0..1
	 * 
	 * 27 pmt clearing CH | 0 | 1 37 Giro banque (BGI) étranger | 0 | 1
	 * 
	 * 24 mandat pmt (MPT) CH | 0 | 0 34 Cash inter (PCA) étranger | 0 | 0
	 * 
	 * 
	 * Non utilisé : 28 BVR CH
	 */
	// @formatter:on
    public static int findGenre(CAOperationAdrPmt data) {
        boolean hasCCP = !JadeStringUtil.isEmpty(data.getCcp());
        boolean hasTiersBanque = !JadeStringUtil.isIntegerEmpty(data.getIdTiersBanque());
        boolean hasReferenceBVR = !JadeStringUtil.isBlankOrZero(data.getReferenceBvr());

        if (hasCCP && hasReferenceBVR) {
            // Bulletin de versement avec numéro de référence
            return 28;
        } else if (hasCCP && !hasReferenceBVR) {
            // boolean serviceInterieur = "CH".equals(data.getPaysIso()); // tiers benef en suisse ?
            // return (serviceInterieur) ? 22 : 32;
            // paiement sur compte jaune du service intérieur
            return 22;
        } else if (hasTiersBanque) {

            // pour avoir le même comportement qu'actuellement
            // un pmt sur une banqaue a l'étranger sera donc refusée par la poste.
            // il faudra donc :
            // soit géré les 37
            // soit que la compta fasse une erreur "plus tôt" pour ne pas générer cette ordre
            return 27;
        } else {
            boolean serviceInterieur = "CH".equals(data.getPaysIso()); // tiers benef en suisse ?
            return (serviceInterieur) ? 24 : 34;
        }
    }

    public static String getSQLBody(String idOrdreGroupe) {
        String col = TIToolBox.getCollection();

        return "from " + col + "CAOPOVP ov " + "inner join " + col + "CAOPERP op on (op.idoperation = ov.idordre)"
                + " inner join " + col + "TIAPAIP ap on (ap.HCIAIU = ov.IDADRESSEPAIEMENT)" + " inner join " + col
                + "TIADRPP pm on (pm.HIIAPA = ap.HIIAPA)" + " inner join " + col
                + "TIADREP ab on (ab.HAIADR = pm.HAIADR)" + " inner join " + col
                + "TILOCAP lb on (lb.HJILOC = ab.HJILOC)" + " inner join " + col
                + "TIPAYSP pb on (pb.HNIPAY = lb.HNIPAY)" + " left outer join " + col
                + "TITIERP tb on (tb.htitie = pm.HITIAD)" + " left outer join " + col
                + "TIBANQP bq on (bq.htitie = pm.HTITIE)" + " left outer join " + col
                + "TITIERP tbq on (bq.htitie = tbq.HTITIE)"

                + " WHERE ov.IDORDREGROUPE= " + idOrdreGroupe + " AND ov.ESTRETIRE<>'1' AND ov.ESTBLOQUE<>'1'";
    }

    public static String getSQLFields() {
        return "pm.HINCBA adrPmtCompte," + "pm.HICCP adrPmtCCP," + "bq.HTITIE bqIdTiers," + "bq.HUCLEA bqClearing,"
                + "bq.HUNMJ bqNumMiseAjour," + "tbq.HNIPAY BQIDPAYS," + "tb.HTLDE1 beneDesi1," + "tb.HTLDE2 beneDesi2,"
                + "tb.HTLDE3 beneDesi3," + "tb.HTTLAN beneLangue," + "ab.HAADR1 adrLigne1," + "ab.HAADR2 adrLigne2,"
                + "ab.HARUE adrRue," + "ab.HANRUE adrNumRue," + "ab.HACPOS adrCP," + "lb.HJNPA locHJNPA,"
                + "lb.HJLOCA locHJLOCA," + "pb.HNCISO paysISO," + "op.MONTANT opMONTANT,"
                + "ov.CODEISOMONDEP ovCODEISOMONDEP," + "ov.CODEISOMONBON ovCODEISOMONBON,"
                + "ov.NUMTRANSACTION ovNUMTRANSACTION," + "ov.MOTIF ovMOTIF" + ", ov.referencebvr ovREFERENCEBVR";
    }

    @Override
    public void executeOrdreVersement(CAOrdreGroupe og, final CAProcessOrdre context) throws Exception {
        final boolean wantNewLine = getInsertNewLine();
        IntAdressePaiement apTaxes = og.getOrganeExecution().getAdresseDebitTaxes();
        IntAdressePaiement apDebit = og.getOrganeExecution().getAdressePaiement();

        if (apTaxes == null) {
            throw new Exception("Error : adresse debit taxe is null ! (organe execution: "
                    + og.getOrganeExecution().getNom() + " - OG: " + og.getIdOrdreGroupe() + ")");
        }

        if (apDebit == null) {
            throw new Exception("Error : adresse paiement is null ! (organe execution: "
                    + og.getOrganeExecution().getNom() + " - OG: " + og.getIdOrdreGroupe() + ")");
        }

        final String compteDebit = apDebit.getNumCompte();
        final String compteTaxe = apTaxes.getNumCompte();
        final String numeroOrdre = og.getNumeroOG();
        final String dateEcheance = og.getDateEcheance();

        final PrintWriter writer = getPrintWriter();

        /*
         * Record 00 - Entete
         */
        writer.write(CAOPAEServiceInterne
                .buildSecteurControl(0L, dateEcheance, compteDebit, compteTaxe, numeroOrdre, 0));
        writer.write(JadeStringUtil.createBlankString(650));
        if (wantNewLine) {
            writer.write(CAOrdreFormateur.CRLF);
        }

        final CAOPAEState state = new CAOPAEState();

        // Calculer le nombre à exécuter
        long count = CASQL.count(og.getSession(), CAProcessFormatOrdreOPAELite.getSQLBody(og.getIdOrdreGroupe()));
        if (context != null) {
            context.setState(og.getSession().getLabel("6113"));
            context.setProgressScaleValue(count);
            context.setProgressDescription("OPAE");
        }

        long nbRows = CASQL.cursor(og.getSession(), CAProcessFormatOrdreOPAELite.getSQLFields(),
                CAProcessFormatOrdreOPAELite.getSQLBody(og.getIdOrdreGroupe()) + " ORDER BY ov.NUMTRANSACTION",
                new CASQL.EachRow() {

                    @Override
                    public void eachRow(Map<String, String> row, long rowNumber) throws Exception {

                        // Vérifier le contexte d'exécution
                        if ((context != null) && context.isAborted()) {
                            throw new CAAbortException();
                        }

                        CAOperationAdrPmt data = CAOperationAdrPmt.adapt(row);
                        long nrTx = Long.parseLong(data.getNumeroTransaction());
                        int genre = CAProcessFormatOrdreOPAELite.findGenre(data);
                        CAOPAEServiceInterne opaeLineBuilder = CAProcessFormatOrdreOPAELite._getBuilder(genre);

                        if (opaeLineBuilder != null) {
                            /*
                             * Ce genre de transaction opae est supporté, on traite le versement.
                             */
                            FWCurrency montant = new FWCurrency(data.getMontant());
                            String secteurCtrl = CAOPAEServiceInterne.buildSecteurControl(nrTx, dateEcheance,
                                    compteDebit, compteTaxe, numeroOrdre, genre);
                            String ordre = opaeLineBuilder.buildLine(nrTx, data);
                            writer.write(secteurCtrl + ordre);
                            if (wantNewLine) {
                                writer.write(CAOrdreFormateur.CRLF);
                            }
                            // tout est ok, on update les compteurs en vue de la construction du record de fin de genre
                            // 97
                            state.incNbTransation();
                            state.addToSomme(montant);
                        } else {
                            /*
                             * Ce genre de transaction opae n'est pas supporté
                             */
                            System.out.println("Erreur sur la transaction n°" + nrTx
                                    + " : Les transactions OPAE de genre " + genre + " ne sont pas supportées.");
                        }

                        /*
                         * Alimente le compteur du nombre de transaction par genre
                         */
                        Integer v = nbTransParGenre.get(genre);
                        if (v == null) {
                            nbTransParGenre.put(genre, 1);
                        } else {
                            nbTransParGenre.put(genre, v.intValue() + 1);
                        }

                        // Progression
                        if (context != null) {
                            context.incProgressCounter();
                        }

                    } // fin each row
                });

        /*
         * record 97 - Fin du fichier
         */
        writer.write(CAOPAEServiceInterne.buildSecteurControl((nbRows + 1), dateEcheance, compteDebit, compteTaxe,
                numeroOrdre, 97));
        writer.write(CAOPAEServiceInterne.buildRecord97((nbRows + 1), "CHF", state));

    }

    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {
        return null; // cette implémentation n'utilise pas cette méthode...
    }

    @Override
    public StringBuffer format(CAOperationOrdreRecouvrement or) throws Exception {
        // cette implémentation n'utilise pas cette méthode...
        // préventif, ne devrait jamais arriver.
        throw new Exception("La classe " + this.getClass().getName() + " n'est pas utilisable pour les recouvrements.");
    }

    @Override
    public StringBuffer formatEOF(APIOrdreGroupe og) throws Exception {
        return null; // cette implémentation n'utilise pas cette méthode...
    }

    @Override
    public StringBuffer formatHeader(APIOrdreGroupe og) throws Exception {
        return null; // cette implémentation n'utilise pas cette méthode...
    }
}
