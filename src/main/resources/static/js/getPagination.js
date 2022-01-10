class Pagination {

    constructor(pagination_url, items, objectNodeId, navNodeId, display, pageNumAttr) {
        this.pagination_url = pagination_url;       //url
        this.items = items;                         //количество объектов
        this.objectNodeId = objectNodeId;           //id div куда будут вставляться объекты
        this.navNodeId = navNodeId;                 //id div куда будет вставляться нумерация
        this.display = display;                     //функция, которая задаёт - как будут вставляться объекты
        this.pageNumAttr = pageNumAttr;             //атрибуты для заголовка и нумерации страниц
    }

    //получение токена происходит из cookie.
    async showPage(event, num) {
        if (event != null) {
            event.preventDefault();
        }
        await this.getPageDto(this.getCookie('token'), this.pagination_url, num, this.items)
            .then(pageDto => {
                this.showNavigation(this.navNodeId, this.pageNumAttr, pageDto.totalPageCount, num);
                this.showObjects(this.objectNodeId, pageDto.items);
            })
    }

    showObjects(objectNodeId, arrayObjects) {
        let table = document.querySelector(`#${objectNodeId}`);
        table.innerHTML = "";
        let node = this.display(arrayObjects);
        table.appendChild(node);
    }

    showNavigation(navNodeId, pageNumAttr, pageCount, numActive) {
        let navigation = document.querySelector(`#${navNodeId}`);
        navigation.innerHTML = "";
        let ul = document.createElement('ul');
        ul.setAttribute('name', 'pages');
        if (pageNumAttr != null && pageNumAttr.ulAttributes != null && pageNumAttr.ulAttributes.length > 0) {
            for (let attribute of pageNumAttr.ulAttributes) {
                ul.setAttribute(attribute.name, attribute.value);
            }

        }
        for (let num = 1; num <= pageCount; num++) {
            let a = document.createElement('a');
            a.textContent = num;
            a.setAttribute('onclick', `showPage(event, ${num})`);
            if (pageNumAttr != null && pageNumAttr.aAttributes != null && pageNumAttr.aAttributes.length > 0) {
                for (let attribute of pageNumAttr.aAttributes) {
                    a.setAttribute(attribute.name, attribute.value);
                }
            }
            if (num == numActive) {
                a.classList.add('active')
            }
            ul.appendChild(a);
        }
        navigation.innerHTML = "";
        navigation.appendChild(ul);
    }

    async getPageDto(token, URL, page, items) {
        let pagination = {};
        URL += "?page=" + page;
        if (items != null) {
            URL += "&items=" + items;
        }
        await fetch(URL, {
                method: 'GET',
                headers: {
                    "Content-type": "application/json",
                    "Authorization": `${token}`,
                }
            })
            .then(data => data.json())
            .then(ob => {
                pagination.totalPageCount = ob.totalPageCount;
                pagination.items = ob.items;
            })
            .catch(mess => {
                console.log(mess);
            })
        return pagination;
    }

    getCookie(name) {
        let matches = document.cookie.match(new RegExp(
            "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
        ));
        return matches ? ('Bearer ' + decodeURIComponent(matches[1])) : undefined;
    }
}