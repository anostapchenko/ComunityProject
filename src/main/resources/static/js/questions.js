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

let selectedValue;
let divTrackedList;
let divIgnoredList;
let listTrackedTag;
let listIgnoredTag;
let iptAddTrackedTag;
let iptAddIgnoredTag;
let btnAddTrackedTag;
let btnAddIgnoredTag;
let btnMakeTrackedCard = document.querySelector("#btn_add_tag_tracked");
let btnMakeIgnoredCard = document.querySelector("#btn_add_tag_ignored");

function makeTrackedCard(){
    btnMakeTrackedCard.parentElement.innerHTML =
        '<div class="mb-1" id="trackedList"></div>' +
        '<div>' +
            '<div class="d-inline-block">' +
                '<input class="form-control" list="listTrackedTag" type="text" id="ipt_add_tag_tracked">' +
                '<datalist id="listTrackedTag"></datalist>' +
            '</div>' +
            '<div class="d-inline-block">' +
        '       <a id="btn_add_tracked_tag" href="#" class="btn btn-info">+</a>' +
            '</div>' +
        '</div>';

    listTrackedTag = document.querySelector("#listTrackedTag");
    iptAddTrackedTag = document.querySelector("#ipt_add_tag_tracked");
    iptAddTrackedTag.addEventListener('input', () => {

        tagInputEvent(iptAddTrackedTag.value, listTrackedTag, iptAddTrackedTag);
    })

    divTrackedList = document.querySelector("#trackedList");
    btnAddTrackedTag = document.querySelector("#btn_add_tracked_tag");
    btnAddTrackedTag.addEventListener('click', () => {

        addTagClickEvent(addTag, addTagInCard, divTrackedList, iptAddTrackedTag, "tracked");
    })
}

function makeIgnoredCard(){
    btnMakeIgnoredCard.parentElement.innerHTML =
        '<div class="mb-1" id="ignoredList"> </div>' +
        '<div>' +
            '<div class="d-inline-block">' +
                '<input class="form-control" list="listIgnoredTag" type="text" id="ipt_add_tag_ignored">' +
                '<datalist id="listIgnoredTag"></datalist>' +
            '</div>' +
            '<div class="d-inline-block">' +
                '<a id="btn_add_ignored_tag" href="#" class="btn btn-info">+</a>' +
            '</div>' +
        '</div>';

    listIgnoredTag = document.querySelector("#listIgnoredTag");
    iptAddIgnoredTag = document.querySelector("#ipt_add_tag_ignored");
    iptAddIgnoredTag.addEventListener('input', () => {

        tagInputEvent(iptAddIgnoredTag.value, listIgnoredTag, iptAddIgnoredTag);
    })

    divIgnoredList = document.querySelector("#ignoredList");
    btnAddIgnoredTag = document.querySelector("#btn_add_ignored_tag");
    btnAddIgnoredTag.addEventListener('click', () => {

        addTagClickEvent(addTag, addTagInCard, divIgnoredList, iptAddIgnoredTag, "ignored");
    })
}

async function loadCards(){

    let tags = await getTags("tracked");

    if(tags.length === 0
        || tags.length === undefined){
        return
    }

    await makeTrackedCard();

    for (let num = 0; num < tags.length; num++){
        addTagInCard(tags[num], divTrackedList, "tracked");
    }

    tags = await getTags("ignored");

    if(tags.length === 0
        || tags.length === undefined){
        return
    }

    await makeIgnoredCard();

    for (let num = 0; num < tags.length; num++){
        addTagInCard(tags[num], divIgnoredList, "ignored");
    }
}

loadCards();

btnMakeTrackedCard.addEventListener('click', () => {
    makeTrackedCard();
})

btnMakeIgnoredCard.addEventListener('click', () => {
    makeIgnoredCard();
})

async function addTagClickEvent(addTags, addTagInCard, divList, iptAddTag, mode){

    await addTag(selectedValue, mode)
            .then(
                tags => {addTagInCard(tags, divList, mode)}
            );
    iptAddTag.value = "";
}

async function tagInputEvent(value, tagsList, iptAddTag){

    tagsList.innerHTML = "";

    await getTagsListLike(value)
            .then(
                tags => {fillDataList(tags, tagsList)}
            );

    if(iptAddTag.list.options.length === 1){
        selectedValue = [...iptAddTag.list.options].find(option => option.innerText === iptAddTag.value).id;
        tagsList.innerHTML = "";
    }else{
        selectedValue = undefined;
    }
}

async function tagClickEvent(tag, mode){

    await deleteTag(tag, mode);
    tag.remove();
}

function addTagInCard(tag, divList, mode){

    if(tag.name === undefined){return;}

    let eventClick = "tagClickEvent(this, '" + mode + "')";

    let newDivTag = document.createElement('div');
    newDivTag.classList.add("mt-1");
    newDivTag.classList.add("mb-1");
    newDivTag.innerHTML = tag.name;

    let newTag = document.createElement('span');
    newTag.classList.add("mr-1");
    newTag.classList.add("badge");
    newTag.classList.add("bg-info");
    newTag.classList.add("text-light");
    newTag.setAttribute('id', tag.id);
    newTag.setAttribute('onclick', eventClick);
    newTag.appendChild(newDivTag);

    divList.appendChild(newTag);
}

function fillDataList(tags, tagsList){

    if(tags.length === 0){return;}

    for (let num = 0; num < tags.length; num++){

        let option = document.createElement('option');
        option.text = tags[num].name;
        option.id   = tags[num].id;
        tagsList.appendChild(option);
    }
}

async function getTagsListLike(value){
    return myFetch('/api/user/tag/latter?value='+value, 'GET');
}

async function getTags(mode){
    return myFetch('/api/user/tag/'+mode, 'GET');
}

async function addTag(id, mode){
    return myFetch('/api/user/tag/'+mode+'/add?tag='+id, 'POST');
}

async function deleteTag(tag, mode){
    return myFetch('/api/user/tag/'+mode+'/delete?tag='+tag.id, 'DELETE');
}

async function myFetch(URL, metod){

    let tags = {};
    let token1 = getCookie('token');

    await fetch(URL, {
            method: metod,
            headers: {
                "Content-type": "application/json",
                "Authorization": `${token1}`,
            }
        }
    )
        .then(data => data.json())
        .then(ob => {tags = ob})
        .catch(mess => {
            console.log(mess);
        })

    return tags;
}












