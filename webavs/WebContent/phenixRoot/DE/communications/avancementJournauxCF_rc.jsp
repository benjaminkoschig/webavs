<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	bButtonNew=false;
	idEcran="CCP1035";
	// si le parametre idJournalRetour ou selectedId est renseigné --> on recherche les communications dans un passage, sinon, toutes les communications
	globaz.phenix.db.communications.CPAvancementJournauxCFViewBean journal = new globaz.phenix.db.communications.CPAvancementJournauxCFViewBean();
	rememberSearchCriterias=true;
%>





<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
top.document.title = "Zunahme der SM Journale"
usrAction = "phenix.communications.avancementJournauxCF.lister";
bFind=true;
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Zunahme der SM Journale<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	    <TR> 
          	<TD>Journal-Nr.</TD>
         	<TD>&nbsp;&nbsp;</TD>
         	<TD> 
				<INPUT name="forNumJournal" size="15" type="text" style="text-align : left;" value="">
			</TD>	                        
  			<TD>&nbsp;</TD>
    	</TR>
    	<TR> 
          	<TD>Steuermeldungsjahr</TD>
         	<TD>&nbsp;&nbsp;</TD>
         	<TD> 
				<INPUT name="forAnneeComFisc" size="15" type="text" style="text-align : left;" value="">
			</TD>	                        
  			<TD>&nbsp;</TD>
    	</TR>
    	<TR>
    		<TD>Mitgliedsart</TD>
    		<TD>&nbsp;&nbsp;</TD>
			<TD nowrap>
			 <%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
				except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
				except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
			%>
			<ct:FWCodeSelectTag 
				name="forGenreAffilie"
				defaut=""
				wantBlank="<%=true%>"
				codeType="CPGENDECIS"
				 except="<%=except%>"
				/>
			</TD>
			<TD>&nbsp;</TD>
		</TR>

	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
	<table class="find" cellspacing="0">
		<tr class="somme">
			<TD class="mtdBold" width="*" align="left">&nbsp;Total</TD>	
			<TD class="mtdNumber" width="9%" align="center"><div id="totauxErreur"></div></TD>
      		<TD class="mtdNumber" width="9%" align="center"><div id="totauxAbandonne"></div></TD>
      		<TD class="mtdNumber" width="11%" align="center"><div id="totauxAvertissement"></div></TD>
     		<TD class="mtdNumber" width="9%" align="center"><div id="totauxReception"></div></TD>
      		<TD class="mtdNumber" width="6%" align="center"><div id="totauxValide"></div></TD>
      		<TD class="mtdNumber" width="9%" align="center"><div id="totauxSansAnomalie"></div></TD>
      		<TD class="mtdNumber" width="9%" align="center"><div id="totauxControle"></div></TD>
		    <TD class="mtdNumber" width="9%" align="center"><div id="totauxEnEnquete"></div></TD>
		    <TD class="mtdNumber" width="10%" align="center"><div id="totauxComptabilise"></div></TD>
		    <TD class="mtdNumber" width="5%" align="center"><div id="totauxTotal"></div></TD>	
		</tr>
	</table>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>