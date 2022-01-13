class Pagination {

    constructor(pagination_url, items, objectNodeId, navNodeId, display, pageNumAttr) {
        this.pagination_url = pagination_url;       //url
        this.items = items;                         //количество объектов
        this.objectNodeId = objectNodeId;           //id div куда будут вставляться массив объектов
        this.navNodeId = navNodeId;                 //id div куда будет вставляться нумерация
        this.display = display;                     //функция, которая задаёт - как будут вставляться объекты
    }

    async showPage(event, num) {
        if (event != null) {
            event.preventDefault();
        }
        await this.getPageDto(this.getCookie('token'), this.pagination_url, num, this.items)
            .then(pageDto => {
                this.showNavigation(this.navNodeId, pageDto.totalPageCount, num);
                this.showObjects(this.objectNodeId, pageDto.items);
            })
    }

    showObjects(objectNodeId, arrayObjects) {
        let table = document.querySelector(`#${objectNodeId}`);
        table.innerHTML = "";
        let node = this.display(arrayObjects);
        table.appendChild(node);
    }

    showNavigation(navNodeId, pageCount, currPage) {
        let navigation = document.querySelector(`#${navNodeId}`);
        currPage = parseInt(currPage);
        let ul = document.createElement('ul');
        ul.setAttribute('class', 'page');

        let firstNum = 1;
        let prevNum = currPage - 1;
        let currentNum = currPage;
        let nextNum = currPage + 1;
        let lastNum = pageCount;

        if (firstNum < prevNum) ul.appendChild(this.nodeReturn(firstNum, true, false));
        if (prevNum > firstNum + 1 && pageCount > 4) ul.appendChild(this.nodeReturn('...', false, false));
        if (currentNum === lastNum && pageCount >= 4) ul.appendChild(this.nodeReturn(lastNum - 2, true, false));
        if (prevNum > 0) ul.appendChild(this.nodeReturn(prevNum, true, false));
        ul.appendChild(this.nodeReturn(currPage, false, true));
        if (nextNum < lastNum + 1) ul.appendChild(this.nodeReturn(nextNum, true, false));
        if (currentNum === firstNum && pageCount >= 4) ul.appendChild(this.nodeReturn(firstNum + 2, true, false));
        if (nextNum < lastNum - 1 && pageCount > 4) ul.appendChild(this.nodeReturn('...', false, false));
        if (lastNum > nextNum) ul.appendChild(this.nodeReturn(lastNum, true, false));

        navigation.innerHTML = "";
        navigation.appendChild(ul);
    }

    nodeReturn(text, isLink, isCurrent) {
        let elem = document.createElement('a')
        if (isLink) {
            elem.setAttribute('href', '#');
            elem.setAttribute('onclick', `showPage(event, text)`);
            elem.style.cssText = 'border-width: 1px;color: black;'
        }
        if (isCurrent) {
            elem.style.cssText = 'border-width: 1px;border-color: 1px;background-color: #f08741; color: white;'
            elem.setAttribute('href', '#');
        }
        elem.textContent = text;
        return elem;
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

function addStyle() {
    let style = document.createElement('style');
    style.innerHTML = ".page a {" +
        "text-decoration: none;" +
        "border-radius: 4px;" +
        "border-color: grey;" +
        "border-style: solid;" +
        "border-width: 0px;" +
        //"color: black;" +
        "width: 25px;" +
        "height: 25px;" +
        "text-align: center;" +
        "display: inline-block;" +
        "margin-right: 15px;" +
        "font-family: system-ui;" +
        "}"
    console.log(style);
    document.head.appendChild(style);
}

window.onload = addStyle();