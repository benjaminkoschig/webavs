package globaz.bms.affiliation;

import globaz.bms.format.BMSNumAffilie;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;

/**
 * Permet la génération automatique du numéro d'affilié à la création de l'affiliation.
 * 
 * @since WebBMS 0.01.03
 */
public class BMSNoAffGenerator implements INumberGenerator {

    public static String MAX_NUMERO_AUTRE = "999999.99";
    public static String MAX_NUMERO_CAF_SEUL = "079999.99";

    public static void main(final String[] args) {
        try {
            BSession session = new BSession("NAOS");
            session.connect("sel", "sel");
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(session);
            affiliation.setIdTiers("1");
            affiliation.setAffilieNumero("7");
            BMSNoAffGenerator gen = new BMSNoAffGenerator();
            System.out.println("Formater un numéro saisi : 7 -> " + gen.generateBeforeAdd(affiliation));
            affiliation.setAffilieNumero("");
            System.out.println("Générer un nouveau numéro -> " + gen.generateBeforeAdd(affiliation));
            affiliation.setAffilieNumero("900221");
            affiliation.setTypeAssocie("1");
            System.out.println("??? Associé de 900221 -> " + gen.generateBeforeAdd(affiliation));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    @Override
    public String generateBeforeAdd(final AFAffiliation affiliation) throws Exception {
        // 0_000_000-00
        if ((affiliation == null) || JadeStringUtil.isEmpty(affiliation.getIdTiers())) {
            return "";
        }

        if (JadeStringUtil.isEmpty(affiliation.getConvention())) {
            return "";
        }

        if (!affiliation.isNew()) {
            return affiliation.getAffilieNumero();
        }

        String noAffilieGenere = affiliation.getAffilieNumero();

        if (JadeStringUtil.isEmpty(noAffilieGenere)) {
            noAffilieGenere = generateNumAffilie(affiliation.getSession());
        }
        // On force le noAffilie à XXXXXXX.XX-XX
        if (!isNoAssocie(noAffilieGenere)) {
            noAffilieGenere = setFormatBase(noAffilieGenere);
        }

        // On regarde si le numéro existe
        // Si il est associés, on compte combien il a d'associés, sinon on l'ajoute avec 00
        noAffilieGenere = ajouterAssocies(noAffilieGenere, affiliation);

        // Ajout de la convention
        noAffilieGenere = ajouterConvention(noAffilieGenere, affiliation.getConvention());

        return noAffilieGenere;
    }

    protected boolean isNoAssocie(String noAffilieGenere) {
        String regex = ".([0-9]{2})-";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(noAffilieGenere);
        if (matcher.find()) {
            int possibleNoAssocie = Integer.valueOf(matcher.group(1));
            return possibleNoAssocie != 0;
        }
        return false;
    }

    private String setFormatBase(String noAffilieGenere) {
        if (noAffilieGenere.indexOf(BMSNumAffilie.CONVENTION_SEPARATOR_CHAR) != -1) {
            noAffilieGenere = noAffilieGenere.substring(0,
                    noAffilieGenere.indexOf(BMSNumAffilie.CONVENTION_SEPARATOR_CHAR));
        }
        if (noAffilieGenere.indexOf(BMSNumAffilie.ASSOCIES_SEPARATOR_CHAR) != -1) {
            noAffilieGenere = noAffilieGenere.substring(0,
                    noAffilieGenere.indexOf(BMSNumAffilie.ASSOCIES_SEPARATOR_CHAR));
        }
        if (noAffilieGenere.length() > 7) {
            throw new IllegalStateException("Le numéro est trop long");
        }
        if (noAffilieGenere.length() < BMSNumAffilie.NUMBER_CORPSE_LENGTH) {
            noAffilieGenere = JadeStringUtil.fillWithZeroes(noAffilieGenere, BMSNumAffilie.NUMBER_CORPSE_LENGTH);
        }
        noAffilieGenere = noAffilieGenere + BMSNumAffilie.ASSOCIES_SEPARATOR_CHAR + "00"
                + BMSNumAffilie.CONVENTION_SEPARATOR_CHAR + "00";
        return noAffilieGenere;
    }

    private String ajouterAssocies(String noAffilieGenere, AFAffiliation affiliation) throws Exception {
        String noAffilieOriginal = noAffilieGenere;
        if (!JadeStringUtil.isEmpty(affiliation.getTypeAssocie())) {
            // On recherche le nombre d'associés déjà existants
            AFAffiliationManager mgr = new AFAffiliationManager();
            mgr.setSession(affiliation.getSession());
            mgr.setLikeAffilieNumero(noAffilieGenere.substring(0, BMSNumAffilie.NUMBER_CORPSE_LENGTH));
            mgr.find();
            int numAssocieMax = 0;
            if (mgr.size() > 0) {
                for (int i = 0; i < mgr.size(); i++) {
                    String noAff = ((AFAffiliation) mgr.getEntity(i)).getAffilieNumero();
                    // On récupère la partie associé, pour le comptage
                    int numAssocie = recupereAssocie(noAff);
                    if (numAssocie > numAssocieMax) {
                        numAssocieMax = numAssocie;
                    }
                }
                // A désactiver pour la reprise
                noAffilieGenere = setAssocies(noAffilieGenere, numAssocieMax);
                return noAffilieGenere;
            } else {
                noAffilieGenere = setAssocies(noAffilieGenere, 0);
                return noAffilieGenere;
            }
        }
        // Si pas associés, on retourne avec -00
        return noAffilieOriginal;
    }

    private String setAssocies(String noAffilieGenere, int numAssocieMax) {
        String noAffilieGenereDebut = noAffilieGenere.substring(0, BMSNumAffilie.ASSOCIES_SEPARATOR_POS + 1);
        String noAffilieGenereFin = noAffilieGenere.substring(BMSNumAffilie.CONVENTION_SEPARATOR_POS,
                BMSNumAffilie.NUMBER_LENGTH_TOTAL);
        String associePart = "";
        if (String.valueOf(numAssocieMax).length() == 1) {
            associePart = "0" + (numAssocieMax + 1);
        } else {
            associePart = "" + (numAssocieMax + 1);
        }
        noAffilieGenere = noAffilieGenereDebut + associePart + noAffilieGenereFin;
        return noAffilieGenere;
    }

    private int recupereAssocie(String noAff) {
        String associePart = noAff.substring(BMSNumAffilie.ASSOCIES_SEPARATOR_POS + 1,
                BMSNumAffilie.ASSOCIES_SEPARATOR_POS + 3);
        return Integer.valueOf(associePart);
    }

    private String ajouterConvention(String noAffilieGenere, String convention) throws JadePersistenceException {
        // Recherche du numéro de la convention
        String numeroConvention;
        AdministrationSearchComplexModel search = new AdministrationSearchComplexModel();
        search.setForIdTiersAdministration(convention);
        search = (AdministrationSearchComplexModel) JadePersistenceManager.search(search);
        if (search.getSearchResults().length > 0) {
            AdministrationComplexModel administration = (AdministrationComplexModel) search.getSearchResults()[0];
            numeroConvention = administration.getAdmin().getCodeAdministration();
        } else {
            throw new IllegalArgumentException("La convention n'existe pas dans les tiers administrations");
        }

        noAffilieGenere = noAffilieGenere.substring(0, BMSNumAffilie.CONVENTION_SEPARATOR_POS + 1);

        if (!JadeStringUtil.isEmpty(numeroConvention)) {
            return noAffilieGenere + numeroConvention;
        }
        return "";
    }

    /**
     * Génère un nouveau numéro d'affilié en incrémentant le dernier numéro trouvé
     * 
     * @return le nouveau numéro d'affiliation formaté
     * @throws Exception
     */
    private String generateNumAffilie(final BSession session) throws Exception {
        if (session == null) {
            return "";
        }

        // recherche du prochain numéro valable
        AFAffiliationManager mgr = new AFAffiliationManager();
        mgr.setSession(session);
        mgr.forIsTraitement(false);
        mgr.setOrder("MALNAF DESC");
        mgr.find();

        if (mgr.size() != 0) {
            String noAff = ((AFAffiliation) mgr.getFirstEntity()).getAffilieNumero();
            if ((noAff != null) && (noAff.length() >= BMSNumAffilie.NUMBER_LENGTH)) {
                noAff = noAff.substring(0, BMSNumAffilie.ASSOCIES_SEPARATOR_POS);
            }
            return String.valueOf(Integer.parseInt(noAff) + 1);
        }
        return "";
    }

    /**
     * Est appelé pour l'affichage de l'écran de saisie d'un affilié.
     * Propose un numéro par défaut.
     */
    @Override
    public String generateBeforeDisplay(final AFAffiliation affiliation) throws Exception {
        if (affiliation == null) {
            return "";
        }
        return format(generateNumAffilie(affiliation.getSession()));
    }

    private String format(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }

        value = unformat(value);
        StringBuffer result = new StringBuffer();
        if (value.length() <= BMSNumAffilie.ASSOCIES_SEPARATOR_POS) {
            result.append(JadeStringUtil.fillWithZeroes(value, BMSNumAffilie.ASSOCIES_SEPARATOR_POS));
            result.append(BMSNumAffilie.ASSOCIES_SEPARATOR_CHAR);
            result.append("00");
            result.append(BMSNumAffilie.CONVENTION_SEPARATOR_CHAR);
            result.append("00");
        } else if (value.length() > BMSNumAffilie.ASSOCIES_SEPARATOR_POS) {
            result.append(value.substring(0, BMSNumAffilie.ASSOCIES_SEPARATOR_POS));
            result.append(BMSNumAffilie.ASSOCIES_SEPARATOR_CHAR);
            result.append(JadeStringUtil.substring(value, BMSNumAffilie.ASSOCIES_SEPARATOR_POS, 2));
            result.append(BMSNumAffilie.CONVENTION_SEPARATOR_CHAR);
            result.append(JadeStringUtil.substring(value, BMSNumAffilie.ASSOCIES_SEPARATOR_POS + 2, 2));
        } else {
            return value;
        }
        return result.toString();
    }

    private String unformat(String value) {
        if (value == null) {
            // retourne vide si null
            return "";
        }
        return value.replace(".", "").replace("-", "").trim();
    }

    @Override
    public boolean isEditable(final AFAffiliation affiliation) throws Exception {
        // Not used !
        return true;
    }
}
