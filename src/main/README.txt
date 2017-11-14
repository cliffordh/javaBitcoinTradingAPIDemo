This project exercises the Bitso.com trading API.

This project is compatible with IntelliJ IDEA. To execute this project, import this project into IntelliJ
and run Main.java.

Schedule the polling of trades over REST.                       Controller.java     init()
Request a book snapshot over REST.                              Controller.java     fetchOrders()
Listen for diff-orders over websocket.                          WSClient.java       onMessage()
Replay diff-orders.                                             OrderBookModel.java addDiffOrder()
Use config option X to request  recent trades.                  Controller.java     init()
Use config option X to limit number of ASKs displayed in UI.    OrderBookModel.java getSortedFilteredObservableAsks
The loop that causes the trading algorithm to reevaluate.       TradesModel.java    setTradeResponse()
