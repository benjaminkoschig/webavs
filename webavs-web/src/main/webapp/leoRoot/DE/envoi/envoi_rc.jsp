<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean provenanceList = (globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean)request.getAttribute(globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean.PROVENANCE_VIEWBEAN);
bButtonNew = false;
rememberSearchCriterias = true;
String numero = "";
idEcran = "GEN0001";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="javaScript">
usrAction = "leo.envoi.envoi.lister";
</SCRIPT>
<ct:menuChange displayId="options" menuId="LE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="LE-MenuPrincipal" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Liste der gesendeten Dokumente<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR> </TR>
						<%
						String forLibelle = "";
						if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+"1") != null){
							numero = java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(1)));
							forLibelle=numero;
							%><SCRIPT>bFind=true;</SCRIPT>
						<%}
						//on regarde si on a des criteres de recherches, pour afficher directement la sélection
						//cela peut etre en paramètre ou en attribut
						if((!globaz.jade.client.util.JadeStringUtil.isEmpty((String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX)))||
							(session.getAttribute("forDate")!=null)||
						   (request.getParameter("forDate")!=null)||
						   (session.getAttribute("forTypeDocument")!=null)||
						   (request.getParameter("forTypeDocument")!=null)||
						   (session.getAttribute("forDateRappel")!=null)||
						   (request.getParameter("forDateRappel")!=null)||
						   (session.getAttribute("forLibelle")!=null)||
						   (request.getParameter("forLibelle")!=null)||
						   (session.getAttribute("forDateReception")!=null)||
						   (request.getParameter("forDateReception")!=null)||
						   (session.getAttribute("forUserName")!=null)||
						   (request.getParameter("forUserName")!=null)
						   ){%><SCRIPT>bFind=true;</SCRIPT>
						<%}%>
						
						<%
						for(int i=0;i<globaz.leo.db.envoi.LEEnvoiListViewBean.NBRE_MAX_CRITERE_ENTREE;i++){
						   
							if(!globaz.jade.client.util.JadeStringUtil.isEmpty(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(i+1)))){
								if(!globaz.jade.client.util.JadeStringUtil.isEmpty(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(i+1)))){%>
									<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(i+1)))%>">
								<%}%>
								<%if(!globaz.jade.client.util.JadeStringUtil.isEmpty(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(i+1)))){%>
										<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(i+1)))%>">	
								<%}%>
									<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(i+1)))%>">
									<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(i+1)))%>">
							<%}
						}%>
						<%if(!globaz.jade.client.util.JadeStringUtil.isEmpty(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK))){%>
							<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK)%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK))%>">
						<%}%>
											
						<TR>
							<%
							  String forDate="";
							   if(request.getParameter("forDate")!=null){
									forDate=request.getParameter("forDate");
							}%>
							<TD>Datum</TD>
							<TD><ct:FWCalendarTag name="forDate" value="<%=forDate%>" doClientValidation="CALENDAR" /></TD>
							<TD>&nbsp;</TD><TD>&nbsp;</TD>
							<%String forTypeDocument="";
							  if(request.getParameter("forTypeDocument")!=null){
									forTypeDocument=request.getParameter("forTypeDocument");
							}%>
							<TD>Kategorie</TD>
							<TD><ct:FWCodeSelectTag name="forTypeDocument" codeType="LECATJOUR" wantBlank="true" defaut="<%=forTypeDocument%>" /> </TD>
						</TR>
						<TR>
							<%String forDateRappel="";
							
							  if(request.getParameter("forDateRappel")!=null){
									forDateRappel=request.getParameter("forDateRappel");
							}%>
							<TD>Mahnungsdatum</TD>
							<TD>
							<ct:FWCalendarTag name="forDateRappel" value="<%=forDateRappel%>" doClientValidation="CALENDAR" />
							</TD>
							<TD>&nbsp;</TD><TD>&nbsp;</TD>
							<%
							  if((request.getParameter("forLibelle")!=null)&& (JadeStringUtil.isEmpty(numero))){
								forLibelle=request.getParameter("forLibelle");
							  } else if (session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX) != null && JadeStringUtil.isEmpty(numero)){
								 forLibelle=(String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX);
							  }
							
							%>
							<TD>Nummer</TD>
							<TD><INPUT type="text" name="forLibelle" value="<%=forLibelle%>"></TD>
						</TR>
						<TR>
							<%String forDateReception="";
							  
							  if(request.getParameter("forDateReception")!=null){
									forDateReception=request.getParameter("forDateReception");
							}%>
							<TD>Empfangsdatum</TD>
							<TD><ct:FWCalendarTag name="forDateReception" value="<%=forDateReception%>"/></TD>
							<TD>&nbsp;</TD><TD>&nbsp;</TD>
							<%String forUserName="";
							 
							  if(request.getParameter("forUserName")!=null){
									forUserName=request.getParameter("forUserName");
							}%>
							<TD>Benutzer</TD>
							<TD><INPUT type="text" name="forUserName" value="<%=forUserName%>"></TD>
						</TR>
						<TR>
							<TD>
								<INPUT type="hidden" name="jointureFichier" value=<%=globaz.journalisation.db.journalisation.access.JOJournalisationManager.TYPE_PAR_IMBRICATION%>>	
								<INPUT type="hidden" name="forCsTypeCodeSysteme" value="<%=globaz.leo.constantes.ILEConstantes.CS_CATEGORIE_GROUPE%>">
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