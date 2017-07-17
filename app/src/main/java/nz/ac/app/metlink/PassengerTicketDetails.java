package nz.ac.app.metlink;

/**
 * Created by 21600481 on 20-10-2016.
 */
public class PassengerTicketDetails {
    private String  TicketNum;
    private String Ticket_Type;
    private String Ticket_Purchase_Date;
    private String Ticket_Expiry_Date;
    private String Ticket_Fare;

    public PassengerTicketDetails(){

    }

    public PassengerTicketDetails(String TicketNum, String Ticket_Type, String Ticket_Purchase_Date, String Ticket_Expiry_Date,String Ticket_Fare )
    {

        this.TicketNum=TicketNum;
        this.Ticket_Type=Ticket_Type;
        this.Ticket_Purchase_Date=Ticket_Purchase_Date;
        this.Ticket_Expiry_Date=Ticket_Expiry_Date;
        this.Ticket_Fare=Ticket_Fare;
    }



    public String getticketNum()

    {
        return this.TicketNum;
    }
    public void setticketNum(String TicketNum)
    {
        this.TicketNum=TicketNum;
    }

    public String getTicket_Type()

    {
        return this.Ticket_Type;
    }
    public void setTicket_Type(String Ticket_Type)
    {
        this.Ticket_Type=Ticket_Type;
    }

    public String getTicket_Purchase_Date()

    {
        return this.Ticket_Purchase_Date;
    }
    public void setTicket_Purchase_Date(String Ticket_Purchase_Date)
    {
        this.Ticket_Purchase_Date=Ticket_Purchase_Date;
    }

    public String getTicket_Expiry_Date()

    {
        return this.Ticket_Expiry_Date;
    }
    public void setTicket_Expiry_Date(String Ticket_Expiry_Date)
    {
        this.Ticket_Expiry_Date=Ticket_Expiry_Date;
    }

    public String getTicket_Fare()

    {
        return this.Ticket_Fare;
    }
    public void setTicket_Fare(String Ticket_Fare)
    {
        this.Ticket_Fare=Ticket_Fare;
    }


}
