  function CodeSystemPopup() {
    var elemWindow;
    var elemNew;
    var elemOld;

    var elemMotifCode;
    var elemDate;
    var libelle;

    this.elemWindow = window;
    
    this.setMotifElement = function (eCode) {
      this.elemMotifCode = eCode;
      if (document.getElementsByName(eCode)[0]==null){
        alert("CodeSystemPopup - element not found : "+eCode)
      }
    }

    this.setDateElement = function (eDate) {
      this.elemDate = eDate;
      if (document.getElementsByName(eDate)[0]==null) {
        alert("CodeSystemPopup - element not found : "+eDate);
      }

    }

    this.setElementToCheck = function (e1,e2) {
      this.elemNew = e1;
      this.elemOld = e2;
    }


    this.setLibelle = function (str) {
      this.libelle = str
    }

    this.hasChanged = function () {

        var e1 = document.getElementsByName(this.elemNew)[0];
        var e2 = document.getElementsByName(this.elemOld)[0];

        if (e1 == null) {
                  alert("CodeSystemPopup - element introuvable : "+this.elemNew);
                  return false;
        }
        if (e2 == null) {
          alert("element not found : "+this.elemOld);
          return false;
        }

        if (e1.value == e2.value) {
          return false
        }
        return true ;
    }
  }