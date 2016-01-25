package globaz.amal.utils;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMSedexHelper {

    private static ComplexAnnonceSedex getDecreeOriginal(ComplexAnnonceSedex currentAnnonce) {
        try {
            // Recherche de la dernière confirmation arrivée sur le subside
            ComplexAnnonceSedexSearch lastConfirmationFoundSearch = new ComplexAnnonceSedexSearch();
            lastConfirmationFoundSearch.setForSDXIdDetailFamille(currentAnnonce.getSimpleDetailFamille()
                    .getIdDetailFamille());
            ArrayList<String> arrayInSubType = new ArrayList<String>();
            // Sélection de toutes les annonces de type 'Confirmation décision' liées au subside
            arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue());
            lastConfirmationFoundSearch.setInSDXMessageSubType(arrayInSubType);
            lastConfirmationFoundSearch.setForCMIdTiersCaisse(currentAnnonce.getSimpleAnnonceSedex().getIdTiersCM());
            lastConfirmationFoundSearch.setOrderKey("orderByIdSedexMsg");
            lastConfirmationFoundSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(
                    lastConfirmationFoundSearch);

            ComplexAnnonceSedex complexAnnonceSedex = new ComplexAnnonceSedex();
            // Si on a trouvé une confirmation
            if (lastConfirmationFoundSearch.getSize() > 0) {
                ComplexAnnonceSedex lastConfirmationFound = (ComplexAnnonceSedex) lastConfirmationFoundSearch
                        .getSearchResults()[0];

                // On a trouvé la confirmation, on recherche maintenant l'annonce DECREE qui lui est rattachée
                ComplexAnnonceSedexSearch originalDecreeSearch = new ComplexAnnonceSedexSearch();
                arrayInSubType = new ArrayList<String>();
                arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue());
                originalDecreeSearch.setInSDXMessageSubType(arrayInSubType);
                originalDecreeSearch.setForSDXNoDecision(lastConfirmationFound.getSimpleAnnonceSedex()
                        .getNumeroDecision());
                originalDecreeSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(originalDecreeSearch);

                if (originalDecreeSearch.getSize() > 0) {
                    // On a trouvé le decree original
                    complexAnnonceSedex = (ComplexAnnonceSedex) originalDecreeSearch.getSearchResults()[0];
                    return complexAnnonceSedex;
                }
            }

        } catch (Exception e) {
            return new ComplexAnnonceSedex();
        }
        return new ComplexAnnonceSedex();
    }

    /**
     * Récupération du montant dans une annonce
     * 
     * @param currentAnnonce
     * @return
     */
    public static String getMontant(ComplexAnnonceSedex currentAnnonce) {
        String returnMontant = "";
        ComplexAnnonceSedex complexAnnonceSedex = new ComplexAnnonceSedex();
        String typeAnnonce = currentAnnonce.getSimpleAnnonceSedex().getMessageSubType();

        // On ne traite que les décisions / interruptions et leurs confirmations / rejets
        if (!AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue().equals(typeAnnonce)
                && !AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue().equals(typeAnnonce)
                && !AMMessagesSubTypesAnnonceSedex.REJET_DECISION.getValue().equals(typeAnnonce)
                && !AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue().equals(typeAnnonce)
                && !AMMessagesSubTypesAnnonceSedex.CONFIRMATION_INTERRUPTION.getValue().equals(typeAnnonce)
                && !AMMessagesSubTypesAnnonceSedex.REJET_INTERRUPTION.getValue().equals(typeAnnonce)) {
            return "";
        }

        if (AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue().equals(typeAnnonce)) {
            // Lorsque c'est une nouvelle décision non envoyée, on prend le montant du subside en cours. Si elle est
            // déjà envoyé on récupère depuis le code XML de l'annonce
            if (!AMStatutAnnonceSedex.CREE.getValue().equals(currentAnnonce.getSimpleAnnonceSedex().getStatus())
                    && !AMStatutAnnonceSedex.ENVOYE.getValue().equals(
                            currentAnnonce.getSimpleAnnonceSedex().getStatus())) {
                return JANumberFormatter.fmt(currentAnnonce.getSimpleDetailFamille()
                        .getMontantContributionAvecSupplExtra(), true, true, false, 2);
            } else {
                complexAnnonceSedex = currentAnnonce;
            }
        } else if (AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue().equals(typeAnnonce)) {
            if (!AMStatutAnnonceSedex.CREE.getValue().equals(currentAnnonce.getSimpleAnnonceSedex().getStatus())
                    && !AMStatutAnnonceSedex.ENVOYE.getValue().equals(
                            currentAnnonce.getSimpleAnnonceSedex().getStatus())) {
                complexAnnonceSedex = AMSedexHelper.getDecreeOriginal(currentAnnonce);
            } else {
                complexAnnonceSedex = currentAnnonce;
            }
        }

        if (!complexAnnonceSedex.isNew()) {
            returnMontant = AMSedexHelper.getValueFromBalise(complexAnnonceSedex.getSimpleAnnonceSedex(), "amount");
        }

        return returnMontant;
    }

    /**
     * Récupération des périodes de l'annonce
     * 
     * @param typeAnnonce
     * @param XMLStr
     * @return
     */
    public static String getPeriodeInfo(String typeAnnonce, String XMLStr) {
        String returnPeriode = "";

        if (JadeStringUtil.isEmpty(XMLStr)) {
            return returnPeriode;
        }
        try {
            String strDebutPeriode = "";
            String strFinPeriode = "";
            String periodeDebut = "";
            String periodeFin = "";
            boolean MMYYYY = true;
            if (typeAnnonce.equals(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue())
                    || typeAnnonce.equals(AMMessagesSubTypesAnnonceSedex.DEMANDE_RAPPORT_ASSURANCE.getValue())) {
                strDebutPeriode = "beginMonth>";
                strFinPeriode = "endMonth>";
            } else if (typeAnnonce.equals(AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue())) {
                strDebutPeriode = "beginMonth>";
                strFinPeriode = "stopMonth>";
            } else if (typeAnnonce.equals(AMMessagesSubTypesAnnonceSedex.MUTATION_RAPPORT_ASSURANCE.getValue())) {
                strDebutPeriode = "mutationDate>";
                MMYYYY = false;
            }

            if (!JadeStringUtil.isEmpty(strDebutPeriode)) {
                int indexDebut = JadeStringUtil.indexOf(XMLStr, strDebutPeriode);
                if (indexDebut > 0) {
                    if (MMYYYY) {
                        periodeDebut = JadeStringUtil.substring(XMLStr, indexDebut + strDebutPeriode.length(), 7);
                        periodeDebut = JadeStringUtil.substring(periodeDebut, 5, 2) + "."
                                + JadeStringUtil.substring(periodeDebut, 0, 4);
                    } else {
                        periodeDebut = JadeStringUtil.substring(XMLStr, indexDebut + strDebutPeriode.length(), 10);
                        periodeDebut = JadeStringUtil.substring(periodeDebut, 8, 2) + "."
                                + JadeStringUtil.substring(periodeDebut, 5, 2) + "."
                                + JadeStringUtil.substring(periodeDebut, 0, 4);
                        ;
                    }
                    returnPeriode = periodeDebut;
                }
            }

            if (!JadeStringUtil.isEmpty(strFinPeriode)) {
                int indexFin = JadeStringUtil.indexOf(XMLStr, strFinPeriode);
                if (indexFin > 0) {
                    String periodeFinVal = JadeStringUtil.substring(XMLStr, indexFin + strFinPeriode.length(), 7);
                    if (!JadeStringUtil.startsWith(periodeFinVal, "</")) {
                        periodeFin += periodeFinVal;
                        periodeFin = JadeStringUtil.substring(periodeFin, 5, 2) + "."
                                + JadeStringUtil.substring(periodeFin, 0, 4);

                        returnPeriode += " - " + periodeFin;
                    }
                }
            }
        } catch (Exception e) {
            returnPeriode = "";
        }
        return returnPeriode;
    }

    /**
     * Récupère la valeur de la balise passé en paramètre dans currentAnnonce
     * 
     * @param currentAnnonce
     * @param balise
     * @return
     */
    public static String getValueFromBalise(SimpleAnnonceSedex currentAnnonce, String balise) {

        String XMLStr = currentAnnonce.getMessageContent();
        String valeur = "";

        if (JadeStringUtil.isEmpty(XMLStr) || JadeStringUtil.isEmpty(balise) || (currentAnnonce == null)
                || currentAnnonce.isNew()) {
            return "";
        }

        try {
            String strSearch = "<" + balise + ">";
            String strSearchEnd = "</" + balise + ">";

            int indexDebut = JadeStringUtil.indexOf(XMLStr, strSearch);
            int indexFin = JadeStringUtil.indexOf(XMLStr, strSearchEnd);
            int length = (indexFin - strSearch.length()) - indexDebut;
            if (indexDebut > 0) {

                valeur = JadeStringUtil.substring(XMLStr, indexDebut + strSearch.length(), length);

            }

        } catch (Exception e) {
            valeur = "";
        }

        return valeur;
    }

}
