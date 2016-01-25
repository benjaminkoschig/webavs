<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersViewBean"%>
<%@page import="globaz.corvus.api.adaptation.IREAdaptationRente"%>
<%@page import="globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersViewBean"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.corvus.vb.demandes.RENSSDTO"%>
<%@page import="globaz.corvus.vb.adaptation.REAdaptationDTO"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PRE0101";
 	RERentesAdapteesJointRATiersViewBean viewBean = (RERentesAdapteesJointRATiersViewBean) request.getAttribute("viewBean");

 	bButtonNew = false;
 	rememberSearchCriterias = true;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>


<SCRIPT language="JavaScript">

	bFind = false;
	usrAction = "<%=globaz.corvus.servlet.IREActions.ACTION_ADAPTATION_RENTES_ADAPTEES%>.lister";

	<%
	
		String typeAdaptation 		= "";
		String codePrestation 		= "";
		String anneeAugmentation 	= "";
		String montantAdapte		= "";
		String nss					= "";
	
		REAdaptationDTO dto = (REAdaptationDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_ADAPTATION_DTO);
	
		if (dto!=null && dto instanceof REAdaptationDTO){
			typeAdaptation 		= dto.getTypeAdaptation();
			codePrestation 		= dto.getCodePrestation();
			anneeAugmentation 	= dto.getAnneeAug();
			montantAdapte 		= dto.getMontantAdapte();
			
			if (!JadeStringUtil.isBlankOrZero(dto.getCodePrestation()) 
					|| !JadeStringUtil.isBlankOrZero(dto.getMontantAdapte())){
				%>bFind = true;<%
			}
			
		} else {
			anneeAugmentation 	= viewBean.getAnneeDefault();
			typeAdaptation 		= IREAdaptationRente.CS_TYPE_NON_AUGMENTEES;
		}
	
		RENSSDTO nssDto = (RENSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO);
		
		if (nssDto!=null && nssDto instanceof RENSSDTO){
			nss 				= nssDto.getNSSForSearchField();
			if (!JadeStringUtil.isBlankOrZero(nss)){
				%>bFind = true;<%
			}
		}
		
	%>

	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value= "";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("forCodePrestation")[0].value="";
		document.getElementsByName("forCsTypeAdaptation")[0].value="<%=IREAdaptationRente.CS_TYPE_NON_AUGMENTEES%>";
		document.getElementsByName("forAnneeAdaptation")[0].value="<%=viewBean.getAnneeDefault()%>";
		document.getElementsByName("forNouveauMontant")[0].value="";

		document.forms[0].elements('partiallikeNumeroAVS').focus();
	}
	
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_TITRE_PAGE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				
						<tr>
							<td><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_TYPE_ADAPT"/></td>
							<td>
								<ct:FWCodeSelectTag codeType="RETYRENAU" 
													name="forCsTypeAdaptation" 
													wantBlank="false" 
													defaut="<%=typeAdaptation%>"/>
							</td>
						</tr>
						<tr>
							<TD><ct:FWLabel key="JSP_ANN_R_NSS"/></TD>
							<TD>
								<ct1:nssPopup avsMinNbrDigit="99"
										  nssMinNbrDigit="99"
										  newnss=""
										  name="likeNumeroAVS"
										  value="<%=nss%>"/>
							</TD>							
							<TD><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_CODE_PREST"/></TD>
							<TD><input type="text" name="forCodePrestation" value="<%=codePrestation%>" size="2"></TD>
							<td colspan="2" width="30%">&nbsp;</td>	
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANNEE"/></td>
							<td>
								<input type="text" name="forAnneeAdaptation" value="<%=anneeAugmentation%>" size="4">
							</td>
							<TD><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_MONTANT_ADAP"/></TD>
							<TD><input type="text" name="forNouveauMontant" value="<%=montantAdapte%>" class="montant"></TD>
						</tr>
						<tr>
							<td colspan="6">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2" width="30%">
								<input 	type="button" 
										onclick="clearFields()" 
										accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
										value="<ct:FWLabel key="JSP_EFFACER"/>">
								<label>
									[ALT+<ct:FWLabel key="AK_EFFACER"/>]
								</label>
							</td>							
						</tr>


	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
	
				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>