activateSideBar()

function activateSideBar () {
    document.querySelector("#sidebar_tags").classList.add("active")
}

//создаем новый объект пагинации и передаем аргументы в конструктор
let pagination = new Pagination(
    'http://localhost:8091/api/user/tag/popular',
           2,
     'pagination_objects',
       'navigation',
          function (arrayObjects) {

        let divFirst = document.createElement('div');
        if (arrayObjects != null && arrayObjects.length > 0) {

            let numCardsInDeck = 0;
            let divCardDeck = undefined;

            for (let num = 0; num < arrayObjects.length; num++) {

                if(numCardsInDeck === 0) {
                    numCardsInDeck = 4;
                    divCardDeck = document.createElement('div');
                    divCardDeck.classList.add("card-deck");
                    divCardDeck.classList.add("tags-card-deck");
                    divFirst.appendChild(divCardDeck);
                }

                let divCard = document.createElement('div');
                divCard.classList.add("card");

                countQuestion        = arrayObjects[num].countQuestion;
                description          = arrayObjects[num].description;
                nameTag              = arrayObjects[num].name;
                questionCountOneDay  = arrayObjects[num].questionCountOneDay;
                questionCountWeekDay = arrayObjects[num].questionCountWeekDay;

                divCard.innerHTML =
                    '<div class="tag-card">'+
                    '   <div class="card-body">'+
                    '       <div class="card-title"><a href="#"><span class="badge bg-info text-light">'+nameTag+'</span></a></div>'+
                    '       <p class="card-text small">'+description+'</p>'+
                    '       <div class="row">'+
                    '           <div class="col"><span class="card-text small text-muted">'+countQuestion+' questions</span></div>'+
                    '           <div class="col"><a class="card-link small text-muted" href="#">'+questionCountOneDay+' asked today, '+questionCountWeekDay+' this week</a></div>'+
                    '       </div>'+
                    '   </div>'+
                    '</div>';

                divCardDeck.appendChild(divCard);
                numCardsInDeck--;
            }
        }
        return divFirst;
    });

init();

function showPage(event, num) {
    pagination.showPage(event, num);
}

async function init() {
    await pagination.showPage(null, 1);
}