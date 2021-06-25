package globaz.corvus.acor2020.business;

import acor.ch.admin.zas.rc.annonces.rente.pool.PoolMeldungZurZAS;
import acor.ch.admin.zas.rc.annonces.rente.rc.*;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import globaz.commons.nss.NSUtil;
import globaz.corvus.acor.parser.rev09.REACORParser;
import globaz.corvus.acor2020.parser.ParserUtils;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.utils.PRDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class REImportAnnoncesAcor {

    private static final String EMPTY_NSS = "00000000000";
    private static REImportAnnoncesAcor instance;

    private REImportAnnoncesAcor() {
    }

    public static REImportAnnoncesAcor getInstance() {
        if (instance == null)
            instance = new REImportAnnoncesAcor();

        return instance;
    }

    public void importAnnonces(BSession session, BTransaction transaction, PoolMeldungZurZAS annonces,
                               List<Long> listIdsRA) throws Exception {
        try {
            List<ZuwachsmeldungO10Type> annoncesOrdinaires10emeRev = new ArrayList<>();
            List<ZuwachsmeldungAO10Type> annoncesExtraOrdinaires10emeRev = new ArrayList<>();

            if (annonces.getLot() != null) {
                List<PoolMeldungZurZAS.Lot> lots = annonces.getLot();
                for (PoolMeldungZurZAS.Lot lot : lots) {
                    List<Object> list = lot
                            .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr();
                    for (Object o : list) {
                        if (o instanceof RRMeldung9Type) {
                            /*
                             * Ce fichier peut contenir des annonces de diminution de la 9ème révision. Ces annonces ne
                             * nous intéresse pas donc on ne les lit pas !
                             */
                        } else if (o instanceof RRMeldung10Type) {
                            // On ne lit que les annonces d'augmentation (Zuwachsmeldung)
                            // Rente ordinaire
                            addAnnoncesToList(annoncesOrdinaires10emeRev, annoncesExtraOrdinaires10emeRev, (RRMeldung10Type) o);
                        } else if (o instanceof LinkedHashMap) {
                            // TODO : a refactorer --> voir avec ACOR pour qu'ils fournissent un typage de l'objet dans le json.
                            try {
                                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                                RRMeldung10Type meldung10 = mapper.convertValue(o, RRMeldung10Type.class);
                                addAnnoncesToList(annoncesOrdinaires10emeRev, annoncesExtraOrdinaires10emeRev, meldung10);
                            } catch (Exception e) {
                                LOG.error("L'objet n'a pas pu être converti. Il ne s'agit pas d'une annonce de 10e révision.", e);
                            }
                        } else {
                            String message = session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_TYPE_INATTENDU");
                            String type = "null";
                            if (o != null) {
                                type = o.getClass().getName();
                            }
                            message = message.replace("{0}", type);
                        }
                    }
                }
            }
            // Création des annonces ordinaires WebAvs pour chacune des annonces récupérée sous forme de classe JaxB.
            for (ZuwachsmeldungO10Type annonce10eme : annoncesOrdinaires10emeRev) {
                buildAnnonceOrdinaire44(session, transaction, annonce10eme, listIdsRA);
            }
            // Création des annonces extra-ordinaires WebAvs pour chacune des annonces récupérée sous forme de classe
            // JaxB.
            for (ZuwachsmeldungAO10Type annonce10eme : annoncesExtraOrdinaires10emeRev) {
                buildAnnonceExtraOrdinaire44(session, transaction, annonce10eme, listIdsRA);
            }
        } catch (final JAXBException e) {
            String message = session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_JAXB") + " : ";
            message += e.getMessage();
            throw new Exception(message, e);
        } catch (Exception e) {
            String message = session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML") + " : ";
            message += e.getMessage();
            throw new Exception(message, e);
        }
    }

    private void addAnnoncesToList(List<ZuwachsmeldungO10Type> annoncesOrdinaires10emeRev, List<ZuwachsmeldungAO10Type> annoncesExtraOrdinaires10emeRev, RRMeldung10Type o) {
        RRMeldung10Type meldung = o;
        if ((meldung.getOrdentlicheRente() != null)
                && (meldung.getOrdentlicheRente().getZuwachsmeldung() != null)) {
            annoncesOrdinaires10emeRev.add(meldung.getOrdentlicheRente().getZuwachsmeldung());

        }
        // Rente extraordinaire
        else if ((meldung.getAusserordentlicheRente() != null)
                && (meldung.getAusserordentlicheRente().getZuwachsmeldung() != null)) {
            annoncesExtraOrdinaires10emeRev
                    .add(meldung.getAusserordentlicheRente().getZuwachsmeldung());

        }
    }

    private void buildAnnonceOrdinaire44(BSession session, BTransaction transaction,
                                         ZuwachsmeldungO10Type annonce10emeRev, List<Long> idsRA) throws Exception {

        if (annonce10emeRev == null) {
            throw new Exception(session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_ANNONCE_10EME_NULL"));
        }

        // Création de l'annonce 44_01.
        REAnnoncesAugmentationModification10Eme annonce44_01 = buildAnnonceOrdinaire44_01(session,
                annonce10emeRev);
        // Création de l'annonce 44_02.
        REAnnoncesAugmentationModification10Eme annonce44_02 = buildAnnonceOrdinaire44_02(session,
                annonce10emeRev);

        // Récupération de l'id tiers correspondant au NSS de l'ayant droit
        PRTiersWrapper tier = PRTiersHelper.getTiers(session,
                NSUtil.formatAVSUnknown(annonce44_01.getNoAssAyantDroit()));
        if (tier != null) {
            annonce44_01.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            annonce44_02.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        // Ce champ ne doit pas être stocké à la création de l'annonce. Il est uniquement récupéré pour la validation
        // ANAKIN. Il doit être supprimé avant l'insertion en DB.
        String moisDeRapport = annonce44_01.getMoisRapport();
        annonce44_01.setMoisRapport("");

        // Création des 2 annonces 44, la 1 en premier (histoire d'avoir les ids dans l'ordre)
        annonce44_01.setSession(session);
        annonce44_01.add(transaction);
        annonce44_02.setSession(session);
        annonce44_02.add(transaction);

        // Mise à jour de l'annonce 44_01 avec l'id de la 44_02
        annonce44_01.setIdLienAnnonce(annonce44_02.getIdAnnonce());
        annonce44_01.update(transaction);

        // Validation des annonces par Anakin

        String moisRapportPourValidation = moisDeRapport.substring(4, 6);
        moisRapportPourValidation += moisDeRapport.substring(2, 4);

        Map<String, String> idsArc44 = new HashMap<>();
        idsArc44.put(annonce44_01.getIdAnnonce(), moisRapportPourValidation);
        try {
            REACORParser.anakinValidation(session, transaction, null, idsArc44);
        } catch (Exception exception) {
            throw new Exception(session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_ERREUR_VALIDATION_ANAKIN")
                    + " " + exception.getMessage());
        }

        // Récupération de l'id de la rente accordée
        String idRA = getIdRenteAccordeeCorrespondanteAAnnonce(session, transaction, annonce44_01, idsRA);
        if (JadeStringUtil.isBlankOrZero(idRA)) {
            StringBuilder message = new StringBuilder();
            message.append(session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_RENTE_ACCORDEE_INTROUVABLE"));
            message.append(" [");
            for (Iterator<Long> iterator = idsRA.iterator(); iterator.hasNext(); ) {
                Long id = iterator.next();
                message.append(id);
                if (iterator.hasNext()) {
                    message.append(", ");
                }
            }
            message.append("].");
            message.append(session.getLabel("NSS_DE_LAYANT_DROIT"));
            message.append(" [" + annonce44_01.getNoAssAyantDroit() + "],");
            message.append(session.getLabel("CODE_PRESTATION_DE_LA_RENTE"));
            message.append(" [" + annonce44_01.getGenrePrestation() + "],");
            message.append(session.getLabel("DATE_DE_DEBUT_DU_DROIT"));
            message.append(" [" + annonce44_01.getDebutDroit() + "],");
            throw new Exception(message.toString());
        }

        // Création de l'annonce REANREN
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(session);
        annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
        annonceRente.setIdAnnonceHeader(annonce44_01.getIdAnnonce());
        annonceRente.setIdRenteAccordee(idRA);
        annonceRente.add(transaction);
    }

    /**
     * Construit l'ensemble des annonces 44_01</br>
     * Graph d'object lié à une annonce 44_01 :
     * REANHEA->REAAL1A->REAAL2A->REAAL3A->REAAL4A</br>
     * L'annonce n'est pas persisté dans cette méthode, l'idTiers n'est
     * pas définit dans cette méthode et l'id lien annonce sur l'annonce 44_02 non plus. C.F method
     * buildAnnonce44(...)</br>
     *
     * @param session         La session en cours
     * @param annonce10emeRev L'annonce 10ème révision récupéré depuis le XML et généré par JaxB
     * @return Une instance de la classe REAnnoncesAugmentationModification10Eme remplie mais non persisté
     * @throws Exception
     */
    private REAnnoncesAugmentationModification10Eme buildAnnonceOrdinaire44_01(BSession session, ZuwachsmeldungO10Type annonce10emeRev) {

        /**
         * REMARQUE : - numeroAnnonce : YXNNOA : n'est pas géré lors de la lecture du fichier annonce.xml -
         * nouveauNoAssureAyantDroit : ZENNNEA : pas géré lors de la lecture du fichier annonce.xml
         */

        String codeApplication = "44";
        String codeEnregistrement01 = "01";
        String etat = IREAnnonces.CS_ETAT_OUVERT;
        String numeroCaisse = "";
        String numeroAgence = "";
        String referenceCaisseInterne = "";
        String noAssAyantDroit = "";
        String premierNoAssComplementaire = "";
        String secondNoAssComplementaire = "";
        String etatCivil = "";
        String isRefugie = "";
        String cantonEtatDomicile = "";
        String genrePrestation = "";
        String debutDroit = "";
        String mensualitePrestationsFrancs = "";
        String finDroit = "";
        String codeMutation = "";
        String moisRapport = "";

        /* (String) : contient 6 positions : numeroCaisse (3pos) + numeroAgence (3pos) */
        String noCaisseEtAgence = ParserUtils.formatIntToStringWithSixChar(annonce10emeRev.getKasseZweigstelle());
        if (!JadeStringUtil.isEmpty(noCaisseEtAgence)) {
            if (noCaisseEtAgence.length() >= 3) {
                /* ZAANOC : 3 positions */
                numeroCaisse = noCaisseEtAgence.substring(0, 3);
            }
            if (noCaisseEtAgence.length() >= 6) {
                /* ZAANOA : 3 positions */
                numeroAgence = noCaisseEtAgence.substring(3, 6);
            }
        }

        /* YXLREI : composé de 'AUG' et du nom de l'utilisateur, le tout en UpperCase */
        String userName = session.getUserId();
        referenceCaisseInterne = "AUG";
        if (userName != null) {
            referenceCaisseInterne = referenceCaisseInterne + userName.toUpperCase();
        }

        /*
         * YXDMRA (XMLGregorianCalendar) : format AAAAMM. Cette valeur est récupérée que pour la validation avec Anakin,
         * elle n'est pas stockée.
         */
        moisRapport = ParserUtils.formatDateToAAAAMM(annonce10emeRev.getBerichtsmonat());

        /* Ayant-droit */
        if (annonce10emeRev.getLeistungsberechtigtePerson() != null) {
            RRLeistungsberechtigtePersonAuslType leistungsberechtigtePerson = annonce10emeRev
                    .getLeistungsberechtigtePerson();

            /* String : NSS */
            if (Objects.nonNull(leistungsberechtigtePerson.getVersichertennummer())) {
                noAssAyantDroit = leistungsberechtigtePerson.getVersichertennummer();
            }

            /* Parenté */
            if (leistungsberechtigtePerson.getFamilienAngehoerige() != null) {
                FamilienAngehoerigeType familienAngehoerigeType = leistungsberechtigtePerson.getFamilienAngehoerige();
                if (!familienAngehoerigeType.getVNr1Ergaenzend().isEmpty()) {
                    /* YXNPNA (String) : format NSS complet sur 13 position sans point */
                    premierNoAssComplementaire = familienAngehoerigeType.getVNr1Ergaenzend().get(0);
                    if (!JadeStringUtil.isBlankOrZero(premierNoAssComplementaire)) {
                        premierNoAssComplementaire = NSUtil.unFormatAVS(
                                definirNssComplementaire(session, NSUtil.formatAVSUnknown(premierNoAssComplementaire)));
                    } else {
                        premierNoAssComplementaire = EMPTY_NSS;
                    }
                }
                if (!familienAngehoerigeType.getVNr2Ergaenzend().isEmpty()) {
                    /* YXNDNA (String) : format NSS complet sur 13 position sans point */
                    secondNoAssComplementaire = familienAngehoerigeType.getVNr2Ergaenzend().get(0);
                    if (!JadeStringUtil.isBlankOrZero(secondNoAssComplementaire)) {
                        secondNoAssComplementaire = NSUtil.unFormatAVS(
                                definirNssComplementaire(session, NSUtil.formatAVSUnknown(secondNoAssComplementaire)));
                    } else {
                        secondNoAssComplementaire = EMPTY_NSS;
                    }
                }
            }

            /* YXLETC (short) : type intrinsèque, sera toujours présent (non null), 1 position */
            etatCivil = ParserUtils.formatShortToString(leistungsberechtigtePerson.getZivilstand());

            /* YXBREF (boolean) : type intrinsèque, sera toujours présent (non null), 1 position */
            isRefugie = ParserUtils.formatBooleanToString(leistungsberechtigtePerson.isIstFluechtling());

            /* YXLCAN (String) : 3 position, si null -> vide sinon formaté sur 3 positions avec des 0 */
            String canton = ParserUtils.formatIntegerToString(leistungsberechtigtePerson.getWohnkantonStaat());
            if (canton != null) {
                cantonEtatDomicile = canton;
                cantonEtatDomicile = ParserUtils.indentLeftWithZero(cantonEtatDomicile, 3);
            }
        }

        /* Description des prestations */
        if (annonce10emeRev.getLeistungsbeschreibung() != null) {
            ZuwachsmeldungO10Type.Leistungsbeschreibung leistungsbeschreibung = annonce10emeRev.getLeistungsbeschreibung();

            /* YXLGEN (String) : pas de format particulier, juste la valeur. Si vide, valeur sera une chaîne vide */
            if (Objects.nonNull(leistungsbeschreibung.getLeistungsart())) {
                genrePrestation = leistungsbeschreibung.getLeistungsart();
            }

            /* YXDDEB (XMLGregorianCalendar) : format MMAA */
            debutDroit = ParserUtils.formatDateToMMAA(leistungsbeschreibung.getAnspruchsbeginn());

            /* YXDFIN (XMLGregorianCalendar) : format MMAA */
            finDroit = ParserUtils.formatDateToMMAA(leistungsbeschreibung.getAnspruchsende());

            /* YXMMEN (BigDecimal) : 5 position, si vide que des 0 */
            BigDecimal mensualite = leistungsbeschreibung.getMonatsbetrag();
            if (mensualite != null) {
                mensualitePrestationsFrancs = ParserUtils.formatBigDecimalToString(mensualite);
                mensualitePrestationsFrancs = ParserUtils.indentLeftWithZero(mensualitePrestationsFrancs, 5);
            }

            /* YXLCOM : code mutation. Vide si pas de valeur (0 veut dire pas de valeur) sinon valeur sur 2 position */
            Short v7 = leistungsbeschreibung.getMutationscode();
            if (v7 != null) {
                codeMutation = ParserUtils.indentLeftWithZero(String.valueOf(v7), 2);
            }
        }

        // Affectation des valeurs récupérées et formatées
        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setCodeApplication(codeApplication);
        annonce44.setCodeEnregistrement01(codeEnregistrement01);
        annonce44.setEtat(etat);
        annonce44.setNumeroCaisse(numeroCaisse);
        annonce44.setNumeroAgence(numeroAgence);
        annonce44.setReferenceCaisseInterne(referenceCaisseInterne);
        annonce44.setNoAssAyantDroit(noAssAyantDroit);
        annonce44.setPremierNoAssComplementaire(premierNoAssComplementaire);
        annonce44.setSecondNoAssComplementaire(secondNoAssComplementaire);
        annonce44.setEtatCivil(etatCivil);
        annonce44.setIsRefugie(isRefugie);
        annonce44.setCantonEtatDomicile(cantonEtatDomicile);
        annonce44.setGenrePrestation(genrePrestation);
        annonce44.setDebutDroit(debutDroit);
        annonce44.setMensualitePrestationsFrancs(mensualitePrestationsFrancs);
        annonce44.setFinDroit(finDroit);
        annonce44.setCodeMutation(codeMutation);
        annonce44.setMoisRapport(moisRapport);
        return annonce44;
    }


    private String definirNssComplementaire(BSession session, String nssComplementaireAnnonceXml) {
        String nssComplementaire = EMPTY_NSS;
        try {
            NumeroSecuriteSociale.validate(nssComplementaireAnnonceXml);

            PRTiersWrapper tiers1 = PRTiersHelper.getTiers(session, nssComplementaireAnnonceXml);
            if (tiers1 != null) {
                nssComplementaire = nssComplementaireAnnonceXml;
            }
        } catch (IllegalArgumentException e) {
            // dans le cas où le NSS est invalide, on rempli avec des zéro
        } catch (Exception ex) {
            // si un problème survient pendant l'accès aux tiers
            throw new RETechnicalException(ex);
        }
        return nssComplementaire;
    }

    /**
     * Construit l'ensemble des annonces 44_02</br>
     * Graph d'object lié à une annonce 44_02 :
     * REANHEA->REAAL1A->REAAL2A->REAAL3A->REAAL4A</br>
     * L'annonce n'est pas persisté dans cette méthode, l'idTiers n'est
     * pas définit dans cette méthode c.f buildAnnonce44(...)</br>
     *
     * @param session         La session en cours
     * @param annonce10emeRev L'annonce 10ème révision récupéré depuis le XML et généré par JaxB
     * @return Une instance de la classe REAnnoncesAugmentationModification10Eme remplie mais non persisté
     * @throws Exception
     */
    private REAnnoncesAugmentationModification10Eme buildAnnonceOrdinaire44_02(BSession session, ZuwachsmeldungO10Type annonce10emeRev) {

        // Ensemble des valeurs qui vont être lues
        String codeApplication = "44";
        String codeEnregistrement = "02";
        String etat = IREAnnonces.CS_ETAT_OUVERT;
        String anneeNiveau = "";
        String echelleRente = "";
        String dureeCoEchelleRenteAv73 = "";
        String dureeCoEchelleRenteDes73 = "";
        String dureeCotManquante48_72 = "";
        String dureeCotManquante73_78 = "";
        String anneeCotClasseAge = "";
        String ramDeterminant = "";
        String dureeCotPourDetRAM = "";
        String codeRevenuSplitte = "";
        String nombreAnneeBTE = "";
        String nbreAnneeBTA = "";
        String nbreAnneeBonifTrans = "";
        String officeAICompetent = "";
        String degreInvalidite = "";
        String codeInfirmite = "";
        String survenanceEvenAssure = "";
        String ageDebutInvalidite = "";
        String dureeAjournement = "";
        String dateRevocationAjournement = "";
        String supplementAjournement = "";
        String nbreAnneeAnticipation = "";
        String dateDebutAnticipation = "";
        String reductionAnticipation = "";
        String reduction = "";
        String isSurvivant = "";
        String codeCasSpecial1 = "";
        String codeCasSpecial2 = "";
        String codeCasSpecial3 = "";
        String codeCasSpecial4 = "";
        String codeCasSpecial5 = "";

        final BigDecimal multiplicateur10 = new BigDecimal(10);
        final BigDecimal multiplicateur100 = new BigDecimal(100);
        BigDecimal tmpVal1 = null;

        if (annonce10emeRev.getLeistungsbeschreibung() != null) {
            ZuwachsmeldungO10Type.Leistungsbeschreibung leistungsbeschreibung = annonce10emeRev.getLeistungsbeschreibung();

            /* YYLRED (Short) : si pas de valeur = vide, sinon la valeur formatée sur 2 position */
            Short reductionShort = leistungsbeschreibung.getKuerzungSelbstverschulden();
            if (reductionShort != null) {
                reduction = ParserUtils.formatShortToString(reductionShort);
                reduction = ParserUtils.indentLeftWithZero(reduction, 2);
            }

            /* YZBSUR (Boolean) : si null -> pas de valeur sinon valeur 0 ou 1 */
            Boolean survivantBoolean = leistungsbeschreibung.isIstInvaliderHinterlassener();
            if (survivantBoolean != null) {
                isSurvivant = ParserUtils.formatBooleanToString(survivantBoolean);
            }

            /* YYLCS1 à YYLCS5 (Short) : Si null ou vide, pas de valeur sinon formaté sur 2 positions */
            List<Short> codesCasSpeciaux = annonce10emeRev.getLeistungsbeschreibung().getSonderfallcodeRente();
            if (codesCasSpeciaux != null) {
                if (codesCasSpeciaux.size() > 0) {
                    codeCasSpecial1 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0));
                }
                if (codesCasSpeciaux.size() > 1) {
                    codeCasSpecial2 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1));
                }
                if (codesCasSpeciaux.size() > 2) {
                    codeCasSpecial3 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2));
                }
                if (codesCasSpeciaux.size() > 3) {
                    codeCasSpecial4 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3));
                }
                if (codesCasSpeciaux.size() > 4) {
                    codeCasSpecial5 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4));
                }
            }

            if (leistungsbeschreibung.getBerechnungsgrundlagen() != null) {
                ZuwachsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen berechnungsgrundlagen = leistungsbeschreibung.getBerechnungsgrundlagen();

                /* YYDANI (XMLGregorianCalendar) : format AA. si null pas de valeur */
                XMLGregorianCalendar value = berechnungsgrundlagen.getNiveaujahr();
                if (value != null) {
                    String tmp1 = String.valueOf(value.getYear());
                    if (tmp1.length() == 4) {
                        anneeNiveau = tmp1.substring(2, 4);
                    }
                }

                if (berechnungsgrundlagen.getSkalaBerechnung() != null) {
                    SkalaBerechnungType skalaBerechnung = berechnungsgrundlagen.getSkalaBerechnung();

                    /* YYLECR (short) : formaté sur 2 position */
                    echelleRente = ParserUtils.formatShortToString(skalaBerechnung.getSkala());

                    /* YYDCEC (BigDecimal) : si valeur vide -> vide, sinon formaté sur 4 position */
                    tmpVal1 = skalaBerechnung.getBeitragsdauerVor1973();
                    if (tmpVal1 != null) {
                        dureeCoEchelleRenteAv73 = ParserUtils.formatBigDecimal(tmpVal1);
                    }

                    /* YYDECH (BigDecimal) : si valeur vide -> vide, sinon formaté sur 4 position */
                    tmpVal1 = skalaBerechnung.getBeitragsdauerAb1973();
                    if (tmpVal1 != null) {
                        dureeCoEchelleRenteDes73 = ParserUtils.formatBigDecimal(tmpVal1);
                    }

                    /* YYDCM1 (int) : type intrinsèque, valeur formatée sur 2 position */
                    dureeCotManquante48_72 = ParserUtils.formatIntegerToString(skalaBerechnung.getAnrechnungVor1973FehlenderBeitragsmonate());
                    dureeCotManquante48_72 = ParserUtils.indentLeftWithZero(dureeCotManquante48_72, 2);

                    /* YYDCM2 (int) : type intrinsèque, valeur formatée sur 2 position */
                    dureeCotManquante73_78 = ParserUtils.formatIntegerToString(skalaBerechnung.getAnrechnungAb1973Bis1978FehlenderBeitragsmonate());
                    dureeCotManquante73_78 = ParserUtils.indentLeftWithZero(dureeCotManquante73_78, 2);

                    /* YYDACC (int) : type intrinsèque, valeur brut, formatage sur 2 positions */
                    anneeCotClasseAge = ParserUtils.indentLeftWithZero(ParserUtils.formatIntegerToString(skalaBerechnung.getBeitragsjahreJahrgang()),
                            2);
                }

                if (berechnungsgrundlagen.getDJEBeschreibung() != null) {
                    DJE10BeschreibungType dJEBeschreibung = berechnungsgrundlagen.getDJEBeschreibung();

                    /* YYMRAM (BigDecimal) : si null pas de valeur -> chaîne vide sinon formaté sur 8 position */
                    tmpVal1 = dJEBeschreibung.getDurchschnittlichesJahreseinkommen();
                    if (tmpVal1 != null) {
                        ramDeterminant = ParserUtils.formatBigDecimalToString(tmpVal1);
                        ramDeterminant = ParserUtils.indentLeftWithZero(ramDeterminant, 8);
                    }

                    /* YYNDCO (BigDecimal) : si valeur null ou vide --> vide, sinon formatage sur 4 position */
                    tmpVal1 = dJEBeschreibung.getBeitragsdauerDurchschnittlichesJahreseinkommen();
                    if (tmpVal1 != null) {
                        dureeCotPourDetRAM = ParserUtils.formatBigDecimal(tmpVal1);
                    }

                    /* YZTCOD (boolean) : type intrinsèque, valeur brut, pas de formatage particulier */
                    codeRevenuSplitte = ParserUtils.formatBooleanToString(dJEBeschreibung.isGesplitteteEinkommen());
                }

                /* Bonification */
                if (berechnungsgrundlagen.getGutschriften() != null) {
                    Gutschriften10Type gutschriften10Type = berechnungsgrundlagen.getGutschriften();

                    /*
                     * YYNANN (BigDecimal) : si valeur null ou vide --> vide, sinon formatage sur 4 position. Multiplier
                     * la valeur par 100
                     */
                    tmpVal1 = gutschriften10Type.getAnzahlErziehungsgutschrift();
                    if (tmpVal1 != null) {
                        tmpVal1 = tmpVal1.multiply(multiplicateur100);
                        nombreAnneeBTE = String.valueOf(tmpVal1.intValue());
                        nombreAnneeBTE = ParserUtils.indentLeftWithZero(nombreAnneeBTE, 4);
                    }

                    /*
                     * YZNBTA (BigDecimal) : si valeur null -> vide, sinon formaté sur 4 position. Multiplier la valeur
                     * par 100
                     */
                    tmpVal1 = gutschriften10Type.getAnzahlBetreuungsgutschrift();
                    if (tmpVal1 != null) {
                        tmpVal1 = tmpVal1.multiply(multiplicateur100);
                        nbreAnneeBTA = String.valueOf(tmpVal1.intValue());
                        nbreAnneeBTA = ParserUtils.indentLeftWithZero(nbreAnneeBTA, 4);
                    }

                    /* YZNBON (BigDecimal) : si valeur null -> vide sinon formaté sur 2 position */
                    tmpVal1 = gutschriften10Type.getAnzahlUebergangsgutschrift();
                    if (tmpVal1 != null) {
                        tmpVal1 = tmpVal1.multiply(multiplicateur10);
                        nbreAnneeBonifTrans = String.valueOf(tmpVal1.intValue());
                        nbreAnneeBonifTrans = ParserUtils.indentLeftWithZero(nbreAnneeBonifTrans, 2);
                    }
                }

                if (berechnungsgrundlagen.getIVDaten() != null) {
                    IVDaten10Type iVDaten10Type = berechnungsgrundlagen.getIVDaten();

                    /* YYLOAI (int) : valeur sans formatage */
                    officeAICompetent = ParserUtils.formatIntegerToString(iVDaten10Type.getIVStelle());

                    /* YYNDIN (short) : 3 position formaté */
                    degreInvalidite = ParserUtils.formatShortToString(iVDaten10Type.getInvaliditaetsgrad());
                    degreInvalidite = ParserUtils.indentLeftWithZero(degreInvalidite, 3);

                    /* code infirmité, 3 position formaté */
                    String codeInf = ParserUtils.formatIntegerToString(iVDaten10Type.getGebrechensschluessel());
                    codeInf = ParserUtils.indentLeftWithZero(codeInf, 3);

                    /* code atteinte fonctionnel, si null -> pas de valeur sinon 2 position formaté */
                    String codeAtteinte = "";
                    Short ca = iVDaten10Type.getFunktionsausfallcode();
                    if (ca != null) {
                        codeAtteinte = ParserUtils.formatShortToString(ca);
                        codeAtteinte = ParserUtils.indentLeftWithZero(codeAtteinte, 2);
                    }

                    /*
                     * YYNCOI : concaténation du code infirmité (3pos formaté) + code atteinte fonctionnel (2pos
                     * formaté)
                     */
                    codeInfirmite = codeInf + codeAtteinte;

                    /* YYNSUR : si valeur null -> chaîne vide sinon date au format MMAA */
                    survenanceEvenAssure = ParserUtils.formatDateToMMAA(iVDaten10Type.getDatumVersicherungsfall());

                    /* YYNAGE (boolean) : Valeur 0 ou 1. Si invalide avant 25 ans 1 sinon 0 */
                    ageDebutInvalidite = ParserUtils.formatBooleanToString(iVDaten10Type.isIstFruehInvalid());
                }

                /* Age flexible de la rente */
                if (berechnungsgrundlagen.getFlexiblesRentenAlter() != null) {
                    if (berechnungsgrundlagen.getFlexiblesRentenAlter().getRentenaufschub() != null) {
                        RentenaufschubType rentenaufschubType = berechnungsgrundlagen.getFlexiblesRentenAlter()
                                .getRentenaufschub();

                        /*
                         * YYNDUR (BigDecimal) : si null -> valeur vide sinon valeur a.b -> valeur entière de a + (si b
                         * < 10 = 0+b else b) en résumé : 1.10 -> 110, 1.1 -> 101, 2.0 -> 200
                         */
                        tmpVal1 = rentenaufschubType.getAufschubsdauer();
                        if (tmpVal1 != null) {
                            String stringValue = tmpVal1.toString();
                            if (stringValue.contains(".")) {
                                String[] values = stringValue.split("\\.");
                                dureeAjournement = values[0];
                                int decVal = Integer.parseInt(values[1]);
                                if (decVal < 10) {
                                    dureeAjournement += "0" + decVal;
                                } else {
                                    dureeAjournement += decVal;
                                }
                            }

                        }

                        /* YYDREV (XMLGregorianCalendar) : si valeur null -> vide sinon format MMAA */
                        dateRevocationAjournement = ParserUtils.formatDateToMMAA(rentenaufschubType.getAbrufdatum());

                        /*
                         * YYNSUP (BigDecimal) : si valeur null -> vide sinon formaté sur 5 position. Pas de
                         * multiplication
                         */
                        tmpVal1 = rentenaufschubType.getAufschubszuschlag();
                        if (tmpVal1 != null) {
                            supplementAjournement = String.valueOf(tmpVal1.intValue());
                            supplementAjournement = ParserUtils.indentLeftWithZero(supplementAjournement, 5);
                        }

                    }
                    /* Rente anticipée */
                    if (berechnungsgrundlagen.getFlexiblesRentenAlter().getRentenvorbezug() != null) {
                        RentenvorbezugType rentenvorbezugType = berechnungsgrundlagen.getFlexiblesRentenAlter()
                                .getRentenvorbezug();

                        /* YZNANT (int) : pas de formatage particulier */
                        nbreAnneeAnticipation = ParserUtils.formatIntegerToString(rentenvorbezugType.getAnzahlVorbezugsjahre());

                        /* YZDDEB (XMLGregorianCalendar) : si null -> pas de valeur sinon formaté en AAMM */
                        dateDebutAnticipation = ParserUtils.formatDateToMMAA(rentenvorbezugType.getVorbezugsdatum());
                        /* YZNRED (BigDecimal) */
                        tmpVal1 = rentenvorbezugType.getVorbezugsreduktion();
                        if (tmpVal1 != null) {
                            reductionAnticipation = String.valueOf(tmpVal1.intValue());
                            reductionAnticipation = ParserUtils.indentLeftWithZero(reductionAnticipation, 5);
                        }
                    }
                }
            }
        }

        // Affectation des valeurs récupérées et traitées
        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setSession(session);
        annonce44.setCodeApplication(codeApplication);
        annonce44.setCodeEnregistrement01(codeEnregistrement);
        annonce44.setEtat(etat);
        annonce44.setAnneeNiveau(anneeNiveau);
        annonce44.setEchelleRente(echelleRente);
        annonce44.setDureeCoEchelleRenteAv73(dureeCoEchelleRenteAv73);
        annonce44.setDureeCoEchelleRenteDes73(dureeCoEchelleRenteDes73);
        annonce44.setDureeCotManquante48_72(dureeCotManquante48_72);
        annonce44.setDureeCotManquante73_78(dureeCotManquante73_78);
        annonce44.setAnneeCotClasseAge(anneeCotClasseAge);
        annonce44.setRamDeterminant(ramDeterminant);
        annonce44.setDureeCotPourDetRAM(dureeCotPourDetRAM);
        annonce44.setCodeRevenuSplitte(codeRevenuSplitte);
        annonce44.setNombreAnneeBTE(nombreAnneeBTE);
        annonce44.setNbreAnneeBTA(nbreAnneeBTA);
        annonce44.setNbreAnneeBonifTrans(nbreAnneeBonifTrans);
        annonce44.setOfficeAICompetent(officeAICompetent);
        annonce44.setDegreInvalidite(degreInvalidite);
        annonce44.setCodeInfirmite(codeInfirmite);
        annonce44.setSurvenanceEvenAssure(survenanceEvenAssure);
        annonce44.setAgeDebutInvalidite(ageDebutInvalidite);
        annonce44.setDureeAjournement(dureeAjournement);
        annonce44.setDateRevocationAjournement(dateRevocationAjournement);
        annonce44.setSupplementAjournement(supplementAjournement);
        annonce44.setNbreAnneeAnticipation(nbreAnneeAnticipation);
        annonce44.setDateDebutAnticipation(dateDebutAnticipation);
        annonce44.setReductionAnticipation(reductionAnticipation);
        annonce44.setReduction(reduction);
        annonce44.setIsSurvivant(isSurvivant);
        annonce44.setCasSpecial1(codeCasSpecial1);
        annonce44.setCasSpecial2(codeCasSpecial2);
        annonce44.setCasSpecial3(codeCasSpecial3);
        annonce44.setCasSpecial4(codeCasSpecial4);
        annonce44.setCasSpecial5(codeCasSpecial5);
        annonce44.setGenreDroitAPI(""); // On n'aura jamais cette information car les API ne sont pas calculé par ACOR
        return annonce44;
    }

    /**
     * Parcourt les rentes accordées dont les ids sont passés en paramètres</br>
     * Les champs suivants seront testé pour
     * déterminer si l'annonce correspond à la rente accordée :</br>
     * - NSS (idTiersBeneficiaire)</br>
     * - Genre de rente
     * (genrePrestation)</br>
     * - Date de début</br>
     *
     * @param session     La session courante
     * @param transaction La transaction en cours
     * @param annonce44   L'annonce en question
     * @param idsRAList   Une liste d'ids de rente accordées sur lesquels on va rechercher une correspondance avec l'annonce 44
     * @return L'id de la rente accordée correspondant à l'annonce 44 ou null si aucune correspondance
     * @throws Exception
     */
    private String getIdRenteAccordeeCorrespondanteAAnnonce(BSession session, BTransaction transaction,
                                                            REAnnoncesAugmentationModification10Eme annonce44, List<Long> idsRAList) throws Exception {

        String idsRAsearch = "";
        for (Iterator<Long> iterator = idsRAList.iterator(); iterator.hasNext(); ) {
            Long idsRA = iterator.next();
            if (iterator.hasNext()) {
                idsRAsearch += idsRA + ", ";
            } else {
                idsRAsearch += idsRA;
            }
        }

        // Retrouver la rente accordée et setter REAnnonceRente
        RERenteAccordeeManager raMan = new RERenteAccordeeManager();
        raMan.setSession(session);
        raMan.setForIdsRentesAccordees(idsRAsearch);
        raMan.find(transaction, BManager.SIZE_NOLIMIT);

        // Itération pour retrouver la rente accordée
        for (Iterator<RERenteAccordee> iterator = raMan.iterator(); iterator.hasNext(); ) {
            RERenteAccordee ra = iterator.next();
            if (isAnnonceLieeALaPrestation(ra, annonce44)) {
                return ra.getIdPrestationAccordee();
            }
        }
        return null;
    }

    private boolean isAnnonceLieeALaPrestation(RERenteAccordee ra, REAnnoncesAugmentationModification10Eme annonce44) {
        boolean result = false;
        if (Objects.equals(ra.getCodePrestation(), annonce44.getGenrePrestation()) &&
                Objects.equals(ra.getIdTiersBeneficiaire(), annonce44.getIdTiers())) {
            String dateDebutDroitAnnonce = PRDateFormater
                    .convertDate_MMAA_to_MMxAAAA(annonce44.getDebutDroit());
            if (PRDateUtils.compare("01." + ra.getDateDebutDroit(), "01." + dateDebutDroitAnnonce)
                    .equals(PRDateUtils.PRDateEquality.EQUALS)) {
                result = true;
            }
        }
        return result;
    }

    private void buildAnnonceExtraOrdinaire44(BSession session, BTransaction transaction,
                                              ZuwachsmeldungAO10Type annonce10emeRev, List<Long> listIdsRA) throws Exception {
        if (annonce10emeRev == null) {
            throw new Exception(session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_ANNONCE_10EME_NULL"));
        }

        // Création de l'annonce 44_01.
        REAnnoncesAugmentationModification10Eme annonce44_01 = buildAnnonceExtraOrdinaire44_01(session,
                annonce10emeRev);
        // Création de l'annonce 44_02.
        REAnnoncesAugmentationModification10Eme annonce44_02 = buildAnnonceExtraOrdinaire44_02(session,
                annonce10emeRev);

        // Récupération de l'id tiers correspondant au NSS de l'ayant droit
        PRTiersWrapper tier = PRTiersHelper.getTiers(session,
                NSUtil.formatAVSUnknown(annonce44_01.getNoAssAyantDroit()));
        if (tier != null) {
            annonce44_01.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            annonce44_02.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        // Ce champ ne doit pas être stocké à la création de l'annonce. Il est uniquement récupéré pour la validation
        // ANAKIN. Il doit être supprimé avant l'insertion en DB.
        String moisDeRapport = annonce44_01.getMoisRapport();
        annonce44_01.setMoisRapport("");

        // Création des 2 annonces 44, la 1 en premier (histoire d'avoir les ids dans l'ordre)
        annonce44_01.setSession(session);
        annonce44_01.add(transaction);
        annonce44_02.setSession(session);
        annonce44_02.add(transaction);

        // Mise à jour de l'annonce 44_01 avec l'id de la 44_02
        annonce44_01.setIdLienAnnonce(annonce44_02.getIdAnnonce());
        annonce44_01.update(transaction);

        // Validation des annonces par Anakin

        String moisRapportPourValidation = moisDeRapport.substring(4, 6);
        moisRapportPourValidation += moisDeRapport.substring(2, 4);

        Map<String, String> idsArc44 = new HashMap<>();
        idsArc44.put(annonce44_01.getIdAnnonce(), moisRapportPourValidation);
        try {
            REACORParser.anakinValidation(session, transaction, null, idsArc44);
        } catch (Exception exception) {
            throw new Exception(session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_ERREUR_VALIDATION_ANAKIN")
                    + " " + exception.getMessage());
        }

        // Récupération de l'id de la rente accordée
        String idRA = getIdRenteAccordeeCorrespondanteAAnnonce(session, transaction, annonce44_01, listIdsRA);
        if (JadeStringUtil.isBlankOrZero(idRA)) {
            StringBuilder message = new StringBuilder();
            message.append(session.getLabel("ERREUR_CALCUL_ACOR_LECTURE_ANNONCE_XML_RENTE_ACCORDEE_INTROUVABLE"));
            message.append(" [");
            for (Iterator<Long> iterator = listIdsRA.iterator(); iterator.hasNext(); ) {
                Long id = iterator.next();
                message.append(id);
                if (iterator.hasNext()) {
                    message.append(", ");
                }
            }
            message.append("].");
            message.append(session.getLabel("NSS_DE_LAYANT_DROIT"));
            message.append(" [" + annonce44_01.getNoAssAyantDroit() + "],");
            message.append(session.getLabel("CODE_PRESTATION_DE_LA_RENTE"));
            message.append(" [" + annonce44_01.getGenrePrestation() + "],");
            message.append(session.getLabel("DATE_DE_DEBUT_DU_DROIT"));
            message.append(" [" + annonce44_01.getDebutDroit() + "],");
            throw new Exception(message.toString());
        }

        // Création de l'annonce REANREN
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(session);
        annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
        annonceRente.setIdAnnonceHeader(annonce44_01.getIdAnnonce());
        annonceRente.setIdRenteAccordee(idRA);
        annonceRente.add(transaction);
    }

    /**
     * Construit l'ensemble des annonces 44_01</br>
     * Graph d'object lié à une annonce 44_01 extraordinaire :
     * REANHEA->REAAL1A->REAAL2A->REAAL3A->REAAL4A</br>
     * L'annonce n'est pas persisté dans cette méthode, l'idTiers n'est
     * pas définit dans cette méthode et l'id lien annonce sur l'annonce 44_02 non plus. C.F method
     * buildAnnonce44(...)</br>
     *
     * @param session         La session en cours
     * @param annonce10emeRev L'annonce 10ème révision récupéré depuis le XML et généré par JaxB
     * @return Une instance de la classe REAnnoncesAugmentationModification10Eme remplie mais non persisté
     * @throws Exception
     */
    private REAnnoncesAugmentationModification10Eme buildAnnonceExtraOrdinaire44_01(BSession session, ZuwachsmeldungAO10Type annonce10emeRev) throws Exception {

        /**
         * REMARQUE : - numeroAnnonce : YXNNOA : n'est pas géré lors de la lecture du fichier annonce.xml -
         * nouveauNoAssureAyantDroit : ZENNNEA : pas géré lors de la lecture du fichier annonce.xml
         */

        String codeApplication = "44";
        String codeEnregistrement01 = "01";
        String etat = IREAnnonces.CS_ETAT_OUVERT;
        String numeroCaisse = "";
        String numeroAgence = "";
        String referenceCaisseInterne = "";
        String noAssAyantDroit = "";
        String premierNoAssComplementaire = "";
        String secondNoAssComplementaire = "";
        String etatCivil = "";
        String isRefugie = "";
        String cantonEtatDomicile = "";
        String genrePrestation = "";
        String debutDroit = "";
        String mensualitePrestationsFrancs = "";
        String finDroit = "";
        String codeMutation = "";
        String moisRapport = "";

        /* (String) : contient 6 positions : numeroCaisse (3pos) + numeroAgence (3pos) */
        String noCaisseEtAgence = ParserUtils.formatIntToStringWithSixChar(annonce10emeRev.getKasseZweigstelle());
        if (noCaisseEtAgence.length() >= 3) {
            /* ZAANOC : 3 positions */
            numeroCaisse = noCaisseEtAgence.substring(0, 3);
        }
        if (noCaisseEtAgence.length() >= 6) {
            /* ZAANOA : 3 positions */
            numeroAgence = noCaisseEtAgence.substring(3, 6);
        }

        /* YXLREI : composé de 'AUG' et du nom de l'utilisateur, le tout en UpperCase */
        String userName = session.getUserId();
        referenceCaisseInterne = "AUG";
        if (userName != null) {
            referenceCaisseInterne = referenceCaisseInterne + userName.toUpperCase();
        }

        /*
         * YXDMRA (XMLGregorianCalendar) : format AAAAMM. Cette valeur est récupérée que pour la validation avec Anakin,
         * elle n'est pas stockée.
         */
        moisRapport = ParserUtils.formatDateToAAAAMM(annonce10emeRev.getBerichtsmonat());

        /* Ayant-droit */
        if (annonce10emeRev.getLeistungsberechtigtePerson() != null) {
            RRLeistungsberechtigtePersonAuslType leistungsberechtigtePerson = annonce10emeRev
                    .getLeistungsberechtigtePerson();

            /* String : NSS */
            if (Objects.nonNull(leistungsberechtigtePerson.getVersichertennummer())) {
                noAssAyantDroit = leistungsberechtigtePerson.getVersichertennummer();
            }

            /* Parenté */
            if (leistungsberechtigtePerson.getFamilienAngehoerige() != null) {
                FamilienAngehoerigeType familienAngehoerigeType = leistungsberechtigtePerson.getFamilienAngehoerige();
                if (!familienAngehoerigeType.getVNr1Ergaenzend().isEmpty()) {
                    /* YXNPNA (String) : format NSS complet sur 13 position sans point */
                    String nssCompl1 = familienAngehoerigeType.getVNr1Ergaenzend().get(0);
                    if (nssCompl1 != null) {
                        if (nssCompl1.equals("0")) {
                            premierNoAssComplementaire = EMPTY_NSS;
                        } else {
                            PRTiersWrapper tiers1 = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(nssCompl1));
                            if (tiers1 != null) {
                                premierNoAssComplementaire = nssCompl1;
                            }
                        }
                    }
                }
                if (!familienAngehoerigeType.getVNr2Ergaenzend().isEmpty()) {
                    /* YXNDNA (String) : format NSS complet sur 13 position sans point */
                    String nssCompl2 = familienAngehoerigeType.getVNr2Ergaenzend().get(0);
                    // K170302_002
                    if (!JadeStringUtil.isBlankOrZero(nssCompl2)) {
                        secondNoAssComplementaire = NSUtil
                                .unFormatAVS(definirNssComplementaire(session, NSUtil.formatAVSUnknown(nssCompl2)));
                    } else {
                        secondNoAssComplementaire = EMPTY_NSS;
                    }
                }
            }

            /* YXLETC (short) : type intrinsèque, sera toujours présent (non null), 1 position */
            etatCivil = ParserUtils.formatShortToString(leistungsberechtigtePerson.getZivilstand());

            /* YXBREF (boolean) : type intrinsèque, sera toujours présent (non null), 1 position */
            isRefugie = ParserUtils.formatBooleanToString(leistungsberechtigtePerson.isIstFluechtling());

            /* YXLCAN (String) : 3 position, si null -> vide sinon formaté sur 3 positions avec des 0 */
            String canton = ParserUtils.formatIntegerToString(leistungsberechtigtePerson.getWohnkantonStaat());
            if (StringUtils.isNotEmpty(canton)) {
                cantonEtatDomicile = canton;
                cantonEtatDomicile = ParserUtils.indentLeftWithZero(cantonEtatDomicile, 3);
            }
        }

        /* Description des prestations */
        if (annonce10emeRev.getLeistungsbeschreibung() != null) {
            ZuwachsmeldungAO10Type.Leistungsbeschreibung leistungsbeschreibung = annonce10emeRev
                    .getLeistungsbeschreibung();

            /* YXLGEN (String) : pas de format particulier, juste la valeur. Si vide, valeur sera une chaîne vide */
            if (Objects.nonNull(leistungsbeschreibung.getLeistungsart())) {
                genrePrestation = leistungsbeschreibung.getLeistungsart();
            }

            /* YXDDEB (XMLGregorianCalendar) : format MMAA */
            debutDroit = ParserUtils.formatDateToMMAA(leistungsbeschreibung.getAnspruchsbeginn());

            /* YXDFIN (XMLGregorianCalendar) : format MMAA */
            finDroit = ParserUtils.formatDateToMMAA(leistungsbeschreibung.getAnspruchsende());

            /* YXMMEN (BigDecimal) : 5 position, si vide que des 0 */
            BigDecimal mensualite = leistungsbeschreibung.getMonatsbetrag();
            if (mensualite != null) {
                mensualitePrestationsFrancs = ParserUtils.formatBigDecimalToString(mensualite);
                mensualitePrestationsFrancs = ParserUtils.indentLeftWithZero(mensualitePrestationsFrancs, 5);
            }

            /* YXLCOM : code mutation. Vide si pas de valeur (0 veut dire pas de valeur) sinon valeur sur 2 position */
            Short v7 = leistungsbeschreibung.getMutationscode();
            if (v7 != null) {
                codeMutation = ParserUtils.indentLeftWithZero(String.valueOf(v7), 2);
            }
        }

        // Affectation des valeurs récupérées et formatées
        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setCodeApplication(codeApplication);
        annonce44.setCodeEnregistrement01(codeEnregistrement01);
        annonce44.setEtat(etat);
        annonce44.setNumeroCaisse(numeroCaisse);
        annonce44.setNumeroAgence(numeroAgence);
        annonce44.setReferenceCaisseInterne(referenceCaisseInterne);
        annonce44.setNoAssAyantDroit(noAssAyantDroit);
        annonce44.setPremierNoAssComplementaire(premierNoAssComplementaire);
        annonce44.setSecondNoAssComplementaire(secondNoAssComplementaire);
        annonce44.setEtatCivil(etatCivil);
        annonce44.setIsRefugie(isRefugie);
        annonce44.setCantonEtatDomicile(cantonEtatDomicile);
        annonce44.setGenrePrestation(genrePrestation);
        annonce44.setDebutDroit(debutDroit);
        annonce44.setMensualitePrestationsFrancs(mensualitePrestationsFrancs);
        annonce44.setFinDroit(finDroit);
        annonce44.setCodeMutation(codeMutation);
        annonce44.setMoisRapport(moisRapport);
        return annonce44;
    }

    /**
     * Construit l'ensemble des annonces 44_02</br>
     * Graph d'object lié à une annonce 44_02 :
     * REANHEA->REAAL1A->REAAL2A->REAAL3A->REAAL4A</br>
     * L'annonce n'est pas persisté dans cette méthode, l'idTiers n'est
     * pas définit dans cette méthode c.f buildAnnonce44(...)</br>
     *
     * @param session         La session en cours
     * @param annonce10emeRev L'annonce 10ème révision récupéré depuis le XML et généré par JaxB
     * @return Une instance de la classe REAnnoncesAugmentationModification10Eme remplie mais non persisté
     * @throws Exception
     */
    private REAnnoncesAugmentationModification10Eme buildAnnonceExtraOrdinaire44_02(BSession session, ZuwachsmeldungAO10Type annonce10emeRev) {

        // Ensemble des valeurs qui vont être lues
        String codeApplication = "44";
        String codeEnregistrement = "02";
        String etat = IREAnnonces.CS_ETAT_OUVERT;
        String anneeNiveau = "";
        String echelleRente = "";
        String dureeCoEchelleRenteAv73 = "";
        String dureeCoEchelleRenteDes73 = "";
        String dureeCotManquante48_72 = "";
        String dureeCotManquante73_78 = "";
        String anneeCotClasseAge = "";
        String ramDeterminant = "";
        String dureeCotPourDetRAM = "";
        String codeRevenuSplitte = "";
        String nombreAnneeBTE = "";
        String nbreAnneeBTA = "";
        String nbreAnneeBonifTrans = "";
        String officeAICompetent = "";
        String degreInvalidite = "";
        String codeInfirmite = "";
        String survenanceEvenAssure = "";
        String ageDebutInvalidite = "";
        String dureeAjournement = "";
        String dateRevocationAjournement = "";
        String supplementAjournement = "";
        String nbreAnneeAnticipation = "";
        String dateDebutAnticipation = "";
        String reductionAnticipation = "";
        String reduction = "";
        String isSurvivant = "";
        String codeCasSpecial1 = "";
        String codeCasSpecial2 = "";
        String codeCasSpecial3 = "";
        String codeCasSpecial4 = "";
        String codeCasSpecial5 = "";

        if (annonce10emeRev.getLeistungsbeschreibung() != null) {
            ZuwachsmeldungAO10Type.Leistungsbeschreibung leistungsbeschreibung = annonce10emeRev
                    .getLeistungsbeschreibung();

            /* YYLRED (Short) : si pas de valeur = vide, sinon la valeur formatée sur 2 position */
            Short reductionShort = leistungsbeschreibung.getKuerzungSelbstverschulden();
            if (reductionShort != null) {
                reduction = ParserUtils.formatShortToString(reductionShort);
                reduction = ParserUtils.indentLeftWithZero(reduction, 2);
            }

            /* YZBSUR (Boolean) : si null -> pas de valeur sinon valeur 0 ou 1 */
            Boolean survivantBoolean = leistungsbeschreibung.isIstInvaliderHinterlassener();
            if (survivantBoolean != null) {
                isSurvivant = ParserUtils.formatBooleanToString(survivantBoolean);
            }

            /* YYLCS1 à YYLCS5 (Short) : Si null ou vide, pas de valeur sinon formaté sur 2 positions */
            List<Short> codesCasSpeciaux = annonce10emeRev.getLeistungsbeschreibung().getSonderfallcodeRente();
            if (codesCasSpeciaux != null) {
                if (codesCasSpeciaux.size() > 0) {
                    codeCasSpecial1 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0));
                }
                if (codesCasSpeciaux.size() > 1) {
                    codeCasSpecial2 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1));
                }
                if (codesCasSpeciaux.size() > 2) {
                    codeCasSpecial3 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2));
                }
                if (codesCasSpeciaux.size() > 3) {
                    codeCasSpecial4 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3));
                }
                if (codesCasSpeciaux.size() > 4) {
                    codeCasSpecial5 = ParserUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4));
                }
            }

            if (leistungsbeschreibung.getBerechnungsgrundlagen() != null) {
                ZuwachsmeldungAO10Type.Leistungsbeschreibung.Berechnungsgrundlagen berechnungsgrundlagen = leistungsbeschreibung
                        .getBerechnungsgrundlagen();

                /* YYDANI (XMLGregorianCalendar) : format AA. si null pas de valeur */
                XMLGregorianCalendar value = berechnungsgrundlagen.getNiveaujahr();
                if (value != null) {
                    String tmp1 = String.valueOf(value.getYear());
                    if (tmp1.length() == 4) {
                        anneeNiveau = tmp1.substring(2, 4);
                    }
                }

                if (berechnungsgrundlagen.getIVDaten() != null) {
                    IVDaten10Type iVDaten10Type = berechnungsgrundlagen.getIVDaten();

                    /* YYLOAI (int) : valeur sans formatage */
                    officeAICompetent = ParserUtils.formatIntegerToString(iVDaten10Type.getIVStelle());

                    /* YYNDIN (short) : 3 position formaté */
                    degreInvalidite = ParserUtils.formatShortToString(iVDaten10Type.getInvaliditaetsgrad());
                    degreInvalidite = ParserUtils.indentLeftWithZero(degreInvalidite, 3);

                    /* code infirmité, 3 position formaté */
                    String codeInf = ParserUtils.formatIntegerToString(iVDaten10Type.getGebrechensschluessel());
                    codeInf = ParserUtils.indentLeftWithZero(codeInf, 3);

                    /* code atteinte fonctionnel, si null -> pas de valeur sinon 2 position formaté */
                    String codeAtteinte = "";
                    Short ca = iVDaten10Type.getFunktionsausfallcode();
                    if (ca != null) {
                        codeAtteinte = ParserUtils.formatShortToString(ca);
                        codeAtteinte = ParserUtils.indentLeftWithZero(codeAtteinte, 2);
                    }

                    /*
                     * YYNCOI : concaténation du code infirmité (3pos formaté) + code atteinte fonctionnel (2pos
                     * formaté)
                     */
                    codeInfirmite = codeInf + codeAtteinte;

                    /* YYNSUR : si valeur null -> chaîne vide sinon date au format MMAA */
                    survenanceEvenAssure = ParserUtils.formatDateToMMAA(iVDaten10Type.getDatumVersicherungsfall());

                    /* YYNAGE (boolean) : Valeur 0 ou 1. Si invalide avant 25 ans 1 sinon 0 */
                    ageDebutInvalidite = ParserUtils.formatBooleanToString(iVDaten10Type.isIstFruehInvalid());
                }
            }
        }

        // Affectation des valeurs récupérées et traitées
        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setSession(session);
        annonce44.setCodeApplication(codeApplication);
        annonce44.setCodeEnregistrement01(codeEnregistrement);
        annonce44.setEtat(etat);
        annonce44.setAnneeNiveau(anneeNiveau);
        annonce44.setEchelleRente(echelleRente);
        annonce44.setDureeCoEchelleRenteAv73(dureeCoEchelleRenteAv73);
        annonce44.setDureeCoEchelleRenteDes73(dureeCoEchelleRenteDes73);
        annonce44.setDureeCotManquante48_72(dureeCotManquante48_72);
        annonce44.setDureeCotManquante73_78(dureeCotManquante73_78);
        annonce44.setAnneeCotClasseAge(anneeCotClasseAge);
        annonce44.setRamDeterminant(ramDeterminant);
        annonce44.setDureeCotPourDetRAM(dureeCotPourDetRAM);
        annonce44.setCodeRevenuSplitte(codeRevenuSplitte);
        annonce44.setNombreAnneeBTE(nombreAnneeBTE);
        annonce44.setNbreAnneeBTA(nbreAnneeBTA);
        annonce44.setNbreAnneeBonifTrans(nbreAnneeBonifTrans);
        annonce44.setOfficeAICompetent(officeAICompetent);
        annonce44.setDegreInvalidite(degreInvalidite);
        annonce44.setCodeInfirmite(codeInfirmite);
        annonce44.setSurvenanceEvenAssure(survenanceEvenAssure);
        annonce44.setAgeDebutInvalidite(ageDebutInvalidite);
        annonce44.setDureeAjournement(dureeAjournement);
        annonce44.setDateRevocationAjournement(dateRevocationAjournement);
        annonce44.setSupplementAjournement(supplementAjournement);
        annonce44.setNbreAnneeAnticipation(nbreAnneeAnticipation);
        annonce44.setDateDebutAnticipation(dateDebutAnticipation);
        annonce44.setReductionAnticipation(reductionAnticipation);
        annonce44.setReduction(reduction);
        annonce44.setIsSurvivant(isSurvivant);
        annonce44.setCasSpecial1(codeCasSpecial1);
        annonce44.setCasSpecial2(codeCasSpecial2);
        annonce44.setCasSpecial3(codeCasSpecial3);
        annonce44.setCasSpecial4(codeCasSpecial4);
        annonce44.setCasSpecial5(codeCasSpecial5);
        annonce44.setGenreDroitAPI(""); // On n'aura jamais cette information car les API ne sont pas calculé par ACOR
        return annonce44;
    }


}
