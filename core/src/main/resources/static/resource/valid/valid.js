let isNoDuplication = false;

// 가입하기 버튼클릭 시 실행
function submitForm() {
    // 중복이 아닌 경우 check
    let usernameField = document.getElementById("usernameMessage");
    let nicknameField = document.getElementById("nicknameMessage");
    let verificationCodeField = document.getElementById("verificationCodeMessage");

    if (usernameField.classList.contains("text-green-500") &&
        nicknameField.classList.contains("text-green-500") &&
        verificationCodeField.classList.contains("text-green-500")) {
        isNoDuplication = true;
    }

    if (!isNoDuplication) {
        toastWarning("모든 인증을 마쳐주세요!");
        event.preventDefault();
    }
}

// 중복확인 버튼 클릭 시 실행되는 함수
function checkDuplication(event) {
    const fieldName = event.target.previousElementSibling.name;

    checkDuplicateField(fieldName);

    event.preventDefault(); // 폼 제출 동작 막기
}

function checkDuplicateField(fieldName) {
    const url = '/member/join/valid/' + fieldName;
    const fieldInput = document.getElementById(fieldName);
    const fieldValue = fieldInput.value.trim();
    const fieldMessage = document.getElementById(fieldName + "Message");

    const formData = new FormData();
    formData.append(fieldName, fieldValue);

    fetch(url, {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') { // 중복인 경우
                fieldMessage.innerText = data.msg;
                fieldMessage.classList.add("text-red-500");
                fieldMessage.classList.remove("text-green-500");
            } else { // 중복 아닌 경우
                fieldMessage.innerText = data.msg;
                fieldMessage.classList.add("text-green-500");
                fieldMessage.classList.remove("text-red-500");
            }

            fieldMessage.style.display = "block";
        });
}

function sendEmail(event) {
    const emailInput = document.getElementById("email");
    const emailValue = emailInput.value.trim();
    const emailMessage = document.getElementById("emailMessage");
    const verificationCodeBlock = document.getElementById("verificationCodeBlock")

    const formData = new FormData();
    formData.append("email", emailValue);

    fetch("/email/send", {
        method: 'post',
        body: formData
    })

    emailMessage.innerText = "이메일 전송이 완료되었습니다.";
    emailMessage.classList.add("text-green-500");
    emailMessage.classList.remove("text-red-500");
    emailMessage.style.display = "block";
    verificationCodeBlock.style.display = "block";

    event.preventDefault();
}

function verifyCode(event) {
    const verificationCodeInput = document.getElementById("verificationCode")
    const verificationCodeValue = verificationCodeInput.value.trim();
    const verificationCodeMessage = document.getElementById("verificationCodeMessage");

    const formData = new FormData();
    formData.append("verificationCode", verificationCodeValue);

    fetch("/email/verify", {
        method: 'post',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.resultCode !== 'S-1') { // 인증실패인 경우
                verificationCodeMessage.innerText = data.msg;
                verificationCodeMessage.classList.add("text-red-500");
                verificationCodeMessage.classList.remove("text-green-500");
            } else { // 인증성공인 경우
                verificationCodeMessage.innerText = data.msg;
                verificationCodeMessage.classList.add("text-green-500");
                verificationCodeMessage.classList.remove("text-red-500");
            }

            verificationCodeMessage.style.display = "block";
        });

    event.preventDefault();
}