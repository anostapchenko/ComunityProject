const button = document.querySelector("input[type=submit]")
const formGroup = document.querySelector("input[type=email]")
button.addEventListener('click', () => {
    const request = {}
    request['username'] = document.querySelector("#username").value
    request['password'] = document.querySelector("#password").value
    loadToken(request)
}, false)

function clearError() {
    if($('#userEmail div').length > 1) {
        $('#userEmail div')[0].remove();
    }
    $('#username').empty().val('')
    $('#password').val('');
}

function loadToken(request) {

    document.cookie = 'rememberme=' + document.querySelector("#rememberme").checked;

    fetch('http://localhost:8091/api/auth/token', {
            method: 'POST',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(request)
        }
    )
        .then(res => res.json())
        .then(res => {
            document.cookie = `token=${res["token"]}`
        }
        )
        .then(() => window.location.assign("http://localhost:8091/main"))
        .catch(() => formGroup.insertAdjacentHTML("beforebegin", "<div><span style='color: red'><b>Invalid username or password</b></span></div>"))
        .then(() => clearError())
}