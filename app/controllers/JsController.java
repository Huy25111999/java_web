package controllers;

import controllers.*;
import controllers.routes;
import jsmessages.JsMessages;
import jsmessages.JsMessagesFactory;
import jsmessages.japi.Helper;
import play.libs.Scala;
import play.mvc.Controller;
import play.mvc.Result;
import play.routing.JavaScriptReverseRouter;

import javax.inject.Inject;

public class JsController extends Controller {
    private final JsMessages jsMessages;


    @Inject
    public JsController(JsMessagesFactory jsMessagesFactory) {
        jsMessages = jsMessagesFactory.all();
    }

    public Result jsMessages() {
        return ok(jsMessages.apply(Scala.Option("window.Messages"), Helper.messagesFromCurrentHttpContext()));
    }

    public Result javascriptRoutes() {
        return ok(
                JavaScriptReverseRouter.create("jsRoutes",
                        controllers.routes.javascript.UserController.exportUserList(),
                        controllers.routes.javascript.UserController.addUser(),
                        controllers.routes.javascript.UserController.delUsers()

                )

        ).as("text/javascript");

    }
}
