package com.tatenda.MoneyTransfer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle
{

  private Map<String, Account> accounts = new LinkedHashMap<String, Account>();

  private void accountData()
  {
    Account userOneAcc = new Account("123456789", 500);
    Account userSixAcc = new Account("1111111111", 500);
    Account userTwoAcc = new Account("987654321", 500);
    Account userThreeAcc = new Account("123454321", 500);
    Account userFourAcc = new Account("987656789", 500);
    Account userFifeAcc = new Account("999999999", 500);
    accounts.put(userOneAcc.getAccountNumber(), userOneAcc);
    accounts.put(userTwoAcc.getAccountNumber(), userTwoAcc);
    accounts.put(userThreeAcc.getAccountNumber(), userThreeAcc);
    accounts.put(userFourAcc.getAccountNumber(), userFourAcc);
    accounts.put(userFifeAcc.getAccountNumber(), userFifeAcc);
    accounts.put(userSixAcc.getAccountNumber(), userSixAcc);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    accountData();

    Router router = Router.router(vertx);
    router.route("/").handler(routingContext -> {
    HttpServerResponse response = routingContext.response();
    response
      .putHeader("content-type", "text/html")
      .end("<h1>Hello from my first Vert.x 3 application</h1>");
    });

    router.get("/api/accounts").handler(this::getAll);
    router.route("/api/accounts*").handler(BodyHandler.create());
    router.put("/api/accounts/transfer").handler(this::transfer);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080,
        result -> {
        if (result.succeeded())
          {
          startPromise.complete();
          }
        else
          {
          startPromise.fail(result.cause());
          }
        }
      );
  }

  private void getAll(RoutingContext routingContext)
  {
    routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(accounts.values()));
  }

  private void transfer(RoutingContext routingContext)
  {
    JsonObject json = routingContext.getBodyAsJson();
    if (json == null)
      {
      routingContext.response().setStatusCode(400).end();
      }
    else
      {
      final String sourceAccountNum = json.getString("sender");
      final String destAccountNum = json.getString("receiver");

      if (sourceAccountNum != null && destAccountNum != null &&
        accounts.containsKey(sourceAccountNum) && accounts.containsKey(destAccountNum))
        {
        Account senderAcc = accounts.get(sourceAccountNum);
        Account receiverAcc = accounts.get(destAccountNum);
        Double senderAccBalance = senderAcc.getBalance();
        Double receiverAccBalance = receiverAcc.getBalance();
        Double amount = Double.valueOf(json.getString("amount"));
        if (senderAccBalance >= amount)
          {
          try
            {
            senderAcc.transfer(-1 * amount);
            receiverAcc.transfer(amount);
            }
          catch (Exception e)
            {
            senderAcc.setBalance(senderAccBalance);
            receiverAcc.setBalance(receiverAccBalance);
            }
          }
        routingContext.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(accounts.values()));
        }
      else
        {
        routingContext.response().setStatusCode(404).end();
        }
      }
  }
}
