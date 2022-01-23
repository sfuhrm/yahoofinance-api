# Finance Quotes API for Yahoo Finance (Java)

[![Java CI with Maven](https://github.com/sfuhrm/yahoofinance-api/actions/workflows/maven.yml/badge.svg)](https://github.com/sfuhrm/yahoofinance-api/actions/workflows/maven.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sfuhrm/YahooFinanceAPI/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sfuhrm/YahooFinanceAPI)
[![javadoc](https://javadoc.io/badge2/de.sfuhrm/YahooFinanceAPI/javadoc.svg)](https://javadoc.io/doc/de.sfuhrm/YahooFinanceAPI)
[![ReleaseDate](https://img.shields.io/github/release-date/sfuhrm/yahoofinance-api)](https://github.com/sfuhrm/yahoofinance-api/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

### Open Developer Jobs (ad)

Take a look at some of open job offers from one of Germanys top internet hoster (2022-01-21), remote work is possible:

- [Software Developer CI/CD](https://united-internet.talentry.com/share/job/192434/600581/1642766341/3)
- [Java Backend Developers](https://united-internet.talentry.com/share/job/200597/600581/1642513126/3)
- [PHP / GO Developers](https://united-internet.talentry.com/share/job/220120/600581/1642513009/3)
- [Java Developers](https://united-internet.talentry.com/share/job/225983/600581/1642513102/3)
- [more jobs](https://united-internet.talentry.com/list/m46c026eamchgwc1islqbv)

German job openings don't mean that we're not talking english :).

### About this fork

This is a fork of sstrickx' yahoofinanceapi which can be found [here](https://github.com/sstrickx/yahoofinance-api).

The original repository seems to be no longer maintained. The intention of this fork is to provide a release with
bugfixes, not new features.

The changes in this fork are:

* Proper closing of input/output streams, thereby working properly with HTTPS sockets.
* Several fixes of deprecation warnings.
* Raise minimum Java required to JDK 8.
* Adding/changing of some badges in the project README.md.
* Change Maven group id to 'de.sfuhrm'.
* Add Github actions for continuous integration.

Besides these changes, the software can be used as a drop-in replacement for sstrickx' Maven artifacts.

### About the library

This library provides some methods that should make it easy to communicate with the Yahoo Finance API. It allows you to request detailed information, some statistics and historical quotes on stocks. Separate functionality is available to request a simple FX quote.
Please check the javadoc (available in dist directory) to get a complete overview of the available methods and to get an idea of which data is available from Yahoo Finance.

> This project is not associated with nor sponsored by Yahoo! Inc. Yahoo! Inc. is the exclusive owner of all trademark and other intellectual property rights in and to the YAHOO! and Y! trademarks (the "Trademarks"), including the stylized YAHOO! and Y! logos. Yahoo! Inc. owns trademark registrations for the Trademarks.

## Add to your project as a dependency
### Maven
```xml
<dependency>
    <groupId>de.sfuhrm</groupId>
    <artifactId>YahooFinanceAPI</artifactId>
    <version>x.y.z</version>
</dependency>
```
### Gradle
```groovy
dependencies {
    compile group: 'de.sfuhrm', name: 'YahooFinanceAPI', version: 'x.y.z'
}
```
### Ivy
```xml
<dependency org="de.sfuhrm" name="YahooFinanceAPI" rev="x.y.z" />
```

# Examples
## Single stock
```java
Stock stock = YahooFinance.get("INTC");

BigDecimal price = stock.getQuote().getPrice();
BigDecimal change = stock.getQuote().getChangeInPercent();
BigDecimal peg = stock.getStats().getPeg();
BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();

stock.print();
```
Output:
```
INTC
--------------------------------
symbol: INTC
name: Intel Corporation
currency: USD
stockExchange: NasdaqNM
quote: Ask: 32.25, Bid: 32.24, Price: 32.2485, Prev close: 33.62
stats: EPS: 2.019, PE: 16.65, PEG: 1.74
dividend: Pay date: Mon Dec 01 06:00:00 CET 2014, Ex date: Tue Aug 05 06:00:00 CEST 2014, Annual yield: 2.68%
history: null
--------------------------------
```

## Single stock, easy refresh
```java
Stock stock = YahooFinance.get("INTC");
double price = stock.getQuote(true).getPrice();
```
This will also automatically refresh the statistics and dividend data of the stock in a single request to Yahoo Finance.
Please be aware that it wouldn't be a good idea to call the getQuote(true), getStats(true) or getDividend(true) too much in a short timespan as this will cost too much delay without providing any added value. There's no problem to call the versions of those methods without argument or with the argument set to false.

## Multiple stocks at once
```java
String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
Map<String, Stock> stocks = YahooFinance.get(symbols); // single request
Stock intel = stocks.get("INTC");
Stock airbus = stocks.get("AIR.PA");
```

## FX quote
```java
FxQuote usdeur = YahooFinance.getFx(FxSymbols.USDEUR);
FxQuote usdgbp = YahooFinance.getFx("USDGBP=X");
System.out.println(usdeur);
System.out.println(usdgbp);
```
Output:
```
USDEUR=X: 0.7842
USDGBP=X: 0.6253
```

## Single stock, include historical quotes (1)
```java
Stock tesla = YahooFinance.get("TSLA", true);
System.out.println(tesla.getHistory());
```
Output: (Symbol@Date: low-high, open->close (adjusted close))
```
[TSLA@2014-10-01: 217.32-265.54, 242.2->229.7 (229.7), TSLA@2014-09-02: 240.12-291.42, 275.5->242.68 (242.68), ...]
```

## Single stock, include historical quotes (2)
```java
Calendar from = Calendar.getInstance();
Calendar to = Calendar.getInstance();
from.add(Calendar.YEAR, -5); // from 5 years ago

Stock google = YahooFinance.get("GOOG", from, to, Interval.WEEKLY);
```

## Multiple stocks, include historical quotes
```java
String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
// Can also be done with explicit from, to and Interval parameters
Map<String, Stock> stocks = YahooFinance.get(symbols, true);
Stock intel = stocks.get("INTC");
Stock airbus = stocks.get("AIR.PA");
```

## Alternatives for historical quotes
If the historical quotes are not yet available, the getHistory() method will automatically send a new request to Yahoo Finance.
```java
Stock google = YahooFinance.get("GOOG");
List<HistoricalQuote> googleHistQuotes = google.getHistory();
```
Or you could explicitly define the from, to and Interval parameters to force a new request.
Check the javadoc for more variations on the getHistory method
```java
Calendar from = Calendar.getInstance();
Calendar to = Calendar.getInstance();
from.add(Calendar.YEAR, -1); // from 1 year ago

Stock google = YahooFinance.get("GOOG");
List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to, Interval.DAILY);
// googleHistQuotes is the same as google.getHistory() at this point
// provide some parameters to the getHistory method to send a new request to Yahoo Finance
```
