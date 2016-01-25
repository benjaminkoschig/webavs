package globaz.lynx.servlet.utils;

import globaz.globall.util.JANumberFormatter;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.canevas.LXCanevasVentilationViewBean;
import globaz.lynx.db.ventilation.LXVentilationViewBean;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class LXVentilationActionUtils {

    /**
     * Return une arraylist qui contiendra les ventilations du canevas envoyées au serveur.
     * 
     * @param request
     * @param acceptMontantAZero
     * @return
     * @throws Exception
     */
    public static ArrayList<LXCanevasVentilationViewBean> getCanevasVentilationsFromRequest(HttpServletRequest request,
            boolean acceptMontantAZero) throws Exception {
        ArrayList<LXCanevasVentilationViewBean> ventilations = new ArrayList<LXCanevasVentilationViewBean>();

        int countParam = 0;
        while (!JadeStringUtil.isIntegerEmpty(request.getParameter("idc" + countParam))) {
            if (LXVentilationActionUtils.validUserMontantEntries(request, countParam) || acceptMontantAZero) {
                LXCanevasVentilationViewBean ventilation = new LXCanevasVentilationViewBean();

                ventilation.setIdCompte(request.getParameter("idc" + countParam));
                ventilation.setIdVentilationCanevas(request.getParameter("idv" + countParam));
                ventilation.setIdExterneCompte(request.getParameter("idext" + countParam));
                ventilation.setIdCentreCharge(request.getParameter("idcc" + countParam));
                ventilation.setLibelle(request.getParameter("l" + countParam));

                String strCanevasPourcentage = request.getParameter("canevasPourcentage");

                if (!Boolean.parseBoolean(strCanevasPourcentage)) {
                    if (!JadeStringUtil.isBlank(request.getParameter("idc" + countParam))) {
                        if (!JadeStringUtil.isBlank(request.getParameter("md" + countParam))) {
                            ventilation.setCodeDebitCredit(CodeSystem.CS_DEBIT);
                            ventilation.setMontant(JANumberFormatter.deQuote(request.getParameter("md" + countParam)));
                        } else {
                            ventilation.setCodeDebitCredit(CodeSystem.CS_CREDIT);
                            ventilation.setMontant(JANumberFormatter.deQuote(request.getParameter("mc" + countParam)));
                        }
                    }

                } else {
                    if (LXVentilationActionUtils.hasPourcentageDebit(request, countParam)) {
                        ventilation.setPourcentage(JANumberFormatter.deQuote(request
                                .getParameter("colPourcentageDebInput" + countParam)));
                        ventilation.setCodeDebitCredit(CodeSystem.CS_DEBIT);
                    }

                    if (LXVentilationActionUtils.hasPourcentageCredit(request, countParam)) {
                        ventilation.setPourcentage(JANumberFormatter.deQuote(request
                                .getParameter("colPourcentageCredInput" + countParam)));
                        ventilation.setCodeDebitCredit(CodeSystem.CS_CREDIT);
                    }
                }

                if (LXVentilationActionUtils.hasMontantMonnaieEtrangere(request, countParam)) {
                    ventilation.setMontantMonnaie(JANumberFormatter.deQuote(request.getParameter("me" + countParam)));
                }

                if (LXVentilationActionUtils.hasCours(request, countParam)) {
                    ventilation.setCoursMonnaie(request.getParameter("c" + countParam));
                }

                ventilations.add(ventilation);
            }

            countParam++;
        }

        return ventilations;
    }

    /**
     * Return une arraylist qui contiendra les ventilations envoyées au serveur.
     * 
     * @param request
     * @param acceptMontantAZero
     * @return
     * @throws Exception
     */
    public static ArrayList<LXVentilationViewBean> getVentilationsFromRequest(HttpServletRequest request,
            boolean acceptMontantAZero, int maxRows) throws Exception {
        ArrayList<LXVentilationViewBean> ventilations = new ArrayList<LXVentilationViewBean>();

        String libelle = ""; // Pour remplir le libelle de facon générique a
        // toutes les ventilations

        for (int countParam = 0; countParam <= maxRows; countParam++) {

            if (LXVentilationActionUtils.validUserMontantEntries(request, countParam) || acceptMontantAZero) {
                if (LXVentilationActionUtils.hasMontantDebit(request, countParam)
                        || LXVentilationActionUtils.hasMontantCredit(request, countParam)) {
                    LXVentilationViewBean ventilation = new LXVentilationViewBean();

                    if (!JadeStringUtil.isEmpty(request.getParameter("l" + countParam))) {
                        libelle = request.getParameter("l" + countParam);
                    }

                    ventilation.setIdCompte(request.getParameter("idc" + countParam));
                    ventilation.setIdVentilation(request.getParameter("idv" + countParam));
                    ventilation.setIdExterneCompte(request.getParameter("idext" + countParam));
                    ventilation.setIdCentreCharge(request.getParameter("idcc" + countParam));
                    ventilation.setLibelle(libelle);

                    if (LXVentilationActionUtils.hasMontantDebit(request, countParam)) {
                        ventilation.setCodeDebitCredit(CodeSystem.CS_DEBIT);
                        ventilation.setMontant(JANumberFormatter.deQuote(request.getParameter("md" + countParam)));
                    } else {
                        ventilation.setCodeDebitCredit(CodeSystem.CS_CREDIT);
                        ventilation.setMontant(JANumberFormatter.deQuote(request.getParameter("mc" + countParam)));
                    }

                    if (LXVentilationActionUtils.hasMontantMonnaieEtrangere(request, countParam)) {
                        ventilation
                                .setMontantMonnaie(JANumberFormatter.deQuote(request.getParameter("me" + countParam)));
                    }

                    if (LXVentilationActionUtils.hasCours(request, countParam)) {
                        ventilation.setCoursMonnaie(request.getParameter("c" + countParam));
                    }

                    ventilations.add(ventilation);
                }
            }
        }

        return ventilations;
    }

    private static boolean hasCours(HttpServletRequest request, int countParam) {
        return ((!JadeStringUtil.isIntegerEmpty(JANumberFormatter.deQuote(request.getParameter("c" + countParam)))));
    }

    private static boolean hasMontantCredit(HttpServletRequest request, int countParam) {
        return !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("mc" + countParam)));
    }

    private static boolean hasMontantDebit(HttpServletRequest request, int countParam) {
        return !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("md" + countParam)));
    }

    private static boolean hasMontantMonnaieEtrangere(HttpServletRequest request, int countParam) {
        return ((!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("me" + countParam)))));
    }

    private static boolean hasPourcentageCredit(HttpServletRequest request, int countParam) {
        return !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("colPourcentageCredInput"
                + countParam)));
    }

    private static boolean hasPourcentageDebit(HttpServletRequest request, int countParam) {
        return !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(request.getParameter("colPourcentageDebInput"
                + countParam)));
    }

    private static boolean validUserMontantEntries(HttpServletRequest request, int countParam) {
        if (LXVentilationActionUtils.hasMontantDebit(request, countParam)) {
            return true;
        } else if (LXVentilationActionUtils.hasMontantCredit(request, countParam)) {
            return true;
        } else if (LXVentilationActionUtils.hasMontantMonnaieEtrangere(request, countParam)
                && LXVentilationActionUtils.hasCours(request, countParam)) {
            return true;
        } else if (LXVentilationActionUtils.hasPourcentageDebit(request, countParam)) {
            return true;
        } else if (LXVentilationActionUtils.hasPourcentageCredit(request, countParam)) {
            return true;
        } else {
            return false;
        }
    }

}
