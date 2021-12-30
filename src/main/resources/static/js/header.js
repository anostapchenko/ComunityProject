const logout_btn = document.querySelector("#logout_btn")

logout_btn.addEventListener("click", logout, false)

function logout() {
    document.cookie=`token=`
    window.location.assign("http://localhost:8091/login")
}