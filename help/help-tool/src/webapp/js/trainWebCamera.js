//this is a script on how to open, retieve data and train web camera

//this will open the web camera:
var video = document.querySelector("#videoElement");
var video_width = document.getElementById("videoElement").wid;
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
        document.getElementById("ceva2").innerHTML = base64FromPhoto;
        //photo.setAttribute('src', data);
    } else {
       // clearphoto();
    }
}

function trimiteCeva(){
    debugger;

    // var xhr = new XMLHttpRequest();
    // xhr.onreadystatechange = function() {
    //     debugger;
    //     if (xhr.readyState == 4) {
    //         debugger;
    //         document.getElementById("ceva1").innerHTML = xhr.responseText;
    //         var data = xhr.responseText;
    //         alert(data);
    //     }
    // }


    var email_toSend = document.getElementById("email_tag");
    var fd = new FormData();
    var new_blob = base64ToBlob(base64FromPhoto, 'image/png');
    var val_trimisa;
    if(img_upload!=null){
        val_trimisa=img_upload;
    }else{
        val_trimisa=base64FromPhoto
    }
    fd.append('photo', new_blob);
    // xhr.open('GET', 'bla', true);
    // xhr.send(params);
    $.ajax({
        type: "POST",
        contentType : false,
        processData: false,
      //dataType : 'json',
        url: "bla",
        data: email_toSend.value + " ##### " + val_trimisa,
        headers:{
            "Content-Length":new_blob.length
        },
        success :function(result) {
            // do what ever you want with data
            debugger;
             var mama=result;
        }
    });
}

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
            document.getElementById("oduda").innerHTML = img_upload;
        };
        FR.readAsDataURL( input.files[0] );

    }
}

$("#asd").change(function(){
    readImage( this );
});

//this will close the web camera:
