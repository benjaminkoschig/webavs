<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML><!-- #BeginTemplate "" -->
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
    globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
    globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
    String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
    String servletContext = request.getContextPath();
    String mainServletPath = (String) request.getAttribute("mainServletPath");
    String actionNew = servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0, request.getParameter("userAction").lastIndexOf('.')) + ".afficher&_method=add";
//String actionFind = "javascript:document.forms[0].submit();";
    String userActionNew = request.getParameter("userAction").substring(0, request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
    boolean bButtonNew = objSession.hasRight(userActionNew, "ADD");
    boolean bButtonFind = true;
    int subTableHeight = 100;

    String IFrameHeight = "350";

    if (mainServletPath == null) {
        mainServletPath = "";
    }
    String idEcran = "CCI0030";
    String btnFindLabel = "Rechercher";
    String btnNewLabel = "Nouveau";
    if ("DE".equals(languePage)) {
        btnFindLabel = "Suchen";
        btnNewLabel = "Neu";
    }
%>
<HEAD>
    <SCRIPT language="JavaScript">
        var langue = "<%=languePage%>";
    </SCRIPT>

    <%


        String userActionUpd = request.getParameter("userAction").substring(0, request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
        globaz.pavo.db.inscriptions.CIJournalViewBean viewBean = (globaz.pavo.db.inscriptions.CIJournalViewBean) session.getAttribute("viewBean");
        IFrameHeight = "180";
        actionNew = "javascript:newEcriture();";


        java.util.HashSet exceptTypeCompteTemporaireSuspend = new java.util.HashSet();
        exceptTypeCompteTemporaireSuspend.add(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS);

    %>
    <%@ page import="globaz.globall.util.*, globaz.pavo.db.inscriptions.CIJournal" %>

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
    <script type="text/javascript" src="<%=servletContext%>/scripts/actions.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/formUtil.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/shortKeys.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/params.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/actionsForButtons.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/autocomplete.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/selectionPopup.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/calendar/CalendarPopup.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/calendar/date.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/ValidationGroups.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/utils.js"></script>
    <script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>
    <%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
    <script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

    <title></title>

    <SCRIPT language="JavaScript">
        <!--
        hide
        this
        script
        from
        non
        javascript - enabled
        browsers

        if ("FR" == langue) {
            shortKeys[82] = 'btnFind';//  R (Rechercher)
            shortKeys[78] = 'btnNew';//   N (Nouveau)
        } else if ("DE" == langue) {
            shortKeys[83] = 'btnFind';//  S (Suchen)
            shortKeys[78] = 'btnNew';//   N (Neu)
        }


        function changeActionAndSubmit(newAction, target) {
            document.forms[0].action = newAction;
            document.forms[0].target = target;
            document.forms[0].submit();
        }

        function setUserAction(newAction) {
            document.forms[0].elements.userAction.value = newAction;
        }

        function setFormAction(newAction) {
            document.forms[0].action = newAction;
        }

        var timeWaiting = 1;
        var timeWaitingId = -1;
        var bFind = true;
        <%if(!"false".equals(viewBean.getForPremiereFoisSurPage())){%>

        bFind = false;
        <%}%>

        var usrAction = "";
        var servlet = "<%=(servletContext + mainServletPath)%>";

        var savedFindOnClick = 'undefined';
        var savedNewOnClick = 'undefined';

        function enableBtn() {

            document.all('btnNew').disabled = false;


        }

        function disableBtn(aBtn) {
            //aBtn.onclick = '';
            //aBtn.style.display = 'none';
            aBtn.disabled = true;
        }

        function onClickNew() {
            disableBtn(document.all('btnNew'));


        }

        function onClickFind() {
            disableBtn(document.all('btnFind'));
            var oBtnNew = document.all('btnNew');
            if (oBtnNew != null) {
                disableBtn(oBtnNew);
            }
        }

        function showButtons() {
            var oBtnNew = document.all('btnNew');
            if (oBtnNew != null) {
                oBtnNew.onclick = savedNewOnClick;
                //oBtnNew.style.display = 'inline';
                oBtnNew.disabled = false;
            }

            var oBtnFind = document.all('btnFind');
            if (oBtnFind != null) {
                oBtnFind.onclick = savedFindOnClick;
                //oBtnFind.style.display = 'inline';
                oBtnFind.disabled = false;
            }
        }

        function setFocus() {
            var defaultFocusElement = document.forms[0].elements[0];
            if (defaultFocusElement == null) {
                return;
            }
            try {
                defaultFocusElement.focus();
            } catch (e) {

            }
        }

        // stop hiding -->
    </SCRIPT>


    <SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/Calculator.js"></SCRIPT>
    <SCRIPT>


        // menu
        //top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-');
        if (parent.fr_detail != null) {
            //parent.location.reload();
            parent.location.href = "<%=(servletContext + mainServletPath)%>?userAction=pavo.compte.ecriture.chercherSurPage"; //&idJournal=<%=viewBean.getIdJournal()%>";
        }

        top.document.title = "CI - Saisie d'inscriptions aux CI";


        usrAction = "pavo.compte.ecriture.listerSurPage";
        servlet = "<%=(servletContext + mainServletPath)%>";


        function newEcriture() {

//Si la page fr_list est vide, on charge la page
            if (fr_detail.document.forms[0] == null) {
                document.getElementById('addNewEcriture').value = true;
                document.getElementById('btnFind').click();
                return;
            }

// On désactive l'enregistrement courant
            if (fr_detail.document.forms[0].all("selectedId").value) {
                fr_list.document.forms[0].all("ecr" + fr_detail.document.forms[0].all("selectedId").value).style.fontWeight = "normal";
            }

            try {
                // mettre le de en mode add si pas déjà fait
                fr_detail.showValidationButtons();
            } catch (e) {

            }
// On affiche les nouveaux champs et on les vide
            fr_list.document.forms(0).all("newEcriture").style.display = "block";

            fr_list.document.forms(0).all("newEcriture").all("partialavsNew").select();
            //fr_list.resetSelection();
            fr_list.document.forms(0).all("newEcriture").all("partialavsNew").value = "";
            fr_list.document.forms(0).all("newEcriture").all("avsNew").value = "";
            fr_list.document.forms(0).all("newEcriture").all("montant").value = "";
            fr_list.document.forms(0).all("newEcriture").all("moisDebut").value = "";
            fr_list.document.forms(0).all("newEcriture").all("moisFin").value = "";
            <% if( globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBean.getIdTypeInscription())
            || "301001".equals(viewBean.getIdTypeInscription()) || "301002".equals(viewBean.getIdTypeInscription())){%>
            fr_list.document.forms(0).all("newEcriture").all("annee").value = "<%=viewBean.getAnneeCotisation()%>";

            <%} else{%>
            fr_list.document.forms(0).all("newEcriture").all("annee").value = "";
            <%}%>
            fr_list.document.forms(0).all("newEcriture").all("gre").value = "";
            fr_list.document.forms(0).all("newEcriture").all("brancheEconomique").value = "";
            fr_list.document.forms(0).all("newEcriture").all("codeSpecial").value = "";
            fr_list.document.forms(0).all("newEcriture").all("code").value = "";
            fr_list.document.forms(0).all("newEcriture").all("caisseChomage").value = "";
            //fr_list.document.forms(0).all("newEcriture").all("idTypeCompte").value = "";

            /*for (i=0; i<fr_list.document.forms(0).all("newEcriture").all("idTypeCompte").options.length; i++) {
                if (fr_list.document.forms(0).all("newEcriture").all("idTypeCompte").options(i).value ==
            <%=viewBean.getIdTypeCompte()%>) {
			fr_list.document.forms(0).all("newEcriture").all("idTypeCompte").selectedIndex = i;
			break;
		}
	}*/
            fr_list.document.forms(0).all("newEcriture").all("partBta").value = "";


            <%if(globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBean.getIdTypeInscription())
            || viewBean.CS_CORRECTIF.equals(viewBean.getIdTypeInscription())
            || viewBean.CS_DECLARATION_SALAIRES.equals(viewBean.getIdTypeInscription()) || "301002".equals(viewBean.getIdTypeInscription())){%>
            fr_list.document.forms(0).all("newEcriture").all("partnerNumnewEcriture").value = "<%=viewBean.getNumeroAffilie()%>";
            fr_list.document.forms(0).all("newEcriture").all("employeurPartenaire").value = "<%=viewBean.getNumeroAffilie()%>";
            fr_list.document.forms(0).all("newEcriture").all("brancheEconomique").value = "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getBrancheEconomique(), session)%>";
//		fr_list.document.forms(0).all("newEcriture").all("partnerNumnewEcriture").class="disabled";

            <%} else{%>

            <%if(CIJournal.CS_ASSURANCE_MILITAIRE.equals(viewBean.getIdTypeInscription())){%>
            fr_list.document.forms(0).all("newEcriture").all("partnerNumnewEcriture").value = "666.66.666.666";
            <%}else if(CIJournal.CS_APG.equals(viewBean.getIdTypeInscription())){%>
            fr_list.document.forms(0).all("newEcriture").all("partnerNumnewEcriture").value = "777.77.777.777";
            <%}else if(CIJournal.CS_PANDEMIE.equals(viewBean.getIdTypeInscription())){%>
            fr_list.document.forms(0).all("newEcriture").all("partnerNumnewEcriture").value = "555.55.555.555";
            <%}else if(CIJournal.CS_IJAI.equals(viewBean.getIdTypeInscription())){%>
            fr_list.document.forms(0).all("newEcriture").all("partnerNumnewEcriture").value = "888.88.888.888";
            <%}else{%>
            fr_list.document.forms(0).all("newEcriture").all("partnerNumnewEcriture").value = "";
            <%}%>
            fr_list.document.forms(0).all("newEcriture").all("employeurPartenaire").value = "";
            fr_list.document.forms(0).all("newEcriture").all("mitgliedNumnewEcriture").value = "";
            fr_list.document.forms[0].all("newEcriture").all("mitgliedNumnewEcriture").onkeypress = "return filterCharForPositivFloat(window.event);"
            <%}%>
// On modifie les boutons dans les autres frames
            //document.images("btnNew").style.display="none"
            //fr_detail.document.images("btnNew").style.display="none";
            //fr_detail.document.images("buttons").src='<%=request.getContextPath()%>/images/<%=languePage%>/btnOkCancel.gif';
            //fr_detail.document.images("buttons").useMap="#btnOkCancel";

// On libère les champs d'entrée et valeurs vides
            fr_detail.document.forms[0].elements("avs").value = "";
            fr_detail.document.forms[0].elements("remarque").value = "";
            fr_detail.document.forms[0].elements("nomPrenom").value = "";
            fr_detail.document.forms[0].elements("dateDeNaissance").value = "";
            fr_detail.document.forms[0].elements("sexe").seletecIndex = "0";
            fr_detail.document.forms[0].elements("messages").seletecIndex = "0";
            fr_detail.document.forms[0].elements("paysOrigine").value = "";
            fr_detail.document.forms[0].elements("paysOrigineLabel").value = "";
            fr_detail.document.forms[0].elements('dateNaissanceNNSS').value = "";
            fr_detail.document.forms[0].elements('sexeNNSS').value = "";
            fr_detail.document.forms[0].elements('paysNNSS').value = "";
            //fr_detail.document.forms[0].elements("dernieresEcritures").value = "";
            //fr_detail.document.getElementById('dernieresEcritures').innerHTML = '';
            fr_detail.dernieresEcritures.location.href = '<%=request.getContextPath()%>/pavoRoot/lastentries.jsp';
            fr_detail.document.forms[0].elements("remarque").disabled = false;
            fr_detail.document.forms[0].elements("nomPrenom").disabled = false;
            fr_detail.document.forms[0].elements("dateDeNaissance").disabled = false;
            fr_detail.document.forms[0].elements("sexe").disabled = false;
            fr_detail.document.forms[0].elements("messages").disabled = false;
            fr_detail.document.forms[0].elements("paysOrigine").disabled = false;

// Le champs _method de ecriture_caDetail.jsp à add
            fr_detail.document.forms[0].elements("_method").value = "add";
//on gère le scroll
            fr_detail.scrollWin();
            fr_list.document.forms(0).all("newEcriture").all("partialavsNew").focus();
            fr_list.document.forms(0).all("newEcriture").all("partialavsNew").select();


        }

        function handleAmount(value) {
            value = new String(value);
            if (value.length != 0) {
                if (value.charAt(value.length - 2) == '.') {
                    value += '0';
                } else if (value.charAt(value.length - 1) == '.') {
                    value += '00';
                } else if (value.charAt(value.length - 3) != '.' && value.indexOf('.') == -1) {
                    value += '.00';
                }
            } else {
                value = '0.00';
            }
            if (value.charAt(value.length - 3) == '.' && value.length > 6 && value.charAt(value.length - 7) != "'" && value.charAt(value.length - 7) != "-") {
                value = value.substring(0, value.length - 6) + "'" + value.substring(value.length - 6, value.length);
            }
            if (value.charAt(value.length - 3) == '.' && value.length > 10 && value.charAt(value.length - 11) != "'" && value.charAt(value.length - 11) != "-") {
                value = value.substring(0, value.length - 10) + "'" + value.substring(value.length - 10, value.length);
            }
            if (value.charAt(value.length - 3) == '.' && value.length > 14 && value.charAt(value.length - 15) != "'" && value.charAt(value.length - 15) != "-") {
                value = value.substring(0, value.length - 14) + "'" + value.substring(value.length - 14, value.length);
            }
            return value;
        }

        function deFormatAmount(value) {
            var result = "";
            for (i = 0; i < value.length; i++) {
                c = value.charAt(i);
                if (c != "'") {
                    result = result.concat(c);
                }
            }
            return result;
        }

        var timeWaiting = 1;
        var timeWaitingId = -1;
        var totalCtrl = <%=viewBean.getTotalControle()%>;
        var totalInsc = <%=viewBean.getTotalInscrit()%>;

        function initTotaux() {
            document.getElementById('totalCtrl').value = handleAmount(totalCtrl);
            document.getElementById('totalInsc').value = handleAmount(totalInsc);
            calculSolde();
        }

        function calculSolde(ctrl) {
            if (ctrl) {
                totalCtrl = deFormatAmount(ctrl);
                document.getElementById('totalCtrl').value = handleAmount(totalCtrl);
            }

            document.getElementById('solde').value = handleAmount(Subtraction(totalCtrl, totalInsc));
            updateTotauxList();
        }

        function updateInsc(inscr) {
            totalInsc = inscr;
            document.getElementById('totalInsc').value = handleAmount(totalInsc);
            calculSolde();
        }

        function updateTotauxList() {
            if (document.fr_list.document.forms[0]) {
                //document.fr_list.document.forms[0].all("totaux").all("controle").innerText = handleAmount(totalCtrl);
                //document.fr_list.document.forms[0].all("totaux").all("inscrit").innerText = handleAmount(totalInsc);
                //document.fr_list.document.forms[0].all("totaux").all("solde").innerText = handleAmount(totalCtrl-totalInsc);
            }
        }

    </SCRIPT>

</HEAD>
<!--
<BODY onLoad="this.focus();if(document.forms[0].elements[0] != null){document.forms[0].elements[0].focus();} ;setFormAction(servlet);setUserAction(usrAction);if(bFind) {alert('test');document.forms[0].submit();}" onKeyDown="keyDown()" onKeyUp="keyUp();">
-->
<BODY onLoad="initTotaux();setFormAction(servlet);setUserAction(usrAction);document.getElementById('fr_list').onreadystatechange=fnStartInit;if(bFind){document.forms[0].submit();}"
      onKeyDown="keyDown()" onKeyUp="keyUp();">
<FORM name="mainForm" action="<%=(servletContext + mainServletPath)%>" target="fr_list"
      onsubmit="onClickFind();showWaitingPopup()">
    <TABLE class="find" cellspacing="0" cellpadding="0" width="100%" height="100">
        <TBODY>
        <TR>
            <TH class="title" colspan="2">
                <DIV style="width: 100%">
                    <SPAN style="float:right; width:100px; font-weight: normal;text-align:right; font-size:8pt"><%=(null == idEcran) ? "" : idEcran%></SPAN>
                    Saisie d'inscriptions aux CI
                </DIV>
            </TH>
        </TR>
        <TR>
            <TD bgcolor="#FFFFFF" colspan="2" height="3"></TD>
        </TR>
        <TR>
            <TD width="5">&nbsp;</TD>
            <TD>
                <TABLE border="0" height="100" cellspacing="0" cellpadding="0">
                    <TBODY>
                    <TR>
                        <TD height="20">&nbsp;</TD>
                    </TR>

                    <INPUT type="hidden" name="addNewEcriture" value='<%=request.getParameter("addNewEcriture")%>'>
                    <INPUT type="hidden" name="premiereFoisSurPage" value="false">

                    <TR>
                        <TD width="60">Journal</TD>
                        <TD>
                            <INPUT type="text" class="disabled" readonly name="idJournal" size="11"
                                   value="<%=viewBean.getIdJournal()%>" tabIndex="-1">
                            <INPUT type="hidden" name="forIdJournal" value="<%=viewBean.getIdJournal()%>">
                        </TD>
                        <TD colspan="3"><INPUT type="text" class="disabled" readonly name="infoAffilie" size="50"
                                               value="<%=viewBean.getDescription()%>" tabIndex="-1"></TD>
                        <TD width="10"></td>
                        <TD width="100">Total de contrôle</td>
                        <TD><INPUT type="text" name="totalCtrl" class="montantDisabled" class="disabled" readonly
                                   value="" size="18" onChange='calculSolde(this.value)'></TD>
                    </TR>
                    <TR>
                        <TD width="60">NSS</TD>
                        <TD><nss:nssPopup name="fromAvs" avsMinNbrDigit="99" avsAutoNbrDigit="99" nssAutoNbrDigit="99"
                                          nssMinNbrDigit="99"
                                          value='<%=viewBean.getFromAvsSauvegarde()==null?"":viewBean.getFromAvsSauvegarde()%>'
                                          newnss="<%=viewBean.getFromAvsSauvegardeNNSS()%>"/></TD>

                        <INPUT type="hidden" name="tri" value="avs">

                        <TD width="10"></TD>
                        <TD>Montant</TD>
                        <TD><INPUT type="text" name="queryRevenu" size="15"
                                   value='<%=viewBean.getForMontantSauvegarde()==null?"":viewBean.getForMontantSauvegarde()%>'>
                        </TD>
                        <TD width="10"></TD>
                        <TD width="100">Total actuel</TD>
                        <TD><input type="text" class="montantDisabled" readonly tabIndex="-1" name="totalInsc" value=""
                                   size="18"></TD>
                    </TR>
                    <TR>
                        <TD width="60">Ann&eacute;e</TD>
                        <TD><INPUT type="text" name="forAnnee"
                                   value='<%=viewBean.getForAnneeSauvegarde()==null?"":viewBean.getForAnneeSauvegarde()%>'
                                   maxlength="4" size="4"></TD>
                        <TD width="10"></TD>
                        <TD>Genre</td>
                        <TD><ct:FWCodeSelectTag name="forIdTypeCompte" defaut="" codeType="CITYPCOM"
                                                except="<%=exceptTypeCompteTemporaireSuspend%>" wantBlank="true"/></TD>
                        <TD width="10"></td>
                        <TD width="100">Solde</TD>
                        <TD><INPUT type="text" name="solde" class="montantDisabled" readonly tabIndex="-1" value=""
                                   size="18"></TD>
                    </TR>
                    <TR>
                        <TD height="20">
                            <INPUT type="hidden" name="userAction" value="">
                            <INPUT type="hidden" name="_type" value='<%=request.getParameter("_type")%>'>
                            <INPUT type="hidden" name="_section" value='<%=request.getParameter("_section")%>'>
                            <INPUT type="hidden" name="_dest" value='<%=request.getParameter("_dest")%>'>
                            <INPUT type="hidden" name="_sl" value="">
                            <INPUT type="hidden" name="_method" value="">
                            <INPUT type="hidden" name="_valid" value=''>
                        </TD>
                    </TR>
                    </TBODY>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <%
                if (!viewBean.getProprietaireNomComplet().equals(objSession.getUserFullName())) {
                    bButtonNew = false;
                }
            %>

            <%
                if (globaz.pavo.util.CIUtil.isSpecialist(session)) {
                    bButtonNew = true;
                }
            %>

            <%
                if (globaz.pavo.db.inscriptions.CIJournal.CS_COMPTABILISE.equals(viewBean.getIdEtat()) || (globaz.pavo.db.inscriptions.CIJournal.CS_PARTIEL.equals(viewBean.getIdEtat()) && !globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_FACULTATIVE.equals(viewBean.getIdTypeInscription()))) {
                    bButtonNew = false;
                }
            %>

            <TD bgcolor="#FFFFFF" colspan="2" align="right">
                <%if (bButtonFind) {%>
                <INPUT type="submit" name="btnFind" value="<%=btnFindLabel%>">
                <%
                    }
                    if (bButtonNew) {
                %>
                <INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="onClickNew();<%=actionNew%>">
                <%}%>
            </TD>
        </TR>
        </TBODY>
    </TABLE>
</FORM>

<div id="waitingPopup" style="width:120;height:50;position : absolute ; visibility : hidden">
    <table border="0" cellspacing="0" cellpadding="0" bgColor="#FFFFFF"
           style="border: solid  1 black ; width:200;height:100%;">
        <tr>
            <td><img src="<%=request.getContextPath()%>/images/<%=languePage%>/labelRecherche.gif"></td>
            <td><img src="<%=request.getContextPath()%>/images/points.gif"></td>
            <td><img src="<%=request.getContextPath()%>/images/disc.gif"></td>
        </tr>
    </table>
</div>

<%
    if ((CIJournal.CS_DECLARATION_SALAIRES.equals(viewBean.getIdTypeInscription()) || CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(viewBean.getIdTypeInscription()))
            && !CIJournal.CS_COMPTABILISE.equals(viewBean.getIdEtat())) {
%>
<ct:menuChange displayId="options" menuId="journal-detailDeclaration" showTab="options" checkAdd="false">
    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailDeclaration"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailDeclaration"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailDeclaration"
                         checkAdd="false"/>
</ct:menuChange>
<%
} else if ((CIJournal.CS_DECISION_COT_PERS.equals(viewBean.getIdTypeInscription()) || CIJournal.CS_COTISATIONS_PERSONNELLES.equals(viewBean.getIdTypeInscription()))
        && !CIJournal.CS_COMPTABILISE.equals(viewBean.getIdEtat())) {
%>
<ct:menuChange displayId="options" menuId="journal-detailCotPers" showTab="options" checkAdd="false">
    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailCotPers"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailCotPers"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="libelle" value="<%=viewBean.getLibelle()%>" menuId="journal-detailCotPers"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detailCotPers"
                         checkAdd="false"/>
</ct:menuChange>
<%} else if (!CIJournal.CS_COMPTABILISE.equals(viewBean.getIdEtat())) {%>
<ct:menuChange displayId="options" menuId="journal-detail" showTab="options" checkAdd="false">
    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail" checkAdd="false"/>
    <ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journal-detail"
                         checkAdd="false"/>
</ct:menuChange>
<%} else {%>
<ct:menuChange displayId="options" menuId="journalAlreadyInscrit" showTab="options" checkAdd="false">
    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" menuId="journalAlreadyInscrit"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>" menuId="journalAlreadyInscrit"
                         checkAdd="false"/>
    <ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>" menuId="journalAlreadyInscrit"
                         checkAdd="false"/>
</ct:menuChange>
<%}%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT>

    function showWaitingPopup() {
        if (timeWaiting != -1 && timeWaitingId == -1)
            timeWaitingId = setTimeout("if (document.getElementById('fr_list').readyState!='complete') document.getElementById('waitingPopup').style.visibility='visible';", timeWaiting * 1000);
        return true;
    }

    document.getElementById("waitingPopup").style.left = document.body.clientWidth / 2 - document.getElementById("waitingPopup").offsetWidth / 2;
    document.getElementById("waitingPopup").style.top = getAnchorPosition("waitingPopup").y + <%=IFrameHeight%>/2 - document.getElementById("waitingPopup").clientHeight/
    2;
    if (bFind)
        showWaitingPopup();

    function fnStartInit() {
        if (document.getElementById("fr_list").readyState == "complete") {
            showButtons();
            document.getElementById("waitingPopup").style.visibility = "hidden";
            timeWaitingId = -1;
            updateTotauxList();
        }
    }

    oBtnFind = document.getElementById("btnFind");
    if (oBtnFind) {
        savedFindOnClick = oBtnFind.onclick;
    }
    oBtnNew = document.getElementById("btnNew");
    if (oBtnNew) {
        savedNewOnClick = oBtnNew.onclick;
    }

    /*	if (document.getElementById("btnFind"))
            document.getElementById("btnFind").onclick=showWaitingPopup;*/
</SCRIPT>


<IFRAME valign="top" name="fr_list" scrolling="YES" style="border : solid 1px black; width:100%" height="170">
</IFRAME>
<PRE></PRE>
<IFRAME valign="top" name="fr_detail" scrolling="NO" style="width:100%" height="220">
</IFRAME>

</BODY>
</HTML>