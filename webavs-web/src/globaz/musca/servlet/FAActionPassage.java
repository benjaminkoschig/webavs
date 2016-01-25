package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageListViewBean;
import globaz.musca.db.facturation.FAPassageViewBean;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionPassage extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionPassage(FWServlet servlet) {
        super(servlet);
    }

    private void _addNbDecompte(FAPassageListViewBean viewBean) throws Exception {
        if (viewBean.size() > 0) {
            BSession ses = viewBean.getSession(); // (BSession)mainDispatcher.getSession();
            StringBuffer passageList = new StringBuffer();
            for (Iterator<FAPassage> it = viewBean.iterator(); it.hasNext();) {
                FAPassage p = it.next();
                passageList.append(p.getIdPassage());
                if (it.hasNext()) {
                    passageList.append(",");
                }
            }
            // sum(TOTALFACTURE)
            Map<String, String> enteteByDecompte = TISQL.queryMap(ses, "IDPASSAGE IDPASSAGE", "count(*) CPT", "from "
                    + TIToolBox.getCollection() + "FAENTFP where idPassage in ( " + passageList.toString()
                    + ") group by idpassage");
            viewBean.setNbDecomptesParPassage(enteteByDecompte);

            /*
             * Summary ( en prévision de la version 1-6)
             */

            List<Map<String, String>> res = null;
            int type_summary = 0; // 0-> none, 1 catégory, 2 rubriques
            try {
                type_summary = Integer.parseInt(viewBean.getTypeSummary());
            } catch (Exception e) {
                type_summary = 0;
            }
            // type_summary = 3;
            String langue = viewBean.getSession().getIdLangueISO().toUpperCase();

            int type_libelle = 0; // defaut : libelle simple;
            switch (type_summary) {

                case 1:
                    /*
                     * Groupement par sous type de facture
                     */
                    res = TISQL.query(ses, "e.IDPASSAGE IDPASSAGE, e.IDSOUSTYPE LIBELLE, count(*) CPT",
                            "from " + TIToolBox.getCollection() + "FAENTFP e " + "where e.idPassage in ( "
                                    + passageList.toString()
                                    + ") group by e.idpassage,e.IDSOUSTYPE order by e.IDSOUSTYPE");
                    type_libelle = 1; // libelle CS
                    break;
                case 2:
                    /*
                     * Groupement par rubrique
                     */
                    res = TISQL.query(
                            ses,
                            "e.IDPASSAGE IDPASSAGE, t.LIBELLE LIBELLE,r.IDEXTERNE NUM, count(*) CPT",
                            "from " + TIToolBox.getCollection() + "FAENTFP e " + "inner join "
                                    + TIToolBox.getCollection()
                                    + "FAAFACP a on (a.identetefacture = e.identetefacture)" + "inner join "
                                    + TIToolBox.getCollection() + "CARUBRP r on (a.idrubrique = r.idrubrique) "
                                    + "inner join " + TIToolBox.getCollection()
                                    + "PMTRADP t on (t.idtraduction = r.idtraduction and CODEISOLANGUE='" + langue
                                    + "') " + "where e.idPassage in ( " + passageList.toString()
                                    + ") group by e.idpassage,t.LIBELLE,r.IDEXTERNE order by r.IDEXTERNE,t.LIBELLE");
                    type_libelle = 2; // num + libelle
                    break;

                case 3:
                    /*
                     * Groupement par secteur
                     */
                    res = TISQL.query(
                            ses,
                            "e.IDPASSAGE IDPASSAGE,s.idsecteur num, t.libelle LIBELLE, count(*) CPT",
                            "from " + TIToolBox.getCollection() + "FAENTFP e " + "inner join "
                                    + TIToolBox.getCollection()
                                    + "FAAFACP a on (a.identetefacture = e.identetefacture)" + "inner join "
                                    + TIToolBox.getCollection() + "CARUBRP r on (a.idrubrique = r.idrubrique) "
                                    + "inner join " + TIToolBox.getCollection()
                                    + "CASECOP s on (s.idsecteur = r.idsecteur ) " + "inner join "
                                    + TIToolBox.getCollection()
                                    + "PMTRADP t on (t.idtraduction = s.idtraduction and CODEISOLANGUE='" + langue
                                    + "') " + "where e.idPassage in ( " + passageList.toString()
                                    + ") group by e.idpassage,s.idsecteur,t.libelle order by s.idsecteur,t.libelle");
                    type_libelle = 2; // num + libelle
                    break;

                case 4:
                    /*
                     * Groupement par type de total (cerdit, débit ou zero)
                     */
                    List<Map<String, String>> resQuery = TISQL.query(ses, "e.IDPASSAGE IDPASSAGE, "
                            + "COUNT(case when e.totalfacture > 0 then e.identetefacture end) deb, "
                            + "COUNT(case when e.totalfacture < 0 then e.identetefacture end) cre, "
                            + "COUNT(case when e.totalfacture = 0 then e.identetefacture end) zero ",
                            " from " + TIToolBox.getCollection() + "FAENTFP e " + "where e.idPassage in ( "
                                    + passageList.toString() + ") group by e.idpassage");

                    res = new ArrayList<Map<String, String>>();
                    for (Map<String, String> row : resQuery) {
                        Map<String, String> m = null;
                        m = new HashMap<String, String>();
                        m.put("IDPASSAGE", row.get("IDPASSAGE"));
                        m.put("LIBELLE", "montant(s) positif(s)");
                        m.put("CPT", row.get("DEB"));
                        res.add(m);

                        m = new HashMap<String, String>();
                        m.put("IDPASSAGE", row.get("IDPASSAGE"));
                        m.put("LIBELLE", "montant(s) négatif(s) :");
                        m.put("CPT", row.get("CRE"));
                        res.add(m);

                        m = new HashMap<String, String>();
                        m.put("IDPASSAGE", row.get("IDPASSAGE"));
                        m.put("LIBELLE", "montant à 0 :");
                        m.put("CPT", row.get("ZERO"));
                        res.add(m);
                    }
                    break;

                case 5:
                    /*
                     * Groupement par module
                     */
                    res = TISQL.query(
                            ses,
                            "e.IDPASSAGE IDPASSAGE, m.LIBELLE" + langue + " LIBELLE, count(*) CPT",
                            "from " + TIToolBox.getCollection() + "FAENTFP e " + "inner join "
                                    + TIToolBox.getCollection()
                                    + "FAAFACP a on (a.identetefacture = e.identetefacture)" + "inner join "
                                    + TIToolBox.getCollection() + "FAMODUP m on (m.IDMODFAC = a.IDMODFAC)" +

                                    "where e.idPassage in ( " + passageList.toString()
                                    + ") group by e.idpassage,m.LIBELLE" + langue + " order by m.LIBELLE" + langue);

                    break;

                case 6:
                    /*
                     * Groupement par numéro de décompte
                     */
                    res = TISQL.query(ses, "e.IDPASSAGE IDPASSAGE, e.IDEXTERNEFACTURE LIBELLE, count(*) CPT",
                            "from " + TIToolBox.getCollection() + "FAENTFP e " + "where e.idPassage in ( "
                                    + passageList.toString()
                                    + ") group by e.idpassage,e.IDEXTERNEFACTURE order by e.IDEXTERNEFACTURE");
                    break;

            }
            if (res != null) {

                /*
                 * Groupement par idpassage
                 */
                Map<String, List<Map<String, String>>> nbCotiByPassage = JadeListUtil.groupBy(res,
                        new JadeListUtil.Key<Map<String, String>>() {
                            @Override
                            public String exec(Map<String, String> e) {
                                return e.get("IDPASSAGE");
                            }
                        });

                /*
                 * Modification du libelle des passages pour inclure les summaries
                 */
                for (Iterator<FAPassage> it = viewBean.iterator(); it.hasNext();) {
                    FAPassage p = it.next();
                    List<Map<String, String>> coti = nbCotiByPassage.get(p.getIdPassage());
                    p.setLibelle("<b>" + p.getLibelle() + "</b>");
                    if (coti != null) {
                        p.setLibelle(p.getLibelle() + "<table style='margin-left:0.2cm'>");

                        /*
                         * Pour chaque ligne du summary
                         */
                        for (Map<String, String> c : coti) {
                            String libelle = "";
                            switch (type_libelle) {
                                case 1:
                                    libelle = ses.getCodeLibelle(c.get("LIBELLE"));
                                    break;
                                case 2:
                                    libelle = c.get("NUM") + ", " + c.get("LIBELLE");
                                    break;
                                default:
                                    libelle = c.get("LIBELLE");
                                    break;
                            }
                            p.setLibelle(p.getLibelle() + "<tr><td align='right'>" + c.get("CPT")
                                    + "</td><td>-</td><td>" + libelle + "</td><tr>");
                        }
                        p.setLibelle(p.getLibelle() + "</table>");
                    }
                }
            }
        }
    }

    protected void actionAnnuler(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        FWAction action = null;

        try {

            String selectedId = request.getParameter("selectedId");

            /*
             * Récupération du viewBean
             */
            FAPassageViewBean viewBean = (FAPassageViewBean) session.getAttribute("viewBean");
            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                // CAInteretMoratoireListViewBean interetMana = new CAInteretMoratoireListViewBean();
                // CAInteretMoratoireViewBean interetVB = new CAInteretMoratoireViewBean();
                CAInteretMoratoireManager interetMana = new CAInteretMoratoireManager();
                CAInteretMoratoire interet = new CAInteretMoratoire();
                BISession osirisSession = GlobazSystem.getApplication("OSIRIS").newSession(
                        globaz.musca.translation.CodeSystem.getSession(session));
                interetMana.setISession(osirisSession);
                interetMana.setForIdJournalFacturation(selectedId);
                interetMana.find(BManager.SIZE_NOLIMIT);
                if (interetMana.size() > 0) {
                    for (int i = 0; i < interetMana.size(); i++) {
                        interet = (CAInteretMoratoire) interetMana.getEntity(i);
                        if (interet.getIdJournalCalcul().equals(interet.getIdJournalFacturation())) {
                            interet.wantCallMethodBefore(false);
                            interet.delete();
                        } else {
                            interet.setIdJournalFacturation("0");
                            interet.update();
                        }
                    }
                }

                // On change l'état du Passage et on le met à jour
                viewBean.setStatus(FAPassage.CS_ETAT_ANNULE);
                viewBean.update();

                CASection section = new CASection();
                CASectionManager sectionMana = new CASectionManager();
                sectionMana.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));
                sectionMana.setForModeCompensationEquals(APISection.MODE_REPORT);
                sectionMana.setForIdPassageComp(selectedId);
                sectionMana.find();
                if (sectionMana.size() > 0) {
                    for (int i = 0; i < sectionMana.size(); i++) {
                        section = (CASection) sectionMana.getEntity(i);
                        section.setIdPassageComp("");
                        section.update();
                    }
                }

                /*
                 * choix destination
                 */
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    destination = getActionFullURL() + ".succesAnnuler";
                    // destination = getRelativeURL(request,session) + "_rc.jsp";
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher (destination).forward (request, response);
        goSendRedirect(destination, request, response);

    }

    protected void actionConfirmAnnuler(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        FWAction action = null;

        try {

            String selectedId = request.getParameter("selectedId");
            /*
             * recuperation du bean depuis la session
             */
            // FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageViewBean viewBean = new FAPassageViewBean();
            // FAPassageViewBean viewBean = mainDispatcher.dispatch(viewBean, super.getAction());

            viewBean.setIdPassage(selectedId);
            viewBean.setAction(FWAction.ACTION_MODIFIER);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));
            viewBean.retrieve();
            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "ConfirmationAnnuler_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    protected void actionConfirmDevalider(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";
        FWAction action = null;

        try {

            String selectedId = request.getParameter("selectedId");

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageViewBean viewBean = new FAPassageViewBean();

            viewBean.setIdPassage(selectedId);
            viewBean.setAction(FWAction.ACTION_MODIFIER);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));
            viewBean.retrieve();
            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "ConfirmationDevalider_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    protected void actionConfirmValider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        FWAction action = null;

        try {

            String selectedId = request.getParameter("selectedId");

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageViewBean viewBean = new FAPassageViewBean();

            viewBean.setIdPassage(selectedId);
            viewBean.setAction(FWAction.ACTION_MODIFIER);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));
            viewBean.retrieve();
            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "ConfirmationValider_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("confirmationAnnuler")) {
            // chercher avec chargement des données nécessaire
            actionConfirmAnnuler(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("confirmationDevalider")) {
            actionConfirmDevalider(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("confirmationValider")) {
            actionConfirmValider(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("annuler")) {
            // chercher avec chargement des données nécessaire
            actionAnnuler(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("devalider")) {
            actionDevalider(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("valider")) {
            actionValider(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("succesAnnuler")) {
            actionSuccesAnnuler(session, request, response, dispatcher);
        }
    }

    protected void actionDevalider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        FWAction action = null;

        try {

            String selectedId = request.getParameter("selectedId");

            /*
             * Récupération du viewBean
             */
            FAPassageViewBean viewBean = (FAPassageViewBean) session.getAttribute("viewBean");
            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                // On change l'état du Passage et on le met à jour
                viewBean.setStatus(FAPassage.CS_ETAT_TRAITEMENT);
                viewBean.update();

                /*
                 * choix destination
                 */
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    destination = getActionFullURL() + ".succesAnnuler";
                    // destination = getRelativeURL(request,session) + "_rc.jsp";
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher (destination).forward (request, response);
        goSendRedirect(destination, request, response);

    }

    /**
	 * 
	 */
    @Override
    protected void actionFindNext(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("listViewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            _addNbDecompte((FAPassageListViewBean) viewBean);

            request.setAttribute("viewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
	 * 
	 */
    @Override
    protected void actionFindPrevious(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("listViewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            _addNbDecompte((FAPassageListViewBean) viewBean);

            request.setAttribute("viewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {

            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            /*
             * creation automatique du listviewBean
             */
            FAPassageListViewBean viewBean = (FAPassageListViewBean) FWListViewBeanActionFactory.newInstance(_action,
                    mainDispatcher.getPrefix());

            /*
             * set automatique des properietes du listViewBean depuis la requete
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeLister() , puis appelle du dispatcher, puis le bean est mis en request
             */
            viewBean = (FAPassageListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (FAPassageListViewBean) mainDispatcher.dispatch(viewBean, _action);

            _addNbDecompte(viewBean);

            request.setAttribute("viewBean", viewBean);

            // pour bt [...] et pagination
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            /*
             * destination : remarque : si erreur, on va quand meme sur la liste avec le bean vide en erreur
             */
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionSuccesAnnuler(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        _destination = getRelativeURL(request, session) + "_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionValider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        FWAction action = null;

        try {

            String selectedId = request.getParameter("selectedId");

            /*
             * Récupération du viewBean
             */
            FAPassageViewBean viewBean = (FAPassageViewBean) session.getAttribute("viewBean");
            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                // On change l'état du Passage et on le met à jour
                viewBean.setStatus(FAPassage.CS_ETAT_VALIDE);
                viewBean.update();

                /*
                 * choix destination
                 */
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    destination = getActionFullURL() + ".succesAnnuler";
                    // destination = getRelativeURL(request,session) + "_rc.jsp";
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher (destination).forward (request, response);
        goSendRedirect(destination, request, response);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAPassageListViewBean vBean = (FAPassageListViewBean) viewBean;
        vBean.setForStatus(request.getParameter("forStatus"));
        // Positionnement selon la zone "A partir de " daans la langue de l'utilisateur
        if (request.getParameter("tri").equalsIgnoreCase(FAPassage.CS_TRI_LIBELLE)) {
            vBean.setForLibelleLike(request.getParameter("forLibelleLike"));
            vBean.orderByLibelle();
        }
        if (request.getParameter("tri").equalsIgnoreCase(FAPassage.CS_TRI_NUMERO)) {
            if (JadeStringUtil.isIntegerEmpty(request.getParameter("fromIdPassage"))) {
                vBean.orderByIdPassageDecroissant();
            } else {
                vBean.setFromIdPassage(request.getParameter("fromIdPassage"));
                vBean.orderByIdPassageCroissant();
            }
        }
        if (request.getParameter("tri").equalsIgnoreCase(FAPassage.CS_TRI_DATEFACTURATION)) {
            if (JAUtil.isDateEmpty(request.getParameter("fromDateFacturation"))) {
                vBean.orderByDateFacturationDecroissant();
            } else {
                vBean.setFromDateFacturation(request.getParameter("fromDateFacturation"));
                vBean.orderByDateFacturationCroissant();
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(request.getParameter("idPlanFacturation"))) {
            vBean.setForIdPlanFacturation(request.getParameter("idPlanFacturation"));
        }
        vBean.setForPassageBloque(null);

        return vBean;
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAPassageViewBean vBean = (FAPassageViewBean) viewBean;
        vBean.setStatus(FAPassage.CS_ETAT_OUVERT);
        return vBean;
    }
}
