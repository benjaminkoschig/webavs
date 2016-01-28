<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0035";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	rememberSearchCriterias = true;
%>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
bFind=false;
usrAction = "pyxis.adressecourrier.theGroupeLocalite.lister";

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche d'un groupe de localités
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<script>document.getElementById('subtable').style.width='100%';</script>
						<tr>
							<td style="width: 100%">
								<table border="0" style="width: 100%;" cellpadding="3">
									<tr>
										<td style="text-align: right;">Type du groupe</td>
										<td>
											<ct:select name="type" wantBlank="true"  styleClass="libelleLong">
												<ct:optionsCodesSystems csFamille="PYTYPLOCA"/>
											</ct:select>
										</td>
										<td style="text-align: right;">Nom du groupe</td>
										<td>
											<input type="text" name="nom" class="libelleLong"/>
											<ct:inputHidden name="langue"/>
										</td>
									</tr>
								</table>
						</td></tr>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>