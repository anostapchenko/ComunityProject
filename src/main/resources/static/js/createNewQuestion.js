const button = document.getElementById('addButton')
const titles = document.querySelector("input[id=headTextInput]")

button.onclick = function() {
    let title = document.getElementById('headTextInput').value;
    if(title.trim() == ''){
        titles.insertAdjacentHTML("beforebegin", "<div><span style='color: red'><b>Придумайте заголовок</b></span></div>")
    } else {
        let description = document.getElementById('mainTextInput').value;
        let tags = document.getElementById('tagsTextInput').value;
        let tag = {
            name: "tag7",
            description: "Description of tag 7"
        }
        const tagsList = new Array(tag);

        let data = {
            title: title,
            description: description,
            tags: tagsList
        }
        createNewQuestion(data);
    }
}

function createNewQuestion(questionCreateDto){
    fetch('http://localhost:8091/api/user/question',{
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Referer': null
        },
        body: JSON.stringify(questionCreateDto)
    })
}

