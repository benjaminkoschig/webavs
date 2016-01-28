<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0027";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	
	usrAction = "naos.lienAffiliation.affilieLienAffiliation.selectAffilieLister";

	function updateAffilieNumero(tag){
		if(tag.select && tag.select.selectedIndex != -1){
			document.getElementById('forAffilieNumero').value = tag.select[tag.select.selectedIndex].value;
			document.forms[0].submit();
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					S&eacute;lection de l'affili&eacute; à lier
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
                        	<TD nowrap width="128">Num&eacute;ro d'affili&eacute;</TD>
                        	<TD nowrap colspan="2" width="200">
								<INPUT type="hidden" name="affiliationId" value='<%=request.getParameter("affiliationId")%>'>
								<INPUT type="hidden" name="lienAffiliationId" value='<%=request.getParameter("lienAffiliationId")%>'>
								<INPUT type="hidden" name="typeLien" value='<%=request.getParameter("typeLien")%>'>
								<INPUT type="hidden" name="dateDebut" value='<%=request.getParameter("dateDebut")%>'>
								<INPUT type="hidden" name="dateFin" value='<%=request.getParameter("dateFin")%>'>
				            	<ct:FWPopupList 
				            		name="forAffilieNumero" 
				            		value="" 
				            		className="libelle" 
				            		jspName="<%=jspLocation%>" 
				            		autoNbrDigit="<%=autoDigiAff%>" 
				            		size="25"
				            		onChange="updateAffilieNumero(tag);"
				            		minNbrDigit="3"
								/>
                        	</TD>
               
 		 				</TR>
                    	<TR>
                        	<TD nowrap width="128">Nom</TD>
                        	<TD nowrap colspan="2" width="200">
                        		<INPUT type="text" name="fromNom" size="50" maxlength="50" tabindex="2">
                        	</TD>
                    	</TR>
            			<TR> 
            				<TD nowrap width="128">&nbsp;</TD>
            				<TD nowrap colspan="2" width="298">&nbsp; </TD>
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