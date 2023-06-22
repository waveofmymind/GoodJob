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