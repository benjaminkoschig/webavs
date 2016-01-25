package globaz.corvus.servlet;

import globaz.babel.api.ICTDocument;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.ordresversements.IRESoldePourRestitution;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.RECompensationInterDecisionsManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.db.ordresversements.RESoldePourRestitutionManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.decisions.KeyPeriodeInfo;
import globaz.corvus.vb.decisions.REBeneficiaireInfoVO;
import globaz.corvus.vb.decisions.REDecisionInfoContainer;
import globaz.corvus.vb.decisions.REDecisionsContainer;
import globaz.corvus.vb.decisions.REDecompteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REDecompteAction extends PRDefaultAction {

    private REDecisionsContainer decisionsContainer = new REDecisionsContainer();
    private ICTDocument documentCommun;
    private ICTDocument documentHelper;
    private boolean isRemarqueCompensation = false;
    private FWCurrency montantPourSolde = new FWCurrency();
    private List<REOrdresVersements> ovCompenser = new ArrayList<REOrdresVersements>();
    private List<REOrdresVersements> rentesDejaVersees = new ArrayList<REOrdresVersements>();

    public REDecompteAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // Charger toute la structure nécessaire à l'affichage et compléter le viewBean
        String _destination = "";

        try {
            BSession bSession = (BSession) mainDispatcher.getSession();

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");

            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            JSPUtils.setBeanProperties(request, viewBean);

            Class<?> b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            ((REDecompteViewBean) viewBean).setSession(bSession);

            // conversion du viewBean pour y setter toutes les informations
            REDecompteViewBean vb = (REDecompteViewBean) viewBean;

            String idTierRequerant = request.getParameter("idTierRequerant");
            vb.setIdTierRequerant(idTierRequerant);

            // Retrieve de la décision
            REDecisionEntity decision = new REDecisionEntity();
            decision.setIdDecision(request.getParameter("selectedId"));
            decision.setSession(bSession);
            decision.retrieve();

            vb.setIdDemandeRente(decision.getIdDemandeRente());
            vb.setCsTypeDecision(decision.getCsTypeDecision());
            vb.setIdDecision(request.getParameter("selectedId"));
            vb.setIdTierBeneficiaire(decision.getIdTiersBeneficiairePrincipal());

            // Chargement du catalogue de textes pour reprendre les mêmes textes
            // quand dans la décision
            // Retrieve du destinataire de la décision
            String idTiersPrincipal = "";

            idTiersPrincipal = decision.getIdTiersBeneficiairePrincipal();
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(bSession, idTiersPrincipal);

            if (null == tiers) {
                tiers = PRTiersHelper.getAdministrationParId(bSession, idTiersPrincipal);
            }

            // Retrieve du codeIsoLangue pour la décision
            String codeIsoLangue = bSession.getUserInfo().getLanguage();

            // chargement du catalogue de texte (Charger le document communs)
            documentHelper = PRBabelHelper.getDocumentHelper(bSession);
            documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
            documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
            documentHelper.setActif(Boolean.TRUE);
            documentHelper.setCodeIsoLangue(codeIsoLangue);
            documentHelper.setNom("openOffice");

            // Récupération du document
            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                throw new Exception(
                        ((BSession) mainDispatcher.getSession()).getLabel("ERREUR_IMPOSSIBLE_RETROUVER_TEXTES"));
            } else {
                documentCommun = documents[0];
            }

            // Chargement du decisionInfoContainer
            decisionsContainer.loadDecision(bSession, decision);
            decisionsContainer.parcourDecisionsIC(bSession);
            REDecisionInfoContainer decisionInfoContainer = decisionsContainer.getDecisionIC();
            Set<KeyPeriodeInfo> keys = null;
            FWCurrency montantTotal = new FWCurrency(0);

            String tableauHTML = "";
            JACalendar cal = new JACalendarGregorian();

            if ((decisionsContainer != null) && (decisionInfoContainer != null)) {
                // Retrieve des périodes et création d'un tableau en html pour
                // affichage dans l'écran
                tableauHTML += "<div align=left><table width=85%>";

                // On reprend toutes les clés de la map des bénéficiaires info
                keys = decisionInfoContainer.getMapBeneficiairesInfo().keySet();

                if (keys != null) {
                    Iterator<KeyPeriodeInfo> iter = keys.iterator();

                    // Pour chaque clé, on remplit les droits
                    while ((iter != null) && iter.hasNext()) {
                        tableauHTML += "<tr>";

                        KeyPeriodeInfo keyPeriode = iter.next();

                        String dateDebut = PRDateFormater.format_MMMYYYY(
                                new JADate(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(keyPeriode.dateDebut)),
                                bSession.getIdLangueISO());

                        if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())
                                && JadeStringUtil.isEmpty(keyPeriode.dateFin)) {
                            keyPeriode.dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(decision
                                    .getDateFinRetro());
                        }

                        String dateFin = PRDateFormater.format_MMMYYYY(
                                new JADate(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(keyPeriode.dateFin)),
                                bSession.getIdLangueISO());

                        if (JadeStringUtil.isEmpty(keyPeriode.dateFin)) {

                            // Si pas de date de fin, on multiplie le nombre de mois entre la date de début
                            // et le mois avant la date du dernier paiement, puis on détail le mois du dernier paiement

                            // mm.aaaa
                            String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(bSession);

                            // On prend la plus petite date entre la date du
                            // dernier pmt et la date de validation !!!
                            if (!JadeStringUtil.isBlankOrZero(decision.getDateDecision())) {
                                // mm.aaaa
                                String dateDecision = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(decision
                                        .getDateDecision());
                                cal = new JACalendarGregorian();
                                if (cal.compare(new JADate(dateDernierPaiement), new JADate(dateDecision)) == JACalendar.COMPARE_SECONDLOWER) {
                                    dateDernierPaiement = dateDecision;
                                }
                            }

                            // mm.aaaa
                            String dateDepuis = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut);
                            int nbMois = PRDateFormater.nbrMoisEntreDates(new JADate(dateDepuis), new JADate(
                                    dateDernierPaiement));

                            // on retire le mois en cours (mois du dernier
                            // paiement)
                            nbMois = nbMois - 1;

                            FWCurrency montantMensuel = new FWCurrency(0);

                            REBeneficiaireInfoVO[] benefs = decisionInfoContainer.getBeneficiaires(keyPeriode);
                            for (int inc = 0; inc < benefs.length; inc++) {
                                montantMensuel.add(benefs[inc].getMontant());
                            }

                            if (nbMois == 0) {

                                nbMois = 1;

                                String des = documentCommun.getTextes(5).getTexte(3).getDescriptionBrut();
                                des = PRStringUtils.replaceString(des, "{dateDebut}", dateDebut);

                                tableauHTML += "<td width=60%>" + des + "</td>";

                                if (!JadeStringUtil.isBlankOrZero(decision.getDateFinRetro())) {
                                    nbMois = PRDateFormater.nbrMoisEntreDates(
                                            new JADate(PRDateFormater
                                                    .convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut)), new JADate(
                                                    decision.getDateFinRetro()));
                                }

                                if (nbMois == 0) {
                                    nbMois = 1;
                                }

                                String nbMoisA = documentCommun.getTextes(5).getTexte(4).getDescriptionBrut();
                                nbMoisA = PRStringUtils.replaceString(nbMoisA, "{nbMois}", String.valueOf(nbMois));

                                tableauHTML += "<td width=10%>" + nbMoisA + "</td>";

                                tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                        + "</td>";
                                tableauHTML += "<td align=right width=10%>" + montantMensuel.toStringFormat() + "</td>";

                                tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                        + "</td>";
                                tableauHTML += "<td align=right width=10%>"
                                        + new FWCurrency(String.valueOf(montantMensuel.doubleValue()
                                                * Double.parseDouble(String.valueOf(nbMois)))).toStringFormat()
                                        + "</td>";

                                montantTotal.add(String.valueOf(montantMensuel.doubleValue()
                                        * Double.parseDouble(String.valueOf(nbMois))));
                            } else if (nbMois > 0) {
                                cal = new JACalendarGregorian();

                                // 01.04.2008
                                JADate dateFinAvantDernierPmt = new JADate(dateDernierPaiement);
                                // 01.03.2008
                                dateFinAvantDernierPmt = cal.addMonths(dateFinAvantDernierPmt, -1);
                                // 31.03.2008
                                String date = cal.lastInMonth(dateFinAvantDernierPmt.toString());

                                String ligneDebutFin = PRStringUtils.replaceString(documentCommun.getTextes(5)
                                        .getTexte(2).getDescription(), "{dateDebut}", dateDebut);

                                ligneDebutFin = PRStringUtils.replaceString(ligneDebutFin, "{dateFin}",
                                        PRDateFormater.format_MMMYYYY(new JADate(date), codeIsoLangue));

                                tableauHTML += "<td width=60%>" + ligneDebutFin + "</td>";

                                String nbMoisA = documentCommun.getTextes(5).getTexte(4).getDescriptionBrut();
                                nbMoisA = PRStringUtils.replaceString(nbMoisA, "{nbMois}", String.valueOf(nbMois));

                                tableauHTML += "<td width=10%>" + nbMoisA + "</td>";

                                tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                        + "</td>";
                                tableauHTML += "<td align=right width=10%>" + montantMensuel.toStringFormat() + "</td>";

                                tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                        + "</td>";
                                tableauHTML += "<td align=right width=10%>"
                                        + new FWCurrency(String.valueOf(montantMensuel.doubleValue()
                                                * Double.parseDouble(String.valueOf(nbMois)))).toStringFormat()
                                        + "</td>";

                                montantTotal.add(String.valueOf(montantMensuel.doubleValue()
                                        * Double.parseDouble(String.valueOf(nbMois))));

                                tableauHTML += "</tr><tr>";

                                nbMois = 1;

                                String des = documentCommun.getTextes(5).getTexte(3).getDescriptionBrut();
                                des = PRStringUtils.replaceString(des, "{dateDebut}",
                                        PRDateFormater.format_MMMYYYY(new JADate(dateDernierPaiement), codeIsoLangue));

                                tableauHTML += "<td width=60%>" + des + "</td>";

                                nbMoisA = documentCommun.getTextes(5).getTexte(4).getDescriptionBrut();
                                nbMoisA = PRStringUtils.replaceString(nbMoisA, "{nbMois}", "1");

                                tableauHTML += "<td width=10%>" + nbMoisA + "</td>";

                                tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                        + "</td>";
                                tableauHTML += "<td align=right width=10%>" + montantMensuel.toStringFormat() + "</td>";

                                tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                        + "</td>";
                                tableauHTML += "<td align=right width=10%>"
                                        + new FWCurrency(String.valueOf(montantMensuel.doubleValue()
                                                * Double.parseDouble(String.valueOf(nbMois)))).toStringFormat()
                                        + "</td>";

                                montantTotal.add(String.valueOf(montantMensuel.doubleValue()
                                        * Double.parseDouble(String.valueOf(nbMois))));
                            }
                        } else {
                            String df = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateFin);
                            // On prend la plus petite date entre la date du
                            // dernier pmt et la date de décision!!!
                            if (!JadeStringUtil.isBlankOrZero(decision.getDateDecision())) {
                                // mm.aaaa
                                String dateDecision = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(decision
                                        .getDateDecision());
                                String dfp = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateFin);

                                cal = new JACalendarGregorian();
                                if (cal.compare(new JADate(dfp), new JADate(dateDecision)) == JACalendar.COMPARE_SECONDLOWER) {
                                    df = dateDecision;
                                }
                            }

                            dateFin = PRDateFormater.format_MMMYYYY(
                                    new JADate(PRDateFormater.convertDate_MMxAAAA_to_MMAAAA(df)),
                                    bSession.getIdLangueISO());

                            String deA = documentCommun.getTextes(5).getTexte(2).getDescriptionBrut();
                            deA = PRStringUtils.replaceString(deA, "{dateDebut}", dateDebut);
                            deA = PRStringUtils.replaceString(deA, "{dateFin}", dateFin);

                            tableauHTML += "<td width=60%>" + deA + "</td>";

                            int nbMois = PRDateFormater.nbrMoisEntreDates(
                                    new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut)),
                                    new JADate(df));

                            String nbMoisA = documentCommun.getTextes(5).getTexte(4).getDescriptionBrut();
                            nbMoisA = PRStringUtils.replaceString(nbMoisA, "{nbMois}", String.valueOf(nbMois));

                            tableauHTML += "<td width=10%>" + nbMoisA + "</td>";

                            FWCurrency montantMensuel = new FWCurrency(0);

                            REBeneficiaireInfoVO[] benefs = decisionInfoContainer.getBeneficiaires(keyPeriode);
                            for (int inc = 0; inc < benefs.length; inc++) {
                                montantMensuel.add(benefs[inc].getMontant());
                            }

                            tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                    + "</td>";
                            tableauHTML += "<td align=right width=10%>" + montantMensuel.toStringFormat() + "</td>";

                            tableauHTML += "<td align=right width=5%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                    + "</td>";
                            tableauHTML += "<td align=right width=10%>"
                                    + new FWCurrency(String.valueOf(montantMensuel.doubleValue()
                                            * Double.parseDouble(String.valueOf(nbMois)))).toStringFormat() + "</td>";

                            montantTotal.add(String.valueOf(montantMensuel.doubleValue()
                                    * Double.parseDouble(String.valueOf(nbMois))));
                        }
                        tableauHTML += "</tr>";
                    }
                }

                // Dispatch des OV
                FWCurrency montantInteretsMoratoires = new FWCurrency(0);
                FWCurrency montantRentesDejaVersees = new FWCurrency(0);
                FWCurrency montantImpotSource = new FWCurrency(0);

                Map<String, FWCurrency> montantsACompenser = new HashMap<String, FWCurrency>();
                Map<String, String> creanciers = new HashMap<String, String>();

                // Retrieve des ordres de versements
                REOrdresVersementsManager ovMgr = new REOrdresVersementsManager();
                ovMgr.setSession(bSession);
                ovMgr.setForIdPrestation(decision.getPrestation((bSession).getCurrentThreadTransaction())
                        .getIdPrestation());
                ovMgr.find((bSession).getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);

                for (Iterator<REOrdresVersements> iterator = ovMgr.iterator(); iterator.hasNext();) {
                    REOrdresVersements ov = iterator.next();

                    if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE)) {
                        montantInteretsMoratoires.add(ov.getMontant());
                    } else if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE)) {

                        // si dette compensé interdecision
                        if (ov.getIsCompensationInterDecision().booleanValue()) {
                            rentesDejaVersees.add(ov);
                        } else {
                            if (ov.getIsCompense().booleanValue()) {
                                montantRentesDejaVersees.add(ov.getMontant());
                                isRemarqueCompensation = true;
                                ovCompenser.add(ov);

                                RECompensationInterDecisionsManager cmpMgr = new RECompensationInterDecisionsManager();
                                cmpMgr.setSession(bSession);
                                cmpMgr.setForIdOV(ov.getIdOrdreVersement());
                                cmpMgr.find();
                                montantPourSolde = new FWCurrency(0);
                                for (int i = 0; i < cmpMgr.size(); i++) {
                                    RECompensationInterDecisions cid = (RECompensationInterDecisions) cmpMgr
                                            .getEntity(i);
                                    montantPourSolde.add(cid.getMontant());
                                }

                                // Voir s'il y a une restitution
                                RESoldePourRestitutionManager soldeMgr = new RESoldePourRestitutionManager();
                                soldeMgr.setSession(bSession);
                                soldeMgr.setForIdPrestation(decision.getPrestation(
                                        bSession.getCurrentThreadTransaction()).getIdPrestation());
                                soldeMgr.find();

                                // On supprime la remarque si un solde pour
                                // restitution existe et qu'il ne s'agisse pas
                                // d'une CID
                                if (!soldeMgr.isEmpty() && montantPourSolde.isZero()) {
                                    isRemarqueCompensation = false;
                                }
                            }
                        }
                    } else if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_TIERS)
                            || ov.getCsType().equals(IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE)) {
                        creanciers.put(ov.getIdTiers(), ov.getMontant());
                    } else if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_IMPOT_SOURCE)) {
                        montantImpotSource.add(ov.getMontant());
                    } else if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {
                        vb.setIdTierBeneficiaire(ov.getIdTiers());
                        vb.setIdTierAdrPmt(ov.getIdTiersAdressePmt());
                        vb.setIdDomAdrPmt(ov.getIdDomaineApplication());
                        vb.setIdExtAdrPmt(ov.getIdSection());
                    } else {
                        if (ov.getIsCompense().booleanValue()) {
                            // BZ 5399
                            if (montantsACompenser.containsKey(ov.getCsType())) {
                                montantsACompenser.get(ov.getCsType()).add(ov.getMontant());
                            } else {
                                montantsACompenser.put(ov.getCsType(), new FWCurrency(ov.getMontant()));
                            }
                        }
                    }
                }

                if (!montantInteretsMoratoires.isZero()) {

                    tableauHTML += "<tr><td width=60%>" + documentCommun.getTextes(5).getTexte(5).getDescriptionBrut()
                            + "</td>";
                    tableauHTML += "<td colspan=4 align=right width=30%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                            + "</td>";
                    tableauHTML += "<td width=10% align=right>" + montantInteretsMoratoires.toStringFormat()
                            + "</td></tr>";
                    montantTotal.add(montantInteretsMoratoires);
                }

                tableauHTML += "<tr><td colspan=6><hr></td></tr>";

                tableauHTML += "<tr><td width=60%><b>" + documentCommun.getTextes(5).getTexte(6).getDescriptionBrut()
                        + "</b></td><td colspan=3 width=30%%>&nbsp;</td>";
                tableauHTML += "<td align=right width=5%><b>" + bSession.getLabel("JSP_DECOMPTE_D_CHF") + "</b></td>";
                tableauHTML += "<td align=right width=10%><b>" + montantTotal.toStringFormat() + "</b></td></tr>";
                tableauHTML += "<tr><td colspan=6>&nbsp;</td></tr>";

                // Rentes déjà versées
                // 1) Traiter les rentes de type : Rentes versées à tort à [nomPrenom]
                if (rentesDejaVersees.size() > 0) {

                    for (Iterator<REOrdresVersements> iterator2 = rentesDejaVersees.iterator(); iterator2.hasNext();) {
                        REOrdresVersements ov = iterator2.next();

                        String texte = documentCommun.getTextes(5).getTexte(15).getDescriptionBrut();

                        RECompensationInterDecisions compIntDec = new RECompensationInterDecisions();
                        compIntDec.setSession(bSession);
                        compIntDec.setAlternateKey(RECompensationInterDecisions.ALTERNATE_KEY_ID_OV_COMPENSATION);
                        compIntDec.setIdOVCompensation(ov.getIdOrdreVersement());
                        compIntDec.retrieve();

                        REOrdresVersements ordreV = new REOrdresVersements();
                        ordreV.setSession(bSession);
                        ordreV.setIdOrdreVersement(compIntDec.getIdOrdreVersement());
                        ordreV.retrieve();

                        tiers = PRTiersHelper.getTiersParId(bSession, ordreV.getIdTiers());

                        String nomPrenom = "";
                        if (null != tiers) {
                            nomPrenom = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                        }

                        tableauHTML += "<tr><td width=60%>"
                                + PRStringUtils.replaceString(texte, "{nomPrenom}", nomPrenom) + "</td>";

                        FWCurrency montant = new FWCurrency(compIntDec.getMontant());
                        montant.negate();

                        tableauHTML += "<td colspan=4 align=right width=30%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                + "</td>";
                        tableauHTML += "<td width=10% align=right>" + montant.toStringFormat() + "</td></tr>";
                        montantTotal.add(montant);
                    }
                }

                // 2) Traiter les rentes de type : Rentes déjà versées
                if (!montantRentesDejaVersees.isZero()) {

                    tableauHTML += "<tr><td width=60%>" + documentCommun.getTextes(5).getTexte(7).getDescriptionBrut()
                            + "</td>";

                    montantRentesDejaVersees.negate();

                    tableauHTML += "<td colspan=4 align=right width=30%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                            + "</td>";
                    tableauHTML += "<td width=10% align=right>" + montantRentesDejaVersees.toStringFormat()
                            + "</td></tr>";
                    montantTotal.add(montantRentesDejaVersees);

                    // Voir s'il y a une restitution
                    RESoldePourRestitutionManager soldeMgr = new RESoldePourRestitutionManager();
                    soldeMgr.setSession(bSession);
                    soldeMgr.setForIdPrestation(decision.getPrestation(bSession.getCurrentThreadTransaction())
                            .getIdPrestation());
                    soldeMgr.find();

                    if (!soldeMgr.isEmpty()) {
                        // TODO Remarque dans catalogue de texte avec nom et prénom correspondant
                        String nomsPrenoms = "";

                        for (Iterator<REOrdresVersements> iterator = ovCompenser.iterator(); iterator.hasNext();) {
                            REOrdresVersements ov = iterator.next();

                            RECompensationInterDecisionsManager compIntMgr = new RECompensationInterDecisionsManager();
                            compIntMgr.setSession(bSession);
                            compIntMgr.setForIdOV(ov.getIdOrdreVersement());
                            compIntMgr.find();

                            for (Iterator<RECompensationInterDecisions> iterator2 = compIntMgr.iterator(); iterator2
                                    .hasNext();) {
                                RECompensationInterDecisions compInt = iterator2.next();
                                tiers = PRTiersHelper.getTiersParId(bSession, compInt.getIdTiers());

                                if (null != tiers) {
                                    if (nomsPrenoms.length() == 0) {
                                        nomsPrenoms += tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                    } else {
                                        nomsPrenoms += ", " + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                    }
                                }
                            }
                        }
                        if (!montantPourSolde.isZero()) {
                            montantTotal.add(montantPourSolde);
                        }
                    }
                }

                Set<String> keysCreanciers = creanciers.keySet();

                if (keysCreanciers.size() > 0) {
                    for (Iterator<String> iterator = keysCreanciers.iterator(); iterator.hasNext();) {
                        String idTiersCreancier = iterator.next();

                        PRTiersWrapper tiersCreanciers = PRTiersHelper.getTiersParId(bSession, idTiersCreancier);
                        if (null == tiersCreanciers) {
                            tiersCreanciers = PRTiersHelper.getAdministrationParId(bSession, idTiersCreancier);
                        }

                        String nomCreancier = tiersCreanciers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tiersCreanciers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                        String montantCreancier = creanciers.get(idTiersCreancier);

                        FWCurrency montantCreanciers = new FWCurrency(montantCreancier);
                        montantCreanciers.negate();

                        tableauHTML += "<tr><td width=60%>" + nomCreancier + "</td>";
                        tableauHTML += "<td colspan=4 align=right width=30%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                                + "</td>";
                        tableauHTML += "<td width=10% align=right>" + montantCreanciers.toStringFormat() + "</td></tr>";

                        montantTotal.add(montantCreanciers);
                    }
                }

                if (!montantImpotSource.isZero()) {
                    montantImpotSource.negate();
                    montantTotal.add(montantImpotSource);
                    tableauHTML += "<tr><td width=60%>" + documentCommun.getTextes(5).getTexte(8).getDescriptionBrut()
                            + "</td>";
                    tableauHTML += "<td colspan=4 align=right width=30%>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                            + "</td>";
                    tableauHTML += "<td width=10% align=right>" + montantImpotSource.toStringFormat() + "</td></tr>";
                }

                // BZ 5399, tri des montant par type d'ordre de versement
                if (!montantsACompenser.isEmpty()) {
                    Set<String> montantsKey = montantsACompenser.keySet();

                    for (String key : montantsKey) {
                        FWCurrency montant = montantsACompenser.get(key);
                        montant.negate();

                        StringBuilder montantBuilder = new StringBuilder();
                        montantBuilder.append("<tr>");

                        // ajout de la description du montant à compenser en fonction du type d'ordre de versement
                        montantBuilder.append("<td width=60%>");
                        if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_DECISION.equals(key)) {
                            montantBuilder.append(documentCommun.getTextes(5).getTexte(17));
                        } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RETOUR.equals(key)) {
                            montantBuilder.append(documentCommun.getTextes(5).getTexte(18));
                        } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(key)) {
                            montantBuilder.append(documentCommun.getTextes(5).getTexte(19));
                        } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_AVANCES.equals(key)) {
                            montantBuilder.append(documentCommun.getTextes(5).getTexte(20));
                        } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_PRST_BLOQUE.equals(key)) {
                            montantBuilder.append(documentCommun.getTextes(5).getTexte(21));
                        }
                        montantBuilder.append("</td>");

                        montantBuilder.append("<td colspan=4 align=right width=30%>");
                        montantBuilder.append(bSession.getLabel("JSP_DECOMPTE_D_CHF"));
                        montantBuilder.append("</td>");

                        montantBuilder.append("<td width=10% align=right>");
                        montantBuilder.append(montant.toStringFormat());
                        montantBuilder.append("</td>");

                        montantBuilder.append("</tr>");

                        tableauHTML += montantBuilder.toString();

                        // ajout du montant pour le calcul du total
                        montantTotal.add(montant);
                    }
                }
                // Afficher la remarque pour compensation inter-décisions

                if (isRemarqueCompensation) {
                    String remarque = documentCommun.getTextes(6).getTexte(2).getDescriptionBrut();

                    // BZ 5446, une seule fois chaque tiers dans la remarque
                    Map<String, String> mapIdTiersNomPrenom = new HashMap<String, String>();

                    for (Iterator<REOrdresVersements> iterator = ovCompenser.iterator(); iterator.hasNext();) {
                        REOrdresVersements ov = iterator.next();

                        RECompensationInterDecisionsManager compIntMgr = new RECompensationInterDecisionsManager();
                        compIntMgr.setSession(bSession);
                        compIntMgr.setForIdOV(ov.getIdOrdreVersement());
                        compIntMgr.find();

                        for (Iterator<RECompensationInterDecisions> iterator2 = compIntMgr.iterator(); iterator2
                                .hasNext();) {
                            RECompensationInterDecisions compInt = iterator2.next();

                            tiers = PRTiersHelper.getTiersParId(bSession, compInt.getIdTiers());

                            if (null != tiers) {
                                if (!mapIdTiersNomPrenom.containsKey(tiers
                                        .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                                    mapIdTiersNomPrenom.put(
                                            tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                                            tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                                }
                            }
                        }
                    }

                    StringBuilder nomsPrenoms = new StringBuilder();
                    for (String nomPrenom : mapIdTiersNomPrenom.values()) {
                        if (nomsPrenoms.length() > 0) {
                            nomsPrenoms.append(", ");
                        }
                        nomsPrenoms.append(nomPrenom);
                    }

                    remarque = PRStringUtils.replaceString(remarque, "{nomsPrenoms}", nomsPrenoms.toString());
                    tableauHTML += "<tr><td colspan=4>" + remarque + "</td><td align=right width=30%>"
                            + bSession.getLabel("JSP_DECOMPTE_D_CHF") + "</td><td align=right>"
                            + montantPourSolde.toStringFormat() + "</td></tr>";

                }

                tableauHTML += "<tr><td colspan=6><hr></td></tr>";

                if (montantTotal.isNegative()) {
                    tableauHTML += "<tr><td width=60%><b>"
                            + documentCommun.getTextes(5).getTexte(12).getDescriptionBrut() + "</b></td>";
                } else {
                    tableauHTML += "<tr><td width=60%><b>" + bSession.getLabel("JSP_DECOMPTE_D_VERSEMENT")
                            + "</b></td>";
                }

                tableauHTML += "<td colspan=4 align=right width=30%><b>" + bSession.getLabel("JSP_DECOMPTE_D_CHF")
                        + "</b></td>";
                tableauHTML += "<td align=right width=30%><b>" + montantTotal.toStringFormat() + "</b></td></tr>";

                tableauHTML += "<tr><td colspan=6><br><hr><br></td></tr>";

                RESoldePourRestitutionManager soldeMgr = new RESoldePourRestitutionManager();
                soldeMgr.setSession(bSession);
                soldeMgr.setForIdPrestation(decision.getPrestation(bSession.getCurrentThreadTransaction())
                        .getIdPrestation());
                soldeMgr.find();
                if (!soldeMgr.isEmpty()) {
                    if (!isRemarqueCompensation) {
                        tableauHTML += "<tr><td colspan=6><br><hr><br></td></tr>";
                    }

                    boolean isSPRRetenue = false;
                    boolean isSPREditionBVR = false;
                    boolean isAutre = false;
                    boolean isMontantDifferent = false;
                    FWCurrency montantRetenu = new FWCurrency();
                    FWCurrency montantMensuelARetenir = new FWCurrency();
                    for (int i = 0; i < soldeMgr.size(); i++) {
                        RESoldePourRestitution spr = (RESoldePourRestitution) soldeMgr.get(i);
                        // Insérer une remarque pour indiquer le montant et le
                        // type de la restitution
                        if (spr.getCsTypeRestitution().equals(IRESoldePourRestitution.CS_RETENUES)) {
                            isSPRRetenue = true;

                        } else if (spr.getCsTypeRestitution().equals(IRESoldePourRestitution.CS_EDITIONBVR)) {
                            isSPREditionBVR = true;
                        } else {
                            isAutre = true;
                        }
                        montantMensuelARetenir.add(Double.valueOf(spr.getMontantMensuelARetenir()));
                        montantRetenu.add(Double.valueOf(spr.getMontant()));

                    }

                    if (montantMensuelARetenir.compareTo(montantRetenu) == -1) {
                        isMontantDifferent = true;
                    }

                    String remarque = "";
                    if (isSPRRetenue) {
                        if (isMontantDifferent) {
                            remarque += PRStringUtils.replaceString(documentCommun.getTextes(6).getTexte(8)
                                    .getDescription(), "{montant}", montantMensuelARetenir.toString())
                                    + "\n\n";
                        } else {
                            remarque += documentCommun.getTextes(6).getTexte(3).getDescription() + "\n\n";
                        }
                    }
                    if (isSPREditionBVR) {
                        remarque += documentCommun.getTextes(6).getTexte(4).getDescription() + "\n\n";
                    }
                    if (isAutre) {
                        remarque += documentCommun.getTextes(6).getTexte(5).getDescription() + "\n\n";
                    }

                    tableauHTML += "<tr><td colspan=6>" + remarque + "</td></tr>";

                }

            }
            tableauHTML += "</table></div>";
            vb.setDecompteHTML(tableauHTML);

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    public REDecisionsContainer getDecisionsContainer() {
        return decisionsContainer;
    }

    public void setDecisionsContainer(REDecisionsContainer decisionsContainer) {
        this.decisionsContainer = decisionsContainer;
    }

}
