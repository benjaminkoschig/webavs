package ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde;

import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.babel.BabelTextDefinition;
import ch.globaz.common.document.babel.BabelTextDefinitionImpl;
import ch.globaz.common.domaine.Checkers;

/**
 * Cette classe représente l'implémentation de la page de garde pour les décisions de transfert.
 * 
 * @author jwe
 * 
 */
public class PageDeGardeDefinition implements IPageDeGardeDefinition {

    public PageDeGardeDefinition(TextGiver<BabelTextDefinition> textGiver) {
        Checkers.checkNotNull(textGiver, "textGiver");
        this.textGiver = textGiver;
    }

    // Déclaré avec connaissance du catalogue de texte
    private TextGiver<BabelTextDefinition> textGiver = null;
    private static final BabelTextDefinition INTRO = new BabelTextDefinitionImpl(1, 1, "INTRO", "");
    private static final BabelTextDefinition POLITESSE = new BabelTextDefinitionImpl(1, 6, "POLITESSE",
            "Formule de politesse. Exemple: Madame, Monsieur");
    private static final BabelTextDefinition ANNEXE = new BabelTextDefinitionImpl(1, 2, "ANNEXE", "Annexes du document");
    private static final BabelTextDefinition ANNEXE_MENT = new BabelTextDefinitionImpl(1, 3, "ANNEXE_MENT",
            "Annexes mentionnées du document");
    private static final BabelTextDefinition SALUTATIONS_PART_1 = new BabelTextDefinitionImpl(1, 4,
            "SALUTATIONS_PART_1", "Salutations du document partie 1");
    private static final BabelTextDefinition SALUTATIONS_PART_2 = new BabelTextDefinitionImpl(1, 5,
            "SALUTATIONS_PART_2", "Salutations du document partie 2");
    private static final BabelTextDefinition SIGNATURE_NOM_CAISSE = new BabelTextDefinitionImpl(19, 2,
            "SIGNATURE_NOM_CAISSE", "");
    private static final BabelTextDefinition SIGNATAIRE = new BabelTextDefinitionImpl(19, 3, "SIGNATAIRE", "");
    private static final BabelTextDefinition SIGNATURE_NOM_SERVICE = new BabelTextDefinitionImpl(19, 4,
            "SIGNATURE_NOM_SERVICE", "");

    private static final BabelTextDefinition PERSONNE_DE_REFERENCE = new BabelTextDefinitionImpl(1, 8,
            "PERSONNE_DE_REFERENCE", "personne de référence");

    private BabelTextDefinition TEL_COLLABORATEUR = new BabelTextDefinitionImpl(1, 9, "TEL_COLLABORATEUR",
            "téléphone du collaborateur");
    private BabelTextDefinition DATE_ET_LIEU = new BabelTextDefinitionImpl(1, 7, "DATE_ET_LIEU",
            "lieu pour date et lieu du document");
    // Déclaré avec uniquement la clé
    private BabelTextDefinition N_REF = new BabelTextDefinitionImpl(1, 10, "N_REF", "Numéro de référence");
    private BabelTextDefinition V_REF = new BabelTextDefinitionImpl(1, 11, "V_REF", "votre reference");
    private BabelTextDefinition SERVICE_COLLABORATEUR = new BabelTextDefinitionImpl("SERVICE_COLLABORATEUR");
    private BabelTextDefinition EMAIL_COLLABORATEUR = new BabelTextDefinitionImpl("EMAIL_COLLABORATEUR");
    private BabelTextDefinition TEL_GESTIONNAIRE = new BabelTextDefinitionImpl("TEL_GESTIONNAIRE");
    private BabelTextDefinition GESTIONNAIRE = new BabelTextDefinitionImpl("GESTIONNAIRE");
    private BabelTextDefinition ID_USER = new BabelTextDefinitionImpl("ID_USER");
    private BabelTextDefinition NOM_COLLABORATEUR = new BabelTextDefinitionImpl("NOM_COLLABORATEUR");
    private BabelTextDefinition SALUTATIONS = new BabelTextDefinitionImpl("SALUTATIONS");
    private BabelTextDefinition SIGNATURE = new BabelTextDefinitionImpl("SIGNATURE");
    private BabelTextDefinition SIGNATURE_GESTIONNAIRE = new BabelTextDefinitionImpl("SIGNATURE_GESTIONNAIRE");
    private BabelTextDefinition NSS_BENEFICIAIRE = new BabelTextDefinitionImpl("NSS_BENEFICIAIRE");
    private BabelTextDefinition ADRESSE = new BabelTextDefinitionImpl("ADRESSE");
    private BabelTextDefinition header = new BabelTextDefinitionImpl("header");

    @Override
    public BabelTextDefinition getPolitesse() {
        return POLITESSE;
    }

    @Override
    public BabelTextDefinition getSalutationPart1() {
        return SALUTATIONS_PART_1;
    }

    @Override
    public BabelTextDefinition getSalutationPart2() {
        return SALUTATIONS_PART_2;
    }

    @Override
    public BabelTextDefinition getIntro() {
        return INTRO;
    }

    @Override
    public BabelTextDefinition getSignatureNomCaisse() {
        return SIGNATURE_NOM_CAISSE;
    }

    @Override
    public BabelTextDefinition getSignataire() {
        return SIGNATAIRE;
    }

    @Override
    public BabelTextDefinition getSignatureNomService() {
        return SIGNATURE_NOM_SERVICE;
    }

    @Override
    public BabelTextDefinition getAnnexes() {
        return ANNEXE;
    }

    @Override
    public BabelTextDefinition getAnnexesMent() {
        return ANNEXE_MENT;
    }

    @Override
    public BabelTextDefinition getSalutations(String formulePolitesse, String langueIso) {
        String salutations = "";
        if ("de".equalsIgnoreCase(langueIso)) {
            salutations = textGiver.resolveText(SALUTATIONS_PART_1);
        } else {
            salutations = textGiver.resolveText(SALUTATIONS_PART_1) + " " + formulePolitesse + " "
                    + textGiver.resolveText(SALUTATIONS_PART_2);
        }

        SALUTATIONS = new BabelTextDefinitionImpl(salutations, "SALUTATIONS", "salutations");
        return SALUTATIONS;
    }

    @Override
    public BabelTextDefinition getSignature(String signature) {
        SIGNATURE = new BabelTextDefinitionImpl(signature, "signature", "signature");
        return SIGNATURE;

    }

    @Override
    public BabelTextDefinition getSignatureGestionnaire(String gestionnaire) {
        SIGNATURE_GESTIONNAIRE = new BabelTextDefinitionImpl(gestionnaire, "SIGNATURE_GESTIONNAIRE",
                "signature du gestionnaire");
        return SIGNATURE_GESTIONNAIRE;
    }

    @Override
    public BabelTextDefinition getPersonneDeReference() {
        return PERSONNE_DE_REFERENCE;
    }

    @Override
    public BabelTextDefinition getTelCollaborateur() {
        return TEL_COLLABORATEUR;
    }

    @Override
    public BabelTextDefinition getNReference() {
        return N_REF;
    }

    @Override
    public BabelTextDefinition getNReference(String nRef) {
        nRef = textGiver.resolveText(N_REF) + " " + nRef;
        N_REF = new BabelTextDefinitionImpl(nRef, "N_REF", "numéro de référence du collaborateur");
        return N_REF;
    }

    @Override
    public BabelTextDefinition getVReference() {
        return V_REF;
    }

    @Override
    public BabelTextDefinition getNomCollaborateur(String nomCollaborateur) {
        NOM_COLLABORATEUR = new BabelTextDefinitionImpl(nomCollaborateur, "NOM_COLLABORATEUR", "nom du collaborateur");
        return NOM_COLLABORATEUR;
    }

    @Override
    public BabelTextDefinition getTelCollaborateur(String tel) {
        String telCollabo = textGiver.resolveText(TEL_COLLABORATEUR);
        TEL_COLLABORATEUR = new BabelTextDefinitionImpl(tel + " " + telCollabo, "TEL_COLLABORATEUR",
                "téléphone du collaborateur");
        return TEL_COLLABORATEUR;
    }

    @Override
    public BabelTextDefinition getServiceCollaborateur(String serviceCollaborateur) {
        SERVICE_COLLABORATEUR = new BabelTextDefinitionImpl(serviceCollaborateur, "SERVICE_COLLABORATEUR",
                "service du collaborateur");
        return SERVICE_COLLABORATEUR;
    }

    @Override
    public BabelTextDefinition getEmailCollaborateur(String emailCollaborateur) {
        EMAIL_COLLABORATEUR = new BabelTextDefinitionImpl(emailCollaborateur, "EMAIL_COLLABORATEUR",
                "email du collaborateur");
        return EMAIL_COLLABORATEUR;
    }

    @Override
    public BabelTextDefinition getTelGestionnaire(String telGestionnaire) {
        TEL_GESTIONNAIRE = new BabelTextDefinitionImpl(telGestionnaire, "TEL_GESTIONNAIRE",
                "numéro de téléphone du gestionnaire");
        return TEL_GESTIONNAIRE;
    }

    @Override
    public BabelTextDefinition getGestionnaire(String gestionnaire) {
        GESTIONNAIRE = new BabelTextDefinitionImpl(gestionnaire, "GESTIONNAIRE", "nom - prénom du gestionnaire");
        return GESTIONNAIRE;
    }

    @Override
    public BabelTextDefinition getIdUser(String idUser) {
        ID_USER = new BabelTextDefinitionImpl(idUser, "ID_USER", "id utilisateur");
        return ID_USER;
    }

    @Override
    public BabelTextDefinition getNSSBeneficiare(String nssBeneficiaire) {
        NSS_BENEFICIAIRE = new BabelTextDefinitionImpl(nssBeneficiaire, "NSS_BENEFICIAIRE", "NSS du bénéficiaire");
        return NSS_BENEFICIAIRE;
    }

    @Override
    public BabelTextDefinition getVReference(String vRef) {
        vRef = textGiver.resolveText(V_REF) + " " + vRef;
        V_REF = new BabelTextDefinitionImpl(vRef, "V_REF", "votre référence");
        return V_REF;
    }

    @Override
    public BabelTextDefinition getDateEtLieu(String dateLieu) {
        String lieu = textGiver.resolveText(DATE_ET_LIEU);
        DATE_ET_LIEU = new BabelTextDefinitionImpl(lieu + " " + dateLieu, "DATE_ET_LIEU",
                "lieu pour date et lieu du document");
        return DATE_ET_LIEU;
    }

    @Override
    public BabelTextDefinition getAdresse(String adresse) {
        ADRESSE = new BabelTextDefinitionImpl(adresse, "ADRESSE", "adresse du destinataire");
        return ADRESSE;
    }

    @Override
    public BabelTextDefinition getDateEtLieu() {
        return DATE_ET_LIEU;
    }

    @Override
    public BabelTextDefinition getHeader(String headerDoc) {
        header = new BabelTextDefinitionImpl(headerDoc, "header", "header du document");
        return header;
    }

    @Override
    public TextGiver<BabelTextDefinition> getTextGiver() {
        return textGiver;
    }
}
