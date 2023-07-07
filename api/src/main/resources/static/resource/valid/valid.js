let isNoDuplication = false;
let countdown; // 타이머 인터벌 ID

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
    const fieldValue = document.getElementById(fieldName).value.trim();
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
    // 이전 타이머 중지
    clearInterval(countdown);

    const emailValue = document.getElementById("email").value.trim();
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

    // 타이머 시작
    startTimer();

    event.preventDefault();
}

function verifyCode(event) {
    const verificationCodeValue = document.getElementById("verificationCode").value.trim();
    const verificationCodeMessage = document.getElementById("verificationCodeMessage");
    const verificationCodeBlock = document.getElementById("verificationCodeBlock")

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

                clearInterval(countdown); // 타이머 멈춤
                verificationCodeBlock.style.display = "none"; // 블록 숨김
            }

            verificationCodeMessage.style.display = "block";
        });

    event.preventDefault();
}

function startTimer() {
    var minutes = 5; // 타이머 시간 (분)
    var seconds = minutes * 60; // 초 단위로 변환
    const timerMessage = document.getElementById('timer');

    countdown = setInterval(function() {
        var minutesRemaining = Math.floor(seconds / 60);
        var secondsRemaining = seconds % 60;

        // 타이머를 표시하는 HTML 엘리먼트의 ID를 지정하고 해당 엘리먼트에 시간을 표시
        timerMessage.innerHTML = minutesRemaining + '분 ' + secondsRemaining + '초 남음';

        seconds--;``

        // 타이머 종료 시
        if (seconds < 0) {
            clearInterval(countdown);

            timerMessage.innerText = "인증 시간이 초과되었습니다. 인증번호를 다시 발급해주세요."
            timerMessage.classList.add("text-red-500");
            timerMessage.classList.remove("text-green-500");
        }
    }, 1000);
}