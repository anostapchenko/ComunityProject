
fetch("http://localhost:8091/api/auth/check")
    .then(response => {
        if (!response.ok){
            window.location.assign("http://localhost:8091/login");
        }
    });
