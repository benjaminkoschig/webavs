package globaz.ij.acor.parser.rev3;

import globaz.globall.db.BSession;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * un classe qui parse un fichier globaz.ij.acor annonce.pay pour en extraire les bases de calcul (IJIJCalculee) et les
 * prestations (IJIndemnitesJournalieres)
 * </p>
 * 
 * @author vre
 * @see globaz.ij.db.prestations.IJIJCalculee
 * @see globaz.ij.db.prestations.IJIndemniteJournaliere
 */
public class IJACORBasesCalculParser extends IJACORAbstractFlatFileParser {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * parse un fichier annonce.pay et insere les IJIJCalculee et IJIndemniteJournaliere detectees dans le fichier.
     * 
     * @param session
     *            non null
     * @param prononce
     *            non null
     * @param reader
     *            non null, peut-etre vide
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final List parse(BSession session, IJPrononce prononce, Reader reader) throws PRACORException {
        LinkedList retValue = new LinkedList();
        BufferedReader bufferedReader = new BufferedReader(reader);
        HashMap fields;

        loadConfigurations();

        try {
            String line = bufferedReader.readLine();

            // recherche des bases de calcul
            while (line != null) {
                if (line.startsWith(CODE_BASE_CALCUL)) {
                    fields = getConfiguration(CODE_BASE_CALCUL);

                    // creation de l'ij calculee
                    IJIJCalculee ijCalculee;

                    if (PRACORConst.CA_TYPE_IJ_GRANDE.equals(getField(line, fields, "GENRE_INDEMNITE"))) {
                        IJGrandeIJCalculee grandeIJ = new IJGrandeIJCalculee();

                        // champs specifiques à la grande ij
                        grandeIJ.setMontantIndemniteEnfant(getField(line, fields, "MONTANT_ENFANTS"));
                        grandeIJ.setNbEnfants(getField(line, fields, "NB_ENFANTS"));
                        grandeIJ.setMontantIndemniteAssistance(getField(line, fields, "MONTANT_ASSISTANCE"));
                        grandeIJ.setMontantIndemniteExploitation(getField(line, fields, "MONTANT_EXPLOITATION"));

                        ijCalculee = grandeIJ;
                    } else {
                        IJPetiteIJCalculee petiteIJ = new IJPetiteIJCalculee();

                        /*
                         * champs specifiques à la petite IJ il est probable que le champ FORMATION (ACOR=$b(21)) soit
                         * celui qu'on cherche ici mais dans le doute on reprend la valeur d'origine
                         */
                        petiteIJ.setCsModeCalcul(((IJPetiteIJ) prononce).getCsSituationAssure());

                        ijCalculee = petiteIJ;
                    }

                    // champs communs a petite et grande ij
                    ijCalculee.setNoAVS(getField(line, fields, "NUMERO_AVS"));
                    ijCalculee.setOfficeAI(getField(line, fields, "OFFICE_AI"));
                    ijCalculee.setCsGenreReadaptation(PRACORConst.caGenreReadaptationToCS(session,
                            getField(line, fields, "GENRE_READAPTATION")));
                    ijCalculee.setDatePrononce(getDate(line, fields, "DATE_PRONONCE"));
                    ijCalculee.setDateDebutDroit(getDate(line, fields, "DEBUT_DROIT"));
                    ijCalculee.setDateFinDroit(getDate(line, fields, "FIN_DROIT"));
                    ijCalculee.setCsTypeBase(PRACORConst.caTypeBaseToCS(session, getField(line, fields, "TYPE_BASE")));
                    ijCalculee.setRevenuDeterminant(getField(line, fields, "REVENU_DETERMINANT"));
                    ijCalculee.setDateRevenu(getDate(line, fields, "DATE_REVENU"));
                    ijCalculee.setMontantBase(getField(line, fields, "MONTANT_BASE"));
                    ijCalculee.setSupplementPersonneSeule(getField(line, fields, "SUPPLEMENT_SEULE"));
                    ijCalculee.setRevenuJournalierReadaptation(getField(line, fields, "REVENU_READAPTATION"));
                    ijCalculee.setCsStatutProfessionnel(PRACORConst.caStatutProfessionnelToCS(session,
                            getField(line, fields, "STATUT")));
                    ijCalculee.setPourcentDegreIncapaciteTravail(getField(line, fields, "INCAPACITE"));
                    ijCalculee.setDemiIJACBrut(getField(line, fields, "DEMI_IJ_AC_BRUTE"));

                    ijCalculee.setIdPrononce(prononce.getIdPrononce());
                    ijCalculee.setCsTypeIJ(prononce.getCsTypeIJ());
                    // insertion dans la base
                    ijCalculee.setSession(session);
                    ijCalculee.add();

                    // ajouter aux resultats
                    retValue.add(ijCalculee);

                    // recherche des prestations
                    fields = getConfiguration(CODE_PRESTATION);

                    while (((line = bufferedReader.readLine()) != null) && line.startsWith(CODE_PRESTATION)) {
                        IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();

                        if (PRACORConst.CA_TYPE_MESURE_INTERNE
                                .equals(getField(line, fields, "SUPPLEMENT_READAPTATION"))) {
                            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
                        } else {
                            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
                        }

                        // champs de prestation
                        indemniteJournaliere.setMontantSupplementaireReadaptation(getField(line, fields,
                                "MONTANT_READAPTATION"));
                        indemniteJournaliere.setMontantGarantiAANonReduit(getField(line, fields,
                                "GARANTIE_AA_NON_REDUITE"));
                        indemniteJournaliere.setIndemniteAvantReduction(getField(line, fields,
                                "INDEMNITE_AVANT_REDUCTION"));
                        indemniteJournaliere.setDeductionRenteAI(getField(line, fields, "DEDUCTION_RENTE_AI"));
                        indemniteJournaliere.setFractionReductionSiRevenuAvantReadaptation(getField(line, fields,
                                "FRACTION_REDUCTION_SI_REVENU_READAPTATION"));
                        indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation(getField(line, fields,
                                "MONTANT_REDUCTION_SI_REVENU_READAPTATION"));
                        indemniteJournaliere.setMontantJournalierIndemnite(getField(line, fields, "MONTANT_INDEMNITE"));
                        indemniteJournaliere.setMontantGarantiAAReduit(getField(line, fields, "GARANTIE_AA_REDUITE"));
                        indemniteJournaliere.setMontantComplet(getField(line, fields, "MONTANT_COMPLET"));
                        indemniteJournaliere.setMontantPlafonne(getField(line, fields, "MONTANT_PLAFONNE"));
                        indemniteJournaliere.setMontantPlafonneMinimum(getField(line, fields,
                                "MONTANT_PLAFONNE_MINIMUM"));

                        // insertion dans la base
                        indemniteJournaliere.setIdIJCalculee(ijCalculee.getIdIJCalculee());
                        indemniteJournaliere.setSession(session);
                        indemniteJournaliere.add();
                    }
                } else {
                    line = bufferedReader.readLine();
                }
            }

            return retValue;
        } catch (Exception e) {
            throw new PRACORException("impossible de parser", e);
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private IJACORBasesCalculParser() {
    }
}
