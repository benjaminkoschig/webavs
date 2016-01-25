package globaz.aquila.ts.rules;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.ts.COTSValidationRule;
import globaz.aquila.ts.opge.COTSOPGEExecutor;
import globaz.aquila.ts.rules.utils.COOPGESpellChecker;
import globaz.aquila.ts.utils.COTSTiersUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.adressecourrier.TIPays;
import java.util.StringTokenizer;

/**
 * Rules pour FER.
 * 
 * @author DDA
 */
public class COTSOPGERule implements COTSValidationRule {

    public static final String LABEL_LIGNE_IMPOSSIBLE_TROUVER_NOM = "LIGNE_IMPOSSIBLE_TROUVER_NOM";
    public static final String LABEL_TIERS_ADRESSE_NON_SUISSE = "TIERS_ADRESSE_NON_SUISSE";
    public static final String LABEL_TIERS_CARACTERES_NON_SUPPORTES = "TIERS_CARACTERES_NON_SUPPORTES";
    public static final String LABEL_TIERS_CASE_POSTAL_INTERDITE = "TIERS_CASE_POSTAL_INTERDITE";
    public static final String LABEL_TIERS_LIGNE_TROP_LONG = "TIERS_LIGNE_TROP_LONG";
    public static final String LABEL_TIERS_NOM_OBLIGATOIRE = "TIERS_NOM_OBLIGATOIRE";
    public static final String LABEL_TIERS_NOM_TROP_LONG = "TIERS_NOM_TROP_LONG";
    public static final String LABEL_TIERS_NPA_LOCALITE_TROP_LONG = "TIERS_NPA_LOCALITE_TROP_LONG";
    public static final String LABEL_TIERS_NPA_OBLIGATOIRE = "TIERS_NPA_OBLIGATOIRE";
    public static final String LABEL_TIERS_OBLIGATOIRE = "TIERS_OBLIGATOIRE";
    public static final String LABEL_TIERS_RUE_NUMERO_TROP_LONG = "TIERS_RUE_NUMERO_TROP_LONG";
    public static final String LIGNE_REMARQUE_TROP_LONGUE = "LIGNE_REMARQUE_TROP_LONGUE";

    /**
     * Test la validité d'une adresse.
     * 
     * @param session
     * @param transaction
     * @param elementJournalBatch
     * @param adresseDataSource
     * @throws Exception
     */
    private void testAdresseDataSource(BSession session, BTransaction transaction,
            COElementJournalBatch elementJournalBatch, TIAdresseDataSource adresseDataSource) throws Exception {
        if (JadeStringUtil.isBlank(adresseDataSource.localiteNpa)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_NPA_OBLIGATOIRE));
        }

        if (COTSTiersUtils.convertSpecialChars(adresseDataSource.rue + " " + adresseDataSource.numeroRue).length() > COTSOPGEExecutor.LENGTH_RUE_NUMERO) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_RUE_NUMERO_TROP_LONG));
        }

        if (COTSTiersUtils.convertSpecialChars(adresseDataSource.localiteNpa + " " + adresseDataSource.localiteNom)
                .length() > COTSOPGEExecutor.LENGTH_NPA_LOCALITE) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_NPA_LOCALITE_TROP_LONG));
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.casePostale)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_CASE_POSTAL_INTERDITE));
        }
    }

    /**
     * Test le datasource étranger pour le texte du débiteur (zone 11).
     * 
     * @param session
     * @param transaction
     * @param elementJournalBatch
     * @param adresseDataSource
     * @throws Exception
     */
    private void testAdresseDataSourceEtranger(BSession session, BTransaction transaction,
            COElementJournalBatch elementJournalBatch, TIAdresseDataSource adresseDataSourceDomicile,
            TIAdresseDataSource adresseDataSource) throws Exception {
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch,
                COTSTiersUtils.getLigneCumule(adresseDataSourceDomicile, " "));
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch, adresseDataSource.rue + " "
                + adresseDataSource.numeroRue);
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch,
                COTSTiersUtils.limitNpaEtranger(adresseDataSource.localiteNpa) + " " + adresseDataSource.localiteNom);
    }

    /**
     * Test le datasource pour le texte du débiteur (zone 11).
     * 
     * @param session
     * @param transaction
     * @param elementJournalBatch
     * @param adresseDataSource
     * @throws Exception
     */
    private void testAdresseDataSourceTexteDebiteur(BSession session, BTransaction transaction,
            COElementJournalBatch elementJournalBatch, TIAdresseDataSource adresseDataSource) throws Exception {
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch, adresseDataSource.fullLigne1);
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch, adresseDataSource.fullLigne2);
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch, adresseDataSource.fullLigne3);
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch, adresseDataSource.fullLigne4);
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch, adresseDataSource.rue + " "
                + adresseDataSource.numeroRue);
        testLengthTexteDuDebiteur(session, transaction, elementJournalBatch, adresseDataSource.localiteNpa + " "
                + adresseDataSource.localiteNom);
    }

    /**
     * Test la longueur d'une ligne de complément (zone 11, texte débiteur).
     * 
     * @param session
     * @param transaction
     * @param elementJournalBatch
     * @param texte
     * @throws Exception
     */
    private void testLengthTexteDuDebiteur(BSession session, BTransaction transaction,
            COElementJournalBatch elementJournalBatch, String texte) throws Exception {
        if (!JadeStringUtil.isBlank(texte)
                && !COOPGESpellChecker.isLengthTexteComplementAllowed(COTSTiersUtils.convertSpecialChars(texte))) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_LIGNE_TROP_LONG) + " (" + texte + ")");
        }
    }

    /**
     * Test la validité d'une adresse.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param elementJournalBatch
     * @param adresseDataSource
     * @throws Exception
     */
    private void testLigneDataSource(BSession session, BTransaction transaction, COContentieux contentieux,
            COElementJournalBatch elementJournalBatch, TIAdresseDataSource adresseDataSource) throws Exception {
        if (JadeStringUtil.isBlank(adresseDataSource.fullLigne1)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_NOM_OBLIGATOIRE));
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne1)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne1)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                            + adresseDataSource.fullLigne1 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne2)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne2)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                            + adresseDataSource.fullLigne2 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne3)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne3)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                            + adresseDataSource.fullLigne3 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne4)
                && !COOPGESpellChecker.isWellFormated(session, contentieux.getCompteAnnexe().getTiers(),
                        adresseDataSource.fullLigne4)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_TIERS_CARACTERES_NON_SUPPORTES) + " ("
                            + adresseDataSource.fullLigne4 + ")");
        }

        if (!JadeStringUtil.isBlank(adresseDataSource.fullLigne1)
                && COServiceLocator.getTiersService().isPersonnePhysique(session,
                        contentieux.getCompteAnnexe().getTiers())
                && !COOPGESpellChecker.containsName(adresseDataSource.fullLigne1, adresseDataSource.tiersLigne1)) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                    session.getLabel(COTSOPGERule.LABEL_LIGNE_IMPOSSIBLE_TROUVER_NOM));
        }
    }

    /**
     * @see COTSValidationRule#validate(BSession session, BTransaction transaction, COContentieux contentieux,
     *      COTransition transition, COElementJournalBatch elementJournalBatch)
     */
    @Override
    public void validate(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransition transition, COElementJournalBatch elementJournalBatch) {
        try {
            TIAdresseDataSource adresseDataSource = COServiceLocator.getTiersService().getAdresseDataSource(session,
                    contentieux.getCompteAnnexe().getTiers(), contentieux.getCompteAnnexe().getIdExterneRole(),
                    JACalendar.today().toString());

            if (adresseDataSource == null) {
                elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                        session.getLabel(COTSOPGERule.LABEL_TIERS_OBLIGATOIRE));
            }

            if (!TIPays.CODE_ISO_CH.equals(adresseDataSource.paysIso)) {
                elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                        session.getLabel(COTSOPGERule.LABEL_TIERS_ADRESSE_NON_SUISSE));
            }

            testLigneDataSource(session, transaction, contentieux, elementJournalBatch, adresseDataSource);
            testAdresseDataSource(session, transaction, elementJournalBatch, adresseDataSource);

            TIAdresseDataSource adresseDataSourceComplement = COServiceLocator.getTiersService()
                    .getAdresseDataSourceComplementRDP(session, contentieux.getCompteAnnexe().getTiers(),
                            contentieux.getCompteAnnexe().getIdExterneRole(), JACalendar.today().toString());
            if (adresseDataSourceComplement != null) {
                if (!JadeStringUtil.isBlank(adresseDataSourceComplement.fullLigne1)
                        && !COOPGESpellChecker.isLengthTexteComplementAllowed(adresseDataSourceComplement.fullLigne1)) {
                    elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                            session.getLabel(COTSOPGERule.LABEL_TIERS_LIGNE_TROP_LONG) + " ("
                                    + adresseDataSourceComplement.fullLigne1 + ")");
                }

                if (!JadeStringUtil.isBlank(adresseDataSourceComplement.fullLigne2)
                        && !COOPGESpellChecker.isLengthTexteComplementAllowed(adresseDataSourceComplement.fullLigne2)) {
                    elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                            session.getLabel(COTSOPGERule.LABEL_TIERS_LIGNE_TROP_LONG) + " ("
                                    + adresseDataSourceComplement.fullLigne2 + ")");
                }

                if (!JadeStringUtil.isBlank(adresseDataSourceComplement.fullLigne3)
                        && !COOPGESpellChecker.isLengthTexteComplementAllowed(adresseDataSourceComplement.fullLigne3)) {
                    elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                            session.getLabel(COTSOPGERule.LABEL_TIERS_LIGNE_TROP_LONG) + " ("
                                    + adresseDataSourceComplement.fullLigne3 + ")");
                }

                if (!JadeStringUtil.isBlank(adresseDataSourceComplement.fullLigne4)
                        && !COOPGESpellChecker.isLengthTexteComplementAllowed(adresseDataSourceComplement.fullLigne4)) {
                    elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                            session.getLabel(COTSOPGERule.LABEL_TIERS_LIGNE_TROP_LONG) + " ("
                                    + adresseDataSourceComplement.fullLigne4 + ")");
                }

                testAdresseDataSource(session, transaction, elementJournalBatch, adresseDataSourceComplement);
                testAdresseDataSourceTexteDebiteur(session, transaction, elementJournalBatch,
                        adresseDataSourceComplement);
            } else {
                TIAdresseDataSource adresseDataSourceDomicile = COServiceLocator.getTiersService()
                        .getAdresseDataSourceDomicileStandard(session, contentieux.getCompteAnnexe().getTiers(),
                                contentieux.getCompteAnnexe().getIdExterneRole(), JACalendar.today().toString());

                if ((adresseDataSourceDomicile != null)
                        && !TIPays.CODE_ISO_CH.equals(adresseDataSourceDomicile.paysIso)) {
                    testAdresseDataSourceEtranger(session, transaction, elementJournalBatch, adresseDataSourceDomicile,
                            adresseDataSource);
                } else if (COTSTiersUtils.needTexteDuDebiteurAutomatique(session, contentieux, adresseDataSource)) {
                    testAdresseDataSourceTexteDebiteur(session, transaction, elementJournalBatch, adresseDataSource);
                }
            }

            // Vérifie que le montant de la créance soit pas négatif. L'OP n'accepte pas de montant négatif.
            String[] soldeInitial = globaz.aquila.db.rdp.CORequisitionPoursuiteUtil.getSoldeSectionInitial(session,
                    contentieux.getIdSection());
            if (!JadeNumericUtil.isNumericPositif(soldeInitial[0])) {
                elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                        session.getLabel("LIGNE_MONTANT_CREANCE_NEGATIF") + " (" + soldeInitial[0] + ")");
            }

            if (!JadeStringUtil.isIntegerEmpty(elementJournalBatch.getIdRemarque())) {
                StringTokenizer st = new StringTokenizer(elementJournalBatch.getRemarque().getTexte(),
                        COTSOPGEExecutor.LINE_BREAK);
                while (st.hasMoreTokens()) {
                    String tmp = COTSTiersUtils.convertSpecialChars(st.nextToken());
                    if (tmp.length() > COTSOPGEExecutor.LENGTH_TEXTE_DEBITEUR) {
                        elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(),
                                session.getLabel(COTSOPGERule.LIGNE_REMARQUE_TROP_LONGUE) + " (" + tmp + ")");
                    }
                }

            }
        } catch (Exception e) {
            elementJournalBatch.addErrors(session, transaction, COTSOPGERule.class.getName(), e.getMessage());
        }

    }

}
