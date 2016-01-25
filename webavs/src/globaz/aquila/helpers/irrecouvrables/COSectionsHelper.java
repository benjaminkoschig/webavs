/*
 * Créé le 10 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.helpers.irrecouvrables;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.irrecouvrables.CODetailSection;
import globaz.aquila.db.irrecouvrables.COPoste;
import globaz.aquila.db.irrecouvrables.COSectionsViewBean;
import globaz.aquila.helpers.irrecouvrables.process.COProcessSections;
import globaz.aquila.process.COProcessValiderIrrecouvrable;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIVPDetailMontant;
import globaz.osiris.api.APIVPPoste;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.comptes.CATauxRubriquesManager;
import globaz.osiris.db.irrecouvrable.CAKeyPosteContainer;
import globaz.osiris.db.irrecouvrable.CAPoste;
import globaz.osiris.db.irrecouvrable.CAVentilateur;
import globaz.osiris.db.ventilation.CAVPDetailMontant;
import globaz.osiris.db.ventilation.CAVPListePosteParSection;
import globaz.osiris.db.ventilation.CAVPPoste;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdre;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreManager;
import globaz.osiris.db.ventilation.CAVPVentilateur;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author vre
 */
public class COSectionsHelper extends FWHelper {
    private Map<String, CARubrique> rubriquesMap;// key=idRubrique
    private Map<String, CASection> sectionsMap;// key=idSection
    private CATauxRubriquesManager tauxRubriquesManager = null;
    private Map<String, CATauxRubriques> tauxRubriquesMap;// key=idRubrique
    private CAVPTypeDeProcedureOrdreManager typeDeProcedureOrdreManager = null;
    private Map<String, Map<String, CAVPTypeDeProcedureOrdre>> typeDeProcedureOrdreMapMap;// key=idRubrique,key2=TypeOrdre

    public COSectionsHelper() {
        super();
        typeDeProcedureOrdreMapMap = new HashMap<String, Map<String, CAVPTypeDeProcedureOrdre>>();
        tauxRubriquesMap = new HashMap<String, CATauxRubriques>();
        rubriquesMap = new HashMap<String, CARubrique>();
        sectionsMap = new HashMap<String, CASection>();
    }

    /**
     * A partir de la liste des sections choisies, appelle le service de ventilation de Osiris, ventile les montants des
     * sections et transforme le tout pour l'affichage dans l'écran de modification de la ventilation.
     * 
     * @param viewBean
     * @param action
     * @param session
     * @throws Exception
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        COSectionsViewBean sectionsViewBean = (COSectionsViewBean) viewBean;

        // initialisation du sole total des sections sélectionnées. Utilisé dans COSectionsViewBean.validerPourExecution
        FWCurrency soldeTotalSections = new FWCurrency("0");

        // création du ventilateur
        CAVPVentilateur ventilateur = new CAVPVentilateur();
        BSession sessionOsiris = createSessionOsiris((BSession) session);
        ventilateur.setSession(sessionOsiris);

        // Parcours et traitement des sections sélectionnées
        CASection section = null;
        for (String idSection : sectionsViewBean.getIdSectionsList()) {
            // l'idSection peut avoir la valeur "-1" si la section a été cochée puis décochée
            if (!JadeStringUtil.isBlank(idSection) && !"-1".equals(idSection)) {
                section = loadSection(sessionOsiris, idSection);
                soldeTotalSections.add(section.getSolde());
                ventilateur.traiteSection(section);
                ventilateur.reParcoursTableauPourPostesNegatifs(section);
            }
        }

        // this.displayTableauFinal(sessionOsiris, ventilateur);
        // System.out.println("*****************************************************************************************");
        sectionsViewBean.setSoldeTotalSections(soldeTotalSections.getBigDecimalValue());

        // Renseigne le champ idCompteAnnexe pour être sûr d'avoir le bon plus tard dans le process
        // utilise la dernière section car le compteAnnexe est identique pour toutes les sections.
        // ne devrait jamais être null car vérifié dans l'écran
        if (section != null) {
            sectionsViewBean.setIdCompteAnnexe(section.getIdCompteAnnexe());
        } else {
            throw new Exception("Il faut sélectionner au moins une section pour créer un dossier irrécouvrables");
        }

        // ventiler les montants suivant procédure de ventilation 'autre procédure'
        ventilateur.setTypeDeProcedure(APIVPPoste.CS_PROCEDURE_AUTRE_PROCEDURE);
        ventilateur.ventiler();

        // transformer et stocker le résultat dans le viewBean pour affichage dans la page
        stoquerResultatDansViewbean(sessionOsiris, sectionsViewBean, ventilateur);

        CAVentilateur nouveauVentilateur = new CAVentilateur(sessionOsiris, sectionsViewBean.getIdSectionsList());
        nouveauVentilateur.executerVentilation();
        // nouveauVentilateur.displayPostContainer();
        nouveauVentilateur.displayMontantAVentilerContainer();
        Map<CAKeyPosteContainer, CAPoste> postesMap = nouveauVentilateur.getPostesTries();

        System.out.println("--------------------------- AFFICHAGE DES POSTES ---------------------------");
        String result = "";
        for (Map.Entry<CAKeyPosteContainer, CAPoste> posteEntry : postesMap.entrySet()) {
            CAKeyPosteContainer key = posteEntry.getKey();
            CAPoste poste = posteEntry.getValue();
            result += "\nkey(ordrePriorite, numeroRubriqueIrrecouvrable, annee, , type) : " + key.getOrdrePriorite()
                    + ", " + key.getNumeroRubriqueIrrecouvrable() + ", " + key.getAnnee() + ", " + key.getType();
            result += "\n" + poste.toString();
            result += "###########################################################################";
        }
        System.out.println(result);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            // set les champs du process de validation des irrécouvrables et lance le process
            COSectionsViewBean sectionsViewBean = (COSectionsViewBean) viewBean;
            COProcessValiderIrrecouvrable processValiderIrrecouvrable = new COProcessValiderIrrecouvrable();
            processValiderIrrecouvrable.setSession((BSession) sectionsViewBean.getISession());
            processValiderIrrecouvrable.setIdSections(sectionsViewBean.getIdSectionsList());
            processValiderIrrecouvrable.setPostes(sectionsViewBean.getPostes());
            processValiderIrrecouvrable.setEMailAddress(sectionsViewBean.getEmailAddress());
            processValiderIrrecouvrable.setAnnee(sectionsViewBean.getAnnee());
            processValiderIrrecouvrable.setIdCompteAnnexe(sectionsViewBean.getIdCompteAnnexe());
            processValiderIrrecouvrable.setLibelleJournal(sectionsViewBean.getLibelleJournal());
            processValiderIrrecouvrable.setIdCaisseProfessionnelle(sectionsViewBean.getIdFirstCaisseProf());
            processValiderIrrecouvrable.setExtournerCI(sectionsViewBean.isExtourneCI());
            processValiderIrrecouvrable.setControleTransaction(true);
            processValiderIrrecouvrable.start();
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

    /**
     * @param sessionOsiris
     * @param idSection
     * @param idRubrique
     * @param detailMontant
     * @return
     * @throws Exception
     */
    private CODetailSection createDetailSection(BSession sessionOsiris, String idSection, String idRubrique,
            CAVPDetailMontant detailMontant) throws Exception {
        CASection section = loadSection(sessionOsiris, idSection);
        CARubrique rubrique = loadRubrique(sessionOsiris, idRubrique);

        CODetailSection detailSection = new CODetailSection();

        detailSection.setIdRubrique(idRubrique);
        detailSection.setIdSection(idSection);
        detailSection.setTypeOrdre(detailMontant.getTypeMontant());
        detailSection.setTaux(loadTauxRubriques(sessionOsiris, idRubrique));
        detailSection.setMontantDu(detailMontant.getMontantBase());
        detailSection.setMontantVerse(detailMontant.getMontantVentile());
        detailSection.setLibelle(section.getDescription() + " - " + rubrique.getDescription());
        detailSection.setIdExterne(section.getIdExterne());
        detailSection.setMontantParAnnee(detailMontant.getMontantParAnnee());
        detailSection.setIdCaisseProfessionnelle(section.getIdCaisseProfessionnelle());
        return detailSection;
    }

    /**
     * crée une session Osiris
     * 
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private BSession createSessionOsiris(BSession session) throws Exception {
        BSession sessionOsiris;
        sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS).newSession();
        session.connectSession(sessionOsiris);
        return sessionOsiris;
    }

    private void displayTableauFinal(BSession sessionOsiris, CAVPVentilateur ventilateur) throws Exception {
        Map<String, CAVPListePosteParSection> tableauFinal = ventilateur.getTableauFinal();
        for (Map.Entry<String, CAVPListePosteParSection> tableauFinalEntry : tableauFinal.entrySet()) {
            CAVPListePosteParSection listePosteParSection = tableauFinalEntry.getValue();

            // le format de la clé est YYYYMMDDidSection, pour récupérer l'id, on enlève les 8 premiers caractères
            String idSection = tableauFinalEntry.getKey().substring(8);
            System.out.println("#############################" + idSection
                    + "(idSection)#####################################");

            if (JadeStringUtil.isIntegerEmpty(idSection)) {
                // le tableauFinal contient une entrée pour le total, ça doit être celle-la
                System.out.println("le tableauFinal contient une entrée pour le total, ça doit être celle-la");
                continue;
            }

            // Parcours des rubriquesPrésentes puis recherche du poste correspondant
            for (String idRubrique : ventilateur.getRubriquesPresentesMap().keySet()) {
                System.out.println("-------" + idRubrique + "(idRubrique)--------");
                CAVPPoste vpPoste = listePosteParSection.getPoste(idRubrique);
                if (vpPoste == null) {
                    System.out.println("aucun poste trouvé pour la rubrique : " + idRubrique);
                    continue;
                }

                if (vpPoste.isMontantSimple()) {
                    CAVPDetailMontant detailMontantSimple = vpPoste
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);
                    System.out.println("simple : base = " + detailMontantSimple.getMontantBase() + " ventilé = "
                            + detailMontantSimple.getMontantVentile());
                    for (Map.Entry<String, BigDecimal> montantParAnneeEntry : detailMontantSimple.getMontantParAnnee()
                            .entrySet()) {
                        String annee = montantParAnneeEntry.getKey();
                        BigDecimal montant = montantParAnneeEntry.getValue();
                        System.out.println("------------------------> montant par année : " + annee + " >> "
                                + montant.toString());
                    }
                } else {
                    CAVPDetailMontant detailMontantEmployeur = vpPoste
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);
                    System.out.println("employeur : base = " + detailMontantEmployeur.getMontantBase() + " ventilé = "
                            + detailMontantEmployeur.getMontantVentile());

                    CAVPDetailMontant detailMontantSalarie = vpPoste
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);
                    System.out.println("salarié : base = " + detailMontantSalarie.getMontantBase() + " ventilé = "
                            + detailMontantSalarie.getMontantVentile());
                }
            }
        }
        System.out.println("###############################################################################");
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof COContentieux) {
            COProcessSections process = new COProcessSections();
            process.setContentieux((COContentieux) viewBean);
            process.setISession(session);

            try {
                process.executeProcess();
                if (process.getMemoryLog().hasErrors()) {
                    viewBean.setMessage(process.getMemoryLog().getMessagesInString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            } catch (Exception e) {
                viewBean.setMessage(viewBean.getMessage() + "\n" + e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return viewBean;
    }

    /**
     * retourne le poste pour la rubrique irrecouvrable correspondante à la rubrique donnée
     * 
     * @return le poste ou null si pas de rubrique irrecouvrable correspondante.
     * @throws Exception
     */
    private COPoste getPosteOfViewBeanAndCreateIfNotExist(BSession sessionOsiris, COSectionsViewBean sectionsViewBean,
            String idRubrique, String typeOrdre) throws Exception {
        CAVPTypeDeProcedureOrdre typeDeProcedureOrdre = loadTypeDeProcedureOrdre(sessionOsiris, idRubrique, typeOrdre);
        if (typeDeProcedureOrdre != null) {
            // il y a un type de procedure, donc il faut gérer cette rubrique
            String idRubriqueIrrecouvrable = typeDeProcedureOrdre.getIdRubriqueIrrecouvrable();
            COPoste poste = sectionsViewBean.getPoste(idRubriqueIrrecouvrable);

            if (poste == null) {
                // charger la rubrique pour ce type
                CARubrique rubrique = loadRubrique(sessionOsiris, idRubrique);

                // creer un nouveau poste
                poste = new COPoste(loadRubrique(sessionOsiris, idRubriqueIrrecouvrable), rubrique,
                        loadTypeDeProcedureOrdre(sessionOsiris, idRubrique, typeOrdre).getOrdre());
                sectionsViewBean.addPoste(poste);
            }

            return poste;
        } else {
            // il n'y a pas de type proc ordre, on ignore cette rubrique
            return null;
        }
    }

    /**
     * retourne la rubrique et l'enregistre dans rubriquesMap si necessaire
     * 
     * @param sessionOsiris
     * @param idRubrique
     * @return
     * @throws Exception
     */
    private CARubrique loadRubrique(BSession sessionOsiris, String idRubrique) throws Exception {
        CARubrique rubrique = rubriquesMap.get(idRubrique);

        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setIdRubrique(idRubrique);
            rubrique.setSession(sessionOsiris);
            rubrique.retrieve();

            if (rubrique.isNew()) {
                throw new Exception("Impossible de trouver la rubrique ayant l'identifiant: " + idRubrique);
            }

            rubriquesMap.put(idRubrique, rubrique);
        }

        return rubrique;
    }

    /**
     * retourne la Section et l'enregistre dans sectionsMap si necessaire
     * 
     * @param sessionOsiris
     * @param idSection
     * @return
     * @throws Exception
     */
    private CASection loadSection(BSession sessionOsiris, String idSection) throws Exception {
        CASection section = sectionsMap.get(idSection);

        if (section == null) {
            section = new CASection();
            section.setIdSection(idSection);
            section.setSession(sessionOsiris);
            section.retrieve();

            if (section.isNew()) {
                throw new Exception("Impossible de trouver la section ayant l'identifiant: " + idSection);
            }

            sectionsMap.put(idSection, section);
        }

        return section;
    }

    /**
     * retourne le tauxRubrique et l'enregistre dans tauxRubriqueMap si necessaire
     * 
     * @param sessionOsiris
     * @param idRubrique
     * @return
     * @throws Exception
     */
    private CATauxRubriques loadTauxRubriques(BSession sessionOsiris, String idRubrique) throws Exception {
        CATauxRubriques tauxRubrique = tauxRubriquesMap.get(idRubrique);

        if (tauxRubrique == null) {
            if (tauxRubriquesManager == null) {
                tauxRubriquesManager = new CATauxRubriquesManager();
                tauxRubriquesManager.setSession(sessionOsiris);
            }

            tauxRubriquesManager.setForIdRubrique(idRubrique);
            tauxRubriquesManager.find();

            if (tauxRubriquesManager.isEmpty()) {
                return null;
            } else {
                tauxRubrique = (CATauxRubriques) tauxRubriquesManager.get(0);
            }

            tauxRubriquesMap.put(idRubrique, tauxRubrique);
        }

        return tauxRubrique;
    }

    /**
     * retourne le typeDeProcedureOrdre et l'enregistre dans typeDeProcedureOrdreMapMap si necessaire retourne le
     * CAVPTypeDeProcedureOrdre pour un type ordre donné
     * 
     * @param sessionOsiris
     * @param idRubrique
     * @param typeOrdre
     * @return
     * @throws Exception
     */
    private CAVPTypeDeProcedureOrdre loadTypeDeProcedureOrdre(BSession sessionOsiris, String idRubrique,
            String typeOrdre) throws Exception {
        return loadTypeDeProcedureOrdreMap(sessionOsiris, idRubrique).get(typeOrdre);
    }

    /**
     * recherche dans les paramètres de config de Osiris pour obtenir les paramètres de configuration d'ordre de la
     * procédure autre procedure et retourne une map contenant toutes les options de configu concernant une rubrique
     * donnee.
     * 
     * retourne la map et l'enregistre dans typeDeProcedureOrdreMapMap si necessaire
     * 
     * @param sessionOsiris
     * @param idRubrique
     * @return
     * @throws Exception
     */
    private Map<String, CAVPTypeDeProcedureOrdre> loadTypeDeProcedureOrdreMap(BSession sessionOsiris, String idRubrique)
            throws Exception {
        Map<String, CAVPTypeDeProcedureOrdre> typeDeProcedureOrdreMap = typeDeProcedureOrdreMapMap.get(idRubrique);

        if (typeDeProcedureOrdreMap == null) {
            if (typeDeProcedureOrdreManager == null) {
                typeDeProcedureOrdreManager = new CAVPTypeDeProcedureOrdreManager();
                typeDeProcedureOrdreManager.setForTypeProcedure(APIVPPoste.CS_PROCEDURE_AUTRE_PROCEDURE);
                typeDeProcedureOrdreManager.setSession(sessionOsiris);
            }

            typeDeProcedureOrdreManager.setForIdRubrique(idRubrique);
            typeDeProcedureOrdreManager.find();

            if (typeDeProcedureOrdreManager.isEmpty()) {
                typeDeProcedureOrdreMap = Collections.emptyMap();
            } else {
                typeDeProcedureOrdreMap = new HashMap<String, CAVPTypeDeProcedureOrdre>();

                for (int idType = 0; idType < typeDeProcedureOrdreManager.size(); ++idType) {
                    CAVPTypeDeProcedureOrdre typeDeProcedureOrdre = (CAVPTypeDeProcedureOrdre) typeDeProcedureOrdreManager
                            .get(idType);

                    typeDeProcedureOrdreMap.put(typeDeProcedureOrdre.getTypeOrdre(), typeDeProcedureOrdre);
                }
            }

            typeDeProcedureOrdreMapMap.put(idRubrique, typeDeProcedureOrdreMap);
        }

        return typeDeProcedureOrdreMap;
    }

    /**
     * transformer et stocker le résultat dans le viewBean pour affichage dans la page
     * 
     * @param sessionOsiris
     * @param sectionsViewBean
     * @param ventilateur
     * @throws Exception
     */
    private void stoquerResultatDansViewbean(BSession sessionOsiris, COSectionsViewBean sectionsViewBean,
            CAVPVentilateur ventilateur) throws Exception {
        sectionsViewBean.reset();

        Map<String, CAVPListePosteParSection> tableauFinal = ventilateur.getTableauFinal();
        for (Map.Entry<String, CAVPListePosteParSection> tableauFinalEntry : tableauFinal.entrySet()) {
            CAVPListePosteParSection listePosteParSection = tableauFinalEntry.getValue();

            // le format de la clé est YYYYMMDDidSection, pour récupérer l'id, on enlève les 8 premiers caractères
            String idSection = tableauFinalEntry.getKey().substring(8);

            if (JadeStringUtil.isIntegerEmpty(idSection)) {
                // le tableauFinal contient une entrée pour le total, ça doit être celle-la
                continue;
            }

            for (String idRubrique : ventilateur.getRubriquesPresentesMap().keySet()) {
                CAVPPoste vpPoste = listePosteParSection.getPoste(idRubrique);
                if (vpPoste == null) {
                    continue;
                }

                if (vpPoste.isMontantSimple()) {
                    COPoste posteOfViewBean = getPosteOfViewBeanAndCreateIfNotExist(sessionOsiris, sectionsViewBean,
                            idRubrique, APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);

                    if (posteOfViewBean == null) {
                        // la rubrique courante n'est pas à considérer pour les irrécouvrables
                        continue;
                    }
                    CAVPDetailMontant detailMontant = vpPoste.getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SIMPLE);

                    // créer un detail pour cette section
                    // on ajoute dans la rubrique "employeur" qui contient la valeur simple
                    posteOfViewBean.addDetailSection(createDetailSection(sessionOsiris, idSection, idRubrique,
                            detailMontant));
                } else {
                    // ajout de la part employeurs --------------------------------------------------------
                    COPoste posteOfViewBean = getPosteOfViewBeanAndCreateIfNotExist(sessionOsiris, sectionsViewBean,
                            idRubrique, APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);

                    if (posteOfViewBean != null) {
                        // la rubrique courante est à considérer pour les irrécouvrables
                        CAVPDetailMontant detailMontant = vpPoste
                                .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR);

                        // créer un detail pour cette section et la rubrique
                        // on ajoute dans la rubrique "employeur"
                        posteOfViewBean.addDetailSection(createDetailSection(sessionOsiris, idSection, idRubrique,
                                detailMontant));
                    }

                    // ajout de la part salarie -----------------------------------------------------------
                    posteOfViewBean = getPosteOfViewBeanAndCreateIfNotExist(sessionOsiris, sectionsViewBean,
                            idRubrique, APIVPDetailMontant.CS_VP_MONTANT_SALARIE);

                    if (posteOfViewBean == null) {
                        // la rubrique courante n'est pas à considérer pour les irrécouvrables
                        continue;
                    }
                    CAVPDetailMontant detailMontant = vpPoste
                            .getDetailMontant(APIVPDetailMontant.CS_VP_MONTANT_SALARIE);

                    // on ajoute dans la rubrique "salarié"
                    posteOfViewBean.addDetailSection(createDetailSection(sessionOsiris, idSection, idRubrique,
                            detailMontant));
                }

            }
        }

        // recalculer les montants totaux
        sectionsViewBean.resetMontants();
    }
}
