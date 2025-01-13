package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO = new MessageDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Creates a new message.
     * @param message The message to create.
     * @return The created message, or null if validation fails.
     */
    public Message createMessage(Message message) {
        // Validate message text
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            return null; // Message text cannot be blank
        }
        if (message.getMessage_text().length() > 255) {
            return null; // Message text cannot exceed 255 characters
        }

        // Validate user existence
        if (accountDAO.findById(message.getPosted_by()) == null) {
            return null; // User must exist to post a message
        }

        // Save the message to the database
        return messageDAO.save(message);
    }

    /**
     * Retrieves all messages.
     * @return A list of all messages.
     */
    public List<Message> getAllMessages() {
        List<Message> messages = messageDAO.findAll();
        return messages != null ? messages : new ArrayList<>();
    }

    /**
     * Retrieves a message by its ID.
     * @param messageId The ID of the message.
     * @return The message, or null if not found.
     */
    public Message getMessageById(int messageId) {
        return messageDAO.findById(messageId);
    }

    /**
     * Deletes a message by its ID.
     * @param messageId The ID of the message to delete.
     * @return The deleted message, or null if not found.
     */
    public Message deleteMessage(int messageId) {
        return messageDAO.delete(messageId);
    }

    /**
     * Updates the text of a message by its ID.
     * @param messageId The ID of the message to update.
     * @param newMessageText The new text for the message.
     * @return The updated message, or null if validation fails or message not found.
     */
    public Message updateMessage(int messageId, String newMessageText) {
        // Validate input
        if (newMessageText == null || newMessageText.isBlank()) {
            return null; // Updated message text cannot be blank
        }
        if (newMessageText.length() > 255) {
            return null; // Updated message text cannot exceed 255 characters
        }

        // Update the message
        return messageDAO.update(messageId, newMessageText);
    }

    /**
     * Retrieves all messages by a specific user.
     * @param userId The ID of the user.
     * @return A list of messages by the user, or an empty list if no messages are found.
     */
    public List<Message> getMessagesByUser(int userId) {
        // Validate user existence
        if (accountDAO.findById(userId) == null) {
            return new ArrayList<>(); // Return an empty list if the user does not exist
        }

        // Retrieve messages
        List<Message> messages = messageDAO.findByUserId(userId);
        return messages != null ? messages : new ArrayList<>();
    }
}
