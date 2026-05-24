package com.mycompany.chatapppart1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONObject;

/**
 * represents one ChatApp message and handles validation, hashing, sending, and JSON storage.
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String sendStatus;

    /**
     * Creates an empty message object for testing or later setup.
     */
    //---MESSAGE CLASS---
// This class handles all the message details for Part 2.
    public Message() {
        this.messageID = generateMessageID();
    }

    /**
     * Creates a message with all required details.
     * @param messageNumber the message number from the loop counter
     * @param recipient the recipient cellphone number
     * @param messageText the text of the message
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    /**
     * Generates a random 10 digit message ID.
     * @return a 10 digit message ID as a String
     */
       private String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(number);
    }

    /**
     // This method checks that the message ID is not more than 10 characters.
     * @return true if the message ID is valid, otherwise false
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     // This method checks if the message is not more than 250 characters.
     * @param message the message text to validate
     * @return a success or failure message
     */
    
    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        }

        int over = message.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    /**
     // This method checks if the recipient cellphone number is correct.
     * @return a success or failure message
     */
    public String checkRecipientCell() {
        Login login = new Login();

        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     // This method creates the message hash.
     * @return the generated message hash in uppercase
     */
    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
            return "";
        }

        String idPart = messageID.substring(0, 2);
        String cleanedMessage = messageText.trim().replaceAll("[^a-zA-Z0-9 ]", "");
        String[] words = cleanedMessage.split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        messageHash = (idPart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    /**
    // This method allows the user to send, disregard or store the message.
     * @return the selected action message
     */
    public String sentMessage() {
        Scanner input = new Scanner(System.in);

        System.out.println("What would you like to do with this message?");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Choose option: ");

        int option = input.nextInt();
        return sentMessage(option);
    }

    /**
     // This method prints the message details.
     * @param option the user's selected option
     * @return the selected action message
     */
    public String sentMessage(int option) {
        switch (option) {
            case 1 -> {
                sendStatus = "Sent";
                return "Message successfully sent.";
            }
            case 2 -> {
                sendStatus = "Disregarded";
                return "Press 0 to delete the message.";
            }
            case 3 -> {
                sendStatus = "Stored";
                storeMessage();
                return "Message successfully stored.";
            }
            default -> {
                return "Invalid option selected.";
            }
        }
    }

    /**
     * Returns message details in the required order.
     * @return formatted message details
     */
    public String printMessages() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    /**
     * Returns the total number of messages entered.
     * @param totalMessages the total messages counted in the loop
     * @return total message count
     */
    public int returnTotalMessages(int totalMessages) {
        return totalMessages;
    }

    /**
     // This method stores the message in a JSON file.
     * Attribution: org.json library from https://mvnrepository.com/artifact/org.json/json
     */
    public void storeMessage() {
        JSONObject messageObject = new JSONObject();
        messageObject.put("messageID", messageID);
        messageObject.put("messageHash", messageHash);
        messageObject.put("recipient", recipient);
        messageObject.put("message", messageText);
        messageObject.put("status", sendStatus);

        try (FileWriter writer = new FileWriter("messages.json", true)) {
            writer.write(messageObject.toString(4));
            writer.write(System.lineSeparator());
        } catch (IOException error) {
            System.out.println("Could not store message: " + error.getMessage());
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
