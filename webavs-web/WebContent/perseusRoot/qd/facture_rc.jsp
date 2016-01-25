<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFNssHelper"%>
<%@page import="ch.globaz.perseus.business.models.qd.CSTypeQD"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Vector"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%

	idEcran="PPF1122";
	
	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PF_DOS_R_TRIER_PAR_NOM")});	
	rememberSearchCriterias = false;
	
	String idQd = "";
	if (!JadeStringUtil.isEmpty(request.getParameter("idQd"))) {
		idQd = request.getParameter("idQd");
	}
	
	bButtonNew = false;

	String idDossier = "";
	if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))){
		idDossier = request.getParameter("idDossier");
		if(objSession.hasRight("perseus.qd.detailfacture", FWSecureConstants.ADD)){
			bButtonNew = true;		
		}
		actionNew =  servletContext + mainServletPath + "?userAction=perseus.qd.facture.afficher&_method=add&idDossier="+idDossier;
	}
	
%>

<%-- /tpl:insert --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@ include file="/theme/find/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>
<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "perseus.qd.facture.lister";
	
	$(function(){
		
		<%if(JadeStringUtil.isEmpty(idQd)){%>
			// hide forIdDossier field and fill cell with blank
			$('#forIdQd,[for=forIdQd]').hide().after('&nbsp;');
		<%}%>
		
		<%if(JadeStringUtil.isEmpty(idDossier)){%>
			// hide forIdDossier field and fill cell with blank
			$('#forIdDossier,[for=forIdDossier]').hide().after('&nbsp;');
		<%}%>
		
	})
	
	//sur envoi 
	function onClickFind(){
		//Recup champ no dec deux partie
		var partAn = $('#inpNoDecAn').attr('value');
		var partInc= $('#inpNoDecInc').attr('value');

		
		//Chaine a setter sur champ hidden (que si un des deux champs n'est pas vide)
		if(partAn!="" || partInc!=""){
			var chaineNoDec = partAn+"-"+partInc;
			$('#forNumDecision').attr('value',chaineNoDec);
		}
	}
	
	function clearFields () {
		$('.clearable,#searchModel\\.forCsSexe,#searchModel\\.forIdGestionnaire,[name=searchModel\\.likeNss],#partialsearchModel\\.likeNss,#searchModel\\.forCSTypeQD,#searchModel\\.forAnnee,#searchModel\\.forCsEtatFacture').val('');
		$('#partialsearchModel\\.likeNss').focus();
	}
		
</SCRIPT>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_FAC_R_TITRE" /><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<TR>
		<TD>
			<TABLE border="0" cellspacing="0" cellpadding="5" width="100%">
				<TR>
					<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_PF_FAC_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="searchModel.hasPostitField" id="hasPostitField" value="true"></TD>
					<TD>
						<nss:nssPopup avsMinNbrDigit="99"
								  nssMinNbrDigit="99"
								  newnss=""
								  name="searchModel.likeNss"/>								  
					</TD>
					<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PF_FAC_R_NOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likeNom" name="searchModel.likeNom" class="clearable" value=""></TD>
					<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PF_FAC_R_PRENOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likePrenom" name="searchModel.likePrenom" class="clearable" value=""></TD>
				</TR>				<TR>
					<TD><LABEL for="type"><ct:FWLabel key="JSP_PF_QD_R_TYPE"/></LABEL>&nbsp;</TD>
					<%
						HashSet hs = new HashSet();
						hs.add(CSTypeQD.FRAIS_MALADIE.getCodeSystem());
					%>
					<TD colspan="3"><ct:FWCodeSelectTag name="searchModel.forCSTypeQD" codeType="PFTYPEQD" wantBlank="true" except="<%=hs %>" defaut=""/></TD>
					<TD><LABEL for="type"><ct:FWLabel key="JSP_PF_QD_R_ANNEE"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likePrenom" name="searchModel.forAnnee" class="clearable" value=""></TD>
				</TR>
				<tr>
					<TD><LABEL for="forCsEtatFacture"><ct:FWLabel key="JSP_PF_FAC_R_ETAT_FACTURE"/></LABEL>&nbsp;</TD>
					<TD><ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_ETAT_FACTURE%>" name="searchModel.forCsEtatFacture" wantBlank="true" defaut="blank"/></TD>
					<TD><LABEL for="forIdDossier"><ct:FWLabel key="JSP_PF_QD_R_DOSSIER"/></LABEL></TD>
					<TD><INPUT type="text" name="searchModel.forIdDossier" id="forIdDossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1">
					<input type="hidden" value="<%=idDossier%>">
					</TD>
					<TD><LABEL for="forIdQd"><ct:FWLabel key="JSP_PF_FAC_R_QD"/></LABEL></TD>
					<TD><INPUT type="text" name="searchModel.forIdQd" id="forIdQd" value="<%=idQd%>" class="disabled" readonly tabindex="-1"></TD>
				</tr>
				<tr>
					<TD><LABEL for="forIdGestionnaire"><ct:FWLabel key="JSP_PF_QD_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
					<TD><ct:FWListSelectTag data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="searchModel.forIdGestionnaire"/></TD>
					<td><label for="forNumDecision"><ct:FWLabel key="JSP_PF_QD_R_NUM_DECISION" /></label>&nbsp;</td>
					<td>
						<input id="inpNoDecAn" style="width:45px" maxlength="4" type="text" name="numeroDecisionAn" value="" class="clearable"/>-
						<input id="inpNoDecInc" style="width:60px" maxlength="6" type="text" name="numeroDecisionInc" value="" class="clearable"/>
						<input type="hidden" id="forNumDecision" name="searchModel.forNumDecision" value="" class="clearable"/>
					</td>
					<TD colspan="2">&nbsp;</TD>
				</tr>
				<TR>
					<TD colspan="6">&nbsp;</TD>
				</TR>
				<TR>
					<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
					<TD colspan="3">&nbsp;</TD>
					<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
					<TD><ct:FWListSelectTag data="<%=orderList%>" defaut="" name="searchModel.orderBy" /></TD>
				</TR>
				
			</TABLE>
		</TD>
	</TR>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
