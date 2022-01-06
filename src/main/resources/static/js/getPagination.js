// async function getPagination(token, URL, page, items) {
//     let pagination;
//     URL +="?page=" + page;
//     if(items != null) URL += "&items=" + items;
//     await fetch(URL, {
//         method: 'GET',
//         headers: {
//             "Content-Type" : "application/json",
//             "Authorization" : `${token}`,
//         }
//     })
//         .then(data => data.json())
//         .then(ob => {
//             pagination = ob;
//         });
//     return pagination;
// }

//pagination API url
let pagination_url = "";
//Authorization token                
let token = "";
//Count of object on one page
let items = null;
//node - where and how will add objects 
let objectNodeId = "";
//node - where will add page`s numbers
let navNodeId = "";
//must return node for objects
let display = null;
//attributes for navigation numbers
let liAttributes = new Array();

async function showPage(event, num) {
    if (event != null) {
        event.preventDefault();
    }
    let pageDto = await getPageDto(token, pagination_url, num, items);
    showNavigation(navNodeId, liAttributes, pageDto.totalPageCount);
    showObjects(objectNodeId, display, pageDto.items);
}

function showObjects(objectNodeId, display, arrayObjects) {
    let table = document.querySelector(`#${objectNodeId}`);
    let node = display(arrayObjects);
    table.appendChild(node);
}

function showNavigation(navNodeId, liAttributes, pageCount) {
    let navigation = document.querySelector(`#${navNodeId}`);
    let ul = document.createElement('ul');
    ul.setAttribute('name', 'pages');
    for (let num = 1; num <= pageCount; num++) {
        let li = document.createElement('li');
        li.textContent = num;
        li.setAttribute('onclick', `showPage(event, ${num})`);
        if (liAttributes.length > 0) {
            for (let attribute of liAttributes) {
                li.setAttribute(attribute.name, attribute.value);
            }
        }
        ul.appendChild(li);
    }
    navigation.innerHTML = "";
    navigation.appendChild(ul);
}

async function getPageDto(token, URL, page, items) {
    let pagination;
    URL += "?page=" + page;
    if (items != null) URL += "&items=" + items;
    await fetch(URL, {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                "Authorization": `${token}`,
            }
        })
        .then(data => data.json())
        .then(ob => {
            pagination = ob;
        })
        .catch(mess => {
            console.log(mess);
        })
    return pagination;
}
