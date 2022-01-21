
var request = new XMLHttpRequest();
request.open('GET', 'http://localhost:8091/api/auth/check', false);
request.send(null);

if (request.status !== 200) {
    window.location.assign("http://localhost:8091/login");
}

