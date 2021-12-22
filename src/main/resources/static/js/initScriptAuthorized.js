const side_bar_width = 120

loadCss()
loadScripts()
start()

function loadCss() {
    const head = document.querySelector("head")

    const heading = document.createElement("link")
    heading.rel = "stylesheet"
    heading.href = "/css/headingCss.css"
    heading.type = "text/css"

    const footer = document.createElement("link")
    footer.rel = "stylesheet"
    footer.href = "/css/footerCss.css"
    footer.type = "text/css"

    const sideBar = document.createElement("link")
    sideBar.rel = "stylesheet"
    sideBar.href = "/css/side_barCss.css"
    sideBar.type = "text/css"

    const util = document.createElement("link")
    util.rel = "stylesheet"
    util.href = "/css/utilCss.css"
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
            <div class="col-1" style="height: 50px">
                <div class="colcol">
                    <svg aria-hidden="true"  width="25" height="25" viewBox="0 0 512.000000 512.000000" preserveAspectRatio="xMidYMid meet">
                        <g transform="translate(0.000000,512.000000) scale(0.100000,-0.100000)" stroke="none">
                            <path d="M2492 5093 c-24 -12 -67 -64 -150 -180 l-117 -163 -190 -61 c-113 -36 -199 -70 -213 -82 -53 -49 -68 -126 -34 -182 10 -16 67 -95 126 -175 l108 -145 -6 -180 c-7 -209 0 -248 47 -291 60 -56 88 -54 306 17 l191 63 194 -63 c218 -72 242 -73 302 -18 48 44 55 82 48 292 l-6 180 108 145 c59 80 116 159 126 175 34 56 19 133 -34 182 -14 12 -100 46 -213 82 l-190 61 -114 160 c-66 92 -128 169 -146 180 -39 24 -98 25 -143 3z m124 -460 c89 -128 99 -136 248 -183 67 -22 94 -35 89 -43 -4 -7 -30 -43 -58 -82 -83 -112 -85 -118 -85 -255 0 -66 -4 -120 -8 -120 -5 0 -48 14 -98 31 -116 40 -172 40 -288 0 -50 -17 -93 -31 -98 -31 -4 0 -8 54 -8 120 0 137 -2 142 -84 254 -29 39 -56 76 -59 83 -5 8 22 21 89 43 149 47 159 55 248 183 26 37 51 67 56 67 5 0 30 -30 56 -67z"/>
                            <path d="M719 4647 c-19 -12 -86 -94 -149 -182 -131 -180 -84 -150 -358 -236 -148 -46 -186 -70 -203 -129 -20 -67 -6 -98 121 -270 l120 -161 0 -207 c0 -188 2 -210 20 -239 25 -41 59 -63 108 -70 31 -4 79 8 226 58 l187 63 187 -62 c103 -34 200 -62 215 -62 50 0 107 37 128 83 16 37 19 67 19 243 l0 201 67 89 c164 217 173 232 173 286 0 53 -18 93 -54 120 -12 9 -107 43 -210 75 l-187 60 -117 163 c-124 173 -154 200 -223 200 -21 0 -50 -10 -70 -23z m214 -565 c25 -26 55 -40 144 -68 62 -19 113 -38 113 -41 0 -3 -29 -45 -64 -92 -36 -48 -70 -99 -75 -115 -6 -15 -11 -81 -11 -146 l0 -118 -120 39 c-135 44 -133 44 -292 -10 l-88 -30 0 130 c0 80 -4 139 -12 152 -6 12 -40 60 -75 107 l-64 85 109 36 c59 20 118 41 130 48 12 6 53 56 93 111 l71 100 55 -77 c30 -43 69 -93 86 -111z"/>
                            <path d="M4260 4651 c-25 -13 -70 -67 -152 -181 l-117 -163 -187 -60 c-103 -32 -198 -66 -210 -75 -36 -27 -54 -67 -54 -120 0 -54 9 -69 173 -286 l67 -89 0 -198 c0 -221 8 -262 62 -300 56 -40 89 -37 296 32 l191 63 183 -62 c101 -34 199 -62 218 -62 44 0 94 30 120 72 18 30 20 51 20 240 l0 207 120 162 c127 172 141 203 121 269 -17 59 -55 83 -203 129 -274 86 -227 56 -358 236 -63 88 -130 170 -149 182 -41 28 -94 30 -141 4z m139 -481 c40 -55 81 -105 93 -111 12 -7 71 -28 130 -48 l109 -36 -64 -85 c-35 -47 -69 -95 -75 -107 -8 -13 -12 -72 -12 -152 l0 -130 -87 30 c-160 54 -158 54 -293 10 l-120 -39 0 112 c0 61 -4 126 -9 144 -6 18 -39 73 -75 121 -37 49 -66 91 -66 94 0 3 51 22 114 42 127 39 143 52 236 188 25 37 46 67 47 67 0 0 33 -45 72 -100z"/>
                            <path d="M2470 3339 c-391 -36 -717 -320 -816 -709 -29 -116 -27 -384 4 -499 43 -159 114 -286 230 -412 l61 -66 -41 -12 c-198 -60 -447 -218 -617 -391 -271 -278 -421 -620 -446 -1020 -7 -120 5 -156 65 -197 l33 -23 1617 0 1617 0 33 23 c60 41 72 77 65 197 -25 400 -175 742 -446 1020 -170 173 -419 331 -617 391 l-41 12 61 66 c116 126 187 253 230 412 31 115 33 383 4 499 -69 271 -251 497 -503 624 -71 35 -210 74 -291 81 -37 3 -78 7 -92 9 -14 1 -63 -1 -110 -5z m225 -304 c91 -19 218 -83 288 -146 76 -68 150 -182 184 -282 24 -73 27 -97 27 -217 0 -153 -12 -208 -72 -325 -46 -89 -162 -208 -252 -258 -188 -104 -432 -104 -620 0 -90 50 -206 169 -252 258 -60 117 -72 172 -72 325 0 120 3 144 27 217 54 162 170 296 321 373 133 67 275 85 421 55z m200 -1620 c472 -76 863 -399 1014 -836 29 -83 61 -219 61 -259 l0 -20 -1410 0 -1410 0 0 20 c0 39 32 175 59 255 63 180 162 336 306 480 195 195 425 313 700 359 115 20 562 20 680 1z"/>
                        </g>
                    </svg>
                </div>
            </div>
            <div class="col-2" style="height: 50px; padding: 8px">
                
                    <form action="https://google.com" target="_blank">
                        <button class="mbutton">Log out</button>
                    </form>
           
            </div>
        </div>
        `
    heading.classList.add("container-fluid")
    console.log("createHeading", heading)
    console.log("createHeight", heading.clientHeight)
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




