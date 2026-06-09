package com.mycompany.chatapppart1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        // This clears the Part 3 lists before each test.
        // It is like wiping the table before doing new homework.
        Message.clearMessageLists();

        message1 = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message1.setMessageID("0012345678");

        message2 = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        message2.setMessageID("0012345678");
    }

    @Test
    public void testCheckMessageLength_validMessage_returnsSuccess() {
        String result = message1.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testCheckMessageLength_over250chars_returnsFailureWithCount() {
        String longMessage = "a".repeat(260);
        String result = message1.checkMessageLength(longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }

    @Test
    public void testCheckMessageLength_exactlyAtLimit_returnsSuccess() {
        String messageText = "a".repeat(250);
        String result = message1.checkMessageLength(messageText);
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testCheckMessageLength_oneOver_returnsFailureWithCountOf1() {
        String messageText = "a".repeat(251);
        String result = message1.checkMessageLength(messageText);
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.", result);
    }

    @Test
    public void testCheckRecipientCell_validNumber_returnsSuccess() {
        String result = message1.checkRecipientCell();
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testCheckRecipientCell_invalidNumber_returnsFailure() {
        String result = message2.checkRecipientCell();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    @Test
    public void testCreateMessageHash_correctFormat_returnsExpectedHash() {
        String result = message1.createMessageHash();
        assertEquals("00:0:HITONIGHT", result);
    }

    @Test
    public void testCreateMessageHash_isUppercase() {
        String result = message1.createMessageHash();
        assertEquals(result.toUpperCase(), result);
    }

    @Test
    public void testCreateMessageHash_multipleMessages_loopTest() {
        Message[] messages = {
            new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?"),
            new Message(1, "+27718693003", "Hello friend see you later")
        };

        messages[0].setMessageID("0012345678");
        messages[1].setMessageID("0012345678");

        String[] expectedEnds = {":0:HITONIGHT", ":1:HELLOLATER"};

        for (int i = 0; i < messages.length; i++) {
            assertTrue(messages[i].createMessageHash().endsWith(expectedEnds[i]));
        }
    }

    @Test
    public void testCheckMessageID_generatedID_isNotNull() {
        Message message = new Message();
        assertNotNull(message.getMessageID());
    }

    @Test
    public void testCheckMessageID_generatedID_isExactly10Chars() {
        Message message = new Message();
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    @Test
    public void testSentMessage_userSelectsSend_returnsCorrectString() {
        String result = message1.sentMessage(1);
        assertEquals("Message successfully sent.", result);
    }

    @Test
    public void testSentMessage_userSelectsDisregard_returnsCorrectString() {
        String result = message1.sentMessage(2);
        assertEquals("Press 0 to delete the message.", result);
    }

    @Test
    public void testSentMessage_userSelectsStore_returnsCorrectString() {
        String result = message1.sentMessage(3);
        assertEquals("Message successfully stored.", result);
    }

    // === PART 3 TESTS ===
    // These tests check the new arrays, searches, deleting and report.

    @Test
    public void testSentMessagesArray_correctlyPopulated() {
        // Message 1 is sent.
        Message testMessage1 = new Message(0, "+27834557896", "Did you get the cake?");
        testMessage1.setMessageID("0000000001");
        testMessage1.sentMessage(1);

        // Message 4 is also sent.
        Message testMessage4 = new Message(3, "0838884567", "It is dinner time!");
        testMessage4.setMessageID("0838884567");
        testMessage4.sentMessage(1);

        assertTrue(Message.getSentMessages().contains("Did you get the cake?"));
        assertTrue(Message.getSentMessages().contains("It is dinner time!"));
    }

    @Test
    public void testDisplayLongestMessage_returnsCorrectMessage() {
        // These are the stored messages from the POE test data.
        Message.addStoredMessageForTesting("Did you get the cake?");
        Message.addStoredMessageForTesting("Where are you? You are late! I have asked you to be on time.");
        Message.addStoredMessageForTesting("Yohoooo, I am at your gate.");
        Message.addStoredMessageForTesting("It is dinner time!");
        Message.addStoredMessageForTesting("Ok, I am leaving without you.");

        String result = Message.displayLongestMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", result);
    }

    @Test
    public void testSearchByMessageID_returnsCorrectMessage() {
        Message testMessage4 = new Message(3, "0838884567", "It is dinner time!");
        testMessage4.setMessageID("0838884567");
        testMessage4.sentMessage(1);

        String result = Message.searchByMessageID("0838884567");
        assertEquals("It is dinner time!", result);
    }

    @Test
    public void testSearchByRecipient_returnsAllMatchingMessages() {
        Message testMessage2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        testMessage2.setMessageID("0000000002");
        testMessage2.sentMessage(1);

        Message testMessage5 = new Message(4, "+27838884567", "Ok, I am leaving without you.");
        testMessage5.setMessageID("0000000005");
        testMessage5.sentMessage(1);

        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByHash_removesCorrectMessage() {
        Message testMessage2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        testMessage2.setMessageID("0000000002");
        testMessage2.sentMessage(1);

        String hash = testMessage2.getMessageHash();
        String result = Message.deleteByHash(hash);

        assertEquals("Message: Where are you? You are late! I have asked you to be on time. successfully deleted.", result);
    }

    @Test
    public void testDisplayReport_containsRequiredFields() {
        Message testMessage1 = new Message(0, "+27834557896", "Did you get the cake?");
        testMessage1.setMessageID("0000000001");
        testMessage1.sentMessage(1);

        String report = Message.displayReport();

        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
    }
}
