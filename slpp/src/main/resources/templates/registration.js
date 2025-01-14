document.getElementById('registerButton').addEventListener('click', function () {
    const email = document.getElementById('email').value;
    const fullName = document.getElementById('fullName').value;
    const dob = document.getElementById('dob').value;
    const password = document.getElementById('password').value;
    const bioId = document.getElementById('bioId').value;

    // Validate BioID (simple validation: must be 10 characters)
    if (bioId.length !== 10) {
        alert('BioID must be a 10-digit code.');
        return;
    }

    // Example request (backend integration required)
    const requestData = {
        email: email,
        fullName: fullName,
        dob: dob,
        password: password,
        bioId: bioId
    };

    // Mock API call
    console.log('Registering user:', requestData);
    alert('Registration successful! Please proceed to login.');
});

// Initialize the QR code scanner
let html5QrCode;

document.getElementById('scanBioIdButton').addEventListener('click', function () {
    const qrScannerElement = document.getElementById('Qrscanner');
    if (!html5QrCode) {
        html5QrCode = new Html5Qrcode("scanBioIdButton");
    }

    if (qrScannerElement.style.display === "none") {
        qrScannerElement.style.display = "block";

        html5QrCode.start(
            { facingMode: "environment" }, // Use rear camera
            { fps: 10, qrbox: { width: 250, height: 250 } },
            (decodedText) => {
                // On successful scan
                document.getElementById('bioId').value = decodedText;
                alert(`QR Code scanned: ${decodedText}`);
                html5QrCode.stop();
                qrScannerElement.style.display = "none";
            },
            (errorMessage) => {
                // On error (optional)
                console.log(`QR Code scanning error: ${errorMessage}`);
            }
        ).catch((err) => {
            console.error(`Unable to start scanning: ${err}`);
        });
    } else {
        // Stop the scanner if it's already active
        html5QrCode.stop().then(() => {
            qrScannerElement.style.display = "none";
        }).catch((err) => {
            console.error(`Error stopping the scanner: ${err}`);
        });
    }
});
