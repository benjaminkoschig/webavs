package globaz.corvus.acor.parser.xml.rev10;

import globaz.commons.nss.NSUtil;
import globaz.corvus.acor.parser.xml.rev10.annonces.REAIXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REAnnonce09XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REAnnonce10XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REAnnonceXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REAyantDroitXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REBaseCalcul09XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REBaseCalcul10XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REBonification09XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REBonification10XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REEchelleXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REPrestation09XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REPrestation10XmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.RERetraiteFlexibleXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.RERevAnnuelMoyenXmlDataStructure;
import globaz.corvus.acor.parser.xml.rev10.annonces.REXmlDataContainer;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un parser capable de parser le fichier xml annonce.xml retourné par ACOR.
 * </p>
 * 
 * @author scr
 * 
 * 
 */
public class REImportAnnonceACOR extends REACORAbstractXMLFileParser {

    private static String idAnnonce41_01 = "";

    private static String idAnnonce41_02 = "";
    private static String idAnnonce44_01 = "";
    private static String idAnnonce44_02 = "";

    private static String idRA = "";
    private static String noAvsAnnonce41_01 = "";
    private static String noAvsAnnonce44_01 = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private static REAnnoncesAugmentationModification9Eme importAnnonce41_01(BSession session,
            REAnnonce09XmlDataStructure annonceXML) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();

        annonce41.setCodeApplication("41");
        annonce41.setCodeEnregistrement01("01");

        String noCaisse = annonceXML.getNoCaisseAgence().substring(0, 3);
        String noAgence = annonceXML.getNoCaisseAgence().substring(3, annonceXML.getNoCaisseAgence().length() - 1);

        annonce41.setNumeroCaisse(noCaisse);
        annonce41.setNumeroAgence(noAgence);
        annonce41.setNumeroAnnonce(annonceXML.getNoAnnonce());
        annonce41.setReferenceCaisseInterne(annonceXML.getRefInterneCaisse());

        REAyantDroitXmlDataStructure iad = annonceXML.getInfoAyantDroit();
        annonce41.setNoAssAyantDroit(NSUtil.unFormatAVS(iad.getNssAyantDroit()));
        annonce41.setPremierNoAssComplementaire(iad.getNssCmpl1());
        annonce41.setSecondNoAssComplementaire(iad.getNssCmpl2());
        annonce41.setEtatCivil(iad.getEtatCivil());
        annonce41.setIsRefugie(iad.getRefugie());
        annonce41.setCantonEtatDomicile(iad.getCodeCantonEtatDomicile());

        // Prestation

        REPrestation09XmlDataStructure iprst = annonceXML.getInfoPrestation();
        annonce41.setGenrePrestation(iprst.getGenrePrestation());
        annonce41.setDebutDroit(iprst.getDebutDroit());
        annonce41.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(iprst.getMensualite(), 5));
        annonce41.setMensualiteRenteOrdRemp(iprst.getMensualiteRenteOrdinaireRemplacee());
        annonce41.setFinDroit(iprst.getFinDroit());
        annonce41.setCodeMutation(iprst.getCodeMutation());

        annonce41.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        PRTiersWrapper tier = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(annonce41.getNoAssAyantDroit()));
        if (tier != null) {
            annonce41.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }
        return annonce41;

    }

    private static REAnnoncesAugmentationModification9Eme importAnnonce41_02(BSession session,
            REAnnonce09XmlDataStructure annonceXML) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
        annonce41.setSession(session);

        annonce41.setCodeApplication("41");
        annonce41.setCodeEnregistrement01("02");

        annonce41.setRamDeterminant(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRevAnnuelMoyen().getRam());
        annonce41.setDureeCotPourDetRAM(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRevAnnuelMoyen()
                .getDureeCotiRam());
        annonce41.setAnneeNiveau(annonceXML.getInfoPrestation().getBaseCalcul().getAnneeNiveau());
        annonce41.setRevenuPrisEnCompte(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRevAnnuelMoyen()
                .getRevenuPrisEnCompte());
        annonce41.setEchelleRente(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle().getEchelle());
        annonce41.setDureeCoEchelleRenteAv73(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiAv73());
        annonce41.setDureeCoEchelleRenteDes73(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiDes73());
        annonce41.setDureeCotManquante48_72(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiManquante48_72());
        annonce41.setAnneeCotClasseAge(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getAnneeCotiClsAge());
        annonce41.setDureeAjournement(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getDureeAjournement());
        annonce41.setSupplementAjournement(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getSupplementAjournement());
        annonce41.setDateRevocationAjournement(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getDateRevocationAjournement());

        annonce41.setIsLimiteRevenu(annonceXML.getInfoPrestation().getBaseCalcul().getLimiteRevenu());
        annonce41.setIsMinimumGaranti(annonceXML.getInfoPrestation().getBaseCalcul().getMinimumGaranti());

        annonce41.setOfficeAICompetent(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpoux().getOai());
        annonce41.setOfficeAiCompEpouse(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpouse().getOai());
        annonce41.setDegreInvalidite(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpoux()
                .getDegreInvalidite());
        annonce41.setDegreInvaliditeEpouse(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpouse()
                .getDegreInvalidite());
        annonce41.setCodeInfirmite(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpoux().getCodeInfirmite());
        annonce41.setCodeInfirmiteEpouse(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpouse()
                .getCodeInfirmite());
        annonce41.setSurvenanceEvenAssure(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpoux()
                .getSurvenanceEvenementAssure());
        annonce41.setSurvenanceEvtAssureEpouse(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpouse()
                .getSurvenanceEvenementAssure());
        annonce41.setAgeDebutInvalidite(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpoux()
                .getInvalidePrecoce());
        annonce41.setAgeDebutInvaliditeEpouse(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAIEpouse()
                .getInvalidePrecoce());
        annonce41.setReduction(annonceXML.getInfoPrestation().getReduction());

        // TODO
        String cs = annonceXML.getInfoPrestation().getCodeSpecial();
        annonce41.setCasSpecial1(cs);
        annonce41.setCasSpecial2("");
        annonce41.setCasSpecial3("");
        annonce41.setCasSpecial4("");
        annonce41.setCasSpecial5("");
        annonce41.setDureeCotManquante73_78(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiManquante73_78());

        annonce41.setRevenuAnnuelMoyenSansBTE(annonceXML.getInfoPrestation().getBaseCalcul().getInfoBonification()
                .getRamSansBTE());
        annonce41.setBteMoyennePrisEnCompte(annonceXML.getInfoPrestation().getBaseCalcul().getInfoBonification()
                .getMontantBTEMoyenne());
        annonce41.setNombreAnneeBTE(annonceXML.getInfoPrestation().getBaseCalcul().getInfoBonification()
                .getNombreAnneeBTE());

        return annonce41;

    }

    private static REAnnoncesAugmentationModification10Eme importAnnonce44_01(BSession session,
            REAnnonce10XmlDataStructure annonceXML) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setSession(session);

        annonce44.setCodeApplication("44");
        annonce44.setCodeEnregistrement01("01");

        String noCaisse = annonceXML.getNoCaisseAgence().substring(0, 3);
        String noAgence = annonceXML.getNoCaisseAgence().substring(3, annonceXML.getNoCaisseAgence().length() - 1);

        annonce44.setNumeroCaisse(noCaisse);
        annonce44.setNumeroAgence(noAgence);
        annonce44.setNumeroAnnonce(annonceXML.getNoAnnonce());
        annonce44.setReferenceCaisseInterne(annonceXML.getRefInterneCaisse());

        REAyantDroitXmlDataStructure iad = annonceXML.getInfoAyantDroit();
        annonce44.setNoAssAyantDroit(NSUtil.unFormatAVS(iad.getNssAyantDroit()));
        annonce44.setPremierNoAssComplementaire(iad.getNssCmpl1());
        annonce44.setSecondNoAssComplementaire(iad.getNssCmpl2());
        annonce44.setEtatCivil(iad.getEtatCivil());
        annonce44.setIsRefugie(iad.getRefugie());
        annonce44.setCantonEtatDomicile(iad.getCodeCantonEtatDomicile());

        // Prestation

        REPrestation10XmlDataStructure iprst = annonceXML.getInfoPrestation();
        annonce44.setGenrePrestation(iprst.getGenrePrestation());
        annonce44.setDebutDroit(iprst.getDebutDroit());
        annonce44.setMensualitePrestationsFrancs(JadeStringUtil.fillWithZeroes(iprst.getMensualite(), 5));
        annonce44.setFinDroit(iprst.getFinDroit());
        annonce44.setCodeMutation(iprst.getCodeMutation());

        annonce44.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        PRTiersWrapper tier = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(annonce44.getNoAssAyantDroit()));
        if (tier != null) {
            annonce44.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }
        return annonce44;

    }

    private static REAnnoncesAugmentationModification10Eme importAnnonce44_02(BSession session,
            REAnnonce10XmlDataStructure annonceXML) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setSession(session);

        annonce44.setCodeApplication("44");
        annonce44.setCodeEnregistrement01("02");

        annonce44.setEchelleRente(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle().getEchelle());
        annonce44.setDureeCoEchelleRenteAv73(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiAv73());
        annonce44.setDureeCoEchelleRenteDes73(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiDes73());
        annonce44.setDureeCotManquante48_72(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiManquante48_72());
        annonce44.setDureeCotManquante73_78(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getDureeCotiManquante73_78());
        annonce44.setAnneeCotClasseAge(annonceXML.getInfoPrestation().getBaseCalcul().getInfoEchelle()
                .getAnneeCotiClsAge());
        annonce44.setRamDeterminant(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRevAnnuelMoyen().getRam());
        annonce44.setDureeCotPourDetRAM(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRevAnnuelMoyen()
                .getDureeCotiRam());

        annonce44.setNombreAnneeBTE(annonceXML.getInfoPrestation().getBaseCalcul().getInfoBonification()
                .getNbrAnneeBTE());

        annonce44.setCodeRevenuSplitte(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRevAnnuelMoyen()
                .getRevenuSplitte());
        annonce44.setAnneeNiveau(annonceXML.getInfoPrestation().getBaseCalcul().getAnneeNiveau());

        annonce44
                .setNbreAnneeBTA(annonceXML.getInfoPrestation().getBaseCalcul().getInfoBonification().getNbrAnneeBTA());
        annonce44.setNbreAnneeBonifTrans(annonceXML.getInfoPrestation().getBaseCalcul().getInfoBonification()
                .getNbrAnneeBTR());

        annonce44.setOfficeAICompetent(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAI().getOai());
        annonce44.setDegreInvalidite(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAI().getDegreInvalidite());
        annonce44.setCodeInfirmite(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAI().getCodeInfirmite());
        annonce44.setSurvenanceEvenAssure(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAI()
                .getSurvenanceEvenementAssure());
        annonce44
                .setAgeDebutInvalidite(annonceXML.getInfoPrestation().getBaseCalcul().getInfoAI().getInvalidePrecoce());

        annonce44.setReduction(annonceXML.getInfoPrestation().getReduction());

        // TODO
        String cs = annonceXML.getInfoPrestation().getCodeSpecial();
        annonce44.setCasSpecial1(cs);
        annonce44.setCasSpecial2("");
        annonce44.setCasSpecial3("");
        annonce44.setCasSpecial4("");
        annonce44.setCasSpecial5("");
        annonce44.setNbreAnneeAnticipation(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getNombreAnneeAnticipation());
        annonce44.setReductionAnticipation(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getReductionAnticipation());
        annonce44.setDateDebutAnticipation(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getDateDebutAnticipation());
        annonce44.setDureeAjournement(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getDureeAjournement());
        annonce44.setSupplementAjournement(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getSupplementAjournement());
        annonce44.setDateRevocationAjournement(annonceXML.getInfoPrestation().getBaseCalcul().getInfoRetraiteFlexible()
                .getDateRevocationAjournement());
        annonce44.setIsSurvivant(annonceXML.getInfoPrestation().getInvalidSurvivant());

        return annonce44;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * 
     * Parse le fichier f_calcul.xml (Reader).
     * 
     * @return RXmlDataContainer
     * 
     * @throws Exception
     * 
     */
    public static final List parse(BSession session, BTransaction transaction, LinkedList listIdsRA, Reader reader)
            throws Exception, PRACORException {

        Document document = loadDocument(reader);

        REXmlDataContainer dc = new REXmlDataContainer();

        NodeList nl = document.getElementsByTagName("lot");
        if (nl.getLength() == 0) {
            throw new PRACORException("format de fichier xml invalide: pas d'element <f_calcul>");
        }
        Node nLot = nl.item(0);

        NodeList nl2 = nLot.getChildNodes();
        for (int i = 0; i < nl2.getLength(); i++) {
            Node nAnnonce = nl2.item(i);
            if ("RRMeldung10".equals(nAnnonce.getNodeName())) {
                dc.addAnnonces10(parseAnnonce10(nAnnonce));
            } else if ("RRMeldung9".equals(nAnnonce.getNodeName())) {
                dc.addAnnonces09(parseAnnonce09(nAnnonce));
            }
        }

        List retValue = new LinkedList();

        try {

            String idsRA = "";

            for (Iterator iterator = listIdsRA.iterator(); iterator.hasNext();) {
                idRA = (String) iterator.next();

                if (iterator.hasNext()) {
                    idsRA += idRA + ", ";
                } else {
                    idsRA += idRA;
                }

            }

            REAnnoncesAugmentationModification10Eme annonce44;
            REAnnoncesAugmentationModification9Eme annonce41;

            REAnnonce10XmlDataStructure[] annonces10 = dc.getAnnonces10();
            REAnnonce09XmlDataStructure[] annonces09 = dc.getAnnonces09();

            for (int i = 0; i < annonces10.length; i++) {
                REAnnonce10XmlDataStructure annonceXML = annonces10[i];

                annonce44 = importAnnonce44_01(session, annonceXML);

                noAvsAnnonce44_01 = NSUtil.formatAVSUnknown(annonce44.getNoAssAyantDroit());
                annonce44.add(transaction);
                idAnnonce44_01 = annonce44.getId();

                // Retrouver la rente accordée et setter REAnnonceRente
                RERenteAccordeeManager raMan = new RERenteAccordeeManager();
                raMan.setSession(session);
                raMan.setForIdsRentesAccordees(idsRA);
                raMan.find(transaction);
                // On parcourt les rentes accordées dont les ids sont passés en
                // paramètres dans la liste des ra crées précédemment
                String idRA = "";
                if (raMan.size() == 1) {
                    RERenteAccordee ra = (RERenteAccordee) raMan.getEntity(0);
                    idRA = ra.getIdPrestationAccordee();
                } else {
                    // si il y a plusieurs rentes accordee on prend celle qui a
                    // les mêmes:
                    // - NSS (idTiersBeneficiaire)
                    // - Genre de rente (genrePrestation)
                    // - Date de debut
                    for (Iterator iterator = raMan.iterator(); iterator.hasNext();) {
                        RERenteAccordee ra = (RERenteAccordee) iterator.next();

                        // Tests pour être sûr que c'est la bonne rente accordée
                        if (ra.getCodePrestation().equals(annonce44.getGenrePrestation())) {
                            if (ra.getIdTiersBeneficiaire().equals(annonce44.getIdTiers())) {
                                if (BSessionUtil.compareDateEqual(session, ra.getDateDebutDroit(),
                                        PRDateFormater.convertDate_MMAA_to_MMxAAAA(annonce44.getDebutDroit()))) {
                                    idRA = ra.getIdPrestationAccordee();
                                }
                            }
                        }
                    }
                }

                if (JadeStringUtil.isBlankOrZero(idRA)) {
                    throw new Exception("RA not found for id : " + idRA);
                }

                REAnnonceRente annonceRente = new REAnnonceRente();
                annonceRente.setSession(session);
                annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                annonceRente.setIdAnnonceHeader(annonce44.getIdAnnonce());
                annonceRente.setIdRenteAccordee(idRA);
                annonceRente.add(transaction);

                annonce44 = importAnnonce44_02(session, annonceXML);
                annonce44.setSession(session);

                annonce44.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                PRTiersWrapper tier = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(noAvsAnnonce44_01));
                if (tier != null) {
                    annonce44.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                }

                annonce44.add(transaction);
                idAnnonce44_02 = annonce44.getId();

                if (!JadeStringUtil.isEmpty(idAnnonce44_01) && !JadeStringUtil.isEmpty(idAnnonce44_02)) {
                    // mise à jour de l'annonce 44 01
                    REAnnonceHeader annonceHeader = new REAnnonceHeader();
                    annonceHeader.setSession(session);
                    annonceHeader.setIdAnnonce(idAnnonce44_01);
                    annonceHeader.retrieve(transaction);

                    annonceHeader.setIdLienAnnonce(idAnnonce44_02);
                    annonceHeader.update(transaction);
                }

            }

            for (int i = 0; i < annonces09.length; i++) {
                REAnnonce09XmlDataStructure annonceXML = annonces09[i];

                annonce41 = importAnnonce41_01(session, annonceXML);

                noAvsAnnonce41_01 = NSUtil.formatAVSUnknown(annonce41.getNoAssAyantDroit());
                annonce41.add(transaction);
                idAnnonce41_01 = annonce41.getId();

                // Retrouver la rente accordée et setter REAnnonceRente
                RERenteAccordeeManager raMan = new RERenteAccordeeManager();
                raMan.setSession(session);
                raMan.setForIdsRentesAccordees(idsRA);
                raMan.find(transaction);
                // On parcourt les rentes accordées dont les ids sont passés en
                // paramètres dans la liste des ra crées précédemment
                String idRA = "";
                if (raMan.size() == 1) {
                    RERenteAccordee ra = (RERenteAccordee) raMan.getEntity(0);
                    idRA = ra.getIdPrestationAccordee();
                } else {
                    // si il y a plusieurs rentes accordee on prend celle qui a
                    // les mêmes:
                    // - NSS (idTiersBeneficiaire)
                    // - Genre de rente (genrePrestation)
                    // - Date de debut
                    for (Iterator iterator = raMan.iterator(); iterator.hasNext();) {
                        RERenteAccordee ra = (RERenteAccordee) iterator.next();

                        // Tests pour être sûr que c'est la bonne rente accordée
                        if (ra.getCodePrestation().equals(annonce41.getGenrePrestation())) {
                            if (ra.getIdTiersBeneficiaire().equals(annonce41.getIdTiers())) {
                                if (BSessionUtil.compareDateEqual(session, ra.getDateDebutDroit(),
                                        PRDateFormater.convertDate_MMAA_to_MMxAAAA(annonce41.getDebutDroit()))) {
                                    idRA = ra.getIdPrestationAccordee();
                                }
                            }
                        }
                    }
                }

                if (JadeStringUtil.isBlankOrZero(idRA)) {
                    throw new Exception("RA not found for id : " + idRA);
                }

                REAnnonceRente annonceRente = new REAnnonceRente();
                annonceRente.setSession(session);
                annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                annonceRente.setIdAnnonceHeader(annonce41.getIdAnnonce());
                annonceRente.setIdRenteAccordee(idRA);
                annonceRente.add(transaction);

                annonce41 = importAnnonce41_02(session, annonceXML);
                annonce41.setSession(session);

                annonce41.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                PRTiersWrapper tier = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(noAvsAnnonce41_01));
                if (tier != null) {
                    annonce41.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                }

                annonce41.add(transaction);
                idAnnonce41_02 = annonce41.getId();

                if (!JadeStringUtil.isEmpty(idAnnonce41_01) && !JadeStringUtil.isEmpty(idAnnonce41_02)) {
                    // mise à jour de l'annonce 41 01
                    REAnnonceHeader annonceHeader = new REAnnonceHeader();
                    annonceHeader.setSession(session);
                    annonceHeader.setIdAnnonce(idAnnonce41_01);
                    annonceHeader.retrieve(transaction);

                    annonceHeader.setIdLienAnnonce(idAnnonce41_02);
                    annonceHeader.update(transaction);
                }
            }

        } catch (Exception e) {
            throw new PRACORException("impossible de parser annonce.RR\n" + e.getMessage(), e);
        }

        return retValue;
    }

    protected static REAnnonce09XmlDataStructure parseAnnonce09(Node nAnnonce) throws Exception {

        Map mapAnnonce10 = childrenMap(nAnnonce);

        REAnnonce09XmlDataStructure annonceXml = new REAnnonce09XmlDataStructure();

        Node n = null;

        Node n1 = getNode(mapAnnonce10, REAnnonceXmlDataStructure.TAG_RENTE_ORDINAIRE, false);
        Node n2 = getNode(mapAnnonce10, REAnnonceXmlDataStructure.TAG_RENTE_EXTRAORDINAIRE, false);
        if (n1 != null) {
            n = n1;
            annonceXml.setTypeRente(REAnnonceXmlDataStructure.TYPE_RENTE_ORDINAIRE);
        } else if (n2 != null) {
            n = n2;
            annonceXml.setTypeRente(REAnnonceXmlDataStructure.TYPE_RENTE_EXTRAORDINAIRE);
        }

        boolean isAugmentation = false;
        Node nGR = null;
        NodeList nlGenreRente = n.getChildNodes();
        for (int i = 0; i < nlGenreRente.getLength(); i++) {
            nGR = nlGenreRente.item(i);
            if (REAnnonceXmlDataStructure.TAG_AUGMENTATION.equals(nGR.getNodeName())) {
                annonceXml.setGenreAnnonce(REAnnonceXmlDataStructure.GENRE_ANNONCE_AUGMENTATION);
                isAugmentation = true;
                break;
            }
        }
        // On ne traite que les annonces d'augmentation
        if (!isAugmentation) {
            return null;
        }

        Map mapAnnonceAugmentation = childrenMap(nGR);
        annonceXml.setNoCaisseAgence(getNodeValue(mapAnnonceAugmentation, REAnnonceXmlDataStructure.TAG_CAISSE_AGENCE,
                true));
        annonceXml.setNoAnnonce(getNodeValue(mapAnnonceAugmentation, REAnnonceXmlDataStructure.TAG_NO_ANNONCE, true));
        annonceXml.setRefInterneCaisse(getNodeValue(mapAnnonceAugmentation,
                REAnnonceXmlDataStructure.TAG_REF_INT_CAISSE, false));
        annonceXml.setMoisRapport(getNodeValue(mapAnnonceAugmentation, REAnnonceXmlDataStructure.TAG_MOIS_RAPPORT,
                false));

        NodeList nlAyantDroit = nGR.getChildNodes();
        for (int i = 0; i < nlAyantDroit.getLength(); i++) {
            Node nElem = nlAyantDroit.item(i);
            if (REAnnonceXmlDataStructure.TAG_AYANT_DROIT.equals(nElem.getNodeName())) {
                annonceXml.setInfoAyantDroit(parseAyantDroit(nElem));
            } else if (REAnnonceXmlDataStructure.TAG_PRESTATION.equals(nElem.getNodeName())) {
                annonceXml.setInfoPrestation(parsePrestation09(nElem));
            }
        }
        return annonceXml;
    }

    protected static REAnnonce10XmlDataStructure parseAnnonce10(Node nAnnonce) throws Exception {

        Map mapAnnonce10 = childrenMap(nAnnonce);

        REAnnonce10XmlDataStructure annonceXml = new REAnnonce10XmlDataStructure();

        Node n = null;

        Node n1 = getNode(mapAnnonce10, REAnnonceXmlDataStructure.TAG_RENTE_ORDINAIRE, false);
        Node n2 = getNode(mapAnnonce10, REAnnonceXmlDataStructure.TAG_RENTE_EXTRAORDINAIRE, false);
        if (n1 != null) {
            n = n1;
            annonceXml.setTypeRente(REAnnonceXmlDataStructure.TYPE_RENTE_ORDINAIRE);
        } else if (n2 != null) {
            n = n2;
            annonceXml.setTypeRente(REAnnonceXmlDataStructure.TYPE_RENTE_EXTRAORDINAIRE);
        }

        Node nGR = null;
        NodeList nlGenreRente = n.getChildNodes();
        boolean isAugmentation = false;
        for (int i = 0; i < nlGenreRente.getLength(); i++) {
            nGR = nlGenreRente.item(i);
            if (REAnnonceXmlDataStructure.TAG_AUGMENTATION.equals(nGR.getNodeName())) {
                annonceXml.setGenreAnnonce(REAnnonceXmlDataStructure.GENRE_ANNONCE_AUGMENTATION);
                isAugmentation = true;
                break;
            }
        }

        // On ne traite que les annonces d'augmentation
        if (!isAugmentation) {
            return null;
        }

        Map mapAnnonceAugmentation = childrenMap(nGR);
        annonceXml.setNoCaisseAgence(getNodeValue(mapAnnonceAugmentation, REAnnonceXmlDataStructure.TAG_CAISSE_AGENCE,
                true));
        annonceXml.setNoAnnonce(getNodeValue(mapAnnonceAugmentation, REAnnonceXmlDataStructure.TAG_NO_ANNONCE, true));
        annonceXml.setRefInterneCaisse(getNodeValue(mapAnnonceAugmentation,
                REAnnonceXmlDataStructure.TAG_REF_INT_CAISSE, false));
        annonceXml.setMoisRapport(getNodeValue(mapAnnonceAugmentation, REAnnonceXmlDataStructure.TAG_MOIS_RAPPORT,
                false));

        NodeList nlAyantDroit = nGR.getChildNodes();
        for (int i = 0; i < nlAyantDroit.getLength(); i++) {
            Node nElem = nlAyantDroit.item(i);
            if (REAnnonceXmlDataStructure.TAG_AYANT_DROIT.equals(nElem.getNodeName())) {
                annonceXml.setInfoAyantDroit(parseAyantDroit(nElem));
            } else if (REAnnonceXmlDataStructure.TAG_PRESTATION.equals(nElem.getNodeName())) {
                annonceXml.setInfoPrestation(parsePrestation10(nElem));
            }
        }
        return annonceXml;
    }

    private static REAyantDroitXmlDataStructure parseAyantDroit(Node nAyantDroit) throws Exception {
        REAyantDroitXmlDataStructure result = new REAyantDroitXmlDataStructure();

        Map mapAD = childrenMap(nAyantDroit);
        result.setNssAyantDroit(getNodeValue(mapAD, REAnnonceXmlDataStructure.TAG_NSS_AYANT_DROIT, true));
        result.setEtatCivil(getNodeValue(mapAD, REAnnonceXmlDataStructure.TAG_ETAT_CIVIL, false));
        result.setRefugie(getNodeValue(mapAD, REAnnonceXmlDataStructure.TAG_REFUGIE, false));
        result.setCodeCantonEtatDomicile(getNodeValue(mapAD, REAnnonceXmlDataStructure.TAG_CODE_CANTON_ETAT, false));

        result.setNssCmpl1(getNodeChildValue(mapAD, REAnnonceXmlDataStructure.TAG_PARENTE,
                REAnnonceXmlDataStructure.TAG_NSS_COMPL_1, false));
        result.setNssCmpl2(getNodeChildValue(mapAD, REAnnonceXmlDataStructure.TAG_PARENTE,
                REAnnonceXmlDataStructure.TAG_NSS_COMPL_2, false));

        return result;
    }

    protected static REBaseCalcul09XmlDataStructure parseBaseCalcul09(Node nPrestation) throws Exception {

        REBaseCalcul09XmlDataStructure result = new REBaseCalcul09XmlDataStructure();
        NodeList nl = nPrestation.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (REAnnonceXmlDataStructure.TAG_BASE_CALCUL.equals(n.getNodeName())) {
                Map mBaseCalcul = childrenMap(n);
                result.setAnneeNiveau(getNodeValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_ANNEE_NIVEAU, false));
                result.setMinimumGaranti(getNodeValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_MINIMUM_GARANTI, false));
                result.setLimiteRevenu(getNodeValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_LIMITE_REVENU, false));

                // Info echelle
                REEchelleXmlDataStructure echelleXML = new REEchelleXmlDataStructure();
                echelleXML.setAnneeCotiClsAge(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_ECHELLE, REAnnonceXmlDataStructure.TAG_ANNEE_COTI_CLS_AGE,
                        false));

                echelleXML.setDureeCotiAv73(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COTI_AV73, false));

                echelleXML.setDureeCotiDes73(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COTI_AP73, false));

                echelleXML.setDureeCotiManquante48_72(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COT_MANQUANTE_48_72, false));

                echelleXML.setDureeCotiManquante73_78(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COT_MANQUANTE_73_78, false));

                echelleXML.setEchelle(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_ECHELLE, false));

                result.setInfoEchelle(echelleXML);

                RERevAnnuelMoyenXmlDataStructure infoRAMXml = new RERevAnnuelMoyenXmlDataStructure();
                infoRAMXml.setDureeCotiRam(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_RAM,
                        REAnnonceXmlDataStructure.TAG_DUREE_COTI_RAM_9, false));

                infoRAMXml.setRam(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_RAM,
                        REAnnonceXmlDataStructure.TAG_RAM, false));

                infoRAMXml.setRevenuPrisEnCompte(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_RAM,
                        REAnnonceXmlDataStructure.TAG_REV_PRIS_EN_COMPTE, false));

                result.setInfoRevAnnuelMoyen(infoRAMXml);

                REBonification09XmlDataStructure infoBonicifaction = new REBonification09XmlDataStructure();
                infoBonicifaction.setMontantBTEMoyenne(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_BONIF, REAnnonceXmlDataStructure.TAG_BTE_9, false));

                infoBonicifaction
                        .setNombreAnneeBTE(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_BONIF,
                                REAnnonceXmlDataStructure.TAG_NBR_ANNEE_BTE_9, false));

                infoBonicifaction.setRamSansBTE(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_BONIF, REAnnonceXmlDataStructure.TAG_RAM_SANS_BTE_9, false));

                result.setInfoBonification(infoBonicifaction);

                // Info AI epoux
                REAIXmlDataStructure aiXML = new REAIXmlDataStructure();

                aiXML.setCodeAtteinteFonctionnelle(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_AI, REAnnonceXmlDataStructure.TAG_CODE_ATTEINTE_FONCT, false));

                aiXML.setCodeInfirmite(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_CODE_INFIRMITE, false));

                aiXML.setDegreInvalidite(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_DEGRE_INVALIDITE, false));

                aiXML.setInvalidePrecoce(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_INVALID_PRECOCE, false));

                aiXML.setOai(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_OAI, false));

                aiXML.setSurvenanceEvenementAssure(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_AI, REAnnonceXmlDataStructure.TAG_SURVENANCE_EV_ASSURE,
                        false));

                result.setInfoAIEpoux(aiXML);

                // Info AI epouse
                aiXML = new REAIXmlDataStructure();

                aiXML.setCodeAtteinteFonctionnelle(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_AI_EPOUSE,
                        REAnnonceXmlDataStructure.TAG_CODE_ATTEINTE_FONCT, false));

                aiXML.setCodeInfirmite(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI_EPOUSE,
                        REAnnonceXmlDataStructure.TAG_CODE_INFIRMITE, false));

                aiXML.setDegreInvalidite(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI_EPOUSE,
                        REAnnonceXmlDataStructure.TAG_DEGRE_INVALIDITE, false));

                aiXML.setInvalidePrecoce(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI_EPOUSE,
                        REAnnonceXmlDataStructure.TAG_INVALID_PRECOCE, false));

                aiXML.setOai(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI_EPOUSE,
                        REAnnonceXmlDataStructure.TAG_OAI, false));

                aiXML.setSurvenanceEvenementAssure(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_AI_EPOUSE,
                        REAnnonceXmlDataStructure.TAG_SURVENANCE_EV_ASSURE, false));

                result.setInfoAIEpouse(aiXML);

                // Traitement des info propre à la retraite flexible.
                NodeList nl2 = n.getChildNodes();
                for (int j = 0; j < nl2.getLength(); j++) {
                    Node n2 = nl2.item(j);
                    if (REAnnonceXmlDataStructure.TAG_INFO_RETRAITE_FLEXIBLE.equals(n2.getNodeName())) {

                        RERetraiteFlexibleXmlDataStructure infoRetraiteFlex = new RERetraiteFlexibleXmlDataStructure();
                        Map mapFlex = childrenMap(n2);

                        infoRetraiteFlex.setDateRevocationAjournement(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_AJOURNEMENT_RENTE,
                                REAnnonceXmlDataStructure.TAG_DATE_REVOC_AJOURN, false));

                        infoRetraiteFlex.setDureeAjournement(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_AJOURNEMENT_RENTE,
                                REAnnonceXmlDataStructure.TAG_DUREE_AJOURNEMENT, false));

                        infoRetraiteFlex.setSupplementAjournement(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_AJOURNEMENT_RENTE,
                                REAnnonceXmlDataStructure.TAG_SUPP_AJOURN, false));

                        result.setInfoRetraiteFlexible(infoRetraiteFlex);
                        break;
                    }
                }
                break;
            }
        }
        return result;
    }

    protected static REBaseCalcul10XmlDataStructure parseBaseCalcul10(Node nPrestation) throws Exception {

        REBaseCalcul10XmlDataStructure result = new REBaseCalcul10XmlDataStructure();
        NodeList nl = nPrestation.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (REAnnonceXmlDataStructure.TAG_BASE_CALCUL.equals(n.getNodeName())) {
                Map mBaseCalcul = childrenMap(n);
                result.setAnneeNiveau(getNodeValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_ANNEE_NIVEAU, false));

                // Info echelle
                REEchelleXmlDataStructure echelleXML = new REEchelleXmlDataStructure();
                echelleXML.setAnneeCotiClsAge(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_ECHELLE, REAnnonceXmlDataStructure.TAG_ANNEE_COTI_CLS_AGE,
                        false));

                echelleXML.setDureeCotiAv73(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COTI_AV73, false));

                echelleXML.setDureeCotiDes73(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COTI_AP73, false));

                echelleXML.setDureeCotiManquante48_72(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COT_MANQUANTE_48_72, false));

                echelleXML.setDureeCotiManquante73_78(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_DUREE_COT_MANQUANTE_73_78, false));

                echelleXML.setEchelle(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_ECHELLE,
                        REAnnonceXmlDataStructure.TAG_ECHELLE, false));

                result.setInfoEchelle(echelleXML);

                RERevAnnuelMoyenXmlDataStructure infoRAMXml = new RERevAnnuelMoyenXmlDataStructure();
                infoRAMXml.setDureeCotiRam(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_RAM,
                        REAnnonceXmlDataStructure.TAG_DUREE_COTI_RAM, false));

                infoRAMXml.setRam(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_RAM,
                        REAnnonceXmlDataStructure.TAG_RAM, false));

                infoRAMXml.setRevenuPrisEnCompte(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_RAM,
                        REAnnonceXmlDataStructure.TAG_REV_PRIS_EN_COMPTE, false));

                infoRAMXml.setRevenuSplitte(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_RAM,
                        REAnnonceXmlDataStructure.TAG_REVENU_SPLITTE, false));

                result.setInfoRevAnnuelMoyen(infoRAMXml);

                REBonification10XmlDataStructure infoBonicifaction = new REBonification10XmlDataStructure();
                infoBonicifaction.setNbrAnneeBTA(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_BONIF, REAnnonceXmlDataStructure.TAG_NBR_ANNEE_BTA, false));

                infoBonicifaction.setNbrAnneeBTE(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_BONIF, REAnnonceXmlDataStructure.TAG_NBR_ANNEE_BTE, false));

                infoBonicifaction.setNbrAnneeBTR(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_BONIF, REAnnonceXmlDataStructure.TAG_NBR_ANNEE_BTR, false));

                result.setInfoBonification(infoBonicifaction);

                // Info AI
                REAIXmlDataStructure aiXML = new REAIXmlDataStructure();

                aiXML.setCodeAtteinteFonctionnelle(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_AI, REAnnonceXmlDataStructure.TAG_CODE_ATTEINTE_FONCT, false));

                aiXML.setCodeInfirmite(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_CODE_INFIRMITE, false));

                aiXML.setDegreInvalidite(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_DEGRE_INVALIDITE, false));

                aiXML.setInvalidePrecoce(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_INVALID_PRECOCE, false));

                aiXML.setOai(getNodeChildValue(mBaseCalcul, REAnnonceXmlDataStructure.TAG_INFO_AI,
                        REAnnonceXmlDataStructure.TAG_OAI, false));

                aiXML.setSurvenanceEvenementAssure(getNodeChildValue(mBaseCalcul,
                        REAnnonceXmlDataStructure.TAG_INFO_AI, REAnnonceXmlDataStructure.TAG_SURVENANCE_EV_ASSURE,
                        false));

                result.setInfoAI(aiXML);

                // Traitement des info propre à la retraite flexible.
                NodeList nl2 = n.getChildNodes();
                for (int j = 0; j < nl2.getLength(); j++) {
                    Node n2 = nl2.item(j);
                    if (REAnnonceXmlDataStructure.TAG_INFO_RETRAITE_FLEXIBLE.equals(n2.getNodeName())) {

                        RERetraiteFlexibleXmlDataStructure infoRetraiteFlex = new RERetraiteFlexibleXmlDataStructure();
                        Map mapFlex = childrenMap(n2);

                        infoRetraiteFlex.setDateRevocationAjournement(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_AJOURNEMENT_RENTE,
                                REAnnonceXmlDataStructure.TAG_DATE_REVOC_AJOURN, false));

                        infoRetraiteFlex.setDureeAjournement(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_AJOURNEMENT_RENTE,
                                REAnnonceXmlDataStructure.TAG_DUREE_AJOURNEMENT, false));

                        infoRetraiteFlex.setSupplementAjournement(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_AJOURNEMENT_RENTE,
                                REAnnonceXmlDataStructure.TAG_SUPP_AJOURN, false));

                        infoRetraiteFlex.setNombreAnneeAnticipation(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_RENTE_ANTICIPEE,
                                REAnnonceXmlDataStructure.TAG_NBR_ANNEE_ANTICIPATION, false));

                        infoRetraiteFlex.setDateDebutAnticipation(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_RENTE_ANTICIPEE,
                                REAnnonceXmlDataStructure.TAG_DATE_DEBUT_ANTICIPATION, false));

                        infoRetraiteFlex.setReductionAnticipation(getNodeChildValue(mapFlex,
                                REAnnonceXmlDataStructure.TAG_RENTE_ANTICIPEE,
                                REAnnonceXmlDataStructure.TAG_REDUCTION_ANTICIPATION, false));

                        result.setInfoRetraiteFlexible(infoRetraiteFlex);
                        break;
                    }
                }
                break;
            }
        }
        return result;
    }

    private static REPrestation09XmlDataStructure parsePrestation09(Node nPrestation) throws Exception {
        REPrestation09XmlDataStructure result = new REPrestation09XmlDataStructure();

        Map mapPrst = childrenMap(nPrestation);

        result.setGenrePrestation(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_GENRE_PRESTATION, true));
        result.setCodeMutation(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_CODE_MUTATION, false));
        result.setDebutDroit(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_DEBUT_DROIT, true));
        result.setFinDroit(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_FIN_DROIT, false));
        result.setInvalidSurvivant(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_INVALID_SURVIVANT, false));
        result.setMensualite(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_MENSUALITE, false));
        result.setMensualiteRenteOrdinaireRemplacee(getNodeValue(mapPrst,
                REAnnonceXmlDataStructure.TAG_MENSUALITE_RO_REMPLACEE, false));
        result.setReduction(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_REDUCTION, false));
        result.setCodeSpecial(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_CODE_SPECIAL, false));

        result.setBaseCalcul(parseBaseCalcul09(nPrestation));

        return result;
    }

    private static REPrestation10XmlDataStructure parsePrestation10(Node nPrestation) throws Exception {
        REPrestation10XmlDataStructure result = new REPrestation10XmlDataStructure();

        Map mapPrst = childrenMap(nPrestation);

        result.setGenrePrestation(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_GENRE_PRESTATION, true));
        result.setCodeMutation(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_CODE_MUTATION, false));
        result.setDebutDroit(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_DEBUT_DROIT, true));
        result.setFinDroit(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_FIN_DROIT, false));
        result.setInvalidSurvivant(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_INVALID_SURVIVANT, false));
        result.setMensualite(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_MENSUALITE, false));
        result.setReduction(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_REDUCTION, false));
        result.setCodeSpecial(getNodeValue(mapPrst, REAnnonceXmlDataStructure.TAG_CODE_SPECIAL, false));

        result.setBaseCalcul(parseBaseCalcul10(nPrestation));

        return result;
    }

    /**
     * Crée une nouvelle instance de la classe REACORPrestationsParser.
     */
    private REImportAnnonceACOR() {
    }
}
