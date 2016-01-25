package globaz.ij.module;

import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.api.prononces.IIJSituationProfessionnelle;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prononces.IJRevenu;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRCalcul;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui permet de fusionner toutes les composantes salariales d'une situation professionnelle en un seul
 * montant touche a une certaine frequence.
 * </p>
 * 
 * <p>
 * La periodicite du salaire retournee par cette classe sera toujours celle du salaire si celle-ci a ete renseignee.
 * </p>
 * 
 * <p>
 * Si le flag allocationMax est coche, ce filtre retourne le montant du salaire qui donne droit a une allocation
 * maximale pour une ij.
 * </p>
 * 
 * @author vre
 */
public class IJSalaireFilter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final double FACTEUR_4_SEMAINES = 28.0;
    private static final double FACTEUR_ANNUEL = 360.0;
    private static final double FACTEUR_HEBDOMADAIRE = 7.0;
    private static final double FACTEUR_MENSUEL = 30.0;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    String csPeriodiciteSalaire;
    String montantSalaire;
    String nombreHeuresSemaine;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJSalaireFilter pour le revenu en question.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param revenu
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJSalaireFilter(BSession session, IJRevenu revenu) throws Exception {
        csPeriodiciteSalaire = revenu.getCsPeriodiciteRevenu();
        montantSalaire = revenu.getRevenu();
        nombreHeuresSemaine = revenu.getNbHeuresSemaine();
    }

    /**
     * Crée une nouvelle instance de la classe IJSalaireFilter pour la situation professionnelle en question.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param sitPro
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             si le montant donnant l'allocation max est introuvable
     */
    public IJSalaireFilter(BSession session, IJSituationProfessionnelle sitPro) throws Exception {
        if (sitPro.getAllocationMax().booleanValue()) {
            csPeriodiciteSalaire = IIJSituationProfessionnelle.CS_MENSUEL;
            montantSalaire = session.getApplication().getProperty(IJApplication.PROPERTY_SALAIRE_ALLOCATION_MAX);
            nombreHeuresSemaine = "";
        } else {
            // salaire de base
            csPeriodiciteSalaire = sitPro.getCsPeriodiciteSalaire();
            montantSalaire = sitPro.getSalaire();
            nombreHeuresSemaine = sitPro.getNbHeuresSemaine();

            // fusionner le salaire autreRemuneration
            if (!JadeStringUtil.isDecimalEmpty(sitPro.getAutreRemuneration())) {

                if (sitPro.getPourcentAutreRemuneration().booleanValue()) {
                    sitPro.setCsPeriodiciteAutreRemuneration(csPeriodiciteSalaire);
                }

                fusionnerSalaire(sitPro.getAutreRemuneration(), sitPro.getCsPeriodiciteAutreRemuneration(),
                        sitPro.getSalaire(), sitPro.getPourcentAutreRemuneration().booleanValue());
            }

            // fusionner le salaire en nature
            if (!JadeStringUtil.isDecimalEmpty(sitPro.getSalaireNature())) {
                fusionnerSalaire(sitPro.getSalaireNature(), sitPro.getCsPeriodiciteSalaireNature(), "", false);
            }
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * fusionne le salaire transmis avec celui que cette classe devra retourner.
     * 
     * <p>
     * le salaire que cette classe doit retourner peut ne pas avoir encore ete renseigne.
     * </p>
     * 
     * @param montant
     *            le montant a fusionner
     * @param csPeriodicite
     *            la periodicite du montant a fusionner
     * @param salairePrincipal
     *            le salaire principal a utiliser pour calcule le pourcentage
     * @param pourcentSalairePrincipal
     *            vrai si montant est une valeur de pourcentage du salaire principal
     */
    private void fusionnerSalaire(String montant, String csPeriodicite, String salairePrincipal,
            boolean pourcentSalairePrincipal) {
        if (JadeStringUtil.isDecimalEmpty(montantSalaire)) {
            /*
             * si le salaire a retourner est encore nul, il est plus simple d'ecraser tout a part si c'est un
             * pourcentage auquel cas le montant reste nul
             */
            if (!pourcentSalairePrincipal) {
                montantSalaire = montant;
                csPeriodiciteSalaire = csPeriodicite;
            }
        } else {
            // on commence par obtenir un montant si on a un pourcentage
            if (pourcentSalairePrincipal) {
                montant = String.valueOf(PRCalcul.pourcentage100(salairePrincipal, montant));
            }

            // on adapte le montant en fonction de la periodicite voulue
            if (JadeStringUtil.isIntegerEmpty(csPeriodiciteSalaire)) {
                csPeriodiciteSalaire = csPeriodicite;
            } else {
                montant = this.promouvoirSalaire(montant, csPeriodicite, csPeriodiciteSalaire);
            }

            // on additionne le montant
            montantSalaire = String.valueOf(Double.parseDouble(montantSalaire) + Double.parseDouble(montant));
        }
    }

    /**
     * getter pour l'attribut cs periodicite salaire
     * 
     * @return la valeur courante de l'attribut cs periodicite salaire
     */
    public String getCsPeriodiciteSalaire() {
        return csPeriodiciteSalaire;
    }

    /**
     * getter pour l'attribut montant salaire
     * 
     * @return la valeur courante de l'attribut montant salaire
     */
    public String getMontantSalaire() {
        return String.valueOf(montantSalaire);
    }

    /**
     * retourne le salaire arrondi (precision 0.01, 2 decimales et arrondi au plus proche)
     * 
     * @see JANumberFormatter#format(double, double, int, char)
     * 
     * @return la valeur courante de l'attribut montant salaire
     */
    public String getMontantSalaireArrondi() {
        return this.getMontantSalaireArrondi(0.01, 2, JANumberFormatter.NEAR);
        // on arrondi pas ! on envoie ce qu'on a saisi
        // return montantSalaire;
    }

    /**
     * retourne le salaire arrondi
     * 
     * @see JANumberFormatter#format(double, double, int, char)
     * 
     * @param precision
     *            la precision pour l'arrondi
     * @param decimales
     *            le nombre de decimales apres la virgule
     * @param method
     *            la methode d'arrondi (JANumberFormatter.NEAR, ...)
     * 
     * @return la valeur courante de l'attribut montant salaire
     */
    public String getMontantSalaireArrondi(double precision, int decimales, char method) {
        return JANumberFormatter.round(montantSalaire, precision, decimales, JANumberFormatter.NEAR);
    }

    /**
     * getter pour l'attribut nombre heures semaines
     * 
     * @return la valeur courante de l'attribut nombre heures semaines
     */
    public String getNombreHeuresSemaines() {
        return nombreHeuresSemaine;
    }

    /**
     * transforme un montant journalier en un montant avec une periodicite quelconque.
     * 
     * @param montantJournalier
     *            le montant journalier
     * @param csPeriodicite
     *            la periodicite voulue
     * 
     * @return
     */
    private String mtJournalierToSpecial(String montantJournalier, String csPeriodicite) {
        // si la periodicite voulue est journaliere on ne fait rien
        if (IIJSituationProfessionnelle.CS_JOURNALIER.equals(csPeriodicite)) {
            return montantJournalier;
        }

        // sinon
        double retValue = Double.parseDouble(montantJournalier);

        if (IIJSituationProfessionnelle.CS_4_SEMAINES.equals(csPeriodicite)) {
            retValue *= IJSalaireFilter.FACTEUR_4_SEMAINES;
        } else if (IIJSituationProfessionnelle.CS_ANNUEL.equals(csPeriodicite)) {
            retValue *= IJSalaireFilter.FACTEUR_ANNUEL;
        } else if (IIJSituationProfessionnelle.CS_HEBDOMADAIRE.equals(csPeriodicite)) {
            retValue *= IJSalaireFilter.FACTEUR_HEBDOMADAIRE;
        } else if (IIJSituationProfessionnelle.CS_HORAIRE.equals(csPeriodicite)) {
            // la valeur du nombre d'heures par semaine a forcement ete
            // renseignee lorsque l'on appelle cette fonction
            retValue /= Double.parseDouble(nombreHeuresSemaine);
        } else if (IIJSituationProfessionnelle.CS_MENSUEL.equals(csPeriodicite)) {
            retValue *= IJSalaireFilter.FACTEUR_MENSUEL;
        }

        return String.valueOf(retValue);
    }

    /**
     * transforme un montant quelconque en un montant journalier
     * 
     * @param montant
     *            le montant a transformer
     * @param csPeriodicite
     *            la periodicite du montant a transformer
     * 
     * @return
     */
    private String mtSpecialToJournalier(String montant, String csPeriodicite) {
        // si la periodicite est deja journaliere on ne fait rien
        if (IIJSituationProfessionnelle.CS_JOURNALIER.equals(csPeriodicite)) {
            return montant;
        }

        // sinon
        double retValue = Double.parseDouble(montant);

        if (IIJSituationProfessionnelle.CS_4_SEMAINES.equals(csPeriodicite)) {
            retValue /= IJSalaireFilter.FACTEUR_4_SEMAINES;
        } else if (IIJSituationProfessionnelle.CS_ANNUEL.equals(csPeriodicite)) {
            retValue /= IJSalaireFilter.FACTEUR_ANNUEL;
        } else if (IIJSituationProfessionnelle.CS_HEBDOMADAIRE.equals(csPeriodicite)) {
            retValue /= IJSalaireFilter.FACTEUR_HEBDOMADAIRE;
        } else if (IIJSituationProfessionnelle.CS_HORAIRE.equals(csPeriodicite)) {
            // la valeur du nombre d'heures par semaine a forcement ete
            // renseignee lorsque l'on appelle cette fonction
            retValue *= Double.parseDouble(nombreHeuresSemaine);
        } else if (IIJSituationProfessionnelle.CS_MENSUEL.equals(csPeriodicite)) {
            retValue /= IJSalaireFilter.FACTEUR_MENSUEL;
        }

        return String.valueOf(retValue);
    }

    /**
     * transforme le salaire pour qu'il ait la periodicite voulue.
     * 
     * <p>
     * Note: lors d'une transformation vers un salaire horaire, si le nombre d'heures par semaine n'est pas renseigne au
     * prealable, il est fixe a 40h.
     * </p>
     * 
     * @param csPeriodiciteVoulue
     *            DOCUMENT ME!
     */
    public void promouvoirSalaire(String csPeriodiciteVoulue) {
        if (csPeriodiciteSalaire.equals(csPeriodiciteVoulue)) {
            return; // on ne fait rien
        }

        if (IIJSituationProfessionnelle.CS_HORAIRE.equals(csPeriodiciteVoulue)) {
            nombreHeuresSemaine = "40";
        }

        montantSalaire = this.promouvoirSalaire(montantSalaire, csPeriodiciteSalaire, csPeriodiciteVoulue);
        csPeriodiciteSalaire = csPeriodiciteVoulue;
    }

    /**
     * transforme un montant d'une periodicite quelconque en une autre periodicite quelconque.
     * 
     * @param montant
     *            le montant a transformer
     * @param csPeriodiciteDepart
     *            la periodicite du montant a transformer
     * @param csPeriodiciteVoulue
     *            la periodicite finale voulue
     * 
     * @return
     */
    private String promouvoirSalaire(String montant, String csPeriodiciteDepart, String csPeriodiciteVoulue) {
        if (csPeriodiciteDepart.equals(csPeriodiciteVoulue)) {
            return montant;
        }

        return mtJournalierToSpecial(mtSpecialToJournalier(montant, csPeriodiciteDepart), csPeriodiciteVoulue);
    }
}
