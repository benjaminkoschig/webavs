<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusListViewBean"%>
<%-- tpl:put name="zoneScripts" --%>

<%
	RFRechercheMotifsDeRefusListViewBean viewBean = (RFRechercheMotifsDeRefusListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	detailLink = "cygnus?userAction="+IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_DE_REFUS_ID"/></TH>	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_DE_REFUS_LIBELLE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_DE_REFUS_HAS_MONTANT"/></TH>	
    <%-- /tpl:put --%>    
<%@ include file="/theme/list/tableHeader.jspf" %>

    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>

		<%-- tpl:put name="zoneList" --%>
		<%
			RFRechercheMotifsDeRefusViewBean courant = (RFRechercheMotifsDeRefusViewBean) viewBean.getEntity(i);
			
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdMotifRefus() +
								"&idMotifRefus=" + courant.getIdMotifRefus() +
			   					"&descriptionFR=" + courant.getDescriptionFR().replace("'","\\'") +
			   					"&hasMontant=" + courant.getHasMontant() +
			   					"&idsSoin=" + courant.getIdsSoin() +
			   					"&isMotifRefusSysteme=" + courant.getIsMotifRefusSysteme() +"'";
			
			String image;
			if(courant.getHasMontant())
				image = courant.getImageSuccess();
			else
				image = courant.getImageError();
			
		%>		
		
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdMotifRefus() %></TD>     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDescriptionFR() %></TD>
     	<TD class="mtd" align="center" nowrap onClick="<%=detailUrl%>"><IMG src='<%=request.getContextPath()+image%>' alt="" name="imgCodeTypeDeSoin"></TD>     	  	

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>