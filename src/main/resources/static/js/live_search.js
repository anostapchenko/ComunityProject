const userFetchService = {
    head: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token[1]
    },
    getTagsName: async (value) => await fetch(`http://localhost:8091/api/user/tag/latter?value=${value}`,{method: 'GET',headers: userFetchService.head}),
}

const dataList = document.querySelector("datalist[id=tagsTextInputDataList]")
const tags = document.querySelector("input[id=tagsTextInput]");

document.querySelector('#tagsTextInput').oninput = async function () {

    let val = this.value.trim();
    if(val != '') {
        if (String(val).match(/[^;|,|\s|a-zA-z0-9]/)) {
            tags.insertAdjacentHTML("beforebegin", "<div id='tagsChild'><span style='color: red'><b>Разделяйте теги только пробелом запятой или точкой с заяптой</b></span></div>")
        } else {
            if(document.getElementById("tagsChild") != null) {
                var parent = document.getElementById("tags");
                var child = document.getElementById("tagsChild");
                parent.removeChild(child);
            }
            let elasticElement = new Array;
            $('datalist').empty();
            await userFetchService.getTagsName(val)
                .then(res => res.json())
                .then(tags => {
                    tags.forEach(tag => {
                        elasticElement.push(tag.name);
                    })
                })
            elasticElement.forEach(elem => {
                dataList.insertAdjacentHTML("afterbegin", "<option>" + elem + "</option>");
            });
        }
    }
}

