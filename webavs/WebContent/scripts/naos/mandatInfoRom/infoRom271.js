

var confirmMaj;

function reportNumber(line, pos, name) {
	
	if(line.value!='' && document.getElementById("forceNouvelleMasse" + pos).value == 'false') {
		confirmMaj = 0;
		periodeConcerne = document.getElementsByName("nouvellePeriodicite"+pos)[0];
		selectedPeriode = periodeConcerne.selectedIndex;
		periodiciteMasseSourceLine = $('[name="periodiciteNouvelleMasse'+ pos + '"]:checked') ;
		var lineValue = line.value;
		if(name=='nouvelleMasse') {
			checkMasseLimit(line,periodeConcerne,pos);
		}
		
		if(periodeConcerne!=null) {
			count=0;
			do {
				elem = document.getElementsByName(name+count)[0];
				periodeElem = document.getElementsByName("nouvellePeriodicite"+count)[0];
				periodiciteMasseReportedLine = $('[name="periodiciteNouvelleMasse'+ count + '"]:checked') ; 
				if(elem!=null && elem!=line && elem.value=='' && selectedPeriode==periodeElem.selectedIndex && periodiciteMasseSourceLine.val() == periodiciteMasseReportedLine.val()) {
					elem.value = lineValue;

					if(name=='nouvelleMasse') {
						checkMasseLimit(elem,periodeElem,count);
					}
					
					if(name=='nouvelleMasse' && document.getElementById("forceNouvelleMasse" + count).value == 'true')
					{
						elem.value = '0.00';
					}
					
				}	
				count++;
			} while (elem!=null);
		}
	}
	
}

function focusNextMasse(e,pos) {
	
	//si on presse sur tab on set le focus sur la prochaine nouvelle masse
	if(e.keyCode==9) {
		e.returnValue = false;

		focusM = document.getElementsByName("nouvelleMasse"+(pos+1))[0];
		if(focusM==null) {
			focusM = document.getElementsByName("nouvelleMasse0")[0];
		}
		focusM.focus();
	}
	
}

function reportPer(fraction, pos, per) {
	
		if(document.getElementById("forceNouvelleMasse" + pos).value == 'false'){
			count=0;
			do {
				periodiciteNvMasseReportedLine = document.getElementsByName("periodiciteNouvelleMasse"+count);
				inputNvMasseReportedLine = document.getElementsByName("nouvelleMasse"+count)[0];   
						
				if(periodiciteNvMasseReportedLine != null && inputNvMasseReportedLine != null && inputNvMasseReportedLine.value == '' ) {
					periodiciteNvMasseReportedLine[per].checked = true;
				}
				
				count++;
			} while (inputNvMasseReportedLine != null);
		}	
}

function checkMasseLimit(masse, periode, pos) {
	mnt = masse.value;
	
	// enlever les apostrophes
	while(mnt.indexOf('\'')!=-1) {
		mnt = mnt.replace('\'','');
	}
	
	// calculer le montant annuel
	if(document.getElementsByName('periodiciteNouvelleMasse'+pos)[2].checked) {
		mntToTest = mnt * 12;
	} else  if(document.getElementsByName('periodiciteNouvelleMasse'+pos)[1].checked){
		mntToTest = mnt * 4;
	} else {
		mntToTest = mnt;
	}
	
	if(mntToTest>200000) {
		//masse supérieure à 200'000.-
		if(periode.options[periode.selectedIndex].value!=<%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>) {
			libelle = document.getElementsByName("assuranceLibelle"+pos)[0].value;
			// demande de mise à jour de la périodicité
			if (confirmMaj==2 || (confirmMaj==0 && window.confirm(libelle+": <%=viewBean.getSession().getLabel("NAOS_SAISIE_MASSE_SUP")%>"))) {
				// mensuelle
				periode.value = <%=globaz.naos.translation.CodeSystem.PERIODICITE_MENSUELLE%>;
				confirmMaj=2;
			} else {
				confirmMaj=1;
			}
		}
	}
}