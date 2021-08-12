package globaz.hercule.mappingXmlml;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.helios.tools.TimeHelper;
import globaz.hercule.db.ICEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEAttributionPts;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.db.controleEmployeur.CEControlesAEffectuer;
import globaz.hercule.db.groupement.CEMembreGroupe;
import globaz.hercule.db.groupement.CEMembreGroupeManager;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.db.reviseur.CEReviseurManager;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.process.CEListeControlesAEffectuerProcess;
import globaz.hercule.service.CEAttributionPtsService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEExcelmlUtils;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.util.AFIDEUtil;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Classe permettant la construction d'un container <b>HerculeContainer</b>
 * qui est utilisé pour la génération de la liste de sortie des contrôles à effectuer
 *
 * @author sco
 */
public class CEXmlmlMappingControlesAEffectuer {

    private static final int ANNEE_EN_ARRIERE = 3;
    private List<String> groupesCouvertureAutre;
    private List<String> groupes;
    private List<String> reviseurs;
    private Map<String, CEControlesAEffectuer> mapEntity;
    private Map<String, CEControlesAEffectuer> mapEntityForAnneePrec;
    private boolean reattributionReviseur = false;
    private CommonExcelmlContainer container;
    private CEListeControlesAEffectuerProcess processParent;

    public CEXmlmlMappingControlesAEffectuer(final Map<String, CEControlesAEffectuer> mapEntity,
            final Map<String, CEControlesAEffectuer> mapEntityForAnneePrec,
            final CEListeControlesAEffectuerProcess process, final boolean reattributionReviseur) {
        if (mapEntity == null) {
            throw new NullPointerException("La map qui contient tous les affiliés ne doit pas être null");
        }

        if (process == null) {
            throw new NullPointerException("La processus appelant ne doit pas être null");
        }

        if (process.getSession() == null) {
            throw new NullPointerException("La session du processus appelant ne doit pas être null");
        }

        this.mapEntity = mapEntity;
        this.mapEntityForAnneePrec = mapEntityForAnneePrec;
        processParent = process;
        this.reattributionReviseur = reattributionReviseur;
        container = new CommonExcelmlContainer();
    }

    /**
     * Compile les données pour construire le container
     *
     * @return
     * @throws Exception
     */
    public CommonExcelmlContainer processResults() throws Exception {
        // Info pour la progress bar
        processParent.setProgressScaleValue(mapEntity.size());

        // Ajoute les informations de l'entete de la liste
        addHeaderParameter();

        loadInfos();

        // PArcours des affiliés
        Iterator<String> it = mapEntity.keySet().iterator();
        while (it.hasNext() && !processParent.isAborted()) {
            String numeroAff = it.next();
            CEControlesAEffectuer entity = mapEntity.get(numeroAff);
            processParent.incProgressCounter();

            // On recherche le dernier contrôle si il existe.
            CEControleEmployeur dernierControle = CEUtils.rechercheDernierControle(entity.getIdAffiliation(),
                    processParent.getSession());
            CEControleEmployeur controleNonEffectuePrevu = CEUtils.rechercheDernierControleNonEffectuePrevu(
                    processParent.getSession(), processParent.getTransaction(), entity.getIdAffiliation(),
                    processParent.getAnnee());

            // On regarde qu'il ne soit pas en DS
            String anneeFinControle = null;
            String categorieMasse;

            if (dernierControle != null && !JadeStringUtil.isEmpty(dernierControle.getDateFinControle())) {
                anneeFinControle = String.valueOf(CEUtils.stringDateToAnnee(dernierControle.getDateFinControle()));
            }

            categorieMasse = findCategorieMasse(processParent.getSession(), anneeFinControle, entity.getNumAffilie(),
                    processParent.getAnneeCptr());

            if (checkSiCandidat(categorieMasse, controleNonEffectuePrevu, entity)) {

                // si appartient a un groupe dont annee couverture = année courante)

                if (!groupesCouvertureAutre.contains(entity.getIdAffiliation()) || (controleNonEffectuePrevu != null)) {

                    // Recherche si il existe un controle non effectué prévu
                    // pour une autre année
                    // => si présent, on ignore
                    if (CEUtils.rechercheControleNonEffectuePrevuForAnnee(processParent.getSession(),
                            processParent.getTransaction(), entity.getIdAffiliation(), processParent.getAnnee())
                            && (controleNonEffectuePrevu == null)) {
                        continue;
                    }

                    // ------------------------------------
                    // Information sur l'affilié
                    // ------------------------------------
                    fillContainerInfosAffilie(entity);

                    // ------------------------------------
                    // Information sur le dernier controle
                    // ------------------------------------
                    fillContainerDernierControle(dernierControle);

                    // ------------------------------------
                    // Information sur les masses
                    // ------------------------------------
                    fillContainerMasse(entity);

                    // Si c'est un controle prévu non effectué
                    if (controleNonEffectuePrevu != null) {

                        fillContainerControleNonEffectuePrevu(entity, controleNonEffectuePrevu, dernierControle);

                    } else if (!JadeStringUtil.isEmpty(entity.getIdCouverture())) {

                        // Si le contrôle provient de la couverture

                        fillContainerCouverture(entity, dernierControle);

                    } else {
                        // Peut venir de la couverture du groupe, sans ancien
                        // contrôle et sans contrôle non effectué prévu, dans ce
                        // cas
                        // 01.01.+deb. aff. - 31.12.année couverture du groupe-1

                        fillContainerCouvertureGroupe(entity, dernierControle);
                    }
                }
            }
        }

        // PArcours des affiliés pour les contrôles prévus non effectués des années précedentes
        it = mapEntityForAnneePrec.keySet().iterator();
        while (it.hasNext() && !processParent.isAborted()) {
            String numeroAff = it.next();
            CEControlesAEffectuer entity = mapEntityForAnneePrec.get(numeroAff);
            processParent.incProgressCounter();

            // On recherche le dernier contrôle si il existe.
            CEControleEmployeur dernierControle = CEUtils.rechercheDernierControle(entity.getIdAffiliation(),
                    processParent.getSession());

            // ------------------------------------
            // Information sur l'affilié
            // ------------------------------------
            fillContainerInfosAffilie(entity);

            // ------------------------------------
            // Information sur le dernier controle
            // ------------------------------------
            fillContainerDernierControle(dernierControle);

            // ------------------------------------
            // Information sur les masses
            // ------------------------------------
            fillContainerMasse(entity);

            // ------------------------------------
            // Information sur le controle prévu
            // ------------------------------------
            fillContainerControlePrevu(entity);

        }
        return container;
    }

    private void fillContainerControlePrevu(final CEControlesAEffectuer entity) throws Exception {

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(processParent.getSession());
        manager.setForControleEmployeurId(entity.getIdControle());
        manager.find(BManager.SIZE_USEDEFAULT);

        if (manager.size() > 0) {
            CEControleEmployeur controle = (CEControleEmployeur) manager.getFirstEntity();

            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DATE_PREVUE, controle.getDatePrevue(), "01.01."
                    + (Integer.valueOf(processParent.getAnnee()).intValue()));
            container.put(ICEListeColumns.PREMIERE_PERIODE, controle.getDateDebutControle());

            container.put(ICEListeColumns.DERNIERE_PERIODE, controle.getDateFinControle());

            if (!JadeStringUtil.isEmpty(controle.getControleurVisa())) {
                container.put(ICEListeColumns.REVISEUR, controle.getControleurVisa());
            } else {

                if (reattributionReviseur && (controle != null) && reviseurs.contains(controle.getControleurVisa())) {
                    container.put(ICEListeColumns.REVISEUR, controle.getControleurVisa());
                } else {
                    container.put(ICEListeColumns.REVISEUR, "");
                }
            }

            container.put(ICEListeColumns.TYPE_CONTROLE,
                    processParent.getSession().getCodeLibelle(controle.getGenreControle()));

        }

    }

    /**
     * retourne vrai si
     * l'affilié est de catégorie supèrieur à 1
     * ou si il y a un contrôle prévu non effectué
     * ou si il fait partie d'un groupe
     *
     * @param categorieMasse
     * @param controleNonEffectuePrevu
     * @param entity
     * @return
     */
    private boolean checkSiCandidat(final String categorieMasse, final CEControleEmployeur controleNonEffectuePrevu,
            final CEControlesAEffectuer entity) {
        return categorieMasse.equals(ICEControleEmployeur.CATEGORIE_MASSE_2)
                || categorieMasse.equals(ICEControleEmployeur.CATEGORIE_MASSE_3)
                || categorieMasse.equals(ICEControleEmployeur.CATEGORIE_MASSE_4) || (controleNonEffectuePrevu != null)
                || groupes.contains(entity.getIdAffiliation());
    }

    private void fillContainerCouvertureGroupe(final CEControlesAEffectuer entity,
            final CEControleEmployeur dernierControle) throws JAException {
        container.put(ICEListeColumns.DATE_PREVUE,
                "01.01." + String.valueOf((Integer.valueOf(processParent.getAnnee()).intValue())));

        // BUG 7726
        if (dernierControle != null) {
            CEExcelmlUtils.remplirColumn(
                    container,
                    ICEListeColumns.PREMIERE_PERIODE,
                    "01.01."
                            + String.valueOf(Integer.valueOf(dernierControle.getDateFinControle().substring(6, 10))
                                    .intValue() + 1), "");
        } else {
            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.PREMIERE_PERIODE, entity.getDateDebutAffiliation(),
                    "");
        }
        // BUG 7726

        String dateCouvertureGroupe = CEControleEmployeurService.retrieveCouvertureGroupeWithNumAffilie(
                processParent.getSession(), entity.getIdAffiliation());
        if ((!JadeStringUtil.isEmpty(entity.getDateFinAffiliation()))
                && ((new JADate(entity.getDateFinAffiliation())).toInt() < (new JADate("31.12." + dateCouvertureGroupe))
                        .toInt())) {
            container.put(ICEListeColumns.DERNIERE_PERIODE, entity.getDateFinAffiliation());
        } else {
            if (!JadeStringUtil.isEmpty(dateCouvertureGroupe)) {
                container.put(ICEListeColumns.DERNIERE_PERIODE, "31.12." + dateCouvertureGroupe);
            } else {
                container.put(ICEListeColumns.DERNIERE_PERIODE, "");
            }
        }
        if (reattributionReviseur && (dernierControle != null)
                && reviseurs.contains(dernierControle.getControleurVisa())) {
            container.put(ICEListeColumns.REVISEUR, dernierControle.getControleurVisa());
        } else {
            container.put(ICEListeColumns.REVISEUR, "");
        }

        container.put(ICEListeColumns.TYPE_CONTROLE,
                processParent.getSession().getCodeLibelle(CEControleEmployeur.CS_GENRE_CONTROLE_PERIODIQUE));
    }

    private void fillContainerCouverture(final CEControlesAEffectuer entity, final CEControleEmployeur dernierControle)
            throws NumberFormatException, JAException {
        container.put(ICEListeColumns.DATE_PREVUE,
                "01.01." + String.valueOf((Integer.valueOf(processParent.getAnnee()).intValue())));

        if (dernierControle != null) {
            container.put(
                    ICEListeColumns.PREMIERE_PERIODE,
                    "01.01."
                            + String.valueOf(Integer.valueOf(dernierControle.getDateFinControle().substring(6, 10))
                                    .intValue() + 1));
        } else {
            container.put(ICEListeColumns.PREMIERE_PERIODE, entity.getDateDebutAffiliation());
        }

        if ((!JadeStringUtil.isEmpty(entity.getDateFinAffiliation()))
                && ((new JADate(entity.getDateFinAffiliation())).toInt() < (new JADate("31.12."
                        + String.valueOf((Integer.valueOf(processParent.getAnnee()).intValue() - 1)))).toInt())) {
            container.put(ICEListeColumns.DERNIERE_PERIODE, entity.getDateFinAffiliation());
        } else {
            container.put(ICEListeColumns.DERNIERE_PERIODE,
                    "31.12." + String.valueOf(Integer.valueOf(processParent.getAnnee()).intValue() - 1));
        }

        if (reattributionReviseur && (dernierControle != null)
                && reviseurs.contains(dernierControle.getControleurVisa())) {
            container.put(ICEListeColumns.REVISEUR, dernierControle.getControleurVisa());
        } else {
            container.put(ICEListeColumns.REVISEUR, "");
        }

        container.put(ICEListeColumns.TYPE_CONTROLE,
                processParent.getSession().getCodeLibelle(CEControleEmployeur.CS_GENRE_CONTROLE_PERIODIQUE));
    }

    private void fillContainerInfosAffilie(final CEControlesAEffectuer entity) throws HerculeException {
        // num affilié
        container.put(ICEListeColumns.NUM_AFFILIE, entity.getNumAffilie());

        // Numero IDE
        if (!JadeStringUtil.isEmpty(entity.getNumeroIDE())) {
            container.put(ICEListeColumns.NUM_IDE, AFIDEUtil.formatNumIDE(entity.getNumeroIDE()));
        } else {
            container.put(ICEListeColumns.NUM_IDE, "");
        }
        // Nom
        container.put(ICEListeColumns.NOM, entity.getNom());
        // On recherche l'adresse
        CEExcelmlUtils.renseigneAdresse(processParent.getSession(), processParent.getTypeAdresse(), container,
                entity.getIdTiers(), entity.getNumAffilie());
        // Nom du groupe
        container.put(ICEListeColumns.NOM_GROUPE, entity.getNomGroupe());
        // Affiliation
        container.put(ICEListeColumns.DEBUT_PERIODE_AFFILIATION, entity.getDateDebutAffiliation());
        // Radiation
        container.put(ICEListeColumns.FIN_PERIODE_AFFILIATION, entity.getDateFinAffiliation());
        // Branche économique
        container.put(ICEListeColumns.BRANCHE_ECONOMIQUE,
                processParent.getSession().getCodeLibelle(entity.getBrancheEconomique()));
        // Code Noga
        container.put(ICEListeColumns.CODE_NOGA, processParent.getSession().getCodeLibelle(entity.getCodeNoga()));
        // sans personnel
        if (JadeStringUtil.isEmpty(entity.getDateFinParticularite())) {
            container.put(ICEListeColumns.DATE_PARTICULARITE_SANS_PERSONNEL, entity.getDateDebutParticularite());
        } else {
            container.put(ICEListeColumns.DATE_PARTICULARITE_SANS_PERSONNEL, entity.getDateDebutParticularite() + " - "
                    + entity.getDateFinParticularite());
        }

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.CODE_SUVA, entity.getCodeSuva(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.LIBELLE_SUVA, entity.getLibelleSuva(), "");

    }

    private void fillContainerMasse(final CEControlesAEffectuer entity) throws Exception {
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE_5, entity.getMasse5(), "0.0");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE_4, entity.getMasse4(), "0.0");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE_3, entity.getMasse3(), "0.0");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE_2, entity.getMasse2(), "0.0");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.MASSE_1, entity.getMasse1(), "0.0");

        container.put(ICEListeColumns.CI_5, entity.getNbCI5());
        container.put(ICEListeColumns.CI_4, entity.getNbCI4());
        container.put(ICEListeColumns.CI_3, entity.getNbCI3());
        container.put(ICEListeColumns.CI_2, entity.getNbCI2());
        container.put(ICEListeColumns.CI_1, entity.getNbCI1());

        if (!JadeStringUtil.isEmpty(entity.getMasse1())) {
            double masse = Double.parseDouble(entity.getMasse1());
            container.put(ICEListeColumns.CVS, CEControleEmployeurService.findCategorie(masse));
        } else {
            container.put(ICEListeColumns.CVS, "0");
        }
    }

    private void fillContainerControleNonEffectuePrevu(final CEControlesAEffectuer entity,
            final CEControleEmployeur controleNonEffectuePrevu, final CEControleEmployeur dernierControle)
            throws Exception {

        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.DATE_PREVUE, controleNonEffectuePrevu.getDatePrevue(),
                "01.01." + String.valueOf((Integer.valueOf(processParent.getAnnee()).intValue())));
        container.put(ICEListeColumns.PREMIERE_PERIODE, controleNonEffectuePrevu.getDateDebutControle());

        if ((!JadeStringUtil.isEmpty(entity.getDateFinAffiliation()))
                && ((new JADate(entity.getDateFinAffiliation())).toInt() < (new JADate(
                        controleNonEffectuePrevu.getDateFinControle())).toInt())) {
            container.put(ICEListeColumns.DERNIERE_PERIODE, entity.getDateFinAffiliation());
        } else {
            container.put(ICEListeColumns.DERNIERE_PERIODE, controleNonEffectuePrevu.getDateFinControle());
        }

        if (!JadeStringUtil.isEmpty(controleNonEffectuePrevu.getControleurVisa())) {
            container.put(ICEListeColumns.REVISEUR, controleNonEffectuePrevu.getControleurVisa());
        } else {

            if (reattributionReviseur && (dernierControle != null)
                    && reviseurs.contains(dernierControle.getControleurVisa())) {
                container.put(ICEListeColumns.REVISEUR, dernierControle.getControleurVisa());
            } else {
                container.put(ICEListeColumns.REVISEUR, "");
            }
        }

        container.put(ICEListeColumns.TYPE_CONTROLE,
                processParent.getSession().getCodeLibelle(controleNonEffectuePrevu.getGenreControle()));

    }

    private void fillContainerDernierControle(final CEControleEmployeur dernierControle) throws Exception {

        if (dernierControle != null) {

            // Date du rapport
            container.put(ICEListeColumns.DATE_DERNIER_RAPPORT, dernierControle.getDateEffective());
            // No rapport
            container.put(ICEListeColumns.NUMERO_RAPPORT, dernierControle.getRapportNumero());
            // Correction
            if (dernierControle.isErreur().booleanValue()) {
                container.put(ICEListeColumns.CORRECTION,
                        processParent.getSession().getLabel("LISTE_CONTROLE_EFFECTUER_OUI"));
            } else {
                container.put(ICEListeColumns.CORRECTION,
                        processParent.getSession().getLabel("LISTE_CONTROLE_EFFECTUER_NON"));
            }
            // Rercherche de l'attribution active en fonction du numéro d'affilié
            CEAttributionPts attributionPtsActif = CEUtils.rechercheAttributionPts(dernierControle.getNumAffilie(),
                    dernierControle.getDateDebutControle(), dernierControle.getDateFinControle(),
                    processParent.getSession());

            if (attributionPtsActif != null) {

                // Recherche la déclaration de salaires liée au contrôle
                DSDeclarationViewBean declaration = CEAttributionPtsService.chercheDeclarationDeSalaireControleEmployeurAvecIdControle(dernierControle.getId(), processParent.getSession());

                // Utilise la masse AVS trouvée sur la déclaration de salaires
                if (declaration != null) {
                    if (Float.valueOf(declaration.getMasseSalTotalWhitoutFormat()).floatValue() > 0.0) {
                        container.put(ICEListeColumns.MASSE_AVS, declaration.getMasseSalTotalWhitoutFormat());
                        container.put(ICEListeColumns.MASSE_AVS_NEG, "0.0");
                    } else {
                        container.put(ICEListeColumns.MASSE_AVS_NEG, declaration.getMasseSalTotalWhitoutFormat());
                        container.put(ICEListeColumns.MASSE_AVS, "0.0");
                    }
                // Utilise la masse trouvée sur l'attribution /!\ pour l'instant tout le temps à 0.00
                } else {
                    if (Float.valueOf(attributionPtsActif.getMasseAvs()).floatValue() > 0.0) {
                        container.put(ICEListeColumns.MASSE_AVS, attributionPtsActif.getMasseAvs());
                        container.put(ICEListeColumns.MASSE_AVS_NEG, "0.0");
                    } else {
                        container.put(ICEListeColumns.MASSE_AVS_NEG, attributionPtsActif.getMasseAvs());
                        container.put(ICEListeColumns.MASSE_AVS, "0.0");
                    }
                }
                int note1 = Integer.valueOf(
                        processParent.getSession().getCodeLibelle(attributionPtsActif.getDerniereRevision()).substring(0, 1))
                        .intValue();
                int note2 = Integer.valueOf(
                        processParent.getSession().getCodeLibelle(attributionPtsActif.getQualiteRH()).substring(0, 1))
                        .intValue();
                int note3 = Integer.valueOf(
                        processParent.getSession().getCodeLibelle(attributionPtsActif.getCollaboration()).substring(0, 1))
                        .intValue();
                int note4 = Integer.valueOf(
                        processParent.getSession().getCodeLibelle(attributionPtsActif.getCriteresEntreprise()).substring(0, 1))
                        .intValue();
                int total = note1 + note2 + note3 + note4;

                container.put(ICEListeColumns.NOTE_DERNIERE_REVISION, String.valueOf(note1));
                container.put(ICEListeColumns.NOTE_QUALITE_RH, String.valueOf(note2));
                container.put(ICEListeColumns.NOTE_COLLABORATION, String.valueOf(note3));
                container.put(ICEListeColumns.NOTE_CRITERES_SPECIAUX, String.valueOf(note4));
                container.put(ICEListeColumns.NOTE_TOTAL, String.valueOf(total));

            } else {
                fillContainerIfNotAttribution();
            }

            CEExcelmlUtils.remplirColumn(container, ICEListeColumns.TEMPS_JOURS, dernierControle.getTempsJour(), "0");

        } else {
            fillContainerIfNotControle();
        }
    }

    private List<String> createListGroupesCouvertureAutre() throws Exception {
        List<String> groupes = new ArrayList<String>();

        CEMembreGroupeManager managerGrp = new CEMembreGroupeManager();
        managerGrp.setSession(processParent.getSession());
        managerGrp.setAnneeCouvertureDifferente(CEUtils.subAnnee(processParent.getAnnee(), 1));
        managerGrp.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < managerGrp.size(); i++) {
            CEMembreGroupe groupe = (CEMembreGroupe) managerGrp.getEntity(i);
            groupes.add(groupe.getIdAffiliation());
        }

        return groupes;
    }

    private List<String> createListGroupesForAnnee() throws Exception {
        List<String> groupes = new ArrayList<String>();

        CEMembreGroupeManager managerGrp = new CEMembreGroupeManager();
        managerGrp.setSession(processParent.getSession());
        managerGrp.setForAnneeCouverture(CEUtils.subAnnee(processParent.getAnnee(), 1));
        managerGrp.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < managerGrp.size(); i++) {
            CEMembreGroupe groupe = (CEMembreGroupe) managerGrp.getEntity(i);
            groupes.add(groupe.getIdAffiliation());
        }

        return groupes;
    }

    private List<String> createListReviseur() throws Exception {
        List<String> reviseurs = new ArrayList<String>();

        CEReviseurManager manager = new CEReviseurManager();
        manager.setSession(processParent.getSession());
        manager.setFindOnlyActif(true);
        manager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            CEReviseur rev = (CEReviseur) manager.getEntity(i);
            reviseurs.add(rev.getVisa());
        }

        return reviseurs;
    }

    private void fillContainerIfNotAttribution() {
        container.put(ICEListeColumns.MASSE_AVS, "0.00");
        container.put(ICEListeColumns.MASSE_AVS_NEG, "0.00");
        container.put(ICEListeColumns.NOTE_DERNIERE_REVISION, "0");
        container.put(ICEListeColumns.NOTE_QUALITE_RH, "0");
        container.put(ICEListeColumns.NOTE_COLLABORATION, "0");
        container.put(ICEListeColumns.NOTE_CRITERES_SPECIAUX, "0");
        container.put(ICEListeColumns.NOTE_TOTAL, "0");
    }

    private void fillContainerIfNotControle() {
        container.put(ICEListeColumns.DATE_DERNIER_RAPPORT, "");
        container.put(ICEListeColumns.NUMERO_RAPPORT, "");
        container.put(ICEListeColumns.MASSE_AVS, "0.00");
        container.put(ICEListeColumns.MASSE_AVS_NEG, "0.00");
        container.put(ICEListeColumns.CORRECTION, "");
        container.put(ICEListeColumns.NOTE_DERNIERE_REVISION, "0");
        container.put(ICEListeColumns.NOTE_QUALITE_RH, "0");
        container.put(ICEListeColumns.NOTE_COLLABORATION, "0");
        container.put(ICEListeColumns.NOTE_CRITERES_SPECIAUX, "0");
        container.put(ICEListeColumns.NOTE_TOTAL, "0");
        container.put(ICEListeColumns.TEMPS_JOURS, "0");
    }

    private void addHeaderParameter() {
        // On set le header
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_ANNEE, processParent.getAnnee(), "");
        CEExcelmlUtils.remplirColumn(container, ICEListeColumns.HEADER_GENRE, processParent.getGenreControle(), "");
        CEExcelmlUtils
                .remplirColumn(container, ICEListeColumns.HEADER_ANNEE_COMPTEUR, processParent.getAnneeCptr(), "");
        container.put(ICEListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + processParent.getSession().getUserName());
        container.put(ICEListeColumns.HEADER_BLANK_1, "");
        container.put(ICEListeColumns.HEADER_BLANK_2, "");

        // Numéro inforom
        container.put(ICEListeColumns.HEADER_NUM_INFOROM, CEListeControlesAEffectuerProcess.NUMERO_INFOROM);

        String anneeCptr = processParent.getAnneeCptr();
        container.put(ICEListeColumns.HEADER_MASSE_1, processParent.getSession()
                .getLabel("LISTE_CONTROLE_HEADER_MASSE") + " " + CEUtils.getAnneePrecedente(0, anneeCptr));
        container.put(ICEListeColumns.HEADER_MASSE_2, processParent.getSession()
                .getLabel("LISTE_CONTROLE_HEADER_MASSE") + " " + CEUtils.getAnneePrecedente(1, anneeCptr));
        container.put(ICEListeColumns.HEADER_MASSE_3, processParent.getSession()
                .getLabel("LISTE_CONTROLE_HEADER_MASSE") + " " + CEUtils.getAnneePrecedente(2, anneeCptr));
        container.put(ICEListeColumns.HEADER_MASSE_4, processParent.getSession()
                .getLabel("LISTE_CONTROLE_HEADER_MASSE") + " " + CEUtils.getAnneePrecedente(3, anneeCptr));
        container.put(ICEListeColumns.HEADER_MASSE_5, processParent.getSession()
                .getLabel("LISTE_CONTROLE_HEADER_MASSE") + " " + CEUtils.getAnneePrecedente(4, anneeCptr));
        container.put(ICEListeColumns.HEADER_NB_CI_1, processParent.getSession().getLabel("LISTE_CONTROLE_HEADER_NBCI")
                + " " + CEUtils.getAnneePrecedente(0, anneeCptr));
        container.put(ICEListeColumns.HEADER_NB_CI_2, processParent.getSession().getLabel("LISTE_CONTROLE_HEADER_NBCI")
                + " " + CEUtils.getAnneePrecedente(1, anneeCptr));
        container.put(ICEListeColumns.HEADER_NB_CI_3, processParent.getSession().getLabel("LISTE_CONTROLE_HEADER_NBCI")
                + " " + CEUtils.getAnneePrecedente(2, anneeCptr));
        container.put(ICEListeColumns.HEADER_NB_CI_4, processParent.getSession().getLabel("LISTE_CONTROLE_HEADER_NBCI")
                + " " + CEUtils.getAnneePrecedente(3, anneeCptr));
        container.put(ICEListeColumns.HEADER_NB_CI_5, processParent.getSession().getLabel("LISTE_CONTROLE_HEADER_NBCI")
                + " " + CEUtils.getAnneePrecedente(4, anneeCptr));

        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_CODE_SUVA,
                processParent.getSession().getLabel("LISTE_CONTROLE_HEADER_CODE_SUVA"));
        container.put(ICEListeColumns.HEADER_COLONNE_LABEL_LIBELLE_SUVA,
                processParent.getSession().getLabel("LISTE_CONTROLE_HEADER_LIBELLE_SUVA"));
    }

    private static String findCategorieMasse(final BSession session, final String anneeFinControle,
            final String numAffilie, final String anneeCompteur) throws HerculeException {

        int anneeEnArriere = ANNEE_EN_ARRIERE;

        if (anneeFinControle == null) {
            int int_annee = CEUtils.transformeStringToInt(anneeCompteur);
            if (int_annee <= 2010) {
                anneeEnArriere = 2;
            }
        }

        return CEControleEmployeurService.findCategorieMasse(session, numAffilie, anneeCompteur, anneeFinControle,
                anneeEnArriere);

    }

    private void loadInfos() throws Exception {
        // Réupération des informations sur les groupes et réviseurs
        groupesCouvertureAutre = createListGroupesCouvertureAutre();
        groupes = createListGroupesForAnnee();
        reviseurs = createListReviseur();
    }
}
