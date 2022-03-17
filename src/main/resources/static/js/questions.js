activateSideBar()

function activateSideBar () {
    document.querySelector("#sidebar_questions").classList.add("active")
}

function getCookie(name){
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? ('Bearer ' + decodeURIComponent(matches[1])) : undefined;
}

async function getRelatedTags(numItems){

    let tags = {};
    let token1 = getCookie('token');
    let relatedTags = document.querySelector(`#relatedTags`);
    relatedTags.innerHTML = '';

    await fetch('/api/user/tag/related', {
        method: 'GET',
        headers: {
            "Content-type": "application/json",
            "Authorization": `${token1}`,
        }
    })
        .then(data => data.json())
        .then(ob => {tags = ob})
        .catch(mess => {
            console.log(mess);
        })

    if(numItems === 0){
        numItems = tags.length;
    }

    for (let num = 0; num < numItems; num++){

        let divFirst = document.createElement('div');
        divFirst.classList.add("mb-1");

        divFirst.innerHTML =
            '<a href="#"><span class="badge bg-info text-light">'+tags[num].name+'</span></a>'+
            '<span class="text-muted">×</span>'+
            '<span class="text-muted">'+tags[num].countQuestion+'</span>';
        relatedTags.appendChild(divFirst);
    }

    let divLink = document.createElement('div');
    divLink.classList.add("btn_link_custom");
    divLink.classList.add("mt-2");
    divLink.innerHTML = "больше связанных меток";

    relatedTags.appendChild(divLink);

    divLink.addEventListener("click", {
        handleEvent(event){
            getRelatedTags(0);
        }
    });
};

getRelatedTags(3);















