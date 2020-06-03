/*
 * Cr�� le 3 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul;

import globaz.apg.ApgServiceLocator;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.*;
import globaz.apg.module.calcul.salaire.APMontantVerse;
import globaz.apg.module.calcul.salaire.APSalaireAdapter;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.utils.PRDateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Charge toutes les informations relatives � un droit et cr�e les bases de calcul pour les prestations. ATTENTION: pas
 * thread safe !!!! Cr�er une nouvelle instance de cette classe pour chaque traitement !
 * </p>
 * 
 * <p>
 * Le principe g�n�ral de cette classe est de regrouper toutes les informations n�cessaire au calcul d'une prestation
 * pour un droit donn� et pour une p�riode donn�e.
 * </p>
 * 
 * <p>
 * L'algorithme utilis� pour effectuer cette t�che est du type 'diviser pour mieux r�gner'. Le probl�me est divis� en
 * sous-probl�mes. Chaque sous-probl�me est repr�sent� par une classe interne qui impl�mente
 * APBaseCalculBuilder.Command. Cette interface repr�sente un contrat du type du pattern Command (GOF). C'est-�-dire
 * qu'elles doivent encapsuler toutes les donn�es et la logique n�cessaire � la r�solution du sous-probl�me. Il y a une
 * classe pour chaque type de sous-probl�me, par exemple la fin de l'ann�e, un changement de situation professionnelle
 * (par exemple la fin d'un contrat de travail ou le changement du montant vers� par un employeur durant une p�riode de
 * prestation) ou un changement de situation familiale (par exemple la naissance d'un enfant) ou, dans le cas des APG,
 * les d�buts et fin d'une p�riode de prestation, etc. Le sch�ma domaine des classes est:
 * </p>
 * 
 * <p>
 * <img src="doc-files/general.gif" alt="sch�ma domaine">
 * </p>
 * 
 * <p>
 * L'algorithme s'effectue en trois �tapes:
 * </p>
 * 
 * <h4>1. Cr�ation des commandes</h4>
 * 
 * <p>
 * Une instance de commande sp�cifique � chaque sous-probl�me est cr��e.
 * </p>
 * 
 * <p>
 * <img src="doc-files/etape_1.gif" alt="sch�ma explicatif de l'�tape 1">
 * </p>
 * 
 * <h4>2. tri des commandes</h4>
 * 
 * <p>
 * les commandes sont ensuite tri�es par date d'ex�cution (les commandes sont java.lang.Comparable).
 * </p>
 * 
 * <p>
 * <img src="doc-files/etape_2.gif" alt="sch�ma explicatif de l'�tape 2">
 * </p>
 * 
 * <h4>3. ex�cution des commandes</h4>
 * 
 * <p>
 * enfin les commandes sont ex�cut�es dans l'ordre pour cr�er les bases de calcul.
 * </p>
 * 
 * <p>
 * <img src="doc-files/etape_3.gif" alt="diagramme UML de s�quence des �tapes 2 et 3">
 * </p>
 * 
 * <h4>sch�ma UML (quasi exhaustif)</h4>
 * 
 * <p>
 * <img src="doc-files/base_calcul_cd.gif" alt="diagramme de classe quasi complet">
 * </p>
 * 
 * @author vre
 */
public class APBasesCalculBuilder {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /*
     * Une commande encapsule toutes les donn�es et toute la logique n�cessaire au traitement d'un �v�nement.
     * 
     * <p>Le type de l'�v�nement est d�fini comme une sous-classe de la classe Commande.</p>
     * 
     * <p>Par d�faut, les informations indispensables � une commande sont une date d'ex�cution et un bool�en indiquant
     * si la commande est le d�but ou la fin d'une p�riode de modification de prestation. Par exemple, la naissance d'un
     * enfant est un d�but, la fin d'une p�riode APG est une fin.</p>
     * 
     * <p>L'ex�cution d'une commande affecte potentiellement les variables niveauActivation, baseCourante,
     * derniereBaseCourante et la liste des bases de la classe APBasesCalculBuilder.</p>
     * 
     * <p>La variable la plus importante pour le sous-classement de Command ou l'ex�cution d'une commande est le niveau
     * d'activation. Cette variable permet de contenir le nombre de p�riodes donnant droit � une prestation � la date
     * courante. Par exemple, pour les droits maternit�, cette variable sera incr�ment�e pour chaque nouveau-n� et
     * d�cr�ment�e pour chaque fin de droit d'allocation. En fonction de cette variable, bases de calcul seront retenues
     * ou non.</p>
     * 
     * @author vre
     */
    private abstract class Command implements Comparable {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        /** La date d'ex�cution de la commande. */
        protected Date date = null;

        /**
         * Un bool�en � vrai si la commande concerne le d�but d'une modification de traitement.
         */
        protected boolean debut;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe Command.
         * 
         * @param date
         *            DOCUMENT ME!
         * @param debut
         *            DOCUMENT ME!
         */
        public Command(Date date, boolean debut) {
            this.date = date;
            this.debut = debut;
        }

        /**
         * Cr�e une nouvelle instance de la classe Command.
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
        public Command(String date, boolean debut) throws ParseException {
            if (!JadeStringUtil.isEmpty(date)) {
                this.date = DF.parse(date);
            }

            this.debut = debut;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /** Fermer la base courante et la transformer en derni�re base courante. */
        public void cloreBaseCourante() {
            if (debut) {
                // on la ferme pour le jour pr�cedent
                baseCourante.setDateFin(toJADate(-1));
            } else {
                // on la ferme pour le jour meme
                baseCourante.setDateFin(toJADate(0));
            }

            /*
             * on ajoute la base de calcul uniquement si la p�riode pour la base est importante pour le calcul.
             * C'est-�-dire si le niveau d'activation est plus grand que 0.
             */
            if ((niveauActivation > 0) && !versementInterdit) {
                int nbJours;

                // calculer le nombre de jours soldes
                // les ann�es sont forc�ment �gales (les p�riodes sont splitt�es
                // par ann�es)
                // les mois sont peut-�tre diff�rents (paiements r�troactifs)
                if (baseCourante.getDateDebut().getMonth() == baseCourante.getDateFin().getMonth()) {
                    // m�me mois, m�me ann�e, on soustrait simplement les jours
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
                    // TODO: actuellement, pour un droit APG, on r�partit les
                    // jours sold�s sur les premi�res p�riodes
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

        /**
         * Compare les dates d'ex�cution de deux commandes et retourne leur ordre naturel.
         * 
         * <p>
         * Les null sont consid�r�s comme plus petits que n'importe quelle date.
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
         * retourne vrai s'il faut fermer la base courante, ceci �vite que des bases soit cr��es inutilement pour les
         * cas o� plusieurs commandes tombent le m�me jour.
         */
        private boolean doitCloreBaseCourante() {
            if (debut) {
                return dateDifferente(baseCourante.getDateDebut());
            } else {
                return (derniereBaseCourante != null) && dateDifferente(derniereBaseCourante.getDateFin());
            }
        }

        /**
         * Ex�cute la logique associ�e � cette commande.
         * 
         * <p>
         * Cette m�thode cr�e ou ferme la base courante si n�cessaire.
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
         * Cette m�thode doit �tre impl�ment�e par les sous-classes de commandes pour effectuer la logique sp�cifique au
         * type de commande.
         * 
         * @throws Exception
         *             DOCUMENT ME!
         */
        protected abstract void faireChangement() throws Exception;

        /*
         * cr�e une nouvelle base courante en clonant si n�cessaire la base pr�c�dente ou en en cr�ant une nouvelle. Les
         * informations de base concernant la base de calcul sont renseign�es.
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
        // nombre de jours indiqu�
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
     * Cette commande encapsule un changement de montant vers� par un employeur lors d'une p�riode de prestation par
     * rapport au salaire habituel d'une personne.
     * 
     * Lorsqu'elle est ex�cut�e, elle changement le champ montantVerse de la base de calcul.
     * 
     * @author vre
     */
    private class EmployeurChangeMontantCommand extends EmployeurCommand {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        APMontantVerse montantVerse;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe EmployeurChangeMontantCommand.
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
     * Lorsqu'elle est ex�cut�e, elle ajoute ou enl�ve un employeur � la liste des employeurs de la base de calcul. Dans
     * le cas ou elle enleve, la prestation auparavant vers�e � l'employeur est transf�r�e � l'assur�.
     * 
     * @author vre
     */
    private class EmployeurCommand extends Command {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        protected int index;
        protected APSituationProfessionnelle sitPro;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe EmployeurCommand.
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

                if(IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE.equals(droit.getGenreService())) {
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
                 * dans le cas d'une fin de contrat: - on verse les prestations au tiers - on v�rifie si l'assur� est
                 * toujours salari� - on v�rifie si l'assur� est toujours ind�pendant
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
     * Cette commande encapsule un changement du nombre d'enfants (sp�cifique APG) dans le calcul.
     * 
     * Lorsqu'elle est ex�cut�e, elle ajoute ou enl�ve un enfant � la base de calcul.
     * 
     * @author vre
     */
    private class EnfantAPGCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe EnfantAPGCommand.
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
     * Cette commande encapsule un changement du nombre d'enfants (sp�cifique maternit�) dans le calcul.
     * 
     * Lorsqu'elle est ex�cut�e, elle ajoute ou enl�ve un enfant � la base de calcul.
     * 
     * Cette commande est la seule du c�t� Maternit� � modifier le niveau d'activation.
     * 
     * @author vre
     */
    private class EnfantMatCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe EnfantMatCommand.
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
     * Cette commande ne fait rien de sp�cifique. Elle sert juste � couper les bases de calcul � une date donn�e.
     * 
     * @author vre
     */
    private class NouvelleBaseCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe NouvelleBaseCommand.
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
     * Cette commande encapsule un d�but ou une fin de p�riode de droit � une prestation APG.
     * 
     * Cette commande est la seule du c�t� APG � modifier le niveau d'activation.
     * 
     * @author vre
     */
    private class PeriodeAPGCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe PeriodeAPGCommand.
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

    private class TauxImpositionCommand extends Command {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private PRTauxImposition taux;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe TauxImpositionCommand.
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
     * cette commande d�sactive l'ajout des bases de calcul � la liste.
     * 
     * @author vre
     */
    private class VersementInterditCommand extends Command {

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe PreventAddCommand.
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

    /*
     * un simple formatteur de date JMA, il est n�cessaire car le traitement des dates dans cette classe se fait au
     * moyen d'instances de java.util.Date et de java.util.Calendar
     */
    private static final SimpleDateFormat DF = new SimpleDateFormat("dd.MM.yyyy");
    private static final Date PREMIER_JUILLET_2001;
    private static final Date PREMIER_JUILLET_2005;
    private static final Date TRENTE_JUIN_2001;

    private static final Date TRENTE_JUIN_2005;

    static {
        Calendar cal = getCalendarInstance();

        cal.set(2005, Calendar.JUNE, 30);
        TRENTE_JUIN_2005 = cal.getTime();
        cal.set(Calendar.YEAR, 2001);
        TRENTE_JUIN_2001 = cal.getTime();
        cal.set(Calendar.YEAR, 2005);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        PREMIER_JUILLET_2005 = cal.getTime();
        cal.set(Calendar.YEAR, 2001);
        PREMIER_JUILLET_2001 = cal.getTime();
    }

    // cr�e une instance de Calendar en effacant les champs inutiles
    private static Calendar getCalendarInstance() {
        Calendar retValue = Calendar.getInstance();

        retValue.set(Calendar.HOUR_OF_DAY, 12);
        retValue.set(Calendar.MINUTE, 0);
        retValue.set(Calendar.SECOND, 0);
        retValue.set(Calendar.MILLISECOND, 0);

        return retValue;
    }

    APBaseCalcul baseCourante = null;
    private LinkedList bases = new LinkedList();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    Calendar calendar = getCalendarInstance();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private LinkedList commands = new LinkedList();

    private APBaseCalcul derniereBaseCourante = new APBaseCalcul();

    APDroitLAPG droit;

    private PRTauxImpositionManager mgrTauxImpot;

    private HashMap nbContratsList = new HashMap();

    private int nbJoursSoldes;

    // le niveau d'activation est plus grand que 0 si l'on est dans une p�riode
    // de prestation
    int niveauActivation = 0;

    private BSession session;

    private boolean versementInterdit = false;

    /**
     * Cr�e une nouvelle instance de la classe APBasesCalculBuilder.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param droit
     *            DOCUMENT ME!
     */
    public APBasesCalculBuilder(BSession session, APDroitLAPG droit) {
        this.droit = droit;
        this.session = session;
    }

    // ajouter les �v�nements relatifs aux p�riodes de prestation APG � la liste
    // des commandes et splitter par ann�e.
    private void ajouterPeriodesAPG() throws Exception {
        // charger toutes les p�riodes pour ce droit.
        APPeriodeAPGManager mgr = new APPeriodeAPGManager();
        Calendar bCal = getCalendarInstance();

        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction());

        // pour chaque p�riode
        for (int idPeriode = 0; idPeriode < mgr.size(); ++idPeriode) {
            APPeriodeAPG periode = (APPeriodeAPG) mgr.get(idPeriode);
            PeriodeAPGCommand bCommand = new PeriodeAPGCommand(periode.getDateDebutPeriode(), true);
            PeriodeAPGCommand eCommand = new PeriodeAPGCommand(periode.getDateFinPeriode(), false);

            // ajouter l'�v�nement "d�but de la p�riode"
            commands.add(bCommand);

            // ajouter l'�v�nement "fin de la p�riode"
            commands.add(eCommand);

            // couper par ann�e si n�cessaire
            calendar.setTime(bCommand.date);
            bCal.setTime(eCommand.date);

            for (int year = calendar.get(Calendar.YEAR); year < bCal.get(Calendar.YEAR); ++year) {
                calendar.set(year, Calendar.DECEMBER, 31);
                commands.add(new NouvelleBaseCommand(calendar.getTime(), false));
            }
        }
    }

    private Integer ajouterPeriodesPan(String dateDebut, int autreJours, boolean isIndependant) throws Exception {

        List<APPeriodeComparable> listPeriode = getApPeriodeDroit(droit.getIdDroit());

        int delai = autreJours == 0 ? getDelaiSelonService() : 0;

        if(autreJours == 0 && delai > 0) {
            // v�rifie si les jours de carrence n'ont pas �t� atteind dans d'autres droits
            delai = calcDelaiForDroits(delai, dateDebut);
        }

        calcDateDebutFin(droit.getIdDroit(), delai, dateDebut, listPeriode);

        // ajout d'un d�lai selon les services
        if(autreJours == 0){
            delaiSelonService(listPeriode, delai);
        }

        int nbJourSoldes = 0;
        // pour chaque p�riode
        for (APPeriodeComparable periode : listPeriode) {

            int nbJourSoldesAvantAjout = nbJourSoldes;
            if(!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                nbJourSoldes += Integer.valueOf(periode.getNbrJours());
            } else {
                nbJourSoldes += PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
            }

            nbJourSoldes = joursMaxSelonService(autreJours, nbJourSoldes, periode, isIndependant, dateDebut);

            PeriodeAPGCommand bCommand = new PeriodeAPGCommand(periode.getDateDebutPeriode(), true);
            PeriodeAPGCommand eCommand = new PeriodeAPGCommand(periode.getDateFinPeriode(), false);

            // cota d�pass�
            if(nbJourSoldesAvantAjout >= nbJourSoldes){
                break;
            }

            // ajouter l'�v�nement "d�but de la p�riode"
            commands.add(bCommand);

            // ajouter l'�v�nement "fin de la p�riode"
            commands.add(eCommand);

            if(!IAPDroitLAPG.CS_QUARANTAINE.equals(droit.getGenreService())) {
                // couper par mois
                Date debut = DF.parse(periode.getDateDebutPeriode());
                Calendar fin = getCalendarInstance();

                fin.setTime(DF.parse(periode.getDateFinPeriode()));
                couperParMois(debut, fin);
            }
        }

        return nbJourSoldes;
    }

    private int calcDelaiForDroits(int delai, String dateDebut) throws Exception {
        List<String> listDroit = ApgServiceLocator.getEntityService().getAutresDroits(session, droit.getIdDroit());
        for(String idDroit:listDroit){
            List<APPeriodeComparable> listPeriode = getApPeriodeDroit(idDroit);
            calcDateDebutFin(idDroit, delai, dateDebut, listPeriode);
            delai = delaiSelonService(listPeriode, delai);
        }
        return delai;
    }

    private void calcDateDebutFin(String idDroit, int delai, String dateDebut, List<APPeriodeComparable> listPeriode) throws Exception {

        // pour chaque p�riode
        for (APPeriodeComparable periode : listPeriode) {
            boolean dateDebutModif = false;
            if(!JadeStringUtil.isEmpty(dateDebut) && JadeDateUtil.isDateBefore(periode.getDateDebutPeriode(), dateDebut)) {
                periode.setDateDebutPeriode(dateDebut);
                dateDebutModif = true;
            }
            // si pas de date fin mettre la fin du mois en cours pour le calcul
            if (JadeStringUtil.isEmpty(periode.getDateFinPeriode())) {
                if(IAPDroitLAPG.CS_QUARANTAINE.equals(droit.getGenreService())) {
                    resolveFinJourMaxParam(periode, APParameter.QUARANTAINE_JOURS_MAX.getParameterName(), delai);
                } else if (IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS.equals(droit.getGenreService())){
                    resolveFinJourMaxParam(periode, APParameter.INDEPENDANT_PERTE_DE_GAIN_MAX.getParameterName(), delai);
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                    periode.setDateFinPeriode(JadeDateUtil.getGlobazFormattedDate(cal.getTime()));
                }
            }

            if(dateDebutModif && !JadeStringUtil.isBlankOrZero(periode.getNbrJours())){
                int nbJours = PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1;
                if(nbJours < Integer.valueOf(periode.getNbrJours())) {
                    periode.setNbrJours(Integer.toString(nbJours));
                }
            }
        }
    }

    private List<APPeriodeComparable> getApPeriodeDroit(String idDroit) throws Exception {
        APPeriodeAPGManager mgr = new APPeriodeAPGManager();

        mgr.setSession(session);
        mgr.setForIdDroit(idDroit);
        mgr.find(session.getCurrentThreadTransaction());

        List<APPeriodeComparable> listPeriode;

        listPeriode = mgr.toList().stream().map(obj -> (APPeriodeAPG) obj).map(ap -> new APPeriodeComparable(ap)).collect(Collectors.toList());
        Collections.sort(listPeriode);
        return listPeriode;
    }

    private int getDelaiSelonService() throws Exception {
        Integer delai = 0;
        String valeur = "";
        switch(droit.getGenreService()){
            case IAPDroitLAPG.CS_GARDE_PARENTALE:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP:
                valeur = APParameter.GARDE_PARENTAL_JOURS_SANS_IDEMNISATION.getParameterName();break;
            case IAPDroitLAPG.CS_QUARANTAINE:
                valeur = APParameter.QUARANTAINE_JOURS_SANS_INDEMISATION.getParameterName();break;
            case IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE:
            case IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS:
            case IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE:
                valeur = APParameter.INDEPENDANT_JOURS_SANS_INDEMISATION.getParameterName();break;
            default:
                valeur = "";
        }

        if(!valeur.isEmpty()) {
            delai = Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", valeur, "0", "", 0));
        }
        return delai;
    }

    private void resolveFinJourMaxParam(APPeriodeComparable periode, String param, int delai) throws Exception {
        int jourMax = Integer.parseInt(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", param, "0", "", 0));
        String dateFin = JadeDateUtil.addDays(periode.getDateDebutPeriode(), delai + jourMax - 1);
        periode.setDateFinPeriode(dateFin);
    }

    /**
     * Ajout d'un d�lai selon le genre de service
     * @param listPeriode
     */
    private int delaiSelonService(List<APPeriodeComparable> listPeriode, int delai) throws Exception {
        if(!listPeriode.isEmpty()) {

            //d�cale la date de d�but ou supprime la p�riode si pas assez de jours
            for(APPeriodeComparable periode: new ArrayList<>(listPeriode)) {
                int nbjours = JadeStringUtil.isBlankOrZero(periode.getNbrJours()) ? PRDateUtils.getNbDayBetween(periode.getDateDebutPeriode(), periode.getDateFinPeriode()) + 1 : Integer.valueOf(periode.getNbrJours());
                if (delai <= 0) {
                    break;
                }
                if (nbjours > delai) {
                    periode.setDateDebutPeriode(JadeDateUtil.addDays(periode.getDateDebutPeriode(), delai));
                    if(!JadeStringUtil.isBlankOrZero(periode.getNbrJours())) {
                        periode.setNbrJours(Integer.toString(Integer.valueOf(periode.getNbrJours()) - delai));
                    }
                    break;
                } else {
                    listPeriode.remove(periode);
                    delai -= nbjours;
                }
            }
        }
        return delai;
    }

    private int joursMaxSelonService(int autreJours, int nbJours, APPeriodeComparable periode, boolean independant, String dateDebut) throws Exception {
        Integer jourMaximum = null;
        Integer nbJoursNew = nbJours;
        switch(droit.getGenreService()){
            case IAPDroitLAPG.CS_GARDE_PARENTALE:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP:
                if(independant) {
                    jourMaximum = getJourMax(APParameter.GARDE_PARENTAL_INDE_JOURS_MAX.getParameterName());
                };
                break;
            case IAPDroitLAPG.CS_QUARANTAINE:
                autreJours = 0;
                jourMaximum = getJourMax(APParameter.QUARANTAINE_JOURS_MAX.getParameterName());break;
            case IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS:
                jourMaximum = getJourMax(APParameter.INDEPENDANT_PERTE_DE_GAIN_MAX.getParameterName());break;
            default:
                jourMaximum = null;
        }

        if(jourMaximum != null){
            if(nbJours + autreJours > jourMaximum ) {
                nbJoursNew = jourMaximum - autreJours;
                periode.setDateFinPeriode(JadeDateUtil.addDays(periode.getDateFinPeriode(), nbJoursNew - nbJours));
            }
            checkFinPeriode(periode, dateDebut, jourMaximum);
        }

        return nbJoursNew;
    }

    private void checkFinPeriode(APPeriodeComparable periode, String dateDebut, int jourMax){
        // Doit �tre une date fin fixe donc adapter par rapport � la date de d�but
        if(droit.getGenreService().equals(IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS)) {
            String dateFinMax = JadeDateUtil.addDays(dateDebut, jourMax - 1);
            if (JadeDateUtil.isDateAfter(periode.getDateFinPeriode(), dateFinMax)) {
                periode.setDateFinPeriode(dateFinMax);
            }
        }
    }

    private Integer getJourMax(String param) throws Exception {
        if(!param.isEmpty()) {
            return Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", param, "0", "", 0));
        }
        return null;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    // ajoute le premier juillet 2001 comme date de d�but des versements des
    // prestations.
    private void ajouterPremierJuillet2001() throws ParseException {
        Date debut = DF.parse(droit.getDateDebutDroit());

        if (debut.before(PREMIER_JUILLET_2001)) {
            commands.add(new VersementInterditCommand(null, true));
            commands.add(new VersementInterditCommand(TRENTE_JUIN_2001, false));
        }
        commands.add(new VersementInterditCommand(TRENTE_JUIN_2005, false));
    }

    // ajoute le premier juillet 2005 comme date de d�but des versements des
    // prestations.
    private void ajouterPremierJuillet2005() throws ParseException {
        Date debut = DF.parse(droit.getDateDebutDroit());

        if (debut.before(PREMIER_JUILLET_2005)) {
            commands.add(new VersementInterditCommand(null, true));
            commands.add(new VersementInterditCommand(TRENTE_JUIN_2005, false));
        }
    }

    // ajoute la date de d�but des versements selon le genre de service
    private String ajouterDateDebutFromParam() throws Exception {
        APDroitPanSituation droitSituation = ApgServiceLocator.getEntityService().getDroitPanSituation(session, session.getCurrentThreadTransaction(),
                droit.getIdDroit());
        String valeur = "";
        String dateDebut = "";
        switch(droit.getGenreService()){
            case IAPDroitLAPG.CS_GARDE_PARENTALE:
            case IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP:
                valeur = APParameter.GARDE_PARENTAL_JOURS_SANS_IDEMNISATION.getParameterName();break;
            case IAPDroitLAPG.CS_QUARANTAINE:
                valeur = APParameter.QUARANTAINE_JOURS_SANS_INDEMISATION.getParameterName();break;
            case IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE:
                if(!JadeStringUtil.isEmpty(droitSituation.getDateDebutManifestationAnnulee())
                    && (JadeStringUtil.isEmpty(droitSituation.getDateFermetureEtablissementDebut())
                    || DF.parse(droitSituation.getDateDebutManifestationAnnulee()).before(DF.parse(droitSituation.getDateFermetureEtablissementDebut())))){
                    valeur = APParameter.MANIFESTATION_INDERDITE_DATE_MINI.getParameterName();
                } else {
                    valeur = APParameter.FERMETURE_EMTREPRISE_DATE_MINI.getParameterName();
                }
                break;
            case IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS:
                valeur = APParameter.INDEPENDANT_PERTE_DE_GAIN_MAX.getParameterName();break;
            case IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE:
                valeur = APParameter.FERMETURE_EMTREPRISE_DATE_MINI.getParameterName();break;

            default:return dateDebut;
        }

        FWFindParameterManager manager = new FWFindParameterManager();
        manager.setSession(session);
        manager.setIdCodeSysteme("1");
        manager.setIdCleDiffere(valeur);
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0){
            FWFindParameter param = (FWFindParameter) manager.get(0);
            Date dateDebutValide = DF.parse(new ch.globaz.common.domaine.Date(param.getDateDebutValidite()).getSwissValue());
            Date debut = DF.parse(droit.getDateDebutDroit());
            if (debut.before(dateDebutValide)) {
                Calendar c = Calendar.getInstance();
                c.setTime(dateDebutValide);
                c.add(Calendar.DATE, -1);
                commands.add(new VersementInterditCommand(null, true));
                commands.add(new VersementInterditCommand(c.getTime(), false));
            }
            dateDebut = JadeDateUtil.getGlobazFormattedDate(dateDebutValide);
        }
        return dateDebut;
    }

    // ajouter les �v�nements relatifs � la situation familiale APG � la liste
    // des commandes.
    private void ajouterSituationFamilialeAPG() throws Exception {
        // charger tous les enfants pour la situation familiale de ce droit.
        APEnfantAPGManager mgr = new APEnfantAPGManager();

        mgr.setSession(session);
        mgr.setForIdSituationFamiliale(((APDroitAPG) droit).getIdSituationFam());
        mgr.find(session.getCurrentThreadTransaction());

        // pour chaque enfant
        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            APEnfantAPG enfant = (APEnfantAPG) mgr.get(idEnfant);

            // ajouter l'�v�nement "naissance de l'enfant"
            commands.add(new EnfantAPGCommand(enfant.getDateDebutDroit(), true));

            // si une fin de droit est d�finie pour cet enfant, ajouter
            // l'�v�nement "fin de droit pour l'enfant"
            if (!JadeStringUtil.isEmpty(enfant.getDateFinDroit())) {
                commands.add(new EnfantAPGCommand(enfant.getDateFinDroit(), false));
            }
        }
    }

    // ajouter les �v�nements relatifs � la situation familiale maternit� � la
    // liste des commandes.
    private void ajouterSituationFamilialeMat() throws Exception {
        // obtenir les date des d�buts et de fin du droit
        Date debut = DF.parse(droit.getDateDebutDroit());
        Calendar fin = getCalendarInstance();

        fin.setTime(DF.parse(droit.getDateFinDroit()));

        // creer les commandes de d�but de droit et de find de droit �
        // l'allocation maternit�
        EnfantMatCommand commandDebut = new EnfantMatCommand(debut, true);
        EnfantMatCommand commandFin = new EnfantMatCommand(fin.getTime(), false);

        // ajouter les enfants aux commandes
        APEnfantMatManager mgr = new APEnfantMatManager();

        mgr.setSession(session);
        mgr.setForIdDroitMaternite(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction());

        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            commands.add(commandDebut);
            commands.add(commandFin);
        }

        // couper par mois
        couperParMois(debut, fin);
    }

    /**
         * couper par mois:
         * 
     * allocations par mois, on ajoute donc des com
     *mandes qui vont d�couper la p�riode de
         * prestation en unit�s mensuelles.
         * 
         * Pour les versements r�tro-actifs (c'est-�-dire avant la date du jour), la coupure n'est pas effectu�e.
     *
     * @param debut
     * @param fin
         */
    private void couperParMois(Date debut, Calendar fin) {
        Calendar moisDernier = getCalendarInstance(); // instanci� � la date du
        // jour

        moisDernier.set(Calendar.DAY_OF_MONTH, 5); // on prend une date neutre
        moisDernier.add(Calendar.MONTH, -1); // du mois pr�c�dent

        // on commence l'it�ration au d�but du droit et on se positionne
        // toujours sur le dernier jour dudit mois
        calendar.setTime(debut);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        while (calendar.before(fin)) {
            // si on est avant la fin de la prestation
            if (calendar.after(moisDernier) || (calendar.get(Calendar.MONTH) == Calendar.DECEMBER)) {
                // si ce n'est pas un paiement r�troactif ou si on est en fin
                // d'ann�e, ajouter la commande
                commands.add(new NouvelleBaseCommand(calendar.getTime(), false));
            }

            // passer au mois suivant et prendre le dernier jour de ce mois.
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
    }

    // ajouter les �v�nements relatifs � la situation familiale Pand�mie � la liste
    // des commandes.
    private void ajouterSituationFamilialePan() throws Exception {
        // obtenir les date des d�buts et de fin du droit
        Date debut = DF.parse(droit.getDateDebutDroit());
        Calendar fin = getCalendarInstance();

        fin.setTime(DF.parse(droit.getDateFinDroit()));

        // creer les commandes de d�but de droit et de find de droit �
        // TODO : un EnfantPanCommand
        EnfantMatCommand commandDebut = new EnfantMatCommand(debut, true);
        EnfantMatCommand commandFin = new EnfantMatCommand(fin.getTime(), false);

        // charger toutes les personnes pour la situation familiale de ce droit.
        APSituationFamilialePanManager mgr = new APSituationFamilialePanManager();

        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction());

        // pour chaque personne
        for (int idPersonne = 0; idPersonne < mgr.size(); ++idPersonne) {
            commands.add(commandDebut);
            commands.add(commandFin);
        }
    }

    // ajouter les �v�nements relatifs � la situation professionnelle � la liste
    // des commandes
    private boolean ajouterSituationProfessionnelle() throws Exception {
        boolean isIndependant = false;
        // charger tous les changements de situation professionnelle
        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();

        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction());

        // pour chaque situation professionnelle
        for (int idSitPro = 0; idSitPro < mgr.size(); ++idSitPro) {
            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) mgr.get(idSitPro);

            // mettre a jour le compte du nombre de contrats par employeur
            String cleEmployeur = sitPro.loadEmployeur().getCleUnique();
            Integer nbContrats = (Integer) nbContratsList.get(cleEmployeur);
            if(sitPro.getIsIndependant()) {
                isIndependant = true;
            };
            if (nbContrats != null) {
                nbContrats = new Integer(nbContrats.intValue() + 1);
            } else {
                nbContrats = new Integer(1);
            }

            nbContratsList.put(cleEmployeur, nbContrats);

            /*
             * ajouter la commande.
             * 
             * Il n'y a pas de date de d�but de contrat pour les situations professionnelles. Par d�finition, tous les
             * employeurs saisis sont ceux qui sont n�cessaires au calcul de la prestation.
             * 
             * On saisit donc une date d'ex�cution de la commande null. De cette mani�re, ces commandes seront ex�cut�es
             * en premier (la proc�dure de tri des commandes le garantit).
             */
            commands.add(new EmployeurCommand(null, true, nbContrats.intValue(), sitPro));

            // si le contrat se termine durant la p�riode du droit, le
            // b�n�ficiaire de la prestation sera modifi�
            if (!JAUtil.isDateEmpty(sitPro.getDateFinContrat())) {
                commands.add(new EmployeurCommand(sitPro.getDateFinContrat(), false, nbContrats.intValue(), sitPro));
            }

            // ajouter une commande si le montant vers� par l'employeur pour ce
            // droit est diff�rent du salaire normal
            if (!JAUtil.isDateEmpty(sitPro.getDateDebut()) && !JadeStringUtil.isDecimalEmpty(sitPro.getMontantVerse())) {
                commands.add(new EmployeurChangeMontantCommand(sitPro.getDateDebut(), true, sitPro));

                if (!JAUtil.isDateEmpty(sitPro.getDateFin())) {
                    commands.add(new EmployeurChangeMontantCommand(sitPro.getDateFin(), false, sitPro));
                }
            }
        }
        return isIndependant;
    }

    private void ajouterTauxImposition() throws Exception {
        if (droit.getIsSoumisImpotSource().booleanValue()) {

            // si pas de date fin mettre la fin du mois en cours pour le calcul
            String dateFinDuDroit = droit.getDateFinDroit();
            if(JadeStringUtil.isEmpty(dateFinDuDroit)) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                dateFinDuDroit = JadeDateUtil.getGlobazFormattedDate(cal.getTime());
            }

            // on va rechercher tous les taux pour ce canton et pour la p�riode
            // du droit
            String cantonImposition = PRTiersHelper.getCanton(session, droit.getNpa());
            List tauxImpots = findTauxImposition(droit.getDateDebutDroit(), dateFinDuDroit, cantonImposition);

            /*
             * remarque: s'il n'y a pas de taux d'impositions definis pour ce canton et qu'on n'en a pas saisi � la
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
                    PRTauxImposition taux = (PRTauxImposition) tauxImpots.get(idTaux);

                    commands.add(new TauxImpositionCommand(taux.getDateDebut(), true, taux));
                }
            } else {
                PRTauxImposition taux = new PRTauxImposition();
                taux.setTaux(droit.getTauxImpotSource());
                commands.add(new TauxImpositionCommand(droit.getDateDebutDroit(), true, taux));
            }
        }
    }

    /**
     * Cr�e les bases de calculs pour les prestations.
     * 
     * @return une liste (jamais null, peut-�tre vide) d'instances de APBaseCalcul
     * 
     * @throws Exception
     *             si une erreur survient lors du chargement des informations dans la base
     */
    public List createBasesCalcul() throws Exception {
        /*
         * 1. creation des commandes
         * 
         * On commence par ajouter tous les �v�nements relatifs � la situation professionnelle, le traitement est
         * identique pour un droit APG ou maternit�.
         */
        boolean isIndependant = ajouterSituationProfessionnelle();
        ajouterTauxImposition();

        if (droit instanceof APDroitAPG) {
            /*
             * pour le cas ou on traite un droit APG, on commence par ajouter les �v�nements relatifs aux p�riodes de
             * prestation puis on ajoute les changements de situation familiale.
             */
            ajouterPeriodesAPG(); // ajouter les p�riodes et splitter par ann�e
            ajouterSituationFamilialeAPG();
            nbJoursSoldes = Integer.parseInt(((APDroitAPG) droit).getNbrJourSoldes());
        } else if(droit instanceof APDroitPandemie) {
            String dateDebut = ajouterDateDebutFromParam();
            int jourAutrePresta = ApgServiceLocator.getEntityService().getTotalJourAutreDroit(session, droit.getIdDroit());
            nbJoursSoldes = ajouterPeriodesPan(dateDebut, jourAutrePresta, isIndependant);
            ((APDroitPandemie) droit).setNbrJourSoldes(String.valueOf(nbJoursSoldes));
        } else {
            /*
             * pour le cas o� on traite un droit maternit�, on ne traite que la situation familiale et on d�coupe les
             * prestations par mois et/ou par ann�e.
             */
            if ("true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG).getProperty(
                    "isDroitMaterniteCantonale"))) {
                ajouterPremierJuillet2001();
            } else {
                ajouterPremierJuillet2005();
            }

            ajouterSituationFamilialeMat();
        }

        // 2. tri des commandes
        // le fait que ce soit une linkedlist n'influence pas les perfs (voir
        // apidoc java.util.Collections)
        Collections.sort(commands);

        // Cr�er les bases de calcul
        for (Iterator iter = commands.iterator(); iter.hasNext();) {
            ((Command) iter.next()).execute();
        }

        return bases;
    }

    private List findTauxImposition(String dateDebut, String dateFin, String idCanton) throws Exception {
        if (mgrTauxImpot == null) {
            mgrTauxImpot = new PRTauxImpositionManager();
            mgrTauxImpot.setSession(session);
            mgrTauxImpot.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
            mgrTauxImpot.setOrderBy(PRTauxImposition.FIELDNAME_DATEDEBUT);
        }

        mgrTauxImpot.setForPeriode(dateDebut, dateFin);
        mgrTauxImpot.setForCsCanton(idCanton);
        mgrTauxImpot.find();

        return mgrTauxImpot.getContainer();
    }

    // charge une JADAte donn�e dans this.calendar
    private void loadJADate(JADate date) {
        calendar.set(Calendar.DAY_OF_MONTH, 1); // eviter les erreurs
        // eventuelles a cause de
        // lenient == true
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
    }
}
