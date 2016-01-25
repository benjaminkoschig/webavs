package globaz.musca.itext;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAOrdreRegroupement;
import globaz.musca.db.facturation.FAOrdreRegroupementManager;
import globaz.musca.translation.CodeSystem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.JRException;

public class FANewImpressionFacture_DS implements net.sf.jasperreports.engine.JRDataSource {

    private static final String CODE_SYS_BONIFICATION = "913001";
    private static final String CODE_SYS_COMPENSATION = "913002";
    private int _index = 0;
    private boolean afficherMasse = false;
    private boolean bloc = false;
    private ArrayList container = null;
    private ArrayList enCours = null;
    private FAEnteteFacture enteteFacture = null;
    private boolean hasChange = false;
    private Map<String, String> idOrdreRegroupementNatureMap = null;
    private boolean isTitle = false;
    private String nature = "";
    private boolean passageAutorise = true;
    private boolean setTotalToZero = false;
    private boolean sorted = false;

    private ArrayList stock = null;
    private double totaux = 0;

    public FANewImpressionFacture_DS(ArrayList _container, FAEnteteFacture _enteteFacture) {
        super();
        container = _container;
        enteteFacture = _enteteFacture;
    }

    public FANewImpressionFacture_DS(FAEnteteFacture _enteteFacture) {
        super();
        enteteFacture = _enteteFacture;
        container = new ArrayList();
    }

    public void catHasChange() throws Exception {
        String nat = getNatureFromRegroupement(enCours);
        if (JadeStringUtil.isBlankOrZero(nat)) {
            hasChange = false;
        } else {
            if (nature.equals(nat)) {
                hasChange = false;
            } else {
                nature = nat;
                hasChange = true;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    @Override
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        // VYJ : en cours de l'optimisation, une seule modification apportée,
        // suppression du contrôle si le champs doit être affiché ou pas car non
        // fonctionnel dans l'ancienne version et à ne pas traiter dans la
        // version optimisée -> Suppression de ce contrôle : if
        // (!entity.isNonImprimable().booleanValue()) {
        if (FANewImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL.equalsIgnoreCase(FANewImpressionFacture_BVR_Doc
                .getTemplateFilename(enteteFacture))) {
            return getFieldValueForDecSal(jrField);

        }

        try {
            catHasChange();
        } catch (Exception e1) {
            throw new JRException(e1.toString());
        }

        if ((((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers())).equals("BP")) {
            isTitle = true;
            if (jrField.getName().equals("COL_1")) {
                try {
                    return CodeSystem.getLibelle(((FAAfact) enCours.get(0)).getSession(),
                            FANewImpressionFacture_DS.CODE_SYS_BONIFICATION);
                } catch (Exception e) {
                    throw new JRException(e.toString());
                }
            }
        }

        if ((((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers())).equals("CR")) {
            isTitle = true;
            if (jrField.getName().equals("COL_1")) {
                try {
                    return CodeSystem.getLibelle(((FAAfact) enCours.get(0)).getSession(),
                            FANewImpressionFacture_DS.CODE_SYS_COMPENSATION);
                } catch (Exception e) {
                    throw new JRException(e.toString());
                }
            }
        }

        if (jrField.getName().equalsIgnoreCase("COL_X")) {
            if (isTitle) {
                if (bloc) {
                    bloc = false;
                } else {
                    bloc = true;
                }
                return new Boolean(bloc);
            } else {
                return new Boolean(bloc);
            }
        }

        if (jrField.getName().equals("COL_0")) {
            if (_index < stock.size()) {
                ArrayList liste = (ArrayList) stock.get(_index);
                String n;
                try {
                    n = getNatureFromRegroupement(liste);
                } catch (Exception e) {
                    throw new JRException(e.toString());
                }
                if (!nature.equals(n) && !(JadeStringUtil.isIntegerEmpty(nature) && JadeStringUtil.isIntegerEmpty(n))) {
                    // si les deux sont vide,on les considère comme égaux, même si l'un vaut null et l'autre 0
                    setTotalToZero = true;
                    return new Double(new FWCurrency(totaux).doubleValue());
                } else {
                    return null;
                }
            } else {
                setTotalToZero = true;
                return new Double(new FWCurrency(totaux).doubleValue());
            }
        }

        // retourne chaque champ
        if (jrField.getName().equals("COL_ID")) {
            return new Integer(_index);
        }
        if (((FAAfact) enCours.get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION)
                || ((FAAfact) enCours.get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
            if (jrField.getName().equals("COL_1B")) {
                if (enCours.size() > 1) {
                    for (int i = 1; i < enCours.size(); i++) {
                        if (!((FAAfact) enCours.get(i - 1)).getMasseFacture().equals("")
                                && !((FAAfact) enCours.get(i)).getMasseFacture().equals("")) {
                            double masseClone = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i - 1)).getMasseFacture(), "'", ""));
                            double masse = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i)).getMasseFacture(), "'", ""));
                            if ((masseClone == masse) && passageAutorise) {
                                afficherMasse = true;
                            } else {
                                afficherMasse = false;
                                passageAutorise = false;
                            }
                        }
                    }
                    return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                } else if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getOrdreRegroupement())) {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture
                            .getISOLangueTiers()))) {
                        return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                    } else {
                        if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                                || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                            FAAfact afact = (FAAfact) enCours.get(0);
                            String annee = afact.getAnneeCotisation();
                            String periode = afact.getDebutPeriode() + afact.getFinPeriode();
                            String montantDeja = afact.getMontantDejaFacture();
                            String montantInit = afact.getMontantInitial();

                            /*
                             * Si les 3 colonnes suivantes sont vides, le libelle peut "spanner" et avoir plus de 38
                             * car. sans \n"
                             */
                            return (JadeStringUtil.isBlank(annee + periode + montantDeja + montantInit)) ? afact
                                    .getLibelle() : afact.getLibelleRetourLigne();
                        } else {

                        }
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                            || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                        return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                    } else {
                        return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                    }
                }
            }
        } else {
            if (jrField.getName().equals("COL_1")) {
                if (enCours.size() > 1) {
                    for (int i = 1; i < enCours.size(); i++) {
                        if (!((FAAfact) enCours.get(i - 1)).getMasseFacture().equals("")
                                && !((FAAfact) enCours.get(i)).getMasseFacture().equals("")) {
                            double masseClone = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i - 1)).getMasseFacture(), "'", ""));
                            double masse = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i)).getMasseFacture(), "'", ""));
                            if ((masseClone == masse) && passageAutorise) {
                                afficherMasse = true;
                            } else {
                                afficherMasse = false;
                                passageAutorise = false;
                            }
                        }
                    }
                    return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                } else if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getOrdreRegroupement())) {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture
                            .getISOLangueTiers()))) {
                        return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                    } else {
                        if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                                || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                            return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                        } else {
                            return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                        }
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                            || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                        return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                    } else {
                        return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                    }
                }
            }
        }
        if (jrField.getName().equals("COL_2")) {
            if (enCours.size() > 1) {
                FWCurrency montantIni = new FWCurrency(((FAAfact) enCours.get(0)).getMontantInitial());
                for (int i = 1; i < enCours.size(); i++) {
                    montantIni.add(new FWCurrency(((FAAfact) enCours.get(i)).getMontantInitial()));
                }

                return new Double(montantIni.doubleValue());
            } else {
                return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMontantInitial()).doubleValue());
                // masse initiale (base)
            }
        }
        if (jrField.getName().equals("COL_3")) {
            if (enCours.size() > 1) {
                FWCurrency montantDeja = new FWCurrency(((FAAfact) enCours.get(0)).getMontantDejaFacture());
                for (int i = 1; i < enCours.size(); i++) {
                    montantDeja.add(new FWCurrency(((FAAfact) enCours.get(i)).getMontantDejaFacture()));
                }
                return new Double(montantDeja.doubleValue());
            } else {
                return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMontantDejaFacture()).doubleValue());
                // acompte
            }
        }
        if (jrField.getName().equals("COL_4")) {

            if (enCours.size() > 1) {
                if (afficherMasse) {
                    ((FAAfact) enCours.get(0)).setMasseFacture(((FAAfact) enCours.get(0)).getMasseFacture());
                } else {
                    ((FAAfact) enCours.get(0)).setMasseFacture(" ");
                }
                return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMasseFacture()).doubleValue());
            } else {
                if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getMasseFacture())) {
                    return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMasseFacture()).doubleValue());
                    // base
                } else {
                    return null;
                }
            }
        }
        if (jrField.getName().equals("COL_5")) {
            if (enCours.size() > 1) {
                if (afficherMasse && ((FAAfact) enCours.get(0)).getAffichtaux().booleanValue()) {
                    BigDecimal taux = new BigDecimal(((FAAfact) enCours.get(0)).getTauxFacture());
                    for (int i = 1; i < enCours.size(); i++) {
                        taux = taux.add(new BigDecimal(((FAAfact) enCours.get(i)).getTauxFacture()));
                    }
                    if (taux.equals("0.00")) {
                        return "";
                    } else {
                        return taux.toString();
                    }
                }
                return "";
            } else {
                if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getTauxFacture())
                        && ((FAAfact) enCours.get(0)).getAffichtaux().booleanValue()) {
                    if (((FAAfact) enCours.get(0)).getTauxFacture().equals("0.00")) {
                        return "";
                    } else {
                        return ((FAAfact) enCours.get(0)).getTauxFacture();
                    }
                } else {
                    return null;
                }
            }
        }
        if (jrField.getName().equals("COL_6")) {
            if (enCours.size() > 1) {
                FWCurrency montant = new FWCurrency();
                FWCurrency superMontant = new FWCurrency();
                for (int i = 0; i < enCours.size(); i++) {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(i)).getMontantFacture())) {
                        montant = new FWCurrency(((FAAfact) enCours.get(i)).getMontantFacture());
                    }
                    superMontant.add(montant);
                }
                ((FAAfact) enCours.get(0)).setMontantFacture(JANumberFormatter.deQuote(superMontant.toString()));
                if (setTotalToZero) {
                    totaux = 0.00;
                    setTotalToZero = false;
                }
                totaux += Double.parseDouble(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
                return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
            } else {
                if (setTotalToZero) {
                    totaux = 0.00;
                    setTotalToZero = false;
                }
                if (!isTitle) {
                    totaux += Double.parseDouble(JANumberFormatter.deQuote(((FAAfact) enCours.get(0))
                            .getMontantFacture()));
                    return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
                } else {
                    return null;
                }
            }
        }
        if (jrField.getName().equals("COL_7")) {
            if (enCours.size() > 1) {
                String periodeClone = "";
                String periode = "";
                passageAutorise = true;
                for (int i = 1; i < enCours.size(); i++) {
                    if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(i)).getAnneeCotisation())) {
                        if (!JadeStringUtil.isBlankOrZero(((FAAfact) enCours.get(i)).getDebutPeriode())
                                && !JadeStringUtil.isBlankOrZero(((FAAfact) enCours.get(i)).getFinPeriode())) {
                            if ((((FAAfact) enCours.get(i)).getDebutPeriode().substring(3, 5))
                                    .equals(((FAAfact) enCours.get(0)).getFinPeriode().substring(3, 5))) {
                                periode = ((FAAfact) enCours.get(i)).getFinPeriode().substring(3, 5) + "."
                                        + ((FAAfact) enCours.get(i)).getAnneeCotisation();
                                periodeClone = ((FAAfact) enCours.get(i - 1)).getFinPeriode().substring(3, 5) + "."
                                        + ((FAAfact) enCours.get(i - 1)).getAnneeCotisation();
                            } else {
                                periode = ((FAAfact) enCours.get(i)).getDebutPeriode().substring(3, 5) + "-"
                                        + ((FAAfact) enCours.get(i)).getFinPeriode().substring(3, 5) + "."
                                        + ((FAAfact) enCours.get(i)).getAnneeCotisation();
                                periodeClone = ((FAAfact) enCours.get(i - 1)).getDebutPeriode().substring(3, 5) + "-"
                                        + ((FAAfact) enCours.get(i - 1)).getFinPeriode().substring(3, 5) + "."
                                        + ((FAAfact) enCours.get(i - 1)).getAnneeCotisation();
                            }
                        } else {
                            periode = ((FAAfact) enCours.get(i)).getAnneeCotisation();
                            periodeClone = ((FAAfact) enCours.get(i - 1)).getAnneeCotisation();
                        }
                    }
                    if (periodeClone.equals(periode) && passageAutorise) {
                        afficherMasse = true;
                    } else {
                        afficherMasse = false;
                        passageAutorise = false;
                    }
                }
                if (afficherMasse) {
                    return periode;
                } else {
                    return "";
                }
            } else {
                if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getAnneeCotisation())) {
                    if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getDebutPeriode())
                            && !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getFinPeriode())) {
                        if ((((FAAfact) enCours.get(0)).getDebutPeriode().substring(3, 5)).equals(((FAAfact) enCours
                                .get(0)).getFinPeriode().substring(3, 5))) {
                            return ((FAAfact) enCours.get(0)).getFinPeriode().substring(3, 5) + "."
                                    + ((FAAfact) enCours.get(0)).getAnneeCotisation();
                        } else {
                            return ((FAAfact) enCours.get(0)).getDebutPeriode().substring(3, 5) + "-"
                                    + ((FAAfact) enCours.get(0)).getFinPeriode().substring(3, 5) + "."
                                    + ((FAAfact) enCours.get(0)).getAnneeCotisation();
                        }
                    } else {
                        return ((FAAfact) enCours.get(0)).getAnneeCotisation();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    public Object getFieldValueForDecSal(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        // VYJ : en cours de l'optimisation, une seule modification apportée,
        // suppression du contrôle si le champs doit être affiché ou pas car non
        // fonctionnel dans l'ancienne version et à ne pas traiter dans la
        // version optimisée -> Suppression de ce contrôle : if
        // (!entity.isNonImprimable().booleanValue()) {

        try {
            catHasChange();
        } catch (Exception e1) {
            throw new JRException(e1.toString());
        }

        if ((((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers())).equals("BP")) {
            isTitle = true;
            if (jrField.getName().equals("COL_1")) {
                try {
                    return CodeSystem.getLibelle(((FAAfact) enCours.get(0)).getSession(),
                            FANewImpressionFacture_DS.CODE_SYS_BONIFICATION);
                } catch (Exception e) {
                    throw new JRException(e.toString());
                }
            }
        }

        if ((((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers())).equals("CR")) {
            isTitle = true;
            if (jrField.getName().equals("COL_1")) {
                try {
                    return CodeSystem.getLibelle(((FAAfact) enCours.get(0)).getSession(),
                            FANewImpressionFacture_DS.CODE_SYS_COMPENSATION);
                } catch (Exception e) {
                    throw new JRException(e.toString());
                }
            }
        }

        if (jrField.getName().equalsIgnoreCase("COL_X")) {
            if (isTitle) {
                if (bloc) {
                    bloc = false;
                } else {
                    bloc = true;
                }
                return new Boolean(bloc);
            } else {
                return new Boolean(bloc);
            }
        }

        if (jrField.getName().equals("COL_0")) {
            if (_index < stock.size()) {
                ArrayList liste = (ArrayList) stock.get(_index);
                String n;
                try {
                    n = getNatureFromRegroupement(liste);
                } catch (Exception e) {
                    throw new JRException(e.toString());
                }
                if (!nature.equals(n) && !(JadeStringUtil.isIntegerEmpty(nature) && JadeStringUtil.isIntegerEmpty(n))) {
                    // si les deux sont vide,on les considère comme égaux, même si l'un vaut null et l'autre 0
                    setTotalToZero = true;
                    return new Double(new FWCurrency(totaux).doubleValue());
                } else {
                    return null;
                }
            } else {
                setTotalToZero = true;
                return new Double(new FWCurrency(totaux).doubleValue());
            }
        }

        // retourne chaque champ
        if (jrField.getName().equals("COL_ID")) {
            return new Integer(_index);
        }

        if (((FAAfact) enCours.get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION)
                || ((FAAfact) enCours.get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
            if (jrField.getName().equals("COL_1B")) {
                if (enCours.size() > 1) {
                    for (int i = 1; i < enCours.size(); i++) {
                        if (!((FAAfact) enCours.get(i - 1)).getMasseInitiale().equals("")
                                && !((FAAfact) enCours.get(i)).getMasseInitiale().equals("")) {
                            double masseClone = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i - 1)).getMasseInitiale(), "'", ""));
                            double masse = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i)).getMasseInitiale(), "'", ""));
                            if ((masseClone == masse) && passageAutorise) {
                                afficherMasse = true;
                            } else {
                                afficherMasse = false;
                                passageAutorise = false;
                            }
                        }
                    }
                    return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                } else if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getOrdreRegroupement())) {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture
                            .getISOLangueTiers()))) {
                        return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                    } else {
                        if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                                || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                            return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                        } else {
                            return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                        }
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                            || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                        return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                    } else {
                        return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                    }
                }
            }
        } else {
            if (jrField.getName().equals("COL_1")) {
                if (enCours.size() > 1) {
                    for (int i = 1; i < enCours.size(); i++) {
                        if (!((FAAfact) enCours.get(i - 1)).getMasseInitiale().equals("")
                                && !((FAAfact) enCours.get(i)).getMasseInitiale().equals("")) {
                            double masseClone = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i - 1)).getMasseInitiale(), "'", ""));
                            double masse = Double.parseDouble(JadeStringUtil.change(
                                    ((FAAfact) enCours.get(i)).getMasseInitiale(), "'", ""));
                            if ((masseClone == masse) && passageAutorise) {
                                afficherMasse = true;
                            } else {
                                afficherMasse = false;
                                passageAutorise = false;
                            }
                        }
                    }
                    return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                } else if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getOrdreRegroupement())) {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture
                            .getISOLangueTiers()))) {
                        return ((FAAfact) enCours.get(0)).getLibelleOrdre(enteteFacture.getISOLangueTiers());
                    } else {
                        if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                                || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                            return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                        } else {
                            return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                        }
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                            || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                        return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                    } else {
                        return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                    }
                }
            }
        }
        if (jrField.getName().equals("COL_2")) {
            if (enCours.size() > 1) {
                if (afficherMasse) {
                    if (!((FAAfact) enCours.get(0)).getMontantInitial().equals("")
                            && !((FAAfact) enCours.get(0)).getMontantInitial().equals(null)) {
                        FWCurrency montant = new FWCurrency(((FAAfact) enCours.get(0)).getMontantInitial());
                        for (int i = 1; i < enCours.size(); i++) {
                            if (!((FAAfact) enCours.get(i)).getMontantInitial().equals("")
                                    && !((FAAfact) enCours.get(i)).getMontantInitial().equals(null)) {
                                montant.add(new FWCurrency(((FAAfact) enCours.get(i)).getMontantInitial()));
                            }
                        }

                        return new Double(montant.doubleValue());
                    } else {
                        return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMontantInitial()).doubleValue());
                    }
                }
                return null;
            } else {
                if (!((FAAfact) enCours.get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_TABLEAU)) {
                    return null;
                } else {
                    return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMontantInitial()).doubleValue());
                }
            }
        }
        if (jrField.getName().equals("COL_3")) {
            if (enCours.size() > 1) {
                if (afficherMasse) {
                    if (!((FAAfact) enCours.get(0)).getMontantDejaFacture().equals("")
                            && !((FAAfact) enCours.get(0)).getMontantDejaFacture().equals(null)) {
                        FWCurrency montant = new FWCurrency(((FAAfact) enCours.get(0)).getMontantDejaFacture());
                        for (int i = 1; i < enCours.size(); i++) {
                            if (!((FAAfact) enCours.get(i)).getMontantDejaFacture().equals("")
                                    && !((FAAfact) enCours.get(i)).getMontantDejaFacture().equals(null)) {
                                montant.add(new FWCurrency(((FAAfact) enCours.get(i)).getMontantDejaFacture()));
                            }
                        }
                        return new Double(montant.doubleValue());
                    } else {
                        return new Double(
                                new FWCurrency(((FAAfact) enCours.get(0)).getMontantDejaFacture()).doubleValue());
                    }
                }
                return null;
            } else {
                if (!((FAAfact) enCours.get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_TABLEAU)) {
                    return null;
                } else {
                    return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMontantDejaFacture()).doubleValue());
                }
            }
        }
        if (jrField.getName().equals("COL_5")) {
            if (enCours.size() > 1) {
                if (afficherMasse && ((FAAfact) enCours.get(0)).getAffichtaux().booleanValue()) {
                    BigDecimal taux = new BigDecimal(((FAAfact) enCours.get(0)).getTauxFacture());
                    for (int i = 1; i < enCours.size(); i++) {
                        if (!((FAAfact) enCours.get(i)).getTauxFacture().equals("")
                                && !((FAAfact) enCours.get(i)).getTauxFacture().equals(null)) {
                            taux = taux.add(new BigDecimal(((FAAfact) enCours.get(i)).getTauxFacture()));
                        }
                    }
                    if (taux.equals("0.00")) {
                        return "";
                    } else {
                        return taux.toString();
                    }
                }
                return "";
            } else {
                if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getTauxFacture())
                        && ((FAAfact) enCours.get(0)).getAffichtaux().booleanValue()) {
                    if (((FAAfact) enCours.get(0)).getTauxFacture().equals("0.00")) {
                        return "";
                    } else {
                        return ((FAAfact) enCours.get(0)).getTauxFacture();
                    }
                } else {
                    return null;
                }
            }
        }
        if (jrField.getName().equals("COL_6")) {
            if (enCours.size() > 1) {
                double montant = 0;
                double superMontant = 0;
                for (int i = 0; i < enCours.size(); i++) {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(i)).getMontantFacture())) {
                        montant = Double.parseDouble(JadeStringUtil.change(
                                ((FAAfact) enCours.get(i)).getMontantFacture(), "'", ""));
                    }
                    superMontant = superMontant + montant;
                }
                ((FAAfact) enCours.get(0)).setMontantFacture(String.valueOf(superMontant));
                if (setTotalToZero) {
                    totaux = 0.00;
                    setTotalToZero = false;
                }
                // totaux += Double.parseDouble(((FAAfact)
                // enCours.get(0)).getMontantFacture());
                totaux += new FWCurrency(((FAAfact) enCours.get(0)).getMontantFacture()).doubleValue();
                return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMontantFacture()).doubleValue());
            } else {
                if (setTotalToZero) {
                    totaux = 0.00;
                    setTotalToZero = false;
                }
                if (!isTitle) {
                    totaux += Double.parseDouble(JANumberFormatter.deQuote(((FAAfact) enCours.get(0))
                            .getMontantFacture()));
                    return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
                } else {
                    return null;
                }
                // totaux += Double.parseDouble(((FAAfact)
                // enCours.get(0)).getMontantFacture());
                // return new Double(new FWCurrency(((FAAfact)
                // enCours.get(0)).getMontantFacture()).doubleValue());
            }
        }

        if (jrField.getName().equals("COL_7")) {
            if (enCours.size() > 1) {
                for (int i = 1; i < enCours.size(); i++) {
                    if (afficherMasse) {
                        ((FAAfact) enCours.get(0)).setMasseInitiale(((FAAfact) enCours.get(i)).getMasseInitiale());
                    } else {
                        ((FAAfact) enCours.get(0)).setMasseInitiale(" ");
                    }
                }
                return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMasseInitiale()).doubleValue());
            } else {
                if (!((FAAfact) enCours.get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_TABLEAU)) {
                    return null;
                } else {
                    return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMasseInitiale()).doubleValue());
                }

            }
        }
        return null;
    }

    public String getNatureFromRegroupement(ArrayList afact) throws Exception {
        String nature = "";
        try {
            FAAfact af = (FAAfact) afact.get(0);
            if ("BP".equals(af.getLibelleOrdreFr())) {
                nature = FANewImpressionFacture_DS.CODE_SYS_BONIFICATION;
            } else if ("CR".equals(af.getLibelleOrdreFr())) {
                nature = FANewImpressionFacture_DS.CODE_SYS_COMPENSATION;
            } else {
                if (idOrdreRegroupementNatureMap == null) {
                    // charge le cache d'instance pour retouver la nature d'un
                    // ordre de regroupement.
                    idOrdreRegroupementNatureMap = new HashMap<String, String>();
                    FAOrdreRegroupementManager mgr = new FAOrdreRegroupementManager();
                    mgr.setSession(af.getSession());
                    mgr.find(BManager.SIZE_NOLIMIT);
                    for (Iterator it = mgr.iterator(); it.hasNext();) {
                        FAOrdreRegroupement ordre = (FAOrdreRegroupement) it.next();
                        String idOrdreRegroupement = ordre.getIdOrdreRegroupement();
                        String nat = ordre.getNature();
                        idOrdreRegroupementNatureMap.put(idOrdreRegroupement, nat);
                    }
                }
                // renvoie la nature pour cette ordre ou "" si il n'y a pas de
                // nature
                nature = idOrdreRegroupementNatureMap.get(af.getIdOrdreRegroupement());
                nature = (nature == null) ? "" : nature;
            }

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
        return nature;
    }

    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        isTitle = false;
        try {
            if (!sorted) {
                sortArray(container);
                sorted = true;
            }
        } catch (Exception e) {
            throw new JRException(e.toString());
        }
        boolean hasNext = _index < stock.size();
        if (hasNext) {
            enCours = (ArrayList) stock.get(_index);
            _index++;
        }
        return hasNext;
    }

    private FAOrdreRegroupement retrieveOrdreRegroupement(FAAfact af, String idR) throws Exception {
        FAOrdreRegroupement rgr = new FAOrdreRegroupement();
        rgr.setSession(af.getSession());
        rgr.setIdOrdreRegroupement(idR);
        rgr.retrieve();
        return rgr;
    }

    public void sortArray(ArrayList liste) throws Exception {
        int i = 0;
        Map map = new TreeMap();
        for (Iterator iter = liste.iterator(); iter.hasNext();) {
            ArrayList list1 = (ArrayList) iter.next();
            String nature = getNatureFromRegroupement(list1);

            if (JadeStringUtil.isBlankOrZero(nature)) {
                nature = String.valueOf(i);
                i++;
            } else {
                nature += String.valueOf(i);
                i++;
            }

            map.put(nature, list1);
        }

        int counter = 0;
        stock = new ArrayList(i + 2);
        boolean BPadded = false;
        boolean CRadded = false;
        for (Iterator t = map.keySet().iterator(); t.hasNext();) {
            String key = (String) t.next();

            if (!BPadded && key.startsWith(FANewImpressionFacture_DS.CODE_SYS_BONIFICATION)) {
                BPadded = true;
                ArrayList title = new ArrayList();
                FAAfact afact = new FAAfact();
                afact.setLibelleOrdreFr("BP");
                afact.setLibelleOrdreDe("BP");
                afact.setLibelleOrdreIt("BP");
                afact.setSession(enteteFacture.getSession());
                title.add(afact);
                stock.add(counter, title);
                counter++;
            }

            if (!CRadded && key.startsWith(FANewImpressionFacture_DS.CODE_SYS_COMPENSATION)) {
                CRadded = true;
                ArrayList title = new ArrayList();
                FAAfact afact = new FAAfact();
                afact.setLibelleOrdreFr("CR");
                afact.setLibelleOrdreDe("CR");
                afact.setLibelleOrdreIt("CR");
                afact.setSession(enteteFacture.getSession());
                title.add(afact);
                stock.add(counter, title);
                counter++;
            }
            ArrayList tr = (ArrayList) map.get(key);
            stock.add(counter, tr);
            counter++;
        }
    }

}
