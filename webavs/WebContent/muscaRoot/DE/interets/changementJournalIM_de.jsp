<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%> 

	<%@ page import="globaz.musca.db.interets.FAChangementJournalIMViewBean"%>

	<%
		idEcran="CFA0016";
		
		FAChangementJournalIMViewBean viewBean = (FAChangementJournalIMViewBean) session.getAttribute("viewBean");
		
		userActionValue = "musca.interets.changementJournalIM.executer";
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
		globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
	%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>

<%@ include file="/theme/process/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%> 
	<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart.jspf" %>

<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_CHOIX_JOURNAL_IM"/><%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart2.jspf" %>

<%-- tpl:put name="zoneMain" --%> 

	<TR>
		<TD colspan="2"><b><ct:FWLabel key="DESCRIPTION_ECRAN_CHOIX_JOURNAL_IM"/></b></TD>
	</TR>
	
	<TR>
		<TD>&nbsp;</TD>
	</TR>
	
	<TR>
		<TD>&nbsp;</TD>
	</TR>
	
	<TR>
	      	<TD nowrap width="60"><ct:FWLabel key="PASSAGE"/></TD>
	        <TD><INPUT type="text" name="idPassage" class="libelleLongDisabled" value="<%=viewBean.getIdJournal() + " " + viewBean.getLibelleJournal() %>" readonly="readonly" >
	        
	        <%
			Object[] psgMethodsName = new Object[]{
	        				new String[]{"setIdJournal","getIdPassage"},
	        				new String[]{"setLibelleJournal","getLibelle"}
	        };  
			Object[] psgParams= new Object[]{};	
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/interets/changementJournalIM_de.jsp";	
			%>
			
			<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
			    <ct:FWSelectorTag 
					name="passageSelector" 
					methods="<%=psgMethodsName%>"
					providerPrefix="FA"			
					providerApplication ="musca"			
					providerAction ="musca.facturation.passage.chercher"			
					providerActionParams ="<%=psgParams%>"
					redirectUrl="<%=redirectUrl%>"
				/> 
			</ct:ifhasright>
			<input type="hidden" name="selectorName" value="">
	        </TD>	
	</TR>
	
	<TR>
    	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
        <TD height="2"> 
       		<INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmail()%>">
        </TD>
   </TR>

<%-- /tpl:put --%>					
	
	<%@ include file="/theme/process/footer.jspf" %>

<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
	
	<%@ include file="/theme/process/bodyClose.jspf" %>

<%-- /tpl:insert --%>