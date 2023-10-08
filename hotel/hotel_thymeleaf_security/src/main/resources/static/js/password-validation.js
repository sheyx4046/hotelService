function validatePassword() {
    var password = document.getElementById("password").value;
    var passwordValidation = document.getElementById("password-validation");
    var submitButton = document.querySelector('button[type="submit"]');
    var regex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

    if (regex.test(password)) {
        passwordValidation.textContent = ""; // Clear any existing error message
        submitButton.removeAttribute("disabled"); // Enable the submit button
    } else {
        passwordValidation.textContent = "Password must contain at least 1 uppercase letter, 1 lowercase letter, and be at least 8 characters long.";
        submitButton.setAttribute("disabled", "disabled"); // Disable the submit button
    }
}

var passwordInput = document.getElementById("password");
passwordInput.addEventListener("input", validatePassword);

var submitButton = document.querySelector('button[type="submit"]');
submitButton.setAttribute("disabled", "disabled");
