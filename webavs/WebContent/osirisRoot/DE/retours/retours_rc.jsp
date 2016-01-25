
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0071";
rememberSearchCriterias = true;

boolean wasVbNull = false;

CARetoursViewBean viewBean = (CARetoursViewBean) request.getAttribute("viewBean");
if(viewBean==null){
	viewBean = new CARetoursViewBean();
	viewBean.setSession(objSession);
	wasVbNull = true;
}

%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<% 
	
	String idLot = "";
	if(!JadeStringUtil.isNull(request.getParameter("idLot"))){
		idLot = request.getParameter("idLot");
	}
	
	String libelleLot = "";
	if(!JadeStringUtil.isNull(request.getParameter("libelleLot"))){
		libelleLot = request.getParameter("libelleLot");
	}
	
	String montantTotal = "";
	if(!JadeStringUtil.isNull(request.getParameter("montantTotal"))){
		montantTotal = request.getParameter("montantTotal");
	}
	
	String csEtatLot = "";
	if(!JadeStringUtil.isNull(request.getParameter("csEtatLot"))){
		csEtatLot = request.getParameter("csEtatLot");
	}
	
	bButtonNew= bButtonNew && !JadeStringUtil.isIntegerEmpty(idLot) && CALotsRetours.CS_ETAT_LOT_OUVERT.equals(csEtatLot); 
	
	actionNew = actionNew + "&idLot="+idLot;
%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.osiris.db.retours.CALotsRetours"%>
<%@page import="globaz.osiris.db.retours.CARetours"%>
<%@page import="globaz.jade.publish.client.JadePublishDocument"%>
<%@page import="globaz.osiris.db.retours.CARetoursViewBean"%>
<script>
	top.document.title = "Retours - " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
	usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.retours.retours.lister";
	bFind = true;
	function initRc(){
		<%if(!JadeStringUtil.isIntegerEmpty(idLot)){ %>	
			validateFloatNumber(document.forms[0].elements('montantTotal'));
		<%} %>	
	}

	function imprimerListePrestations(){
		document.forms[0].elements('userAction').value = "osiris.retours.retours.imprimerListePrestations";
		document.forms[0].target="_main";
		document.forms[0].submit();
	}
-->
</SCRIPT>

<%if(viewBean.getAttachedDocuments() != null && viewBean.getAttachedDocuments().size()>0){
	for(int i=0;i<viewBean.getAttachedDocuments().size();i++){
		String docName = ((JadePublishDocument)viewBean.getAttachedDocuments().get(i)).getDocumentLocation();
		int index = docName.lastIndexOf("/");
		if(index == -1){
			index = docName.lastIndexOf("\\");
		}
		docName = docName.substring(index);
		%>
		<script>
		window.open("<%=request.getContextPath()+ "/work/" + docName%>");
		</script>
<%	}
}
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Rückkehr<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <%if(!JadeStringUtil.isIntegerEmpty(idLot)){ %>				
		  <TR>
		  	 <TD width="160">Job-Nr.</TD>
		  	 <TD>
		  	 	<INPUT type="text" name="idLotAffiche" readonly class="disabled" value="<%=idLot+" - "+libelleLot%>">
		  	 	&nbsp;
		  	 	<input type="hidden" name="idLot" value="<%=idLot%>">
		  	 	<input type="hidden" name="forIdLot" value="<%=idLot%>">
		  	 </TD>
		  	 <TD width="160">Gesamtbetrag des Jobs</TD>
		  	 <TD>
		  	 	<INPUT type="text" name="montantTotal" readonly class="montantDisabled" value="<%=montantTotal%>">
		  	 </TD>
		  </TR>	
		  <%}%>	
		  
		  <TR>
		  	<TD nowrap width="160">Nr oder Name des Abrechnungskontos</TD>
		  	<TD>
		  		<input type="text" name="likeNumNom" value="<%=wasVbNull?"":viewBean.getLikeNumNom()%>" class="libelleStandard" tabindex="1">
		  	</TD>
		  	<TD nowrap width="160">Betrag</TD>
            <TD>
              <input name="forMontantRetour" value="<%=wasVbNull?"":viewBean.getForMontantRetour()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"/>
            </TD>
		  </TR>	
						
          <TR>
            <TD width="160">Status</TD>
            <TD>
              <%if(wasVbNull){
              	String defaut = CARetours.CS_ETAT_RETOUR_SUSPENS;
              %>
              <ct:FWListSelectTag name="forCsEtatRetour" data="<%=viewBean.getEtatRetourData()%>"  defaut="<%=defaut%>"/>
              <%}else{ %>
              <ct:FWListSelectTag data="<%=viewBean.getEtatRetourData()%>" defaut="<%=viewBean.getForCsEtatRetour()%>" name="forCsEtatRetour"/>
              <%}%>
            </TD>
            <TD width="160">Grund</TD>
            <TD>
            <%if(wasVbNull){ %>
            	<ct:FWCodeSelectTag codeType="OSIMOTRET" name="forCsMotifRetour" wantBlank="true" defaut="" />
            <%} else {%>
             <ct:FWCodeSelectTag codeType="OSIMOTRET" name="forCsMotifRetour" wantBlank="true" defaut="<%=viewBean.getForCsMotifRetour()%>" />
            <%}%>
            </TD>
          </TR>
                    
		  <TR>
            <TD width="160">Rückkehrdatum</TD>
            <TD>
            <%if(wasVbNull){ %>
				<ct:FWCalendarTag name="forDateRetour" value=""/>
            <%} else {%>
				<ct:FWCalendarTag name="forDateRetour" value="<%=viewBean.getForDateRetour()%>"/>
            <%}%>
            </TD>
           	<TD nowrap width="160">Bezeichnung</TD>
		  	<TD>
		  		<input type="text" name="likeLibelleRetour" value="<%=wasVbNull?"":viewBean.getLikeLibelleRetour()%>" class="libelleStandard" tabindex="1">
		  	</TD>
          </TR>
		  <TR>
			<td colspan="4">&nbsp;</td>
			<TD><INPUT type="button" value="<ct:FWLabel key="JSP_IMPRIMER"/> (alt+<ct:FWLabel key="AK_IMPRIMER"/>)" onclick="imprimerListePrestations()" accesskey="<ct:FWLabel key="AK_IMPRIMER"/>"></TD>
		  </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>