class Pagination {

    constructor(pagination_url, items, objectNodeId, navNodeId, display, pageNumAttr) {
        this.pagination_url = pagination_url;
        this.items = items;
        this.objectNodeId = objectNodeId;
        this.navNodeId = navNodeId;
        this.display = display;
        this.pageNumAttr = pageNumAttr;
    }

    async showPage(event, num, token) {
        if (event != null) {
            event.preventDefault();
        }
        await this.getPageDto(token, this.pagination_url, num, this.items)
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
            let li = document.createElement('a');
            li.textContent = num;
            li.setAttribute('onclick', `showPage(event, ${num})`);
            if (pageNumAttr != null && pageNumAttr.liAttributes != null && pageNumAttr.liAttributes.length > 0) {
                for (let attribute of pageNumAttr.liAttributes) {
                    li.setAttribute(attribute.name, attribute.value);
                }
            }
            if (num == numActive) {
                li.classList.add('active')
            }
            ul.appendChild(li);
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
                console.log(pagination);
                pagination.totalPageCount = ob.totalPageCount;
                pagination.items = ob.items;
            })
            .catch(mess => {
                console.log(mess);
            })
        return pagination;
    }
}