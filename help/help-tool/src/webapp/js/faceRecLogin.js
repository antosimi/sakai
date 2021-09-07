//this is a script on how to open, retieve data and train web camera

//this will open the web camera:
var video = document.querySelector("#videoElement");
var img_upload ;
var base64FromPhoto;
function openWebCamera() {
    debugger;
    var  videoStyle = document.getElementById('videoElement');
    videoStyle.style.backgroundImage='none';
    videoStyle.offsetWidth=500;
    videoStyle.clientWidth=600;
    // var video = document.querySelector("#videoElement");
    debugger;
    if (navigator.mediaDevices.getUserMedia) {
        debugger;
        navigator.mediaDevices.getUserMedia({video: true})
            .then(function (stream) {
                video.srcObject = stream;
                video.play();
                debugger;
                var mm= videoStyle.offsetWidth;

            })
            .catch(function (err0r) {
                console.log("Something went wrong!");
            });
    }
}
function takepicture() {
    debugger;
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
    //first we take a picture:
    takepicture();

    var email_toSend = document.getElementById("email_tag");
    //var fd = new FormData();
    //var new_blob = base64ToBlob(base64FromPhoto, 'image/png');
    var val_trimisa;
    if(img_upload!=null){
        val_trimisa=img_upload;
    }else{
        val_trimisa=base64FromPhoto
    }
    //fd.append('photo', new_blob);
    var final_val2send=email_toSend.value + " ##### " + val_trimisa;

    closeWebCam();
    $.ajax({
        type: "POST",
        contentType : false,
        processData: false,
        url: "faceLogin",
        data: final_val2send,
        headers:{
            "Content-Length": final_val2send.length
        },
        success :function(result) {
            // do what ever you want with data
            debugger;
           if(result==true){
               successfulLogin();
               setTimeout(makeLoginHappen,2000);
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
/*
function base64ToBlob(base64, mime)
{
    mime = mime || '';
    var sliceSize = 1024;
    var byteChars = window.atob(base64);
    var byteArrays = [];

    for (var offset = 0, len = byteChars.length; offset < len; offset += sliceSize) {
        var slice = byteChars.slice(offset, offset + sliceSize);

        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        var byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {type: mime});
}
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
    document.getElementById("resultMsg").style.color="#00688b";
    document.getElementById("resultMsg").innerHTML = "Login successfully";
    document.getElementById("resultMsg").style.visibility='visible';
}

function failedLogin(){
    document.getElementById("resultMsg").style.color="#D71D2D";
    document.getElementById("resultMsg").innerHTML = "Login failed";
    document.getElementById("resultMsg").style.visibility='visible';
}
function wrongEmail(){
    document.getElementById("resultMsg").style.color="#D71D2D";
    document.getElementById("resultMsg").innerHTML = "Wrong email";
    document.getElementById("resultMsg").style.visibility='visible';
}

function makeLoginHappen(){
    debugger;
    var email_toSend = document.getElementById("email_tag");
    var da11 = true;
    $.ajax({
        type: "POST",
        contentType : false,
        processData: false,
        url: "xlogin",
        data: da11,
        headers:{
            "Face-Recognition-Login-OK": email_toSend.value
        },
        success :function(result) {
            // do what ever you want with data
            debugger;
            document.body.innerHTML=result;

            // else{
            //     failedLogin();
            // }
        }
        ,
        error: function (resp,text) {
            debugger;
            // if(resp.status==404 && text=="error"){
            //     wrongEmail();
            // }
            // else{
            //     failedLogin();
            // }
            failedLogin();
        }
    });
}

function closeWebCam(){
    navigator.getUserMedia({audio: false, video: true},
        function(stream) {
            // can also use getAudioTracks() or getVideoTracks()
            var track = stream.getTracks()[0];  // if only one media track
            // ...
            track.stop();
        },
        function(error){
            console.log('getUserMedia() error', error);
        });
}


