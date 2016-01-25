<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%

idEcran="PIJ0017";
globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean viewBean = (globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean) session.getAttribute("viewBean");

String idPrononce = request.getParameter("idPrononce");
String csTypeIJ = request.getParameter("csTypeIJ");
String noAVS = request.getParameter("noAVS");
String prenomNom = request.getParameter("prenomNom");
String dateDebutPrononce = request.getParameter("dateDebutPrononce");
String detailRequerant = request.getParameter("detailRequerant");

bButtonCancel = false;
bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
bButtonValidate = bButtonValidate && viewBean.isModifierPermis() && viewBean.getSession().hasRight("ij.prononces.situationProfessionnelle.ajouter", FWSecureConstants.UPDATE);
bButtonDelete = bButtonDelete && viewBean.isModifierPermis();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<script language="JavaScript">

  var servlet = "<%=(servletContext + mainServletPath)%>";

  function calculer(){
  	if(parent.isModification ||(parent.isNouveau && document.forms[0].elements('idAffilieEmployeur').value.length>0)){  		
		warningObj.text="<ct:FWLabel key='JSP_MODIF_PERDUES_WARN'/>";		
		showWarnings();
		parent.isModification=false;
		parent.isNouveau=false;
	}else{
		<%if(IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)){%>
			document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_IJ%>.calculerAit";
		<%}else{%>
			document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_CALCUL_IJ%>.afficher";
		<%}%>

		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
  }

	var warningObj = new Object();
	warningObj.text = "";

	function showWarnings() {
		if (warningObj.text != "") {
			showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
		}
	}

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_SITUATION_PROFESSIONNELLE%>.ajouter"
  }

  function upd() {
     parent.isModification = true;
  }

  function validate() {
    state = true;
    parent.isNouveau = false;
    parent.isModification = false;

    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_SITUATION_PROFESSIONNELLE%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_SITUATION_PROFESSIONNELLE%>.modifier";
    return state;
  }

  function cancel() {
  	//pas de cancel ici
  }

  function del() {
	  if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_SITUATION_PROFESSIONNELLE%>.supprimer";
        document.forms[0].submit();
    }
  }

  function init(){
  	parent.isNouveau = false;
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
    	parent.isNouveau = true;
	<%}%>

  	pourcentClique();
  	document.forms[0].target="fr_main";
  }

  function arret() {
	parent.location.href = servlet + "?userAction=ij.prononces.prononceJointDemande.chercher";
  }

  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly &&
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

  function pourcentClique(){
  	if (document.forms[0].elements('pourcentAutreRemuneration').checked){
  		document.forms[0].elements('csPeriodiciteAutreRemuneration').style.display ='none';
  	} else {
  		document.forms[0].elements('csPeriodiciteAutreRemuneration').style.display ='inline';
  	}
  }

  function heuresSemaineChange(){
  	if (document.forms[0].elements('nbHeuresSemaine').value !=""){
  		document.forms[0].elements('csPeriodiciteSalaire').value = <%=globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_HORAIRE%>

  	}
  }

  function rechercherAffilie(value) {
  	if (value!=""){
		// si le numero d'affilie n'a pas une longueur de 8 -> ###.####
		// on essaye de rajouter le formatage si la longueur vaut 7 ####### -> ###.####
		if(value.length == 7){
			 var valueForm = "";
			 var valueForm = valueForm.concat(value.substring(0, 3),".",value.substring(3,7));
			 document.forms[0].elements('numAffilieEmployeur').value = valueForm;
			 value = valueForm;
		}

	   document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_SITUATION_PROFESSIONNELLE%>.rechercherAffilie";
	   document.forms[0].submit();
	}
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SITPRO"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<INPUT type="hidden" name="idTiersEmployeur" value="<%=viewBean.getIdTiersEmployeur()%>">
								<INPUT type="hidden" name="idGrandeIJ" value="<%=idPrononce%>">
								<INPUT type="hidden" name="modifierPermis" value="<%=viewBean.isModifierPermis()%>">

								<INPUT type="hidden" name="idPrononce" value="<%=idPrononce%>">
								<INPUT type="hidden" name="csTypeIJ" value="<%=csTypeIJ%>">
								<INPUT type="hidden" name="noAVS" value="<%=noAVS%>">
								<INPUT type="hidden" name="prenomNom" value="<%=prenomNom%>">
								<INPUT type="hidden" name="detailRequerant" value="<%=detailRequerant%>">
								<INPUT type="hidden" name="dateDebutPrononce" value="<%=dateDebutPrononce%>">

								<ct:FWLabel key="JSP_SITPRO_ANNEE_CORRESPONDANTE"/>
							</TD>
							<TD colspan="3"><INPUT type="text" name="anneeCorrespondante" value="<%=viewBean.getAnneeCorrespondante()%>" onblur="document.all('selecteurEmployeur').focus();"></TD>
						</TR>
						<TR>
							<TD><LABEL for="nomEmployeur"><ct:FWLabel key="JSP_EMPLOYEUR"/></LABEL></TD>
							<TD>
								<INPUT type="text" name="nomEmployeur" value="<%=viewBean.getNomEmployeur()%>" class="disabled" readonly>
								<ct:FWSelectorTag
									name="selecteurEmployeur"

									methods="<%=viewBean.getMethodesSelecteurEmployeur()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.tiers.tiers.chercher"
									target="fr_main"
									redirectUrl="<%=mainServletPath%>"/>
							</TD>
							<TD><LABEL for="idAffilie"><ct:FWLabel key="JSP_NO_AFFILIE"/></LABEL></TD>
							<TD>
								<% if (viewBean.isRetourDesTiers()){%>
									<INPUT type="hidden" name="versementEmployeur" value ="<%=IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)?Boolean.TRUE:Boolean.FALSE%>">
									<ct:FWListSelectTag data="<%=viewBean.getAffiliationsEmployeur()%>" name="idAffilieEmployeur" defaut="<%=viewBean.getIdAffilieEmployeur()%>"/>
								<%} else {%>
									<INPUT type="text" name="numAffilieEmployeur" value="<%=viewBean.getNumAffilieEmployeur()%>"  onchange="rechercherAffilie(value)" class="montant">
									<INPUT type="hidden" name="idAffilieEmployeur" value ="<%=viewBean.getIdAffilieEmployeur()%>">
									<INPUT type="hidden" name="versementEmployeur" value ="<%=IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)?Boolean.TRUE:Boolean.FALSE%>">
								<%}%>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SITPRO_MONTANT_MAXIMUM"/></TD>
							<TD colspan="3"><INPUT type="checkbox" name="allocationMax" <%=viewBean.getAllocationMax().booleanValue()?"CHECKED":""%>></TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SITPRO_NB_HEURES_SEMAINE"/></TD>
							<TD colspan="3"><INPUT type="text" name="nbHeuresSemaine" value="<%=viewBean.getNbHeuresSemaine()%>" class="numeroCourt" onchange="validateFloatNumber(this);" onkeypress="heuresSemaineChange(); return filterCharForFloat(window.event);"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SITPRO_SALAIRE"/></TD>
							<TD colspan="3" nowrap>
								<INPUT type="text" name="salaire" value="<%=viewBean.getSalaire()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
								<ct:select name="csPeriodiciteSalaire" defaultValue="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getSalaire())?globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_MENSUEL:viewBean.getCsPeriodiciteSalaire()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_GROUPE_PERIODICITE_SALAIRE%>"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SITPRO_AUTRE_REMUNERATION"/></TD>
							<TD colspan="3">
								<INPUT type="text" name="autreRemuneration" value="<%=viewBean.getAutreRemuneration()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
								<INPUT type="checkbox" name="pourcentAutreRemuneration" onclick="pourcentClique();"<%=viewBean.getPourcentAutreRemuneration().booleanValue()?"CHECKED":""%>><ct:FWLabel key="JSP_SITPRO_POURCENT"/>
								<ct:select name="csPeriodiciteAutreRemuneration" defaultValue="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAutreRemuneration())?globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_ANNUEL:viewBean.getCsPeriodiciteAutreRemuneration()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_GROUPE_PERIODICITE_SALAIRE%>"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SITPRO_SALAIRE_NATURE"/></TD>
							<TD colspan="3" nowrap>
								<INPUT type="text" name="salaireNature" value="<%=viewBean.getSalaireNature()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);">
								<ct:select name="csPeriodiciteSalaireNature" defaultValue="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getSalaireNature())?globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_MENSUEL:viewBean.getCsPeriodiciteSalaireNature()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_GROUPE_PERIODICITE_SALAIRE%>"/>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SITPRO_DEPARTEMENT"/></TD>
							<TD colspan="3">
								<ct:FWListSelectTag data="<%=viewBean.getMenuDepartement()%>" defaut="<%=viewBean.getIdParticulariteEmployeur()%>" name="idParticulariteEmployeur"/>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
					<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_SITPRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_SITPRO_ARRET"/>">
					<%if(!(IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ) &&
					     !IIJPrononce.CS_ATTENTE.equals(viewBean.loadPrononce().getCsEtat()))){%>
					     <ct:ifhasright element="ij.acor.calculACORIJ.calculerAit" crud="u">
							<INPUT type="button" value="<ct:FWLabel key="JSP_CALCULER"/> (alt+<ct:FWLabel key="AK_SITPRO_CALCULER"/>)" onclick="calculer()" accesskey="<ct:FWLabel key="AK_SITPRO_CALCULER"/>">
						</ct:ifhasright>
					<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>