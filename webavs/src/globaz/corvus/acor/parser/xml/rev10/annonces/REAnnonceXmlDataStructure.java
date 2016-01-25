package globaz.corvus.acor.parser.xml.rev10.annonces;

/**
 * 
 * @author SCR
 * 
 */
public abstract class REAnnonceXmlDataStructure {

    public final static int GENRE_ANNONCE_AUGMENTATION = 1000;
    public final static int GENRE_ANNONCE_DIMINUTION = 2000;
    public final static int GENRE_ANNONCE_MODIFICATION = 3000;

    public final static String TAG_AJOURNEMENT_RENTE = "rc:Rentenaufschub";
    public final static String TAG_ANNEE_COTI_CLS_AGE = "rc:BeitragsjahreJahrgang";
    public final static String TAG_ANNEE_NIVEAU = "rc:Niveaujahr";

    public final static String TAG_AUGMENTATION = "rc:Zuwachsmeldung";
    // Ayant droit
    public final static String TAG_AYANT_DROIT = "rc:LeistungsberechtigtePerson";
    // BASE CALCUL
    public final static String TAG_BASE_CALCUL = "rc:Berechnungsgrundlagen";
    public final static String TAG_BTE_9 = "rc:AngerechneteErziehungsgutschrift";
    public final static String TAG_CAISSE_AGENCE = "rc:KasseZweigstelle";
    public final static String TAG_CODE_ATTEINTE_FONCT = "rc:Funktionsausfallcode";
    public final static String TAG_CODE_CANTON_ETAT = "rc:WohnkantonStaat";
    public final static String TAG_CODE_INFIRMITE = "rc:Gebrechensschluessel";

    public final static String TAG_CODE_MUTATION = "rc:Mutationscode";
    public final static String TAG_CODE_SPECIAL = "rc:SonderfallcodeRente";
    public final static String TAG_DATE_DEBUT_ANTICIPATION = "rc:Vorbezugsdatum";
    public final static String TAG_DATE_REVOC_AJOURN = "rc:Abrufdatum";
    public final static String TAG_DEBUT_DROIT = "rc:Anspruchsbeginn";
    public final static String TAG_DEGRE_INVALIDITE = "rc:Invaliditaetsgrad";
    public final static String TAG_DUREE_AJOURNEMENT = "rc:Aufschubsdauer";
    public final static String TAG_DUREE_COT_MANQUANTE_48_72 = "rc:AnrechnungVor1973FehlenderBeitragsmonate";

    public final static String TAG_DUREE_COT_MANQUANTE_73_78 = "rc:AnrechnungAb1973Bis1978FehlenderBeitragsmonate";
    public final static String TAG_DUREE_COTI_AP73 = "rc:BeitragsdauerAb1973";
    public final static String TAG_DUREE_COTI_AV73 = "rc:BeitragsdauerVor1973";
    public final static String TAG_DUREE_COTI_RAM = "rc:BeitragsdauerDurchschnittlichesJahreseinkommen";
    // 9ème
    public final static String TAG_DUREE_COTI_RAM_9 = "rc:BeitragsdauerDurchschnittlichesJahreseinkommen";
    public final static String TAG_ECHELLE = "rc:Skala";
    public final static String TAG_ETAT_CIVIL = "rc:Zivilstand";
    public final static String TAG_FIN_DROIT = "rc:Anspruchsende";
    public final static String TAG_GENRE_PRESTATION = "rc:Leistungsart";
    public final static String TAG_INFO_AI = "rc:IVDaten";

    public final static String TAG_INFO_AI_EPOUSE = "rc:IVDatenEhefrau";
    // Bonification
    public final static String TAG_INFO_BONIF = "rc:Gutschriften";

    public final static String TAG_INFO_ECHELLE = "rc:SkalaBerechnung";
    public final static String TAG_INFO_RAM = "rc:DJEBeschreibung";
    public final static String TAG_INFO_RETRAITE_FLEXIBLE = "rc:FlexiblesRentenAlter";
    public final static String TAG_INVALID_PRECOCE = "rc:IstFruehInvalid";
    public final static String TAG_INVALID_SURVIVANT = "rc:IstInvaliderHinterlassener";
    public final static String TAG_LIMITE_REVENU = "rc:EinkommensgrenzenCode";
    public final static String TAG_LOT = "lot";

    public final static String TAG_MENSUALITE = "rc:Monatsbetrag";
    public final static String TAG_MENSUALITE_RO_REMPLACEE = "rc:MonatsbetragErsetzteOrdentlicheRente";
    public final static String TAG_MINIMUM_GARANTI = "rc:MinimalgarantieCode";
    public final static String TAG_MOIS_RAPPORT = "rc:Berichtsmonat";
    public final static String TAG_NBR_ANNEE_ANTICIPATION = "rc:AnzahlVorbezugsjahre";

    public final static String TAG_NBR_ANNEE_BTA = "rc:AnzahlBetreuungsgutschrift";
    public final static String TAG_NBR_ANNEE_BTE = "rc:Gutschriften";
    public final static String TAG_NBR_ANNEE_BTE_9 = "rc:AnzahlErziehungsgutschrift";

    public final static String TAG_NBR_ANNEE_BTR = "rc:AnzahlUebergangsgutschrift";
    public final static String TAG_NO_ANNONCE = "rc:Meldungsnummer";
    public final static String TAG_NSS_AYANT_DROIT = "rc:Versichertennummer";
    public final static String TAG_NSS_COMPL_1 = "rc:VNr1Ergaenzend";

    public final static String TAG_NSS_COMPL_2 = "rc:VNr2Ergaenzend";
    public final static String TAG_OAI = "rc:IVStelle";
    public final static String TAG_PARENTE = "rc:FamilienAngehoerige";

    // Prestation
    public final static String TAG_PRESTATION = "rc:Leistungsbeschreibung";
    public final static String TAG_RAM = "rc:DurchschnittlichesJahreseinkommen";
    // 9ème
    public final static String TAG_RAM_SANS_BTE_9 = "rc:DJEohneErziehungsgutschrift";
    public final static String TAG_REDUCTION = "rc:KuerzungSelbstverschulden";
    public final static String TAG_REDUCTION_ANTICIPATION = "rc:Vorbezugsreduktion";
    public final static String TAG_REF_INT_CAISSE = "rc:KasseneigenrHinweis";
    public final static String TAG_REFUGIE = "rc:IstFluechtling";
    public final static String TAG_RENTE_ANTICIPEE = "rc:Rentenvorbezug";

    public final static String TAG_RENTE_EXTRAORDINAIRE = "rc:AusserordentlicheRente";
    public final static String TAG_RENTE_ORDINAIRE = "rc:OrdentlicheRente";
    public final static String TAG_REV_PRIS_EN_COMPTE = "rc:AngerechneteEinkommen";
    public final static String TAG_REVENU_SPLITTE = "rc:GesplitteteEinkommen";
    public final static String TAG_SUPP_AJOURN = "rc:Aufschubszuschlag";

    public final static String TAG_SURVENANCE_EV_ASSURE = "rc:DatumVersicherungsfall";
    public final static int TYPE_RENTE_API = 300;
    public final static int TYPE_RENTE_EXTRAORDINAIRE = 200;
    public final static int TYPE_RENTE_ORDINAIRE = 100;

    private int genreAnnonce = -1;
    private String noAnnonce = null;
    private String noCaisseAgence = null;
    private String refInterneCaisse = null;
    private int typeRente = -1;

    public int getGenreAnnonce() {
        return genreAnnonce;
    }

    public String getNoAnnonce() {
        return noAnnonce;
    }

    public String getNoCaisseAgence() {
        return noCaisseAgence;
    }

    public String getRefInterneCaisse() {
        return refInterneCaisse;
    }

    public int getTypeRente() {
        return typeRente;
    }

    public void setGenreAnnonce(int genreAnnonce) {
        this.genreAnnonce = genreAnnonce;
    }

    public void setNoAnnonce(String noAnnonce) {
        this.noAnnonce = noAnnonce;
    }

    public void setNoCaisseAgence(String noCaisseAgence) {
        this.noCaisseAgence = noCaisseAgence;
    }

    public void setRefInterneCaisse(String refInterneCaisse) {
        this.refInterneCaisse = refInterneCaisse;
    }

    public void setTypeRente(int typeRente) {
        this.typeRente = typeRente;
    }

}
