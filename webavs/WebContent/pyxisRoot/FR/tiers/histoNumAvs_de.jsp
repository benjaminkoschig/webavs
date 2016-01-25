<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ page import="globaz.commons.nss.*"%>
		<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0042";
	TIHistoNumAvsViewBean viewBean = (TIHistoNumAvsViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");
	
	String idTiers = "";
	if(!JadeStringUtil.isNull(request.getParameter("idTiers"))){
		idTiers = request.getParameter("idTiers");
	}
	
	viewBean.setAncienNumAvs(viewBean.getNumAvs());
	bButtonDelete = bButtonDelete && viewBean.isSupprimable();
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable();
			
%>

<%@page import="globaz.pyxis.db.tiers.TIHistoNumAvsViewBean"%><SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers - Historique AVS détail"

function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.histoNumAvs.ajouter"
}

function upd() {
}

function validate() {
	state = validateFields(); 
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.tiers.histoNumAvs.ajouter";
    else
	document.forms[0].elements('userAction').value="pyxis.tiers.histoNumAvs.modifier";
	return (state);
}

function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.histoNumAvs.afficher"
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.histoNumAvs.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}

</SCRIPT> 

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Historique AVS - Détail
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain"  --%>

	<TR>
        <TD><LABEL for="nss"><ct:FWLabel key="HISTORIQUE_AVS_NSS"/></LABEL></TD>
		<TD> 
		
			<!--
			<INPUT   name="numAvsActuel" type="text" class="libelleLong"  value="<%=viewBean.getNumAvs()%> ">
			-->
				<%
				String ean13 ="false";
				String prefxieNSS = globaz.pyxis.application.TIApplication.pyxisApp().getNSSPrefixe();
				if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equalsIgnoreCase("add"))) { 
					if (viewBean.getNumAvs().length()==16) {
		  	     	 ean13 ="true";
		 	       if (viewBean.getNumAvs().startsWith(prefxieNSS)) {
		 	           viewBean.setNumAvs(viewBean.getNumAvs().substring(3));
		 	       }
		 	     } else {
		 	       ean13 ="false";
			      }     
			    } else {
	    	      ean13 =viewBean.getNumAvs();
	    	      if ("true".equals(ean13)) {
	    	        if (viewBean.getNumAvs().startsWith(prefxieNSS)) {
	    	          viewBean.setNumAvs(viewBean.getNumAvs().substring(3));
	   	           }
		     	  }
		        }
				%>
			<ct1:nssPopup prefixePays="<%=globaz.pyxis.application.TIApplication.pyxisApp().getNSSPrefixe()%>"  avsMinNbrDigit="99" nssMinNbrDigit="99" newnss="<%=ean13%>" name="numAvs" value="<%=viewBean.getNumAvs()%>" /> 
			<INPUT type="hidden" name="forIdTiers" value='<%=idTiers%>' >
			<INPUT type="hidden" name="idTiers" value='<%=idTiers%>' >
		</TD>
	</TR>
	<TR>
		  <TD nowrap width="140">Motif</TD>
          <TD nowrap>
		  <ct:FWCodeSelectTag name="MotifHistorique"
            		defaut="<%=viewBean.getMotifHistorique()%>"
            		codeType="PYMOTIFHIS" wantBlank="true"/>
		  </TD>
    </TR>    
    <TR>
		<TD><LABEL for="EntreeVigieur"><ct:FWLabel key="ENTREE_VIGUEUR"/></LABEL></TD>
		<TD><ct:FWCalendarTag name="EntreeVigueur" value="<%=viewBean.getEntreeVigueur()%>"/></TD>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%> 

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>