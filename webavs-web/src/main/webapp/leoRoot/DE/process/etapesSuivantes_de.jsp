<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.leo.vb.process.LEGenererEtapesSuivantesViewBean viewBean = (globaz.leo.vb.process.LEGenererEtapesSuivantesViewBean)session.getAttribute ("viewBean");
	userActionValue = "leo.process.etapesSuivantes.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	java.util.Vector testVector = viewBean.getFormule();
	idEcran = "GEN3001";
	%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
function updateFormule(){
	document.getElementById("userAction").value="leo.process.etapesSuivantes.updateFormuleEtape";
	document.forms[0].submit();
}
function focus(){
	<%if(request.getParameter("forCategorie")!=null){%>
		document.forms[0].elements("forCsFormule1").focus();
	<%}%>
}
</script>
<ct:menuChange displayId="options" menuId="LE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="LE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>N?chste Etappen generieren<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Referenzdatum</TD>
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
							<TD>Kategorie</TD>
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
									<TD>Formel</TD>
									<TD>&nbsp;<ct:FWListSelectTag name="forCsFormule1" defaut="<%=formule%>" data="<%=testVector%>"/> </TD>
									<TD>&nbsp;</TD>
								</TR>
							<%}else{
									testVector = viewBean.getFormulesList(objSession,null);
							%>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Formel</TD>
							<TD>&nbsp;<ct:FWListSelectTag name="forCsFormule1" defaut="" data="<%=testVector%>" /> </TD>
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
							<TD>E-Mail Adresse</TD>
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
							<TD>1. Sort
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
								2. Sort
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
							<TD>Ausdruckdatum</TD>
							<TD>&nbsp;<ct:FWCalendarTag name="forDateImpression" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>" doClientValidation="CALENDAR" /></TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Kommentare</TD>
							<TD>&nbsp;<INPUT type="text" name="commentaire" size="50"></TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD>Simulationmodus</TD>
							<TD><INPUT type="checkbox" name="forIsSimulation" value="<%=globaz.leo.vb.process.LEGenererEtapesSuivantesViewBean.SIMULATION_TRUE%>" checked="checked"></TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"></TD>
						</TR>						
				<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>