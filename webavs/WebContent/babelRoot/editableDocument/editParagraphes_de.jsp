<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

	<%
		idEcran="GCT????";
		globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl viewBean = (globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl) session.getAttribute("viewBean");
		
		bButtonUpdate = false;
		bButtonValidate = false;
		bButtonDelete = false;
		bButtonCancel = false;
			
		
		subTableWidth = "90%";
		tableHeight = 20;
	%>
	
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

	<!-- TinyMCE -->
	<script language="javascript" type="text/javascript" src="<%=servletContext%>/scripts/tiny_mce/tiny_mce.js"></script>
	<script language="javascript" type="text/javascript">
		tinyMCE.init({
			theme : "advanced",
			mode : "textareas",
			editor_selector : "editable",
			content_css : "<%=servletContext%>/theme/tinyMceStyle.css",
			theme_advanced_styles : "Rouge=red;Vert=green;Bleu=blue",
			theme_advanced_buttons1_add : "fontselect, fontsizeselect",
			theme_advanced_toolbar_location : "top",
			handle_event_callback : "interceptForbiddenCharacters",
			debug : false,		
			theme_advanced_disable : "strikethrough,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,outdent,indent,cut,copy,paste,undo,redo,link,unlink,image,cleanup,help,code,hr,formatselect,fontselect,fontsizeselect,sub,sup,forecolor,backcolor,charmap,visualaid,anchor,newdocument,separator"
		});
	</script>
	
	<script language="JavaScript">
	
		function interceptForbiddenCharacters(event){
		  if (event.keyCode==60 || event.keyCode==62) 
		  	event.returnValue = false;
		  if (event.which==60 || event.which==62) 
		  	return false;
		}
		
		function postInit(){
			document.all('processStarted').style.display = 'none';
		}
		
		function arret() {
			document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_EDIT_PARAGRAPHES%>.arreterGenererDocument";
			document.forms[0].target = "fr_main";
	  		document.forms[0].submit();
	  	}
	  
	  	function versEcranSuivant() {
	  		<%if(viewBean.getWantSelectAnnexeCopie()){%>
				document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_EDIT_PARAGRAPHES%>.allerVersEcranSuivant";
				document.forms[0].target = "fr_main";
				document.forms[0].submit();
			<%}else{%>
			  	document.all('processStarted').style.display = 'block';
			  	window.setTimeout("genererDoc()", 2000);
			<%}%>
	  	}
	  	
	  	function versEcranPrecedent() {
			document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_EDIT_PARAGRAPHES%>.allerVersEcranPrecedent";
			document.forms[0].target = "fr_main";
			document.forms[0].submit();
	  	}
	  
		function genererDoc() {
			document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_ANNEXES_COPIES%>.genererDocument";
			document.forms[0].target = "fr_main";
			document.forms[0].submit();
		}
	  
	</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_EPA_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR>
							<TD><ct:FWLabel key="JSP_CTAC_D_DOCUMENT"/></TD>
							<TD>
								<INPUT name="documentNameAndDomaine" type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsType())%> - <%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsDomaine())%>"  class="disabled" readonly>
							</TD>
							<TD><ct:FWLabel key="JSP_CTAC_D_DESTINATAIRE"/></TD>
							<TD>
								<INPUT name="documentDestinataire" type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getDocumentCsDestinataire())%>" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD colspan="4" height="15">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_CTAC_D_TIERS_PRINCIPAL"/></TD>
							<TD>
								<INPUT name="prenomNomTiersPrincipal" type="text" value="<%=viewBean.getNomPrenomTiersPrincipal()%>" class="disabled" readonly>
							</TD>
							<TD><ct:FWLabel key="JSP_CTAC_D_NSS_ABREGE"/></TD>
							<TD>
								<INPUT name="noAvsTiersPrincipal" type="text" value="<%=viewBean.getNoAVSTiersPrincipal()%>" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD colspan="4" height="30">
								<INPUT name="wantSelectParagraph" type="hidden" value="<%=viewBean.getWantSelectParagraph()%>">
								<INPUT name="wantEditParagraph" type="hidden" value="<%=viewBean.getWantEditParagraph()%>">
								<INPUT name="wantSelectAnnexeCopie" type="hidden" value="<%=viewBean.getWantSelectAnnexeCopie()%>">
								&nbsp;
							</TD>
						</TR>
						<TR>
							<TD colspan="4">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>" style="max-height:10cm; overflow:scroll">
		
									<%	
										javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
										java.io.InputStream stream = globaz.jade.client.util.JadeUtil.getGlobazInputStream("editParagraphes.xsl");
										javax.xml.transform.Source stylesheet = new javax.xml.transform.stream.StreamSource(stream);
										javax.xml.transform.Transformer transformer = tFactory.newTransformer(stylesheet);
										
										transformer.transform(new javax.xml.transform.stream.StreamSource(new java.io.StringReader(viewBean.getScalableDocumentDataAsXml())),
										                      new javax.xml.transform.stream.StreamResult (out));
									%>
		
								</TABLE>
							</TD>
						</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_PRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_PRO_ARRET"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_PRECEDENT"/> (alt+<ct:FWLabel key="AK_PRO_PRECEDENT"/>)" onclick="versEcranPrecedent()" accesskey="<ct:FWLabel key="AK_PRO_PRECEDENT"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_PRO_SUIVANT"/>)" onclick="versEcranSuivant()" accesskey="<ct:FWLabel key="AK_PRO_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<Table width="100%">
	<TBODY id="processStarted" style="display: none">
		<tr>
			<td colspan="3" style="height: 2em; color: white; font-weight: bold; text-align: center;background-color: green"><ct:FWLabel key="FW_PROCESS_STARTED"/></td>
		</tr>
	</TBODY>
</Table>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>