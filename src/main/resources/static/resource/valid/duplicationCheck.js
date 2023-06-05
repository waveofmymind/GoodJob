let checkedUsername;
let checkedNickname;
let checkedEmail;

// 중복확인 버튼 클릭 시 실행되는 함수
function checkDuplication(event) {
    const fieldName = event.target.previousElementSibling.name;

    if (fieldName === "username") {
        checkDuplicateUsername();
    } else if (fieldName === "nickname") {
        checkDuplicateNickname();
    } else if (fieldName === "email") {
        checkDuplicateEmail();
    }

    event.preventDefault(); // 폼 제출 동작 막기
}

function checkDuplicateUsername() {
    const usernameInput = document.getElementById("username");
    const username = usernameInput.value.trim();
    const usernameMessage = document.getElementById("usernameMessage");

    const formData = new FormData();
    formData.append("username", username);

    fetch('/member/join/valid/username', {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') { // 중복인 경우
                usernameMessage.innerText = data.msg;
                usernameMessage.classList.add("text-red-500");
                usernameMessage.classList.remove("text-green-500");
            } else { // 중복 아닌 경우
                usernameMessage.innerText = data.msg;
                usernameMessage.classList.add("text-green-500");
                usernameMessage.classList.remove("text-red-500");
                checkedUsername = username;
            }

            usernameMessage.style.display = "block";
        });
}

function checkDuplicateNickname() {
    const nicknameInput = document.getElementById("nickname");
    const nickname = nicknameInput.value.trim();
    const nicknameMessage = document.getElementById("nicknameMessage");

    const formData = new FormData();
    formData.append("nickname", nickname);

    fetch('/member/join/valid/nickname', {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') {
                nicknameMessage.innerText = data.msg;
                nicknameMessage.classList.add("text-red-500");
                nicknameMessage.classList.remove("text-green-500"); // 중복 아닌 경우를 위해 기존 클래스 제거
            } else {
                nicknameMessage.innerText = data.msg;
                nicknameMessage.classList.add("text-green-500");
                nicknameMessage.classList.remove("text-red-500"); // 중복인 경우를 위해 기존 클래스 제거
                checkedNickname = nickname;
            }

            nicknameMessage.style.display = "block";
        });
}

function checkDuplicateEmail() {
    const emailInput = document.getElementById("email");
    const email = emailInput.value.trim();
    const emailMessage = document.getElementById("emailMessage");

    const formData = new FormData();
    formData.append("email", email);

    fetch('/member/join/valid/email', {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') {
                emailMessage.innerText = data.msg;
                emailMessage.classList.add("text-red-500");
                emailMessage.classList.remove("text-green-500"); // 중복 아닌 경우를 위해 기존 클래스 제거
            } else {
                emailMessage.innerText = data.msg;
                emailMessage.classList.add("text-green-500");
                emailMessage.classList.remove("text-red-500"); // 중복인 경우를 위해 기존 클래스 제거
                checkedEmail = email;
            }

            emailMessage.style.display = "block";
        });
}