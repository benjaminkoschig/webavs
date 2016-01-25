package globaz.alfagest.tools;

/**
 * JAMsg est une classe contenant les messages
 * <i>
 * <p>
 * La classe <code>JAMsg</code> ne fournit que des constantes (statiques).
 * <p>
 * La convention de d�finition des messages est <b>ROOTcode</b> o�:
 * <ul>
 * <li>ROOT = la racine du message indiquant l'origine du message
 * <li>code = un num�ro � 4 chiffres identifiant le message
 * </ul>
 * <i>
 * 
 * @author Emmanuel Fleury
 * @version efl - 0.408.000
 */
public class JAMsg {
    // A = Application
    public static String A0001 = "Erreur A0001 - Le programme de cr�ation d'identificateurs n'a pas �t� trouv�";
    public static String A0002 = "Erreur A0002 - L'appel du programme de cr�ation d'identificateurs a �chou�";
    public static String A0003 = "Erreur A0003 - Erreur lors de l'appel du programme de cr�ation d'identificateurs";
    public static String A0004 = "Erreur A0004 - Le programme de calcul du droit n'a pas �t� trouv�";
    public static String A0005 = "Erreur A0005 - L'appel du programme de calcul du droit a �chou�";
    public static String A0006 = "Erreur A0006 - Erreur lors de l'appel du programme de calcul du droit";
    public static String A0007 = "Erreur A0007 - Le programme AS/400 n'a pas �t� trouv�";
    public static String A0008 = "Erreur A0008 - L'appel du programme AS/400 a �chou�";
    public static String A0009 = "Erreur A0009 - Erreur lors de l'appel du programme AS/400";
    public static String A0010 = "Erreur A0010 - R�le s�lectionn� diff�rent de r�le attendu";
    public static String A0011 = "Erreur A0011 - Erreur lors de l'appel du programme SERG500";
    public static String A0012 = "Erreur A0012 - Le programme SERG500 n'a pas �t� trouv�";
    public static String A0013 = "Erreur A0013 - La valeur retour de du programme SERG500 n'est pas valide";
    public static String A0014 = "Erreur A0014 - La date de versement est obligatoire";
    public static String A0015 = "Erreur A0015 - Code non valide, utilisez F4";
    public static String A0016 = "Erreur A0016 - La g�n�ration du dossier � �chou�e d� � une erreur dans le calcul";

    // C = Controler
    // D = DataAccess
    public static String D0001 = "Erreur D0001 - Impossible de charger un enregistrement sans s�lection";
    public static String D0002 = "Erreur D0002 - Aucun enregistrement trouv� avec la s�lection donn�e";
    public static String D0003 = "Erreur D0003 - Erreur de donn�e: ";
    public static String D0004 = "Erreur D0004 - Impossible de sauvegarder le d�tail de prestation";
    public static String D0005 = "Erreur D0005 - Impossible de sauvegarder l'en-t�te de prestation";
    public static String D0006 = "Erreur D0006 - Impossible de sauvegarder le r�capitulatif de prestation";
    public static String D0007 = "Erreur D0007 - Erreur lors de la suppression de la prestation";
    public static String D0008 = "Erreur D0008 - Impossible de sauvegarder. Donn�es de JBEnfantRow incoh�rentes";
    public static String D0009 = "Erreur D0009 - Erreur lors du chargement des donn�es depuis la base de donn�es";
    public static String D0010 = "Erreur D0010 - Impossible de sauvegarder la rubrique statistique";
    // I = Informations
    // M = Model
    public static String M0001 = "Erreur M0001 - Aucune ligne n'existe dans l'ensemble � la position indiqu�e";
    public static String M0002 = "Erreur M0002 - La classe d'acc�s � la ligne n'a pas �t� trouv�e";
    public static String M0003 = "Erreur M0003 - Aucune ligne s�lectionn�e dans la table";

    public static String M1001 = "Erreur M1001 - La date n'est pas valide";
    public static String M1002 = "Erreur M1002 - Le code est inconnu";
    public static String M1003 = "Erreur M1003 - Le nombre ne peut pas �tre n�gatif";
    public static String M1004 = "Erreur M1004 - Ce champ ne peut �tre modifi� qu'en mode cr�ation";
    public static String M1005 = "Erreur M1005 - Le nombre est inf�rieur au minimum accept�";
    public static String M1006 = "Erreur M1006 - Le nombre est sup�rieur au maximum accept�";
    public static String M1007 = "Erreur M1007 - Aucune s�lection effectu�e";
    public static String M1008 = "Erreur M1008 - La date de s�lection n'est pas valide";
    public static String M1009 = "Erreur M1009 - La date d'impression n'est pas valide";

    public static String M2001 = "Le nom est obligatoire";
    public static String M2002 = "Le pr�nom est obligatoire";
    public static String M2003 = "Le num�ro AVS est obligatoire";
    public static String M2004 = "Le sexe est obligatoire";
    public static String M2005 = "La date de naissance est obligatoire";
    public static String M2006 = "L'�tat civil est obligatoire";
    public static String M2007 = "La nationalit� est obligatoire";
    public static String M2008 = "Il est obligatoire d'indiquer si l'enfant est capable d'exercer une activit� lucrative";
    public static String M2009 = "Le conjoint n'existe pas";
    public static String M2010 = "Le dossier doit �tre attribu� � un allocataire";
    public static String M2011 = "L'allocataire n'existe pas";
    public static String M2012 = "Le dossier doit �tre li� � une entreprise";
    public static String M2013 = "L'entreprise n'existe pas";
    public static String M2014 = "L'�tat du dossier doit �tre renseign�";
    public static String M2015 = "La date de d�but de validit� est obligatoire";
    public static String M2016 = "L'unit� de calcul doit �tre renseign�e";
    public static String M2017 = "Le motif de r�duction doit �tre renseign� si un taux est donn�";
    public static String M2018 = "Il est obligatoire d'indiquer si l'imp�t � la source doit �tre retenu";
    public static String M2019 = "Le statut familial est obligatoire";
    public static String M2020 = "Le type d'allocataire est obligatoire";
    public static String M2021 = "Le pays de r�sidence est obligatoire";
    public static String M2022 = "Il est obligatoire d'indiquer si la d�cision doit �tre imprim�e";
    public static String M2023 = "La date de fin de validit� ne peut pas �tre ant�rieure � la date de d�but de validit�";
    public static String M2024 = "La date de fin d'activit� ne peut pas �tre ant�rieure � la date de d�but d'activit�";
    public static String M2025 = "L'entit� de provenance de l'adresse est obligatoire";
    public static String M2026 = "Le type d'adresse est obligatoire";
    public static String M2027 = "Il est obligatoire d'indiquer si la formule de politesse doit �tre imprim�e";
    public static String M2028 = "La provenance du donnant droit doit �tre indiqu�e";
    public static String M2029 = "Le droit doit �tre attribu� � un dossier";
    public static String M2030 = "Le dossier indiqu� n'existe pas";
    public static String M2031 = "Le donnant droit indiqu� n'existe pas";
    public static String M2032 = "L'�tat du droit doit �tre indiqu�";
    public static String M2033 = "La date de d�but du droit est obligatoire";
    public static String M2034 = "Le type de prestation est obligatoire";
    public static String M2035 = "Le motif de r�duction doit �tre renseign� si un taux est donn�";
    public static String M2036 = "La date de fin de droit ne peut pas �tre ant�rieure � la date de d�but de droit";
    public static String M2037 = "Un allocataire doit �tre s�lectionn�";
    public static String M2038 = "Un enfant doit �tre s�lectionn�";
    public static String M2039 = "Le conjoint doit �tre li� � un allocataire";
    public static String M2040 = "Le type d'adresse est obligatoire";
    public static String M2041 = "L'adresse de paiement doit �tre li�e � une adresse postale";
    public static String M2042 = "Un allocataire doit �tre s�lectionn�";
    public static String M2043 = "Le r�le est obligatoire";
    public static String M2044 = "La date de fin de droit est obligatoire";
    public static String M2045 = "Si une �ch�ance est sp�cifi�e, le motif de l'�ch�ance est obligatoire.";
    public static String M2046 = "Le motif de fin de droit est incorrect";
    public static String M2047 = "Le motif de fin de droit est incorrect";
    public static String M2048 = "Le droit doit �tre attribu� � une personne";
    public static String M2049 = "Le droit doit �tre attribu� � un allocataire";
    public static String M2050 = "La date de versement est obligatoire avec ce type d'allocation";
    public static String M2051 = "Le taux d'allocation de naissance est obligatoire";
    public static String M2052 = "Le nombre de jour du d�but est obligatoire";
    public static String M2053 = "Le nombre de jour de fin est obligatoire";
    public static String M2054 = "La date de d�but d'activit� est obligatoire";
    public static String M2055 = "Le motif de r�duction doit �tre : COMP/Complet";
    public static String M2056 = "Le motif de r�duction doit �tre diff�rent de : COMP/Complet";
    public static String M2057 = "Le type de prestation ne doit pas �tre : MEN/Allocation de m�nage AF";
    public static String M2058 = "Aucun dossier trouv� pour cet affili�";
    public static String M2059 = "La p�riode doit �tre renseign�e";
    public static String M2060 = "Le lot de versement doit �tre renseign�e";
    public static String M2061 = "Le responsable doit �tre renseign�e";
    public static String M2062 = "L'�tat du droit doit �tre renseign�";
    public static String M2063 = "Le motif de r�duction doit �tre renseign�";
    public static String M2064 = "Aucun dossier d�fini au niveau du conjoint de l'allocataire";
    public static String M2065 = "Aucun conjoint d�fini pour l'allocataire";
    public static String M2066 = "Vous devez choisir un allocataire";
    public static String M2067 = "Le responsable doit �tre renseign�";
    public static String M2068 = "La date de d�but de validit� ne peut pas �tre ant�rieure � la date de d�but d'activit�";
    public static String M2069 = "La date de fin de validit� ne peut pas �tre post�rieure � la date de fin d'activit�";
    public static String M2070 = "L'indication d'une localit� est obligatoire";
    public static String M2071 = "Le num�ro de CCP est obligatoire";
    public static String M2072 = "La banque est obligatoire";
    public static String M2073 = "Le num�ro de compte est obligatoire";
    public static String M2074 = "La personne s�lectionn�e n'a pas d'adresse de paiement";
    public static String M2075 = "L'id-dossier p�re est obligatoire (ZIDDP)";
    public static String M2076 = "L'id-dossier fils est obligatoire (ZIDDF)";
    public static String M2077 = "Le type de relation est obligatoire (ZREL)";
    public static String M2078 = "La date <<� verser le ..>> est obligatoire";

    /*
     * Messages d'erreurs pour les suppressions
     */
    public static String M2079 = "Erreur M2079 - Seuls les d�comptes ADI en �tat PR peuvent �tre supprim�s";
    public static String M2080 = "Erreur M2080 - Un allocataire ne peut pas �tre supprimer s'il a des dossiers";
    public static String M2081 = "Erreur M2081 - Un conjoint ne peut pas �tre supprim� s'il est li� � un allocataire";
    public static String M2082 = "Erreur M2082 - Un enfant ne peut pas �tre supprim� s'il a des droits";

    // processus PR
    public static String M2083 = "Erreur M2083 - Une prestation ne peut �tre supprim�e que si elle n'est pas comptabilis�e et que sont �tat est PR";
    // processus SA
    public static String M2084 = "Erreur M2084 - Une prestation ne peut �tre supprim�e que si elle n'est pas comptabilis�e et que sont �tat est PR ou TR";

    // processus PR
    public static String M2086 = "Erreur M2086 - Un r�capitulatif ne peut �tre supprim�e si sont �tat est PR";
    // processus SA
    public static String M2087 = "Erreur M2087 - Un r�capitulatif ne peut �tre supprim�e si sont �tat est PR ou TR";

    public static String M2088 = "Erreur M2088 - Un b�n�ficiaire ne peut pas �tre supprim� s'il est li� � un/des dossiers et/ou droits";

    // G�n�ration des prestations
    public static String M3001 = "Erreur M3001 - La r�cap. ou le type de bonification est incorrect";
    public static String M3002 = "Erreur M3002 - Une prestation existe d�j� pour la p�riode � g�n�rer.";
    public static String M3003 = "Erreur M3003 - Des prestations existes d�j� pour ce dossier. Impossible de forc�e le montant";
    public static String M3004 = "Erreur M3004 - Impossible d'affecter une valeur � la recap. Recap inexistante.";
    public static String M3005 = "Erreur M3005 - Impossible d'affecter l'ID de recap pour les ADI Suisse";
    public static String M3006 = "Erreur M3006 - Aucun param�tre existant pour la g�n�ration unitaire.";
    public static String M3007 = "Erreur M3007 - Impossible de g�n�rer la prestation de naissance";
    public static String M3008 = "Erreur M3008 - Le calcul des droits du dossier � �chou�";
    public static String M3009 = "Erreur M3009 - Le r�capitulatif n'a pas pu �tre initialis�";
    public static String M3010 = "Erreur M3010 - Impossible de g�n�rer la prestation de type *ERR";
    public static String M3011 = "Erreur M3011 - Impossible d'extourner ou supprimer l'ancienne prestation";
    public static String M3012 = "Erreur M3012 - Attention, une prestation identique existe d�j� ! Celle-ci a �t� supprimer automatiquement de la base de donn�es";
    public static String M3013 = "Erreur M3013 - L'�tat de la recap de genre <Caisse cantonale> ne correspond pas � celle permise";
    public static String M3014 = "Erreur M3014 - L'�tat de la recap de genre <Caisse horlog�re> ne correspond pas � celle permise";
    public static String M3015 = "Erreur M3015 - Recap inexistante !";
    public static String M3016 = "Erreur M3016 - Le type de bonification diff�re entre la r�cap et l'en-t�te";
    public static String M3017 = "Erreur M3017 - Le type de bonification n'est pas compatible avec le mode de paiement";
    public static String M3018 = "Erreur M3018 - La date est compos�e de caract�res invalides";
    public static String M3019 = "Erreur M3019 - Aucun code de processus correspondant lors de l'ex�cution";
    public static String M3020 = "Erreur M3020 - Aucune date valide trouver pour ex�cuter le processus";
    public static String M3021 = "Erreur M3021 - Impossible de trouver un incr�ment valide pour le num�ro de passage";
    public static String M3022 = "Erreur M3022 - Le montant de la prestation dont l'unit� a �t� forc�e est sup�rieur au montant l�gal";
    public static String M3023 = "Erreur M3023 - Impossible de g�n�rer une allocation de naissance pour un dossier bilat�ral";
    public static String M3024 = "Erreur M3024 - Impossible d'affecter une valeur � un �l�ment de la s�lection";
    public static String M3025 = "Erreur M3025 - La date de d�but de g�n�ration est plus grande que la date de fin";
    public static String M3026 = "Erreur M3026 - La g�n�ration unitaire pour le dossier donne deux prestations pour cette periode. Impossible de r�partir les unit�s correctement.";

    // G�n�ration des prestations : Warning
    public static String W0001 = "Warning W0001 - L'affili�, pour lequel la g�n�ration a �t� effectu�, est inactif";
    public static String W0002 = "Warning W0002 - Impossible de sauvegarder la rubrique comptable";

    // ADI
    public static String MADI001 = "L'ann�e du d�compte est obligatoire";
    public static String MADI002 = "La p�riode de d�but est obligatoire";
    public static String MADI003 = "La p�riode de fin est obligatoire";
    public static String MADI004 = "La p�riode de d�but doit �tre ant�rieure ou �gale � la p�riode de fin";
    public static String MADI005 = "L'�tat \"Comptabilis�\" ne peut pas �tre s�lectionn�";
    public static String MADI006 = "Totalit� du montant non-r�parti";
    public static String MADI007 = "La prestation que vous g�n�r�e poss�de du paiement direct ET indirect. Impossible de tra�ter un tel dossier !";

    // N = Network
    public static String N0001 = "Erreur N0001 - Impossible d'�tablir la connexion avec le serveur";
    public static String N0002 = "Erreur N0002 - Le driver de connexion n'a pas �t� trouv�";
    public static String N0003 = "Erreur N0003 - La connexion n'a pas �t� ferm�e correctement";
    public static String N0004 = "Erreur N0004 - Il n'existe aucune connexion avec le serveur";
    public static String N0005 = "Erreur N0005 - Erreur lors de l'ex�cution d'une transaction avec le serveur";
    public static String N0006 = "Erreur N0006 - Il existe d�j� une connexion avec un serveur, veuiller la fermer avant d'effectuer une nouvelle connexion";
    public static String N0007 = "Erreur N0007 - Connexion refus�e: ";
    public static String N0008 = "Erreur N0008 - Impossible de d�marrer une transaction: ";
    // S= Standard
    public static String S_WARNING = "Avertisement";
    public static String S_ERROR = "Erreur";
    public static String S_INFORMATION = "Information";
    public static String S_CONFIRMATION = "Demande de confirmation";

    public static String S0001 = "Les donn�es de l'�cran ont �t� modifi�es, souhaitez-vous les sauvegarder ?";
    public static String S0002 = "Les donn�es de l'�cran ont �t� modifi�es et doivent �tre sauvegard�es avant de continuer, souhaitez-vous les sauvegarder maintenant ?";

    // V = View

    /*
     * Renvoye le message identifi� par un code. Throw une exception si code est null ou introuvable.
     * 
     * @param code le code du message
     * 
     * @return le texte du message
     */
    public static String getMessage(String code) throws Exception {
        return JAMsg.class.getDeclaredField(code).get(null).toString();
    }
}