activateSideBar()


function activateSideBar() {
    document.querySelector("#sidebar_users").classList.add("active")
}


function getAllUsers() {
    const usersInfo = fetch('api/user/reputation?page=1&items=10',
        {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token[1]
            }
        }).then((response) => {
        return response.json();
    }).then((data) => {
        data.items.forEach((user) => {
            console.log(user)
            document.querySelector('#UsersGrid').insertAdjacentHTML('beforeend',
                `<div class="card border-0">
            <div class="user-card">
                <div><img src=${user.imageLink} class="avatar card-img-top" alt="..."></div>
                <div class="card-body">
                    <div class="card-title"><a href="#">${user.fullName}</a></div>
                    <div class="card-text small">${user.city}</div>
                    <div class="card-text font-weight-bolder small">${user.reputation}</div>
                    <div class="card-text small"><a href="#">python</a>, <a href="#">java</a>, <a href="#">c#</a></div>
                </div>
            </div>
        </div>`);})})

}


// getAllUsers()