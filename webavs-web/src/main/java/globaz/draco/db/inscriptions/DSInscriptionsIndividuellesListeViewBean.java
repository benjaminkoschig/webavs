package globaz.draco.db.inscriptions;

import globaz.commons.nss.NSUtil;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.util.DSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;
import java.util.Collection;

public class DSInscriptionsIndividuellesListeViewBean extends DSDeclarationViewBean implements FWViewBeanInterface {

    private static final long serialVersionUID = 3080464130999093292L;
    private final static int MAX_INSC_PAR_VALIDATION = 15;

    private Collection<String> acI = new ArrayList<String>();
    private Collection<String> acII = new ArrayList<String>();
    private Collection<String> anneeInsc = new ArrayList<String>();
    private Boolean aTraiter = new Boolean(false);
    private Collection<String> casSpecial = new ArrayList<String>();
    private Collection<String> categoriePerso = new ArrayList<String>();
    private Collection<String> codeCanton = new ArrayList<String>();
    private Collection<String> description = new ArrayList<String>();
    private Boolean forAvertissement = new Boolean(false);
    private String forMontantSigne = "";
    private String forMontantSigneValue = "";
    private String fromNomPrenom = "";
    private String fromNumeroAvs = "";
    private String fromNumeroAvsNNSS = "";
    private Collection<String> genreEcriture = new ArrayList<String>();
    private boolean hasShowRight = true;
    private Collection<String> idDepart = new ArrayList<String>();
    private Collection<String> idInscription = new ArrayList<String>();
    private Collection<String> isCI = new ArrayList<String>();
    private Collection<String> isRA = new ArrayList<String>();

    private boolean isSalaireDifferePresent = false;
    private CIJournal journal = null;
    private String likeNumeroAvs = "";
    private DSInscriptionsIndividuellesManager mgr = new DSInscriptionsIndividuellesManager();
    private String modeAjout = "lecture";
    private Collection<String> montant = new ArrayList<String>();
    private Collection<String> montantAF = new ArrayList<String>();
    private Collection<String> nnss = new ArrayList<String>();
    private Collection<String> nomPrenom = new ArrayList<String>();
    private Collection<String> numeroAvs = new ArrayList<String>();
    private Collection<String> numeroAvsNNSS = new ArrayList<String>();
    private Collection<String> periodeDebut = new ArrayList<String>();

    private Collection<String> periodeFin = new ArrayList<String>();

    private Collection<String> remarqueCont = new ArrayList<String>();
    private Collection<String> soumis = new ArrayList<String>();

    public DSInscriptionsIndividuellesListeViewBean() {
        super();
        mgr.setSession(getSession());
        mgr.changeManagerSize(DSInscriptionsIndividuellesListeViewBean.MAX_INSC_PAR_VALIDATION);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        Collection<String> saveAcI = new ArrayList<String>();
        Collection<String> saveAcII = new ArrayList<String>();
        Collection<String> saveAF = new ArrayList<String>();
        Collection<String> saveIdInscription = new ArrayList<String>();
        Collection<String> savePeriodeDebut = new ArrayList<String>();
        Collection<String> savePeriodeFin = new ArrayList<String>();
        Collection<String> saveCasSpecial = new ArrayList<String>();
        Collection<String> saveSoumis = new ArrayList<String>();
        Collection<String> saveNomPrenom = new ArrayList<String>();
        Collection<String> saveGre = new ArrayList<String>();
        Collection<String> saveId = new ArrayList<String>();
        Collection<String> saveNumeroAvs = new ArrayList<String>();
        Collection<String> saveNNSS = new ArrayList<String>();
        Collection<String> saveIsRa = new ArrayList<String>();
        Collection<String> saveDescription = new ArrayList<String>();
        Collection<String> saveCodeCanton = new ArrayList<String>();
        Collection<String> saveCategoriePerso = new ArrayList<String>();
        Collection<String> saveAnnee = new ArrayList<String>();
        Collection<String> saveMontant = new ArrayList<String>();

        // en premier lieu, on reparcours tous les ids de départ pour voir si on a de la suppression

        for (int ind = 0; ind < idDepart.size(); ind++) {
            String depart = ((ArrayList<String>) idDepart).get(ind);
            // Si la table des ids ne contient plus la valeurs des ids de départ, il faut la supprimer
            if (!idInscription.contains(depart)) {
                DSInscriptionsIndividuelles inscritpionASupprimer = new DSInscriptionsIndividuelles();
                inscritpionASupprimer.setSession(getSession());
                inscritpionASupprimer.setIdInscription(depart);
                inscritpionASupprimer.retrieve(transaction);
                if (!inscritpionASupprimer.isNew()) {
                    inscritpionASupprimer.delete(transaction);
                }

            }
        }
        for (int i = 0; i < idInscription.size(); i++) {

            DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
            // Si le numéro avs est saisi vide, on le remplace par "0000"
            if (JadeStringUtil.isBlank(((ArrayList<String>) numeroAvs).get(i))
                    && JadeStringUtil.isBlank(((ArrayList<String>) nomPrenom).get(i))
                    && (!JadeStringUtil.isIntegerEmpty(((ArrayList<String>) genreEcriture).get(i))
                            || !JadeStringUtil.isIntegerEmpty(((ArrayList<String>) acI).get(i))
                            || !JadeStringUtil.isIntegerEmpty(((ArrayList<String>) acII).get(i))
                            || !JadeStringUtil.isIntegerEmpty(((ArrayList<String>) montantAF).get(i)) || !JadeStringUtil
                                .isIntegerEmpty(((ArrayList<String>) montant).get(i)))) {
                ((ArrayList<String>) numeroAvs).remove(i);
                ((ArrayList<String>) numeroAvs).add(i, "000.00.000");
            }

            if (!JadeStringUtil.isBlank(((ArrayList<String>) numeroAvs).get(i))
                    || !JadeStringUtil.isBlank(((ArrayList<String>) nomPrenom).get(i))) {
                if (JadeStringUtil.isIntegerEmpty(((ArrayList<String>) idInscription).get(i))) {
                    insc.setDeclaration(this);
                    insc.setIdDeclaration(getIdDeclaration());
                    insc.setPeriodeDebut(((ArrayList<String>) periodeDebut).get(i));
                    insc.setPeriodeFin(((ArrayList<String>) periodeFin).get(i));
                    insc.setMontant(JANumberFormatter.deQuote(((ArrayList<String>) montant).get(i)));
                    insc.setRemarqueControle(((ArrayList<String>) remarqueCont).get(i));
                    insc.setNumeroAvs(CIUtil.unFormatAVS(((ArrayList<String>) numeroAvs).get(i)));
                    FWCurrency mont = new FWCurrency(JANumberFormatter.deQuote(((ArrayList<String>) montant).get(i)));
                    String genreEcr = ((ArrayList<String>) genreEcriture).get(i);
                    // Si montant négatif, on le remet à 0 et on set le code extourne
                    if (mont.isNegative()) {
                        mont.negate();
                        insc.setMontant(mont.toString());
                        if (JadeStringUtil.isBlank(genreEcr)) {
                            insc.setGenreEcriture("11");
                        } else if (genreEcr.length() == 2) {
                            insc.setGenreEcriture("1" + genreEcr.substring(1, 2));
                        }
                    } else {
                        insc.setGenreEcriture(genreEcr);
                        insc.setMontant(mont.toString());
                    }
                    insc.setNomPrenom(((ArrayList<String>) nomPrenom).get(i));
                    insc.setNumeroAvsNNSS(((ArrayList<String>) numeroAvsNNSS).get(i));
                    if ("true".equalsIgnoreCase(((ArrayList<String>) numeroAvsNNSS).get(i))) {
                        insc.setIsNNNSS(new Boolean(true));
                    } else if ("false".equalsIgnoreCase(((ArrayList<String>) numeroAvsNNSS).get(i))) {
                        insc.setIsNNNSS(new Boolean(false));
                    }
                    if ((anneeInsc != null) && (anneeInsc.size() > i)) {
                        if (!JadeStringUtil.isBlankOrZero(((ArrayList<String>) anneeInsc).get(i))) {
                            insc.setAnneeInsc(((ArrayList<String>) anneeInsc).get(i));
                        } else {
                            insc.setAnneeInsc(getAnnee());
                        }
                    } else {
                        insc.setAnneeInsc(getAnnee());
                    }
                    insc.setACI(JANumberFormatter.deQuote(((ArrayList<String>) acI).get(i)));
                    insc.setACII(JANumberFormatter.deQuote(((ArrayList<String>) acII).get(i)));
                    // insc.setACII(JANumberFormatter.deQuote((String)((ArrayList)acII).get(i)));
                    insc.setSoumis(((ArrayList<String>) soumis).get(i).equals("true") ? new Boolean(true)
                            : new Boolean(false));
                    insc.setMontantAf(JANumberFormatter.deQuote(((ArrayList<String>) montantAF).get(i)));
                    insc.setRemarqueControle(((ArrayList<String>) remarqueCont).get(i));
                    insc.setCodeCanton(((ArrayList<String>) codeCanton).get(i));
                    // insc.setGenreEcriture((String)((ArrayList)genreEcriture).get(i));
                    insc.setCategoriePerso(((ArrayList<String>) categoriePerso).get(i));
                    try {
                        insc.add(transaction);
                    } catch (Exception e) {
                        _addError(transaction, getSession().getLabel("ERREUR_AJOUT_INSCRITPION"));
                    }
                    // pour le rafraichissement des données à l'écran
                    saveAcI.add(new FWCurrency(insc.getACI()).toStringFormat());
                    saveAcII.add(new FWCurrency(insc.getACII()).toStringFormat());
                    saveAF.add(new FWCurrency(insc.getMontantAf()).toStringFormat());
                    saveSoumis.add(insc.getSoumis().booleanValue() ? "true" : "false");
                    savePeriodeDebut.add(!JadeStringUtil.isIntegerEmpty(insc.getMontant()) ? insc.getPeriodeDebut()
                            : insc.getPeriodeDebutAF());
                    savePeriodeFin.add(!JadeStringUtil.isIntegerEmpty(insc.getMontant()) ? insc.getPeriodeFin() : insc
                            .getPeriodeFinAF());
                    saveIdInscription.add(insc.getIdInscription());
                    saveCasSpecial.add(insc.getCasSpecial().booleanValue() ? "true" : "false");
                    saveNomPrenom.add(insc.getNomPrenom());
                    saveNumeroAvs.add(insc.getNssFormateWithoutPrefix());
                    saveNNSS.add(insc.getNumeroAvsNNSS());
                    saveDescription.add(insc.getEtatFormate());
                    saveIsRa.add(insc.isRAString());
                    saveCodeCanton.add(insc.getCodeCanton());
                    saveGre.add(!JadeStringUtil.isBlankOrZero(insc.getGenreEcriture()) ? insc.getGenreEcriture() : "");
                    saveId.add(insc.getIdInscription());
                    saveCategoriePerso.add(insc.getCategoriePerso());
                    saveAnnee.add(insc.getAnneeInsc());
                    if (!JadeStringUtil.isBlankOrZero(insc.getGenreEcriture())) {
                        if (insc.getGenreEcriture().startsWith("1") && (insc.getGenreEcriture().length() == 2)) {
                            FWCurrency mon = new FWCurrency(insc.getMontant());
                            mon.negate();
                            saveMontant.add(mon.toStringFormat());
                        } else {
                            saveMontant.add(new FWCurrency(insc.getMontant()).toStringFormat());
                        }

                    } else {
                        saveMontant.add(new FWCurrency(insc.getMontant()).toStringFormat());
                    }
                    if (transaction.hasErrors()) {
                        Collection<String> saveNumWithoutPrefixe = new ArrayList<String>();
                        // en cas d'erreur, il faut réafficher le NSS sans préfixe
                        for (int j = 0; j < numeroAvs.size(); j++) {
                            String noAvs = ((ArrayList<String>) numeroAvs).get(j);
                            String noAvsNNSS = ((ArrayList<String>) numeroAvsNNSS).get(j);
                            if ("true".equalsIgnoreCase(noAvsNNSS)) {
                                saveNumWithoutPrefixe.add(NSUtil.formatWithoutPrefixe(noAvs, true));
                            } else if ("false".equalsIgnoreCase(noAvsNNSS)) {
                                saveNumWithoutPrefixe.add(NSUtil.formatWithoutPrefixe(noAvs, false));
                            }

                        }
                        numeroAvs = null;
                        numeroAvs = saveNumWithoutPrefixe;
                        StringBuffer errs = new StringBuffer();
                        errs.append(insc.getNssFormate() + ":");
                        errs.append("\n");
                        errs.append(transaction.getErrors());
                        transaction.clearErrorBuffer();
                        transaction.addErrors(errs.toString());
                        break;
                    }
                } else {
                    insc.setIdInscription(((ArrayList<String>) idInscription).get(i));
                    insc.retrieve(transaction);
                    insc.setDeclaration(this);
                    insc.setIdDeclaration(getIdDeclaration());
                    insc.setPeriodeDebut(((ArrayList<String>) periodeDebut).get(i));
                    insc.setPeriodeFin(((ArrayList<String>) periodeFin).get(i));
                    FWCurrency mont = new FWCurrency(JANumberFormatter.deQuote(((ArrayList<String>) montant).get(i)));
                    insc.setRemarqueControle(((ArrayList<String>) remarqueCont).get(i));
                    String genreEcr = ((ArrayList<String>) genreEcriture).get(i);
                    // Si montant négatif, on le remet à 0 et on set le code extourne
                    if (mont.isNegative()) {
                        mont.negate();
                        insc.setMontant(mont.toString());
                        if (JadeStringUtil.isBlank(genreEcr)) {
                            insc.setGenreEcriture("11");
                        } else if (genreEcr.length() > 1) {
                            insc.setGenreEcriture("1" + genreEcr.substring(1, 2));
                        }
                    } else {
                        insc.setMontant(mont.toString());
                        insc.setGenreEcriture(genreEcr);
                    }

                    insc.setNumeroAvs(CIUtil.unFormatAVS(((ArrayList<String>) numeroAvs).get(i)));
                    insc.setNomPrenom(((ArrayList<String>) nomPrenom).get(i));
                    insc.setNumeroAvsNNSS(((ArrayList<String>) numeroAvsNNSS).get(i));
                    if ("true".equalsIgnoreCase(((ArrayList<String>) numeroAvsNNSS).get(i))) {
                        insc.setIsNNNSS(new Boolean(true));
                    } else if ("false".equalsIgnoreCase(((ArrayList<String>) numeroAvsNNSS).get(i))) {
                        insc.setIsNNNSS(new Boolean(false));
                    }
                    insc.setACI(JANumberFormatter.deQuote(((ArrayList<String>) acI).get(i)));
                    insc.setACII(JANumberFormatter.deQuote(((ArrayList<String>) acII).get(i)));
                    if (!JadeStringUtil.isBlankOrZero(((ArrayList<String>) anneeInsc).get(i))) {
                        insc.setAnneeInsc(((ArrayList<String>) anneeInsc).get(i));
                    } else {
                        insc.setAnneeInsc(getAnnee());
                    }
                    // insc.setACII(JANumberFormatter.deQuote((String)((ArrayList)acII).get(i)));
                    insc.setMontantAf(JANumberFormatter.deQuote(((ArrayList<String>) montantAF).get(i)));
                    insc.setSoumis(((ArrayList<String>) soumis).get(i).equals("true") ? new Boolean(true)
                            : new Boolean(false));
                    insc.setCodeCanton(((ArrayList<String>) codeCanton).get(i));

                    insc.setCategoriePerso(((ArrayList<String>) categoriePerso).get(i));
                    try {
                        insc.update(transaction);
                    } catch (Exception e) {
                        _addError(transaction, getSession().getLabel("ERREUR_AJOUT_INSCRITPION"));
                    }
                    // pour le rafraichissement des données à l'écran
                    saveAcI.add(new FWCurrency(insc.getACI()).toStringFormat());
                    saveAcII.add(new FWCurrency(insc.getACII()).toStringFormat());
                    saveAF.add(new FWCurrency(insc.getMontantAf()).toStringFormat());
                    // saveSoumis.add((String)((ArrayList)soumis).get(i));
                    saveSoumis.add(insc.getSoumis().booleanValue() ? "true" : "false");
                    savePeriodeDebut.add(!JadeStringUtil.isIntegerEmpty(insc.getMontant()) ? insc.getPeriodeDebut()
                            : insc.getPeriodeDebutAF());
                    savePeriodeFin.add(!JadeStringUtil.isIntegerEmpty(insc.getMontant()) ? insc.getPeriodeFin() : insc
                            .getPeriodeFinAF());
                    saveIdInscription.add(insc.getIdInscription());
                    saveCasSpecial.add(insc.getCasSpecial().booleanValue() ? "true" : "false");
                    saveNomPrenom.add(insc.getNomPrenom());
                    saveGre.add(!JadeStringUtil.isBlankOrZero(insc.getGenreEcriture()) ? insc.getGenreEcriture() : "");
                    saveCodeCanton.add(insc.getCodeCanton());
                    saveId.add(insc.getIdInscription());
                    saveNumeroAvs.add(insc.getNssFormateWithoutPrefix());
                    saveNNSS.add(insc.getNumeroAvsNNSS());
                    saveDescription.add(insc.getEtatFormate());
                    saveIsRa.add(insc.isRAString());
                    saveCategoriePerso.add(insc.getCategoriePerso());
                    saveAnnee.add(insc.getAnneeInsc());
                    if (!JadeStringUtil.isBlankOrZero(insc.getGenreEcriture())) {
                        if (insc.getGenreEcriture().startsWith("1") && (insc.getGenreEcriture().length() == 2)) {
                            FWCurrency mon = new FWCurrency(insc.getMontant());
                            mon.negate();
                            saveMontant.add(mon.toStringFormat());
                        } else {
                            saveMontant.add(new FWCurrency(insc.getMontant()).toStringFormat());
                        }

                    } else {
                        saveMontant.add(new FWCurrency(insc.getMontant()).toStringFormat());
                    }
                    if (transaction.hasErrors()) {
                        Collection<String> saveNumWithoutPrefixe = new ArrayList<String>();
                        // en cas d'erreur, il faut réafficher le NSS sans préfixe
                        for (int j = 0; j < numeroAvs.size(); j++) {
                            String noAvs = ((ArrayList<String>) numeroAvs).get(j);
                            String noAvsNNSS = ((ArrayList<String>) numeroAvsNNSS).get(j);
                            if ("true".equalsIgnoreCase(noAvsNNSS)) {
                                saveNumWithoutPrefixe.add(NSUtil.formatWithoutPrefixe(noAvs, true));
                            } else if ("false".equalsIgnoreCase(noAvsNNSS)) {
                                saveNumWithoutPrefixe.add(NSUtil.formatWithoutPrefixe(noAvs, false));
                            }

                        }
                        numeroAvs = null;
                        numeroAvs = saveNumWithoutPrefixe;
                        StringBuffer errs = new StringBuffer();
                        errs.append(insc.getNssFormate() + ":");
                        errs.append("\n");
                        errs.append(transaction.getErrors());
                        transaction.clearErrorBuffer();
                        transaction.addErrors(errs.toString());
                        break;
                    }

                }
            }

        }
        // Les valeurs qui peuvent être mises à jour par le bentity doivent être rafraichie
        if (!transaction.hasErrors()) {
            acI = null;
            acI = saveAcI;
            acII = null;
            acII = saveAcII;
            montant = null;
            montant = saveMontant;
            montantAF = null;
            montantAF = saveAF;
            idInscription = null;
            idInscription = saveIdInscription;
            periodeDebut = null;
            periodeDebut = savePeriodeDebut;
            periodeFin = null;
            periodeFin = savePeriodeFin;
            casSpecial = null;
            casSpecial = saveCasSpecial;
            nomPrenom = null;
            nomPrenom = saveNomPrenom;
            genreEcriture = null;
            genreEcriture = saveGre;
            idDepart = null;
            idDepart = saveId;
            numeroAvs = null;
            numeroAvs = saveNumeroAvs;
            isRA = null;
            isRA = saveIsRa;
            nnss = null;
            nnss = new ArrayList<String>();
            categoriePerso = null;
            categoriePerso = saveCategoriePerso;
            anneeInsc = null;
            anneeInsc = saveAnnee;
            // On parcours le NNSS, pour connaître la value, pour le rechargement correct du taglib
            for (int i = 0; i < DSInscriptionsIndividuellesListeViewBean.MAX_INSC_PAR_VALIDATION; i++) {
                if (i < saveNNSS.size()) {
                    String temp = ((ArrayList<String>) saveNNSS).get(i);
                    if (!JadeStringUtil.isBlank(temp)) {
                        nnss.add(temp);
                    } else {
                        nnss.add(GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).getProperty(
                                "nsstag.defaultdisplay.newnss"));
                    }
                } else {
                    nnss.add(GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).getProperty(
                            "nsstag.defaultdisplay.newnss"));
                }

            }
            codeCanton = null;
            codeCanton = new ArrayList<String>();

            // On parcourt les cantons pour remettre le canton par défaut
            for (int i = 0; i < DSInscriptionsIndividuellesListeViewBean.MAX_INSC_PAR_VALIDATION; i++) {
                if (i < saveCodeCanton.size()) {
                    String temp = ((ArrayList<String>) saveCodeCanton).get(i);
                    if (!JadeStringUtil.isBlank(temp)) {
                        codeCanton.add(temp);
                    } else {
                        codeCanton.add(getCodeCantonAF());
                    }
                } else {
                    codeCanton.add(getCodeCantonAF());
                }
            }
            soumis = null;
            soumis = new ArrayList<String>();
            // On parcourt les cantons pour remettre le canton par défaut
            for (int i = 0; i < DSInscriptionsIndividuellesListeViewBean.MAX_INSC_PAR_VALIDATION; i++) {
                if (i < saveSoumis.size()) {
                    String temp = ((ArrayList<String>) saveSoumis).get(i);
                    if (!JadeStringUtil.isBlank(temp)) {
                        soumis.add(temp);
                    } else {
                        soumis.add("true");
                    }
                } else {
                    soumis.add("true");
                }
            }

            description = null;
            description = saveDescription;
        } else {
            transaction.rollback();
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            journal = new CIJournal();
            journal.setSession((BSession) getSessionCI(getSession()));
            journal.setIdJournal(getIdJournal());
            journal.retrieve();
            if (!journal.isNew()) {
                journal.setTotalControle(getTotalControleDS());
                journal.updateInscription(transaction);

            } else {
                // setIdJournal("");
            }
        }
        // Mise à jour des totaux AC et AF
        calculeTotauxAcAf();

    }

    private void _clear() {
        numeroAvs = new ArrayList<String>();
        idInscription = new ArrayList<String>();
        periodeDebut = new ArrayList<String>();
        periodeFin = new ArrayList<String>();
        montant = new ArrayList<String>();
        remarqueCont = new ArrayList<String>();
        acI = new ArrayList<String>();
        acII = new ArrayList<String>();
        anneeInsc = new ArrayList<String>();
        montantAF = new ArrayList<String>();
        codeCanton = new ArrayList<String>();
        soumis = new ArrayList<String>();
        nomPrenom = new ArrayList<String>();
        idDepart = new ArrayList<String>();
        casSpecial = new ArrayList<String>();
        genreEcriture = new ArrayList<String>();
        isCI = new ArrayList<String>();
        isRA = new ArrayList<String>();
        nnss = new ArrayList<String>();
        description = new ArrayList<String>();
        categoriePerso = new ArrayList<String>();
    }

    public void _initialise() throws Exception {
        if (getAffiliation().hasRightAccesSecurity()) {
            mgr.setSession(getSession());
            mgr.setForIdDeclaration(getIdDeclaration());
            mgr.setFromNumeroAvs(fromNumeroAvs);
            mgr.setFromNomPrenom(getFromNomPrenom());
            mgr.setATraiter(aTraiter);
            mgr.setIsAvertissement(forAvertissement);
            mgr.setForMontantSigne(forMontantSigne);
            mgr.setForMontantSigneValue(forMontantSigneValue);

            mgr.setTri(getTri());
            mgr.setDateProdNNSS(DSUtil.isNNSSActif(getSession(),
                    String.valueOf((new Integer(super.getAnnee()).intValue() + 1))));
            mgr.find();
            fillContainer();

            // Si declaration de salaire principale
            // On regarde si on trouve des salaires différés pour cette année
            if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getTypeDeclaration())) {
                DSDeclarationListViewBean mgrForSalaireDiff = new DSDeclarationListViewBean();
                mgrForSalaireDiff.setSession(getSession());
                mgrForSalaireDiff.setForTypeDeclaration(DSDeclarationViewBean.CS_SALAIRE_DIFFERES);
                mgrForSalaireDiff.setFromAffilie(getNumeroAffilie());
                mgrForSalaireDiff.setToAffilie(getNumeroAffilie());
                mgrForSalaireDiff.setForAnnee(getAnnee());
                mgrForSalaireDiff.setForSelectionTri("3");

                if (mgrForSalaireDiff.getCount() > 0) {
                    setSalaireDifferePresent(true);
                }
            }

        } else {
            String msg = FWMessageFormat.format("ERROR User [{0}] has no right [{1}] for the action [{2}]",
                    getSession().getUserId(), "read", "dsdeclaration.inscriptions.afficher");
            setMessage(msg);
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    public void calculeTotauxAcAf() {
        DSInscriptionsIndividuellesManager inscManagerAc = new DSInscriptionsIndividuellesManager();
        inscManagerAc.setSession(getSession());
        inscManagerAc.setForIdDeclaration(getIdDeclaration());
        try {
            setTotalControleAc(inscManagerAc.getSum("TEMAI").toString());
        } catch (Exception e) {
            setTotalControleAc("0");
        }
        DSInscriptionsIndividuellesManager inscManagerAf = new DSInscriptionsIndividuellesManager();
        inscManagerAf.setSession(getSession());
        inscManagerAf.setForIdDeclaration(getIdDeclaration());
        try {
            setTotalControleAf(inscManagerAf.getSum("TEMAF").toString());
        } catch (Exception e) {
            setTotalControleAf("0");
        }
    }

    public boolean canDoNext() {
        return mgr.canDoNext();
    }

    public boolean canDoPrev() {
        return mgr.canDoPrev();
    }

    public void createLeer() {
        _clear();
        for (int i = 0; i < DSInscriptionsIndividuellesListeViewBean.MAX_INSC_PAR_VALIDATION; i++) {
            numeroAvs.add("");
            codeCanton.add(getCodeCantonAF());
            try {
                nnss.add(GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).getProperty(
                        "nsstag.defaultdisplay.newnss"));
            } catch (Exception e) {
                nnss.add("true");
            }
            soumis.add("true");
            isRA.add("false");
            remarqueCont.add("");

        }

    }

    private void fillContainer() throws Exception {
        for (int i = 0; i < DSInscriptionsIndividuellesListeViewBean.MAX_INSC_PAR_VALIDATION; i++) {
            if (i < mgr.size()) {
                DSInscriptionsIndividuelles insc = (DSInscriptionsIndividuelles) mgr.getEntity(i);
                if (!insc.hasShowRight()) {
                    String msg = FWMessageFormat.format("ERROR User [{0}] has no right [{1}] for the action [{2}]",
                            getSession().getUserId(), "read", "dsdeclaration.inscriptions.afficher");
                    setMessage(msg);
                    setMsgType(FWViewBeanInterface.ERROR);
                }
                idInscription.add(insc.getIdInscription());
                idDepart.add(insc.getIdInscription());
                periodeDebut.add(insc.getPeriodeDebut());
                periodeFin.add(insc.getPeriodeFin());
                numeroAvs.add(insc.getNssFormateWithoutPrefix());
                numeroAvsNNSS.add(insc.getNumeroAvsNNSS());
                nnss.add(insc.getNumeroAvsNNSS());
                acI.add(new FWCurrency(insc.getACI()).toStringFormat());
                acII.add(new FWCurrency(insc.getACII()).toStringFormat());
                montantAF.add(new FWCurrency(insc.getMontantAf()).toStringFormat());
                if (!JadeStringUtil.isBlankOrZero(insc.getGenreEcriture())) {
                    if (insc.getGenreEcriture().startsWith("1") && (insc.getGenreEcriture().length() == 2)) {
                        FWCurrency mon = new FWCurrency(insc.getMontant());
                        mon.negate();
                        montant.add(mon.toStringFormat());
                    } else {
                        montant.add(new FWCurrency(insc.getMontant()).toStringFormat());
                    }

                } else {
                    montant.add(new FWCurrency(insc.getMontant()).toStringFormat());
                }
                remarqueCont.add(insc.getRemarqueControle());
                soumis.add(insc.getSoumis().booleanValue() ? "true" : "false");
                casSpecial.add(insc.getCasSpecial().booleanValue() ? "true" : "false");
                nomPrenom.add(insc.getNomPrenom());
                codeCanton.add(!JadeStringUtil.isIntegerEmpty(insc.getCodeCanton()) ? insc.getCodeCanton()
                        : getCodeCantonAF());
                genreEcriture.add(!JadeStringUtil.isIntegerEmpty(insc.getGenreEcriture()) ? insc.getGenreEcriture()
                        : "");
                categoriePerso.add(insc.getCategoriePerso());
                anneeInsc.add(insc.getAnneeInsc());
                if (insc.isEcritureCI()) {
                    isCI.add("true");
                } else {
                    isCI.add("false");
                }
                isRA.add(insc.isRAString());
                description.add(insc.getEtatFormate());
            } else {
                numeroAvs.add("");
                String appValue = "false";
                try {
                    appValue = GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).getProperty(
                            "nsstag.defaultdisplay.newnss");
                    if ("false".equals(appValue)) {
                        nnss.add("false");
                        numeroAvsNNSS.add("false");
                    } else if ("true".equals(appValue)) {
                        nnss.add("true");
                        numeroAvsNNSS.add("true");
                    }
                } catch (Exception e) {
                    // TODO: faire qqchose avec ces try catch
                }
                idInscription.add("");
                periodeDebut.add("");
                periodeFin.add("");
                categoriePerso.add("");
                anneeInsc.add("");
                codeCanton.add(getCodeCantonAF());
                soumis.add("true");
                isCI.add("false");
                isRA.add("false");
                remarqueCont.add("");
            }
        }
    }

    public void findNext() {
        try {
            _clear();
            mgr.findNext();
            fillContainer();
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
    }

    public void findPrev() {
        try {
            _clear();
            mgr.findPrev();
            fillContainer();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public Collection<String> getAcI() {
        return acI;
    }

    /**
     * @return
     */
    public Collection<String> getAcII() {
        return acII;
    }

    public Collection<String> getAnneeInsc() {
        return anneeInsc;
    }

    /**
     * @return
     */
    public Boolean getATraiter() {
        return aTraiter;
    }

    /**
     * @return
     */
    public Collection<String> getCasSpecial() {
        return casSpecial;
    }

    public Collection<String> getCategoriePerso() {
        return categoriePerso;
    }

    /**
     * @return
     */
    public Collection<String> getCodeCanton() {
        return codeCanton;
    }

    public Collection<String> getDescription() {
        return description;
    }

    public Boolean getForAvertissement() {
        return forAvertissement;
    }

    public String getForMontantSigne() {
        return forMontantSigne;
    }

    public String getForMontantSigneValue() {
        return forMontantSigneValue;
    }

    public String getFromNomPrenom() {
        return fromNomPrenom;
    }

    public String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    public String getFromNumeroAvsPartial() {
        if (!JadeStringUtil.isBlank(fromNumeroAvs) && !JadeStringUtil.isBlankOrZero(fromNumeroAvsNNSS)) {
            return NSUtil.formatWithoutPrefixe(fromNumeroAvs, (new Boolean(fromNumeroAvsNNSS)).booleanValue());
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public Collection<String> getGenreEcriture() {
        return genreEcriture;
    }

    /**
     * @return
     */
    public Collection<String> getIdInscription() {
        return idInscription;
    }

    /**
     * @return
     */
    public Collection<String> getIsCI() {
        return isCI;
    }

    public Collection<String> getIsRA() {
        return isRA;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    /**
     * @return
     */
    public String getModeAjout() {
        return modeAjout;
    }

    /**
     * @return
     */
    public Collection<String> getMontant() {
        return montant;
    }

    /**
     * @return
     */
    public Collection<String> getMontantAF() {
        return montantAF;
    }

    public Collection<String> getNnss() {
        return nnss;
    }

    /**
     * @return
     */
    public Collection<String> getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return
     */
    public Collection<String> getNumeroAvs() {
        return numeroAvs;
    }

    public Collection<String> getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    /**
     * @return
     */
    public Collection<String> getPeriodeDebut() {
        return periodeDebut;
    }

    /**
     * @return
     */
    public Collection<String> getPeriodeFin() {
        return periodeFin;
    }

    public Collection<String> getRemarqueCont() {
        return remarqueCont;
    }

    @Override
    public BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * @return
     */
    public Collection<String> getSoumis() {
        return soumis;
    }

    public String getTotalContoleJournal() {
        try {
            if ((journal != null) && !journal.isNew()) {
                return journal.getTotalControleFormat();
            } else if (!JadeStringUtil.isIntegerEmpty(getIdJournal())) {
                journal = new CIJournal();
                journal.setSession((BSession) getSessionCI(getSession()));
                journal.setIdJournal(getIdJournal());
                journal.retrieve();
                if (!journal.isNew()) {
                    return journal.getTotalControleFormat();
                }
            }
            return "0.00";
        } catch (Exception e) {
            return "0.00";
        }
    }

    public String getTotalInscritJournal() {
        try {
            if ((journal != null) && !journal.isNew()) {
                return journal.getTotalInscritFormat();
            } else if (!JadeStringUtil.isIntegerEmpty(getIdJournal())) {
                journal = new CIJournal();
                journal.setSession((BSession) getSessionCI(getSession()));
                journal.setIdJournal(getIdJournal());
                journal.retrieve();
                if (!journal.isNew()) {
                    return journal.getTotalInscritFormat();
                }
            }
            return "0.00";
        } catch (Exception e) {
            return "0.00";
        }
    }

    public boolean isHasShowRight() {
        return hasShowRight;
    }

    public boolean isSalaireDifferePresent() {
        return isSalaireDifferePresent;
    }

    public String notIn(AFAssurance assurance) {
        String[] codeExcluAC;
        String notIn = "";
        try {
            codeExcluAC = assurance.getListExclusionsCatPers("31.12." + anneeInsc);
            for (int j = 0; j < codeExcluAC.length; j++) {
                if (j == codeExcluAC.length - 1) {
                    notIn = notIn + codeExcluAC[j];
                } else {
                    notIn = notIn + codeExcluAC[j] + ",";
                }

            }
        } catch (Exception ex) {
            _addError(null, getSession().getLabel("INSCIND_NOTIN")
                    + " : DSInscriptionsIndividuellesListeViewBean.notIn(AFAssurance assurance)");
        }
        return notIn;
    }

    /**
     * @param collection
     */
    public void setAcI(Collection<String> collection) {
        acI = collection;
    }

    /**
     * @param collection
     */
    public void setAcII(Collection<String> collection) {
        acII = collection;
    }

    public void setAnneeInsc(Collection<String> anneeInsc) {
        this.anneeInsc = anneeInsc;
    }

    /**
     * @param boolean1
     */
    public void setATraiter(Boolean boolean1) {
        aTraiter = boolean1;
    }

    /**
     * @param collection
     */
    public void setCasSpecial(Collection<String> collection) {
        casSpecial = collection;
    }

    public void setCategoriePerso(Collection<String> categoriePerso) {
        this.categoriePerso = categoriePerso;
    }

    /**
     * @param collection
     */
    public void setCodeCanton(Collection<String> collection) {
        codeCanton = collection;
    }

    public void setDescription(Collection<String> description) {
        this.description = description;
    }

    public void setForAvertissement(Boolean forAvertissement) {
        this.forAvertissement = forAvertissement;
    }

    public void setForMontantSigne(String forMontantSigne) {
        this.forMontantSigne = forMontantSigne;
    }

    public void setForMontantSigneValue(String forMontantSigneValue) {
        this.forMontantSigneValue = forMontantSigneValue;
    }

    public void setFromNomPrenom(String fromNomPrenom) {
        this.fromNomPrenom = fromNomPrenom;
    }

    public void setFromNumeroAvs(String fromNumeroAvs) {
        this.fromNumeroAvs = fromNumeroAvs;
    }

    /**
     * @param collection
     */
    public void setGenreEcriture(Collection<String> collection) {
        genreEcriture = collection;
    }

    public void setHasShowRight(boolean hasShowRight) {
        this.hasShowRight = hasShowRight;
    }

    /**
     * @param collection
     */
    public void setIdInscription(Collection<String> collection) {
        idInscription = collection;
    }

    /**
     * @param collection
     */
    public void setIsCI(Collection<String> collection) {
        isCI = collection;
    }

    public void setIsRA(Collection<String> isRA) {
        this.isRA = isRA;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = string;
    }

    /**
     * @param string
     */
    public void setModeAjout(String string) {
        modeAjout = string;
    }

    /**
     * @param collection
     */
    public void setMontant(Collection<String> collection) {
        montant = collection;
    }

    /**
     * @param collection
     */
    public void setMontantAF(Collection<String> collection) {
        montantAF = collection;
    }

    public void setNnss(Collection<String> nnss) {
        this.nnss = nnss;
    }

    /**
     * @param collection
     */
    public void setNomPrenom(Collection<String> collection) {
        nomPrenom = collection;
    }

    /**
     * @param collection
     */
    public void setNumeroAvs(Collection<String> collection) {
        numeroAvs = collection;
    }

    public void setNumeroAvsNNSS(Collection<String> numeroAvsNNSS) {
        this.numeroAvsNNSS = numeroAvsNNSS;
    }

    /**
     * @param collection
     */
    public void setPeriodeDebut(Collection<String> collection) {
        periodeDebut = collection;
    }

    /**
     * @param collection
     */
    public void setPeriodeFin(Collection<String> collection) {
        periodeFin = collection;
    }

    public void setRemarqueCont(Collection<String> remarqueCont) {
        this.remarqueCont = remarqueCont;
    }

    public void setSalaireDifferePresent(boolean isSalaireDifferePresent) {
        this.isSalaireDifferePresent = isSalaireDifferePresent;
    }

    /**
     * @param collection
     */
    public void setSoumis(Collection<String> collection) {
        soumis = collection;
    }

}
