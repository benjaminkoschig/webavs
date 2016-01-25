<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="GCF4007";
	globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
%>	
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "helios.classifications.definitionListe.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Uebersicht der Definitionen der Listen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

							<tr>
						      <TD colspan="2">&nbsp;Mandant&nbsp;<input name='libelle' class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>"></td>						      
						      <TD colspan="2">&nbsp;Rechnungsjahr&nbsp;<input name='fullDescription' readonly class="libelleLongDisabled" value="<%=exerciceComptable.getFullDescription()%>"></TD>
							</tr>


						<tr>
						<TD>&nbsp;Klassifikation&nbsp;
						</td>
						<td>
							<ct:FWListSelectTag name="forIdClassification"
								 defaut=""
								 data="<%=globaz.helios.translation.CGListes.getClassificationListe(session, exerciceComptable.getIdMandat(), null)%>"/>								 
						 </td>
						 <td colspan="2">&nbsp;</td>		  	
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