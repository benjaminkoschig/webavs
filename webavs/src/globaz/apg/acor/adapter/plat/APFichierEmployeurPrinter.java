package globaz.apg.acor.adapter.plat;

import globaz.apg.acor.parser.APACORPrestationsParser;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.module.calcul.APReferenceDataParser;
import globaz.apg.module.calcul.salaire.APSalaireAdapter;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.PRStringUtils;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe permettant la création du fichier employeurs pour le traitement d'une demande de prestation APG ou maternité
 * avec ACOR.
 * </p>
 * 
 * @author vre
 */
public class APFichierEmployeurPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final JACalendar CALENDAR = new JACalendarGregorian();
    private static final int ETAT_APRES_MONTANT_VERSE = 3;
    private static final int ETAT_NORMAL = 1;

    private static final int ETAT_PENDANT_MONTANT_VERSE = 2;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private int etat = APFichierEmployeurPrinter.ETAT_NORMAL;
    private HashMap idContrats = null;

    private int idSituationProfessionnelle;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe FichierEmployeurPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public APFichierEmployeurPrinter(APAbstractACORDroitAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private APAbstractACORDroitAdapter adapter() {
        return (APAbstractACORDroitAdapter) parent;
    }

    private String dateMoinsUnJour(String date) throws PRACORException {
        try {
            return APFichierEmployeurPrinter.CALENDAR.addDays(date, -1);
        } catch (JAException e) {
            throw new PRACORException(getSession().getLabel("ERROR_FORMAT_DATE") + date, e);
        }
    }

    private String datePlusUnJour(String date) throws PRACORException {
        try {
            return APFichierEmployeurPrinter.CALENDAR.addDays(date, 1);
        } catch (JAException e) {
            throw new PRACORException(getSession().getLabel("ERROR_FORMAT_DATE") + date, e);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        return (etat != APFichierEmployeurPrinter.ETAT_NORMAL)
                || (idSituationProfessionnelle < adapter().situationsProfessionnelles().size());
    }

    private int idContrat(String numAffilie) {
        if (idContrats == null) {
            idContrats = new HashMap();
        }

        Integer idContrat = (Integer) idContrats.get(numAffilie);

        if (idContrat == null) {
            idContrat = new Integer(1);
        } else {
            idContrat = new Integer(idContrat.intValue() + 1);
        }

        idContrats.put(numAffilie, idContrat);

        return idContrat.intValue();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        APSituationProfessionnelle sitPro = adapter().situationProfessionnelle(idSituationProfessionnelle);
        String dateDebut = PRACORConst.CA_DATE_VIDE;
        String dateFin = PRACORConst.CA_DATE_VIDE;
        boolean forcer100Pourcent = false;

        switch (etat) {
            case ETAT_NORMAL:
                if (!JAUtil.isDateEmpty(sitPro.getDateDebut())) {
                    dateFin = dateMoinsUnJour(sitPro.getDateDebut());
                    forcer100Pourcent = true;
                    etat = APFichierEmployeurPrinter.ETAT_PENDANT_MONTANT_VERSE;
                } else if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                    dateFin = sitPro.getDateFin();
                    etat = APFichierEmployeurPrinter.ETAT_APRES_MONTANT_VERSE;
                } else if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                    dateFin = sitPro.getDateFinContrat();
                }

                break;

            case ETAT_PENDANT_MONTANT_VERSE:
                dateDebut = sitPro.getDateDebut();

                if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                    dateFin = sitPro.getDateFin();
                    etat = APFichierEmployeurPrinter.ETAT_APRES_MONTANT_VERSE;
                } else {
                    if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                        dateFin = sitPro.getDateFinContrat();
                    }

                    etat = APFichierEmployeurPrinter.ETAT_NORMAL;
                }

                break;

            case ETAT_APRES_MONTANT_VERSE:
                dateDebut = datePlusUnJour(sitPro.getDateFin());

                if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                    dateFin = sitPro.getDateFinContrat();
                }

                forcer100Pourcent = true;
                etat = APFichierEmployeurPrinter.ETAT_NORMAL;

                break;

            default:
                throw new PRACORException("nous sommes tombés dans un état inconnu");
        }

        if (etat == APFichierEmployeurPrinter.ETAT_NORMAL) {
            ++idSituationProfessionnelle;
        }

        String[] salaire = salaire(sitPro, forcer100Pourcent);
        String numAffilie;
        String nomAffilie;

        try {
            numAffilie = sitPro.loadEmployeur().loadNumero();
            nomAffilie = sitPro.loadEmployeur().loadNom();
        } catch (Exception e) {
            throw new PRACORException(parent.getSession().getLabel("ERREUR_CHARGEMENT_EMPLOYEUR"), e);
        }

        // 1. le numéro affilié de l'employeur
        this.writeNumAffilie(writer, numAffilie);

        // 2. le nom (raison sociale) de l'employeur
        int nbContrats = adapter().nbContrats(numAffilie);

        // indicer le nom de l'employeur s'il y a plusieurs contrats
        if (nbContrats > 1) {
            this.writeChaine(writer, PRStringUtils.indicer(nomAffilie, 25, idContrat(numAffilie)));
        } else {
            if ((nomAffilie != null) && (nomAffilie.length() > 30)) {
                this.writeChaine(writer, nomAffilie.substring(0, 30));
            } else {
                this.writeChaine(writer, nomAffilie);
            }
        }

        // 3. le salaire
        // Avant de saisir le salaire, si l'autre rémunération est un pourcent
        // du salaire, alors on l'additionne
        // au montant du tableau, si c'est pas un pourcentage, on fait comme
        // avant...
        if (sitPro.getIsPourcentAutreRemun().booleanValue()) {

            FWCurrency sal = new FWCurrency();

            // on ajoute le salaire de base
            sal.add(salaire[0]);

            // si pourcent, on calcule ce qu'on doit ajouter au salaire de base
            FWCurrency autreRem = new FWCurrency(sitPro.getAutreRemuneration());

            sal.add((sal.doubleValue() / 100) * autreRem.doubleValue());

            this.writeReel(writer, sal.toString());

        } else {
            this.writeReel(writer, salaire[0]);
        }

        // 4. le type de salaire (valeurs admises: toutes)
        this.writeEntier(writer, salaire[1]);

        // 5. Le nombre d'heures par semaines s'il s'agit d'un salaire horaire
        // si le nombre d'heures est un float on arrondit au 0.1 le plus proche
        // sinon au 1 le plus proche
        if (salaire[2].lastIndexOf(".") == -1) {
            this.writeEntier(writer, JANumberFormatter.round(salaire[2], 1, 0, JANumberFormatter.NEAR));
        } else {
            this.writeEntier(writer, JANumberFormatter.round(salaire[2], 0.1, 1, JANumberFormatter.NEAR));
        }

        // 6. prestations en nature
        this.writeReel(writer, sitPro.getSalaireNature());

        // 7. type de prestations en nature (valeurs admises, toutes)
        this.writeEntier(writer, PRACORConst.csPeriodiciteSalaireToAcor(sitPro.getPeriodiciteSalaireNature()));

        if (!sitPro.getIsPourcentAutreRemun().booleanValue()) {
            // 8. 13e, gratifications, autre salaire
            this.writeReel(writer, sitPro.getAutreRemuneration());
        } else {
            this.writeChampVide(writer);
        }

        // 9. type de l'autre salaire (valeurs admises, toutes)
        this.writeEntier(writer, PRACORConst.csPeriodiciteSalaireToAcor(sitPro.getPeriodiciteAutreRemun()));

        // 10. Pourcentage du dernier salaire qui va être versé
        this.writeEntier(writer, salaire[3]); // valeur entiere 0..100

        // 11. statut
        this.writeEntier(writer, salaire[6]);

        // 12. date de début de contrat
        this.writeDate(writer, dateDebut);

        // 13. date de fin de contrat
        this.writeDate(writer, dateFin);

        // 14. paiement à l'assuré
        this.writeEntier(writer, salaire[7]);

        // 15. calcul des cotisations AVS/AI/APG
        this.writeOuiNon(writer, false); // on ne veut pas qu'acor calcule les
        // cotisations.

        // 16. calcul des cotisations AC
        this.writeOuiNon(writer, false); // idem

        // 17. canton pour l'imposition (seuls les assures sont imposes)
        this.writeChaine(writer, PRACORConst.CA_CODE_3_VIDE);

        // 18. taux d'imposition à la source
        this.writeReel(writer, PRACORConst.CA_REEL_VIDE);

        // 19. champ vide
        this.writeChampVide(writer);

        // 20. calcul des cotisation LFA
        this.writeOuiNon(writer, false);

        // 21. salaire versé pendant la période de service/congé maternité
        this.writeReel(writer, salaire[4]);

        // 22. type salaire versé pendant la période service/congé
        this.writeEntierSansFinDeChamp(writer, salaire[5]);
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {
        APSituationProfessionnelle sitPro = adapter().situationProfessionnelle(idSituationProfessionnelle);
        String dateDebut = PRACORConst.CA_DATE_VIDE;
        String dateFin = PRACORConst.CA_DATE_VIDE;
        boolean forcer100Pourcent = false;

        switch (etat) {
            case ETAT_NORMAL:
                if (!JAUtil.isDateEmpty(sitPro.getDateDebut())) {
                    dateFin = dateMoinsUnJour(sitPro.getDateDebut());
                    forcer100Pourcent = true;
                    etat = APFichierEmployeurPrinter.ETAT_PENDANT_MONTANT_VERSE;
                } else if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                    dateFin = sitPro.getDateFin();
                    etat = APFichierEmployeurPrinter.ETAT_APRES_MONTANT_VERSE;
                } else if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                    dateFin = sitPro.getDateFinContrat();
                }

                break;

            case ETAT_PENDANT_MONTANT_VERSE:
                dateDebut = sitPro.getDateDebut();

                if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                    dateFin = sitPro.getDateFin();
                    etat = APFichierEmployeurPrinter.ETAT_APRES_MONTANT_VERSE;
                } else {
                    if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                        dateFin = sitPro.getDateFinContrat();
                    }

                    etat = APFichierEmployeurPrinter.ETAT_NORMAL;
                }

                break;

            case ETAT_APRES_MONTANT_VERSE:
                dateDebut = datePlusUnJour(sitPro.getDateFin());

                if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                    dateFin = sitPro.getDateFinContrat();
                }

                forcer100Pourcent = true;
                etat = APFichierEmployeurPrinter.ETAT_NORMAL;

                break;

            default:
                throw new PRACORException("nous sommes tombés dans un état inconnu");
        }

        if (etat == APFichierEmployeurPrinter.ETAT_NORMAL) {
            ++idSituationProfessionnelle;
        }

        String[] salaire = salaire(sitPro, forcer100Pourcent);
        String numAffilie;
        String nomAffilie;

        try {
            numAffilie = sitPro.loadEmployeur().loadNumero();
            nomAffilie = sitPro.loadEmployeur().loadNom();

            if (!JadeStringUtil.isBlankOrZero(sitPro.loadEmployeur().getIdAffilie())) {
                try {
                    AFAffiliation af = new AFAffiliation();
                    af.setSession((BSession) PRSession.connectSession(getSession(),
                            AFApplication.DEFAULT_APPLICATION_NAOS));
                    af.setAffiliationId(sitPro.loadEmployeur().getIdAffilie());
                    af.retrieve();

                    if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(af.getTypeAffiliation())) {
                        nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP + nomAffilie;
                    } else if (IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(af.getTypeAffiliation())) {
                        nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_INDEP_EMPLOY + nomAffilie;
                    } else if (IAFAffiliation.TYPE_AFFILI_EMPLOY.equals(af.getTypeAffiliation())) {
                        nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY + nomAffilie;
                    }

                    else if (IAFAffiliation.TYPE_AFFILI_EMPLOY_D_F.equals(af.getTypeAffiliation())) {
                        nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_EMPLOY_D_F + nomAffilie;
                    } else if (IAFAffiliation.TYPE_AFFILI_LTN.equals(af.getTypeAffiliation())) {
                        nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_LTN + nomAffilie;
                    } else if (IAFAffiliation.TYPE_AFFILI_TSE.equals(af.getTypeAffiliation())) {
                        nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_TSE + nomAffilie;
                    } else if (IAFAffiliation.TYPE_AFFILI_TSE_VOLONTAIRE.equals(af.getTypeAffiliation())) {
                        nomAffilie = APACORPrestationsParser.CONST_TYPE_AFFILI_TSE_VOLONTAIRE + nomAffilie;
                    }

                } catch (Exception e) {
                    ;
                }
            }
        } catch (Exception e) {
            throw new PRACORException(parent.getSession().getLabel("ERREUR_CHARGEMENT_EMPLOYEUR"), e);
        }

        // 1. le numéro affilié de l'employeur
        this.writeNumAffilie(writer, numAffilie);

        // 2. le nom (raison sociale) de l'employeur
        int nbContrats = adapter().nbContrats(numAffilie);

        // indicer le nom de l'employeur s'il y a plusieurs contrats
        if (nbContrats > 1) {
            this.writeChaine(writer, PRStringUtils.indicer(nomAffilie, 25, idContrat(numAffilie)));
        } else {
            if ((nomAffilie != null) && (nomAffilie.length() > 30)) {
                this.writeChaine(writer, nomAffilie.substring(0, 30));
            } else {
                this.writeChaine(writer, nomAffilie);
            }
        }

        // 3. le salaire
        // Avant de saisir le salaire, si l'autre rémunération est un pourcent
        // du salaire, alors on l'additionne
        // au montant du tableau, si c'est pas un pourcentage, on fait comme
        // avant...
        if (sitPro.getIsPourcentAutreRemun().booleanValue()) {

            FWCurrency sal = new FWCurrency();

            // on ajoute le salaire de base
            sal.add(salaire[0]);

            // si pourcent, on calcule ce qu'on doit ajouter au salaire de base
            FWCurrency autreRem = new FWCurrency(sitPro.getAutreRemuneration());

            sal.add((sal.doubleValue() / 100) * autreRem.doubleValue());

            this.writeReel(writer, sal.toString());

        } else {
            this.writeReel(writer, salaire[0]);
        }

        // 4. le type de salaire (valeurs admises: toutes)
        this.writeEntier(writer, salaire[1]);

        // 5. Le nombre d'heures par semaines s'il s'agit d'un salaire horaire
        // si le nombre d'heures est un float on arrondit au 0.1 le plus proche
        // sinon au 1 le plus proche
        if (salaire[2].lastIndexOf(".") == -1) {
            this.writeEntier(writer, JANumberFormatter.round(salaire[2], 1, 0, JANumberFormatter.NEAR));
        } else {
            this.writeEntier(writer, JANumberFormatter.round(salaire[2], 0.1, 1, JANumberFormatter.NEAR));
        }

        // 6. prestations en nature
        this.writeReel(writer, sitPro.getSalaireNature());

        // 7. type de prestations en nature (valeurs admises, toutes)
        this.writeEntier(writer, PRACORConst.csPeriodiciteSalaireToAcor(sitPro.getPeriodiciteSalaireNature()));

        if (!sitPro.getIsPourcentAutreRemun().booleanValue()) {
            // 8. 13e, gratifications, autre salaire
            this.writeReel(writer, sitPro.getAutreRemuneration());
        } else {
            this.writeChampVide(writer);
        }

        // 9. type de l'autre salaire (valeurs admises, toutes)
        this.writeEntier(writer, PRACORConst.csPeriodiciteSalaireToAcor(sitPro.getPeriodiciteAutreRemun()));

        // 10. Pourcentage du dernier salaire qui va être versé
        this.writeEntier(writer, salaire[3]); // valeur entiere 0..100

        // 11. statut
        this.writeEntier(writer, salaire[6]);

        // 12. date de début de contrat
        this.writeDate(writer, dateDebut);

        // 13. date de fin de contrat
        this.writeDate(writer, dateFin);

        // 14. paiement à l'assuré
        this.writeEntier(writer, salaire[7]);

        // 15. calcul des cotisations AVS/AI/APG
        this.writeOuiNon(writer, false); // on ne veut pas qu'acor calcule les
        // cotisations.

        // 16. calcul des cotisations AC
        this.writeOuiNon(writer, false); // idem

        // 17. canton pour l'imposition (seuls les assures sont imposes)
        this.writeChaine(writer, PRACORConst.CA_CODE_3_VIDE);

        // 18. taux d'imposition à la source
        this.writeReel(writer, PRACORConst.CA_REEL_VIDE);

        // 19. champ vide
        this.writeChampVide(writer);

        // 20. calcul des cotisation LFA
        this.writeOuiNon(writer, false);

        // 21. salaire versé pendant la période de service/congé maternité
        this.writeReel(writer, salaire[4]);

        // 22. type salaire versé pendant la période service/congé
        this.writeEntierSansFinDeChamp(writer, salaire[5]);
    }

    // retourne un tableau {salaire, typeSalaire, heuresSemaines,
    // pourcentageVerse, montantVerse, statut}
    private String[] salaire(APSituationProfessionnelle sitPro, boolean forcer100Pourcent) throws PRACORException {
        String[] retValue = new String[8];
        APSalaireAdapter adapter = new APSalaireAdapter(sitPro);

        if (sitPro.getIsIndependant().booleanValue()) {
            // 0 le salaire
            retValue[0] = sitPro.getRevenuIndependant();

            // 1 le type de salaire
            retValue[1] = PRACORConst.CA_TYPE_SALAIRE_ANNUEL;

            // 2 le nombre d'heures pas semaine
            retValue[2] = PRACORConst.CA_ENTIER_VIDE;

            // 3 le pourcentage du montant versé
            retValue[3] = "100";

            // 4 le montant versé
            retValue[4] = sitPro.getRevenuIndependant();

            // 5 la périodicité du montant versé
            retValue[5] = PRACORConst.CA_TYPE_SALAIRE_ANNUEL;

            // 6 statut
            retValue[6] = PRACORConst.CA_STATUT_INDEPENDANT;

            // 7 le type de paiement
            retValue[7] = PRACORConst.CA_VERSEMENT_POURCENTAGE;
        } else {
            // 0 le salaire
            retValue[0] = adapter.salairePrincipal().getMontant();

            // 1 le type de salaire
            retValue[1] = PRACORConst.csPeriodiciteSalaireToAcor(adapter.salairePrincipal().getCsPeriodiciteSalaire());

            // 2 le nombre d'heures pas semaine
            retValue[2] = sitPro.getHeuresSemaine();

            if (!forcer100Pourcent && (adapter.montantVerse() != null)) {
                // 3 le pourcentage du montant versé
                retValue[3] = JANumberFormatter.round(adapter.montantVerse().getPourcentage(), 0.01, 2,
                        JANumberFormatter.NEAR);

                // 4 le montant versé
                retValue[4] = adapter.montantVerse().getMontant();

                // 5 la périodicité du montant versé
                retValue[5] = PRACORConst.csPeriodiciteSalaireToAcor(adapter.montantVerse().getCsPeriodiciteSalaire());

                // 7 le type de versement
                retValue[7] = adapter.montantVerse().isPourcent() ? PRACORConst.CA_VERSEMENT_POURCENTAGE
                        : PRACORConst.CA_VERSEMENT_MONTANT;
            } else {
                // 3 le pourcentage du montant versé
                retValue[3] = "100";

                // 4 le montant versé
                retValue[4] = adapter.salairePrincipal().getMontant();

                // 5 la périodicité du montant versé
                retValue[5] = PRACORConst.csPeriodiciteSalaireToAcor(adapter.salairePrincipal()
                        .getCsPeriodiciteSalaire());

                // 7 le type de versement
                retValue[7] = PRACORConst.CA_VERSEMENT_POURCENTAGE;
            }

            // 6 statut
            retValue[6] = PRACORConst.CA_STATUT_SALARIE;
        }

        if (!sitPro.getIsVersementEmployeur().booleanValue()) {
            // écraser le type de paiement si la prestation doit etre versée à
            // l'assuré
            retValue[7] = PRACORConst.CA_VERSEMENT_ASSURE;
        }

        if (sitPro.getIsAllocationMax().booleanValue()) {
            // ecraser les montants si on veut l'allocation max.

            try {
                FWCurrency montantMax;

                if (adapter().droit instanceof APDroitMaternite) {
                    montantMax = APReferenceDataParser.loadReferenceData(getSession(), "MATERNITE",
                            new JADate(adapter().getDroit().getDateDebutDroit()),
                            new JADate(adapter().getDroit().getDateFinDroit()),
                            new JADate(adapter().getDroit().getDateFinDroit())).getMontantJournalierMax();
                } else {
                    montantMax = APReferenceDataParser.loadReferenceData(getSession(), "APG",
                            new JADate(adapter().getDroit().getDateDebutDroit()),
                            new JADate(adapter().getDroit().getDateFinDroit()),
                            new JADate(adapter().getDroit().getDateFinDroit())).getMontantJournalierMax();
                }

                retValue[0] = montantMax.getBigDecimalValue().multiply(new BigDecimal(40d)).toString();
            } catch (Exception e) {
                throw new PRACORException(getSession().getLabel("ERROR_CHARGEMENT_MONTANT_MAXIMUM"));
            }

            retValue[1] = PRACORConst.CA_TYPE_SALAIRE_MENSUEL;
            retValue[3] = "100";
            retValue[7] = PRACORConst.CA_VERSEMENT_POURCENTAGE;
        }

        return retValue;
    }
}
