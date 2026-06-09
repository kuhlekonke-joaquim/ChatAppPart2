package com.mycompany.chatapppart1;

import java.util.Scanner;

/**
 * Main class for ChatApp. Runs Part 1 registration/login and launches the Part 2 and Part 3 message menus.
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // === REGISTRATION SECTION ===
        // This part lets the user make an account before using the app.
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

        // === LOGIN SECTION ===
        // This part checks if the user can enter the app.
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

            // Part 3: load stored messages before showing the menu.
            // This is like opening the toy box before playing with the toys.
            Message.loadStoredMessages();

            runMessageMenu(input);
        } else {
            System.out.println("You must be logged in to send messages.");
        }
    }

    /**
     * Runs the message menu until the user chooses to quit.
     *
     * @param input Scanner used to read user input
     */
    private static void runMessageMenu(Scanner input) {
        boolean running = true;

        // === MAIN CHAT MENU ===
        // This menu is the main control room of the app.
        while (running) {
            System.out.println("\n========= CHAT MENU =========");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
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
                case 4:
                    storedMessagesMenu(input);
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }
    }

    /**
     * Uses a for loop to allow the user to enter multiple messages.
     *
     * @param input Scanner used to read user input
     */
    private static void sendMessages(Scanner input) {
        System.out.println("How many messages would you like to send?");
        int numMessages = input.nextInt();
        input.nextLine();

        // This loop repeats for the amount of messages the user asked for.
        for (int i = 0; i < numMessages; i++) {
            int messageNumber = i;
            System.out.println("\n--- Message " + (i + 1) + " ---");

            System.out.print("Enter recipient cell number: ");
            String recipient = input.nextLine();

            System.out.print("Enter your message: ");
            String messageText = input.nextLine();

            // Create one message object with its number, recipient and text.
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

    /**
     * Runs the Part 3 stored messages sub-menu.
     *
     * @param input Scanner used to read user input
     */
    private static void storedMessagesMenu(Scanner input) {
        boolean inStoredMenu = true;

        // === STORED MESSAGES MENU ===
        // This is the small menu inside option 4.
        // It lets the user search, delete and view reports.
        while (inStoredMenu) {
            System.out.println("\n========= STORED MESSAGES MENU =========");
            System.out.println("a) Display all stored messages");
            System.out.println("b) Display longest message");
            System.out.println("c) Search by message ID");
            System.out.println("d) Search by recipient");
            System.out.println("e) Delete by message hash");
            System.out.println("f) Display full report");
            System.out.println("g) Back to main menu");
            System.out.print("Choose option: ");

            String choice = input.nextLine().toLowerCase();

            switch (choice) {
                case "a":
                    // Show the stored messages loaded from messages.json.
                    System.out.println(Message.displayStoredMessages());
                    break;
                case "b":
                    // Find the biggest/longest stored message.
                    System.out.println("Longest message: " + Message.displayLongestMessage());
                    break;
                case "c":
                    // Search by the message ID number.
                    System.out.print("Enter message ID: ");
                    String id = input.nextLine();
                    System.out.println(Message.searchByMessageID(id));
                    break;
                case "d":
                    // Search by the recipient phone number.
                    System.out.print("Enter recipient number: ");
                    String recipient = input.nextLine();
                    System.out.println(Message.searchByRecipient(recipient));
                    break;
                case "e":
                    // Delete one message using its hash.
                    System.out.print("Enter message hash: ");
                    String hash = input.nextLine();
                    System.out.println(Message.deleteByHash(hash));
                    break;
                case "f":
                    // Print the full sent message report.
                    System.out.println(Message.displayReport());
                    break;
                case "g":
                    inStoredMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a, b, c, d, e, f, or g.");
            }
        }
    }
}
