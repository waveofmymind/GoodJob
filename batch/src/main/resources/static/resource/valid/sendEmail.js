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