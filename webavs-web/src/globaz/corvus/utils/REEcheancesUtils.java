package globaz.corvus.utils;

import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationNiveau1;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationNiveau2;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationNiveau3;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationNiveau4;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationNiveau5;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationNiveau6;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Set;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;

public class REEcheancesUtils {

    /**
     * Cette méthode va analyser la liste des IREEcheances et retourner le code prestation de plus haut niveau. Pour ce
     * faire, cette méthode s'appuie sur les <type>enum</type> suivant : -> @see RECodePrestationNiveau1 -> @see
     * RECodePrestationNiveau2 -> @see RECodePrestationNiveau3 -> @see RECodePrestationNiveau4 -> @see
     * RECodePrestationNiveau5 -> @see RECodePrestationNiveau6
     * 
     * @author lga
     * @param echeances
     *            Une IREEcheances contenant les rentes du tiers
     * @return Le code prestation de plus haut niveau ou <code>null</code> si 'echeances' est null ou si aucune rente
     *         trouvée dans IREEcheance.getRentesDuTiers()
     */
    public static IRERenteEcheances getRentePrincipale(IREEcheances echeances) {

        if ((echeances == null) || (echeances.getRentesDuTiers() == null) || (echeances.getRentesDuTiers().size() == 0)) {
            return null;
        }
        Set<IRERenteEcheances> rentesSet = echeances.getRentesDuTiers();

        IRERenteEcheances[] rentes = new IRERenteEcheances[rentesSet.size()];
        rentesSet.toArray(rentes);

        // Groupe 1
        for (IRERenteEcheances rente : rentes) {
            if (RECodePrestationNiveau1.isCodePrestationACeNiveau(rente.getCodePrestation())) {
                return rente;
            }
        }
        // Groupe 2
        for (IRERenteEcheances rente : rentes) {
            if (RECodePrestationNiveau2.isCodePrestationACeNiveau(rente.getCodePrestation())) {
                return rente;
            }
        }
        // Groupe 3
        for (IRERenteEcheances rente : rentes) {
            if (RECodePrestationNiveau3.isCodePrestationACeNiveau(rente.getCodePrestation())) {
                return rente;
            }
        }
        // Groupe 4
        for (IRERenteEcheances rente : rentes) {
            if (RECodePrestationNiveau4.isCodePrestationInGroup(rente.getCodePrestation())) {
                return rente;
            }
        }
        // Groupe 5
        for (IRERenteEcheances rente : rentes) {
            if (RECodePrestationNiveau5.isCodePrestationInGroup(rente.getCodePrestation())) {
                return rente;
            }
        }
        // Groupe 6
        for (IRERenteEcheances rente : rentes) {
            if (RECodePrestationNiveau6.isCodePrestationACeNiveau(rente.getCodePrestation())) {
                return rente;
            }
        }
        return null;
    }

    /**
     * <b>Règle établit par RCO avec l'aide de RJE</b><br/>
     * <br/>
     * 
     * <caption><br mce_bogus="1">
     * </caption>
     * <table BORDER="1">
     * <tbody>
     * <tr>
     * <td><b>REPRACC.ZTIEBK<br>
     * </b></td>
     * <td><b>REPRACC.ZTBPRB<br>
     * </b></td>
     * <td><b>REENTBLK.ZZMBLK et REENTBLK.ZZMDBK</b></td>
     * <td><b>Valeur de retour</b></td>
     * </tr>
     * <tr>
     * <td>1<br mce_bogus="1">
     * </td>
     * <td>=0<br mce_bogus="1">
     * </td>
     * <td>null<br mce_bogus="1">
     * </td>
     * <td>false<br mce_bogus="1">
     * </td>
     * </tr>
     * <tr>
     * <td>1<br mce_bogus="1">
     * </td>
     * <td>!=0<br mce_bogus="1">
     * </td>
     * <td>=<br mce_bogus="1">
     * </td>
     * <td>false<br mce_bogus="1">
     * </td>
     * </tr>
     * <tr>
     * <td><span style="background-color: rgb(0, 255, 0);">1<br>
     * </span></td>
     * <td><span style="background-color: rgb(0, 255, 0);">!=0<br>
     * </span></td>
     * <td><span style="background-color: rgb(0, 255, 0);">!=<br>
     * </span></td>
     * <td><span style="background-color: rgb(0, 255, 0);">true<br>
     * </span></td>
     * </tr>
     * <tr>
     * <td>2<br mce_bogus="1">
     * </td>
     * <td>=0<br mce_bogus="1">
     * </td>
     * <td>null<br mce_bogus="1">
     * </td>
     * <td>false<br mce_bogus="1">
     * </td>
     * </tr>
     * <tr>
     * <td>2<br mce_bogus="1">
     * </td>
     * <td>!=0<br mce_bogus="1">
     * </td>
     * <td>=<br mce_bogus="1">
     * </td>
     * <td>false<br mce_bogus="1">
     * </td>
     * </tr>
     * <tr>
     * <td><span style="background-color: rgb(0, 255, 0);">2<br>
     * </span></td>
     * <td><span style="background-color: rgb(0, 255, 0);">!=0<br>
     * </span></td>
     * <td><span style="background-color: rgb(0, 255, 0);">!=<br>
     * </span></td>
     * <td><span style="background-color: rgb(0, 255, 0);">true<br>
     * </span></td>
     * </tr>
     * </tbody>
     * </table>
     * 
     * <br/>
     * <br/>
     * Comme le démontre le tableau ci-dessus, on a uniquement deux cas qui retournent "true". <br/>
     * Dans les deux cas on a REPRACC.ZTIEBK != 0 et REENTBLK.ZZMBLK != REENTBLK.ZZMDBK. Autrement on retourne false.<br/>
     * 
     * @param prestation
     *            La prestation à tester
     * @param enteteBlocage
     *            Peut-être null dans le cas ou REPRACC.ZTIEBK est vide. Si REPRACC.ZTIEBK possède un id et que le
     *            paramètre <strong>enteteBlocage</strong> est null, une exception sera lancée
     * @return true si bloqué, false sinon
     * @author rco
     */
    public static boolean isPrestationBloquee(boolean isPrestationBloquee, String idEnteteBlocage,
            REEnteteBlocage enteteBlocage) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(idEnteteBlocage)) {
            if ((enteteBlocage == null) || enteteBlocage.isNew()) {
                throw new Exception(
                        "REEcheancesUtils.isPrestationNonBloquee(REPrestationsAccordees prestation, REEnteteBlocage enteteBlocage) : REEnteteBlocage is null or new");
            }
            if (!enteteBlocage.getMontantBloque().equals(enteteBlocage.getMontantDebloque())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test si la prestation est réellement bloqué (dans le sens ou REPRACC.ZTBPRB == 1 ne veut pas dire qu'elle est
     * forcément bloquée) <b> Conditions de blocage : </b></br> 1 : REPRACC.ZTBPRB == '1' ET REPRACC.ZTIEBK == 0 ou
     * null</br> 2 : REPRACC.ZTBPRB != '1' ET REPRACC.ZTIEBK != 0 ou null.</br> 3 : REPRACC.ZTBPRB == 1 ET
     * REPRACC.ZTIEBK != 0 ou null ET (REENTBLK.ZZMBLK != REENTBLK.ZZMDBK)
     * 
     * @param prestation
     *            La prestation à tester
     * @param enteteBlocage
     *            Peut-être null dans le cas ou REPRACC.ZTIEBK est vide. Si REPRACC.ZTIEBK possède un id et que le
     *            paramètre <strong>enteteBlocage</strong> est null, une exception sera lancée
     * @return true si une des 3 conditions est remplies
     */
    public static boolean isPrestationBloquee(REPrestationsAccordees prestation, REEnteteBlocage enteteBlocage)
            throws Exception {
        if ((prestation == null) || prestation.isNew()) {
            throw new Exception(
                    "REEcheancesUtils.isPrestationNonBloquee(REPrestationsAccordees prestation, REEnteteBlocage enteteBlocage) : REPrestationsAccordees is null or new");
        }
        return REEcheancesUtils.isPrestationBloquee(prestation.getIsPrestationBloquee(),
                prestation.getIdEnteteBlocage(), enteteBlocage);
    }
}
