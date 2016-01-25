 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
	<%@ page import="globaz.pyxis.adresse.formater.*"%>
	<%@ page import="globaz.pyxis.adresse.datasource.*" %>
	
   <%
    globaz.pyxis.db.adressepaiement.TIAdressePaiementDataListViewBean viewBean = (globaz.pyxis.db.adressepaiement.TIAdressePaiementDataListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize ();
    detailLink ="pyxis?userAction=pyxis.adressepaiement.adressePaiement.afficher&selectedId=";
    session.setAttribute("listViewBean",viewBean);
    %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 

      <Th width="16">&nbsp;</Th>
      <TH  align="left">Validité</TH>
      <TH  align="left">Paiement</TH>
      <TH  align="left">Bénéficiaire</TH>
 	  <TH  align="left">NSS & Nom du tiers</TH>
      <TH  align="left">Status</TH>
      
	        

      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 

       <%
       globaz.pyxis.db.adressepaiement.TIAdressePaiementData entity = (globaz.pyxis.db.adressepaiement.TIAdressePaiementData) viewBean.getEntity(i);
     actionDetail = "parent.location.href='"+detailLink+entity.getIdAdressePaiement()+"'";
    %>
    <TD class="mtd" width="16" >
    	
    	
<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.adressepaiement.adressePaiement.afficher&selectedId="+entity.getIdAdressePaiement();%>
<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />

    	
    	
    	
    	</TD>

	<%
		TIAdressePaiementDataSource dataSource = new TIAdressePaiementDataSource();
		dataSource.load(entity);
		
		TIAdressePaiementCppFormater formaterCCP = new TIAdressePaiementCppFormater();
		TIAdressePaiementBanqueFormater formaterBanque = new TIAdressePaiementBanqueFormater();
		TIAdressePaiementBeneficiaireFormater formaterBeneficiaire = new TIAdressePaiementBeneficiaireFormater();
		TIAdressePaiementValiditeFormater formaterValidite = new TIAdressePaiementValiditeFormater();
		
		String validite = formaterValidite.format(dataSource);
		String beneficiaire = formaterBeneficiaire.format(dataSource);
		String paiement = formaterCCP.format(dataSource);
		if (globaz.globall.util.JAUtil.isStringEmpty(paiement)) {
			paiement = formaterBanque.format(dataSource);
		}
		
	%>
	<!--<td><%=entity.getIdAdressePaiement()%></td>-->
	<td  class="mtd" onClick="<%=actionDetail%>"><pre style="font-size:10pt"><%=validite%></pre></td>
	<td  class="mtd" onClick="<%=actionDetail%>"><pre style="font-size:10pt"><%=paiement%></pre></td>
	<td  class="mtd" onClick="<%=actionDetail%>"><pre style="font-size:10pt"><%=beneficiaire%></pre></td>
	<td  class="mtd" onClick="<%=actionDetail%>"><pre style="font-size:10pt"><%=entity.getNss()+"\n"+ entity.getNomTiers1() +"\n"+ entity.getNomTiers2()%></pre></td>
	<td  class="mtd" onClick="<%=actionDetail%>"><pre style="font-size:10pt"><%=entity.getSession().getCodeLibelle(entity.getCode())%></pre></td>
	
	
	

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>