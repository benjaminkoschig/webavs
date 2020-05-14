package globaz.apg.rapg.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.utils.APRuleUtils;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.utils.PRDateUtils;

/**
 * Classe abstraite pour les règles de validation des données de l'annonces
 *
 * @author lga
 */
public abstract class Rule implements APRuleDBDataProvider {

    private APRuleDBDataProvider dataBaseDataProvider;
    private final String errorCode;
    private boolean isBreakable;
    private BSession session;

    public Rule(String errorCode, boolean breakable) {
        this.errorCode = errorCode;
        isBreakable = breakable;
        dataBaseDataProvider = new APRuleDBDataProviderImpl();
    }

    /**
     * Exécution de la règle. Doit retourner true si la validation s'est passée sans problème sinon false
     *
     * @param champsAnnonce
     *                          L'ensemble des champs de l'annonce
     * @return <code>true</code> si la validation s'est passé sans erreur sinon <code>false</code>
     * @throws IllegalArgumentException
     *                                      Dans le cas ou les champs qui doivent êtres testé par la règle sont null,
     *                                      vide ou invalid (mauvais
     *                                      format de date, etc)
     * @throws APRuleExecutionException
     *                                      Lors d'exception interne à la règle (accès DB, etc)
     */
    public abstract boolean check(APChampsAnnonce champsAnnonce)
            throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException;

    /**
     * Set l'objet responsable de l'accès à la base de données. Cette composition est nécessaire pour pouvoir passer les
     * règles sous tests unitaires dans être tributaire d'une base de données
     *
     * @param dataBaseDataProvider
     */
    public void setDataBaseDataProvider(APRuleDBDataProvider dataBaseDataProvider) {
        this.dataBaseDataProvider = dataBaseDataProvider;
    }

    public  String getErrorCode() {
        return errorCode;
    }

    public void throwRuleExecutionException(Exception exception) throws APRuleExecutionException {
        throw new APRuleExecutionException("An internal Exception was thrown on rule ["
                + this.getClass().getSimpleName() + "] execution : " + exception.toString());
    }

    /**
     * Retourne le type d'annonce en fonction du subMessageType</br>
     * Valeurs retournée :</br>
     * 1 - Annonce
     * augmentation</br>
     * 3 - Annonce correction</br>
     * 4 - Annonce restitution</br>
     *
     * @param champsAnnonce
     * @return
     * @throws IllegalArgumentException
     *                                      Dans le cas ou le type d'annonce ne peut pas être résolut. Ce champ doit
     *                                      toujours être renseigné
     */
    protected int getTypeAnnonce(APChampsAnnonce champsAnnonce) throws IllegalArgumentException {
        String subMessageType = champsAnnonce.getSubMessageType();
        validNotEmpty(subMessageType, "subMessageType");

        if (APAnnoncesRapgService.subMessageType1.equals(subMessageType)) {
            return 1;
        } else if (APAnnoncesRapgService.subMessageType3.equals(subMessageType)) {
            return 3;
        } else if (APAnnoncesRapgService.subMessageType4.equals(subMessageType)) {
            return 4;
        } else {
            throw new IllegalArgumentException("Unable to define the 'type d'annonce' (1, 3, or 4). SubMessageType = ["
                    + champsAnnonce.getSubMessageType() + "]}");
        }
    }

    /**
     * Test que le champ passé en paramètre ne soit pas null ou une chaîne vide
     *
     * @param value
     *                       La valeur à tester
     * @param nomDuChamp
     *                       Le nom du champ. Utilisé pour générer le message d'erreur
     * @throws IllegalArgumentException
     *                                      Si le paramètre <code>value</code> est null ou une chaîne vide
     */
    protected final void validNotEmpty(String value, String nomDuChamp) throws IllegalArgumentException {
        if (JadeStringUtil.isEmpty(value)) {
            throw new IllegalArgumentException("The value of the field [" + nomDuChamp + "] is null");
        }
    }

    /**
     * Test que le champ passé en paramètre ne soit pas null ou une chaîne vide et qu'il soit conforme au test
     * isGlobazDate
     *
     * @param value
     * @param nomDuChamp
     * @throws IllegalArgumentException
     */
    protected final void testDateNotEmptyAndValid(String value, String nomDuChamp) throws IllegalArgumentException {
        validNotEmpty(value, nomDuChamp);
        if (!JadeDateUtil.isGlobazDate(value)) {
            throw new IllegalArgumentException(
                    "The value of the field [" + nomDuChamp + "] is not a valid Globaz date");
        }
    }

    protected final void validNotNull(String value, String nomDuChamp) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("The value of the field [" + nomDuChamp + "] is null");
        }
    }

    /**
     * <p>
     * Déduira le nombre d'année à la date, et retournera le 1er janvier de l'année calculée par ce biais.<br/>
     * L'année en cours, dans la date, compte comme une année à soustraire.
     * </p>
     * <p>
     * Exemple :<br/>
     * <code>
     * date = 31.12.2005, nombreAnneeEnMoins = 5<br/>
     * 31.12.2005 - 4 ans -> 31.12.2001 -> </code>(au 1er janvier)<code> 01.01.2001<br/>
     * retournera 01.01.2001
     * </code>
     * </p>
     * <p>
     * Retournera <code>null</code> si la date n'est pas au bon format
     * </p>
     * <p>
     * Si <code>nombreAnneeEnMoins = 1</code>, retournera le 1er janvier de l'année passée en paramètre.
     * </p>
     *
     * @param date
     *                               une date au format JJ.MM.AAAA
     * @param nombreAnneeEnMoins
     *                               un nombre d'année à retirer à la date
     * @return une date au format JJ.MM.AAAA, ou <code>null</code> si la date passée en paramètre n'est pas au bon
     *         format
     */
    protected String getDateMoinsXAnneeCiviles(String date, int nombreAnneeEnMoins) {
        if (JadeDateUtil.isGlobazDate(date)) {
            String dateMoinsLesAnnees = JadeDateUtil.addYears(date, -(nombreAnneeEnMoins - 1));
            return "01.01." + dateMoinsLesAnnees.substring(6);
        }
        return null;
    }

    /**
     * Recherche et retourne le nombre de jours que le tiers de cette annonce a passer à l'école de recrue
     *
     * @param champsAnnonce
     * @param nombreAnneeCouvert
     *                               le nombre d'année civile à remonter pour vérifier (si 1, recherche à partir du 1er
     *                               janvier du début de
     *                               la période dans l'annonce)
     * @return
     * @throws Exception
     *                       si un problème survient lors de la recherche avec le manager
     */
    protected int getNombreJoursEcoleRecrue(APAnnonceAPG champsAnnonce, int nombreAnneeCouvert) throws Exception {

        String finPeriodeCouverte = champsAnnonce.getStartOfPeriod();
        String debutPeriodeCouverte = getDateMoinsXAnneeCiviles(finPeriodeCouverte, nombreAnneeCouvert);

        APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
        manager.setSession(getSession());
        manager.setForDroitContenuDansDateDebut(debutPeriodeCouverte);
        manager.setForDroitContenuDansDateFin(finPeriodeCouverte);
        manager.setLikeNumeroAvs(champsAnnonce.getInsurant());
        manager.setForCsGenreService(APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodeSysteme());
        manager.find();

        int nombreJourEcoleRecrue = 0;
        for (int i = 0; i < manager.size(); i++) {
            APDroitAPGJointTiers unDroit = (APDroitAPGJointTiers) manager.get(i);
            if (!JadeStringUtil.isBlank(unDroit.getNbrJourSoldes())) {
                nombreJourEcoleRecrue += Integer.parseInt(unDroit.getNbrJourSoldes());
            }
        }

        return nombreJourEcoleRecrue;
    }

    /**
     * <p>
     * Retournera l'énuméré correspondant au type de service dans la protection civile pour le numéro de référence
     * (numéro de compte dans l'ancienne nomenclature) passé en paramètre.
     * </p>
     * <p>
     * Retournera {@link APTypeProtectionCivile#Indefini} si le numéro de référence est vide ou <code>null</code>, ou
     * s'il ne correspond à aucun numéro connu (voir le document 2.10 de l'OFAS, et la doc de
     * {@link APTypeProtectionCivile})
     * </p>
     *
     * @param refNumber
     * @return
     */
    protected APTypeProtectionCivile getTypePCenFonctionDuRefNumber(String refNumber) {
        return APRuleUtils.getTypePCenFonctionDuRefNumber(refNumber);
    }

    /**
     * <p>
     * Retournera <code>true</code> si la période chevauche la date de Noël suivant le début de la période.
     * </p>
     * <p>
     * Retournera <code>false</code> si une des dates n'est pas valide ou si la date de fin est plus ancienne que celle
     * de début
     * </p>
     *
     * @param dateDebut
     *                      date au format JJ.MM.AAAA
     * @param dateFin
     *                      date au format JJ.MM.AAAA
     * @return
     */
    protected boolean isPeriodePendantNoel(String dateDebut, String dateFin) {
        if (!JadeDateUtil.isGlobazDate(dateDebut) || !JadeDateUtil.isGlobazDate(dateFin)
                || JadeDateUtil.isDateAfter(dateDebut, dateFin)) {
            return false;
        }
        int anneeDroit = Integer.parseInt(dateDebut.substring(6));
        JadePeriodWrapper periodeDroit = new JadePeriodWrapper(dateDebut, dateFin);
        return periodeDroit.isDateDansLaPeriode("25.12." + anneeDroit);
    }

    protected List<APDroitAvecParent> skipDroitLuiMeme(List<APDroitAvecParent> tousLesDroits, String idDroit) {
        List<APDroitAvecParent> droitFinaux = new ArrayList<APDroitAvecParent>();
        for (APDroitAvecParent droit : tousLesDroits) {
            if (!idDroit.equals(droit.getIdDroit())) {
                droitFinaux.add(droit);
            }
        }
        return droitFinaux;
    }

    /**
     * Tri des droits pour garder uniquement la dernière correction d'un droit
     *
     * @param tousLesDroits
     * @return
     * @throws APRuleExecutionException
     */
    protected List<APDroitAvecParent> skipDroitParent(List<APDroitAvecParent> tousLesDroits)
            throws APRuleExecutionException {
        List<APDroitAvecParent> droitFinaux = new ArrayList<APDroitAvecParent>();
        // On récupère tous les droits et leurs idParents s'il y en a
        Map<String, List<APDroitAvecParent>> droitAvecParent = new HashMap<String, List<APDroitAvecParent>>();
        for (APDroitAvecParent droit : tousLesDroits) {

            // S'il n'y a pas de droit parent, on le prend directement
            if (JadeStringUtil.isBlankOrZero(droit.getIdDroitParent())) {
                droitFinaux.add(droit);
            } else {
                String idParent = droit.getIdDroitParent();
                if (!droitAvecParent.containsKey(idParent)) {
                    droitAvecParent.put(idParent, new ArrayList<APDroitAvecParent>());
                }
                droitAvecParent.get(idParent).add(droit);
            }
        }

        // Maintenant on va prend tous les droits avec parent de plus grand id
        for (String idDroitParent : droitAvecParent.keySet()) {
            APDroitAvecParent droitAvecPlusGrandId = null;
            List<APDroitAvecParent> allDroits = new ArrayList<APDroitAvecParent>();
            for (APDroitAvecParent droit : droitAvecParent.get(idDroitParent)) {
                if (droitAvecPlusGrandId == null) {
                    droitAvecPlusGrandId = droit;
                } else {
                    try {
                        int idDroitAvecPlusGrandId = Integer.valueOf(droitAvecPlusGrandId.getIdDroit());
                        int idDroit = Integer.valueOf(droit.getIdDroit());
                        if (idDroit > idDroitAvecPlusGrandId) {
                            droitAvecPlusGrandId = droit;
                        }
                    } catch (Exception e) {
                        throw new APRuleExecutionException(
                                "Exception thrown when getting the child with big id for APDroitLAPGJointTiers. Id 1 = ["
                                        + droitAvecPlusGrandId.getIdDroit() + "], id 2 = [" + droit.getIdDroit() + "]",
                                e);
                    }
                }
            }
            // Test en cas d'erreur lors de l'évaluation des id...
            if (droitAvecPlusGrandId != null) {
                droitFinaux.add(droitAvecPlusGrandId);
            }
        }
        return droitFinaux;
    }

    /**
     * Recherche dans la liste des droits fournis en paramètres si des prestations d'allocation (pas de restit,
     * duplicata, etc) existent pour la période définie par startOfPeriod et endOfPeriod
     *
     * @param startOfPeriod
     *                          Le début de la période
     * @param endOfPeriod
     *                          La fin de la période
     * @param droitsTries
     *                          La liste des droits
     * @throws Exception
     *                       En cas d'erreur liée au manager
     */
    protected boolean hasPrestationEnConflit(String startOfPeriod, String endOfPeriod,
            List<APDroitAvecParent> droitsTries) throws Exception {
        PRPeriode periode = new PRPeriode(startOfPeriod, endOfPeriod);
        APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(getSession());
        // prestationManager.setInDateDebut(startOfPeriod);
        // prestationManager.setInDateFin(endOfPeriod);
        prestationManager.setForContenuAnnonce(IAPAnnonce.CS_DEMANDE_ALLOCATION);
        for (APDroitAvecParent dp : droitsTries) {
            prestationManager.setForIdDroit(dp.getIdDroit());
            prestationManager.find();
            for (Object o : prestationManager.getContainer()) {
                APPrestation prestation = (APPrestation) o;
                if (PRDateUtils.isDateDansLaPeriode(periode, prestation.getDateDebut())) {
                    return true;
                }
                if (PRDateUtils.isDateDansLaPeriode(periode, prestation.getDateFin())) {
                    return true;
                }

                // BZ 9354 correction
                // Inversion du contrôle
                periode = new PRPeriode(prestation.getDateDebut(), prestation.getDateFin());
                if (PRDateUtils.isDateDansLaPeriode(periode, startOfPeriod)) {
                    return true;
                }
                if (PRDateUtils.isDateDansLaPeriode(periode, endOfPeriod)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean isBreakable() {
        return isBreakable;
    }

    public final void setBreakable(boolean isBreakable) {
        this.isBreakable = isBreakable;
    }

    // Delegates methods
    @Override
    public boolean isTimeStampUnique(String timeStamp, BSession session) throws APRuleExecutionException {
        return dataBaseDataProvider.isTimeStampUnique(timeStamp, session);
    }

    @Override
    public boolean isCodePaysExistant(String insurantDomicileCountry, BSession session)
            throws APRuleExecutionException {
        return dataBaseDataProvider.isCodePaysExistant(insurantDomicileCountry, session);
    }

    public final BSession getSession() {
        return session;
    }

    public final void setSession(BSession session) {
        this.session = session;
    }

    public String getDetailMessageErreur() {
        return null;
    }

}
