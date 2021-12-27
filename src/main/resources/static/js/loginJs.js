const button = document.querySelector("input[type=submit]")
button.addEventListener('click', () => {
    const request = {}
    request['username'] = document.querySelector("#username").value
    request['password'] = document.querySelector("#password").value
    loadToken(request)
}, false)

function loadToken(request) {
    fetch("http://localhost:8091/api/auth/token", {
            method: 'POST',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(request)
        }
    )
        .then(res => res.json())
        .then(res => {
            document.cookie=`token=${res["token"]}`
        }
        )
        .then(() => window.location.assign("http://localhost:8091/main"))
}