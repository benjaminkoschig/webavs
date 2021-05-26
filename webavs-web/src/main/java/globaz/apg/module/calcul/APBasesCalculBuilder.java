/*
 * Créé le 3 juin 05
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul;

import ch.globaz.common.util.Instances;
import globaz.apg.db.droits.*;
import globaz.apg.module.calcul.salaire.APMontantVerse;
import globaz.apg.module.calcul.salaire.APSalaireAdapter;
import globaz.apg.utils.APGUtils;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import lombok.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <H1>Description</H1>
 *
 * <p>
 * Charge toutes les informations relatives à un droit et crée les bases de calcul pour les prestations. ATTENTION: pas
 * thread safe !!!! Créer une nouvelle instance de cette classe pour chaque traitement !
 * </p>
 *
 * <p>
 * Le principe général de cette classe est de regrouper toutes les informations nécessaire au calcul d'une prestation
 * pour un droit donné et pour une période donnée.
 * </p>
 *
 * <p>
 * L'algorithme utilisé pour effectuer cette tâche est du type 'diviser pour mieux règner'. Le problème est divisé en
 * sous-problèmes. Chaque sous-problème est représenté par une classe interne qui implémente
 * APBaseCalculBuilder.Command. Cette interface représente un contrat du type du pattern Command (GOF). C'est-à-dire
 * qu'elles doivent encapsuler toutes les données et la logique nécessaire à la résolution du sous-problème. Il y a une
 * classe pour chaque type de sous-problème, par exemple la fin de l'année, un changement de situation professionnelle
 * (par exemple la fin d'un contrat de travail ou le changement du montant versé par un employeur durant une période de
 * prestation) ou un changement de situation familiale (par exemple la naissance d'un enfant) ou, dans le cas des APG,
 * les débuts et fin d'une période de prestation, etc. Le schéma domaine des classes est:
 * </p>
 *
 * <p>
 * <img src="doc-files/general.gif" alt="schéma domaine">
 * </p>
 *
 * <p>
 * L'algorithme s'effectue en trois étapes:
 * </p>
 *
 * <h4>1. Création des commandes</h4>
 *
 * <p>
 * Une instance de commande spécifique à chaque sous-problème est créée.
 * </p>
 *
 * <p>
 * <img src="doc-files/etape_1.gif" alt="schéma explicatif de l'étape 1">
 * </p>
 *
 * <h4>2. tri des commandes</h4>
 *
 * <p>
 * les commandes sont ensuite triées par date d'exécution (les commandes sont java.lang.Comparable).
 * </p>
 *
 * <p>
 * <img src="doc-files/etape_2.gif" alt="schéma explicatif de l'étape 2">
 * </p>
 *
 * <h4>3. exécution des commandes</h4>
 *
 * <p>
 * enfin les commandes sont exécutées dans l'ordre pour créer les bases de calcul.
 * </p>
 *
 * <p>
 * <img src="doc-files/etape_3.gif" alt="diagramme UML de séquence des étapes 2 et 3">
 * </p>
 *
 * <h4>schéma UML (quasi exhaustif)</h4>
 *
 * <p>
 * <img src="doc-files/base_calcul_cd.gif" alt="diagramme de classe quasi complet">
 * </p>
 *
 * @author vre
 */
public abstract class APBasesCalculBuilder {

    /*
     * un simple formatteur de date JMA, il est nécessaire car le traitement des dates dans cette classe se fait au
     * moyen d'instances de java.util.Date et de java.util.Calendar
     */
    protected static final SimpleDateFormat DF = new SimpleDateFormat("dd.MM.yyyy");

    // crée une instance de Calendar en effacant les champs inutiles
    static Calendar getCalendarInstance() {
        Calendar retValue = Calendar.getInstance();

        retValue.set(Calendar.HOUR_OF_DAY, 12);
        retValue.set(Calendar.MINUTE, 0);
        retValue.set(Calendar.SECOND, 0);
        retValue.set(Calendar.MILLISECOND, 0);

        return retValue;
    }

    APBaseCalcul baseCourante = null;
    LinkedList<APBaseCalcul> bases = new LinkedList<>();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    Calendar calendar = getCalendarInstance();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    LinkedList<Command> commands = new LinkedList<>();

    APBaseCalcul derniereBaseCourante = new APBaseCalcul();

    APDroitLAPG droit;

    PRTauxImpositionManager mgrTauxImpot;

    HashMap<String, Integer> nbContratsList = new HashMap<>();

    int nbJoursSoldes;

    // le niveau d'activation est plus grand que 0 si l'on est dans une période
    // de prestation
    int niveauActivation = 0;

    BSession session;

    boolean versementInterdit = false;

    protected APBasesCalculBuilder(BSession session, APDroitLAPG droit) {
        this.droit = droit;
        this.session = session;
    }

    public static APBasesCalculBuilder of(final BSession session, final APDroitLAPG droit) {
        return Instances.of(droit)
                .is(APDroitProcheAidant.class, d -> new APBasesCalculProcheAidantBuilder(session, d))
                .is(APDroitPaternite.class, d -> new APBasesCalculPaterniteBuilder(session, d))
                .is(APDroitPandemie.class, d -> new APBasesCalculPandemieBuilder(session, d))
                .is(APDroitMaternite.class, d -> new APBasesCalculMaterniteBuilder(session, d))
                .is(APDroitLAPG.class, d -> new APBasesCalculAPGBuilder(session, d)).result();
    }

    protected List<APPeriodeComparable> getApPeriodeDroit(String idDroit) throws Exception {
        APPeriodeAPGManager mgr = new APPeriodeAPGManager();

        mgr.setSession(session);
        mgr.setForIdDroit(idDroit);
        mgr.find(session.getCurrentThreadTransaction());

        List<APPeriodeComparable> listPeriode;

        listPeriode = mgr.toList().stream().map(obj -> (APPeriodeAPG) obj).map(ap -> new APPeriodeComparable(ap)).collect(Collectors.toList());
        Collections.sort(listPeriode);
        return listPeriode;
    }

    protected String ajouterDateMinDebutParam(String valeur, String dateDebut) throws Exception {
        FWFindParameterManager manager = new FWFindParameterManager();
        manager.setSession(session);
        manager.setIdCodeSysteme("1");
        manager.setIdCleDiffere(valeur);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0){
            FWFindParameter param = (FWFindParameter) manager.get(0);
            dateDebut = ajouterDateMinDebutParam(new ch.globaz.common.domaine.Date(param.getDateDebutValidite()).getSwissValue());
        }
        return dateDebut;
    }

    protected String ajouterDateMinDebutParam(String dateDebutValiditeParam) throws ParseException {
        Date dateDebutValide = DF.parse(dateDebutValiditeParam);
        Date debut = DF.parse(droit.getDateDebutDroit());
        if (debut.before(dateDebutValide)) {
            Calendar c = Calendar.getInstance();
            c.setTime(dateDebutValide);
            c.add(Calendar.DATE, -1);
            commands.add(new VersementInterditCommand(null, true));
            commands.add(new VersementInterditCommand(c.getTime(), false));
        }
        return JadeDateUtil.getGlobazFormattedDate(dateDebutValide);
    }

    protected boolean changeImposition(APPeriodeComparable periode, PeriodeTauxCourant tauxCourant){
        return changeImposition(periode, tauxCourant.canton, tauxCourant.taux);
    }

    /**
     * Test s'il y a un changement d'imposition entre les périodes
     * @param periode à controler
     * @param canton canton courant
     * @param taux taux courant
     * @return vrai si changement
     */
    protected boolean changeImposition(APPeriodeComparable periode, String canton, String taux){
        String cantonPeriode = periode.getCantonImposition();
        String tauxPeriode = periode.getTauxImposition();

        if(JadeStringUtil.isBlankOrZero(canton)) {
            return !JadeStringUtil.isBlankOrZero(cantonPeriode);
        } else if(JadeStringUtil.isBlankOrZero(cantonPeriode)) {
            return true;
        } else if(!canton.equals(cantonPeriode)) {
            return true;
        } else {
            if(JadeStringUtil.isBlankOrZero(taux)) {
                return !JadeStringUtil.isBlankOrZero(tauxPeriode);
            } else if(JadeStringUtil.isBlankOrZero(tauxPeriode)) {
                return true;
            } else {
                return !taux.equals(tauxPeriode);
            }
        }
    }

       /**
         * couper par mois:
         *
     * allocations par mois, on ajoute donc des com
     *mandes qui vont découper la période de
         * prestation en unités mensuelles.
         *
         * Pour les versements rétro-actifs (c'est-à-dire avant la date du jour), la coupure n'est pas effectuée.
     *
     * @param debut
     * @param fin
         */
    protected void couperParMois(Date debut, Calendar fin) {
        Calendar moisDernier = getCalendarInstance(); // instancié à la date du
        // jour

        moisDernier.set(Calendar.DAY_OF_MONTH, 5); // on prend une date neutre
        moisDernier.add(Calendar.MONTH, -1); // du mois précédent

        // on commence l'itération au début du droit et on se positionne
        // toujours sur le dernier jour dudit mois
        calendar.setTime(debut);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        while (calendar.before(fin)) {
            // si on est avant la fin de la prestation
            if (calendar.after(moisDernier) || (calendar.get(Calendar.MONTH) == Calendar.DECEMBER)) {
                // si ce n'est pas un paiement rétroactif ou si on est en fin
                // d'année, ajouter la commande
                commands.add(new NouvelleBaseCommand(calendar.getTime(), false));
            }

            // passer au mois suivant et prendre le dernier jour de ce mois.
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
    }

    // ajouter les événements relatifs à la situation professionnelle à la liste
    // des commandes
    protected boolean ajouterSituationProfessionnelle() throws Exception {
        boolean isIndependant = false;
        // charger tous les changements de situation professionnelle
        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();

        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction(), BManager.SIZE_USEDEFAULT);

        // pour chaque situation professionnelle
        for (int idSitPro = 0; idSitPro < mgr.size(); ++idSitPro) {
            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) mgr.get(idSitPro);

            // mettre a jour le compte du nombre de contrats par employeur
            String cleEmployeur = sitPro.loadEmployeur().getCleUnique();
            Integer nbContrats = nbContratsList.get(cleEmployeur);
            if(sitPro.getIsIndependant()) {
                isIndependant = true;
            }
            if (nbContrats != null) {
                nbContrats = new Integer(nbContrats.intValue() + 1);
            } else {
                nbContrats = 1;
            }

            nbContratsList.put(cleEmployeur, nbContrats);

            /*
             * ajouter la commande.
             *
             * Il n'y a pas de date de début de contrat pour les situations professionnelles. Par définition, tous les
             * employeurs saisis sont ceux qui sont nécessaires au calcul de la prestation.
             *
             * On saisit donc une date d'exécution de la commande null. De cette manière, ces commandes seront exécutées
             * en premier (la procédure de tri des commandes le garantit).
             */
            commands.add(new EmployeurCommand(null, true, nbContrats.intValue(), sitPro));

            // si le contrat se termine durant la période du droit, le
            // bénéficiaire de la prestation sera modifié
            if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                commands.add(new EmployeurCommand(sitPro.getDateFinContrat(), false, nbContrats.intValue(), sitPro));
            }

            // ajouter une commande si le montant versé par l'employeur pour ce
            // droit est différent du salaire normal
            if (!JAUtil.isDateEmpty(sitPro.getDateDebut()) && !JadeStringUtil.isDecimalEmpty(sitPro.getMontantVerse())) {
                commands.add(new EmployeurChangeMontantCommand(sitPro.getDateDebut(), true, sitPro));

                if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                    commands.add(new EmployeurChangeMontantCommand(sitPro.getDateFin(), false, sitPro));
                }
            }
        }
        return isIndependant;
    }

    protected void ajouterTauxImposition() throws Exception {
        if (!(droit instanceof APDroitPaternite)
                && !(droit instanceof APDroitProcheAidant)
                && droit.getIsSoumisImpotSource().booleanValue()) {

            // si pas de date fin mettre la fin du mois en cours pour le calcul
            String dateFinDuDroit = droit.getDateFinDroit();
            if(JadeStringUtil.isEmpty(dateFinDuDroit)) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                dateFinDuDroit = JadeDateUtil.getGlobazFormattedDate(cal.getTime());
            }

            // on va rechercher tous les taux pour ce canton et pour la période
            // du droit
            String cantonImposition = PRTiersHelper.getCanton(session, droit.getNpa());
            List<PRTauxImposition> tauxImpots = findTauxImposition(droit.getDateDebutDroit(), dateFinDuDroit, cantonImposition);

            /*
             * remarque: s'il n'y a pas de taux d'impositions definis pour ce canton et qu'on n'en a pas saisi à la
             * main, aucune cotisation n'est creee...
             */
            if (((tauxImpots == null) || tauxImpots.isEmpty())
                    && JadeStringUtil.isDecimalEmpty(droit.getTauxImpotSource())) {
                return;
            }

            if (JadeStringUtil.isDecimalEmpty(droit.getTauxImpotSource())) {
                /*
                 * Si l'utilisateur n'a pas redefini de taux d'imposition on va rechercher tous les taux pour la periode
                 * du droit et segmenter eventuellement le droit s'il y a plusieurs taux differents.
                 */
                // il y a des taux d'imposition, on ajoute des commandes qui
                // vont renseigner la base de calcul
                for (int idTaux = tauxImpots.size(); --idTaux >= 0;) {
                    PRTauxImposition taux = tauxImpots.get(idTaux);

                    commands.add(new TauxImpositionCommand(taux.getDateDebut(), true, taux));
                }
            } else {
                PRTauxImposition taux = new PRTauxImposition();
                taux.setTaux(droit.getTauxImpotSource());
                commands.add(new TauxImpositionCommand(droit.getDateDebutDroit(), true, taux));
            }
        }
    }

    protected void ajouterTauxImposition(PeriodeTauxCourant periodeTauxCourant) throws Exception {
        ajouterTauxImposition(periodeTauxCourant.taux, periodeTauxCourant.dateDebut, periodeTauxCourant.dateFin, periodeTauxCourant.canton);
    }

    protected void ajouterTauxImposition(String tauxForce, String dateDebut, String dateFin, String canton) throws Exception {

        if(!JadeStringUtil.isBlankOrZero(tauxForce)){
            PRTauxImposition taux = new PRTauxImposition();
            taux.setTaux(tauxForce);
            commands.add(new TauxImpositionCommand(dateDebut, true, taux));
            commands.add(new TauxImpositionCommand(dateFin, false, taux));
        } else {
            // on va rechercher tous les taux pour ce canton et pour la période
            List<PRTauxImposition> tauxImpots = findTauxImposition(dateDebut, dateFin, canton);
            if (tauxImpots == null || tauxImpots.isEmpty()){
                return;
            }

            // il y a des taux d'imposition, on ajoute des commandes qui
            // vont renseigner la base de calcul
            PRTauxImposition lastTaux = null;
            for (int idTaux = tauxImpots.size(); --idTaux >= 0;) {
                PRTauxImposition taux = tauxImpots.get(idTaux);
                String dateDebutEncompte = dateDebut;
                if(JadeDateUtil.isDateAfter(taux.getDateDebut(), dateDebutEncompte)) {
                    dateDebutEncompte = taux.getDateDebut();
                }
                commands.add(new TauxImpositionCommand(dateDebutEncompte, true, taux));
                lastTaux = taux;
            }
            commands.add(new TauxImpositionCommand(dateFin, false, lastTaux));
        }
    }

    /**
     * Crée les bases de calculs pour les prestations.
     *
     * @return une liste (jamais null, peut-être vide) d'instances de APBaseCalcul
     *
     * @throws Exception
     *             si une erreur survient lors du chargement des informations dans la base
     */
    public List<APBaseCalcul> createBasesCalcul() throws Exception {
        ajoutDesCommandes();
        triDesCommandes();
        creerBasesDeCalcul();
        return bases;
    }

    abstract void ajoutDesCommandes() throws Exception;

    protected void triDesCommandes() {
        Collections.sort(commands);
    }

    protected void creerBasesDeCalcul() throws Exception {
        for(Command command: commands) {
            command.execute();
        }
    }

    protected List<PRTauxImposition> findTauxImposition(String dateDebut, String dateFin, String idCanton) throws Exception {
        if (mgrTauxImpot == null) {
            mgrTauxImpot = new PRTauxImpositionManager();
            mgrTauxImpot.setSession(session);
            mgrTauxImpot.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
            mgrTauxImpot.setOrderBy(PRTauxImposition.FIELDNAME_DATEDEBUT);
        }

        mgrTauxImpot.setForPeriode(dateDebut, dateFin);
        mgrTauxImpot.setForCsCanton(idCanton);
        mgrTauxImpot.find(BManager.SIZE_NOLIMIT);

        return mgrTauxImpot.getContainerAsList();
    }


    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /*
     * Une commande encapsule toutes les données et toute la logique nécessaire au traitement d'un événement.
     *
     * <p>Le type de l'événement est défini comme une sous-classe de la classe Commande.</p>
     *
     * <p>Par défaut, les informations indispensables à une commande sont une date d'exécution et un booléen indiquant
     * si la commande est le début ou la fin d'une période de modification de prestation. Par exemple, la naissance d'un
     * enfant est un début, la fin d'une période APG est une fin.</p>
     *
     * <p>L'exécution d'une commande affecte potentiellement les variables niveauActivation, baseCourante,
     * derniereBaseCourante et la liste des bases de la classe APBasesCalculBuilder.</p>
     *
     * <p>La variable la plus importante pour le sous-classement de Command ou l'exécution d'une commande est le niveau
     * d'activation. Cette variable permet de contenir le nombre de périodes donnant droit à une prestation à la date
     * courante. Par exemple, pour les droits maternité, cette variable sera incrémentée pour chaque nouveau-né et
     * décrémentée pour chaque fin de droit d'allocation. En fonction de cette variable, bases de calcul seront retenues
     * ou non.</p>
     *
     * @author vre
     */
    protected abstract class Command implements Comparable {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        /** La date d'exécution de la commande. */
        protected Date date = null;

        /**
         * Un booléen à vrai si la commande concerne le début d'une modification de traitement.
         */
        protected boolean debut;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe Command.
         *
         * @param date
         *            DOCUMENT ME!
         * @param debut
         *            DOCUMENT ME!
         */
        protected Command(Date date, boolean debut) {
            this.date = date;
            this.debut = debut;
        }

        /**
         * Crée une nouvelle instance de la classe Command.
         *
         * @param date
         *            DOCUMENT ME!
         * @param debut
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws ParseException
         *             DOCUMENT ME!
         */
        protected Command(String date, boolean debut) throws ParseException {
            if (!JadeStringUtil.isEmpty(date)) {
                this.date = DF.parse(date);
            }

            this.debut = debut;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /** Fermer la base courante et la transformer en dernière base courante. */
        public void cloreBaseCourante() {
            if (debut) {
                // on la ferme pour le jour précedent
                baseCourante.setDateFin(toJADate(-1));
            } else {
                // on la ferme pour le jour meme
                baseCourante.setDateFin(toJADate(0));
            }

            /*
             * on ajoute la base de calcul uniquement si la période pour la base est importante pour le calcul.
             * C'est-à-dire si le niveau d'activation est plus grand que 0.
             */
            if ((niveauActivation > 0) && !versementInterdit) {
                int nbJours;

                // calculer le nombre de jours soldes
                // les années sont forcément égales (les périodes sont splittées
                // par années)
                // les mois sont peut-être différents (paiements rétroactifs)
                if (baseCourante.getDateDebut().getMonth() == baseCourante.getDateFin().getMonth()) {
                    // même mois, même année, on soustrait simplement les jours
                    // (en incluant le premier)
                    nbJours = baseCourante.getDateFin().getDay() - baseCourante.getDateDebut().getDay() + 1;
                } else {
                    loadJADate(baseCourante.getDateFin());
                    nbJours = calendar.get(Calendar.DAY_OF_YEAR);
                    loadJADate(baseCourante.getDateDebut());
                    nbJours = nbJours - calendar.get(Calendar.DAY_OF_YEAR) + 1;
                }

                if (droit instanceof APDroitMaternite) {
                    baseCourante.setNombreJoursSoldes(nbJours);
                } else {
                    // TODO: actuellement, pour un droit APG, on répartit les
                    // jours soldés sur les premières périodes
                    if (nbJoursSoldes > 0) {
                        baseCourante.setNombreJoursSoldes(Math.min(nbJours, nbJoursSoldes));
                        nbJoursSoldes = Math.max(0, nbJoursSoldes - nbJours);
                    } else {
                        baseCourante.setNombreJoursSoldes(0);
                    }
                }

                bases.add(baseCourante);
            }

            derniereBaseCourante = baseCourante;
        }

        // charge une JADAte donnée dans this.calendar
        private void loadJADate(JADate date) {
            calendar.set(Calendar.DAY_OF_MONTH, 1); // eviter les erreurs
            // eventuelles a cause de
            // lenient == true
            calendar.set(Calendar.YEAR, date.getYear());
            calendar.set(Calendar.MONTH, date.getMonth() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
        }

        /**
         * Compare les dates d'exécution de deux commandes et retourne leur ordre naturel.
         *
         * <p>
         * Les null sont considérés comme plus petits que n'importe quelle date.
         * </p>
         *
         * @param o
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(Object o) {
            if (date == null) {
                if (((Command) o).date == null) {
                    return 0;
                } else {
                    return -1;
                }
            }

            if (((Command) o).date == null) {
                return 1;
            }

            return date.compareTo(((Command) o).date);
        }

        private boolean dateDifferente(JADate date) {
            if ((this.date == null) || (date == null)) {
                return !((this.date == null) && (date == null));
            }

            calendar.setTime(this.date);

            return (date.getDay() != calendar.get(Calendar.DAY_OF_MONTH))
                    || (date.getMonth() != (calendar.get(Calendar.MONTH) + 1))
                    || (date.getYear() != calendar.get(Calendar.YEAR));
        }

        /*
         * retourne vrai s'il faut fermer la base courante, ceci évite que des bases soit créées inutilement pour les
         * cas où plusieurs commandes tombent le même jour.
         */
        private boolean doitCloreBaseCourante() {
            if (debut) {
                return dateDifferente(baseCourante.getDateDebut());
            } else {
                return (derniereBaseCourante != null) && dateDifferente(derniereBaseCourante.getDateFin());
            }
        }

        /**
         * Exécute la logique associée à cette commande.
         *
         * <p>
         * Cette méthode crée ou ferme la base courante si nécessaire.
         * </p>
         *
         * @throws Exception
         *             DOCUMENT ME!
         */
        public void execute() throws Exception {
            if (baseCourante == null) {
                nouvelleBaseCourante(0);
            }

            if (doitCloreBaseCourante()) {
                cloreBaseCourante();

                if (debut) {
                    nouvelleBaseCourante(0);
                } else {
                    nouvelleBaseCourante(1);
                }
            }

            faireChangement();
        }

        /**
         * Cette méthode doit être implémentée par les sous-classes de commandes pour effectuer la logique spécifique au
         * type de commande.
         *
         * @throws Exception
         *             DOCUMENT ME!
         */
        protected abstract void faireChangement() throws Exception;

        /*
         * crée une nouvelle base courante en clonant si nécessaire la base précédente ou en en créant une nouvelle. Les
         * informations de base concernant la base de calcul sont renseignées.
         */
        private void nouvelleBaseCourante(int delaiJours) throws Exception {
            if (baseCourante == null) {
                baseCourante = new APBaseCalcul();

                if (droit instanceof APDroitAPG) {
                    baseCourante.setNombreJoursSoldes(Integer.parseInt(((APDroitAPG) droit).getNbrJourSoldes()));
                    baseCourante.setNoRevision(((APDroitAPG) droit).getNoRevision());
                    baseCourante.setNombreEnfants(Integer.parseInt(((APDroitAPG) droit).loadSituationFamilliale()
                            .getNbrEnfantsDebutDroit()));
                } else if(droit instanceof APDroitPandemie) {
                    baseCourante.setNoRevision(((APDroitPandemie) droit).getNoRevision());
                }

                baseCourante.setTypeAllocation(droit.getGenreService());
            } else {
                baseCourante = (APBaseCalcul) baseCourante.clone();
            }

            if (date != null) {
                baseCourante.setDateDebut(toJADate(delaiJours));
            }
        }

        // transforme la date de cette commande en une JADate en ajoutant le
        // nombre de jours indiqué
        private JADate toJADate(int delaiJours) {
            if (date == null) {
                return null;
            }

            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, delaiJours);

            return new JADate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.YEAR));
        }
    }

    /*
     * Cette commande encapsule un changement de montant versé par un employeur lors d'une période de prestation par
     * rapport au salaire habituel d'une personne.
     *
     * Lorsqu'elle est exécutée, elle changement le champ montantVerse de la base de calcul.
     *
     * @author vre
     */
    protected class EmployeurChangeMontantCommand extends EmployeurCommand {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        APMontantVerse montantVerse;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe EmployeurChangeMontantCommand.
         *
         * @param date
         * @param debut
         * @param sitPro
         *            DOCUMENT ME!
         *
         * @throws ParseException
         */
        public EmployeurChangeMontantCommand(String date, boolean debut, APSituationProfessionnelle sitPro)
                throws ParseException {
            super(date, debut, 0, sitPro);
            montantVerse = new APSalaireAdapter(sitPro).montantVerse();
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see globaz.apg.module.calcul.APBasesCalculBuilder.Command#faireChangement()
         */
        @Override
        protected void faireChangement() throws Exception {
            for (Iterator bcSitPros = baseCourante.getBasesCalculSituationProfessionnel().iterator(); bcSitPros
                    .hasNext();) {
                APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) bcSitPros.next();

                if (bcSitPro.isPourSituationProfessionnelle(sitPro)) {
                    if (debut) {
                        APBaseCalculSalaireJournalier bcj = new APBaseCalculSalaireJournalier();

                        if (montantVerse != null) {
                            montantVerse.updateBaseCalculSalaire(bcj, sitPro.getHeuresSemaine());
                        }
                    } else {
                        bcSitPro.setVersementEmployeur(null);
                    }

                    break;
                }
            }
        }
    }

    /*
     * Cette commande encapsule un changement d'employeur.
     *
     * Lorsqu'elle est exécutée, elle ajoute ou enlève un employeur à la liste des employeurs de la base de calcul. Dans
     * le cas ou elle enleve, la prestation auparavant versée à l'employeur est transférée à l'assuré.
     *
     * @author vre
     */
    protected class EmployeurCommand extends Command {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        protected int index;
        protected APSituationProfessionnelle sitPro;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe EmployeurCommand.
         *
         * @param date
         *            DOCUMENT ME!
         * @param debut
         *            DOCUMENT ME!
         * @param index
         *            DOCUMENT ME!
         * @param sitPro
         *            DOCUMENT ME!
         *
         * @throws ParseException
         */
        public EmployeurCommand(String date, boolean debut, int index, APSituationProfessionnelle sitPro)
                throws ParseException {
            super(date, debut);
            this.index = index;
            this.sitPro = sitPro;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see globaz.apg.module.calcul.APBasesCalculBuilder.Command#faireChangement()
         */
        @Override
        protected void faireChangement() throws Exception {
            if (debut) {
                // ajouter une situation professionnelle
                if (sitPro.getIsIndependant().booleanValue()) {
                    baseCourante.setIndependant(true);
                } else {
                    baseCourante.setSalarie(true);
                }

                if(APGUtils.isTypeAllocationPandemie(droit.getGenreService())
                        || droit instanceof APDroitPaternite
                        || droit instanceof APDroitProcheAidant) {
                    baseCourante.setAllocationExploitation(false);
                } else {
                    baseCourante.setAllocationExploitation(sitPro.getIsAllocationExploitation().booleanValue());
                }
                baseCourante.setAllocationMaximum(sitPro.getIsAllocationMax().booleanValue());

                // le nombre de contrats avec cet employeur
                int nbContrats = ((Integer) nbContratsList.get(sitPro.loadEmployeur().getCleUnique())).intValue();

                baseCourante.addBaseCalculSituationProfessionnel(new APBaseCalculSituationProfessionnel(sitPro,
                        nbContrats, index));
            } else {
                /*
                 * dans le cas d'une fin de contrat: - on verse les prestations au tiers - on vérifie si l'assuré est
                 * toujours salarié - on vérifie si l'assuré est toujours indépendant
                 */
                boolean independant = false;
                boolean salarie = false;

                for (Iterator bcSitPros = baseCourante.getBasesCalculSituationProfessionnel().iterator(); bcSitPros
                        .hasNext();) {
                    APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) bcSitPros.next();

                    if (bcSitPro.isPourSituationProfessionnelle(sitPro)) {
                        bcSitPros.remove();
                    } else {
                        independant = independant || bcSitPro.isIndependant();
                        salarie = salarie || !bcSitPro.isIndependant();
                    }
                }

                baseCourante.setIndependant(independant);
                baseCourante.setSalarie(salarie);
            }
        }
    }

    /*
     * Cette commande encapsule un changement du nombre d'enfants (spécifique APG) dans le calcul.
     *
     * Lorsqu'elle est exécutée, elle ajoute ou enlève un enfant à la base de calcul.
     *
     * @author vre
     */
    protected class EnfantAPGCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe EnfantAPGCommand.
         *
         * @param date
         * @param debut
         *
         * @throws ParseException
         */
        public EnfantAPGCommand(String date, boolean debut) throws ParseException {
            super(date, debut);
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see globaz.apg.module.calcul.APBasesCalculBuilder.Command#faireChangement()
         */
        @Override
        protected void faireChangement() throws Exception {
            if (debut) {
                baseCourante.setNombreEnfants(baseCourante.getNombreEnfants() + 1);
            } else {
                baseCourante.setNombreEnfants(baseCourante.getNombreEnfants() - 1);
            }
        }
    }

    /*
     * Cette commande encapsule un changement du nombre d'enfants (spécifique maternité) dans le calcul.
     *
     * Lorsqu'elle est exécutée, elle ajoute ou enlève un enfant à la base de calcul.
     *
     * Cette commande est la seule du côté Maternité à modifier le niveau d'activation.
     *
     * @author vre
     */
    protected class EnfantMatCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe EnfantMatCommand.
         *
         * @param date
         *            DOCUMENT ME!
         * @param debut
         *            DOCUMENT ME!
         */
        public EnfantMatCommand(Date date, boolean debut) {
            super(date, debut);
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see globaz.apg.module.calcul.APBasesCalculBuilder.Command#faireChangement()
         */
        @Override
        protected void faireChangement() throws Exception {
            baseCourante.setNombreEnfants(debut ? ++niveauActivation : --niveauActivation);
        }
    }

    /*
     * Cette commande ne fait rien de spécifique. Elle sert juste à couper les bases de calcul à une date donnée.
     *
     * @author vre
     */
    protected class NouvelleBaseCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe NouvelleBaseCommand.
         *
         * @param date
         * @param debut
         */
        public NouvelleBaseCommand(Date date, boolean debut) {
            super(date, debut);
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see globaz.apg.module.calcul.APBasesCalculBuilder.Command#faireChangement()
         */
        @Override
        protected void faireChangement() throws Exception {
            // ne fait rien
        }
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /*
     * Cette commande encapsule un début ou une fin de période de droit à une prestation APG.
     *
     * Cette commande est la seule du côté APG à modifier le niveau d'activation.
     *
     * @author vre
     */
    protected class PeriodeAPGCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe PeriodeAPGCommand.
         *
         * @param date
         * @param debut
         *
         * @throws ParseException
         */
        public PeriodeAPGCommand(String date, boolean debut) throws ParseException {
            super(date, debut);
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see globaz.apg.module.calcul.APBasesCalculBuilder.Command#faireChangement()
         */
        @Override
        protected void faireChangement() throws Exception {
            if (debut) {
                ++niveauActivation;
                // baseCourante.setTypeAllocation(typePeriode);
            } else {
                --niveauActivation;
                // baseCourante.setTypeAllocation("");
            }
        }
    }

    protected class TauxImpositionCommand extends Command {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private PRTauxImposition taux;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe TauxImpositionCommand.
         *
         * @param date
         *            DOCUMENT ME!
         * @param debut
         *            DOCUMENT ME!
         * @param taux
         *            DOCUMENT ME!
         *
         * @throws ParseException
         *             DOCUMENT ME!
         */
        public TauxImpositionCommand(String date, boolean debut, PRTauxImposition taux) throws ParseException {
            super(date, debut);
            this.taux = taux;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @throws Exception
         *             DOCUMENT ME!
         */
        @Override
        protected void faireChangement() throws Exception {
            if (debut) {
                baseCourante.setSoumisImpotSource(true);
                baseCourante.setIdTauxImposition(taux.getIdTauxImposition());
                baseCourante.setTauxImposition(taux.getTaux());
            } else {
                baseCourante.setSoumisImpotSource(false);
                baseCourante.setIdTauxImposition("");
                baseCourante.setTauxImposition("");
            }
        }
    }

    /*
     * cette commande désactive l'ajout des bases de calcul à la liste.
     *
     * @author vre
     */
    protected class VersementInterditCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe PreventAddCommand.
         *
         * @param date
         * @param debut
         */
        public VersementInterditCommand(Date date, boolean debut) {
            super(date, debut);
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * @see globaz.apg.module.calcul.APBasesCalculBuilder.Command#faireChangement()
         */
        @Override
        protected void faireChangement() throws Exception {
            versementInterdit = debut;
        }
    }

    @Value
    static class PeriodeTauxCourant {
        protected String dateDebut;
        protected String dateFin;
        protected String canton;
        protected String taux;

        public static PeriodeTauxCourant of(APPeriodeComparable periode) {
            return new PeriodeTauxCourant(periode.getDateDebutPeriode(), periode.getDateFinPeriode(), periode.getCantonImposition(), periode.getTauxImposition());
        }

        public PeriodeTauxCourant updateDateDebut(String dateDebut) {
            return new PeriodeTauxCourant(dateDebut, this.dateFin, this.canton, this.taux);
        }

        public PeriodeTauxCourant updateDateFin(String dateFin) {
            return new PeriodeTauxCourant(this.dateDebut, dateFin, this.canton, this.taux);
        }
    }

}
