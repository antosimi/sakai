var USERANTO = {};
USERANTO.showMess=false;


// mica metoda mai mult sa invat
// Get an element by ID
USERANTO.get = function (id) {
    return document.getElementById(id);
};


USERANTO.doFaceRec = function () {
    debugger;
    var faceRecButton = USERANTO.get("eventSubmit_faceRecognitionAnto");
    USERANTO.showMess=true;

    var hai1= USERANTO.get("hai_copii_iar");
    USERANTO.display(hai1,true);

    faceRecButton.style.backgroundColor="#ff00ff";
    faceRecButton.disabled = true;


    //setMainFrameHeightNow(window.name);
};

// Show/hide the given element
USERANTO.display = function (element, show) {
    if (show) {
        if (element) {
            element.style.display = "block";
            element.style.color="#ff00ff";
        }
    }
    else {
        if (element) {
            element.style.display = "none";
        }
    }
};
