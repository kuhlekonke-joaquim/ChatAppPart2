package com.mycompany.chatapppart1;

import java.util.Scanner;

/**
 * Main class for ChatApp. Runs Part 1 registration/login and launches the Part 2 message menu.
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();
//---REGISTRATION SECTION---
// This section allows the user to register by entering a username, password and phone number.
        System.out.println("=== USER REGISTRATION ===");

        System.out.print("Enter a username: ");
        String username = input.nextLine();

        System.out.print("Enter a password: ");
        String password = input.nextLine();

        System.out.print("Enter your South African phone number (+27...): ");
        String phone = input.nextLine();

        String response = login.registerUser(username, password, phone);
        System.out.println(response);

        if (!response.equals("User registered successfully.")) {
            System.out.println("Registration failed. Program closing.");
            return;
        }
//---LOGIN SECTION---
// This section allows the user to log in using the registered username and password.
        System.out.println("\n=== USER LOGIN ===");

        System.out.print("Enter your username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = input.nextLine();

        boolean loggedIn = login.loginUser(loginUsername, loginPassword);
        String loginMessage = login.returnLoginStatus(loggedIn);
        System.out.println(loginMessage);

        if (loggedIn) {
            System.out.println("Welcome to ChatApp.");
            runMessageMenu(input);
        } else {
            System.out.println("You must be logged in to send messages.");
        }
    }

    /**
     * Runs the Part 2 menu until the user chooses to quit.
     * @param input Scanner used to read user input
     */
    //---PART 2 MENU SECTION---
// This section displays the message menu after the user has successfully logged in.
    private static void runMessageMenu(Scanner input) {
        boolean running = true;
//---PART 2 MESSAGE MENU---
// This menu allows the user to send messages, view the coming soon feature,
// or quit the application.
        while (running) {
            System.out.println("\n========= CHAT MENU =========");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose option: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    sendMessages(input);
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }

    /**
      Uses a for loop to allow the user to enter multiple messages.
     * @param input Scanner used to read user input
     */
    //---SEND MESSAGE SECTION---
// This section asks the user how many messages they want to send.
    private static void sendMessages(Scanner input) {
        System.out.println("How many messages would you like to send?");
        int numMessages = input.nextInt();
        input.nextLine();
// This for loop runs according to the number of messages entered by the user.
        for (int i = 0; i < numMessages; i++) {
            int messageNumber = i;
            System.out.println("\n--- Message " + (i + 1) + " ---");

            System.out.print("Enter recipient cell number: ");
            String recipient = input.nextLine();

            System.out.print("Enter your message: ");
            String messageText = input.nextLine();
// Create the message object using the message number, recipient and message text
            Message message = new Message(messageNumber, recipient, messageText);

            System.out.println(message.checkRecipientCell());
            String lengthStatus = message.checkMessageLength(messageText);
            System.out.println(lengthStatus);

            if (lengthStatus.equals("Message ready to send.")) {
                String actionMessage = message.sentMessage();
                System.out.println(actionMessage);
                System.out.println(message.printMessages());
            }
        }

        System.out.println("Total messages entered: " + numMessages);
    }
}