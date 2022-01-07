
function checkAndLogin(){
    $('#loginform').submit();
}

function checkAndRegister(){
    // var captchaRes = grecaptcha.getResponse();
    // if(captchaRes.length == 0) {
    //     showNotification('bottom','center','danger',"ERROR - Captcha empty!",'block');
    //     return;
    // }
    // $('#registerform input[name=recaptcharespone]').val(captchaRes);
    console.log(document.getElementById("customChecka1").checked);
    if(!document.getElementById("customChecka1").checked){
        showNotificationNew('notification-16', 3000, 'messages-16', Messages("register.checkbox"));
        return;
    }
    $('#registerform').submit();
}

$("#password").keyup(function(event) {
    if (event.keyCode == 13) {
        $("#loginButton").click();
    }
});

$("#email").keyup(function(event) {
    if (event.keyCode == 13) {
        $("#loginButton").click();
    }
});

function checkAndChangePass(){
    $('#changePassForm').submit();
}

