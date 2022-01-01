async function getPagination(token, URL, page, items) {
    let pagination;
    URL +="?page=" + page;
    if(items != null) URL += "&items=" + items;
    await fetch(URL, {
        method: 'GET',
        headers: {
            "Content-Type" : "application/json",
            "Authorization" : `${token}`,
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