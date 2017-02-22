package ch.globaz.naos.ree.domain.converter;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.naos.util.AFIDEUtil;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ree.ch.admin.bfs.xmlns.bfs_5053_000101._1.MasterDataType;
import ree.ch.ech.xmlns.ech_0010._4.AddressInformationType;
import ree.ch.ech.xmlns.ech_0097._1.UidOrganisationIdCategorieType;
import ree.ch.ech.xmlns.ech_0097._1.UidStructureType;
import ch.globaz.common.domaine.Adresse;
import ch.globaz.naos.ree.domain.AdresseLoader;
import ch.globaz.naos.ree.domain.SuiviCaisseLoader;
import ch.globaz.naos.ree.domain.pojo.AdresseCache;
import ch.globaz.naos.ree.domain.pojo.AdresseCacheNoMatchException;
import ch.globaz.naos.ree.domain.pojo.Pojo5053_101;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pyxis.business.service.AdresseService;

public class Converter5053_101 implements Converter<Pojo5053_101, MasterDataType> {
    private static final Logger LOG = LoggerFactory.getLogger(Converter5053_101.class);
    /**
     * Valeur utilisée dans la requête pour classer les coti encore ouverte en tête
     */
    private static final String MAX_SELECT_DATE_FIN = "99999999";
    private SuiviCaisseLoader suivis;
    private InfoCaisse currentCaisse;
    private AdresseCache adresses;
    private BSession session;

    /**
     * @param ids Map<String idTiers,String numAff>
     * @param idAffilies
     * @param infoCaisse
     * @param session
     */
    public Converter5053_101(Map<String, String> ids, List<String> idAffilies, InfoCaisse infoCaisse, BSession session) {// List<String>
                                                                                                                         // idAff,
        // charger les suivis de caisse
        suivis = new SuiviCaisseLoader(session);
        suivis.load(idAffilies);
        // charger les adresses
        AdresseLoader adresseLoader = new AdresseLoader(session);
        adresses = new AdresseCache(adresseLoader.loadLastByIdsTiersAndGroupByIdTiers(ids,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT, AdresseService.CS_TYPE_DOMICILE));
        currentCaisse = infoCaisse;
        this.session = session;
    }

    @Override
    public MasterDataType convert(Pojo5053_101 businessMessage) throws REEBusinessException {
        return convertPojo(businessMessage);
    }

    private MasterDataType convertPojo(Pojo5053_101 pojo) throws REEBusinessException {
        MasterDataType masterData = new MasterDataType();
        try {
            try {
                masterData.setEnteringDate(ConverterUtils.convertToXMLGregorianCalendar(pojo.getDateDebutCotisation()));
            } catch (Exception e) {
                LOG.debug("[505300101 : setEnteringDate] Date d'entrée non convertie {}", e);
                throw new REEBusinessException("La donnée 'Date de debut de Cotisation' est inexploitable ["
                        + pojo.getDateDebutCotisation() + "]");
            }
            String enteringReasonCode = "X";
            if (suivis.hasBefore(pojo.getIdAffilie())) {
                if (pojo.getDateDebutAffiliation().compareTo(suivis.getBefore(pojo.getIdAffilie()).getDateFinSuivi()) > 0) {
                    enteringReasonCode = "K";
                }
            } else {
                if (pojo.getMotifAffiliation().equals("816001")) {
                    enteringReasonCode = "N";
                }
            }
            masterData.setEnteringReason(enteringReasonCode);

            String leavingReasonCode = "X";
            if (pojo.getDateFinCotisation() != null && !MAX_SELECT_DATE_FIN.equals(pojo.getDateFinCotisation())) {// affiliation
                try {
                    masterData
                            .setLeavingDate(ConverterUtils.convertToXMLGregorianCalendar(pojo.getDateFinCotisation()));
                } catch (Exception e) {
                    LOG.debug("[505300101 : setLeavingDate] Date fin non convertie {}", e);
                    throw new REEBusinessException("La donnée 'Date de fin de Cotisation' est inexploitable ["
                            + pojo.getDateFinCotisation() + "]");
                }
                leavingReasonCode = mappingLeavingReason(pojo, suivis);
                masterData.setLeavingReason(leavingReasonCode);
            }

            if (leavingReasonCode.equalsIgnoreCase("K")) {
                masterData.setLeavingCompensationOffice(ConverterUtils.formatCodeCaisse(suivis.getAfter(
                        pojo.getIdAffilie()).getCodeCaisse()));
            }
            if (enteringReasonCode.equalsIgnoreCase("K")) {
                masterData.setEnteringCompensationOffice(ConverterUtils.formatCodeCaisse(suivis.getBefore(
                        pojo.getIdAffilie()).getCodeCaisse()));
            }

            masterData.setAccountNumber(pojo.getNumeroAffilie());
            masterData.setCompensationOffice(ConverterUtils.formatCodeCaisse(currentCaisse.getNumeroCaisse() + "."
                    + currentCaisse.getNumeroAgenceFormate()));

            if (pojo.getNumeroIde() != null && !pojo.getNumeroIde().isEmpty()) {
                UidStructureType uidStructureType = new UidStructureType();
                uidStructureType.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
                uidStructureType.setUidOrganisationId(intUnformatedNumIde(pojo.getNumeroIde()));
                masterData.setUid(uidStructureType);
            }

            masterData
                    .setLegalForm(determinerLegalForm(pojo.getFormeJuridique(), session, pojo.getBrancheEconomique()));

            masterData.setFirmForm(BigInteger.valueOf(determinerFirmForm(masterData.getLegalForm(),
                    pojo.getTypeEntreprise())));
            masterData.setVn(ConverterUtils.formatNssInLong(pojo.getNumeroNSS()));

            try {
                masterData.setDateOfBirth(ConverterUtils.defineDatePartiel(pojo.getDateNaissance()));
            } catch (Exception e) {
                LOG.debug("[505300101 : setDateOfBirth] Date de naissance non convertie {}", e);
                throw new REEBusinessException("La donnée 'Date de Naissance' est inexploitable pour l'affilié "
                        + pojo.getNumeroAffilie());
            }

            // Sexe. Vide si non trouvé
            masterData.setSex(ConverterUtils.translateSex(pojo.getSexe()));

            if (isTrue(pojo.getPersonnePhysique())) {
                masterData.setOfficialName(pojo.getNomTiers());
                masterData.setFirstName(pojo.getPrenomTiers());
            }
            if (isTrue(pojo.getPersonneMorale())) {
                masterData.setOrganisationName((pojo.getNomTiers() + " " + pojo.getPrenomTiers()).trim());
            }

            Adresse pojoAdr = adresses.getAdresse(pojo.getIdTiers());
            // Pays, NPA, Localité, Rue
            AddressInformationType adresseInformation = new AddressInformationType();

            adresseInformation.setCountry(pojoAdr.getPays());
            if (isAdresseSuisse(pojoAdr)) {
                adresseInformation.setSwissZipCode(Long.valueOf(pojoAdr.getNpa()));
                // TODO missing suffix in setSwissZipCodeAddOn
            } else {
                adresseInformation.setForeignZipCode(pojoAdr.getNpa());
            }
            adresseInformation.setTown(pojoAdr.getLocalite());
            adresseInformation.setStreet(pojoAdr.getRue());
            adresseInformation.setHouseNumber(pojoAdr.getRueNumero());

            // résoudre la dépendance d'info adr. du tier ou de l'adresse
            adresseInformation.setAddressLine1(pojoAdr.resolveDesignation3());
            adresseInformation.setAddressLine2(pojoAdr.resolveDesignation4());

            masterData.setAddress(adresseInformation);

        } catch (AdresseCacheNoMatchException e) {
            throw new REEBusinessException("Aucune adresse valide trouvée pour l'affilié " + pojo.getNumeroAffilie());
        }
        return masterData;
    }

    private static boolean isAdresseSuisse(Adresse pojoAdr) {
        return pojoAdr.getPays().equalsIgnoreCase("CH");
    }

    private String mappingLeavingReason(Pojo5053_101 pojo, SuiviCaisseLoader suivis) {
        String motifFinCS;
        if (pojo.getDateFinAffiliation() != null && pojo.getDateFinAffiliation().equals(pojo.getDateFinCotisation())) {
            motifFinCS = pojo.getMotifFin();
        } else {
            motifFinCS = pojo.getMotifFinCotisation();
        }
        return translateMotifFinToLeavingreason(motifFinCS, pojo.getIdAffilie(), suivis);
    }

    protected static String translateMotifFinToLeavingreason(String motifFinCS, String idAffilie,
            SuiviCaisseLoader suivis) {
        int value = 0;
        try {
            value = Integer.valueOf(motifFinCS);
        } catch (NumberFormatException e) {
            LOG.debug(
                    "Mapping [translateMotifFinToLeavingreason] Impossible de récupérer la valeur numérique du motif de fin {} : {}",
                    motifFinCS, e.getMessage());
        }
        switch (value) {
            case 803006:// Décès
                return "D";
            case 803004:// Affilie par erreur
                return "E";
            case 803001:// Cessation d'activité
                return "F";
            case 803008:// Retraite
                return "H";
            case 803002:// Changement de caisse
                // Vérif supp, est-ce que j'ai bien un suivi?
                if (!suivis.hasAfter(idAffilie)) {
                    return "X";
                }
                return "K";
            case 803038:// Sans personnel
                return "P";
            case 803040:// Départ hors canton
                return "Q";
            case 803042:// Remise de l'exploitation
                return "R";
            case 803041:// Changement de commune
                return "T";

            default:
                return "X";
        }

    }

    protected static int determinerFirmForm(String LegalForm, String typeEntCS) {
        if ("J".equals(LegalForm)) {
            return 5;
            // Si employé de maison, FormForm = 5
        } else if ("P".equals(LegalForm)) {
            // si LegalForm = P (vérifier immeuble) FirmForm = 7
            return 7;
        }
        return translateTypeEntrepriseToFirmForm(typeEntCS);
    }

    protected static String determinerLegalForm(String persJuriCS, BSession session, String brancheEcoCS) {

        if ("805038".equals(brancheEcoCS)) {
            // Employeur de personnel de maison
            return "J";
        } else {
            return translateFormeJuriToLegalForm(persJuriCS, session);
        }

    }

    /**
     * Mapping personnalité juridique -> LegalForm
     * 
     * @param persJuriCS
     * @return LegalForm Code
     */
    protected static String translateFormeJuriToLegalForm(String persJuriCS, BSession session) {
        int value = 0;
        try {
            value = Integer.valueOf(persJuriCS);
        } catch (NumberFormatException e) {
            LOG.debug(
                    "Mapping [translatePersJuriVersLegalForm] Impossible de récupérer la valeur numérique de la personalité juridique {} : {}",
                    persJuriCS, e.getMessage());
        }
        switch (value) {
            case 806001:// Raison individuelle
                return "A";
            case 806002:// Société en nom collectif
                return "D";
            case 806003:// Société en commandite simple
                return "E";
            case 806004:// Société anonyme
                return "G";
            case 806005:// Société à responsabilité limitée
                return "H";
            case 806006:// Société coopérative
                return "I";
            case 806007:// Société simple
                return "C";
            case 806008:// Société en commandite par actions
                return "K";
            case 806009:// Association
                return "L";
            case 806012:// Corporation de droit public
                return "N";
            case 806013:// Fondation
                return "M";
            case 806014:// Hoirie
                return "F";
            case 806018:// NA
                return "B";
            case 806019:// Indéfini
                return "Z";

            default:
                // si code utilisateur correspond à la valeur P(immeuble)
                return verifierCSImmeuble(value, session);
        }

    }

    /**
     * Vérifier si le code système correspond à la valeur immeuble dans les codes unique au caisse en utilisant la
     * valeur du code que l'on aurait mis à P
     * 
     * @param valueCS
     * @param session
     * @return
     */
    protected static String verifierCSImmeuble(int valueCS, BSession session) {

        if (valueCS > 0 && "P".equalsIgnoreCase(session.getCode("" + valueCS))) {
            return "P";
        }
        return "X";
    }

    protected static int translateTypeEntrepriseToFirmForm(String typeEntCS) {

        int value = 0;
        try {
            value = Integer.valueOf(typeEntCS);
        } catch (NumberFormatException e) {
            LOG.debug(
                    "Mapping [translateTypeEntrepriseToFirmForm] Impossible de récupérer la valeur numérique du type d'entreprise {} : {}",
                    typeEntCS, e.getMessage());
        }
        switch (value) {
            case 804001:// Indépendant
                return 3;
            case 804002:// Employeur
            case 804010:// LTN
                return 1;
            case 804004:// Non-actif
            case 804006:// NA selon art.1a, al.4, let.c
                return 6;
            case 804005:// Indépendant et employeur
                return 2;
            case 804008:// Travailleur sans employeur
            case 804011:// TSE Volontaire
                return 4;
            case 804012:// Empl.D/F
                return 5;

            default:
                return 99;
        }
    }

    private static int intUnformatedNumIde(String numIde) {
        String chiffreIde = AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(numIde);

        return Integer.parseInt(chiffreIde);

    }

    private static boolean isTrue(String jadeBoolStr) {
        if (jadeBoolStr.equals("1")) {
            return true;
        }
        return false;
    }

}
