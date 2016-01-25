/*
 * Cr�� le 7 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveLineFacturation;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.process.AFProcessFacturation.LineFacturation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Calcule les cotisation r�troactives
 * 
 * @author mmu
 */
public class AFCalculRetroactifProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Une classe qui encapsule les donnees et la logique necessaire a la cr�ation des cotisations d'exception pour
     * l'adaptation d'un montant sur les p�riodes restantes durant la fin de l'ann�e.
     * 
     * @author vre
     */
    private class CotisationsList {
        private boolean changementTranche;
        private LinkedList<AFCotisation> cotisations = new LinkedList<AFCotisation>();
        private JADate dateDebutExceptions;

        private LinkedList<AFCotisation> exceptions;

        private LinkedList<AFCotisation> propositionsNouvellesException = new LinkedList<AFCotisation>();

        /**
         * examine les anciennes exceptions pour la p�riode qui nous occupe et les efface ou modifie leurs dates de de
         * d�but ou de fin pour qu'elles ne chevauchent pas les nouvelles que nous allons sauver.
         */
        public void adapterAnciennesExceptions() throws Exception {
            // inutile d'aller plus loin s'il y a d�j� des erreurs dans la
            // transaction
            if (getTransaction().hasErrors()) {
                return;
            }

            // accorder les anciennes exceptions avec celles qui viennent d'�tre
            // cr��es
            for (Iterator<?> exceptIter = getExceptions(); exceptIter.hasNext();) {
                AFCotisation exception = (AFCotisation) exceptIter.next();
                JADate dateDebutAncienne = new JADate(exception.getDateDebut());
                JADate dateFinAncienne = new JADate(exception.getDateFin());

                if ((calendar().compare(dateDebutAncienne, dateDebutExceptions) != JACalendar.COMPARE_FIRSTLOWER)
                        && (calendar().compare(dateFinAncienne, dateFinAnnee) != JACalendar.COMPARE_FIRSTUPPER)) {
                    // l'ancienne exception est enti�rement masqu�e par les
                    // nouvelles, on l'efface
                    exception.delete(getTransaction());

                    if (!getTransaction().hasErrors()) {
                        log(getSession().getLabel("NAOS_ANCIENNE_EXCEPTION_EFFACEE"), FWMessage.INFORMATION);
                    }
                } else if ((calendar().compare(dateDebutAncienne, dateDebutExceptions) == JACalendar.COMPARE_FIRSTLOWER)
                        && (calendar().compare(dateFinAncienne, dateFinAnnee) == JACalendar.COMPARE_FIRSTUPPER)) {
                    // l'ancienne exception masque enti�rement les nouvelles, il
                    // faut splitter l'ancienne
                    AFCotisation split = new AFCotisation();

                    split.copyDataFromEntity(exception);
                    split.setDateDebut(calendar().addDays(dateFinAnnee, 1).toStr("."));
                    split.add(getTransaction());

                    // inutile d'aller plus loin s'il y a d�j� des erreurs dans
                    // la transaction
                    if (getTransaction().hasErrors()) {
                        return;
                    }

                    // modifier la date de fin de l'exception
                    exception.setDateFin(calendar().addDays(dateDebutExceptions, -1).toStr("."));
                    exception.update(getTransaction());

                    if (!getTransaction().hasErrors()) {
                        log(getSession().getLabel("NAOS_ANCIENNE_EXCEPTION_MODIFIEE"), FWMessage.INFORMATION);
                    }
                } else if (calendar().compare(dateDebutAncienne, dateDebutExceptions) == JACalendar.COMPARE_FIRSTLOWER) {
                    // la fin de l'ancienne exception est masqu�e par les
                    // nouvelles, on va modifier la date de fin
                    exception.setDateFin(calendar().addDays(dateDebutExceptions, -1).toStr("."));
                    exception.update(getTransaction());

                    if (!getTransaction().hasErrors()) {
                        log(getSession().getLabel("NAOS_ANCIENNE_EXCEPTION_MODIFIEE"), FWMessage.INFORMATION);
                    }
                } else if (calendar().compare(dateFinAncienne, dateFinAnnee) == JACalendar.COMPARE_FIRSTUPPER) {
                    // le d�but de l'ancienne exception est masqu�e par les
                    // nouvelles, on va modifier la date de d�but
                    exception.setDateDebut(calendar().addDays(dateFinAnnee, 1).toStr("."));
                    exception.update(getTransaction());

                    if (!getTransaction().hasErrors()) {
                        log(getSession().getLabel("NAOS_ANCIENNE_EXCEPTION_MODIFIEE"), FWMessage.INFORMATION);
                    }
                }
            }

        }

        /**
         * ajoute une cotisation dans cette liste, s'il s'agit d'une cotisation simple, elle est ajout� dans les les
         * cotisations, sinon dans les exceptions.
         * 
         * @param cotisation
         */
        public void addCotisation(AFCotisation cotisation) {
            if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(cotisation.getMotifFin())) {
                if (exceptions == null) {
                    exceptions = new LinkedList<AFCotisation>();
                }

                exceptions.add(cotisation);
            } else {
                cotisations.add(cotisation);
            }
        }

        public void addPropositionNouvelleException(AFCotisation propositionNouvelleException) {
            propositionsNouvellesException.add(propositionNouvelleException);
        }

        /**
         * It�re sur toutes les cotisations et, en tenant compte de leur p�riodicit�, cr�e une cotisation d'exception
         * pour chaque p�riode qui suit ou comprend le mois de calcul jusqu'� la fin de l'ann�e , le montant de cette
         * cotisation d'exception est adapt� en fonction du param�tre 'montant' et du nombre de p�riodes restantes.
         * 
         * Les exceptions sont enregistr�es dans une collection priv�e mais ne sont pas sauv�es dans la base, avant de
         * le faire, il faudra d'abord {@link #adapterAnciennesExceptions() adapter les anciennes exceptions}.
         * 
         * @param montant
         *            le montant de diff�rences qu'il faut rattraper
         * @param moisCalcul
         *            le mois de calcul
         * @throws Exception
         */
        public void creerNouvellesExceptions(double montant, int moisCalcul) throws Exception {
            // inutile d'aller plus loin s'il y a d�j� des erreurs dans la
            // transaction
            if (getTransaction().hasErrors()) {
                return;
            }

            // ---
            int anneeCourante = Integer.parseInt(annee);
            int moisDebutException = moisCalcul + 1;

            for (Iterator<AFCotisation> cotisIter = getCotisations(); cotisIter.hasNext();) {
                AFCotisation cotisation = cotisIter.next();
                new JADate(cotisation.getDateDebut());
                AFCotisation exception = new AFCotisation();
                exception.copyDataFromEntity(cotisation);
                exception.setCotisationId(""); // effacer l'id pour permettre
                // l'ajout dans la base
                exception.setMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
                exception.setDateDebut("01."
                        + (moisDebutException < 10 ? "0" + moisDebutException : "" + moisDebutException) + "."
                        + anneeCourante);
                exception.setDateFin(AFUtil.getDateEndOfYear(exception.getDateDebut()));

                try {
                    if (!JadeStringUtil.isBlankOrZero(cotisation.getAssurance().getIdAssuranceReference())) {
                        montant = corrigeMontantForAssRef(cotisation.getAssurance().getAssuranceReference(), "01."
                                + JadeStringUtil.fillWithZeroes(String.valueOf(moisCalcul), 2) + "." + anneeCourante,
                                montant);
                    }
                } catch (Exception e) {
                    throw new Exception(getSession().getLabel("CALCUL_ERRONE_EXCEPTION") + " : " + e.toString(), e);
                }

                if (CodeSystem.PERIODICITE_ANNUELLE.equals(cotisation.getPeriodicite())) {
                    // aucune modif
                    return;
                } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(cotisation.getPeriodicite())) {
                    int div = (12 - moisCalcul) / 3;
                    BigDecimal mnt = new BigDecimal(montant * 4 / div);
                    mnt = mnt.add(new BigDecimal(cotisation.getMasseAnnuelle()));
                    exception.setMasseAnnuelle(mnt.toString());
                } else if (CodeSystem.PERIODICITE_MENSUELLE.equals(cotisation.getPeriodicite())) {
                    int div = 12 - moisCalcul;
                    BigDecimal mnt = new BigDecimal(montant * 12 / div);
                    mnt = mnt.add(new BigDecimal(cotisation.getMasseAnnuelle()));
                    exception.setMasseAnnuelle(mnt.toString());
                }
                // l'exception est pr�te
                addPropositionNouvelleException(exception);
            }
        }

        public Iterator<AFCotisation> getCotisations() {
            return cotisations.iterator();
        }

        public Iterator<?> getExceptions() {
            return exceptions == null ? Collections.EMPTY_LIST.iterator() : exceptions.iterator();
        }

        public Iterator<AFCotisation> getPropositionsNouvellesExceptions() {
            return propositionsNouvellesException.iterator();
        }

        /**
         * Retourne vrai si au moins une assurance pour une cotisation de cette liste est du type taux variable et que
         * la masse annuelle adapt�e pour cette cotisation entra�ne un changement de tranche (et donc de taux de
         * cotisations).
         * 
         * @return vrai si la nouvelle masse entra�ne un changement de tranche.
         */
        public boolean isChangementTranche() {
            return changementTranche;
        }

        /**
         * sauve les nouvelles exception.
         * 
         * @throws Exception
         */
        public void sauverNouvellesExceptions() throws Exception {
            // inutile d'aller plus loin s'il y a d�j� des erreurs dans la
            // transaction
            if (getTransaction().hasErrors()) {
                return;
            }

            // sauver les exceptions
            MessageFormat mf = new MessageFormat(JadeStringUtil.change(getSession().getLabel("NAOS_EXCEPTION_CREE"),
                    "'", "''"));
            Object[] args = new Object[4];

            for (Iterator<AFCotisation> exceptIter = getPropositionsNouvellesExceptions(); exceptIter.hasNext();) {
                AFCotisation exception = exceptIter.next();

                exception.add(getTransaction());

                // inutile d'aller plus loin s'il y a d�j� des erreurs dans la
                // transaction
                if (getTransaction().hasErrors()) {
                    return;
                } else {
                    args[0] = exception.getAssurance().getAssuranceLibelleCourt();
                    args[1] = exception.getDateDebut();
                    args[2] = exception.getDateFin();
                    args[3] = exception.getMasseAnnuelle();
                    log(mf.format(args), FWMessage.INFORMATION);
                }
            }
        }
    }

    private AFAffiliationViewBean affiliation = null;
    private String annee = "";
    private Boolean compenserMontantNegatif = Boolean.FALSE;
    private Boolean compenserMontantPositif = Boolean.FALSE;
    private String dateCalcul = "";
    private String dateDebutCalculRevele = "";
    private JADate dateFinAnnee;
    private String idAffiliation = "";
    private String idTiers = "";
    private String mois = "";
    private Boolean previsionAcompteEbu = Boolean.FALSE;
    private boolean processExterne = false;
    private HashMap<String, AFAssurance> rubrAssu = new HashMap<String, AFAssurance>(); // table de cache qui contient
                                                                                        // le

    // lien idRubrique -->
    // AFAssurance
    private BISession sessionOsiris = null;

    private TITiersViewBean tiers = null;

    /**
	 * 
	 */
    public AFCalculRetroactifProcess() {
        super();
    }

    /**
     * @return the dateDebutCalculRevele
     */
    public String getDateDebutCalculRevele() {
        return dateDebutCalculRevele;
    }

    /**
     * @param dateDebutCalculRevele the dateDebutCalculRevele to set
     */
    public void setDateDebutCalculRevele(String dateDebutCalculRevele) {
        this.dateDebutCalculRevele = dateDebutCalculRevele;
    }

    /**
     * @param parent
     */
    public AFCalculRetroactifProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public AFCalculRetroactifProcess(BSession session) {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        boolean success = true;
        mois = dateCalcul.substring(0, 2);
        annee = dateCalcul.substring(3);
        dateFinAnnee = new JADate(31, 12, Integer.parseInt(annee));
        tiers = getAffiliation().getTiers();
        idTiers = tiers.getIdTiers();

        try {
            // cl�s: idRubrique, valeurs: double[3] (cotisation, factur�,
            // diff�rence)
            HashMap<String, double[]> rubrMasseFactures = new HashMap<String, double[]>();
            // Calcule le montant de la cotisation que l'affili� devrait/aurait du payer
            calculerMasses(rubrMasseFactures);
            // Calcule le montant qui a d�j� �t� pay�
            getMasseFacture(rubrMasseFactures);
            // Calcule les diff�rences entre les valeurs calcul�es et les valeurs factur�es
            boolean changed = false;
            double montantARecevoir = 0;
            for (Iterator<double[]> iter = rubrMasseFactures.values().iterator(); iter.hasNext();) {
                double[] element = iter.next();
                double diff = element[0] - element[1]; // montant calcul� -
                // montant factur�
                element[2] = diff; // met � jour la hashtable
                // s'il y a une difference
                if (diff != 0) {
                    changed = true;
                    montantARecevoir += diff;
                }
            }

            // si cotisation factur�e vaut la cotisation calcul�e, on ne fait
            // rien.
            if (changed) {
                // On compense ou on cr�e un releve
                if (montantARecevoir > 0.0) {
                    if (isCompenserMontantPositif()) {
                        genererCompensation(rubrMasseFactures);
                    } else {
                        genererReleve(rubrMasseFactures);
                    }
                } else if (montantARecevoir < 0.0) {
                    if (isCompenserMontantNegatif()) {
                        genererCompensation(rubrMasseFactures);
                    } else {
                        genererReleve(rubrMasseFactures);
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, toString());
            success = false;
            // Si le calcul a �t� lanc� par un autre process, ne pas fermer la transaction pour que le process appelant
            // continue de fonctionner
            if (isProcessExterne() == false) {
                getTransaction().rollback();
                getTransaction().closeTransaction();
            } else {
                this._addError(getTransaction(), e.getMessage().toString());
            }
        }

        return success;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        super._validate();

        // V�rifie l'affiliation
        if (JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
            this._addError(getSession().getLabel("2050"));
        } else {
            if (getAffiliation().isNew()) {
                this._addError(getSession().getLabel("2050"));
            } else {
                idTiers = affiliation.getIdTiers();
            }
        }
        // V�rifie le format de la date
        if (JadeStringUtil.isBlank(dateCalcul)) {
            this._addError(getSession().getLabel("2030"));

        } else {
            try {
                if (dateCalcul.length() != 7) {
                    throw new Exception();
                }
                mois = dateCalcul.substring(0, 2);
                annee = dateCalcul.substring(3);
                dateFinAnnee = new JADate(31, 12, Integer.parseInt(annee));
                if ((Integer.parseInt(mois) < 1) || (Integer.parseInt(mois) > 12)) {
                    throw new Exception();
                }
                Integer.parseInt(annee); // lance une exception si format
                // incorrecte
            } catch (Exception e) {
                this._addError(getSession().getLabel("2040"));
            }

        }
        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Calcule la cotisation qui devrait �tre factur�e � un affil� pour une ann�e jusqu'au mois donn�
     * 
     */
    protected void calculerMasses(HashMap<String, double[]> rubrMasseFactures) throws Exception {

        AFProcessFacturationManager factMgr = new AFProcessFacturationManager();
        factMgr.setSession(getSession());
        factMgr.setForTypeAffiliation(AFProcessFacturationManager.TYPE_AFFILIATION_PARITAIRE_PERSONNEL);
        factMgr.setForIdTiers(idTiers);
        factMgr.setForAffilieNumero(getAffiliation().getAffilieNumero());

        int moisFin = Integer.parseInt(mois);
        // calcule les cotisations pour chaque mois
        for (int iMois = 1; iMois <= moisFin; iMois++) {
            String moisPeriode = (iMois < 10 ? "0" + String.valueOf(iMois) : String.valueOf(iMois));
            String periodeFacturation = moisPeriode + "." + annee;
            factMgr.setForDateFacturation(periodeFacturation);
            BStatement statement = factMgr.cursorOpen(getTransaction());
            AFProcessFacturationViewBean donneesFacturation = null;
            // itere sur toutes les lignes de factures qui devrait �tre g�n�r�
            // et les enregiste dans la HashMap
            while ((donneesFacturation = (AFProcessFacturationViewBean) factMgr.cursorReadNext(statement)) != null) {
                AFProcessFacturation.CalculCotisation calculCotisation = AFProcessFacturation.calculerCotisation(this,
                        donneesFacturation, annee, moisPeriode, true, true, false, false,
                        new ArrayList<LineFacturation>(), 0, "");
                for (Iterator<?> iter = calculCotisation.getMontantList().iterator(); iter.hasNext();) {
                    AFProcessFacturation.LineFacturation lineFacturation = (AFProcessFacturation.LineFacturation) iter
                            .next();
                    double masse = lineFacturation.getMasse();
                    // on ajoute la masse d'une ligne de facture au montant de
                    // la cotisation pour une rubrique
                    double[] masseFacture = rubrMasseFactures.get(lineFacturation.getIdRubrique());
                    if (masseFacture == null) {
                        // si aucun montant n'est associ� � la rubrique, cr�e
                        // l'entr�e
                        rubrMasseFactures.put(lineFacturation.getIdRubrique(), new double[] { masse, 0, 0, });
                        // rempli le cache: rubrique/assurance
                        rubrAssu.put(lineFacturation.getIdRubrique(), donneesFacturation.getAssurance());

                    } else {
                        // sinon on ajoute le montant
                        masseFacture[0] += masse;
                    }
                }
            }
            factMgr.cursorClose(statement);
        }
    }

    JACalendar calendar() throws Exception {
        return getSession().getApplication().getCalendar();
    }

    private double corrigeMontantForAssRef(AFAssurance assDeReference, String dateCalcul, double montant)
            throws Exception {

        StringBuffer wrongArguments = new StringBuffer();
        String exceptionTextFixe = "Can't correct montant for ass ref due to ";

        if (assDeReference == null) {
            wrongArguments.append("assDeReference : " + assDeReference + " ");
        }

        if (!(new JACalendarGregorian().isValid(dateCalcul))) {
            wrongArguments.append("dateCalcul : " + dateCalcul + " ");
        }

        if (!JadeStringUtil.isEmpty(wrongArguments.toString())) {
            throw new Exception(exceptionTextFixe + "wrong arguments : " + wrongArguments.toString());
        }

        AFTauxAssurance theTaux = null;
        double theTauxDouble = 1;

        theTaux = assDeReference.getTaux(dateCalcul);

        if (theTaux == null) {
            throw new Exception(FWMessageFormat.format(getSession().getLabel("AUCUN_TAUX_TROUVE_FOR_ASS_REF"),
                    assDeReference.getAssuranceLibelle(), dateCalcul));
        }

        theTauxDouble = theTaux.getTauxDouble();

        if (theTauxDouble <= 0) {
            throw new Exception(FWMessageFormat.format(getSession().getLabel("TAUX_ASS_REF_NON_VALIDE"), theTauxDouble,
                    assDeReference.getAssuranceLibelle(), dateCalcul));
        }

        return montant / theTauxDouble;
    }

    /**
     * Adapte l'acompte pour les p�riodes futures afin de rattrapper une diff�rence de cotisations.
     * 
     * Cr�e des cotisations d'exceptions pour toutes les p�riodes jusqu'� la fin de l'ann�e si possible.
     * 
     * @param rubrCotiFactures
     *            map des montants cotis, factur�s et diffs index�s par rubriques
     * @param montant
     *            le montant total de diff�rence
     */
    protected void genererCompensation(HashMap<String, double[]> rubrMasseFactures) throws Exception {
        // d�terminer les mois pour la cr�ation de l'exception
        int moisCalcul = Integer.parseInt(mois);

        // charger les cotisations
        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setForAnneeActive(annee);
        cotisMgr.setForAffiliationId(idAffiliation);
        cotisMgr.setSession(getSession());
        cotisMgr.find();

        // s�lectionner les cotisations actives pour cet affilie et trier par idRubrique
        /* String(idRubrique) -> CotisationsList */
        HashMap<String, CotisationsList> idRubriToCotis = new HashMap<String, CotisationsList>();

        for (int idCoti = 0; idCoti < cotisMgr.size(); ++idCoti) {
            AFCotisation cotisation = (AFCotisation) cotisMgr.get(idCoti);
            String rubriqueId = cotisation.getAssurance().getRubriqueId();

            if (!rubrMasseFactures.keySet().contains(rubriqueId)
                    || !CodeSystem.GENRE_ASS_PARITAIRE.equals(cotisation.getAssurance().getAssuranceGenre())
                    || CodeSystem.TYPE_ASS_FFPP.equals(cotisation.getAssurance().getTypeAssurance())) {
                // si la rubrique n'a pas �t� consid�r�e dans la facturation
                // ou si l'assurance n'est pas une assurance paritaire
                // ou s'il s'agit de FFPP, on ignore la cotisation
                continue;
            }

            // si la cotisation a une date de fin et que cette date est
            // ant�rieure � la date de calcul, on l'ignore
            if (!JadeStringUtil.isEmpty(cotisation.getDateFin())
                    && (calendar().compare(dateCalcul, cotisation.getDateFin()) != JACalendar.COMPARE_SECONDUPPER)) {
                continue;
            }
            // sinon on ajoute la cotisation dans la liste
            CotisationsList cotis = idRubriToCotis.get(rubriqueId);
            if (cotis == null) {
                cotis = new CotisationsList();
                idRubriToCotis.put(rubriqueId, cotis);
            }
            cotis.addCotisation(cotisation);
        }

        // Retrouver les diff�rences de cotisations
        boolean changementTranche = false;

        for (Iterator<?> rubrIter = rubrMasseFactures.entrySet().iterator(); rubrIter.hasNext();) {
            Map.Entry entry = (Map.Entry) rubrIter.next();
            String rubriqueId = (String) entry.getKey();
            // si la rubrique n'est pas � consid�rer pour les r�tro-actifs, on
            // ignore la rubrique
            if (!idRubriToCotis.keySet().contains(rubriqueId)) {
                continue;
            }
            // cr�er les exceptions
            double[] diffs = (double[]) entry.getValue();
            if (diffs[2] != 0.0) {
                // il y a une diff�rence entre les cotisations calcul�es et les
                // cotisations pay�es, il faut adapter
                CotisationsList cotis = idRubriToCotis.get(rubriqueId);
                cotis.creerNouvellesExceptions(diffs[2], moisCalcul);
                cotis.adapterAnciennesExceptions();
                cotis.sauverNouvellesExceptions();
                changementTranche = changementTranche || cotis.isChangementTranche();
            }
        }
    }

    protected void genererReleve(HashMap<String, double[]> rubrMasseFactures) throws Exception {

        String dateDebut = "01.01." + annee;

        if (!JadeStringUtil.isBlank(dateDebutCalculRevele)) {
            dateDebut = "01." + dateDebutCalculRevele;
        }

        if (affiliation != null) {
            if (BSessionUtil.compareDateFirstGreater(getSession(), affiliation.getDateDebut(), dateDebut)) {
                // l'affiliation est apr�s le 01.01 -> utiliser la date de d�but
                // d'affiliation
                dateDebut = affiliation.getDateDebut();
            }
        }

        int jours = new JACalendarGregorian().daysInMonth(Integer.parseInt(mois), Integer.parseInt(annee));
        String dateFin = jours + "." + mois + "." + annee; // jusqu'au dernier jour du mois

        // 1. Cr�er un relev�
        AFApercuReleve releve = new AFApercuReleve();
        if (!releve.retrieveIdPassage()) {
            throw new Exception("Aucun passage pour relev� ouvert!");
        }
        releve.setISession(getSession());
        releve.setIdTiers(idTiers);
        releve.setAffilieNumero(getAffiliation().getAffilieNumero());
        releve.setDateDebut(dateDebut); // depuis de d�but de l'an
        releve.setDateFin(dateFin); // jusqu'au dernier jour du mois
        releve.setType(CodeSystem.TYPE_RELEVE_RECTIF);
        releve.setEtat(CodeSystem.ETATS_RELEVE_SAISIE);
        releve.setInterets(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);
        releve.retrieveIdPassage();
        releve.setIdExterneFacture(annee + mois + "000");
        releve.setIdSousTypeFacture("2270" + mois);
        releve.setInterets(CodeSystem.INTERETS_RELEVE_AUTO);
        releve.setNewEtat(CodeSystem.ETATS_RELEVE_SAISIE);
        releve.generationCotisationList();
        for (int i = 0; i < releve.getCotisationList().size(); i++) {
            AFApercuReleveLineFacturation releveLine = releve.getCotisationList().get(i);
            double[] massesFactures = rubrMasseFactures.get(releveLine.getAssuranceRubriqueId());
            if (massesFactures != null) {
                double difference = massesFactures[2];
                if (difference != 0) {
                    releveLine.setMasse(difference);
                }
            }
        }
        releve.setPrevisionAcompteEBU(getPrevisionAcompteEbu());
        releve.calculeCotisation();
        releve.setTotalControl(releve.getTotalCalculer());
        releve.setWantControleCotisation(false);
        releve.add(getTransaction());

        // si le relev� n'a pas �t� cr�e
        if (releve.isNew()) {
            throw new Exception("Impossible de cr�er un relev�:" + getTransaction().getErrors().toString());
        }

    }

    public AFAffiliationViewBean getAffiliation() throws Exception {
        if (affiliation == null) {
            // try {
            affiliation = new AFAffiliationViewBean();
            affiliation.setSession(getSession());
            affiliation.setId(getIdAffiliation());
            affiliation.retrieve();
            // } catch (Exception e) {}
        }

        return affiliation;
    }

    public String getAnnee() {
        return annee;
    }

    public Boolean getCompenserMontantNegatif() {
        return compenserMontantNegatif;
    }

    public Boolean getCompenserMontantPositif() {
        return compenserMontantPositif;
    }

    /**
     * Retourne le compte annexe associ� � l'affili� <BR>
     * Le cr�e s'il n'existe pas
     */
    protected CACompteAnnexe getCompteAnnexe() throws Exception {
        CACompteAnnexe compte;
        compte = new CACompteAnnexe();
        compte.setISession(getSessionOsiris());
        compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compte.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication()));
        compte.setIdExterneRole(getAffiliation().getAffilieNumero());
        compte.setIdTiers(idTiers);
        compte.retrieve(getTransaction());
        // cr�e le compte s'il n'existe pas
        if (compte.isNew()) {
            compte.setIdCategorie(CACompteAnnexe.CATEGORIE_COMPTE_STANDARD);
            compte.add(getTransaction());
            if (compte.isNew()) {
                throw new Exception("Impossible de cr�er le compte annexe: " + getTransaction().getErrors().toString());
            }
        }
        return compte;
    }

    public String getDateCalcul() {
        return dateCalcul;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return "Calcul des cotisations r�troactives";
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Calcule la masse qui a d�j� �t� factur� � l'affili�: <BR>
     * ajoute dans la 2�me colonne de la HashMap le montant factur� par rubrique
     */
    protected void getMasseFacture(HashMap<String, double[]> rubrMasseFactures) throws Exception {
        // cherche le compte annexe
        CACompteAnnexe compte = getCompteAnnexe();

        // Cherche tous les montants qui ont d�j� �t� pay�s par l'affil�
        CACompteurManager compteurMgr = new CACompteurManager();
        compteurMgr.setISession(getSessionOsiris());
        compteurMgr.setForAnnee(getAnnee());
        compteurMgr.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
        compteurMgr.find(getTransaction());
        for (Iterator<?> iter = compteurMgr.iterator(); iter.hasNext();) {
            CACompteur compteur = (CACompteur) iter.next();

            double[] masseFacture = rubrMasseFactures.get(compteur.getIdRubrique());
            if (masseFacture == null) {
                // si aucun montant n'est associ� � la rubrique, cr�e l'entr�e
                rubrMasseFactures.put(compteur.getIdRubrique(),
                        new double[] { 0, Double.parseDouble(compteur.getCumulMasse()), 0 });

            } else {
                // sinon on ajoute le montant
                masseFacture[1] += Double.parseDouble(compteur.getCumulMasse());
            }
            // System.out.println("Compteur "+compteur.getIdRubrique()+": "+compteur.getCumulMasse());
        }
    }

    public String getMois() {
        return mois;
    }

    public Boolean getPrevisionAcompteEbu() {
        return previsionAcompteEbu;
    }

    /*
     * public void setAffiliation(AFAffiliationViewBean affiliation) { this.affiliation = affiliation; }
     */
    public BISession getSessionOsiris() throws Exception {
        if (sessionOsiris == null) {
            AFApplication appAf = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            sessionOsiris = appAf.getSessionOsiris(getSession());

        }
        return sessionOsiris;
    }

    public boolean isCompenserMontantNegatif() {
        return compenserMontantNegatif.booleanValue();
    }

    public boolean isCompenserMontantPositif() {
        return compenserMontantPositif.booleanValue();
    }

    public boolean isProcessExterne() {
        return processExterne;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * log un message du type donn�
     * 
     * @param message
     * @param msgType
     */
    private void log(String message, String msgType) {
        getMemoryLog().logMessage(message, msgType, this.getClass().getName());
    }

    public void setCompenserMontantNegatif(Boolean boolean1) {
        compenserMontantNegatif = boolean1;
    }

    public void setCompenserMontantPositif(Boolean boolean1) {
        compenserMontantPositif = boolean1;
    }

    public void setDateCalcul(String string) {
        dateCalcul = string;
    }

    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

    public void setPrevisionAcompteEbu(Boolean previsionAcompteEbu) {
        this.previsionAcompteEbu = previsionAcompteEbu;
    }

    // ----------------------------------------------------------------------------------------------------------

    public void setProcessExterne(boolean processExterne) {
        this.processExterne = processExterne;
    }
}
