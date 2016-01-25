<%@page import="globaz.hermes.api.IHEAnnoncesViewBean"%>
<html>
<HEAD>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
</HEAD>
<%@ page import="globaz.globall.util.*,globaz.hermes.db.gestion.*,java.util.*"%>
<%
	//String compteIndividuelId = request.getParameter("compteIndividuelId");
	ArrayList list = new ArrayList();
	Object ecritureDansSession = session.getAttribute("EcrituresCISaisies");
	if(ecritureDansSession != null)
		list = (ArrayList)ecritureDansSession;
%>	
<BODY style="margin-left:0px; margin-right:0px;"  bgcolor="#F0F0F0">
<TABLE width="100%" border="0" cellspacing="0"> 
    <TR>
    <th>Abr.-Nr.</th>
    <th>Einkommenscode</th>
    <th>Periode</th>
    <th>Einkommen</th>	
    <th>Arbeitgeber oder Einkommensart</th>	
    <% int index38001 = 0;%>
    </TR>
<%	String rowStyle = "";
		for(int i=0;i<list.size();i++) {
			HEInputAnnonceViewBean line = (HEInputAnnonceViewBean)list.get(i);
			boolean condition = (i % 2 == 0);
	%>
    		<%condition = (index38001 % 2 == 0);%>
	<%
			if (condition) {
				rowStyle = "row";
			} else {
				rowStyle = "rowOdd";
			}
	%>
	<%if(line.getField(IHEAnnoncesViewBean.CODE_1_OU_2).equals("1")){
		index38001++;
	 %>
	 <%
		String numAff = line.getField(IHEAnnoncesViewBean.NUMERO_AFILLIE);
		if(line.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS).equals("8")){
			numAff = globaz.globall.util.JAUtil.formatAvs(numAff);
		}
		/* else {
			numAff = globaz.globall.util.JAStrings.insertRight(globaz.hermes.utils.StringUtils.unPad(numAff).trim(), ".", 3);
		}*/
	%>
		<TR class="<%=rowStyle%>">
			<td class="text"><%=numAff.equals("")?"":numAff%></td>
			<td class="text" align="right"><%=line.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_EXTOURNES)+line.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS)%></td>
			<td class="text"><%=line.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_DEBUT) + "-" + line.getField(IHEAnnoncesViewBean.DUREE_COTISATIONS_FIN)+ "."+ line.getField(IHEAnnoncesViewBean.ANNEE_COTISATIONS)%></td>
			<td class="text"><%=globaz.hermes.utils.CurrencyUtils.formatCurrency(line.getField(IHEAnnoncesViewBean.REVENU),true,true,true,2)%></td>
		<%}
					
		line = (i==list.size()-1)?null:(HEInputAnnonceViewBean)list.get(i+1);
		if(line != null && line.getField(IHEAnnoncesViewBean.CODE_1_OU_2).equals("2")){
			if(i+1 != list.size()-1){
				i++;
			}%>
			<td class="text"><%=line.getField(IHEAnnoncesViewBean.PARTIE_INFORMATION)%></td>
		<%}%>
		</TR>
	<% } %>

</TABLE>
</body>
</html>