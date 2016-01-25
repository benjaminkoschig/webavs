package globaz.corvus.acor.adapter.plat;

import globaz.corvus.api.ci.IRERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.corvus.vb.ci.REInscriptionCIListViewBean;
import globaz.corvus.vb.ci.REInscriptionCIViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * <p>
 * Génère le fichier CI des inscriptions CI de tous les membres de la famille du requérant. Ne prend que les
 * rassemblements 'RASSEMBLE'.
 * </p>
 * 
 * @author scr
 */
public class REFichierCIPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private class RCIContainer {

        private List idsRCI = new ArrayList();
        private int previousMotif = -1;

        public void addRCI(int motif, String idRCI) {

            if (previousMotif < 0) {
                previousMotif = motif;
                idsRCI.add(idRCI);
            } else {
                // Le motif du rci précédent est un extrait de CI.
                if (isExtrait(previousMotif)) {
                    // Le motif du rci courant est un extrait de CI.
                    if (isExtrait(motif)) {
                        previousMotif = motif;
                        return;
                    }
                    // Le motif du rci courant est un rassemblement définitif.
                    else {
                        previousMotif = motif;
                        idsRCI.add(idRCI);
                    }
                }
                // Le motif du rci précédent est un rassemblement définitif
                else {
                    if (!isExtrait(motif)) {
                        idsRCI.add(idRCI);
                    }
                }
            }
        }

        public void addRCIAdditionnel(int motif, String idRCI) {
            idsRCI.add(idRCI);
        }

        public List getIdsRCI() {
            return idsRCI;
        }

        private boolean isExtrait(int motif) {
            if ((motif >= 92) && (motif <= 99)) {
                return true;
            }
            return false;
        }
    }

    private Iterator assures;

    private int idCI;
    private REInscriptionCIViewBean[] inscriptionsCI;

    private Object membre;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REFichierPeriodePrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public REFichierCIPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private REACORDemandeAdapter adapter() {
        return (REACORDemandeAdapter) parent;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {

        if (assures == null) {
            assures = adapter().membres().iterator();
        }

        if ((inscriptionsCI == null) || (idCI >= inscriptionsCI.length)) {
            // s'il n'y a plus de membre, on arrete le processus
            if (!assures.hasNext()) {
                return false;
            }

            // sinon on passe aux inscriptions CI du membre suivant.
            idCI = 0;
            inscriptionsCI = null;
            BTransaction transaction = null;
            try {

                transaction = (BTransaction) getSession().newTransaction();
                if (!transaction.isOpened()) {
                    transaction.openTransaction();
                }

                while (assures.hasNext() && (inscriptionsCI == null)) {
                    membre = assures.next();

                    // On passe au membre de famille suivant.
                    if (JadeStringUtil.isBlankOrZero(((ISFMembreFamilleRequerant) membre).getIdTiers())) {
                        continue;
                    }

                    /*
                     * CI en envoyer a ACOR :
                     * 
                     * Tous les rassemblements 'définitif', ainsi que les extraits de CI postérieur au dernier
                     * rassemblement. Si plusieurs extraits postérieur au dernier rassemblement sont trouvé, seul le
                     * plus récent sera pris. Egalement inclure tous les CI additionnels de tous les rassemblement
                     * trouvé.
                     */

                    RERassemblementCIManager mgrRCI = new RERassemblementCIManager();
                    mgrRCI.setSession(getSession());
                    mgrRCI.setForIdTiers(((ISFMembreFamilleRequerant) membre).getIdTiers());
                    mgrRCI.setForCsEtatDiffentDe(IRERassemblementCI.CS_ETAT_REVOQUE);
                    mgrRCI.setForNoDateRevocation(Boolean.TRUE);
                    mgrRCI.setForRassWithoutParent(Boolean.TRUE);
                    // Tri par date de rassemblement (plus récente à plus
                    // ancienne) et motif (ordre croissant)
                    mgrRCI.setOrderBy(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT + " DESC, "
                            + RERassemblementCI.FIELDNAME_MOTIF + " ASC ");

                    mgrRCI.find(transaction, BManager.SIZE_NOLIMIT);
                    RCIContainer rciCo = new RCIContainer();

                    // Traitement des RCI sans les CI additionnel
                    if (mgrRCI.getSize() > 0) {
                        for (Iterator<RERassemblementCI> iterator = mgrRCI.iterator(); iterator.hasNext();) {
                            RERassemblementCI rci = iterator.next();
                            // Les rassemblements sans motif ne sont pas envoyé
                            // à ACOR.
                            // Contrôler avec RJE, car avec la reprise, il y a
                            // qqes rassemblements sans motif (~3%)
                            if (JadeStringUtil.isBlankOrZero(rci.getMotif())) {
                                continue;
                            }
                            rciCo.addRCI(Integer.valueOf(rci.getMotif()).intValue(), rci.getIdRCI());
                        }
                    }

                    if (!rciCo.getIdsRCI().isEmpty()) {

                        // Rajout des CI Additionnel....
                        mgrRCI.setForRassWithoutParent(null);
                        mgrRCI.setForCIAdditionnelOnly(Boolean.TRUE);
                        List<String> idsRCICopy = new ArrayList<String>(rciCo.getIdsRCI());
                        mgrRCI.find(transaction, BManager.SIZE_NOLIMIT);
                        if (mgrRCI.getSize() > 0) {
                            for (Iterator<RERassemblementCI> iterator = mgrRCI.iterator(); iterator.hasNext();) {
                                RERassemblementCI rciAdd = iterator.next();
                                // Les rassemblements sans motif ne sont pas
                                // considéré !!!
                                if (JadeStringUtil.isBlankOrZero(rciAdd.getMotif())) {
                                    continue;
                                }
                                if (idsRCICopy.contains(rciAdd.getIdParent())) {
                                    rciCo.addRCIAdditionnel(Integer.valueOf(rciAdd.getMotif()).intValue(),
                                            rciAdd.getIdRCI());
                                }
                            }
                        }
                    }

                    JAVector rcis = new JAVector();
                    int tailleTab = 0;

                    for (Iterator<String> iterator = rciCo.getIdsRCI().iterator(); iterator.hasNext();) {
                        String idRCI = iterator.next();

                        // Récupération des inscriptions CI
                        REInscriptionCIListViewBean mgr = new REInscriptionCIListViewBean();
                        mgr.setSession(getSession());
                        mgr.setForIdRCI(idRCI);
                        mgr.setIsCITraiteExclu(true);
                        mgr.find(transaction, BManager.SIZE_NOLIMIT);

                        if (!mgr.isEmpty()) {
                            rcis.addAll(mgr.getContainer());
                            tailleTab += mgr.getSize();
                        }

                    }

                    inscriptionsCI = (REInscriptionCIViewBean[]) rcis.toArray(new REInscriptionCIViewBean[tailleTab]);

                    // Tous rassemblement de CI additionnel seront marqués comme étant traité avec la date du jour
                    String dateTraitementCIAdditionnel = JadeDateUtil.getGlobazFormattedDate(new Date());
                    for (Iterator<String> iterator = rciCo.getIdsRCI().iterator(); iterator.hasNext();) {
                        String idRCI = iterator.next();
                        RERassemblementCI rassemblementCI = new RERassemblementCI();
                        rassemblementCI.setSession(getSession());
                        rassemblementCI.setId(idRCI);
                        rassemblementCI.retrieve(transaction);
                        if (rassemblementCI.isNew()) {
                            throw new Exception("Unable to retrieve the RERassemblementCI with id [" + idRCI + "]");
                        }
                        // Pour savoir si le rassemblement contient des CI additionnel, on test l'id parent qui
                        // référence un rassemblement contenant des CI en attente de CIs additionnels
                        if (!JadeStringUtil.isBlankOrZero(rassemblementCI.getIdParent())) {
                            if (JadeStringUtil.isBlankOrZero(rassemblementCI.getDateTraitement())) {
                                // Si la date de traitement est déjà renseigné, on la conserve
                                rassemblementCI.setDateTraitement(dateTraitementCIAdditionnel);
                                rassemblementCI.update(transaction);
                            }
                        }
                    }
                    if ((inscriptionsCI != null) && (inscriptionsCI.length == 0)) {
                        inscriptionsCI = null;
                    }
                }
                if ((transaction != null) && !transaction.hasErrors() && !transaction.isRollbackOnly()) {
                    transaction.commit();
                }
            } catch (Exception e) {
                String message = getSession().getLabel("ERREUR_INSCRIPTIONS_CI_ENFANT");
                message += " Parent Exception message : " + e.getMessage();
                if (transaction != null) {
                    try {
                        transaction.rollback();
                    } catch (Exception e1) {
                        message += " Exception during transaction rollback. Error message : " + e1.getMessage();
                    }
                }
                throw new PRACORException(message, e);
            } finally {
                if (transaction != null) {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        JadeLogger.error(this, e);
                    }
                }
            }
        }
        return (inscriptionsCI != null) && (idCI < inscriptionsCI.length);
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public void printLigne(PrintWriter writer) throws PRACORException {
        REInscriptionCIViewBean inscription = inscriptionsCI[idCI++];

        // 1. le no AVS de l'assure
        this.writeAVS(writer, ((ISFMembreFamilleRequerant) membre).getNss());

        // 2. Branche économique
        this.writeEntier(writer, getSession().getCode(inscription.getBrancheEconomique()));

        // 3. Code diminution
        this.writeEntier(writer, inscription.getCodeExtourne());

        // 4. Genre cotisation
        this.writeEntier(writer, inscription.getGenreCotisation());

        // 5. Code particulier
        this.writeEntier(writer, inscription.getCodeParticulier());

        // 6. Mois début période de cotisation
        this.writeEntier(writer, inscription.getMoisDebutCotisations());

        // 7. Mois de fin de période de cotisation
        this.writeEntier(writer, inscription.getMoisFinCotisations());

        // 8. Année de cotisation
        this.writeEntier(writer, inscription.getAnneeCotisations());

        // 9. Montant du revenu en francs
        this.writeEntier(writer, inscription.getRevenu());

        // 10. Caisse + agence
        this.writeEntier(writer, inscription.getNumeroCaisse() + inscription.getNumeroAgence());

        // 11. No de relevé
        if (JadeStringUtil.isBlankOrZero(inscription.getNoAffilie())) {
            this.writeChaine(writer, "0");
        } else {
            this.writeAVS(writer, inscription.getNoAffilie());
        }

        // 12. Code amortissement
        this.writeEntier(writer, inscription.getCodeADS());

        // 13. Part. aux bonif. assistance
        this.writeEntier(writer, inscription.getPartBonifAssist());

        // 14. Code provenance
        this.writeEntier(writer, inscription.getProvenance());

        // 15. Champ spécial
        this.writeChampVide(writer);

        this.writeFinChamp(writer);
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {

        REInscriptionCIViewBean inscription = inscriptionsCI[idCI++];

        // 1. le no AVS de l'assure
        this.writeAVS(writer, ((ISFMembreFamilleRequerant) membre).getNss());

        // 2. Branche économique
        this.writeEntier(writer, getSession().getCode(inscription.getBrancheEconomique()));

        // 3. Code diminution
        this.writeEntier(writer, inscription.getCodeExtourne());

        // 4. Genre cotisation
        this.writeEntier(writer, inscription.getGenreCotisation());

        // 5. Code particulier
        this.writeEntier(writer, inscription.getCodeParticulier());

        // 6. Mois début période de cotisation
        this.writeEntier(writer, inscription.getMoisDebutCotisations());

        // 7. Mois de fin de période de cotisation
        this.writeEntier(writer, inscription.getMoisFinCotisations());

        // 8. Année de cotisation
        this.writeEntier(writer, inscription.getAnneeCotisations());

        // 9. Montant du revenu en francs
        this.writeEntier(writer, inscription.getRevenu());

        // 10. Caisse + agence
        this.writeEntier(writer, inscription.getNumeroCaisse() + inscription.getNumeroAgence());

        // 11. No de relevé
        if (JadeStringUtil.isBlankOrZero(inscription.getNoAffilie())) {
            this.writeChaine(writer, "0");
        } else {
            this.writeAVS(writer, inscription.getNoAffilie());
        }

        // 12. Code amortissement
        this.writeEntier(writer, inscription.getCodeADS());

        // 13. Part. aux bonif. assistance
        this.writeEntier(writer, inscription.getPartBonifAssist());

        // 14. Code provenance
        this.writeEntier(writer, inscription.getProvenance());

        // 15. Champ spécial

        // BZ 4574 si code cas special vide ou 00 ne rien mettre sinon mettre le
        // code cas special
        if (JadeStringUtil.isEmpty(inscription.getCodeSpecial()) || "00".equals(inscription.getCodeSpecial())) {
            this.writeChampVide(writer);
        } else {
            this.writeEntier(writer, inscription.getCodeSpecial());
        }

        this.writeFinChamp(writer);
    }

}
