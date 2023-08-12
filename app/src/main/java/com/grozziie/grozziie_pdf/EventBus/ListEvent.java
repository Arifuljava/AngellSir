package com.grozziie.grozziie_pdf.EventBus;;


import java.util.List;

public class ListEvent {
    private List listM;
    public ListEvent(List list){
        listM=list;
    }
    public List GetList(){
        return  listM;
    }
}
