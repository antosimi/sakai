//this is a script on how to open, retieve data and train web camera
//this will open the web camera:
var video = document.querySelector("#videoElement");
var img_upload ;
var base64FromPhoto;
function openWebCamera() {
   // var video = document.querySelector("#videoElement");
    debugger;
    if (navigator.mediaDevices.getUserMedia) {
        debugger;
        navigator.mediaDevices.getUserMedia({video: true})
            .then(function (stream) {
                video.srcObject = stream;
                video.play();
            })
            .catch(function (err0r) {
                console.log("Something went wrong!");
            });
    }
}
function takepicture() {
    var  canvas = document.getElementById('canvas');
    var context = canvas.getContext('2d');
    debugger;
    if(true){
        debugger;
        canvas.width = video.clientWidth;
        canvas.height = video.clientHeight;
        context.drawImage(video, 0, 0, video.clientWidth, video.clientHeight);

        base64FromPhoto = canvas.toDataURL('image/png').split(';base64,')[1];
    } else {
       // clearphoto();
    }
}

function trimiteCeva(){
    debugger;
    //First of all we take a photo:
    takepicture();

    var email_toSend = document.getElementById("email_tag");

    var val_trimisa;
    if(img_upload!=null){
        val_trimisa=img_upload;
    }else{
        val_trimisa=base64FromPhoto
    }
    var final_val2send=email_toSend.value + " ##### " + val_trimisa;

    $.ajax({
        type: "POST",
        contentType : false,
        processData: false,
      //dataType : 'json',
        url: "signupFace",
        data: final_val2send,
        headers:{
            "Content-Length": final_val2send.length
        },
        success :function(result) {
            // do what ever you want with data
            debugger;
            if(result==true){
                successfulLogin();
            }else{
                failedLogin();
            }
        },
        error: function (resp,text) {
            debugger;
            if(resp.status==404 && text=="error"){
                wrongEmail();
            }
        }

    });
}


/*const getBase64FromUrl = async (url) => {
    const data = await fetch(url);
    const blob = await data.blob();
    return new Promise((resolve) => {
        const reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onloadend = () => {
            const base64data = reader.result;
            resolve(base64data);
        }
    });
}

window.addEventListener('load', function() {
    debugger;
    document.querySelector('input[type="file"]').addEventListener('change', function() {
        if (this.files && this.files[0]) {
            img = document.querySelector('img');
            img.onload = () => {
                URL.revokeObjectURL(img.src);  // no longer needed, free memory
            }

            img.src = URL.createObjectURL(this.files[0]); // set src to blob url
            debugger;
            document.getElementById("oduda").innerHTML = getBase64FromUrl(img.src);
        }
    });
});

 */
function readImage(input) {
    if ( input.files && input.files[0] ) {
        var FR= new FileReader();
        FR.onload = function(e) {
            $('#img').attr( "src", e.target.result );
            $('#base').text( e.target.result );
            debugger;
            img_upload=e.target.result.split(';base64,')[1];
        };
        FR.readAsDataURL( input.files[0] );

    }
}

$("#asd").change(function(){
    readImage( this );
});

function successfulLogin(){
    debugger;
    document.getElementById("resultMsg").style.color="#00688b";
    document.getElementById("resultMsg").innerHTML = "Face recognition train succeed";
    document.getElementById("resultMsg").style.visibility='visible';
}

function failedLogin(){
    document.getElementById("resultMsg").style.color="#D71D2D";
    document.getElementById("resultMsg").innerHTML = "Face recignition train failed";
    document.getElementById("resultMsg").style.visibility='visible';
}

function wrongEmail(){
    document.getElementById("resultMsg").style.color="#D71D2D";
    document.getElementById("resultMsg").innerHTML = "Wrong email";
    document.getElementById("resultMsg").style.visibility='visible';
}
