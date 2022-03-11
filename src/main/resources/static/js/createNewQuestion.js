const button = document.getElementById('addButton');
const titles = document.querySelector("input[id=headTextInput]");

function addTag(open, close) {
    var control = $('#mainTextInput')[0];
    var start = control.selectionStart;
    var end = control.selectionEnd;
    if (start != end) {
        var text = $(control).val();
        $(control).text('')
        $(control).val(text.substring(0, start) + open + text.substring(start, end) + close + text.substring(end));
        $(control).focus();
        var sel = end + (open + close).length;
        control.setSelectionRange(sel, sel);

    }
    return false;
}

$('#button-b').click(function(){
    return addTag('<b>', '</b>');
});

$('#button-k').click(function(){
    return addTag('<i>', '</i>');
});

$('#button-c').click(function(){
    return addTag('<code>', '</code>');
});

button.onclick = function() {
    let title = document.getElementById('headTextInput').value;
    if(title.trim() == ''){
        titles.insertAdjacentHTML("beforebegin", "<div><span style='color: red'><b>Придумайте заголовок</b></span></div>")
    } else {
        let description = document.getElementById('mainTextInput').value;
        let tags = document.getElementById('tagsTextInput').value.split(/[\s|,|;]+/);
        const tagsList = new Array;
        tags.forEach(tag => {
            let tagData = {
                name:tag
            }
            tagsList.push(tagData)
        })
        console.log(tagsList)
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
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token[1]
        },
        body: JSON.stringify(questionCreateDto)
    })
}

