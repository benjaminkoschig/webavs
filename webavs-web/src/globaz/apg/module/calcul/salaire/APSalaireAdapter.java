package globaz.apg.module.calcul.salaire;

import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.tools.PRCalcul;
import java.util.HashMap;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APSalaireAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static String AUTRE_REMUNERATION = "3";
    private static String MONTANT_VERSE = "4";
    private static String SALAIRE_NATURE = "2";
    private static String SALAIRE_PRINCIPAL = "1";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private HashMap salaires = new HashMap();
    private APSituationProfessionnelle sitPro;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APSalaireAdapter.
     * 
     * @param sitPro
     *            DOCUMENT ME!
     */
    public APSalaireAdapter(APSituationProfessionnelle sitPro) {
        this.sitPro = sitPro;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public APMontantVerse montantVerse() {
        APMontantVerse retValue = (APMontantVerse) salaires.get(MONTANT_VERSE);
        // On met le montant versé sous la forme annuelle pour ACCOR
        if (sitPro.getPeriodiciteMontantVerse().equals(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)) {
            float x = JadeStringUtil.parseFloat(sitPro.getSalaireHoraire(), 0);
            float y = JadeStringUtil.parseFloat(sitPro.getHeuresSemaine(), 0);
            sitPro.setMontantVerse(String.valueOf(((x * y) / 7) * 360));
            sitPro.setPeriodiciteMontantVerse(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE);
        }
        if (retValue == null) {
            if (!JadeStringUtil.isDecimalEmpty(sitPro.getMontantVerse())) {
                if (sitPro.getIsPourcentMontantVerse().booleanValue()) {
                    retValue = new APMontantVerse(String.valueOf(PRCalcul.pourcentage100(salairePrincipal()
                            .getMontant(), sitPro.getMontantVerse())), sitPro.getMontantVerse(), true,
                            sitPro.getPeriodiciteMontantVerse());
                } else {
                    retValue = new APMontantVerse(sitPro.getMontantVerse(), String.valueOf(PRCalcul.quotientX100(
                            sitPro.getMontantVerse(), salairePrincipal().getMontant())), false,
                            sitPro.getPeriodiciteMontantVerse());
                }
            }

            salaires.put(MONTANT_VERSE, retValue);
        }

        return retValue;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public APSalaire remunerationAutre() {
        APSalaire retValue = (APSalaire) salaires.get(AUTRE_REMUNERATION);

        if (retValue == null) {
            if (!JadeStringUtil.isDecimalEmpty(sitPro.getAutreRemuneration())) {
                if (sitPro.getIsPourcentAutreRemun().booleanValue()) {
                    retValue = new APSalaire(String.valueOf(PRCalcul.pourcentage100(salairePrincipal().getMontant(),
                            sitPro.getAutreRemuneration())), sitPro.getPeriodiciteAutreRemun());
                } else {
                    retValue = new APSalaire(sitPro.getAutreRemuneration(), sitPro.getPeriodiciteAutreRemun());
                }
            }

            salaires.put(AUTRE_REMUNERATION, retValue);
        }

        return retValue;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public APSalaire salaireNature() {
        APSalaire retValue = (APSalaire) salaires.get(SALAIRE_NATURE);

        if (retValue == null) {
            if (!JadeStringUtil.isDecimalEmpty(sitPro.getSalaireNature())) {
                retValue = new APSalaire(sitPro.getSalaireNature(), sitPro.getPeriodiciteSalaireNature());
            }

            salaires.put(SALAIRE_NATURE, retValue);
        }

        return retValue;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public APSalaire salairePrincipal() {
        APSalaire retValue = (APSalaire) salaires.get(SALAIRE_PRINCIPAL);

        if (retValue == null) {
            if (!JadeStringUtil.isDecimalEmpty(sitPro.getSalaireHoraire())) {
                retValue = new APSalaire(sitPro.getSalaireHoraire(), IPRSituationProfessionnelle.CS_PERIODICITE_HEURE);
            } else if (!JadeStringUtil.isDecimalEmpty(sitPro.getSalaireMensuel())) {
                retValue = new APSalaire(sitPro.getSalaireMensuel(), IPRSituationProfessionnelle.CS_PERIODICITE_MOIS);
            } else if (!JadeStringUtil.isDecimalEmpty(sitPro.getAutreSalaire())) {
                retValue = new APSalaire(sitPro.getAutreSalaire(), sitPro.getPeriodiciteAutreSalaire());
            }

            salaires.put(SALAIRE_PRINCIPAL, retValue);
        }

        return retValue;
    }
}
