<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.utils.PFNssHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="ch.globaz.perseus.business.models.situationfamille.MembreFamille"%>
<%@page import="globaz.perseus.vb.qd.PFQdChercherViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	idEcran="PPF1112";
	
	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PF_DOS_R_TRIER_PAR_NOM")});	
	rememberSearchCriterias = true;
	
	PFQdChercherViewBean viewBean = (PFQdChercherViewBean) request.getAttribute("viewBean");
	
	bButtonNew = false;
	
	String idDossier = "";
	if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
		idDossier = request.getParameter("idDossier");
	}
	
%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<SCRIPT language="JavaScript">

	<% if (JadeStringUtil.isEmpty(idDossier)) { %>
		bFind = false;
	<% } else { %>
		bFind = true;
	<% } %>
	usrAction = "perseus.qd.qd.lister";
	
	$(function(){
		
		<%if(JadeStringUtil.isEmpty(idDossier)){%>
			// hide forIdDossier field and fill cell with blank
			$('#forIdDossier,[for=forIdDossier]').hide().after('&nbsp;');
			$('#forIdMembreFamille,[for=forIdMembreFamille]').hide().after('&nbsp;');
		<%}%>
		
	})
		
	function clearFields () {
		$('.clearable,#searchModel\\.forCsSexe,[name=searchModel\\.likeNss],#partialsearchModel\\.likeNss,#searchModel\\.forCSTypeQD,#searchModel\\.forAnnee,#searchModel\\.forCsEtatFacture').val('');
		$('#partialsearchModel\\.likeNss').focus();
	}
	
</SCRIPT>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_QD_R_TITRE" /><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
	<TR>
		<TD>
			<TABLE border="0" cellspacing="0" cellpadding="5" width="100%">
				<TR>
					<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_PF_QD_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="searchModel.hasPostitField" id="hasPostitField" value="true"></TD>
					<TD>
						<nss:nssPopup avsMinNbrDigit="99"
								  nssMinNbrDigit="99"
								  newnss=""
								  name="searchModel.likeNss"/>										  
					</TD>
					<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PF_QD_R_NOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likeNom" name="searchModel.likeNom" class="clearable" value=""></TD>
					<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PF_QD_R_PRENOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likePrenom" name="searchModel.likePrenom" class="clearable" value=""></TD>
				</TR>
				<TR>
					<TD><LABEL for="type"><ct:FWLabel key="JSP_PF_QD_R_TYPE"/></LABEL>&nbsp;</TD>
					<TD colspan="3"><ct:FWCodeSelectTag name="searchModel.forCSTypeQD" codeType="PFTYPEQD" wantBlank="true" defaut=""/></TD>
					<TD><LABEL for="type"><ct:FWLabel key="JSP_PF_QD_R_ANNEE"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likePrenom" name="searchModel.forAnnee" class="clearable" value=""></TD>
				</TR>
				<tr>
					<TD><LABEL for="forIdDossier"><ct:FWLabel key="JSP_PF_QD_R_DOSSIER"/></LABEL></TD>
					<TD><INPUT type="text" name="searchModel.forIdDossier" id="forIdDossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1"></TD>
					<TD><LABEL for="forIdMembreFamille"><ct:FWLabel key="JSP_PF_QD_R_MEMBRE_FAMILLE"/></LABEL></TD>
					<TD colspan="3">
						<select name="searchModel.forIdMembreFamille" id="forIdMembreFamille">
							<option value=""></option>
							<% for (MembreFamille mf : viewBean.getListMembreFamille()) { %>
								<option value="<%=mf.getId() %>"><%=mf.getPersonneEtendue().getTiers().getDesignation1() + " " + mf.getPersonneEtendue().getTiers().getDesignation2() %></option>
							<% } %>	
						</select>
					</TD>
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
