package com.mycompany.chatapppart1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONObject;

/**
 * Represents one ChatApp message and handles validation, hashing, sending,
 * storing, searching, deleting and reporting for the POE.
 */
public class Message {

    // === PART 3 MEMORY LISTS ===
    // This list remembers every message the user actually sends.
    private static List<String> sentMessages = new ArrayList<>();

    // This list remembers every message the user throws away/disregards.
    private static List<String> disregardedMessages = new ArrayList<>();

    // This list remembers messages that were read from the JSON file.
    private static List<String> storedMessages = new ArrayList<>();

    // This list remembers every message hash, like a name tag for each message.
    private static List<String> messageHashes = new ArrayList<>();

    // This list remembers every message ID, like a special number for each message.
    private static List<String> messageIDs = new ArrayList<>();

    // This list remembers who each message was sent to.
    // We need this for searching by recipient in Part 3.
    private static List<String> recipientList = new ArrayList<>();

    // This list remembers all message texts that have an ID/hash/recipient beside them.
    // It keeps the same position as messageHashes, messageIDs and recipientList.
    private static List<String> allTrackedMessages = new ArrayList<>();

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String sendStatus;

    /**
     * Creates an empty message object for testing or later setup.
     */
    public Message() {
        this.messageID = generateMessageID();
    }

    /**
     * Creates a message with all required details.
     *
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
     *
     * @return a 10 digit message ID as a String
     */
    private String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(number);
    }

    /**
     * This method checks that the message ID is not more than 10 characters.
     *
     * @return true if the message ID is valid, otherwise false
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * This method checks if the message is not more than 250 characters.
     *
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
     * This method checks if the recipient cellphone number is correct.
     *
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
     * This method creates the message hash.
     *
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
     * This method allows the user to send, disregard or store the message.
     *
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
     * This method handles the user's selected message action.
     *
     * @param option the user's selected option
     * @return the selected action message
     */
    public String sentMessage(int option) {
        // Before we save anything, make sure the hash is fresh and matches this message.
        createMessageHash();

        switch (option) {
            case 1 -> {
                sendStatus = "Sent";

                // The user chose send, so we put the message into the sent basket.
                sentMessages.add(messageText);

                // Same index = same message.
                trackMessageDetails();

                return "Message successfully sent.";
            }
            case 2 -> {
                sendStatus = "Disregarded";

                // The user chose disregard, so we only remember it in the discarded basket.
                disregardedMessages.add(messageText);

                return "Press 0 to delete the message.";
            }
            case 3 -> {
                sendStatus = "Stored";

                // The user chose store, so we write it into messages.json.
                storeMessage();

                // We also track the hash, ID, recipient and text for searching/reporting.
                trackMessageDetails();

                return "Message successfully stored.";
            }
            default -> {
                return "Invalid option selected.";
            }
        }
    }

    /**
     * Saves one message's ID, hash, recipient and text in matching positions.
     */
    private void trackMessageDetails() {
        messageHashes.add(messageHash);
        messageIDs.add(messageID);
        recipientList.add(recipient);
        allTrackedMessages.add(messageText);
    }

    /**
     * Returns message details for this one message.
     *
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
     *
     * @param totalMessages the total messages counted in the loop
     * @return total message count
     */
    public int returnTotalMessages(int totalMessages) {
        return totalMessages;
    }

    /**
     * This method stores the message in a JSON file.
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

    /**
     * Reads messages.json and places stored messages into the storedMessages list.
     * Attribution: org.json library from https://mvnrepository.com/artifact/org.json/json
     */
    // This method loads stored messages from the JSON file.
    public static void loadStoredMessages() {
        // We clear first so the same file does not get loaded twice by mistake.
        storedMessages.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            StringBuilder objectText = new StringBuilder();
            int openBraces = 0;

            // We read the JSON file slowly, line by line, like picking up puzzle pieces.
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty()) {
                    continue;
                }

                objectText.append(trimmedLine);

                // These counters help us know when one JSON object starts and ends.
                openBraces += countCharacter(trimmedLine, '{');
                openBraces -= countCharacter(trimmedLine, '}');

                if (openBraces == 0 && objectText.length() > 0) {
                    JSONObject object = new JSONObject(objectText.toString());
                    storedMessages.add(object.optString("message", object.optString("Message", "")));
                    objectText.setLength(0);
                }
            }
        } catch (IOException error) {
            // If no file exists yet, the app must not crash.
            System.out.println("No stored messages found yet.");
        }
    }

    /**
     * Counts a specific character in a String.
     *
     * @param text the text being checked
     * @param character the character being counted
     * @return number of times the character appears
     */
    private static int countCharacter(String text, char character) {
        int count = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == character) {
                count++;
            }
        }

        return count;
    }

    /**
     * Displays all stored messages that were loaded from JSON.
     *
     * @return formatted stored messages
     */
    public static String displayStoredMessages() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        StringBuilder output = new StringBuilder("=== STORED MESSAGES ===\n");

        for (int i = 0; i < storedMessages.size(); i++) {
            output.append("Stored Message ").append(i + 1).append(": ")
                    .append(storedMessages.get(i)).append("\n");
        }

        return output.toString();
    }

    /**
     * Finds the longest stored message.
     *
     * @return the longest message from storedMessages
     */
    // This method finds the longest stored message.
    public static String displayLongestMessage() {
        String longest = "";

        // We check each message and keep the biggest one.
        for (String message : storedMessages) {
            if (message.length() > longest.length()) {
                longest = message;
            }
        }

        return longest;
    }

    /**
     * Searches for a message using the message ID.
     *
     * @param id the message ID being searched for
     * @return matching message or not found message
     */
    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                return allTrackedMessages.get(i);
            }
        }

        return "Message not found.";
    }

    /**
     * Searches for all messages sent to one recipient.
     *
     * @param recipient the cellphone number being searched for
     * @return all matching messages or no messages found
     */
    // This method searches for messages using the recipient number.
    public static String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();

        // We look through the recipient list and collect every matching message.
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).equals(recipient)) {
                results.append(allTrackedMessages.get(i)).append("\n");
            }
        }

        if (results.length() == 0) {
            return "No messages found.";
        }

        return results.toString();
    }

    /**
     * Deletes a message using the message hash.
     *
     * @param hash the hash being searched for
     * @return success message or not found message
     */
    public static String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String deletedMessage = allTrackedMessages.get(i);

                // Remove from every matching list so the positions stay correct.
                messageHashes.remove(i);
                messageIDs.remove(i);
                recipientList.remove(i);
                allTrackedMessages.remove(i);
                sentMessages.remove(deletedMessage);
                storedMessages.remove(deletedMessage);

                return "Message: " + deletedMessage + " successfully deleted.";
            }
        }

        return "Hash not found.";
    }

    /**
     * Creates a full report for sent messages.
     *
     * @return formatted report with hash, recipient and message
     */
    public static String displayReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== MESSAGE REPORT ===\n");

        // The report only shows sent messages, as required by Part 3.
        for (String sentMessage : sentMessages) {
            int trackedIndex = allTrackedMessages.indexOf(sentMessage);

            if (trackedIndex != -1) {
                report.append("------------------------------\n");
                report.append("Message Hash: ").append(messageHashes.get(trackedIndex)).append("\n");
                report.append("Recipient: ").append(recipientList.get(trackedIndex)).append("\n");
                report.append("Message: ").append(sentMessage).append("\n");
            }
        }

        return report.toString();
    }

    /**
     * Clears all Part 3 lists so tests can start fresh every time.
     */
    public static void clearMessageLists() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipientList.clear();
        allTrackedMessages.clear();
    }

    /**
     * Adds a stored message for unit testing.
     *
     * @param message the stored message text
     */
    public static void addStoredMessageForTesting(String message) {
        storedMessages.add(message);
    }

    public static List<String> getSentMessages() {
        return sentMessages;
    }

    public static List<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static List<String> getStoredMessages() {
        return storedMessages;
    }

    public static List<String> getMessageHashes() {
        return messageHashes;
    }

    public static List<String> getMessageIDs() {
        return messageIDs;
    }

    public static List<String> getRecipientList() {
        return recipientList;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
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
        this.messageHash = createMessageHash();
    }

    public String getMessageHash() {
        return messageHash;
    }
}
