/*
 * Créé le 20 juin 06
 */
package globaz.external.tucana;

import globaz.apg.api.alfa.IAPBouclementAlfa;
import globaz.apg.api.alfa.IAPBouclementAlfaLoader;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPPrestationLoader;
import globaz.apg.enums.APTypeDePrestation;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.itucana.constantes.ITUCSRubriqueListeDesRubriques;
import globaz.itucana.exception.TUModelInstanciationException;
import globaz.itucana.model.ITUModelBouclement;
import globaz.itucana.process.TUProcessusBouclement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.prestation.tools.PRSession;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Classe permettant de récupérer tous les données nécessaires pour la création du bouclement de Toucana Dans un premier
 * temps, on va recherche les valeurs nécessaires pour le bouclement : C'est-à-dire le montant des prestations ACM, des
 * cotisations ACM et des impôts ACM ainsi que le type de prestation et d'autres informations pour le traitement.
 * Ensuite, on va chercher le code du canton de l'affilié afin de trier ces prestations ACM par type de prestations et
 * par canton. Puis, on regroupe toutes ces informations dans une map avec le type et le canton comme clé. ATTENTION :
 * pour le calcul des nombres de cartes, seules la prestation qui est la première du droit comme une carte. Dans les
 * autres cas, toutes les informations sont prises, mais le nombre de cartes n'est pas augmenté ! Ensuite, on set le
 * bouclement avec les différentes informations retrouvées...
 * 
 * @author hpe 20.06.2006
 **/

public class APProcessBouclementAlpha extends TUProcessusBouclement {

    // Classe interne pour les infos repris de la requête pour le tri par canton
    // et type de prestations
    private class AlphaInfo {
        public String idAffilie;
        public BigDecimal montantBrut;
        public BigDecimal montantCotisations;
        public BigDecimal montantImpots;
        public int nbrCartes = 0;
        public int nbrJoursSolde = 0;
    }

    // Classe interne qui défini les clés pour le tri par canton et type de
    // prestations
    private class Key {
        public String canton;
        public String typePrestation;

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         * @param o
         *            DOCUMENT ME!
         * @return DOCUMENT ME!
         */
        public int compareTo(final Object o) {
            final Key key = (Key) o;

            if (canton.compareTo(key.canton) != 0) {
                return canton.compareTo(key.canton);
            } else if (typePrestation.compareTo(key.typePrestation) != 0) {
                return typePrestation.compareTo(key.typePrestation);
            } else {
                return 0;
            }
        }

        /**
         * @param obj
         *            DOCUMENT ME!
         * @return DOCUMENT ME!
         */
        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            final Key key = (Key) obj;
            return ((key.canton.equals(canton)) && (key.typePrestation.equals(typePrestation)));
        }

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         * @return DOCUMENT ME!
         */
        @Override
        public int hashCode() {
            return (canton + typePrestation).hashCode();
        }
    }

    /**
     * Constructeur de la classe principale
     * 
     * @param _annee
     * @param _mois
     */
    public APProcessBouclementAlpha(final String _annee, final String _mois) {
        super(_annee, _mois);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itucana.process.TUProcessusBouclement#initBouclement(globaz.itucana .model.ITUModelBouclement)
     */
    @Override
    public void initBouclement(final ITUModelBouclement bouclement) throws TUModelInstanciationException {

        String codeCanton = "";
        boolean isCarteSupp = false;

        try {
            final BISession session = PRSession.connectSession(getSession(), "APG");
            final IAPBouclementAlfaLoader bcl = (IAPBouclementAlfaLoader) session
                    .getAPIFor(IAPBouclementAlfaLoader.class);

            final IAPBouclementAlfa[] array = bcl.load(getMois(), getAnnee());

            final Map map = new Hashtable();

            for (int i = 0; i < array.length; i++) {
                final IAPBouclementAlfa elem = array[i];

                // reprendre l'affilie par l'idAffilie de la requête (voir si ça
                // passe si pas d'idTiers --> Sinon créer nouvelle méthode)
                final IAFAffiliation affilie = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
                affilie.setISession(PRSession.connectSession(session, "NAOS"));
                affilie.setAffiliationId(elem.getIdAffilie());
                affilie.retrieve(((BSession) affilie.getISession()).newTransaction());
                if (!affilie.isNew()) {
                    codeCanton = affilie.getCantonAF("01." + getMois() + "." + getAnnee());
                }

                // Création de la clé
                final Key k = new Key();
                k.canton = codeCanton;

                // Si pas de canton pour l'affilié, on y est la valeur "NULL"
                if (JadeStringUtil.isBlank(k.canton)) {
                    k.canton = "**";
                }

                k.typePrestation = elem.getType();

                // Reprendre toutes les prestations selon l'idDroit de l'element
                // en cours afin de voir si c'est la première prestation du droit
                final IAPPrestationLoader prest = (IAPPrestationLoader) session.getAPIFor(IAPPrestationLoader.class);

                // Charger toutes les prestations de type ACM et ACM2
                final IAPPrestation[] arrayPrest = prest.load(
                        elem.getIdDroit(),
                        new String[] { APTypeDePrestation.ACM_ALFA.getCodesystemString(),
                                APTypeDePrestation.ACM2_ALFA.getCodesystemString() },
                        IAPPrestation.FIELDNAME_DATEDEBUT_PRESTATION);

                if ((arrayPrest != null) && (arrayPrest.length > 0)) {
                    final IAPPrestation firstPrestation = arrayPrest[0];

                    // Si la première prestation du droit est la prestation en
                    // cours... Ajouter 1 carte...
                    if (firstPrestation.getIdPrestation().equals(elem.getIdPrestation())) {
                        isCarteSupp = true;
                    } else {
                        isCarteSupp = false;
                    }
                }

                // Si déjà même clé, alors on reprend l'existant et on remplace
                // en ajoutant
                if (map.containsKey(k)) {
                    final AlphaInfo ai = (AlphaInfo) map.get(k);
                    if (!JadeStringUtil.isBlankOrZero(elem.getMontantBrutACM())) {
                        ai.montantBrut = ai.montantBrut.add(new BigDecimal(elem.getMontantBrutACM()));
                    }

                    if (!JadeStringUtil.isBlankOrZero(elem.getNombreJoursCouvertsACM())) {
                        ai.nbrJoursSolde += Integer.parseInt(elem.getNombreJoursCouvertsACM());
                    }

                    if (!JadeStringUtil.isBlankOrZero(elem.getMontantCotisationsACM())) {
                        ai.montantCotisations = ai.montantCotisations.add(new BigDecimal(elem
                                .getMontantCotisationsACM()));
                    }

                    if (!JadeStringUtil.isBlankOrZero(elem.getMontantImpotsACM())) {
                        ai.montantImpots = ai.montantImpots.add(new BigDecimal(elem.getMontantImpotsACM()));
                    }

                    if (isCarteSupp) {
                        ai.nbrCartes++;
                    }

                    map.put(k, ai);
                }
                // Sinon on insère simplement les valeurs ! Et si la valeur est "VIDE", alors on insère "0" !
                else {
                    final AlphaInfo ai = new AlphaInfo();

                    ai.montantBrut = JadeStringUtil.isBlankOrZero(elem.getMontantBrutACM()) ? new BigDecimal(0)
                            : new BigDecimal(elem.getMontantBrutACM());
                    ai.nbrJoursSolde = JadeStringUtil.isBlankOrZero(elem.getNombreJoursCouvertsACM()) ? 0 : Integer
                            .parseInt(elem.getNombreJoursCouvertsACM());
                    ai.montantCotisations = JadeStringUtil.isDecimalEmpty(elem.getMontantCotisationsACM()) ? new BigDecimal(
                            0) : new BigDecimal(elem.getMontantCotisationsACM());
                    ai.montantImpots = JadeStringUtil.isDecimalEmpty(elem.getMontantImpotsACM()) ? new BigDecimal(0)
                            : new BigDecimal(elem.getMontantImpotsACM());

                    if (isCarteSupp) {
                        ai.nbrCartes++;
                    }

                    map.put(k, ai);
                }
            }

            final Set keys = map.keySet();
            final Iterator iter = keys.iterator();

            // Setter de l'entête
            bouclement.setEntete(getAnnee(), getMois(), getAnnee() + getMois());

            while (iter.hasNext()) {
                final Key key = (Key) iter.next();
                final AlphaInfo ai = (AlphaInfo) map.get(key);

                // Création du montant net (montant brut + montant cotisations +
                // montant impôt)
                BigDecimal montantNet = new BigDecimal(0);

                montantNet = montantNet.add(ai.montantBrut);
                montantNet = montantNet.add(ai.montantCotisations);
                montantNet = montantNet.add(ai.montantImpots);

                // Affichage des résultats pour les tests
                //
                // System.out.println("---------------------------------------------------------------------------");
                // System.out.println("Résultat pour le canton de '"+key.canton+"' et pour le type '"+key.typePrestation);
                // System.out.println(" ");
                // System.out.println("Montant des prestations ACM (net) = " + montantNet.toString());
                // System.out.println("Nombre de cartes  ACM        	  = "+Integer.toString(ai.nbrCartes));
                // System.out.println("Nombre de jours soldés ACM        = "+Integer.toString(ai.nbrJoursSolde));
                // System.out.println("---------------------------------------------------------------------------");
                // System.out.println(" ");

                // Si type = APG
                if (IAPBouclementAlfa.TYPE_APG.equals(key.typePrestation)) {

                    // Ajout de la ligne pour le montant des prestations des ACM
                    // "APG" (Montant brut - Cotisations - Impôts)
                    bouclement.addLine(key.canton,
                            ITUCSRubriqueListeDesRubriques.CS_ALLOCATIONS_COMPLEMENTAIRES_SERVICE_MILITAIRE_CAPG,
                            montantNet.toString());

                    // Ajout de la ligne pour le nombre de carte ACM "APG"
                    bouclement.addLine(key.canton,
                            ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_SERVICE_MILITAIRE_CAPG,
                            Integer.toString(ai.nbrCartes));

                    // Ajout de la ligne pour le nombre de jours soldés "APG"
                    bouclement.addLine(key.canton,
                            ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_SERVICE_MILITAIRE_CAPG,
                            Integer.toString(ai.nbrJoursSolde));

                    // Si type = Maternité (AMAT)
                } else if (IAPBouclementAlfa.TYPE_AMAT.equals(key.typePrestation)) {

                    // Ajout de la ligne pour le montant des prestations "AMAT"
                    // (Montant brut - Cotisations - Impôts)
                    bouclement.addLine(key.canton,
                            ITUCSRubriqueListeDesRubriques.CS_ALLOCATIONS_COMPLEMENTAIRES_MATERNITE_CAPG,
                            montantNet.toString());

                    // Ajout de la ligne pour le nombre de carte ACM "AMAT"
                    bouclement.addLine(key.canton, ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_MATERNITE_CAPG,
                            Integer.toString(ai.nbrCartes));

                    // Ajout de la ligne pour le nombre de jours soldés "AMAT"
                    bouclement.addLine(key.canton,
                            ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_MATERNITE_CAPG,
                            Integer.toString(ai.nbrJoursSolde));

                }
            }
        } catch (final Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }
    }
}
