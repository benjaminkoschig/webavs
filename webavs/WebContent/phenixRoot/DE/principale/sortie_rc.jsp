<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP0020";
  	globaz.phenix.db.principale.CPSortieViewBean viewBean = (globaz.phenix.db.principale.CPSortieViewBean)session.getAttribute ("viewBean");
  	bButtonNew = false;
  	rememberSearchCriterias=true;
  	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
  	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
// menu 
top.document.title = "Beiträge - Abgang"
usrAction = "phenix.principale.sortie.lister";
servlet = "phenix";
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Abgang<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <TD nowrap width="140" height="20">Job</TD>
            <TD nowrap> 
              <INPUT type="text" name="forIdPassage" maxlength="15" size="15"  value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/sortie_rc.jsp";	
			%>
			<!--
			-->
			<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
            <ct:FWSelectorTag 
			name="passageSelector" 
			
			methods="<%=psgMethodsName%>"
			providerPrefix="FA"			
			providerApplication ="musca"			
			providerAction ="musca.facturation.passage.chercher"			
			providerActionParams ="<%=psgParams%>"
			redirectUrl="<%=redirectUrl%>"
			target="fr_main"	
			/> 
			</ct:ifhasright>		
			<input type="hidden" name="selectorName" value="">
			</TD>
          </tr>
        <TR>
            <TD nowrap width="140">Ab Mitglied-Nr.</TD>
            <TD nowrap>
            <ct:FWPopupList 
	           		name="fromNoAffilie" 
	           		value="" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       	/>
            </TD>
          </TR>   
          <TR>
          <TD nowrap>Bereits verbuchte auch anzeigen</TD>
			<TD nowrap>
				<input type="checkbox" name="forChecked">
			</TD>
		  </TR> 

		  
		  <%
		   	// display error message if needed
			if(session.getAttribute("errorMessage")!=null){
		  %>
          <TR>
            <TD nowrap colspan="6"><br><b><%=session.getAttribute("errorMessage")%></b></TD>
          </TR>		  
		  <%
			  // delete error message
				session.removeAttribute("errorMessage");
			}
		  %>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>