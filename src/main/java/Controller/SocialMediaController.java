package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

public class SocialMediaController {
    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // User endpoints
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);

        // Message endpoints
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessageText);

        // User-specific messages
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);

        return app;
    }

    private void registerUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account createdAccount = accountService.registerAccount(account);
        if (createdAccount != null) {
            ctx.json(createdAccount).status(200);
        } else {
            ctx.status(400).result(""); // Test cases expect an empty body on failure
        }
    }

    private void loginUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account authenticatedAccount = accountService.loginAccount(account);
        if (authenticatedAccount != null) {
            ctx.json(authenticatedAccount).status(200);
        } else {
            ctx.status(401).result(""); // Test cases expect an empty body on failure
        }
    }

    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.json(createdMessage).status(200);
        } else {
            ctx.status(400).result(""); // Test cases expect an empty body on failure
        }
    }

    private void getAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages()).status(200);
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message).status(200);
        } else {
            ctx.status(200).result(""); // Return an empty body if the message is not found
        }
    }

    private void deleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.status(200).result(""); // Return an empty body if the message is not found
        }
    }

    private void updateMessageText(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageUpdate = ctx.bodyAsClass(Message.class);
        Message updatedMessage = messageService.updateMessage(messageId, messageUpdate.getMessage_text());
        if (updatedMessage != null) {
            ctx.json(updatedMessage).status(200);
        } else {
            ctx.status(400).result(""); // Test cases expect an empty body on failure
        }
    }

    private void getMessagesByUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByUser(accountId)).status(200);
    }
}
