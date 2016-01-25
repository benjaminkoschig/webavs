package ch.globaz.pegasus.businessimpl.utils.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.ConstantesCalcul;
import ch.globaz.pegasus.business.constantes.IPCCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroit;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroitSearch;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.VariableMetier;

/**
 * Factory fournissant des conditions de partage des dates pour le calcul du droit.<br>
 * Pour l'utiliser, appellez la méthode getInstance().
 * 
 * @author ECO
 * 
 */
public final class ConditionPartageFactory {

    /**
     * Instance servant pour le singleton
     */
    private final static ConditionPartageFactory instance = new ConditionPartageFactory();

    /**
     * retourne la date arrondie au mois supérieur
     * 
     * @param originalDate
     *            La date à arrondir
     * @return La date arrondie
     */
    private final static String ceilDate(String originalDate) {
        if (JadeStringUtil.isBlank(originalDate)) {
            return null;
        }
        if (JadeDateUtil.isGlobazDateMonthYear(originalDate)) {
            return originalDate;
        } else {
            Calendar parsedCalendar = JadeDateUtil.getGlobazCalendar(originalDate);
            if (parsedCalendar.get(Calendar.DAY_OF_MONTH) > 1) {
                parsedCalendar.add(Calendar.MONTH, 1);
            }
            parsedCalendar.set(Calendar.DAY_OF_MONTH, 1);
            return JadeDateUtil.getGlobazFormattedDate(parsedCalendar.getTime());
        }
    }

    /**
     * retourne la date arrondie au premier du mois
     * 
     * @param originalDate
     *            La date à arrondir
     * @return La date arrondie
     */
    private final static String floorDate(String originalDate) {
        if (JadeStringUtil.isBlank(originalDate)) {
            return null;
        }
        if (JadeDateUtil.isGlobazDateMonthYear(originalDate)) {
            return originalDate;
        } else {
            Calendar parsedCalendar = JadeDateUtil.getGlobazCalendar(originalDate);
            parsedCalendar.set(Calendar.DAY_OF_MONTH, 1);
            return JadeDateUtil.getGlobazFormattedDate(parsedCalendar.getTime());
        }
    }

    /**
     * Retourne une instance unique du factory, selon le template du singleton
     * 
     * @return
     */
    public final static ConditionPartageFactory getInstance() {
        return ConditionPartageFactory.instance;
    }

    /**
     * liste des conditions de partage de périodes
     */
    private List<IConditionPartagePeriode> listeConditions = new ArrayList<IConditionPartagePeriode>();

    /**
     * Constructeur de la factory. Toutes les conditions sont implémentées, comme classes anonymes, ou ajoutées ici.
     */
    private ConditionPartageFactory() {
        super();

        // condition nouvelle année civile
        listeConditions.add(new IConditionPartagePeriode() {

            @Override
            public Collection<String> calculateDates(String dateDebut,
                    Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal) {
                Set<String> dates = new HashSet<String>();
                Calendar maintenant = Calendar.getInstance();
                Calendar debutPlage = JadeDateUtil.getGlobazCalendar(dateDebut);
                for (int i = debutPlage.get(Calendar.YEAR) + 1; i <= maintenant.get(Calendar.YEAR); i++) {
                    dates.add("01.01." + i);
                }

                return dates;
            }

        });

        // condition début/fin donnée financière
        listeConditions.add(new IConditionPartagePeriode() {

            @Override
            public Collection<String> calculateDates(String dateDebut,
                    Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal) {
                Set<String> dates = new HashSet<String>();
                CalculDonneesDroitSearch searchModel = (CalculDonneesDroitSearch) cacheDonnees
                        .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT);
                for (JadeAbstractModel df : searchModel.getSearchResults()) {
                    CalculDonneesDroit donnee = (CalculDonneesDroit) df;
                    if (donnee.getDateDebutDonneeFinanciere() != null) {

                        String dateDebutDF;
                        if (IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                            dateDebutDF = "01." + donnee.getDateDebutDonneeFinanciere();
                        } else {
                            dateDebutDF = ConditionPartageFactory.ceilDate("01."
                                    + donnee.getDateDebutDonneeFinanciere());
                        }
                        if (JadeDateUtil.isDateBefore(dateDebutDF, dateDebut)) {
                            dateDebutDF = dateDebut;
                        }
                        dates.add(dateDebutDF);

                        String dateFinDF = donnee.getDateFinDonneeFinanciere();
                        if (!JadeStringUtil.isBlank(dateFinDF)) {
                            dateFinDF = ConditionPartageFactory.ceilDate("02." + dateFinDF);
                            if (JadeDateUtil.isDateBefore(dateFinDF, dateDebut)) {
                                dateFinDF = dateDebut;
                            }
                        } else {
                            dateFinDF = null;
                        }

                        dates.add(dateFinDF);
                    }
                }
                return dates;
            }
        });

        // condition de changement d'état civil
        listeConditions.add(new IConditionPartagePeriode() {

            @Override
            public Collection<String> calculateDates(String dateDebut,
                    Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal) {

                Set<String> dates = new HashSet<String>();
                CalculDonneesDroitSearch searchModel = (CalculDonneesDroitSearch) cacheDonnees
                        .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT);
                for (JadeAbstractModel df : searchModel.getSearchResults()) {
                    CalculDonneesDroit donnee = (CalculDonneesDroit) df;
                    // TODO avoir les bons models et ajouter dates
                }

                return dates;
            }
        });

        // condition de nouvel enfant ou echeance (atteint 25 ans)
        listeConditions.add(new IConditionPartagePeriode() {

            @Override
            public Collection<String> calculateDates(String dateDebut,
                    Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal) {

                final String maintenant = JadeDateUtil.getGlobazFormattedDate(new Date());
                final int dureePlage = JadeDateUtil.getNbYearsBetween(dateDebut, maintenant);

                Set<String> dates = new HashSet<String>();
                CalculDonneesDroitSearch searchModel = (CalculDonneesDroitSearch) cacheDonnees
                        .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT);
                for (JadeAbstractModel abstractDonnee : searchModel.getSearchResults()) {
                    CalculDonneesDroit donnee = (CalculDonneesDroit) abstractDonnee;
                    final String dateNaissance = donnee.getDateNaissance();
                    final int age = JadeDateUtil.getNbYearsBetween(dateNaissance, maintenant)
                            - IPCCalcul.AGE_INDEPENDANCE_ENFANT;

                    if ((dateNaissance != null)
                            && (JadeDateUtil.isDateAfter(dateNaissance, dateDebut) || ((age >= 0) && (age < dureePlage)))) {
                        dates.add(ConditionPartageFactory.ceilDate(dateNaissance));
                    }

                }
                return dates;
            }
        });

        // condition de enfant arrivant à échéance (fin d'études)

        // condition de décès
        listeConditions.add(new IConditionPartagePeriode() {

            @Override
            public Collection<String> calculateDates(String dateDebut,
                    Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal) {

                Set<String> dates = new HashSet<String>();
                CalculDonneesDroitSearch searchModel = (CalculDonneesDroitSearch) cacheDonnees
                        .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT);
                for (JadeAbstractModel abstractDonnee : searchModel.getSearchResults()) {
                    CalculDonneesDroit donnee = (CalculDonneesDroit) abstractDonnee;
                    final String dateDeces = donnee.getDateDeces();
                    if ((dateDeces != null) && JadeDateUtil.isDateAfter(dateDeces, dateDebut)) {
                        dates.add(ConditionPartageFactory.ceilDate(dateDeces));
                    }
                }
                return dates;
            }
        });

        // condition de domicile

        // condition de debut/fin de parametre
        listeConditions.add(new IConditionPartagePeriode() {

            @Override
            public Collection<String> calculateDates(String dateDebut,
                    Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal) {
                Set<String> dates = new HashSet<String>();

                // final Date dateDebutD = JadeDateUtil.getGlobazDate(dateDebut);
                final Long dateDebutD = JadeDateUtil.getGlobazDate(dateDebut).getTime();

                // TODO setter maintenant à prochain paiement
                // final Date maintenant = new Date();
                final long maintenant = new Date().getTime();

                // Iteration sur les variables metiers
                for (VariableMetier varMet : containerGlobal.getListeVariablesMetiers()) {
                    // final String dateDebutVarMet = "01." + varMet..getDateDebut();
                    // final Date dateDebutVarMetD = JadeDateUtil.getGlobazDate(dateDebutVarMet);
                    // Iteration sur les périodes des variables métier
                    for (Long dateDebutPeriodeVarMet : varMet.getVariablesMetiers().keySet()) {
                        if ((dateDebutPeriodeVarMet >= dateDebutD) && (dateDebutPeriodeVarMet < maintenant)) {
                            String dateToAdd = JadeDateUtil.getGlobazFormattedDate(new Date(dateDebutPeriodeVarMet));
                            // JadeDateUtil.getGlobazFormattedDate(toAdd);//getFormattedDate(date)getDMYDate(date)

                            dates.add(dateToAdd);
                        }
                    }
                }

                return dates;
            }
        });

        // condition de prix de chambre du home
        listeConditions.add(new IConditionPartagePeriode() {

            @Override
            public Collection<String> calculateDates(String dateDebut,
                    Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal) {
                Set<String> dates = new HashSet<String>();

                return dates;
            }
        });

    }

    /**
     * Retourne la liste des conditions de partage de la plage temporelle.
     * 
     * @return the listeConditions
     */
    public List<IConditionPartagePeriode> getListeConditions() {
        return listeConditions;
    }

}
