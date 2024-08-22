<%@ page language="java" contentType="text/html"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lupa Password Helpdesk</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background-color: #fff;
            padding: 20px 40px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }
        .container .row {
            margin-bottom: 15px;
        }
        .container label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .container input[type="text"],
        .container input[type="password"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .container button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            border: none;
            border-radius: 4px;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }
        .container button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <form >
        <div class="row">
            <label for="nip">Nip:</label>
            <input type="text" readonly id="nip" value="${nipValue}" name="nip">
        </div>
        <div class="row">
            <label for="pass">Masukan Password Baru Anda:</label>
            <input type="password" id="pass" name="password">
        </div>
        <div class="row">
            <label for="passConfirm">Masukan Kembali Password Baru Anda:</label>
            <input type="password" id="passConfirm" name="passwordConfrim">
        </div>
        <div class="row">
            <button class="btn">Submit</button>
        </div>
    </form>
</div>
</body>
<script>
    document.querySelector('form').addEventListener('submit', function(event) {
        event.preventDefault();

        var password = document.getElementById('pass').value;
        var passwordConfirm = document.getElementById('passConfirm').value;

        if (password !== passwordConfirm) {
            alert('Passwords do not match!');
            return;
        }
        if(password.length < 8){
            alert('Passwords must be at least 8 characters!');
            return;
        }

        fetch('forgotpasssubmit', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nip: document.getElementById('nip').value,
                password: password
            })
        }).then(response => {
            if (response.ok) {
                alert('Password reset successfully!');
            } else {
                alert('Password reset failed!');
            }
        }).catch(error => {
            console.error('Error:', error);
            alert('Password reset failed!');
        });
    });
</script>
</html>
