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
}
