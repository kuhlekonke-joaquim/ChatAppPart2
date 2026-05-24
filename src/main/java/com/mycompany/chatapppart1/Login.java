package com.mycompany.chatapppart1;

/**
 * Handles user registration, validation, and login for ChatApp.
 */
public class Login {

    private String username;
    private String password;
    private String phoneNumber;

    /**
     * Checks that the username contains an underscore and is no more than five characters.
     * @param username the username entered by the user
     * @return true if the username is valid, otherwise false
     */
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks that the password has at least 8 characters, a capital letter, a number, and a special character.
     * @param password the password entered by the user
     * @return true if the password is valid, otherwise false
     */
    public boolean checkPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)) {
                hasCapital = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Checks that the cell phone number starts with +27 and is no more than 12 characters.
     * @param phone the cellphone number entered by the user
     * @return true if the number is valid, otherwise false
     */
    public boolean checkCellPhoneNumber(String phone) {
        return phone != null && phone.startsWith("+27") && phone.length() <= 12;
    }

    /**
     * Registers the user if all validation rules are met.
     * @param username the chosen username
     * @param password the chosen password
     * @param phoneNumber the user's phone number
     * @return a registration status message
     */
    public String registerUser(String username, String password, String phoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPassword(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;

        return "User registered successfully.";
    }

    /**
     * Checks if the entered login details match the registered user details.
     * @param username the username entered at login
     * @param password the password entered at login
     * @return true if login details are correct, otherwise false
     */
    public boolean loginUser(String username, String password) {
        return this.username != null && this.password != null
                && this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Returns the correct login status message.
     * @param success whether login was successful
     * @return login status message
     */
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + username + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
