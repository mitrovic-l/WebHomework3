package glavni.app.controller;

import glavni.app.quotes.Quote;
import glavni.app.quotes.QuoteGetter;
import glavni.app.quotes.QuoteStorage;
import glavni.http.Request;
import glavni.http.response.HTMLResponse;
import glavni.http.response.RedirectResponse;
import glavni.http.response.Response;

import java.util.Date;
import java.util.Iterator;

public class QuotesController extends Controller{

    public QuotesController(Request request) {
        super(request);
    }

    @Override
    public Response doGET() {
        Quote selectedQuote = null;
        Date currentDate = new Date();

        if (QuoteStorage.getInstance().getSelQuote() == null) {
            selectedQuote = (new QuoteGetter()).getQuoteFromServerHelper();
            QuoteStorage.getInstance().setSelQuote(selectedQuote);
        }
        else {
            long durationOfSelectedQuote = currentDate.getTime() - QuoteStorage.getInstance().getDateOfQuote().getTime();
            long daysElapsed = (durationOfSelectedQuote/ (1000 * 60 * 60 * 24)) % 365;
            if (daysElapsed >=1){
                selectedQuote = (new QuoteGetter()).getQuoteFromServerHelper();
                QuoteStorage.getInstance().setSelQuote(selectedQuote);
            } else
                selectedQuote = QuoteStorage.getInstance().getSelQuote();
        }
        StringBuilder html = new StringBuilder("<br> <br>" +
                "<div id=\"formDiv\" style=\"margin-left: 25%;margin-right: 25%\">  " +
                "<h2> Add a new quote </h2> " +
                "<form action=\"/save-quote\" method=\"POST\" >" +
                "<label style=\"display:flex;flex-direction:column;\">Author </label> <input style=\"display:flex;flex-direction:column;\" name=\"author\" type=\"text\" placeholder=\"Author's first and last name\"> <br> " +
                "<label style=\"display:flex;flex-direction:column;\">Quote </label> <input style=\"display:flex;flex-direction:column;\" name=\"text\" type=\"text\" placeholder=\"Author's quote\"> <br> " +
                "<button style=\"background:#0a95f2;color:white;\" > Save Quote </button> <br> <br> " +
                "</form> <br>" +
                "<br>"+
                "<h3> <u> Quote of the day </u> </h3>\n"+
                "<p><b>" + selectedQuote.getAuthor() + "</b>: <i> \"" + selectedQuote.getText() + "\"</i> </p> <br> <br>" +
                "<h1> Saved quotes </h1>");
        Iterator iterator = QuoteStorage.getInstance().getQuotes().iterator();
        html.append("<table>" +
                "<tr>" +
                "<th></th>" +
                "</tr>");
        while (iterator.hasNext()){
            Quote quote = (Quote) iterator.next();
            System.out.println("[QUOTE] Autor: " +quote.getAuthor()+ "Text: " + quote.getText());
            html.append("<tr>" +
                    "<td style=\"border: 1px solid black;\">" + quote.getAuthor() + ": " +
                    "<i> \"" + quote.getText() + "\" </i> </td>" +
                    "</tr>");
        }
        html.append("</table> \n</div>");

        return new HTMLResponse(html.toString());
    }

    @Override
    public Response doPOST() {
        QuoteStorage.getInstance().getQuotes().add( new Quote( request.getPostParameters().get("author"), request.getPostParameters().get("text")));
        return new RedirectResponse();
    }
}
