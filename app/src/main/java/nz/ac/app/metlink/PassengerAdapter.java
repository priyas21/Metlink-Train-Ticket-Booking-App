package nz.ac.app.metlink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 21600481 on 20-10-2016.
 */
public class PassengerAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public PassengerAdapter(Context context, int resource) {
        super(context, resource);
    }



    public void add(PassengerTicketDetails object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row=convertView;
        PassengerHolder passengerHolder=new PassengerHolder();
        if(row==null)
        {
           /* LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.row_layout,parent,false);
           passengerHolder =new PassengerHolder();
            passengerHolder.tx_ticketNum=(TextView)row.findViewById(R.id.tx_ticketNum);
            passengerHolder.tx_ticketType=(TextView)row.findViewById(R.id.tx_ticketNum);
            passengerHolder.tx_purchaseDate=(TextView)row.findViewById(R.id.tx_ticketNum);
            passengerHolder.tx_expiryDate=(TextView)row.findViewById(R.id.tx_ticketNum);
            passengerHolder.tx_ticketFare=(TextView)row.findViewById(R.id.tx_ticketNum);
            row.setTag(passengerHolder);*/


        }
        else {
            passengerHolder=(PassengerHolder)row.getTag();
        }
        PassengerTicketDetails passengerTicketDetails=(PassengerTicketDetails)this.getItem(position);
        passengerHolder.tx_ticketNum.setText((passengerTicketDetails.getticketNum()));
        passengerHolder.tx_ticketType.setText((passengerTicketDetails.getTicket_Type()));
        passengerHolder.tx_purchaseDate.setText((passengerTicketDetails.getTicket_Purchase_Date()));
        passengerHolder.tx_expiryDate.setText((passengerTicketDetails.getTicket_Expiry_Date()));
        passengerHolder.tx_ticketFare.setText((passengerTicketDetails.getTicket_Fare()));

        return row;
    }
   public static class PassengerHolder
    {
        TextView tx_ticketNum,tx_ticketType,tx_purchaseDate,tx_expiryDate,tx_ticketFare;
    }
}
