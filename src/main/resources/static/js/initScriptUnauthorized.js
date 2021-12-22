const side_bar_width = 120

loadCss()
loadScripts()
start()

function loadCss() {
    const head = document.querySelector("head")

    const heading = document.createElement("link")
    heading.rel = "stylesheet"
    heading.href = "/css/header.css"
    heading.type = "text/css"

    const footer = document.createElement("link")
    footer.rel = "stylesheet"
    footer.href = "/css/footer.css"
    footer.type = "text/css"

    const sideBar = document.createElement("link")
    sideBar.rel = "stylesheet"
    sideBar.href = "/css/sidebar.css"
    sideBar.type = "text/css"

    const util = document.createElement("link")
    util.rel = "stylesheet"
    util.href = "/css/util.css"
    util.type = "text/css"

    const bootstrap = document.createElement("link")
    bootstrap.rel = "stylesheet"
    bootstrap.href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
    bootstrap.type = "text/css"
    bootstrap.integrity = "sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
    bootstrap.crossOrigin = "anonymous"

    head.appendChild(heading)
    head.appendChild(footer)
    head.appendChild(sideBar)
    head.appendChild(util)
    head.appendChild(bootstrap)
}

function loadScripts() {
    const head = document.querySelector("body")

    const heading = document.createElement("script")
    heading.src = "/js/headingJS.js"
    heading.type = "text/javascript"

    const footer = document.createElement("script")
    footer.src = "/js/footerJS.js"
    footer.type = "text/javascript"

    const sideBar = document.createElement("script")
    sideBar.src = "/js/side_barJS.js"
    sideBar.type = "text/javascript"

    head.appendChild(heading)
    head.appendChild(footer)
    head.appendChild(sideBar)
}

function start() {
    const body = document.querySelector("body")
    const mainDiv = body.querySelector(".main")
    const heading = createHeading()
    const footer = createFooter()

    if (mainDiv !== null) {
        body.insertBefore(heading, mainDiv)
        mainDiv.classList.add("main")
        const mainDivHeight = Math.max(mainDiv.clientHeight, mainDiv.scrollHeight, mainDiv.offsetHeight)
        const headingWidth = Math.max(heading.clientWidth,heading.scrollWidth,heading.offsetWidth) - 1
        if (mainDivHeight < document.documentElement.clientHeight){
            const headingHeight = Math.max(heading.clientHeight)
            body.insertBefore(createSideBar(document.documentElement.clientHeight - headingHeight), mainDiv)
        } else {
            body.insertBefore(createSideBar(mainDivHeight), mainDiv)
        }
        mainDiv.style.width = headingWidth - side_bar_width + 'px'
        body.appendChild(footer)
    } else {
        body.appendChild(heading)
        const headingHeight = Math.max(heading.clientHeight,heading.scrollHeight,heading.offsetHeight)
        body.appendChild(createSideBar(document.documentElement.clientHeight - headingHeight))
        body.appendChild(footer)
    }
}

function createFooter() {
    const footer = document.createElement("div")
    footer.innerHTML =
        `
        <div class="block">
            <div class="title">
                BLOCK1
            </div>
            <div class="mdiv">
                item1
            </div>
            <div class="mdiv">
                item2
            </div>
            <div class="mdiv">
                item3
            </div>
        </div>
        <div class="block">
            <div class="title">
                BLOCK2
            </div>
            <div class="mdiv">
                item1
            </div>
            <div class="mdiv">
                item2
            </div>
            <div class="mdiv">
                item3
            </div>
        </div>
        <div class="block">
            <div class="title">
                BLOCK3
            </div>
            <div class="mdiv">
                item1
            </div>
            <div class="mdiv">
                item2
            </div>
            <div class="mdiv">
                item3
            </div>
        </div>
        <div class="block">
            <div class="title">
                CONTACT US
            </div>
            <div class="mdiv">
                item1
            </div>
            <div class="mdiv">
                item2
            </div>
            <div class="mdiv">
                item3
            </div>
        </div>
    `
    footer.classList.add("main_block")
    return footer
}

function createHeading() {
    const heading = document.createElement("div")
    heading.innerHTML =
        `
            <div class="row p-0" style="background-color: #2e2e35; height: 50px">
            <div class="col-1 p-0 pl-3 m-0" style="height: 50px">
                <svg id="logo_svg__Layer_1"
                     width="100px"
                     height="50px"
                     xmlns="http://www.w3.org/2000/svg"
                     x="0" y="0" viewBox="0 0 1000 375"
                     xml:space="preserve">
                    <path class="logo"
                          d="M32.84 197.93L21.6 210.02v29.96H0V135.12h21.6v47.53l9.51-13.03 26.72-34.5h26.58l-37.24 46.6 38.31 58.26H59.77zM373.57 152.63h-32.11v87.35h-21.61v-87.35h-31.69v-17.51h85.41zM425.97 91.74l14.83-14.83-29.66-14.74 14.83 29.57zM454 83.5l-21.43 21.43 8.15 16.3-13 13-37.24-81.4 11.33-11.33 81.52 37.12-12.99 13L454 83.5zM251.04 312.77l-14.74-29.66-14.83 14.83 29.57 14.83zm-29.46-59.19l12.99-12.99 37.12 81.52-11.33 11.33-81.4-37.24 13-13 16.3 8.15 21.43-21.43-8.11-16.34zM338.95 48.23V32.5l-23.55 7.91 23.55 7.82zM349.45 29v22.73l12.96 4.32v13.79L299.5 46.41V34.4l62.92-23.55v13.78L349.45 29zM241.77 74.75l-7.42-7.42-7.37 14.83 14.79-7.41zm-4.12-14.02l10.71 10.71 8.15-4.08 6.5 6.5-40.7 18.62-5.67-5.67 18.56-40.76 6.5 6.5-4.05 8.18zM463.51 200.87h26.22l-13.18-39.24-13.04 39.24zm32.06 17.5h-37.88l-7.2 21.61h-22.98l39.03-104.86h20.03l39.25 104.86h-22.97l-7.28-21.61zM198.38 174.21h-26.22l13.18 39.24 13.04-39.24zm-32.05-17.5h37.88l7.2-21.61h22.98l-39.03 104.86h-20.03L136.09 135.1h22.97l7.27 21.61zM322.94 326.77v15.73l23.55-7.91-23.55-7.82zM312.45 346v-22.73l-12.97-4.32v-13.79l62.92 23.42v12.02l-62.92 23.55v-13.78l12.97-4.37zM419.73 299.99l7.42 7.42 7.37-14.83-14.79 7.41zm4.12 14.01l-10.71-10.71-8.15 4.08-6.5-6.5 40.7-18.62 5.66 5.66-18.56 40.76-6.5-6.5 4.06-8.17z"></path>
                </svg>
            </div>
            <div class="col-4 ml-0 mr-0" style="height: 50px">
                <div style="display:flex;justify-content:center;align-items:center;height:100%;width: 100%">
                    <input type="text" class="form-control" placeholder="Search..."
                           style="height: 35px;margin: 0 auto"></div>
            </div>
            <div class="col-1" style="height: 50px">
                <div class="colcol">
                    <svg aria-hidden="true" width="25" height="25" viewBox="0 0 20 18">
                        <path d="M4.63 1h10.56a2 2 0 011.94 1.35L20 10.79V15a2 2 0 01-2 2H2a2 2 0 01-2-2v-4.21l2.78-8.44c.25-.8 1-1.36 1.85-1.35Zm8.28 12 2-2h2.95l-2.44-7.32a1 1 0 00-.95-.68H5.35a1 1 0 00-.95.68L1.96 11h2.95l2 2h6Z"></path>
                    </svg>
                </div>
            </div>
            <div class="col-1" style="height: 50px">
                <div class="colcol">
                    <svg aria-hidden="true" width="25" height="25" viewBox="0 0 18 18">
                        <path d="M15 2V1H3v1H0v4c0 1.6 1.4 3 3 3v1c.4 1.5 3 2.6 5 3v2H5s-1 1.5-1 2h10c0-.4-1-2-1-2h-3v-2c2-.4 4.6-1.5 5-3V9c1.6-.2 3-1.4 3-3V2h-3ZM3 7c-.5 0-1-.5-1-1V4h1v3Zm8.4 2.5L9 8 6.6 9.4l1-2.7L5 5h3l1-2.7L10 5h2.8l-2.3 1.8 1 2.7h-.1ZM16 6c0 .5-.5 1-1 1V4h1v2Z"></path>
                    </svg>
                </div>
            </div>
            <div class="col-1" style="height: 50px">
                <div class="colcol">
                    <svg aria-hidden="true" width="25" height="25" viewBox="0 0 18 18">
                        <path d="M9 1C4.64 1 1 4.64 1 9c0 4.36 3.64 8 8 8 4.36 0 8-3.64 8-8 0-4.36-3.64-8-8-8Zm.81 12.13c-.02.71-.55 1.15-1.24 1.13-.66-.02-1.17-.49-1.15-1.2.02-.72.56-1.18 1.22-1.16.7.03 1.2.51 1.17 1.23ZM11.77 8c-.59.66-1.78 1.09-2.05 1.97a4 4 0 00-.09.75c0 .05-.03.16-.18.16H7.88c-.16 0-.18-.1-.18-.15.06-1.35.66-2.2 1.83-2.88.39-.29.7-.75.7-1.24.01-1.24-1.64-1.82-2.35-.72-.21.33-.18.73-.18 1.1H5.75c0-1.97 1.03-3.26 3.03-3.26 1.75 0 3.47.87 3.47 2.83 0 .57-.2 1.05-.48 1.44Z"></path>
                    </svg>
                </div>
            </div>
            <div class="col-1" style="height: 50px">
                <div class="colcol">
                    <svg aria-hidden="true" width="25" height="25" viewBox="0 0 18 18">
                        <path d="M15 1H3a2 2 0 00-2 2v2h16V3a2 2 0 00-2-2ZM1 13c0 1.1.9 2 2 2h8v3l3-3h1a2 2 0 002-2v-2H1v2Zm16-7H1v4h16V6Z"></path>
                    </svg>
                </div>
            </div>
            <div class="col-3" style="height: 50px">
                <div class="mform">
                    <form action="https://google.com" target="_blank">
                        <button class="mbutton">Log in</button>
                    </form>
                </div>
                <div class="mform">
                    <form action="https://google.com" target="_blank">
                        <button class="mbutton">Registration</button>
                    </form>
                </div>
            </div>
        </div>
        `
    heading.classList.add("container-fluid")
    return heading
}

function createSideBar(height) {
    const sideBar = document.createElement("div")
    sideBar.innerHTML =
        `
        <div class="mfield active" id="questions">
            Вопросы
        </div>
        <div class="mfield" id="tags">
            Метки
        </div>
        <div class="mfield" id="members">
            Участники
        </div>
        <div class="mfield" id="unanswered">
            Неотвеченные
        </div>
        `
    sideBar.classList.add("mbody")
    sideBar.classList.add("side_bar")
    sideBar.style.height = height + 'px'
    return sideBar
}




