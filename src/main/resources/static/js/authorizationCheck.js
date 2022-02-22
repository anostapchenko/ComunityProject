
var request = new XMLHttpRequest();
var token = document.cookie.match(/token=(.+?)(;|$)/)
request.open('GET', '/api/auth/check', false);
if (token !== null) {
     request.setRequestHeader('Authorization', 'Bearer ' + token[1])
}
request.send(null);

if (request.status === 403) {
     window.location.assign("/accessDenied");
}else if (request.status !== 200){
     alert(request.status)
     window.location.assign("/login");
}

