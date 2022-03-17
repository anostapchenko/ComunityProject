activateSideBar()


function activateSideBar() {
    document.querySelector("#sidebar_users").classList.add("active")
}

//создаем новый объект пагинации и передаем аргументы в конструктор
let pagination = new Pagination(
    'http://localhost:8091/api/user/reputation',            //url
    16,                                                //количество объектов
    'users_grid',                             //id div куда будут вставляться объекты
    'users_navigation',                        //id div куду будет вставляться нумерация

    function (arrayObjects) {                      //функция, которая задаёт - как будут вставляться объекты

        let mainDiv = document.createElement('div');

        if (arrayObjects != null && arrayObjects.length > 0) {

            let numCardsInDeck = 0;
            let cardDeckDiv = undefined;

            for (let num = 0; num < arrayObjects.length; num++) {

                if (numCardsInDeck === 0) {
                    numCardsInDeck = 4;
                    cardDeckDiv = document.createElement('div');
                    cardDeckDiv.className = "card-deck";
                    mainDiv.appendChild(cardDeckDiv);
                }

                let cardDiv = document.createElement('div');
                cardDiv.className = "card border-0";
                cardDiv.innerHTML =
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
        `;
                cardDeckDiv.appendChild(cardDiv);
                numCardsInDeck--;
            }
        }
        return mainDiv;
    });

init();

function showPage(event, num) {
    pagination.showPage(event, num);
}

async function init() {
    await pagination.showPage(null, 1);
}