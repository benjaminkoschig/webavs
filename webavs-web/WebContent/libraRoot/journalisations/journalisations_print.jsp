<%@ page language="java" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%

	globaz.libra.vb.journalisations.LIJournalisationsJointDossiersListViewBean viewBean = (globaz.libra.vb.journalisations.LIJournalisationsJointDossiersListViewBean) session.getAttribute("listViewBean");
	
%>
<HTML>
<HEAD>
	<TITLE><%= viewBean.getSession().getApplicationId() %></TITLE>
	<STYLE>

	body {
		font-family : Verdana,sans-serif;
	}

	<!-- 
	#title {
		font-size: 9pt;
	}

	#maintable {
		font-size: 9pt;
		width: 90%;
		border: 1px solid #0b0b0b;
	}
	
	#maintable thead {
		background-color: #D0D0D0;
	}

	#maintable thead th {
		padding: 5px;
		text-align: left;		
		border-bottom : 1px solid #0b0b0b;
	}

	#maintable tbody tr td {
		padding: 3px;
	}
	#maintable tbody .false{
		background-color: #E0E0E0;
	}
	#maintable tbody .true{
		background-color: #FFFFFF;
	}
	-->
	</STYLE>
</HEAD>
<BODY onLoad="self.focus()">
	<DIV style="width: 100%; text-align: center;">
		<!-- En-tête de la page -->
			<DIV style="width: 100%;"><hr/></DIV>
			<TABLE style="width: 90%; padding: 2px;" border="0" id="title">
				<TR>
					<TD colspan="2">
						<table width="100%" id="title">
							<TR><TD colspan="2"><b><u><ct:FWLabel key="ECRAN_CRIT_RECH"/></u></b></TD></TR>
							<tr>
								<TD>
									<b><ct:FWLabel key="ECRAN_IMP_TIERS"/></b>
									&nbsp;:&nbsp;
									<%= viewBean.getLibelleTiers() %>
									&nbsp;
									<b><%= viewBean.getLibelleVueFamille() %></b>
								</TD>
								<TD>
									<b><ct:FWLabel key="ECRAN_IMP_DOSSIER"/></b>
									&nbsp;:&nbsp;
									<%= viewBean.getLibelleDossier() %>
								</TD>		
							</tr>
							<TR>
								<TD>
									<b><ct:FWLabel key="ECRAN_PERIODE"/></b>
									&nbsp;:&nbsp;
									<%= (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getForDateDebut())?"00.00.0000":viewBean.getForDateDebut()) 
										+" - "+ (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getForDateFin())?"00.00.0000":viewBean.getForDateFin()) %>
								</TD>
								<TD>
									<b><ct:FWLabel key="ECRAN_IMP_USER"/></b>
									&nbsp;:&nbsp;
									<%= viewBean.getLibelleUtilisateur() %>
								</TD>					
							</TR>
						</table>
					</TD>
				</TR>
				<TR><TD colspan="2"></TD></TR>
				<TR>
					<TD colspan="2">
						<table width="100%" id="title">
							<TR><TD colspan="2"><b><u><ct:FWLabel key="ECRAN_DET_IMPR"/> (<%= viewBean.getSession().getLabel("MENU_OPTION_JOURNALISATIONS") %>)</u></b></TD></TR>
							<TR>
								<TD>
									<b><ct:FWLabel key="ECRAN_IMP_DATE"/> :&nbsp;</b>
									<%=globaz.globall.util.JACalendarGregorian.format(globaz.globall.util.JACalendarGregorian.today())%>
									&nbsp; / &nbsp;
									<b><ct:FWLabel key="ECRAN_IMP_HEURE"/> :&nbsp;</b>
									<%=new java.text.SimpleDateFormat("HH.mm.ss").format(java.util.Calendar.getInstance().getTime())%>
								</TD>			
							</TR>
						</table>
					</TD>
				</TR>					
			</TABLE>	

		<DIV style="width: 100%;"><hr/></DIV>
		<TABLE id="maintable" cellpadding="0" cellspacing="0">
			<THEAD>
				<TR>
			    	<TH>&nbsp;</TH>
		    		<TH><ct:FWLabel key="ECRAN_ECH_LI_DATE_CREATION"/></TH> 
		    		<TH><ct:FWLabel key="ECRAN_ECH_LI_LIBELLE"/></TH> 
		    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TYPE"/></TH> 
		    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TIERS"/></TH>
		    		<TH><ct:FWLabel key="ECRAN_ECH_LI_RAPPEL"/></TH>
		    		<TH><ct:FWLabel key="ECRAN_ECH_LI_RECEPTION"/></TH>
		    		<TH><ct:FWLabel key="ECRAN_ECH_LI_USER"/></TH> 
				</TR>
			</THEAD>		
			<TBODY>
				<%globaz.libra.vb.journalisations.LIJournalisationsJointDossiersListViewBean manager = (globaz.libra.vb.journalisations.LIJournalisationsJointDossiersListViewBean) session.getAttribute("listViewBean");
				boolean clazz=true;
				for(int i=0; i < manager.size(); i ++) {
					globaz.libra.vb.journalisations.LIJournalisationsJointDossiersViewBean line = (globaz.libra.vb.journalisations.LIJournalisationsJointDossiersViewBean) manager.getEntity(i);
				%>
					<TR  class="<%=clazz%>">
						<TD style="vertical-align: top;">
							<FONT face="<%="".equals(line.getPoliceIcone())?"&nbsp;":line.getPoliceIcone()%>" size="3" 
								<%=globaz.journalisation.constantes.JOConstantes.CS_JO_RAPPEL.equals(line.getCsTypeJournal())
									| !"".equals(line.getDateRappel()) ?"style=\"color: red\"": "style=\"color: black\""%>
								>&#<%="".equals(line.getIcone())?"&nbsp;":line.getIcone()%>
							</FONT>
						</TD>
						<TD style="vertical-align: top;"><%=line.getDate()%></TD>
						<TD style="vertical-align: top;"><%=line.getLibelleAffichage()%></TD>
						<TD style="vertical-align: top;"><%=line.getSession().getCodeLibelle(line.getValeurCodeSysteme())%></TD>
						<TD style="vertical-align: top;"><%=line.getDetailTiers()%></TD>
						<TD style="vertical-align: top;"><%=line.getDateRappel()%></TD>
						<TD style="vertical-align: top;"><%=line.getDateReception()%></TD>
						<TD style="vertical-align: top;"><%=line.getIdUtilisateur()%></TD>
					</TR>
				<%	clazz=!clazz;
				}%>
			</TBODY>
		</TABLE>
	</DIV>
</BODY>
</HTML>