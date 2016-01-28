<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_CTT_D'
--%>
<%
idEcran="GCT0008";

globaz.babel.vb.cat.CTTexteSaisieViewBean viewBean = (globaz.babel.vb.cat.CTTexteSaisieViewBean) session.getAttribute("viewBean");
bButtonCancel = false;
selectedIdValue = viewBean.getIdElement();
bButtonValidate = objSession.hasRight("babel.cat.document.actionAjouter", "ADD");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<!-- tinyMCE -->
<script language="javascript" type="text/javascript" src="<%=servletContext%>/scripts/tiny_mce/tiny_mce.js"></script>
<% if (viewBean.isStyledDocument()) { %>
<script language="javascript" type="text/javascript">
	tinyMCE.init({
		theme : "advanced",
		mode : "exact",
		elements : "descriptionDE, descriptionFR, descriptionIT",
		content_css : "<%=servletContext%>/theme/tinyMceStyle.css",
		theme_advanced_styles : "Rouge=red;Vert=green;Bleu=blue",
		theme_advanced_buttons1_add : "fontselect",
		theme_advanced_toolbar_location : "top",
		handle_event_callback : "interceptForbiddenCharacters",
		debug : false,		
		theme_advanced_disable : "strikethrough,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,outdent,indent,cut,copy,paste,undo,redo,link,unlink,image,cleanup,help,code,hr,formatselect,fontselect,fontsizeselect,sub,sup,forecolor,backcolor,charmap,visualaid,anchor,newdocument,separator"
	});
</script>
<%}%>
<!-- /tinyMCE -->


<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE%>.ajouter"
  }
  function upd() {
  	document.all('textesReadOnly').style.display = 'none';
	document.all('textesEditable').style.display = 'block';
  }

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}

	<%}%>
	
	<%if (!"add".equalsIgnoreCase(request.getParameter("_method"))) {%>
		document.all('textesReadOnly').style.display = 'block';
		document.all('textesEditable').style.display = 'none';	
	<%} else {%>
		document.all('textesReadOnly').style.display = 'none';
		document.all('textesEditable').style.display = 'block';	
	<%}%>
  }
  
  function niveauChange(){
	   document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_TEXTES_SAISIE%>.afficher";
	   document.forms[0].submit();
  }
  
  function postInit(){
  	<%if(!viewBean.isAdministrateur()){%>	  	
		document.forms[0].elements('isSelectable').readOnly=true;
		document.forms[0].elements('description').readOnly=true;
		document.forms[0].elements('isSelectedByDefault').readOnly=true;
		document.forms[0].elements('isEditable').readOnly=true;
	<%}%>
  }
  
  function interceptForbiddenCharacters(event){
	  if (event.keyCode==60 || event.keyCode==62) 
	  	event.returnValue = false;
	  if (event.which==60 || event.which==62) 
	  	return false;
  }
  
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CTT_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="niveau"><ct:FWLabel key="JSP_CTT_D_NIVEAU"/></LABEL></TD>
							<TD>
								<TABLE border="0" width="100%">	
									<TR>
										<TD>							
											<ct:select name="niveau" defaultValue="<%=viewBean.getNiveau()%>" onchange="niveauChange();">
												<ct:option label="1" value="1"/>
												<ct:option label="2" value="2"/>
												<ct:option label="3" value="3"/>
												<ct:option label="4" value="4"/>
												<ct:option label="5" value="5"/>
												<ct:option label="6" value="6"/>
												<ct:option label="7" value="7"/>
												<ct:option label="8" value="8"/>
												<ct:option label="9" value="9"/>
												<ct:option label="10" value="10"/>
												<ct:option label="11" value="11"/>
												<ct:option label="12" value="12"/>
												<ct:option label="13" value="13"/>
												<ct:option label="14" value="14"/>
												<ct:option label="15" value="15"/>
												<ct:option label="16" value="16"/>
												<ct:option label="17" value="17"/>
												<ct:option label="18" value="18"/>
												<ct:option label="19" value="19"/>
												<ct:option label="20" value="20"/>
											</ct:select>
										</TD>
										<%--<TD>
											<LABEL for="isSelectable"><ct:FWLabel key="JSP_CTT_D_SELECTIONNABLE"/></LABEL>
											<INPUT type="checkbox" name="isSelectable" value="on" <%=viewBean.getIsSelectable().booleanValue()?" checked":""%>>
										</TD>
										<TD>
											<LABEL for="isEditable"><ct:FWLabel key="JSP_CTT_D_EDITABLE"/></LABEL>
											<INPUT type="checkbox" name="isEditable" value="on" <%=viewBean.getIsEditable().booleanValue()?" checked":""%>>
										</TD>--%>
										<TD>
											<LABEL for="description"><ct:FWLabel key="JSP_CTT_D_DESCRIPTION"/></LABEL>
											<INPUT type="text" name="description" value="<%=viewBean.getDescription()%>" class="libelleLong">
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="position"><ct:FWLabel key="JSP_CTT_D_POSITION"/></LABEL></TD>
							<TD>
								<TABLE border="0" width="35%">	
									<TR>
										<TD>
											<INPUT type="text" name="position" value="<%=viewBean.getPosition()%>" class="numeroCourt">
											<INPUT type="hidden" name="idDocument" value="<%=viewBean.getIdDocument()%>">
											<INPUT type="hidden" name="borneInferieure" value="<%=viewBean.getBorneInferieure()%>">
										</TD>
										<%--<TD>
											<LABEL for="isSelectedByDefault"><ct:FWLabel key="JSP_CTT_D_SELECTED_BY_DEFAULT"/></LABEL>
											<INPUT type="checkbox" name="isSelectedByDefault" value="on" <%=viewBean.getIsSelectedByDefault().booleanValue()?" checked":""%>>
										</TD>--%>
									</TR>
								</TABLE>		
							</TD>
						</TR>
						<TBODY id="textesEditable">
						<TR><TD colspan="2" height="4"></TD></TR>
						<TR>
							<TD><LABEL for="descriptionDE"><ct:FWLabel key="JSP_CTT_D_DESCRIPTION_DE"/></LABEL></TD>
							<TD>
								<TEXTAREA name="descriptionDE" cols="100" rows="5"><%=viewBean.getDescriptionDE()%></TEXTAREA>
							</TD>
						</TR>

						<TR ><TD colspan="2" height="1"></TD></TR>
						<TR>
							<TD><LABEL for="descriptionFR"><ct:FWLabel key="JSP_CTT_D_DESCRIPTION_FR"/></LABEL></TD>
							<TD>
								<TEXTAREA name="descriptionFR" cols="100" rows="5"><%=viewBean.getDescriptionFR()%></TEXTAREA>
							</TD>
						</TR>
						<TR ><TD colspan="2" height="1"></TD></TR>
						<TR>
							<TD><LABEL for="descriptionIT"><ct:FWLabel key="JSP_CTT_D_DESCRIPTION_IT"/></LABEL></TD>
							<TD>
								<TEXTAREA name="descriptionIT" cols="100" rows="5"><%=viewBean.getDescriptionIT()%></TEXTAREA>
							</TD>
						</TR>
						</TBODY>
						<TBODY id="textesReadOnly">
						<TR>
							<TD><ct:FWLabel key="JSP_CTT_D_DESCRIPTION_DE"/></TD>
							<TD>
								<div style="height:70px;overflow:auto;background-color: white;width: 192mm; color: #999999; font-weight: bold;" align="left" valign="top">
									<%=viewBean.getDescriptionDE()%>
								</div>
							</TD>
						</TR>
						<tr><td>&nbsp;</td></tr>
						<TR>
							<TD><ct:FWLabel key="JSP_CTT_D_DESCRIPTION_FR"/></TD>
							<TD>
								<div style="height:70px;overflow:auto;background-color: white;width: 192mm; color: #999999; font-weight: bold;" align="left" valign="top">
									<%=viewBean.getDescriptionFR()%>
								</div>
							</TD>
						</TR>
						<tr><td>&nbsp;</td></tr>
						<TR>
							<TD><ct:FWLabel key="JSP_CTT_D_DESCRIPTION_IT"/></TD>
							<TD>
								<div style="height:70px;overflow:auto;background-color: white;width: 192mm; color: #999999; font-weight: bold;" align="left" valign="top">
									<%=viewBean.getDescriptionIT()%>
								</div>
							</TD>
						</TR>
						</TBODY>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
