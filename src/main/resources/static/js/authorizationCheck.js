
var request = new XMLHttpRequest();
request.open('GET', '/api/auth/check', false);
request.send(null);

if (request.status === 403) {
     window.location.assign("/accessDenied");
}else if (request.status !== 200){
     window.location.assign("/login");
}

