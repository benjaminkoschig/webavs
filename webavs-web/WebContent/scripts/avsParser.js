<!--hide this script from non-javascript-enabled browsers

/**
 ** Olivier Carnal
 **
 **
 ** Codage Année :
 ** ___.XX.___.___
 **
 **
 ** Codage du sexe :
 ** ___.__.X__.___
 **
 ** Si le code est entre 1 et 4, il s'agit d'un homme
 ** Si le code est entre 5 et 8, il s'agit d'une femme
 ** En fait, il est simplement rajouté 400 au code de la
 ** date de naissance expliqué ci-dessous si le sexe est
 ** féminin
 **
 **
 ** Codage de la date de naissance :
 ** ___.__.XXX.___
 **
 **
 ** Semestre    mois            jours
 ** 1           Janvier         101 à 131
 ** 1           Février         132 à 160
 ** 1           Mars            163 à 193
 ** 2           Avril           201 à 230
 ** 2           Mai             232 à 262
 ** 2           Juin            263 à 292
 ** 3           Juillet         301 à 331
 ** 3           Août            332 à 362
 ** 3           Septembre       363 à 392
 ** 4           Octobre         401 à 431
 ** 4           Novembre        432 à 461
 ** 4           Décembre        463 à 493
 **
 **
 ** les dates 0 à 100,161, 162, 194 à 200,
 ** 231, 293 à 300, 393 à 400, 462 et 494 à 500
 ** n'existent pas
 **
 **/

  function AvsParser (str) {

    var basculeAnnee 
    var noAvs
    this.noAvs = str;
    var avsPart = this.noAvs.split(".");
   
    this.newYear= new Date().getFullYear().toString().substr(0,2);
    this.oldYear=parseInt(this.newYear-1);
    this.basculeAnnee = new Date().getFullYear().toString().substr(2);
    
    // ------------------------------------------------------
    // getYear
    // ------------------------------------------------------
    this.getYear = function() {
        str = avsPart[1];
        return str;
    }
    
    // ------------------------------------------------------
    // getFullYear
    // ------------------------------------------------------
     this.getFullYear = function() {
           str = avsPart[1];

           if (parseInt(str,10)>this.basculeAnnee) {
                str = this.oldYear+str;
           } else {
                str = this.newYear+str;
           }
           return str;
    }
    
    // ------------------------------------------------------
    // getMonth
    // ------------------------------------------------------
    this.getMonth = function () {

        str = avsPart[2];
        if (this.getSexe() == "F") {
            str = parseInt(str-400).toString();
        }

        var semestre = str.charAt(0);
        var day = str.substr(1);
        month =  parseInt(semestre)*3 - parseInt(3-(day/31));

        if (parseInt(month)<10){
            month = "0"+month
        }
        return month;
    }
    
    // ------------------------------------------------------
    // getDay
    // ------------------------------------------------------
    this.getDay = function() {
        str = avsPart[2];
        jour = str.substr(1);
        jour%=31;
        
        if (parseInt(jour)==0) {
		   jour=31;
		}
        
        if (parseInt(jour)<10) {
            jour = "0"+parseInt(jour);
        }
        return jour;
    }

    // ------------------------------------------------------
    // getBirthDate
    // ------------------------------------------------------
    this.getBirthDate = function() {
    
        return this.getDay()+"."+this.getMonth()+"."+this.getFullYear();

    }
    
    // ------------------------------------------------------
    // getSexe
    // ------------------------------------------------------
    this.getSexe = function() {
        str = avsPart[2];
        sexe = "M"
        if (parseInt(str)>493) {
            sexe = "F"
        }
        return sexe;
    }
    
    // ------------------------------------------------------
    // isWellFormed
    // ------------------------------------------------------
    this.isWellFormed = function() {
        // simple control du format XXX.XX.XXX.XXX 
        var result = true;
        re = /^\d{3}.\d{2}.\d{3}.\d{3}$/;
        if ((re.test(this.noAvs))== false) {
                       result = false;
        }
        return result;   
    }

    // ------------------------------------------------------
    // isSwiss
    // ------------------------------------------------------
    this.isSwiss = function() {
        
        return true;   
    }


    // ------------------------------------------------------
    // setBasculeAnnee
    // ------------------------------------------------------
    this.setBasculeAnnee = function(bascule) {
	this.basculeAnnee = bascule;
    }

    	
  }
  
// stop hiding -->