<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_ICI_R"

	idEcran="PRE0044";
	
	globaz.corvus.vb.ci.REInscriptionCIViewBean viewBean = (globaz.corvus.vb.ci.REInscriptionCIViewBean) request.getAttribute("viewBean");
	
	String idTiers = request.getParameter("idTiers");
	
	String forIdRCI = request.getParameter("selectedId");
	if(JadeStringUtil.isIntegerEmpty(forIdRCI)){
		forIdRCI = request.getParameter("idRCI");
	}
	
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "corvus.ci.inscriptionCI.lister";
	
	function ajouterInsCI(){
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_MANUELLE_INSCRIPTION_CI%>.afficher";
	    document.forms[0].target="fr_main";
	    document.forms[0].submit();
	}
	
	function modificationNSSExConjoint(){
	    document.forms[0].elements('userAction').value="<%=IREActions.ACTION_MODIFICATION_NSS_EX_CONJOINT%>.afficher";
	    document.forms[0].target="fr_main";
	    document.forms[0].submit();
	}
	
	function formatNoCaisse(noCaisse){
	
		no = noCaisse.value;
		if(no.length>0){		
			while(no.length<3){
				no = '0' + no;
			}
			noCaisse.value = no;
		}
	}

	function formatNoAgence(noAgence){
		no = noAgence.value;
		if(no.length>0){
			while(no.length<3){
				no = '0' + no;
			}
			noAgence.value = no;
		}
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ICI_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD>
											<LABEL for="requerantDescription"><ct:FWLabel key="JSP_ICI_R_REQUERANT"/></LABEL>
											<INPUT type="hidden" name="forIdTiers" value="<%=idTiers%>">
											<INPUT type="hidden" name="idTiers" value="<%=idTiers%>">
											<INPUT type="hidden" name="selectedId" value="<%=forIdRCI%>">
										</TD>
										<TD colspan="3">
											<re:PRDisplayRequerantInfoTag 
													session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
													idTiers="<%=idTiers%>"
													style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
										</TD>
									</TR>
									<TR><TD colspan="2">&nbsp;</TD></TR>
									<TR>									
										<TD><ct:FWLabel key="JSP_ICI_R_REVENU_TOTAL"/></TD>
										<TD><INPUT name="montantTotal" class="montantDisabled" value="0.00" readonly="readonly"></TD>
										<TD><LABEL for="forIdRCI"><ct:FWLabel key="JSP_ICI_R_NO_RASSEMBLEMENT"/></LABEL>&nbsp;</TD>
										<TD><INPUT name="forIdRCI" value="<%=forIdRCI%>"></TD>
										<TD><LABEL for="forNoCaisse"><ct:FWLabel key="JSP_ICI_R_CAISSE"/></LABEL>&nbsp;
										    <INPUT name="forNoCaisse" size="3" maxlength="3" onchange="formatNoCaisse(this);">
											<LABEL for="forNoAgence"><ct:FWLabel key="JSP_ICI_R_AGENCE"/></LABEL>&nbsp;
											<INPUT name="forNoAgence" size="3" maxlength="3" onchange="formatNoAgence(this);">
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:ifhasright element="<%=IREActions.ACTION_INSCRIPTION_CI%>" crud="u">
					<INPUT class="btnCtrl" type="button" id="btnAjouterInsCI" value="<ct:FWLabel key="MENU_OPTION_AJOUTER_INSCRIPTIONS_CI"/>" onclick="ajouterInsCI()">
				</ct:ifhasright>
				<INPUT class="btnCtrl" type="button" id="btnModificationNSSExConjoint" value="<ct:FWLabel key="BOUTON_MODIFICATION_NSS_EXCONJOINT"/>" onclick="modificationNSSExConjoint()">
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>