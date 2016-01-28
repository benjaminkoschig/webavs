/**
 * Créé le 13 mars 06
 */
package globaz.aquila.service.taxes;

import globaz.aquila.db.access.batch.COCalculTaxe;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeCalculTaxe;
import globaz.aquila.db.access.batch.COEtapeCalculTaxeManager;
import globaz.aquila.db.access.batch.COParametreTaxe;
import globaz.aquila.db.access.batch.COParametreTaxeManager;
import globaz.aquila.db.access.batch.COTrancheTaxe;
import globaz.aquila.db.access.batch.COTrancheTaxeManager;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.amende.COAmende;
import globaz.aquila.db.amende.COAmendeManager;
import globaz.aquila.util.COSectionProxy;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.contentieux.CACalculTaxe;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperation;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>Description</h1>
 * <p>
 * Source de taxes qui les chargent en utilisant les tables de configuration habituelles ({@link COCalculTaxe}).
 * </p>
 * 
 * @author dvh
 */
public class COTaxeDefaut implements ICOTaxeProducer {

    public static final String CODE_AMENDE_0 = "243001";
    public static final String CODE_AMENDE_1 = "243002";
    public static final String CODE_AMENDE_2 = "243003";
    public static final String CODE_AMENDE_3 = "243004";
    public static final String CODE_AMENDE_4 = "243005";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public static final String ID_EXTERNE_BEGIN_WITH_2110_4000 = APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4000.substring(
            0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4000.substring(5, 9) + ")(.)*";
    public static final String ID_EXTERNE_BEGIN_WITH_2110_4010 = APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4010.substring(
            0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_2110_4010.substring(5, 9) + ")(.)*";
    public static final String ID_EXTERNE_BEGIN_WITH_2160_4030 = APIRubrique.ID_EXTERNE_BEGIN_WITH_2160_4030.substring(
            0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_2160_4030.substring(5, 9) + ")(.)*";
    public static final String ID_EXTERNE_BEGIN_WITH_6010_4030 = APIRubrique.ID_EXTERNE_BEGIN_WITH_6010_4030.substring(
            0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_6010_4030.substring(5, 9) + ")(.)*";
    public static final String ID_EXTERNE_BEGIN_WITH_9100_6000 = APIRubrique.ID_EXTERNE_BEGIN_WITH_9100_6000.substring(
            0, 3) + "([0-9])(.)(" + APIRubrique.ID_EXTERNE_BEGIN_WITH_9100_6000.substring(5, 9) + ")(.)*";

    /**
     * Creates a new COTaxeDefaut object.
     */
    COTaxeDefaut() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param session
     * @param contentieux
     * @param etapeCalculTaxe
     * @param caProxy
     * @param taxe
     * @param calculTaxe
     * @return
     * @throws Exception
     */
    private COTaxe calculTaxe(BSession session, COContentieux contentieux, COEtapeCalculTaxe etapeCalculTaxe,
            COSectionProxy caProxy, COTaxe taxe, COCalculTaxe calculTaxe) throws Exception {
        // Chargement du manager de trancheTaxe
        COTrancheTaxeManager tranches = new COTrancheTaxeManager();

        tranches.setForIdCalculTaxe(etapeCalculTaxe.getIdCalculTaxe());
        tranches.setSession(session);
        tranches.find();

        if (tranches.isEmpty()) {
            tranches = null;
        }

        // Chargement du manager de ParametreTaxe
        COParametreTaxeManager parametres = new COParametreTaxeManager();

        parametres.setForIdCalculTaxe(etapeCalculTaxe.getIdCalculTaxe());
        parametres.setSession(session);
        parametres.find();

        if (parametres.isEmpty()) {
            parametres = null;
        }

        // Montant déterminant de section
        if (calculTaxe.getBaseTaxe().equalsIgnoreCase(COCalculTaxe.SECTION)) {
            taxe.setMontantBase(contentieux.getSection().getSolde());
        } else if (calculTaxe.getBaseTaxe().equalsIgnoreCase(COCalculTaxe.MASSE)) {
            String masseCompteur = giveMasseCompteur(session, contentieux, parametres);

            if (masseCompteur == null) {
                return null;
            }

            taxe.setMontantBase(masseCompteur);
        } else {
            // Parcourir CAEciture si baseTaxe = Rubrique CACompteCourantSection
            // si baseTaxe = Compte_courant
            FWCurrency _total = new FWCurrency();

            if (calculTaxe.getBaseTaxe().equalsIgnoreCase(COCalculTaxe.RUBRIQUE)) {
                for (int i = 0; i < caProxy.ecritures().size(); i++) {
                    CAEcriture elemEcriture = (CAEcriture) caProxy.ecritures().get(i);

                    if (elemEcriture.getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)
                            || elemEcriture.getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)) {
                        continue;
                    }

                    // recherche de la rubrique dans cacheParametreTaxeManager
                    COParametreTaxe elemCacheParamTaxe;

                    for (int j = 0; j < parametres.size(); j++) {
                        elemCacheParamTaxe = (COParametreTaxe) parametres.getEntity(j);

                        if (elemCacheParamTaxe.getIdRubrique().equalsIgnoreCase(elemEcriture.getIdCompte())) {
                            _total.add(elemEcriture.getMontant());
                            elemCacheParamTaxe.setEstTrouve(Boolean.TRUE);
                        }
                    }
                }
            } else if (calculTaxe.getBaseTaxe().equalsIgnoreCase(COCalculTaxe.COMPTE_COURANT)) {
                for (int i = 0; i < caProxy.operations().size(); i++) {
                    CAJoinCompteCourantOperation joinEntity = (CAJoinCompteCourantOperation) caProxy.operations()
                            .get(i);

                    // Recherche de la rubrique dans cacheParametreTaxeManager
                    COParametreTaxe elemCacheParamTaxe;

                    for (int j = 0; j < parametres.size(); j++) {
                        elemCacheParamTaxe = (COParametreTaxe) parametres.getEntity(j);

                        CACompteCourant compteCourant = caProxy.compteCourant(joinEntity.getIdCompteCourant());

                        if (compteCourant.isNew()) {
                            throw new Exception("Compte courant id=" + joinEntity.getIdCompteCourant() + "non resolu)");
                        }

                        if (elemCacheParamTaxe.getIdRubrique().equalsIgnoreCase(compteCourant.getIdRubrique())) {
                            _total.add(joinEntity.getMontant());
                            elemCacheParamTaxe.setEstTrouve(Boolean.TRUE);
                        }
                    }
                }
            }

            // contrôle que estRequis soit renseigné pour toutes les rubriques
            // dans parametreTaxe
            if (calculTaxe.getBaseTaxe().equalsIgnoreCase(COCalculTaxe.RUBRIQUE)
                    || calculTaxe.getBaseTaxe().equalsIgnoreCase(COCalculTaxe.COMPTE_COURANT)) {
                boolean nonRenseigne = false;
                COParametreTaxe elemCacheParamTaxe;

                if (parametres != null) {
                    for (int j = 0; j < parametres.size(); j++) {
                        elemCacheParamTaxe = (COParametreTaxe) parametres.getEntity(j);

                        if (elemCacheParamTaxe.getEstRequis().booleanValue()
                                && !elemCacheParamTaxe.getEstTrouve().booleanValue()) {
                            nonRenseigne = true;
                        }
                    }
                }

                if (nonRenseigne) {
                    return null;
                }

                if (_total.isZero() || _total.isNegative()) {
                    return null;
                } else {
                    taxe.setMontantBase(_total.toString());
                }
            }
        }

        // Montant fixe
        FWCurrency _montantFixe = new FWCurrency();

        _montantFixe.add(calculTaxe.getMontantFixe());

        if (calculTaxe.getTypeTaxe().equalsIgnoreCase(COCalculTaxe.MONTANT) && !_montantFixe.isZero()) {
            taxe.setMontantTaxe(calculTaxe.getMontantFixe());
        } else
        // Taux fixe
        if (calculTaxe.getTypeTaxe().equalsIgnoreCase(COCalculTaxe.TAUX) && !_montantFixe.isZero()) {
            taxe.setTaux(calculTaxe.getMontantFixe());
            taxe.setMontantTaxe(""
                    + JANumberFormatter.round(
                            Float.parseFloat(taxe.getTaux()) * Float.parseFloat(taxe.getMontantBase()), 0.05, 2,
                            JANumberFormatter.NEAR));
        } else {
            // Recherche de la tranche de taxe
            COTrancheTaxe elemTrancheTaxe;

            if (tranches != null) {
                int i = 0;

                do {
                    elemTrancheTaxe = (COTrancheTaxe) tranches.get(i);
                    i++;
                } while ((i < tranches.size())
                        && (Float.parseFloat(taxe.getMontantBase()) > Float.parseFloat(elemTrancheTaxe
                                .getValeurPlafond())));

                if ((tranches.size() >= i)
                        && (Float.parseFloat(taxe.getMontantBase()) > Float.parseFloat(elemTrancheTaxe
                                .getValeurPlafond()))) {

                    session.addError(session.getLabel("AQUILA_ERR_CA7175"));

                    return null;
                }

                // Si Montant
                if (calculTaxe.getTypeTaxe().equalsIgnoreCase(COCalculTaxe.MONTANT)) {
                    taxe.setMontantTaxe(elemTrancheTaxe.getMontantVariable());
                } else {
                    // Taux ou montant fixe + taux
                    BigDecimal _tauxPlafond = new BigDecimal(elemTrancheTaxe.getTauxPlafond());

                    if (calculTaxe.getTypeTaxe().equalsIgnoreCase(COCalculTaxe.TAUX)
                            || (calculTaxe.getTypeTaxe().equalsIgnoreCase(COCalculTaxe.MONTANT_TAUX) && (_tauxPlafond
                                    .compareTo(new BigDecimal(0.0)) != 0))) {
                        taxe.setTaux(elemTrancheTaxe.getTauxPlafond());

                        String taxeResult = ""
                                + JANumberFormatter.round(
                                        Float.parseFloat(elemTrancheTaxe.getTauxPlafond())
                                                * Float.parseFloat(taxe.getMontantBase()), 0.05, 2,
                                        JANumberFormatter.NEAR);

                        if (calculTaxe.getTypeTaxe().equalsIgnoreCase(COCalculTaxe.MONTANT_TAUX)) {
                            FWCurrency cMontant = new FWCurrency();

                            // Si il n'y a pas de taxe, on ne met pas de frais
                            // fixe
                            cMontant.add(taxeResult);

                            if (!cMontant.equals(new FWCurrency(0.0))) {
                                cMontant.add(calculTaxe.getMontantFixe());
                            }

                            if (elemTrancheTaxe.getMontantVariable() != null) {
                                int compare = cMontant.compareTo(new FWCurrency(elemTrancheTaxe.getMontantVariable()));

                                if (compare == 1) {
                                    // Taxe plus grande que le montant variable
                                    // définit comme maximum
                                    // on prend dans ce cas le le montant
                                    // maximum
                                    taxe.setMontantTaxe(elemTrancheTaxe.getMontantVariable());
                                } else {
                                    // Taxe plus petite que le montant maximum,
                                    // on peut prendre
                                    // la taxe calculée
                                    taxe.setMontantTaxe(cMontant.toString());
                                }
                            } else {
                                taxe.setMontantTaxe(cMontant.toString());
                            }
                        } else {
                            taxe.setMontantTaxe(taxeResult);
                        }
                    }
                }
            } else {
                taxe = null;
            }
        }

        return taxe;
    }

    /**
     * Repris à peu près tel quel de {@link CACalculTaxe#calculTaxe(globaz.osiris.db.comptes.CASection)}.
     * 
     * @param session
     * @param contentieux
     * @param etape
     * @param etapeCalculTaxe
     * @param caProxy
     * @return
     * @throws Exception
     */
    private COTaxe creerTaxe(BSession session, COContentieux contentieux, COEtape etape,
            COEtapeCalculTaxe etapeCalculTaxe, COSectionProxy caProxy) throws Exception {
        COTaxe taxe = new COTaxe();

        taxe.setIdCalculTaxe(etapeCalculTaxe.getIdCalculTaxe());
        taxe.setImputerTaxe(etapeCalculTaxe.getImputerTaxe().booleanValue());

        // chargement du calcul taxe
        COCalculTaxe calculTaxe = taxe.loadCalculTaxe(session);
        if (calculTaxe.getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_AVS)
                || calculTaxe.getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_PS)
                || calculTaxe.getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_AVS_PARITAIRE)
                || calculTaxe.getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_PS_PARITAIRE)) {
            return getTaxeAmende(session, contentieux, etapeCalculTaxe, caProxy, taxe, calculTaxe);
        } else {
            return getTaxeDefaut(session, contentieux, etapeCalculTaxe, caProxy, taxe, calculTaxe);
        }
    }

    private String getAmende(String montantBase, String taux, String min, String max) {
        BigDecimal result = new BigDecimal(montantBase);
        result = result.multiply(new BigDecimal(taux)).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);

        if (result.compareTo(new BigDecimal(min)) < 0) {
            return min;
        } else if (result.compareTo(new BigDecimal(max)) > 0) {
            return max;
        } else {
            FWCurrency resultCurrency = new FWCurrency(result.toString());
            // TODO Arrondir à Fr. 5.00 contrôler que cela fonctionne
            resultCurrency.round(5.00d);

            return resultCurrency.toString();
        }

    }

    /**
     * @see globaz.aquila.service.taxes.ICOTaxeProducer#getListeTaxes(globaz.globall.db.BSession,globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.aquila.db.access.batch.COEtape)
     */
    @Override
    public List getListeTaxes(BSession session, COContentieux contentieux, COEtape etape) throws Exception {
        LinkedList retValue = new LinkedList();

        // trouver les taxes pour cette étape
        COEtapeCalculTaxeManager taxesPourEtape = new COEtapeCalculTaxeManager();

        taxesPourEtape.setForIdEtape(etape.getIdEtape());
        taxesPourEtape.setSession(session);
        taxesPourEtape.find();

        if (!taxesPourEtape.isEmpty()) {
            COSectionProxy caProxy = new COSectionProxy(session, contentieux.getIdSection());

            for (int i = 0; i < taxesPourEtape.size(); i++) {
                COEtapeCalculTaxe etapeCalculTaxe = (COEtapeCalculTaxe) taxesPourEtape.getEntity(i);

                COTaxe taxe = creerTaxe(session, contentieux, etape, etapeCalculTaxe, caProxy);
                if (taxe != null) {
                    retValue.add(taxe);
                }
            }
        }

        return retValue;
    }

    private COTaxe getTaxeAmende(BSession session, COContentieux contentieux, COEtapeCalculTaxe etapeCalculTaxe,
            COSectionProxy caProxy, COTaxe taxe, COCalculTaxe calculTaxe) throws Exception {
        // si dans le compte annexe on veut ne pas mettre certains type de
        // taxes, on renvoie null
        if (contentieux.getCompteAnnexe().getBloquerAmendeStatutaire().booleanValue()
                && (COCalculTaxe.AMENDE_STATUTAIRE_AVS.equals(calculTaxe.getTypeTaxeEtape())
                        || COCalculTaxe.AMENDE_STATUTAIRE_PS.equals(calculTaxe.getTypeTaxeEtape())
                        || COCalculTaxe.AMENDE_STATUTAIRE_AVS_PARITAIRE.equals(calculTaxe.getTypeTaxeEtape()) || COCalculTaxe.AMENDE_STATUTAIRE_PS_PARITAIRE
                            .equals(calculTaxe.getTypeTaxeEtape()))) {
            return null;
        }
        if (contentieux.getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE_PARITAIRE)) {
            if (etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_AVS)
                    || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_PS)) {
                return null;
            }
        } else if (etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape()
                .equals(COCalculTaxe.AMENDE_STATUTAIRE_AVS_PARITAIRE)
                || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape()
                        .equals(COCalculTaxe.AMENDE_STATUTAIRE_PS_PARITAIRE)) {
            return null;
        }

        // si le code amende est 0 , on a un rien, donc pas le peine de faire la
        // suite. Idem si y a rien dedans
        if (COTaxeDefaut.CODE_AMENDE_0.equals(contentieux.getCompteAnnexe().getQualiteDebiteur())
                || JadeStringUtil.isIntegerEmpty(contentieux.getCompteAnnexe().getQualiteDebiteur())) {
            // do nothing
            return null;
        } else {

            boolean executerAmendeStatuaire = false;
            ArrayList rubriqueRequise = new ArrayList();
            // Chargement du manager de ParametreTaxe
            COParametreTaxeManager parametres = new COParametreTaxeManager();

            parametres.setForIdCalculTaxe(etapeCalculTaxe.getIdCalculTaxe());
            parametres.setSession(session);
            parametres.find();

            if (!parametres.isEmpty()) {
                for (int i = 0; i < parametres.size(); i++) {
                    COParametreTaxe param = (COParametreTaxe) parametres.getEntity(i);
                    rubriqueRequise.add(param.getIdRubrique());
                }
            }

            // somme des montants de la section par rubrique
            COAmendeManager manager = new COAmendeManager();
            manager.setSession(session);
            manager.changeManagerSize(BManager.SIZE_NOLIMIT);
            manager.setForIdSection(contentieux.getIdSection());
            manager.find();

            if (!manager.isEmpty()) {
                FWCurrency totalAVS = new FWCurrency(0);
                FWCurrency totalPS = new FWCurrency(0);
                for (int i = 0; i < manager.size(); i++) {
                    COAmende amende = (COAmende) manager.getEntity(i);
                    // parcours du résultat pour avoir le total AVS et le total
                    // PS le total AVS correspond aux rubriques
                    // 211_.4010.xxxx, 211_.4000.xxxx, 216_.4030.xxxx,
                    // 601_.4030.xxxx et 910_.6000.xxxx

                    String numeroRubrique = amende.getIdExterne();
                    if (rubriqueRequise.contains(amende.getIdRubrique())) {
                        executerAmendeStatuaire = true;
                    }
                    Pattern pat = null;
                    Matcher mat = null;
                    pat = Pattern.compile(COTaxeDefaut.ID_EXTERNE_BEGIN_WITH_2110_4000);
                    mat = pat.matcher(numeroRubrique);
                    boolean a = mat.matches();

                    pat = Pattern.compile(COTaxeDefaut.ID_EXTERNE_BEGIN_WITH_2110_4010);
                    mat = pat.matcher(numeroRubrique);
                    boolean b = mat.matches();

                    pat = Pattern.compile(COTaxeDefaut.ID_EXTERNE_BEGIN_WITH_2160_4030);
                    mat = pat.matcher(numeroRubrique);
                    boolean c = mat.matches();

                    pat = Pattern.compile(COTaxeDefaut.ID_EXTERNE_BEGIN_WITH_6010_4030);
                    mat = pat.matcher(numeroRubrique);
                    boolean d = mat.matches();

                    pat = Pattern.compile(COTaxeDefaut.ID_EXTERNE_BEGIN_WITH_9100_6000);
                    mat = pat.matcher(numeroRubrique);
                    boolean e = mat.matches();

                    if (a || b || c || d || e) {
                        totalAVS.add(amende.getSomme());
                    } else {
                        if (rubriqueRequise.contains(amende.getIdRubrique()) && executerAmendeStatuaire) {
                            // Pour les AF, il prend les rubriques qui sont
                            // saisies en tant que requise dans la table COTXPTP
                            totalPS.add(amende.getSomme());
                        }
                    }
                }

                // calcul des amendes
                FWCurrency fTotalPaye = new FWCurrency(contentieux.getSection().getPmtCmp());
                if (fTotalPaye.isNegative()) {
                    totalAVS.add(fTotalPaye.toString());
                }

                // Chargement du manager de trancheTaxe
                COTrancheTaxeManager tranches = new COTrancheTaxeManager();

                tranches.setForIdCalculTaxe(etapeCalculTaxe.getIdCalculTaxe());
                tranches.setSession(session);
                tranches.find();

                if (tranches.isEmpty()) {
                    tranches = null;
                }

                // on a deja traité les cas ou le code amende est vide ou = à 0
                // plus haut
                if (parametres.isEmpty() || (!parametres.isEmpty() && executerAmendeStatuaire)) {
                    if (totalAVS.isPositive()
                            && !totalAVS.isZero()
                            && (calculTaxe.getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_AVS) || calculTaxe
                                    .getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_AVS_PARITAIRE))) {
                        String codeAmende = contentieux.getCompteAnnexe().getQualiteDebiteur();

                        taxe.setImputerTaxe(true);
                        taxe.setLibelle(session.getLabel("AQUILA_AMENDE_AVS"));

                        if (COTaxeDefaut.CODE_AMENDE_1.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 1)) {
                            // les montants sont fixes
                            taxe.setMontantTaxe(((COTrancheTaxe) tranches.get(0)).getMontantVariable());
                        } else if (COTaxeDefaut.CODE_AMENDE_2.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 2)) {
                            taxe.setMontantTaxe(getAmende(totalAVS.toString(),
                                    ((COTrancheTaxe) tranches.get(1)).getTauxPlafond(),
                                    ((COTrancheTaxe) tranches.get(1)).getMontantVariable(),
                                    ((COTrancheTaxe) tranches.get(1)).getValeurPlafond()));
                        } else if (COTaxeDefaut.CODE_AMENDE_3.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 3)) {
                            taxe.setMontantTaxe(getAmende(totalAVS.toString(),
                                    ((COTrancheTaxe) tranches.get(2)).getTauxPlafond(),
                                    ((COTrancheTaxe) tranches.get(2)).getMontantVariable(),
                                    ((COTrancheTaxe) tranches.get(2)).getValeurPlafond()));
                        } else if (COTaxeDefaut.CODE_AMENDE_4.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 4)) {
                            taxe.setMontantTaxe(getAmende(totalAVS.toString(),
                                    ((COTrancheTaxe) tranches.get(3)).getTauxPlafond(),
                                    ((COTrancheTaxe) tranches.get(3)).getMontantVariable(),
                                    ((COTrancheTaxe) tranches.get(3)).getValeurPlafond()));
                        }
                        return taxe;
                    }
                }
                if (parametres.isEmpty() || (!parametres.isEmpty() && executerAmendeStatuaire)) {
                    if (totalPS.isPositive()
                            && !totalPS.isZero()
                            && (calculTaxe.getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_PS) || calculTaxe
                                    .getTypeTaxeEtape().equals(COCalculTaxe.AMENDE_STATUTAIRE_PS_PARITAIRE))) {
                        String codeAmende = contentieux.getCompteAnnexe().getQualiteDebiteur();

                        taxe.setImputerTaxe(true);
                        taxe.setLibelle(session.getLabel("AQUILA_AMENDE_PS"));

                        if (COTaxeDefaut.CODE_AMENDE_1.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 1)) {
                            taxe.setMontantTaxe(((COTrancheTaxe) tranches.get(0)).getMontantVariable());
                        } else if (COTaxeDefaut.CODE_AMENDE_2.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 2)) {
                            taxe.setMontantTaxe(getAmende(totalPS.toString(),
                                    ((COTrancheTaxe) tranches.get(1)).getTauxPlafond(),
                                    ((COTrancheTaxe) tranches.get(1)).getMontantVariable(),
                                    ((COTrancheTaxe) tranches.get(1)).getValeurPlafond()));
                        } else if (COTaxeDefaut.CODE_AMENDE_3.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 3)) {
                            taxe.setMontantTaxe(getAmende(totalPS.toString(),
                                    ((COTrancheTaxe) tranches.get(2)).getTauxPlafond(),
                                    ((COTrancheTaxe) tranches.get(2)).getMontantVariable(),
                                    ((COTrancheTaxe) tranches.get(2)).getValeurPlafond()));
                        } else if (COTaxeDefaut.CODE_AMENDE_4.equals(codeAmende) && (tranches != null)
                                && (tranches.size() >= 4)) {
                            taxe.setMontantTaxe(getAmende(totalPS.toString(),
                                    ((COTrancheTaxe) tranches.get(3)).getTauxPlafond(),
                                    ((COTrancheTaxe) tranches.get(3)).getMontantVariable(),
                                    ((COTrancheTaxe) tranches.get(3)).getValeurPlafond()));
                        }
                        return taxe;
                    }
                }
                return null;
            }
            return taxe;
        }
    }

    private COTaxe getTaxeDefaut(BSession session, COContentieux contentieux, COEtapeCalculTaxe etapeCalculTaxe,
            COSectionProxy caProxy, COTaxe taxe, COCalculTaxe calculTaxe) throws Exception {
        // si dans le compte annexe on veut ne pas mettre certains type de
        // taxes, on renvoie null
        if (contentieux.getCompteAnnexe().getBloquerTaxeSommation().booleanValue()
                && (COCalculTaxe.TAXE_DE_SOMMATION.equals(calculTaxe.getTypeTaxeEtape())
                        || COCalculTaxe.TAXE_DE_SOMMATION_PARITAIRE.equals(calculTaxe.getTypeTaxeEtape())
                        || COCalculTaxe.TAXE_DE_SOMMATION_AVS.equals(calculTaxe.getTypeTaxeEtape())
                        || COCalculTaxe.TAXE_DE_SOMMATION_AVS_PARITAIRE.equals(calculTaxe.getTypeTaxeEtape())
                        || COCalculTaxe.TAXE_DE_SOMMATION_AF.equals(calculTaxe.getTypeTaxeEtape()) || COCalculTaxe.TAXE_DE_SOMMATION_AF_PARITAIRE
                            .equals(calculTaxe.getTypeTaxeEtape()))) {
            return null;
        }
        if (contentieux.getCompteAnnexe().getBloquerFraisPoursuite().booleanValue()
                && COCalculTaxe.AVANCE_DE_FRAIS.equals(calculTaxe.getTypeTaxeEtape())) {
            return null;
        }
        if (contentieux.getCompteAnnexe().getIdRole().equals(IntRole.ROLE_AFFILIE_PARITAIRE)) {
            if (etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.TAXE_DE_SOMMATION_PARITAIRE)
                    || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape()
                            .equals(COCalculTaxe.TAXE_DE_SOMMATION_AVS_PARITAIRE)
                    || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape()
                            .equals(COCalculTaxe.TAXE_DE_SOMMATION_AF_PARITAIRE)
                    || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.AVANCE_DE_FRAIS)) {
                return calculTaxe(session, contentieux, etapeCalculTaxe, caProxy, taxe, calculTaxe);
            }
        } else if (etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.TAXE_DE_SOMMATION)
                || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.AVANCE_DE_FRAIS)
                || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.TAXE_DE_SOMMATION_AVS)
                || etapeCalculTaxe.getCalculTaxe().getTypeTaxeEtape().equals(COCalculTaxe.TAXE_DE_SOMMATION_AF)) {
            return calculTaxe(session, contentieux, etapeCalculTaxe, caProxy, taxe, calculTaxe);
        }
        return null;
    }

    private String giveMasseCompteur(BSession session, COContentieux contentieux, COParametreTaxeManager parametres)
            throws Exception {
        if ((parametres == null) || (parametres.size() != 1)) {
            session.addError(session.getLabel("ERR_PARAMETRE_TAXE"));
            return null;
        }

        CACompteurManager cptMgr = new CACompteurManager();
        cptMgr.setSession(session);
        cptMgr.setForIdCompteAnnexe(contentieux.getIdCompteAnnexe());
        cptMgr.setForIdRubrique(((COParametreTaxe) parametres.getFirstEntity()).getIdRubrique());
        cptMgr.setForAnnee(String.valueOf((Integer.valueOf(contentieux.getSection().getIdExterne().substring(0, 4))
                .intValue() - 1)));
        cptMgr.find();

        /**
         * si aucun compteur est trouvé le montant de base de la taxe sera "setté" à 0 et ainsi la taxe minimum sera
         * prise
         */
        if (cptMgr.isEmpty()) {
            return "0";
        } else if (cptMgr.size() > 1) {
            session.addError(session.getLabel("ERR_COMPTEUR_TROUVE"));
            return null;
        } else {
            return ((CACompteur) cptMgr.getFirstEntity()).getCumulMasse();
        }

    }

}
