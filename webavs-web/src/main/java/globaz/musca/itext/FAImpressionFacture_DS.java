package globaz.musca.itext;

// ITEXT
import globaz.framework.util.FWCurrency;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.naos.application.AFApplication;
import globaz.osiris.api.APISection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Ce DS (DataSource) représente le data source pour les lignes de factures (afacts) Pour iText, cette classe gère
 * l'impression de la partie [detail] du document Lié au document MUSCA_BVR_LST.jasper Date de création : (10.03.2003
 * 10:37:34)<br>
 * <br>
 * Modifications liées à une première optimisation apportées par VYJ. Cette classe ne surcharge plus
 * <code>FAAfactImpressionListViewBean</code> car il n'y avait aucun besoin à la surchager, la seule méthode utilisée
 * par l'héritage était le traitement du curseur sur un statement qui de toute manière a été supprimé.<br>
 * Le chargement des données de ce datasource est maintenant fait dans la classe
 * <code>FAImpressionFactureDataSource</code> et se fait à l'aide d'un manager.
 *
 * @author: btc
 */
public class FAImpressionFacture_DS implements net.sf.jasperreports.engine.JRDataSource {

    private static final Logger LOG = LoggerFactory.getLogger(FAImpressionFacture_DS.class);
    /**
     * Permet de conserver l'index de la ligne en cours de traitement
     */
    private int _index = 0;
    private String affichageMontantInRegroupement = "false";
    private boolean afficherMasse = false;
    // Application NAOS
    private AFApplication app = null;
    /**
     * Contient l'ensemble des lignes de factures à traiter
     */
    private ArrayList<?> container = null;
    /**
     * Contient les lignes de factures regroupées si nécessaire et qui sont en cours de traitement
     */
    private ArrayList<?> enCours = null;
    /**
     * L'entête de la facture à laquelle appartiennent les lignes de factures
     */
    private FAEnteteFacture enteteFacture = null;
    private boolean passageAutorise = true;

    /**
     * @param _container
     *            Contient l'ensemble des lignes de factures liées à l'entête
     * @param _enteteFacture
     *            L'entête de facture
     */
    public FAImpressionFacture_DS(ArrayList<?> _container, FAEnteteFacture _enteteFacture) {
        super();
        container = _container;
        enteteFacture = _enteteFacture;
        try {
            app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
        } catch (Exception e) {
            LOG.error("Erreur à la création de app {}", e);
        }
        loadPropertyAffichageMontantInRegroupement();
    }

    public void resetStatus() {
        passageAutorise = true;
        afficherMasse = false;
    }

    /**
     * @param _enteteFacture
     *            L'entête de facture Constructeur utilisé si problème lors du chargement des lignes de factures
     */
    public FAImpressionFacture_DS(FAEnteteFacture _enteteFacture) {
        super();
        enteteFacture = _enteteFacture;
        container = new ArrayList<Object>();
        try {
            app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
        } catch (Exception e) {
            LOG.error("Erreur à la création de app {}", e);
        }

        loadPropertyAffichageMontantInRegroupement();
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
        if (FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL.equalsIgnoreCase(FAImpressionFacture_BVR_Doc
                .getTemplateFilename(enteteFacture))) {
            return getFieldValueForDecSal(jrField);

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
                    ((FAAfact) enCours.get(0)).setMasseFacture("");
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
                if ((afficherMasse || APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(enteteFacture
                        .getIdTypeFacture())) && isAfficheTaux()) {

                    BigDecimal taux = new BigDecimal(((FAAfact) enCours.get(0)).getTauxFacture());
                    for (int i = 1; i < enCours.size(); i++) {
                        taux = taux.add(new BigDecimal(((FAAfact) enCours.get(i)).getTauxFacture()));
                    }
                    if (taux.equals("0.00")) {
                        return "";
                    } else {
                        return app.afficheTauxParParlier() ? taux.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
                                : taux.toString();
                    }
                }
                return "";
            } else {
                if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getTauxFacture())
                        && isAfficheTaux()) {
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
                return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
            } else {
                return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
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
    };

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     * Copie de la méthode getFieldValue mais sans modification de l'état de l'objet en cours
     */
    public Object getFieldValueSansModifEtatObject(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        // VYJ : en cours de l'optimisation, une seule modification apportée,
        // suppression du contrôle si le champs doit être affiché ou pas car non
        // fonctionnel dans l'ancienne version et à ne pas traiter dans la
        // version optimisée -> Suppression de ce contrôle : if
        if (FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL.equalsIgnoreCase(FAImpressionFacture_BVR_Doc
                .getTemplateFilename(enteteFacture))) {
            //return getFieldValueForDecSal(jrField);
            return null;

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
                            //return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                            return ((FAAfact) enCours.get(0)).getLibelleRetourLigneSansModifEtatObject();
                        } else {
                            //return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                            return ((FAAfact) enCours.get(0)).getLibelleSurFactureSansModifEtatObjet(enteteFacture.getISOLangueTiers());
                        }
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                            || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                        //return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                        return ((FAAfact) enCours.get(0)).getLibelleRetourLigneSansModifEtatObject();
                    } else {
                        //return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                        return ((FAAfact) enCours.get(0)).getLibelleSurFactureSansModifEtatObjet(enteteFacture.getISOLangueTiers());
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
                            //return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                            return ((FAAfact) enCours.get(0)).getLibelleRetourLigneSansModifEtatObject();
                        } else {
                            //return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                            return ((FAAfact) enCours.get(0)).getLibelleSurFactureSansModifEtatObjet(enteteFacture.getISOLangueTiers());
                        }
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(((FAAfact) enCours.get(0)).getLibelle())
                            || !JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getLibelle())) {
                        //return ((FAAfact) enCours.get(0)).getLibelleRetourLigne();
                        return ((FAAfact) enCours.get(0)).getLibelleRetourLigneSansModifEtatObject();
                    } else {
                        //return ((FAAfact) enCours.get(0)).getLibelleSurFacture(enteteFacture.getISOLangueTiers());
                        return ((FAAfact) enCours.get(0)).getLibelleSurFactureSansModifEtatObjet(enteteFacture.getISOLangueTiers());
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

            String masseFacture = null;
            if (enCours.size() > 1) {
                if (afficherMasse) {
                    //((FAAfact) enCours.get(0)).setMasseFacture(((FAAfact) enCours.get(0)).getMasseFacture());
                    masseFacture = ((FAAfact) enCours.get(0)).getMasseFacture();
                } else {
                    //((FAAfact) enCours.get(0)).setMasseFacture("");
                    masseFacture = "";
                }
                //return new Double(new FWCurrency(((FAAfact) enCours.get(0)).getMasseFacture()).doubleValue());
                return new Double(new FWCurrency((masseFacture)).doubleValue());
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
                if ((afficherMasse || APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(enteteFacture
                        .getIdTypeFacture())) && isAfficheTaux()) {

                    BigDecimal taux = new BigDecimal(((FAAfact) enCours.get(0)).getTauxFacture());
                    for (int i = 1; i < enCours.size(); i++) {
                        taux = taux.add(new BigDecimal(((FAAfact) enCours.get(i)).getTauxFacture()));
                    }
                    if (taux.equals("0.00")) {
                        return "";
                    } else {
                        return app.afficheTauxParParlier() ? taux.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
                                : taux.toString();
                    }
                }
                return "";
            } else {
                if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getTauxFacture())
                        && isAfficheTaux()) {
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
                //((FAAfact) enCours.get(0)).setMontantFacture(JANumberFormatter.deQuote(superMontant.toString()));
                //return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
                return new Double(JANumberFormatter.deQuote(superMontant.toString()));
            } else {
                return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
            }
        }
        if (jrField.getName().equals("COL_7_DEBUT")) {
            if (enCours.size() > 1) {
                String periodeClone = "";
                String periode = "";
                passageAutorise = true;
                for (int i = 1; i < enCours.size(); i++) {
                    if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(i)).getAnneeCotisation())) {
                        if (!JadeStringUtil.isBlankOrZero(((FAAfact) enCours.get(i)).getDebutPeriode())
                                && !JadeStringUtil.isBlankOrZero(((FAAfact) enCours.get(i)).getFinPeriode())) {
                            periode = ((FAAfact) enCours.get(i)).getDebutPeriode().substring(0, 5) + "."
                                    + ((FAAfact) enCours.get(i)).getAnneeCotisation();
                            periodeClone = ((FAAfact) enCours.get(i - 1)).getDebutPeriode().substring(0, 5) + "."
                                    + ((FAAfact) enCours.get(i - 1)).getAnneeCotisation();
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
                            return ((FAAfact) enCours.get(0)).getDebutPeriode().substring(0, 5) + "."
                                    + ((FAAfact) enCours.get(0)).getAnneeCotisation();
                    } else {
                        return ((FAAfact) enCours.get(0)).getAnneeCotisation();
                    }
                }
            }
        }
        if (jrField.getName().equals("COL_7_FIN")) {
            if (enCours.size() > 1) {
                String periodeClone = "";
                String periode = "";
                passageAutorise = true;
                for (int i = 1; i < enCours.size(); i++) {
                    if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(i)).getAnneeCotisation())) {
                        if (!JadeStringUtil.isBlankOrZero(((FAAfact) enCours.get(i)).getDebutPeriode())
                                && !JadeStringUtil.isBlankOrZero(((FAAfact) enCours.get(i)).getFinPeriode())) {
                            periode = ((FAAfact) enCours.get(i)).getFinPeriode().substring(0, 5) + "."
                                    + ((FAAfact) enCours.get(i)).getAnneeCotisation();
                            periodeClone = ((FAAfact) enCours.get(i - 1)).getFinPeriode().substring(0, 5) + "."
                                    + ((FAAfact) enCours.get(i - 1)).getAnneeCotisation();
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
                            return ((FAAfact) enCours.get(0)).getFinPeriode().substring(0, 5) + "."
                                    + ((FAAfact) enCours.get(0)).getAnneeCotisation();
                    } else {
                        return ((FAAfact) enCours.get(0)).getAnneeCotisation();
                    }
                }
            }
        }
        return null;
    };

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
                if (afficherMasse || "true".equalsIgnoreCase(affichageMontantInRegroupement)) {
                    if (!((FAAfact) enCours.get(0)).getMontantInitial().equals("")
                            && !(Objects.isNull(((FAAfact) enCours.get(0)).getMontantInitial()))) {
                        FWCurrency montant = new FWCurrency(((FAAfact) enCours.get(0)).getMontantInitial());
                        for (int i = 1; i < enCours.size(); i++) {
                            if (!((FAAfact) enCours.get(i)).getMontantInitial().equals("")
                                    && !(Objects.isNull(((FAAfact) enCours.get(i)).getMontantInitial()))) {
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
                if (afficherMasse || "true".equalsIgnoreCase(affichageMontantInRegroupement)) {
                    if (!((FAAfact) enCours.get(0)).getMontantDejaFacture().equals("")
                            && !(Objects.isNull(((FAAfact) enCours.get(0)).getMontantDejaFacture()))) {
                        FWCurrency montant = new FWCurrency(((FAAfact) enCours.get(0)).getMontantDejaFacture());
                        for (int i = 1; i < enCours.size(); i++) {
                            if (!((FAAfact) enCours.get(i)).getMontantDejaFacture().equals("")
                                    && !(Objects.isNull(((FAAfact) enCours.get(i)).getMontantDejaFacture()))) {
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
                if (afficherMasse && isAfficheTaux()) {
                    BigDecimal taux = new BigDecimal(((FAAfact) enCours.get(0)).getTauxFacture());
                    for (int i = 1; i < enCours.size(); i++) {
                        if (!((FAAfact) enCours.get(i)).getTauxFacture().equals("")
                                && !(Objects.isNull(((FAAfact) enCours.get(i)).getTauxFacture()))) {
                            taux = taux.add(new BigDecimal(((FAAfact) enCours.get(i)).getTauxFacture()));
                        }
                    }
                    if (taux.equals("0.00")) {
                        return "";
                    } else {
                        return app.afficheTauxParParlier() ? taux.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
                                : taux.toString();
                    }
                }
                return "";
            } else {
                if (!JadeStringUtil.isBlank(((FAAfact) enCours.get(0)).getTauxFacture())
                        && isAfficheTaux()) {
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
                return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
            } else {
                return new Double(JANumberFormatter.deQuote(((FAAfact) enCours.get(0)).getMontantFacture()));
            }
        }
        if (jrField.getName().equals("COL_7")) {
            if (enCours.size() > 1) {

                if (!afficherMasse && "true".equalsIgnoreCase(affichageMontantInRegroupement)) {
                    return null;
                }

                for (int i = 1; i < enCours.size(); i++) {
                    if (afficherMasse) {
                        ((FAAfact) enCours.get(0)).setMasseInitiale(((FAAfact) enCours.get(i)).getMasseInitiale());
                    } else {
                        ((FAAfact) enCours.get(0)).setMasseInitiale("");
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

    private void loadPropertyAffichageMontantInRegroupement() {
        try {
            affichageMontantInRegroupement = GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                    .getProperty("affichageMontantInRegroupement", "false");
        } catch (Exception e) {
            LOG.warn("unable to read property affichageMontantInRegroupement : {}", e.getMessage());
            affichageMontantInRegroupement = "false";
        }
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.<br>
     * <br>
     * VYJ : Quelques changements par rapport à la version originale, permet d'initialiser les variables de context
     * iteratif (enCours et _index).<br>
     * <br>
     * Retourne <code>true</code> si il y a encore des lignes à traiter, <code>false</code> sinon
     */
    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        boolean hasNext = _index < container.size();
        if (hasNext) {
            enCours = (ArrayList<?>) container.get(_index);
            _index++;
        }
        return hasNext;
    }

    /**
     * Méthode qui permet d'afficher un taux en fonction d'un paramètre activé
     * WEBAVS-7173 : Affichage du taux moyen
     *
     * @return Boolean
     */

    private Boolean isAfficheTaux() {
        return ((FAAfact) enCours.get(0)).getAffichtaux().booleanValue() || ((app.afficheTauxParParlier()) && !Objects.isNull(((FAAfact) enCours.get(0)).getTauxFacture()));
    }

    public ArrayList<?> getEnCours() {
        return enCours;
    }

}
