package com.erp.sheelafoam.model;

import com.erp.sheelafoam.BE.InboxBE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/28/2016.
 */

public class InboxModel {
    ArrayList<InboxBE> inbox;

    public ArrayList<InboxBE> getInbox() {
        return inbox;
    }

    public void setInbox(ArrayList<InboxBE> inbox) {
        this.inbox = inbox;
    }
}
