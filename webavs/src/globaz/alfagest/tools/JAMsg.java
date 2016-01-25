package globaz.alfagest.tools;

/**
 * JAMsg est une classe contenant les messages
 * <i>
 * <p>
 * La classe <code>JAMsg</code> ne fournit que des constantes (statiques).
 * <p>
 * La convention de définition des messages est <b>ROOTcode</b> où:
 * <ul>
 * <li>ROOT = la racine du message indiquant l'origine du message
 * <li>code = un numéro à 4 chiffres identifiant le message
 * </ul>
 * <i>
 * 
 * @author Emmanuel Fleury
 * @version efl - 0.408.000
 */
public class JAMsg {
    // A = Application
    public static String A0001 = "Erreur A0001 - Le programme de création d'identificateurs n'a pas été trouvé";
    public static String A0002 = "Erreur A0002 - L'appel du programme de création d'identificateurs a échoué";
    public static String A0003 = "Erreur A0003 - Erreur lors de l'appel du programme de création d'identificateurs";
    public static String A0004 = "Erreur A0004 - Le programme de calcul du droit n'a pas été trouvé";
    public static String A0005 = "Erreur A0005 - L'appel du programme de calcul du droit a échoué";
    public static String A0006 = "Erreur A0006 - Erreur lors de l'appel du programme de calcul du droit";
    public static String A0007 = "Erreur A0007 - Le programme AS/400 n'a pas été trouvé";
    public static String A0008 = "Erreur A0008 - L'appel du programme AS/400 a échoué";
    public static String A0009 = "Erreur A0009 - Erreur lors de l'appel du programme AS/400";
    public static String A0010 = "Erreur A0010 - Rôle sélectionné différent de rôle attendu";
    public static String A0011 = "Erreur A0011 - Erreur lors de l'appel du programme SERG500";
    public static String A0012 = "Erreur A0012 - Le programme SERG500 n'a pas été trouvé";
    public static String A0013 = "Erreur A0013 - La valeur retour de du programme SERG500 n'est pas valide";
    public static String A0014 = "Erreur A0014 - La date de versement est obligatoire";
    public static String A0015 = "Erreur A0015 - Code non valide, utilisez F4";
    public static String A0016 = "Erreur A0016 - La génération du dossier à échouée dû à une erreur dans le calcul";

    // C = Controler
    // D = DataAccess
    public static String D0001 = "Erreur D0001 - Impossible de charger un enregistrement sans sélection";
    public static String D0002 = "Erreur D0002 - Aucun enregistrement trouvé avec la sélection donnée";
    public static String D0003 = "Erreur D0003 - Erreur de donnée: ";
    public static String D0004 = "Erreur D0004 - Impossible de sauvegarder le détail de prestation";
    public static String D0005 = "Erreur D0005 - Impossible de sauvegarder l'en-tête de prestation";
    public static String D0006 = "Erreur D0006 - Impossible de sauvegarder le récapitulatif de prestation";
    public static String D0007 = "Erreur D0007 - Erreur lors de la suppression de la prestation";
    public static String D0008 = "Erreur D0008 - Impossible de sauvegarder. Données de JBEnfantRow incohérentes";
    public static String D0009 = "Erreur D0009 - Erreur lors du chargement des données depuis la base de données";
    public static String D0010 = "Erreur D0010 - Impossible de sauvegarder la rubrique statistique";
    // I = Informations
    // M = Model
    public static String M0001 = "Erreur M0001 - Aucune ligne n'existe dans l'ensemble à la position indiquée";
    public static String M0002 = "Erreur M0002 - La classe d'accès à la ligne n'a pas été trouvée";
    public static String M0003 = "Erreur M0003 - Aucune ligne sélectionnée dans la table";

    public static String M1001 = "Erreur M1001 - La date n'est pas valide";
    public static String M1002 = "Erreur M1002 - Le code est inconnu";
    public static String M1003 = "Erreur M1003 - Le nombre ne peut pas être négatif";
    public static String M1004 = "Erreur M1004 - Ce champ ne peut être modifié qu'en mode création";
    public static String M1005 = "Erreur M1005 - Le nombre est inférieur au minimum accepté";
    public static String M1006 = "Erreur M1006 - Le nombre est supérieur au maximum accepté";
    public static String M1007 = "Erreur M1007 - Aucune sélection effectuée";
    public static String M1008 = "Erreur M1008 - La date de sélection n'est pas valide";
    public static String M1009 = "Erreur M1009 - La date d'impression n'est pas valide";

    public static String M2001 = "Le nom est obligatoire";
    public static String M2002 = "Le prénom est obligatoire";
    public static String M2003 = "Le numéro AVS est obligatoire";
    public static String M2004 = "Le sexe est obligatoire";
    public static String M2005 = "La date de naissance est obligatoire";
    public static String M2006 = "L'état civil est obligatoire";
    public static String M2007 = "La nationalité est obligatoire";
    public static String M2008 = "Il est obligatoire d'indiquer si l'enfant est capable d'exercer une activité lucrative";
    public static String M2009 = "Le conjoint n'existe pas";
    public static String M2010 = "Le dossier doit être attribué à un allocataire";
    public static String M2011 = "L'allocataire n'existe pas";
    public static String M2012 = "Le dossier doit être lié à une entreprise";
    public static String M2013 = "L'entreprise n'existe pas";
    public static String M2014 = "L'état du dossier doit être renseigné";
    public static String M2015 = "La date de début de validité est obligatoire";
    public static String M2016 = "L'unité de calcul doit être renseignée";
    public static String M2017 = "Le motif de réduction doit être renseigné si un taux est donné";
    public static String M2018 = "Il est obligatoire d'indiquer si l'impôt à la source doit être retenu";
    public static String M2019 = "Le statut familial est obligatoire";
    public static String M2020 = "Le type d'allocataire est obligatoire";
    public static String M2021 = "Le pays de résidence est obligatoire";
    public static String M2022 = "Il est obligatoire d'indiquer si la décision doit être imprimée";
    public static String M2023 = "La date de fin de validité ne peut pas être antérieure à la date de début de validité";
    public static String M2024 = "La date de fin d'activité ne peut pas être antérieure à la date de début d'activité";
    public static String M2025 = "L'entité de provenance de l'adresse est obligatoire";
    public static String M2026 = "Le type d'adresse est obligatoire";
    public static String M2027 = "Il est obligatoire d'indiquer si la formule de politesse doit être imprimée";
    public static String M2028 = "La provenance du donnant droit doit être indiquée";
    public static String M2029 = "Le droit doit être attribué à un dossier";
    public static String M2030 = "Le dossier indiqué n'existe pas";
    public static String M2031 = "Le donnant droit indiqué n'existe pas";
    public static String M2032 = "L'état du droit doit être indiqué";
    public static String M2033 = "La date de début du droit est obligatoire";
    public static String M2034 = "Le type de prestation est obligatoire";
    public static String M2035 = "Le motif de réduction doit être renseigné si un taux est donné";
    public static String M2036 = "La date de fin de droit ne peut pas être antérieure à la date de début de droit";
    public static String M2037 = "Un allocataire doit être sélectionné";
    public static String M2038 = "Un enfant doit être sélectionné";
    public static String M2039 = "Le conjoint doit être lié à un allocataire";
    public static String M2040 = "Le type d'adresse est obligatoire";
    public static String M2041 = "L'adresse de paiement doit être liée à une adresse postale";
    public static String M2042 = "Un allocataire doit être sélectionné";
    public static String M2043 = "Le rôle est obligatoire";
    public static String M2044 = "La date de fin de droit est obligatoire";
    public static String M2045 = "Si une échéance est spécifiée, le motif de l'échéance est obligatoire.";
    public static String M2046 = "Le motif de fin de droit est incorrect";
    public static String M2047 = "Le motif de fin de droit est incorrect";
    public static String M2048 = "Le droit doit être attribué à une personne";
    public static String M2049 = "Le droit doit être attribué à un allocataire";
    public static String M2050 = "La date de versement est obligatoire avec ce type d'allocation";
    public static String M2051 = "Le taux d'allocation de naissance est obligatoire";
    public static String M2052 = "Le nombre de jour du début est obligatoire";
    public static String M2053 = "Le nombre de jour de fin est obligatoire";
    public static String M2054 = "La date de début d'activité est obligatoire";
    public static String M2055 = "Le motif de réduction doit être : COMP/Complet";
    public static String M2056 = "Le motif de réduction doit être différent de : COMP/Complet";
    public static String M2057 = "Le type de prestation ne doit pas être : MEN/Allocation de ménage AF";
    public static String M2058 = "Aucun dossier trouvé pour cet affilié";
    public static String M2059 = "La période doit être renseignée";
    public static String M2060 = "Le lot de versement doit être renseignée";
    public static String M2061 = "Le responsable doit être renseignée";
    public static String M2062 = "L'état du droit doit être renseigné";
    public static String M2063 = "Le motif de réduction doit être renseigné";
    public static String M2064 = "Aucun dossier défini au niveau du conjoint de l'allocataire";
    public static String M2065 = "Aucun conjoint défini pour l'allocataire";
    public static String M2066 = "Vous devez choisir un allocataire";
    public static String M2067 = "Le responsable doit être renseigné";
    public static String M2068 = "La date de début de validité ne peut pas être antérieure à la date de début d'activité";
    public static String M2069 = "La date de fin de validité ne peut pas être postérieure à la date de fin d'activité";
    public static String M2070 = "L'indication d'une localité est obligatoire";
    public static String M2071 = "Le numéro de CCP est obligatoire";
    public static String M2072 = "La banque est obligatoire";
    public static String M2073 = "Le numéro de compte est obligatoire";
    public static String M2074 = "La personne sélectionnée n'a pas d'adresse de paiement";
    public static String M2075 = "L'id-dossier père est obligatoire (ZIDDP)";
    public static String M2076 = "L'id-dossier fils est obligatoire (ZIDDF)";
    public static String M2077 = "Le type de relation est obligatoire (ZREL)";
    public static String M2078 = "La date <<à verser le ..>> est obligatoire";

    /*
     * Messages d'erreurs pour les suppressions
     */
    public static String M2079 = "Erreur M2079 - Seuls les décomptes ADI en état PR peuvent être supprimés";
    public static String M2080 = "Erreur M2080 - Un allocataire ne peut pas être supprimer s'il a des dossiers";
    public static String M2081 = "Erreur M2081 - Un conjoint ne peut pas être supprimé s'il est lié à un allocataire";
    public static String M2082 = "Erreur M2082 - Un enfant ne peut pas être supprimé s'il a des droits";

    // processus PR
    public static String M2083 = "Erreur M2083 - Une prestation ne peut être supprimée que si elle n'est pas comptabilisée et que sont état est PR";
    // processus SA
    public static String M2084 = "Erreur M2084 - Une prestation ne peut être supprimée que si elle n'est pas comptabilisée et que sont état est PR ou TR";

    // processus PR
    public static String M2086 = "Erreur M2086 - Un récapitulatif ne peut être supprimée si sont état est PR";
    // processus SA
    public static String M2087 = "Erreur M2087 - Un récapitulatif ne peut être supprimée si sont état est PR ou TR";

    public static String M2088 = "Erreur M2088 - Un bénéficiaire ne peut pas être supprimé s'il est lié à un/des dossiers et/ou droits";

    // Génération des prestations
    public static String M3001 = "Erreur M3001 - La récap. ou le type de bonification est incorrect";
    public static String M3002 = "Erreur M3002 - Une prestation existe déjà pour la période à générer.";
    public static String M3003 = "Erreur M3003 - Des prestations existes déjà pour ce dossier. Impossible de forcée le montant";
    public static String M3004 = "Erreur M3004 - Impossible d'affecter une valeur à la recap. Recap inexistante.";
    public static String M3005 = "Erreur M3005 - Impossible d'affecter l'ID de recap pour les ADI Suisse";
    public static String M3006 = "Erreur M3006 - Aucun paramètre existant pour la génération unitaire.";
    public static String M3007 = "Erreur M3007 - Impossible de générer la prestation de naissance";
    public static String M3008 = "Erreur M3008 - Le calcul des droits du dossier à échoué";
    public static String M3009 = "Erreur M3009 - Le récapitulatif n'a pas pu être initialisé";
    public static String M3010 = "Erreur M3010 - Impossible de générer la prestation de type *ERR";
    public static String M3011 = "Erreur M3011 - Impossible d'extourner ou supprimer l'ancienne prestation";
    public static String M3012 = "Erreur M3012 - Attention, une prestation identique existe déjà ! Celle-ci a été supprimer automatiquement de la base de données";
    public static String M3013 = "Erreur M3013 - L'état de la recap de genre <Caisse cantonale> ne correspond pas à celle permise";
    public static String M3014 = "Erreur M3014 - L'état de la recap de genre <Caisse horlogère> ne correspond pas à celle permise";
    public static String M3015 = "Erreur M3015 - Recap inexistante !";
    public static String M3016 = "Erreur M3016 - Le type de bonification diffère entre la récap et l'en-tête";
    public static String M3017 = "Erreur M3017 - Le type de bonification n'est pas compatible avec le mode de paiement";
    public static String M3018 = "Erreur M3018 - La date est composée de caractères invalides";
    public static String M3019 = "Erreur M3019 - Aucun code de processus correspondant lors de l'exécution";
    public static String M3020 = "Erreur M3020 - Aucune date valide trouver pour exécuter le processus";
    public static String M3021 = "Erreur M3021 - Impossible de trouver un incrément valide pour le numéro de passage";
    public static String M3022 = "Erreur M3022 - Le montant de la prestation dont l'unité a été forcée est supérieur au montant légal";
    public static String M3023 = "Erreur M3023 - Impossible de générer une allocation de naissance pour un dossier bilatéral";
    public static String M3024 = "Erreur M3024 - Impossible d'affecter une valeur à un élément de la sélection";
    public static String M3025 = "Erreur M3025 - La date de début de génération est plus grande que la date de fin";
    public static String M3026 = "Erreur M3026 - La génération unitaire pour le dossier donne deux prestations pour cette periode. Impossible de répartir les unités correctement.";

    // Génération des prestations : Warning
    public static String W0001 = "Warning W0001 - L'affilié, pour lequel la génération a été effectué, est inactif";
    public static String W0002 = "Warning W0002 - Impossible de sauvegarder la rubrique comptable";

    // ADI
    public static String MADI001 = "L'année du décompte est obligatoire";
    public static String MADI002 = "La période de début est obligatoire";
    public static String MADI003 = "La période de fin est obligatoire";
    public static String MADI004 = "La période de début doit être antérieure ou égale à la période de fin";
    public static String MADI005 = "L'état \"Comptabilisé\" ne peut pas être sélectionné";
    public static String MADI006 = "Totalité du montant non-réparti";
    public static String MADI007 = "La prestation que vous générée possède du paiement direct ET indirect. Impossible de traîter un tel dossier !";

    // N = Network
    public static String N0001 = "Erreur N0001 - Impossible d'établir la connexion avec le serveur";
    public static String N0002 = "Erreur N0002 - Le driver de connexion n'a pas été trouvé";
    public static String N0003 = "Erreur N0003 - La connexion n'a pas été fermée correctement";
    public static String N0004 = "Erreur N0004 - Il n'existe aucune connexion avec le serveur";
    public static String N0005 = "Erreur N0005 - Erreur lors de l'exécution d'une transaction avec le serveur";
    public static String N0006 = "Erreur N0006 - Il existe déjà une connexion avec un serveur, veuiller la fermer avant d'effectuer une nouvelle connexion";
    public static String N0007 = "Erreur N0007 - Connexion refusée: ";
    public static String N0008 = "Erreur N0008 - Impossible de démarrer une transaction: ";
    // S= Standard
    public static String S_WARNING = "Avertisement";
    public static String S_ERROR = "Erreur";
    public static String S_INFORMATION = "Information";
    public static String S_CONFIRMATION = "Demande de confirmation";

    public static String S0001 = "Les données de l'écran ont été modifiées, souhaitez-vous les sauvegarder ?";
    public static String S0002 = "Les données de l'écran ont été modifiées et doivent être sauvegardées avant de continuer, souhaitez-vous les sauvegarder maintenant ?";

    // V = View

    /*
     * Renvoye le message identifié par un code. Throw une exception si code est null ou introuvable.
     * 
     * @param code le code du message
     * 
     * @return le texte du message
     */
    public static String getMessage(String code) throws Exception {
        return JAMsg.class.getDeclaredField(code).get(null).toString();
    }
}