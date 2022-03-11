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



//создаем новый объект пагинации и передаем аргументы в конструктор
let pagination = new Pagination(
    'http://localhost:8091/api/user/reputation',            //url
    4,                                                //количество объектов
    'users_grid',                             //id div куда будут вставляться объекты
    'users_navigation',                        //id div куду будет вставляться нумерация

    function (arrayObjects) {                         //функция, которая задаёт - как будут вставляться объекты
        let ul = document.createElement('div');        //здесь был создан корневой узел(список)
        ul.className = "card-deck";
        if (arrayObjects != null && arrayObjects.length > 0) { //проверка массива с объектами
            for (let num = 0; num < arrayObjects.length; num++) {
                let li = document.createElement('div'); //для каждого объекта создаем узел
                li.className = "card border-0";
                li.insertAdjacentHTML('beforeend',
                    `
            <div class="user-card">
                <div><img src=${arrayObjects[num].imageLink} class="avatar card-img-top" alt="..."></div>
                <div class="card-body">
                    <div class="card-title"><a href="#">${arrayObjects[num].fullName}</a></div>
                    <div class="card-text small">${arrayObjects[num].city}</div>
                    <div class="card-text font-weight-bolder small">${arrayObjects[num].reputation}</div>
                    <div class="card-text small"><a href="#">python</a>, <a href="#">java</a>, <a href="#">c#</a></div>
                </div>
            </div>
        `); //помещаем текстовое представление в узел
                ul.appendChild(li);             //добавляем узел в корневой
            }
        }
        return ul;
    });

init();

function showPage(event, num) {
    pagination.showPage(event, num);
}

async function init() {
    await pagination.showPage(null, 1);
}