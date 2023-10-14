package glavni.app.quotes;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class QuoteStorage {
    private static QuoteStorage instance = null;
    private CopyOnWriteArrayList<Quote> quotes = new CopyOnWriteArrayList<>();
    private Quote selQuote = null;
    private Date dateOfQuote;

    private QuoteStorage(){}

    public static QuoteStorage getInstance(){
        if (instance == null)
            instance = new QuoteStorage();
        return instance;
    }

    public CopyOnWriteArrayList<Quote> getQuotes() {
        return quotes;
    }

    public Quote getSelQuote() {
        return selQuote;
    }

    public void setSelQuote(Quote selQuote) {
        this.selQuote = selQuote;
        setDateOfQuote( new Date());
    }

    public Date getDateOfQuote() {
        return dateOfQuote;
    }

    public void setDateOfQuote(Date dateOfQuote) {
        this.dateOfQuote = dateOfQuote;
    }
}
