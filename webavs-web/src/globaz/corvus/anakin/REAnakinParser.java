package globaz.corvus.anakin;

import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.vb.process.REAnakinViewBean;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import ch.admin.ofit.anakin.commum.Environnement;
import ch.admin.ofit.anakin.donnee.AnnonceAbstraite;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;
import ch.admin.ofit.anakin.routeur.Routeur;

/**
 * Classe fournissant les méthodes de validation des annonces envoyées à la centrale. <br/>
 * Cette class se base sur les librairies fournies par la centrale (anakin.jar...)
 * 
 * @author scr
 */

public class REAnakinParser {

    private static REAnakinParser FACTORY;
    private static Routeur routeur = null;

    /**
     * Getter retournant l'instance du parser (Singleton)
     * 
     * @return le singleton
     */
    public static final synchronized REAnakinParser getInstance() {
        if (REAnakinParser.FACTORY == null) {
            REAnakinParser.FACTORY = new REAnakinParser();
        }
        return REAnakinParser.FACTORY;
    }

    /**
     * Crée une nouvelle instance de la classe REAnakinParser.
     */
    protected REAnakinParser() {

    }

    String formatToAnakinDate(String moisRapport) {
        if (JadeDateUtil.isGlobazDateMonthYear(moisRapport)) {
            SimpleDateFormat entryFormat = new SimpleDateFormat("MM.yyyy");
            SimpleDateFormat expectedFormat = new SimpleDateFormat("MMyy");
            try {
                return expectedFormat.format(entryFormat.parse(moisRapport));
            } catch (ParseException ex) {
                // on retourne moisRapport sans modification (voir plus bas)
            }
        }
        return moisRapport;
    }

    /**
     * Parse les annonces d'augmentation/diminution/modification 9ème et 10ème révision <br/>
     * (Code d'application : 41, 42, 43, 44, 45, 46)
     * 
     * @param session
     *            la session courante
     * @param arc01
     *            engistrement 01
     * @param arc02
     *            engistrement 02, peut être <code>null</code> pour les annonces de diminutions.
     * @param moisRapport
     *            le mois pour lequel les annonces doivent être verifiées
     * @return {@link java.util.Enumeration Enumeration} contenant les erreurs (
     *         {@link ch.admin.ofit.anakin.donnee.AnnonceAbstraite AnnonceAbstraite}). <code>null</code> si aucune
     *         erreur. <br/>
     *         Le module Anakin ne retourne pas la liste des erreur en définissant que c'est une
     *         {@link java.util.Enumeration Enumeration}<{@link ch.admin.ofit.anakin.donnee.AnnonceAbstraite
     *         AnnonceAbstraite}>, mais une simple {@link java.util.Enumeration Enumeration} non paramétrée. <br/>
     *         il est donc nécessaire de caster chaque élement de la liste pour pouvoir les utiliser
     * @throws Exception
     */
    public Enumeration<AnnonceErreur> parse(BSession session, REAnnonceHeader arc01, REAnnonceHeader arc02,
            String moisRapport) throws Exception {

        if (!REAnakinViewBean.isAnakinValidatorActif(session)) {
            return null;
        }

        if (REAnakinParser.routeur == null) {
            REAnakinParser.routeur = new Routeur();
            Environnement.initialize("FR", "fr");
        }

        String anakinFormatedMoisRapport = formatToAnakinDate(moisRapport);

        REArcConverter converter = new REArcConverter();
        AnnonceAbstraite aa = converter.convertToAnakinArc(session, arc01, arc02, anakinFormatedMoisRapport);
        AnnonceAbstraite result = REAnakinParser.routeur.router(aa, "00" + aa.getDateEtatRegistre());
        System.out.println("- date état du registre : " + aa.getDateEtatRegistre());

        System.out.println("Liste des erreurs suite au contrôle de plausibilité de l'annonce : ");
        if ((result.getErreur().getListe() != null) && (result.getErreur().getListe().size() > 0)) {

            System.out.println("------------------------------------------------------------ ");
            System.out.println("--                  Données saisies                       -- ");
            System.out.println("------------------------------------------------------------ ");

            System.out.println("Mois de rapport = " + moisRapport);
            System.out.println("AnneeNiveau = " + result.getAnneeNiveau());
            System.out.println("AnneesAppointAvant1973 = " + result.getAnneesAppointAvant1973());
            System.out.println("AnneesAppointDes1973 = " + result.getAnneesAppointDes1973());
            System.out.println("CasSpecial(index 1 = " + result.getCasSpecial(1));
            System.out.println("CasSpecial(index 2 = " + result.getCasSpecial(2));
            System.out.println("CasSpecial(index 3 = " + result.getCasSpecial(3));
            System.out.println("CasSpecial(index 4 = " + result.getCasSpecial(4));
            System.out.println("CasSpecial(index 5 = " + result.getCasSpecial(5));
            System.out.println("CasSpecialNumerique(index 1 = " + result.getCasSpecialNumerique(1));
            System.out.println("CasSpecialNumerique(index 2 = " + result.getCasSpecialNumerique(2));
            System.out.println("CasSpecialNumerique(index 3 = " + result.getCasSpecialNumerique(3));
            System.out.println("CasSpecialNumerique(index 4 = " + result.getCasSpecialNumerique(4));
            System.out.println("CasSpecialNumerique(index 5 = " + result.getCasSpecialNumerique(5));
            System.out.println("CasSpeciaux = " + result.getCasSpeciaux());
            System.out.println("Categorie = " + result.getCategorie());
            System.out.println("CategorieRente = " + result.getCategorieRente());
            System.out.println("ClasseAge = " + result.getClasseAge());
            System.out.println("ClefInfirmite = " + result.getClefInfirmite());
            System.out.println("ClefPrestationId = " + result.getClefPrestationId());
            System.out.println("ClefPrestationRevision = " + result.getClefPrestationRevision());
            System.out.println("ClefTriSurAssurance = " + result.getClefTriSurAssurance());
            System.out.println("CodeCommentaire(index 1 = " + result.getCodeCommentaire(1));
            System.out.println("CodeCommentaire(index 2 = " + result.getCodeCommentaire(2));
            System.out.println("CodeCommentaire(index 3 = " + result.getCodeCommentaire(3));
            System.out.println("CodeCommentaire(index 4 = " + result.getCodeCommentaire(4));
            System.out.println("CodeCommentaire(index 5 = " + result.getCodeCommentaire(5));
            System.out.println("DateAnnonceDiminution = " + result.getDateAnnonceDiminution());
            System.out.println("DateEtatRegistre = " + result.getDateEtatRegistre());
            System.out.println("DateMutation = " + result.getDateMutation());
            System.out.println("DateRevocation = " + result.getDateRevocation());
            System.out.println("DebutDroit = " + result.getDebutDroit());
            System.out.println("DegreInvalidite = " + result.getDegreInvalidite());
            System.out.println("Domicile = " + result.getDomicile());
            System.out.println("DureeAjournement = " + result.getDureeAjournement());
            System.out.println("DureeCotisationRevenuAnnuelMoyen = " + result.getDureeCotisationRevenuAnnuelMoyen());
            System.out.println("EchelleRente = " + result.getEchelleRente());
            System.out.println("EtatCivil = " + result.getEtatCivil());
            System.out.println("FinDroit = " + result.getFinDroit());
            System.out.println("FractionRente = " + result.getFractionRente());
            System.out.println("Genre = " + result.getGenre());
            System.out.println("GenreDroitAPI = " + result.getGenreDroitAPI());
            System.out.println("GenrePrestation = " + result.getGenrePrestation());
            System.out.println("InvaliditePrecoce = " + result.getInvaliditePrecoce());
            System.out.println("MontantCalcule = " + result.getMontantCalcule());
            System.out.println("MontantPrestation = " + result.getMontantPrestation());
            System.out.println("NaissanceNumeroAVS = " + result.getNaissanceNumeroAVS());
            System.out.println("NaissanceNumeroAVSComplementaire1 = " + result.getNaissanceNumeroAVSComplementaire1());
            System.out.println("NaissanceNumeroAVSComplementaire2 = " + result.getNaissanceNumeroAVSComplementaire2());
            System.out.println("NaissanceNumeroAVSCorrige = " + result.getNaissanceNumeroAVSCorrige());
            System.out.println("Nationalite = " + result.getNationalite());
            System.out.println("NomPrenom = " + result.getNomPrenom());
            System.out.println("NumeroAnnonce = " + result.getNumeroAnnonce());
            System.out.println("NumeroAVS = " + result.getNumeroAVS());
            System.out.println("NumeroAVSComplementaire1 = " + result.getNumeroAVSComplementaire1());
            System.out.println("NumeroAVSComplementaire2 = " + result.getNumeroAVSComplementaire2());
            System.out.println("NumeroAVSCorrige = " + result.getNumeroAVSCorrige());
            System.out.println("NumeroAVSFamille = " + result.getNumeroAVSFamille());
            System.out.println("NumeroCaisse = " + result.getNumeroCaisse());
            System.out.println("NumeroSequenceMutation = " + result.getNumeroSequenceMutation());
            System.out.println("PeriodeCotisationAvant1973 = " + result.getPeriodeCotisationAvant1973());
            System.out.println("PeriodeCotisationDes1973 = " + result.getPeriodeCotisationDes1973());
            System.out.println("RaisonDiminution = " + result.getRaisonDiminution());
            System.out.println("Reduction = " + result.getReduction());
            System.out.println("ReferenceInterne = " + result.getReferenceInterne());
            System.out.println("RevenuAnnuelMoyen = " + result.getRevenuAnnuelMoyen());
            System.out.println("Revision = " + result.getRevision());
            System.out.println("SexeNumeroAVS = " + result.getSexeNumeroAVS());
            System.out.println("SexeNumeroAVSComplementaire1 = " + result.getSexeNumeroAVSComplementaire1());
            System.out.println("SexeNumeroAVSComplementaire2 = " + result.getSexeNumeroAVSComplementaire2());
            System.out.println("SexeNumeroAVSCorrige = " + result.getSexeNumeroAVSCorrige());
            System.out.println("SupplementAjournement = " + result.getSupplementAjournement());
            System.out.println("SurvenanceEvenementAssure = " + result.getSurvenanceEvenementAssure());
            System.out.println("\n\n\n");

            return result.getErreur().getListe().elements();
        } else {
            return null;
        }

    }
}
