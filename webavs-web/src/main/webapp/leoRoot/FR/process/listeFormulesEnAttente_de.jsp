<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.leo.vb.process.LEListeFormulesEnAttenteViewBean viewBean = (globaz.leo.vb.process.LEListeFormulesEnAttenteViewBean)session.getAttribute ("viewBean");
	userActionValue = "leo.process.listeFormulesEnAttente.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	java.util.Vector testVector = viewBean.getFormule();
	idEcran = "GEN2001";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
function updateFormule(){
document.getElementById("userAction").value="leo.process.listeFormulesEnAttente.updateFormule";
document.forms[0].submit();
}
function focus(){
	<%if(request.getParameter("forCategorie")!=null){%>
		document.forms[0].elements("forCsFormule1").focus();
	<%}%>
}
</SCRIPT>
<ct:menuChange displayId="options" menuId="LE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="LE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste des formules en attente<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Date de référence</TD>
							<%
							  String dateR=""; 
								if(request.getParameter("forDateReference")!=null){
									dateR= request.getParameter("forDateReference");
								}
							%>
							<TD>&nbsp;<ct:FWCalendarTag name="forDateReference" value="<%=dateR%>" doClientValidation="CALENDAR" /></TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Catégorie</TD>
							<%
							  String cat=""; 
								if(request.getParameter("forCategorie")!=null){
									cat= request.getParameter("forCategorie");
								}
							%>
							<TD>&nbsp;<ct:FWCodeSelectTag name="forCategorie" codeType="LECATJOUR" wantBlank="true" defaut="<%=cat%>" />
							<script>
								document.getElementById("forCategorie").onchange=function() {updateFormule();}
								document.getElementById("forCategorie").onload=function(){focus();}
							</script>	
							</TD>
						</TR>
						<%
							String formule=""; 
							if(request.getParameter("forCsFormule1")!=null){
								formule= request.getParameter("forCsFormule1");
							}
							if(testVector.size()>0){%>
								<TR>
									<TD>&nbsp;</TD>
									<TD>Formule</TD>
									<TD>&nbsp;<ct:FWListSelectTag name="forCsFormule1" defaut="<%=formule%>" data="<%=testVector%>"/> </TD>
									<TD>&nbsp;</TD>
								</TR>
						<%}else{
							testVector = viewBean.getFormulesList(objSession,null);
						%>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Formule</TD>
							<TD>&nbsp;<ct:FWListSelectTag name="forCsFormule1" defaut="<%=formule%>" data="<%=testVector%>"/> </TD>
							<TD>&nbsp;</TD>
						</TR>
				         <%}%>
						<TR>
						<%
							  String email=eMailAddress; 
								if(request.getParameter("forEmail")!=null){
									email= request.getParameter("forEmail");
								}
							%>
							<TD>&nbsp;</TD>
							<TD>Adresse email</TD>
							<TD>&nbsp;<INPUT id="forEmail" name="forEmail" type="text" value="<%=email%>" maxlength="40" size="40" style="width:8cm;" /></TD>
							<TD>&nbsp;</TD>
						</TR>
						<%
						java.util.HashSet provenanceAOter = new java.util.HashSet();
						provenanceAOter.add(globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT);
						provenanceAOter.add(globaz.leo.constantes.ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION);
						%>
						<TR>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Tri 1
							<%
							  String tri1=""; 
								if(request.getParameter("order1")!=null){
									tri1= request.getParameter("order1");
								}
							%>
							<TD>&nbsp;<ct:FWCodeSelectTag name="order1" codeType="LEParamGen" wantBlank="true" defaut="<%=tri1%>" except="<%=provenanceAOter%>"/></TD>
							
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>
								Tri 2
							<%
							  String tri2=""; 
								if(request.getParameter("order2")!=null){
									tri2= request.getParameter("order2");
								}
							%>
							<TD>&nbsp;<ct:FWCodeSelectTag name="order2" codeType="LEParamGen" wantBlank="true" defaut="<%=tri2%>" except="<%=provenanceAOter%>"/> </TD>
							
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD width="26%">Liste Excel</TD>
							<TD>&nbsp;<INPUT type="checkbox" name="isFormatExcel" value="on"></TD>
							<TD>&nbsp;</TD>
						</TR>
          				<TR>
          					<TD>&nbsp;</TD>
							<TD width="26%">Liste Pdf</TD>
							<TD>&nbsp;<INPUT type="checkbox" name="isFormatIText" value="on"></TD>
							<TD>&nbsp;</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>