package globaz.musca.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAEnteteCompteAnnexe;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.process.helper.FASectionHelper;
import globaz.musca.process.helper.FASectionsACompenserHelper;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Compensation de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public class FAPassageCompenserNewNoteCreditProcess extends FAPassageCompenserNewProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Insert the method's description here. Creation date: (28.07.2003 16:00:55)
     * 
     * @return boolean
     * @param proposition_iterator
     *            java.util.Iterator
     */

    @Override
    protected boolean _doCompenserAnnexe(Collection sections, FAEnteteCompteAnnexe enteteFacture,
            CACompteAnnexe compteAnnexe) {
        try {
            FWCurrency posMontantRestantFacture = null;
            boolean isCompensationTotal, aQuittancer;
            boolean isAffilieRadie;
            String remarque;

            FAEnteteFactureManager entManager = new FAEnteteFactureManager();
            entManager.setISession(getSession());
            entManager.setForIdPassage(getIdPassage());
            entManager.setForIdExterneRole(enteteFacture.getIdExterneRole());
            BStatement statement = null;
            FAEnteteFacture entFacture = null;

            FWCurrency totalFacture = new FWCurrency(enteteFacture.getTotalDecomptes());

            if (totalFacture.isNegative()) {
                entManager.setOrderBy(" TOTALFACTURE ASC ");
            } else if (totalFacture.isPositive()) {
                entManager.setOrderBy(" TOTALFACTURE DESC ");
            }

            FASectionsACompenserHelper sectHelper = new FASectionsACompenserHelper();
            sectHelper.initSectionsACompenser(sections);
            statement = entManager.cursorOpen(getTransaction());
            boolean passerAuSuivant = false;
            // prendre les entêtes de la facturation une à une et compenser
            while (((entFacture = (FAEnteteFacture) entManager.cursorReadNext(statement)) != null)
                    && !entFacture.isNew()) {
                ArrayList mapTri1 = new ArrayList();
                ArrayList mapTri2 = new ArrayList();
                ArrayList mapTri3 = new ArrayList();

                if (passerAuSuivant) {
                    sectHelper.initSectionsACompenser(sections);
                }

                // Compenser la section et mise à jours dans le helper !!!
                FWCurrency montantFacture = new FWCurrency(entFacture.getTotalFacture());
                posMontantRestantFacture = new FWCurrency(montantFacture.toString());

                // On travaille en positif
                posMontantRestantFacture.abs();

                FASectionHelper sectionACompenser = null;

                isAffilieRadie = isAffiliationRadie(entFacture);

                if (montantFacture.isNegative()) {
                    // Si c'est une note de crédit et et que l'affilié est radié
                    // on verifie s'il a été radié dans l'année de cotisation
                    if (isAffilieRadie) {
                        if (getDateFinAffiliation().substring(6).equals(
                                entFacture.getIdExterneFacture().substring(0, 4))) {
                            while ((sectionACompenser = sectHelper.getNextSectionACompenser()) != null) {
                                CASection section = new CASection();
                                section = (CASection) sectionACompenser.getSection();
                                int type = JadeStringUtil.parseInt(section.getIdExterne().substring(4, 6), 0);
                                // On prends en premier les sections qui ont une
                                // date de fin de période > que la date de fin
                                // d'affiliation
                                // et qui sont périodique
                                if (BSessionUtil.compareDateFirstGreater(getSession(), section.getDateFinPeriode(),
                                        getDateFinAffiliation())
                                        && (((type >= 1) && (type <= 12)) || ((type >= 40) && (type <= 46)))) {
                                    mapTri1.add(section);
                                    isCompensationTotal = true;
                                    sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                                    sectionACompenser.setMontantACompenser(new FWCurrency(0));
                                    sectHelper.updateSection(sectionACompenser);
                                    continue;
                                    // Ensuite toutes les factures qui sont dans
                                    // l'année de radiation
                                } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), "01.01."
                                        + getDateFinAffiliation().substring(6), "31.12."
                                        + getDateFinAffiliation().substring(6), section.getDateFinPeriode())) {
                                    mapTri2.add(section);
                                    isCompensationTotal = true;
                                    sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                                    sectionACompenser.setMontantACompenser(new FWCurrency(0));
                                    sectHelper.updateSection(sectionACompenser);
                                    continue;
                                    // Et pour finir le reste dans l'ordre
                                    // décroissant.
                                } else {
                                    mapTri3.add(section);
                                    isCompensationTotal = true;
                                    sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                                    sectionACompenser.setMontantACompenser(new FWCurrency(0));
                                    sectHelper.updateSection(sectionACompenser);
                                    continue;
                                }
                            }
                        } else {
                            while ((sectionACompenser = sectHelper.getNextSectionACompenser()) != null) {
                                CASection section = new CASection();
                                section = (CASection) sectionACompenser.getSection();
                                if (section.getIdExterne().substring(0, 4)
                                        .equals(entFacture.getIdExterneFacture().substring(0, 4))) {
                                    mapTri1.add(section);
                                } else {
                                    mapTri2.add(section);
                                }
                                isCompensationTotal = true;
                                sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                                sectionACompenser.setMontantACompenser(new FWCurrency(0));
                                sectHelper.updateSection(sectionACompenser);
                                continue;
                            }
                        }
                    } else {
                        while ((sectionACompenser = sectHelper.getNextSectionACompenser()) != null) {
                            CASection section = new CASection();
                            section = (CASection) sectionACompenser.getSection();
                            mapTri1.add(section);
                            isCompensationTotal = true;
                            sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                            sectionACompenser.setMontantACompenser(new FWCurrency(0));
                            sectHelper.updateSection(sectionACompenser);
                            continue;
                        }
                    }
                } else {
                    while ((sectionACompenser = sectHelper.getNextSectionACompenser()) != null) {
                        CASection section = new CASection();
                        section = (CASection) sectionACompenser.getSection();
                        mapTri1.add(section);
                        isCompensationTotal = true;
                        sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                        sectionACompenser.setMontantACompenser(new FWCurrency(0));
                        sectHelper.updateSection(sectionACompenser);
                        continue;
                    }
                }
                mapTri1.addAll(mapTri2);
                mapTri1.addAll(mapTri3);
                FASectionsACompenserHelper sectHelperMap = new FASectionsACompenserHelper();
                sectHelperMap.initSectionsACompenser(mapTri1);
                // Tant qu'il y a des sections à compenser sectHelperMap ....
                while ((sectionACompenser = sectHelperMap.getNextSectionACompenser()) != null) {
                    if (posMontantRestantFacture.isNegative() || posMontantRestantFacture.isZero()) {
                        break;
                    }

                    CASection section = new CASection();
                    section = (CASection) sectionACompenser.getSection();
                    int typeSection = JadeStringUtil.parseInt(section.getIdTypeSection(), 0);
                    if ((typeSection != 16) && (typeSection != 21) && (typeSection != 28) && (typeSection != 29)
                            && (typeSection != 15)) {
                        isCompensationTotal = false;
                        aQuittancer = false;
                        // Si la section est en mode report on ne la prend pas.
                        if (APISection.MODE_REPORT.equals(section.getIdModeCompensation())
                                || APISection.MODE_COMP_COMPLEMENTAIRE.equals(section.getIdModeCompensation())
                                || APISection.MODE_COMP_CONT_EMPLOYEUR.equals(section.getIdModeCompensation())
                                || APISection.MODE_COMP_COT_PERS.equals(section.getIdModeCompensation())
                                || APISection.MODE_COMP_DEC_FINAL.equals(section.getIdModeCompensation())) {
                            isCompensationTotal = true;
                            sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                            sectionACompenser.setMontantACompenser(new FWCurrency(0));
                            sectHelperMap.updateSection(sectionACompenser);
                            continue;
                        }
                        // Si c'est une facture pour les étudiants, les
                        // compensations ne seront jamais à valider
                        if (entFacture.getIdSousType()
                                .equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT)) {
                            aQuittancer = false;
                        } else {
                            if (montantFacture.isNegative()) {
                                // Si c'est une note de crédit et et que
                                // l'affilié est radié
                                // on verifie s'il a du contentieux
                                if (isAffilieRadie) {
                                    aQuittancer = hasContentieux(section, compteAnnexe);
                                } else {
                                    if (isSectionEchue(section, passage)) {
                                        aQuittancer = hasContentieux(section, compteAnnexe);
                                    } else {
                                        // Comme on ne veut pas utiliser cette
                                        // section pour compenser la facturation
                                        // On la met dans le même état que si
                                        // elle avait été compensé totalement.
                                        isCompensationTotal = true;
                                        sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                                        sectionACompenser.setMontantACompenser(new FWCurrency(0));
                                        sectHelperMap.updateSection(sectionACompenser);
                                        continue;
                                    }
                                }
                            } else {
                                aQuittancer = hasContentieux(section, compteAnnexe);
                            }
                        }

                        remarque = "";
                        FWCurrency montantSectionACompenser = new FWCurrency(sectionACompenser.getMontantACompenser()
                                .toString());

                        FWCurrency soldeMontantACompenser = new FWCurrency(montantSectionACompenser.toString());
                        // Variable de travail
                        FWCurrency posMontantSection = new FWCurrency(montantSectionACompenser.toString());
                        // On travaille en positif
                        posMontantSection.abs();

                        FWCurrency montantACompenser = new FWCurrency(0);

                        // Compensation totale
                        if (posMontantRestantFacture.isPositive()
                                && (posMontantRestantFacture.compareTo(posMontantSection) >= 0)) {
                            montantACompenser = new FWCurrency(posMontantSection.toString());
                            isCompensationTotal = true;
                        }
                        // Compensation partielle
                        else if (posMontantRestantFacture.isPositive()
                                && (posMontantRestantFacture.compareTo(posMontantSection) < 0)) {
                            montantACompenser = new FWCurrency(posMontantRestantFacture.toString());
                            isCompensationTotal = false;
                        }

                        // CAS montant facture > 0 --> montant sections < 0
                        // On va compenser avec des montant négatif
                        if (montantFacture.isPositive()) {
                            montantACompenser.negate();
                        }

                        // Si le compte est a un solde positif avec un motif de
                        // contentieux bloqué et actif
                        // mettre une remarque pour information
                        if (montantSectionACompenser.isPositive()
                                && compteAnnexe.isCompteBloqueEtActif(passage.getDateFacturation()))// bloqué
                        {
                            remarque = getSession().getLabel("COMPTEANNEXE_CONTENTIEUX_MOTIF")
                                    + CACodeSystem.getLibelle(getSession(), compteAnnexe.getIdContMotifBloque());
                        }
                        // Si le compte est a un solde positif avec une section
                        // avec contentieux
                        // mettre une remarque pour information
                        else if (montantSectionACompenser.isPositive()) {
                            Collection collec = compteAnnexe.propositionCompensation(APICompteAnnexe.PC_TYPE_MONTANT,
                                    APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN, entFacture.getTotalFacture(), false);
                            Iterator it = collec.iterator();
                            while (it.hasNext()) {
                                APISection sec = (APISection) it.next();
                                if (sec.getContentieuxEstSuspendu().booleanValue()) {
                                    remarque = getSession().getLabel("SECTION_CONTENTIEUX");
                                    break;
                                }
                            }

                        }
                        _creerAfactCompensationAnnexe(entFacture, sectionACompenser.getSection(),
                                montantACompenser.toString(), aQuittancer, remarque);

                        soldeMontantACompenser.sub(montantACompenser);

                        montantACompenser.abs();
                        posMontantRestantFacture.sub(montantACompenser);

                        // Update de la section
                        if (isCompensationTotal) {
                            sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                            sectionACompenser.setMontantACompenser(new FWCurrency(0));
                        } else {
                            sectionACompenser.setStatus(FASectionHelper.PARTIELLEMENT_COMPENSEE);
                            sectionACompenser.setMontantACompenser(soldeMontantACompenser);

                        }
                        sectHelperMap.updateSection(sectionACompenser);
                    } else {
                        isCompensationTotal = true;
                        sectionACompenser.setStatus(FASectionHelper.COMPENSEE);
                        sectionACompenser.setMontantACompenser(new FWCurrency(0));
                        sectHelperMap.updateSection(sectionACompenser);
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage("Ne trouve pas d'entete de facture pour le tiers:" + enteteFacture.getIdTiers(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
            // ****************ROLLBACK!!
        } finally {
            return (!getTransaction().hasErrors());
        }
    }
}
