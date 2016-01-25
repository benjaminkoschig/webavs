<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.pegasus.vb.droit.PCDroitViewBean"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>

<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="java.util.Vector"%>
<%@page import="ch.globaz.pegasus.businessimpl.checkers.droit.DroitChecker"%><script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	idEcran="PPC0005";
	//Les labels de cette page commence par le préfix "JSP_PC_DRO_R"

 	rememberSearchCriterias = true;
 	String idDroit = request.getParameter("idDroit");
	String idDemandePc = request.getParameter("idDemandePc");
	String isFratrie = request.getParameter("isFratrie");
	String idDossier = request.getParameter("idDossier");
	
	/*if (JadeStringUtil.isNull(idDossier)){
		idDossier = "";
		globaz.pegasus.vb.droit.PCDroitViewBean viewBean = (globaz.pegasus.vb.droit.PCDroitViewBean)  session.getAttribute("viewBean");
		idDossier = viewBean.getIdDossier();
		idDossier = session.getAttribute("idDossier");
	}*/
	
	if(JadeStringUtil.isNull(isFratrie)){
		isFratrie = "0";
	}
	
	if (JadeStringUtil.isNull(idDemandePc)){
		idDemandePc =  request.getParameter("idDemande");
		if (JadeStringUtil.isNull(idDemandePc)){
			idDemandePc="";
		}
	}
	
	if (JadeStringUtil.isNull(idDroit)){
		idDroit =  "";
	}
	
	
	bButtonNew = !DroitChecker.existDroit(idDemandePc); //!JadeStringUtil.isEmpty(idDemandePc);

	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PC_DRO_R_TRIER_PAR_NOM")});
	if(isFratrie.equals("1")){
		actionNew = servletContext + mainServletPath + "?userAction=" + IPCActions.ACTION_FRATRIE + ".afficher&_method=add&idDemandePc="+idDemandePc+"&idDroit="+idDroit;
	}else{
		actionNew =  servletContext + mainServletPath + "?userAction=" + IPCActions.ACTION_DROIT + ".ajouter&_method=add&idDemande="+idDemandePc;
	}
	
	//message d'erreurs du calculateurs
	String idCalculErrorMsg=request.getParameter("calculErrorMsg");
	String listCalculErrorParams=request.getParameter("CalculErrorParams");
	String calculErrorMsg="";
	if(!JadeStringUtil.isEmpty(idCalculErrorMsg)){
		
		String[] params=null;
		
		if(!JadeStringUtil.isEmpty(listCalculErrorParams)){
			params=listCalculErrorParams.split(",");
		}
		
		calculErrorMsg=BusinessExceptionHandler.getErrorMessage(idCalculErrorMsg,params);
	}
	
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "<%=IPCActions.ACTION_DROIT%>.lister";
	
	function clearFields () {
		$('.clearable,#forCsSexe,#partiallikeNumeroAVS,#forCsEtatDroit').val('');
		$('#partiallikeNumeroAVS').focus();		
	}

	$(function(){
		$('#partialdossierSearchlikeNss').change(function(){
			$('#hiddenDossierSearchlikeNss').val($('[name=dossierSearchlikeNss]').val());
		});

		$('#likeNom,#likePrenom').change(function(){
			if($(this).val()!=""){
				$('#droitSearch\\.orderBy').val("nomPrenom");
			}
		});
		//Erreurs
		var msgError="<%=calculErrorMsg%>";
		if(msgError!=""){
			globazNotation.utils.consoleError(msgError);
		}
		
		
		//lien retour demande
		<% if(!JadeStringUtil.isBlankOrZero(idDemandePc)){ %>
			$('[for=forIdDemande]').wrap('<a id="toDemandeLink" href="" />');
			$('#toDemandeLink').click(function () {
				window.location.href='pegasus?userAction=pegasus.demande.demande.chercher&idDemandePc=<%=idDemandePc%>';
			});
		<%}else{%>
			$('#forIdDemande,[for=forIdDemande]').hide().after('&nbsp;');
		<%}%>
		
		//lien retour droit
		<% if(JadeStringUtil.isBlankOrZero(idDroit)){ %>
			$('#forIdDroit,[for=forIdDroit]').hide().after('&nbsp;');
		<%}%>
		
	});
	
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_DRO_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<TR>
<TD>
	<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
		<TR>
		
			<TD>
			<INPUT type="hidden" name="idDemande" value="<%=idDemandePc%>">
			<LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_PC_DRO_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
			<TD>
				<nss:nssPopup avsMinNbrDigit="99"
						  nssMinNbrDigit="99"
						  newnss=""
						  name="likeNumeroAVS"/>
				<input type="hidden" id="hiddenlikeNumeroAVS" name="droitSearch.likeNss" class="clearable">					
			</TD>
			<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PC_DRO_R_NOM"/></LABEL>&nbsp;</TD>
			<TD><INPUT type="text" id="likeNom" name="droitSearch.likeNom" value="" class="clearable"></TD>
			<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PC_DRO_R_PRENOM"/></LABEL>&nbsp;</TD>
			<TD><INPUT type="text" id="likePrenom" name="droitSearch.likePrenom" value="" class="clearable"></TD>
		</TR>
		<TR>
			<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_PC_DRO_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
			<TD><INPUT name="forDateNaissance" class="clearable" value="" data-g-calendar="mandatory:false"/></TD>
			<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PC_DRO_R_SEXE"/></LABEL>&nbsp;</TD>	
			<TD colspan="3"><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD>
				<LABEL for="forIdDemande"><ct:FWLabel key="JSP_PC_DRO_R_NO_DEMANDE_PC"/></LABEL>
			</TD>
			<TD><INPUT id="forIdDemande" type="text" name="droitSearch.forIdDemandePc" value="<%=idDemandePc%>" class="disabled" readonly tabindex="-1"></TD>
			<TD>
				<LABEL for="forIdDroit"><ct:FWLabel key="JSP_PC_DRO_R_NO_DROIT_PC"/></LABEL>
			</TD>
			<TD><INPUT id="forIdDroit" type="text" name="droitSearch.forIdDroit" value="<%=idDroit%>" class="disabled" readonly tabindex="-1"></TD>
			<TD><LABEL for="forCsEtatDroit"><ct:FWLabel key="JSP_PC_DRO_R_ETAT_DROIT"/></LABEL>&nbsp;</TD>
			<TD colspan="3"><ct:FWCodeSelectTag codeType="<%=IPCDroits.CS_ETAT_DROIT%>" name="forCsEtatDroit" wantBlank="true" defaut="blank"/></TD>
		</TR>
		<tr>
			<td><label for="orderBy"><ct:FWLabel key="JSP_TRIER_PAR"/></label></td>
			<TD><ct:FWListSelectTag data="<%=orderList%>" defaut="" name="droitSearch.orderBy" /></TD>
		</tr>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
			<TD colspan="5">&nbsp;</TD>
		</TR>
		</TABLE>
	</TD>
</TR>		
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>