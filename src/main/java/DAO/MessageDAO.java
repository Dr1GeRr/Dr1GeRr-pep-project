package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Save a new message to the database
    public Message save(Message message) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    message.setMessage_id(generatedKeys.getInt(1));
                    return message;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve all messages
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Retrieve a message by ID
    public Message findById(int messageId) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete a message by ID
    public Message delete(int messageId) {
        Message deletedMessage = findById(messageId);
        if (deletedMessage != null) {
            try {
                Connection connection = ConnectionUtil.getConnection();
                String sql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, messageId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return deletedMessage;
    }

    // Update a message text by ID
    public Message update(int messageId, String newMessageText) {
        Message messageToUpdate = findById(messageId);
        if (messageToUpdate != null) {
            try {
                Connection connection = ConnectionUtil.getConnection();
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newMessageText);
                preparedStatement.setInt(2, messageId);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 1) {
                    messageToUpdate.setMessage_text(newMessageText); // Adjusted setter usage
                    return messageToUpdate;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Retrieve all messages by a specific user
    public List<Message> findByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
